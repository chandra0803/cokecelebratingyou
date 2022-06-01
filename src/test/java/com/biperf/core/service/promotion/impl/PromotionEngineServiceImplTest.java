/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/promotion/impl/PromotionEngineServiceImplTest.java,v $
 */

package com.biperf.core.service.promotion.impl;

import java.util.LinkedHashSet;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.hibernate.Session;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.promotion.engine.ActivityLoader;
import com.biperf.core.service.promotion.engine.ActivityLoaderFactory;
import com.biperf.core.service.promotion.engine.PayoutCalculationFacts;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.promotion.engine.PayoutStrategy;
import com.biperf.core.service.promotion.engine.PayoutStrategyFactory;
import com.biperf.core.service.promotion.engine.RecognitionFacts;

/**
 * PromotionEngineServiceImplTest NOTE: Made this an integration test (loads the spring context) so
 * that we can do the end-to-end wiring outside of the container while we are first implementing the
 * rules engine within Spring.
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
 * <td>tom</td>
 * <td>Jul 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionEngineServiceImplTest extends BaseServiceTest
{
  private IMocksControl mockControl;
  private PromotionEngineServiceImpl engine;
  private Session mockSession;

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    mockControl = EasyMock.createControl();
    mockSession = mockControl.createMock( Session.class );
    engine = new PromotionEngineServiceImpl()
    {
      protected Session getHibernateSession()
      {
        return mockSession;
      }
    };
  }

  @Override
  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  // Recognition Type
  public void testCalculatePayout()
  {
    Claim claim = new RecognitionClaim();
    Participant participant = new Participant();

    PayoutCalculationFacts facts = new RecognitionFacts( claim, participant );
    Promotion promotion = new RecognitionPromotion();
    String payoutType = PromotionType.RECOGNITION;

    ActivityLoaderFactory mockActivityLoaderFactory = mockControl.createMock( ActivityLoaderFactory.class );
    ActivityLoader mockActivityLoader = mockControl.createMock( ActivityLoader.class );

    Set<Activity> activities = new LinkedHashSet<Activity>();

    PayoutStrategyFactory mockPayoutStrategyFactory = mockControl.createMock( PayoutStrategyFactory.class );
    PayoutStrategy mockPayoutStrategy = mockControl.createMock( PayoutStrategy.class );

    engine.setActivityLoaderFactory( mockActivityLoaderFactory );
    engine.setPayoutStrategyFactory( mockPayoutStrategyFactory );

    EasyMock.expect( mockActivityLoaderFactory.getActivityLoader( payoutType ) ).andReturn( mockActivityLoader );
    EasyMock.expect( mockActivityLoader.loadActivities( facts, promotion ) ).andReturn( activities );
    EasyMock.expect( mockPayoutStrategyFactory.getPayoutStrategy( payoutType ) ).andReturn( mockPayoutStrategy );
    EasyMock.expect( mockPayoutStrategy.processActivities( activities, promotion, facts ) ).andReturn( new LinkedHashSet<PayoutCalculationResult>() );
    mockSession.flush();
    EasyMock.expectLastCall();

    mockControl.replay();
    engine.calculatePayout( facts, promotion, payoutType );
    mockControl.verify();
  }

  // protected PromotionEngineService getPromotionEngineService()
  // {
  // return (PromotionEngineService)ApplicationContextFactory.getApplicationContext()
  // .getBean( "promotionEngineService" );
  // }

}
