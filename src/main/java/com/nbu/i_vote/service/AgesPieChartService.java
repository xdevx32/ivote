package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Ballot;
import com.nbu.i_vote.entity.Citizen;

import java.util.HashMap;
import java.util.List;

public interface AgesPieChartService {
     public HashMap<Integer, String> generateAgesPieChart(Integer passedYear);
}
