package io.danielhuisman.sanitizers.language.ir;

import io.danielhuisman.sanitizers.language.regex.Regex;
import io.danielhuisman.sanitizers.util.Tuple;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.stream.Collectors;

public class Primitive<T> {

    public Class<?> type;
    public T value;

    public Primitive(Class<?> type, T value) {
        this.type = type;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public Object convert() {
        if (type == Tuple.class) {
            var tuple = (Tuple<Primitive<?>>) value;
            if (tuple.size() == 2) {
                return Pair.of(tuple.get(0).convert(), tuple.get(1).convert());
            } else if (tuple.size() == 3) {
                return Triple.of(tuple.get(0).convert(), tuple.get(1).convert(), tuple.get(2).convert());
            } else {
                throw new NotImplementedException("Tuples of sizes other than 2 or 3 are not implemented.");
            }
        } else if (type == List.class) {
            var list = (List<Primitive<?>>) value;
            return list.stream().map(Primitive::convert).collect(Collectors.toList());
        }

        return value;
    }

    @Override
    public String toString() {
        String val = value.toString();
        if (type == Character.class) {
            val = "'" + StringEscapeUtils.escapeJava(val) + "'";
        } else if (type == String.class) {
            val = "\"" + StringEscapeUtils.escapeJava(val) + "\"";
        } else if (type == Regex.class) {
            val = ((Regex) value).toRegex();
        }

        return "primitive " + type.getSimpleName().toLowerCase() + " " + val;
    }
}
