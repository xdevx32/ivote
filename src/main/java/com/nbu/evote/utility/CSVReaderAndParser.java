package com.nbu.evote.utility;

import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import com.nbu.evote.service.CitizenService;
import com.nbu.evote.service.PartyService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CSVReaderAndParser {

    private static final Throwable _E = null;
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

    public String invokeCitizensUpload(String filename) throws Exception {

        try (
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH + filename));
                CSVReader csvReader = new CSVReader(reader);
        ) {
            // Reading Records One by One in a String array
            String[] nextRecord;
            ArrayList<Citizen> listOfRecords = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                Boolean condition =
                        !nextRecord[0].equals("name") &&
                        !nextRecord[0].equals("egn") &&
                        !nextRecord[0].equals("unique_vote_id") &&
                        !nextRecord[0].equals("city") &&
                        !nextRecord[0].equals("day_of_birth") &&
                        !nextRecord[0].equals("0");
                if(condition) {
                    Citizen citizen = new Citizen();

                    try {
                        citizen.setName(nextRecord[0]);

                    } catch (Exception e) {
                        throw new Exception("ERROR WITH NAME : " + nextRecord[0], _E);
                    }

                    try {
                        citizen.setEGN(nextRecord[1]);

                    } catch (Exception e) {
                        throw new Exception("ERROR WITH EGN : " + nextRecord[1], _E);
                    }


                    try {
                        citizen.setUniqueVoteId(nextRecord[2]);
                    } catch (Exception e) {
                        throw new Exception("ERROR WITH UNIQUEVOTEID : " + nextRecord[2], _E);

                    }

                    try {
                        citizen.setCity(nextRecord[3]);

                    } catch (Exception e) {
                        throw new Exception("ERROR WITH CITY : " + nextRecord[3], _E);

                    }

                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
                        LocalDate dateOfBirth =LocalDate.parse(nextRecord[4], formatter);
                        citizen.setDayOfBirth(dateOfBirth);
                    } catch (Exception e) {
                        throw new Exception("ERROR WITH DOB : " + nextRecord[4], _E);


                    }


                    try {
                        citizen.setHasVoted(Boolean.valueOf(nextRecord[5]));

                    } catch (Exception e) {
                        throw new Exception("ERROR WITH HASVOTED : " + nextRecord[5], _E);


                    }

                    listOfRecords.add(citizen);
                }
            }
            saveCitizensRecords(listOfRecords);
        }
        return filename;
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
                    party.setGroupImageUrl(nextRecord[0]);
                    party.setImageUrl(nextRecord[1]);
                    party.setName(nextRecord[2]);
                    party.setNumber(nextRecord[3]);
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

