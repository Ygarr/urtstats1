package urt.stats.core.dao;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import urt.stats.core.exception.JdbcException;
import urt.stats.core.vo.UrTSortHelper;

/**
 * UrT DAO super interface
 * @author ghost
 *
 */
public interface UrTDao {

    /**
     * Executes parameterized SELECT query
     * and returns {@link SqlRowSet} as a result.
     * 
     * @param query - SELECT query text
     * @param paramMap {@link MapSqlParameterSource} - query params
     * 
     * @return {@link SqlRowSet} - query result
     * 
     * @throws JdbcException on failure
     */
    public SqlRowSet queryForRowSet(String query, MapSqlParameterSource paramMap) throws JdbcException;
    
    /**
     * Executes parameterized SELECT query
     * and returns {@link List} &lt;{@link Map}&lt;{@link String}, {@link Object}&gt;&gt; as a result.
     * 
     * @param query {@link String} SELECT query text
     * @param paramMap {@link MapSqlParameterSource} - query params
     * 
     * @return {@link List} &lt;{@link Map}&lt;{@link String}, {@link Object}&gt;&gt; - query results
     * 
     * @throws JdbcException on failure
     */
    public List<Map<String, Object>> queryForList(String query, MapSqlParameterSource paramMap) throws JdbcException;
    
    /**
     * Executes parameterized SELECT COUNT query
     * 
     * @param query - query text
     * @param paramMap {@link MapSqlParameterSource} - query params
     * 
     * @return int - query count result
     * 
     * @throws JdbcException on failure
     */
    public int queryForCount(String query, MapSqlParameterSource paramMap) throws JdbcException;

    /**
     * Returns default sort helper
     * 
     * @return {@link UrTSortHelper} sort helper
     */
    public UrTSortHelper getSortHelper();
    
    /**
     * Returns sort helper with the specified sort column
     * 
     * @param column int - sort column number
     * @param isAsc boolean sort order
     * 
     * @return {@link UrTSortHelper} sort helper
     */
    public UrTSortHelper getSortHelper(int column, boolean isAsc);

}
