package com.nbu.evote.repository;

import com.nbu.evote.entity.Ballot;
import org.springframework.data.repository.CrudRepository;

public interface BallotRepository extends CrudRepository<Ballot, Long> {

}
