/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/DelayedSendRecognitionProcess.java,v $
 */

package com.biperf.core.process;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.calculator.CalculatorCriterionRating;
import com.biperf.core.domain.claim.CalculatorResponse;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromoRecognitionBehaviorType;
import com.biperf.core.domain.enums.PromotionBehaviorType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.multimedia.Card;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.CelebrationManagerMessage;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.service.multimedia.MultimediaService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.scheduledrecognition.ScheduledRecognitionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * DelayedSendRecognitionProcess.
 * 
 * @author arasi
 * @since 12-Sep-2012
 * @version 1.0
 */
public class DelayedSendRecognitionProcess extends BaseProcessImpl
{
  private static final Log logger = LogFactory.getLog( DelayedSendRecognitionProcess.class );

  public static final String PROCESS_NAME = "Delayed Send Recognition Process";
  public static final String BEAN_NAME = "delayedSendRecognitionProcess";

  private PromotionService promotionService;
  private NodeService nodeService;
  private ClaimService claimService;
  private MultimediaService multimediaService;
  private MerchLevelService merchLevelService;
  private BudgetMasterService budgetMasterService;
  private ClaimFormDefinitionService claimFormDefinitionService;
  private CalculatorService calculatorService;
  private ScheduledRecognitionService scheduledRecognitionService;
  private EmailService emailService;
  private CelebrationService celebrationService;

  // properties set from jobDataMap
  private String promotionId;
  private String submitterUserId;
  private String submitterNodeId;
  private String proxyUserId;
  private String recipientUserId;
  private String recipientNodeId;
  private String submitterComments;
  private String budgetId;
  private String teamId;

  private String behavior;
  private String certificateId;
  private String cardId;
  private String sendCopyToMe;
  private String sendCopyToManager;
  private String copyOthers;
  private String sendCopyToOthers;

  private String awardQuantity;
  private String calculatorScore;
  private String levelId;
  private String productId;
  private String recipientCountryCode;

  private String claimElements;
  private String calculatorResponses;
  private String triggerName;
  private String anniversaryNumberOfDays;
  private String anniversaryNumberOfYears;
  private String celebrationManagerMessageId;

  public void onExecute()
  {
    try
    {
      Participant recipient = participantService.getParticipantById( new Long( recipientUserId ) );
      if ( !recipient.isActive() )
      {
        addComment( "Recognition recipient " + recipient.getNameFLNoComma() + " is inactive. Hence the claim cannot be processed." );
        reportClaimFailure( recipient );
      }
      else
      {
        Claim claim = buildClaim( (RecognitionPromotion)promotionService.getPromotionById( new Long( promotionId ) ), recipient );
        Budget budget = null;
        if ( !StringUtil.isEmpty( budgetId ) )
        {
          budget = budgetMasterService.getBudgetbyId( new Long( budgetId ) );
        }
        claimService.saveandUpdateRecognitionClaim( claim, null, null, false, false, budget );
        addComment( "Recognition claim has been successfully processed." );

        if ( triggerName != null )
        {
          scheduledRecognitionService.updateScheduleRecognitionWithClaimId( triggerName, claim.getId() );
          addComment( "Scheduled Recognition entry has been successfully updated." );
        }
      }
    }
    catch( ServiceErrorException se )
    {
      logger.error( "An exception occurred while processing claim " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ")", se );
      addComment( "An exception occurred while processing claim " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );

      Participant submitter = participantService.getParticipantById( new Long( submitterUserId ) );
      Participant recipient = participantService.getParticipantById( new Long( recipientUserId ) );
      String subject = "An exception occurred while processing claim";
      EmailHeader header = new EmailHeader();
      PropertySetItem sysProperty = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS );
      header.setRecipients( new String[] { sysProperty.toString() } );
      header.setSender( submitter.getPrimaryEmailAddress().getEmailAddr() );
      subject = "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() + " ] " + subject;
      header.setSubject( subject );

