package com.nbu.evote.service;

import com.nbu.evote.entity.Citizen;
import com.nbu.evote.entity.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class AgesPieChartServiceImpl implements AgesPieChartService {

    @Autowired
    PartyService partyService;

    @Autowired
    CitizenService citizenService;

    @Override
    public HashMap<Integer, String> generateAgesPieChart(Integer passedYear) {

        List<Citizen> citizenList = citizenService.getAllCitizens().stream()
                .filter(Citizen::getHasVoted) // e.g. citizen has voted is equal to true
                .collect(toList());
        List<Integer> citizensAgeList = new ArrayList<>();
        citizenList.forEach(
                a -> {
                    citizensAgeList.add(passedYear - a.getDayOfBirth().getYear() + 1);
                }
        );

        HashMap<Integer, String> agesPieChartData = new HashMap<>();
        citizensAgeList.forEach(
                a -> agesPieChartData.put(Collections.frequency(citizensAgeList, a), String.valueOf(a) + "г.")
        );
        return agesPieChartData;
    }

    public List<String> generate2(Integer passedYear) {
        List<Citizen> citizenList = citizenService.getAllCitizens().stream()
                .filter(Citizen::getHasVoted) // e.g. citizen has voted is equal to true
                .collect(toList());
        List<String> citizensAgeList = new ArrayList<>();
        citizenList.forEach(
                a -> {
                    citizensAgeList.add(String.valueOf(passedYear - a.getDayOfBirth().getYear() + 1));
                }
        );
        return citizensAgeList.stream()
                .sorted(Comparator.reverseOrder())
                .collect(toList());
    }
}
