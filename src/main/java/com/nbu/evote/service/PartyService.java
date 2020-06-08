package com.nbu.evote.service;

import com.nbu.evote.entity.Party;
import com.nbu.evote.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartyService {

    @Autowired
    private PartyRepository partyRepository;

    public ArrayList<Party> getAllParties() {
        ArrayList<Party> partyList = new ArrayList<>();
        partyRepository.findAll().forEach(party -> partyList.add(party));

        return partyList;
    }


    public Party getParty(long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невалиден номер на кандидат: " + id));
        return party;
    }

    public void addParty(Party party) {
        partyRepository.save(party);
    }

    public void updateParty(Party party) {
        partyRepository.save(party);
    }

    public void deleteParty(Long id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Невалидно ID на партия: " + id));
        partyRepository.delete(party);
    }

    public Party getPartyByName(String partyName) {
        Party party = partyRepository.findByName(partyName);
//                .orElseThrow(() -> new IllegalArgumentException("Невалидно име на партия: " + partyName));
        return party;
    }
}


