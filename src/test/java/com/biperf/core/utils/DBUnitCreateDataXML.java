/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/utils/DBUnitCreateDataXML.java,v $
 */

package com.biperf.core.utils;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;

/**
 * DBUnitCreateDataXML.
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
 * <td>tennant</td>
 * <td>Sep 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class DBUnitCreateDataXML
{

  public static void main( String[] args ) throws Exception
  {
    // database connection
    // Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");

    // The FindBugs warning here was examined and deemed non-hazardous
    Connection jdbcConnection = DriverManager.getConnection( "jdbc:oracle:thin:@localhost:1521:beacon", "BEACON", "BEACON" );
    IDatabaseConnection connection = new DatabaseConnection( jdbcConnection, "BEACON" );

    // partial database export
    QueryDataSet partialDataSet = new QueryDataSet( connection );
    // partialDataSet.addTable("STAGE_PAX_IMPORT_RECORD", "SELECT * FROM STAGE_PAX_IMPORT_RECORD
    // WHERE COL='VALUE'");
    // partialDataSet.addTable("STAGE_PAX_IMPORT_RECORD");
    partialDataSet.addTable( "COUNTRY" );
    FlatXmlDataSet.write( partialDataSet, new FileOutputStream( "/partial.xml" ) );

    // full database export
    IDataSet fullDataSet = connection.createDataSet();
    FlatXmlDataSet.write( fullDataSet, new FileOutputStream( "/full.xml" ) );
  }
}
