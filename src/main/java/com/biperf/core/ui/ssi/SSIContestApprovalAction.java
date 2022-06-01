
package com.biperf.core.ui.ssi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestLevel;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestStackRankPayout;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestApprovalObjectiveDetailView;
import com.biperf.core.ui.ssi.view.SSIContestApprovalPaxAndManagerDetailView;
import com.biperf.core.ui.ssi.view.SSIContestApprovalRankDetailView;
import com.biperf.core.ui.ssi.view.SSIContestApprovalStepItUpDetailView;
import com.biperf.core.ui.ssi.view.SSIContestApproveAwardThemNowSummaryView;
import com.biperf.core.ui.ssi.view.SSIContestApproveDoThisGetThatSummaryView;
import com.biperf.core.ui.ssi.view.SSIContestApproveObjectivesSummaryView;
import com.biperf.core.ui.ssi.view.SSIContestApproveStackRankSummaryView;
import com.biperf.core.ui.ssi.view.SSIContestApproveStepItUpSummaryView;
import com.biperf.core.ui.ssi.view.SSIContestApproveSummaryView;
import com.biperf.core.ui.ssi.view.SSIContestBillCodeView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutObjectivesTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestApprovalAction.
 * 
 * @author kandhi
 * @since Dec 15, 2014
 * @version 1.0
 */
public class SSIContestApprovalAction extends SSIContestBaseAction
{

  private static final String PARTICIPANTS = "participants";

  private static final Map<String, String> contestSortColumnNameMap = new HashMap<String, String>();

  static
  {
    contestSortColumnNameMap.put( "lastName", "last_name" );
  }

