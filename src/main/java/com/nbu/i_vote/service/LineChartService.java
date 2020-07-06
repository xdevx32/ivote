package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Ballot;

import java.util.HashMap;
import java.util.List;

public interface LineChartService {
    public HashMap<String, List<String>> GenerateLineChart(int passedYear, List<Ballot> ballotsList);
}