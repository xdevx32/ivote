package com.nbu.i_vote.service;

import java.util.List;

public interface PartyBallotCountListService {
    List<Integer> generate(Integer passedYear);
}
