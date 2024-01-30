package com.oracle.oBootHello.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.oracle.oBootHello.domain.Member1;
import com.oracle.oBootHello.service.MemberService;

@Controller // component로 설정된 데이터를 읽어서 전달함
public class MemberController {
	
	private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
 
	// MemberService memberService = new MemberService();	// 전통적

	private final MemberService memberService; 
	
	public MemberController(MemberService memberService) { // 클래스 인스턴스화 되어서 tamcat container에 등록되있는상태
		this.memberService = memberService;   
	}
	
	// 받을때 get  뿌릴때 post
	// handler mapping
	@GetMapping (value = "/members/memberForm")
	public String memberForm() {
		System.out.println("MemberController /members/memberForm start...");
		// D/S -> V/R --> template / + members/memberForm + .html
		return "members/memberForm";
	}
	
	@PostMapping (value = "/members/save")
	public String save(Member1 member1) {
		System.out.println("MemberController /members/save Start...");
		System.out.println("MemberController /members/save member1.getName()->" + member1.getName());
		
		Long id = memberService.memberSave(member1);
		System.out.println("MemberController /members/save id->"+id);

		return "redirect:/"; // root path HTTP 리다이렉션을 수행
	}	
	
	@GetMapping (value = "/members/memberList")
	public String memberList(Model model) {
		logger.info("memberList start...");
		List<Member1> memberList = memberService.allMembers();
		model.addAttribute("memberList", memberList);
		logger.info("memberList.size()-> {}", memberList.size());
		
		return "members/memberList";
	}
	
}