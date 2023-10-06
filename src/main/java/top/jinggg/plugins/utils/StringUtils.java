package top.jinggg.plugins.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtils {

    private static final Pattern UPPER_PATTERN = Pattern.compile("([A-Z])");

    private StringUtils() {
        throw new IllegalArgumentException("utility class do not create instance");
    }

    /**
     * 驼峰转下划线
     * @param string   目标字符串
     * @return: java.lang.String
     */
    public static String toUnderLine(String string) {
        Matcher matcher = UPPER_PATTERN.matcher(string);
        while (matcher.find()) {
            String target = matcher.group();
            string = string.replaceAll(target, "_"+target.toLowerCase());
        }
        return string;
    }
}
