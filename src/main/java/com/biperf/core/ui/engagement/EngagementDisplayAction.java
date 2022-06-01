
package com.biperf.core.ui.engagement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.EngagementChartValueBean;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.EngagementRecognizedParticipantValueBean;
import com.biperf.core.value.EngagementSummaryValueBean;
import com.biperf.core.value.EngagementTeamMembersValueBean;
import com.biperf.core.value.EngagementTeamsValueBean;
import com.biperf.util.StringUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * EngagementDisplayAction.
 * 
 * @author kandhi
 * @since May 23, 2014
 * @version 1.0
 */
public class EngagementDisplayAction extends BaseDispatchAction
{
  private static final String PAX_REC_TO = "paxRecTo";
  private static final String TYPE_RECEIVER = "receiver";
  private static final String TYPE = "type";
  private static final String COUNT = "count";
  private static final String CHART_VALUE_BEAN_LIST = "chartValueBeanList";
  private static final String GIVER_RECEIVER = "giverReceiver";
  private static final String MEMBERS_TYPE = "membersType";
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
  private static final String RECEIVER = "recvr";
  private static final String START_DATE = "startDate";
  private static final String END_DATE = "endDate";
  private static final String TIMEFRAME_TYPE = "timeframeType";
  private static final String MODE = "mode";
  private static final String MONTH = "month";
  private static final String NODE_NAME = "nodeName";
  private static final String DRILLDOWN = "_drillDown";
  private static final String TIME_FRAME = "timeframe";
  private boolean sortTeamMembers = false;
  private String navigateTo = "";

  /**
   * Loads the manager and participant tile data
   * mode will decide whether the data is for the manager or the participant
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchTileData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = populateQueryParams( request );

    EngagementSummaryValueBean engagementSummaryValueBean = getEngagementService()
        .getSummaryData( (String)queryParams.get( MODE ),
                         UserManager.getUserId(),
                         UserManager.getLocale(),
                         (String)queryParams.get( TIMEFRAME_TYPE ),
                         (Integer)queryParams.get( END_MONTH ),
                         (Integer)queryParams.get( END_YEAR ) );

    EngagementView view = new EngagementView( (String)queryParams.get( MODE ),
                                              UserManager.getUserId(),
                                              (String)queryParams.get( TIMEFRAME_TYPE ),
                                              (Integer)DateUtils.getMonthFromDate( (Date)queryParams.get( END_DATE ) ),
                                              (Integer)DateUtils.getYearFromDate( (Date)queryParams.get( END_DATE ) ),
                                              engagementSummaryValueBean,
                                              (Date)queryParams.get( START_DATE ),
                                              (Date)queryParams.get( END_DATE ) );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  /**
   * Display the team dash board page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward displayDashboardPage( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<String> eligiblePromotionList = getEngagementService().getAllEligiblePromotions();
    request.setAttribute( "eligiblePromotionList", eligiblePromotionList );
    return mapping.findForward( "team_dashboard_page" );
  }

  /**
   * Fetch team dash board data or profile dash board data based on the mode
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchDashboardData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = populateQueryParams( request );
    if ( EngagementView.TEAM.equals( (String)queryParams.get( MODE ) ) )
    {
      fetchTeamDashboardData( request, response, queryParams );
    }
    else if ( EngagementView.USER.equals( (String)queryParams.get( MODE ) ) )
    {
      fetchProfileDashboardTabData( request, response, queryParams );
    }
    return null;
  }

  /**
   * Fetches the team dash board data
   */
  private void fetchTeamDashboardData( HttpServletRequest request, HttpServletResponse response, Map<String, Object> queryParams ) throws Exception
  {
    EngagementDashboardValueBean engagementDashboardValueBean = getEngagementService().getDashboardTeamData( queryParams );

    EngagementView view = new EngagementView( (String)queryParams.get( MODE ),
                                              UserManager.getUserId(),
                                              (String)queryParams.get( TIMEFRAME_TYPE ),
                                              (Integer)DateUtils.getMonthFromDate( (Date)queryParams.get( END_DATE ) ),
                                              (Integer)DateUtils.getYearFromDate( (Date)queryParams.get( END_DATE ) ),
                                              engagementDashboardValueBean,
                                              (Date)queryParams.get( START_DATE ),
                                              (Date)queryParams.get( END_DATE ),
                                              (String)queryParams.get( NODE_IDS ),
                                              (boolean)queryParams.get( DRILLDOWN ),
                                              (String)queryParams.get( NODE_NAME ) );

    // When the summary is null then set the error message
    if ( engagementDashboardValueBean == null || engagementDashboardValueBean.getEngagementSummaryValueBean() == null )
    {
      createMessages( response, view );
    }

    super.writeAsJsonToResponse( view, response );
  }

