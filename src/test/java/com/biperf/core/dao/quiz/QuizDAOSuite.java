/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/quiz/QuizDAOSuite.java,v $
 */

package com.biperf.core.dao.quiz;

import com.biperf.core.dao.quiz.hibernate.QuizDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * QuizDAOSuite.
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
 * <td>crosenquest</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizDAOSuite extends TestSuite
{

  /**
   * suite to run all Promotion DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.quiz.QuizDAOSuite" );

    suite.addTestSuite( QuizDAOImplTest.class );

    return suite;
  }

}
