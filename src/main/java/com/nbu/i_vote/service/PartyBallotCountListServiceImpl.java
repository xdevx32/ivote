package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PartyBallotCountListServiceImpl implements PartyBallotCountListService {

    @Autowired
    PartyService partyService;

    @Override
    public List<Integer> generate(Integer passedYear) {
        int finalPassedYear1 = passedYear;
        ArrayList<Party> partiesList = partyService.getAllParties();

        return partiesList.stream()
                .map(p -> p.getBallotsCountForSpecificYear(finalPassedYear1))
                .sorted(Comparator.reverseOrder())
                .collect(toList());
    }
}
