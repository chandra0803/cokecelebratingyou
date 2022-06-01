
package com.biperf.core.service.ssi.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.ssi.SSIContestDAO;
import com.biperf.core.dao.ssi.SSIContestParticipantDAO;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestParticipantProgress;
import com.biperf.core.domain.ssi.SSIContestSuperViewer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequest;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.ssi.SSIContestParticipantService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceUtil;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.ssi.SSIConetstParticipantActivityValueBean;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantProgressValueBean;
import com.biperf.core.value.ssi.SSIContestParticipantValueBean;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankTeamValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIPaxContestLevelValueBean;
import com.biperf.core.value.ssi.SSIPaxDTGTActivityProgressValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * Contest Participant handles any contest participant facing services 
 * SSIContestParticipantServiceImpl.
 * 
 * @author kandhi
 * @since Dec 2, 2014
 * @version 1.0
 */
public class SSIContestParticipantServiceImpl implements SSIContestParticipantService
{
  protected static final Log log = LogFactory.getLog( SSIContestParticipantServiceImpl.class );

  private static final String PAX_ROLE = "pax";

  private SSIContestParticipantDAO ssiContestParticipantDAO;
  private SSIContestDAO ssiContestDAO;
  private CMAssetService cmAssetService;
  private SystemVariableService systemVariableService;
  private SSIContestService ssiContestService;

  @Override
  public SSIContestParticipant getContestParticipantById( Long contestParticipantId )
  {
    return ssiContestParticipantDAO.getContestParticipantById( contestParticipantId );
  }

  @Override
  public Double getParticipantActivityAmoumt( Long contestId, Long participantId )
  {
    return this.ssiContestParticipantDAO.getParticipantActivityAmoumt( contestId, participantId );
  }

  @Override
  public SSIContestPaxProgressDetailValueBean getContestParticipantProgress( Long contestId, Long participantId ) throws ServiceErrorException
  {
    try
    {
      Map<String, Object> output = ssiContestParticipantDAO.getContestParticipantProgressDetail( contestId, participantId );
      String contestType = String.valueOf( (BigDecimal)output.get( "p_out_contest_type" ) );
      SSIContestPaxProgressDetailValueBean valueBean = null;
      if ( SSIContestType.OBJECTIVES.equals( contestType ) )
      {
        valueBean = (SSIContestPaxProgressDetailValueBean)output.get( "p_out_obj_ref_cursor" );
      }
      else if ( SSIContestType.DO_THIS_GET_THAT.equals( contestType ) )
      {
        valueBean = new SSIContestPaxProgressDetailValueBean( (List<SSIPaxDTGTActivityProgressValueBean>)output.get( "p_out_DTGT_cursor" ) );
      }
      else if ( SSIContestType.STEP_IT_UP.equals( contestType ) )
      {
        valueBean = new SSIContestPaxProgressDetailValueBean();
        valueBean.setLevels( (List<SSIPaxContestLevelValueBean>)output.get( "p_out_siu_ref_cursor" ) );
      }
      else if ( SSIContestType.STACK_RANK.equals( contestType ) )
      {
        valueBean = (SSIContestPaxProgressDetailValueBean)output.get( "p_out_sr_ref_cursor" );
        valueBean.setPayouts( (List<SSIContestStackRankPayoutValueBean>)output.get( "p_out_sr_payout_ref_cursor" ) );
        valueBean.setStackRankParticipants( (List<SSIContestStackRankTeamValueBean>)output.get( "p_out_sr_pax_ref_cursor" ) );
      }
      return valueBean;
    }
    catch( Exception e )
    {
      log.error( "Error while executing proc PKG_SSI_CONTEST_DATA.PRC_SSI_CONTEST_PAX_PROGRESS " + e );
      throw new ServiceErrorException( "ssi_contest.preview.PROGRESS_ERROR_MESG" );
    }
  }

  public void setSsiContestParticipantDAO( SSIContestParticipantDAO ssiContestParticipantDAO )
  {
    this.ssiContestParticipantDAO = ssiContestParticipantDAO;
  }

  public SSIContestDAO getSsiContestDAO()
  {
    return ssiContestDAO;
  }

  public void setSsiContestDAO( SSIContestDAO ssiContestDAO )
  {
    this.ssiContestDAO = ssiContestDAO;
  }

