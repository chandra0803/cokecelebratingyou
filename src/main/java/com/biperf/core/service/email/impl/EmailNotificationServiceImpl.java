/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/email/impl/EmailNotificationServiceImpl.java,v $
 */

package com.biperf.core.service.email.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStep;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimFormStepEmailNotification;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.ClaimFormStepEmailNotificationType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.LanguageType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.enums.PromotionEmailNotificationType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.promotion.ClaimFormNotificationType;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.domain.promotion.PromotionNotificationType;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.email.EmailNotificationService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.LocaleUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;
import com.biperf.core.domain.enums.NominationEvaluationType;
import com.biperf.core.domain.promotion.NominationPromotion;

/**
 * EmailNotificationServiceImpl
 * 
 *
 */
public class EmailNotificationServiceImpl implements EmailNotificationService
{

  private static final String SYSTEM_NOTIFICATION_EMAIL_NAME = "Incentive System Messenger";

  private static final String DEFAULT_VIDEO_IMAGE = "/assets/skins/default/img/email_videoThumb.jpg";

  private static final Log log = LogFactory.getLog( EmailNotificationServiceImpl.class );

  private MailingService mailingService;
  private MessageService messageService;
  private SystemVariableService systemVariableService;
  private ClaimFormDefinitionService claimFormDefinitionService;
  private ParticipantService participantService;
  @Autowired
  private MTCVideoService mtcVideoService;

  public MTCVideoService getMtcVideoService()
  {
    return mtcVideoService;
  }

