package jp.uich.spring.converter.sample.exception;

import jp.uich.spring.converter.sample.dto.ErrorInfo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApiException extends RuntimeException {

	private ErrorInfo errorInfo;
}
