package com.oracle.oBootJpa01.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity // 도메인에 Entity 반드시 설정
@Table(name = "member1") // table을 걸어주지 않으면 객체가 테이블이 됨
@Getter
@Setter
@ToString
public class Member {
	@Id // 기본키 설정 (반드시)
	private Long id;
	private String name;
	
	
//	@Override
//	public String toString() {
//		// return super.toString();
//		String returnStr = "";
//		returnStr = "[id:" + this.id + ", name:" + this.name + "]";
//		return returnStr;
//	}
	
//	lombok의 @Getter @Setter로 생성	
//	public Long getId() {
//		return id;
//	}
//	public void setId(Long id) {
//		this.id = id;
//	}
//	public String getName() {
//		return name;
//	}
//	public void setName(String name) {
//		this.name = name;
//	}
	
}
