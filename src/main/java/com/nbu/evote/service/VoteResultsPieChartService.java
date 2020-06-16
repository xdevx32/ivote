package com.nbu.evote.service;

import com.nbu.evote.entity.Party;

import java.util.HashMap;
import java.util.List;

public interface VoteResultsPieChartService {
    HashMap<Integer, String> voteResultsPieChartData(Integer passedYear);
}
