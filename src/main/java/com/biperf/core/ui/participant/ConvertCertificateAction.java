/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/participant/ConvertCertificateAction.java,v $
 */

package com.biperf.core.ui.participant;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.JSONException;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.ots.OTSService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ots.v1.reedeem.RedeemResponse;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ConvertCertificateAction.
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
 * <td>Sep 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ConvertCertificateAction extends BaseDispatchAction
{

  // This regEx will make sure the cert is 16 chars in length with alphanumeric.
  private static final String CERTIFICATE_PATTERN = "[a-zA-Z0-9]{16}";

  /**
    * unspecified will display list
    * 
    * @param actionMapping
    * @param actionForm
    * @param request
    * @param response
    * @return ActionForward
    */
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  } // end unspecified

  /**
    * @param mapping
    * @param form
    * @param request
    * @param response
    * @return ActionForward
    */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forward = ActionConstants.SUCCESS_FORWARD;
    return mapping.findForward( forward );
  }

  /**
   * cancelled
   * 
   * @param actionMapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward cancelled( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return actionMapping.findForward( ActionConstants.CANCEL_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException 
   * @throws JSONException 
   */
  public ActionForward convertCert( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws ServiceErrorException, JSONException, IOException
  {
    // Build this object for response.
    WebErrorMessageList messages = new WebErrorMessageList();
    ConvertCertificateForm certForm = (ConvertCertificateForm)form;

    boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
    // Check for authorization
    if ( !UserManager.getUser().isParticipant() || isLoginAs )
    {
      messages.getMessages().add( addErrorMessage( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.NOT_AUTHORIZED" ) ) );
    }
    // Check for empty certificate
    else if ( StringUtil.isEmpty( certForm.getCertNumber() ) )
    {
      messages.getMessages().add( addErrorMessage( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.CERT_NUM_REQ" ) ) );
    }
    else if ( isPaxOptedOutOfAwards() )
    {
      messages.getMessages().add( addErrorMessage( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.PAX_OPT_OUT_AWARDS" ) ) );
    }
    else
    {
      // Check for valid certificate pattern.
      if ( !certForm.getCertNumber().matches( CERTIFICATE_PATTERN ) )
      {
        messages.getMessages().add( addErrorMessage( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.CERTIFICATE_NUMBER_INVALID" ) ) );
      }
      else
      {
        try
        {
          RedeemResponse redeemResponse = getOtsService().redeem( certForm.getCertNumber(), UserManager.getUserId() );
          WebErrorMessage message = new WebErrorMessage();
          message.setName( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.SUBMISSION_SUCCESS" ) );
          message.setText( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.SUCCESS" ) + " " + redeemResponse.getValue() + " "
              + CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.POINTS" ) );
          message.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
          messages.getMessages().add( message );
        }

        catch( ServiceErrorException serviceException )
        {
          String exceptionValue = serviceException.getServiceErrors().get( 0 ).toString();
          exceptionValue = exceptionValue.substring( exceptionValue.indexOf( "[key:" ) + 5, exceptionValue.indexOf( "arg0:" ) - 2 );
          if ( exceptionValue.trim().startsWith( "key" ) )
          {
            exceptionValue = exceptionValue.substring( exceptionValue.indexOf( "key: " ) + 5 );
          }
          if ( !exceptionValue.contains( "IALS FOUND FOR GIVEN" ) && !exceptionValue.contains( "UNKNOWN" ) && !exceptionValue.contains( "UND WITH THESE VALUES" )
              && exceptionValue.contains( "ots.settings.info" ) )
          {
            messages.getMessages().add( addErrorMessage( CmsResourceBundle.getCmsBundle().getString( exceptionValue.trim() ) ) );
          }
          else
          {
            messages.getMessages().add( addErrorMessage( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.SUBMISSION_SERVER_DOWN" ) ) );
          }

        }

      }
    }
    // regardless, lets refresh the account balance
    refreshPointBalance( request );
    super.writeAsJsonToResponse( messages, response );
    return null;
  }

  private boolean isPaxOptedOutOfAwards()
  {
    return getParticipantService().getParticipantById( UserManager.getUserId() ).getOptOutAwards();
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );

    return factory.getAwardBanQService();
  }

  private OTSService getOtsService()
  {
    return (OTSService)BeanLocator.getBean( OTSService.class );
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)getService( AuthorizationService.BEAN_NAME );
  }

  private WebErrorMessage addErrorMessage( String text )
  {
    WebErrorMessage message = new WebErrorMessage();
    message.setText( text );
    message.setName( CmsResourceBundle.getCmsBundle().getString( "hometile.onTheSpotCard.SUBMISSION_FAILED" ) );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    return message;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

}
