
package com.biperf.core.ui.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.gamification.BadgeDetails;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.recognition.RecognitionSentBean;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorBadgesValueBean;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * HomePageController.
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
 * <td>robinsra</td>
 * <td>Dec. 6, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class HomePageController extends BaseController
{
  private static final Log logger = LogFactory.getLog( HomePageController.class );

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext   
   */

  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    String navselected = (String)tileContext.getAttribute( "navselected" );
    request.getSession().setAttribute( "selectedTabId", navselected );

    // Bug# 31553 -Start
    if ( request.getParameter( "contentId" ) != null )
    {
      Content contentForNewsPreview = (Content)ContentReaderManager.getContentReader().getContent( new Long( request.getParameter( "contentId" ) ) );
      List<Content> activeMessages = new ArrayList<Content>();
      activeMessages.add( contentForNewsPreview );
      request.setAttribute( "welcomeActiveMessageList", activeMessages );
    }
    // Bug# 31553 -End

    isMyGroupEnabled( request, UserManager.getUser() );

    if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() )
    {
      if ( UserManager.isUserLoggedIn() )
      {
        if ( !UserManager.isUserDelegateOrLaunchedAs() && ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() ) )
        {
          if ( Objects.nonNull( request.getAttribute( "recognitionSentBean" ) ) )
          {
            RecognitionSentBean recognitionSentBean = (RecognitionSentBean)request.getAttribute( "recognitionSentBean" );
            request.setAttribute( "badgeDetailsCount", recognitionSentBean.getBadgeDetails().size() );
            Map paramMapSent = new HashMap();
            paramMapSent.put( "claimId", recognitionSentBean.getClaimId() );
            request.setAttribute( "raClaimUrl", ClientStateUtils.generateEncodedLink( "", "claim/claimDetail.do", paramMapSent ) );
            request.setAttribute( "baseURI", RequestUtils.getBaseURI( request ) );
            ObjectMapper mapper = new ObjectMapper();
            try
            {
              if ( !recognitionSentBean.getBadgeDetails().isEmpty() )
              {
                request.setAttribute( "recognitionBadgesSent", mapper.writeValueAsString( getBadgeDetails( recognitionSentBean.getBadgeDetails() ) ) );
              }
              else
              {
                request.setAttribute( "recognitionBadgesSent", "[]" );
              }

            }
            catch( JsonProcessingException e )
            {
              logger.error( "Coverting Badges to Jason : " + e.getMessage() );
            }

          }

        }

      }

    }

  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private SSIContestService getSSIContestService()
  {
    return (SSIContestService)getService( SSIContestService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private void isMyGroupEnabled( HttpServletRequest request, AuthenticatedUser authUser )
  {
    boolean isGiver = false;
    boolean enableMyGroups = true;
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    /*
     * Added Promotion Status since eligible Promotion considers Expired Promotions as well.
     * Restricting promotion type to recognition and nomination since, for Quiz promotions also,
     * canSubmit is true
     */
    if ( !Objects.isNull( eligiblePromotions ) )
    {
      List<PromotionMenuBean> liveEligiblePromotions = eligiblePromotions.stream()
          .filter( p -> ( ( PromotionStatusType.LIVE ).equals( p.getPromotion().getPromotionStatus().getCode() ) )
              && ( ( PromotionType.NOMINATION ).equals( p.getPromotion().getPromotionType().getCode() ) || ( PromotionType.RECOGNITION ).equals( p.getPromotion().getPromotionType().getCode() ) ) )
          .collect( Collectors.toList() );
      if ( !Objects.isNull( liveEligiblePromotions ) )
      {
        isGiver = liveEligiblePromotions.stream().filter( p -> p.isCanSubmit() ).findFirst().isPresent();
        if ( !isGiver )
        {
          if ( !getSSIContestService().isContestCreator( authUser.getUserId() ) )
          {
            enableMyGroups = false;
          }
        }
      }
    }
    else
    {
      enableMyGroups = false;
    }
    request.getSession().setAttribute( "promoGiver", isGiver );
    request.getSession().setAttribute( "enableMyGroups", enableMyGroups );
  }

  private List<RecognitionAdvisorBadgesValueBean> getBadgeDetails( List<BadgeDetails> raBadgesForModal )
  {

    List<RecognitionAdvisorBadgesValueBean> badgesForModal = new ArrayList<>();

    raBadgesForModal.forEach( beanValue ->
    {
      RecognitionAdvisorBadgesValueBean raBadgesValueBean = new RecognitionAdvisorBadgesValueBean();

      raBadgesValueBean.setEarned( beanValue.getEarned() );
      raBadgesValueBean.setBadgeName( beanValue.getBadgeNameTextFromCM() );
      raBadgesValueBean.setBadgeDescription( beanValue.getBadgeDescriptionTextFromCM() );
      raBadgesValueBean.setProgressNumerator( beanValue.getProgressNumerator() );
      raBadgesValueBean.setProgressDenominator( beanValue.getProgressDenominator() );
      raBadgesValueBean.setImg( beanValue.getImg() );
      raBadgesValueBean.setDateEarned( beanValue.getDateEarned() );
      raBadgesValueBean.setBadgeType( beanValue.getBadgeType() );

      badgesForModal.add( raBadgesValueBean );

    } );

    return badgesForModal;
  }

}
