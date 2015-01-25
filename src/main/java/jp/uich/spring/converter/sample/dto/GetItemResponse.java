package jp.uich.spring.converter.sample.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GetItemResponse extends ApiResponseBase<Item> {

	private static final long serialVersionUID = 1L;

	private Item item;

	public Item getResource() {
		return item;
	}

}
