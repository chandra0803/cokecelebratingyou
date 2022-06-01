
package com.biperf.core.ui.recognition;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.StringEscapeUtils;

import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.NominationClaimBehaviors;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.enums.PromoNominationBehaviorType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.calculator.CalculatorService;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.claim.RecognitionClaimRecipient;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.claim.ClaimElementForm;
import com.biperf.core.ui.nomination.NominationSubmitForm;
import com.biperf.core.ui.recognition.state.CalculatorResult;
import com.biperf.core.ui.recognition.state.PurlContributorBean;
import com.biperf.core.ui.recognition.state.RecipientBean;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataBehaviorValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean.ParticipantValueBean;

public class RecognitionClaimSubmissionFactory
{
  private RecognitionClaimSubmissionFactory()
  {
    //
  }

  public static RecognitionClaimSubmission buildFrom( RecognitionClaimSource source, SendRecognitionForm form )
  {
    final Long USER_ID = UserManager.getUserId();
    RecognitionClaimSubmission submission = new RecognitionClaimSubmission( source, USER_ID, form.getNodeId(), form.getPromotionId() );

    if ( UserManager.getUser().isDelegate() )
    {
      Long proxyUserId = UserManager.getUser().getOriginalAuthenticatedUser().getUserId();
      submission.setProxyUserId( proxyUserId );
    }
    
 // Client customization for WIP #39189 starts
    for ( TcccClaimFileValueBean claimUpload : form.getClaimUploads() )
    {
      submission.addClaimUploadDocument( claimUpload.getUrl(), claimUpload.getDescription() );
    }
    // Client customization for WIP #39189 ends

    // add recipients
    for ( RecipientBean recipient : form.getRecipients() )
    {
        RecognitionClaimRecipient rcp = null;
          // Client customization for WIP #42701 starts
      if ( Objects.nonNull( recipient.getCurrency() ) )
      {
        String divisionKey = getUserService().getUserDivisionKeyCharValue( recipient.getUserId() );
        rcp = submission.addRecognitionClaimRecipient( recipient.getUserId(),
                                                       recipient.getNodeId(),
                                                       recipient.getAwardQuantity(),
                                                       recipient.getAwardLevel(),
                                                       recipient.getCountryCode(),
                                                       recipient.isOptOutAwards(),
                                                       recipient.getCurrency(),
                                                       divisionKey );
      }
      else
      {
        rcp = submission.addRecognitionClaimRecipient( recipient.getUserId(),
                                                       recipient.getNodeId(),
                                                       recipient.getAwardQuantity(),
                                                       recipient.getAwardLevel(),
                                                       recipient.getCountryCode());
      }
      // Client customization for WIP #42701 ends
      // add calculator responses, if any
      for ( CalculatorResult cr : recipient.getCalculatorResults() )
      {
        rcp.addCalculatorResponse( cr.getCriteriaId(), cr.getRatingId() );
        rcp.addToCalculatorScore( getCalculatorService().getCalculatorRatingScore( form.getPromotionId(), cr.getCriteriaId(), cr.getCriteriaRating().intValue() ) );
      }
    }

    // nomination team name
    submission.setTeamName( form.getTeamName() );

    // comments and behavior
    String formattedComment = null;

    if ( Objects.nonNull( form.getComments() ) )
    {
      formattedComment = removeFormatting( form.getComments() );
    }
    submission.setComments( formattedComment );
    submission.setBehavior( form.getSelectedBehavior() );

    // cards/certificates
    if ( SendRecognitionForm.CARD_TYPE_CERTIFICATE.equals( form.getCardType() ) && form.getCardId() != null )
    {
      submission.setCertificateId( form.getCardId() );
    }
    else if ( SendRecognitionForm.CARD_TYPE_CARD.equals( form.getCardType() ) && form.getCardId() != null )
    {
      submission.setCardId( form.getCardId() );
    }
    else if ( SendRecognitionForm.CARD_TYPE_UPLOAD.equals( form.getCardType() ) )
    {
      // Image upload. Video does not use 'ownCardName'
      boolean isImageUpload = StringUtil.isEmpty( form.getVideoUrl() );
      if ( isImageUpload )
      {
        submission.setOwnCardName( form.getCardUrl() );
      }
      else
      {
        submission.setVideoUrl( form.getVideoUrl() );
        submission.setVideoImageUrl( form.getVideoImageUrl() );
      }
    }
    else if ( SendRecognitionForm.CARD_TYPE_DRAWING.equals( form.getCardType() ) )
    {
      submission.setOwnCardName( form.getCardUrl() );
    }
    else if ( "none".equals( form.getCardType() ) )
    {
      // clear out the state
      form.setCardUrl( "" );
      form.setCardId( null );
    }

    // copies
    submission.setCopySender( form.getSendCopyToMe() );
    submission.setCopyManager( form.getSendCopyToManager() );
    submission.setCopyOthers( form.getSendCopyToOthers() );

    // private recognition
    submission.setPrivateRecognition( form.isMakeRecPrivate() );

    // celebrate anniversary in days or years
    if ( form.getAnniversaryDays() != null )
    {
      submission.setAnniversaryDays( form.getAnniversaryDays() );
    }
    if ( form.getAnniversaryYears() != null )
    {
      submission.setAnniversaryYears( form.getAnniversaryYears() );
    }

    // send date
    submission.setRecipientSendDate( form.getRecipientSendDate() );

    // purl contributors
    for ( PurlContributorBean contributor : form.getContributors() )
    {
      // TODO - need to pass avatar URL.
      submission.addPurlContributor( contributor.getId(), contributor.getFirstName(), contributor.getLastName(), null, contributor.getEmail(), contributor.getContribType(), true );
    }

    if ( form.getClaimElementsList() != null )
    {
      List<ClaimElementForm> sortedList = form.getSortedClaimElementsList( form.getClaimElementsList() );
      ClaimFormDefinitionService claimFormDefinitionService = getClaimFormDefinitionService();
      for ( ClaimElementForm claimElementForm : sortedList )
      {
        claimElementForm.setClaimFormStepElement( claimFormDefinitionService.getClaimFormStepElementById( claimElementForm.getClaimFormStepElementId() ) );
        ClaimElement claimElement = claimElementForm.toDomainObject();
        submission.getClaimElements().add( claimElement );
      }
    }

    submission.setClaimId( form.getClaimId() );
    submission.setCustomElements( form.getCustomElements() );
    return submission;
  }

