
package com.biperf.core.ui.profile;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.gamification.GamificationBadgeTileView;
import com.biperf.core.domain.gamification.ParticipantBadge;
import com.biperf.core.domain.participant.AboutMe;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.domain.workhappier.WorkHappierScore;
import com.biperf.core.service.engagement.EngagementService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.ProfileService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.workhappier.WorkHappierService;
import com.biperf.core.ui.BaseControllerHelper;
import com.biperf.core.ui.ResponseEntity;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.ui.engagement.EngagementView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.workhappier.WhPastResultsViewBean;
import com.biperf.core.ui.workhappier.WorkHappierScoreViewBean;
import com.biperf.core.ui.workhappier.WorkHappinessDataViewBean;
import com.biperf.core.ui.workhappier.WorkHappinessSliderViewBean;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetMeter;
import com.biperf.core.value.BudgetMeterDetailBean;
import com.biperf.core.value.BudgetMeterDetailPromoBean;
import com.biperf.core.value.EngagementDashboardValueBean;
import com.biperf.core.value.PromotionMenuBean;

@Controller
@RequestMapping( "/profile" )
public class ProfileRPMController extends SpringBaseController
{
  private static final Log LOGGER = LogFactory.getLog( ProfileRPMController.class );

  private static final String TIMEFRAME_TYPE = "timeframeType";
  private static final String MODE = "mode";
  private static final int numberOfScores = 6;
  private static final String WH_IMAGES_PATH = "/assets/img/";
  private static final String PARTICIPANT_STATE = "SESSION_PARTICIPANT_STATE";
  private static final String TEAM = "team";
  private static final String USER = "user";
  private static final String END_DATE = "endDate";
  private static final String START_DATE = "startDate";
  private static final String NODE_IDS = "nodeId";
  private static final String DRILLDOWN = "_drillDown";
  private static final String NODE_NAME = "nodeName";
  private static final String USER_ID = "userId";

  @Autowired
  private SystemVariableService systemVariableService;
  @Autowired
  private BaseControllerHelper controllerHelper;

  @Autowired
  private EngagementService engagementService;

  @Autowired
  private GamificationService gamificationService;

  @Autowired
  private WorkHappierService workHappierService;

  @Autowired
  private UserService userService;

  @Autowired
  private ProfileService profileService;

  @Autowired
  private MainContentService mainContentService;

  private ProfileRPMActionHelper profileRPMActionHelper = new ProfileRPMActionHelper();

  /**
   * RPM Tab -  returns  all module data set.
   * @param request
   * @param profileRPMDTO
   * @return
   * @throws Exception 
   */
  @RequestMapping( value = "/rpm.action", method = RequestMethod.POST )
  public @ResponseBody ProfileRPMView fetchRPMData( HttpServletRequest request ) throws Exception
  {
    ProfileRPMView profileRPMView = new ProfileRPMView();
    profileRPMView.setBudgetMeter( getBudgetTrackerDetails( request ) );
    profileRPMView.setGamificationBadgeTileView( getBadgeDetails( request ) );
    profileRPMView.setEngagementView( getEngagementDetails( request ) );
    profileRPMView.setWhPastResultsViewBean( getWorkhappierDetails( request ) );
    return profileRPMView;
  }

  /**
   * My budget module 
   * @param request
   * @param profileRPMDTO
   * @return
   */
  @RequestMapping( value = "/budgetTracker.action", method = RequestMethod.POST )
  public @ResponseBody BudgetMeter getBudgetTrackerDetails( HttpServletRequest request )
  {
    BudgetMeter budgetMeter = new BudgetMeter();
    List<PromotionMenuBean> validPromotions = null;

    try
    {
      List<PromotionMenuBean> eligiblePromotions = controllerHelper.getEligiblePromotions( request );

      if ( isEmpty( eligiblePromotions ) )
      {
        return budgetMeter;
      }

      if ( isNotEmpty( eligiblePromotions ) )
      {
        validPromotions = eligiblePromotions.stream().filter( e -> e.isCanSubmit() ).collect( Collectors.toList() );
      }

      if ( isEmpty( validPromotions ) )
      {
        return budgetMeter;
      }

      budgetMeter = mainContentService.getBudgetMeter( UserManager.getUserId(), UserManager.getTimeZoneID(), validPromotions );
      //budgetMeter.getBudgetMeterDetails().stream().forEach( detailBean -> detailBean.getPromoList().forEach( promoBean -> promoBean.setUrl( buildUrl( promoBean.getPromoId(), request ) ) ) );
      String cheersPromoId = systemVariableService.getPropertyByName( SystemVariableService.COKE_CHEERS_PROMO_ID ).getStringVal();
      for(BudgetMeterDetailBean detailBean :budgetMeter.getBudgetMeterDetails( ))
      {
        List<BudgetMeterDetailPromoBean> promoList = detailBean.getPromoList();
        for(BudgetMeterDetailPromoBean promoBean : promoList)
        {
          promoBean.setUrl( buildUrl( promoBean.getPromoId(), request ) );
          if(StringUtils.isNotBlank( cheersPromoId ) && Long.valueOf( cheersPromoId ).equals( promoBean.getPromoId() ))
          {
            promoBean.setCheersPromotion( true );
          }
        }
      }
 

    }
    catch( Exception e )
    {
      LOGGER.debug( e );
      budgetMeter.setMessages( buildAppExceptionMessage() );
    }
    return budgetMeter;
  }

