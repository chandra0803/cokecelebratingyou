
package com.biperf.core.service.recognitionadvisor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.biperf.core.dao.recognitionadvisor.RecognitionAdvisorDao;
import com.biperf.core.service.recognitionadvisor.RecognitionAdvisorService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.recognitionadvisor.EligibleProgramBean;
import com.biperf.core.ui.recognitionadvisor.RAEligibleProgramsView;
import com.biperf.core.ui.recognitionadvisor.RAReminderParticipantsValueBean;
import com.biperf.core.ui.recognitionadvisor.RecognitionAdvisorView;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorUnusedBudgetBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

/**
 * Used for the RecognitonAdvisor Algorithm screen(s).
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Ramesh J</td>
 * <td>Dec 11, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class RecognitionAdvisorServiceImpl implements RecognitionAdvisorService
{
  private static final Log log = LogFactory.getLog( RecognitionAdvisorServiceImpl.class );
  private @Autowired RecognitionAdvisorDao recognitionAdvisorDao;
  private @Autowired SystemVariableService systemVariableService;

  public RecognitionAdvisorView showRAReminderPaxData( Long userId,
                                                       Long rowStart,
                                                       Long rowEnd,
                                                       String sortColumnName,
                                                       String sortBy,
                                                       Long excludeUpcoming,
                                                       String filterValue,
                                                       String activePage,
                                                       Long pendingStatus )
  {

    Long rowNumStart = rowStart;
    Long rowNumEnd = rowEnd;
    Long rowEndCalculation;

    if ( ( rowEndCalculation = Long.parseLong( activePage ) ) > 0L && excludeUpcoming != 1L )
    {
      rowNumEnd = rowEndCalculation * 50L;
      rowNumStart = rowNumEnd - 49L;
    }

    if ( filterValue.equals( "0" ) )
    {
      filterValue = "";
    }

    return prePareRAReminderPaxDataInJson( recognitionAdvisorDao.showRAReminderPaxData( userId, rowNumStart, rowNumEnd, sortColumnName, sortBy, excludeUpcoming, filterValue, pendingStatus ),
                                           excludeUpcoming.toString(),
                                           rowNumEnd,
                                           rowNumStart );
  }

  private RecognitionAdvisorView prePareRAReminderPaxDataInJson( List<RecognitionAdvisorValueBean> raPaxList, String raExcludeUpcoming, Long rowNumEnd, Long rowNumStart )
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "raPaxList :" + raPaxList.size() );
    }

    RecognitionAdvisorView recognitionAdvisorView = new RecognitionAdvisorView();
    recognitionAdvisorView.setRaModel( Boolean.FALSE );
    recognitionAdvisorView.setRaEndModal( Boolean.FALSE );

    RAReminderParticipantsValueBean raReminderParticipants = new RAReminderParticipantsValueBean();

    if ( !raPaxList.isEmpty() )
    {
      prepareRecognitionDetailUrlForCurrentMgr( raPaxList );
      prepareRecognitionDetailUrlForOtherMgr( raPaxList );

      List<RecognitionAdvisorValueBean> newHirePax = raPaxList.stream().filter( ( p ) -> p.getParticipantGroupType() == 1 && Objects.isNull( p.getRecApprovalStatusType() ) )
          .collect( Collectors.toList() );
      List<RecognitionAdvisorValueBean> overDuePax = raPaxList.stream().filter( ( p ) -> p.getParticipantGroupType() == 2 && Objects.isNull( p.getRecApprovalStatusType() ) )
          .collect( Collectors.toList() );
      List<RecognitionAdvisorValueBean> upcomingPax = raPaxList.stream().filter( ( p ) -> p.getParticipantGroupType() == 3 && Objects.isNull( p.getRecApprovalStatusType() ) )
          .collect( Collectors.toList() );

      if ( rowNumEnd >= 50L && !raExcludeUpcoming.equals( "1" ) )
      {
        raReminderParticipants.setSortingAndPaginationParticipants( raPaxList );
        raReminderParticipants.setRecentlyAddedParticipants( newHirePax );
        raReminderParticipants.setOverDueParticipants( overDuePax );
        raReminderParticipants.setUpComingParticipants( upcomingPax );
        recognitionAdvisorView.setRaTotalParticipants( raPaxList.stream().findFirst().get().getRaTotalParticipants() );
        recognitionAdvisorView.setRaEndModal( Boolean.FALSE );
        recognitionAdvisorView.setRaModel( Boolean.TRUE );
        recognitionAdvisorView.setTableRowNumStart( rowNumStart );
        recognitionAdvisorView.setTableRowNumEnd( ( rowNumStart + raPaxList.size() ) - 1L );
        recognitionAdvisorView.setRaReminderParticipants( raReminderParticipants );

        recognitionAdvisorView.setNewHireAvailable( raPaxList.stream().findFirst().get().getNewHireAvailable() );
        recognitionAdvisorView.setOverDueAvailable( raPaxList.stream().findFirst().get().getOverDueAvailable() );
        recognitionAdvisorView.setUpComingAvailable( raPaxList.stream().findFirst().get().getUpComingAvailable() );

        recognitionAdvisorView.setNewHireTotalcount( raPaxList.stream().findFirst().get().getNewHireTotalcount() );
        recognitionAdvisorView.setOverDueTotalcount( raPaxList.stream().findFirst().get().getOverDueTotalcount() );

      }
      else
      {

        if ( raExcludeUpcoming.equals( "1" ) )
        {
          List<RecognitionAdvisorValueBean> raModalPaxList = raPaxList.stream().limit( 2 ).filter( ( p ) -> p.getParticipantGroupType() != 3 ).collect( Collectors.toList() );

          if ( !raModalPaxList.isEmpty() )
          {
            raReminderParticipants.setRecentlyAddedParticipants( raModalPaxList );
            recognitionAdvisorView.setRaEndModalTotalParticipants( raPaxList.stream().findFirst().get().getRaTotalParticipants() );
            recognitionAdvisorView.setRaEndModal( Boolean.TRUE );
          }

        }
        else
        {
          recognitionAdvisorView.setRaEndModal( Boolean.FALSE );
          recognitionAdvisorView.setRaModel( Boolean.TRUE );
          raReminderParticipants.setRecentlyAddedParticipants( newHirePax );
          raReminderParticipants.setOverDueParticipants( overDuePax );
          raReminderParticipants.setUpComingParticipants( upcomingPax );

        }

        recognitionAdvisorView.setRaReminderParticipants( raReminderParticipants );
        recognitionAdvisorView.setRaTotalParticipants( raPaxList.stream().findFirst().get().getRaTotalParticipants() );

        recognitionAdvisorView.setNewHireTotalcount( raPaxList.stream().findFirst().get().getNewHireTotalcount() );
        recognitionAdvisorView.setOverDueTotalcount( raPaxList.stream().findFirst().get().getOverDueTotalcount() );

      }
    }
    else
    {
      recognitionAdvisorView.setRaTotalParticipants( 0L );
      recognitionAdvisorView.setTableRowNumStart( 0L );
      recognitionAdvisorView.setTableRowNumEnd( 0L );
      recognitionAdvisorView.setRaReminderParticipants( raReminderParticipants );
      recognitionAdvisorView.setRaModel( Boolean.FALSE );
      recognitionAdvisorView.setNewHireAvailable( "0" );
      recognitionAdvisorView.setOverDueAvailable( "0" );
      recognitionAdvisorView.setUpComingAvailable( "0" );
    }

    return recognitionAdvisorView;

  }

  @Override
  public List<RecognitionAdvisorValueBean> showRAReminderNewHireEmailPaxData( Long paxId )
  {
    return recognitionAdvisorDao.getRANewHireForEmail( paxId );
  }

  @Override
  public List<RecognitionAdvisorValueBean> getRATeamMemberReminderData( Long participantId )
  {
    return recognitionAdvisorDao.getRATeamMemberReminderData( participantId );

  }

  @Override
  public Long getLongOverDueNewHireForManager( Long participantId )
  {
    return recognitionAdvisorDao.getLongOverDueNewHireForManager( participantId );

  }

  @Override
  public List<AlertsValueBean> checkNewEmployeeAndTeamMemberExist( Long participantId )
  {
    return recognitionAdvisorDao.checkNewEmployeeAndTeamMemberExist( participantId );
  }

  private void prepareRecognitionDetailUrlForCurrentMgr( List<RecognitionAdvisorValueBean> paxList )
  {
    paxList.forEach( p ->
    {
      Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();

      String reqUrl = null;

      if ( Objects.nonNull( p.getClaimIdByCurrentMgr() ) )
      {
        if ( Long.parseLong( p.getClaimIdByCurrentMgr() ) != 0L )
        {
          clientStateParameterMap.put( "claimId", Long.parseLong( p.getClaimIdByCurrentMgr() ) );
          clientStateParameterMap.put( "isFullPage", "false" );

          reqUrl = ClientStateUtils.generateEncodedLink( "",
                                                         systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + PageConstants.CLAIM_DETAIL_URL,
                                                         clientStateParameterMap );
        }

      }

      p.setClaimUrlByCurrentMgr( reqUrl );

    } );

  }

  private void prepareRecognitionDetailUrlForOtherMgr( List<RecognitionAdvisorValueBean> paxList )
  {
    paxList.forEach( p ->
    {
      Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();

      String reqUrl = null;

      if ( Objects.nonNull( p.getClaimIdByOtherMgr() ) )
      {
        if ( Long.parseLong( p.getClaimIdByOtherMgr() ) != 0L )
        {
          clientStateParameterMap.put( "claimId", Long.parseLong( p.getClaimIdByOtherMgr() ) );
          clientStateParameterMap.put( "isFullPage", "false" );

          reqUrl = ClientStateUtils.generateEncodedLink( "",
                                                         systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + PageConstants.CLAIM_DETAIL_URL,
                                                         clientStateParameterMap );
        }

      }

      p.setClaimUrlByOtherMgr( reqUrl );

    } );
  }

  @Override
  public RAEligibleProgramsView getEligiblePromotions( Long userId, boolean isRAEnabled )
  {
    List<EligibleProgramBean> eligiblePrograms = recognitionAdvisorDao.getEligiblePromotions( userId );
    RAEligibleProgramsView raEligibleProgramsView = new RAEligibleProgramsView();
    raEligibleProgramsView.setEligiblePrograms( eligiblePrograms );
    raEligibleProgramsView.setRaEnabled( isRAEnabled );
    return raEligibleProgramsView;
  }

  @Override
  public List<RecognitionAdvisorUnusedBudgetBean> getRAUnusedBudgetReminderData( Long participantId )
  {
    return recognitionAdvisorDao.getRAUnusedBudgetReminderData( participantId );
  }

  @Override
  public boolean isHavingMembers( Long participantId )
  {
    return recognitionAdvisorDao.isHavingMembers( participantId );
  }

}
