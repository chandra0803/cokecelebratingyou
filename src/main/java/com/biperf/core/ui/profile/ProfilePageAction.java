
package com.biperf.core.ui.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.impl.PromotionServiceImpl;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.throwdown.TeamService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.PromotionPaxValue;
import com.biperf.core.value.ThrowdownPlayerStatsBean;

/**
 * 
 * @author dudam
 * @since Dec 19, 2012
 * @version 1.0
 */
public class ProfilePageAction extends BaseDispatchAction
{
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
	    // Client customizations for WIP #43735 starts
	    String redeemedSuccessMsg = (String)request.getSession().getAttribute( "redeemedSuccessMsg" );
	    request.setAttribute( "redeemedSuccessMsg", redeemedSuccessMsg );
	    request.getSession().removeAttribute( "redeemedSuccessMsg" );
	    // Client customizations for WIP #43735 ends
    return this.display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String forwardTo = ActionConstants.FULL_VIEW;
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    try
    {

      Long paxId = null;
      Boolean isPlayerStatsView = (Boolean)request.getAttribute( "isPlayerStatsView" ) != null ? (Boolean)request.getAttribute( "isPlayerStatsView" ) : false;

      Participant participant = null;
      AuthenticatedUser authUser = UserManager.getUser();
      if ( authUser != null )
      {
        participant = getParticipantService().getParticipantById( authUser.getUserId() );
        List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
        List paxPromotions = getPromotionService().getAllLiveEngagementByUserId( authUser.getUserId(), eligiblePromotions );

        if ( !paxPromotions.isEmpty() )
        {
          request.setAttribute( "pax", participant );
          request.setAttribute( "displayEngagementTab", true );

          List<String> eligiblePromotionList = getEngagementService().getAllEligiblePromotions();
          request.setAttribute( "eligiblePromotionList", eligiblePromotionList );
        }
        request.setAttribute( "displayStatementTab", canShowShopTile( participant ) );
      }

      if ( isPlayerStatsView )
      {
        String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();
        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        paxId = (Long)clientStateMap.get( "paxId" );
      }
      else
      {
        if ( authUser != null )
        {
          if ( authUser.isParticipant() )
          {
            paxId = participant.getId();
          }
          else
          {
            forwardTo = ActionConstants.ADMIN_HOME;
            return mapping.findForward( forwardTo );
          }
        }
      }

      Long promotionId = request.getParameter( "promotionId" ) != null ? new Long( request.getParameter( "promotionId" ) ) : null;
      List paxPromotions = getPromotionService().getAllLiveAndExpiredByTypeAndUserId( PromotionType.THROWDOWN, paxId );
      Map<String, Long> paramMap = new HashMap<String, Long>();
      paramMap.put( "paxId", paxId );
      request.setAttribute( "profileUrl", ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.PRIVATE_PROFILE_VIEW, paramMap ) );
      if ( !paxPromotions.isEmpty() )
      {
        for ( Iterator iter = paxPromotions.iterator(); iter.hasNext(); )
        {
          PromotionPaxValue promoPax = (PromotionPaxValue)iter.next();
          if ( promoPax.getRoleKey().equals( PromotionServiceImpl.THROWDOWN_PRIMARY ) )
          {
            iter.remove();
          }
        }
      }

      PromotionMenuBean promoBean = reorderEligiblePromotions( request, promotionId );
      paxPromotions = reorderPaxPromotions( request, promoBean, paxPromotions );
      Date progressEndDate = null;
      if ( !paxPromotions.isEmpty() && promotionId == null )
      {
        PromotionPaxValue promoPax = (PromotionPaxValue)paxPromotions.get( 0 );
        ThrowdownPromotion promotion = (ThrowdownPromotion)promoPax.getPromotion();
        ThrowdownPlayerStatsBean playerStatsBean = getTeamService().getPlayerStats( paxId, promotion.getId() );
        progressEndDate = getTeamService().getLastFileLoadDateForPromotion( promotion.getId() );
        playerStatsBean.getMatches().get( 0 ).setAsOfDate( DateUtils.toDisplayString( progressEndDate ) );
        request.setAttribute( "playerStats", playerStatsBean );
      }
      else
      {
        if ( promotionId != null )
        {
          ThrowdownPlayerStatsBean playerStatsBean = getTeamService().getPlayerStats( paxId, promotionId );
          forwardTo = ActionConstants.THROWDOWN_VIEW;
          progressEndDate = getTeamService().getLastFileLoadDateForPromotion( promotionId );
          playerStatsBean.getMatches().get( 0 ).setAsOfDate( DateUtils.toDisplayString( progressEndDate ) );
          request.setAttribute( "playerStats", playerStatsBean );
        }
      }
      request.setAttribute( "JstlDatePattern", DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return mapping.findForward( forwardTo );

  }

  private PromotionMenuBean reorderEligiblePromotions( HttpServletRequest request, Long promotionId )
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    if ( !eligiblePromotions.isEmpty() )
    {
      if ( promotionId != null )
      {
        List<PromotionMenuBean> reorderedPromotions = new ArrayList<PromotionMenuBean>();
        List<PromotionMenuBean> otherPromotions = new ArrayList<PromotionMenuBean>();

        for ( PromotionMenuBean promotionBean : eligiblePromotions )
        {
          if ( promotionBean.getPromotion().getId().equals( promotionId ) )
          {
            reorderedPromotions.add( promotionBean );
          }
          else
          {
            otherPromotions.add( promotionBean );
          }
        }
        reorderedPromotions.addAll( otherPromotions );
        request.getSession().setAttribute( "eligiblePromotions", reorderedPromotions );
        return reorderedPromotions.get( 0 );
      }
      else
      {
        return eligiblePromotions.get( 0 );
      }
    }
    return null;
  }

  private List<PromotionPaxValue> reorderPaxPromotions( HttpServletRequest request, PromotionMenuBean promotionMenuBean, List<PromotionPaxValue> paxpromolist )
  {
    List<PromotionPaxValue> reorderedPromotions = new ArrayList<PromotionPaxValue>();
    List<PromotionPaxValue> otherPromotions = new ArrayList<PromotionPaxValue>();

    for ( PromotionPaxValue paxpromo : paxpromolist )
    {
      if ( paxpromo.getPromotion().getId().equals( promotionMenuBean != null ? promotionMenuBean.getPromotion().getId() : null ) )
      {
        reorderedPromotions.add( paxpromo );
      }
      else
      {
        otherPromotions.add( paxpromo );
      }
    }
    reorderedPromotions.addAll( otherPromotions );
    request.setAttribute( "promotions", reorderedPromotions );
    return reorderedPromotions;
  }

  private boolean canShowShopTile( Participant participant )
  {
    return getMainContentService().checkShowShopORTravel();
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private TeamService getTeamService()
  {
    return (TeamService)getService( TeamService.BEAN_NAME );
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private EngagementService getEngagementService()
  {
    return (EngagementService)getService( EngagementService.BEAN_NAME );
  }

  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }
}
