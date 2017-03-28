package urt.stats.core.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * query util for {@link NamedParameterJdbcTemplate}
 * 
 * @author ghost
 *
 */
public class QueryUtil {

    /**
     * Преобразует значение {@link Boolean} поля в {@link SqlParameterValue}, для запроса к БД.
     * 
     *  
     * @param value {@link Boolean}
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(Boolean value){
        SqlParameterValue result = null;
        result = new SqlParameterValue(Types.BOOLEAN, value);
        return result;
    }
    
    /**
     * Преобразует значение {@link Integer} поля в {@link SqlParameterValue}, для запроса к БД.
     * 
     *  
     * @param value {@link Integer}
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(Integer value){
        return new SqlParameterValue(Types.NUMERIC, value);
    }
    
    /**
     * Преобразует значение {@link Long} поля в {@link SqlParameterValue}, для запроса к БД.
     * 
     *  
     * @param value {@link Long}
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(Long value){
        return new SqlParameterValue(Types.BIGINT, value);
    }
    
    /**
     * Преобразует значение {@link Double} поля в {@link SqlParameterValue}, для запроса к БД.
     * 
     *  
     * @param value {@link Double}
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(Double value){
        return new SqlParameterValue(Types.NUMERIC, value);
    }
    
    /**
     * Преобразует значение {@link String} поля в {@link SqlParameterValue}, для запроса к БД.
     * 
     *  
     * @param value {@link String}
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(String value){
        return new SqlParameterValue(Types.VARCHAR, value);
    }
    
    /**
     * Преобразует значение {@link Date} поля в {@link SqlParameterValue}, для запроса к БД.
     * 
     *  
     * @param value {@link Date}
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(Date value){
        return new SqlParameterValue(Types.TIMESTAMP, value);
    }
    
    /**
     * Преобразует значение byte[] поля в {@link SqlParameterValue}, для запроса к БД.
     *  
     * @param value byte[]
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(byte[] value){
        return new SqlParameterValue(Types.BINARY, value);
    }
    
    /**
     * Преобразует значение {@link Enum} поля в {@link SqlParameterValue}, для запроса к БД.
     * 
     *  
     * @param value {@link Enum}
     * 
     * @return {@link SqlParameterValue}
     */
    public static SqlParameterValue convertToQueryParam(@SuppressWarnings("rawtypes") Enum value){
        Integer intValue = null;
        if(value != null){
            intValue = Integer.valueOf(value.ordinal());
        }
        return new SqlParameterValue(Types.NUMERIC, intValue);
    }
    
    /**
     * Выбирает строку из Result set.<br/>
     * Если выбранное значение равно <code>null</code>, возвращает пустую строку.<br/>
     * Если выбранное значение НЕ равно <code>null</code>, и параметр trim равен <code>true</code>,
     * удаляет начальные и конечные пробелы из строки. 
     * 
     * @param rs {@link ResultSet}
     * @param colName {@link String} имя колонки в ResultSet
     * @param trim boolean - удалять или нет начальные и конечные пробелы
     * 
     * @return {@link String} значение колонки
     * 
     * @throws SQLException on failure
     */
    public static String getSafeString(ResultSet rs, String colName, boolean trim) throws SQLException {
        String result = rs.getString(colName);
        if(result == null){
            result = "";
        } else {
            if(trim){
                result = result.trim();
            }
        }
        return result;
    }
    
    /**
     * Выбирает значение 1 или 0 из ResultSet и преобразует его в boolean.
     * Если значение в resultset равно <code>null</code> - вернет <code>false</code>
     * 
     * @param rs {@link ResultSet}
     * @param colName {@link String} имя колонки в ResultSet
     * 
     * @return boolean - занчение
     * 
     * @throws SQLException on failure
     */
    public static boolean getIntAsBoolean(ResultSet rs, String colName) throws SQLException{
        boolean result = false;
        if(rs.getObject(colName) != null){
            int val = rs.getInt(colName);
            result = (val == 1) ? true : false;
        }
        return result;
    }
    
}
