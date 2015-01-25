package jp.uich.spring.converter.sample.dto;

import lombok.Data;

@Data
public abstract class ApiResponseBase<R> implements ApiResponse<R> {

	private ErrorInfo errorInfo;

	public boolean hasError() {
		return errorInfo != null;
	}
}
