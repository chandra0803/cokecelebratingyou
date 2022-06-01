
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
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
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
public class TcccRecognitionClaimAwardAction extends BaseDispatchAction
{

  private static final org.apache.commons.logging.Log logger = LogFactory.getLog( TcccRecognitionClaimAwardAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    TcccRecognitionClaimAwardForm tcccRecognitionClaimAwardForm = (TcccRecognitionClaimAwardForm)form;
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
        AbstractRecognitionClaim claim = (AbstractRecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
        if ( claim.isMine() )
        {
          request.setAttribute( "claim", claim );
          tcccRecognitionClaimAwardForm.load( claim );
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
    TcccRecognitionClaimAwardForm tcccRecognitionClaimAwardForm = (TcccRecognitionClaimAwardForm)form;
    try
    {
      getClaimService().processClaimAward( tcccRecognitionClaimAwardForm.getClaimId(), tcccRecognitionClaimAwardForm.getAwardType() );
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
    request.getSession().setAttribute( "redeemedSuccessMsg", getMessage( tcccRecognitionClaimAwardForm ) );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private String getMessage( TcccRecognitionClaimAwardForm form )
  {
    if ( CokeClientService.REDEEM_AWARD_MODE_CASH.equals( form.getAwardType() ) )
    {
      return ContentReaderManager.getText( "coke.cash.recognition", "CASH_CONFIRM" );
    }
    else if ( CokeClientService.REDEEM_AWARD_MODE_CASH_AND_POINTS.equals( form.getAwardType() ) )
    {
      return MessageFormat.format( ContentReaderManager.getText( "coke.cash.recognition", "PTS_CASH_CONFIRM" ), new Object[] { form.getPointsHalfAmt() } );
    }
    else if ( CokeClientService.REDEEM_AWARD_MODE_POINTS.equals( form.getAwardType() ) )
    {
      return MessageFormat.format( ContentReaderManager.getText( "coke.cash.recognition", "PTS_CONFIRM" ), new Object[] { form.getPointsFullAmt() } );
    }
    else
    {
      return ContentReaderManager.getText( "coke.cash.recognition", "OPTOUT_CONFIRM" );
    }
  }
  
  private static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

}
