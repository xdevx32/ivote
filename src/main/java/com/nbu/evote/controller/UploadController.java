package com.nbu.evote.controller;

import com.nbu.evote.service.CitizenService;
import com.nbu.evote.utility.CSVReaderAndParser;
import com.nbu.evote.service.PartyService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    @Autowired
    private PartyService partyService;

    @Autowired
    private CitizenService citizenService;

    private static String UPLOADED_FOLDER = "src/main/resources/csv/uploaded/";

    @PostMapping("/admin/upload/citizens")
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

    @PostMapping("/admin/upload/parties")
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

}