  /**
   * Engagement or dash board - view
   * @param request
   * @param profileRPMDTO
   * @return
   */
  @RequestMapping( value = "/dashboard.action", method = RequestMethod.POST )
  public @ResponseBody EngagementView getEngagementDetails( HttpServletRequest request )
  {
    try
    {
      Map<String, Object> queryParams = profileRPMActionHelper.populateQueryParams( request );
      return getEngagementDashboardValueBean( queryParams );
    }
    catch( Exception e )
    {
      LOGGER.debug( e );
      return new EngagementView( buildAppExceptionMessage() );
    }
  }

  /**
   * Badge module
   * @param request
   * @param profileRPMDTO
   * @return
   */
  @RequestMapping( value = "/badges.action", method = RequestMethod.POST )
  public @ResponseBody GamificationBadgeTileView getBadgeDetails( HttpServletRequest request )
  {
    try
    {
      return getGamificationBadgeTileView( request );
    }
    catch( Exception e )
    {
      LOGGER.debug( e );
      return new GamificationBadgeTileView( new String[] { getUserFriendlySystemMsg() } );
    }
  }

  /**
   * WorkHappier module
   * @param request
   * @param profileRPMDTO
   * @return
   */
  @RequestMapping( value = "/workhappier.action", method = RequestMethod.POST )
  public @ResponseBody WhPastResultsViewBean getWorkhappierDetails( HttpServletRequest request ) throws Exception
  {
    try
    {
      return getWhPastResultsViewBean( request );
    }
    catch( Exception e )
    {
      LOGGER.debug( e );
      return null;
    }
  }

  /**
   * 
   * @param request
   * @return
   */
  @RequestMapping( value = "/aboutMe.action", method = RequestMethod.POST )
  public @ResponseBody AboutMeView aboutMe( HttpServletRequest request )
  {
    AboutMeView aboutMeView = new AboutMeView();

    try
    {
      List<AboutMe> aboutMeList = profileService.getAllAboutMeByUserId( UserManager.getUserId() );
      aboutMeView.load( aboutMeList );
      String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      aboutMeView.setSiteUrlPrefix( siteUrlPrefix );
      request.getSession( false ).setAttribute( PARTICIPANT_STATE, new ParticipantState( UserManager.getUserId(), 10 ) );
    }
    catch( Exception e )
    {
      LOGGER.debug( e );
    }

    return aboutMeView;
  }

  /**
   * 
   * @return
   */
  @RequestMapping( value = "/settings.action" )
  public @ResponseBody AboutMeView getSettings()
  {
    return null;
  }

  /**
   * 
   * @return
   */
  @RequestMapping( value = "/template.action", method = RequestMethod.GET )
  public ModelAndView getTemplate()
  {
    return new ModelAndView( "index", "model object", "" );
  }

  @ExceptionHandler( Exception.class )
  @ResponseStatus( value = INTERNAL_SERVER_ERROR )
  public @ResponseBody ResponseEntity<List<WebErrorMessage>, Object> handleInternalException( HttpServletRequest request, Exception ex )
  {
    LOGGER.debug( "Requested URL=" + request.getRequestURL(), ex );

    return new ResponseEntity<List<WebErrorMessage>, Object>( buildAppExceptionMessage(), null );
  }

  private EngagementView getEngagementDashboardValueBean( Map<String, Object> queryParams ) throws Exception
  {
    if ( TEAM.equals( (String)queryParams.get( MODE ) ) )
    {
      return fetchTeamDashboardData( queryParams );
    }
    else if ( USER.equals( (String)queryParams.get( MODE ) ) )
    {
      return fetchProfileDashboardTabData( queryParams );
    }

    return new EngagementView();
  }

