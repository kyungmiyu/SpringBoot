package com.oracle.oBootBoard;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.oracle.oBootBoard.dao.BDao;
import com.oracle.oBootBoard.dao.JdbcDao;

@Configuration
public class SpringConfig {
	private final DataSource dataSource;
	public SpringConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Bean
	public BDao jdbcDao() {
		// spring 구조에서는 mvc 모델을 맞춰놓고 시작 
		// bean - component로 설정을 해야한다
		// 연결구조를 만들어놓고 시작하는 것이 spring의 DI
		return new JdbcDao(dataSource);
		// return new MemoryMemberRepository();
	}
}
