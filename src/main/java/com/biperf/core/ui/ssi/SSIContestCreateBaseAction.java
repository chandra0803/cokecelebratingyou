
package com.biperf.core.ui.ssi;

import static com.biperf.core.utils.SSIContestUtil.getClientState;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.gamification.BadgeRule;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.ssi.SSIContestAwardThemNowService;
import com.biperf.core.ui.ssi.view.SSIContestAwardThemNowResponseView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;

/**
 * @author dudam
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestCreateBaseAction extends SSIContestBaseAction
{

  protected static final String SORTED_BY = "sortedBy";
  protected static final String SORTED_ON = "sortedOn";
  protected static final String CURRENT_PAGE = "currentPage";
  protected static final String AWARD_ISSUANCE_NUMBER = "awardIssuanceNumber";

  /**
   * Delete the award them contest issuance when cancel is clicked
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward cancelAtn( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    Short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    String forwardUrl = getSysUrl() + PageConstants.SSI_CREATOR_DETAIL_URL;
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );

    if ( clientStateMap.get( SSIContestUtil.CONTEST_ID ) != null )
    {

      if ( awardIssuanceNumber == null )
      {
        forwardUrl = getFormForwardUrl( request, contestId );
      }
      else if ( awardIssuanceNumber == 1 )
      {
        getSSIContestAwardThemNowService().deleteContest( contestId );
      }
      else if ( awardIssuanceNumber > 1 )
      {
        getSSIContestAwardThemNowService().deleteContestIssuance( contestId, getAwardIssuanceNumberFromClientState( request ) );
        forwardUrl = getFormForwardUrl( request, contestId );
      }
    }
    SSIContestAwardThemNowResponseView contestAwardThemNowResponseView = new SSIContestAwardThemNowResponseView( forwardUrl );
    writeAsJsonToResponse( contestAwardThemNowResponseView, response );
    return null;
  }

  private String getFormForwardUrl( HttpServletRequest request, Long contestId )
  {
    return getSysUrl() + "/ssi/displayContestSummaryAwardThemNow.do?method=load&contestId=" + getClientState( request, contestId, UserManager.getUserId(), true );
  }

  /**
   * De-serialize the client state
   * @param request
   * @return
   */
  protected Map<String, Object> getSSIContestClientStateMap( HttpServletRequest request )
  {
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "ssiContestClientState" );
      return getSSIContestClientStateMap( clientState );
    }
    catch( IllegalArgumentException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  /*
   * Get the award issuance number from client state
   * @param request
   * @return
   */
  protected Short getAwardIssuanceNumberFromClientState( HttpServletRequest request )
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    Short awardIssuanceNumber = null;
    if ( clientStateMap.get( AWARD_ISSUANCE_NUMBER ) != null )
    {
      awardIssuanceNumber = (Short)clientStateMap.get( AWARD_ISSUANCE_NUMBER );
    }
    return awardIssuanceNumber;
  }

  /*
   * Get the contest id from the client state map
   */
  protected Long getContestIdFromClientStateMap( HttpServletRequest request )
  {
    Map<String, Object> clientStateMap = getSSIContestClientStateMap( request );
    Long contestId = null;
    if ( clientStateMap.get( SSIContestUtil.CONTEST_ID ) != null )
    {
      contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );
    }
    return contestId;
  }

  /*
   * Get the page Number from the request
   */
  protected int getPageNumber( HttpServletRequest request )
  {
    return RequestUtils.getOptionalParamInt( request, CURRENT_PAGE ) != 0 ? RequestUtils.getOptionalParamInt( request, CURRENT_PAGE ) : 1;
  }

  /*
   * Get the sorted by column name from the request
   */
  protected String getSortedBy( HttpServletRequest request )
  {
    return !StringUtil.isNullOrEmpty( RequestUtils.getOptionalParamString( request, SORTED_BY ) ) ? RequestUtils.getOptionalParamString( request, SORTED_BY ) : SSIContestUtil.DEFAULT_SORT_BY;
  }

  /*
   * Get the sorted on column name from the request
   */
  protected String getSortedOn( HttpServletRequest request )
  {
    String sortedOn = !StringUtil.isNullOrEmpty( RequestUtils.getOptionalParamString( request, SORTED_ON ) )
        ? RequestUtils.getOptionalParamString( request, SORTED_ON )
        : SSIContestUtil.SORT_BY_LAST_NAME;
    return sortedOn;
  }

  protected SSIContestAwardThemNowService getSSIContestAwardThemNowService()
  {
    return (SSIContestAwardThemNowService)getService( SSIContestAwardThemNowService.BEAN_NAME );
  }

  protected GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
  }

  protected Set<BadgeRule> getSortedBadgeRulesById( Long badgeId )
  {
    return getGamificationService().getSortedBadgeRulesById( badgeId );
  }

}
