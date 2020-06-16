package com.nbu.evote.service;

import com.nbu.evote.entity.Party;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public interface PartyNamesShortListService {
@Autowired
    List<String> generate();
}
