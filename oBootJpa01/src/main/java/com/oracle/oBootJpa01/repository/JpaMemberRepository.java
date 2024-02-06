package com.oracle.oBootJpa01.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.oracle.oBootJpa01.domain.Member;

import jakarta.persistence.EntityManager;

@Repository
public class JpaMemberRepository implements MemberRepository {
	// JPA DML --> EntityManager (DML 작업할 때 필수)
	// JPA는 @Entity 관리하는 EntityManager가 관리함
	// JDBC는 datasource가 관리함
	// EntityManager를 DI시킨것
	private final EntityManager em;
	
	public JpaMemberRepository(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public Member MemberSave(Member member) {
		// 저장 method
		em.persist(member);
		System.out.println("JpaMemberRepository memberSave member After...");
		return member;
	}

	@Override
	public List<Member> findAllMember() {
		// from member(db테이블 아님)는 객체의 member(@Entity)로 등록된 객체 jpql 문법
		// Member.class는 return되는 select의 클래스
		// getResultList 메소드로 결과가 list 형태로 변환되어 나옴
		// Member는 db table이 아니라 객체 이름
		List<Member> memberList = em.createQuery("select m from Member m", Member.class)
									.getResultList()
									;
		System.out.println("JpaMemberRepository findAllMember memberList.size()->"+memberList.size());
		return memberList;
	}

	@Override
	public List<Member> findByNames(String searchName) {
		String pname = searchName + '%';
		System.out.println("JpaMemberRepository findByNames pname->"+pname);
		List<Member> memberList = em.createQuery("select m from Member m where name Like :name", Member.class)
									.setParameter("name", pname).getResultList(); // :name(table의 name)에 setParameter로 pname을 넣음
		System.out.println("JpaMemberRepository memberList.size()->"+memberList.size());
		return memberList;
	}

}
