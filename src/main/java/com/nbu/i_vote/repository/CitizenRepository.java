package com.nbu.i_vote.repository;

import com.nbu.i_vote.entity.Citizen;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CitizenRepository extends PagingAndSortingRepository<Citizen, Long> {

    @Query(value = "SELECT * FROM citizen a WHERE egn = ?1", nativeQuery = true)
    Citizen getCitizenByEGN(String egn);
}
