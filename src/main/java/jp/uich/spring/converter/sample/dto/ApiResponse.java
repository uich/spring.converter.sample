package jp.uich.spring.converter.sample.dto;

import java.io.Serializable;

public interface ApiResponse<R> extends Serializable {

	R getResource();

	ErrorInfo getErrorInfo();

}