  /**
   * Display the Contest Approval summary page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApprovalForm ssiContestApprovalForm = (SSIContestApprovalForm)form;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    Long contestId = getContestIdFromClientState( clientState, cryptoPass );
    try
    {
      populateViewBean( ssiContestApprovalForm, contestId, clientState, cryptoPass );
    }
    catch( ServiceErrorException see )
    {
      ActionMessages errors = new ActionMessages();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( see.getServiceErrors(), errors );
      saveErrors( request, errors );
    }
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  protected void populateViewBean( SSIContestApprovalForm approvalForm, Long contestId, String clientState, String cryptoPass ) throws ServiceErrorException, Exception
  {
    SSIContest ssiContest = getSSIContestService().getContestById( contestId );
    int participantsCount = getSSIContestParticipantService().getContestParticipantsCount( ssiContest.getId() );

    SSIContestValueBean valueBean = getContestValueBean( ssiContest, participantsCount );
    valueBean.setSelectedContestApprovers( getSSIContestService().getSelectedContestApprovers( contestId ) );
    List<SSIContestBillCodeView> billCodes = getBillCodeViewByContest( ssiContest );

    if ( SSIContestType.AWARD_THEM_NOW.equals( ssiContest.getContestType().getCode() ) )
    {
      SSIContestApproveAwardThemNowSummaryView pageViewBean = new SSIContestApproveAwardThemNowSummaryView( ssiContest );
      loadPaxAndManagerCount( pageViewBean, ssiContest, approvalForm, participantsCount, clientState, cryptoPass, billCodes );
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( ssiContest.getContestType().getCode() ) )
    {
      List<SSIContestActivity> contestActivities = getSSIContestService().getContestActivitiesByContestId( ssiContest.getId() );
      SSIContestApproveDoThisGetThatSummaryView pageViewBean = new SSIContestApproveDoThisGetThatSummaryView( ssiContest, contestActivities, valueBean );
      loadPaxAndManagerCount( pageViewBean, ssiContest, approvalForm, participantsCount, clientState, cryptoPass, billCodes );
    }
    else if ( SSIContestType.OBJECTIVES.equals( ssiContest.getContestType().getCode() ) )
    {
      SSIContestApproveObjectivesSummaryView pageViewBean = new SSIContestApproveObjectivesSummaryView( ssiContest, valueBean );
      SSIContestPayoutObjectivesTotalsValueBean contestPayoutTotalsValueBean = getSSIContestService().calculatePayoutObjectivesTotals( contestId );
      pageViewBean.setContestPayoutTotalsValues( contestPayoutTotalsValueBean, ssiContest, valueBean );

      SSIContestUniqueCheckValueBean contestUniqueCheckValueBean = getSSIContestParticipantService().performUniqueCheck( ssiContest.getId() );
      String variesByPax = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.VARIES_BY_PARTICIPANT" );
      pageViewBean.setUniqueValues( contestUniqueCheckValueBean, valueBean, variesByPax, ssiContest );
      loadPaxAndManagerCount( pageViewBean, ssiContest, approvalForm, participantsCount, clientState, cryptoPass, billCodes );
    }
    else if ( SSIContestType.STACK_RANK.equals( ssiContest.getContestType().getCode() ) )
    {
      SSIContestPayoutStackRankTotalsValueBean totals = getSSIContestService().getStackRankTotals( contestId );
      SSIContestApproveStackRankSummaryView pageViewBean = new SSIContestApproveStackRankSummaryView( ssiContest, valueBean, totals );
      loadPaxAndManagerCount( pageViewBean, ssiContest, approvalForm, participantsCount, clientState, cryptoPass, billCodes );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( ssiContest.getContestType().getCode() ) )
    {
      List<SSIContestLevel> ssiContestLevels = getSSIContestService().getContestLevelsByContestId( ssiContest.getId() );
      SSIContestBaseLineTotalsValueBean totalValueBean = getSSIContestService().calculateBaseLineTotalsForStepItUp( ssiContest.getId() );
      SSIContestApproveStepItUpSummaryView pageViewBean = new SSIContestApproveStepItUpSummaryView( ssiContest, ssiContestLevels, valueBean, totalValueBean.getBaselineTotal() );
      loadPaxAndManagerCount( pageViewBean, ssiContest, approvalForm, participantsCount, clientState, cryptoPass, billCodes );
    }

  }

  private void loadPaxAndManagerCount( SSIContestApproveSummaryView pageViewBean,
                                       SSIContest ssiContest,
                                       SSIContestApprovalForm approvalForm,
                                       int participantsCount,
                                       String clientState,
                                       String cryptoPass,
                                       List<SSIContestBillCodeView> billCodes )
  {
    pageViewBean.setParticipantsCount( participantsCount );
    int managersCount = getSSIContestParticipantService().getContestManagersCount( ssiContest.getId() );
    pageViewBean.setManagersCount( managersCount );
    int superviewersCount = getSSIContestParticipantService().getContestSuperViewersCount( ssiContest.getId() );
    pageViewBean.setSuperViewersCount( superviewersCount );
    pageViewBean.setClientState( createContestClientState( ssiContest ) );
    if ( !StringUtil.isNullOrEmpty( cryptoPass ) )
    {
      pageViewBean.setRole( getRoleFromClientState( clientState, cryptoPass ) );
    }
    pageViewBean.setBillCodes( billCodes );
    approvalForm.setInitializationJson( toJson( pageViewBean ) );
  }

  /**
   * Create client state with contest id and approval level
   * @param ssiContest
   * @return
   */
  private String createContestClientState( SSIContest ssiContest )
  {
    Map<String, Object> clientStateParamMap = new HashMap<String, Object>();
    if ( ssiContest != null )
    {
      clientStateParamMap.put( SSIContestUtil.CONTEST_ID, ssiContest.getId() );
      clientStateParamMap.put( "levelApproved", ssiContest.getLevelApproved() );
    }
    String password = ClientStatePasswordManager.getPassword();
    return ClientStateSerializer.serialize( clientStateParamMap, password );
  }

