/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/Attic/AdminTestCelebrationPurlRecipientEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
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
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * AdminTestCelebrationPurlRecipientEmailProcess is the process to generate the celebration email with recipient user id for recog promo with celebration and purl included.
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
 * <td>July 6, 2015</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class AdminTestCelebrationPurlRecipientEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestCelebrationPurlRecipientEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Celebration Purl Recipient Email Process";
  public static final String BEAN_NAME = "adminTestCelebrationPurlRecipientEmailProcess";

  private String purlRecipientUserName;
  private Long promotionId;
  private String awardDate;
  private String comments;
  private String recipientLocale;

  private PromotionService promotionService;
  private CMAssetService cmAssetService;
  private PurlService purlService;

  public AdminTestCelebrationPurlRecipientEmailProcess()
  {
    super();
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'AdminTestCelebrationPurlRecipientEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'AdminTestCelebrationPurlRecipientEmailProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      log.debug( "Starting Admin Test Celebration Purl Recipient Email Process with User Name: " + purlRecipientUserName );
      try
      {
        AssociationRequestCollection associationRequests = new AssociationRequestCollection();
        associationRequests.add( new UserAssociationRequest( UserAssociationRequest.NODE ) );
        Participant participant = participantService.getParticipantByUserNameWithAssociations( purlRecipientUserName, associationRequests );
        if ( participant != null )
        {
          Set recipients = new HashSet();

          User runByUser = userService.getUserById( UserManager.getUserId() );
          Promotion promotion = promotionService.getPromotionById( promotionId );
          Message message = messageService.getMessageByCMAssetCode( MessageService.CELEBRATION_PURL_RECIPIENT_INVITATION_MESSAGE_CM_ASSET_CODE );
          Mailing mailing = new Mailing();

          mailing.setGuid( GuidUtils.generateGuid() );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setMessage( message );
          mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
          mailing.setSender( getSystemIncentiveEmailAddress() );

          MailingRecipient mailingRecipient = new MailingRecipient();
          mailingRecipient.setGuid( GuidUtils.generateGuid() );
          mailingRecipient.setUser( participant );
          mailingRecipient.setLocale( recipientLocale );
          RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;

          Locale locale = CmsUtil.getLocale( mailingRecipient.getLocale() );
          ContentReader contentReader = ContentReaderManager.getContentReader();
          Content content = (Content)contentReader.getContent( "recognition.detail", locale );

          String subjectLine = "Test";
          Map dataMap = new HashMap();
          dataMap.put( "subject", subjectLine );
          populateCelebrationMessageMap( dataMap, participant, (RecognitionPromotion)promotion, new Date() );
          dataMap.put( "firstName", participant.getFirstName() );
          dataMap.put( "lastName", participant.getLastName() );
          if ( promotion.getPromoNameAssetCode() != null )
          {
            String promotionName = cmAssetService.getString( promotion.getPromoNameAssetCode(), Promotion.PROMOTION_NAME_KEY_PREFIX, locale, true );
            dataMap.put( "programName", StringEscapeUtils.unescapeHtml4( promotionName ) );
          }
          dataMap.put( "sender", runByUser.getFirstName() + " " + runByUser.getLastName() );
          dataMap.put( "companyName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
          dataMap.put( "message", comments );

          dataMap.put( "manyAwardAmount", "TRUE" );
          List contentListAwards = (List)contentReader.getContent( "picklist.promotion.awardstype.items", locale );

          for ( Iterator iter = contentListAwards.iterator(); iter.hasNext(); )
          {
            content = (Content)iter.next();
            String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
            String status = (String)content.getContentDataMap().get( PickListItem.ITEM_STATUS_KEY );
            if ( recognitionPromotion.getAwardType().getCode().equalsIgnoreCase( code ) && status.equals( "true" ) )
            {
              String mediaType = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
              dataMap.put( "mediaType", StringEscapeUtils.unescapeHtml4( mediaType ) );
              break;
            }
          }
          dataMap.put( "isClaimRecipient", "TRUE" );
          if ( recognitionPromotion.getAwardType().isPointsAwardType() || recognitionPromotion.getAwardType().isTravelAwardType() )
          {
            dataMap.put( "showAcctLink", "TRUE" );
            dataMap.put( "siteLink",
                         systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/participantProfilePage.do?fromEmail=true#tab/Statement" );
          }
          if ( recognitionPromotion.getAwardType().isMerchandiseAwardType() )
          {
            dataMap.put( "showLevelLink", "TRUE" );
          }
          dataMap.put( "claimNumber", RandomStringUtils.randomNumeric( 10 ) );
          mailingRecipient.addMailingRecipientDataFromMap( dataMap );
          recipients.add( mailingRecipient );
          mailing.addMailingRecipients( recipients );
          mailingService.submitMailing( mailing, null );
        }
        else
        {
          addComment( "User name " + purlRecipientUserName + " not available in the system to launch Admin Test Celebration Purl Recipient Email Process." );
        }
      }
      catch( Exception e )
      {
        logErrorMessage( e );
        addComment( "An exception occurred while processing Admin Test Celebration Purl Recipient Email Process with Username: " + purlRecipientUserName );
      }

    }

  }

  @SuppressWarnings( { "unchecked", "unused" } )
  private void populateCelebrationMessageMap( Map dataMap, Participant participant, RecognitionPromotion recognitionPromotion, Date awardDate )
  {

    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( participant.getLanguageType() != null )
    {
      localeCode = participant.getLanguageType().getCode();
    }

    int numOfDays = systemVariableService.getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    Date purlExpireDate = new Date( awardDate.getTime() + numOfDays * DateUtils.MILLIS_PER_DAY );
    dataMap.put( "purlExpireDate", com.biperf.core.utils.DateUtils.toDisplayString( purlExpireDate, LocaleUtils.getLocale( localeCode ) ) );

    String purlUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
    StringBuffer purlRecipientLink = new StringBuffer( purlUrl );
    purlRecipientLink.append( '/' );
    purlRecipientLink.append( participant.getFirstName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    purlRecipientLink.append( '.' );
    purlRecipientLink.append( participant.getLastName().replace( PurlService.PURL_RECIPIENT_URL_NAME, PurlService.PURL_RECIPIENT_URL_DELIMITER ) );
    purlRecipientLink.append( '.' );
    purlRecipientLink.append( participant.getId() );
    purlRecipientLink.append( "?viewingId=" + participant.getId() );

    dataMap.put( "purlRecipientLink", purlRecipientLink.toString() );
    dataMap.put( "shortLink", "SHORT LINK" );
    dataMap.put( "recipientFirstName", participant.getFirstName() );
    boolean serviceAnniversaryEnabled = false;
    if ( recognitionPromotion.isIncludeCelebrations() && recognitionPromotion.isServiceAnniversary() )
    {
      serviceAnniversaryEnabled = true;
    }
    if ( serviceAnniversaryEnabled )
    {
      dataMap.put( "serviceAnniversary", "TRUE" );
    }
    if ( !serviceAnniversaryEnabled )
    {
      dataMap.put( "noServiceAnniversary", "TRUE" );
    }
    dataMap.put( "celebrationLink", "FIXME" );
    if ( recognitionPromotion.isAwardActive() )
    {
      dataMap.put( "award", "TRUE" );
    }
    Long displayPeriod = recognitionPromotion.getCelebrationDisplayPeriod();
    dataMap.put( "expireDate", "FIXME" );
    // manager data
    Participant manager = purlService.getNodeOwnerForPurlRecipient( participant, participant.getPrimaryUserNode().getNode() );
    dataMap.put( "managerMessage", "TRUE" );
    dataMap.put( "managerFirstName", manager.getFirstName() );
    dataMap.put( "managerLastName", manager.getLastName() );
    dataMap.put( "managerMessageText", comments );
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

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

}
