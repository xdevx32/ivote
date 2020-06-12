package com.nbu.evote.controller;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.entity.PartyMember;
import com.nbu.evote.service.BallotService;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyMemberService;
import com.nbu.evote.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public WebAppController(Environment environment){
        appMode = environment.getProperty("app-mode");
    }

    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST} )
    public String index(Model model,  @ModelAttribute Party party){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        List<Party> parties = new ArrayList<>();
        parties = partyService.getAllParties();

        parties.sort(Comparator.comparing(Party::getNumber));
//        model.addAttribute("party", party);
        model.addAttribute("parties" , parties);
        clickedParty = party;

        return "../static/index-bs";
    }


    @RequestMapping(value = "/party-info-page", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST} )
    public String setClickedParty(Model model, @ModelAttribute Party party) {
        party = partyService.getParty(party.getId());
        clickedParty = party;
        model.addAttribute("party", party);
        return "../static/party-info-page";
    }

    @RequestMapping("/admin")
    public String admin(Model model){
        model.addAttribute("datetime", new Date());
        model.addAttribute("username", "Acho");
        model.addAttribute("mode", appMode);

        return "../static/admin";
    }

    @RequestMapping(value = "/vote", method= RequestMethod.GET)
    public String vote(Model model, Citizen citizen){

        citizen = new Citizen();

        return "../static/voting-page";
    }


    @RequestMapping(value = "vote-validated", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.POST} )
    public String detailRefresh(Model model, Citizen citizen) {

        ArrayList<Party> parties = new ArrayList<>();
        parties = partyService.getAllParties();
        Citizen real_citizen = new Citizen();
        if(citizen != null) {
            real_citizen = this.citizenService.getCitizenByUniqueVoteIdAndEGN(citizen.getUniqueVoteId(), citizen.getEGN());
        }

        Party party = new Party();
        model.addAttribute("parties" , parties);
        model.addAttribute("party", party);
        model.addAttribute("citizen", real_citizen);
        currentCitizen = real_citizen;

        return "../static/voting-page";
    }


    @RequestMapping(method = RequestMethod.POST, value = "vote-success")
    public String voteSuccess(Model model, @ModelAttribute Party party){
        party = partyService.getParty(party.getId());
        selectedParty = party;
        model.addAttribute("party", party);

        Ballot ballot = new Ballot();

        ballot.setParty(party);
        ballot.setCitizen(currentCitizen);
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
    public String voteSuccesss(Model model, @ModelAttribute Party party){

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
    public String barChart(Model model) {
        model.addAttribute("datetime", new Date());
        model.addAttribute("mode", appMode);
        ArrayList<Party> partiesList = partyService.getAllParties();
        List<String> partyNamesList = partiesList.stream()
                .sorted(Comparator.comparing(Party::getBallotsCount, Comparator.reverseOrder()))
                .map(Party::getName)
                .collect(toList());

        List<Integer> partyBallotsCountList = partiesList.stream()
                .map(Party::getBallotsCount)
                .sorted(Comparator.reverseOrder())
                .collect(toList());

        List<Ballot> ballotsList = new ArrayList<>();
        try {
            ballotsList = ballotService.getBallots();
        } catch(Exception e) {
            System.out.println("Error with Ballots in the database. Non existing ones. Fix database!");
            e.printStackTrace();
        }


        /**
         *
         *
         *  TODO!!! DISCUSS IF NEEDED - REMOVE FOR """PRODUCTION"""" THINK!!!!
         *
         *  THIS PIECE OF CODE SETS ALL BALLOT DATES TO
         *  TODAY'S DATE. ONLY IN OBJECTS BUT DOES NOT WRITE TO
         *  DATABASE. SO IF YOU WANT TO REMOVE IT
         *  JUST REMOVE THE STREAM OPERATION BELOW.
         *  THAT WAY , WE CAN VISUALISE MORE DATA
         *  IN ADMIN SECTION. THIS CASE IS ABSOLUTELY UNREALISTIC AND
         *  MUST NOT BE SHIPPED!!!
         *
         *
         *
         *  ONLY TESTING AND DEVELOPING!!!!
         *
         */

        List<Ballot> heads = new ArrayList<>();
        List<Ballot> tails = new ArrayList<>();

        heads = ballotsList.stream()
                .filter(i -> i.getId() % 2 == 0)
                .collect(toList());

        heads.stream().forEach(f -> f.setDate(LocalDate.now()));

        tails = ballotsList.stream()
                .filter(i -> i.getId() % 2 != 0)
                .collect(toList());

        tails.stream().forEach(f -> f.setDate(LocalDate.now().minusYears(1)));

        //Concatenating the two newly modified streams.

        ballotsList = Stream
                .concat(heads.stream(), tails.stream())
                .collect(toList());
        /*
        *
        *
        *
        *
         */
        //Hardcoded values of section days
        //TODO!!!!!!! Discuss!!!!
        int currentYear = LocalDate.now().getYear();
        int lastYear = LocalDate.now().minusYears(1).getYear();


        List<LocalTime> voteTimeListFirstDay = ballotsList.stream()
                .filter(b -> b.getDate().getYear() == (currentYear))
                .map(Ballot::getTime)
//                .map(Ballot::getTimeString)
                .collect(toList());

        List<String> voteTimeListFirstDayStrings = voteTimeListFirstDay.stream()
                .map(LocalTime::toString)
                .collect(toList());

        List<LocalTime> voteTimeListSecondDay = ballotsList.stream()
                .filter(b -> b.getDate().getYear() == lastYear)
                .map(Ballot::getTime)
                .collect(toList());


        List<String> voteTimeListSecondDayStrings = voteTimeListSecondDay.stream()
                .map(LocalTime::toString)
                .collect(toList());


        // Assigning values to list of hours
        // Example
        // labels: ["9:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"],
        // Output list should look like:
        // data: [16, 344, 445, 442, 155, 820, 433, 20, 150, 150, 3]

        HashMap<String, Integer> voteCountForCurrentYearInHoursFormatted = new HashMap<>();
        voteCountForCurrentYearInHoursFormatted.put("9",0);
        voteCountForCurrentYearInHoursFormatted.put("10",0);
        voteCountForCurrentYearInHoursFormatted.put("11",0);
        voteCountForCurrentYearInHoursFormatted.put("12",0);
        voteCountForCurrentYearInHoursFormatted.put("13",0);
        voteCountForCurrentYearInHoursFormatted.put("14",0);
        voteCountForCurrentYearInHoursFormatted.put("15",0);
        voteCountForCurrentYearInHoursFormatted.put("16",0);
        voteCountForCurrentYearInHoursFormatted.put("17",0);
        voteCountForCurrentYearInHoursFormatted.put("18",0);
        voteCountForCurrentYearInHoursFormatted.put("19",0);
        voteCountForCurrentYearInHoursFormatted.put("20",0);
        voteCountForCurrentYearInHoursFormatted.put("21",0);

        HashMap<String, Integer> voteCountForPreviousYearInHoursFormatted = new HashMap<>();
        voteCountForPreviousYearInHoursFormatted.put("9",0);
        voteCountForPreviousYearInHoursFormatted.put("10",0);
        voteCountForPreviousYearInHoursFormatted.put("11",0);
        voteCountForPreviousYearInHoursFormatted.put("12",0);
        voteCountForPreviousYearInHoursFormatted.put("13",0);
        voteCountForPreviousYearInHoursFormatted.put("14",0);
        voteCountForPreviousYearInHoursFormatted.put("15",0);
        voteCountForPreviousYearInHoursFormatted.put("16",0);
        voteCountForPreviousYearInHoursFormatted.put("17",0);
        voteCountForPreviousYearInHoursFormatted.put("18",0);
        voteCountForPreviousYearInHoursFormatted.put("19",0);
        voteCountForPreviousYearInHoursFormatted.put("20",0);
        voteCountForPreviousYearInHoursFormatted.put("21",0);

        Integer startHour = 9;


        for ( int index=0 ; index<voteTimeListFirstDayStrings.size() ; index++) {

            if (voteTimeListFirstDay.get(index).getHour() < startHour) {

                Integer finalStartHour = startHour;
                Integer numberOfVotesForExactHour = voteTimeListFirstDay.stream()
                        .filter(p -> p.getHour() == finalStartHour)
                        .map(LocalTime::getHour)
                        .reduce(0, (a, b) -> a + b)/startHour;

                if(startHour < 10){
                    String stringToPut = "0".concat(String.valueOf(startHour));
                    voteCountForCurrentYearInHoursFormatted.put(stringToPut , numberOfVotesForExactHour);

                } else {
                    voteCountForCurrentYearInHoursFormatted.put(String.valueOf(startHour),numberOfVotesForExactHour);
                }
            }

//            if (startHour == 13) {
//                startHour += 2;
//            } else {
                startHour++;
//            }
        }


        HashMap<String, String> map = new HashMap<String, String>();

        voteCountForCurrentYearInHoursFormatted.put("09", voteCountForCurrentYearInHoursFormatted.remove("9"));
        // TreeMap to store values of HashMap
        TreeMap<String, Integer> sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(voteCountForCurrentYearInHoursFormatted);
        Collection<Integer> values = sorted.values();

        List<String> voteTimeListCurrentYearStringsSorted = new ArrayList<>();
        ArrayList<Integer> listOfValues = new ArrayList<Integer>(values);
        for (Integer value: listOfValues) {
            voteTimeListCurrentYearStringsSorted.add(String.valueOf(value));
        }



        // Previous year
        startHour = 9;

        for ( int index=0 ; index<voteTimeListSecondDayStrings.size() ; index++) {

            if (voteTimeListSecondDay.get(index).getHour() > startHour) {

                Integer finalStartHour = startHour;
                Integer numberOfVotesForExactHour = voteTimeListSecondDay.stream()
                        .filter(p -> p.getHour() == finalStartHour)
                        .map(LocalTime::getHour)
                        .reduce(0, (a, b) -> a + b)/startHour;

                if(startHour < 10){
                    String stringToPut = "0".concat(String.valueOf(startHour));
                    voteCountForPreviousYearInHoursFormatted.put(stringToPut , numberOfVotesForExactHour);

                } else {
                    voteCountForPreviousYearInHoursFormatted.put(String.valueOf(startHour),numberOfVotesForExactHour);
                }
            }

//            if (startHour == 13) {
//                startHour += 2;
//            } else {
                startHour++;
//            }
        }


        map = new HashMap<String, String>();

        voteCountForPreviousYearInHoursFormatted.put("09", voteCountForPreviousYearInHoursFormatted.remove("9"));
        // TreeMap to store values of HashMap
        sorted = new TreeMap<>();
        // Copy all data from hashMap into TreeMap
        sorted.putAll(voteCountForPreviousYearInHoursFormatted);
        values = sorted.values();

        List<String> voteTimeListPreviousYearStringsSorted = new ArrayList<>();
        listOfValues = new ArrayList<Integer>(values);
        for (Integer value: listOfValues) {
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
        partyNamesList.replaceAll(s ->
                s.length() > 4 ? s.substring(0, 4) : s
        );

        System.out.println(partyNamesList);






        // Testing the data
        System.out.println(partyBallotsCountList);
        System.out.println(voteTimeListCurrentYearStringsSorted);
        System.out.println(voteTimeListPreviousYearStringsSorted);
        System.out.println(dateOfVoteFromBackend);
        System.out.println(totalBallotsCastedForSection);
        System.out.println(pieChartData);


        model.addAttribute("partiesNamesList", partyNamesList);
        model.addAttribute("ballotsCountList", partyBallotsCountList);
        model.addAttribute("ballotsTimelineListFirstYear", voteTimeListCurrentYearStringsSorted);
        model.addAttribute("ballotsTimelineListSecondYear", voteTimeListPreviousYearStringsSorted);
        model.addAttribute("dateOfVoteFromBackend", dateOfVoteFromBackend);
        model.addAttribute("totalBallotsCastedForSection",totalBallotsCastedForSection);
        model.addAttribute("pieChartData", pieChartData);
        return "../static/bar-charts";
    }







    // Security

//
//    @GetMapping("/admin")
//    public String getAdminPage() {
//        return "../static/admin";
//    }

}