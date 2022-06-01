
package com.biperf.core.dao.client;

import java.util.List;
import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.client.RecognizeAnyone;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.client.TcccApproverList;
import com.biperf.core.domain.client.TcccCurrencyExchange;
import com.biperf.core.value.ClaimRecipientValueBean;
import com.biperf.core.value.client.CokeNominationFileUploadValueBean;
import com.biperf.core.value.client.TcccLevelPayoutValueBean;

/**
 * CokeClientDAO.
 * 
 * @author dudam
 * @since Feb 19, 2018
 * @version 1.0
 * 
 * This DAO is created as part of WIP #42701 
 */
public interface CokeClientDAO extends DAO
{
  public static final String BEAN_NAME = "cokeClientDAO";

  // Client customization for WIP #42701 starts
  public String getApproverUserName( Long promotionId, String divisionNumber );
  
  // Client customization for WIP 58122
  public String getApproverUserNameForLevel( Long promotionId, String divisionNumber,Long level );

  //public List<TcccApproverMatrix> getLevelApprovers( Long promotionId, String divisionNumber );
  
  
  public List<TcccApproverList> getApproverByParticipantLoginID( String participantLoginID );
  
  public TcccCurrencyExchange getCurrencyExchange( String currencyCode );

  public Double getCurrencyExchangeRate( String currencyCode );
  
  public Double getUserBudgetMediaValue( Long userId );
  // Client customization for WIP #42701 ends
  
  // Client customization for WIP #43735 starts
  public List<ClaimRecipientValueBean> getYetToClaimAwards( Long participantId );
  
  // Client customization for WIP 58122
  public List<ClaimRecipientValueBean> getYetToClaimAwardsForNomination( Long participantId );
  // Client customization for WIP #43735 ends
  public Map launchAndProcessCokePayrollFileExtract();

  /* TCCC  - customization start - wip 46975 */
  void saveRecognizeAnyoneEmail(RecognizeAnyone recognizeAnyone);
  /* TCCC  - customization end */
  
  // Client customization start - WIP 58122
  public List<TccNomLevelPayout> getLevelTotalPoints( Long promotionId );

  public TccNomLevelPayout save( TccNomLevelPayout tccNomLevelPayout );

  public List<TcccLevelPayoutValueBean> getLevelPayoutByPromotionId( String promotionId );

  public TccNomLevelPayout getLevelPayoutById( Long levelPayoutId );
  // Client customization end - WIP 58122

  // Client customization for WIP #59420 starts
  public CokeNominationFileUploadValueBean getNominationFileUploadDetails( Long promotionId );
  // Client customization for WIP #59420 ends
  
}
