/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.approvals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.approvals.NominationsApprovalListView.NominationsApprovalListTabularData;
import com.biperf.core.ui.approvals.NominationsApprovalListView.NominationsApprovalListTabularData.NominationsApprovalListMeta;
import com.biperf.core.ui.approvals.NominationsApprovalListView.NominationsApprovalListTabularData.NominationsApprovalListMeta.Columns;
import com.biperf.core.ui.approvals.NominationsApprovalListView.NominationsApprovalListTabularData.NominationsApprovalListResultsData;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.NominationsApprovalValueBean;
import com.biperf.core.value.PendingNominationsApprovalMainValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * 
 * @author poddutur
 * @since Apr 18, 2016
 */
public class PendingNominationsApprovalListAction extends BaseDispatchAction
{
  private static final String EACH_LEVEL = "eachLevel";

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward fetchPendingNominationsApprovalList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> parameters = new HashMap<String, Object>();
    Long userId = UserManager.getUserId();
    parameters.put( "userId", userId );
    int pageNumber;
    if ( request.getParameter( "currentPage" ) != null )
    {
      pageNumber = Integer.parseInt( request.getParameter( "currentPage" ) );
    }
    else
    {
      pageNumber = 1;
    }
    parameters.put( "pageNumber", pageNumber );
    int approvalsPerPage = 50;
    int rowNumStart = ( pageNumber - 1 ) * approvalsPerPage;
    int rowNumEnd = pageNumber * approvalsPerPage;

    parameters.put( "rowNumStart", rowNumStart );
    parameters.put( "rowNumEnd", rowNumEnd + 1 );

    if ( request.getParameter( "sortedBy" ) != null )
    {
      String sortedBy = request.getParameter( "sortedBy" );
      parameters.put( "sortedBy", sortedBy );
    }
    else
    {
      parameters.put( "sortedBy", "desc" );
    }
    if ( request.getParameter( "sortedOn" ) != null )
    {
      String sortedOn = request.getParameter( "sortedOn" );
      parameters.put( "sortedOn", sortedOn );
    }
    else
    {
      parameters.put( "sortedOn", "date_submitted" );
    }
    parameters.put( "approvalsPerPage", approvalsPerPage );

    buildNominationsApprovalListView( parameters, request, response );

