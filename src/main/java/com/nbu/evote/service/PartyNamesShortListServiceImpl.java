package com.nbu.evote.service;

import com.nbu.evote.entity.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PartyNamesShortListServiceImpl implements PartyNamesShortListService {

    @Autowired
    PartyService partyService;

    @Override
    public List<String> generate() {

        ArrayList<Party> partiesList = partyService.getAllParties();
        List<String> partyNamesList = partiesList.stream()
                .sorted(Comparator.comparing(Party::getBallotsCount, Comparator.reverseOrder()))
                .map(Party::getName)
                .collect(toList());
        List<String> newPartyShortNames = new ArrayList<>();

        for (String s : partyNamesList) {

            int index = 0;
            String abreviature = "";
            for (int i = 0; i < s.length() - 1; i++) {
                if (s.length() < 5 ) {
                    abreviature = s;
                    continue;
                }
                if ( Character.isUpperCase(s.charAt(i))) {
                    abreviature += s.charAt(i);
                }
            }

            newPartyShortNames.add(abreviature);
            index++;
        }

        return (ArrayList<String>)newPartyShortNames;
    }
}
