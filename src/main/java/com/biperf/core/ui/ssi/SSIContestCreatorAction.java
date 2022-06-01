
package com.biperf.core.ui.ssi;

import static com.biperf.core.utils.SSIContestUtil.getClientState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.currency.CurrencyService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.ssi.SSIContestParticipantService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestCheckNameView;
import com.biperf.core.ui.ssi.view.SSIContestDoThisGetThatPayoutsTotalView;
import com.biperf.core.ui.ssi.view.SSIContestObjectivesPayoutsTotalView;
import com.biperf.core.ui.ssi.view.SSIContestPaxPayoutsView;
import com.biperf.core.ui.ssi.view.SSIContestPayoutsTotalView;
import com.biperf.core.ui.ssi.view.SSIContestStackRankPayoutsTotalView;
import com.biperf.core.ui.ssi.view.SSIContestStepItUpPayoutsTotalView;
import com.biperf.core.ui.ssi.view.SSIContestTypesView;
import com.biperf.core.ui.ssi.view.SSIContestWebResponseView;
import com.biperf.core.ui.ssi.view.SSICopyContestErrorResponseView;
import com.biperf.core.ui.ssi.view.SSICopyContestSuccessResponseView;
import com.biperf.core.ui.ssi.view.SSIDeleteContestErrorResponseView;
import com.biperf.core.ui.ssi.view.SSIDeleteContestSuccessResponseView;
import com.biperf.core.ui.ssi.view.SSIPaxContestDataView;
import com.biperf.core.ui.ssi.view.SSIPaxContestMasterModuleView;
import com.biperf.core.ui.ssi.view.SSIUpdateResultsDataViewWrapper;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutsValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankParticipantPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/** 
 * @author dudam
 * @since Nov 25, 2014
 * @version 1.0
 */
public class SSIContestCreatorAction extends SSIContestDetailsBaseAction
{

  private static final Log log = LogFactory.getLog( SSIContestCreatorAction.class );

  private static final String CM_KEY_SET_IT_UP_DESCRIPTION = "SET_IT_UP_DESCRIPTION";
  private static final String CM_KEY_STACK_RANK_DESCRIPTION = "STACK_RANK_DESCRIPTION";
  private static final String CM_KEY_OBJECTIVES_DESCRIPTION = "OBJECTIVES_DESCRIPTION";
  private static final String CM_KEY_DO_THIS_GET_THAT_DESCRIPTION = "DO_THIS_GET_THAT_DESCRIPTION";
  private static final String CM_KEY_AWARD_THEM_NOW_DESCRIPTION = "AWARD_THEM_NOW_DESCRIPTION";
  private static final String SSI_CONTEST_CREATOR_CM_ASSET = "ssi_contest.creator";
  private static final String CONTEST_DETAIL_PAGE = "creatorContestList.do?method=display&id=";

  private static final String SV_ROLE = "superviewer";

  // maps FE sort params to db specific values
  private static final Map<String, String> contestPayoutsSortedOnMap = new HashMap<String, String>();

  static
  {

    contestPayoutsSortedOnMap.put( "lastName", "last_name" );

    // objectives sort columns
    contestPayoutsSortedOnMap.put( "goal", "OBJECTIVE_AMOUNT" );
    contestPayoutsSortedOnMap.put( "objectivePayout", "OBJECTIVE_PAYOUT" );
    contestPayoutsSortedOnMap.put( "totalPotentialPayout", "TOTAL_PAYOUT" );

    // step it up sort columns
    contestPayoutsSortedOnMap.put( "activityAmount", "activity_amt" );
    contestPayoutsSortedOnMap.put( "levelAchieved", "LEVEL_COMPLETED" );
    contestPayoutsSortedOnMap.put( "levelPayout", "LEVEL_PAYOUT" );
    contestPayoutsSortedOnMap.put( "totalPayout", "total_payout" );
    contestPayoutsSortedOnMap.put( "qualifiedActivity", "qualified_activity" );
    contestPayoutsSortedOnMap.put( "payoutIncrements", "number_of_increments" );

    // stack rank columns
    contestPayoutsSortedOnMap.put( "rank", "STACK_RANK" );

    // objectives, step it up, stack rank
    contestPayoutsSortedOnMap.put( "payoutDescription", "PAYOUT_DESCRIPTION" );
    contestPayoutsSortedOnMap.put( "payoutValue", "TOTAL_PAYOUT" );

    // objectives, step it up
    contestPayoutsSortedOnMap.put( "bonusPayout", "BONUS_PAYOUT" );
    contestPayoutsSortedOnMap.put( "qty", "PAYOUT" );

    // objectives, stack rank
    contestPayoutsSortedOnMap.put( "progress", "ACTIVITY_AMT" );
    contestPayoutsSortedOnMap.put( "payout", "TOTAL_PAYOUT" );

  }

