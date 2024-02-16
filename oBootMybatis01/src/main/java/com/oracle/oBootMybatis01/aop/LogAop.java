package com.oracle.oBootMybatis01.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAop {
//	// oBootMybatis01.dao.EmpDao 패키지 안에 있는 모든 메소드에 @Around를 걸어달라
//	// @Pointcut("within(com.oracle.oBootMybatis01.dao.EmpDao*)")
//	@Pointcut("within(com.oracle.oBootMybatis01.dao.*)") // 회사차원 스프링 시스템의 proxy에서 실행
//	private void pointcutMethod() {
//	}
//
//	@Around("pointcutMethod()") // 성능측정 반드시 Around 방식
//	public Object loggerAop(ProceedingJoinPoint joinPoint) throws Throwable {
//		// com.oracle.oBootMybatis01.dao.EmpDao 수행되는 핵심관심사
//		String signatureStr = joinPoint.getSignature().toString();
//		System.out.println(signatureStr+"is Start...");
//		long st = System.currentTimeMillis(); // 시작시간 start time
//		
//		try {
//			// 핵심관심사
//			Object obj = joinPoint.proceed();
//			return obj; // dao에서 멈추면 안되니까 호출해줘야함
//		} finally {
//			long et = System.currentTimeMillis(); // 끝시간 end time
//			System.out.println(signatureStr + "is finished.");
//			System.out.println(signatureStr + "경과시간 : " + (et-st));
//		}
//	} 
//	
//	@Before(("pointcutMethod()"))
//	public void before() throws Throwable {
//		System.out.println("AOP Before.");
//		
//	} 
//	
	
} // end of class
