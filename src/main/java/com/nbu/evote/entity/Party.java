package com.nbu.evote.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private String number;

    private String slogan;

    @OneToMany
    private List<PartyMember> partyMembersList;

    private String imageUrl;

    private String groupImageUrl;

    @OneToMany
    Set<Ballot> ballots = new HashSet<>();

    public Party() {
    }

    public Party(long partyId) {
        this.id = partyId;
        this.name = "Default name";
        this.number = "44";
        this.slogan = "Vote for us!";
        this.imageUrl = "https://atlantaplanningguys.com/wp-content/uploads/2009/10/question-mark-image-200x300.jpg";
        this.groupImageUrl = "https://atlantaplanningguys.com/wp-content/uploads/2009/10/question-mark-image-200x300.jpg";
        this.ballots = new HashSet<>();
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

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<Ballot> getBallots() {
        return ballots;
    }

    public void setBallots(Set<Ballot> ballots) {
        this.ballots = ballots;
    }

    public void addBallot(Ballot ballot) {
        this.ballots.add(ballot);
    }

    public Integer getBallotsCount() {
        return this.ballots.size();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getGroupImageUrl() {
        return groupImageUrl;
    }

    public void setGroupImageUrl(String groupImageUrl) {
        this.groupImageUrl = groupImageUrl;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Party{" + "id=" + id + ", name=" + name + ", number=" + number + '}';
    }


}