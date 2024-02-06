package com.oracle.oBootJpaApi01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@SequenceGenerator(
				name = "member_seq_gen5", // 객체 Seq
				sequenceName = "member_seq_generate5", // DB Seq
				initialValue = 1,
				allocationSize = 1
				)
@Table(name = "member5")
public class Member {
	@Id
	@GeneratedValue(
				strategy = GenerationType.SEQUENCE,
				generator = "member_seq_gen5"
				)
	@Column(name = "member_id")
	private Long id;
	@NotEmpty
	@Column(name = "userName")
	private String name;
	private Long sal;
	
	// 관계 설정
	//foreign key는 다에 연관관계를 맺어주면 됨
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;	// 객체
	
	// 실제 컬럼X --> Buffer 용도
	@Transient
	private String teamname;
	
	@Transient
	private Long teamid;
	
}

