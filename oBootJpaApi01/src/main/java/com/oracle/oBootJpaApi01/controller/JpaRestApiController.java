package com.oracle.oBootJpaApi01.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.oBootJpaApi01.domain.Member;
import com.oracle.oBootJpaApi01.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Controller + ResponseBody
// 사용목적 --> Ajax + RestApi
@RestController
@RequiredArgsConstructor
@Slf4j
public class JpaRestApiController {
	private final MemberService memberService;
	
	// Version 1 (bad)
	// postman ---> Body --> raw ---> JSON	 
    // 예시 { "name" : "kkk222" }
	@PostMapping("/restApi/v1/memberSave")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		// @RequestBody : Json(member)으로 온것을  --> Member member Setting
		System.out.println("JpaRestApiController /api/v1/memberSave member.getId()->"+member.getId());
		log.info("member.getName()-> {}.", member.getName());
		log.info("member.getSal()-> {}.", member.getSal());
		
		Long id = memberService.saveMember(member);
		return new CreateMemberResponse(id);
	}

	// Version 2 (good)
	// Member member 직접 받으면 안됨
	// RestApi는 외부에서 함께 개발하는 경우가 많기 때문에 다른 컬럼들이 따라 들어올 가능성이 있음
	// 그 컬럼들의 값을 변경하는걸 조심해야함 (보안)
	// 버퍼 클래스를 만들어서 임시로 저장해두어 외부에서 테이블 컬럼값을 알지 못하도록 만들어둠
	// 외부 REST API를 사용할 때 반드시 ver2로 사용할 것
	// 목적 : Entity Member member --> 직접 화면이나 API위한 Setting 금지
	// 예시 : @NotEmpty  -->	@Column(name = "userName")
	@PostMapping("/restApi/v2/memberSave")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest cMember) {
		System.out.println("JpaRestApiController /api/v2/memberSave member.getId()->"+cMember.getName());
		log.info("cMember.getName()-> {}.", cMember.getName());
		log.info("cMember.getSal()-> {}.", cMember.getSal());
		Member member = new Member();
		member.setName(cMember.getName());
		member.setSal(cMember.getSal());
		
		Long id = memberService.saveMember(member);
		return new CreateMemberResponse(id);
	}
	
	// @Getter @Setter
	@Data
	static class CreateMemberRequest {
		@NotEmpty
		private String name;
		private Long sal;
	}
	
	@Data
	@RequiredArgsConstructor
	static class CreateMemberResponse {
		private final Long id;
		
//		public CreateMemberResponse(Long id) {
//			this.id = id;
//		}
	}
	
	// Bad API
	@GetMapping ("/restApi/v1/members")
	public List<Member> membersVer1() {
		System.out.println("JpaRestApiController /restApi/v1/members start...");
		List<Member> listMember = memberService.getListAllMember();
		return listMember;
	}
}
