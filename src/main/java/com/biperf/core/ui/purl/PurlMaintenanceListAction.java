
package com.biperf.core.ui.purl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlRecipientAssociationRequest;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;

public class PurlMaintenanceListAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PurlMaintenanceListAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = UserManager.getUser().getUserId();
    // Long promotionId = getPromotionId( request );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "UserId : " + userId );
      // logger.debug( "PromotionId : " + promotionId );
    }

    List<PurlRecipient> invitations = getPurlInvitationsForManager( userId, null );

    if ( invitations != null && !invitations.isEmpty() )
    {
      request.setAttribute( "promotionName", invitations.get( 0 ).getPromotion().getName() );
      request.setAttribute( "purlInvitationList", invitations );
    }

    request.setAttribute( "isNewSAEnabled", NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() );

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private List<PurlRecipient> getPurlInvitationsForManager( Long userId, Long promotionId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_CONTRIBUTOR ) );
    associationRequestCollection.add( new PurlRecipientAssociationRequest( PurlRecipientAssociationRequest.PURL_USER_ADDRESS ) );

    List<PurlRecipient> invitations = getPurlService().getAllPurlInvitationsForManager( userId, promotionId, associationRequestCollection );
    for ( PurlRecipient purlRecipient : invitations )
    {
      PurlContributor managerContributor = getPurlService().getPurlContributorByUserId( userId, purlRecipient.getId() );
      purlRecipient.setManagerContributor( managerContributor );
    }
    return invitations;
  }

  private Long getPromotionId( HttpServletRequest request ) throws InvalidClientStateException
  {
    Long promotionId = null;

    String clientState = RequestUtils.getOptionalParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( StringUtils.isNotEmpty( clientState ) )
    {
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      try
      {
        promotionId = (Long)clientStateMap.get( "promotionId" );
      }
      catch( ClassCastException cce )
      {
        promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
      }
    }

    return promotionId;
  }

  private static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

}
