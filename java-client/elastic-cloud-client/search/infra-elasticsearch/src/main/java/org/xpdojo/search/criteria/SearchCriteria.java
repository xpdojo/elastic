package org.xpdojo.search.criteria;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public interface SearchCriteria {

    default Map<String, String> toTerms() {
        Field[] declaredFields = this.getClass().getDeclaredFields();
        return Arrays.stream(declaredFields)
                .map(Field::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toMap(term -> term, term -> term + ".keyword"));
    }

    default Map<String, String> toCriteria() {
        Field[] declaredFields = this.getClass().getDeclaredFields();

        return Arrays.stream(declaredFields)
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(this) != null
                                && !field.get(this).toString().isEmpty();
                    } catch (IllegalAccessException ignored) {
                    }
                    return false;
                })
                .collect(Collectors.toMap(Field::getName, field -> {
                    try {
                        return (String) field.get(this);
                    } catch (IllegalAccessException ignored) {
                    }
                    return null;
                }));
    }
}
