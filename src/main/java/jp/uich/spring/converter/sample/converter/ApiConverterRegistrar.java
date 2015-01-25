package jp.uich.spring.converter.sample.converter;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import jp.uich.spring.converter.sample.annotation.Mixin;
import jp.uich.spring.converter.sample.annotation.Property;
import jp.uich.spring.converter.sample.dto.ApiResponse;
import jp.uich.spring.converter.sample.dto.mixin.GetUserResponseMixin;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public final class ApiConverterRegistrar {

	private static Map<Class<?>, Class<?>> mixinTypes;
	static {
		mixinTypes = Collections.unmodifiableMap(getMixinClasses(GetUserResponseMixin.class.getPackage().getName()));
	}

	@SneakyThrows
	static Map<Class<?>, Class<?>> getMixinClasses(String packageName) {
		return ClassPath.from(ApiConverterRegistrar.class.getClassLoader()).
				getTopLevelClasses(packageName).stream()
				.map(ClassInfo::load)
				.filter(type -> type.isAnnotationPresent(Mixin.class))
				.collect(Collectors.toMap(
						type -> type.getAnnotation(Mixin.class).value(),
						Function.identity()));
	}

	private ApiConverterRegistrar() {
		// nop. dont use.
	}

	public static void registerConverters(FormattingConversionService formattingConversionService) {
		Set<?> converters = ImmutableSet.builder()
				.add(new ApiResponseConverterFactory(formattingConversionService))
				.build();

		ConversionServiceFactory.registerConverters(converters, formattingConversionService);
	}

	@AllArgsConstructor
	public static class ApiResponseConverterFactory implements
			ConverterFactory<MultiValueMap<String, String>, ApiResponse<?>> {

		private ConversionService conversionService;

		@Override
		public <T extends ApiResponse<?>> Converter<MultiValueMap<String, String>, T> getConverter(Class<T> targetType) {
			return new ApiResponseConverter<>(conversionService, targetType);
		}

		@AllArgsConstructor
		private static class ApiResponseConverter<T> implements Converter<MultiValueMap<String, String>, T> {

			private static final TypeDescriptor STRING_TYPE_DESC = TypeDescriptor.valueOf(String.class);

			private ConversionService conversionService;
			private Class<T> targetType;

			@SuppressWarnings("unchecked")
			@Override
			public T convert(MultiValueMap<String, String> source) {
				T targetNewInstance = BeanUtils.instantiate(targetType);
				BeanWrapper targetWrapper = PropertyAccessorFactory.forBeanPropertyAccess(targetNewInstance);

				// "user.id"でアクセスした際にUserインスタンスを自動で生成させる
				targetWrapper.setAutoGrowNestedPaths(true);

				Class<?> mixinType = getMixinType(targetType); // 実装は割愛しします

				for (Field mixinField : mixinType.getDeclaredFields()) {
					Property property = mixinField.getAnnotation(Property.class);

					if (property == null) {
						continue;
					}

					if (targetWrapper.isWritableProperty(property.accessKey()) == false) {
						continue;
					}

					String value = source.getFirst(property.paramName());

					if (value == null) {
						continue;
					}

					TypeDescriptor targetFieldDesc = new TypeDescriptor(mixinField);

					Object convertedValue = conversionService.convert(value, STRING_TYPE_DESC, targetFieldDesc);

					targetWrapper.setPropertyValue(property.accessKey(), convertedValue);
				}

				return (T) targetWrapper.getWrappedInstance();
			}

			private Class<?> getMixinType(Class<T> targetType) {
				return Optional.ofNullable(ApiConverterRegistrar.mixinTypes.get(targetType))
						.orElseThrow(() -> new IllegalArgumentException());
			}
		}
	}
}
