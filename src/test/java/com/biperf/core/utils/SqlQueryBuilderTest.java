/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/SqlQueryBuilderTest.java,v $
 */

package com.biperf.core.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * SqlQueryBuilderTest.
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
public class SqlQueryBuilderTest extends TestCase
{

  public void testAppendSimpleQuerySingleClause()
  {
    String expectedQuery = "SELECT id \nFROM\nfoo WHERE\nfoo_1=\":foo1\" \n";

    SqlQueryBuilder builder = new SqlQueryBuilder();
    builder.appendSimpleQuery( "id", "foo", "foo_1=\":foo1\"" );
    assertEquals( expectedQuery, builder.toString() );
  }

  public void testAppendSimpleQueryTwoClauses()
  {
    String expectedQuery = "SELECT id \nFROM\nfoo WHERE\nfoo_1=\":foo1\"\n AND foo_2=\":foo2\" \n";

    SqlQueryBuilder builder = new SqlQueryBuilder();
    List clauses = new ArrayList();
    clauses.add( "foo_1=\":foo1\"" );
    clauses.add( "foo_2=\":foo2\"" );

    builder.appendSimpleQuery( "id", "foo", clauses );
    assertEquals( expectedQuery, builder.toString() );
  }

}
