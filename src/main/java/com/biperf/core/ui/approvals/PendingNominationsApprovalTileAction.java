
package com.biperf.core.ui.approvals;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.approvals.NominationsApprovalTileView.PendingNominationsApprovalView;
import com.biperf.core.ui.approvals.NominationsApprovalTileView.PendingNominationsApprovalView.NominationsApprovalView;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.NominationsApprovalValueBean;
import com.biperf.core.value.PendingNominationsApprovalMainValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

public class PendingNominationsApprovalTileAction extends BaseDispatchAction
{
  private static final String EACH_LEVEL = "eachLevel";

  public ActionForward fetchPendingNominationsForApprovalTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Map<String, Object> parameters = new HashMap<String, Object>();
    Long userId = UserManager.getUserId();
    parameters.put( "userId", userId );
    parameters.put( "rowNumStart", 0 );
    parameters.put( "rowNumEnd", 10 + 1 );
    parameters.put( "sortedBy", "desc" );
    parameters.put( "sortedOn", "date_submitted" );
    NominationsApprovalTileView mominationsApprovalTileView = new NominationsApprovalTileView();
    List<PendingNominationsApprovalView> pendingNominationsApprovalSets = new ArrayList<PendingNominationsApprovalView>();

    PendingNominationsApprovalMainValueBean pendingNominationsApprovalMainValueBean = getNominationClaimService().getPendingNominationClaimsForApprovalByUser( parameters );

    List<NominationsApprovalValueBean> pendingNominationsApprovalslist = pendingNominationsApprovalMainValueBean.getPendingNominationsApprovalslist();
    PendingNominationsApprovalView pendingNominationsApprovalView = new PendingNominationsApprovalView();
    if ( !pendingNominationsApprovalslist.isEmpty() )
    {
      pendingNominationsApprovalView.setTotalPendingNominationsApproval( pendingNominationsApprovalslist.get( 0 ).getTotalRecords() );
    }
    List<NominationsApprovalView> nominationsApprovalList = new ArrayList<NominationsApprovalView>();

    for ( NominationsApprovalValueBean nominationsApprovalValueBean : pendingNominationsApprovalslist )
    {
      Map<String, Object> urlParameters = new HashMap<String, Object>();
      urlParameters.put( "promotionId", nominationsApprovalValueBean.getPromotionId() );
      urlParameters.put( "approverUserId", userId );
      urlParameters.put( "levelNumber", nominationsApprovalValueBean.getApprovalLevel() );

      NominationsApprovalView nominationsApprovalView = new NominationsApprovalView();
      nominationsApprovalView.setId( nominationsApprovalValueBean.getClaimId().toString() );

      urlParameters.put( "claimId", nominationsApprovalValueBean.getClaimId().toString() );

      if ( nominationsApprovalValueBean.getPayoutLevelType() != null && nominationsApprovalValueBean.getPayoutLevelType().equals( EACH_LEVEL ) )
      {
        if ( !StringUtils.isEmpty( nominationsApprovalValueBean.getLevelLabel() ) )
        {
          nominationsApprovalView.setWinnersName( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS" ),
                                                                        new Object[] { nominationsApprovalValueBean.getPromotionName() + " " + nominationsApprovalValueBean.getLevelLabel() } ) );
        }
        else
        {
          nominationsApprovalView.setWinnersName( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS_LEVEL_INDEX" ),
                                                                        new Object[] { nominationsApprovalValueBean.getPromotionName(), nominationsApprovalValueBean.getApprovalLevel() } ) );
        }
      }
      else if ( !nominationsApprovalValueBean.isFinalLevel() )
      {
        if ( nominationsApprovalValueBean.isMultipleLevel() )
        {
          if ( !StringUtils.isEmpty( nominationsApprovalValueBean.getLevelLabel() ) )
          {
            nominationsApprovalView.setWinnersName( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_APPROVALS" ),
                                                                          new Object[] { nominationsApprovalValueBean.getPromotionName() + " " + nominationsApprovalValueBean.getLevelLabel() } ) );
          }
          else
          {
            nominationsApprovalView.setWinnersName( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_APPROVALS" ),
                                                                          new Object[] { nominationsApprovalValueBean.getPromotionName(), nominationsApprovalValueBean.getApprovalLevel() } ) );
          }
        }
        else
        {
          nominationsApprovalView.setWinnersName( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "REVIEW_APPROVALS" ),
                                                                        new Object[] { nominationsApprovalValueBean.getPromotionName() } ) );
        }
      }
      else
      {
        if ( !StringUtils.isEmpty( nominationsApprovalValueBean.getLevelLabel() ) )
        {
          nominationsApprovalView.setWinnersName( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS" ),
                                                                        new Object[] { nominationsApprovalValueBean.getPromotionName() + " " + nominationsApprovalValueBean.getLevelLabel() } ) );
        }
        else
        {
          nominationsApprovalView.setWinnersName( MessageFormat.format( ContentReaderManager.getText( "nomination.approvals.module", "SELECT_WINNERS" ),
                                                                        new Object[] { nominationsApprovalValueBean.getPromotionName() } ) );
        }
      }

      nominationsApprovalView.setUrl( buildNominationsApprovalDetailPageUrl( request, urlParameters ) );
      nominationsApprovalView.setDateSubmitted( DateUtils.toDisplayString( nominationsApprovalValueBean.getDateSubmitted() ) );

      nominationsApprovalList.add( nominationsApprovalView );
    }
    pendingNominationsApprovalView.setNominationsApprovalList( nominationsApprovalList );
    pendingNominationsApprovalSets.add( pendingNominationsApprovalView );
    mominationsApprovalTileView.setPendingNominationsApprovalSets( pendingNominationsApprovalSets );

    super.writeAsJsonToResponse( mominationsApprovalTileView, response );
    return null;
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
}
