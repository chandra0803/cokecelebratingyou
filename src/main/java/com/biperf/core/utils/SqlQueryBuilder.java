/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * SqlQueryBuilder.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>wadzinsk</td>
 * <td>Jun 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SqlQueryBuilder
{

  private static final String SPACE = " ";
  protected StringBuffer query = new StringBuffer();

  public boolean applyIntersect = false;

  public SqlQueryBuilder append( Object object )
  {
    appendWithTrailingSpace( object );
    return this;
  }

  public SqlQueryBuilder appendSimpleQuery( String resultColumn, String tableName, String clause )
  {
    List clauses = new ArrayList();
    clauses.add( clause );
    return appendSimpleQuery( resultColumn, tableName, clauses, false );
  }

  public SqlQueryBuilder appendSimpleQueryInIntersectQuery( String resultColumn, String tableName, String clause )
  {
    List clauses = new ArrayList();
    clauses.add( clause );
    return appendSimpleQuery( resultColumn, tableName, clauses, true );
  }

  public SqlQueryBuilder appendSimpleQuery( String resultColumn, String tableName, List clauses )
  {
    return appendSimpleQuery( resultColumn, tableName, clauses, false );
  }

  public SqlQueryBuilder appendSimpleQueryInIntersectQuery( String resultColumn, String tableName, List clauses )
  {
    return appendSimpleQuery( resultColumn, tableName, clauses, true );
  }

  private SqlQueryBuilder appendSimpleQuery( String resultColumn, String tableName, List clauses, boolean inIntersectQuery )
  {
    if ( inIntersectQuery && applyIntersect )
    {
      // Only append intersect if we have already written a simpleQuery marked as inIntersectQuery
      appendIntersect();
    }
    append( "SELECT" );
    append( resultColumn );
    appendWithNewLines( "FROM" );
    append( tableName );
    if ( clauses != null && clauses.size() > 0 )
    {
      appendWithNewLine( "WHERE" );
      append( StringUtils.join( clauses.iterator(), "\n AND " ) );
    }
    if ( inIntersectQuery )
    {
      applyIntersect = true;
    }
    appendNewLine();

    return this;
  }

  public SqlQueryBuilder appendSimpleQueryWithOR( String resultColumn, String tableName, List clauses, boolean inIntersectQuery )
  {
    if ( inIntersectQuery && applyIntersect )
    {
      // Only append intersect if we have already written a simpleQuery marked as inIntersectQuery
      appendIntersect();
    }
    append( "SELECT" );
    append( resultColumn );
    appendWithNewLines( "FROM" );
    append( tableName );
    if ( clauses != null && clauses.size() > 0 )
    {
      appendWithNewLine( "WHERE" );
      append( StringUtils.join( clauses.iterator(), "\n OR " ) );
    }
    if ( inIntersectQuery )
    {
      applyIntersect = true;
    }
    appendNewLine();

    return this;
  }

  /**
   * 
   */
  private void appendNewLine()
  {
    query.append( "\n" );
  }

  public SqlQueryBuilder appendIntersect()
  {
    appendWithNewLines( "INTERSECT" );
    return this;
  }

  private void appendWithTrailingSpace( Object object )
  {
    query.append( object ).append( SPACE );
  }

  /**
   * Used for "pretty printing" purposes only
   * 
   * @param object
   */
  private void appendWithNewLines( Object object )
  {
    query.append( "\n" ).append( object ).append( "\n" );
  }

  /**
   * Used for "pretty printing" purposes only
   * 
   * @param object
   */
  private void appendWithNewLine( Object object )
  {
    query.append( object ).append( "\n" );

  }

  /**
   * Return the built query as a string.
   * 
   * @return String
   */
  public String toString()
  {
    return query.toString();
  }

}
