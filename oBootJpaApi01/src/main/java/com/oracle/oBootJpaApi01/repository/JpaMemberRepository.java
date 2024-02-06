package com.oracle.oBootJpaApi01.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.oracle.oBootJpaApi01.domain.Member;

import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

	private final EntityManager em;
	// final 반드시 필요한 생성자를 만들때는 RequiredArgsConstructor
	// 모든 생성자를 만들때는 AllArgsConstructor
	
	@Override
	public List<Member> findAll() {
		List<Member> memberList = em.createQuery("select m from Member m", Member.class).getResultList();
		System.out.println("JpaMemberRepository findAll memberList.size()->"+memberList.size());
		return memberList;
	}

	@Override
	public Long save(@Valid Member member) {
		System.out.println("JpaMemberRepository save before...");
		em.persist(member);
		return member.getId();
	}

}
