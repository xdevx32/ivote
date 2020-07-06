package com.nbu.i_vote.repository;

import com.nbu.i_vote.entity.Citizen;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CitizenRepository extends PagingAndSortingRepository<Citizen, Long> {

    @Query(value = "SELECT * FROM citizen a WHERE unique_vote_id = ?1 AND egn = ?2", nativeQuery = true)
    Citizen findByEGNAndUniqueVoteId(String uniqueVoteId, String EGN);
}
