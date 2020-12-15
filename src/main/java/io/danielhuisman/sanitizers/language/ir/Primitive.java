package io.danielhuisman.sanitizers.language.ir;

import org.apache.commons.lang3.StringEscapeUtils;

public class Primitive<T> {

    public Class<?> type;
    public T value;

    public Primitive(Class<?> type, T value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        String val = value.toString();
        if (type == Character.class) {
            val = "'" + StringEscapeUtils.escapeJava(value.toString()) + "'";
        } else if (type == String.class) {
            val = "\"" + StringEscapeUtils.escapeJava(value.toString()) + "\"";
        }

        return "primitive " + type.getSimpleName().toLowerCase() + " " + val;
    }
}
