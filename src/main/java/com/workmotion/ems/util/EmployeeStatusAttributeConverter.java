package com.workmotion.ems.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.persistence.AttributeConverter;

public class EmployeeStatusAttributeConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        StringBuilder builder = new StringBuilder();
        Iterator<String> statesIterator = attribute.iterator();
        while (statesIterator.hasNext()) {
            builder.append(statesIterator.next());
            if (statesIterator.hasNext()) {
                builder.append("#");
            }
        }
        return builder.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        return Arrays.asList(dbData.split("#"));
    }

}