  public List<SSIContestListValueBean> getParticipantLiveContestsValueBean( Long participantId )
  {
    List<SSIContestListValueBean> contestList = ssiContestParticipantDAO.getParticipantLiveContestsValueBean( participantId );
    contestList = translateAssetCode( contestList );
    populateRoleAndUrl( contestList, PAX_ROLE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.pax.manager.PARTICIPANT" ) );
    return contestList;
  }

  private void populateRoleAndUrl( List<SSIContestListValueBean> contestList, String role, String roleLabel )
  {
    String siteUrl = getSysUrl();
    for ( SSIContestListValueBean valueBean : contestList )
    {
      Map<String, String> paramMap = new HashMap<String, String>();
      paramMap.put( SSIContestUtil.CONTEST_ID, valueBean.getContestId().toString() );
      String url = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_PAX_DETAIL_URL, paramMap, false, SSIContestUtil.SSI_CLIENTSTATE_PARAM_ID );
      valueBean.setRole( role );
      valueBean.setRoleLabel( roleLabel );
      valueBean.setDetailPageUrl( url );
    }
  }

  public List<SSIContest> getParticipantLiveContests( Long participantId )
  {
    return this.ssiContestParticipantDAO.getParticipantLiveContests( participantId );
  }

  public List<SSIContestListValueBean> getParticipantArchivedContests( Long participantId )
  {
    List<SSIContestListValueBean> contestList = ssiContestParticipantDAO.getParticipantArchivedContests( participantId );
    populateRoleAndUrl( contestList, PAX_ROLE, CmsResourceBundle.getCmsBundle().getString( "ssi_contest.pax.manager.PARTICIPANT" ) );
    return translateAssetCode( contestList );
  }

  private List<SSIContestListValueBean> translateAssetCode( List<SSIContestListValueBean> contestList )
  {
    // Translate the asset code
    for ( SSIContestListValueBean contest : contestList )
    {
      contest.setName( cmAssetService.getString( contest.getName(), SSIContest.CONTEST_CMASSET_NAME, UserManager.getLocale(), true ) );
    }
    return contestList;
  }

  @Override
  public int getContestParticipantsCount( Long contestId )
  {
    return this.ssiContestParticipantDAO.getContestParticipantsCount( contestId );
  }

  @Override
  public List<SSIContestParticipant> getContestParticipants( Long contestId, Integer pageNumber, String sortColumnName, String sortedBy )
  {
    return ssiContestParticipantDAO.getContestParticipants( contestId, pageNumber, SSIContestUtil.PAX_RECORDS_PER_PAGE, sortColumnName, sortedBy );
  }

  @Override
  public List<SSIContestParticipant> getContestParticipantsWithAssociations( Long contestId,
                                                                             Integer pageNumber,
                                                                             String sortColumnName,
                                                                             String sortedBy,
                                                                             AssociationRequestCollection associationRequestCollection )
  {
    List<SSIContestParticipant> participants = this.getContestParticipants( contestId, pageNumber, sortColumnName, sortedBy );
    if ( associationRequestCollection != null )
    {
      for ( SSIContestParticipant participant : participants )
      {
        for ( Iterator<AssociationRequest> iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
        {
          AssociationRequest req = iterator.next();
          req.execute( participant.getParticipant() );
        }
      }
    }
    return participants;
  }

  @Override
  public int getContestManagersCount( Long contestId )
  {
    return this.ssiContestParticipantDAO.getContestManagersCount( contestId );
  }

  @Override
  public int getContestSuperViewersCount( Long contestId )
  {
    return this.ssiContestParticipantDAO.getContestSuperViewersCount( contestId );
  }

  @Override
  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId ) throws ServiceErrorException
  {
    try
    {
      return this.ssiContestParticipantDAO.performUniqueCheck( contestId );
    }
    catch( Exception e )
    {
      log.error( "Error while executing Proc PKG_SSI_CONTEST.PRC_SSI_CONTEST_UNIQUENESS " + e );
      throw new ServiceErrorException( "system.errors.manager.SYSTEM_EXCEPTION" );
    }
  }

