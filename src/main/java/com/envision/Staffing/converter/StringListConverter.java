package com.envision.Staffing.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

@Component
@Converter(autoApply = false)
public class StringListConverter implements AttributeConverter<List<String>, String> {

	@Override
	public String convertToDatabaseColumn(List<String> list) {
		if(list!=null && !list.isEmpty())
		return String.join(",", list);
		else 
			return null;
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if (dbData != null)
			return Arrays.stream(dbData.split(",")).map(String::trim).collect(Collectors.toList());
		else
			return new ArrayList<String>();
	}

}
