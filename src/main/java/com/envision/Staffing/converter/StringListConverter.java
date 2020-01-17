package com.envision.Staffing.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = false)
public class StringListConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> list) {
		return String.join(",", list);
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if (dbData != null)
			return new ArrayList<>(Arrays.asList(dbData.split(",")));
		else
			return new ArrayList<String>();
	}

}
