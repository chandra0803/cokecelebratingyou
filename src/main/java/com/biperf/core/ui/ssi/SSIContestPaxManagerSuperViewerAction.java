
package com.biperf.core.ui.ssi;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.participant.ParticipantAssociationRequest;
import com.biperf.core.ui.recognition.purl.PreSelectedContributorsBean;
import com.biperf.core.ui.ssi.view.SSIContestManagerResponseView;
import com.biperf.core.ui.ssi.view.SSIContestManagerView;
import com.biperf.core.ui.ssi.view.SSIContestParticipantResponseView;
import com.biperf.core.ui.ssi.view.SSIContestParticipantView;
import com.biperf.core.ui.ssi.view.SSIContestSuperViewerResponseView;
import com.biperf.core.ui.ssi.view.SSIContestSuperViewerView;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;

/** 
 * @author dudam
 * @since Dec 22, 2014
 * @version 1.0
 */
public class SSIContestPaxManagerSuperViewerAction extends SSIContestCreateBaseAction
{

  /** fetches team search params based on current user
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchTeamSearchFilter( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.USER_NODE ) );
    Participant currentUser = getParticipantService().getParticipantByIdWithAssociations( UserManager.getUserId(), associationRequestCollection );

    List<Node> childNodes = getNodeService().getNodeAndNodesBelow( currentUser.getPrimaryUserNode().getNode().getId() );
    // convert to the view
    PreSelectedContributorsBean responseView = new PreSelectedContributorsBean( childNodes );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /** Load contest participants based on contest id
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

    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId();
    Integer currentPage = ssiContestPaxManagerSuperViewerForm.getCurrentPage() == 0 ? SSIContestUtil.FIRST_PAGE_NUMBER : ssiContestPaxManagerSuperViewerForm.getCurrentPage();
    String sortOn = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestPaxManagerSuperViewerForm.getSortedOn();
    String sortBy = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestPaxManagerSuperViewerForm.getSortedBy();

    Integer participantsCount = getSSIContestParticipantService().getContestParticipantsCount( ssiContestPaxManagerSuperViewerForm.getContestId() );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    List<SSIContestParticipant> participants = getSSIContestParticipantService().getContestParticipantsWithAssociations( contestId, currentPage, sortOn, sortBy, associationRequestCollection );

    SSIContestParticipantView responseView = new SSIContestParticipantView( participants, SSIContestUtil.PAX_MGR_PER_PAGE, participantsCount, sortBy, sortOn, currentPage );
    if ( Objects.nonNull( responseView.getParticipants() ) )
    {
      responseView.getParticipants().stream().forEach( p -> p.setId( p.getUserId() ) );
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /** Add participants based on contest id and participant id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward addParticipants( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    getSSIContestService().saveContestParticipants( ssiContestPaxManagerSuperViewerForm.getContestId(), ssiContestPaxManagerSuperViewerForm.getPaxIds() );
    super.writeAsJsonToResponse( new SSIContestParticipantResponseView(), response );
    return null;
  }

  /** Delete participant based on contest id and participant id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward deleteParticipant( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    getSSIContestService().deleteContestParticipant( ssiContestPaxManagerSuperViewerForm.getContestId(), ssiContestPaxManagerSuperViewerForm.getPaxIds()[0] );
    super.writeAsJsonToResponse( new SSIContestParticipantResponseView(), response );
    return null;
  }

  /** Load contest managers based on contest id
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadManagers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    SSIContestManagerView responseView = null;
    String sortedOn = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestPaxManagerSuperViewerForm.getSortedOn();
    String sortedBy = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestPaxManagerSuperViewerForm.getSortedBy();

    try
    {
      Map<String, Object> paxManagers = getSSIContestService().getContestManagersForSelectedPax( ssiContestPaxManagerSuperViewerForm.getContestId(),
                                                                                                 UserManager.getLocale().toString(),
                                                                                                 sortedOn,
                                                                                                 sortedBy );
      List<SSIContestParticipantValueBean> managersList = (List<SSIContestParticipantValueBean>)paxManagers.get( "managerList" );
      int managersCount = (Integer)paxManagers.get( "managerCount" );
      responseView = new SSIContestManagerView( managersList, managersCount, sortedOn, sortedBy );
    }
    catch( ServiceErrorException se )
    {
      responseView = new SSIContestManagerView( addServiceException( se ) );
    }
    writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Load existing contest managers
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadSelectedManagers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;

    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId();
    Integer currentPage = ssiContestPaxManagerSuperViewerForm.getCurrentPage() == 0 ? SSIContestUtil.FIRST_PAGE_NUMBER : ssiContestPaxManagerSuperViewerForm.getCurrentPage();
    String sortedOn = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestPaxManagerSuperViewerForm.getSortedOn();
    String sortedBy = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestPaxManagerSuperViewerForm.getSortedBy();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    List<SSIContestManager> contestManagers = getSSIContestService().getContestManagersWithAssociations( contestId, currentPage, sortedOn, sortedBy, associationRequestCollection );

    Integer managersCount = getSSIContestParticipantService().getContestManagersCount( ssiContestPaxManagerSuperViewerForm.getContestId() );
    SSIContestManagerView responseView = new SSIContestManagerView( contestManagers, managersCount, sortedBy, sortedOn, currentPage, SSIContestUtil.PAX_MGR_PER_PAGE );
    writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Add participant managers
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward addManagers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId();
    Long[] managers = ssiContestPaxManagerSuperViewerForm.getPaxIds();
    SSIContestManagerResponseView responseView = null;

    try
    {
      getSSIContestService().saveContestManagers( contestId, managers );
      responseView = new SSIContestManagerResponseView();
      writeAsJsonToResponse( responseView, response );
    }
    catch( ServiceErrorException see )
    {
      responseView = new SSIContestManagerResponseView( addServiceException( see ) );
      writeAsJsonToResponse( responseView, response );
    }
    return null;
  }

  /**
   * Remove participant manager
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward removeManager( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    getSSIContestService().deleteContestManager( ssiContestPaxManagerSuperViewerForm.getContestId(), ssiContestPaxManagerSuperViewerForm.getPaxIds()[0] );
    super.writeAsJsonToResponse( new SSIContestManagerView(), response );
    return null;
  }

  /************************ SUPERVIEWERS ************************/
  /**************************************************************/

