
package com.biperf.core.ui.ssi;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.supplier.Supplier;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.integration.SupplierService;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.ssi.SSIContestParticipantService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.shopping.ILoggerImpl;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ShoppingValueBean;
import com.biperf.web.singlesignon.SingleSignOnException;
import com.biperf.web.singlesignon.SingleSignOnRequest;

/**
 * 
 * SSIAwardRedeemAction.
 * 
 * @author chowdhur
 * @since Dec 4, 2014
 */
public class SSIAwardRedeemAction extends SSIContestDetailsBaseAction
{
  private static final Log logger = LogFactory.getLog( SSIAwardRedeemAction.class );

  public ActionForward redeemAward( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long contestParticipantId = getContestParticipantId( request );

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "contestParticipantId : " + contestParticipantId );
    }

    ActionMessages errors = new ActionMessages();
    ILoggerImpl loggerImpl = new ILoggerImpl();
    try
    {
      SSIContestParticipant contestParticipant = getContestParticipantService().getContestParticipantById( contestParticipantId );
      Long userId = contestParticipant.getParticipant().getId();

      if ( !userId.equals( UserManager.getUserId() ) || UserManager.isUserDelegateOrLaunchedAs() )
      {
        throw new BeaconRuntimeException( "Invalid Access" );
      }

      String awardType = contestParticipant.getContest().getPayoutType().getCode();

      if ( PromotionAwardsType.POINTS.equals( awardType ) )
      {
        String shoppingType = getShoppingService().checkShoppingType( userId );

        if ( shoppingType.equals( ShoppingService.INTERNAL ) )
        {
          ShoppingValueBean shoppingValue = getShoppingService().setupInternalShopping( userId );
          String ssoUrl = shoppingValue.getRemoteURL();
          singleSignOn( ssoUrl, shoppingValue, loggerImpl, request, response, errors );
        }
        else if ( shoppingType.equals( ShoppingService.EXTERNAL ) )
        {
          Map paramMap = new HashMap();
          paramMap.put( "externalSupplierId", getSupplierService().getSupplierByName( Supplier.BII ).getId() );
          String externalUrl = ClientStateUtils.generateEncodedLink( request.getContextPath(), "/externalSupplier.do?method=displayExternal", paramMap );
          response.sendRedirect( externalUrl );
        }
      }
    }
    catch( ServiceErrorException se )
    {
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return null;
  }

  private void singleSignOn( String ssoUrl, ShoppingValueBean shoppingValue, ILoggerImpl loggerImpl, HttpServletRequest request, HttpServletResponse response, ActionMessages errors )
  {
    SingleSignOnRequest signOnRequest = null;
    if ( Environment.isCtech() && shoppingValue != null )
    {
      // Apply Proxy settings in CTECH environment
      signOnRequest = new SingleSignOnRequest( ssoUrl, shoppingValue.getProxy(), shoppingValue.getProxyPort(), loggerImpl );
    }
    else
    {
      // For NON CTECH environments do not setup Proxy details
      signOnRequest = new SingleSignOnRequest( ssoUrl, loggerImpl );
    }

    if ( shoppingValue.isCanShop() )
    {
      signOnRequest.setParameter( "program_id", shoppingValue.getProgramId() );
      signOnRequest.setParameter( "firstname", shoppingValue.getFirstName() );
      signOnRequest.setParameter( "lastname", shoppingValue.getLastName() );
      signOnRequest.setParameter( "account", shoppingValue.getAccount() );
      signOnRequest.setParameter( "locale", shoppingValue.getLanguagePreference() );
      signOnRequest.setParameter( "seamless_registration", "true" );
    }

    try
    {
      // perform SSO signoff to invalidate any previous SSO requests
      signOnRequest.signOff( request, response );

      // perform SSO signon
      signOnRequest.signOn( request, response );
    }
    catch( SingleSignOnException e )
    {
      logger.error( "SingleSignOnException for program_id:" + shoppingValue.getProgramId() + " program_password:" + shoppingValue.getProgramPassword() + " - ", e );

      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }
  }

  private Long getContestParticipantId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "contestParticipantId" );
  }

  public String buildShopUrl( SSIContest contest, HttpServletRequest request, Long contestParticipantId )
  {
    if ( contest.getStatus().isFinalizeResults() && contest.getPayoutType().isPoints() )
    {
      return ClientStateUtils.generateEncodedLink( request.getContextPath(), "/ssi/participantContestList.do?method=redeemAward", buildParameterMap( contestParticipantId ) );
    }
    else
    {
      return null;
    }
  }

  private Map<String, Object> buildParameterMap( Long contestParticipantId )
  {
    Map<String, Object> params = new HashMap<String, Object>();
    params.put( "contestParticipantId", contestParticipantId );
    return params;
  }

  private SSIContestParticipantService getContestParticipantService()
  {
    return (SSIContestParticipantService)getService( SSIContestParticipantService.BEAN_NAME );
  }

  private ShoppingService getShoppingService()
  {
    return (ShoppingService)getService( ShoppingService.BEAN_NAME );
  }

  private SupplierService getSupplierService()
  {
    return (SupplierService)getService( SupplierService.BEAN_NAME );
  }

}
