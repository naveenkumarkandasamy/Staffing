package com.envision.Staffing.converter;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = false)
public class IntegerArrayToStringConverter implements AttributeConverter<Integer[], String>{

	@Override
	public String convertToDatabaseColumn(Integer[] array) {
		return Arrays.stream(array).map(a -> a.toString())
				.collect(Collectors.joining(","));
	}

	@Override
	public Integer[] convertToEntityAttribute(String dbData) {
		if (dbData!=null && !dbData.isEmpty()) {
			return Arrays.stream(dbData.split(",")).mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
		}		
		return new Integer[0];
	}

}
