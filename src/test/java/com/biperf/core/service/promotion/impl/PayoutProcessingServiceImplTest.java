/**
 * 
 */

package com.biperf.core.service.promotion.impl;

import org.jmock.Mock;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.promotion.PromotionEngineService;
import com.biperf.core.service.promotion.PromotionService;

/**
 * PayoutProcessingServiceTest.
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
 * <td>asondgeroth</td>
 * <td>Jul 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutProcessingServiceImplTest extends BaseServiceTest
{
  private PayoutProcessingServiceImpl payoutProcessingServiceImpl = new PayoutProcessingServiceImpl();

  private Mock participantDAOMock;

  private Mock activityDAOMock;
  private Mock audienceServiceMock;

  private Mock promotionEngineServiceMock;
  private Mock promotionServiceMock;

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public PayoutProcessingServiceImplTest( String test )
  {
    super( test );
  }

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    participantDAOMock = new Mock( ParticipantDAO.class );
    payoutProcessingServiceImpl.setParticipantDAO( (ParticipantDAO)participantDAOMock.proxy() );

    audienceServiceMock = new Mock( AudienceService.class );
    payoutProcessingServiceImpl.setAudienceService( (AudienceService)audienceServiceMock.proxy() );

    activityDAOMock = new Mock( ActivityDAO.class );
    payoutProcessingServiceImpl.setActivityDAO( (ActivityDAO)activityDAOMock.proxy() );

    promotionEngineServiceMock = new Mock( PromotionEngineService.class );
    payoutProcessingServiceImpl.setPromotionEngineService( (PromotionEngineService)promotionEngineServiceMock.proxy() );

    promotionServiceMock = new Mock( PromotionService.class );
    payoutProcessingServiceImpl.setPromotionService( (PromotionService)promotionServiceMock.proxy() );

  }

  /**
   * Test getting the claimFormStepElement validation for the id param.
   */
  public void testProcessClaim()
  {
    /*
     * Node node1 = new Node(); node1.setId( new Long( 1 ) ); node1.setName( "testNAME-ABC" );
     * node1.setDescription( "testDESCRIPTION-ABC" ); Participant pax1 = new Participant();
     * pax1.setId( new Long(1) ); pax1.addUserNode(new UserNode(pax1, node1)); ProductClaimPromotion
     * masterPromotion = buildProductClaimPromotion("323");
     * masterPromotion.setPromotionStatus(PromotionStatusType.lookup(PromotionStatusType.LIVE));
     * ProductClaimPromotion childPromotion1 = buildProductClaimPromotion("323-child1");
     * childPromotion1.setPromotionStatus(PromotionStatusType.lookup(PromotionStatusType.LIVE));
     * ProductClaimPromotion childPromotion2 = buildProductClaimPromotion("323-child2");
     * childPromotion2.setPromotionStatus(PromotionStatusType.lookup(PromotionStatusType.
     * UNDER_CONSTRUCTION)); masterPromotion.addChildPromotion(childPromotion1);
     * masterPromotion.addChildPromotion(childPromotion2); ProductClaim claim = new ProductClaim();
     * claim.setId( new Long(1) ); claim.setPromotion(masterPromotion); claim.setNode( node1 );
     * claim.setSubmitter( pax1 ); Participant pax2 = new Participant(); pax2.setId( new Long(2) );
     * ProductClaimParticipant claimParticipant = new ProductClaimParticipant();
     * claimParticipant.setParticipant(pax2); claim.addClaimParticipant(claimParticipant);
     * promotionServiceMock.expects(atLeastOnce()).method("isParticipantMemberOfPromotionAudience").
     * will(returnValue(true));
     * activityServiceMock.expects(atLeastOnce()).method("createActivitiesForProductClaim").with(
     * same(claim)).will( returnValue( new ArrayList() ) );
     * activityServiceMock.expects(atLeastOnce()).method("saveActivities").with(eq(new
     * ArrayList()));
     * promotionEngineServiceMock.expects(atLeastOnce()).method("calculatePayoutAndSaveResults").
     * with(isA(SalesFacts.class), same(masterPromotion), isA(Participant.class),
     * same(masterPromotion.getPayoutType()));
     * promotionEngineServiceMock.expects(atLeastOnce()).method("calculatePayoutAndSaveResults").
     * with(isA(SalesFacts.class), same(childPromotion1), isA(Participant.class),
     * same(childPromotion1.getPayoutType()));
     * payoutProcessingServiceImpl.processProductClaim(claim);
     */
  }

  public static ProductClaimPromotion buildProductClaimPromotion( String suffix )
  {
    return PromotionDAOImplTest.buildProductClaimPromotion( suffix );
  }
}
