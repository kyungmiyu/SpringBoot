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
		em.persist(member); // 저장하면서 sequence > call by reference > 기본키 키 묻어나옴
		return member.getId();
	}

	@Override
	public void updateByMember(Member member) {
		int result = 0;
		Member member3 = em.find(Member.class, member.getId()); // 수정할때 find를 사용 (persist는 insert 됨)
		if (member3 != null) {
			// 회원 저장
			member3.setName(member.getName());
			member3.setSal(member.getSal());
			result = 1;
			System.out.println("JpaMemberRepository updateMember Update...");
		} else {
			result = 0;
			System.out.println("JpaMemberRepository updateMember No Exist...");
		}
		return;
		
	}

	@Override
	public Member findByMember(Long memberId) {
		Member member = em.find(Member.class, memberId);
		return member;
	}

	
	
} // end of class
