package com.oracle.oBootMybatis01.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.oracle.oBootMybatis01.dao.EmpDao;
import com.oracle.oBootMybatis01.model.Emp;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpServiceImpl implements EmpService {

	private final EmpDao ed;

	@Override
	public int totalEmp() {
		System.out.println("EmpSerciveImpl Start total...");
		int totEmpCnt = ed.totalEmp();
		System.out.println("EmpSerciveImpl totalEmp totEmpEnt->" + totEmpCnt);
		return totEmpCnt;
	}

	@Override
	public List<Emp> listEmp(Emp emp) {
		List<Emp> empList = null; 
		System.out.println("EmpServiceImpl listManager Start...");
		empList = ed.listEmp(emp);
		System.out.println("EmpServiceImpl listEmp empList.size()->" + empList.size());
		return empList;
	}

	@Override
	public Emp detailEmp(int empno) {
		System.out.println("EmpServiceImpl detailEmp Start...");
		Emp emp = null;
		emp = ed.detailEmp(empno);
		return emp;
	}
	
	@Override
	public String updateEmp() {
		
	}
	
}
