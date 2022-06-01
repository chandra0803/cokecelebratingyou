
package com.biperf.core.ui.engagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.biperf.core.domain.enums.EngagementSummaryType;
import com.biperf.core.domain.gamification.BadgesReceivedCountComparator;
import com.biperf.core.domain.gamification.BadgesSentCountComparator;
import com.biperf.core.domain.promotion.EngagementPromotion;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.EngagementAveragesValueBean;
import com.biperf.core.value.EngagementBehaviorValueBean;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.EngagementSummaryValueBean;
import com.biperf.core.value.EngagementTeamSumValueBean;
import com.biperf.core.value.NodeBean;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * EngagementKPMView.
 * 
 * @author kandhi
 * @since May 20, 2014
 * @version 1.0
 */
public class EngagementKPMView
{
  private static final String DEFAULT_SORT_MEMBER = "member";
  private static final String DEFAULT_SORT_ASC = "asc";
  private String mode;
  private Long userId;
  private String userName;
  private List<EngagementNodeView> nodes;
  private String timeframeType;
  private int timeframeMonthId;
  private int timeframeYear;
  private String timeframeName;
  private String asof;
  private int teamMemCount;
  private List<EngagementSummaryView> summary;
  private List<EngagementDetailView> detail;
  private EngagementTeamView team;
  private boolean isScoreActive;
  private boolean areTargetsShown;

  /**
   * Constructor for the tile
   * @param mode
   * @param userId
   * @param timeframeType
   * @param timeframeMonthId
   * @param timeframeYear
   * @param engagementSummaryValueBean
   */
  public EngagementKPMView( String mode,
                            Long userId,
                            String timeframeType,
                            int timeframeMonthId,
                            int timeframeYear,
                            EngagementSummaryValueBean engagementSummaryValueBean,
                            Date startDate,
                            Date endDate )
  {
    super();
    this.mode = mode;
    this.userId = userId;
    this.timeframeType = timeframeType;
    this.timeframeMonthId = timeframeMonthId;
    this.timeframeYear = timeframeYear;
    this.isScoreActive = engagementSummaryValueBean.isScoreActive();
    this.areTargetsShown = displayTarget( engagementSummaryValueBean );
    this.asof = populateAsOf( engagementSummaryValueBean );
    this.timeframeName = getDisplayTimeFrameName( timeframeMonthId, timeframeYear );
    this.summary = populateSummaryDetails( engagementSummaryValueBean, null, startDate, endDate, null );
  }

  /**
   * Constructor for the dash board
   * @param mode
   * @param userId
   * @param timeframeType
   * @param timeframeMonthId
   * @param timeframeYear
   * @param engagementDashboardValueBean
   * @param startDate
   * @param endDate
   */
  public EngagementKPMView( String mode,
                            Long userId,
                            String timeframeType,
                            int timeframeMonthId,
                            int timeframeYear,
                            EngagementDashboardValueBean engagementDashboardValueBean,
                            Date startDate,
                            Date endDate,
                            String selectedNodes,
                            boolean isDrilldown,
                            String nodeName )
  {
    super();
    this.mode = mode;
    this.userId = userId;
    this.timeframeType = timeframeType;
    this.timeframeMonthId = timeframeMonthId;
    this.timeframeYear = timeframeYear;
    if ( engagementDashboardValueBean != null )
    {
      this.userName = engagementDashboardValueBean.getUserName();
      this.isScoreActive = engagementDashboardValueBean.getEngagementSummaryValueBean() != null ? engagementDashboardValueBean.getEngagementSummaryValueBean().isScoreActive() : false;
      this.areTargetsShown = displayTarget( engagementDashboardValueBean.getEngagementSummaryValueBean() );
      this.asof = populateAsOf( engagementDashboardValueBean.getEngagementSummaryValueBean() );
      this.summary = populateSummaryDetails( engagementDashboardValueBean.getEngagementSummaryValueBean(),
                                             engagementDashboardValueBean.getEngagementAveragesValueBean(),
                                             startDate,
                                             endDate,
                                             selectedNodes );
      this.nodes = populateNodes( engagementDashboardValueBean, selectedNodes, isDrilldown, nodeName );
      this.teamMemCount = engagementDashboardValueBean.getEngagementSummaryValueBean() != null ? engagementDashboardValueBean.getEngagementSummaryValueBean().getTotalParticipantCount() : 0;
      this.detail = populateDetailView( engagementDashboardValueBean );
      this.team = populateTeamView( engagementDashboardValueBean );
    }
    this.timeframeName = getDisplayTimeFrameName( timeframeType, startDate, endDate );
  }

  private boolean displayTarget( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    boolean displaytarget = false;
    if ( engagementSummaryValueBean != null )
    {
      if ( engagementSummaryValueBean.isScoreActive() )
      {
        if ( EngagementView.USER.equals( mode ) )
        {
          // Only for user mode we need to consider this flag
          displaytarget = engagementSummaryValueBean.isDisplayTarget();
        }
        else
        {
          // The targets are always displayed in team mode as long as the score is active.
          displaytarget = true;
        }
      }
    }
    return displaytarget;
  }

