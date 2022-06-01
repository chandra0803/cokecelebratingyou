/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/budget/BudgetDAOSuite.java,v $
 */

package com.biperf.core.dao.budget;

import com.biperf.core.dao.budget.hibernate.BudgetMasterDAOImplBudgetSegmentTest;
import com.biperf.core.dao.budget.hibernate.BudgetMasterDAOImplBudgetTest;
import com.biperf.core.dao.budget.hibernate.BudgetMasterDAOImplTest;
import com.biperf.core.dao.budget.limits.BooleanCharacteristicConstraintLimitsTest;
import com.biperf.core.dao.budget.limits.DateCharacteristicConstraintLimitsTest;
import com.biperf.core.dao.budget.limits.DecimalCharacteristicConstraintLimitsTest;
import com.biperf.core.dao.budget.limits.IntegerCharacteristicConstraintLimitsTest;
import com.biperf.core.dao.budget.limits.MultiSelectCharacteristicConstraintLimitsTest;
import com.biperf.core.dao.budget.limits.SingleSelectCharacteristicConstraintLimitsTest;
import com.biperf.core.dao.budget.limits.TextCharacteristicConstraintLimitsTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * BudgetDAOSuite is a container for running all Budget DAO tests out of container.
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
 * <td>Sathish</td>
 * <td>May 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetDAOSuite extends TestSuite
{

  /**
   * suite to run all Hierarchy DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.budget.hibernate.BudgetDAOSuite" );

    suite.addTestSuite( BudgetMasterDAOImplBudgetTest.class );
    suite.addTestSuite( BudgetMasterDAOImplBudgetSegmentTest.class );
    suite.addTestSuite( BudgetMasterDAOImplTest.class );
    suite.addTestSuite( BooleanCharacteristicConstraintLimitsTest.class );
    suite.addTestSuite( DateCharacteristicConstraintLimitsTest.class );
    suite.addTestSuite( DecimalCharacteristicConstraintLimitsTest.class );
    suite.addTestSuite( IntegerCharacteristicConstraintLimitsTest.class );
    suite.addTestSuite( MultiSelectCharacteristicConstraintLimitsTest.class );
    suite.addTestSuite( SingleSelectCharacteristicConstraintLimitsTest.class );
    suite.addTestSuite( TextCharacteristicConstraintLimitsTest.class );

    return suite;
  }

}
