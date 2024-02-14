package com.oracle.oBootMybatis01.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.oracle.oBootMybatis01.model.Emp;
import com.oracle.oBootMybatis01.model.EmpDept;

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
			System.out.println("EmpDaoImpl listEmp empList.size()->"+empList.size());
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

	@Override
	public int updateEmp(Emp emp) {
		System.out.println("EmpDaoImpl update start..");
		int updateCount = 0;
		
		try {
			updateCount = session.update("tkEmpUpdate", emp);
		} catch (Exception e) {
			System.out.println("EmpDaoImpl updateEmp Exception->"+e.getMessage());
		}
		
		return updateCount;
	}

	@Override
	public List<Emp> listManager() {
		List<Emp> empList = null;
		System.out.println("EmpDaoImpl listManager Start...");
		
		try {
			// emp 관리자만 Select
			empList = session.selectList("tkSelectManager");
		} catch(Exception e) {
			System.out.println("EmpDaoImpl listManager Exception->"+e.getMessage());
		}
		
		return empList;
	}

	@Override
	public int insertEmp(Emp emp) {
//		 Mybatis SQL Return 값
//		 1) Insert - 1 (여러개인 경우 1) 
//		 2) Update - 업데이트 된 행의 개수 (없으면 0) 
//		 3) Delete - 삭제 된 행의 개수 (없으면 0)
		int result = 0;
		System.out.println("EmpDaoImpl insertEmp Start...");
		
		try {
			result = session.insert("insertEmp",emp);
		} catch (Exception e) {
			System.out.println("EmpDaoImpl insertEmp Exception->"+e.getMessage());
		}
		
		return result;
	}
	
	@Override
	public List<Emp> empSearchList3(Emp emp) {
		List<Emp> empSearchList3 = null;
		System.out.println("EmpDaoImpl empSearchList3 Start...");
		System.out.println("EmpDaoImpl empSearchList3 emp->" + emp);
	
		try {
			empSearchList3 = session.selectList("tkEmpSearchList3", emp);
		} catch (Exception e) {
			System.out.println("EmpDaoImpl empSearchList3 Exception->"+e.getMessage());
		}

		return empSearchList3;
	}
	
	@Override
	public int condTotalEmp(Emp emp) {
		int totEmpCount = 0;
		System.out.println("EmpDaoImpl condTotalEmp Start...");
		System.out.println("EmpDaoImpl condTotalEmp Start emp->" + emp);
		try {
			totEmpCount = session.selectOne("condEmpTotal", emp);
			System.out.println("EmpDaoImpl totalEmp totEmpCount->" +totEmpCount);
		} catch (Exception e) {
			System.out.println("EmpDaoImpl condTotalEmp Exception->" + e.getMessage());
		}
		return totEmpCount;
	}

	@Override
	public int deleteEmp(int empno) {
		System.out.println("EmpDaoImpl delete start..");
		int result = 0;
		System.out.println("EmpDaoImpl deleteEmp empno->" + empno);
	
		try {
			result = session.delete("deleteEmp", empno);
			System.out.println("EmpDaoImpl delete result->"+result);
		} catch (Exception e) {
			System.out.println("EmpDaoImpl deleteEmp Exception->"+e.getMessage());
		}
	
		return result;
	}

	@Override
	public List<EmpDept> listEmpDept() {
		System.out.println("EmpDaoImpl listEmpDept Start...");
		List<EmpDept> empDept = null;
		try {
			empDept = session.selectList("tkListEmpDept");
			System.out.println("EmpDaoImpl listEmpDept empDept.size()->" + empDept.size());
		} catch (Exception e) {
			System.out.println("EmpDaoImpl listEmpDept Excpetion->" + e.getMessage());
		}
		return empDept;
	}
	

}
