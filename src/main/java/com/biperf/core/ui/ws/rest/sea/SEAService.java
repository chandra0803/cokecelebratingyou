
package com.biperf.core.ui.ws.rest.sea;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.biperf.core.domain.budget.Budget;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBehavior;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.country.CountryService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ParticipantSearchCriteria;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.PublicRecognitionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.ws.rest.sea.dto.RecognitionRequestView;
import com.biperf.core.ui.ws.rest.sea.dto.RecognitionResponseView;
import com.biperf.core.ui.ws.rest.sea.util.EmailResponseUtil;
import com.biperf.core.ui.ws.rest.sea.util.HashTagParser;
import com.biperf.core.utils.BudgetUtils;
import com.objectpartners.cms.util.CmsUtil;
import com.objectpartners.cms.util.ContentReaderManager;

// NOTE: the prefix for the Simple Email Application "sea" along with the supported version "1".
// The version SEA requests can be updated via the SEA registerOrganization method from this application
@Path( "sea/1" )
@Component
public class SEAService extends BaseService
{
  private final static Log log = LogFactory.getLog( SEAService.class );

  @POST
  @Path( "/submitRecognition.biws" )
  @Consumes( MediaType.APPLICATION_JSON )
  @Produces( { MediaType.APPLICATION_JSON } )
  public RecognitionResponseView submitRecognition( RecognitionRequestView request, @Context UriInfo uriInfo )
  {
    RecognitionResponseView response = buildRecognitionResponseView( request, uriInfo );
    RecognitionPromotion promotion = validateEmailEnabledPromotion( response );
    try
    {
      boolean isConfirmationEnabled = !( promotion != null ? promotion.isEmailConfirmEnabled() : true ) ;
      validate( request, response, isConfirmationEnabled, promotion, false );
    }
    catch( Throwable t )
    {
      log.error( t.getMessage() + " Uri: " + uriInfo.getRequestUri().toString(), t );
      response.setCode( 99 );
      response.buildStack( t );
    }
    return response;
  }

  @POST
  @Path( "/confirmRecognition.biws" )
  @Consumes( MediaType.APPLICATION_JSON )
  @Produces( { MediaType.APPLICATION_JSON } )
  public RecognitionResponseView confirmRecognition( RecognitionRequestView request, @Context UriInfo uriInfo )
  {
    RecognitionResponseView response = buildRecognitionResponseView( request, uriInfo );
    RecognitionPromotion promotion = validateEmailEnabledPromotion( response );
    try
    {
      validate( request, response, true, promotion, true );
    }
    catch( Throwable t )
    {
      log.error( t.getMessage() + " Uri: " + uriInfo.getRequestUri().toString(), t );
      response.setCode( 99 );
      response.buildStack( t );
    }
    return response;
  }

  @GET
  @Path( "/ping.biws" )
  public String ping()
  {
    return "SEA Service Ping Response";
  }

