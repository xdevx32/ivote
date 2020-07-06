package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Party;
import com.nbu.i_vote.entity.PartyMember;
import com.nbu.i_vote.repository.PartyMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<PartyMember> getPartyMembersForParty(Party party) {
        Iterable<PartyMember> partyMembers = partyMemberRepository.findAll();
        List<PartyMember> result = new ArrayList<>();

        partyMembers.forEach(result::add);
        result = result.stream()
                .filter(
                partyMember1 ->  partyMember1.getParty().getId() == party.getId()
        ).collect(Collectors.toList());

        return result;
    }
}
