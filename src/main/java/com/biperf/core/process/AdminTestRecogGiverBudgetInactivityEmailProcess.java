
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.QuizPromotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.promotion.SurveyPromotion;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.objectpartners.cms.util.CmsUtil;

public class AdminTestRecogGiverBudgetInactivityEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestRecogGiverBudgetInactivityEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Recognition Giver Budget Inactivity Email Process";
  public static final String BEAN_NAME = "adminTestRecogGiverBudgetInactivityEmailProcess";

  private PromotionService promotionService;
  private CMAssetService cmAssetService;

  private String recipientUserName;
  private Long promotionId;
  private String recipientLocale;

  public AdminTestRecogGiverBudgetInactivityEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    log.debug( "Starting Admin Test Recognition Giver Budget Inactivity Email Process with User Name: " + recipientUserName );
    try
    {
      Participant participant = participantService.getParticipantByUserName( recipientUserName );
      if ( participant != null )
      {
        /* normally sent by proactiveEmailProcess */
        Promotion promo = promotionService.getPromotionById( promotionId );

        MailingRecipient mr = new MailingRecipient();
        mr.setGuid( GuidUtils.generateGuid() );
        mr.setUser( participant );
        mr.setLocale( recipientLocale );

        // Add the personalization data to the objectMap
        Map objectMap = new HashMap();
        objectMap.put( "url", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );

        // Compose the mailing
        Mailing mailing = composeMail( MessageService.RECOGNITION_GIVER_BUDGET_INACTIVITY_NOTIFICATION_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );

        // Set the recipients on the mailing
        objectMap.put( "firstName", mr.getUser().getFirstName() );

        if ( promo.getSubmissionEndDate() != null )
        {
          objectMap.put( "showDeadline", String.valueOf( Boolean.TRUE ) );
          if ( promo instanceof RecognitionPromotion )
          {
            objectMap.put( "deadline", com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) + "." );
          }
          else if ( promo instanceof ProductClaimPromotion )
          {
            objectMap.put( "deadline", com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) + "." );
          }
          else if ( promo instanceof QuizPromotion )
          {
            objectMap.put( "deadline", com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) + "." );
          }
          else if ( promo instanceof SurveyPromotion )
          {
            objectMap.put( "deadline", com.biperf.core.utils.DateUtils.toDisplayString( promo.getSubmissionEndDate(), LocaleUtils.getLocale( mr.getLocale() ) ) + "." );
          }
        }

        mr.addMailingRecipientDataFromMap( objectMap );
        mailing.addMailingRecipient( mr );

        if ( promo.getPromoNameAssetCode() != null )
        {
          MailingRecipientData promotionName = new MailingRecipientData();
          promotionName.setKey( "promotionName" );
          promotionName.setValue( getPromotionName( promo.getPromoNameAssetCode(), mr.getLocale() ) );
          mr.addMailingRecipientData( promotionName );
        }

        mailing.addMailingRecipient( mr );
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

  private String getPromotionName( String promoNameAssetCode, String userLocale )
  {
    Locale locale = CmsUtil.getLocale( userLocale );
    return cmAssetService.getString( promoNameAssetCode, Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
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

}
