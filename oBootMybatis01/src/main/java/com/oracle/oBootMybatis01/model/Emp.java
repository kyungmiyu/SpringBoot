package com.oracle.oBootMybatis01.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Emp {
	
	private int empno;
	// validation 서버에서 체크하는 것 그동안 JS로 함수로 했었음
	@NotEmpty(message = "이름은 필수입니다.")
	private String ename;
	private String job;
	private int mgr;
	private String hiredate;
	private int sal;
	private int comm;
	private int deptno;

	// 조회용
	// 테이블에 있는 정보와 페이지에 조회용 정보들을 DTO에 넣어둠
	private String search;
	private String keyword;
	private String pageNum;
	private int start;
	private int end;
	
	// page 정보
	private String currentPage;
}
