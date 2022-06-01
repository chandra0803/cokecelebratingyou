
package com.biperf.core.ui.nomination;

import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.json.simple.JSONObject;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.claim.NominationClaimBehaviors;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.domain.client.TcccClaimFile;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.biperf.core.domain.enums.NominationClaimStatusType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.mtc.MTCVideo;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantSearchListView;
import com.biperf.core.domain.participant.ParticipantSearchView;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.cashcurrency.CashCurrencyService;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.claim.RecognitionClaimSubmission;
import com.biperf.core.service.claim.RecognitionClaimSubmissionResponse;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.service.promotion.NominationPromotionService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.ui.JsonResponseMessageList;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.recognition.EcardUtil;
import com.biperf.core.ui.recognition.RecognitionClaimSubmissionFactory;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UrlReader;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.NameableBean;
import com.biperf.core.value.nomination.NominationStepWizard;
import com.biperf.core.value.nomination.NominationSubmitDataAttachmentValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataBehaviorValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataECardValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsPromotionListValueBean;
import com.biperf.core.value.nomination.NominationsSubmissionWizardTabValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class NominationSubmitAction extends BaseRecognitionAction
{

  static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;

  static final List<String> docList = Arrays.asList( "pdf", "xls", "xlsx", "doc", "docx", "ppt", "pptx" );

  static final List<String> imageList = Arrays.asList( "jpg", "png" );

  static final List<String> videoList = Arrays.asList( "mp4", "webm" );

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm submitForm = (NominationSubmitForm)form;

    Long promotionId = null;
    Long claimId = null;

    String clientState = request.getParameter( "clientState" );
    if ( clientState != null && clientState.trim().length() > 0 )
    {
      String cryptoPass = request.getParameter( "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();
      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      try
      {
        Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        if ( clientStateMap.get( "promotionId" ) != null )
        {
          promotionId = (Long)clientStateMap.get( "promotionId" );
        }
        if ( clientStateMap.get( "claimId" ) != null )
        {
          claimId = (Long)clientStateMap.get( "claimId" );
        }
      }
      catch( InvalidClientStateException e )
      {
        // Never Enter This Block
      }
    }
    else
    {
      if ( request.getParameter( "claimId" ) != null )
      {
        claimId = Long.valueOf( request.getParameter( "claimId" ) );
      }
      if ( request.getParameter( "promoId" ) != null )
      {
        promotionId = Long.valueOf( request.getParameter( "promoId" ) );
      }
    }
    submitForm.setPromotionId( promotionId );
    submitForm.setClaimId( claimId );

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  /**
   * Deals with the JSON request for promotion information when going to the submit a nomination page (for either new or in progress submission)
   */
  @SuppressWarnings( "unchecked" )
  public ActionForward nomSubmitData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm submitForm = (NominationSubmitForm)form;
    NominationClaim nomClaim = null;

    // This will be our JSON response
    NominationsPromotionListValueBean submitDataValueBean = null;
    try
    {
      // Presence of a claim ID means continuing an existing claim
      if ( submitForm.getClaimId() != null && submitForm.getClaimId() != 0 )
      {
        // Get Claim object first. It'll lead us to the promotion.
        AssociationRequestCollection claimAssociationRequestCollection = new AssociationRequestCollection();
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_BEHAVIORS ) );
        claimAssociationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );

         nomClaim = (NominationClaim)getClaimService().getClaimByIdWithAssociations( submitForm.getClaimId(), claimAssociationRequestCollection );

        // We'll grab the list for a new claim first - it'll serve as a base to modify
        submitDataValueBean = getNominationPromotionService().getNominationForSubmissionList( nomClaim.getPromotion().getId(), UserManager.getUser(), UserManager.getUserLocale() );

        NominationSubmitDataPromotionValueBean nomPromo = submitDataValueBean.getPromotion();
        nomPromo.setClaimId( submitForm.getClaimId() );
        nomPromo.setComments( nomClaim.getSubmitterComments() );

        if ( nomClaim.getAwardGroupType() != null )
        {
          nomPromo.setIndividualOrTeam( nomClaim.getAwardGroupType().getCode() );
        }
        nomPromo.setTeamName( nomClaim.getTeamName() );

        if ( nomClaim.getClaimRecipients() != null && nomClaim.getClaimRecipients().size() > 0 )
        {
          ClaimRecipient recipient = (ClaimRecipient)nomClaim.getClaimRecipients().iterator().next();

          nomPromo.setRecipientName( recipient.getFirstName() + " " + recipient.getLastName() );
        }
        if ( nomPromo != null && nomClaim != null )
        {
          if ( nomClaim.getNode() != null && nomClaim.getNode().getId() != null )
          {
            // If there is only one node, select it by default
            if ( submitDataValueBean.getNodes() != null && submitDataValueBean.getNodes().size() == 1 )
            {
              submitDataValueBean.getNodes().get( 0 ).setSelected( true );
            }
            // Otherwise multiple nodes - look through list and find the one that is already
            // selected
            else
            {
              for ( NominationsPromotionListValueBean.NodeValueBean node : submitDataValueBean.getNodes() )
              {
                if ( node.getId().longValue() == nomClaim.getNode().getId().longValue() )
                {
                  node.setSelected( true );
                }
              }
            }
          }

          if ( nomClaim.getNominationClaimBehaviors() != null && nomPromo.getBehaviors() != null )
          {
            for ( NominationSubmitDataBehaviorValueBean nominationsBehavior : nomPromo.getBehaviors() )
            {
              for ( NominationClaimBehaviors nominationClaimBehaviors : nomClaim.getNominationClaimBehaviors() )
              {
                if ( nominationsBehavior.getId().equals( nominationClaimBehaviors.getBehavior().getCode() ) )
                {
                  nominationsBehavior.setSelected( true );
                }
              }
            }
          }

          ClaimRecipient cr = nomClaim.getClaimRecipients().stream().findFirst().get();

          nomPromo.setAwardQuantity( cr.getAwardQuantity() );
          // Card
          if ( NominationSubmitForm.CARD_TYPE_CARD.equals( nomClaim.getCardType() ) && nomClaim.getCard() != null && nomClaim.getCard().getId() != null && nomPromo.geteCards() != null )
          {
            nomPromo.setCardType( NominationSubmitForm.CARD_TYPE_CARD );
            nomPromo.setCardId( nomClaim.getCard().getId() );
            nomPromo.setCardUrl( nomClaim.getCard().getLargeImageNameLocale() );
          }
          // Certificate
          else if ( NominationSubmitForm.CARD_TYPE_CERTIFICATE.equals( nomClaim.getCardType() ) && nomClaim.getCertificateId() != null )
          {
            nomPromo.setCardType( NominationSubmitForm.CARD_TYPE_CERTIFICATE );
            nomPromo.setCardId( nomClaim.getCertificateId() );

            NominationSubmitDataPromotionValueBean certificateValues = getNominationPromotionService().getCertificateImages( submitDataValueBean.getPromotion().getId(), nomClaim.getAwardGroupType() );
            List<NominationSubmitDataECardValueBean> eCards = certificateValues.geteCards();

            for ( NominationSubmitDataECardValueBean cert : eCards )
            {
              if ( cert.getId() == nomClaim.getCertificateId() )
              {
                nomPromo.setCardUrl( cert.getLargeImage() );
              }
            }
          }
          // Drawing
          else if ( NominationSubmitForm.CARD_TYPE_DRAWING.equals( nomClaim.getCardType() ) && nomClaim.getOwnCardName() != null )
          {
            // Drawing data is stored in a file... we need to send it back, so we need to read it
            // from the file
            nomPromo.setCardType( NominationSubmitForm.CARD_TYPE_CARD );
            nomPromo.setCardId( nomClaim.getCard().getId() );
            nomPromo.setCardUrl( nomClaim.getCard().getLargeImageNameLocale() );
            nomPromo.setDrawingData( new UrlReader().asString( nomClaim.getDrawingDataUrl(), new HashMap<>() ) );
          }
          // Image Upload
          else if ( NominationSubmitForm.CARD_TYPE_UPLOAD.equals( nomClaim.getCardType() ) && nomClaim.getOwnCardName() != null )
          {
            nomPromo.setCardType( NominationSubmitForm.CARD_TYPE_UPLOAD );
            nomPromo.setCardUrl( nomClaim.getOwnCardName() );
          }
          // Video Upload
          else if ( NominationSubmitForm.CARD_TYPE_UPLOAD.equals( nomClaim.getCardType() ) && nomClaim.getCardVideoUrl() != null )
          {
            nomPromo.setCardType( NominationSubmitForm.CARD_TYPE_UPLOAD );
            // MTC - To be changed

            MTCVideo mtcVideo = getMTCVideoService().getMTCVideoByRequestId( nomClaim.getRequestId( nomClaim.getCardVideoUrl() ) );
            nomPromo.setVideoUrl( mtcVideo.getOriginalFormat().equals( "mp4" ) ? mtcVideo.getMp4Url() : mtcVideo.getWebmUrl() );
            nomPromo.setCardUrl( mtcVideo.getThumbNailImageUrl() ); // FE will display cardUrl. We
                                                                    // move it to videoImageUrl on
                                                                    // our side
          }
        }
        if ( nomClaim.getStepNumber() != null )
        {
          nomPromo.setCurrentStep( NominationStepWizard.getById( nomClaim.getStepNumber() ).getName() );
        }
        nomPromo.setIsEditMode( true );

        if ( nomClaim.whyAttachmentUrlExist() )
        {
          nomPromo.setAddAttachment( true );
        }

//        if ( StringUtils.isEmpty( nomClaim.getWhyAttachmentName() ) )
//        {
//          nomPromo.setNominationUrl( nomClaim.getWhyAttachmentUrl() );
//        }
//        else
//        {
//          nomPromo.setNominationLink( nomClaim.getWhyAttachmentUrl() );
//          nomPromo.setFileName( nomClaim.getWhyAttachmentName() );
//        }
      }
      // Otherwise look for a promotion ID for a new claim
      else if ( submitForm.getPromotionId() != null )
      {
        submitDataValueBean = getNominationPromotionService().getNominationForSubmissionList( submitForm.getPromotionId(), UserManager.getUser(), UserManager.getUserLocale() );
      }
      
      NominationPromotion promotion = (NominationPromotion)getNominationPromotionService().getNominationPromotion( submitForm.getPromotionId() );
      // Client customization for WIP #39189 starts
      if ( promotion != null )
      {
        if ( promotion.isEnableFileUpload() )
        {
          submitDataValueBean.getPromotion().setMinDocsAllowed( promotion.getFileMaxNumber() );
          submitDataValueBean.getPromotion().setMinLinksAllowed( promotion.getFileMinNumber() );
          submitDataValueBean.getPromotion().setMaxDocsAllowed( promotion.getFileMaxNumber() );
          submitDataValueBean.getPromotion().setMaxLinksAllowed( promotion.getFileMaxNumber() );
          submitDataValueBean.getPromotion().setAddAttachment( true );
          
          if ( nomClaim != null && nomClaim.getClaimFiles().size() > 0 )
          {
            List<NominationSubmitDataAttachmentValueBean> claimFileValueBeans = new ArrayList<NominationSubmitDataAttachmentValueBean>();
            nomClaim.getClaimFiles().forEach( claimFile ->
            {
              NominationSubmitDataAttachmentValueBean claimFileValueBean = new NominationSubmitDataAttachmentValueBean();
              claimFileValueBean.setLinkid( claimFile.getId() );
              claimFileValueBean.setFileName( StringUtil.isNullOrEmpty( claimFile.getFileName() ) ? claimFile.getFileUrl() : claimFile.getFileName() );
              claimFileValueBean.setNominationLink( claimFile.getFileUrl() );
              claimFileValueBean.setNominationUrl( claimFile.getFileUrl() );
              claimFileValueBeans.add( claimFileValueBean );
            } );
            submitDataValueBean.getPromotion().setNominationLinks( claimFileValueBeans );
            submitDataValueBean.getPromotion().setUpdatedDocCount( nomClaim.getClaimFiles().size() );
          }
        }
        else
        {
          submitDataValueBean.getPromotion().setMinDocsAllowed( 0 );
          submitDataValueBean.getPromotion().setMinLinksAllowed( 0 );
          submitDataValueBean.getPromotion().setMaxDocsAllowed( 1 );
          submitDataValueBean.getPromotion().setMaxLinksAllowed( 1 );
        }
      }
      // Client customization for WIP #39189 ends
      /*
       * NominationPromotion promotion =
       * (NominationPromotion)getNominationPromotionService().getNominationPromotion(
       * submitForm.getPromotionId() ); int count = 0; if (
       * !promotion.getCustomApproverOptions().isEmpty() ) { Iterator<ApproverOption> iterator =
       * promotion.getCustomApproverOptions().iterator(); while ( iterator.hasNext() ) {
       * ApproverOption approverOption = iterator.next(); if ( approverOption.getApproverType() !=
       * null ) { if ( approverOption.getApproverType().getCode().equals(
       * CustomApproverType.BEHAVIOR ) ) { count++; } } } } if ( count > 0 )
       */

      if ( getNominationPromotionService().isBehaviorBasedApproverTypeExist( submitForm.getPromotionId() ) )
      {
        submitDataValueBean.getPromotion().setMaxBehaviorsAllowed( 1 );
      }
      else
      {
        submitDataValueBean.getPromotion().setMaxBehaviorsAllowed( submitDataValueBean.getPromotion().getBehaviors().size() );
      }
      NominationsSubmitDataViewMapper mapper = new NominationsSubmitDataViewMapper( submitDataValueBean );
      writeAsJsonToResponse( mapper.getViewBean(), response );

    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward getParticipantData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm submitForm = (NominationSubmitForm)form;
    ParticipantSearchListView participantSearchListView = null;
    Set<ClaimRecipient> recipients = null;
    try
    {
      if ( submitForm.getGroupId() != null )
      {
        participantSearchListView = getParticipantService().getParticipantGroupList( submitForm.getGroupId(), submitForm.getPromotionId() );
      }
      else
      {
        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_PARTICIPANTS ) );
        Claim claim = getClaimService().getClaimByIdWithAssociations( submitForm.getClaimId(), associationRequestCollection );
        NominationClaim nomClaim = (NominationClaim)claim;
        recipients = nomClaim.getClaimRecipients();
        participantSearchListView = getParticipantService().getParticipant( recipients, submitForm.getPromotionId() );
      }
      writeAsJsonToResponse( getNominationParticipantView( participantSearchListView, recipients, submitForm ), response );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

  private Map<Long, ClaimRecipient> mapClaimItemWithRecipient( Set<ClaimRecipient> recipients )
  {
    Map<Long, ClaimRecipient> map = new HashMap<Long, ClaimRecipient>();

    if ( !CollectionUtils.isEmpty( recipients ) )
    {
      for ( ClaimRecipient claimRecipient : recipients )
      {
        map.put( claimRecipient.getRecipient().getId(), claimRecipient );
      }
    }
    return map;
  }

  private NominationsParticipantDataViewBean getNominationParticipantView( ParticipantSearchListView participantSearchListView, Set<ClaimRecipient> recipients, NominationSubmitForm submitForm )
  {

    NominationsParticipantDataViewBean nominees = new NominationsParticipantDataViewBean();
    Map<Long, ClaimRecipient> map = mapClaimItemWithRecipient( recipients );
    String paxCurrencyCode = getParticipantService().getParticipantCurrencyCode( UserManager.getUserId() );

    for ( ParticipantSearchView view : participantSearchListView.getParticipants() )
    {
      List<NominationsParticipantDataViewBean.NodeViewBean> nodes = new ArrayList<NominationsParticipantDataViewBean.NodeViewBean>();
      NominationsParticipantDataViewBean.ParticipantViewBean p = new NominationsParticipantDataViewBean.ParticipantViewBean();
      p.setId( view.getId() );
      p.setLastName( view.getLastName() );
      p.setFirstName( view.getFirstName() );
      p.setCountryName( view.getCountryName() );
      p.setCountryCode( view.getCountryCode() );
      p.setCountryRatio( view.getCountryRatio() );
      p.setJobName( view.getJobName() );
      p.setDepartmentName( view.getDepartmentName() );

      List<NameableBean> nodesView = view.getNodes();

      for ( NameableBean nameableBean : nodesView )
      {
        NominationsParticipantDataViewBean.NodeViewBean n = new NominationsParticipantDataViewBean.NodeViewBean( nameableBean.getId().intValue(), nameableBean.getName() );
        nodes.add( n );
      }

      p.setNodes( nodes );

      if ( !MapUtils.isEmpty( map ) )
      {
        ClaimRecipient claimRecipient = map.get( p.getId() );
        if ( claimRecipient != null )
        {
          // points
          Long points = claimRecipient.getAwardQuantity();
          if ( points != null )
          {
            p.setAwardQuantity( points + "" );
          }
          // calculator
          Long calculatorScore = claimRecipient.getCalculatorScore();
          if ( calculatorScore != null )
          {
            BigDecimal convertedCurrency = getCashCurrencyService().convertCurrency( "USD", paxCurrencyCode, new BigDecimal( calculatorScore ), null );
            p.setAwardQuantity( convertedCurrency != null ? convertedCurrency.toPlainString() : null );

          }
          // cash
          BigDecimal cashAwardQuantity = claimRecipient.getCashAwardQuantity();
          if ( cashAwardQuantity != null )
          {
            BigDecimal convertedCurrency = getCashCurrencyService().convertCurrency( "USD", paxCurrencyCode, cashAwardQuantity, null );
            p.setAwardQuantity( convertedCurrency != null ? convertedCurrency.toPlainString() : null );
          }

        }

      }
      nominees.addParticiapnt( p );
    }
    return nominees;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward tabMenu( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm submitForm = (NominationSubmitForm)form;
    List<NominationsSubmissionWizardTabViewBean> view = new ArrayList<NominationsSubmissionWizardTabViewBean>();

    try
    {

      List<NominationsSubmissionWizardTabValueBean> tabListObj = getNominationPromotionService().getSubmissionWizardTabs( submitForm.getPromotionId() );

      for ( NominationsSubmissionWizardTabValueBean tab : tabListObj )
      {
        view.add( new NominationsSubmissionWizardTabViewBean( tab.getId(), tab.getName(), tab.isActive(), tab.getState(), tab.getContentSel(), tab.getWtvNumber(), tab.getWtvName() ) );
      }

      JSONObject jsonObject = new JSONObject();
      jsonObject.put( "tabsJson", view );
      writeAsJsonToResponse( jsonObject, response );

    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

  public ActionForward stepNominating( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return null;
  }

  @SuppressWarnings( "unchecked" )
  public ActionForward stepNominee( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    NominationSubmitForm nomForm = (NominationSubmitForm)form;

    try
    {

      if ( !canSubmitClaimToday( nomForm ) )
      {
        writeAsJsonToResponse( getTimePeriodResponseMessage(), response );
        return null;
      }
      RecognitionClaimSubmission submission = RecognitionClaimSubmissionFactory.buildFrom( RecognitionClaimSource.WEB, nomForm );

      submission.setParticipants( nomForm.getParticipants() );
      submission.setNomSubmitDataPromotionValueBean( nomForm.getPromotion() );
      submission.setDraft( nomForm.isDraft() );

      RecognitionClaimSubmissionResponse submissionResponse = getNominationClaimService().stepNominee( submission, nomForm.getGroupId() );
      writeAsJsonToResponse( getStepSubmissionSuccessMessage( submissionResponse, nomForm ), response );

    }
    catch( ServiceErrorException serviceException )
    {
      writeAsJsonToResponse( getMessageFromServiceErrors( serviceException.getServiceErrors() ), response );
    }

    catch( Exception e )
    {
      writeAppErrorAsJsonResponse( response, e );
      e.printStackTrace();
    }
    return null;
  }

  public ActionForward stepBehavior( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm nomForm = (NominationSubmitForm)form;
    NominationPromotionService nomPromoService = getNominationPromotionService();

    try
    {
      NominationClaimStatusType statusType = nomForm.isDraft() ? null : NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE );
      if ( CollectionUtils.isEmpty( nomForm.getPromotion().getBehaviors() ) )
      {
        NominationSubmitResponseView r = new NominationSubmitResponseView();
        r.setClaimId( nomForm.getPromotion().getClaimId() );
        writeAsJsonToResponse( r, response );
        return null;
      }

      boolean canSubmitToday = nomPromoService.canSubmitClaimToday( nomForm.getPromotionId(), UserManager.getTimeZoneID(), UserManager.getUserId() );
      if ( !canSubmitToday )
      {
        writeAsJsonToResponse( getTimePeriodResponseMessage(), response );
        return null;
      }

      // Save behavior selection
      RecognitionClaimSubmission submission = RecognitionClaimSubmissionFactory.buildForNomBehaviors( nomForm );
      getNominationClaimService().stepBehavior( submission, nomForm.getPromotion().isEditMode() );

      if ( isLastStep( submission.getPromotionId(), NominationStepWizard.BEHAVIOUR ) && !nomForm.isDraft() )
      {
        RecognitionClaimSubmissionResponse submissionResponse = getNominationClaimService()
            .submitClaim( submission, statusType, nomForm.getPromotion().getIndividualOrTeam(), NominationStepWizard.BEHAVIOUR.getId() );
        writeAsJsonToResponse( getStepSubmissionSuccessMessage( submissionResponse, nomForm ), response );
      }
      else
      {
        writeAsJsonToResponse( getStepSubmissionSuccessMessage( submission, nomForm ), response );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

  public ActionForward stepEcard( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm nomForm = (NominationSubmitForm)form;

    NominationPromotionService nomPromoService = getNominationPromotionService();

    try
    {
      NominationClaimStatusType statusType = nomForm.isDraft() ? null : NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE );
      if ( CollectionUtils.isEmpty( nomForm.getPromotion().geteCards() ) )
      {
        NominationSubmitResponseView r = new NominationSubmitResponseView();
        r.setClaimId( nomForm.getPromotion().getClaimId() );
        writeAsJsonToResponse( r, response );
        return null;
      }

      boolean canSubmitToday = nomPromoService.canSubmitClaimToday( nomForm.getPromotionId(), UserManager.getTimeZoneID(), UserManager.getUserId() );
      if ( !canSubmitToday )
      {
        writeAsJsonToResponse( getTimePeriodResponseMessage(), response );
        return null;
      }

      // Save card drawing
      storeNominationUserEditedCard( nomForm );

      // Save other card information
      RecognitionClaimSubmission submission = RecognitionClaimSubmissionFactory.buildForNomEcards( nomForm );
      getNominationClaimService().stepEcard( submission );

      if ( isLastStep( submission.getPromotionId(), NominationStepWizard.ECARD ) && !nomForm.isDraft() )
      {
        RecognitionClaimSubmissionResponse submissionResponse = getNominationClaimService().submitClaim( submission,
                                                                                                         statusType,
                                                                                                         nomForm.getPromotion().getIndividualOrTeam(),
                                                                                                         NominationStepWizard.ECARD.getId() );
        writeAsJsonToResponse( getStepSubmissionSuccessMessage( submissionResponse, nomForm ), response );
      }
      else
      {
        writeAsJsonToResponse( getStepSubmissionSuccessMessage( submission, nomForm ), response );
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }
    return null;
  }

  public ActionForward stepWhy( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm nomForm = (NominationSubmitForm)form;
    String submitterCommnets = nomForm.getPromotion().getComments();
    // added to remove pre html tag
    if ( submitterCommnets != null )
    {
      submitterCommnets = removeFormatting( nomForm.getPromotion().getComments() );
      nomForm.getPromotion().setComments( submitterCommnets );
    }

    try
    {
      if ( !canSubmitClaimToday( nomForm ) )
      {
        writeAsJsonToResponse( getTimePeriodResponseMessage(), response );
        return null;
      }
      RecognitionClaimSubmission submission = RecognitionClaimSubmissionFactory.buildNominationSubmit( RecognitionClaimSource.WEB, nomForm );
      NominationClaimStatusType statusType = nomForm.isDraft() ? null : NominationClaimStatusType.lookup( NominationClaimStatusType.COMPLETE );
      // For Save Draft, do not check for validations.So sending isDraft value
      getNominationClaimService().stepWhy( submission, nomForm.isDraft() );

      if ( isLastStep( submission.getPromotionId(), NominationStepWizard.WHY ) && !nomForm.isDraft() )
      {
        RecognitionClaimSubmissionResponse submissionResponse = getNominationClaimService().submitClaim( submission,
                                                                                                         statusType,
                                                                                                         nomForm.getPromotion().getIndividualOrTeam(),
                                                                                                         NominationStepWizard.WHY.getId() );
        writeAsJsonToResponse( getStepSubmissionSuccessMessage( submissionResponse, nomForm ), response );
      }
      else
      {
        writeAsJsonToResponse( getStepSubmissionSuccessMessage( submission, nomForm ), response );
      }
    }
    catch( ServiceErrorException serviceException )
    {
      writeAsJsonToResponse( getMessageFromServiceErrors( serviceException.getServiceErrors() ), response );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }
    return null;
  }
  
  private boolean isMoreInfo( NominationClaim nomClaim )
  {
    boolean moreInfo = false;
    boolean pending = false;
    // Individual based claims will have recipients
    // Check if nominator is editing the nomination for more info.
    if ( nomClaim.getClaimRecipients() != null && !nomClaim.getClaimRecipients().isEmpty() )
    {
      for ( Iterator claimRecipientIter = nomClaim.getClaimRecipients().iterator(); claimRecipientIter.hasNext(); )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIter.next();
        if ( claimRecipient != null && claimRecipient.getApprovalStatusType() != null && claimRecipient.getCurrentClaimItemApprover() != null
            && claimRecipient.getCurrentClaimItemApprover().getApprovalStatusType() != null )
        {
          if ( claimRecipient.getApprovalStatusType().getCode().equals( ApprovalStatusType.MORE_INFO )
              || claimRecipient.getCurrentClaimItemApprover().getApprovalStatusType().getCode().equals( ApprovalStatusType.MORE_INFO ) )
          {
            moreInfo = true;
          }
        }
        if ( claimRecipient.getApprovalStatusType().getCode().equals( ApprovalStatusType.PENDING ) )
        {
          pending = true;
        }
      }
    }

    // Check if approver is editing the nomination
    if ( !moreInfo )
    {
      if ( nomClaim.getSubmitter().getId().longValue() != UserManager.getUserId().longValue() && pending )
        moreInfo = true;
    }
    return moreInfo;
  }

  private RecognitionClaimSubmissionResponse updateMoreInfo( NominationClaim nomClaim, HttpServletRequest request, RecognitionClaimSubmission submission )
  {
    RecognitionClaimSubmissionResponse submissionResponse = new RecognitionClaimSubmissionResponse();
    Participant approver = null;
    Participant nominee = null;
    // Individual based claims will have recipients
    if ( nomClaim.getClaimRecipients() != null && !nomClaim.getClaimRecipients().isEmpty() )
    {
      for ( Iterator claimRecipientIter = nomClaim.getClaimRecipients().iterator(); claimRecipientIter.hasNext(); )
      {
        ClaimRecipient claimRecipient = (ClaimRecipient)claimRecipientIter.next();
        if ( claimRecipient != null )
        {
          if ( claimRecipient.getApprovalStatusType().getCode().equals( ApprovalStatusType.MORE_INFO ) )
          {
            claimRecipient.getApprovalStatusType().setCode( ApprovalStatusType.PENDING );
          }
          if ( claimRecipient.getCurrentClaimItemApprover() != null && claimRecipient.getCurrentClaimItemApprover().getApprovalStatusType() != null
              && claimRecipient.getCurrentClaimItemApprover().getApprovalStatusType().getCode().equals( ApprovalStatusType.MORE_INFO ) )
          {
            claimRecipient.getCurrentClaimItemApprover().getApprovalStatusType().setCode( ApprovalStatusType.PENDING );
            ApprovableItemApprover claimItemApprover = claimRecipient.getCurrentClaimItemApprover();
            approver = (Participant)claimItemApprover.getApproverUser();
            nominee = claimRecipient.getRecipient();
          }
        }
      }
    }
    
    // Client customization for WIP #39189 starts
    // Create new set of claim files
    Set<TcccClaimFile> detachedFiles = buildClaimFiles( submission );
    if ( detachedFiles.size() > 0 )
    {
      for ( TcccClaimFile claimFile : detachedFiles )
      {
        nomClaim.addClaimFile( claimFile );
      }
    }
    // Client customization for WIP #39189 ends
    
    getClaimService().saveClaim( nomClaim );
    submissionResponse.setClaimId( nomClaim.getId() );
    return submissionResponse;
  }
  
  // Client customization for WIP #39189 starts
  private Set<TcccClaimFile> buildClaimFiles( RecognitionClaimSubmission submission )
  {
    Set<TcccClaimFile> claimFiles = new LinkedHashSet<TcccClaimFile>();
    if ( submission.getNomSubmitDataPromotionValueBean().getNominationLinks() != null )
    {
      for ( NominationSubmitDataAttachmentValueBean claimFile : submission.getNomSubmitDataPromotionValueBean().getNominationLinks() )
      {
        TcccClaimFile file = new TcccClaimFile();
        file.setFileName( claimFile.getFileName() );
        file.setFileUrl( StringUtil.isNullOrEmpty( claimFile.getNominationLink() ) ? claimFile.getNominationUrl() : claimFile.getNominationLink() );
        claimFiles.add( file );
      }
    }
    return claimFiles;
  }
  // Client customization for WIP #39189 ends

  private static String removeFormatting( String comments )
  {
    comments = comments.replaceAll( "(?s)<!--.*?-->", "" );
    comments = comments.replaceAll( "&lt;/?pre{1}.*?/?&gt;", "" ).trim();
    comments = StringEscapeUtils.unescapeHtml( comments );
    comments = comments.replaceAll( "&", "&amp;" );
    return comments;
  }

  private boolean isLastStep( Long promotionId, NominationStepWizard wizardStep )
  {
    String lastStepName = getPromotionService().getLastWizardStepName( promotionId );

    return lastStepName != null && lastStepName.equalsIgnoreCase( wizardStep.getDbOrderName() );
  }

  public ActionForward removeNom( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm nomForm = (NominationSubmitForm)form;
    NominationSubmitResponseView responseMessage = new NominationSubmitResponseView();
    responseMessage.setForwardUrl( "/homePage.do" );

    try
    {
      getClaimService().deleteClaim( nomForm.getClaimId() );
    }
    catch( Exception e )
    {
      writeAsJsonToResponse( responseMessage, response );
      e.printStackTrace();
    }
    return null;
  }

  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    WebErrorMessage message = new WebErrorMessage();
    message.setType( "serverCommand" );
    message.setCommand( "redirect" );
    message.setUrl( request.getContextPath() + "/homePage.do" );

    WebErrorMessageList list = new WebErrorMessageList();
    list.getMessages().add( message );
    writeAsJsonToResponse( list, response );
    return null;
  }

  public ActionForward uploadWhyAttachment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    JsonResponseMessageList<NominationsUploadDocView> jsonResponse = new JsonResponseMessageList<NominationsUploadDocView>();

    NominationSubmitForm nomForm = (NominationSubmitForm)form;
    String fileUrl = null;
    String fileName = null;

    try
    {
      fileName = nomForm.getNominationLink().getFileName();
      Attachmentvalidator validator = new Attachmentvalidator( nomForm.getNominationLink() );
      List<String> validationResult = validator.validateAttachment();

      if ( validationResult.size() > 0 )
      {
        jsonResponse.getMessages().addAll( getAttachmentvalidationMessage( validationResult ) );
        writeAsJsonToResponse( jsonResponse, response );
        return null;
      }

      fileUrl = uploadToWebdav( nomForm.getNominationLink() );
      getNominationClaimService().addWhyAttachmentUrlReference( nomForm.getClaimId(), fileUrl, fileName );
      jsonResponse.addMessage( getUploadSuccessMessage( fileName, fileUrl ) );
      writeAsJsonToResponse( jsonResponse, response );

    }
    catch( Exception e )
    {
      if ( fileUrl != null )
      {
        getWebDavFileUploadStrategy().delete( fileUrl );
      }
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }
    return null;
  }

  private NominationsUploadDocView getUploadSuccessMessage( String fileName, String fileUrl )
  {
    NominationsUploadDocView v = new NominationsUploadDocView();
    String successMsg = CmsResourceBundle.getCmsBundle().getString( "promotion.nomination.submit.UPLOAD_SUCCESSFULLY" );
    v.setName( successMsg );
    v.setFileName( fileName );
    v.setNominationLink( fileUrl );
    v.setSuccess( true );
    v.setType( "Success" );
    v.setText( successMsg );
    return v;
  }

  public ActionForward removeWhyAttachment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    try
    {
      getNominationClaimService().removeWhyAttachment( ( (NominationSubmitForm)form ).getClaimId() );
      writeAsJsonToResponse( new WebErrorMessageList(), response );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }
    return null;
  }

  private String uploadToWebdav( FormFile formFile ) throws Exception
  {
    String orginalfilename = formFile.getFileName();
    String extension = "." + getFileExtension( orginalfilename );
    String filename = orginalfilename.substring( 0, orginalfilename.length() - extension.length() );
    if ( filename != null )
    {
      filename = ValidatorChecks.removesSpecialCharactersAndSpaces( filename );

    }
    filename = filename + extension;

    filename = UserManager.getUserId() + "_" + DateUtils.getCurrentDateAsLong() + "_" + filename;
    String filePath = "nomination" + File.separator + filename;

    if ( !getAppDataFileUploadStrategy().uploadFileData( filePath, formFile.getFileData() ) )
    {
      return "";
    }

    if ( !moveFileToWebdav( filePath ) )
    {
      return "";
    }
    return EcardUtil.getNominatorAttachmentUrl( filename );
  }

  /**
   * Certificate image URLs are different based on user input. FE will call this to obtain the correct URL some time before 
   * the ecard step. 
   * Promotion ID and the nomination type (individual / team) are given
   */
  public ActionForward getCertificateImages( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationSubmitForm nomForm = (NominationSubmitForm)form;

    NominationAwardGroupType nominationType = NominationAwardGroupType.lookup( nomForm.getPromotion().getIndividualOrTeam().toLowerCase() );
    NominationSubmitDataPromotionValueBean certificateValues = getNominationPromotionService().getCertificateImages( nomForm.getPromotion().getId(), nominationType );
    CertificateImagesView certificateImagesView = new CertificateImagesView();

    Iterator<NominationSubmitDataECardValueBean> valueBeanIterator = certificateValues.geteCards().iterator();
    while ( valueBeanIterator.hasNext() )
    {
      NominationSubmitDataECardValueBean valueBean = valueBeanIterator.next();
      CertificateImagesView.CertificateImage certificateImage = new CertificateImagesView.CertificateImage();

      certificateImage.setCertificateId( valueBean.getId() );
      certificateImage.setLargeImage( valueBean.getLargeImage() );

      certificateImagesView.getCertificates().add( certificateImage );
    }

    writeAsJsonToResponse( certificateImagesView, response );
    return null;
  }

  private SystemVariableService getSystemvariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private CashCurrencyService getCashCurrencyService()
  {
    return (CashCurrencyService)getService( CashCurrencyService.BEAN_NAME );
  }

  private NominationSubmitResponseView getStepSubmissionSuccessMessage( RecognitionClaimSubmission submission, NominationSubmitForm nomForm )
  {
    NominationSubmitResponseView response = new NominationSubmitResponseView();
    response.setClaimId( submission.getClaimId() );
    return response;
  }

  private NominationSubmitResponseView getStepSubmissionSuccessMessage( RecognitionClaimSubmissionResponse submission, NominationSubmitForm nomForm )
  {

    NominationSubmitResponseView response = new NominationSubmitResponseView();

    if ( !CollectionUtils.isEmpty( submission.getErrors() ) )
    {

      for ( ServiceError serviceError : submission.getErrors() )
      {
        WebErrorMessage errMessage = new WebErrorMessage();
        errMessage.setText( CmsResourceBundle.getCmsBundle().getString( serviceError.getKey() ) );
        WebErrorMessage.addErrorMessage( errMessage );

        response.getMessages().add( errMessage );

      }
    }

    response.setClaimId( submission.getClaimId() );
    return response;
  }

  public WebErrorMessageList getMessageFromServiceErrors( List<ServiceError> serviceErrors )
  {

    WebErrorMessageList messageList = new WebErrorMessageList();
    WebErrorMessage msg = null;

    for ( Object obj : serviceErrors )
    {
      ServiceError error = (ServiceError)obj;
      String errorMessage = CmsResourceBundle.getCmsBundle().getString( error.getKey() );
      if ( StringUtils.isNotEmpty( error.getArg1() ) )
      {
        errorMessage = errorMessage.replace( "{0}", error.getArg1() );
      }
      if ( StringUtils.isNotEmpty( error.getArg2() ) )
      {
        errorMessage = errorMessage.replace( "{1}", error.getArg2() );
      }
      if ( StringUtils.isNotEmpty( error.getArg3() ) )
      {
        errorMessage = errorMessage.replace( "{2}", error.getArg3() );
      }
      errorMessage = errorMessage.replace( "???", "" );
      errorMessage = errorMessage.replace( "???", "" );
      error.setArg1( errorMessage );

      msg = new WebErrorMessage();
      msg.setText( errorMessage );
      WebErrorMessage.addErrorMessage( msg );
      messageList.getMessages().add( msg );

    }

    return messageList;
  }

  public List<NominationsUploadDocView> getAttachmentvalidationMessage( List<String> messages )
  {
    List<NominationsUploadDocView> viewList = new ArrayList<NominationsUploadDocView>();

    for ( String message : messages )
    {
      NominationsUploadDocView msg = new NominationsUploadDocView();
      msg.setText( message );
      msg.setType( "Failed" );
      viewList.add( msg );
    }
    return viewList;
  }

  private WebErrorMessageList getTimePeriodResponseMessage()
  {
    WebErrorMessageList messageList = new WebErrorMessageList();

    WebErrorMessage message = new WebErrorMessage();
    message.setText( CmsResourceBundle.getCmsBundle().getString( "promotion.nomination.submit.TIME_PERIOD_MAX_LIMIT_MESSAGE" ) );
    WebErrorMessage.addErrorMessage( message );
    messageList.getMessages().add( message );
    return messageList;
  }

  private boolean canSubmitClaimToday( NominationSubmitForm form )
  {
    String timeZoneID = UserManager.getTimeZoneID();
    return getNominationPromotionService().canSubmitClaimToday( form.getPromotionId(), timeZoneID, UserManager.getUserId() );
  }

  public ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );

  }

  public MTCVideoService getMTCVideoService()
  {
    return (MTCVideoService)getService( MTCVideoService.BEAN_NAME );
  }

  private static FileUploadStrategy getAppDataFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.APPDATADIR );
  }

  private static FileUploadStrategy getWebDavFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.WEBDAV );
  }

  private class Attachmentvalidator
  {
    private final FormFile formFile;

    public Attachmentvalidator( FormFile formFile )
    {
      this.formFile = formFile;
    }

    public List<String> validateAttachment() throws Exception
    {
      List<String> validationResult = new ArrayList<String>();
      String originalFileName = formFile.getFileName();

      String fileFormat = FilenameUtils.getExtension( originalFileName );
      String extension = "." + fileFormat;
      String justFilename = originalFileName.substring( 0, originalFileName.length() - extension.length() );

      if ( justFilename != null )
      {
        justFilename = ValidatorChecks.removesSpecialCharactersAndSpaces( justFilename );
      }

      if ( docList.contains( fileFormat.toLowerCase() ) )
      {
        validateDocumentType( formFile, validationResult );
      }
      else if ( imageList.contains( fileFormat.toLowerCase() ) )
      {
        validateImageType( formFile, validationResult );
      }
      else if ( videoList.contains( fileFormat.toLowerCase() ) )
      {
        validateVideoType( formFile, validationResult );
      }
      else
      {
        String supportedFileExtentions = docList.toString() + "," + imageList.toString() + " ," + videoList.toString();
        String supportedFormatMsg = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "promotion.nomination.submit.NOT_SUPPORTED_FORMAT" ), new Object[] { supportedFileExtentions } );
        validationResult.add( supportedFormatMsg );
      }

      return validationResult;
    }

    private void validateDocumentType( FormFile formFile, List<String> validationResult ) throws Exception
    {

      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * getSystemvariableService().getPropertyByName( SystemVariableService.SYSTEM_PDF_SIZE_LIMIT ).getIntVal();

      if ( formFile.getFileData().length > sizeLimit )
      {
        String allowedSizeLimit = sizeLimit / MEGABYTES_TO_BYTES_MULTIPLIER + " MB";
        String docMaxSizeLimitMsg = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "promotion.nomination.submit.ATTACHMENT_EXCEEDS_SIZE_LIMIT" ),
                                                          new Object[] { allowedSizeLimit } );
        validationResult.add( docMaxSizeLimitMsg );
      }

    }

    private void validateImageType( FormFile formFile, List<String> validationResult ) throws Exception
    {
      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * getSystemvariableService().getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal();
      String allowedSizeLimit = sizeLimit / MEGABYTES_TO_BYTES_MULTIPLIER + " MB";
      String imageMaxSizeLimitMsg = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "promotion.nomination.submit.ATTACHMENT_EXCEEDS_SIZE_LIMIT" ),
                                                          new Object[] { allowedSizeLimit } );

      if ( formFile.getFileData().length > sizeLimit )
      {
        validationResult.add( imageMaxSizeLimitMsg );
      }
    }

    private void validateVideoType( FormFile formFile, List<String> validationResult ) throws Exception
    {

      int sizeLimit = MEGABYTES_TO_BYTES_MULTIPLIER * getSystemvariableService().getPropertyByName( SystemVariableService.SYSTEM_VIDEO_SIZE_LIMIT ).getIntVal();
      String allowedSizeLimit = sizeLimit / MEGABYTES_TO_BYTES_MULTIPLIER + " MB";
      String videoMaxSizeLimitMsg = MessageFormat.format( CmsResourceBundle.getCmsBundle().getString( "promotion.nomination.submit.ATTACHMENT_EXCEEDS_SIZE_LIMIT" ),
                                                          new Object[] { allowedSizeLimit } );

      if ( formFile.getFileData().length > sizeLimit )
      {
        validationResult.add( videoMaxSizeLimitMsg );
      }

    }
  }
  
  private MessageService getMessageService()
  {
    return (MessageService)getService( MessageService.BEAN_NAME );
  }

  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }
}
