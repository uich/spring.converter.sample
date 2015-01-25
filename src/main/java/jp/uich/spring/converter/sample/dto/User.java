package jp.uich.spring.converter.sample.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private LocalDate birthDay;
}
