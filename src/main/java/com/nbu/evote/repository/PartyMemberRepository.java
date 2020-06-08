package com.nbu.evote.repository;

import com.nbu.evote.entity.Ballot;
import com.nbu.evote.entity.PartyMember;
import org.springframework.data.repository.CrudRepository;

public interface PartyMemberRepository extends CrudRepository<PartyMember, Long> {

}
