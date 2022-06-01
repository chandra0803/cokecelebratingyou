
package com.biperf.core.ui.ssi;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.ui.ssi.view.SSIContestParticipantATNView;
import com.biperf.core.ui.ssi.view.SSIContestParticipantResponseView;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;

/**
 * 
 * SSIContestParticipantsAwardThemNowAction.
 * 
 * @author kandhi
 * @since Feb 5, 2015
 * @version 1.0
 */
public class SSIContestParticipantsAwardThemNowAction extends SSIContestPaxManagerSuperViewerAction
{

  /** Load contest participants based on contest id and award issuance number
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadParticipants( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;

    Short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    Integer currentPage = ssiContestPaxManagerSuperViewerForm.getCurrentPage() == 0 ? 1 : ssiContestPaxManagerSuperViewerForm.getCurrentPage();
    String sortedOn = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestPaxManagerSuperViewerForm.getSortedOn();
    String sortedBy = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestPaxManagerSuperViewerForm.getSortedBy();
    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId();
    if ( contestId == null )
    {
      contestId = getContestIdFromClientStateMap( request );
    }
    Integer participantsCount = getSSIContestAwardThemNowService().getContestParticipantsCount( contestId, awardIssuanceNumber );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    List<SSIContestParticipant> participants = getSSIContestAwardThemNowService().getContestParticipants( contestId,
                                                                                                          awardIssuanceNumber,
                                                                                                          currentPage,
                                                                                                          sortedOn,
                                                                                                          sortedBy,
                                                                                                          associationRequestCollection );
    SSIContest contest = getSSIContestAwardThemNowService().getContestById( contestId );
    SSIContestParticipantATNView responseView = new SSIContestParticipantATNView( participants, SSIContestUtil.PAX_MGR_PER_PAGE, participantsCount, sortedBy, sortedOn, currentPage, contest );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  @Override
  public ActionForward addParticipants( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    Short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId();
    if ( contestId == null )
    {
      contestId = getContestIdFromClientStateMap( request );
    }
    getSSIContestAwardThemNowService().saveContestParticipants( contestId, ssiContestPaxManagerSuperViewerForm.getPaxIds(), awardIssuanceNumber );
    super.writeAsJsonToResponse( new SSIContestParticipantResponseView(), response );
    return null;
  }

  @Override
  public ActionForward deleteParticipant( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId() != null ? ssiContestPaxManagerSuperViewerForm.getContestId() : getContestIdFromClientStateMap( request );

    Short awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    getSSIContestAwardThemNowService().deleteContestParticipant( contestId, ssiContestPaxManagerSuperViewerForm.getPaxIds()[0], awardIssuanceNumber );
    super.writeAsJsonToResponse( new SSIContestParticipantResponseView(), response );
    return null;
  }

}
