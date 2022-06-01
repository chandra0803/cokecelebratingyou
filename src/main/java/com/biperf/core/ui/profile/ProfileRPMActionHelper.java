
package com.biperf.core.ui.profile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.ui.engagement.EngagementTeamIndividualsMembersView;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

public class ProfileRPMActionHelper
{
  private static final String PAX_REC_TO = "paxRecTo";
  private static final String TYPE_RECEIVER = "receiver";
  private static final String TYPE = "type";
  private static final String GIVER_RECEIVER = "giverReceiver";
  private static final String END_YEAR = "endYear";
  private static final String END_MONTH = "endMonth";
  private static final String LOCALE = "locale";
  private static final String USER_ID = "userId";
  private static final String TIMEFRAME_MONTH_ID = "timeframeMonthId";
  private static final String NODE_IDS = "nodeId";
  private static final String ROW_NUM_END = "rowNumEnd";
  private static final String ROW_NUM_START = "rowNumStart";
  private static final String SORTED_BY = "sortedBy";
  private static final String PAGE_NUMBER = "page";
  private static final String SORTED_ON = "sortedOn";
  private static final String TIMEFRAME_NAVIGATE = "timeframeNavigate";
  private static final String TIMEFRAME_YEAR = "timeframeYear";
  private static final String GIVER = "giver";
  private static final String START_DATE = "startDate";
  private static final String END_DATE = "endDate";
  private static final String TIMEFRAME_TYPE = "timeframeType";
  private static final String MODE = "mode";
  private static final String MONTH = "month";
  private static final String NODE_NAME = "nodeName";
  private static final String DRILLDOWN = "_drillDown";
  private boolean sortTeamMembers = false;
  private String navigateTo = "";
  private static final String USER = "user";

  public Map<String, Object> populateQueryParams( HttpServletRequest request )
  {
    int timeframeMonthId = 0;
    Date currentDate = DateUtils.getCurrentDate();
    String mode = StringUtils.isEmpty( request.getParameter( MODE ) ) ? USER : request.getParameter( MODE );
    String selectedNodes = StringUtils.isEmpty( request.getParameter( NODE_IDS ) ) ? null : request.getParameter( NODE_IDS );

    String navigateToVal = this.getNavigateTo();
    if ( isSortTeamMembers() && !StringUtils.isEmpty( navigateToVal ) && !navigateToVal.equals( "false" ) )
    {
      if ( navigateToVal.equals( "prev" ) )
      {
        timeframeMonthId = Integer.parseInt( request.getParameter( TIMEFRAME_MONTH_ID ) ) + 1;
      }
      else if ( navigateToVal.equals( "next" ) )
      {
        timeframeMonthId = Integer.parseInt( request.getParameter( TIMEFRAME_MONTH_ID ) ) - 1;
      }
    }
    else
    {
      timeframeMonthId = StringUtils.isEmpty( request.getParameter( TIMEFRAME_MONTH_ID ) ) ? DateUtils.getMonthFromDate( currentDate ) : Integer.parseInt( request.getParameter( TIMEFRAME_MONTH_ID ) );
    }

    this.setSortTeamMembers( false );
    this.setNavigateTo( "" );

    String timeframeType = StringUtils.isEmpty( request.getParameter( TIMEFRAME_TYPE ) ) ? MONTH : request.getParameter( TIMEFRAME_TYPE );
    int timeframeYear = StringUtils.isEmpty( request.getParameter( TIMEFRAME_YEAR ) ) ? DateUtils.getYearFromDate( currentDate ) : Integer.parseInt( request.getParameter( TIMEFRAME_YEAR ) );
    Long userId = StringUtils.isEmpty( request.getParameter( USER_ID ) ) ? UserManager.getUserId() : Long.parseLong( request.getParameter( USER_ID ) );
    String timeframeNavigate = request.getParameter( TIMEFRAME_NAVIGATE );
    String sortedOn = request.getParameter( SORTED_ON );
    String sortedBy = request.getParameter( SORTED_BY );
    String nodeName = request.getParameter( NODE_NAME );
    boolean drillDown = StringUtils.isEmpty( request.getParameter( DRILLDOWN ) ) ? false : Boolean.valueOf( request.getParameter( DRILLDOWN ) );

    int pageNumber = request.getParameter( PAGE_NUMBER ) == null ? 1 : Integer.parseInt( request.getParameter( PAGE_NUMBER ) );
    int rowNumStart = ( pageNumber - 1 ) * EngagementTeamIndividualsMembersView.TEAM_MEMBERS_PER_PAGE;
    int rowNumEnd = ( pageNumber - 1 ) * EngagementTeamIndividualsMembersView.TEAM_MEMBERS_PER_PAGE + EngagementTeamIndividualsMembersView.TEAM_MEMBERS_PER_PAGE + 1;

    Date nextStartDate = DateUtils.getRollingPeriodStartDate( timeframeMonthId, timeframeYear, timeframeType, timeframeNavigate );
    Date nextEndDate = DateUtils.getRollingPeriodEndDate( timeframeMonthId, timeframeYear, timeframeType, timeframeNavigate );
    int endMonth = DateUtils.getMonthFromDate( nextEndDate );
    int endYear = DateUtils.getYearFromDate( nextEndDate );

    Map<String, Object> queryParams = new HashMap<String, Object>();
    queryParams.put( NODE_IDS, selectedNodes );
    queryParams.put( NODE_NAME, nodeName );
    queryParams.put( USER_ID, userId );
    queryParams.put( TIMEFRAME_TYPE, timeframeType );
    queryParams.put( MODE, mode );
    queryParams.put( LOCALE, UserManager.getLocale() );
    queryParams.put( END_MONTH, endMonth + 1 );
    queryParams.put( END_YEAR, endYear );
    queryParams.put( START_DATE, nextStartDate );
    queryParams.put( END_DATE, nextEndDate );
    queryParams.put( SORTED_BY, sortedBy );
    queryParams.put( SORTED_ON, sortedOn );
    queryParams.put( PAGE_NUMBER, pageNumber );
    queryParams.put( ROW_NUM_START, rowNumStart );
    queryParams.put( ROW_NUM_END, rowNumEnd );
    queryParams.put( DRILLDOWN, drillDown );
    String type = (String)request.getParameter( TYPE );
    if ( type != null && type.equals( PAX_REC_TO ) )
    {
      queryParams.put( GIVER_RECEIVER, GIVER );
    }
    else
    {
      queryParams.put( GIVER_RECEIVER, TYPE_RECEIVER );
    }
    return queryParams;
  }

  public boolean isSortTeamMembers()
  {
    return sortTeamMembers;
  }

  public void setSortTeamMembers( boolean sortTeamMembers )
  {
    this.sortTeamMembers = sortTeamMembers;
  }

  public String getNavigateTo()
  {
    return navigateTo;
  }

  public void setNavigateTo( String navigateTo )
  {
    this.navigateTo = navigateTo;
  }

}
