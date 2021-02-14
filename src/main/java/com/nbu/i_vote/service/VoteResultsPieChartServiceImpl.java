package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class VoteResultsPieChartServiceImpl implements VoteResultsPieChartService {

    @Autowired
    PartyService partyService;

    @Override
    public HashMap<Integer, String> voteResultsPieChartData(Integer passedYear) {

        ArrayList<Party> partiesList = partyService.getAllParties();
        List<String> partyNamesList = partiesList.stream()
                .sorted(Comparator.comparing(party -> party.getBallotsCountForSpecificYear(passedYear), Comparator.reverseOrder()))
                .map(Party::getName)
                .collect(toList());

        int finalPassedYear1 = passedYear;
        List<Integer> partyBallotsCountList = partiesList.stream()
                .map(p -> p.getBallotsCountForSpecificYear(finalPassedYear1))
                .sorted(Comparator.reverseOrder())
                .collect(toList());
        // Getting the years of all ballots


        HashMap<Integer, String> pieChartData = new HashMap<>();
        for (int i = 0; i < partyBallotsCountList.size(); i++) {
            pieChartData.put(partyBallotsCountList.get(i), partyNamesList.get(i));
        }
        return pieChartData;
    }
}
