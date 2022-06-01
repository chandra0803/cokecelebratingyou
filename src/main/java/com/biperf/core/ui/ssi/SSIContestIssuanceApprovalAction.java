
package com.biperf.core.ui.ssi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestATNSummaryAwardHistoryView;
import com.biperf.core.ui.ssi.view.SSIContestApprovalATNView;
import com.biperf.core.ui.ssi.view.SSIContestSummaryATNView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestAwardHistoryTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestAwardHistoryValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestIssuanceApprovalAction.
 * 
 * @author patepp
 * @since Feb 24 15, 2014
 * @version 1.0
 */

public class SSIContestIssuanceApprovalAction extends SSIContestBaseAction
{

  protected static final String CONTEST_ID = "contestId";
  protected static final String AWARD_ISSUANCE_NUMBER = "awardIssuanceNumber";
  private static final Integer CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL = 2;
  private static final Integer CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL = 1;
  public static final String WAITING_FOR_APPROVAL = "waiting_for_approval";
  public static final String CALLED_BY = "calledBy";
  public static final String APPROVE = "approve";
  public static final String DENY = "deny";
  public final String SUCCESS = getCMAssetService().getString( "ssi_contest.claims", "SUCCESS", UserManager.getLocale(), true );

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
    SSIContestIssuanceApprovalForm contestIssuanceApprovalForm = (SSIContestIssuanceApprovalForm)form;
    Long contestId = null;
    if ( request.getMethod().equalsIgnoreCase( "POST" ) && ( contestIssuanceApprovalForm.getClientState() != null || contestIssuanceApprovalForm.getContestId() != null ) )
    {
      String clientState = contestIssuanceApprovalForm.getClientState() != null ? contestIssuanceApprovalForm.getClientState() : contestIssuanceApprovalForm.getContestId();
      contestId = SSIContestUtil.getContestIdFromClientState( request, clientState, true );
    }
    else
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      contestId = getContestIdFromClientState( clientState, cryptoPass );
    }

    // Get the next issuance number for this contest
    SSIContest contest = getSSIContestAwardThemNowService().getContestById( contestId );

    // Get the total participants count for this contest including all issuances
    int participantsCount = getSSIContestParticipantService().getContestParticipantsCount( contest.getId() );

    // Get the totals value bean for the contest for all the issuances
    SSIContestAwardHistoryTotalsValueBean totalsBean = getSSIContestAwardThemNowService().getContestAwardTotals( contestId );

    SSIContestSummaryATNView pageViewBean = new SSIContestSummaryATNView( contest,
                                                                          getContestValueBean( contest, participantsCount ),
                                                                          contestIssuanceApprovalForm.getSortedOn(),
                                                                          contestIssuanceApprovalForm.getSortedBy() );

    // display success model

    setModelAttribute( request );

    String activityMeasureCurrencyCode = getActivityPrefix( contest );
    String payoutOtherCurrencyCode = getPayoutPrefix( contest );

    pageViewBean.setTotalValues( totalsBean, participantsCount, contest, activityMeasureCurrencyCode, payoutOtherCurrencyCode );
    contestIssuanceApprovalForm.setInitializationJson( toJson( pageViewBean ) );

    // TODO award history need to be loaded base on the structure of json

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );

  }

  private void setModelAttribute( HttpServletRequest request )
  {
    if ( request.getAttribute( CALLED_BY ) != null )
    {
      request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
      request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE,
                                         APPROVE.equalsIgnoreCase( request.getAttribute( CALLED_BY ).toString() )
                                             ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CONTEST_APPROVED" )
                                             : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CONTEST_DENIED" ) );
    }
    else
    {
      request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.FALSE );
    }
  }

  private WebErrorMessage getMessages( Object calledBy )
  {
    WebErrorMessage message = new WebErrorMessage();
    if ( calledBy != null )
    {

      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
      message.setSuccess( true );
      message.setName( SUCCESS );
      message.setText( APPROVE.equalsIgnoreCase( calledBy.toString() )
          ? CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CONTEST_APPROVED" )
          : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CONTEST_DENIED" ) );
    }
    return message;
  }

  /**
   * Load the summary of all the issuances for this contest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadIssuances( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestIssuanceApprovalForm contestIssuanceApprovalForm = (SSIContestIssuanceApprovalForm)form;
    boolean decode = !request.getMethod().equalsIgnoreCase( SSIContestUtil.GET_METHOD );
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, contestIssuanceApprovalForm.getContestId(), decode );
    try
    {
      Map<String, Object> outParams = getSSIContestAwardThemNowService().getAllIssuancesForContest( contestId,
                                                                                                    contestIssuanceApprovalForm.getPage(),
                                                                                                    SSIContestATNSummaryAwardHistoryView.RECORDS_PER_PAGE,
                                                                                                    contestIssuanceApprovalForm.getSortedOn(),
                                                                                                    contestIssuanceApprovalForm.getSortedBy() );
      int totalRecords = (Integer)outParams.get( "p_out_issuance_count" );
      List<SSIContestAwardHistoryValueBean> awardHistoryBeans = (ArrayList<SSIContestAwardHistoryValueBean>)outParams.get( "p_out_ref_cursor" );

      // generate the new client State
      generateClientStateIds( request, awardHistoryBeans, contestId );

      // getContestWith promotion
      SSIContest contest = getSSIContestService().getContestByIdWithAssociations( contestId, getAssociationRequestCollection() );
      setApprovalFlag( contest, awardHistoryBeans );

      String activityMeasureCurrencyCode = getActivityPrefix( contest );
      String payoutOtherCurrencyCode = getPayoutPrefix( contest );

      SSIContestATNSummaryAwardHistoryView responseView = new SSIContestATNSummaryAwardHistoryView( contestIssuanceApprovalForm.getPage(),
                                                                                                    awardHistoryBeans,
                                                                                                    totalRecords,
                                                                                                    contest,
                                                                                                    activityMeasureCurrencyCode,
                                                                                                    payoutOtherCurrencyCode );
      super.writeAsJsonToResponse( responseView, response );
    }
    catch( ServiceErrorException see )
    {
      SSIContestATNSummaryAwardHistoryView responseView = new SSIContestATNSummaryAwardHistoryView( addServiceException( see ) );
      super.writeAsJsonToResponse( responseView, response );
    }
    return null;
  }

  private void setApprovalFlag( SSIContest contest, List<SSIContestAwardHistoryValueBean> awardHistoryBeans )
  {
    Map<String, Set<Participant>> contestApprovers = getSSIContestService().getSelectedContestApprovers( contest.getId() );
    boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );

    if ( CONTEST_BELONG_TO_TWO_LEVEL_APPROVAL == contest.getPromotion().getContestApprovalLevels() )
    {
      for ( SSIContestAwardHistoryValueBean contestAwardHistoryValueBean : awardHistoryBeans )
      {
        if ( isLoginAs )
        {
          contestAwardHistoryValueBean.setCanApprove( false );
        }
        else if ( WAITING_FOR_APPROVAL.equalsIgnoreCase( contestAwardHistoryValueBean.getStatus() ) )
        {
          if ( isUserLevel1Approver( contest, contestApprovers ) && contestAwardHistoryValueBean.getApprovedByLevel1() == 0 )
          {
            contestAwardHistoryValueBean.setCanApprove( true );
          }
          if ( isUserLevel2Approver( contest, contestApprovers ) && contestAwardHistoryValueBean.getApprovedByLevel1() != 0 && !isIssuanceApprovedMe( contestAwardHistoryValueBean ) )
          {
            contestAwardHistoryValueBean.setCanApprove( true );
          }
        }
      }

    }
    else if ( CONTEST_BELONG_TO_ONE_LEVEL_APPROVAL == contest.getPromotion().getContestApprovalLevels() )
    {
      for ( SSIContestAwardHistoryValueBean contestAwardHistoryValueBean : awardHistoryBeans )
      {
        if ( isLoginAs )
        {
          contestAwardHistoryValueBean.setCanApprove( false );
        }
        else if ( WAITING_FOR_APPROVAL.equalsIgnoreCase( contestAwardHistoryValueBean.getStatus() ) )
        {
          if ( isUserLevel1Approver( contest, contestApprovers ) && contestAwardHistoryValueBean.getApprovedByLevel1() == 0 )
          {
            contestAwardHistoryValueBean.setCanApprove( true );
          }
        }
      }
    }

  }

  private boolean isIssuanceApprovedByLevel1( SSIContestAwardHistoryValueBean contestAwardHistoryValueBean )
  {
    boolean isIsuancePreApproved = true;

    if ( contestAwardHistoryValueBean.getApprovedByLevel1() != null )
    {
      isIsuancePreApproved = false;
    }
    return isIsuancePreApproved;
  }

  private boolean isIssuanceApprovedMe( SSIContestAwardHistoryValueBean contestAwardHistoryValueBean )
  {
    boolean isIssuanceApprovedMe = false;

    if ( UserManager.getUserId().equals( contestAwardHistoryValueBean.getApprovedByLevel1() ) )
    {
      isIssuanceApprovedMe = true;
    }
    return isIssuanceApprovedMe;
  }

  private boolean isUserLevel1Approver( SSIContest contest, Map<String, Set<Participant>> contestApprovers )
  {
    boolean isUserLevel1Approver = false;
    Set<Participant> level1Approvers = contestApprovers.get( "selected_contest_approver_level_1" );
    for ( Participant participant : level1Approvers )
    {
      if ( UserManager.getUserId().equals( participant.getId() ) )
      {
        isUserLevel1Approver = true;
        break;
      }
    }
    return isUserLevel1Approver;
  }

  private boolean isUserLevel2Approver( SSIContest contest, Map<String, Set<Participant>> contestApprovers )
  {
    boolean isUserLevel2Approver = false;
    Set<Participant> level1Approvers = contestApprovers.get( "selected_contest_approver_level_2" );
    for ( Participant participant : level1Approvers )
    {
      if ( UserManager.getUserId().equals( participant.getId() ) )
      {
        isUserLevel2Approver = true;
        break;
      }
    }
    return isUserLevel2Approver;
  }

  private void generateClientStateIds( HttpServletRequest request, List<SSIContestAwardHistoryValueBean> awardHistoryBeans, Long contestId )
  {
    for ( SSIContestAwardHistoryValueBean awardHistoryBean : awardHistoryBeans )
    {
      awardHistoryBean.setClientStateId( SSIContestUtil.getClientState( request, contestId, awardHistoryBean.getIssuanceNumber(), awardHistoryBean.getApprovalLevelActionTaken(), true ) );
    }
  }

  /**
   * Invoked when the contest' issuance is approved
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward approve( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestIssuanceApprovalForm contestIssuanceApprovalForm = (SSIContestIssuanceApprovalForm)form;
    SSIContestApprovalATNView contestApprovalATNView = new SSIContestApprovalATNView();
    Long contestId = null;
    Short awardIssuanceNumber = null;
    Integer levelApproved = null;
    if ( contestIssuanceApprovalForm.getClientState() != null )
    {
      contestId = SSIContestUtil.getContestIdFromClientState( request, contestIssuanceApprovalForm.getClientState(), true );
      awardIssuanceNumber = SSIContestUtil.getIssuanceNumberFromClientState( request, contestIssuanceApprovalForm.getClientState(), true );
      levelApproved = SSIContestUtil.getLevelApprovedFromClientState( request, contestIssuanceApprovalForm.getClientState(), true );
    }
    else
    {
      contestId = SSIContestUtil.getContestIdFromClientState( request, request.getParameter( "contestId" ), true );
      awardIssuanceNumber = SSIContestUtil.getIssuanceNumberFromClientState( request, request.getParameter( "contestId" ), true );
      levelApproved = SSIContestUtil.getLevelApprovedFromClientState( request, request.getParameter( "contestId" ), true );

    }

    try
    {

      SSIContestAwardThemNow contestAwardThemNow = getSSIContestAwardThemNowService().approveContest( contestId, UserManager.getUserId(), levelApproved, awardIssuanceNumber );
      if ( contestAwardThemNow.getIssuanceStatusType().isApproved() && getSSIContestService().approveContestPayouts( contestId, awardIssuanceNumber, null, null ) )
      {
        getSSIContestService().launchContestPayoutsDepositProcess( contestId, awardIssuanceNumber );
      }

    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        WebErrorMessage message = new WebErrorMessage();
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setSuccess( false );
        message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
        contestApprovalATNView.getMessages().add( message );
      }
    }

    if ( contestIssuanceApprovalForm.getClientState() != null )
    {
      request.setAttribute( CALLED_BY, APPROVE );
      return display( mapping, form, request, response );
    }
    else
    {
      contestApprovalATNView.getMessages().add( getMessages( APPROVE ) );
      super.writeAsJsonToResponse( contestApprovalATNView, response );
      return null;
    }

  }

  /**
   * Invoked when the contest's Issuance is denied
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward deny( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    SSIContestIssuanceApprovalForm contestIssuanceApprovalForm = (SSIContestIssuanceApprovalForm)form;
    SSIContestApprovalATNView contestApprovalATNView = new SSIContestApprovalATNView();
    Long contestId = null;
    Short awardIssuanceNumber = null;
    Integer levelApproved = null;
    if ( contestIssuanceApprovalForm.getClientState() != null )
    {
      contestId = SSIContestUtil.getContestIdFromClientState( request, contestIssuanceApprovalForm.getClientState(), true );
      awardIssuanceNumber = SSIContestUtil.getIssuanceNumberFromClientState( request, contestIssuanceApprovalForm.getClientState(), true );
      levelApproved = SSIContestUtil.getLevelApprovedFromClientState( request, contestIssuanceApprovalForm.getClientState(), true );
    }
    else
    {
      contestId = SSIContestUtil.getContestIdFromClientState( request, request.getParameter( "contestId" ), true );
      awardIssuanceNumber = SSIContestUtil.getIssuanceNumberFromClientState( request, request.getParameter( "contestId" ), true );
      levelApproved = SSIContestUtil.getLevelApprovedFromClientState( request, request.getParameter( "contestId" ), true );

    }

    try
    {

      getSSIContestAwardThemNowService().denyContest( contestId, UserManager.getUserId(), levelApproved, contestIssuanceApprovalForm.getComment(), awardIssuanceNumber );

    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        WebErrorMessage message = new WebErrorMessage();
        message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
        message.setSuccess( false );
        message.setText( CmsResourceBundle.getCmsBundle().getString( error.getKey() ) );
        contestApprovalATNView.getMessages().add( message );
      }
    }

    // display success model

    if ( contestIssuanceApprovalForm.getClientState() != null )
    {
      request.setAttribute( CALLED_BY, DENY );
      return display( mapping, form, request, response );
    }
    else
    {
      contestApprovalATNView.getMessages().add( getMessages( DENY ) );
      super.writeAsJsonToResponse( contestApprovalATNView, response );
      return null;
    }
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

  private ActionMessages getServiceErrors( ServiceErrorException se )
  {
    List serviceErrors = se.getServiceErrors();
    ActionMessages errors = new ActionMessages();
    for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
    {
      ServiceError error = (ServiceError)iter.next();
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( error.getKey() ) );
    }
    return errors;
  }

  private AssociationRequestCollection getAssociationRequestCollection()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ALL_APPROVERS ) );
    return associationRequestCollection;
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
  }
}
