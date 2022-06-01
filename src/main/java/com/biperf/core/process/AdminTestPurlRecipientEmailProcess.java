/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/Attic/AdminTestPurlRecipientEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * AdminTestPurlRecipientEmailProcess is the process to generate the purl award email to recipient
 * This process created for "EmailTestChangeRequestMay042015.doc"
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
 * <td>Venkatesh Dudam</td>
 * <td>July 7, 2015</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class AdminTestPurlRecipientEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestPurlRecipientEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Purl Recipient Email Process";
  public static final String BEAN_NAME = "adminTestPurlRecipientEmailProcess";

  private PromotionService promotionService;
  private CMAssetService cmAssetService;

  private String purlRecipientUserName;
  private Long promotionId;
  private String awardDate;
  private String award;
  private String customFormField1;
  private String customFormField2;
  private String customFormField3;
  private String recipientLocale;

  public AdminTestPurlRecipientEmailProcess()
  {
    super();
  }

  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestPurlRecipientEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestPurlRecipientEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Starting Admin Test Purl Recipient Email Process with User Name: " + purlRecipientUserName );
      try
      {
        Participant recipient = participantService.getParticipantByUserName( purlRecipientUserName );
        if ( recipient != null )
        {
          Mailing mailing = new Mailing();
          Message message = messageService.getMessageByCMAssetCode( MessageService.PURL_RECIPIENT_INVITATION_MESSAGE_CM_ASSET_CODE );
          mailing.setGuid( GuidUtils.generateGuid() );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setMessage( message );
          mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
          mailing.setSender( getSystemIncentiveEmailAddress() );
          MailingRecipient mailingRecipient = buildMailingRecipientForPurlRecipientInvitation( recipient );
          mailing.addMailingRecipient( mailingRecipient );
          mailingService.submitMailing( mailing, null );
        }
        else
        {
          addComment( "User name " + purlRecipientUserName + " not available in the system to launch Admin Test Purl Recipient Email Process." );
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing Admin Test Purl Recipient Email Process with Username: " + purlRecipientUserName );
      }

    }

  }

  public MailingRecipient buildMailingRecipientForPurlRecipientInvitation( Participant recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( recipient );
    mailingRecipient.setLocale( recipientLocale );

    Promotion promotion = promotionService.getPromotionById( promotionId );
    Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );

    Map dataMap = new HashMap();
    dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    if ( promotion.getPromoNameAssetCode() != null )
    {
      String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
      dataMap.put( "promotionName", StringEscapeUtils.unescapeHtml4( promotionName ) );
    }
    if ( promotion.getAwardType().isPointsAwardType() )
    {
      dataMap.put( "pointsType", "TRUE" );
    }
    if ( promotion.getAwardType().isMerchandiseAwardType() )
    {
      dataMap.put( "merchandiseType", "TRUE" );
    }
    dataMap.put( "recipientFirstName", recipient.getFirstName() );
    int numOfDays = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    Date awardDateDate = com.biperf.core.utils.DateUtils.toDate( awardDate );
    Date purlExpireDate = new Date( awardDateDate.getTime() + numOfDays * DateUtils.MILLIS_PER_DAY );
    dataMap.put( "purlExpireDate", com.biperf.core.utils.DateUtils.toDisplayString( purlExpireDate, locale ) );

    if ( promotion.getAwardType().isMerchandiseAwardType() )
    {
      dataMap.put( "awardAmount", award );
    }
    else
    {
      try
      {
        long awardLong = Long.parseLong( award );
        if ( awardLong > 1 )
        {
          dataMap.put( "manyAwardAmount", "TRUE" );
        }
        dataMap.put( "awardAmount", NumberFormatUtil.getLocaleBasedNumberFormat( awardLong ) );
      }
      catch( NumberFormatException nfe )
      {
        log.error( "Award can't be string for award type points promotion. Ignoring this error for test email" );
        dataMap.put( "awardAmount", "Invalid Award Input" );
      }
    }

    ContentReader contentReader = ContentReaderManager.getContentReader();
    List contentList = (List)contentReader.getContent( "picklist.promotion.awardstype.items", locale );
    for ( Iterator iter = contentList.iterator(); iter.hasNext(); )
    {
      Content content = (Content)iter.next();
      String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
      String status = (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY );
      if ( promotion.getAwardType().getCode().equalsIgnoreCase( code ) && status.equals( "true" ) )
      {
        String mediaType = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
        dataMap.put( "mediaType", StringEscapeUtils.unescapeHtml4( mediaType ) );
        break;
      }
    }

    String purlUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
    StringBuffer purlRecipientLink = new StringBuffer( purlUrl );
    purlRecipientLink.append( '/' );
    purlRecipientLink.append( recipient.getFirstName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    purlRecipientLink.append( '.' );
    purlRecipientLink.append( recipient.getLastName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    purlRecipientLink.append( '.' );
    purlRecipientLink.append( recipient.getId() );
    purlRecipientLink.append( "?viewingId=" + recipient.getId() );
    dataMap.put( "purlRecipientLink", purlRecipientLink.toString() );
    dataMap.put( "shortLink", "NOT AVAILABLE" );

    int i = 1;
    if ( !StringUtil.isNullOrEmpty( customFormField1 ) )
    {
      dataMap.put( "formElement" + i, customFormField1 );
      i++;
    }
    if ( !StringUtil.isNullOrEmpty( customFormField2 ) )
    {
      dataMap.put( "formElement" + i, customFormField2 );
      i++;
    }
    if ( !StringUtil.isNullOrEmpty( customFormField3 ) )
    {
      dataMap.put( "formElement" + i, customFormField3 );
    }

    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    return mailingRecipient;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public String getPurlRecipientUserName()
  {
    return purlRecipientUserName;
  }

  public void setPurlRecipientUserName( String purlRecipientUserName )
  {
    this.purlRecipientUserName = purlRecipientUserName;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getAwardDate()
  {
    return awardDate;
  }

  public void setAwardDate( String awardDate )
  {
    this.awardDate = awardDate;
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
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

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

}
