package com.oracle.oBootHello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.oBootHello.domain.Emp;

// 어노테이션으로 설정하면 dispatcher-sevlet이 찾아서 실행함
@Controller
public class HelloController {
	
	//  Logger에 HelloController를 달아줌
	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
	

	// java -> src/main/java 
	// view -> resources
	
	// prefix -> templates (경로)
	// suffix -> .html (확장자명)
	@RequestMapping("hello") // 핸들러방식 - hello 핸들
	public String hello(Model model) {
		System.out.println("hello start...");
		logger.info("logger start...");
		model.addAttribute("parameter", "boot start...");
		// D/S --> V/R --> templates/ + hello + .html (dispatcher-servlet이 하는 일)
		return "hello";
	}
	
	// ResponseBody 어노테이션하면 return 값에 담긴 값을 보냄
	@ResponseBody 
	@GetMapping("ajaxString")
	// @RequestParam엔 파라미터 값 넣어줘야함
	// 내장 Tomcat : port(8381) 번호 별로 어플리케이션이 지정됨 
	public String ajaxString(@RequestParam("ajaxName") String aName) {
		System.out.println("HelloController ajaxString aName"+aName);
		return aName;
	}
	
	@ResponseBody
	@GetMapping("ajaxEmp")
	public Emp ajaxEmp(@RequestParam("empno") String empno, @RequestParam("ename") String ename) {
		System.out.println("HelloController ajaxEmp empno->"+empno);
		logger.info("ename->{}", ename);
		// 객체는 JSON 형태로 들어감
		Emp emp = new Emp();
		emp.setEmpno(empno);
		emp.setEname(ename);
		
		return emp;
	}

	
}
