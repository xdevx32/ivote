package com.nbu.evote.service;

import java.util.List;

public interface PartyBallotCountListService {
    List<Integer> generate(Integer passedYear);
}
