package com.nbu.i_vote.repository;

import com.nbu.i_vote.entity.Ballot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BallotRepository extends CrudRepository<Ballot, Long> {
    @Query(value = "SELECT * FROM ballot a WHERE party_id = ?1", nativeQuery = true)
    List<Ballot> getBallotsByPartyId(Integer partyId);
}
