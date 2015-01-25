package jp.uich.spring.converter.sample.dto.mixin;

import jp.uich.spring.converter.sample.annotation.Mixin;
import jp.uich.spring.converter.sample.annotation.Property;
import jp.uich.spring.converter.sample.dto.GetItemResponse;

@Mixin(GetItemResponse.class)
public abstract class GetItemResponseMixin {

	@Property(accessKey = "item.id", paramName = "Item_ID")
	private Long itemId;
	@Property(accessKey = "item.name", paramName = "Item_Name")
	private String itemName;
	@Property(accessKey = "item.price", paramName = "Item_Price")
	private Integer price;

	@Property(accessKey = "errorInfo.code", paramName = "ErrorCode")
	private String errorCode;
	@Property(accessKey = "errorInfo.message", paramName = "ErrorMessage")
	private String errorMessage;
}
