package com.nbu.evote.service;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.Citizen;

import java.util.HashMap;
import java.util.List;

public interface AgesPieChartService {
     public HashMap<Integer, String> generateAgesPieChart(Integer passedYear);
}
