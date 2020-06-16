package com.nbu.evote.service;

import com.nbu.evote.entity.Citizen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class CityActivityServiceImpl implements CityActivityService {

    @Autowired
    CitizenService citizenService;

    @Override
    public HashMap<Integer, String> generate(Integer passedYear) {

        List<Citizen> citizenList = citizenService.getAllCitizens().stream()
                .filter(Citizen::getHasVoted) // e.g. citizen has voted is equal to true
                .collect(toList());
        List<String> citizensCityList = new ArrayList<>();
        citizenList.forEach(
                a -> {
                    citizensCityList.add(a.getCity());
                }
        );

        HashMap<Integer, String> citiesPieChartData = new HashMap<>();
        citizensCityList.forEach(
                a -> citiesPieChartData.put(Collections.frequency(citizensCityList, a), String.valueOf(a))
        );
        return citiesPieChartData;
    }
}
