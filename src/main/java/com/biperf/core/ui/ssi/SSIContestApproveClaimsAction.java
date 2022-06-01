
package com.biperf.core.ui.ssi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestPaxClaimAssociationRequest;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestApproveClaimsSummaryView;
import com.biperf.core.ui.ssi.view.SSIContestApproveClaimsView;
import com.biperf.core.ui.ssi.view.SSIContestClaimApprovalView;
import com.biperf.core.ui.ssi.view.SSIContestClaimDetailView;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.ServiceLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.WebResponseConstants;
import com.biperf.core.value.ssi.SSIContestPaxClaimCountValueBean;
import com.biperf.core.value.ssi.SSIContestPaxClaimValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * @author patelp
 * @since May 22, 2015
 * @version 1.0
 */

public class SSIContestApproveClaimsAction extends SSIContestBaseAction
{

  public static final String APPROVED_ALL = "approveAll";
  public static final String UPDATED_ALL = "updateAll";
  public static final String UPDATED_IN_PROGRESS = "updateInProgress";

  /**
   * Display the Contest claims Approval summary page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    SSIContestApproveClaimsForm ssiContestApproveClaimsForm = (SSIContestApproveClaimsForm)form;
    Long contestId = null;
    boolean canApprove = Boolean.TRUE;

    if ( ssiContestApproveClaimsForm.getClientState() != null )
    {
      contestId = getClientStateParameterValueAsLong( request, SSIContestUtil.CONTEST_ID );
    }
    else if ( ssiContestApproveClaimsForm.getClaimId() != null )
    {
      contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestApproveClaimsForm.getClaimId(), true );
    }
    else if ( ssiContestApproveClaimsForm.getContestId() != null )
    {
      contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestApproveClaimsForm.getContestId(), true );
    }
    if ( StringUtil.isNullOrEmpty( ssiContestApproveClaimsForm.getSortedOn() ) )
    {
      ssiContestApproveClaimsForm.setSortedOn( SSIContestUtil.DEFAULT_SORT_ON );
    }
    if ( StringUtil.isNullOrEmpty( ssiContestApproveClaimsForm.getSortedBy() ) )
    {
      ssiContestApproveClaimsForm.setSortedBy( SSIContestUtil.DEFAULT_SORT_BY );
    }
    boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
    if ( isLoginAs )
    {
      canApprove = Boolean.FALSE;
    }

    request.setAttribute( "id", SSIContestUtil.getClientState( contestId ) );

    SSIContest contest = getSSIContestService().getContestById( contestId );
    SSIContestPaxClaimCountValueBean ssiContestPaxClaimCountValueBean = getSSIContestPaxClaimService().getPaxClaimsCountByApproverId( contestId );
    SSIContestApproveClaimsView ssiContestApproveClaimsView = new SSIContestApproveClaimsView( contest,
                                                                                               ssiContestPaxClaimCountValueBean,
                                                                                               getFullName( contest.getCreatorId() ),
                                                                                               ssiContestApproveClaimsForm.getSortedOn(),
                                                                                               ssiContestApproveClaimsForm.getSortedBy(),
                                                                                               canApprove );
    ssiContestApproveClaimsForm.setInitializationJson( toJson( ssiContestApproveClaimsView ) );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  /**
   * Load the summary of all the claims for this contest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public ActionForward loadClaimsSummary( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    SSIContestApproveClaimsForm ssiContestApproveClaimsForm = (SSIContestApproveClaimsForm)form;
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestApproveClaimsForm.getContestId(), true );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
    SSIContest contest = getSSIContestService().getContestByIdWithAssociations( contestId, associationRequestCollection );

    Map<String, Object> outParams = getSSIContestPaxClaimService().getPaxClaimsByContestIdAndStatus( contestId,
                                                                                                     ssiContestApproveClaimsForm.getFilter(),
                                                                                                     ssiContestApproveClaimsForm.getPage(),
                                                                                                     SSIContestUtil.CLAIM_APPROVALS_PER_PAGE,
                                                                                                     ssiContestApproveClaimsForm.getSortedOn(),
                                                                                                     ssiContestApproveClaimsForm.getSortedBy() );

    List<SSIContestPaxClaimValueBean> ssiContestPaxClaimValueBeanList = (List<SSIContestPaxClaimValueBean>)outParams.get( "claimList" );
    int claimsCount = (Integer)outParams.get( "claimsCount" );
    SSIContestApproveClaimsSummaryView ssiContestApproveClaimsSummaryView = new SSIContestApproveClaimsSummaryView( ssiContestPaxClaimValueBeanList,
                                                                                                                    ssiContestApproveClaimsForm.getPage(),
                                                                                                                    claimsCount,
                                                                                                                    SSIContestUtil.CLAIM_APPROVALS_PER_PAGE,
                                                                                                                    request );
    ssiContestApproveClaimsSummaryView.setClaimsSubmittedCount( (Integer)outParams.get( "claimsSubmittedCount" ) );
    ssiContestApproveClaimsSummaryView.setClaimsPendingCount( (Integer)outParams.get( "claimsPendingCount" ) );
    ssiContestApproveClaimsSummaryView.setClaimsApprovedCount( (Integer)outParams.get( "claimsApprovedCount" ) );
    ssiContestApproveClaimsSummaryView.setClaimsDeniedCount( (Integer)outParams.get( "claimsDeniedCount" ) );

    super.writeAsJsonToResponse( ssiContestApproveClaimsSummaryView, response );
    return null;
  }

  /**
   * Invoked when the contest PAX claim is approved
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward approve( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApproveClaimsForm ssiContestApproveClaimsForm = (SSIContestApproveClaimsForm)form;
    SSIContestClaimApprovalView ssiContestClaimApprovalView = new SSIContestClaimApprovalView();
    Long claimId = null;
    if ( ssiContestApproveClaimsForm.getClaimId() != null )
    {
      claimId = SSIContestUtil.getClaimIdFromClientState( request, ssiContestApproveClaimsForm.getClaimId(), true );
    }
    else
    {
      claimId = SSIContestUtil.getClaimIdFromClientState( request, ssiContestApproveClaimsForm.getClientState(), false );
    }
    try
    {
      getSSIContestPaxClaimService().approvePaxClaim( claimId, UserManager.getUserId() );

      getSSIContestPaxClaimService().notifyClaimSubmitterAndExpireClaimApprovalAlert( claimId, UserManager.getUserId() );
    }
    catch( ServiceErrorException se )
    {
      ssiContestClaimApprovalView.getMessages().add( addServiceException( se ) );
    }

    return forwardRequestWithParameters( mapping, claimId );

  }

  private ActionForward forwardRequestWithParameters( ActionMapping mapping, Long claimId )
  {
    SSIContestPaxClaim ssiContestPaxClaim = getSSIContestPaxClaimService().getPaxClaimById( claimId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", getClientStateParameterMap( ssiContestPaxClaim ) );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, "method=display" } );
  }

  /**
   * Invoked when the contest's claim is denied
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward deny( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApproveClaimsForm ssiContestApproveClaimsForm = (SSIContestApproveClaimsForm)form;
    SSIContestClaimApprovalView ssiContestClaimApprovalView = new SSIContestClaimApprovalView();
    Long claimId;
    if ( ssiContestApproveClaimsForm.getClaimId() != null )
    {
      claimId = SSIContestUtil.getClaimIdFromClientState( request, ssiContestApproveClaimsForm.getClaimId(), true );
    }
    else
    {
      claimId = SSIContestUtil.getClaimIdFromClientState( request, ssiContestApproveClaimsForm.getClientState(), false );

    }
    try
    {
      getSSIContestPaxClaimService().denyPaxClaim( claimId, UserManager.getUserId(), ssiContestApproveClaimsForm.getComment() );

      getSSIContestPaxClaimService().notifyClaimSubmitterAndExpireClaimApprovalAlert( claimId, UserManager.getUserId() );
    }
    catch( ServiceErrorException se )
    {
      ssiContestClaimApprovalView.getMessages().add( addServiceException( se ) );
    }

    return forwardRequestWithParameters( mapping, claimId );

  }

  /**
   * Approve all the pending claim
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward approveAll( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApproveClaimsForm ssiContestApproveClaimsForm = (SSIContestApproveClaimsForm)form;
    SSIContestClaimApprovalView ssiContestClaimApprovalView = new SSIContestClaimApprovalView();
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestApproveClaimsForm.getContestId(), true );

    try
    {
      getSSIContestPaxClaimService().approveAllPaxClaimsAndNotify( contestId, UserManager.getUserId() );
    }
    catch( ServiceErrorException se )
    {
      ssiContestClaimApprovalView.getMessages().add( addServiceException( se ) );
    }
    moveToRequest( request );
    request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
    request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.ALL_CLAIM_APPROVED" ) );

    String queryString = getQueryString( contestId );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_FORWARD, new String[] { queryString, "method=display" } );

  }

  private String getQueryString( Long contestId )
  {
    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "contestId", contestId );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return queryString;
  }

  /**
   * Load the claim detail page
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */

  public ActionForward displayClaimDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestClaimDetailForm ssiContestClaimDetailForm = (SSIContestClaimDetailForm)form;
    String clientState = ssiContestClaimDetailForm.getClientState();
    String claimNumber = SSIContestUtil.getClaimNumberFromClientState( request, clientState, false );

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new SSIContestPaxClaimAssociationRequest( SSIContestPaxClaimAssociationRequest.CLAIM_FIELDS ) );
    SSIContestPaxClaim paxClaim = getSSIContestPaxClaimService().getPaxClaimByClaimNumberWithAssociations( claimNumber, associationRequestCollection );

    SSIContestClaimDetailView contestJson = popluateClaimDetailValueBean( paxClaim, clientState );

    boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );

    if ( paxClaim.getStatus().isWaitingForApproval() && !isLoginAs )
    {
      contestJson.setShowApproveDeny( true );
    }
    ssiContestClaimDetailForm.setInitializationJson( toJson( contestJson ) );
    ssiContestClaimDetailForm.setBackButtonUrl( getBackUrl( paxClaim ) );
    request.setAttribute( "pageTitle", CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.APPROVE_CLAIM_TITLE" ) );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  /**
   * It will update the contest claim activity totals.
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward update( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestApproveClaimsForm ssiContestApproveClaimsForm = (SSIContestApproveClaimsForm)form;
    SSIContestClaimApprovalView ssiContestClaimApprovalView = new SSIContestClaimApprovalView();
    moveToRequest( request );
    Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestApproveClaimsForm.getContestId(), true );
    try
    {
      SSIContest contest = getSSIContestService().getContestById( contestId );
      if ( contest.getUpdateInProgress() != null && contest.getUpdateInProgress() )
      {
        ssiContestClaimApprovalView.getMessages().add( getText( UPDATED_IN_PROGRESS ) );
      }
      else
      {
        contest.setUpdateInProgress( Boolean.TRUE );
        getSSIContestService().saveContest( contest );
        getSSIContestPaxClaimService().updateAllPaxClaimsAndStackRank( contestId, UserManager.getUserId() );
        ssiContestClaimApprovalView.getMessages().add( getText( UPDATED_ALL ) );
      }
    }
    catch( ServiceErrorException se )
    {
      ssiContestClaimApprovalView.getMessages().add( addServiceException( se ) );
    }

    super.writeAsJsonToResponse( ssiContestClaimApprovalView, response );
    return null;

  }

  private String getBackUrl( SSIContestPaxClaim paxClaim )
  {
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( SSIContestUtil.CONTEST_ID, String.valueOf( paxClaim.getContestId() ) );
    return ClientStateUtils.generateEncodedLink( getSysUrl(), PageConstants.SSI_CLAIM_APPROVAL_LIST, clientStateParameterMap );
  }

  private WebErrorMessage getText( String calledBy )
  {

    WebErrorMessage message = new WebErrorMessage();
    if ( calledBy != null )
    {
      message.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_CMD );
      message.setCommand( WebResponseConstants.RESPONSE_COMMAND_MODAL );
      message.setSuccess( true );
      if ( UPDATED_IN_PROGRESS.equalsIgnoreCase( calledBy ) )
      {
        message.setName( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.UPDATED_IN_PROGRESS" ) );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.UPDATED_IN_PROGRESS" ) );
      }
      else if ( UPDATED_ALL.equalsIgnoreCase( calledBy ) )
      {
        message.setName( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.claims.SUCCESS" ) );
        message.setText( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CLAIMS_UPDATED" ) );
      }

    }
    return message;
  }

  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
  }

  private Map<String, Object> getClientStateParameterMap( SSIContestPaxClaim ssiContestPaxClaim )
  {
    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();
    clientStateParameterMap.put( SSIContestUtil.CONTEST_ID, ssiContestPaxClaim.getContestId() );
    clientStateParameterMap.put( SSIContestUtil.CLAIM_ID, ssiContestPaxClaim.getId() );
    clientStateParameterMap.put( SSIContestUtil.CLAIM_NUMBER, ssiContestPaxClaim.getClaimNumber() );
    return clientStateParameterMap;
  }

}
