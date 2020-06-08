package com.nbu.evote.entity;

import javax.persistence.*;
import java.time.*;


/***
 *
 * Клас представляващ всяка бюлетина като отделна единица.
 *
 */

@Entity
@Table(name = "ballot")
public class Ballot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //AUTO WORKS!>>>>>
    private long id;

    LocalDate date;

    LocalTime time;

    @OneToOne
    Citizen citizen;

    @ManyToOne
    Party party;

    public Ballot() {
    }

    public Ballot(long ballotId) {
        this.id = ballotId;
        this.date = LocalDate.now().plusDays(1);
        this.time = LocalTime.now().plusHours(2);
        this.citizen = new Citizen();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate localDate) {
        this.date = localDate;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public String getTimeString() {
        return time.toString();
    }
}