  /**
   * Populate the team view when the mode is team
   * @param engagementDashboardValueBean
   * @return
   */
  private EngagementTeamView populateTeamView( EngagementDashboardValueBean engagementDashboardValueBean )
  {
    if ( EngagementView.TEAM.equals( mode ) )
    {
      EngagementTeamModelView engagementTeamModelView = getTeamModelView( engagementDashboardValueBean );

      EngagementTeamIndividualsView individuals = new EngagementTeamIndividualsView( engagementDashboardValueBean.getEngagementTeamMembersValueBeanList(),
                                                                                     engagementDashboardValueBean.getEngagementSummaryValueBean().getTotalMembersAvailable(),
                                                                                     1,
                                                                                     DEFAULT_SORT_MEMBER,
                                                                                     DEFAULT_SORT_ASC );

      EngagementTeamTeamsView teams = new EngagementTeamTeamsView( engagementDashboardValueBean.getEngagementTeamsValueBeanList(),
                                                                   engagementDashboardValueBean.getEngagementSummaryValueBean().getTotalTeamsAvailable(),
                                                                   1,
                                                                   DEFAULT_SORT_MEMBER,
                                                                   DEFAULT_SORT_ASC );

      team = new EngagementTeamView( engagementTeamModelView, individuals, teams );
    }
    return team;
  }

  /**
   * Populate Model View in Team
   * @param engagementDashboardValueBean
   * @return
   */
  protected EngagementTeamModelView getTeamModelView( EngagementDashboardValueBean engagementDashboardValueBean )
  {
    List<EngagementTeamModelDataView> data = new ArrayList<EngagementTeamModelDataView>();
    if ( engagementDashboardValueBean.getEngagementTeamSumValueBeanList() != null && engagementDashboardValueBean.getEngagementTeamSumValueBeanList().size() > 0 )
    {
      for ( EngagementTeamSumValueBean engagementTeamSumValueBean : engagementDashboardValueBean.getEngagementTeamSumValueBeanList() )
      {
        EngagementTeamModelDataView engagementTeamModelDataView = null;
        if ( engagementTeamSumValueBean != null )
        {
          if ( isScoreActive )
          {
            engagementTeamModelDataView = new EngagementTeamModelDataView( engagementTeamSumValueBean.getCompanyGoal(),
                                                                           engagementTeamSumValueBean.getScore(),
                                                                           EngagementSummaryType.PARTICIPATION_SCORE );
            data.add( engagementTeamModelDataView );
          }
          if ( displayRecognitionSent( engagementDashboardValueBean.getEngagementSummaryValueBean() ) )
          {
            engagementTeamModelDataView = new EngagementTeamModelDataView( engagementTeamSumValueBean.getSentTarget(),
                                                                           engagementTeamSumValueBean.getSentCnt(),
                                                                           EngagementSummaryType.RECOGNITIONS_SENT );
            data.add( engagementTeamModelDataView );
          }
          if ( displayRecognitionReceived( engagementDashboardValueBean.getEngagementSummaryValueBean() ) )
          {
            engagementTeamModelDataView = new EngagementTeamModelDataView( engagementTeamSumValueBean.getReceivedTarget(),
                                                                           engagementTeamSumValueBean.getReceivedCnt(),
                                                                           EngagementSummaryType.RECOGNITIONS_RECEIVED );
            data.add( engagementTeamModelDataView );
          }
          if ( displayUniqueRecognitionSent( engagementDashboardValueBean.getEngagementSummaryValueBean() ) )
          {
            engagementTeamModelDataView = new EngagementTeamModelDataView( engagementTeamSumValueBean.getConnectedToTarget(),
                                                                           engagementTeamSumValueBean.getConnectedToCnt(),
                                                                           EngagementSummaryType.RECOGNIZED );
            data.add( engagementTeamModelDataView );
          }
          if ( displayUniqueRecognitionReceived( engagementDashboardValueBean.getEngagementSummaryValueBean() ) )
          {
            engagementTeamModelDataView = new EngagementTeamModelDataView( engagementTeamSumValueBean.getConnectedFromTarget(),
                                                                           engagementTeamSumValueBean.getConnectedFromCnt(),
                                                                           EngagementSummaryType.RECOGNIZED_BY );
            data.add( engagementTeamModelDataView );
          }
          if ( displayLoginActivity( engagementDashboardValueBean.getEngagementSummaryValueBean() ) )
          {
            engagementTeamModelDataView = new EngagementTeamModelDataView( engagementTeamSumValueBean.getLoginActivityTarget(),
                                                                           engagementTeamSumValueBean.getLoginActivityCnt(),
                                                                           EngagementSummaryType.VISITS );
            data.add( engagementTeamModelDataView );
          }
        }
      }
    }
    EngagementTeamModelView engagementTeamModelView = new EngagementTeamModelView( data );
    return engagementTeamModelView;
  }

