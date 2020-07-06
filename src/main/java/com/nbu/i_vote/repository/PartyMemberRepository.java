package com.nbu.i_vote.repository;

import com.nbu.i_vote.entity.Ballot;
import com.nbu.i_vote.entity.PartyMember;
import org.springframework.data.repository.CrudRepository;

public interface PartyMemberRepository extends CrudRepository<PartyMember, Long> {

}
