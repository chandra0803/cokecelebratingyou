/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.oracle;

import com.biperf.core.dao.DAO;

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
public interface OracleSequenceDAO extends DAO
{

  public static final String BEAN_NAME = "oracleSequenceDAO";

  /**
   * Get the value of the sequence next value.
   */
  public long getOracleSequenceNextValue( String sequenceName );

}
