package com.nbu.evote.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String EGN;

    private LocalDate dayOfBirth;

    @OneToOne
    @Nullable
    private Ballot ballot;

    private String city;

    private String uniqueVoteId;

    private Boolean hasVoted;


    public Citizen() {
    }

    public Citizen(long citizenId) {
        this.id = citizenId;
        this.name = "Default name";
        this.EGN = "";
        this.uniqueVoteId = "";
        this.dayOfBirth = LocalDate.now();
        this.city = "";
        this.ballot = new Ballot();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Ballot getBallot() {
        return ballot;
    }

    public Boolean getHasVoted() {
        return hasVoted;
    }

    public void setHasVoted(Boolean hasVoted) {
        this.hasVoted = hasVoted;
    }


    public void setBallot(Ballot ballot) {
        this.ballot = ballot;
    }

    public String getEGN() {
        return EGN;
    }

    public void setEGN(String EGN) {
        this.EGN = EGN;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(LocalDate dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
    }


    public String getUniqueVoteId() {
        return uniqueVoteId;
    }

    public void setUniqueVoteId(String uniqueVoteId) {
        this.uniqueVoteId = uniqueVoteId;
    }
}
