package com.envision.Staffing.converter;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = false)
public class DoubleArrayToStringConverter implements AttributeConverter<Double[], String> {

	@Override
	public String convertToDatabaseColumn(Double[] array) {
		return Arrays.stream(array).map(element -> (Math.round(element * 100.0) / 100.0)).map(a -> a.toString())
				.collect(Collectors.joining(","));
	}

	@Override
	public Double[] convertToEntityAttribute(String dbData) {
		if (dbData!=null && !dbData.isEmpty()) {
			return Arrays.stream(dbData.split(",")).mapToDouble(Double::parseDouble).boxed().toArray(Double[]::new);
		}
		return new Double[0];
	}

}
