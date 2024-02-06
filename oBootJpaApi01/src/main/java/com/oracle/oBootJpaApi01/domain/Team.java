package com.oracle.oBootJpaApi01.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@SequenceGenerator(
			name = "team_seq_gen5",
			sequenceName = "team_seq_generator5",
			initialValue = 1,
			allocationSize = 1
			)
// foreinkey를  맺어주는 것에 다에 연관관계를 맺어주면 됨
@Table(name = "team5")
public class Team {
	@Id
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "team_seq_gen5"
			)
	
	private Long team_id;
	@Column(name = "team_name")
	private String name;
}
