package com.nbu.evote.controller;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.service.*;
import com.nbu.evote.utility.DateContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Controller
public class WebAppController {

    private Party selectedParty;

    private Party clickedParty;

    private String appMode;

    @Autowired
    private PartyService partyService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private PartyMemberService partyMemberService;

    @Autowired
    private BallotService ballotService;

    @Autowired
    private LineChartService lineChartService;

    Citizen currentCitizen;

    @Autowired
    public WebAppController(Environment environment) {
        appMode = environment.getProperty("app-mode");
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    public String index(Model model, @ModelAttribute Party party) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        List<Party> parties = new ArrayList<>();
        parties = partyService.getAllParties();

        parties.sort(Comparator.comparing(Party::getNumber));
//        model.addAttribute("party", party);
        model.addAttribute("parties", parties);
        clickedParty = party;

        return "../static/index-bs";
    }


    @RequestMapping(value = "/party-info-page/{id}/", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    public String setClickedParty(Model model, @ModelAttribute Party party, @PathVariable("id") Integer id) {
//        party = partyService.getParty(party.getId());
        party=partyService.getParty(id);
        clickedParty = party;
        model.addAttribute("party", party);
        return "../static/party-info-page";
    }

    @RequestMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        Integer totalBallotsCount = (int) ballotService.getBallots().stream()
                .count();

        Integer emptyBallotsCount = (int) ballotService.getBallots().stream()
                .filter(ballot -> ballot.getParty().getNumber().equals(String.valueOf(0)))
                .count();


        model.addAttribute("totalBallotsCount", totalBallotsCount);
        model.addAttribute("emptyBallotsCount", emptyBallotsCount);
        return "../static/admin";
    }


    @RequestMapping(value = "/vote", method = RequestMethod.GET)
    public String vote(Model model, Citizen citizen) {

        citizen = new Citizen();

        return "../static/voting-page";
    }


    @RequestMapping(value = "vote-validated", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST})
    public String detailRefresh(Model model, Citizen citizen) {

        ArrayList<Party> parties = new ArrayList<>();
        parties = partyService.getAllParties();
        Citizen real_citizen = new Citizen();
        if (citizen != null) {
            real_citizen = this.citizenService.getCitizenByUniqueVoteIdAndEGN(citizen.getUniqueVoteId(), citizen.getEGN());
        }
        if (real_citizen.getHasVoted()) {
            return "../static/already-voted";
        }

        Party party = new Party();
        model.addAttribute("parties", parties);
        model.addAttribute("party", party);
        model.addAttribute("citizen", real_citizen);
        currentCitizen = real_citizen;


        return "../static/voting-page";
    }


    @RequestMapping(method = RequestMethod.POST, value = "vote-success")
    public String voteSuccess(Model model, @ModelAttribute Party party) {
        party = partyService.getParty(party.getId());
        selectedParty = party;
        model.addAttribute("party", party);

        Ballot ballot = new Ballot();

        ballot.setParty(party);
        ballot.setCitizen(currentCitizen);
        currentCitizen.setHasVoted(true);
        ballot.setDate(LocalDate.now().plusDays(1));
        ballot.setTime(LocalTime.now().plusHours(2));
        ballotService.addBallot(ballot);

        assert currentCitizen != null;
        currentCitizen.setBallot(ballot);
        party.addBallot(ballot);
        citizenService.updateCitizen(currentCitizen);
        return "../static/vote-success-animation";
    }

    @RequestMapping(method = RequestMethod.GET, value = "vote-successs")
    public String voteSuccesss(Model model, @ModelAttribute Party party) {

        model.addAttribute("party", selectedParty);

        return "../static/vote-success";
    }

    @RequestMapping("/uploadParties")
    public String uploadParties(Model model) {
        return "../static/normal-table-parties";
    }

    @RequestMapping("/uploadCitizens")
    public String uploadCitizens(Model model) {
        return "../static/normal-table-citizens";
    }


    @RequestMapping("/barchart")
    public String barChart(Model model, final DateContainer dateContainer) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("mode", appMode);

        //LocalDate dateToCheckFor = model.getAttribute("datePickerValue");


        List<Ballot> ballotsList = new ArrayList<>();
        try {
            ballotsList = ballotService.getBallots();
        } catch (Exception e) {
            System.out.println("Error with Ballots in the database. Non existing ones. Fix database!");
            e.printStackTrace();
        }

        int passedYear = 0;

        if (dateContainer.getYear() == null) {
            passedYear = LocalDate.now().getYear();
        } else {
            passedYear = Integer.parseInt(dateContainer.getYear());
        }

        ArrayList<Party> partiesList = partyService.getAllParties();
        List<String> partyNamesList = partiesList.stream()
                .sorted(Comparator.comparing(Party::getBallotsCount, Comparator.reverseOrder()))
                .map(Party::getName)
                .collect(toList());

        int finalPassedYear1 = passedYear;
        List<Integer> partyBallotsCountList = partiesList.stream()
                .map(p -> p.getBallotsCountForSpecificYear(finalPassedYear1))
                .sorted(Comparator.reverseOrder())
                .collect(toList());
        // Getting the years of all ballots

        HashMap<String,List<String>> result = lineChartService.GenerateLineChart(passedYear, ballotsList);
        List<String> voteTimeListPreviousYearStringsSorted = result.get("voteTimeListPreviousYearStringsSorted");
        List<String> voteTimeListCurrentYearStringsSorted = result.get("voteTimeListCurrentYearStringsSorted");
        

        HashMap<Integer, String> pieChartData = new HashMap<>();
        for (int i = 0; i < partyBallotsCountList.size(); i++) {
            pieChartData.put(partyBallotsCountList.get(i), partyNamesList.get(i));
        }

        Integer totalBallotsCastedForSection = ballotService.getBallots().size();

        System.out.println("Total Ballots for section: " + totalBallotsCastedForSection);
        String dateOfVoteFromBackend = String.valueOf(passedYear);


        List<String> newPartyShortNames = new ArrayList<>();

        for (String s : partyNamesList) {

            int index = 0;
            String abreviature = "";
            for (int i = 0; i < s.length() - 1; i++) {
                if (s.length() < 5 ) {
                    abreviature = s;
                    continue;
                }
                if ( Character.isUpperCase(s.charAt(i))) {
                    abreviature += s.charAt(i);
                }
            }

            newPartyShortNames.add(abreviature);
            index++;
        }


        model.addAttribute("partiesNamesList", newPartyShortNames);
        model.addAttribute("ballotsCountList", partyBallotsCountList);
        model.addAttribute("ballotsTimelineListFirstYear", voteTimeListPreviousYearStringsSorted); // SWAPPED VALUES BECAUSE TOO LAZY
        model.addAttribute("ballotsTimelineListSecondYear", voteTimeListCurrentYearStringsSorted);// SWAPPED VALUES BECAUSE TOO LAZY
        model.addAttribute("dateOfVoteFromBackend", dateOfVoteFromBackend);
        model.addAttribute("totalBallotsCastedForSection", totalBallotsCastedForSection);
        model.addAttribute("pieChartData", pieChartData);
        return "../static/bar-charts";
    }
}
