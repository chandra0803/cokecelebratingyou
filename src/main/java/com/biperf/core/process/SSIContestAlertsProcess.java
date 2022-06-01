
package com.biperf.core.process;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.time.DateUtils;

import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.managertoolkit.AlertMessage;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestManager;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.managertoolkit.ParticipantAlertService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.value.ssi.SSIContestListValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class SSIContestAlertsProcess extends BaseProcessImpl
{
  public static final String PROCESS_NAME = "SSI Contest Alert Process";
  public static final String BEAN_NAME = "ssiContestAlertProcess";

  private ParticipantService participantService;
  private SSIContestService ssiContestService;
  private ParticipantAlertService participantAlertService;
  private CMAssetService cmAssetService;
  private SSIPromotionService ssiPromotionService;

  protected void onExecute()
  {
    List<SSIContestListValueBean> liveContests = this.ssiContestService.getContestsWithTodayTileStartDate();
    if ( liveContests != null && liveContests.size() > 0 )
    {
      AssociationRequestCollection promotionAssociationRequest = new AssociationRequestCollection();
      promotionAssociationRequest.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
      Promotion promotion = ssiPromotionService.getLiveSSIPromotionWithAssociations( promotionAssociationRequest );

      AssociationRequestCollection ssiContestAssociationRequest = new AssociationRequestCollection();
      ssiContestAssociationRequest.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_MANAGERS ) );
      ssiContestAssociationRequest.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_PARTICIPANTS ) );
      for ( SSIContestListValueBean valueBean : liveContests )
      {
        // Send alerts to participants, managers & creators regarding contest launch alert
        SSIContest ssiContest = this.ssiContestService.getContestByIdWithAssociations( valueBean.getContestId(), ssiContestAssociationRequest );
        String contestName = this.cmAssetService.getString( ssiContest.getCmAssetCode(), SSIContest.CONTEST_CMASSET_NAME, Locale.US, true );

        // Contest Launch alert
        if ( ssiContest.getDisplayStartDate() != null && DateUtils.isSameDay( ssiContest.getDisplayStartDate(), new Date() ) )
        {
          String subject = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_LAUNCH_SUBJECT" );
          String message = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_LAUNCH_MESG" ), new Object[] { contestName } );
          sendAlert( ssiContest, subject, message );
        }
        /*
         * // Contest Progress Update alert TODO add update progress condition for sending alerts
         * else if ( ssiContest.isIncludeStackRank() ) { String subject =
         * CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.PROGRESS_UPDATE_SUBJECT"
         * ); String message = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString(
         * "ssi_contest.alerts.PROGRESS_UPDATE_MESG" ), new Object[] { contestName } ); sendAlert(
         * ssiContest, subject, message ); } // Contest Final Results alert TODO add final results
         * condition for sending alerts else if ( true ) { String subject =
         * CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_RESULT_SUBJECT"
         * ); String message = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString(
         * "ssi_contest.alerts.CONTEST_RESULT_MESG" ), new Object[] { contestName } ); sendAlert(
         * ssiContest, subject, message ); }
         */
        // Contest End Alert
        sendContestEndAlert( ssiContest, contestName, promotion );

      }
      addComment( "Process:'" + PROCESS_NAME + "' completed successfully" );
    }
  }

  private void sendAlert( SSIContest ssiContest, String subject, String message )
  {
    AlertMessage alertMessage = new AlertMessage();
    alertMessage.setSubject( subject );
    alertMessage.setExpiryDate( DateUtils.addDays( new Date(), 1 ) );
    alertMessage.setMessage( message );

    // sending alert to participant
    for ( SSIContestParticipant ssiContestParticipant : ssiContest.getContestParticipants() )
    {
      alertMessage.setMessageTo( String.valueOf( ssiContestParticipant.getParticipant().getId() ) );
      ParticipantAlert participantAlert = new ParticipantAlert();
      participantAlert.setUser( ssiContestParticipant.getParticipant() );
      participantAlert.setAlertMessage( alertMessage );
      this.participantAlertService.saveParticipantAlert( participantAlert );
    }

    // sending alert to manager
    for ( SSIContestManager ssiContestManager : ssiContest.getContestManagers() )
    {
      alertMessage.setMessageTo( String.valueOf( ssiContestManager.getManager().getId() ) );
      ParticipantAlert participantAlert = new ParticipantAlert();
      participantAlert.setUser( ssiContestManager.getManager() );
      participantAlert.setAlertMessage( alertMessage );
      this.participantAlertService.saveParticipantAlert( participantAlert );
    }

    // sending alert to creator(contest)
    Participant creator = this.participantService.getParticipantById( ssiContest.getCreatorId() );
    alertMessage.setMessageTo( String.valueOf( creator.getId() ) );
    ParticipantAlert participantAlert = new ParticipantAlert();
    participantAlert.setUser( creator );
    participantAlert.setAlertMessage( alertMessage );
    this.participantAlertService.saveParticipantAlert( participantAlert );
  }

  private void sendContestEndAlert( SSIContest ssiContest, String contestName, Promotion promotion )
  {
    if ( promotion.getPromotionNotifications() != null && promotion.getPromotionNotifications().size() > 0 )
    {
      String message = null;
      String subject = CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_END_SUBJECT" );
      Iterator notificationIterator = promotion.getPromotionNotifications().iterator();
      while ( notificationIterator.hasNext() )
      {
        PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notificationIterator.next();
        String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
        if ( notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
            || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT ) )
        {
          if ( promotionNotificationType.getNotificationMessageId() > 0 )
          {
            // send alert n days before contest end date
            if ( org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.addDays( new Date(), promotionNotificationType.getNumberOfDays() ), ssiContest.getEndDate() ) )
            {
              message = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_END_MESG" ),
                                              new Object[] { promotionNotificationType.getNumberOfDays(), contestName } );
              sendAlert( subject, message, notificationTypeCode, ssiContest );
            }
          }
          // send alert 2 days before contest end date
          else if ( org.apache.commons.lang3.time.DateUtils.isSameDay( DateUtils.addDays( new Date(), 2 ), ssiContest.getEndDate() ) )
          {
            message = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "ssi_contest.alerts.CONTEST_END_MESG" ), new Object[] { 2, contestName } );
            sendAlert( subject, message, notificationTypeCode, ssiContest );
          }
        }
      }
    }
  }

  private void sendAlert( String subject, String message, String notificationTypeCode, SSIContest ssiContest )
  {
    if ( notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_DO_THIS_GET_THAT )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_OBJECTIVES )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STACK_RANK )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_CREATOR_STEP_IT_UP ) )
    {
      AlertMessage alertMessage = new AlertMessage();
      alertMessage.setSubject( subject );
      alertMessage.setExpiryDate( DateUtils.addDays( new Date(), 1 ) );
      alertMessage.setMessage( message );

      // sending alert to creator(contest)
      Participant creator = this.participantService.getParticipantById( ssiContest.getCreatorId() );
      alertMessage.setMessageTo( String.valueOf( creator.getId() ) );
      ParticipantAlert participantAlert = new ParticipantAlert();
      participantAlert.setUser( creator );
      participantAlert.setAlertMessage( alertMessage );
      this.participantAlertService.saveParticipantAlert( participantAlert );
    }
    else if ( notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_DO_THIS_GET_THAT )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_OBJECTIVES )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STACK_RANK )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_MGR_STEP_IT_UP ) )
    {
      AlertMessage alertMessage = new AlertMessage();
      alertMessage.setSubject( subject );
      alertMessage.setExpiryDate( DateUtils.addDays( new Date(), 1 ) );
      alertMessage.setMessage( message );
      // sending alert to manager
      for ( SSIContestManager ssiContestManager : ssiContest.getContestManagers() )
      {
        alertMessage.setMessageTo( String.valueOf( ssiContestManager.getManager().getId() ) );
        ParticipantAlert participantAlert = new ParticipantAlert();
        participantAlert.setUser( ssiContestManager.getManager() );
        participantAlert.setAlertMessage( alertMessage );
        this.participantAlertService.saveParticipantAlert( participantAlert );
      }

    }
    else if ( notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STEP_IT_UP )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_STACK_RANK )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_OBJECTIVES )
        || notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_END_NOTIFY_PAX_DO_THIS_GET_THAT ) )
    {
      AlertMessage alertMessage = new AlertMessage();
      alertMessage.setSubject( subject );
      alertMessage.setExpiryDate( DateUtils.addDays( new Date(), 1 ) );
      alertMessage.setMessage( message );
      // sending alert to participant
      for ( SSIContestParticipant ssiContestParticipant : ssiContest.getContestParticipants() )
      {
        alertMessage.setMessageTo( String.valueOf( ssiContestParticipant.getParticipant().getId() ) );
        ParticipantAlert participantAlert = new ParticipantAlert();
        participantAlert.setUser( ssiContestParticipant.getParticipant() );
        this.participantAlertService.saveParticipantAlert( participantAlert );
      }

    }
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setSsiPromotionService( SSIPromotionService ssiPromotionService )
  {
    this.ssiPromotionService = ssiPromotionService;
  }

  public void setParticipantAlertService( ParticipantAlertService participantAlertService )
  {
    this.participantAlertService = participantAlertService;
  }

}
