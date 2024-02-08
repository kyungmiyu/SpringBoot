package com.oracle.oBootMybatis01.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oracle.oBootMybatis01.model.Emp;
import com.oracle.oBootMybatis01.service.EmpService;
import com.oracle.oBootMybatis01.service.Paging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmpController {
	
	private final EmpService es;
	
	@RequestMapping(value = "listEmp")
	public String empList(Emp emp, Model model) {
		System.out.println("EmpController Start listEmp...");
		if (emp.getCurrentPage() == null) emp.setCurrentPage("1");
		// Emp 전체 count 14
		int totalEmp = es.totalEmp();
		model.addAttribute("totalEmp", totalEmp);
		System.out.println("EmpController Start totalEmp->" + totalEmp);
	
		Paging page = new Paging(totalEmp, emp.getCurrentPage());
		emp.setStart(page.getStart()); // 시작시 1
		emp.setEnd(page.getEnd()); // 시작시 10
		
		List<Emp> listEmp = es.listEmp(emp);
		System.out.println("EmpController list listEmp.size()->" + listEmp.size());
		
		model.addAttribute("totalEmp", totalEmp);
		model.addAttribute("listEmp", listEmp);
		model.addAttribute("page", page);
		
		return "list";
	}
	
	@GetMapping(value = "detailEmp")
	public String detailEmp(Emp emp1, Model model) {
		System.out.println("EmpController Start detailEmp...");
//		1. EmpService안에 detailEmp method 선언
//		   1) parameter : empno
//		   2) Return      Emp
//
//		2. EmpDao   detailEmp method 선언 
////		                    mapper ID   ,    Parameter
//		emp = session.selectOne("tkEmpSelOne",    empno);
//		System.out.println("emp->"+emp1);
		model.addAttribute("emp", emp1);=
		return "detailEmp";
	}
	
	
	@GetMapping(value="updateFormEmp")
	public String updateFormEmp(Emp emp1, Model model) {
		System.out.println("EmpController Start updateForm");
		Emp emp = es.detailEmp(emp1.getEmpno());
		
		System.out.println("emp.getEname()->" + emp.getEname());
		System.out.println("emp.getHiredate()->" + emp.getHiredate());
//		System.out.println("hiredate()->" + hiredate);
		
		// 문제 
		// 1. DTO  String hiredate
		// 2. View : 단순조회 OK ,JSP에서 input type="date" 문제 발생
		// 3. 해결책  : 년월일만 짤라 넣어 주어야 함
		model.addAttribute("emp", emp);
		
		String hiredate = "";
		if (emp.getHiredate() != null) {
			hiredate = emp.getHiredate().substring(0, 10);
			emp.setHiredate(hiredate);
		}
		System.out.println("hiredate->" + hiredate);
		model.addAttribute("emp", emp);
		return "updateFormEmp";
	}
	
	@PostMapping(value="updateEmp")
	public String updateEmp(Emp emp, Model model) {
		log.info("updateEmp Start...");
//      1. EmpService안에 updateEmp method 선언
//      1) parameter : Emp
//      2) Return      updateCount (int)
//
//   	2. EmpDao updateEmp method 선언
////                              mapper ID   ,    Parameter
//   updateCount = session.update("tkEmpUpdate",emp);
		
		return "forward:listEmp";
	}
	
	
	
} // end of class