  private boolean displayUniqueRecognitionSent( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    EngagementPromotion engagementPromotion = new EngagementPromotion();
    engagementPromotion.setSelectedBenchmarks( engagementSummaryValueBean.getSelectedBenchmarks() );
    if ( isScoreActive )
    {
      return true;
    }
    else
    {
      return engagementPromotion.isUniqueRecognitionSent();
    }
  }

  private boolean displayUniqueRecognitionReceived( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    EngagementPromotion engagementPromotion = new EngagementPromotion();
    engagementPromotion.setSelectedBenchmarks( engagementSummaryValueBean.getSelectedBenchmarks() );
    if ( isScoreActive )
    {
      return true;
    }
    else
    {
      return engagementPromotion.isUniqueRecognitionReceived();
    }
  }

  private boolean displayRecognitionSent( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    EngagementPromotion engagementPromotion = new EngagementPromotion();
    engagementPromotion.setSelectedBenchmarks( engagementSummaryValueBean.getSelectedBenchmarks() );
    if ( isScoreActive )
    {
      return true;
    }
    else
    {
      return engagementPromotion.isRecognitionSent();
    }
  }

  private boolean displayRecognitionReceived( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    EngagementPromotion engagementPromotion = new EngagementPromotion();
    engagementPromotion.setSelectedBenchmarks( engagementSummaryValueBean.getSelectedBenchmarks() );
    if ( isScoreActive )
    {
      return true;
    }
    else
    {
      return engagementPromotion.isRecognitionReceived();
    }
  }

  private boolean displayLoginActivity( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    EngagementPromotion engagementPromotion = new EngagementPromotion();
    engagementPromotion.setSelectedBenchmarks( engagementSummaryValueBean.getSelectedBenchmarks() );
    if ( isScoreActive )
    {
      return true;
    }
    else
    {
      return engagementPromotion.isLoginActivity();
    }
  }

