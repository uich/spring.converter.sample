package jp.uich.spring.converter.sample.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ErrorInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String code;
	private String message;
}
