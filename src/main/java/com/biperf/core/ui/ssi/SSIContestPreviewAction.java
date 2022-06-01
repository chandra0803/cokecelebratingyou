
package com.biperf.core.ui.ssi;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestActivity;
import com.biperf.core.domain.ssi.SSIContestAwardThemNow;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ssi.view.SSIContestAwardThemNowPreviewView;
import com.biperf.core.ui.ssi.view.SSIContestDoThisGetThatPreviewView;
import com.biperf.core.ui.ssi.view.SSIContestObjectivePreviewView;
import com.biperf.core.ui.ssi.view.SSIContestPreviewResponseView;
import com.biperf.core.ui.ssi.view.SSIContestPreviewView;
import com.biperf.core.ui.ssi.view.SSIContestPreviewViewWrapper;
import com.biperf.core.ui.ssi.view.SSIContestStackRankPreviewView;
import com.biperf.core.ui.ssi.view.SSIContestStepItUpPreviewView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.value.ssi.SSIContestBaseLineTotalsValueBean;
import com.biperf.core.value.ssi.SSIContestPayoutStackRankTotalsValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * SSIContestPreviewAction.
 * 
 * @author Patelp
 * @since Dec 26, 2014
 * @version 1.0
 */

public class SSIContestPreviewAction extends SSIContestCreateBaseAction
{

  private static final String AWARD_THEM_NOW_CONTEST = "Award Them Now";
  private static final String WHITESPACE = " ";

  /**
   * Display preview of SSIContest
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      Long contestId = getContestIdFromClientStateMap( request );
      SSIContestPreviewView previewViewBeanWithRespectiveContest = getPreviewViewBeanWithRespectiveContest( request );
      writeAsJsonToResponse( previewViewBeanWithRespectiveContest, response );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ActionMessages errors = new ActionMessages();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( error.getKey() ) );
      }
      saveErrors( request, errors );
    }
    return null;
  }

  /**
   * update SSIContest Status
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */

  public ActionForward create( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContest contest = getSSIContestService().getContestById( contestId );
    SSIContestPreviewResponseView previewResponse = null;
    if ( SSIContestStatus.DRAFT.equals( contest.getStatus().getCode() ) )
    {
      getSSIContestService().updateContestStatus( getContestIdFromClientStateMap( request ) );
      previewResponse = new SSIContestPreviewResponseView( getSysUrl() );
      request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
      int sstContentApproval = getSSIContestService().getRequireContentApprovalProcess( contestId );
      if ( sstContentApproval == SSIContestUtil.SSTCONTENTAPPROVAL )
      {
        request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.CREATE_SUCCESS_APPROVAL" ) );
      }
      else
      {
        request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.CREATE_SUCCESS" ) );
      }
    }
    else
    {
      previewResponse = new SSIContestPreviewResponseView( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.INVALID_OPERATION" ),
                                                           CmsResourceBundle.getCmsBundle().getString( "ssi_contest.preview.ERROR" ) );
    }
    writeAsJsonToResponse( previewResponse, response );
    return null;
  }

  /**
   * update SSI Award Them Now Contest Status
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws Exception 
   */

  public ActionForward issueAwardsAtn( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    try
    {
      Long contestId = getContestIdFromClientStateMap( request );
      SSIContest contest = getSSIContestService().getContestById( contestId );
      getSSIContestAwardThemNowService().updateAwardThemNowContestStatus( contestId, getAwardIssuanceNumberFromClientState( request ) );
      SSIContestPreviewResponseView ssiContestPreviewResponseView = new SSIContestPreviewResponseView( getSysUrl() );
      writeAsJsonToResponse( ssiContestPreviewResponseView, response );
      request.getSession().setAttribute( SSIContestUtil.SHOW_MODAL, Boolean.TRUE );
      if ( contest.getPromotion().getRequireContestApproval() )
      {
        request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.AWARDS_WAITING" ) );
      }
      else
      {
        request.getSession().setAttribute( SSIContestUtil.MODAL_MESSAGE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.creator.AWARDS_ISSUED" ) );
      }
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ActionMessages errors = new ActionMessages();
      for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
      {
        ServiceError error = (ServiceError)iter.next();
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( error.getKey() ) );
      }
      saveErrors( request, errors );
    }
    return null;
  }

