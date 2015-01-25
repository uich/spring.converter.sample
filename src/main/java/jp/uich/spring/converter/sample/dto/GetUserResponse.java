package jp.uich.spring.converter.sample.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetUserResponse extends ApiResponseBase<User> {

	private static final long serialVersionUID = 1L;

	private User user;

	public User getResource() {
		return user;
	}

}