  /**
   * Populate the detail view of the JSON
   * @param engagementDashboardValueBean
   * @return
   */
  private List<EngagementDetailView> populateDetailView( EngagementDashboardValueBean engagementDashboardValueBean )
  {
    String sysUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    detail = new ArrayList<EngagementDetailView>();
    EngagementSummaryValueBean engagementSummaryValueBean = engagementDashboardValueBean.getEngagementSummaryValueBean();
    if ( engagementSummaryValueBean != null )
    {
      if ( isScoreActive )
      {
        // Detail score section
        EngagementRecognitionSentView recSent = new EngagementRecognitionSentView( engagementSummaryValueBean.getSentCnt(),
                                                                                   engagementSummaryValueBean.getSentTarget(),
                                                                                   EngagementSummaryType.RECOGNITIONS_SENT,
                                                                                   EngagementSummaryType.lookup( EngagementSummaryType.RECOGNITIONS_SENT ).getName() );
        EngagementRecognitionReceivedView recRecv = new EngagementRecognitionReceivedView( engagementSummaryValueBean.getReceivedCnt(),
                                                                                           engagementSummaryValueBean.getReceivedTarget(),
                                                                                           EngagementSummaryType.lookup( EngagementSummaryType.RECOGNITIONS_RECEIVED ).getName(),
                                                                                           EngagementSummaryType.RECOGNITIONS_RECEIVED );
        String recognizedLabel = null;
        String teamAverageText = null;

        if ( EngagementView.USER.equals( mode ) )
        {
          recognizedLabel = CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_I_RECOGNIZED" );
        }
        else
        {
          recognizedLabel = CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_TEAM_RECOGNIZED_BASED_AVG" );
          teamAverageText = CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TEAM_AVERAGE_DESC" );
        }
        EngagementUniqueRecognitionReceivedView paxRecTo = new EngagementUniqueRecognitionReceivedView( engagementSummaryValueBean.getConnectedToCnt(),
                                                                                                        engagementSummaryValueBean.getConnectedToTarget(),
                                                                                                        EngagementSummaryType.RECOGNIZED,
                                                                                                        recognizedLabel,
                                                                                                        teamAverageText );
        String recognizedByLabel = null;
        if ( EngagementView.USER.equals( mode ) )
        {
          recognizedByLabel = CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_RECOGNIZED_BY" );
        }
        else
        {
          recognizedByLabel = CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_RECOGNIZED_BY_TEAM" );
          teamAverageText = CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TEAM_AVERAGE_DESC" );
        }

        EngagementUniqueRecognitionSentView paxRecBy = new EngagementUniqueRecognitionSentView( engagementSummaryValueBean.getConnectedFromCnt(),
                                                                                                engagementSummaryValueBean.getConnectedFromTarget(),
                                                                                                EngagementSummaryType.RECOGNIZED_BY,
                                                                                                recognizedByLabel,
                                                                                                teamAverageText );
        EngagementSiteVisitsView visits = new EngagementSiteVisitsView( engagementSummaryValueBean.getLoginActivityCnt(),
                                                                        engagementSummaryValueBean.getLoginActivityTarget(),
                                                                        EngagementSummaryType.VISITS,
                                                                        CmsResourceBundle.getCmsBundle().getString( "engagement.participant.SITE_VISITS" ) );

        EngagementDetailDataView engagementScoreDetailDataView = new EngagementDetailDataView( recSent, recRecv, paxRecTo, paxRecBy, visits );
        EngagementDetailView engagementScoreDetailView = new EngagementDetailView( EngagementSummaryType.PARTICIPATION_SCORE, engagementScoreDetailDataView );
        detail.add( engagementScoreDetailView );
      }

      // Detail recSent Section
      if ( displayRecognitionSent( engagementSummaryValueBean ) )
      {
        List<EngagementRecognitionByBehaviorView> byBehavior = new ArrayList<EngagementRecognitionByBehaviorView>();
        if ( engagementDashboardValueBean.getEngagementBehaviorValueBeanList() != null )
        {
          int index = 0;

          List<EngagementBehaviorValueBean> engagementBehaviorValueBeanList = engagementDashboardValueBean.getEngagementBehaviorValueBeanList();
          Collections.sort( engagementBehaviorValueBeanList, new BadgesSentCountComparator() );

          for ( EngagementBehaviorValueBean engagementBehaviorValueBean : engagementBehaviorValueBeanList )
          {
            String imageUrl = null;
            if ( !StringUtil.isNullOrEmpty( engagementBehaviorValueBean.getBadgeImageUrl() ) )
            {
              imageUrl = sysUrl + engagementBehaviorValueBean.getBadgeImageUrl();
            }
            byBehavior.add( new EngagementRecognitionByBehaviorView( index++, engagementBehaviorValueBean.getBehavior(), engagementBehaviorValueBean.getSentCnt(), imageUrl ) );
          }
        }
        String recognitionsUrl = "publicRecognitionResult.do?method=fetchRecognitionsForEngagementDashboard";

        EngagementDetailDataView engagementRecSentDetailDataView = new EngagementDetailDataView( null, byBehavior, recognitionsUrl );
        EngagementDetailView engagementRecSentDetailView = new EngagementDetailView( EngagementSummaryType.RECOGNITIONS_SENT, engagementRecSentDetailDataView );
        detail.add( engagementRecSentDetailView );
      }

      // Detail recRecv Section
      if ( displayRecognitionReceived( engagementSummaryValueBean ) )
      {
        List<EngagementRecognitionByBehaviorView> byBehavior = new ArrayList<EngagementRecognitionByBehaviorView>();
        if ( engagementDashboardValueBean.getEngagementBehaviorValueBeanList() != null )
        {
          int index = 0;

          List<EngagementBehaviorValueBean> engagementBehaviorValueBeanList = engagementDashboardValueBean.getEngagementBehaviorValueBeanList();
          Collections.sort( engagementBehaviorValueBeanList, new BadgesReceivedCountComparator() );

          for ( EngagementBehaviorValueBean engagementBehaviorValueBean : engagementBehaviorValueBeanList )
          {
            String imageUrl = null;
            if ( !StringUtil.isNullOrEmpty( engagementBehaviorValueBean.getBadgeImageUrl() ) )
            {
              imageUrl = sysUrl + engagementBehaviorValueBean.getBadgeImageUrl();
            }
            byBehavior.add( index++, new EngagementRecognitionByBehaviorView( index, engagementBehaviorValueBean.getBehavior(), engagementBehaviorValueBean.getReceivedCnt(), imageUrl ) );
          }
        }
        String recognitionsUrl = "publicRecognitionResult.do?method=fetchRecognitionsForEngagementDashboard";

        EngagementDetailDataView engagementRecRecvDetailDataView = new EngagementDetailDataView( null, byBehavior, recognitionsUrl );
        EngagementDetailView engagementRecRecvDetailView = new EngagementDetailView( EngagementSummaryType.RECOGNITIONS_RECEIVED, engagementRecRecvDetailDataView );
        detail.add( engagementRecRecvDetailView );
      }

      // Detail paxRecTo Section
      if ( displayUniqueRecognitionSent( engagementSummaryValueBean ) )
      {
        String detailUrl = "engagementDisplay.do?method=fetchEngagementRecognized";
        String chartUrl = "engagementDisplay.do?method=getTeamRecognitionsSentChartData";
        EngagementChartUrlParamsView chartUrlParams = new EngagementChartUrlParamsView();
        chartUrlParams.setMode( mode );
        EngagementDetailDataView engagementPaxRecToDetailDataView = new EngagementDetailDataView( engagementSummaryValueBean.getConnectedToPaxCnt(), detailUrl, chartUrl, chartUrlParams );
        EngagementDetailView engagementPaxRecToDetailView = new EngagementDetailView( EngagementSummaryType.RECOGNIZED, engagementPaxRecToDetailDataView );
        detail.add( engagementPaxRecToDetailView );
      }
      // Detail paxRecBy Section
      if ( displayUniqueRecognitionReceived( engagementSummaryValueBean ) )
      {
        String detailUrl = "engagementDisplay.do?method=fetchEngagementRecognized";
        String chartUrl = "engagementDisplay.do?method=getTeamRecognitionsReceivedChartData";
        EngagementChartUrlParamsView chartUrlParams = new EngagementChartUrlParamsView();
        chartUrlParams.setMode( mode );
        EngagementDetailDataView engagementPaxRecByDetailDataView = new EngagementDetailDataView( engagementSummaryValueBean.getConnectedFromPaxCnt(), detailUrl, chartUrl, chartUrlParams );
        EngagementDetailView engagementPaxRecByDetailView = new EngagementDetailView( EngagementSummaryType.RECOGNIZED_BY, engagementPaxRecByDetailDataView );
        detail.add( engagementPaxRecByDetailView );
      }
      // Detail visits Section
      if ( displayLoginActivity( engagementSummaryValueBean ) )
      {
        if ( EngagementView.USER.equals( mode ) )
        {
          EngagementDetailDataView engagementPaxVisitsDetailDataView = new EngagementDetailDataView( engagementDashboardValueBean.getEngagementSiteVisitsLoginValueBeanList() );
          EngagementDetailView engagementVisitsDetailView = new EngagementDetailView( EngagementSummaryType.VISITS, engagementPaxVisitsDetailDataView );
          detail.add( engagementVisitsDetailView );
        }
        else
        {
          String chartUrl = "engagementDisplay.do?method=getLoginVisitsChartData";
          EngagementChartUrlParamsView chartUrlParams = new EngagementChartUrlParamsView();
          chartUrlParams.setMode( mode );
          EngagementDetailDataView engagementPaxVisitsDetailDataView = new EngagementDetailDataView( engagementSummaryValueBean.getLoginActivityCnt(), null, chartUrl, chartUrlParams );
          EngagementDetailView engagementVisitsDetailView = new EngagementDetailView( EngagementSummaryType.VISITS, engagementPaxVisitsDetailDataView );
          detail.add( engagementVisitsDetailView );
        }
      }
    }
    return detail;
  }