  /** Fetch Active, Pending, Waiting for Approval and Incomplete contests for list page, based on the creator id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    List<SSIContestListValueBean> view = getSSIContestService().getContestListByCreator( UserManager.getUserId() );
    List<SSIContestListValueBean> approverView = getSSIContestService().getContestWaitingForApprovalByUserId( UserManager.getUserId() );
    for ( SSIContestListValueBean contestListValueBean : view )
    {
      SSIAdminContestActions adminContestAction = getSSIContestService().getAdminActionByEditCreator( contestListValueBean.getContestId() );
      if ( adminContestAction != null )
      {
        contestListValueBean.setUpdatedBy( getParticipantService().getLNameFNameByPaxIdWithComma( adminContestAction.getUserID() ) );
        contestListValueBean.setUpdatedOn( DateUtils.toDisplayString( adminContestAction.getAuditCreateInfo().getDateCreated() ) );
      }

      SSIContest contest = getSSIContestService().getContestById( contestListValueBean.getContestId() );
      if ( !contest.getContestOwnerId().equals( UserManager.getUser().getUserId() ) )
      {
        contestListValueBean.setRole( SV_ROLE );
        contestListValueBean.setRoleLabel( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.pax.manager.SUPERVIEWER" ) );
        contestListValueBean.setCanShowActionLinks( false );
      }
      contestListValueBean.setContestTypeName( SSIContestType.lookup( contestListValueBean.getContestType() ).getName() );
    }
    buildReadOnlyUrl( view, request );
    view = populatePaxMgrActiveContestList( view );
    view = populateApproverContestList( view );
    ssiContestListForm.setInitializationJson( toJson( view ) );

    // load the details for the passed contest id
    if ( ssiContestListForm.getId() != null )
    {
      boolean decode = !request.getMethod().equalsIgnoreCase( "GET" );
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), decode );
      SSIContest contest = getSSIContestService().getContestById( contestId );
      if ( contest.getCreatorId().equals( UserManager.getUser().getUserId() ) )
      {
        request.setAttribute( "isSuperViewer", false );
      }
      else
      {
        request.setAttribute( "isSuperViewer", true );
      }
      SSIPaxContestDataView ssiPaxContestDataView = populatePaxContestDataView( contest, true, false );
      // reset the contest id in the form
      ssiContestListForm.setId( getClientState( request, contestId, UserManager.getUserId(), true ) );
      ssiContestListForm.setContestJson( toJson( ssiPaxContestDataView ) );
    }
    ssiContestListForm.setRole( SSIContest.CONTEST_ROLE_CREATOR );
    if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
    {
      request.setAttribute( "canShowDeleteLink", false );
    }
    else
    {
      request.setAttribute( "canShowDeleteLink", true );
    }

    // set the role in the scope
    request.setAttribute( "isAdmin", isAdmin() );
    request.setAttribute( "isDelegate", isDelegate() );

    moveToRequest( request );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private List<SSIContestListValueBean> populatePaxMgrActiveContestList( List<SSIContestListValueBean> view )
  {
    List<SSIContestListValueBean> paxView = getSSIContestParticipantService().getParticipantLiveContestsValueBean( UserManager.getUserId() );
    if ( paxView.size() > 0 )
    {
      view.addAll( paxView );
    }
    List<SSIContestListValueBean> mgrView = getSSIContestService().getManagerLiveContests( UserManager.getUserId() );
    if ( mgrView.size() > 0 )
    {
      view.addAll( mgrView );
    }
    if ( paxView.size() > 0 || mgrView.size() > 0 )
    {
      Collections.sort( view );
    }
    return view;
  }

  private List<SSIContestListValueBean> populateApproverContestList( List<SSIContestListValueBean> view )
  {
    List<SSIContestListValueBean> approverViews = getSSIContestService().getApprovalsForListPageByUserId( UserManager.getUserId() );
    if ( approverViews.size() > 0 )
    {
      view.addAll( approverViews );
      Collections.sort( view );
    }
    return view;
  }

  private void buildReadOnlyUrl( List<SSIContestListValueBean> valueBeans, HttpServletRequest request )
  {
    for ( SSIContestListValueBean valueBean : valueBeans )
    {
      if ( !SSIContestType.AWARD_THEM_NOW.equals( valueBean.getContestType() )
          && ( SSIContestStatus.WAITING_FOR_APPROVAL.equals( valueBean.getStatus() ) || SSIContestStatus.FINALIZE_RESULTS.equals( valueBean.getStatus() ) ) )
      {
        Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
        clientStateParameterMap.put( "contestId", valueBean.getContestId() );
        clientStateParameterMap.put( "role", SSIContest.CONTEST_ROLE_CREATOR );
        valueBean.setReadOnlyUrl( ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.SSI_APPROVAL_SUMMARY, clientStateParameterMap ) );
      }
    }
  }

  public List<SSIContestListValueBean> fetchArchivedContests( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<SSIContestListValueBean> view = getSSIContestService().getArchivedContestListByCreator( UserManager.getUserId() );
    // setting contestType as 'awardThemNow' when it is archived and if it is award them now contest
    for ( SSIContestListValueBean valueBean : view )
    {
      if ( "1".equals( valueBean.getContestType() ) )
      {
        valueBean.setContestType( SSIContest.CONTEST_TYPE_AWARD_THEM_NOW );
      }
    }
    view = populatePaxMgrArchivedContestList( view );

    for ( SSIContestListValueBean contestListValueBean : view )
    {
      SSIContest contest = getSSIContestService().getContestById( contestListValueBean.getContestId() );
      if ( !contest.getContestOwnerId().equals( UserManager.getUser().getUserId() ) )
      {
        contestListValueBean.setRole( SV_ROLE );
        contestListValueBean.setRoleLabel( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.pax.manager.SUPERVIEWER" ) );
        contestListValueBean.setCanShowActionLinks( false );
      }
    }

    super.writeAsJsonToResponse( view, response );
    return null;
  }

  private List<SSIContestListValueBean> populatePaxMgrArchivedContestList( List<SSIContestListValueBean> view )
  {
    List<SSIContestListValueBean> paxView = getSSIContestParticipantService().getParticipantArchivedContests( UserManager.getUserId() );
    if ( paxView.size() > 0 )
    {
      view.addAll( paxView );
    }

    List<SSIContestListValueBean> mgrView = getSSIContestService().getManagerArchivedContests( UserManager.getUserId() );
    if ( mgrView.size() > 0 )
    {
      view.addAll( mgrView );
    }
    if ( paxView.size() > 0 || mgrView.size() > 0 )
    {
      Collections.sort( view );
    }
    return view;
  }

  public List<SSIContestListValueBean> fetchDeniedContests( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<SSIContestListValueBean> view = getSSIContestService().getDeniedContestListByCreator( UserManager.getUserId() );
    for ( SSIContestListValueBean contestListValueBean : view )
    {
      SSIContest contest = getSSIContestService().getContestById( contestListValueBean.getContestId() );
      if ( !contest.getContestOwnerId().equals( UserManager.getUser().getUserId() ) )
      {
        contestListValueBean.setRole( SV_ROLE );
        contestListValueBean.setRoleLabel( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.pax.manager.SUPERVIEWER" ) );
        contestListValueBean.setCanShowActionLinks( false );
      }
    }
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  /** Fetch my live contest for tile, based on the creator id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchContests( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<SSIContest> contests = getSSIContestService().getCreatorLiveContests( UserManager.getUserId() );
    int createListAfter = 0;
    if ( contests != null && contests.size() > 0 )
    {
      createListAfter = getCreateListAfter( contests );
    }
    int objectivesCount = SSIContestUtil.getLiveContestSize( contests, SSIContestType.OBJECTIVES );
    int doThisGetThatCount = SSIContestUtil.getLiveContestSize( contests, SSIContestType.DO_THIS_GET_THAT );
    int stepItUpCount = SSIContestUtil.getLiveContestSize( contests, SSIContestType.STEP_IT_UP );
    int stackRankCount = SSIContestUtil.getLiveContestSize( contests, SSIContestType.STACK_RANK );

    List<SSIPaxContestDataView> masterModuleList = new ArrayList<SSIPaxContestDataView>();
    for ( SSIContest contest : contests )
    {
      // no tile to display for award them now contest
      if ( !SSIContestType.AWARD_THEM_NOW.equals( contest.getContestType().getCode() ) )
      {
        boolean fullView = true;
        if ( SSIContestType.OBJECTIVES.equals( contest.getContestType().getCode() ) && objectivesCount > createListAfter )
        {
          fullView = false;
        }
        else if ( SSIContestType.DO_THIS_GET_THAT.equals( contest.getContestType().getCode() ) && doThisGetThatCount > createListAfter )
        {
          fullView = false;
        }
        else if ( SSIContestType.STEP_IT_UP.equals( contest.getContestType().getCode() ) && stepItUpCount > createListAfter )
        {
          fullView = false;
        }
        else if ( SSIContestType.STACK_RANK.equals( contest.getContestType().getCode() ) && stackRankCount > createListAfter )
        {
          fullView = false;
        }
        SSIPaxContestDataView view = populatePaxContestDataView( contest, fullView, true );
        masterModuleList.add( view );
      }
    }
    Collections.sort( masterModuleList );
    SSIPaxContestMasterModuleView masterView = new SSIPaxContestMasterModuleView( createListAfter, masterModuleList );
    super.writeAsJsonToResponse( masterView, response );
    return null;
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchContestDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    // if ( displayDetail( getUserIdFromClientState( ssiContestListForm.getId(), false ) ) )
    Long userId = SSIContestUtil.getUserIdFromClientState( request, ssiContestListForm.getId(), true );
    if ( displayDetail( userId ) )
    {
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), true );
      SSIContest contest = getSSIContestService().getContestById( contestId );
      SSIPaxContestDataView view = populatePaxContestDataView( contest, true, false );
      super.writeAsJsonToResponse( view, response );
    }
    return null;
  }

  /**
   * 
   * @param contest
   * @param request
   * @return
   * @throws ServiceErrorException 
   */
  private SSIPaxContestDataView populatePaxContestDataView( SSIContest contest, boolean fullView, boolean fromTile ) throws ServiceErrorException
  {
    SSIContestValueBean valueBean = getContestValueBean( contest, 0 );
    List<SSIContestProgressValueBean> contestProgressData = null;
    SSIContestProgressValueBean contestProgress = null;
    List<SSIContestStackRankPaxValueBean> stackRanks = null;
    SSIContestUniqueCheckValueBean uniqueCheckValueBean = null;
    SSIPaxContestDataView view = null;
    if ( fullView )
    {
      contestProgressData = getSSIContestParticipantService().getContestProgress( contest.getId(), null );
      if ( contest.getContestType().isStackRank() && fromTile )
      {
        stackRanks = getSSIContestParticipantService().getContestStackRank( contest.getId(), null, null, 1, 20, false, true );
      }
    }
    else if ( !contest.getContestType().isDoThisGetThat() )
    {
      contestProgress = getSSIContestParticipantService().getContestProgressForTile( contest.getId() );
    }
    if ( contest.getContestType().isObjectives() && fullView )
    {
      uniqueCheckValueBean = getSSIContestParticipantService().performUniqueCheck( contest.getId() );
    }
    if ( fullView )
    {
      view = new SSIPaxContestDataView( contest, contestProgressData, SSIContest.CONTEST_ROLE_CREATOR, valueBean, stackRanks, uniqueCheckValueBean, null, true );
    }
    else
    {
      view = new SSIPaxContestDataView( contest, contestProgress, SSIContest.CONTEST_ROLE_CREATOR, valueBean, true );
    }
    return view;
  }

