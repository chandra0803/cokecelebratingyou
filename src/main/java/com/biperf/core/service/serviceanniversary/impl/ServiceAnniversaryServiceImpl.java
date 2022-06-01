package com.biperf.core.service.serviceanniversary.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biperf.core.dao.engageprogram.EngageProgramDAO;
import com.biperf.core.dao.participant.ParticipantDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.serviceanniversary.ServiceAnniversaryDAO;
import com.biperf.core.domain.engageprogram.EngageProgram;
import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.domain.purl.PurlCelebration;
import com.biperf.core.domain.serviceanniversary.SACelebrationInfo;
import com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo;
import com.biperf.core.exception.DataException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryRepositoryFactory;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.utils.CmxTranslateHelperBean;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.SAConstants;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.serviceanniversary.SAValueBean;

@Service( "serviceAnniversaryService" )
public class ServiceAnniversaryServiceImpl implements ServiceAnniversaryService
{

  private static final Log log = LogFactory.getLog( ServiceAnniversaryServiceImpl.class );

  @Autowired
  private ServiceAnniversaryDAO serviceAnniversaryDAO;

  @Autowired
  private ServiceAnniversaryRepositoryFactory serviceAnniversaryRepo;

  @Autowired
  private UserDAO userDAO;

  @Autowired
  private ParticipantDAO participantDAO;

  @Autowired
  private EngageProgramDAO engageProgramDAO;

  @Override
  public boolean saveSACelebrationInfo( SACelebrationInfo saInvitationInfo ) throws Exception
  {

    serviceAnniversaryDAO.saveSAInvitationInfo( saInvitationInfo );
    return true;
  }

  @Override
  public SACelebrationInfo getSACelebrationInfo( Long programId, UUID celebrationId, UUID companyId ) throws Exception
  {
    return serviceAnniversaryDAO.getSAInvitationInfo( programId, celebrationId, companyId );
  }

  @Override
  public String getContributePageUrl( String invitationId, String uuid ) throws Exception
  {
    String url = null;

    try
    {
      url = serviceAnniversaryRepo.getRepo().getContributePageUrl( invitationId, uuid );
    }
    catch( ServiceErrorException ex )
    {
      throw ex;
    }

    return url;
  }

  @Override
  public boolean saveSAInviteAndContributeInfo( SAInviteAndContributeInfo saInviteAndContributeInfo ) throws Exception
  {
    serviceAnniversaryDAO.saveSAInviteAndContributeInfo( saInviteAndContributeInfo );
    return true;
  }

  @Override
  public List<SAInviteAndContributeInfo> getSAInviteAndContributeInfoByContributorPersonId( Long contributorPersonId ) throws Exception
  {
    return serviceAnniversaryDAO.getSAInviteAndContributeInfoByContributorPersonId( contributorPersonId );
  }

  @Override
  public SAInviteAndContributeInfo getSAInviteAndContributeInfoByPersonIdAndCelebrationId( Long contributorPersonId, UUID CelebrationId ) throws Exception
  {
    return serviceAnniversaryDAO.getSAInviteAndContributeInfoByPersonIdAndCelebrationId( contributorPersonId, CelebrationId );
  }

