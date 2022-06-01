
package com.biperf.core.ui.ssi;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.ssi.SSIAdminContestActions;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.ssi.view.SSIContestListViewBean;
import com.biperf.core.ui.ssi.view.SSIPaxContestDataView;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;

/**
 * SSIContestManagerAction.
 * @author kandhi
 * @since Nov 19, 2014
 * @version 1.0
 */
public class SSIContestManagerAction extends SSIContestDetailsBaseAction
{

  private static Comparator<SSIContestListValueBean> ALPHA_COMPARATOR = new Comparator<SSIContestListValueBean>()
  {
    public int compare( SSIContestListValueBean vb1, SSIContestListValueBean vb2 )
    {
      return vb1.getName().compareTo( vb2.getName() );
    }
  };

  /** Display contest list page for participant
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
    // get contest based on participant manager
    List<SSIContestListValueBean> view = getSSIContestService().getManagerLiveContests( UserManager.getUserId() );
    for ( SSIContestListValueBean contestListValueBean : view )
    {
      SSIAdminContestActions adminContestAction = getSSIContestService().getAdminActionByEditCreator( contestListValueBean.getContestId() );
      if ( adminContestAction != null )
      {
        contestListValueBean.setUpdatedBy( getParticipantService().getLNameFNameByPaxIdWithComma( adminContestAction.getUserID() ) );
        contestListValueBean.setUpdatedOn( DateUtils.toDisplayString( adminContestAction.getAuditCreateInfo().getDateCreated() ) );
      }
    }
    Collections.sort( view, ALPHA_COMPARATOR );
    ssiContestListForm.setInitializationJson( toJson( view ) );

    // load the details for the passed contest id
    if ( ssiContestListForm.getId() != null )
    {
      boolean decode = !request.getMethod().equalsIgnoreCase( "GET" );
      Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), decode );
      SSIContest contest = getSSIContestService().getContestById( contestId );
      SSIPaxContestDataView ssiPaxContestDataView = populatePaxContestDataView( contest );
      ssiContestListForm.setContestJson( toJson( ssiPaxContestDataView ) );
    }
    ssiContestListForm.setRole( SSIContest.CONTEST_ROLE_MGR );
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  /** Display contest list page for participant
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchLiveContests( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<SSIContestListValueBean> contestList = getSSIContestService().getManagerLiveContests( UserManager.getUserId() );

    for ( SSIContestListValueBean contest : contestList )
    {

      switch ( contest.getContestType() )
      {
        case "1":
          contest.setContestTypeName( "awardThemNow" );
          break;

        case "2":
          contest.setContestTypeName( "doThisGetThat" );
          break;

        case "4":
          contest.setContestTypeName( "objectives" );
          break;

        case "8":
          contest.setContestTypeName( "stackRank" );
          break;

        case "16":
          contest.setContestTypeName( "stepItUp" );
          break;

        default:
          break;
      }
    }
    SSIContestListViewBean responseView = new SSIContestListViewBean( contestList );
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward fetchArchivedContests( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List<SSIContestListValueBean> contestList = getSSIContestService().getManagerArchivedContests( UserManager.getUserId() );
    super.writeAsJsonToResponse( contestList, response );
    return null;
  }

  /** Fetches contest details based on contest id
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
    Long userId = SSIContestUtil.getUserIdFromClientState( request, ssiContestListForm.getId(), true );
    SSIPaxContestDataView responseView = null;
    try
    {
      if ( displayDetail( userId ) )
      {
        Long contestId = SSIContestUtil.getContestIdFromClientState( request, ssiContestListForm.getId(), true );
        SSIContest contest = getSSIContestService().getContestById( contestId );
        responseView = populatePaxContestDataView( contest );
      }
    }
    catch( ServiceErrorException see )
    {
      responseView = new SSIPaxContestDataView( addServiceException( see ) );
    }
    super.writeAsJsonToResponse( responseView, response );
    return null;
  }

  private SSIPaxContestDataView populatePaxContestDataView( SSIContest contest ) throws ServiceErrorException
  {
    SSIContestUniqueCheckValueBean uniqueCheckValueBean = null;
    SSIContestValueBean valueBean = getContestValueBean( contest, 0 );
    List<SSIContestProgressValueBean> contestProgressData = getSSIContestParticipantService().getContestProgress( contest.getId(), UserManager.getUserId() );
    if ( contest.getContestType().isObjectives() )
    {
      uniqueCheckValueBean = getSSIContestParticipantService().performUniqueCheck( contest.getId() );
    }
    String creatorName = getFullName( contest.getCreatorId() );
    SSIPaxContestDataView view = new SSIPaxContestDataView( contest, contestProgressData, SSIContest.CONTEST_ROLE_MGR, valueBean, null, uniqueCheckValueBean, creatorName, true );
    return view;
  }

  public ActionForward fetchContestDetailsTable( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    ssiContestListForm.setRole( SSIContest.CONTEST_ROLE_MGR );
    ssiContestListForm.setUserId( UserManager.getUserId() );
    return super.fetchContestSummaryTable( mapping, ssiContestListForm, request, response );
  }

  public ActionForward fetchStackRank( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    SSIContestListForm ssiContestListForm = (SSIContestListForm)form;
    ssiContestListForm.setRole( SSIContest.CONTEST_ROLE_MGR );
    ssiContestListForm.setUserId( UserManager.getUserId() );
    return super.fetchStackRankTable( mapping, ssiContestListForm, request, response );
  }

}
