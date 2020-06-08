package com.nbu.evote.service;

import com.nbu.evote.entity.Citizen;
import com.nbu.evote.repository.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CitizenService {

    @Autowired
    private CitizenRepository citizenRepository;

    public List<Citizen> getCitizens() {
        List<Citizen> citizenList = new ArrayList<>();
        citizenRepository.findAll().forEach(citizen -> citizenList.add(citizen));

        return citizenList;
    }

    public Citizen getCitizen(long id) {
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid citizen Id:" + id));
        return citizen;
    }

    public void addCitizen(Citizen citizen) {
        citizenRepository.save(citizen);
    }

    public void updateCitizen(Citizen citizen) {
        citizenRepository.save(citizen);
    }

    public void deleteCitizen(long id) {
        Citizen citizen = citizenRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid citizen Id:" + id));
        citizenRepository.delete(citizen);
    }

    public Citizen getCitizenByUniqueVoteIdAndEGN(String uniqueVoteId, String egn) {
        return citizenRepository.findByEGNAndUniqueVoteId(uniqueVoteId, egn);
    }
}
