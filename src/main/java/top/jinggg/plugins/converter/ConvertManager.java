package top.jinggg.plugins.converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConvertManager {
    private static final Map<String, ConvertService> CONVERT_SERVICE_MAP = new ConcurrentHashMap<>();

    static {
        init();
    }

    public static String convert(String dbType, String javaType) {
        ConvertService convertService = CONVERT_SERVICE_MAP.get(dbType);
        return convertService.convert(javaType);
    }

    private static void init() {
        CONVERT_SERVICE_MAP.put("postgres", new PostgresConvertService());
    }
}
