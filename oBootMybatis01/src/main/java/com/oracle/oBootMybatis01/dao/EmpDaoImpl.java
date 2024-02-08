package com.oracle.oBootMybatis01.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.oracle.oBootMybatis01.model.Emp;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EmpDaoImpl implements EmpDao {
	// Mybatis DB 연동
	private final SqlSession session;
	
	@Override
	public int totalEmp() {
		int totEmpCount = 0;
		System.out.println("EmpDaoImpl Start total...");
		
		try {
			// sesseion mybatis
			// selectOne 하나의 row
			// selectList 여러개
			totEmpCount = session.selectOne("com.oracle.oBootMybatis01.EmpMapper.empTotal");
			System.out.println("EmpDaoImpl totalEmp totEmpCount->" + totEmpCount);
			
		} catch (Exception e) {
			System.out.println("EmpDaoImpl totalEmp Exception->" + e.getMessage());
		}
		return totEmpCount;
	}

	@Override
	public List<Emp> listEmp(Emp emp) {
		List<Emp> empList = null;
		System.out.println("EmpDaoImpl listEmp Start...");
		try {
			empList = session.selectList("tkEmpListAll", emp); // Map ID, parameter
		} catch (Exception e) {
			System.out.println("EmpDaoImpl listEmp e.getMessage()->"+e.getMessage());
		}
		return empList;
	}

	@Override
	public Emp detailEmp(int empno) {
		System.out.println("EmpDaoImpl detail start...");
		Emp emp = new Emp();
		
		try {
			emp = session.selectOne("tkEmpSelOne", empno); // mapper ID, parameter
			System.out.println("EmpDaoImpl detail getEname->"+emp.getEname());
		} catch (Exception e) {
			System.out.println("EmpDaoImpl detail Exception->" + e.getMessage());
		}
		
		return emp;
	}

}
