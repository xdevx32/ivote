package com.nbu.i_vote.utility;

import com.nbu.i_vote.entity.Citizen;
import com.nbu.i_vote.entity.Party;
import com.nbu.i_vote.service.CitizenService;
import com.nbu.i_vote.service.PartyService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class CSVReaderAndParser {

    @Autowired
    private PartyService partyService;

    @Autowired
    CitizenService citizenService;

    public CSVReaderAndParser(PartyService partyService) {
        this.partyService = partyService;
    }

    public CSVReaderAndParser(CitizenService citizenService) {
        this.citizenService = citizenService;
    }

    private static final String SAMPLE_CSV_FILE_PATH = "src/main/resources/csv/uploaded/";

    public void invokeCitizensUpload(String filename)throws IOException, CsvValidationException {

        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH + filename));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            ArrayList<Citizen> listOfRecords = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                Boolean condition =
                        !nextRecord[0].equals("egn") &&
                        !nextRecord[0].equals("city") &&
                        !nextRecord[0].equals("day_of_birth") &&
                        !nextRecord[0].equals("name") &&
                        !nextRecord[0].equals("unique_vote_id") &&
                        !nextRecord[0].equals("");
                if(condition) {
                    Citizen citizen = new Citizen();
                    // Order:     name	egn	day_of_birth   city	 unique_vote_id	has_voted
                    citizen.setName(nextRecord[0]);
                    citizen.setEGN(nextRecord[1]);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate dateOfBirth =LocalDate.parse(nextRecord[2], formatter);
                    citizen.setDayOfBirth(dateOfBirth);
                    citizen.setCity(nextRecord[3]);
                    citizen.setUniqueVoteId(nextRecord[4]);
                    citizen.setHasVoted(Boolean.valueOf(nextRecord[5]));
                    listOfRecords.add(citizen);
                }
            }
            saveCitizensRecords(listOfRecords);
        }
    }

    public void invokePartiesUpload(String filename)throws IOException, CsvValidationException {

        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH + filename));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            ArrayList<Party> listOfRecords = new ArrayList<>();

            while ((nextRecord = csvReader.readNext()) != null) {
                Boolean condition =
                        !nextRecord[0].equals("number") &&
                        !nextRecord[0].equals("group_image_url") &&
                        !nextRecord[0].equals("image_url") &&
                        !nextRecord[0].equals("name") &&
                        !nextRecord[0].equals("slogan") &&
                        !nextRecord[0].equals("");
                if(condition) {
                    Party party = new Party();
                    party.setNumber(nextRecord[0]);
                    party.setGroupImageUrl(nextRecord[1]);
                    party.setImageUrl(nextRecord[2]);
                    party.setName(nextRecord[3]);
                    party.setSlogan(nextRecord[4]);
                    listOfRecords.add(party);
                }
            }
            savePartyRecords(listOfRecords);
        }
    }

    private void savePartyRecords(ArrayList<Party> listOfRecords) {
        if(listOfRecords.get(0) instanceof Party) {
            listOfRecords.stream().forEach(x -> {
                partyService.addParty(x);
            });
        }
    }

    private void saveCitizensRecords(ArrayList<Citizen> listOfRecords) {
        if(listOfRecords.get(0) instanceof Citizen) {
            listOfRecords.stream().forEach(x -> {
                citizenService.addCitizen(x);
            });
        }
    }

    //TODO: Fix duplication of code.
}

