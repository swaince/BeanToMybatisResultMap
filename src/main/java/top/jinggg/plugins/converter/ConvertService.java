package top.jinggg.plugins.converter;

public interface ConvertService {

    /**
     * javaType to jdbcType
     * @param javaType java field type
     * @return jdbcType
     */
    String convert(String javaType);
}
