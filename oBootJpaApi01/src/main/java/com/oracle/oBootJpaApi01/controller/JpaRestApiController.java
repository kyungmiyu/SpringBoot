package com.oracle.oBootJpaApi01.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.oracle.oBootJpaApi01.domain.Member;
import com.oracle.oBootJpaApi01.service.MemberService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
	
	
	// Good API  Easy Version
	// 목표 : 이름 & 급여 만 전송
	@GetMapping("/restApi/v21/members")
	public Result memberVer21() {
	// pubic Object memberVer21() {
		// public List<MemberRtnDto> memberVer21() {
		List<Member> findMembers = memberService.getListAllMember();
		System.out.println("JpaRestApiController restApi/v21/members findMembers.size()->"+findMembers.size());
		List<MemberRtnDto> resultList = new ArrayList<MemberRtnDto>();
		
		// List<MemberRtnDto> resultList = new ArrayList<MemberRtnDto>();
		// 이전목적 : 반드시 필요한 Data만 보여준다(외부 노출 최대한 금지)
		for (Member member : findMembers) {
			MemberRtnDto memberRtnDto = new MemberRtnDto(member.getName(), member.getSal());
			System.out.println("restApi/v21/members getName->"+memberRtnDto.getName());
			System.out.println("restApi/v21/members getSal->"+memberRtnDto.getSal());
			resultList.add(memberRtnDto);
		}
		
		System.out.println("restApi/v21/members resultList.size()->"+resultList.size());
		return new Result(resultList.size(), resultList);
		// return resultList;
		// return new Result(resultList);
	}
	
	
	// Good API Lamda Version
	// 목표 : 이름 & 급여만 전송
	@GetMapping("/restApi/v22/members")
	public Result memberVer22() {
		List<Member> findMembers = memberService.getListAllMember();
		System.out.println("restApi/v22/members findMembers.size()->" + findMembers.size());
		// 자바 8에서 추가한 Stream은 람다를 활용할 수 있는 기술 중 하나
		List<MemberRtnDto> memberCollect = findMembers.stream()
										   .map(m->new MemberRtnDto(m.getName(), m.getSal()))
										   .collect(Collectors.toList());
		System.out.println("restApi/v22/members membercollect.size()->" + memberCollect.size());
		return new Result(memberCollect.size(), memberCollect);
	}
	
	
	// T는 인스턴스를 생성할 때 구체적인 타입으로 변경  --> 유연성
	@Data
	@AllArgsConstructor
	class Result<T> {
		private final int totCount; // 총 인원수 추가
		private final T data;
	}
	
	// final이 아니기 때문에 allargsconstructor를 써야함 
	// RequiredArgsConstructor는 지정된 속성(final)일 때 반드시 붙여야함
	@Data
	@AllArgsConstructor
	class MemberRtnDto { 
		private String name;
		private Long sal;
	}
	
	
	/*
	 *   수정 API
	 *   PUT 방식을사용했는데, PUT은 전체 업데이트를 할 때 사용
	 *   URI 상에서 '{ }' 로 감싸여있는 부분과 동일한 변수명을 사용하는 방법
	 *   해당 데이터가 있으면 업데이트를 하기에 
	 *   PUT요청이 여러번 실행되어도 해당 데이터는 같은 상태이기에 멱등(어떤 작업을 여러 번 적용하더라도 결과가 변하지 않는 성질)
	 *   {}로 url에 프라이머리 키를 넘겨줌
	 */
	@PutMapping("/restApi/v21/members/{id}") // 
	public UpdateMemberResponse updateMemberv21 (@PathVariable("id") Long id, 
												 @RequestBody @Valid UpdateMemberRequest umember) {
			
		memberService.updateMember(id, umember.getName(), umember.getSal());
		Member findMember = memberService.findByMember(id);
		return new UpdateMemberResponse(findMember.getId(), findMember.getName(), findMember.getSal());
			
	}
	
	@Data
	static class UpdateMemberRequest {
		private String name;
		private Long sal;
	}
	
	// 모든 필드가 final로 선언되어 있거나 생성자에서 모든 필드를 초기화하는 경우 
	// @AllArgsConstructor을 사용하여 생성자를 생성
	// = 필드를 가지고 있는 클래스에서 모든 필드를 초기화하는 생성자가 필요한 경우
	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {
		private Long id;
		private String name;
		private Long sal;
	}
	
	
	

} // end of class