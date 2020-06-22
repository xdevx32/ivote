package com.nbu.evote.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "party_member")
public class PartyMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private LocalDate dayOfBirth;

    private String portraitUrl;

    @ManyToOne
    Party party;

    public PartyMember() {
    }

    public PartyMember(long partyMemberId) {
        this.id = partyMemberId;
        this.name = "";
        this.dayOfBirth = LocalDate.now();
        this.portraitUrl = "http://www.google.com/party.jpg";
        this.party = new Party();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
