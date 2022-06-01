/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/promotion/PromotionDAOSuite.java,v $
 */

package com.biperf.core.dao.promotion;

import com.biperf.core.dao.promotion.hibernate.PromoMerchCountryDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionNotificationDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionParticipantDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionPayoutDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PromotionSweepstakeDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.PublicRecognitionDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.StackRankDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.StackRankNodeDAOImplTest;
import com.biperf.core.dao.promotion.hibernate.StackRankParticipantDAOImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * PromotionDAOSuite is a container for running all Promotion DAO tests out of container.
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
 * <td>June 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionDAOSuite extends TestSuite
{

  /**
   * suite to run all Promotion DAO Tests.
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.dao.promotion.PromotionDAOSuite" );

    suite.addTestSuite( PromotionDAOImplTest.class );
    suite.addTestSuite( PromotionNotificationDAOImplTest.class );
    suite.addTestSuite( PromotionParticipantDAOImplTest.class );
    suite.addTestSuite( PromotionPayoutDAOImplTest.class );
    suite.addTestSuite( PromotionSweepstakeDAOImplTest.class );
    suite.addTestSuite( StackRankDAOImplTest.class );
    suite.addTestSuite( StackRankParticipantDAOImplTest.class );
    suite.addTestSuite( StackRankNodeDAOImplTest.class );
    suite.addTestSuite( PromoMerchCountryDAOImplTest.class );
    suite.addTestSuite( PublicRecognitionDAOImplTest.class );

    return suite;
  }

}
