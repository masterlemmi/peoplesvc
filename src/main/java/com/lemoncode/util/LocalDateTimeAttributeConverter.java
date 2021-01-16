package com.lemoncode.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, String> {

    @Override
    public String convertToDatabaseColumn(LocalDateTime locDateTime) {
        return locDateTime == null ? null : locDateTime.toString();
    }

    @Override
    public LocalDateTime convertToEntityAttribute(String s) {
        return (s == null ? null : LocalDateTime.parse(s));
    }

}