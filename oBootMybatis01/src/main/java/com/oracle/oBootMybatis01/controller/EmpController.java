package com.oracle.oBootMybatis01.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.oBootMybatis01.model.Dept;
import com.oracle.oBootMybatis01.model.DeptVO;
import com.oracle.oBootMybatis01.model.Emp;
import com.oracle.oBootMybatis01.model.EmpDept;
import com.oracle.oBootMybatis01.model.Member1;
import com.oracle.oBootMybatis01.service.EmpService;
import com.oracle.oBootMybatis01.service.Paging;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmpController {
	//생성자 1개일때 @Autowired 생략가능
	private final EmpService es;
	private final JavaMailSender mailSender;
	
	@RequestMapping(value = "listEmp")
	public String empList(Emp emp, Model model) {
		System.out.println("EmpController Start listEmp...");
		// if (emp.getCurrentPage() == null) emp.setCurrentPage("1");
		// Emp 전체 count 14
		int totalEmp = es.totalEmp();
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
		Emp emp = es.detailEmp(emp1.getEmpno());
		model.addAttribute("emp",emp);		
	
		return "detailEmp";
	}
	
	
	@GetMapping(value="updateFormEmp")
	public String updateFormEmp(Emp emp1, Model model) {
		System.out.println("EmpController Start updateForm...");
	
		Emp emp = es.detailEmp(emp1.getEmpno());
		System.out.println("emp.getEname()->" + emp.getEname());
		System.out.println("emp.getHiredate()->" + emp.getHiredate());
//		System.out.println("hiredate()->" + hiredate);
		
		// 문제 
		// 1. DTO  String hiredate
		// 2. View : 단순조회 OK ,JSP에서 input type="date" 문제 발생
		// 3. 해결책  : 년월일만 짤라 넣어 주어야 함
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
		int updateCount = es.updateEmp(emp);
		System.out.println("empController es.updateEmp updateCount-->"+updateCount);
		model.addAttribute("uptCnt", updateCount);
		model.addAttribute("kk3","Message Test");
		
		// model에 저장된 데이터를 가지고 이동해서 model한테 저장하지 않더라도 데이터 유지 파라미터를 데리고 다니려면 forward를 써야함
		return "forward:listEmp"; 
//		return "redirect:listEmp"; // 단순한 페이지 이동 (컨트롤러의 이동일뿐)
	}
	
	
	@RequestMapping(value="writeFormEmp")
	public String writeFormEmp(Model model) {
		System.out.println("EmpController wirteFormEmp Start...");
		// 관리자 사번 만 GET
		List<Emp> empList = es.listManager();
		System.out.println("EmpController writeForm empList.size()->" + empList.size());
		model.addAttribute("empMngList", empList); // emp Manager List
		
		// 1. Service, Dao -> listManager
		// 2. Mapper -> tkSelectManager
		// 1) Emp table -> MGR에 등록된 정보 GET
		
		// 부서 코드명
		List<Dept> deptList = es.deptSelect();
		model.addAttribute("deptList", deptList);
		System.out.println("EmpController writeForm deptList.size()->" + deptList.size());
		
		return "writeFormEmp";
	}
	
	@PostMapping(value="writeEmp")
	public String writeEmp(Emp emp, Model model) {
		System.out.println("EmpController Start writeEmp...");
		// Service, Dao, Mapper명 [insertEmp] 까지 -> insert
		int insertResult = es.insertEmp(emp);
		
		if (insertResult > 0) {
			return "redirect:listEmp";
		}
		else {
			model.addAttribute("msg", "입력 실패 확인해 보세요");
			return "forward:writeFormEmp";
		}
	}
	

	@RequestMapping(value="writeFormEmp3")
	public String writeFormEmp3(Model model) {
		System.out.println("EmpController wirteFormEmp3 Start...");
		// 관리자 사번 만 GET
		List<Emp> empList = es.listManager();
		System.out.println("EmpController writeFormEmp3 empList.size()->" + empList.size());
		model.addAttribute("empMngList", empList); // emp Manager List
		
		// 1. Service, Dao -> listManager
		// 2. Mapper -> tkSelectManager
		// 1) Emp table -> MGR에 등록된 정보 GET
		
//		// 부서 코드명
		List<Dept> deptList = es.deptSelect();
		model.addAttribute("deptList", deptList); // dept
		System.out.println("EmpController writeFormEmp3 deptList.size()->" + deptList.size());
		
		return "writeFormEmp3";
	}
	
	// Validation 참조
	@PostMapping(value="writeEmp3")
	public String writeEmp3(@ModelAttribute("emp") @Valid Emp emp, BindingResult result, Model model) {
		// 오류난 message를 BindingResult의 result로 보낼수있음
		// Validation 참조할때는 view단(jsp)에서 form 태그 걸어줘야함
		System.out.println("EmpControllerStart writeEmp3...");
		
		// Validation 오류시 Result
		if (result.hasErrors()) {
			System.out.println("EmpController writeEmp3 hasErrors...");
			model.addAttribute("msg", "BindingResult 입력 실패 확인해보세요");
			
			return "forward:writeFormEmp3";
		}
		
		// Service, Dao, Mapper명 [insertEmp] 까지 -> insert
		int insertResult = es.insertEmp(emp);
		if (insertResult > 0) return "redirect:listEmp";
		else {
			model.addAttribute("msg", "입력 실패 확인해보세요");
		
			return "forward:writeFormEmp3";
		}
	}
	
	@GetMapping(value="confirm")
	public String confirm(Emp emp1, Model model) {
		Emp emp = es.detailEmp(emp1.getEmpno());
		model.addAttribute("empno", emp1.getEmpno());
		if (emp != null) {
			System.out.println("empController confirm 중복된 사번입니다.");
			model.addAttribute("msg", "중복된 사번입니다.");
			
			return "forward:writeFormEmp";
		
		} else {
			System.out.println("empController confirm 사용 가능한 사번입니다.");
			model.addAttribute("msg", "사용 가능한 사번입니다.");
		
			return "forward:writeFormEmp";
		}
	}
	
	@RequestMapping(value="deleteEmp")
	public String deleteEmp(Emp emp, Model model) {
		System.out.println("EmpController Start delete...");
		int result = es.deleteEmp(emp.getEmpno());
		model.addAttribute("msg", "삭제완료");
		return "redirect:listEmp";
	}
	
	@RequestMapping(value="listSearch3") // dto 받음
	public String listSearch3(Emp emp, Model model) {
		// Emp 전체 Count (condition에 따라서 페이지 작업을 다르게 해줌)
		int totalEmp = es.condTotalEmp(emp);
		System.out.println("EmpController listSearch3 totalEmp=>"+totalEmp);
		// paging 작업
		Paging page = new Paging(totalEmp, emp.getCurrentPage());
		// Parameter emp --> Page만 추가 Setting
		emp.setStart(page.getStart()); // 시작시 1
		emp.setEnd(page.getEnd()); // 시작시 10
		
		List<Emp> listSearchEmp = es.listSearchEmp(emp);
		System.out.println("EmpController listSearch3 listSearchEmp.size()->"+listSearchEmp.size());
	
		model.addAttribute("totalEmp", totalEmp);
		model.addAttribute("listEmp", listSearchEmp);
		model.addAttribute("page", page);
		
		return "list";
	}
	
	@GetMapping(value="listEmpDept")
	public String listEmpDept(Model model) {
		System.out.println("EmpController listEmpDept Start...");
		// Service Dao -> listEmpDept
		// Mapper만 -> tkListEmpDept
		List<EmpDept> listEmpDept = es.listEmpDept();
		model.addAttribute("listEmpDept", listEmpDept);
		
		return "listEmpDept";
	}
	
	@RequestMapping(value="mailTransport")
	public String mailTransport(HttpServletRequest request, Model model) {
	     System.out.println("mailSending...");
	     String tomail = "ttaekwang3@naver.com";         // 받는 사람 이메일
	     System.out.println(tomail);
	     String setfrom = "yu.km0304@gmail.com";
	     String title = "mailTransport 입니다";               // 제목
		
		try {
			// MimeMessage : 이메일 Internet 표준 Format
			// MimeMessage 표준 객체에 MimeMessageHelper로 세팅해야함
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(setfrom); // 보내는 사람 생략하면 정상작동 안함
			messageHelper.setTo(tomail); // 받는 사람 이메일
			messageHelper.setSubject(title); // 메일 제목은 생략가능
			
			String tempPassword = (int) (Math.random() * 99999) + 1+"";
			messageHelper.setText("임시 비밀번호입니다." + tempPassword); // 메일 내용
			System.out.println("임시 비밀번호입니다 : " + tempPassword);
			mailSender.send(message);
			model.addAttribute("check", 1); // 전송 성공
			
			// DB Logic 구성
			// Service를 이용해 새로 발급할 new password을 받아서 mapper에 연결하는 로직
		} catch(Exception e) {
			System.out.println("mailTransport Exception->" + e.getMessage());
			model.addAttribute("check", 2); // 전송 실패
		}
		
		return "mailResult";
	}
	
	// Procedure Test 입력화면
	//                     writeDeptIn
	@RequestMapping(value="writeDeptIn")
	public String writeDeptIn(Model model) {
		System.out.println("writeDeptIn Start...");
		return "writeDept3";
	}
	
	
	@PostMapping(value="writeDept")
	public String writeDept(DeptVO deptVO, Model model) {
		es.insertDept(deptVO);
		if (deptVO == null) {
			System.out.println("deptVO NULL");
		} else {
			System.out.println("deptVO.getOdeptno()->" + deptVO.getDeptno());
			System.out.println("deptVO.getDname()->" + deptVO.getDname());
			System.out.println("deptVO.getOloc()->" + deptVO.getOloc());
			model.addAttribute("msg", "정상입력되었습니다.");
			model.addAttribute("dept", deptVO);
		}
		
		return "writeDept3";
	}
	
	// Map 적용
	@GetMapping(value = "writeDeptCursor")
	public String writeDeptCursor(Model model) {
	    System.out.println("EmpController writeDeptCursor Start...");
	    // 파라미터 보내는 방식 3가지
	    // Map : 레이아웃을 정의해두고 사용하지 않음, 유연함, 개발의 편의성, 유지보수가 어려움, 서로 다른 회사이거나 부서가 다를 때 사용
	    // DTO : 레이아웃을 명확히 정의해두고 사용, 방식을 명확하게 확인가능, 협업이 잘 이루어질때 사용
	    // 부서범위 조회
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("sDeptno", 10);
		map.put("eDeptno", 55);

        es.selListDept(map);
        List<Dept> deptLists = (List<Dept>) map.get("dept");
//        10	ACCOUNTING	NEW YORK
//        20	RESEARCH	DALLAS
//        30	SALES	CHICAGO
//        40	OPERATIONS	BOSTON
        for(Dept dept : deptLists) {
        	System.out.println("dept.getDname->"+dept.getDname());
			System.out.println("dept.getLoc->"+dept.getLoc());
        }
		System.out.println("deptList Size->"+ deptLists.size());
		model.addAttribute("deptList", deptLists);
		
		 return "writeDeptCursor";
	}
	
	// InterCeptor 시작화면
	@RequestMapping(value="interCeptorForm")
	public String interCeptorForm(Model model) {
		System.out.println("interCeptorForm Start...");
		
		return "interCeptorForm";
	}
	
	// 2번 interCeptor Number2
	@RequestMapping(value="interCeptor")
	public String interCeptor(Member1 member1, Model model) {
		System.out.println("EmpController interCeptor Test Start");
		System.out.println("EmpController interCeptor id->" + member1.getId());
		
		// 존재 : 1, 비존재 : 0
		int memCnt = es.memCount(member1.getId());
		
		System.out.println("EmpController interCeptor memCnt->" + memCnt);
		
		model.addAttribute("id", member1.getId());
		model.addAttribute("memCnt", memCnt);
		System.out.println("interCeptor Test End");
		
		return "interCeptor"; // User 존재하면 User 이용 조회 Page
	}
	
	// SampleInterCeptor 내용을 받아 처리
	@RequestMapping(value="doMemberWrite", method = RequestMethod.GET)
	public String doMemberWrite(Model model, HttpServletRequest request) {
		String ID = (String)request.getSession().getAttribute("ID");
		System.out.println("doMemberWrite 부터 하세요");
		model.addAttribute("id", ID);
		
		return "doMemberWrite";
	}
	
	// interCeptor 진행 Test
	@RequestMapping(value="doMemberList")
	public String doMemberList(Model model, HttpServletRequest request) {
		String ID = (String)request.getSession().getAttribute("ID");
		System.out.println("doMemberList Test Start ID->" + ID);
		Member1 member1 = null;
		// Member1 List Get Service
		List<Member1> listMem = es.listMem(member1);
		model.addAttribute("ID", ID);
		model.addAttribute("listMem", listMem);
		
		return "doMemberList"; // User 존재하면 User 이용 조희 Page
	}
	
	// ajaxForm Test 입력화면
	@RequestMapping(value="ajaxForm")
	public String ajaxForm(Model model) {
		System.out.println("ajaxForm Start...");
		
		return "ajaxForm";
	}
	
	@ResponseBody // RestController(@RestController)가 아니기 때문에 ResponseBody를 걸어줘야함
	@RequestMapping(value="getDeptName")
	public String getDeptName(Dept dept, Model model) {
		System.out.println("deptno->" + dept.getDeptno());
		String deptName = es.deptName(dept.getDeptno());
		System.out.println("deptName->"+deptName);
	
		return deptName;
	}
	
	// Ajax List Test
	@RequestMapping(value="listEmpAjaxForm")
	public String listEmpAjaxForm(Model model) {
		Emp emp = new Emp();
		System.out.println("Ajax List Test Start");
		// Parameter emp -> Page만 추가 Setting
		emp.setStart(1); // 시작시 1
		emp.setEnd(10); // 시작시 10
		
		List<Emp> listEmp = es.listEmp(emp);
		System.out.println("EmpController listEmpAjax listEmp.size()->" + listEmp.size());
		model.addAttribute("result", "kkk");
		model.addAttribute("listEmp", listEmp);
		
		return "listEmpAjaxForm";
	}
	
	@ResponseBody //  this 클래스가 RestController가 아니기 때문에 @ResponseBody를 붙여서 Ajax 형태로 되돌려주기
	@RequestMapping(value="empSerializeWrite")
	public Map<String, Object> empSerializeWrite(@RequestBody @Valid Emp emp) {
		System.out.println("EmpController Start...");
		System.out.println("EmpController emp-> " + emp);
		int writeResult = 1;
		
		// int writeResult = kkk.writeEmp(emp);
		// String folloewingProStr = Integer.toString(followingPro);
		Map<String, Object> resultMap = new HashMap<>();
		System.out.println("EmpController empSerializeWrite writeResult-> "+ writeResult);
		resultMap.put("writeResult", writeResult);
		
		return resultMap;
	}
	
	@RequestMapping(value="listEmpAjaxForm2")
	public String listEmpAjaxForm2(Model model) {
		System.out.println("listEmpAjaxForm2 Start...");
		
		Emp emp = new Emp();
		System.out.println("Ajax List Test Start");
		// Parameter emp --> Page만 추가 Setting
		emp.setStart(1); // 시작시 1
		emp.setEnd(15); // 시작시 15
		
		List<Emp> listEmp = es.listEmp(emp);
		model.addAttribute("listEmp", listEmp);
		
		return "listEmpAjaxForm2";
	}
	
	@RequestMapping(value="listEmpAjaxForm3")
	public String listEmpAjaxForm3(Model model) {
		System.out.println("listEmpAjaxForm3 Start...");
		Emp emp = new Emp();
		// Parameter emp --> Page만 추가 Setting
		emp.setStart(1); // 시작시 1
		emp.setEnd(15); // 시작시 15
		List<Emp> listEmp = es.listEmp(emp);
		model.addAttribute("listEmp", listEmp);
		return "listEmpAjaxForm3";
		
	}
	
	@ResponseBody
	@RequestMapping(value="empListUpdate")
	public Map<String, Object> empListUpdate(@RequestBody @Valid List<Emp> listEmp) {
		System.out.println("EmpController empListUpdate Start...");
		int updateResult = 1;
		
		for(Emp emp : listEmp) {
			System.out.println("EmpController empListUpdate emp->"+emp);
		}
		
		// int writeResult = kkk.listUpdateEmp(emp);
		// String followProStr = Integer.toString(followingPro);
		System.out.println("EmpController empListUpdate writeResult->"+updateResult);
		
		Map<String, Object> resultMap = new HashMap<>();
		return resultMap;
	}
	
	@ResponseBody
	@RequestMapping(value="transactionInsertUpdate")
	public String transactionInsertUpdate(Emp emp, Model model) {
		System.out.println("EmpController transactionInsertUpdate Start...");
		// Member Insert 성공과 실패
		int returnMember = es.transactionInsertUpdate();
		System.out.println("EmpController transactionInsertUpdate returnMember->"+returnMember);
		String returnMemberString = String.valueOf(returnMember);
		return returnMemberString;
	}
	
	
	
	
	
} // end of class