  /**
   * populate the as of date
   * Get the first one
   * @param engagementDashboardValueBean
   * @return
   */
  private String populateAsOfDate( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    String dateString = DateUtils.getDateTimeStringIn12HourPattern( engagementSummaryValueBean.getAsofDate(), UserManager.getLocale() );
    return dateString + " " + UserManager.getTimeZoneID();
  }

  /**
   * populate the as of date
   * Get the first one
   * @param engagementDashboardValueBean
   * @return
   */
  private String populateAsOf( EngagementSummaryValueBean engagementSummaryValueBean )
  {
    // Calling new method for conversion
    String timeString = "00:00 AM CST";
    try
    {
      timeString = DateUtils.convertTimeTo24HourFormatAndTimezone( engagementSummaryValueBean.getDate(), engagementSummaryValueBean.getTime(), engagementSummaryValueBean.getTimeZoneId() );
    }
    catch( ParseException e )
    {
      e.printStackTrace();
    }
    return engagementSummaryValueBean.getDate() + " " + timeString;
  }

  /**
   * The nodes
   * @param engagementDashboardValueBean
   * @return
   */
  private List<EngagementNodeView> populateNodes( EngagementDashboardValueBean engagementDashboardValueBean, String selectedNodes, boolean isDrillDown, String nodeName )
  {
    nodes = new ArrayList<EngagementNodeView>();
    if ( isDrillDown && !StringUtil.isNullOrEmpty( selectedNodes ) )
    {
      // For drill down the selectedNodes will contain only single node id
      EngagementNodeView nodeView = new EngagementNodeView( Long.parseLong( selectedNodes ), nodeName, true );
      nodes.add( nodeView );
    }
    else
    {
      // The nodes selected from UI is a comma separated string of nodeIds.
      List<String> selectedNodesList = new ArrayList<String>();
      if ( !StringUtil.isEmpty( selectedNodes ) )
      {
        selectedNodesList = Arrays.asList( selectedNodes.split( "," ) );
      }

      List<NodeBean> nodeBeanList = engagementDashboardValueBean.getManagerNodeBeanList();
      for ( NodeBean nodeBean : nodeBeanList )
      {
        boolean isNodeSelected = false;
        // For initial load mark all the parent nodes as selected. if user picks a node or a group
        // selected those as well.
        if ( selectedNodesList.isEmpty() || nodeBean.getId() != null && selectedNodesList.contains( String.valueOf( nodeBean.getId() ) ) )
        {
          isNodeSelected = true;
        }
        EngagementNodeView nodeView = new EngagementNodeView( nodeBean.getId(), nodeBean.getName(), isNodeSelected );
        nodes.add( nodeView );
      }
    }
    return nodes;
  }

