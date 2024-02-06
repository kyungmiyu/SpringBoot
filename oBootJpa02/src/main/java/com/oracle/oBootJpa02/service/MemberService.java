package com.oracle.oBootJpa02.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.oBootJpa02.domain.Member;
import com.oracle.oBootJpa02.repository.MemberRepository;

// JPA  --> 서비스 계층에 반드시 트랜잭션 추가 (무결성)
// 트랜잭션이 일어나는 단위는 service 단위, 롤백과 커밋이 동시에 이루어져야함 (원자성)
// 스프링은 해당 클래스의 메서드를 실행할 때 트랜잭션을 시작하고,
// 메서드가 정상 종료되면 트랜잭션을 커밋. 만약 런타임 예외가 발생하면 롤백.
// JPA를 통한 모든 데이터 변경은 트랜잭션 안에서 실행
@Service
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;
	
	// Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	// 회원가입
	public Member join(Member member) {
		System.out.println("MemberService join member.getName()->"+member.getName());
		memberRepository.save(member);
		return member;
	}
	
	// 전체회원 조회
	public List<Member> getListAllMember() {
		List<Member> listMember = memberRepository.findAll();
		System.out.println("MemberService getListAllMember listMember.size()->"+listMember.size());
		return listMember;
	}

	public List<Member> getListSearchMember(String searchName) {
		System.out.println("MemberService getListSearchMember Start...");
		// String pSearchName = searchName + '%';
		System.out.println("MemberService getListSearchMember searchName->"+searchName);
		List<Member> listMember = memberRepository.findByNames(searchName);
		System.out.println("MemberService getListSearchMember memberList.size()->"+listMember.size());
		return listMember;
	}

	public Optional<Member> findByMember(Long id) {
		Optional<Member> member = memberRepository.findByMember(id);
		System.out.println("MemberService findByMember member->"+member);
		return member;
	}

	public void memberUpdate(Member member) {
		System.out.println("MemberService memberUpdate member->"+member);
		memberRepository.updateByMember(member);
		// Lazy loading: 즉시 실행시키기 않고 나중에 필요한 시점에 실행되도록 하는 것
		return; // return 하는 순간 commit
		
	}
	
	
}
