package com.oracle.oBootHello.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oracle.oBootHello.domain.Member1;
import com.oracle.oBootHello.repository.MemberRepository;

@Service
public class MemberService {
	
//	MemberRepository memberRepository = new MemoryMemberRepository(); // 전통적
	
	private final MemberRepository memberRepository;
	
	@Autowired
	public MemberService(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	// 회원가입
	public Long memberSave(Member1 member1) {
		System.out.println("MemberService memberSave start...");
		memberRepository.save(member1);
		return member1.getId();
	}
	
	public List<Member1> allMembers() {
		System.out.println("MemberService allMembers start...");
		List<Member1> memList = null;
		memList = memberRepository.findAll();
		System.out.println("memList.size()->" + memList.size());
		return memList;
	}

}
