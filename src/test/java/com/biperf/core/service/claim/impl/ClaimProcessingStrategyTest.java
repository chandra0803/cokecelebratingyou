/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/claim/impl/ClaimProcessingStrategyTest.java,v $
 */

package com.biperf.core.service.claim.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;

import com.biperf.core.dao.claim.MinimumQualifierStatusDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.enums.PromotionProcessingModeType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.claim.ProductClaimProcessingStrategy;
import com.biperf.core.service.claim.RecognitionClaimProcessingStrategy;
import com.biperf.core.service.journal.JournalService;
import com.biperf.core.service.participant.impl.ParticipantServiceImplTest;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.BudgetCalculator;

/**
 * ClaimProcessingStrategyTest.
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
 * <td>zahler</td>
 * <td>Oct 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimProcessingStrategyTest extends BaseServiceTest
{
  private ProductClaimProcessingStrategy productClaimProcessingStrategy;
  private RecognitionClaimProcessingStrategy recognitionClaimProcessingStrategy;

  private ActivityService activityServiceMock;
  private PromotionEngineService promotionEngineServiceMock;
  private JournalService journalServiceMock;
  private PromotionService promotionServiceMock;
  private ParticipantDAO participantDAOMock;
  private MinimumQualifierStatusDAO minQualifierStatusDAOMock;
  private BudgetCalculator budgetCalculatorMock;

  /**
   * Setup the mock objects. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  public void setUp() throws Exception
  {
    super.setUp();

    activityServiceMock = EasyMock.createMock( ActivityService.class );
    promotionEngineServiceMock = EasyMock.createMock( PromotionEngineService.class );
    journalServiceMock = EasyMock.createMock( JournalService.class );
    promotionServiceMock = EasyMock.createMock( PromotionService.class );
    participantDAOMock = EasyMock.createMock( ParticipantDAO.class );
    minQualifierStatusDAOMock = EasyMock.createMock( MinimumQualifierStatusDAO.class );
    budgetCalculatorMock = EasyMock.createMock( BudgetCalculator.class );

    recognitionClaimProcessingStrategy = new RecognitionClaimProcessingStrategy();
    recognitionClaimProcessingStrategy.setActivityService( activityServiceMock );
    recognitionClaimProcessingStrategy.setPromotionEngineService( promotionEngineServiceMock );
    recognitionClaimProcessingStrategy.setJournalService( journalServiceMock );
    recognitionClaimProcessingStrategy.setPromotionService( promotionServiceMock );

    productClaimProcessingStrategy = new ProductClaimProcessingStrategy();
    productClaimProcessingStrategy.setActivityService( activityServiceMock );
    productClaimProcessingStrategy.setPromotionEngineService( promotionEngineServiceMock );
    productClaimProcessingStrategy.setJournalService( journalServiceMock );
    productClaimProcessingStrategy.setPromotionService( promotionServiceMock );
    productClaimProcessingStrategy.setParticipantDAO( participantDAOMock );
    productClaimProcessingStrategy.setMinimumQualifierStatusDAO( minQualifierStatusDAOMock );
    productClaimProcessingStrategy.setBudgetCalculator( budgetCalculatorMock );
  }

  /**
   * Testing the processClaim method in the ProductClaimProcessingStrategy.
   */
  public void testProcessProductClaim() throws ServiceErrorException
  {
    ProductClaim claim = buildProductClaim();

    EasyMock.expect( promotionServiceMock.isParticipantMemberOfPromotionAudience( claim.getSubmitter(), claim.getPromotion(), true, claim.getNode() ) ).andReturn( true );

    List<Activity> activities = new ArrayList<>();
    final Activity activity = new SalesActivity();
    final String GUID = "abc123";
    activity.setGuid( GUID );
    activities.add( activity );
    EasyMock.expect( activityServiceMock.saveActivities( EasyMock.anyObject() ) ).andReturn( activities );
    EasyMock.replay( activityServiceMock );

    EasyMock.expect( promotionServiceMock.isPromotionClaimableByParticipant( EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( true );

    EasyMock.expect( promotionServiceMock.isParticipantMemberOfPromotionAudience( claim.getSubmitter(), claim.getPromotion(), true, claim.getNode() ) ).andReturn( true );
    EasyMock.replay( promotionServiceMock );

    ProductClaimPromotion promotion = (ProductClaimPromotion)claim.getPromotion();
    promotion.setPromotionProcessingMode( PromotionProcessingModeType.lookup( PromotionProcessingModeType.REAL_TIME ) );
    EasyMock.expect( promotionEngineServiceMock.calculatePayoutAndSaveResults( EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject(), EasyMock.anyObject() ) )
        .andReturn( new LinkedHashSet<>() );

    promotionEngineServiceMock.depositApprovedPayouts( EasyMock.anyObject() );
    EasyMock.expectLastCall().once();

    EasyMock.replay( promotionEngineServiceMock );

    EasyMock.replay( journalServiceMock );

    final Map<Object, Object> minQualifierStatusByPromoPayoutGroup = new HashMap<>();
    EasyMock.expect( minQualifierStatusDAOMock.getMinQualifierStatusByPromoPayoutGroup( EasyMock.anyObject(), EasyMock.anyObject() ) ).andReturn( minQualifierStatusByPromoPayoutGroup );
    EasyMock.replay( minQualifierStatusDAOMock );

    budgetCalculatorMock.calculateBudget( EasyMock.anyObject(), EasyMock.anyObject() );
    EasyMock.expectLastCall().once();
    EasyMock.replay( budgetCalculatorMock );

    productClaimProcessingStrategy.processApprovable( claim, true, null ); // Fixed as part of bug
                                                                           // #56006,55519

    EasyMock.verify( promotionServiceMock );
    EasyMock.verify( activityServiceMock );
    EasyMock.verify( promotionEngineServiceMock );
    EasyMock.verify( journalServiceMock );
    EasyMock.verify( minQualifierStatusDAOMock );
  }

  private ProductClaim buildProductClaim()
  {
    ProductClaimPromotion promotion = new ProductClaimPromotion();
    promotion.setId( new Long( 1 ) );
    promotion.setPromotionType( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );
    promotion.setPromotionStatus( PromotionStatusType.lookup( PromotionStatusType.LIVE ) );
    promotion.setPayoutType( PromotionPayoutType.lookup( PromotionPayoutType.TIERED ) );
    ClaimForm claimForm = new ClaimForm();
    claimForm.setCmAssetCode( "123456" );
    promotion.setClaimForm( claimForm );
    ProductClaim claim = new ProductClaim();
    claim.setSubmissionDate( new Date() );
    claim.setPromotion( promotion );
    claim.setSubmitter( ParticipantServiceImplTest.buildStaticParticipant() );

    ClaimProduct claimProduct = new ClaimProduct();
    claimProduct.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.APPROVED ) );

    claim.addClaimProduct( claimProduct );

    return claim;
  }
}