  public void setMtcVideoService( MTCVideoService mtcVideoService )
  {
    this.mtcVideoService = mtcVideoService;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  /**
   * Set message service
   * 
   * @param messageService
   */
  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  /**
   * set claim form def service
   * 
   * @param claimFormDefinitionService
   */
  public void setClaimFormDefinitionService( ClaimFormDefinitionService claimFormDefinitionService )
  {
    this.claimFormDefinitionService = claimFormDefinitionService;
  }

  /**
   * Process submitted claim notifications given claimId
   * 
   * @param claim
   * @param claimFormStepId
   */
  public void processSubmittedClaimNotifications( Claim claim, Long claimFormStepId, List unnotifiedApprovers, boolean forApprovers )
  {
    if ( claim.getPromotion().isClaimFormUsed() )
    {
      processClaimFormNotifications( claim, claimFormStepId, true, unnotifiedApprovers, forApprovers );
    }

    else if ( claim.getPromotion().isNominationPromotion() )
    {
      processNominationNotifications( claim, unnotifiedApprovers, forApprovers );
    }
  }

  /**
   * Process closed claim (claims w/ all claim products that have status notifications given claimId
   *  
   * @param claim
   */
  public void processClosedClaimNotifications( Claim claim )
  {
    if ( claim.getPromotion().isClaimFormUsed() )
    {
      processClaimFormNotifications( claim, null, false, null, false );
    }

    if ( claim.getPromotion().isNominationPromotion() )
    {
      processNominationNotifications( claim, null, false );
    }

  }

  private boolean isClaimFormNotificationMatch( PromotionNotification notification, ClaimFormStep claimFormStep )
  {
    return notification.isClaimFormNotificationType() && ( (ClaimFormNotificationType)notification ).getClaimFormStepEmailNotification().getClaimFormStep().equals( claimFormStep );
  }

  public void processNominationApproverNotification( Claim claim, List unnotifiedApprovers )
  {
    // Get the promotion for dynamic loading
    Promotion promotion = claim.getPromotion();

    for ( Iterator iter = promotion.getPromotionNotifications().iterator(); iter.hasNext(); )
    {
      String notificationCode = "";
      Long notificationMessageId = 0L;
      PromotionNotification notification = (PromotionNotification)iter.next();
      if(notification.isClaimFormNotificationType())
      {
        ClaimFormNotificationType notificationType = (ClaimFormNotificationType)notification;
        notificationCode = notificationType.getClaimFormStepEmailNotificationType().getCode();
        notificationMessageId = notificationType.getNotificationMessageId();
      }
      else
      {
        PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;
        notificationCode = promotionNotificationType.getPromotionEmailNotificationType().getCode();
        notificationMessageId = promotionNotificationType.getNotificationMessageId();
      }
      

      Set mailingRecipients = new HashSet();

      if ( notificationCode.equals( PromotionEmailNotificationType.TO_APPROVER_WHEN_CLAIM_SUBMITTED ) )
      {
        // TODO need to pass if the approver is getting mail as defaultApprover or normal approver.
        // Setting as false for testing purpose
        mailingRecipients = getApproversRecipientsList( claim, unnotifiedApprovers, promotion.getName(), false );
      }

      // since this approval start date available to all promotions, it should be done here
      Date approvalStartDate = promotion.getApprovalStartDate();
      Date submissionDate = claim.getSubmissionDate();
      if ( approvalStartDate != null && approvalStartDate.after( submissionDate ) )
      {
        for ( Iterator aIter = mailingRecipients.iterator(); aIter.hasNext(); )
        {
          MailingRecipient mailingRecipient = (MailingRecipient)aIter.next();
          MailingRecipientData mailingRecipientData = new MailingRecipientData();
          mailingRecipientData.setKey( "approvalStartDate" );
          mailingRecipientData.setValue( DateUtils.toDisplayString( approvalStartDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
          mailingRecipient.addMailingRecipientData( mailingRecipientData );
        }
      }

      // Only submit a mailing if recipients were found.
      if ( mailingRecipients.size() > 0 )
      {
        Message message = messageService.getMessageById( notificationMessageId );
        if ( message == null )
        {
          log.debug( "message not found for id:" + notificationMessageId );
          continue;
        }
        // Create mailing object
        Mailing mailing = new Mailing();
        mailing.setMessage( message );
        mailing.addMailingRecipients( mailingRecipients );
        mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        mailing.setSender( SYSTEM_NOTIFICATION_EMAIL_NAME );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setGuid( GuidUtils.generateGuid() );

        mailingService.submitMailing( mailing, null );
      }

    }
  }

  private void processNominationNotifications( Claim claim, List unnotifiedApprovers, boolean forApprovers )
  {
    // Get the promotion for dynamic loading
    Promotion promotion = claim.getPromotion();

    for ( Iterator iter = promotion.getPromotionNotifications().iterator(); iter.hasNext(); )
    {
      PromotionNotification notification = (PromotionNotification)iter.next();
      PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notification;

      Set mailingRecipients = new HashSet();

      /*
       * if ( forApprovers &&
       * promotionNotificationType.getPromotionEmailNotificationType().getCode().equals(
       * PromotionEmailNotificationType.TO_APPROVER_WHEN_CLAIM_SUBMITTED ) ) { //TODO need to pass
       * if the approver is getting mail as defaultApprover or normal approver. Setting as false for
       * testing purpose mailingRecipients = getApproversRecipientsList( claim, unnotifiedApprovers,
       * promotion.getName(), false ); }
       */

      // look at content manager code for email notification

      if ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.TO_NOMINEE_WHEN_CLAIM_SUBMITTED ) )
      {
        mailingRecipients = getNomineeForSubmitList( (NominationClaim)claim, promotion.getName() );
      }

      if ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equals( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_CLAIM_SUBMITTED ) )
      {
        mailingRecipients = getNomineeManagerForSubmitList( (NominationClaim)claim, promotion.getName() );
      }

      // since this approval start date available to all promotions, it should be done here
      Date approvalStartDate = promotion.getApprovalStartDate();
      Date submissionDate = claim.getSubmissionDate();
      if ( approvalStartDate != null && approvalStartDate.after( submissionDate ) )
      {
        for ( Iterator aIter = mailingRecipients.iterator(); aIter.hasNext(); )
        {
          MailingRecipient mailingRecipient = (MailingRecipient)aIter.next();
          MailingRecipientData mailingRecipientData = new MailingRecipientData();
          mailingRecipientData.setKey( "approvalStartDate" );
          mailingRecipientData.setValue( DateUtils.toDisplayString( approvalStartDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
          mailingRecipient.addMailingRecipientData( mailingRecipientData );
        }
      }

      // Only submit a mailing if recipients were found.
      if ( mailingRecipients.size() > 0 )
      {
        Message message = messageService.getMessageById( new Long( promotionNotificationType.getNotificationMessageId() ) );
        if ( message == null )
        {
          log.debug( "message not found for id:" + promotionNotificationType.getNotificationMessageId() );
          continue;
        }
        // Create mailing object
        Mailing mailing = new Mailing();
        mailing.setMessage( message );
        mailing.addMailingRecipients( mailingRecipients );
        mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
        mailing.setSender( SYSTEM_NOTIFICATION_EMAIL_NAME );
        mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
        mailing.setGuid( GuidUtils.generateGuid() );

        mailingService.submitMailing( mailing, null );
      }
    }
  }

  /**
   * Process email notifications given claim, claimFromStepId and type claimFormStepId will be null
   * if this method is being used for closed claim notifications - the id will be pulled from
   * elsewhere
   * 
   * @param claim
   * @param claimFormStepId The claim form step id
   * @param unnotifiedApprovers
   * @param forApprovers 
   */
  private void processClaimFormNotifications( Claim claim, Long claimFormStepId, boolean isSubmitted, List unnotifiedApprovers, boolean forApprovers )
  {
    ClaimFormStep claimFormStep = null;

    // if id not passed in - it is an approval - pull id from MANDATORY claim elements
    // (every claim must have 1 claimformstep - and that step must have at least one
    // element verified by Lori & Arun
    if ( claimFormStepId != null )
    {
      claimFormStep = claimFormDefinitionService.getClaimFormStep( claimFormStepId );
    }
    else if ( claim.getClaimElements().size() > 0 )
    {
      // get claim form step via claimFormStepElement
      // NOT claimElement
      // get the first one (there will always be one - see above)
      ClaimElement claimElement = claim.getClaimElement( 0 );
      claimFormStep = claimElement.getClaimFormStepElement().getClaimFormStep();
    }

    // Get the promotion for dynamic loading
    Promotion promotion = claim.getPromotion();

    // Client customization for WIP 58122 Starts
    boolean levelPayout=false;
    boolean isClaimClosed=false;
    NominationEvaluationType evaluationType=null;
    NominationClaim nc=null;
    if(promotion.isNominationPromotion())
    {
    	NominationPromotion promo=(NominationPromotion)promotion;
    	nc=(NominationClaim)claim;
    	evaluationType = promo.getEvaluationType(); // independent or
    	if(promo.isLevelPayoutByApproverAvailable())
    	{
    		levelPayout=true;
    		if(!claim.isOpen())
    			isClaimClosed=true;
    	}
    }
    // Client customization for WIP 58122 Ends

    for ( Iterator iter = promotion.getPromotionNotifications().iterator(); iter.hasNext(); )
    {
      PromotionNotification notification = (PromotionNotification)iter.next();
      if ( isClaimFormNotificationMatch( notification, claimFormStep ) )
      {
        ClaimFormNotificationType claimFormNotificationType = (ClaimFormNotificationType)notification;
        ClaimFormStepEmailNotification claimFormStepEmailNotification = claimFormNotificationType.getClaimFormStepEmailNotification();

        Set mailingRecipients = new HashSet();

        String typeCode = claimFormStepEmailNotification.getClaimFormStepEmailNotificationType().getCode();

        // look at content manager code for emailnotification
        // Client customization for WIP 58122 Starts
        if(typeCode.equals( ClaimFormStepEmailNotificationType.TO_NOMINEE_WHEN_WINNER ) && levelPayout && isClaimClosed)
        {
        	Set winningNomineeRecipients = new HashSet();
            winningNomineeRecipients = mailingService.buildMailingRecipientsForWinningNominee( nc, evaluationType, null );
        	for(Iterator aIter = winningNomineeRecipients.iterator(); aIter.hasNext();)
            {
              MailingRecipient mr = (MailingRecipient) aIter.next();
              sendCustomNomineeWinnerMessage( mr);
            }
        }
        // Client customization for WIP 58122 ends

        if ( isSubmitted )
        {
          if ( forApprovers && ( typeCode.equals( ClaimFormStepEmailNotificationType.CLAIM_SUBMITTED ) || typeCode.equals( ClaimFormStepEmailNotificationType.RECOGNITION_SUBMITTED )
              || typeCode.equals( PromotionEmailNotificationType.TO_APPROVER_WHEN_CLAIM_SUBMITTED ) ) )
          {
            // TODO need to pass if the approver is getting mail as defaultApprover or normal
            // approver. Setting as false for testing purpose
            mailingRecipients = getApproversRecipientsList( claim, unnotifiedApprovers, promotion.getName(), false );
          }
          if ( !forApprovers && typeCode.equals( PromotionEmailNotificationType.TO_NOMINEE_WHEN_CLAIM_SUBMITTED ) )
          {
            mailingRecipients = getNomineeForSubmitList( (NominationClaim)claim, promotion.getName() );
          }
          if ( !forApprovers && typeCode.equals( PromotionEmailNotificationType.TO_NOMINEES_MANAGER_WHEN_CLAIM_SUBMITTED ) )
          {
            mailingRecipients = getNomineeManagerForSubmitList( (NominationClaim)claim, promotion.getName() );
          }
          
          
        //Client customizations for WIP #59461 starts
          if ( !forApprovers && typeCode.equals( ClaimFormStepEmailNotificationType.TO_NOMINATOR_WHEN_SUBMITTED ) )
          {
            mailingRecipients = getNominatorForSubmitList( (NominationClaim)claim, promotion.getName() );
          }
          //Client customizations for WIP #59461 ends
          // since this approval start date available to all promotions, it should be done here
          Date approvalStartDate = promotion.getApprovalStartDate();
          Date submissionDate = claim.getSubmissionDate();
          if ( approvalStartDate != null && approvalStartDate.after( submissionDate ) )
          {
            for ( Iterator aIter = mailingRecipients.iterator(); aIter.hasNext(); )
            {
              MailingRecipient mailingRecipient = (MailingRecipient)aIter.next();
              MailingRecipientData mailingRecipientData = new MailingRecipientData();
              mailingRecipientData.setKey( "approvalStartDate" );
              mailingRecipientData.setValue( DateUtils.toDisplayString( approvalStartDate, LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );
              mailingRecipient.addMailingRecipientData( mailingRecipientData );
            }
          }
        }
        else if ( !forApprovers && !isSubmitted
            && ( typeCode.equals( ClaimFormStepEmailNotificationType.CLAIM_APPROVED ) || typeCode.equals( ClaimFormStepEmailNotificationType.RECOGNITION_APPROVED_EMAIL ) ) )
        {
          if ( promotion.isRecognitionPromotion() )
          {
            RecognitionClaim recognitionClaim = (RecognitionClaim)claim;
            RecognitionPromotion recognitionPromotion = (RecognitionPromotion)promotion;

            for ( Iterator recipientIterator = recognitionClaim.getClaimRecipients().iterator(); recipientIterator.hasNext(); )
            {
              ClaimRecipient claimRecipient = (ClaimRecipient)recipientIterator.next();
              MailingRecipient mailingRecipient = new MailingRecipient();

              User user = claim.getSubmitter();
              mailingRecipient.setUser( user );
              if ( user.getLanguageType() != null )
              {
                mailingRecipient.setLocale( user.getLanguageType().getCode() );
              }
              else
              {
                mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
              }
              mailingRecipient.setGuid( GuidUtils.generateGuid() );

              Map dataMap = new HashMap();

              dataMap.put( "giverFirstName", user.getFirstName() );
              dataMap.put( "giverLastName", user.getLastName() );
              dataMap.put( "receiverFirstName", claimRecipient.getFirstName() );
              dataMap.put( "receiverLastName", claimRecipient.getLastName() );
              String awardQuantity = null;
              if ( claimRecipient.getAwardQuantity() != null )
              {
                awardQuantity = NumberFormatUtil.getUserLocaleBasedNumberFormat( claimRecipient.getAwardQuantity(), LocaleUtils.getLocale( mailingRecipient.getLocale().toString() ) );
                // QC bug fix #3135 also changed cmData.xml
                dataMap.put( "awardAmountUsed", "TRUE" );
                dataMap.put( "awardAmount", String.valueOf( awardQuantity ) );
                dataMap.put( "mediaType", recognitionPromotion.getAwardType().getAbbr() );
              }

              dataMap.put( "claimNumber", claim.getClaimNumber() );

              // dataMap.put( "approvalStatus", claimRecipient.getApprovalStatusType().getName() );
              // bug fix:38841
              ContentReader contentReader = ContentReaderManager.getContentReader();
              List contentList = (List)contentReader.getContent( "picklist.approval.status.items", CmsUtil.getLocale( mailingRecipient.getLocale() ) );

              for ( Iterator itr = contentList.iterator(); itr.hasNext(); )
              {
                Content content = (Content)itr.next();
                String code = (String)content.getContentDataMap().get( PickListItem.ITEM_CODE_KEY );
                if ( claimRecipient.getApprovalStatusType().getCode().equalsIgnoreCase( code ) )
                {
                  String approvalStatus = (String)content.getContentDataMap().get( PickListItem.ITEM_NAME_KEY );
                  dataMap.put( "approvalStatus", StringEscapeUtils.unescapeHtml4( approvalStatus ) );
                  break;
                }
              }
              // bug fix:38841

              if ( recognitionPromotion.isAwardActive() && recognitionPromotion.getAwardType().isMerchandiseAwardType() && recognitionPromotion.getAwardStructure().equals( MerchGiftCodeType.LEVEL ) )
              {
                dataMap.put( "levelLabel", claimRecipient.getPromoMerchProgramLevel().getDisplayLevelName() );
              }

              User mostRecentApprover = claimRecipient.getCurrentApproverUser();
              if ( mostRecentApprover != null )
              {
                dataMap.put( "approverFirstName", mostRecentApprover.getFirstName() );
                dataMap.put( "approverLastName", mostRecentApprover.getLastName() );
              }

              mailingRecipient.addMailingRecipientDataFromMap( dataMap );

              mailingRecipients.add( mailingRecipient );

            } // for claimRecipients
          } // if recognition claim

        }
        else if ( !forApprovers && !isSubmitted && typeCode.equals( ClaimFormStepEmailNotificationType.CLAIM_DENIED ) )
        {
          if ( promotion.isProductClaimPromotion() )
          {
            ProductClaim productClaim = (ProductClaim)claim;
            boolean anyClaimProductApproved = false;

            ProductClaimPromotion pcp = (ProductClaimPromotion)promotion;
            PromotionPayoutType payoutType = pcp.getPayoutType();
            if ( payoutType != null && payoutType.getCode().equals( PromotionPayoutType.CROSS_SELL ) )
            {
              anyClaimProductApproved = true;
              // if we find any denied product claims here, then we gotta send notification
              for ( Iterator productClaimIter = productClaim.getClaimProducts().iterator(); productClaimIter.hasNext(); )
              {
                ClaimProduct claimProduct = (ClaimProduct)productClaimIter.next();
                if ( claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.DENIED ) )
                {
                  anyClaimProductApproved = false;
                  break;
                }
              } // for ClaimProducts
            }
            else
            {
              for ( Iterator productClaimIter = productClaim.getClaimProducts().iterator(); productClaimIter.hasNext(); )
              {
                ClaimProduct claimProduct = (ClaimProduct)productClaimIter.next();
                if ( claimProduct.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) )
                {
                  anyClaimProductApproved = true;
                  break;
                }
              } // for ClaimProducts
            }

            if ( !anyClaimProductApproved )
            {
              MailingRecipient mailingRecipient = mailingService.buildMailingRecipientForProductClaimEmail( productClaim, claim.getSubmitter(), null, null, null, claim.getPromotion() );
              mailingRecipients.add( mailingRecipient );
            }
          }
        }

        // Only submit a mailing if recipients were found.
        if ( mailingRecipients.size() > 0 )
        {
          Message message = messageService.getMessageById( new Long( claimFormNotificationType.getNotificationMessageId() ) );
          if ( message == null )
          {
            log.debug( "message not found for id:" + claimFormNotificationType.getNotificationMessageId() );
            continue;
          }
          // Create mailing object
          Mailing mailing = new Mailing();
          mailing.setMessage( message );

          mailing.addMailingRecipients( mailingRecipients );
          mailing.setDeliveryDate( new Timestamp( new Date().getTime() ) );
          mailing.setSender( SYSTEM_NOTIFICATION_EMAIL_NAME );
          mailing.setMailingType( MailingType.lookup( MailingType.PROMOTION ) );
          mailing.setGuid( GuidUtils.generateGuid() );

          mailingService.submitMailing( mailing, null );
        }

      } // if notification was a ClaimFormNotification
    } // for promotion notifications
  }

  private Set getApproversRecipientsList( Approvable claim, List unnotifiedApprovers, String promoName, boolean defaultApprover )
  {
    Set approversRecipientsSet = new HashSet();

    // build approver recipient list
    Iterator approverIter = unnotifiedApprovers.iterator();
    while ( approverIter.hasNext() )
    {
      MailingRecipient mailingRecipient = new MailingRecipient();
      User user = (User)approverIter.next();

      mailingRecipient.setUser( user );
      if ( user.getLanguageType() != null )
      {
        mailingRecipient.setLocale( user.getLanguageType().getCode() );
      }
      else
      {
        mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
      }
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      Map dataMap = new HashMap();
      dataMap.put( "firstName", user.getFirstName() );
      dataMap.put( "lastName", user.getLastName() );

      if ( claim instanceof NominationClaim )
      {
        NominationClaim nomClaim = (NominationClaim)claim;
        Long pendingNominationCount = participantService.getPendingNominationCountForApprover( user.getId() );
        pendingNominationCount = pendingNominationCount + 1;
        if ( nomClaim.isTeam() && StringUtils.isNotEmpty( nomClaim.getTeamName() ) )
        {
          dataMap.put( "team", "TRUE" );
          dataMap.put( "teamName", nomClaim.getTeamName() );
        }
        else
        {
          dataMap.put( "individual", "TRUE" );
        }

        dataMap.put( "promotionName", promoName );
        if ( defaultApprover )
        {
          dataMap.put( "defaultApprover", "TRUE" );
        }
        // dataMap.put( "defaultApprover", String.valueOf( defaultApprover ) );
        dataMap.put( "pendingNominationCount", pendingNominationCount.toString() );
        if ( pendingNominationCount > 1 )
        {
          dataMap.put( "multipleNominationsPending", "TRUE" );
        }

        if ( !nomClaim.isTeam() || ( nomClaim.isTeam() && StringUtils.isEmpty( nomClaim.getTeamName() ) ) )
        {
          Iterator<ClaimRecipient> claimRecipientIter = nomClaim.getClaimRecipients().iterator();
          while ( claimRecipientIter.hasNext() )
          {
            ClaimRecipient recipient = claimRecipientIter.next();

            dataMap.put( "nomineeFirstName", recipient.getFirstName() );
            dataMap.put( "nomineeLastName", recipient.getLastName() );
          }
        }
        dataMap.put( "nominatorFirstName", nomClaim.getSubmitter().getFirstName() );
        dataMap.put( "nominatorLastName", nomClaim.getSubmitter().getLastName() );
        dataMap.put( "nominationSubmissionDate", DateUtils.toDisplayString( nomClaim.getSubmissionDate(), LocaleUtils.getLocale( mailingRecipient.getLocale() ) ) );

      }

      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

      dataMap.put( "approvalPageLink", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/claim/approvalsListPage.do" );

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );

      approversRecipientsSet.add( mailingRecipient );
    }

    return approversRecipientsSet;
  }

  private Set<MailingRecipient> getNomineeForSubmitList( NominationClaim claim, String promoName )
  {
    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    Iterator<ClaimRecipient> recipientIterator = null;

    recipientIterator = claim.getClaimRecipients().iterator();

    while ( recipientIterator.hasNext() )
    {
      User user = null;
      String firstName = null;
      String lastName = null;
      Long paxId = null;

      ClaimRecipient claimRecipient = (ClaimRecipient)recipientIterator.next();
      user = claimRecipient.getRecipient();
      firstName = claimRecipient.getRecipient().getFirstName();
      lastName = claimRecipient.getRecipient().getLastName();
      paxId = claimRecipient.getRecipient().getId();

      MailingRecipient mailingRecipient = new MailingRecipient();

      mailingRecipient.setUser( user );
      if ( user.getLanguageType() != null )
      {
        mailingRecipient.setLocale( user.getLanguageType().getCode() );
      }
      else
      {
        mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
      }
      mailingRecipient.setGuid( GuidUtils.generateGuid() );

      Map<String, String> dataMap = new HashMap<>();

      dataMap.put( "firstName", firstName );
      dataMap.put( "lastName", lastName );
      dataMap.put( "promotionName", promoName );
      dataMap.put( "nominator", claim.getSubmitter().getFirstName() + " " + claim.getSubmitter().getLastName() );
      dataMap.put( "siteUrl", systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() );
      dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

      // Begin behaviors
      if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
      {
        dataMap.put( "showCategory", "TRUE" );
        List<String> behaviorNames = claim.getNominationClaimBehaviors().stream().map( ( behavior ) -> behavior.getBehavior().getName() ).collect( Collectors.toList() );
        String behaviorString = StringUtil.convertListToCommaSeparated( behaviorNames );
        dataMap.put( "category", behaviorString );
      }
      // End behaviors

      if ( claim.hasTeamName() )
      {
        dataMap.put( "team", "TRUE" );
        dataMap.put( "teamName", claim.getTeamName() );
      }
      else
      {
        dataMap.put( "individual", "TRUE" );
      }

      dataMap.put( "message", getNominationClaimComment( claim ) );

      // Begin card
      if ( StringUtils.isNotBlank( claim.getOwnCardName() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        dataMap.put( "eCardImg", claim.getOwnCardName() );
      }
      else if ( claim.getCard() != null )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeImage", "TRUE" );
        String eCardImg = claim.getCard().getLargeImageNameLocale();
        dataMap.put( "eCardImg", eCardImg );
      }
      else if ( StringUtils.isNotBlank( claim.getCardVideoUrl() ) )
      {
        dataMap.put( "showECard", "TRUE" );
        dataMap.put( "ecardTypeVideo", "TRUE" );
        // MTC- To be changed
        if ( claim.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
        {
          MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( claim.getRequestId( claim.getCardVideoUrl() ) );
          String eCardVideoLink = null;
          if ( Objects.nonNull( mtcVideo ) )
          {
            eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
          }
          else
          {
            eCardVideoLink = claim.getActualCardUrl( claim.getCardVideoUrl() );
          }
          dataMap.put( "eCardVideoLink", eCardVideoLink );
        }
        else
        {
          dataMap.put( "eCardVideoLink", claim.getCardVideoUrl() );
        }
        // Generic play button thumbnail
        String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
        dataMap.put( "eCardVideoImg", eCardVideoImg );
      }
      // End card

      // to add badge information
      mailingService.buildBadgesForNominationPromotionNotification( dataMap, claim, paxId, false );

      // Begin certificate
      if ( claim.getCertificateId() != null )
      {
        Map<String, Object> clientStateParameterMap = new HashMap<>();
        clientStateParameterMap.put( "userId", user.getId() );
        clientStateParameterMap.put( "claimId", claim.getId() );
        clientStateParameterMap.put( "promotionId", claim.getPromotion().getId() );
        String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                       "/claim/displayCertificate.do?method=showCertificateNominationDetail",
                                                                       clientStateParameterMap );
        dataMap.put( "showCertificate", "TRUE" );
        dataMap.put( "certificateLink", certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
      }
      // End certificate

      dataMap.put( "claimNumber", claim.getClaimNumber() );

      mailingRecipient.addMailingRecipientDataFromMap( dataMap );

      mailingRecipients.add( mailingRecipient );
    }

    return mailingRecipients;
  }

  // get nomination claim comment from claim table or from claim element if nom promo use is why in
  // form library
  private String getNominationClaimComment( NominationClaim nomClaim )
  {
    String comment = null;
    NominationPromotion nomPromo = (NominationPromotion)nomClaim.getPromotion();
    if ( !nomPromo.isWhyNomination() )
    {
      for ( Iterator iter = nomClaim.getClaimElements().iterator(); iter.hasNext(); )
      {
        ClaimElement claimElement = (ClaimElement)iter.next();
        ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
        if ( claimFormStepElement.isWhyField() )
        {
          comment = claimElement.getValue();
        }
      }
    }
    else
    {
      comment = nomClaim.getSubmitterComments();
    }

    return comment;
  }

  private Set<MailingRecipient> getNomineeManagerForSubmitList( NominationClaim claim, String promoName )
  {
    Set<MailingRecipient> mailingRecipients = new HashSet<>();

    Iterator<ClaimRecipient> recipientIterator = null;

    recipientIterator = claim.getClaimRecipients().iterator();

    while ( recipientIterator.hasNext() )
    {
      User user = null;
      Iterator managerIter = null;
      String firstName = null;
      String lastName = null;
      Long paxId = null;

      if ( claim.isTeam() )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)recipientIterator.next();
        user = claimRecipient.getRecipient();
        if ( user.isMemberOf( claimRecipient.getNode() ) )
        {
          managerIter = participantService.getNodeManager( claimRecipient.getNode().getId(), HierarchyRoleType.MANAGER ).iterator();
          if ( !managerIter.hasNext() )
          {
            managerIter = participantService.getNodeManager( claimRecipient.getNode().getId(), HierarchyRoleType.OWNER ).iterator();
            if ( !managerIter.hasNext() )
            {
              Node node = claimRecipient.getNode().getParentNode();
              while ( null != node )
              {
                // Get the node owner
                managerIter = participantService.getNodeManager( node.getId(), HierarchyRoleType.OWNER ).iterator();
                if ( managerIter.hasNext() )
                {
                  // Found the node owner
                  break;
                }
                node = node.getParentNode();
              }
            }
          }
        }
        else if ( user.isOwnerOf( claimRecipient.getNode() ) )
        {
          Node node = claimRecipient.getNode().getParentNode();
          while ( null != node )
          {
            // Get the node owner
            managerIter = participantService.getNodeManager( node.getId(), HierarchyRoleType.OWNER ).iterator();
            if ( managerIter.hasNext() )
            {
              // Found the node owner
              break;
            }
            node = node.getParentNode();
          }
        }
        else
        {
          managerIter = participantService.getNodeManager( claimRecipient.getNode().getId(), HierarchyRoleType.OWNER ).iterator();
          if ( !managerIter.hasNext() )
          {
            Node node = claimRecipient.getNode().getParentNode();
            while ( null != node )
            {
              // Get the node owner
              managerIter = participantService.getNodeManager( node.getId(), HierarchyRoleType.OWNER ).iterator();
              if ( managerIter.hasNext() )
              {
                // Found the node owner
                break;
              }
              node = node.getParentNode();
            }
          }
        }
        // managerIter = claimRecipient.getNode().getNodeManagersForUser( user ).iterator();
        firstName = claimRecipient.getRecipient().getFirstName();
        lastName = claimRecipient.getRecipient().getLastName();
        paxId = claimRecipient.getRecipient().getId();
      }
      else
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)recipientIterator.next();
        user = claimRecipient.getRecipient();
        if ( user.isMemberOf( claimRecipient.getNode() ) )
        {
          managerIter = participantService.getNodeManager( claimRecipient.getNode().getId(), HierarchyRoleType.MANAGER ).iterator();
          if ( !managerIter.hasNext() )
          {
            managerIter = participantService.getNodeManager( claimRecipient.getNode().getId(), HierarchyRoleType.OWNER ).iterator();
            if ( !managerIter.hasNext() )
            {
              Node node = claimRecipient.getNode().getParentNode();
              while ( null != node )
              {
                // Get the node owner
                managerIter = participantService.getNodeManager( node.getId(), HierarchyRoleType.OWNER ).iterator();
                if ( managerIter.hasNext() )
                {
                  // Found the node owner
                  break;
                }
                node = node.getParentNode();
              }
            }
          }
        }
        else if ( user.isOwnerOf( claimRecipient.getNode() ) )
        {
          Node node = claimRecipient.getNode().getParentNode();
          while ( null != node )
          {
            // Get the node owner
            managerIter = participantService.getNodeManager( node.getId(), HierarchyRoleType.OWNER ).iterator();
            if ( managerIter.hasNext() )
            {
              // Found the node owner
              break;
            }
            node = node.getParentNode();
          }
        }
        else
        {
          managerIter = participantService.getNodeManager( claimRecipient.getNode().getId(), HierarchyRoleType.OWNER ).iterator();
          if ( !managerIter.hasNext() )
          {
            Node node = claimRecipient.getNode().getParentNode();
            while ( null != node )
            {
              // Get the node owner
              managerIter = participantService.getNodeManager( node.getId(), HierarchyRoleType.OWNER ).iterator();
              if ( managerIter.hasNext() )
              {
                // Found the node owner
                break;
              }
              node = node.getParentNode();
            }
          }
        }
        // managerIter = claimRecipient.getNode().getNodeManagersForUser( user ).iterator();
        firstName = claimRecipient.getRecipient().getFirstName();
        lastName = claimRecipient.getRecipient().getLastName();
        paxId = claimRecipient.getRecipient().getId();
      }

