package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Citizen;
import com.nbu.i_vote.entity.Party;
import com.nbu.i_vote.repository.CitizenRepository;
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

    public List<Citizen> getAllCitizens() {
        ArrayList<Citizen> citizenList = new ArrayList<>();
        citizenRepository.findAll().forEach(citizen -> citizenList.add(citizen));

        return citizenList;
    }

    public Citizen getCitizenByEGN(String egn) {
        return citizenRepository.getCitizenByEGN(egn);
    }
}