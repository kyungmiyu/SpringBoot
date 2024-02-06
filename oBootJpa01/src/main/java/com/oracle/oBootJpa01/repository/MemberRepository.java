package com.oracle.oBootJpa01.repository;

import java.util.List;

import com.oracle.oBootJpa01.domain.Member;

// repository interface로 만들기 db가 다른걸로 바뀔 것을 대비
public interface MemberRepository {
	Member MemberSave(Member member);
	List<Member> findAllMember();
	List<Member> findByNames(String searchName);

}