  private SSIContestPreviewView getPreviewViewBeanWithRespectiveContest( HttpServletRequest request ) throws ServiceErrorException, Exception
  {
    SSIContestPreviewView contestPreviewView = null;
    Long contestId = getContestIdFromClientStateMap( request );
    SSIContest contest = getSSIContestService().getContestById( contestId );
    Short awardIssuanceNumber = null;
    if ( SSIContestType.AWARD_THEM_NOW.equals( contest.getContestType().getCode() ) )
    {
      awardIssuanceNumber = getAwardIssuanceNumberFromClientState( request );
    }
    SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper = getSSIContestPreviewViewWrapper( contest,
                                                                                                 RequestUtils.getRequiredParamString( request, "ssiContestClientState" ),
                                                                                                 awardIssuanceNumber );
    if ( SSIContestType.OBJECTIVES.equals( contest.getContestType().getCode() ) )
    {
      contestPreviewView = getSSIContestObjectivePreviewView( contest, ssiContestPreviewViewWrapper );
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( contest.getContestType().getCode() ) )
    {
      contestPreviewView = getSSIContestDoThisGetThatPreviewView( contest, ssiContestPreviewViewWrapper );
    }
    else if ( SSIContestType.STACK_RANK.equals( contest.getContestType().getCode() ) )
    {
      contestPreviewView = getSSIContestStackRankPreviewView( contest, ssiContestPreviewViewWrapper );
    }
    else if ( SSIContestType.AWARD_THEM_NOW.equals( contest.getContestType().getCode() ) )
    {
      setAwardThemNowEmailMessge( contestId, awardIssuanceNumber, ssiContestPreviewViewWrapper );
      contestPreviewView = getSSIContestAwardThemNowPreviewView( contest, ssiContestPreviewViewWrapper, awardIssuanceNumber );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( contest.getContestType().getCode() ) )
    {
      contestPreviewView = getSSIContestStepItUpPreviewView( contest, ssiContestPreviewViewWrapper );
    }
    contestPreviewView.setBillCodes( getBillCodeViewByContest( contest ) );

    return contestPreviewView;
  }

  private void setAwardThemNowEmailMessge( Long contestId, Short issuanceNumber, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper )
  {
    SSIContestAwardThemNow contestAwardThemNow = getSSIContestAwardThemNowService().getContestAwardThemNowByIdAndIssunace( contestId, issuanceNumber );
    if ( contestAwardThemNow != null )
    {
      ssiContestPreviewViewWrapper.setMessage( contestAwardThemNow.getNotificationMessageText() );
    }
  }

  private SSIContestPreviewView getSSIContestAwardThemNowPreviewView( SSIContest contest, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper, Short issuanceNumber )
      throws ServiceErrorException, Exception
  {
    setATNContestPayoutTotalAndUniqueCheckValueBean( contest, ssiContestPreviewViewWrapper, issuanceNumber );
    return new SSIContestAwardThemNowPreviewView( ssiContestPreviewViewWrapper );
  }

  private void setATNContestPayoutTotalAndUniqueCheckValueBean( SSIContest contest, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper, Short issuanceNumber )
      throws Exception, ServiceErrorException
  {
    ssiContestPreviewViewWrapper.setContestUniqueCheckValueBean( getSSIContestParticipantService().performUniqueCheck( contest.getId(), issuanceNumber ) );
    ssiContestPreviewViewWrapper.setContestPayoutTotalsValueBean( getSSIContestAwardThemNowService().calculatePayoutObjectivesTotals( contest.getId(), issuanceNumber ) );
  }

  private SSIContestPreviewView getSSIContestStackRankPreviewView( SSIContest contest, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper ) throws Exception, ServiceErrorException
  {
    ssiContestPreviewViewWrapper.setContest( contest );
    SSIContestPayoutStackRankTotalsValueBean totals = getSSIContestService().getStackRankTotals( contest.getId() );
    return new SSIContestStackRankPreviewView( ssiContestPreviewViewWrapper, totals );
  }