      if ( managerIter != null )
      {
        while ( managerIter.hasNext() )
        {
          User managerUser = (User)managerIter.next();
          MailingRecipient mailingRecipient = new MailingRecipient();

          mailingRecipient.setUser( managerUser );
          if ( managerUser.getLanguageType() != null )
          {
            mailingRecipient.setLocale( managerUser.getLanguageType().getCode() );
          }
          else
          {
            mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
          }
          mailingRecipient.setGuid( GuidUtils.generateGuid() );

          Map<String, String> dataMap = new HashMap<>();

          dataMap.put( "firstName", firstName );
          dataMap.put( "lastName", lastName );
          dataMap.put( "promotionName", promoName );
          dataMap.put( "nominator", claim.getSubmitter().getFirstName() + " " + claim.getSubmitter().getLastName() );
          dataMap.put( "managerFirstName", managerUser.getFirstName() );

          // Begin behaviors
          if ( claim.getNominationClaimBehaviors() != null && !claim.getNominationClaimBehaviors().isEmpty() )
          {
            dataMap.put( "showCategory", "TRUE" );
            List<String> behaviorNames = claim.getNominationClaimBehaviors().stream().map( ( behavior ) -> behavior.getBehavior().getName() ).collect( Collectors.toList() );
            String behaviorString = StringUtil.convertListToCommaSeparated( behaviorNames );
            dataMap.put( "category", behaviorString );
          }
          // End behaviors

          if ( claim.hasTeamName() )
          {
            dataMap.put( "team", "TRUE" );
            dataMap.put( "teamName", claim.getTeamName() );
          }
          else
          {
            dataMap.put( "individual", "TRUE" );
          }
          dataMap.put( "message", getNominationClaimComment( claim ) );

          // Begin card
          if ( StringUtils.isNotBlank( claim.getOwnCardName() ) )
          {
            dataMap.put( "showECard", "TRUE" );
            dataMap.put( "ecardTypeImage", "TRUE" );
            dataMap.put( "eCardImg", claim.getOwnCardName() );
          }
          else if ( claim.getCard() != null )
          {
            dataMap.put( "showECard", "TRUE" );
            dataMap.put( "ecardTypeImage", "TRUE" );
            String eCardImg = claim.getCard().getLargeImageNameLocale();
            dataMap.put( "eCardImg", eCardImg );
          }
          else if ( StringUtils.isNotBlank( claim.getCardVideoUrl() ) )
          {
            dataMap.put( "showECard", "TRUE" );
            dataMap.put( "ecardTypeVideo", "TRUE" );
            // MTC- To be changed

            if ( claim.getCardVideoUrl().contains( ActionConstants.REQUEST_ID ) )
            {

              MTCVideo mtcVideo = getMtcVideoService().getMTCVideoByRequestId( claim.getRequestId( claim.getCardVideoUrl() ) );
              String eCardVideoLink = null;
              if ( Objects.nonNull( mtcVideo ) )
              {
                eCardVideoLink = mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl();
              }
              else
              {
                eCardVideoLink = claim.getActualCardUrl( claim.getCardVideoUrl() );
              }
              dataMap.put( "eCardVideoLink", eCardVideoLink );
            }
            else
            {
              dataMap.put( "eCardVideoLink", claim.getCardVideoUrl() );
            }
            // Generic play button thumbnail
            String eCardVideoImg = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + DEFAULT_VIDEO_IMAGE;
            dataMap.put( "eCardVideoImg", eCardVideoImg );
          }
          // End card

          // to add badge information
          mailingService.buildBadgesForNominationPromotionNotification( dataMap, claim, paxId, false );

          // Begin certificate
          if ( claim.getCertificateId() != null )
          {
            Map<String, Object> clientStateParameterMap = new HashMap<>();
            clientStateParameterMap.put( "userId", user.getId() );
            clientStateParameterMap.put( "claimId", claim.getId() );
            clientStateParameterMap.put( "promotionId", claim.getPromotion().getId() );
            String certificatelink = ClientStateUtils.generateEncodedLink( systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                                           "/claim/displayCertificate.do?method=showCertificateNominationDetail",
                                                                           clientStateParameterMap );
            dataMap.put( "showCertificate", "TRUE" );
            dataMap.put( "certificateLink", certificatelink + "&cmsLocaleCode=" + mailingRecipient.getLocale() );
          }
          // End certificate

          dataMap.put( "claimNumber", claim.getClaimNumber() );

          mailingRecipient.addMailingRecipientDataFromMap( dataMap );

          mailingRecipients.add( mailingRecipient );
        }
      }
    }

    return mailingRecipients;
  }

  public String getShortUrl( String url )
  {
    String returnUrl = "";
    String initialUrl = "";
    String signature = "";
    String xmlString = "";
    BufferedReader br = null;
    try
    {
      initialUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.URLSHORTNER_URL ).getStringVal();
      signature = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.URLSHORTNER_SIGNATURE ).getStringVal();
      URL shortUrl = new URL( initialUrl + "?signature=" + signature + "&action=shorturl&url=" + url );

      URLConnection uc = shortUrl.openConnection( Environment.buildProxy() );

      br = new BufferedReader( new InputStreamReader( uc.getInputStream() ) );
      xmlString = br.readLine();
      br.close();
      DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      InputSource is = new InputSource();
      is.setCharacterStream( new StringReader( xmlString ) );

      Document doc = db.parse( is );
      NodeList nodes = doc.getElementsByTagName( "result" );

      for ( int i = 0; i < nodes.getLength(); i++ )
      {
        Element element = (Element)nodes.item( i );

        NodeList name = element.getElementsByTagName( "shorturl" );
        Element line = (Element)name.item( 0 );
        returnUrl = getCharacterDataFromElement( line );
      }
    }
    catch( Exception e )
    {
      returnUrl = "";
      log.error( "Url we are trying to shorten is: " + url );
      log.error( "Error occurred getting shortened url:" + e );
    }
    finally
    {
      try
      {
        if ( br != null )
        {
          br.close();
        }
      }
      catch( IOException ex )
      {
        log.error( "Error while closing br connection in finally block: " + ex );
      }
    }
    return returnUrl;
  }

  public static String getCharacterDataFromElement( Element e )
  {
    org.w3c.dom.Node child = e.getFirstChild();
    if ( child instanceof CharacterData )
    {
      CharacterData cd = (CharacterData)child;
      return cd.getData();
    }
    return "";
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  // Client customization for WIP 58122 starts
  private void sendCustomNomineeWinnerMessage( MailingRecipient recipientUser)
  {
    Map objectMap = new HashMap();
    // Compose the mailing
    Mailing mailing = composeMail( MessageService.CUSTOM_NOMINATION_WINNER_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );
    mailing.addMailingRecipient( recipientUser );
    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " );
    }
  }
  protected Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();
    mailing.setMailingType( MailingType.lookup( mailingType ) );
    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );
    return mailing;
  }
  protected Mailing composeMail()
  {
    Mailing mailing = new Mailing();
    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );
    // Sender
    String sender = systemVariableService
        .getPropertyByName( SystemVariableService.SYSTEM_SENDER_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );
    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate()
        .getTime() );
    mailing.setDeliveryDate( deliveryDate );
    return mailing;
  }
  protected MailingRecipient addRecipient( User recipient )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    mailingRecipient.setLocale( localeCode );
    mailingRecipient.setUser( recipient );
    return mailingRecipient;
  }
  // Client customization for WIP 58122 ends
  
