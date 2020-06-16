package com.nbu.evote.service.impl;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.service.LineChartService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class LineChartServiceImpl implements LineChartService {

    @Override
    public HashMap<String, List<String>> GenerateLineChart(int passedYear, List<Ballot> ballotsList) {

        List<String> voteTimeListCurrentYearStringsSorted;
        List<String> voteTimeListPreviousYearStringsSorted;
        int currentYear = passedYear;

        int lastYear = passedYear - 1;

        int finalCurrentYear = currentYear;
        List<String> voteTimeListFirstDayStrings = ballotsList.stream()
                    .filter(b -> b.getDate().getYear() == (finalCurrentYear))
                    .map(t -> t.getTime().plusHours(3))
                    .map(LocalTime::toString)
                    .map(str -> str.substring(0, 2))
                    .collect(toList());

        List<String> voteTimeListSecondDayStrings = ballotsList.stream()
                .filter(b -> b.getDate().getYear() == lastYear)
                .map(t -> t.getTime().plusHours(3))
                .map(LocalTime::toString)
                .map(str -> str.substring(0, 2))
                .collect(toList());

        // Assigning values to list of hours
        // Example
        // labels: ["9:00", "10:00", "11:00", "12:00", "13:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"],
        // Output list should look like:
        // data: [16, 344, 445, 442, 155, 820, 433, 20, 150, 150, 3]


        //1 Previous
        HashMap<String, Integer> voteCountForPreviousYearInHoursFormatted = new HashMap<>();


        //2 Current
        HashMap<String, Integer> voteCountForCurrentYearInHoursFormatted = new HashMap<>();


        //1 Previous
        for (int i = 1; i <= 24; i++) {
            voteCountForPreviousYearInHoursFormatted.put(String.valueOf(i), 0);
        }
        //1 Previous
        for (String str : voteTimeListFirstDayStrings) {
            voteCountForPreviousYearInHoursFormatted.merge(str, 1, Integer::sum);
        }

        //2 Current
        for (int i = 1; i <= 24; i++) {
            voteCountForCurrentYearInHoursFormatted.put(String.valueOf(i), 0);
        }
        //2 Current
        for (String str : voteTimeListSecondDayStrings) {
            voteCountForCurrentYearInHoursFormatted.merge(str, 1, Integer::sum);
        }

        //2 Current
        TreeMap<String, Integer> sorted = new TreeMap<>();
        sorted.putAll(voteCountForCurrentYearInHoursFormatted);
        Collection<Integer> values = sorted.values();
        voteTimeListCurrentYearStringsSorted = new ArrayList<>();
        ArrayList<Integer> listOfValues = new ArrayList<Integer>(values);
        for (Integer value : listOfValues) {
            voteTimeListCurrentYearStringsSorted.add(String.valueOf(value));
        }
        for (String str : voteTimeListSecondDayStrings) {
            voteCountForCurrentYearInHoursFormatted.merge(str, 1, Integer::sum);
        }

        //1 Previous
        TreeMap<String, Integer> sorted2 = new TreeMap<>();
        sorted2.putAll(voteCountForPreviousYearInHoursFormatted);
        Collection<Integer> values2 = sorted2.values();
        voteTimeListPreviousYearStringsSorted = new ArrayList<>();
        ArrayList<Integer> listOfValues2 = new ArrayList<Integer>(values2);
        for (Integer value : listOfValues2) {
            voteTimeListPreviousYearStringsSorted.add(String.valueOf(value));
        }
        for (String str : voteTimeListFirstDayStrings) {
            voteCountForPreviousYearInHoursFormatted.merge(str, 1, Integer::sum);
        }

        HashMap<String, List<String>> elementsToReturn = new HashMap<>();
        elementsToReturn.put("voteTimeListCurrentYearStringsSorted",voteTimeListCurrentYearStringsSorted);
        elementsToReturn.put("voteTimeListPreviousYearStringsSorted",voteTimeListPreviousYearStringsSorted);

        return elementsToReturn;
    }
}


