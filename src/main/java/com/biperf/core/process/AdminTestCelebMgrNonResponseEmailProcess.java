
package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.process.AdminTestProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.value.AdminTestProcessParmBean;

public class AdminTestCelebMgrNonResponseEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestCelebMgrNonResponseEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Celebration Manager Non Response Email Process";
  public static final String BEAN_NAME = "adminTestCelebMgrNonResponseEmailProcess";

  private AdminTestProcessService adminTestProcessService;

  private String recipientUserName;
  private String managerUserName;
  private Long promotionId;
  private String recipientLocale;
  private String awardDate;
  private String msgCollectExpireDate;
  private String customFormField1;
  private String customFormField2;
  private String customFormField3;
  private String anniversaryNumberOfYears;
  private String anniversaryNumberOfDays;

  public AdminTestCelebMgrNonResponseEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestCelebMgrNonResponseEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestCelebMgrNonResponseEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
    }
    else
    {
      log.debug( "Starting Admin Test Celebration Manager Non Response Email Process with Manager User Name: " + managerUserName );
      try
      {
        Participant manager = participantService.getParticipantByUserName( managerUserName );
        if ( manager != null )
        {
          Message celebrationManagerMessage = messageService.getMessageByCMAssetCode( MessageService.CELEBRATION_MANAGER_NONRESPONSE_MESSAGE_CM_ASSET_CODE );

          AdminTestProcessParmBean parmBean = new AdminTestProcessParmBean();
          parmBean.setProcessInvocationId( getProcessInvocationId() );
          parmBean.setRecipientUserName( recipientUserName );
          parmBean.setManagerUserName( managerUserName );
          parmBean.setPromotionId( promotionId );
          parmBean.setRecipientLocale( recipientLocale );
          parmBean.setAwardDate( awardDate );
          parmBean.setMsgCollectExpireDate( msgCollectExpireDate );
          parmBean.setCustomFormField1( customFormField1 );
          parmBean.setCustomFormField2( customFormField2 );
          parmBean.setCustomFormField3( customFormField3 );
          parmBean.setAnniversaryNumberOfYears( anniversaryNumberOfYears );
          parmBean.setAnniversaryNumberOfDays( anniversaryNumberOfDays );

          /* normally sent by ProactiveEmailProcess */
          MailingRecipient mailingRecipient = adminTestProcessService.buildCelebrationManagerMailingRecipient( manager, parmBean );
          Mailing mailing = new Mailing();
          mailing.setGuid( GuidUtils.generateGuid() );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setMessage( celebrationManagerMessage );
          mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
          mailing.setSender( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
          mailing.addMailingRecipient( mailingRecipient );
          this.mailingService.submitMailing( mailing, null );

          addComment( "Email sent to user name " + managerUserName );
        }
        else
        {
          addComment( "Manager user name " + managerUserName + " not available in the system." );
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing with manager user name: " + managerUserName );
      }

    }

  }

  public void setAdminTestProcessService( AdminTestProcessService adminTestProcessService )
  {
    this.adminTestProcessService = adminTestProcessService;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
  }

  public String getManagerUserName()
  {
    return managerUserName;
  }

  public void setManagerUserName( String managerUserName )
  {
    this.managerUserName = managerUserName;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

  public String getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( String awardDate )
  {
    this.awardDate = awardDate;
  }

  public String getMsgCollectExpireDate()
  {
    return msgCollectExpireDate;
  }

  public void setMsgCollectExpireDate( String msgCollectExpireDate )
  {
    this.msgCollectExpireDate = msgCollectExpireDate;
  }

  public String getCustomFormField1()
  {
    return customFormField1;
  }

  public void setCustomFormField1( String customFormField1 )
  {
    this.customFormField1 = customFormField1;
  }

  public String getCustomFormField2()
  {
    return customFormField2;
  }

  public void setCustomFormField2( String customFormField2 )
  {
    this.customFormField2 = customFormField2;
  }

  public String getCustomFormField3()
  {
    return customFormField3;
  }

  public void setCustomFormField3( String customFormField3 )
  {
    this.customFormField3 = customFormField3;
  }

  public String getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( String anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public String getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( String anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

}