  /**
   * This is for the tile on the home page. Ex. Jun 2014
   * @param month
   * @param year
   * @return
   */
  private String getDisplayTimeFrameName( int month, int year )
  {
    Calendar cal = Calendar.getInstance( UserManager.getLocale() );
    cal.setTimeZone( TimeZone.getTimeZone( UserManager.getTimeZoneID() ) );
    cal.getTime();
    return cal.getDisplayName( Calendar.MONTH, Calendar.SHORT, UserManager.getLocale() ) + " " + cal.get( Calendar.YEAR );
  }

  /**
   * This is for the rolling period name display on the dash board
   * @param timeframeType
   * @param startDate
   * @param endDate
   * @return
   */
  private String getDisplayTimeFrameName( String timeframeType, Date startDate, Date endDate )
  {
    String displayName = "";
    Calendar cal = Calendar.getInstance( UserManager.getLocale() );
    cal.setTime( startDate );
    displayName = cal.getDisplayName( Calendar.MONTH, Calendar.SHORT, UserManager.getLocale() ) + " " + cal.get( Calendar.YEAR );

    if ( "quarter".equals( timeframeType ) || "year".equals( timeframeType ) )
    {
      cal.setTime( endDate );
      displayName = displayName + " - " + cal.getDisplayName( Calendar.MONTH, Calendar.SHORT, UserManager.getLocale() ) + " " + cal.get( Calendar.YEAR );
    }
    return displayName;
  }

  /**
   * View for the summary section in the JSON
   * @param engagementSummaryValueBean
   * @return
   */
  private List<EngagementSummaryView> populateSummaryDetails( EngagementSummaryValueBean engagementSummaryValueBean,
                                                              EngagementAveragesValueBean engagementAveragesValueBean,
                                                              Date startDate,
                                                              Date endDate,
                                                              String selectedNodes )
  {
    summary = new ArrayList<EngagementSummaryView>();
    if ( engagementSummaryValueBean != null )
    {
      EngagementSummaryView engagementSummaryView = null;

      if ( isScoreActive )
      {
        engagementSummaryView = new EngagementSummaryView();
        engagementSummaryView.setType( EngagementSummaryType.PARTICIPATION_SCORE );
        engagementSummaryView.setActual( engagementSummaryValueBean.getScore() );
        if ( EngagementView.USER.equals( mode ) )
        {
          engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_SCORE_HELP" ) );
          engagementSummaryView.setActualLabel( "" );
          engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MY_PARTICIPATION_SCORE" ) );
        }
        else
        {
          engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TEAM_AVERAGE" ) );
          engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TEAM_PARTICIPATION_SCORE" ) );
          engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_SCORE_HELP" ) );
          engagementSummaryView.setReportTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.REPORT_DETAIL_EXTRACT" ) );
          engagementSummaryView.setReportUrl( getScoreReportUrl( startDate, endDate, selectedNodes ) );
          // If the large audience flag is true then the extract will be a JSON response with a
          // message that the report will be mailed otherwise the response will be a CSV.
          boolean isLargeAudienceEnabled = getSystemVariableService().getPropertyByName( SystemVariableService.ENABLE_LARGE_AUDIENCE_REPORT_GENERATION ).getBooleanVal();
          engagementSummaryView.setLargeAudienceEnabled( isLargeAudienceEnabled );
        }
        engagementSummaryView.setTarget( engagementSummaryValueBean.getCompanyGoal() );
        engagementSummaryView.setTargetLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.COMPANY_GOAL" ) );
        engagementSummaryView.setTeamMemMetTarget( engagementSummaryValueBean.getScoreAchievedCnt() );
        if ( engagementAveragesValueBean != null )
        {
          engagementSummaryView.setAvgCompany( engagementAveragesValueBean.getScoreCompanyAvg() );
        }
        summary.add( engagementSummaryView );
      }

      if ( displayRecognitionSent( engagementSummaryValueBean ) )
      {
        engagementSummaryView = new EngagementSummaryView();
        engagementSummaryView.setType( EngagementSummaryType.RECOGNITIONS_SENT );
        engagementSummaryView.setActual( engagementSummaryValueBean.getSentCnt() );
        engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.SENT" ) );
        if ( EngagementView.USER.equals( mode ) )
        {
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_REC_SENT_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_REC_SENT_HELP_WITHOUT_SCORE" ) );
          }
        }
        else
        {
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_REC_SENT_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_REC_SENT_HELP_WITHOUT_SCORE" ) );
          }
          engagementSummaryView.setReportTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.VIEW_REPORT" ) );
          engagementSummaryView.setReportUrl( getReportUrl( startDate, endDate, "4015" ) );// recognition-sent-by-org
        }
        engagementSummaryView.setTarget( engagementSummaryValueBean.getSentTarget() );
        engagementSummaryView.setTargetLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TARGET" ) );
        engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.RECOGNITION_SENT" ) );
        engagementSummaryView.setTeamMemMetTarget( engagementSummaryValueBean.getSentAchievedCnt() );
        if ( engagementAveragesValueBean != null )
        {
          engagementSummaryView.setAvgCompany( engagementAveragesValueBean.getRecSentCompanyAvg() );
          engagementSummaryView.setAvgTeamMem( engagementAveragesValueBean.getRecSentTeamAvg() );
        }
        summary.add( engagementSummaryView );
      }

