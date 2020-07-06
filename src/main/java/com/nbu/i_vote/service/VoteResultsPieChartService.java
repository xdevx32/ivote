package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Party;

import java.util.HashMap;
import java.util.List;

public interface VoteResultsPieChartService {
    HashMap<Integer, String> voteResultsPieChartData(Integer passedYear);
}
