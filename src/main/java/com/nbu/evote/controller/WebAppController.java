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

    @Autowired
    PartyNamesShortListService partyNamesShortListService;

    @Autowired
    AgesPieChartService agesPieChartService;

    @Autowired
    VoteResultsPieChartService voteResultsPieChartService;

    @Autowired
    PartyBallotCountListService partyBallotCountListService;

    @Autowired
    CityActivityService cityActivityService;

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

        Integer totalCitizensDidNotVote = (int) ballotService.getBallots().stream()
                .filter(ballot -> ballot.getParty().getNumber().equals(String.valueOf(0)))
                .count();


        model.addAttribute("totalBallotsCount", totalBallotsCount);
        model.addAttribute("emptyBallotsCount", emptyBallotsCount);
        model.addAttribute("totalCitizensDidNotVote", totalCitizensDidNotVote);
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

        Integer totalBallotsCastedForSection = ballotService.getBallots().size();

        System.out.println("Total Ballots for section: " + totalBallotsCastedForSection);
        String dateOfVoteFromBackend = String.valueOf(passedYear);

        HashMap<String,List<String>> result = lineChartService.GenerateLineChart(passedYear, ballotsList);
        List<String> voteTimeListPreviousYearStringsSorted = result.get("voteTimeListPreviousYearStringsSorted");
        List<String> voteTimeListCurrentYearStringsSorted = result.get("voteTimeListCurrentYearStringsSorted");

        List<String> newPartyShortNames = partyNamesShortListService.generate();
        HashMap<Integer, String> pieChartData = voteResultsPieChartService.voteResultsPieChartData(passedYear);
        HashMap<Integer, String> agesPieChartData = agesPieChartService.generateAgesPieChart(passedYear);
        List<Integer> partyBallotsCountList = partyBallotCountListService.generate(passedYear);

        List<Integer> keyList = new ArrayList(agesPieChartData.keySet());
        List<String> valueList = new ArrayList(agesPieChartData.values());

        HashMap<Integer, String> citiesPieChartData = cityActivityService.generate(passedYear);

        List<Integer> citiesCountListKeyList = new ArrayList(citiesPieChartData.keySet());
        List<String> citiesNamesListvalueList = new ArrayList(citiesPieChartData.values());

        model.addAttribute("partiesNamesList", newPartyShortNames);
        model.addAttribute("ballotsCountList", partyBallotsCountList);
        model.addAttribute("ballotsTimelineListFirstYear", voteTimeListPreviousYearStringsSorted); // SWAPPED VALUES BECAUSE TOO LAZY
        model.addAttribute("ballotsTimelineListSecondYear", voteTimeListCurrentYearStringsSorted);// SWAPPED VALUES BECAUSE TOO LAZY
        model.addAttribute("dateOfVoteFromBackend", dateOfVoteFromBackend);
        model.addAttribute("totalBallotsCastedForSection", totalBallotsCastedForSection);
        model.addAttribute("pieChartData", pieChartData);
        model.addAttribute("citizenAgesListCount", keyList);
        model.addAttribute("citizenAgesList", valueList);
        model.addAttribute("citiesCountList", citiesCountListKeyList);
        model.addAttribute("citiesNamesList", citiesNamesListvalueList);

        return "../static/bar-charts";
    }
}