  private SSIContestPreviewView getSSIContestStepItUpPreviewView( SSIContest contest, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper ) throws Exception, ServiceErrorException
  {
    String activityPrefix = SSIContestUtil.getActivityPrefix( ssiContestPreviewViewWrapper.getContestValueBean() );
    String payoutPrefix = SSIContestUtil.getPayoutPrefix( ssiContestPreviewViewWrapper.getContestValueBean() );
    String payoutSuffix = SSIContestUtil.getPayoutSuffix( ssiContestPreviewViewWrapper.getContestValueBean() );
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    ssiContestPreviewViewWrapper.setSsiContestLevels( getSSIContestService().getContestLevelsByContestId( contest.getId() ) );
    if ( contest.isIncludeBonus() )
    {
      StringBuffer bonusRow = new StringBuffer();
      bonusRow.append( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.FOR_EVERY" ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( activityPrefix + SSIContestUtil.getFormattedValue( contest.getStepItUpBonusIncrement(), precision ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.OVER_LEVEL" ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( ssiContestPreviewViewWrapper.getSsiContestLevels().size() );
      bonusRow.append( WHITESPACE );
      bonusRow.append( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_stepitup.PAX_EARN" ) );
      bonusRow.append( WHITESPACE );
      bonusRow.append( payoutPrefix + SSIContestUtil.getFormattedValue( contest.getStepItUpBonusPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + payoutSuffix );
      ssiContestPreviewViewWrapper.setBonusRow( bonusRow.toString() );
    }
    SSIContestBaseLineTotalsValueBean totalValueBean = getSSIContestService().calculateBaseLineTotalsForStepItUp( contest.getId() );
    return new SSIContestStepItUpPreviewView( ssiContestPreviewViewWrapper, totalValueBean.getBaselineTotal() );
  }

  private SSIContestPreviewView getSSIContestDoThisGetThatPreviewView( SSIContest contest, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper )
  {
    List<SSIContestActivity> contestActivities = getSSIContestService().getContestActivitiesByContestId( contest.getId() );
    ssiContestPreviewViewWrapper.setContest( contest );
    ssiContestPreviewViewWrapper.setTotalMaxPayout( getSSIContestService().calculatePayoutDoThisGetThatTotals( contest.getId() ) );
    return new SSIContestDoThisGetThatPreviewView( ssiContestPreviewViewWrapper, contestActivities );
  }

  private SSIContestPreviewView getSSIContestObjectivePreviewView( SSIContest contest, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper ) throws Exception, ServiceErrorException
  {
    setContestPayoutTotalAndUniqueCheckValueBean( contest, ssiContestPreviewViewWrapper );
    return new SSIContestObjectivePreviewView( ssiContestPreviewViewWrapper );
  }

  private void setContestPayoutTotalAndUniqueCheckValueBean( SSIContest contest, SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper ) throws Exception, ServiceErrorException
  {
    ssiContestPreviewViewWrapper.setContestUniqueCheckValueBean( getSSIContestParticipantService().performUniqueCheck( contest.getId() ) );
    ssiContestPreviewViewWrapper.setContestPayoutTotalsValueBean( getSSIContestService().calculatePayoutObjectivesTotals( contest.getId() ) );
  }

  private SSIContestPreviewViewWrapper getSSIContestPreviewViewWrapper( SSIContest contest, String contestClientState, Short awardIssuanceNumber ) throws Exception
  {
    SSIContestPreviewViewWrapper ssiContestPreviewViewWrapper = new SSIContestPreviewViewWrapper();
    ssiContestPreviewViewWrapper.setContest( contest );
    int participantCount = 0;
    if ( contest.getContestType() != null && AWARD_THEM_NOW_CONTEST.equalsIgnoreCase( contest.getContestType().getName() ) )
    {
      participantCount = getATNContestParticipantsCount( contest.getId(), awardIssuanceNumber );
    }
    else
    {
      participantCount = getContestParticipantsCount( contest.getId() );
    }
    ssiContestPreviewViewWrapper.setParticipantsCount( participantCount );
    ssiContestPreviewViewWrapper.setManagersCount( getContestManagersCount( contest.getId() ) );
    ssiContestPreviewViewWrapper.setSuperViewersCount( getContestSuperViewersCount( contest.getId() ) );
    ssiContestPreviewViewWrapper.setSsiContestClientState( contestClientState );
    ssiContestPreviewViewWrapper.setContestValueBean( getContestValueBean( contest, participantCount ) );
    ssiContestPreviewViewWrapper.setSysUrl( getSysUrl() );
    return ssiContestPreviewViewWrapper;
  }

  private int getContestParticipantsCount( Long contestId )
  {
    return getSSIContestParticipantService().getContestParticipantsCount( contestId );
  }

  private int getATNContestParticipantsCount( Long contestId, Short awardIssuanceNumber )
  {
    return getSSIContestAwardThemNowService().getContestParticipantsCount( contestId, awardIssuanceNumber );
  }

  private int getContestManagersCount( Long contestId )
  {
    return getSSIContestParticipantService().getContestManagersCount( contestId );
  }

  private int getContestSuperViewersCount( Long contestId )
  {
    return getSSIContestParticipantService().getContestSuperViewersCount( contestId );
  }

}
