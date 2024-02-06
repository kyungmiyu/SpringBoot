package com.oracle.oBootJpa02.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;

@Entity
@Data
@SequenceGenerator(
			name = "team_seq_gen",
			sequenceName = "team_seq_generator",
			initialValue = 1,
			allocationSize = 1
			)
// foreinkey를  맺어주는 것에 다에 연관관계를 맺어주면 됨
public class Team {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "team_seq_gen"
			)
	
	private Long team_id;
	@Column(name = "teamname")
	private String name;
}
