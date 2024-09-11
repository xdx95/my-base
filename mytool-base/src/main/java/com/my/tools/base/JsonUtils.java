package com.my.tools.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.SimpleDateFormat;
import org.slf4j.Logger;

/**
 * @author: xdx
 * @date: 2024/8/29
 * @description: Json 工具类
 */
public class JsonUtils {

	private static final Logger log = LogUtils.get();

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// 日期格式化
	private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";

	static {
		// 对象的所有字段全部列入
		objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
		// 忽略空 Bean to Json 错误
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// Json to Bean 忽略 Bean 对象中不存在对应属性
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// 取消默认转换 Timestamps 形式
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// 设置日期统一格式，只支持 Date ，LocalDateTime 需要引入 jackson-datatype-jsr310
		objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
	}

	/**
	 * Object 对象格式化成 JSON 字符串
	 */
	public static <T> String format(T object) {
		if (object == null) {
			return null;
		}
		try {
			return object instanceof String ? (String) object
				: objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Json Format Exception", e);
			return null;
		}
	}

	/**
	 * Object 对象格式化成 JSON 字符串，且美化
	 */
	public static <T> String formatAndPretty(T object) {
		if (object == null) {
			return null;
		}
		try {
			return object instanceof String ? (String) object
				: objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("Json Format Exception", e);
			return null;
		}
	}

	/**
	 * JSON 字符串解析为 Object
	 */
	public static <T> T parse(String str, Class<T> clazz) {
		if (str == null || str.isEmpty() || clazz == null) {
			return null;
		}
		try {
			return objectMapper.readValue(str, clazz);
		} catch (Exception e) {
			log.error("Json Parse Exception", e);
			return null;
		}
	}

	public static <T> T parse(String str, TypeReference<T> typeReference) {
		if (str == null || str.isEmpty() || typeReference == null) {
			return null;
		}
		try {
			return objectMapper.readValue(str, typeReference);
		} catch (IOException e) {
			log.error("Json Parse Exception", e);
			return null;
		}
	}

	public static <T> T parse(String str, Class<?> collectionClazz,
		Class<?>... elementClazz) {
		JavaType javaType = objectMapper.getTypeFactory()
			.constructParametricType(collectionClazz, elementClazz);
		try {
			return objectMapper.readValue(str, javaType);
		} catch (IOException e) {
			log.error("Json Parse Exception", e);
			return null;
		}
	}
}
