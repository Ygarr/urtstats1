package urt.stats.core.dao.impl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import urt.stats.core.dao.UrTDao;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.util.QueryUtil;
import urt.stats.core.util.StringUtil;
import urt.stats.core.vo.UrTSortHelper;

/**
 * Abstract JDBC DAO
 * 
 * @author ghost
 *
 */
public class UrTJdbcDaoImpl implements UrTDao{

    /**
     * JDBC template
     */
    private NamedParameterJdbcTemplate jdbcTemplate;
    
    /**
     * Initialization
     * 
     * @param dataSource {@link DataSource}
     */
    @Autowired
    protected void init(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate (dataSource);
    }

    /**
     * Returns jdbc template
     * 
     * @return {@link NamedParameterJdbcTemplate } - jdbc template
     */
    protected NamedParameterJdbcTemplate getJdbcTemplate(){
        return this.jdbcTemplate;
    }
    
    
    /**
     * Returns new {@link Long} ID from the specified sequence
     * 
     * @param sequenceName {@link String} - sequence name
     * 
     * @return {@link Long} - new ID
     * 
     * @throws JdbcException on failure
     */
    protected Long getNewLongId(String sequenceName) throws JdbcException {
        if(!StringUtil.hasText(sequenceName)){
            throw new IllegalArgumentException("Sequence name required");
        }
        Long result = null;
        String query = " SELECT nextval(:sequence_name)";

        try{
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("sequence_name", QueryUtil.convertToQueryParam(sequenceName));
            result = getJdbcTemplate().queryForLong(query, params);
        } catch(Exception e){
            throw new JdbcException("Failed to get next sequence value for seq '" + sequenceName + "'", e);
        }
        
        return result;
    }

    public SqlRowSet queryForRowSet(String query, MapSqlParameterSource paramMap) throws JdbcException{
        SqlRowSet rs = getJdbcTemplate().queryForRowSet(query, paramMap);
        
        return rs;
    }

    public List<Map<String, Object>> queryForList(String query, MapSqlParameterSource paramMap) throws JdbcException{
        List<Map<String,Object>> rs = getJdbcTemplate().queryForList(query, paramMap);
        
        return rs;
    }

    public int queryForCount(String query, MapSqlParameterSource paramMap) throws JdbcException{
        int result = 0;
        try {
            result = getJdbcTemplate().queryForInt(query, paramMap);
        } catch (Exception e){
            throw new JdbcException("Failed to execute count query", e);
        }
        return result;
    }
    
    /**
     * Returns table coumn names.
     * 
     * @return {@link String}[] column names
     */
    protected String[] getSortColumns() {
        throw new IllegalAccessError("Not implemented!");
    }
    
    /**
     * Reeturns default sort column number
     * 
     * @return default sort column number
     */
    protected int getDefaultSortColNum() {
        throw new IllegalAccessError("Not implemented!");
    }
    
    /**
     * Reeturns default sort order
     * 
     * @return default sort order
     */
    protected boolean isDefaultSortAsc() {
        throw new IllegalAccessError("Not implemented!");
    }
    
    /**
     * Returns sort column name with col number check
     * 
     * @param colNum int - sort col number
     * 
     * @return {@link String} sort column name
     */
    protected String getSortColumn(int colNum){
        String result = null;
        int col = colNum;
        if(col <0 || col >= getSortColumns().length){
            col = getDefaultSortColNum();
        }
        result = getSortColumns()[col];
        return result;
    }
    
    public UrTSortHelper getSortHelper() {
        return getSortHelper(getDefaultSortColNum(), isDefaultSortAsc());
    }

    public UrTSortHelper getSortHelper(int column, boolean isAsc) {
        int col = column;
        if(col <0 || col >= getSortColumns().length){
            col = getDefaultSortColNum();
        }
        return new UrTSortHelper(col, isAsc, getSortColumns().length);
    }

    
}
