package com.nbu.evote.controller;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.service.BallotService;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyMemberService;
import com.nbu.evote.service.PartyService;
import com.nbu.evote.utility.DateContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

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

        if (dateContainer.getDateTime() == null) {
            passedYear = LocalDate.now().getYear();
        } else {
            passedYear = dateContainer.getDateTime().getYear();
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
        int finalPassedYear = passedYear;
        List<Ballot> ballotsForSpecificYear = ballotsList.stream()
                .filter(ballot -> ballot.getDate().getYear() == finalPassedYear)
                .collect(toList());

//
//        /**
//         *
//         *
//         *  TODO!!! DISCUSS IF NEEDED - REMOVE FOR """PRODUCTION"""" THINK!!!!
//         *
//         *  THIS PIECE OF CODE SETS ALL BALLOT DATES TO
//         *  TODAY'S DATE. ONLY IN OBJECTS BUT DOES NOT WRITE TO
//         *  DATABASE. SO IF YOU WANT TO REMOVE IT
//         *  JUST REMOVE THE STREAM OPERATION BELOW.
//         *  THAT WAY , WE CAN VISUALISE MORE DATA
//         *  IN ADMIN SECTION. THIS CASE IS ABSOLUTELY UNREALISTIC AND
//         *  MUST NOT BE SHIPPED!!!
//         *
//         *
//         *
//         *  ONLY TESTING AND DEVELOPING!!!!
//         *
//         */
//
//        List<Ballot> heads = new ArrayList<>();
//        List<Ballot> tails = new ArrayList<>();
//
//        heads = ballotsList.stream()
//                .filter(i -> i.getId() % 2 == 0)
//                .collect(toList());
//
//        heads.stream().forEach(f -> f.setDate(LocalDate.now()));
//
//        tails = ballotsList.stream()
//                .filter(i -> i.getId() % 2 != 0)
//                .collect(toList());
//
//        tails.stream().forEach(f -> f.setDate(LocalDate.now().minusYears(1)));
//
//        //Concatenating the two newly modified streams.
//
//        ballotsList = Stream
//                .concat(heads.stream(), tails.stream())
//                .collect(toList());
        /*
         *
         *
         *
         *
         */

        //Hardcoded values of section days
        //TODO!!!!!!! Discuss!!!!
        int currentYear = passedYear;

        int lastYear = passedYear - 1;


        List<LocalTime> voteTimeListFirstDay = ballotsList.stream()
                .filter(b -> b.getDate().getYear() == (currentYear))
                .map(Ballot::getTime)
//                .map(Ballot::getTimeString)
                .collect(toList());

        List<String> voteTimeListFirstDayStrings = voteTimeListFirstDay.stream()
                .map(LocalTime::toString)
                .map(str -> str.substring(0, 2))
                .collect(toList());

        List<LocalTime> voteTimeListSecondDay = ballotsList.stream()
                .filter(b -> b.getDate().getYear() == lastYear)
                .map(Ballot::getTime)
                .collect(toList());

        List<String> voteTimeListSecondDayStrings = voteTimeListSecondDay.stream()
                .map(LocalTime::toString)
                .map(str -> str.substring(0, 2))
                .collect(toList());


        // Assigning values to list of hours
        // Example
        // labels: ["9:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"],
        // Output list should look like:
        // data: [16, 344, 445, 442, 155, 820, 433, 20, 150, 150, 3]

        HashMap<String, Integer> voteCountForCurrentYearInHoursFormatted = new HashMap<>();
        HashMap<String, Integer> voteCountForPreviousYearInHoursFormatted = new HashMap<>();

        for (int i = 1; i <= 24; i++) {
            voteCountForPreviousYearInHoursFormatted.put(String.valueOf(i), 0);
            voteCountForCurrentYearInHoursFormatted.put(String.valueOf(i), 0);
        }

        for (String str : voteTimeListFirstDayStrings) {
            voteCountForPreviousYearInHoursFormatted.merge(str, 1, Integer::sum);
        }

        HashMap<String, String> map = new HashMap<String, String>();

//        voteCountForCurrentYearInHoursFormatted.put("09", voteCountForCurrentYearInHoursFormatted.remove("9"));
        // TreeMap to store values of HashMap
        TreeMap<String, Integer> sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(voteCountForCurrentYearInHoursFormatted);
        Collection<Integer> values = sorted.values();

        List<String> voteTimeListCurrentYearStringsSorted = new ArrayList<>();
        ArrayList<Integer> listOfValues = new ArrayList<Integer>(values);
        for (Integer value : listOfValues) {
            voteTimeListCurrentYearStringsSorted.add(String.valueOf(value));
        }

        for (String str : voteTimeListSecondDayStrings) {
            voteCountForCurrentYearInHoursFormatted.merge(str, 1, Integer::sum);
        }

        map = new HashMap<String, String>();

//        voteCountForPreviousYearInHoursFormatted.put("09", voteCountForPreviousYearInHoursFormatted.remove("9"));
        // TreeMap to store values of HashMap
        sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(voteCountForPreviousYearInHoursFormatted);
        values = sorted.values();

        List<String> voteTimeListPreviousYearStringsSorted = new ArrayList<>();
        listOfValues = new ArrayList<Integer>(values);
        for (Integer value : listOfValues) {
            voteTimeListPreviousYearStringsSorted.add(String.valueOf(value));
        }

        //End second day
        // BUG ABOVE. FIX IT!


//
//        // Adding a Party Member Object
//        PartyMember boykoBorissov = new PartyMember();
//
//        boykoBorissov.setName("Бойко Борисов");
//        boykoBorissov.setDayOfBirth(LocalDate.of(1959,6,13));
////        boykoBorissov.setParty(partyService.getPartyByName("ГЕРБ"));
//        boykoBorissov.setParty(partyService.getParty(38));
//
//        partyMemberService.addPartyMember(boykoBorissov);
//

        HashMap<Integer, String> pieChartData = new HashMap<>();
        for (int i = 0; i < partyBallotsCountList.size(); i++) {
            pieChartData.put(partyBallotsCountList.get(i), partyNamesList.get(i));
        }

        Integer totalBallotsCastedForSection = ballotService.getBallots().size();

        System.out.println("Total Ballots for section: " + totalBallotsCastedForSection);
        String dateOfVoteFromBackend = String.valueOf(currentYear);


        // Създаване на абривиатури за имената, понеже на страницата излизат
        // неправилно и разгъват таблицата прекалено много
//        partyNamesList.replaceAll(s ->
//                for(int i=s.length()-1;i>=0;i--) {
//                    if(Character.isUpperCase(s.charAt(i))) {
//                        return i;
//                }
//        }
//        );

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
//        AtomicReference<AtomicReferenceArray<String>> splitted = new AtomicReference<>(new AtomicReferenceArray<>(new String[0]));
//        //                        partyName.indexOf(" ")
//        for (String partyName : partyNamesList) {
//             splitted.set(new AtomicReferenceArray<>(partyName.split("^/d ")));
//        }

        System.out.println(partyNamesList);

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
