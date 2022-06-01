
package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.budget.BudgetMaster;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.budget.BudgetMasterToBudgetSegmentsAssociationRequest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

public class AdminTestBudgetReminderNotificationEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestBudgetReminderNotificationEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Budget Reminder Notification Email Process";
  public static final String BEAN_NAME = "adminTestBudgetReminderNotificationEmailProcess";

  private PromotionService promotionService;
  private CMAssetService cmAssetService;
  private BudgetMasterService budgetMasterService;

  private String recipientUserName;
  private Long promotionId;
  private String recipientLocale;
  private String budgetRemaining;
  private Date budgetEndDate;

  public AdminTestBudgetReminderNotificationEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    log.debug( "Starting Admin Test Budget Reminder Notification Email Process with User Name: " + recipientUserName );
    try
    {
      Participant participant = participantService.getParticipantByUserName( recipientUserName );
      if ( participant != null )
      {
        Message budgetReminderMessage = messageService.getMessageByCMAssetCode( MessageService.BUDGET_REMINDER_NOTIFICATION_MESSAGE_CM_ASSET_CODE );

        /* normally sent by RecognitionBudgetReminderProcess */
        RecognitionPromotion promoRecognition = (RecognitionPromotion)promotionService.getPromotionById( promotionId );
        MailingRecipient mailingRecipient = sendBudgetReminderMessage( participant, promoRecognition );
        Mailing mailing = new Mailing();
        mailing.setGuid( GuidUtils.generateGuid() );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setMessage( budgetReminderMessage );
        mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        mailing.setSender( systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal() );
        mailing.addMailingRecipient( mailingRecipient );

        // Add the personalization data to the objectMap
        Map objectMap = new HashMap();
        objectMap.put( "promotionName", promoRecognition.getName() );
        objectMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
        objectMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

        this.mailingService.submitMailing( mailing, objectMap );

        addComment( "Email sent to user name " + recipientUserName );
      }
      else
      {
        addComment( "User name " + recipientUserName + " not available in the system." );
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing with user name: " + recipientUserName );
    }
  }

  public MailingRecipient sendBudgetReminderMessage( Participant participant, RecognitionPromotion promoRecognition )
  {

    Long budgetMasterId = promoRecognition.getBudgetMaster().getId();
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new BudgetMasterToBudgetSegmentsAssociationRequest() );
    BudgetMaster budgetMaster = budgetMasterService.getBudgetMasterById( budgetMasterId, associationRequestCollection );

    MailingRecipient mr = new MailingRecipient();
    mr.setGuid( GuidUtils.generateGuid() );
    mr.setUser( participant );
    mr.setLocale( recipientLocale );

    Map dataMap = new HashMap();
    String siteLink = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    dataMap.put( "siteLink", siteLink );
    dataMap.put( "siteURL", siteLink );
    dataMap.put( "webSite", siteLink );
    dataMap.put( "contactUsUrl", new StringBuffer( siteLink ).append( systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() ).toString() );
    mr.addMailingRecipientDataFromMap( dataMap );

    // Set recipient specific data on the MailingRecipientData
    MailingRecipientData mrd1 = new MailingRecipientData();
    mrd1.setKey( "firstName" );
    mrd1.setValue( mr.getUser().getFirstName() );
    mr.addMailingRecipientData( mrd1 );

    MailingRecipientData mrd2 = new MailingRecipientData();
    mrd2.setKey( "lastName" );
    mrd2.setValue( mr.getUser().getLastName() );
    mr.addMailingRecipientData( mrd2 );

    MailingRecipientData mrd3 = new MailingRecipientData();
    mrd3.setKey( "budgetRemaining" );
    Long budgetRemainingLong = new Long( budgetRemaining );
    String currentValue = NumberFormatUtil.getUserLocaleBasedNumberFormat( budgetRemainingLong.longValue(), LocaleUtils.getLocale( mr.getLocale() ) );
    mrd3.setValue( String.valueOf( currentValue ) );
    mr.addMailingRecipientData( mrd3 );

    MailingRecipientData mrd4 = new MailingRecipientData();
    mrd4.setKey( "budgetName" );
    mrd4.setValue( budgetMaster.getBudgetName() );
    mr.addMailingRecipientData( mrd4 );

    MailingRecipientData mrd5 = new MailingRecipientData();
    if ( budgetEndDate != null )
    {
      mrd5.setKey( "endDate" );
      mrd5.setValue( com.biperf.core.utils.DateUtils.toDisplayString( budgetEndDate, LocaleUtils.getLocale( mr.getLocale() ) ) );
    }

    mr.addMailingRecipientData( mrd5 );

    if ( promoRecognition.getAwardType() != null )
    {
      MailingRecipientData awardMedia = new MailingRecipientData();
      awardMedia.setKey( "awardMedia" );
      ContentReader contentReader = ContentReaderManager.getContentReader();
      List promotionAwardList = new ArrayList();
      if ( contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) ) instanceof java.util.List )
      {
        promotionAwardList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", CmsUtil.getLocale( mr.getLocale() ) );
        for ( Object content : promotionAwardList )
        {
          Content contentData = (Content)content;
          Map m = contentData.getContentDataMapList();
          Translations nameObject = (Translations)m.get( "CODE" );

          if ( nameObject.getValue().equalsIgnoreCase( PromotionAwardsType.POINTS ) )
          {
            Translations valueObject = (Translations)m.get( "NAME" );
            awardMedia.setValue( valueObject.getValue() );
            mr.addMailingRecipientData( awardMedia );
            break;
          }
        }
      }
    }

    return mr;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  public String getRecipientUserName()
  {
    return recipientUserName;
  }

  public void setRecipientUserName( String recipientUserName )
  {
    this.recipientUserName = recipientUserName;
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

  public String getBudgetRemaining()
  {
    return budgetRemaining;
  }

  public void setBudgetRemaining( String budgetRemaining )
  {
    this.budgetRemaining = budgetRemaining;
  }

  public Date getBudgetEndDate()
  {
    return budgetEndDate;
  }

  public void setBudgetEndDate( Date budgetEndDate )
  {
    this.budgetEndDate = budgetEndDate;
  }

}
