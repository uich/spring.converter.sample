package jp.uich.spring.converter.sample.http.converter;

import java.io.IOException;
import java.util.List;

import jp.uich.spring.converter.sample.dto.ApiRequest;
import jp.uich.spring.converter.sample.dto.ApiResponse;
import lombok.AllArgsConstructor;

import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.MultiValueMap;

@AllArgsConstructor
public class ApiFormHttpMessageConverter implements HttpMessageConverter<Object> {

	private FormHttpMessageConverter formHttpMessageConverter;
	private ConversionService conversionService;

	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return ApiResponse.class.isAssignableFrom(clazz)
				&& formHttpMessageConverter.canRead(MultiValueMap.class, mediaType);
	}

	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return ApiRequest.class.isAssignableFrom(clazz)
				&& formHttpMessageConverter.canWrite(MultiValueMap.class, mediaType);
	}

	public List<MediaType> getSupportedMediaTypes() {
		return formHttpMessageConverter.getSupportedMediaTypes();
	}

	@SuppressWarnings("unchecked")
	public Object read(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		final MultiValueMap<String, String> params = formHttpMessageConverter.read(
				(Class<? extends MultiValueMap<String, ?>>) MultiValueMap.class, inputMessage);

		return conversionService.convert(params, clazz);
	}

	@SuppressWarnings("unchecked")
	public void write(Object source, MediaType contentType, HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		final MultiValueMap<String, String> params = conversionService.convert(source, MultiValueMap.class);

		formHttpMessageConverter.write(params, contentType, outputMessage);
	}

}
