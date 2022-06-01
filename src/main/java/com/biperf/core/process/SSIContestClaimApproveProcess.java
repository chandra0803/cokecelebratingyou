
package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.domain.ssi.SSIPromotion;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.ssi.SSIContestAssociationRequest;
import com.biperf.core.service.ssi.SSIContestPaxClaimService;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.ssi.SSIPromotionService;
import com.biperf.core.utils.SSIContestUtil;

/**
 * 
 * SSIContestClaimApproveProcess.
 * 
 * @author patepp
 * @since May 30 15, 2014
 * @version 1.0
 */

public class SSIContestClaimApproveProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( SSIContestClaimApproveProcess.class );

  public static final String PROCESS_NAME = "SSI contest claim approve Process";
  public static final String BEAN_NAME = "ssiContestClaimApproveProcess";

  private SSIContestPaxClaimService ssiContestPaxClaimService;
  private SSIPromotionService ssiPromotionService;
  private MessageService messageService;
  private ParticipantService participantService;
  private MailingService mailingService;
  private SSIContestService ssiContestService;
  private Long contestId;
  private Long userId;

  protected void onExecute()
  {
    log.debug( "starting SSIContestClaimApproveProcess onExecute....with contestId: " + contestId );
    try
    {
      List<SSIContestPaxClaim> ssiContestPaxClaims = new ArrayList<SSIContestPaxClaim>();
      if ( ssiContestPaxClaims != null )
      {
        // Get the list of
        // approve all waiting for approval claims
        Date approveDenyDate = this.ssiContestPaxClaimService.approveAllPaxClaims( contestId, userId );
        ssiContestPaxClaims = this.ssiContestPaxClaimService.getPaxClaimsByContestIdAndApproveDenyDate( contestId, approveDenyDate );
        // get promotion & check for claim update status notification
        AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
        promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
        SSIPromotion promotion = ssiPromotionService.getLiveSSIPromotionWithAssociations( promotionAssociationRequestCollection );

        long messageId = 0;
        boolean sendNotifications = false;
        if ( promotion.getPromotionNotifications().size() > 0 )
        {
          Iterator notificationsIter = promotion.getPromotionNotifications().iterator();
          while ( notificationsIter.hasNext() )
          {
            PromotionNotification notification = (PromotionNotification)notificationsIter.next();
            if ( notification.isPromotionNotificationType() )
            {
              PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
              messageId = promotionNotificationType.getNotificationMessageId();
              String notificationTypeCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
              if ( messageId > 0 && notificationTypeCode.equals( PromotionEmailNotificationType.CONTEST_CLAIM_ACTION_NOTIFY_SUBMITTER ) )
              {
                sendNotifications = true;
                break;
              }
            }
          }
        }

        // send notifications if enabled
        if ( sendNotifications )
        {
          String approverName = this.participantService.getLNameFNameByPaxId( userId );
          Message message = this.messageService.getMessageById( messageId );

          AssociationRequestCollection contestAssociationRequestCollection = new AssociationRequestCollection();
          contestAssociationRequestCollection.add( new SSIContestAssociationRequest( SSIContestAssociationRequest.SSI_CONTEST_ACTIVITIES ) );
          SSIContest contest = this.ssiContestService.getContestByIdWithAssociations( contestId, contestAssociationRequestCollection );

          boolean populateContestParticipant = contest.getContestType().isObjectives() && !contest.getSameObjectiveDescription();
          SSIContestParticipant contestParticipant = null;

          for ( SSIContestPaxClaim ssiContestPaxClaim : ssiContestPaxClaims )
          {
            Participant submitter = this.participantService.getParticipantById( ssiContestPaxClaim.getSubmitterId() );
            if ( populateContestParticipant )
            {
              contestParticipant = this.ssiContestService.getContestParticipantByContestIdAndPaxId( contest.getId(), submitter.getId() );
            }
            Mailing mailing = this.mailingService
                .buildContestClaimApprovalUpdateStatusNotification( ssiContestPaxClaim,
                                                                    approverName,
                                                                    message,
                                                                    contest,
                                                                    submitter,
                                                                    SSIContestUtil.getActivityDescription( ssiContestPaxClaim, contestParticipant, contest ) );
            if ( mailing != null )
            {
              this.mailingService.submitMailing( mailing, null );
            }
          }
        }
      }
    }
    catch( ServiceErrorException e )
    {
      logErrorMessage( e );
      log.error( " exception in SSIContestClaimApproveProcess onExecute: " + e.getMessage() + ":::" + e );
      addComment( "An exception occurred while approving the contest's claim , contestId: " + contestId + ": executed By " + userId );
    }
  }

  public void setSsiContestPaxClaimService( SSIContestPaxClaimService ssiContestPaxClaimService )
  {
    this.ssiContestPaxClaimService = ssiContestPaxClaimService;
  }

  public void setSsiPromotionService( SSIPromotionService ssiPromotionService )
  {
    this.ssiPromotionService = ssiPromotionService;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setSsiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

}
