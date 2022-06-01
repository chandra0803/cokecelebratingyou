
package com.biperf.core.ui.celebration;

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

import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.shopping.ShoppingService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.shopping.ILoggerImpl;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ShoppingValueBean;
import com.biperf.web.singlesignon.SingleSignOnException;
import com.biperf.web.singlesignon.SingleSignOnRequest;

public class CelebrationShoppingAction extends BaseCelebrationAction
{
  private static final Log logger = LogFactory.getLog( CelebrationShoppingAction.class );

  public ActionForward displayShopping( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    ActionMessages errors = new ActionMessages();
    ILoggerImpl loggerImpl = new ILoggerImpl();

    Long userId = UserManager.getUserId();
    Long claimId = getClaimId( request );

    RecognitionClaim recognitionClaim = getRecognitionClaim( claimId, request );

    if ( recognitionClaim != null )
    {
      RecognitionPromotion recognitionPromotion = (RecognitionPromotion)recognitionClaim.getPromotion();

      try
      {
        Map<String, String> shoppingUrlMap = getShoppingService().getShopUrlMapping( userId, recognitionPromotion.getAwardType(), request.getContextPath() );
        String ssoUrl = shoppingUrlMap.get( "sso" );
        String redirectUrl = shoppingUrlMap.get( "redirect" );

        boolean isApqConversion = recognitionPromotion.isApqConversion();
        if ( PromotionAwardsType.POINTS.equals( recognitionPromotion.getAwardType().getCode() ) )
        {
          if ( ssoUrl != null )
          {
            ShoppingValueBean shoppingValue = getShoppingService().setupInternalShopping( userId );
            singleSignOn( ssoUrl, userId, recognitionClaim.getId(), shoppingValue, isApqConversion, null, null, loggerImpl, request, response, errors );
          }
          else if ( redirectUrl != null )
          {
            response.sendRedirect( redirectUrl );
          }
        }
      }
      catch( ServiceErrorException se )
      {
        ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( se.getServiceErrors(), errors );
      }
    }
    return null;
  }

  private void singleSignOn( String ssoUrl,
                             Long userId,
                             Long claimId,
                             ShoppingValueBean shoppingValue,
                             boolean isApqConversion,
                             String giftcode,
                             Long promoMerchCountryId,
                             ILoggerImpl loggerImpl,
                             HttpServletRequest request,
                             HttpServletResponse response,
                             ActionMessages errors )
  {
    if ( UserManager.isUserDelegateOrLaunchedAs() )
    {
      throw new BeaconRuntimeException( "Invalid Access" );
    }

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
    if ( shoppingValue != null && shoppingValue.isCanShop() )
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
      logger.error( "SingleSignOnException for userId:" + userId + " program_id:" + shoppingValue.getProgramId() + " program_password:" + shoppingValue.getProgramPassword() + " - ", e );

      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SINGLE_SIGNON_TO_AWARDSLINQ_ERROR ) );
    }
  }

  private ShoppingService getShoppingService()
  {
    return (ShoppingService)getService( ShoppingService.BEAN_NAME );
  }

}
