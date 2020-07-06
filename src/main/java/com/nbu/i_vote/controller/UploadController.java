package com.nbu.i_vote.controller;

import com.nbu.i_vote.entity.Ballot;
import com.nbu.i_vote.entity.Citizen;
import com.nbu.i_vote.entity.Party;
import com.nbu.i_vote.service.BallotService;
import com.nbu.i_vote.service.CitizenService;
import com.nbu.i_vote.utility.CSVReaderAndParser;
import com.nbu.i_vote.service.PartyService;
import com.opencsv.exceptions.CsvValidationException;
import com.sun.codemodel.internal.JCatchBlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class UploadController {

    @Autowired
    private PartyService partyService;

    @Autowired
    private CitizenService citizenService;


    @Autowired
    private BallotService ballotService;

    private static String UPLOADED_FOLDER = "src/main/resources/csv/uploaded/";

    @PostMapping("/uploadCitizens")
    public String citizenFileUploader(@RequestParam("file") MultipartFile file,Model model,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/uploadStatus";
        }

        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
            CSVReaderAndParser csvReaderAndParser = new CSVReaderAndParser(citizenService);
            csvReaderAndParser.invokeCitizensUpload(file.getOriginalFilename());

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            System.out.println("************************* OH snap!" +
                    " It looks like you added something to Citizen " +
                    "or Party entity, but forgot to check the upload method." +
                    "You broke the whole upload logic. Sorry if it is too fragile for you" +
                    "but you're lucky today and I will give you" +
                    "a little hint. You should set the missing property in this file: " +
                    "CSVReaderAndParser.java and find your method: invokeCitizensUpload" +
                    "or invokePartiesUpload, respectively. Add your new property as the " +
                    " other properties are carefully added, add you won't get an exception " +
                    "*************************");
        }

        return "redirect:/uploadStatus";
    }

    @PostMapping("uploaParties")
    public String partyFileUploader(@RequestParam("file") MultipartFile file,Model model,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/uploadStatus";
        }

        try {

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
            CSVReaderAndParser csvReaderAndParser = new CSVReaderAndParser(partyService);
            csvReaderAndParser.invokePartiesUpload(file.getOriginalFilename());

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            System.out.println("************************* OH snap!" +
                    " It looks like you added something to Citizen " +
                    "or Party entity, but forgot to check the upload method." +
                    "You broke the whole upload logic. Sorry if it is too fragile for you" +
                    "but you're lucky today and I will give you" +
                    "a little hint. You should set the missing property in this file: " +
                    "CSVReaderAndParser.java and find your method: invokeCitizensUpload" +
                    "or invokePartiesUpload, respectively. Add your new property as the " +
                    " other properties are carefully added, add you won't get an exception " +
                    "*************************");
        }

        return "redirect:/uploadStatus";
    }

    //TODO: Fix duplicates.
    @GetMapping("/uploadStatus")
    public String uploadStatus(Model model) {

        return "uploadStatus";
    }

    // За кирилицата UTF-8
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }



    @RequestMapping(value = "/addCitizens", method = RequestMethod.GET)
    public String addCitizenes() {
        for (int i = 1 ; i < 1200; i++) {
            Citizen newCitizen = new Citizen(i,"Citizen" + i,"3211" + i,
                    LocalDate.now(),null, "Burgas",String.valueOf(i),false
                    );
            citizenService.addCitizen(newCitizen);
        }
        return "../static/bar-charts";
    }

    @RequestMapping(value = "/addSomeMockData", method = RequestMethod.GET)
    public String addMockData() {
        List<Citizen> allCitizens = citizenService.getAllCitizens();
        for (Citizen citizen : allCitizens) {
            LocalTime z = LocalTime.now();

            if(!citizen.getHasVoted()) {
                Ballot ballot = new Ballot();
                ballot.setCitizen(citizen);
                if (citizen.getId() % 2 == 0) {
                    ballot.setTime(z.plusHours(ThreadLocalRandom.current().nextInt(1, 24)));
                    ballot.setDate(LocalDate.of(2020, 5, 12));
                } else {
                    ballot.setTime(z.plusHours(ThreadLocalRandom.current().nextInt(1, 24)));
                    ballot.setDate(LocalDate.of(2019, 5, 21));
                }

                List<Party> allParties = partyService.getAllParties();
                Party p = allParties.get(ThreadLocalRandom.current().nextInt(1, allParties.size()));
                p.addBallot(ballot);
                citizen.setBallot(ballot);
                ballot.setParty(p);
                ballotService.addBallot(ballot);
                citizen.setBallot(ballot);
                citizen.setHasVoted(true);
                ballotService.addBallot(ballot);
                citizenService.updateCitizen(citizen);
            }
        }
        return "../static/bar-charts";
    }


}