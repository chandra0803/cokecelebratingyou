/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.oracle.impl;

import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.oracle.OracleSequenceDAO;

/**
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
 * <td>Dec 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class OracleSequenceDAOImpl extends BaseDAO implements OracleSequenceDAO
{

  /**
   * Get the value of the sequence next value. Overridden from
   * 
   * @see com.biperf.core.dao.oracle.OracleSequenceDAO#getOracleSequenceNextValue(java.lang.String)
   * @param sequenceName
   */
  public long getOracleSequenceNextValue( String sequenceName )
  {

    SQLQuery sqlQuery = getSession().createSQLQuery( "SELECT " + sequenceName + ".nextval as nextValue from dual" );
    sqlQuery = sqlQuery.addScalar( "nextValue", StandardBasicTypes.LONG );

    Long nextVal = (Long)sqlQuery.uniqueResult();

    return nextVal.longValue();
  }

}
