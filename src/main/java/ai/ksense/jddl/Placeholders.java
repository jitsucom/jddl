package ai.ksense.jddl;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Placeholders {
    private static final Pattern DEFAULT_PROPERTY_VAL_PATTERN = Pattern.compile("\\$\\{(.+?):(.+?)\\}");
    private Map<String, String> placeholders;

    public Placeholders(Map<String, String> placeholders) {
        this.placeholders = placeholders;
    }

    public String apply(String where, boolean strict) {
        StringBuilder b = new StringBuilder();
        int lastStart = 0;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            String value = entry.getValue();
            String token = "${" + entry.getKey() + "}";
            where = where.replace(token, value);
        }
        Matcher m = DEFAULT_PROPERTY_VAL_PATTERN.matcher(where);
        while (m.find()) {
            int start = m.start();
            b.append(where, lastStart, start);
            String name = m.group(1);
            String value = placeholders.get(name);
            if (value != null) {
                b.append(value);
            } else if (m.groupCount() >= 2) {
                b.append(m.group(2));
            } else if (!strict) {
                b.append(m.group(0));
            } else {
                throw new IllegalStateException("Can't resolve placeholder " + m.group(0));
            }
            lastStart = m.end();
        }
        b.append(where.substring(lastStart));
        return b.toString();
    }
}
