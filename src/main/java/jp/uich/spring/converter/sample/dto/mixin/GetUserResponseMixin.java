package jp.uich.spring.converter.sample.dto.mixin;

import java.time.LocalDate;

import jp.uich.spring.converter.sample.annotation.Mixin;
import jp.uich.spring.converter.sample.annotation.Property;
import jp.uich.spring.converter.sample.dto.GetUserResponse;

import org.springframework.format.annotation.DateTimeFormat;

@Mixin(GetUserResponse.class)
public abstract class GetUserResponseMixin {

	@Property(accessKey = "user.id", paramName = "User_ID")
	private Long userId;
	@Property(accessKey = "user.name", paramName = "User_Name")
	private String userName;
	@Property(accessKey = "user.birthday", paramName = "User_Birthday")
	@DateTimeFormat(pattern = "yyyyMMdd")
	private LocalDate birthday;

	@Property(accessKey = "errorInfo.code", paramName = "ErrorCode")
	private String errorCode;
	@Property(accessKey = "errorInfo.message", paramName = "ErrorMessage")
	private String errorMessage;
}
