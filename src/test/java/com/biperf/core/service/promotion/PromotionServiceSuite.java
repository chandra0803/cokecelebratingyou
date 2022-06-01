/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/promotion/PromotionServiceSuite.java,v $
 */

package com.biperf.core.service.promotion;

import com.biperf.core.service.promotion.impl.NominationPromotionServiceImplTest;
import com.biperf.core.service.promotion.impl.PayoutProcessingServiceImplTest;
import com.biperf.core.service.promotion.impl.PromoMerchCountryServiceImplTest;
import com.biperf.core.service.promotion.impl.PromotionEngineServiceImplTest;
import com.biperf.core.service.promotion.impl.PromotionNotificationServiceImplTest;
import com.biperf.core.service.promotion.impl.PromotionServiceImplTest;
import com.biperf.core.service.promotion.impl.PromotionSweepstakeServiceImplTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Promotion Service test suite for running all promotion service tests out of container.
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
public class PromotionServiceSuite extends TestSuite
{

  /**
   * Promotion Service Test Suite
   * 
   * @return Test
   */
  public static Test suite()
  {
    TestSuite suite = new TestSuite( "com.biperf.core.service.promotion.PromotionServiceSuite" );

    suite.addTestSuite( PromotionServiceImplTest.class );
    suite.addTestSuite( PromotionEngineServiceImplTest.class );
    suite.addTestSuite( PromotionNotificationServiceImplTest.class );
    suite.addTestSuite( PayoutProcessingServiceImplTest.class );
    suite.addTestSuite( PromotionBehaviorUpdateAssociationTest.class );
    // suite.addTestSuite( PromotionSweepstakesUpdateAssociationTest.class );
    suite.addTestSuite( PromotionSweepstakeServiceImplTest.class );
    suite.addTestSuite( PromoMerchCountryServiceImplTest.class );
    suite.addTestSuite( NominationPromotionServiceImplTest.class );

    return suite;
  }

}