  /**
   * Fetches the team dash board data
   */
  private EngagementView fetchTeamDashboardData( Map<String, Object> queryParams ) throws Exception
  {
    EngagementDashboardValueBean engagementDashboardValueBean = engagementService.getDashboardTeamData( queryParams );

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
    return view;
  }

  /**
   * Fetches the profile page engagement user dash board data  
   */
  private EngagementView fetchProfileDashboardTabData( Map<String, Object> queryParams ) throws Exception
  {
    EngagementDashboardValueBean engagementDashboardValueBean = engagementService.getDashboardUserData( queryParams );

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
    return view;
  }

  private GamificationBadgeTileView getGamificationBadgeTileView( HttpServletRequest request )
  {
    List<PromotionMenuBean> eligiblePromotions = controllerHelper.getEligiblePromotions( request );

    List<ParticipantBadge> partcipantBadgeList = gamificationService.getBadgeByParticipantProfileSorted( eligiblePromotions, UserManager.getUserId(), 0 );

    return gamificationService.getTileViewJson( partcipantBadgeList );
  }

  private WorkHappinessSliderViewBean getWhSliderViewBean()
  {

    WorkHappinessSliderViewBean workHappinessSliderViewBean = new WorkHappinessSliderViewBean();

    List<WorkHappinessDataViewBean> resultZonesData = new ArrayList<WorkHappinessDataViewBean>();

    List<WorkHappier> workHappierData = workHappierService.getWorkHappier();

    for ( WorkHappier workHappier : workHappierData )
    {
      WorkHappinessDataViewBean workHappinessDataViewBean = new WorkHappinessDataViewBean();
      workHappinessDataViewBean.setHeadline( workHappier.getHeadlineFromCM() );
      workHappinessDataViewBean.setFeeling( workHappier.getFeelingWithPrefixFromCM() );
      workHappinessDataViewBean.setMinValue( workHappier.getMinValue() );
      workHappinessDataViewBean.setThoughts( workHappier.getThoughtsFromCM().split( ";" ) );
      resultZonesData.add( workHappinessDataViewBean );
    }

    workHappinessSliderViewBean.setResultZonesData( resultZonesData );

    return workHappinessSliderViewBean;
  }

  private WhPastResultsViewBean getWhPastResultsViewBean( HttpServletRequest request )
  {
    WhPastResultsViewBean whPastResultsViewBean = new WhPastResultsViewBean();

    try
    {
      Long score = Math.round( Double.parseDouble( request.getParameter( "value" ) ) );
      WorkHappier workHappier = workHappierService.geWorkHappierByScore( score );
      User user = userService.getUserById( UserManager.getUserId() );

      WorkHappierScore whScore = new WorkHappierScore();
      whScore.setUserId( user.getId() );
      whScore.setNodeId( user.getPrimaryUserNode().getNode().getId() );
      whScore.setScore( score );
      whScore.setWorkHappier( workHappier );

      WorkHappierScore savedWhScore = workHappierService.saveScore( whScore );
      List<WorkHappierScore> workHappierScoreList = workHappierService.getWHScore( UserManager.getUserId(), numberOfScores );

      if ( isEmpty( workHappierScoreList ) )
      {
        return whPastResultsViewBean;
      }

      Long savedWhScoreId = savedWhScore.getId();

      List<WorkHappierScoreViewBean> pastResults = workHappierScoreList.stream().filter( e -> !e.getId().equals( savedWhScoreId ) ).map( s -> getWhScoreBean( request, s ) )
          .collect( Collectors.toList() );

      if ( CollectionUtils.isNotEmpty( pastResults ) )
      {
        whPastResultsViewBean.setPastResults( pastResults );
      }
      whPastResultsViewBean.setHappinessSliderOpts( getWhSliderViewBean() );

    }
    catch( Exception e )
    {
      LOGGER.error( e );
    }
    return whPastResultsViewBean;
  }

  public WorkHappierScoreViewBean getWhScoreBean( HttpServletRequest request, WorkHappierScore workHappierScore )
  {
    WorkHappierScoreViewBean whScore = new WorkHappierScoreViewBean();
    whScore.setImgUrl( RequestUtils.getBaseURI( request ) + WH_IMAGES_PATH + workHappierScore.getWorkHappier().getImageName() );
    whScore.setDate( DateUtils.toDisplayString( workHappierScore.getAuditCreateInfo().getDateCreated() ) );
    whScore.setMood( workHappierScore.getWorkHappier().getFeelingFromCM() );
    return whScore;
  }

  private String buildUrl( Long promotionId, HttpServletRequest request )
  {
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    return ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/recognitionWizard/prepopulatePromotionRecognition.do", parameterMap );
  }
}
