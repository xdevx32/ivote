package com.nbu.i_vote.repository;
import com.nbu.i_vote.entity.Party;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PartyRepository extends CrudRepository<Party, Long>  {

    Party findByName(String partyName);
}
