<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>직원정보조회</h2>
	<form action="updateEmp" method="post">
	<input type="hidden" name="" value="${emp.empno}">
	<table>
		<tr><th>사번</th><td>${emp.empno}</td></tr>
		<tr><th>이름</th><td><input type="text" name="ename" required="required" value="${emp.ename}"></td></tr>
		<tr><th>업무</th><td><input type="text" name="job" required="required" value="${emp.job}"></td></tr>
		<tr><th>급여</th><td><input type="text" name="sal" required="required" value="${emp.sal}"></td></tr>
		<tr><th>입사일</th><td><input type="text" name="hiredate" required="required" value="${emp.hiredate}"></td></tr>
		<tr><th>보너스</th><td><input type="text" name="comm" required="required" value="${emp.comm}"></td></tr>
		<tr><th>관리사번</th><td><input type="text" name="mgr" value="${emp.mgr}"></td></tr>
		<tr><th>부서코드</th><td><input type="text" name="deptno" required="required" value="${emp.deptno}"></td></tr>
		<tr><td colspan="2"><input type="submit" value="확인"></td></tr>
	</table>
	</form>
</body>
</html>