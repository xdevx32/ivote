package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Ballot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class LineChartServiceImplTest {

    private LineChartServiceImpl underTest;
    private int passedYear;
    private Ballot ballot = mock(Ballot.class);
    private List<Ballot> ballotList= new ArrayList<>();
    @BeforeEach
    void setUp() {
        underTest = new LineChartServiceImpl();
    }

    @Test
    public void shouldRunMethod() {
        underTest.GenerateLineChart(passedYear, ballotList);
        assertEquals(passedYear != 0, !ballotList.isEmpty());
    }
}