//Client customizations for WIP #59461 starts
  private Set getNominatorForSubmitList( NominationClaim claim, String promoName )
  {
    Set mailingRecipients = new HashSet();
    String rfirstName=null;
    String rlastName=null;
    Iterator recipientIterator = null;
    
    if ( claim.isTeam() )
    {
      recipientIterator = claim.getTeamMembers().iterator();
    }
    else
    {
      recipientIterator = claim.getClaimRecipients().iterator();
    }
    StringBuilder emailDataBuilder = new StringBuilder();
    emailDataBuilder.append( "<table style=\"width: 550px;\" border=\"0\" cellspacing=\"0\"> " );
    emailDataBuilder.append( "<tbody>" );
    while (recipientIterator.hasNext() )
    {
    if ( claim.isTeam() )
    {
      ProductClaimParticipant claimRecipient = (ProductClaimParticipant)recipientIterator.next();
      User claimRecipientUser = claimRecipient.getParticipant();
      rfirstName = claimRecipient.getParticipant().getFirstName();
      rlastName = claimRecipient.getParticipant().getLastName();
     emailDataBuilder.append( "<tr>" );
     emailDataBuilder.append( "<td>" );
     emailDataBuilder.append( rfirstName );
     emailDataBuilder.append( "," );
     emailDataBuilder.append( rlastName );
     emailDataBuilder.append( "</td>" );
     emailDataBuilder.append( "</tr>" );
    }
    else
    {
    ClaimRecipient claimRecipient = (ClaimRecipient)recipientIterator.next();
    User claimRecipientUser = claimRecipient.getRecipient();
    emailDataBuilder.append( "<tr>" );
    emailDataBuilder.append( "<td>" );
    emailDataBuilder.append( claimRecipientUser.getFirstName() );
    emailDataBuilder.append( "," );
    emailDataBuilder.append( claimRecipientUser.getLastName() );
    emailDataBuilder.append( "</td>" );
    emailDataBuilder.append( "</tr>" );
    }
    }
    emailDataBuilder.append( "</tbody> \r\n" + "</table> " );
    
        User submitterUser = claim.getSubmitter(); 
        MailingRecipient mailingRecipient = new MailingRecipient();
        mailingRecipient.setUser( claim.getSubmitter() );
        if ( submitterUser.getLanguageType() != null )
        {
          mailingRecipient.setLocale( submitterUser.getLanguageType().getCode() );
        }
        else
        {
          mailingRecipient.setLocale( LanguageType.ENGLISH );
        }
        mailingRecipient.setGuid( GuidUtils.generateGuid() );
        Map dataMap = new HashMap();
        dataMap.put( "firstName", claim.getSubmitter().getFirstName() );
        dataMap.put( "lastName", claim.getSubmitter().getLastName() );
        dataMap.put( "programName", promoName );
        if ( claim.isTeam() )
        {
          dataMap.put( "team", "TRUE" );
        }
        else
        {
          dataMap.put( "individual", "TRUE" );
        }
        dataMap.put( "teamName", claim.getTeamName() );
        dataMap.put( "tableData", emailDataBuilder.toString() );
        mailingRecipient.addMailingRecipientDataFromMap( dataMap );
        mailingRecipients.add( mailingRecipient );
    return mailingRecipients;
  }
  //Client customizations for WIP #59461 ends

}
