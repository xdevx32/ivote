package com.nbu.evote.service;

import com.nbu.evote.entity.Ballot;

import java.util.HashMap;
import java.util.List;

public interface LineChartService {
    public HashMap<String, List<String>> GenerateLineChart(int passedYear, List<Ballot> ballotsList);
}