  @Override
  public List<AlertsValueBean> getPendingSAContributionsForAlerts( Participant contributor, CmxTranslateHelperBean cmxTranslateHelper ) throws Exception
  {
    List<AlertsValueBean> saContributionList = new ArrayList<AlertsValueBean>();

    List<Long> eligibleProgramIds = serviceAnniversaryDAO.getEligibleSAProgramsForContributor( contributor.getId() );

    if ( Objects.nonNull( eligibleProgramIds ) && !eligibleProgramIds.isEmpty() )
    {
      eligibleProgramIds.stream().forEach( programId ->
      {
        List<SAValueBean> saContributionsList = serviceAnniversaryDAO.getAllPendingSAContributions( contributor.getId(), programId );
        if ( Objects.nonNull( saContributionsList ) && !saContributionsList.isEmpty() )
        {
          AlertsValueBean alertsVB = new AlertsValueBean();
          SAValueBean contributorValue = saContributionsList.get( 0 );
          alertsVB.setProgramName( contributorValue.getProgramName() );
          alertsVB.setProgramId( programId );
          alertsVB.setActivityType( ActivityType.PURL_CONTRIBUTION );
          if ( saContributionsList.size() == 1 )
          {
            alertsVB.setSaCelebrationId( contributorValue.getCelebrationId() );
            alertsVB.setPurlIssuedDate( DateUtils.toDisplayString( contributorValue.getInviteSendDate() ) );
            alertsVB.setPurlExpiryDate( DateUtils.toDisplayString( contributorValue.getCloseDate() ) );
          }
          if ( null != cmxTranslateHelper )
          {
            alertsVB.setProgramName( cmxTranslateHelper.getCmxTranslatedValue( contributorValue.getProgramCmxCode(), contributorValue.getProgramName() ) );
          }
          saContributionList.add( alertsVB );
        }
      } );
    }

    return saContributionList;
  }

  @Override
  public List<SAValueBean> getAllPendingSAContributions( Long contributorPersonId, Long programId ) throws Exception
  {
    List<SAValueBean> saContributionsList = serviceAnniversaryDAO.getAllPendingSAContributions( contributorPersonId, programId );

    if ( Objects.nonNull( saContributionsList ) && !saContributionsList.isEmpty() )
    {
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
      saContributionsList.stream().forEach( saContributorValue ->
      {
        saContributorValue.setRecipient( userDAO.getUserByIdWithAssociations( saContributorValue.getRecipientId(), associationRequestCollection ) );
        List<SAValueBean> statusWiseCountList = serviceAnniversaryDAO.getContributionStatusCountByCelebrationId( saContributorValue.getCelebrationId() );
        if ( Objects.nonNull( statusWiseCountList ) && !statusWiseCountList.isEmpty() )
        {
          statusWiseCountList.stream().forEach( s ->
          {
            if ( SAConstants.CONTRIBUTE_STATUS_CONTRIBUTED.equals( s.getContributionStatus() ) )
            {
              saContributorValue.setViewCount( s.getViewCount() );
            }
            else if ( SAConstants.CONTRIBUTE_STATUS_INVITED.equals( s.getContributionStatus() ) )
            {
              saContributorValue.setInviteCount( s.getViewCount() );
            }
          } );
        }
      } );
    }
    return saContributionsList;
  }

  @Override
  public List<AlertsValueBean> getSACelebrationsByRecipient( Long recipientPersonId, int numOfDays, CmxTranslateHelperBean cmxTranslateHelper )
  {
    List<AlertsValueBean> saCelebrationAlertLists = new ArrayList<AlertsValueBean>();

    List<SAValueBean> saCelebrationList = serviceAnniversaryDAO.getSACelebrationsByRecipient( recipientPersonId, numOfDays );

    if ( Objects.nonNull( saCelebrationList ) && !saCelebrationList.isEmpty() )
    {
      saCelebrationList.stream().forEach( saCelebration ->
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setProgramName( saCelebration.getProgramName() );
        alertsVB.setActivityType( ActivityType.CELEBRATION_PAX_ALERT );
        alertsVB.setSaCelebrationId( saCelebration.getCelebrationId() );
        alertsVB.setPurlIssuedDate( DateUtils.toDisplayString( saCelebration.getAwardDate() ) );
        Date purlExpireDate = new Date( saCelebration.getAwardDate().getTime() + numOfDays * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
        alertsVB.setPurlExpiryDate( DateUtils.toDisplayString( purlExpireDate ) );
        if ( null != cmxTranslateHelper )
        {
          alertsVB.setProgramName( cmxTranslateHelper.getCmxTranslatedValue( saCelebration.getProgramCmxCode(), saCelebration.getProgramName() ) );
        }
        saCelebrationAlertLists.add( alertsVB );
      } );
    }
    return saCelebrationAlertLists;
  }

