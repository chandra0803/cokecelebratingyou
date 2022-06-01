
package com.biperf.core.ui.claim;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;

public class ViewRecognitionAction extends BaseRecognitionAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // get the claim ID from client state
    Long claimId = null;
    String referralPage = null;
    String forwardTo = "success";
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        claimId = (Long)clientStateMap.get( "claimId" );
      }
      catch( ClassCastException cce )
      {
        claimId = new Long( (String)clientStateMap.get( "claimId" ) );
      }

      String isFullPage = (String)clientStateMap.get( "isFullPage" );
      if ( !StringUtil.isEmpty( isFullPage ) )
      {
        if ( !Boolean.valueOf( isFullPage ) )
        {
          forwardTo = ActionConstants.SHEET_VIEW;
        }
      }

      referralPage = (String)clientStateMap.get( "referralPage" );
      if ( !StringUtil.isEmpty( referralPage ) )
      {
        request.setAttribute( "referralPage", referralPage );
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    // put the claimId on the request for the JSP
    request.setAttribute( "claimId", claimId );

    if ( claimId != null )
    {
      Claim claim = getClaimService().getClaimById( claimId );
      request.setAttribute( "promotionType", claim.getPromotion().getPromotionType().getCode() );
    }

    return mapping.findForward( forwardTo );
  }

}
