
package com.biperf.core.service.client.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.client.CokeClientDAO;
import com.biperf.core.domain.client.RecognizeAnyone;
import com.biperf.core.domain.client.TccNomLevelPayout;
import com.biperf.core.domain.client.TcccApproverList;
import com.biperf.core.domain.client.TcccCurrencyExchange;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.value.ClaimRecipientValueBean;
import com.biperf.core.value.client.CokeNominationFileUploadValueBean;
import com.biperf.core.value.client.TcccLevelPayoutValueBean;

/**
 * CokeClientServiceImpl.
 * 
 * @author dudam
 * @since Feb 19, 2018
 * @version 1.0
 * 
 * This service is created as part of WIP #42701
 */
public class CokeClientServiceImpl implements CokeClientService
{

  private CokeClientDAO cokeClientDAO;
  
  private MailingService mailingService;
  
  private static final Log logger = LogFactory.getLog( CokeClientServiceImpl.class );

  // Client customization for WIP #42701 starts
  /*
  @Override
  public TcccApproverMatrix getLevelOneApprover( Long promotionId, String divisionNumber )
  {
    return this.cokeClientDAO.getLevelApprover( promotionId, divisionNumber, 1 );
  }

  @Override
  public TcccApproverMatrix getLevelTwoApprover( Long promotionId, String divisionNumber )
  {
    return this.cokeClientDAO.getLevelApprover( promotionId, divisionNumber, 2 );
  }

  @Override
  public List<TcccApproverMatrix> getLevelApprovers( Long promotionId, String divisionNumber )
  {
    return this.cokeClientDAO.getLevelApprovers( promotionId, divisionNumber );
  }
  */

  @Override
  public List<TcccApproverList> getApproverByParticipantLoginID( String participantLoginID )
  {
    return this.cokeClientDAO.getApproverByParticipantLoginID( participantLoginID );
  }
  
  @Override
  public TcccCurrencyExchange getCurrencyExchange( String currencyCode )
  {
    return this.cokeClientDAO.getCurrencyExchange( currencyCode );
  }

  @Override
  public Double getCurrencyExchangeRate( String currencyCode )
  {
    return this.cokeClientDAO.getCurrencyExchangeRate( currencyCode );
  }

  @Override
  public Double getUserBudgetMediaValue( Long userId )
  {
    return this.cokeClientDAO.getUserBudgetMediaValue( userId );
  }
  // Client customization for WIP #42701 ends

  // Client customization for WIP #43735 starts
  @Override
  public List<ClaimRecipientValueBean> getYetToClaimAwards( Long participantId )
  {
    return this.cokeClientDAO.getYetToClaimAwards( participantId );
  }
  // Client customization for WIP #43735 ends

  public CokeClientDAO getCokeClientDAO()
  {
    return cokeClientDAO;
  }

  public void setCokeClientDAO( CokeClientDAO cokeClientDAO )
  {
    this.cokeClientDAO = cokeClientDAO;
  }

@Override
public Map launchAndProcessCokePayrollFileExtract() {
	// TODO Auto-generated method stub
	return this.cokeClientDAO.launchAndProcessCokePayrollFileExtract();
}
	/* TCCC  - customization start - wip 46975 */
	@Override
	public boolean sendRecognizeAnyoneEmail(List<RecognizeAnyone> recognizeAnyoneList) {
		for (RecognizeAnyone recognizeAnyone : recognizeAnyoneList) {

			try {
				cokeClientDAO.saveRecognizeAnyoneEmail(recognizeAnyone);
				Mailing mailing = mailingService.buildSendRecognizeAnyoneMailing(recognizeAnyone);
				mailingService.submitMailing(mailing, null);
			} catch (Exception e) {

				logger.error("Unable to save or send recipient " + recognizeAnyone.getReceiverEmail(), e);
			}
		}
		return true;
	}
	
	public void setMailingService(MailingService mailingService) 
	{
		this.mailingService = mailingService;
	}
	/* TCCC  - customization end */	
 
  // Client customization start - WIP 58122
  @Override
  public List<TccNomLevelPayout> getLevelTotalPoints( Long promotionId )
  {
    return this.cokeClientDAO.getLevelTotalPoints( promotionId );
  }

  @Override
  public TccNomLevelPayout save( TccNomLevelPayout tccNomLevelPayout )
  {
    return this.cokeClientDAO.save( tccNomLevelPayout );
  }

  public List<TcccLevelPayoutValueBean> getLevelPayoutByPromotionId( String promotionId )
  {
    return this.cokeClientDAO.getLevelPayoutByPromotionId( promotionId );
  }

  public TccNomLevelPayout getLevelPayoutById( Long levelPayoutId )
  {
    return this.cokeClientDAO.getLevelPayoutById( levelPayoutId );
  }
  // Client customization end - WIP 58122
  

  // Client customization for WIP #59420 starts
  public CokeNominationFileUploadValueBean getNominationFileUploadDetails( Long promotionId )
  {
    return this.cokeClientDAO.getNominationFileUploadDetails( promotionId );
  }
  // Client Customization for WIP #59420 ends
  
}
