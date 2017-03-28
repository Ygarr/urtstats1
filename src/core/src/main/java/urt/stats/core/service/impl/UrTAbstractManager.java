package urt.stats.core.service.impl;

import urt.stats.core.dao.UrTDao;
import urt.stats.core.exception.JdbcException;
import urt.stats.core.service.UrTManager;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * Abstract UrT manager implementation
 * @author ghost
 *
 */
public abstract class UrTAbstractManager implements UrTManager {

    /**
     * {@inheritDoc}
     */
    public SqlRowSet queryForRowSet(String query, MapSqlParameterSource paramMap) throws JdbcException{
        return getDao().queryForRowSet(query, paramMap);
    }

    /**
     * {@inheritDoc}
     */
    public List<Map<String, Object>> queryForList(String query, MapSqlParameterSource paramMap) throws JdbcException{
        return getDao().queryForList(query, paramMap);
    }

    /**
     * {@inheritDoc}
     */
    public int queryForCount(String query, MapSqlParameterSource paramMap) throws JdbcException{
        return getDao().queryForCount(query, paramMap);
    }

    /**
     * Returns main DAO class for manager implementation
     * 
     * @return {@link UrTDao} - main DAO class for manager implementation
     */
    protected abstract UrTDao getDao();
}
