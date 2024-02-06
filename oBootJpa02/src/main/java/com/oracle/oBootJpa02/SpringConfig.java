package com.oracle.oBootJpa02;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oracle.oBootJpa02.repository.JpaMemberRepository;
import com.oracle.oBootJpa02.repository.MemberRepository;
import com.oracle.oBootJpa02.service.MemberService;

import jakarta.persistence.EntityManager;

@Configuration
public class SpringConfig {
	private final DataSource datasource;
	private final EntityManager em;
	
	public SpringConfig(DataSource datasource, EntityManager em) {
		this.datasource = datasource;
		this.em = em;
	}
	
	@Bean
	public MemberService memberService() { // MemberService 클래스에서 @Service를 제외하려면 @Bean으로 등록해줘야함
		return new MemberService(memberRepository());
	}
	
	@Bean
	public MemberRepository memberRepository() {
		return new JpaMemberRepository(em);
	}
}