  /**
   * Fetches the profile page engagement user dash board data  
   */
  private void fetchProfileDashboardTabData( HttpServletRequest request, HttpServletResponse response, Map<String, Object> queryParams ) throws Exception
  {
    EngagementDashboardValueBean engagementDashboardValueBean = getEngagementService().getDashboardUserData( queryParams );

    EngagementView view = new EngagementView( (String)queryParams.get( MODE ),
                                              (Long)queryParams.get( USER_ID ),
                                              (String)queryParams.get( TIMEFRAME_TYPE ),
                                              (Integer)DateUtils.getMonthFromDate( (Date)queryParams.get( END_DATE ) ),
                                              (Integer)DateUtils.getYearFromDate( (Date)queryParams.get( END_DATE ) ),
                                              engagementDashboardValueBean,
                                              (Date)queryParams.get( START_DATE ),
                                              (Date)queryParams.get( END_DATE ),
                                              (String)queryParams.get( NODE_IDS ),
                                              (boolean)queryParams.get( DRILLDOWN ),
                                              (String)queryParams.get( NODE_NAME ) );

    // When the summary is null then set the error message
    if ( engagementDashboardValueBean == null || engagementDashboardValueBean.getEngagementSummaryValueBean() == null )
    {
      createMessages( response, view );
    }

    super.writeAsJsonToResponse( view, response );
  }

  private void createMessages( HttpServletResponse response, EngagementView view ) throws IOException
  {
    List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();
    WebErrorMessage message = new WebErrorMessage();
    message.setType( "error" );
    String messageText = CmsResourceBundle.getCmsBundle().getString( "engagement.participant", "NO_DATA_FOUND_ERROR" );
    message.setText( messageText );
    messages.add( message );
    view.setMessages( messages );
  }

