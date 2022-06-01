  // Client customization for WIP 58122
package com.biperf.core.ui.client;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.ServiceErrorExceptionWithRollback;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * TcccClaimFileAction.
 * 
 * This class is created as part of Client Customization for WIP #43735
 * 
 * @author dudam
 * @since Mar 20, 2018
 * @version 1.0
 */
public class TcccNominationClaimAwardAction extends BaseDispatchAction
{

  private static final org.apache.commons.logging.Log logger = LogFactory.getLog( TcccNominationClaimAwardAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    TcccNominationClaimAwardForm tcccNominationClaimAwardForm = (TcccNominationClaimAwardForm)form;
    try
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
      if ( clientState != null && clientState.length() > 0 )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();
        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        Long claimId = (Long)clientStateMap.get( "claimId" );
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_ADDRESS ) );
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENT_EMPLOYERS ) );
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_SUBMITTER_ADDRESS ) );
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_SUBMITTER_EMPLOYER ) );
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_PARTICIPANTS ) );
        AbstractRecognitionClaim claim = (AbstractRecognitionClaim) getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
        
        Claim claim1 =  getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
        NominationClaim nc = (NominationClaim)claim1;
       if(nc.isMine())
       {
          request.setAttribute( "claim", claim );
          tcccNominationClaimAwardForm.load( claim,nc );
          return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
        }
        else
        {
          return mapping.findForward( ActionConstants.FAIL_FORWARD );
		}
      }
      else
      {
        return mapping.findForward( ActionConstants.FAIL_FORWARD );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  public ActionForward process( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
	  TcccNominationClaimAwardForm tcccNominationClaimAwardForm = (TcccNominationClaimAwardForm)form;
	  boolean success=false;

    try
    {
    	//AbstractRecognitionClaim claim = (AbstractRecognitionClaim)claimDAO.getClaimById( claimId );
      success=getClaimService().processNominationClaimAward( tcccNominationClaimAwardForm.getClaimId(), tcccNominationClaimAwardForm.getAward_type_selected() );
     	
    }
    catch( ServiceErrorException e )
    {
      List<String> errors = new ArrayList<String>();
      log.error( e );
      List serviceErrors = e.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError serviceError = (ServiceError)iter.next();
        errors.add( CmsResourceBundle.getCmsBundle().getString( serviceError.getKey() ) );
      }
      request.setAttribute( "errors", errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    super.writeAsJsonToResponse(success, response);
    return null;
  }
  
  private String getMessage( TcccNominationClaimAwardForm form )
  {
     if ( CokeClientService.REDEEM_AWARD_MODE_POINTS.equals( form.getAwardType() ) )
    {
      return MessageFormat.format( ContentReaderManager.getText( "coke.cash.nomination", "PTS_CONFIRM" ), new Object[] { form.getPointsFullAmt() } );
    }
    else
    {
      return ContentReaderManager.getText( "coke.cash.nomination", "OPTTRAINING_CONFIRM" );
    }
  }
  
  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }


}
