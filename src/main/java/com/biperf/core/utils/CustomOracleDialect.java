/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/utils/CustomOracleDialect.java,v $
 *
 */

package com.biperf.core.utils;

import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * CustomOracleDialect <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>May 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CustomOracleDialect extends Oracle10gDialect
{
  /**
   * We need to add level to the oracle dialect.
   */
  public CustomOracleDialect()
  {
    super();
    registerFunction( "level", new NoArgSQLFunction( "level", StandardBasicTypes.LONG, false ) );
    registerFunction( "FNC_JAVA_DECRYPT", new StandardSQLFunction( "FNC_JAVA_DECRYPT", StandardBasicTypes.STRING ) );
    registerFunction( "FNC_JAVA_ENCRYPT", new StandardSQLFunction( "FNC_JAVA_ENCRYPT", StandardBasicTypes.STRING ) );
  }
}
