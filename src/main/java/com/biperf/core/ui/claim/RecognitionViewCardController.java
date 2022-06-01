
package com.biperf.core.ui.claim;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.multimedia.ECard;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.recognition.SupportedEcardVideoTypes;
import com.biperf.core.ui.utils.CardUtilties;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsUtil;

/**
 * RecognitionViewCardController.
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
 * <td>Tammy Cheng</td>
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class RecognitionViewCardController extends BaseController
{
  public static String CMS_LOCALE_CODE = "cmsLocaleCode";

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    Long claimId = getClaimIdFromRequest( request );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.PROMOTION ) );
    AbstractRecognitionClaim arc = (AbstractRecognitionClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
    if ( arc instanceof RecognitionClaim )
    {
      request.setAttribute( "claimType", "recognition" );
    }
    else if ( arc instanceof NominationClaim )
    {
      request.setAttribute( "claimType", "nomination" );
    }

    request.setAttribute( "message", arc.getSubmitterComments() );

    AuthenticatedUser authUser = new AuthenticatedUser();
    Locale locale = UserManager.getDefaultLocale();
    if ( arc.getSubmitter() != null )
    {
      if ( null != request.getSession().getAttribute( CMS_LOCALE_CODE ) )
      {
        locale = CmsUtil.getLocale( (String)request.getSession().getAttribute( CMS_LOCALE_CODE ) );
      }

      authUser.setUserId( arc.getSubmitter().getId() );
      authUser.setLocale( locale );
    }

    Card card = arc.getCard();

    if ( card != null && card.getClass().equals( ECard.class ) )
    {
      ECard eCard = (ECard)card;

      boolean flashNeeded = eCard.isFlashNeeded();
      request.setAttribute( "flashNeeded", new Boolean( flashNeeded ) );

      if ( eCard.isSwf() )
      {
        String flashRequestString = CardUtilties.getFlashRequestString( eCard, request );
        request.setAttribute( "flashRequestString", flashRequestString );
        request.setAttribute( "cardPresent", new Boolean( true ) );
      }
      else
      {
        String staticRequestString = CardUtilties.getSizedStaticG5CardString( eCard, request );
        request.setAttribute( "staticRequestString", staticRequestString );
        request.setAttribute( "cardPresent", new Boolean( true ) );
      }
    }
    else
    {
      request.setAttribute( "flashNeeded", new Boolean( false ) );
      request.setAttribute( "cardPresent", new Boolean( false ) );
    }

    if ( arc.getOwnCardName() != null && arc.getOwnCardName().length() > 0 )
    {
      if ( SupportedEcardVideoTypes.isSupportedVideo( arc.getOwnCardName() ) )
      {
        request.setAttribute( "isVideo", Boolean.TRUE );
      }
      request.setAttribute( "staticRequestString", arc.getOwnCardName() );
      request.setAttribute( "cardPresent", new Boolean( true ) );
      request.setAttribute( "flashNeeded", new Boolean( false ) );
    }

    if ( arc.getCardVideoUrl() != null && arc.getCardVideoUrl().length() > 0 )
    {
      request.setAttribute( "isVideo", Boolean.TRUE );
      // MTC - To be changed
      MTCVideo mtcVideo = getMTCVideoService().getMTCVideoByRequestId( arc.getRequestId( arc.getCardVideoUrl() ) );
      String eCardVideoLink = null;
      if ( Objects.nonNull( mtcVideo ) )
      {
        eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
      }
      else
      {
        eCardVideoLink = arc.getActualCardUrl( arc.getCardVideoUrl() );
      }
      request.setAttribute( "staticRequestString", eCardVideoLink );
      request.setAttribute( "cardPresent", new Boolean( true ) );
      request.setAttribute( "flashNeeded", new Boolean( false ) );
    }

    if ( authUser != null )
    {
      request.setAttribute( "designTheme", getDesignTheme( authUser ) );
      request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( authUser.getLocale() ) );
    }
    request.setAttribute( "submitterComments", arc.getSubmitterComments() );
    request.setAttribute( "submissionDate", arc.getSubmissionDate() );
  }

  private String getDesignTheme( AuthenticatedUser authUser )
  {
    return getDesignThemeService().getDesignTheme( authUser );
  }

  private DesignThemeService getDesignThemeService()
  {
    return (DesignThemeService)getService( DesignThemeService.BEAN_NAME );
  }

  /**
   * Does a Bean lookup for the ClaimService
   * 
   * @return ClaimService
   */
  protected static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  protected static MTCVideoService getMTCVideoService()
  {
    return (MTCVideoService)getService( MTCVideoService.BEAN_NAME );
  }

  private Long getClaimIdFromRequest( HttpServletRequest request ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    Long claimId = (Long)clientStateMap.get( "claimId" );
    return claimId;
  }

}