  public ActionForward getRecognitionsSentByPromoChartData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = populateQueryParams( request );
    queryParams.put( GIVER_RECEIVER, GIVER );
    List<EngagementChartValueBean> valueBeanList = getEngagementService().getRecognitionsByPromoChartData( queryParams );
    boolean chartHasValues = false;
    if ( valueBeanList != null )
    {
      for ( EngagementChartValueBean chartValueBean : valueBeanList )
      {
        if ( chartValueBean != null && chartValueBean.getValue() > 0 )
        {
          chartHasValues = true;
          break;
        }
      }
    }
    request.setAttribute( "chartHasValues", chartHasValues );
    request.setAttribute( CHART_VALUE_BEAN_LIST, valueBeanList );
    return mapping.findForward( "display_recognitions_sent_by_promo_chart" );
  }

  public ActionForward getRecognitionsReceivedByPromoChartData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = populateQueryParams( request );
    queryParams.put( GIVER_RECEIVER, RECEIVER );
    List<EngagementChartValueBean> valueBeanList = getEngagementService().getRecognitionsByPromoChartData( queryParams );
    boolean chartHasValues = false;
    if ( valueBeanList != null )
    {
      for ( EngagementChartValueBean chartValueBean : valueBeanList )
      {
        if ( chartValueBean != null && chartValueBean.getValue() > 0 )
        {
          chartHasValues = true;
          break;
        }
      }
    }
    request.setAttribute( "chartHasValues", chartHasValues );
    request.setAttribute( CHART_VALUE_BEAN_LIST, valueBeanList );
    return mapping.findForward( "display_recognitions_received_by_promo_chart" );
  }

  public ActionForward getTeamRecognitionsReceivedChartData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = populateQueryParams( request );
    queryParams.put( GIVER_RECEIVER, RECEIVER );
    List<EngagementChartValueBean> valueBeanList = getEngagementService().getUniqueRecognitionsChartData( queryParams );
    request.setAttribute( CHART_VALUE_BEAN_LIST, valueBeanList );
    return mapping.findForward( "display_unique_recognitions_pax_received_chart" );
  }

  public ActionForward getTeamRecognitionsSentChartData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> queryParams = populateQueryParams( request );
    queryParams.put( GIVER_RECEIVER, GIVER );
    List<EngagementChartValueBean> valueBeanList = getEngagementService().getUniqueRecognitionsChartData( queryParams );
    request.setAttribute( CHART_VALUE_BEAN_LIST, valueBeanList );
    return mapping.findForward( "display_unique_recognitions_pax_sent_chart" );
  }

  public ActionForward getLoginVisitsChartData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    Map<String, String> commaSuported = new HashMap<String, String>();
    commaSuported.put( "pl_PL", "pl_PL" );
    commaSuported.put( "fr_CA", "fr_CA" );
    commaSuported.put( "es_ES", "es_ES" );
    commaSuported.put( "it_IT", "it_IT" );
    commaSuported.put( "nl_NL", "nl_NL" );
    commaSuported.put( "vi_VN", "vi_VN" );
    commaSuported.put( "fr_FR", "fr_FR" );
    commaSuported.put( "de_DE", "de_DE" );
    commaSuported.put( "tr_TR", "tr_TR" );

    request.setAttribute( "commaSuported", commaSuported );
    Map<String, Object> queryParams = populateQueryParams( request );
    List<EngagementChartValueBean> valueBeanList = getEngagementService().getLoginVisitsChartData( queryParams );
    request.setAttribute( CHART_VALUE_BEAN_LIST, valueBeanList );
    request.setAttribute( TIME_FRAME, request.getParameter( "timeframeType" ) );
    return mapping.findForward( "display_login_visits_chart" );
  }

  public ActionForward sortTeamsOrTeamMembers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String timeframeNavigate = request.getParameter( TIMEFRAME_NAVIGATE );
    this.setSortTeamMembers( true );
    this.setNavigateTo( timeframeNavigate );

    Map<String, Object> queryParams = populateQueryParams( request );
    String sortType = request.getParameter( MEMBERS_TYPE );
    if ( EngagementView.USER.equals( sortType ) )
    {
      List<EngagementTeamMembersValueBean> engagementTeamMembersValueBeanList = getEngagementService().sortTeamMembers( queryParams );
      EngagementTeamIndividualsView individuals = new EngagementTeamIndividualsView( engagementTeamMembersValueBeanList,
                                                                                     Integer.parseInt( request.getParameter( COUNT ) ),
                                                                                     (Integer)queryParams.get( PAGE_NUMBER ),
                                                                                     request.getParameter( SORTED_ON ),
                                                                                     request.getParameter( SORTED_BY ) );
      EngagementIndividualsView view = new EngagementIndividualsView( individuals );
      super.writeAsJsonToResponse( view, response );
    }
    else if ( EngagementView.TEAM.equals( sortType ) )
    {
      List<EngagementTeamsValueBean> engagementTeamsValueBeanList = getEngagementService().sortTeams( queryParams );
      EngagementTeamTeamsView teams = new EngagementTeamTeamsView( engagementTeamsValueBeanList,
                                                                   Integer.parseInt( request.getParameter( COUNT ) ),
                                                                   (Integer)queryParams.get( PAGE_NUMBER ),
                                                                   request.getParameter( SORTED_ON ),
                                                                   request.getParameter( SORTED_BY ) );
      EngagementTeamsView view = new EngagementTeamsView( teams );
      super.writeAsJsonToResponse( view, response );
    }
    return null;
  }

  /**
   * Chart specific params
   * @param request
   * @return
   */
  private Map<String, Object> populateQueryParams( HttpServletRequest request )
  {
    int timeframeMonthId = 0;
    Date currentDate = DateUtils.getCurrentDate();
    String mode = StringUtils.isEmpty( request.getParameter( MODE ) ) ? EngagementView.USER : request.getParameter( MODE );
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

    // Calculate the rolling period start date and the end date
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

  /**
   * Fetches the engagement Recognized 
   *  * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchEngagementRecognized( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String avatarUrl;
    Map<String, Object> queryParams = new HashMap<String, Object>();
    EngagementRecognizedView view = new EngagementRecognizedView();
    EngagementRecognizedTreeView engagementRecognizedTreeView = new EngagementRecognizedTreeView();
    EngagementRecognizedRootView engagementRecognizedRootView = null;

    queryParams = populateQueryParams( request );
    Map<String, Object> output = getEngagementService().getEngagementRecognized( queryParams );

    List<EngagementRecognizedParticipantValueBean> paxValueBeanList = (List<EngagementRecognizedParticipantValueBean>)output.get( "p_out_data" );

    if ( paxValueBeanList.size() > 0 )
    {
      List<EngagementRecognizedParticipantView> paxViewBeanList = new ArrayList<EngagementRecognizedParticipantView>();
      for ( EngagementRecognizedParticipantValueBean pax : paxValueBeanList )
      {
        EngagementRecognizedParticipantView viewbean = new EngagementRecognizedParticipantView();
        viewbean.setAvatarUrl( pax.getAvatarUrl() );
        viewbean.setId( pax.getId() );
        viewbean.setCount( pax.getCount() );
        viewbean.setName( pax.getName() );
        viewbean.setNodeId( pax.getNodeId() );
        viewbean.setNodeName( pax.getNodeName() );
        paxViewBeanList.add( viewbean );
      }

      engagementRecognizedTreeView.setParticipants( paxViewBeanList );

      if ( EngagementView.TEAM.equals( (String)queryParams.get( MODE ) ) )
      {
        List<EngagementRecognizedParticipantValueBean> nodesList = (List<EngagementRecognizedParticipantValueBean>)output.get( "p_out_node_names" );

        for ( EngagementRecognizedParticipantValueBean nodesBean : nodesList )
        {
          engagementRecognizedRootView = new EngagementRecognizedRootView( nodesBean.getNodeId(), null, nodesBean.getNodeName(), null );
        }
      }
      else
      {
        Long userId = (Long)queryParams.get( USER_ID );
        String viewerFirstName = (String)output.get( "p_out_viewer_first_name" );
        String viewerAvatarUrl = (String)output.get( "p_out_viewer_avatar_url" );
        avatarUrl = viewerAvatarUrl;

        engagementRecognizedRootView = new EngagementRecognizedRootView( null, userId, viewerFirstName, avatarUrl );
      }
      engagementRecognizedTreeView.setRoot( engagementRecognizedRootView );

      view.setTree( engagementRecognizedTreeView );
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  private EngagementService getEngagementService()
  {
    return (EngagementService)getService( EngagementService.BEAN_NAME );
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