  private Map<String, Object> getContestClientStateMap( HttpServletRequest request )
  {
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String password = ClientStatePasswordManager.getPassword();
      return ClientStateSerializer.deserialize( clientState, password );
    }
    catch( IllegalArgumentException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "Invalid client state" );
    }
  }

  protected String getRoleFromClientState( String clientState, String cryptoPass )
  {
    String password = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    try
    {
      Map<String, Object> clientStateMap = getSSIContestClientStateMap( clientState, password );
      return (String)clientStateMap.get( "role" );
    }
    catch( Exception e )
    {
      throw new RuntimeException( "Unable Decode Client State: " + e );
    }
  }

  /**
   * Invoked when the contest is approved
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward approve( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApprovalForm ssiContestApprovalForm = (SSIContestApprovalForm)form;
    Map clientStateMap = getContestClientStateMap( request );
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );
    int levelApproved = (Integer)clientStateMap.get( "levelApproved" );
    try
    {
      getSSIContestService().approveContest( contestId, UserManager.getUserId(), levelApproved );
    }
    catch( ServiceErrorException see )
    {
      ActionMessages errors = new ActionMessages();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( see.getServiceErrors(), errors );
      saveErrors( request, errors );
      populateViewBean( ssiContestApprovalForm, contestId, null, null );
      return mapping.getInputForward();
    }
    request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
    request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CONTEST_APPROVED" ) );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Invoked when the contest is denied
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward deny( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApprovalForm ssiContestApprovalForm = (SSIContestApprovalForm)form;
    Map clientStateMap = getContestClientStateMap( request );
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );
    int levelApproved = (Integer)clientStateMap.get( "levelApproved" );
    try
    {
      getSSIContestService().denyContest( contestId, UserManager.getUserId(), levelApproved, ssiContestApprovalForm.getComment() );
    }
    catch( ServiceErrorException see )
    {
      ActionMessages errors = new ActionMessages();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( see.getServiceErrors(), errors );
      saveErrors( request, errors );
      populateViewBean( ssiContestApprovalForm, contestId, null, null );
      return mapping.getInputForward();
    }
    request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
    request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CONTEST_DENIED" ) );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Display the details of the participants and managers from summary page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadParticipantsAndManagers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApprovalForm ssiContestApprovalForm = (SSIContestApprovalForm)form;
    Integer currentPage = ssiContestApprovalForm.getPage() == 0 ? SSIContestUtil.FIRST_PAGE_NUMBER : ssiContestApprovalForm.getPage();
    Map clientStateMap = getContestClientStateMap( request );
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );

    Integer participantsCount = 0;
    Integer managersCount = 0;
    Integer superviewersCount = 0;
    List<SSIContestParticipant> participants = null;
    List<SSIContestManager> managers = null;
    List<SSIContestSuperViewer> superviewers = null;

    if ( PARTICIPANTS.equalsIgnoreCase( ssiContestApprovalForm.getSet() ) )
    {
      participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
      participants = getParticipants( contestId, currentPage, ssiContestApprovalForm );
    }
    else
    {
      managersCount = getSSIContestParticipantService().getContestManagersCount( contestId );
      managers = getManagers( contestId, currentPage, ssiContestApprovalForm );

      superviewersCount = getSSIContestParticipantService().getContestSuperViewersCount( contestId );
      superviewers = getSuperViewers( contestId, currentPage, ssiContestApprovalForm );
    }
    SSIContestApprovalPaxAndManagerDetailView view = new SSIContestApprovalPaxAndManagerDetailView( SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                                                                    currentPage,
                                                                                                    participantsCount,
                                                                                                    managersCount,
                                                                                                    superviewersCount,
                                                                                                    participants,
                                                                                                    managers,
                                                                                                    superviewers );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  /**
   * Display the details of the objectives and payouts from summary page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadObjectivesAndPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApprovalForm ssiContestApprovalForm = (SSIContestApprovalForm)form;
    Integer currentPage = ssiContestApprovalForm.getPage() == 0 ? 1 : ssiContestApprovalForm.getPage();
    Map clientStateMap = getContestClientStateMap( request );
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );
    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    SSIContestPayoutObjectivesTotalsValueBean contestPayoutTotalsValueBean = getSSIContestService().calculatePayoutObjectivesTotals( contestId );
    SSIContestApprovalObjectiveDetailView view = new SSIContestApprovalObjectiveDetailView( SSIContestUtil.PAX_RECORDS_PER_PAGE,
                                                                                            currentPage,
                                                                                            participantsCount,
                                                                                            getParticipants( contestId, currentPage, ssiContestApprovalForm ),
                                                                                            contestPayoutTotalsValueBean );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  /**
   * Display the details of the objectives and payouts from summary page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadRanksAndPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map clientStateMap = getContestClientStateMap( request );
    Long contestId = (Long)clientStateMap.get( SSIContestUtil.CONTEST_ID );
    List<SSIContestStackRankPayout> rankPayouts = getSSIContestService().getStackRankPayoutsByContestId( contestId );
    String siteUrl = null;
    List<BadgeLibrary> badgeLibrary = null;
    boolean hasBadge = rankPayouts.get( 0 ).getContest().getPromotion().getBadge() != null && rankPayouts.get( 0 ).getContest().getPromotion().getBadge().getBadgeRules() != null
        && rankPayouts.get( 0 ).getContest().getPromotion().getBadge().getBadgeRules().size() > 0;
    if ( hasBadge )
    {
      siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      badgeLibrary = getPromotionService().buildBadgeLibraryList();
    }
    String payoutPrefix = getPayoutPrefix( rankPayouts.get( 0 ).getContest() );
    SSIContestApprovalRankDetailView view = new SSIContestApprovalRankDetailView( rankPayouts, siteUrl, badgeLibrary, hasBadge, payoutPrefix );
    super.writeAsJsonToResponse( view, response );
    return null;
  }

  /**
   * Display the details of the objectives and payouts from summary page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadStepItUpBaselines( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApprovalForm ssiContestApprovalForm = (SSIContestApprovalForm)form;
    Integer currentPage = ssiContestApprovalForm.getPage() == 0 ? SSIContestUtil.FIRST_PAGE_NUMBER : ssiContestApprovalForm.getPage();
    Long contestId = (Long)getContestClientStateMap( request ).get( SSIContestUtil.CONTEST_ID );

    SSIContest contest = getSSIContestService().getContestByIdWithAssociations( contestId, getAssociationRequestCollection() );
    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contestId );
    List<SSIContestParticipant> participants = getParticipants( contestId, currentPage, ssiContestApprovalForm );

    SSIContestBaseLineTotalsValueBean baseLineTotalsValueBean = getSSIContestService().calculateBaseLineTotalsForStepItUp( contestId );
    SSIContestApprovalStepItUpDetailView responseView = new SSIContestApprovalStepItUpDetailView( participants, contest, baseLineTotalsValueBean, participantsCount, currentPage );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private List<SSIContestParticipant> getParticipants( Long contestId, Integer currentPage, SSIContestApprovalForm ssiContestApprovalForm )
  {
    String sortColumnName = StringUtil.isNullOrEmpty( ssiContestApprovalForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestApprovalForm.getSortedOn();
    String sortedBy = StringUtil.isNullOrEmpty( ssiContestApprovalForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestApprovalForm.getSortedBy();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    return getSSIContestParticipantService().getContestParticipantsWithAssociations( contestId, currentPage, sortColumnName, sortedBy, associationRequestCollection );
  }

  private List<SSIContestManager> getManagers( Long contestId, Integer currentPage, SSIContestApprovalForm ssiContestApprovalForm )
  {
    String sortedOn = StringUtil.isNullOrEmpty( ssiContestApprovalForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestApprovalForm.getSortedOn();
    String sortedBy = StringUtil.isNullOrEmpty( ssiContestApprovalForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestApprovalForm.getSortedBy();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    return getSSIContestParticipantService().getContestManagers( contestId, currentPage, sortedOn, sortedBy, associationRequestCollection );
  }

  private List<SSIContestSuperViewer> getSuperViewers( Long contestId, Integer currentPage, SSIContestApprovalForm ssiContestApprovalForm )
  {
    String sortedOn = StringUtil.isNullOrEmpty( ssiContestApprovalForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestApprovalForm.getSortedOn();
    String sortedBy = StringUtil.isNullOrEmpty( ssiContestApprovalForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestApprovalForm.getSortedBy();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    return getSSIContestParticipantService().getContestSuperviewers( contestId, currentPage, sortedOn, sortedBy, associationRequestCollection );
  }

  /*
   * Get the Association Request Collection
   */
  private AssociationRequestCollection getAssociationRequestCollection()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_LEVELS ) );
    return associationRequestCollection;
  }

}