  private void validate( RecognitionRequestView request, RecognitionResponseView response, boolean forceConfirmProcess, RecognitionPromotion promotion, boolean isConfirm )
  {
    // check promotion first, no need to validate further if there isn't a SEA promotion setup
    // Then validate the participants
    // Then validate hash tags
    response.setProgramEmailAddress( getSystemVariableService().getPropertyByName( SystemVariableService.SEA_EMAIL_ACCOUNT ).getStringVal() );
    Participant sender = null;
    if ( null != promotion )
    {
      response.setEmailEnabledPromotion( true );// email enabled by the fact that we found it in the
                                                // database
      response.setPromotionName( promotion.getName() );
      sender = validateSender( request, response, promotion );

      boolean validRecipients = false;
      boolean validBudget = false;
      boolean validBehaviors = false;
      boolean endTagFound = false;
      boolean validMessage = false;
      boolean validRecipient = false;

      if ( sender != null )
      {
        validBudget = validateBudget( request, response, promotion, sender );
        validBehaviors = validateBehaviors( request, response, promotion, forceConfirmProcess );
        endTagFound = checkEndTag( request, response, promotion, isConfirm );
        validMessage = isValidMessage( request, response );
        validRecipients = validateRecipients( request, response, promotion );
        validRecipient = !response.getValidRecipients().isEmpty();
      }

      if ( response.isPointsAvailableToEmail() )
      {
        // in points recog with all recipients valid, "validRecipients"
        if ( sender == null || !validBehaviors || !endTagFound || !validMessage || !validBudget || !validRecipients || !validRecipient )
        {
          response.setCode( 2 );
        }
      }
      else
      {
        // In no points recog at least one valid recipient, "validRecipient"
        if ( sender == null || !validBehaviors || !endTagFound || !validMessage || !validRecipient )
        {
          response.setCode( 2 );
        }
      }
      
      /*
       * Send the recognition if all is valid, and either the confirmation has already been sent or the
       * promotion doesn't use confirmation emails.  Furthermore, if the recognition does not have points, 
       * still create the recognition for all of the VALID recipients.  If points are in recognition, then ALL 
       * participants need to be valid.
       */
      boolean validToSendToValidRecipients = ( response.isPointsAvailableToEmail() ) ? response.isValid() : response.getValidRecipients().size() > 0 ;
      if ( ( forceConfirmProcess || isConfirm ) && validToSendToValidRecipients && response.getCode() == 1 )
      {
        if ( !response.isPointsAvailableToEmail() )
        {
          request.setPoints( 0 );
        }
        sendRecognition( sender, promotion, request, response, isConfirm );
      }
    }
    else
    {
      response.setEmailEnabledPromotion( false );
      response.setCode( 99 );
    }

    EmailResponseUtil emailUtil = new EmailResponseUtil();
    try
    {
      // apply the correct Locale....
      ContentReaderManager.getContentReader().setLocale( buildParticipantLocale( sender ) );
      // YUCK!!!
      // if there are errors, but the confirmation email is disabled, force
      // the code to at least send the corrections response message
      if ( response.getCode()==2 && forceConfirmProcess )
      {
        // bypass SEA status - if not, we get one "extra" response we don't want
        if( request.getMessageStatus()!=null && request.getMessageStatus().equalsIgnoreCase( "reply_affirmed" ) )
          emailUtil.buildConfirmRecognitionMessageBodyContent( request, response );
        else
          emailUtil.buildSubmitRecognitionMessageBodyContent( request, response );
      }
      else if ( isConfirm || forceConfirmProcess )
        emailUtil.buildConfirmRecognitionMessageBodyContent( request, response );
      else
        emailUtil.buildSubmitRecognitionMessageBodyContent( request, response );
    }
    finally
    {
      // make sure to remove the ThreadLocal variable
      ContentReaderManager.removeContentReader();
    }
  }

  private void sendRecognition( Participant sender, RecognitionPromotion promotion, RecognitionRequestView request, RecognitionResponseView response, boolean isConfirm )
  {
    try
    {
      List<Participant> recipients = new ArrayList<Participant>();
      for ( String validRecipient : response.getValidRecipients() )
      {
        List<Participant> validRecipients = searchForParticipant( validRecipient );
        if ( validRecipients != null && validRecipients.size() > 0 )
          recipients.add( validRecipients.get( 0 ) );
      }
      String comments = "";
      if ( isConfirm )
      {
        comments = buildFilteredComments( request ) ;
      }
      else
      {
        comments = HashTagParser.getMessage( request.getInitialComments() );
        comments = HashTagParser.removeHashtags( comments ) ;
      }
      String behavior = buildBehaviorCodeFromAbbreviation( promotion, request.getBehaviorHashTag().getName() ) ;
      List<Long> claimIds = getPublicRecognitionService().submitRecognitionWithPubRecAddPoints( sender,
                                                                                           recipients,
                                                                                           promotion,
                                                                                           comments,
                                                                                           behavior,// can be null
                                                                                           request.getPoints());
      response.setClaimIds( claimIds );
      response.setCode( 1 );
    }
    catch( Exception e )
    {
      log.error( e.getMessage() );
      response.setCode( 99 );
      response.buildStack( e );
    }
  }

  /*
   * This method takes the hashtag - which should be mapped to the picklist item's abbreviation, and returns the
   * actual code associateed with the picklist item for future lookup in the service method.
   */
  private String buildBehaviorCodeFromAbbreviation( RecognitionPromotion promotion, String abbreviation )
  {
    if( StringUtils.isEmpty( abbreviation ) )
    {
      return null ;
    }
    else if ( promotion.isBehaviorActive() )
    {
      for( PromotionBehavior promoBehavior: promotion.getPromotionBehaviors() )
      {
        if( promoBehavior.getPromotionBehaviorType().getAbbr().equals( abbreviation ) )
        {
          return promoBehavior.getPromotionBehaviorType().getCode() ;
        }
      }
    }
    
    return null ;
  }
  
