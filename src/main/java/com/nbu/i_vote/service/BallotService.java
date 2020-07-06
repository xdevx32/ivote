package com.nbu.i_vote.service;

import com.nbu.i_vote.entity.Ballot;
import com.nbu.i_vote.repository.BallotRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BallotService {

    @Autowired
    private BallotRepository ballotRepository;

    public List<Ballot> getBallots() {
        List<Ballot> ballotList = new ArrayList<>();
        try {
            ballotRepository.findAll().forEach(ballot -> ballotList.add(ballot));
        }  catch (Exception e){
            throw new IllegalArgumentException("Проблем с бюлетините");
        }

        return ballotList;
    }

    public Ballot getBallot(long id) {
        Ballot ballot = ballotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ballot Id:" + id));
        return ballot;
    }

    public void addBallot(Ballot ballot) {
        ballotRepository.save(ballot);
    }

    public void updateBallot(Ballot ballot) {
        ballotRepository.save(ballot);
    }

    public void deleteBallot(long id) {
        Ballot ballot = ballotRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ballot Id:" + id));
        ballotRepository.delete(ballot);
    }

}
