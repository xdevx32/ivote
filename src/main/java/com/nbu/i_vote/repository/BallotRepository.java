package com.nbu.i_vote.repository;

import com.nbu.i_vote.entity.Ballot;
import org.springframework.data.repository.CrudRepository;

public interface BallotRepository extends CrudRepository<Ballot, Long> {

}