  /** fetches all available contest types
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchAvailableContestTypes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIPromotion ssiPromotion = getSSIPromotionService().getLiveSSIPromotion();
    String contestGuideUrl = getSSIPromotionService().getSSIContestGuideUrl( ssiPromotion.getContestGuideUrl() );
    SSIContestTypesView contestTypes = new SSIContestTypesView( ssiPromotion, contestGuideUrl, getDescription( ssiPromotion ) );
    super.writeAsJsonToResponse( contestTypes, response );
    return null;
  }

  private SSIContestValueBean getDescription( SSIPromotion ssiPromotion )
  {
    SSIContestValueBean valueBean = new SSIContestValueBean();
    Locale locale = UserManager.getLocale();
    if ( ssiPromotion.isAwardThemNowSelected() )
    {
      String description = getCMAssetService().getString( SSI_CONTEST_CREATOR_CM_ASSET, CM_KEY_AWARD_THEM_NOW_DESCRIPTION, locale, true );
      valueBean.setDescriptionAtn( description );
    }
    if ( ssiPromotion.isDoThisGetThatSelected() )
    {
      String description = getCMAssetService().getString( SSI_CONTEST_CREATOR_CM_ASSET, CM_KEY_DO_THIS_GET_THAT_DESCRIPTION, locale, true );
      valueBean.setDescriptionDtgt( description );
    }

    if ( ssiPromotion.isObjectivesSelected() )
    {
      String description = getCMAssetService().getString( SSI_CONTEST_CREATOR_CM_ASSET, CM_KEY_OBJECTIVES_DESCRIPTION, locale, true );
      valueBean.setDescriptionO( description );
    }
    if ( ssiPromotion.isStackRankSelected() )
    {
      String description = getCMAssetService().getString( SSI_CONTEST_CREATOR_CM_ASSET, CM_KEY_STACK_RANK_DESCRIPTION, locale, true );
      valueBean.setDescriptionSr( description );
    }
    if ( ssiPromotion.isStepItUpSelected() )
    {
      String description = getCMAssetService().getString( SSI_CONTEST_CREATOR_CM_ASSET, CM_KEY_SET_IT_UP_DESCRIPTION, locale, true );
      valueBean.setDescriptionStu( description );
    }
    return valueBean;
  }

  /**
   * Checks whether there is already a contest with the same name
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward validateContestName( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestCheckNameView contestCheckNameView = new SSIContestCheckNameView();
    WebErrorMessage message = new WebErrorMessage();
    String contestName = request.getParameter( "contestName" ).trim();
    List<String> validationErrors = SSIContestUtil.validateContestName( contestName );
    if ( validationErrors.size() > 0 )
    {
      for ( String error : validationErrors )
      {
        WebErrorMessage errorMessage = new WebErrorMessage();
        errorMessage.setSuccess( false );
        errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        errorMessage.setText( error );
        contestCheckNameView.getMessages().add( errorMessage );
      }
    }
    else
    {
      boolean isContestNameUnique = getSSIContestService().isContestNameUnique( contestName, null, "en_US" );
      if ( isContestNameUnique )
      {
        message.setSuccess( true );
        message.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_NAME_UNIQUE" ) );
      }
      else
      {
        message.setSuccess( false );
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.generalInfo.CONTEST_NAME_NOT_UNIQUE" ) );
      }
      contestCheckNameView.getMessages().add( message );
    }
    writeAsJsonToResponse( contestCheckNameView, response );
    return null;
  }

  /** Delete contest based on contest id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward deleteContest( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    try
    {
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), true );
      getSSIContestService().deleteContest( contestId );
      writeAsJsonToResponse( new SSIDeleteContestSuccessResponseView(), response );
      return null;
    }
    catch( ServiceErrorException see )
    {
      log.error( "<<<<<<<ERROR >>>>>>" + see );
      writeAsJsonToResponse( new SSIDeleteContestErrorResponseView( addServiceException( see ) ), response );
      return null;
    }
  }

  public ActionForward copyContest( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, request.getParameter( "contest_id" ), true );
      Long newContestId = getSSIContestService().copyContest( Long.valueOf( contestId ), request.getParameter( "contestName" ), "en_US" );
      // Track SSI Admin Action
      if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
      {
        getSSIContestService().saveAdminAction( newContestId,
                                                UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                                SSIAdminContestActions.COPY_CONTEST,
                                                "Copy Contest -- Old Contest ID: " + contestId + " New contest ID: " + newContestId + " by Admin ID: "
                                                    + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );

      }
      writeAsJsonToResponse( new SSICopyContestSuccessResponseView( newContestId ), response );
    }
    catch( ServiceErrorException see )
    {
      log.error( "<<<<<<<ERROR >>>>>>" + see );
      writeAsJsonToResponse( new SSICopyContestErrorResponseView( addServiceException( see ) ), response );
    }
    return null;
  }

  public ActionForward fetchContestDetailsTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    ssiContestListForm.setRole( SSIContest.CONTEST_ROLE_CREATOR );
    ssiContestListForm.setUserId( null );
    return super.fetchContestSummaryTable( mapping, ssiContestListForm, request, response );
  }

  public ActionForward fetchStackRank( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    ssiContestListForm.setRole( SSIContest.CONTEST_ROLE_CREATOR );
    ssiContestListForm.setUserId( null );
    return super.fetchStackRankTable( mapping, ssiContestListForm, request, response );
  }

  public ActionForward approveContestPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestWebResponseView responseView = new SSIContestWebResponseView();
    WebErrorMessage message = new WebErrorMessage();
    try
    {
      SSIContestPayoutsForm ssiContestPayoutsForm = (SSIContestPayoutsForm)form;

      List<String> userIdList = new ArrayList<String>();
      List<String> payAmountList = new ArrayList<String>();
      List<String> payoutDescList = new ArrayList<String>();

      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestPayoutsForm.getId(), true );
      SSIContest contest = getSSIContestService().getContestById( contestId );

      for ( SSIContestStackRankParticipantPayoutValueBean paxPayout : ssiContestPayoutsForm.getParticipantsAsList() )
      {
        userIdList.add( paxPayout.getId() );
        if ( contest.getPayoutType().isPoints() )
        {
          if ( paxPayout.getPayout() != null )
          {
            payAmountList.add( paxPayout.getPayout().replaceAll( ",", "" ) );
          }
        }
        else
        {
          if ( paxPayout.getPayoutValue() != null )
          {
            payAmountList.add( paxPayout.getPayoutValue().replaceAll( ",", "" ) );
          }
        }
        payoutDescList.add( paxPayout.getPayoutDescription() );
      }

      String userIds = StringUtils.join( userIdList, "," );
      String payoutAmounts = StringUtils.join( payAmountList, "," );
      String payoutDesc = StringUtils.join( payoutDescList, "," );

      // Save current page pax payouts
      getSSIContestService().saveContestPaxPayouts( contestId, userIds, payoutAmounts, payoutDesc, "stack_rank", "asc", 1, SSIContestUtil.PAYOUTS_PER_PAGE );

      approvePayouts( request, ssiContestPayoutsForm );
      // Track SSI Admin Action
      if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
      {
        getSSIContestService().saveAdminAction( contestId,
                                                UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                                SSIAdminContestActions.ISSUE_PAYOUTS,
                                                "Payout issued for Contest ID: " + contestId + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );

      }

      message.setSuccess( true );
      message.setType( WebResponseConstants.RESPONSE_TYPE_SUCCESS );
      // below two values can be ignored
      message.setName( "Success name" );
      message.setText( "Success text" );
      responseView.getMessages().add( message );
      request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
      request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.PAYOUTS_IN_PROCESS" ) );
    }
    catch( ServiceErrorException see )
    {
      log.error( "<<<<<<<ERROR >>>>>>" + see );
      responseView.getMessages().add( addServiceException( see ) );
    }
    writeAsJsonToResponse( responseView, response );
    return null;
  }

  private void approvePayouts( HttpServletRequest request, SSIContestPayoutsForm form ) throws ServiceErrorException, InvalidClientStateException
  {
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, form.getId(), true );
    String csvUserIds = null;
    String csvPayoutAmounts = null;

    if ( getSSIContestService().approveContestPayouts( contestId, null, csvUserIds, csvPayoutAmounts ) )
    {
      getSSIContestService().launchContestPayoutsDepositProcess( contestId, null );
    }
  }

  public ActionForward updateAndApproveContestPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return approveContestPayouts( mapping, form, request, response );
  }

  public ActionForward displayContestPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPayoutsForm ssiContestPayoutsForm = (SSIContestPayoutsForm)form;
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestPayoutsForm.getId(), false );
    SSIContest contest = getSSIContestService().getContestById( contestId );
    String sortColumnName = "last_name";
    if ( contest.getContestType().isStackRank() )
    {
      sortColumnName = "stack_rank";
    }
    SSIContestPayoutsValueBean ssiContestPayoutsValueBean = getSSIContestService().getContestPayouts( contestId, null, sortColumnName, "asc", 1, 10 );
    String activityPrefix = getActivityPrefix( contest );
    String payoutPrefix = getPayoutPrefix( contest );
    String payoutSuffix = getPayoutSuffix( contest );
    boolean hasQualifiedPayouts = hasPayouts( contest );
    boolean includeSubmitClaim = isIncludeClaimSubmission( contest.getDataCollectionType() );
    SSIContestPayoutsTotalView contestPayoutTotalView = getContestPayoutsTotalView( contest,
                                                                                    hasQualifiedPayouts,
                                                                                    includeSubmitClaim,
                                                                                    ssiContestPayoutsValueBean,
                                                                                    activityPrefix,
                                                                                    payoutPrefix,
                                                                                    payoutSuffix );
    String contestIdEncrypted = SSIContestUtil.getClientState( request, contest.getId(), UserManager.getUserId(), true );
    contestPayoutTotalView.setId( contestIdEncrypted );
    contestPayoutTotalView.setHasQualifiedPayouts( hasQualifiedPayouts );
    request.setAttribute( "id", contestIdEncrypted );
    ssiContestPayoutsForm.setContestPayoutsTotalJson( toJson( contestPayoutTotalView ) );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private boolean isIncludeClaimSubmission( String collectDataMethod )
  {
    return SSIContestUtil.CLAIM_SUBMISSION.equals( collectDataMethod ) ? true : false;
  }

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return load( mapping, form, request, response );
  }

  public ActionForward load( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      SSIContestPayoutsForm ssiContestPayoutsForm = (SSIContestPayoutsForm)form;
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestPayoutsForm.getId(), true );
      int currentPage = ssiContestPayoutsForm.getCurrentPage() == 0 ? 1 : ssiContestPayoutsForm.getCurrentPage();
      String sortedOn = contestPayoutsSortedOnMap.get( ssiContestPayoutsForm.getSortedOn() );

      if ( ssiContestPayoutsForm.getSortedBy() == null )
      {
        ssiContestPayoutsForm.setSortedBy( SSIContestUtil.DEFAULT_SORT_BY );
      }

      if ( sortedOn == null )
      {
        SSIContest contest = getSSIContestService().getContestById( contestId );

        if ( contest.getContestType().isStackRank() )
        {
          sortedOn = SSIContestUtil.DEFAULT_SORT_BY_STACK_RANK;
        }
        else
        {
          sortedOn = SSIContestUtil.DEFAULT_SORT_BY_LAST_NAME;
        }
      }

      ssiContestPayoutsForm.setSortedOn( sortedOn );

      SSIContestPayoutsValueBean ssiContestPayoutsValueBean = getSSIContestService()
          .getContestPayouts( contestId, ssiContestPayoutsForm.getActivityId(), sortedOn, ssiContestPayoutsForm.getSortedBy(), currentPage, SSIContestUtil.PAYOUTS_PER_PAGE );
      SSIContestPaxPayoutsView paxPayoutsView = new SSIContestPaxPayoutsView( ssiContestPayoutsValueBean,
                                                                              currentPage,
                                                                              ssiContestPayoutsForm.getSortedBy(),
                                                                              ssiContestPayoutsForm.getSortedOn(),
                                                                              SSIContestUtil.PAYOUTS_PER_PAGE );
      super.writeAsJsonToResponse( paxPayoutsView, response );
    }
    catch( ServiceErrorException see )
    {
      log.error( "<<<<<<<ERROR >>>>>>" + see );
      super.writeAsJsonToResponse( new SSIContestPaxPayoutsView( addServiceException( see ) ), response );
    }
    return null;
  }

  public ActionForward paxNav( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      SSIContestPayoutsForm ssiContestPayoutsForm = (SSIContestPayoutsForm)form;
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestPayoutsForm.getId(), true );
      int currentPage = ssiContestPayoutsForm.getCurrentPage() == 0 ? 1 : ssiContestPayoutsForm.getCurrentPage();
      String sortedOn = contestPayoutsSortedOnMap.get( ssiContestPayoutsForm.getSortedOn() );
      SSIContest contest = getSSIContestService().getContestById( contestId );
      if ( currentPage == 1 && StringUtils.isEmpty( sortedOn ) )
      {
        if ( contest.getContestType().isStackRank() )
        {
          sortedOn = contestPayoutsSortedOnMap.get( "rank" ); // default sortedOn for stack rank
          ssiContestPayoutsForm.setSortedOn( "rank" );
        }
        else
        {
          sortedOn = contestPayoutsSortedOnMap.get( "lastName" ); // default sortedOn for other
                                                                  // contest types
          ssiContestPayoutsForm.setSortedOn( "lastName" );
        }
      }
      if ( ssiContestPayoutsForm.getSortedBy() == null )
      {
        ssiContestPayoutsForm.setSortedBy( SSIContestUtil.DEFAULT_SORT_BY );
      }
      List<String> userIdList = new ArrayList<String>();
      List<String> payAmountList = new ArrayList<String>();
      List<String> payoutDescList = new ArrayList<String>();

      for ( SSIContestStackRankParticipantPayoutValueBean paxPayout : ssiContestPayoutsForm.getParticipantsAsList() )
      {
        userIdList.add( paxPayout.getId() );
        if ( contest.getPayoutType().isPoints() )
        {
          payAmountList.add( paxPayout.getPayout() );
        }
        else
        {
          payAmountList.add( paxPayout.getPayoutValue() );
        }
        payoutDescList.add( paxPayout.getPayoutDescription() );
      }

      String userIds = StringUtils.join( userIdList, "," );
      String payoutAmounts = StringUtils.join( payAmountList, "," );
      String payoutDesc = StringUtils.join( payoutDescList, "," );

      SSIContestPayoutsValueBean ssiContestPayoutsValueBean = getSSIContestService()
          .saveContestPaxPayouts( contestId, userIds, payoutAmounts, payoutDesc, sortedOn, ssiContestPayoutsForm.getSortedBy(), currentPage, SSIContestUtil.PAYOUTS_PER_PAGE );

      SSIContestPaxPayoutsView paxPayoutsView = new SSIContestPaxPayoutsView( ssiContestPayoutsValueBean,
                                                                              currentPage,
                                                                              ssiContestPayoutsForm.getSortedBy(),
                                                                              ssiContestPayoutsForm.getSortedOn(),
                                                                              SSIContestUtil.PAYOUTS_PER_PAGE );
      super.writeAsJsonToResponse( paxPayoutsView, response );
    }
    catch( ServiceErrorException see )
    {
      log.error( "<<<<<<<ERROR >>>>>>" + see );
      super.writeAsJsonToResponse( new SSIContestPaxPayoutsView( addServiceException( see ) ), response );
    }
    return null;
  }

  public ActionForward closeContest( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      SSIContestPayoutsForm ssiContestPayoutsForm = (SSIContestPayoutsForm)form;
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestPayoutsForm.getId(), false );
      if ( SSIContestUtil.SELECTION_YES.equals( ssiContestPayoutsForm.getCloseContest() ) )
      {
        SSIContest contest = getSSIContestService().getContestById( contestId );
        contest.setStatus( SSIContestStatus.lookup( SSIContestStatus.FINALIZE_RESULTS ) );
        contest.setPayoutIssuedDate( DateUtils.getCurrentDateTrimmed() );
        getSSIContestService().saveContest( contest );
        // Track SSI Admin Action
        if ( UserManager.getUser().isSSIAdmin() || UserManager.getUser().getAuthorities().contains( new SimpleGrantedAuthority( "ROLE_LOGIN_AS" ) ) )
        {
          getSSIContestService().saveAdminAction( contestId,
                                                  UserManager.getUser().getOriginalAuthenticatedUser().getUserId(),
                                                  SSIAdminContestActions.CLOSE_CONTEST,
                                                  "Closed Contest ID: " + contestId + " by Admin ID: " + UserManager.getUser().getOriginalAuthenticatedUser().getUserId() );

        }
        return sendSuccessResponse( contestId, response, request, CONTEST_DETAIL_PAGE );
      }
      else
      {
        return sendSuccessResponse( contestId, response, request, CONTEST_DETAIL_PAGE );
      }
    }
    catch( Exception e )
    {
      return sendErrorResponse( response );
    }
  }

  private ActionForward sendSuccessResponse( Long contestId, HttpServletResponse response, HttpServletRequest request, String redirectUrl ) throws Exception
  {
    SSIUpdateResultsDataViewWrapper view = new SSIUpdateResultsDataViewWrapper();
    WebErrorMessage message = new WebErrorMessage();
    message.setSuccess( true );
    message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
    message.setCommand( WebResponseConstants.RESPONSE_COMMAND_REDIRECT );
    message.setUrl( redirectUrl + SSIContestUtil.getClientState( request, contestId, true ) );
    view.getMessages().add( message );
    writeAsJsonToResponse( view, response, ContentType.HTML );
    return null;
  }

  private ActionForward sendErrorResponse( HttpServletResponse response ) throws Exception
  {
    WebErrorMessage errorMessage = new WebErrorMessage();
    errorMessage.setSuccess( false );
    errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    errorMessage.setText( CmsResourceBundle.getCmsBundle().getString( "system.errors.USER_FRIENDLY_SYSTEM_ERROR_MESSAGE" ) );
    SSIUpdateResultsDataViewWrapper view = new SSIUpdateResultsDataViewWrapper();
    view.getMessages().add( errorMessage );
    writeAsJsonToResponse( view, response );
    return null;
  }

  private SSIContestPayoutsTotalView getContestPayoutsTotalView( SSIContest contest,
                                                                 boolean hasQualifiedPayouts,
                                                                 boolean includeSubmitClaim,
                                                                 SSIContestPayoutsValueBean ssiContestPayouts,
                                                                 String activityPrefix,
                                                                 String payoutPrefix,
                                                                 String payoutSuffix )
      throws Exception
  {
    String contestType = contest.getContestType().getCode();
    if ( SSIContestType.OBJECTIVES.equals( contestType ) )
    {
      return new SSIContestObjectivesPayoutsTotalView( contest,
                                                       ssiContestPayouts,
                                                       contest.getContestNameFromCM(),
                                                       hasQualifiedPayouts,
                                                       includeSubmitClaim,
                                                       activityPrefix,
                                                       payoutPrefix,
                                                       payoutSuffix );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( contestType ) )
    {
      return new SSIContestStepItUpPayoutsTotalView( contest, ssiContestPayouts, contest.getContestNameFromCM(), hasQualifiedPayouts, includeSubmitClaim, activityPrefix, payoutPrefix, payoutSuffix );
    }
    else if ( SSIContestType.STACK_RANK.equals( contestType ) )
    {
      List<SSIContestStackRankPayoutValueBean> payouts = getContestSRPayouts( contest.getId() );
      return new SSIContestStackRankPayoutsTotalView( contest,
                                                      ssiContestPayouts,
                                                      contest.getContestNameFromCM(),
                                                      hasQualifiedPayouts,
                                                      includeSubmitClaim,
                                                      activityPrefix,
                                                      payoutPrefix,
                                                      payoutSuffix,
                                                      payouts );
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( contestType ) )
    {
      return new SSIContestDoThisGetThatPayoutsTotalView( contest,
                                                          ssiContestPayouts,
                                                          contest.getContestNameFromCM(),
                                                          hasQualifiedPayouts,
                                                          includeSubmitClaim,
                                                          activityPrefix,
                                                          payoutPrefix,
                                                          payoutSuffix );
    }
    else
    {
      return null;
    }
  }

  private List<SSIContestStackRankPayoutValueBean> getContestSRPayouts( Long contestId ) throws ServiceErrorException
  {
    List<SSIContestStackRankPayoutValueBean> payouts = new ArrayList<SSIContestStackRankPayoutValueBean>();
    payouts = getSSIContestService().getContestSRPayoutList( contestId );
    return payouts;
  }

  private int getCreateListAfter( List<SSIContest> contests )
  {
    int maxContestsToDisplay = 0;
    for ( SSIContest contest : contests )
    {
      if ( contest.getPromotion().isLive() )
      {
        maxContestsToDisplay = contest.getPromotion().getMaxContestsToDisplay();
        break;
      }
    }
    if ( maxContestsToDisplay == 0 )
    {
      maxContestsToDisplay = contests.get( 0 ).getPromotion().getMaxContestsToDisplay();
    }
    return maxContestsToDisplay;
  }

  protected SSIPromotionService getSSIPromotionService()
  {
    return (SSIPromotionService)getService( SSIPromotionService.BEAN_NAME );
  }

  protected PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  protected static NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  protected SSIContestParticipantService getSSIContestParticipantService()
  {
    return (SSIContestParticipantService)BeanLocator.getBean( SSIContestParticipantService.BEAN_NAME );
  }

  protected CurrencyService getCurrencyService()
  {
    return (CurrencyService)getService( CurrencyService.BEAN_NAME );
  }

}
