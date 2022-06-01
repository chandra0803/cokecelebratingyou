
package com.biperf.core.process;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.activity.ActivityService;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PlateauAwardsReminderSubmitProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PlateauAwardsReminderSubmitProcess.class );

  public static final String BEAN_NAME = "plateauAwardsReminderSubmitProcess";
  public static final String PROCESS_NAME = "Plateau Awards Reminder Submit Process";

  private MerchOrderDAO merchOrderDao;
  private MerchOrderService merchOrderService;
  private ActivityService activityService;
  private PromotionService promotionService;

  // properties from jobDataMap
  private List<Long> merchOrderIds;
  private Long managerId;
  private String comments;

  @Override
  protected void onExecute()
  {
    try
    {
      sendPlateauAwardReminders();
    }
    catch( Throwable t )
    {
      StringBuilder sb = new StringBuilder( "\n\n" );
      sb.append( "\n****************************************************" );
      sb.append( "\n* ERROR in " ).append( this.getClass().getName() ).append( "#onExecute" );
      sb.append( "\n****************************************************" );
      sb.append( "\nmanagerId: " ).append( managerId );
      sb.append( "\ncomments: " ).append( comments );
      if ( merchOrderIds == null )
      {
        sb.append( "\nmerchOrderIds: null" );
      }
      else if ( merchOrderIds.isEmpty() )
      {
        sb.append( "\nmerchOrderIds: empty" );
      }
      else
      {
        sb.append( "\nmerchOrderIds:" );
        for ( Long merchOrderId : merchOrderIds )
        {
          sb.append( " " ).append( merchOrderId );
        }
      }
      sb.append( "\nerror message: " ).append( t.getMessage() );
      sb.append( "\nstack trace: " );
      sb.append( ExceptionUtils.getStackTrace( t ) );
      sb.append( "\n" );
      sb.append( "\n****************************************************" );
      sb.append( "\n* END ERROR" );
      sb.append( "\n****************************************************" );

      log.error( sb.toString() );
    }
  }

  /* default */ void sendPlateauAwardReminders()
  {
    Message message = messageService.getMessageByCMAssetCode( MessageService.PLATEAU_AWARDS_REMINDER_MESSAGE_CM_ASSET_CODE );
    Participant manager = participantService.getParticipantById( managerId );
    String clientName = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal();

    for ( Long merchOrderId : merchOrderIds )
    {
      MerchOrder merchOrder = merchOrderDao.getMerchOrderById( merchOrderId );
      Participant pax = merchOrder.getParticipant();

      // manager cannot send a reminder to himself
      if ( pax.getId().equals( manager.getId() ) )
      {
        continue;
      }

      Mailing reminderMailing = createReminderMailing( message );
      MailingRecipient mailingRecipient = createReminderMailingRecipient( pax );
      reminderMailing.addMailingRecipient( mailingRecipient );

      Map<String, Object> dataMap = new HashMap<String, Object>();
      if ( merchOrder.getClaim() == null && merchOrder.getPromoMerchProgramLevel() == null )
      {
        Long promotionId = activityService.getPromotionbyMerchOrderId( merchOrder.getId() );
        AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
        promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
        GoalQuestPromotion promotion = (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
        Set<AbstractGoalLevel> goalLevels = promotion.getGoalLevels();
        String cmAssetKey = "";
        for ( Iterator<AbstractGoalLevel> iter = goalLevels.iterator(); iter.hasNext(); )
        {
          GoalLevel goalLevel = (GoalLevel)iter.next();
          if ( goalLevel.getAward().equals( new BigDecimal( merchOrder.getPoints() ) ) )
          {
            cmAssetKey = goalLevel.getGoalLevelName();
          }
        }

        dataMap.put( "promotionName", promotion.getName() );
        dataMap.put( "awardLevel", cmAssetKey );
      }
      else
      {
        dataMap.put( "promotionName", merchOrder.getClaim().getPromotion().getName() );
        dataMap.put( "awardLevel", CmsResourceBundle.getCmsBundle().getString( merchOrder.getPromoMerchProgramLevel().getCmAssetKey() + "." + PromoMerchProgramLevel.SPOTLIGHT_LEVEL_NAME_KEY ) );
      }
      dataMap.put( "firstName", pax.getFirstName() );
      dataMap.put( "lastName", pax.getLastName() );
      dataMap.put( "siteUrl", getShoppingUrlFor( merchOrder, pax ) );

      dataMap.put( "clientName", clientName );

      if ( merchOrder.getExpirationDate() != null )
      {
        dataMap.put( "expirationDate", DateUtils.getDateTimeStringIn12HourPattern( merchOrder.getExpirationDate(), getParticipantLocale( pax ) ) );
      }

      if ( comments != null && !comments.equals( "<p>&nbsp;</p>" ) && !comments.equals( "<p></p>" ) )
      {
        dataMap.put( "managerFirstName", manager.getFirstName() );
        dataMap.put( "managerLastName", manager.getLastName() );
        dataMap.put( "managerComments", comments );
      }

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );

      mailingService.submitMailing( reminderMailing, null );
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception
  {
    // The method overridden as the job data map data type is not a simple type of number or string
  }

  /* default */ Locale getParticipantLocale( Participant pax )
  {
    return LocaleUtils.getLocale( pax.getLanguageType() == null ? UserManager.getDefaultLocale().toString() : pax.getLanguageType().getCode() );
  }

  /* default */ Mailing createReminderMailing( Message message )
  {
    Mailing reminderMailing = new Mailing();
    reminderMailing.setGuid( GuidUtils.generateGuid() );
    reminderMailing.setSender( "Test - This is a test statement." );
    reminderMailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
    reminderMailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
    reminderMailing.setMessage( message );

    return reminderMailing;
  }

  /* default */ MailingRecipient createReminderMailingRecipient( Participant pax )
  {
    MailingRecipient mr = new MailingRecipient();
    mr.setUser( pax );
    mr.setLocale( pax.getLanguageType() == null ? systemVariableService.getDefaultLanguage().getStringVal() : pax.getLanguageType().getCode() );
    mr.setGuid( GuidUtils.generateGuid() );

    return mr;
  }

  /* default */ String getShoppingUrlFor( MerchOrder merchOrder, Participant pax )
  {

    String siteUrl = "";
    if ( merchOrder.getClaim() == null && merchOrder.getPromoMerchProgramLevel() == null )
    {
      Long promotionId = activityService.getPromotionbyMerchOrderId( merchOrder.getId() );
      AssociationRequestCollection promoAssociationRequestCollection = new AssociationRequestCollection();
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
      promoAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.CP_LEVELS ) );
      GoalQuestPromotion promotion = (GoalQuestPromotion)promotionService.getPromotionByIdWithAssociations( promotionId, promoAssociationRequestCollection );
      siteUrl = merchOrderService.getOnlineShoppingUrl( promotion, merchOrder.getId(), getPrimaryCountryFor( pax ), pax, false );
    }
    else
    {
      // generate the shopping url
      siteUrl = merchOrderService.getOnlineShoppingUrl( merchOrder.getClaim().getPromotion(), merchOrder.getId(), getPrimaryCountryFor( pax ), pax, false );
    }

    String encodedLink = null;
    try
    {
      encodedLink = URLEncoder.encode( siteUrl, "UTF-8" );
    }
    catch( UnsupportedEncodingException e )
    {
      //
    }

    // shorten it so it can be used in text messaging
    siteUrl = mailingService.getShortUrl( encodedLink );

    return siteUrl;
  }

  private Country getPrimaryCountryFor( Participant pax )
  {
    Country country = null;

    if ( pax.getPrimaryAddress() != null )
    {
      country = pax.getPrimaryAddress().getAddress().getCountry();
    }

    return country;
  }

  public void setMerchOrderIds( List<Long> merchOrderIds )
  {
    this.merchOrderIds = merchOrderIds;
  }

  public void setManagerId( Long managerId )
  {
    this.managerId = managerId;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public void setMerchOrderDAO( MerchOrderDAO merchOrderDao )
  {
    this.merchOrderDao = merchOrderDao;
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  public void setActivityService( ActivityService activityService )
  {
    this.activityService = activityService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

}
