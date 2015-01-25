package jp.uich.spring.converter.sample;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import jp.uich.spring.converter.sample.converter.ApiConverterRegistrar;
import jp.uich.spring.converter.sample.dto.GetItemResponse;
import jp.uich.spring.converter.sample.dto.GetUserResponse;
import jp.uich.spring.converter.sample.dto.Item;
import jp.uich.spring.converter.sample.dto.User;
import jp.uich.spring.converter.sample.exception.ApiException;
import jp.uich.spring.converter.sample.http.converter.ApiFormHttpMessageConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class ApiImpl implements Api {

	@Value("${api.base.url}")
	private String baseUrl;

	private RestTemplate restTemplate;
	private FormattingConversionService conversionService;

	public ApiImpl() {
		this.restTemplate = new RestTemplate();
		configureRestTemplate(restTemplate);

		this.conversionService = getConversionService();
	}

	protected void configureRestTemplate(RestTemplate restTemplate) {
		restTemplate.setMessageConverters(getConverters());
	}

	protected List<HttpMessageConverter<?>> getConverters() {
		FormHttpMessageConverter converter = new FormHttpMessageConverter();
		converter.setCharset(Charset.forName("Windows31J"));
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.TEXT_PLAIN, MediaType.APPLICATION_FORM_URLENCODED));

		return Arrays.asList(new ApiFormHttpMessageConverter(converter, conversionService));
	}

	protected FormattingConversionService getConversionService() {
		FormattingConversionServiceFactoryBean factoryBean = new FormattingConversionServiceFactoryBean();
		factoryBean.afterPropertiesSet();
		FormattingConversionService conversionService = factoryBean.getObject();
		ApiConverterRegistrar.registerConverters(conversionService);

		return conversionService;
	}

	@Override
	public User getUser(Long id) {
		GetUserResponse response = restTemplate.getForObject(baseUrl + "/user/" + id, GetUserResponse.class);

		if (response.hasError()) {
			throw new ApiException(response.getErrorInfo());
		}

		return response.getResource();
	}

	@Override
	public Item getItem(Long id) {
		GetItemResponse response = restTemplate.getForObject(baseUrl + "/item/" + id, GetItemResponse.class);

		if (response.hasError()) {
			throw new ApiException(response.getErrorInfo());
		}

		return response.getResource();
	}
}