  @Override
  public List<SSIContestManager> getContestManagers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy, AssociationRequestCollection associationRequestCollection )
  {
    List<SSIContestManager> managers = this.ssiContestParticipantDAO.getContestManagers( contestId, pageNumber, SSIContestManager.RECORDS_PER_PAGE, sortedOn, sortedBy );
    if ( associationRequestCollection != null )
    {
      for ( SSIContestManager manager : managers )
      {
        for ( Iterator<AssociationRequest> iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
        {
          AssociationRequest req = iterator.next();
          req.execute( manager.getManager() );
        }
      }
    }
    return managers;
  }

  @Override
  public List<SSIContestSuperViewer> getContestSuperviewers( Long contestId, Integer pageNumber, String sortedOn, String sortedBy, AssociationRequestCollection associationRequestCollection )
  {
    List<SSIContestSuperViewer> superviewers = this.ssiContestParticipantDAO.getContestSuperviewers( contestId, pageNumber, SSIContestManager.RECORDS_PER_PAGE, sortedOn, sortedBy );
    if ( associationRequestCollection != null )
    {
      for ( SSIContestSuperViewer superviewer : superviewers )
      {
        for ( Iterator<AssociationRequest> iterator = associationRequestCollection.iterator(); iterator.hasNext(); )
        {
          AssociationRequest req = iterator.next();
          req.execute( superviewer.getSuperViewer() );
        }
      }
    }
    return superviewers;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  /**
   * Method to save the contest and participant progress
   * {@inheritDoc}
   */
  @Override
  public void saveContestParticipantProgress( Long contestId, Date activityDate, List<SSIConetstParticipantActivityValueBean> participantActivities )
  {
    // Look up the SSIContest
    SSIContest attachedContest = this.ssiContestDAO.getContestById( contestId );
    // update the Last Progress Update Date on the contest
    attachedContest.setLastProgressUpdateDate( activityDate );
    updateContestParticipantProgress( attachedContest, activityDate, participantActivities );
  }

  @Override
  public List<SSIContestParticipantProgressValueBean> getContestParticipantsProgresses( Long contestId, int currentPage, int resultsPerPage, String sortedOn, String sortedBy, boolean isDoThisGetThat )
  {
    return ssiContestParticipantDAO.getContestParticipantsProgresses( contestId, currentPage, resultsPerPage, sortedOn, sortedBy, isDoThisGetThat );
  }

  private void updateContestParticipantProgress( SSIContest contest, Date activityDate, List<SSIConetstParticipantActivityValueBean> participantActivities )
  {
    List<Long> paxIds = getParticipantIds( participantActivities );

    if ( paxIds.size() > 0 )
    {
      List<SSIContestParticipantProgress> attachedContestParticipantProgresses = ssiContestParticipantDAO.getContestParticipantsProgresses( contest.getId(), paxIds );
      updateContestParticipantProgress( contest, activityDate, attachedContestParticipantProgresses, participantActivities );
    }
  }

  private List<Long> getParticipantIds( List<SSIConetstParticipantActivityValueBean> participantActivities )
  {
    List<Long> paxIds = new ArrayList<Long>();
    for ( SSIConetstParticipantActivityValueBean participantActivity : ServiceUtil.emptyIfNull( participantActivities ) )
    {
      paxIds.add( participantActivity.getId() );
    }
    return paxIds;
  }

  private void updateContestParticipantProgress( SSIContest contest,
                                                 Date activityDate,
                                                 List<SSIContestParticipantProgress> attachedContestParticipantProgress,
                                                 List<SSIConetstParticipantActivityValueBean> participantActivities )
  {
    for ( SSIConetstParticipantActivityValueBean participantActivity : ServiceUtil.emptyIfNull( participantActivities ) )
    {
      int activityIndex = 0;
      // for each activity of participant; multiple activities for DTGT contest; only one for other
      // contests
      if ( participantActivity.getActivityAsList() != null && participantActivity.getActivityAsList().size() > 0 )
      {
        for ( SSIConetstParticipantActivityValueBean.Activity activity : participantActivity.getActivityAsList() )
        {
          if ( !StringUtil.isNullOrEmpty( activity.getTotalActivity() ) )
          {
            updateContestParticipantActivityProgress( contest, activityDate, attachedContestParticipantProgress, participantActivity, activityIndex );
          }
          activityIndex++;
        }
      }
    }
  }

  private void updateContestParticipantActivityProgress( SSIContest contest,
                                                         Date activityDate,
                                                         List<SSIContestParticipantProgress> attachedContestParticipantProgress,
                                                         SSIConetstParticipantActivityValueBean participantActivity,
                                                         int activityIndex )
  {
    boolean newActivity = true;

    for ( SSIContestParticipantProgress participantProgress : ServiceUtil.emptyIfNull( attachedContestParticipantProgress ) )
    {
      if ( participantProgress.getParticipant().getId().equals( participantActivity.getId() )
          && ( participantProgress.getContestActivity() == null || participantProgress.getContestActivity().getId().equals( participantActivity.getActivityAsList().get( activityIndex ).getId() ) ) )
      {
        // update existing activity
        participantProgress.setActivityAmount( Double.parseDouble( SSIContestUtil.removeComma( participantActivity.getActivityAsList().get( activityIndex ).getTotalActivity() ) ) );
        participantProgress.setActivityDate( activityDate );
        newActivity = false;
        break;
      }
    }
    if ( newActivity )
    {
      // add new activity
      SSIContestParticipantProgress newPaxProgressObject = new SSIContestParticipantProgress();
      newPaxProgressObject.setContest( contest );
      // TODO; optimize; batch insert / fetch all pax in single call ???
      newPaxProgressObject.setParticipant( ssiContestParticipantDAO.getParticipant( participantActivity.getId() ) );
      newPaxProgressObject.setActivityAmount( Double.parseDouble( participantActivity.getActivityAsList().get( activityIndex ).getTotalActivity() ) );
      newPaxProgressObject.setActivityDate( activityDate );
      // TODO; optimize ; batch insert / fetch all contest activities in single call???
      if ( isDoThisGetThat( contest ) )
      {
        newPaxProgressObject.setContestActivity( this.ssiContestDAO.getContestActivityById( participantActivity.getActivityAsList().get( activityIndex ).getId() ) );
      }
      ssiContestParticipantDAO.saveContestParticipantProgress( newPaxProgressObject );
    }
  }

  protected boolean isDoThisGetThat( SSIContest contest )
  {
    return SSIContestType.DO_THIS_GET_THAT.equals( contest.getContestType().getCode() );
  }

  @Override
  public void updateSameValueForAllPax( Long contestId, String key, SSIContestParticipantValueBean participant )
  {
    ssiContestDAO.updateSameValueForAllPax( contestId, key, participant );
  }

  public boolean isPaxContestCreatorSuperViewer( Long participantId )
  {
    return ssiContestParticipantDAO.isPaxContestCreator( participantId ) || ssiContestParticipantDAO.isPaxContestSuperViewer( participantId );
  }

  public boolean isPaxInContestPaxAudience( Long participantId )
  {
    return ssiContestParticipantDAO.isPaxInContestPaxAudience( participantId );
  }

  public boolean isPaxInContestManagerAudience( Long participantId )
  {
    return ssiContestParticipantDAO.isPaxInContestManagerAudience( participantId );
  }

  public List<SSIContestStackRankPaxValueBean> getContestStackRank( Long contestId, Long userId, Long activityId, int currentPage, int resultsPerPage, boolean isTeam, boolean isIncludeAll )
      throws ServiceErrorException
  {
    try
    {
      return ssiContestParticipantDAO.getContestStackRank( contestId, userId, activityId, currentPage, resultsPerPage, isTeam, isIncludeAll );
    }
    catch( Exception e )
    {
      log.error( "Stack Rank Procedure Returned Error: " + e );
      throw new ServiceErrorException( "ssi_contest.creator.STACKRANK_POPULATE_ERR" );
    }
  }

  @Override
  public SSIContestUniqueCheckValueBean performUniqueCheck( Long contestId, Short issuanceNumber ) throws ServiceErrorException
  {
    try
    {
      return this.ssiContestParticipantDAO.performUniqueCheck( contestId, issuanceNumber );
    }
    catch( Exception e )
    {
      log.error( "Error while executing Proc PKG_SSI_CONTEST.PRC_SSI_CONTEST_UNIQUENESS " + e );
      throw new ServiceErrorException( "system.errors.manager.SYSTEM_EXCEPTION" );
    }
  }

  public List<SSIContestProgressValueBean> getContestProgress( Long contestId, Long userId ) throws ServiceErrorException
  {
    List<SSIContestProgressValueBean> contestProgressData = new ArrayList<SSIContestProgressValueBean>();
    Map<String, Object> output = this.ssiContestDAO.getContestProgress( contestId, userId );
    String contestType = (String)output.get( "contestType" );
    SSIContest contest = this.ssiContestDAO.getContestById( contestId );
    String activityMeasureType = contest.getActivityMeasureType().getCode();

    List<SSIContestStackRankTeamValueBean> contestProgressStackRanks = (List<SSIContestStackRankTeamValueBean>)output.get( "contestProgressStackRankData" );

    SSIContestProgressValueBean contestProgress = null;
    List<SSIContestProgressValueBean> contestProgresses = null;
    List<SSIPaxContestLevelValueBean> contestProgressSiuLevels = null;
    List<SSIContestStackRankPayoutValueBean> contestProgressSrPayouts = null;

    if ( SSIContestType.OBJECTIVES.equals( contestType ) || SSIContestType.DO_THIS_GET_THAT.equals( contestType ) )
    {
      contestProgresses = (List<SSIContestProgressValueBean>)output.get( "contestProgressData" );
      contestProgressData.addAll( contestProgresses );
      if ( contestProgressStackRanks != null && contestProgressStackRanks.size() > 0 )
      {
        for ( SSIContestProgressValueBean contestProgressValueBean : contestProgresses )
        {
          List<SSIContestStackRankTeamValueBean> filteredStackRanks = new ArrayList<SSIContestStackRankTeamValueBean>();
          for ( SSIContestStackRankTeamValueBean contestProgressStackRank : contestProgressStackRanks )
          {
            if ( contestProgressValueBean.getActivityId().equals( contestProgressStackRank.getActivityId() ) )
            {
              filteredStackRanks.add( contestProgressStackRank );
            }
          }
          contestProgressValueBean.addStackRanks( filteredStackRanks, activityMeasureType );
        }
      }
    }
    else if ( SSIContestType.STEP_IT_UP.equals( contestType ) )
    {
      contestProgress = (SSIContestProgressValueBean)output.get( "contestProgressSiuData" );
      contestProgressSiuLevels = (List<SSIPaxContestLevelValueBean>)output.get( "contestProgressSiuLevelsData" );
      if ( contestProgressSiuLevels != null && contestProgressSiuLevels.size() > 0 )
      {
        contestProgress.setContestSiuLevels( contestProgressSiuLevels );
      }
      if ( contestProgressStackRanks != null && contestProgressStackRanks.size() > 0 )
      {
        contestProgress.addStackRanks( contestProgressStackRanks, activityMeasureType );
      }
      contestProgressData.add( contestProgress );// adding progress data to main list
    }
    else if ( SSIContestType.STACK_RANK.equals( contestType ) )
    {
      contestProgress = (SSIContestProgressValueBean)output.get( "contestProgressSrData" );
      contestProgressSrPayouts = (List<SSIContestStackRankPayoutValueBean>)output.get( "contestProgressSrPayoutsData" );
      List<SSIContestStackRankTeamValueBean> paxPayouts = (List<SSIContestStackRankTeamValueBean>)output.get( "contestProgressStackRankPaxData" );
      if ( contestProgressSrPayouts != null && contestProgressSrPayouts.size() > 0 )
      {
        contestProgress.setPayouts( contestProgressSrPayouts );
      }
      if ( paxPayouts != null && paxPayouts.size() > 0 )
      {
        contestProgress.setStackRankParticipants( paxPayouts );
      }
      contestProgressData.add( contestProgress );
    }
    return contestProgressData;
  }

  public SSIContestProgressValueBean getContestProgressForTile( Long contestId ) throws ServiceErrorException
  {
    Map<String, Object> output = this.ssiContestDAO.getContestProgressForTile( contestId );
    Integer contestType = (Integer)output.get( "contestType" );
    SSIContestProgressValueBean contestProgress = null;
    if ( Integer.parseInt( SSIContestType.OBJECTIVES ) == contestType )
    {
      contestProgress = (SSIContestProgressValueBean)output.get( "contestProgressObjData" );
    }
    else if ( Integer.parseInt( SSIContestType.STEP_IT_UP ) == contestType )
    {
      contestProgress = (SSIContestProgressValueBean)output.get( "contestProgressSiuData" );
    }
    else if ( Integer.parseInt( SSIContestType.STACK_RANK ) == contestType )
    {
      contestProgress = (SSIContestProgressValueBean)output.get( "contestProgressSrData" );
    }
    return contestProgress;
  }

  public List<Long> getContestProgressLoadParticipantIdsByImportFileId( Long contestId, Long importFileId ) throws ServiceErrorException
  {
    return ssiContestParticipantDAO.getContestProgressLoadParticipantIdsByImportFileId( contestId, importFileId );
  }

  @Override
  public SSIContestParticipant getSSIContestParticipantByPaxId( Long contestId, Long userId )
  {

    return ssiContestParticipantDAO.getSSIContestParticipantByPaxId( contestId, userId );
  }

  protected String getSysUrl()
  {
    return systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  @Override
  public boolean isSuperViewer( SSIContest contest, Long userId )
  {
    if ( contest.getCreatorId().equals( userId ) )
    {
      return false;
    }
    else
    {
      boolean loggedInAsParticipant = this.ssiContestDAO.isParticipantInSsiContest( contest.getId(), userId );
      if ( loggedInAsParticipant )
      {
        return false;
      }
    }
    return true;
  }

  public SSIContestService getSsiContestService()
  {
    return ssiContestService;
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

}
