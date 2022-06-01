
package com.biperf.core.process.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.CokeCareerMomentsService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.client.CokeCommentsLikes;

public class CokeLikesAndCommentsProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "cokeLikesAndCommentsProcess";
  public static final String MESSAGE_NAME = "Coke Likes And Comments Process Notification";

  private static final Log log = LogFactory.getLog( CokeLikesAndCommentsProcess.class );

  String dateToRun;

  private CokeCareerMomentsService cokeCareerMomentsService;

  @Override
  protected void onExecute()
  {
    List<CokeCommentsLikes> likesComments = cokeCareerMomentsService.getCareerMomentsLikesCommentsCount( dateToRun );

    List<Long> userIdFailed = new ArrayList<Long>();
    int countOfNotificationsSent = 0;
    Long userIdProcessing = 0L;
    if ( likesComments != null && likesComments.size() > 0 )
    {
      log.debug( "Number of records from stored proc-" + likesComments.size() );
      log.debug( "LikesComments list as string-" + likesComments.toString() );
      try
      {
        for ( CokeCommentsLikes likeCommentBean : likesComments )
        {
          log.debug( "Processing userId-" + likeCommentBean.getUserId() );
          userIdProcessing = likeCommentBean.getUserId();
          if ( StringUtils.isNotBlank( likeCommentBean.getEmailAddress() ) )
          {
            sendNotification( likeCommentBean );
            countOfNotificationsSent++;
          }
          else
          {
            userIdFailed.add( likeCommentBean.getUserId() );
          }
        }
      }
      catch( Exception e )
      {
        e.printStackTrace();
        addComment( "Process failed. Please look in logs for more details. Error message -" + e.getMessage() );
        addComment( "Failed at userId-" + userIdProcessing );
      }
    }
    addComment( "Total number of recipients found -" + likesComments.size() );
    addComment( countOfNotificationsSent + " recipients successfully received email." );
    if ( userIdFailed.size() > 0 )
    {
      addComment( "UserIds of users didnt receive email -" + userIdFailed.toString() );
    }

    addComment( "Process completed." );
  }

  private void sendNotification( CokeCommentsLikes commentsLikes )
  {
    Mailing mailing = composeMail( MessageService.CUSTOM_LIKES_COMMENTS_NOTIFICATION, MailingType.PROCESS_EMAIL );

    MailingRecipient mailingRecipient = createMailingRecipient( userService.getUserById( commentsLikes.getUserId() ) );
    addMailingRecipientData( mailingRecipient, "firstName", commentsLikes.getFirstName() );
    addMailingRecipientData( mailingRecipient, "lastName", commentsLikes.getLastName() );
    if ( commentsLikes.getLikesCount() != null && commentsLikes.getLikesCount() > 0 )
    {
      addMailingRecipientData( mailingRecipient, "numLikes", commentsLikes.getLikesCount().toString() );
    }
    if ( commentsLikes.getCommentsCount() != null && commentsLikes.getCommentsCount() > 0 )
    {
      addMailingRecipientData( mailingRecipient, "numComments", commentsLikes.getCommentsCount().toString() );
    }

    mailing.addMailingRecipient( mailingRecipient );
    mailing = mailingService.submitMailing( mailing, null, UserManager.getUserId() );
  }

  private void addMailingRecipientData( MailingRecipient mailingRecipient, String key, String value )
  {
    MailingRecipientData mrd = new MailingRecipientData();
    mrd.setKey( key );
    mrd.setValue( value );
    mailingRecipient.addMailingRecipientData( mrd );
  }

  private MailingRecipient createMailingRecipient( User user )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );

    if ( user.getLanguageType() != null )
    {
      mailingRecipient.setLocale( user.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    mailingRecipient.setUser( user );

    return mailingRecipient;
  }

  public void setCokeCareerMomentsService( CokeCareerMomentsService cokeCareerMomentsService )
  {
    this.cokeCareerMomentsService = cokeCareerMomentsService;
  }

  public String getDateToRun()
  {
    return dateToRun;
  }

  public void setDateToRun( String dateToRun )
  {
    this.dateToRun = dateToRun;
  }

}
