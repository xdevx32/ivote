package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Party;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public interface PartyNamesShortListService {
@Autowired
    List<String> generate();
}
