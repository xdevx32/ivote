package com.nbu.evote.repository;
import com.nbu.evote.entity.Party;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PartyRepository extends CrudRepository<Party, Long>  {

    Party findByName(String partyName);
}