    return null;
  }

  private void buildNominationsApprovalListView( Map<String, Object> parameters, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    NominationsApprovalListMeta nomiApprovalListMeta = new NominationsApprovalListMeta();
    buildNominationsApprovalListMeta( nomiApprovalListMeta );

    PendingNominationsApprovalMainValueBean pendingNominationsApprovalMainValueBean = getNominationClaimService().getPendingNominationClaimsForApprovalByUser( parameters );

    List<NominationsApprovalValueBean> pendingNominationsApprovalslist = pendingNominationsApprovalMainValueBean.getPendingNominationsApprovalslist();
    List<NominationsApprovalListResultsData> resultsDataList = new ArrayList<NominationsApprovalListResultsData>();
    buildResultsDataList( request, pendingNominationsApprovalslist, resultsDataList );

    NominationsApprovalListTabularData nominationsApprovalListTabularData = new NominationsApprovalListTabularData();
    nominationsApprovalListTabularData.setNomiApprovalListMeta( nomiApprovalListMeta );
    nominationsApprovalListTabularData.setResultsDatalist( resultsDataList );

    NominationsApprovalListView nominationsApprovalListView = new NominationsApprovalListView();
    nominationsApprovalListView.setNomiApprovalListTabularData( nominationsApprovalListTabularData );

    nominationsApprovalListView.setSortedOn( (String)parameters.get( "sortedOn" ) );
    nominationsApprovalListView.setSortedBy( (String)parameters.get( "sortedBy" ) );

    if ( !pendingNominationsApprovalslist.isEmpty() )
    {
      nominationsApprovalListView.setTotal( pendingNominationsApprovalslist.get( 0 ).getTotalPromoRecords() );

    }
    nominationsApprovalListView.setNominationPerPage( (int)parameters.get( "approvalsPerPage" ) );
    nominationsApprovalListView.setCurrentPage( (int)parameters.get( "pageNumber" ) );

    super.writeAsJsonToResponse( nominationsApprovalListView, response );
  }

  private void buildResultsDataList( HttpServletRequest request, List<NominationsApprovalValueBean> pendingNominationsApprovalslist, List<NominationsApprovalListResultsData> resultsDataList )
  {

    for ( NominationsApprovalValueBean nominationsApprovalValueBean : pendingNominationsApprovalslist )
    {

      Map<String, Object> urlParameters = new HashMap<String, Object>();
      urlParameters.put( "promotionId", nominationsApprovalValueBean.getPromotionId() );
      urlParameters.put( "approverUserId", UserManager.getUserId() );
      urlParameters.put( "levelNumber", nominationsApprovalValueBean.getApprovalLevel() );
      urlParameters.put( "userLocale", UserManager.getUserLocale() );
      urlParameters.put( "claimId", nominationsApprovalValueBean.getClaimId().toString() );
      urlParameters.put( "status", nominationsApprovalValueBean.getStatus() );

      NominationsApprovalListResultsData nominationsApprovalListResultsData = new NominationsApprovalListResultsData();
      nominationsApprovalListResultsData.setId( nominationsApprovalValueBean.getClaimId() );
      nominationsApprovalListResultsData.setNominationPromotionName( nominationsApprovalValueBean.getPromotionName() );
      nominationsApprovalListResultsData.setLevelName( nominationsApprovalValueBean.getLevelLabel() );
      nominationsApprovalListResultsData.setLevelNumber( nominationsApprovalValueBean.getApprovalLevel() );
      if ( nominationsApprovalValueBean.getStatus() != null && nominationsApprovalValueBean.getStatus().equals( ApprovalStatusType.PENDING ) )
      {
        if ( nominationsApprovalValueBean.getPayoutLevelType() != null && nominationsApprovalValueBean.getPayoutLevelType().equals( EACH_LEVEL ) || nominationsApprovalValueBean.isFinalLevel() )
        {
          nominationsApprovalListResultsData.setTasks( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS_TEXT" ) );
        }
        else
        {
          nominationsApprovalListResultsData.setTasks( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_APPROVALS_TEXT" ) );
        }
      }
      else
      {
        nominationsApprovalListResultsData.setTasks( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_HISTORY" ) );
      }
      nominationsApprovalListResultsData.setUrl( buildNominationsApprovalDetailPageUrl( request, urlParameters ) );
      ApprovalStatusType statusType = ApprovalStatusType.lookup( nominationsApprovalValueBean.getStatus() );
      nominationsApprovalListResultsData.setStatus( statusType == null ? "" : statusType.getName() );

      resultsDataList.add( nominationsApprovalListResultsData );
    }
  }

  private void buildNominationsApprovalListMeta( NominationsApprovalListMeta nomiApprovalListMeta )
  {
    List<Columns> columnslist = new ArrayList<Columns>();
    Columns column1 = new Columns();
    column1.setId( 1 );
    column1.setName( "promotion_name" );
    column1.setDisplayName( ContentReaderManager.getText( "nomination.approvals.module", "NOMINATION_PROMOTION_NAME" ) );
    column1.setSortable( true );
    columnslist.add( column1 );

    Columns column2 = new Columns();
    column2.setId( 2 );
    column2.setName( "level_label" );
    column2.setDisplayName( ContentReaderManager.getText( "nomination.approvals.module", "LEVEL" ) );
    column2.setSortable( true );
    columnslist.add( column2 );

    Columns column3 = new Columns();
    column3.setId( 3 );
    column3.setName( "status" );
    column3.setDisplayName( ContentReaderManager.getText( "nomination.approvals.module", "STATUS" ) );
    column3.setSortable( true );
    columnslist.add( column3 );

    Columns column4 = new Columns();
    column4.setId( 4 );
    column4.setName( "tasks" );
    column4.setDisplayName( ContentReaderManager.getText( "nomination.approvals.module", "TASKS" ) );
    column4.setSortable( false );
    columnslist.add( column4 );

    nomiApprovalListMeta.setColumnslist( columnslist );
  }

  private String buildNominationsApprovalDetailPageUrl( HttpServletRequest request, Map<String, Object> urlParams )
  {
    String url = ClientStateUtils.generateEncodedLink( request.getContextPath(), "/claim/nominationsApprovalPage.do?method=display", urlParams );
    return url;
  }

  private NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)getService( NominationClaimService.BEAN_NAME );
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