  /**
   * Load existing contest super viewers
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward loadSelectedSuperViewers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;

    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId();
    Integer currentPage = ssiContestPaxManagerSuperViewerForm.getCurrentPage() == 0 ? SSIContestUtil.FIRST_PAGE_NUMBER : ssiContestPaxManagerSuperViewerForm.getCurrentPage();
    String sortedOn = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedOn() ) ? SSIContestUtil.SORT_BY_LAST_NAME : ssiContestPaxManagerSuperViewerForm.getSortedOn();
    String sortedBy = StringUtil.isNullOrEmpty( ssiContestPaxManagerSuperViewerForm.getSortedBy() ) ? SSIContestUtil.DEFAULT_SORT_BY : ssiContestPaxManagerSuperViewerForm.getSortedBy();

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ParticipantAssociationRequest( ParticipantAssociationRequest.ADDRESSES ) );
    List<SSIContestSuperViewer> contestSuperViewers = getSSIContestService().getContestSuperViewersWithAssociations( contestId, currentPage, sortedOn, sortedBy, associationRequestCollection );

    Integer superViewersCount = getSSIContestParticipantService().getContestSuperViewersCount( ssiContestPaxManagerSuperViewerForm.getContestId() );
    SSIContestSuperViewerView responseView = new SSIContestSuperViewerView( contestSuperViewers, superViewersCount, sortedBy, sortedOn, currentPage, SSIContestUtil.PAX_SUPERVIEWER_PER_PAGE );
    writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * Add participant super viewers
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward addSuperViewers( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    Long contestId = ssiContestPaxManagerSuperViewerForm.getContestId();
    Long[] superViewers = ssiContestPaxManagerSuperViewerForm.getPaxIds();
    SSIContestSuperViewerResponseView responseView = null;

    try
    {
      getSSIContestService().saveContestSuperViewers( contestId, superViewers );
      responseView = new SSIContestSuperViewerResponseView();
      writeAsJsonToResponse( responseView, response );
    }
    catch( ServiceErrorException see )
    {
      responseView = new SSIContestSuperViewerResponseView( addServiceException( see ) );
      writeAsJsonToResponse( responseView, response );
    }
    return null;
  }

  /**
   * Remove participant super viewer
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward removeSuperViewer( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestPaxManagerSuperViewerForm ssiContestPaxManagerSuperViewerForm = (SSIContestPaxManagerSuperViewerForm)form;
    getSSIContestService().deleteContestSuperViewer( ssiContestPaxManagerSuperViewerForm.getContestId(), ssiContestPaxManagerSuperViewerForm.getPaxIds()[0] );
    super.writeAsJsonToResponse( new SSIContestSuperViewerView(), response );
    return null;
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

}