      if ( displayRecognitionReceived( engagementSummaryValueBean ) )
      {
        engagementSummaryView = new EngagementSummaryView();
        engagementSummaryView.setType( EngagementSummaryType.RECOGNITIONS_RECEIVED );
        engagementSummaryView.setActual( engagementSummaryValueBean.getReceivedCnt() );
        engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.RECEIVED" ) );
        if ( EngagementView.USER.equals( mode ) )
        {
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_REC_RCVD_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_REC_RCVD_HELP_WITHOUT_SCORE" ) );
          }
        }
        else
        {
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_REC_RCVD_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_REC_RCVD_HELP_WITHOUT_SCORE" ) );
          }
          engagementSummaryView.setReportTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.VIEW_REPORT" ) );
          engagementSummaryView.setReportUrl( getReportUrl( startDate, endDate, "4016" ) ); // recognition-received-by-org
        }
        engagementSummaryView.setTarget( engagementSummaryValueBean.getReceivedTarget() );
        engagementSummaryView.setTargetLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TARGET" ) );
        engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.RECOGNITION_RECEIVED" ) );
        engagementSummaryView.setTeamMemMetTarget( engagementSummaryValueBean.getReceivedAchievedCnt() );
        if ( engagementAveragesValueBean != null )
        {
          engagementSummaryView.setAvgCompany( engagementAveragesValueBean.getRecRecvCompanyAvg() );
          engagementSummaryView.setAvgTeamMem( engagementAveragesValueBean.getRecRecvTeamAvg() );
        }
        summary.add( engagementSummaryView );
      }

      if ( displayUniqueRecognitionSent( engagementSummaryValueBean ) )
      {
        engagementSummaryView = new EngagementSummaryView();
        engagementSummaryView.setType( EngagementSummaryType.RECOGNIZED );
        engagementSummaryView.setActual( engagementSummaryValueBean.getConnectedToCnt() );
        engagementSummaryView.setTarget( engagementSummaryValueBean.getConnectedToTarget() );
        engagementSummaryView.setTargetLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TARGET" ) );
        if ( EngagementView.USER.equals( mode ) )
        {
          engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE" ) );
          engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_I_RECOGNIZED" ) );
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_TEAM_REC_TO_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_TEAM_REC_TO_HELP_WITHOUT_SCORE" ) );
          }
        }
        else
        {
          engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.AVERAGE" ) );
          engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_TEAM_RECOGNIZED" ) );
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_TEAM_REC_TO_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_TEAM_REC_TO_HELP_WITHOUT_SCORE" ) );
          }
        }
        engagementSummaryView.setTeamMemMetTarget( engagementSummaryValueBean.getConnectedToAchievedCnt() );
        if ( engagementAveragesValueBean != null )
        {
          engagementSummaryView.setAvgCompany( engagementAveragesValueBean.getConnectedToCompanyAvg() );
          engagementSummaryView.setAvgTeamMem( engagementAveragesValueBean.getConnectedToTeamAvg() );
        }
        summary.add( engagementSummaryView );
      }

      if ( displayUniqueRecognitionReceived( engagementSummaryValueBean ) )
      {
        engagementSummaryView = new EngagementSummaryView();
        engagementSummaryView.setType( EngagementSummaryType.RECOGNIZED_BY );
        engagementSummaryView.setActual( engagementSummaryValueBean.getConnectedFromCnt() );
        engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.DESCRIPTION" ) );
        engagementSummaryView.setTarget( engagementSummaryValueBean.getConnectedFromTarget() );
        engagementSummaryView.setTargetLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TARGET" ) );
        if ( EngagementView.USER.equals( mode ) )
        {
          engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE" ) );
          engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_RECOGNIZED_BY" ) );
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_TEAM_REC_BY_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_TEAM_REC_BY_HELP_WITHOUT_SCORE" ) );
          }
        }
        else
        {
          engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.AVERAGE" ) );
          engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PEOPLE_TEAM_RECOGNIZED_BY" ) );
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_TEAM_REC_BY_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_TEAM_REC_BY_HELP_WITHOUT_SCORE" ) );
          }
        }
        engagementSummaryView.setTeamMemMetTarget( engagementSummaryValueBean.getConnectedFromAchievedCnt() );
        if ( engagementAveragesValueBean != null )
        {
          engagementSummaryView.setAvgCompany( engagementAveragesValueBean.getConnectedFromCompanyAvg() );
          engagementSummaryView.setAvgTeamMem( engagementAveragesValueBean.getConnectedFromTeamAvg() );
        }
        summary.add( engagementSummaryView );
      }

      if ( displayLoginActivity( engagementSummaryValueBean ) )
      {
        engagementSummaryView = new EngagementSummaryView();
        engagementSummaryView.setType( EngagementSummaryType.VISITS );
        engagementSummaryView.setActual( engagementSummaryValueBean.getLoginActivityCnt() );
        engagementSummaryView.setActualLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.VISITS" ) );
        if ( EngagementView.USER.equals( mode ) )
        {
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_SITE_VISITS_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.PAX_SITE_VISITS_HELP_WITHOUT_SCORE" ) );
          }
        }
        else
        {
          if ( isScoreActive )
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_SITE_VISITS_HELP" ) );
          }
          else
          {
            engagementSummaryView.setDescription( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.MGR_SITE_VISITS_HELP_WITHOUT_SCORE" ) );
          }
          engagementSummaryView.setReportTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.VIEW_REPORT" ) );
          engagementSummaryView.setReportUrl( getReportUrl( startDate, endDate, "4001" ) );// login-activity-by-org
        }
        engagementSummaryView.setTarget( engagementSummaryValueBean.getLoginActivityTarget() );
        engagementSummaryView.setTargetLabel( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.TARGET" ) );
        engagementSummaryView.setTitle( CmsResourceBundle.getCmsBundle().getString( "engagement.participant.SITE_VISITS" ) );
        engagementSummaryView.setTeamMemMetTarget( engagementSummaryValueBean.getLoginAchievedCnt() );
        if ( engagementAveragesValueBean != null )
        {
          engagementSummaryView.setAvgCompany( engagementAveragesValueBean.getLoginActivityCompanyAvg() );
          engagementSummaryView.setAvgTeamMem( engagementAveragesValueBean.getLoginActivityTeamAvg() );
        }
        summary.add( engagementSummaryView );
      }
    }
    return summary;
  }

  private String getReportUrl( Date startDate, Date endDate, String reportId )
  {
    Map<String, String> clientStateParameterMap = new HashMap<String, String>();
    clientStateParameterMap.put( "isEngagement", "true" );
    clientStateParameterMap.put( "fromDate", DateUtils.toDisplayString( startDate ) );
    clientStateParameterMap.put( "toDate", DateUtils.toDisplayString( endDate ) );
    clientStateParameterMap.put( "reportId", reportId );
    return ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                 "/reports/allReports.do",
                                                 clientStateParameterMap );
  }

  private String getScoreReportUrl( Date startDate, Date endDate, String selectedNodes )
  {
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( "fromDate", DateUtils.toDisplayString( startDate ) );
    clientStateParameterMap.put( "toDate", DateUtils.toDisplayString( endDate ) );
    clientStateParameterMap.put( "timeframeType", timeframeType );
    clientStateParameterMap.put( "nodeId", selectedNodes );
    clientStateParameterMap.put( "userId", userId );
    return ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                 "/reports/displayEngagementScoreExtractReport.do?method=getParticipationScoreExtract",
                                                 clientStateParameterMap );
  }

  public String getMode()
  {
    return mode;
  }

  public void setMode( String mode )
  {
    this.mode = mode;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public List<EngagementNodeView> getNodes()
  {
    return nodes;
  }

  public void setNodes( List<EngagementNodeView> nodes )
  {
    this.nodes = nodes;
  }

  public String getTimeframeType()
  {
    return timeframeType;
  }

  public void setTimeframeType( String timeframeType )
  {
    this.timeframeType = timeframeType;
  }

  public int getTimeframeMonthId()
  {
    return timeframeMonthId;
  }

  public void setTimeframeMonthId( int timeframeMonthId )
  {
    this.timeframeMonthId = timeframeMonthId;
  }

  public int getTimeframeYear()
  {
    return timeframeYear;
  }

  public void setTimeframeYear( int timeframeYear )
  {
    this.timeframeYear = timeframeYear;
  }

  public String getTimeframeName()
  {
    return timeframeName;
  }

  public void setTimeframeName( String timeframeName )
  {
    this.timeframeName = timeframeName;
  }

  public String getAsof()
  {
    return asof;
  }

  public void setAsof( String asof )
  {
    this.asof = asof;
  }

  public int getTeamMemCount()
  {
    return teamMemCount;
  }

  public void setTeamMemCount( int teamMemCount )
  {
    this.teamMemCount = teamMemCount;
  }

  public List<EngagementSummaryView> getSummary()
  {
    return summary;
  }

  public void setSummary( List<EngagementSummaryView> summary )
  {
    this.summary = summary;
  }

  public List<EngagementDetailView> getDetail()
  {
    return detail;
  }

  public void setDetail( List<EngagementDetailView> detail )
  {
    this.detail = detail;
  }

  public EngagementTeamView getTeam()
  {
    return team;
  }

  public void setTeam( EngagementTeamView team )
  {
    this.team = team;
  }

  @JsonProperty( "isScoreActive" )
  public boolean isScoreActive()
  {
    return isScoreActive;
  }

  public void setScoreActive( boolean isScoreActive )
  {
    this.isScoreActive = isScoreActive;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public boolean isAreTargetsShown()
  {
    return areTargetsShown;
  }

  public void setAreTargetsShown( boolean areTargetsShown )
  {
    this.areTargetsShown = areTargetsShown;
  }

  protected static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
}
