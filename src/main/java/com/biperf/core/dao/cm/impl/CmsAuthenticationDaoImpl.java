/*
 * (c) 2014 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/cm/impl/CmsAuthenticationDaoImpl.java,v $
 */

package com.biperf.core.dao.cm.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.biperf.core.dao.cm.CmsAuthenticationDao;
import com.objectpartners.cms.domain.CmsUser;

/**
 * 
 * @author kothanda
 * @since Dec 8, 2014
 * @version 1.0
 */
public class CmsAuthenticationDaoImpl implements CmsAuthenticationDao
{

  private JdbcTemplate jdbcTemplate;

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public CmsUser getUser( String username )
  {
    String query = "select u.user_id, u.user_name, u.password, u.is_cms_terms_accepted, u.user_type from application_user u where upper(u.user_name) = ?";

    Object[] params = { toUpper( username ) };

    RowMapper mapper = new RowMapper()
    {

      public Object mapRow( ResultSet rs, int index ) throws SQLException
      {
        CmsUser user = new CmsUser();
        user.setUserId( rs.getLong( "user_id" ) );
        user.setUsername( rs.getString( "user_name" ) );
        user.setPassword( rs.getString( "password" ) );
        user.setTermsAccepted( rs.getBoolean( "is_cms_terms_accepted" ) );
        user.setUserType( rs.getString( "user_type" ) );
        return user;
      }
    };

    CmsUser user = null;

    try
    {
      user = (CmsUser)jdbcTemplate.queryForObject( query, params, mapper );
    }
    catch( EmptyResultDataAccessException e )
    {
      // do nothing, not found; return null
    }

    return user;
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public List<String> getRoles( Long userId )
  {
    String query = "select code from role where role_id in (select role_id from user_role where user_id = ?)";

    Object[] params = { userId };
    RowMapper mapper = new RowMapper()
    {

      public Object mapRow( ResultSet rs, int index ) throws SQLException
      {
        return rs.getString( "code" );
      }
    };

    List<String> roles = null;

    try
    {
      roles = jdbcTemplate.query( query, params, mapper );
    }
    catch( EmptyResultDataAccessException e )
    {
      e.printStackTrace();
      // do nothing, not found; return null
    }

    return roles;
  }

  public void updateUserTermsAndConditions( int acceptance, long userId )
  {
    String query = "update application_user set is_cms_terms_accepted = ? where user_id = ?";

    Object[] params = { acceptance, userId };

    try
    {
      jdbcTemplate.update( query, params );
    }
    catch( DataAccessException e )
    {
      e.printStackTrace();
    }
  }

  private String toUpper( String input )
  {
    return input == null ? null : input.toUpperCase();
  }

  public void setDataSource( DataSource dataSource )
  {
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

}