  @Override
  public List<AlertsValueBean> getSAGiftCodeReminderListForAlerts( Long recipientPersonId, CmxTranslateHelperBean cmxTranslateHelper )
  {
    List<AlertsValueBean> saGiftCodeAlertLists = new ArrayList<AlertsValueBean>();

    List<SAValueBean> saGiftCodeList = serviceAnniversaryDAO.getSAGiftCodeReminderListForAlerts( recipientPersonId );

    if ( Objects.nonNull( saGiftCodeList ) && !saGiftCodeList.isEmpty() )
    {
      saGiftCodeList.stream().forEach( saCelebration ->
      {
        AlertsValueBean alertsVB = new AlertsValueBean();
        alertsVB.setProgramName( saCelebration.getProgramName() );
        alertsVB.setActivityType( ActivityType.AWARD_REMINDER );
        alertsVB.setSaCelebrationId( saCelebration.getCelebrationId() );
        if ( null != cmxTranslateHelper )
        {
          alertsVB.setProgramName( cmxTranslateHelper.getCmxTranslatedValue( saCelebration.getProgramCmxCode(), saCelebration.getProgramName() ) );
        }
        saGiftCodeAlertLists.add( alertsVB );
      } );
    }
    return saGiftCodeAlertLists;
  }

  @Override
  public String getGiftCodePageUrl( String celebrationId ) throws Exception
  {
    String url = null;

    try
    {
      url = serviceAnniversaryRepo.getRepo().getGiftCodePageUrl( celebrationId );
    }
    catch( ServiceErrorException ex )
    {
      throw ex;
    }

    return url;
  }

  @Override
  public String getCelebrationId( Long purlContributorId ) throws DataException
  {

    return serviceAnniversaryDAO.getCelebrationId( purlContributorId );
  }

  @Override
  public String getCelebrationIdByClaim( Long claimId, Long recipientId, int numOfDays ) throws DataException
  {
    return serviceAnniversaryDAO.getCelebrationIdByClaim( claimId, recipientId, numOfDays );
  }

  @Override
  public List<PurlCelebration> getUpcomingCelebrationRecipients( int pageSize, String sortedBy, String sortedOn, int startIndex )
  {
    List<SACelebrationInfo> saCelebratioInfoList = new ArrayList<SACelebrationInfo>();
    List<PurlCelebration> celebrationList = new ArrayList<PurlCelebration>();

    int rowNumStart = ( startIndex - 1 ) * pageSize;
    int rowNumEnd = rowNumStart + pageSize;

    saCelebratioInfoList = serviceAnniversaryDAO.getUpcomingCelebrationRecipients( rowNumStart, rowNumEnd, sortedBy, sortedOn, pageSize );

    for ( SACelebrationInfo saCelebrationInfo : saCelebratioInfoList )
    {
      Participant participantBean = participantDAO.getParticipantById( saCelebrationInfo.getRecipientId() );
      EngageProgram engageProgram = engageProgramDAO.fetchEngageProgramById( saCelebrationInfo.getProgramId() );

      PurlCelebration purlCelebration = new PurlCelebration();
      ParticipantInfoView participant = new ParticipantInfoView();

      if ( null != participantBean )
      {
        participant.setId( saCelebrationInfo.getRecipientId() );
        participant.setFirstName( participantBean.getFirstName() );
        participant.setLastName( participantBean.getLastName() );
        participant.setAvatarUrl( participantBean.getAvatarSmall() );
      }
      purlCelebration.setParticipant( participant );
      purlCelebration.setMilestone( engageProgram == null ? "" : engageProgram.getProgramName() );
      purlCelebration.setId( saCelebrationInfo.getId() );
      purlCelebration.setContributeBy( DateUtils.toDisplayString( saCelebrationInfo.getAwardDate() ) );
      purlCelebration.setCelebrationId( saCelebrationInfo.getCelebrationId() != null ? saCelebrationInfo.getCelebrationId().toString() : null );

      celebrationList.add( purlCelebration );

    }

    return celebrationList;

  }

  @Override
  public int getUpcomingCelebrationCount()
  {
    return serviceAnniversaryDAO.getUpcomingCelebrationCount();
  }

  @Override
  public String getRecipientName( String celebrationId )
  {
    return serviceAnniversaryDAO.getRecipientName( celebrationId );
  }

}
