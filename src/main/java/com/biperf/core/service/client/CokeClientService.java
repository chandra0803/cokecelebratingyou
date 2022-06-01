
package com.biperf.core.service.client;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.client.RecognizeAnyone;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.client.TcccApproverList;
import com.biperf.core.domain.client.TcccCurrencyExchange;
import com.biperf.core.service.SAO;
import com.biperf.core.value.ClaimRecipientValueBean;
import com.biperf.core.value.client.CokeNominationFileUploadValueBean;
import com.biperf.core.value.client.TcccLevelPayoutValueBean;

/**
 * CokeClientService.
 * 
 * @author dudam
 * @since Feb 19, 2018
 * @version 1.0
 * 
 * This service is created as part of wip #42701
 */
public interface CokeClientService extends SAO
{

  public final String BEAN_NAME = "cokeClientService";
  public static String REDEEM_AWARD_MODE_CASH = "cash";
  public static String REDEEM_AWARD_MODE_POINTS = "points";
  public static String REDEEM_AWARD_MODE_CASH_AND_POINTS = "cashAndPoints";
  // Client customization for WIP 58122
  public static String REDEEM_AWARD_MODE_TRAINING = "training";
  public static String DECLINE_AWARD = "decline";

  // Client customization for WIP #42701 starts
  /*
   * public TcccApproverMatrix getLevelOneApprover( Long promotionId, String divisionNumber );
   * public TcccApproverMatrix getLevelTwoApprover( Long promotionId, String divisionNumber );
   * public List<TcccApproverMatrix> getLevelApprovers( Long promotionId, String divisionNumber );
   */

  public List<TcccApproverList> getApproverByParticipantLoginID( String participantLoginID );

  public TcccCurrencyExchange getCurrencyExchange( String currencyCode );

  public Double getCurrencyExchangeRate( String currencyCode );

  public Double getUserBudgetMediaValue( Long userId );
  // Client customization for WIP #42701 ends

  // Client customization for WIP #43735 starts
  public List<ClaimRecipientValueBean> getYetToClaimAwards( Long participantId );
  // Client Customization for WIP #43735 ends

  public Map launchAndProcessCokePayrollFileExtract();

  /* TCCC - customization start - wip 46975 */
  public boolean sendRecognizeAnyoneEmail( List<RecognizeAnyone> recognizeAnyoneList );
  /* TCCC - customization end */

  // Client customization start - WIP 58122
  public List<TccNomLevelPayout> getLevelTotalPoints( Long promotionId );

  public TccNomLevelPayout save( TccNomLevelPayout tccNomLevelPayout );

  public List<TcccLevelPayoutValueBean> getLevelPayoutByPromotionId( String promotionId );

  public TccNomLevelPayout getLevelPayoutById( Long levelPayoutId );
  // Client customization end - WIP 58122

  // Client customization for WIP #59420 starts
  public CokeNominationFileUploadValueBean getNominationFileUploadDetails( Long promotionId );
  // Client Customization for WIP #59420 ends

}
