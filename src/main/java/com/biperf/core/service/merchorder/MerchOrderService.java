
package com.biperf.core.service.merchorder;

import java.util.List;

import com.biperf.awardslinqDataRetriever.client.MerchLevel;
import com.biperf.core.dao.merchandise.hibernate.MerchOrderActivityQueryConstraint;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ProjectionCollection;
import com.biperf.core.service.SAO;
import com.biperf.core.service.awardbanq.GiftCodes;
import com.biperf.core.value.DepositProcessBean;

public interface MerchOrderService extends SAO
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * Static final for the BEAN_NAME for use in the applicationContext.xml
   */
  public static final String BEAN_NAME = "merchOrderService";

  /**
   * returns a list of MerchOrder objects based on the criteria but does
   * NOT re-evaluate the status in the OM system.
   * 
   * @param constraint
   * @return List
   */
  public List getMerchOrderList( MerchOrderActivityQueryConstraint constraint );

  /**
   * returns a list of MerchOrder that have not been redeemed for a promotion.
   * This method will find all the unredeemed gift codes in G3, check the status in OM,
   * and update the status if necessary.  If the code has not been redeemed, it is returned
   * as part of the collection of MerchOrder objects
   * 
   * @param constraint
   * @return List
   */
  public List getUnredeemedMerchOrdersAndUpdateStatus( MerchOrderActivityQueryConstraint constraint ) throws ServiceErrorException;

  /**
   * checks OM for redemption status and updates G3 tables if necessary
   * 
   * @param code
   */
  public MerchOrder updateOrderStatus( MerchOrder code ) throws ServiceErrorException;

  /**
   * returns a list of MerchAwardReminderBean that have not been redeemed for a participant.
   * 
   * @param participantId
   * @return List
   */
  public List getMerchAwardReminders( Long participantId );

  /**
   * Get (single sign-on) online shopping URL.
   * 
   * @param promotion
   * @param merchOrderId
   * @param country
   * @param participantId
   * @param doubleEncode
   * @return online shopping URL
   */
  public String getOnlineShoppingUrl( Promotion promotion, Long merchOrderId, Country country, Participant participant, boolean doubleEncode );

  public String getOnlineShoppingProductDetailUrl( Promotion promotion, Long merchOrderId, Country country, Participant participant, boolean doubleEncode, String productSetId, String catalogId );

  public MerchOrder getMerchOrderByGiftCode( String giftCode );

  public MerchOrder getMerchOrderByReferenceNumber( String referenceNumber );

  /**
   * Save the replacement gift code and send email to participant
   * 
   * @param merchOrderId
   * @param programId
   * @param giftCode
   * @param emailAddress
   * @param message
   * @throws ServiceErrorException
   */
  public void replaceGiftCodeAndSendEmail( Long merchOrderId, String programId, String giftCode, String emailAddress, String message ) throws ServiceErrorException;

  public MerchOrder getMerchOrderById( Long merchOrderId );

  // Alerts Performance Tuning
  public MerchOrder getMerchOrderByIdWithProjections( Long merchOrderId, ProjectionCollection collection );

  public MerchOrder getMerchOrderByClaimIdUserId( Long userId, Long claimId );

  public MerchOrder saveMerchOrder( MerchOrder merchOrder );

  public PlateauRedemptionTracking savePlateauRedemptionTracking( PlateauRedemptionTracking plateauRedemptionTracking );

  public Long getNextBatchId();

  public List<Long> getGiftCodeFailures( Long promotionId ); // Bug 66870

  public MerchOrder processGiftCodes( MerchOrder merchOrder, List<GiftCodes> giftCodes, boolean isLevel, DepositProcessBean depositProcessBean, MerchLevel omLevel );

  public Mailing buildMerchRecognitionMailing( Long claimId );

  public Mailing buildMerchCelebrationMailing( Long claimId );

  public Long getPromotionIdByMerchOrderId( Long merchOrderId );

  public List<Long> getMerchOrderIds();

  public List getMerchOrdersList( Long merchOrderId );
  
  //Client customizations for wip #23129 starts
  public List getAllUnredeemedOrdersByPromotion( Long promoId, String mmyyyy );
 
  public MerchOrder updateOrderBillingCodeAndRedeem( Long merchOrderId, String billingCode );
  // Client customizations for wip #23129 ends

}