  /*
   * The purpose of this method is to build the actual comments - which means stripping out any point tags, behaviors tags, and #end tag.
  */
  private String buildFilteredComments( RecognitionRequestView request )
  {
    String missingEndTagMessage = getMessageService().getMessageByCMAssetCode( MessageService.SEA_EMAIL_MISSING_END_TAG ).getI18nPlainTextBody();
    String initialComments = HashTagParser.removeHashtagsExceptEndTags( request.getInitialComments() );
    String latestComments = request.getLatestComments().replaceAll( missingEndTagMessage, "" );
    latestComments = HashTagParser.removeHashtagsExceptEndTags( HashTagParser.removeHashtagsExceptEndTags( latestComments ) );
    return HashTagParser.getMessage( initialComments, latestComments );
  }
    
  @SuppressWarnings( "unchecked" )
  private RecognitionPromotion validateEmailEnabledPromotion( RecognitionResponseView response )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PRIMARY_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.SECONDARY_AUDIENCES ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.PROMOTION_BEHAVIORS ) );

    //RecognitionPromotion promotion = getPromotionService().getEmailEnabledPromotion( associationRequestCollection );
    RecognitionPromotion promotion =  null;
    return promotion;
  }

  private Participant validateSender( RecognitionRequestView request, RecognitionResponseView response, RecognitionPromotion promotion )
  {
    // if the sender is invalid, don't need to check the rest
    List<Participant> senderList = searchForParticipant( request.getSenderEmail() );
    if ( senderList.isEmpty() )
    {
      response.setSenderNotFound( true );
      return null;
    }
    else if ( senderList.size() > 1 )
    {
      response.setSenderNotUnique( true );
      return null;
    }

    if ( !isSenderInAudience( senderList.get( 0 ), promotion ) )
    {
      response.setSenderNotInAudience( true );
      return null;
    }
    else
    {
      response.setFirstName( senderList.get( 0 ).getFirstName() );
      response.setLastName( senderList.get( 0 ).getLastName() );
      return senderList.get( 0 );
    }
  }

  private boolean validateRecipients( RecognitionRequestView request, RecognitionResponseView response, RecognitionPromotion promotion )
  {
    // remove sender email from recipient email list
    Set<String> recipientEmails = request.getRecipientEmails();
    for ( Iterator<String> recipientEmail = recipientEmails.iterator(); recipientEmail.hasNext(); )
    {
      if ( recipientEmail.next().equalsIgnoreCase( request.getSenderEmail() ) )
      {
        response.setSameSenderAndRecipient( true );
      }
    }

    // validate each of the recipients and check their eligibilty in the promotion
    boolean allRecipientsValid = true;
    for ( String recipientEmail : recipientEmails )
    {
      // first, check the not unique list, if the current recipient is already in this list,
      // we already know it's a dupe...
      if ( response.getRecipientsNotUnique().contains( recipientEmail ) )
      {
        continue;
      }
      // Don't collect the fully hydrated user, as this could potentially return thousands
      // of users and break things.
      Long participantCount = searchForParticipantCount( recipientEmail );
      if ( participantCount.intValue() == 0 )
      {
        response.getInvalidRecipients().add( recipientEmail );
        allRecipientsValid = false;
      }
      else if ( participantCount.intValue() > 1 )
      {
        response.getRecipientsNotUnique().add( recipientEmail );
        allRecipientsValid = false;
      }
      else
      {
        // find full participant by email
        List<Participant> participantList = searchForParticipant( recipientEmail );
        if ( !isRecipientInAudience( participantList.get( 0 ), promotion ) )
        {
          response.getRecipientsNotInAudience().add( recipientEmail );
          allRecipientsValid = false;
        }
        else
        {
          if ( !request.getSenderEmail().equalsIgnoreCase( recipientEmail ) )
          {
            response.getValidRecipients().add( recipientEmail );
          }
        }
      }
    }
    return recipientEmails.size() == 1 && response.isSameSenderAndRecipient() ? false : allRecipientsValid;
  }
  
  private boolean validateBudget( RecognitionRequestView request, RecognitionResponseView response, RecognitionPromotion promotion, Participant sender )
  {
    if ( promotion.isPublicRecBudgetUsed() )
    {
      Budget budget = getPromotionService().getPublicRecognitionBudget( promotion.getId(), sender.getId(), sender.getPrimaryUserNode().getNode().getId() );
      
      if( budget==null )
        response.setPointsAvailable( 0 );
      else
      {
        response.setPointsAvailable( budget.getCurrentValue().intValue() );
        response.setBudgetPromotionName( budget.getBudgetSegment().getBudgetMaster().getBudgetName() );
      }
    }

    response.setPointsAvailableToEmail( request.getPoints()>0 );
    response.setPointsAvailableToPromotion( promotion.isAllowPublicRecognitionPoints() );
    
    // points are optional, so validate only if points used for promo & user included in email.
    if ( response.isPointsAvailableToPromotion() && response.isPointsAvailableToEmail() )
    {
      if ( promotion.isPublicRecogAwardAmountTypeFixed() )
      {
        response.setPromotionFixedPoints( true );
        response.setFixedPoints( promotion.getPublicRecogAwardAmountFixed().intValue() );
        if ( request.getPoints() != 0 && request.getPoints() != promotion.getPublicRecogAwardAmountFixed().intValue() )
        {
          response.setFixedPointsDoesntMatch( true );
          return false;
        }
      }
      else
      {
        response.setPromotionPointRange( true );
        response.setPointRangeMin( promotion.getPublicRecogAwardAmountMin().intValue() );
        response.setPointRangeMax( promotion.getPublicRecogAwardAmountMax().intValue() );
        if ( request.getPoints() != 0 && ( request.getPoints() < promotion.getPublicRecogAwardAmountMin().intValue() || request.getPoints() > promotion.getPublicRecogAwardAmountMax().intValue() ) )
        {
          response.setPointsOutOfRange( true );
          return false;
        }
      }
    }

    if ( promotion.isPublicRecBudgetUsed() )
    {
      if ( response.getPointsAvailable() == 0 )
      {
        response.setNoBudget( true );
        return false;
      }

      int totalBudgetToSend = 0;
      List<Participant> senderParticipant = searchForParticipant( request.getSenderEmail() );
      if ( senderParticipant != null && senderParticipant.size() > 0 )
      {
        final BigDecimal SENDER_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( searchForParticipant( request.getSenderEmail() ).get( 0 ).getPrimaryCountryCode() );
        for ( String recipientEmail : request.getRecipientEmails() )
        {
          List<Participant> recipientParticipant = searchForParticipant( recipientEmail );
          if ( recipientParticipant != null && recipientParticipant.size() > 0 )
          {
            final BigDecimal RECIPIENT_MEDIA_VALUE = getCountryService().getBudgetMediaValueByCountryCode( searchForParticipant( recipientEmail ).get( 0 ).getPrimaryCountryCode() );
            totalBudgetToSend = totalBudgetToSend + BudgetUtils.applyMediaConversion( new BigDecimal( request.getPoints() ), SENDER_MEDIA_VALUE, RECIPIENT_MEDIA_VALUE ).intValue();
          }
        }
      }
      if ( response.getPointsAvailable() < totalBudgetToSend )
      {
        response.setNotEnoughBudget( true );
        return false;
      }
    }

    return true;
  }

  private boolean validateBehaviors( RecognitionRequestView request, RecognitionResponseView response, RecognitionPromotion promotion, boolean confirmProcess )
  {
    response.setBehaviorRequired( promotion.isBehaviorRequired() );

    // populate valid behaviors
    for ( PromotionBehavior behavior : promotion.getPromotionBehaviors() )
    {
      response.getValidBehaviors().add( behavior.getPromotionBehaviorType().getAbbr() );
    }

    boolean validBehavior = false;
    if ( request.getBehaviorHashTag() != null && !StringUtils.isEmpty( request.getBehaviorHashTag().getName() ) )
    {
      for ( PromotionBehavior behavior : promotion.getPromotionBehaviors() )
      {
        if ( behavior.getPromotionBehaviorType().getAbbr().equalsIgnoreCase( request.getBehaviorHashTag().getName() ) )
        {
          validBehavior = true;
          break;
        }
      }
    }

    if ( !validBehavior )
    {
      if ( request.getBehaviorHashTag() != null && !StringUtils.isEmpty( request.getBehaviorHashTag().getName() ) )
      {
        response.setInvalidBehavior( true );
      }
      if ( response.isBehaviorRequired() && ( request.getBehaviorHashTag() == null || StringUtils.isEmpty( request.getBehaviorHashTag().getName() ) ) )
      {
        response.setMissingBehavior( true );
        return false;
      }
      if ( confirmProcess && !response.isInvalidBehavior() )
        return true;
      else
        return false;
    }

    return true;
  }

  private boolean checkEndTag( RecognitionRequestView request, RecognitionResponseView response, RecognitionPromotion promotion, boolean isConfirm )
  {
    if ( isConfirm )
    {
      String missingEndTagMessage = getMessageService().getMessageByCMAssetCode( MessageService.SEA_EMAIL_MISSING_END_TAG ).getI18nPlainTextBody();
      String message = request.getLatestComments().replaceAll( missingEndTagMessage, "" );
      response.setEndTagFound( HashTagParser.isEndTagAvailable( message ) );
    }
    else
    {
      response.setEndTagFound( HashTagParser.isEndTagAvailable( request.getInitialComments() ) );
    }
    return response.isEndTagFound();
  }
  
  private boolean isValidMessage( RecognitionRequestView request, RecognitionResponseView response )
  {
    boolean messageEmpty = ( StringUtils.isEmpty( request.getInitialComments() ) );
    response.setMessageEmpty( messageEmpty );
    return !messageEmpty ;
  }

  private boolean isValidRecipient( RecognitionRequestView request, RecognitionResponseView response )
  {
    Set<String> recipientEmails = request.getRecipientEmails();
    for ( Iterator<String> recipientEmail = recipientEmails.iterator(); recipientEmail.hasNext(); )
    {
      if ( recipientEmail.next().equalsIgnoreCase( request.getSenderEmail() ) )
      {
        recipientEmail.remove();
      }
    }
    if ( recipientEmails.size() == 0 )
      response.setSameSenderAndRecipient( true );
    return recipientEmails.size() >= 0;
  }
  
  private Locale buildParticipantLocale( Participant sender )
  {
    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if( sender!=null )
    {
      if ( sender.getLanguageType() != null )
      {
        localeCode = sender.getLanguageType().getCode();
      }
    }
    return CmsUtil.getLocale( localeCode );
  }

  private boolean isSenderInAudience( Participant participant, Promotion promotion )
  {
    return getAudienceService().isParticipantInPrimaryAudience( promotion, participant );
  }

  private boolean isRecipientInAudience( Participant participant, Promotion promotion )
  {
    return getAudienceService().isParticipantInSecondaryAudience( promotion, participant );
  }

  @SuppressWarnings( "unchecked" )
  private List<Participant> searchForParticipant( String emailAddress )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.ADDRESS ) );
    ParticipantSearchCriteria search = new ParticipantSearchCriteria();
    search.setEmailAddr( emailAddress );
    return getParticipantService().searchParticipantWithAssociations( search, false, associationRequestCollection );
  }
  
  private Long searchForParticipantCount( String emailAddress )
  {
    ParticipantSearchCriteria search = new ParticipantSearchCriteria();
    search.setEmailAddr( emailAddress );
    search.setActive( true );
    return getParticipantService().getPaxCountBasedOnEmailCriteria( search );
  }

  private RecognitionResponseView buildRecognitionResponseView( RecognitionRequestView request, UriInfo uriInfo )
  {
    RecognitionResponseView response = new RecognitionResponseView();
    response.setProgramName( getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    // log URL
    response.setUrl( uriInfo.getRequestUri().toString() );
    return response;
  }
  
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME ) ; 
  }
  
  private AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }
  
  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
  
  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }
  
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
  
  private CountryService getCountryService()
  {
    return (CountryService)getService( CountryService.BEAN_NAME );
  }
  
  private PublicRecognitionService getPublicRecognitionService()
  {
    return (PublicRecognitionService)getService( PublicRecognitionService.BEAN_NAME );
  }
  
  
}
