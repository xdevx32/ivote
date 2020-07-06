package com.nbu.i_vote.utility;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class DateContainer {

    private String name;

    private String year;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}