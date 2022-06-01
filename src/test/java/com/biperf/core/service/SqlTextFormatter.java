/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/activity/impl/ActivityServiceImplTest.java,v $
 */

package com.biperf.core.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

/*
 * ActivityServiceImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Jul
 * 14, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class SqlTextFormatter extends TestCase
{
  public void testGeneratesql()
  {
    try
    {
      FileReader fr = new FileReader( "C:\\Users\\sedey\\Desktop\\input.txt" );
      BufferedReader br = new BufferedReader( fr );
      String s;
      StringBuffer sql = new StringBuffer();
      sql.append( "StringBuffer sql = new StringBuffer();" );
      while ( ( s = br.readLine() ) != null )
      {
        sql.append( "sql.append( \" " );
        sql.append( s );
        sql.append( " \" ); " );
      }
      sql.append( "return sql.toString();" );
      System.out.println( sql.toString() );
    }
    catch( FileNotFoundException e )
    {
      e.printStackTrace();
    }
    catch( IOException e )
    {
      e.printStackTrace();
    }

  }

}
