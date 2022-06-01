/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/fileload/FileloadServiceSuite.java,v $
 */

package com.biperf.core.service.fileload;

import com.biperf.core.service.fileload.impl.AutoVinImportStrategyTest;
import com.biperf.core.service.fileload.impl.BudgetImportStrategyTest;
import com.biperf.core.service.fileload.impl.DepositImportStrategyTest;
import com.biperf.core.service.fileload.impl.LeaderBoardImportStrategyTest;
import com.biperf.core.service.fileload.impl.PaxBaseImportStrategyTest;
import com.biperf.core.service.fileload.impl.PaxGoalImportStrategyTest;
import com.biperf.core.service.fileload.impl.ProductClaimImportStrategyTest;
import com.biperf.core.service.fileload.impl.ProductImportStrategyTest;
import com.biperf.core.service.fileload.impl.ProgressImportStrategyTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * IntegrationService test suite for running all external integration service tests out of
 * container.
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
 * <td>jenniget</td>
 * <td>September 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class FileloadServiceSuite extends TestSuite
{

  /**
   * Fileload Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.fileload.FileloadServiceSuite" );

    suite.addTestSuite( ImportStrategyTest.class );
    suite.addTestSuite( DepositImportStrategyTest.class );
    suite.addTestSuite( BudgetImportStrategyTest.class );
    suite.addTestSuite( ProductImportStrategyTest.class );
    suite.addTestSuite( ProductClaimImportStrategyTest.class );
    suite.addTestSuite( ProgressImportStrategyTest.class );
    suite.addTestSuite( PaxBaseImportStrategyTest.class );
    suite.addTestSuite( PaxGoalImportStrategyTest.class );
    suite.addTestSuite( AutoVinImportStrategyTest.class );
    suite.addTestSuite( LeaderBoardImportStrategyTest.class );
    return suite;
  }
}