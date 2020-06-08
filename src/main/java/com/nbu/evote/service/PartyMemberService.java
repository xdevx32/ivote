package com.nbu.evote.service;

import com.nbu.evote.entity.PartyMember;
import com.nbu.evote.repository.PartyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PartyMemberService {

    @Autowired
    private PartyMemberRepository partyMemberRepository;

    public List<PartyMember> getPartyMembers() {
        List<PartyMember> partyMemberList = new ArrayList<>();
        partyMemberRepository.findAll().forEach(ballot -> partyMemberList.add(ballot));

        return partyMemberList;
    }

    public PartyMember getPartyMember(long id) {
        PartyMember partyMember = partyMemberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Party Member Id:" + id));
        return partyMember;
    }

    public void addPartyMember(PartyMember partyMember) {
        partyMemberRepository.save(partyMember);
    }

    public void updatePartyMember(PartyMember partyMember) {
        partyMemberRepository.save(partyMember);
    }

    public void deletePartyMember(long id) {
        PartyMember partyMember = partyMemberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Party Member Id:" + id));
        partyMemberRepository.delete(partyMember);
    }
}
