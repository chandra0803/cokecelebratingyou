
package com.biperf.core.ui.purl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.purl.impl.PurlContributorAssociationRequest;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;

public class PurlContributionListAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( PurlContributionListAction.class );

  @SuppressWarnings( "unchecked" )
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long userId = UserManager.getUser().getUserId();
    Long promotionId = getPromotionId( request );
    boolean isDefaultInvitee = isDefaultInvitee( request );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "UserId : " + userId );
      logger.debug( "PromotionId : " + promotionId );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PurlContributorAssociationRequest( PurlContributorAssociationRequest.PURL_RECIPIENT_USER_ADDRESS ) );
    associationRequestCollection.add( new PurlContributorAssociationRequest( PurlContributorAssociationRequest.PURL_RECIPIENT_CONTRIBUTORS ) );
    List<PurlContributor> invitations = getPurlService().getAllPurlContributions( userId, promotionId, isDefaultInvitee, associationRequestCollection );

    request.setAttribute( "promotionName", invitations.get( 0 ).getPurlRecipient().getPromotion().getName() );
    request.setAttribute( "purlContributionList", invitations );
    request.setAttribute( "isNewSAEnabled", NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() );

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private Long getPromotionId( HttpServletRequest request ) throws InvalidClientStateException
  {
    Long promotionId = null;

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
      promotionId = (Long)clientStateMap.get( "promotionId" );
    }
    catch( ClassCastException cce )
    {
      promotionId = new Long( (String)clientStateMap.get( "promotionId" ) );
    }
    return promotionId;
  }

  private boolean isDefaultInvitee( HttpServletRequest request ) throws InvalidClientStateException
  {
    boolean isDefaultInvitee = false;

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
      isDefaultInvitee = (boolean)clientStateMap.get( "isDefaultInvitee" );
    }
    catch( ClassCastException cce )
    {
      isDefaultInvitee = (boolean)clientStateMap.get( "isDefaultInvitee" );
    }
    return isDefaultInvitee;
  }

  private static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

}
