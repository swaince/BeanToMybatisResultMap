package top.jinggg.plugins.converter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PostgresConvertService implements ConvertService {

    private Map<String, String> registry = new ConcurrentHashMap<>(){
        {

            // 数值类型
            put("java.lang.Integer", "INTEGER");
            put("java.lang.Long", "BIGINT");
            put("java.math.BigDecimal", "DECIMAL");

            // 字符串类型
            put("java.lang.String", "VARCAHR");
        }
    };
    @Override
    public String convert(String javaType) {
        return registry.get(javaType);
    }
}
