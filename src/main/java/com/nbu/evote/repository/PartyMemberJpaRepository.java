package com.nbu.evote.repository;

import com.nbu.evote.entity.PartyMember;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public class PartyMemberJpaRepository implements JpaRepository<PartyMember, String> {


    @Override
    public List<PartyMember> findAll() {
        return null;
    }

    @Override
    public List<PartyMember> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PartyMember> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<PartyMember> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(PartyMember entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends PartyMember> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends PartyMember> S save(S entity) {
        return null;
    }

    @Override
    public <S extends PartyMember> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<PartyMember> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PartyMember> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<PartyMember> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PartyMember getOne(String s) {
        return null;
    }

    @Override
    public <S extends PartyMember> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PartyMember> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PartyMember> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PartyMember> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PartyMember> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PartyMember> boolean exists(Example<S> example) {
        return false;
    }
}
