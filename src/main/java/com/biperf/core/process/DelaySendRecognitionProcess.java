/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/DelaySendRecognitionProcess.java,v $
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
import com.biperf.core.domain.promotion.ScheduledRecognition;
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
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.StringUtil;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * DelaySendRecognitionProcess.
 * 
 * This process replaces DelayedSendRecognitionProcess. Instead of passing all the parameter values
 * in the quartz job this process will look up the parameter values from the scheduled_recognition table.
 * 
 * DelayedSendRecognitionProcess cannot be deleted because upgrading clients may have pending
 * quartz jobs that have not been executed and will fail if DelayedSendRecognitionProcess does
 * not exist.
 * 
 * @author bethke
 * @since Dec 5, 2014
 * @version 1.0
 */
public class DelaySendRecognitionProcess extends BaseProcessImpl
{
  private static final Log logger = LogFactory.getLog( DelaySendRecognitionProcess.class );

  public static final String PROCESS_NAME = "Delay Send Recognition Process";
  public static final String BEAN_NAME = "delaySendRecognitionProcess";

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
  private String scheduledRecogId;
  private RecognitionClaimSource source;

  // private variables
  private Long submitterUserId;
  private Long recipientUserId;

  public void onExecute()
  {
    try
    {
      ScheduledRecognition scheduledRecognition = scheduledRecognitionService.getScheduledRecognitionById( new Long( scheduledRecogId ) );
      RecognitionPromotion recPromotion = (RecognitionPromotion)promotionService.getPromotionById( scheduledRecognition.getPromotion().getId() );

      if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() && ( recPromotion.isIncludePurl() || recPromotion.isIncludeCelebrations() ) )
      {
        logger.info( "The delayed recognition ' " + scheduledRecognition.getId() + " ' is not able to process as the new DMSA has enabled." );
      }
      else
      {
        try
        {
          submitterUserId = scheduledRecognition.getSender().getId();
          recipientUserId = scheduledRecognition.getRecipient().getId();

          Participant recipient = participantService.getParticipantById( scheduledRecognition.getRecipient().getId() );
          if ( !recipient.isActive() )
          {
            addComment( "Recognition recipient " + recipient.getNameFLNoComma() + " is inactive. Hence the claim cannot be processed." );
            reportClaimFailure( recipient );
          }
          else
          {
            Claim claim = buildClaim( recPromotion, recipient, scheduledRecognition );
            Budget budget = null;
            if ( !StringUtil.isEmpty( scheduledRecognition.getBudgetId() ) )
            {
              budget = budgetMasterService.getBudgetbyId( new Long( scheduledRecognition.getBudgetId() ) );
            }
            claimService.saveandUpdateRecognitionClaim( claim, null, null, false, false, budget );
            addComment( "Recognition claim has been successfully processed." + " scheduledRecogId = " + scheduledRecognition.getId() );

            if ( scheduledRecognition.getTriggerName() != null )
            {
              scheduledRecognitionService.updateScheduleRecognitionWithClaimId( scheduledRecognition.getTriggerName(), claim.getId() );
              addComment( "Scheduled Recognition entry has been successfully updated." + " scheduledRecogId = " + scheduledRecognition.getId() );
            }
          }
        }
        catch( ServiceErrorException se )
        {
          logger.error( "An exception occurred while processing claim " + BEAN_NAME + " (process invocation ID = " + getProcessInvocationId() + ")", se );
          addComment( "An exception occurred while processing claim " + BEAN_NAME + ". See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );

          Participant submitter = participantService.getParticipantById( submitterUserId );
          Participant recipient = participantService.getParticipantById( recipientUserId );
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
    }
    catch( Exception ex )
    {
      logger.error( "An exception occurred while processing claim " + ex );
    }

  }

  private Claim buildClaim( RecognitionPromotion promotion, Participant recipient, ScheduledRecognition scheduledRecognition )
  {
    RecognitionClaim claim = new RecognitionClaim();

    Card selectedCard = null;
    Long selectedCertificateId = null;
    if ( !StringUtil.isEmpty( scheduledRecognition.getCertificateId() ) )
    {
      selectedCertificateId = new Long( scheduledRecognition.getCertificateId() );
    }
    if ( !StringUtil.isEmpty( scheduledRecognition.getCardId() ) )
    {
      selectedCard = multimediaService.getCardById( new Long( scheduledRecognition.getCardId() ) );
    }
    String comments = scheduledRecognition.getSubmitterComments();
    if ( StringUtil.isEmpty( comments ) )
    {
      comments = " ";
    }
    Participant submitter = participantService.getParticipantById( scheduledRecognition.getSender().getId() );
    Node submitterNode = nodeService.getNodeById( new Long( scheduledRecognition.getSubmitterNodeId() ) );
    claim.setSubmitter( submitter );
    claim.setNode( submitterNode );
    String timeZoneID = getUserService().getUserTimeZone( submitter.getId() );
    Date referenceDate = DateUtils.applyTimeZone( new Date(), timeZoneID );
    claim.setSubmissionDate( referenceDate );

    if ( !StringUtil.isEmpty( scheduledRecognition.getProxyUserId() ) )
    {
      claim.setProxyUser( userService.getUserById( new Long( scheduledRecognition.getProxyUserId() ) ) );
    }

    ClaimRecipient claimRecipient = buildClaimRecipient( promotion, recipient, scheduledRecognition );
    claim.addClaimRecipient( claimRecipient );

    claim.setPromotion( promotion );
    claim.setOpen( true );
    claim.setSubmitterComments( comments );
    claim.setSubmitterCommentsLanguageType( userService.getPreferredLanguageFor( submitter ) );
    claim.setCopySender( Boolean.valueOf( scheduledRecognition.getCopySender() ).booleanValue() );
    claim.setCopyManager( Boolean.valueOf( scheduledRecognition.getCopyManager() ).booleanValue() );
    claim.setCopyOthers( Boolean.valueOf( scheduledRecognition.getCopyOthers() ).booleanValue() );
    claim.setSendCopyToOthers( scheduledRecognition.getSendCopyToOthers() );
    claim.setHidePublicRecognition( scheduledRecognition.isHidePublicRecognition() );
    claim.setTeamId( new Long( scheduledRecognition.getTeamId() ) );
    claim.setOwnCardName( scheduledRecognition.getOwnCardName() );
    claim.setCard( selectedCard );
    claim.setCertificateId( selectedCertificateId );
    if ( scheduledRecognition.getSource() != null )
    {
      claim.setSource( scheduledRecognition.getSource() );
    }
    else
    {
      claim.setSource( RecognitionClaimSource.WEB );
    }

    if ( !StringUtil.isEmpty( scheduledRecognition.getBehavior() ) )
    {
      PromotionBehaviorType promotionBehaviorType = PromoRecognitionBehaviorType.lookup( scheduledRecognition.getBehavior() );
      claim.setBehavior( promotionBehaviorType );
    }

    if ( !StringUtils.isEmpty( scheduledRecognition.getAnniversaryNumberOfDays() ) )
    {
      claim.setAnniversaryNumberOfDays( new Integer( scheduledRecognition.getAnniversaryNumberOfDays() ) );
    }

    if ( !StringUtils.isEmpty( scheduledRecognition.getAnniversaryNumberOfYears() ) )
    {
      claim.setAnniversaryNumberOfYears( new Integer( scheduledRecognition.getAnniversaryNumberOfYears() ) );
    }

    CelebrationManagerMessage celebrationManagerMessage = null;
    if ( !StringUtil.isEmpty( scheduledRecognition.getCelebrationManagerMessageId() ) )
    {
      celebrationManagerMessage = celebrationService.getCelebrationManagerMessageById( new Long( scheduledRecognition.getCelebrationManagerMessageId() ) );
    }
    claim.setCelebrationManagerMessage( celebrationManagerMessage );

    String pipeDelim = "[|]";
    String commaDelim = "[,]";

    // add Claim Form Element values to the claim.
    if ( !StringUtil.isEmpty( scheduledRecognition.getClaimElements() ) )
    {
      String[] pipeTokens = scheduledRecognition.getClaimElements().split( pipeDelim );
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
    if ( !StringUtil.isEmpty( scheduledRecognition.getCalculatorResponses() ) )
    {
      String[] pipeTokens = scheduledRecognition.getCalculatorResponses().split( pipeDelim );
      for ( int i = 0; i < pipeTokens.length; i++ )
      {
        String calculatorResponseValue = pipeTokens[i];
        String[] commaTokens = calculatorResponseValue.split( commaDelim );

        CalculatorResponse calculatorResponse = new CalculatorResponse();
        calculatorResponse.setCriterion( calculatorService.getCalculatorCriterionByIdWithAssociations( new Long( commaTokens[0] ), null ) );
        if ( !StringUtils.isEmpty( commaTokens[1] ) )
        {
          calculatorResponse.setCriterionWeight( new Integer( commaTokens[1] ) );
        }
        CalculatorCriterionRating selectedRating = calculatorService.getCriterionRatingById( new Long( commaTokens[2] ) );
        calculatorResponse.setSelectedRating( selectedRating );
        calculatorResponse.setRatingValue( selectedRating.getRatingValue() );
        calculatorResponse.setClaim( claim );
        claim.addCalculatorResponse( calculatorResponse );
      }
    }

    return claim;
  }

  private ClaimRecipient buildClaimRecipient( RecognitionPromotion promotion, Participant recipient, ScheduledRecognition scheduledRecognition )
  {
    ClaimRecipient claimRecipient = new ClaimRecipient();

    if ( StringUtils.isNotBlank( scheduledRecognition.getAwardQuantity() ) && StringUtils.isNumeric( scheduledRecognition.getAwardQuantity() ) )
    {
      if ( recipient.getOptOutAwards() )
      {
        claimRecipient.setAwardQuantity( 0L );
      }
      else
      {
        claimRecipient.setAwardQuantity( new Long( scheduledRecognition.getAwardQuantity() ) );
      }
    }
    if ( StringUtils.isNotBlank( scheduledRecognition.getCalculatorScore() ) && StringUtils.isNumeric( scheduledRecognition.getCalculatorScore() ) )
    {
      claimRecipient.setCalculatorScore( new Long( scheduledRecognition.getCalculatorScore() ) );
    }
    claimRecipient.setPromoMerchCountry( promotion.getPromoMerchCountryForCountryCode( scheduledRecognition.getRecipientCountryCode() ) );
    if ( !StringUtil.isEmpty( scheduledRecognition.getLevelId() ) )
    {
      claimRecipient.setPromoMerchProgramLevel( merchLevelService.getPromoMerchProgramLevelById( new Long( scheduledRecognition.getLevelId() ) ) );
    }
    if ( !StringUtil.isEmpty( scheduledRecognition.getProductId() ) )
    {
      claimRecipient.setProductId( scheduledRecognition.getProductId() );
    }

    claimRecipient.setRecipient( recipient );
    if ( !StringUtil.isEmpty( scheduledRecognition.getRecipientNodeId() ) )
    {
      claimRecipient.setNode( nodeService.getNodeById( new Long( scheduledRecognition.getRecipientNodeId() ) ) );
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

  public String getScheduledRecogId()
  {
    return scheduledRecogId;
  }

  public void setScheduledRecogId( String scheduledRecogId )
  {
    this.scheduledRecogId = scheduledRecogId;
  }

  public Long getSubmitterUserId()
  {
    return submitterUserId;
  }

  public void setSubmitterUserId( Long submitterUserId )
  {
    this.submitterUserId = submitterUserId;
  }

  public Long getRecipientUserId()
  {
    return recipientUserId;
  }

  public void setRecipientUserId( Long recipientUserId )
  {
    this.recipientUserId = recipientUserId;
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

  public RecognitionClaimSource getSource()
  {
    return source;
  }

  public void setSource( RecognitionClaimSource source )
  {
    this.source = source;
  }

}
