package jp.uich.spring.converter.sample;

import jp.uich.spring.converter.sample.dto.Item;
import jp.uich.spring.converter.sample.dto.User;

public interface Api {

	/*
	 * Response-Body: User_ID=1&User_Name=ジョン&User_Birthday=19900613 
	 */
	User getUser(Long id);

	/*
	 * Response-Body: Item_ID=1&Item_Name=えんぴつ&Price=108
	 */
	Item getItem(Long id);
}