  public static RecognitionClaimSubmission buildNominationSubmit( RecognitionClaimSource source, NominationSubmitForm form )
  {
    final Long USER_ID = UserManager.getUserId();
    RecognitionClaimSubmission submission = new RecognitionClaimSubmission( source, USER_ID, form.getNodeId(), form.getPromotionId() );

    if ( UserManager.getUser().isDelegate() )
    {
      Long proxyUserId = UserManager.getUser().getOriginalAuthenticatedUser().getUserId();
      submission.setProxyUserId( proxyUserId );
    }
    // add recipients
    submission.setParticipants(form.getParticipants()); 
    submission.setClaimId( form.getClaimId() );
    submission.setNomSubmitDataPromotionValueBean( form.getPromotion() );
    submission.setCustomElements( form.getCustomElements() );
    return submission;
  }

  /**
   * Get a form with only information needed for nomination submission's behavior step
   */
  public static RecognitionClaimSubmission buildForNomBehaviors( SendRecognitionForm form )
  {
    final Long USER_ID = UserManager.getUserId();
    RecognitionClaimSubmission submission = new RecognitionClaimSubmission( RecognitionClaimSource.WEB, USER_ID, form.getNodeId(), form.getPromotionId() );

    // Behaviors step requires claim ID and of course, behavior
    submission.setClaimId( form.getClaimId() );

    List<NominationClaimBehaviors> behaviors = new ArrayList<NominationClaimBehaviors>();

    for ( NominationSubmitDataBehaviorValueBean behavior : ( (NominationSubmitForm)form ).getPromotion().getBehaviors() )
    {
      if ( behavior.isSelected() )
      {
        behaviors.add( new NominationClaimBehaviors( PromoNominationBehaviorType.lookup( behavior.getId() ) ) );
      }
    }
    submission.setNominationBehaviors( behaviors );

    return submission;
  }

  /**
   * Get a form with only information needed for nomination submission's ecard step
   */
  public static RecognitionClaimSubmission buildForNomEcards( NominationSubmitForm form )
  {
    final Long USER_ID = UserManager.getUserId();
    RecognitionClaimSubmission submission = new RecognitionClaimSubmission( RecognitionClaimSource.WEB, USER_ID, form.getNodeId(), form.getPromotionId() );

    // Ecard step requires claim ID
    submission.setClaimId( form.getClaimId() );

    NominationSubmitDataPromotionValueBean nominationsPromotion = form.getPromotion();
    // The card information varies based on type
    submission.setCardType( nominationsPromotion.getCardType() );
    if ( NominationSubmitForm.CARD_TYPE_CERTIFICATE.equals( nominationsPromotion.getCardType() ) && nominationsPromotion.getCardId() != null )
    {
      submission.setCertificateId( nominationsPromotion.getCardId() );
    }
    else if ( NominationSubmitForm.CARD_TYPE_CARD.equals( nominationsPromotion.getCardType() ) && nominationsPromotion.getCardId() != null )
    {
      submission.setCardId( nominationsPromotion.getCardId() );
    }
    else if ( NominationSubmitForm.CARD_TYPE_UPLOAD.equals( nominationsPromotion.getCardType() ) )
    {
      // Image upload. Video does not use 'ownCardName'
      boolean isImageUpload = StringUtil.isEmpty( nominationsPromotion.getVideoUrl() );
      if ( isImageUpload )
      {
        submission.setOwnCardName( nominationsPromotion.getCardUrl() );
      }
      else
      {
        submission.setVideoUrl( nominationsPromotion.getVideoUrl() );
        submission.setVideoImageUrl( nominationsPromotion.getVideoImageUrl() );
      }
    }
    else if ( NominationSubmitForm.CARD_TYPE_DRAWING.equals( nominationsPromotion.getCardType() ) )
    {
      submission.setCardId( nominationsPromotion.getCardId() );
      submission.setOwnCardName( nominationsPromotion.getCardUrl() );
      submission.setDrawingDataUrl( nominationsPromotion.getDrawingDataUrl() );
    }
    else if ( "none".equals( nominationsPromotion.getCardType() ) )
    {
      // Clear out the state
      nominationsPromotion.setCardUrl( "" );
      nominationsPromotion.setCardId( null );
    }

    return submission;
  }

  /**
   * Retrieves a ClaimFormDefinitionService
   * 
   * @return ClaimFormDefinitionService
   */
  private static ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)BeanLocator.getBean( ClaimFormDefinitionService.BEAN_NAME );
  }

  private static CalculatorService getCalculatorService()
  {
    return (CalculatorService)BeanLocator.getBean( CalculatorService.BEAN_NAME );
  }
  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
  
  private static String removeFormatting( String comments )
  {
    comments = comments.replaceAll( "(?s)<!--.*?-->", "" );
    comments = StringEscapeUtils.unescapeHtml( comments );
    comments = comments.replaceAll( "&", "&amp;" );
    return comments;
  }

}