      String name = "";
      StringBuffer bodyHeader = null;
      if ( recipient.getFirstName() != null && recipient.getFirstName().length() > 0 || recipient.getLastName() != null && recipient.getLastName().length() > 0 )
      {
        bodyHeader = new StringBuffer();
        bodyHeader.append( CmsResourceBundle.getCmsBundle().getString( "help.contact.us.HEADER_BODY" ) );
        bodyHeader.append( " '" );
        bodyHeader.append( recipient.getFirstName() );
        bodyHeader.append( " " );
        bodyHeader.append( recipient.getLastName() );
        bodyHeader.append( " (" );
        bodyHeader.append( recipient.getPrimaryEmailAddress().getEmailAddr() );
        bodyHeader.append( ")':" );
        bodyHeader.append( "\n" );
        bodyHeader.append( " '" );
        bodyHeader.append( submitter.getFirstName() );
        bodyHeader.append( " " );
        bodyHeader.append( submitter.getLastName() );
        bodyHeader.append( " (" );
        bodyHeader.append( submitter.getPrimaryEmailAddress().getEmailAddr() );
        bodyHeader.append( ")':" );
        bodyHeader.append( "\n" );

        name = bodyHeader.toString();
      }
      TextEmailBody body = new TextEmailBody( name );
      try
      {
        emailService.sendMessage( header, body );
      }
      catch( ServiceErrorException e )
      {
        throw new BeaconRuntimeException( "Error sending message", e );
      }
      throw new BeaconRuntimeException( se );
    }
  }

  private Claim buildClaim( RecognitionPromotion promotion, Participant recipient )
  {
    RecognitionClaim claim = new RecognitionClaim();

    Card selectedCard = null;
    Long selectedCertificateId = null;
    if ( !StringUtil.isEmpty( certificateId ) )
    {
      selectedCertificateId = new Long( certificateId );
    }
    if ( !StringUtil.isEmpty( cardId ) )
    {
      selectedCard = multimediaService.getCardById( new Long( cardId ) );
    }
    String comments = submitterComments;
    if ( StringUtil.isEmpty( comments ) )
    {
      comments = " ";
    }
    Participant submitter = participantService.getParticipantById( new Long( submitterUserId ) );
    Node submitterNode = nodeService.getNodeById( new Long( submitterNodeId ) );
    claim.setSubmitter( submitter );
    claim.setNode( submitterNode );
    String timeZoneID = getUserService().getUserTimeZone( submitter.getId() );
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
    claim.setSubmissionDate( referenceDate );

    if ( !StringUtil.isEmpty( proxyUserId ) )
    {
      claim.setProxyUser( userService.getUserById( new Long( proxyUserId ) ) );
    }

    ClaimRecipient claimRecipient = buildClaimRecipient( promotion, recipient );
    claim.addClaimRecipient( claimRecipient );

    claim.setPromotion( promotion );
    claim.setOpen( true );
    claim.setSubmitterComments( comments );
    claim.setSubmitterCommentsLanguageType( userService.getPreferredLanguageFor( submitter ) );
    claim.setCopySender( Boolean.valueOf( sendCopyToMe ).booleanValue() );
    claim.setCopyManager( Boolean.valueOf( sendCopyToManager ).booleanValue() );
    claim.setCopyOthers( Boolean.valueOf( copyOthers ).booleanValue() );
    claim.setSendCopyToOthers( sendCopyToOthers );
    claim.setTeamId( new Long( teamId ) );

    claim.setCard( selectedCard );
    claim.setCertificateId( selectedCertificateId );
    claim.setSource( RecognitionClaimSource.WEB );

    if ( !StringUtil.isEmpty( behavior ) )
    {
      PromotionBehaviorType promotionBehaviorType = PromoRecognitionBehaviorType.lookup( behavior );
      claim.setBehavior( promotionBehaviorType );
    }

    if ( !StringUtils.isEmpty( anniversaryNumberOfDays ) )
    {
      claim.setAnniversaryNumberOfDays( new Integer( anniversaryNumberOfDays ) );
    }

    if ( !StringUtils.isEmpty( anniversaryNumberOfYears ) )
    {
      claim.setAnniversaryNumberOfYears( new Integer( anniversaryNumberOfYears ) );
    }

    CelebrationManagerMessage celebrationManagerMessage = null;
    if ( !StringUtil.isEmpty( celebrationManagerMessageId ) )
    {
      celebrationManagerMessage = celebrationService.getCelebrationManagerMessageById( new Long( celebrationManagerMessageId ) );
    }
    claim.setCelebrationManagerMessage( celebrationManagerMessage );

    String pipeDelim = "[|]";
    String commaDelim = "[,]";

    // add Claim Form Element values to the claim.
    if ( !StringUtil.isEmpty( claimElements ) )
    {
      String[] pipeTokens = claimElements.split( pipeDelim );
      for ( int i = 0; i < pipeTokens.length; i++ )
      {
        String claimElementValue = pipeTokens[i];
        String[] commaTokens = claimElementValue.split( commaDelim );
        ClaimFormStepElement claimFormStepElement = claimFormDefinitionService.getClaimFormStepElementById( new Long( commaTokens[0] ) );
        ClaimElement claimElement = new ClaimElement();
        claimElement.setClaimFormStepElement( claimFormStepElement );
        if ( commaTokens.length > 1 )
        {
          if ( commaTokens[1].contains( "~" ) )
          {
            commaTokens[1] = commaTokens[1].replace( "~", "|" );
            claimElement.setValue( commaTokens[1] );
          }
          else
          {
            claimElement.setValue( commaTokens[1] );
          }
        }
        else
        {
          claimElement.setValue( "" );
        }
        claimElement.setClaim( claim );
        claim.addClaimElement( claimElement );
      }
    }

    // add calculator responses to the claim.
    if ( !StringUtil.isEmpty( calculatorResponses ) )
    {
      String[] pipeTokens = calculatorResponses.split( pipeDelim );
      for ( int i = 0; i < pipeTokens.length; i++ )
      {
        String calculatorResponseValue = pipeTokens[i];
        String[] commaTokens = calculatorResponseValue.split( commaDelim );

        CalculatorResponse calculatorResponse = new CalculatorResponse();
        calculatorResponse.setCriterion( calculatorService.getCalculatorCriterionByIdWithAssociations( new Long( commaTokens[0] ), null ) );
        calculatorResponse.setCriterionWeight( new Integer( commaTokens[1] ) );
        CalculatorCriterionRating selectedRating = calculatorService.getCriterionRatingById( new Long( commaTokens[2] ) );
        calculatorResponse.setSelectedRating( selectedRating );
        calculatorResponse.setRatingValue( selectedRating.getRatingValue() );
        calculatorResponse.setClaim( claim );
        claim.addCalculatorResponse( calculatorResponse );
      }
    }

    return claim;
  }

  private ClaimRecipient buildClaimRecipient( RecognitionPromotion promotion, Participant recipient )
  {
    ClaimRecipient claimRecipient = new ClaimRecipient();

    if ( StringUtils.isNotBlank( awardQuantity ) && StringUtils.isNumeric( awardQuantity ) )
    {
      if ( recipient.getOptOutAwards() )
      {
        claimRecipient.setAwardQuantity( 0L );
      }
      else
      {
        claimRecipient.setAwardQuantity( new Long( awardQuantity ) );
      }
    }
    if ( StringUtils.isNotBlank( calculatorScore ) && StringUtils.isNumeric( calculatorScore ) )
    {
      claimRecipient.setCalculatorScore( new Long( calculatorScore ) );
    }
    claimRecipient.setPromoMerchCountry( promotion.getPromoMerchCountryForCountryCode( recipientCountryCode ) );
    if ( !StringUtil.isEmpty( levelId ) )
    {
      claimRecipient.setPromoMerchProgramLevel( merchLevelService.getPromoMerchProgramLevelById( new Long( levelId ) ) );
    }
    if ( !StringUtil.isEmpty( productId ) )
    {
      claimRecipient.setProductId( productId );
    }

    claimRecipient.setRecipient( recipient );
    if ( !StringUtil.isEmpty( recipientNodeId ) )
    {
      claimRecipient.setNode( nodeService.getNodeById( new Long( recipientNodeId ) ) );
    }
    claimRecipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.PENDING ) );

    return claimRecipient;
  }

  private void reportClaimFailure( Participant recipient )
  {
    String paxNameAndUserName = recipient.getFirstName() + " " + recipient.getLastName() + " (" + recipient.getUserName() + ")";

    StringBuffer buf = new StringBuffer();
    buf.append( "Recognition recipient " );
    buf.append( paxNameAndUserName );
    buf.append( " is inactive in the system. Hence the claim cannot be processed." );

    String message = buf.toString();
    mailingService.submitSystemMailing( "Delayed Send Recognition Failure for " + paxNameAndUserName, message, message );
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getSubmitterUserId()
  {
    return submitterUserId;
  }

  public void setSubmitterUserId( String submitterUserId )
  {
    this.submitterUserId = submitterUserId;
  }

  public String getSubmitterNodeId()
  {
    return submitterNodeId;
  }

  public void setSubmitterNodeId( String submitterNodeId )
  {
    this.submitterNodeId = submitterNodeId;
  }

  public String getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( String proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public String getRecipientUserId()
  {
    return recipientUserId;
  }

  public void setRecipientUserId( String recipientUserId )
  {
    this.recipientUserId = recipientUserId;
  }

  public String getRecipientNodeId()
  {
    return recipientNodeId;
  }

  public void setRecipientNodeId( String recipientNodeId )
  {
    this.recipientNodeId = recipientNodeId;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  public String getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( String certificateId )
  {
    this.certificateId = certificateId;
  }

  public String getCardId()
  {
    return cardId;
  }

  public void setCardId( String cardId )
  {
    this.cardId = cardId;
  }

  public String getSendCopyToMe()
  {
    return sendCopyToMe;
  }

  public void setSendCopyToMe( String sendCopyToMe )
  {
    this.sendCopyToMe = sendCopyToMe;
  }

  public String getSendCopyToManager()
  {
    return sendCopyToManager;
  }

  public void setSendCopyToManager( String sendCopyToManager )
  {
    this.sendCopyToManager = sendCopyToManager;
  }

  public String getCopyOthers()
  {
    return copyOthers;
  }

  public void setCopyOthers( String copyOthers )
  {
    this.copyOthers = copyOthers;
  }

  public String getSendCopyToOthers()
  {
    return sendCopyToOthers;
  }

  public void setSendCopyToOthers( String sendCopyToOthers )
  {
    this.sendCopyToOthers = sendCopyToOthers;
  }

  public String getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( String awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public String getCalculatorScore()
  {
    return calculatorScore;
  }

  public void setCalculatorScore( String calculatorScore )
  {
    this.calculatorScore = calculatorScore;
  }

  public String getLevelId()
  {
    return levelId;
  }

  public void setLevelId( String levelId )
  {
    this.levelId = levelId;
  }

  public String getProductId()
  {
    return productId;
  }

  public void setProductId( String productId )
  {
    this.productId = productId;
  }

  public String getRecipientCountryCode()
  {
    return recipientCountryCode;
  }

  public void setRecipientCountryCode( String recipientCountryCode )
  {
    this.recipientCountryCode = recipientCountryCode;
  }

  public String getBudgetId()
  {
    return budgetId;
  }

  public void setBudgetId( String budgetId )
  {
    this.budgetId = budgetId;
  }

  public String getClaimElements()
  {
    return claimElements;
  }

  public void setClaimElements( String claimElements )
  {
    this.claimElements = claimElements;
  }

  public String getCalculatorResponses()
  {
    return calculatorResponses;
  }

  public void setCalculatorResponses( String calculatorResponses )
  {
    this.calculatorResponses = calculatorResponses;
  }

  public String getTeamId()
  {
    return teamId;
  }

  public void setTeamId( String teamId )
  {
    this.teamId = teamId;
  }

  public String getTriggerName()
  {
    return triggerName;
  }

  public void setTriggerName( String triggerName )
  {
    this.triggerName = triggerName;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setMultimediaService( MultimediaService multimediaService )
  {
    this.multimediaService = multimediaService;
  }

  public void setMerchLevelService( MerchLevelService merchLevelService )
  {
    this.merchLevelService = merchLevelService;
  }

  public void setBudgetMasterService( BudgetMasterService budgetMasterService )
  {
    this.budgetMasterService = budgetMasterService;
  }

  public void setClaimFormDefinitionService( ClaimFormDefinitionService claimFormDefinitionService )
  {
    this.claimFormDefinitionService = claimFormDefinitionService;
  }

  public void setCalculatorService( CalculatorService calculatorService )
  {
    this.calculatorService = calculatorService;
  }

  public ScheduledRecognitionService getScheduledRecognitionService()
  {
    return scheduledRecognitionService;
  }

  public void setScheduledRecognitionService( ScheduledRecognitionService scheduledRecognitionService )
  {
    this.scheduledRecognitionService = scheduledRecognitionService;
  }

  public void setEmailService( EmailService emailService )
  {
    this.emailService = emailService;
  }

  public EmailService getEmailService()
  {
    return emailService;
  }

  public void setCelebrationService( CelebrationService celebrationService )
  {
    this.celebrationService = celebrationService;
  }

  public String getAnniversaryNumberOfDays()
  {
    return anniversaryNumberOfDays;
  }

  public void setAnniversaryNumberOfDays( String anniversaryNumberOfDays )
  {
    this.anniversaryNumberOfDays = anniversaryNumberOfDays;
  }

  public String getAnniversaryNumberOfYears()
  {
    return anniversaryNumberOfYears;
  }

  public void setAnniversaryNumberOfYears( String anniversaryNumberOfYears )
  {
    this.anniversaryNumberOfYears = anniversaryNumberOfYears;
  }

  public String getCelebrationManagerMessageId()
  {
    return celebrationManagerMessageId;
  }

  public void setCelebrationManagerMessageId( String celebrationManagerMessageId )
  {
    this.celebrationManagerMessageId = celebrationManagerMessageId;
  }

}
