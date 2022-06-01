
package com.biperf.core.ui.nomination;

import java.io.File;
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.biperf.core.domain.claim.ApprovableItemApprover;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.client.TcccClaimFile;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.gamification.BadgeLibrary;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionNotification;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.nomination.NominationService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.FileUploadStrategy;
import com.biperf.core.ui.JsonResponseMessageList;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.nomination.NominationMoreInfoViewBean.NominationMoreInfoDetailViewBean;
import com.biperf.core.ui.nomination.NominationMoreInfoViewBean.NominationMoreInfoDetailViewBean.NominationSubmissionViewBean;
import com.biperf.core.ui.nomination.NominationMoreInfoViewBean.NominationMoreInfoDetailViewBean.NominationSubmissionViewBean.NominationSubmissionBadgeViewBean;
import com.biperf.core.ui.nomination.NominationMoreInfoViewBean.NominationMoreInfoDetailViewBean.NominationSubmissionViewBean.NominationSubmissionCertificateViewBean;
import com.biperf.core.ui.nomination.NominationMoreInfoViewBean.NominationMoreInfoDetailViewBean.NominationSubmissionViewBean.NominationSubmissionCustomFieldViewBean;
import com.biperf.core.ui.nomination.NominationMoreInfoViewBean.NominationMoreInfoDetailViewBean.NominatorInfoViewBean;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.recognition.EcardUtil;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.ui.utils.ValidatorChecks;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.nomination.NominationMoreInfoPageBehaviorValueBean;
import com.biperf.core.value.nomination.NominationMoreInfoPageCustomFieldValueBean;
import com.biperf.core.value.nomination.NominationMoreInfoPageTeamValueBean;
import com.biperf.core.value.nomination.NominationMoreInfoValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataAttachmentValueBean;
import com.biperf.core.value.nomination.TranslateCommentViewBean;
import com.objectpartners.cms.util.BeanLocator;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * This page is for a nominator whose nomination was sent back to them by the approver. 
 * The approver is request more information from the nominator. 
 * This page collects the nominators extra submission information.
 */
public class NominationMoreInfoAction extends BaseRecognitionAction
{
  private static final String TRANSLATE_COMMENT_URL = "/claim/nominationsMoreInfoPage.do?method=translateComment";
  private static final String MORE_INFO_URL = "/claim/nominationsMoreInfoPage.do?method=getPageInfo";
  private static final String UPLOAD_DOC_URL = "/claim/nominationsMoreInfoPage.do?method=uploadWhyAttachment";
  private static final String SUBMIT_NOMS_DATA = "/claim/nominationsMoreInfoPage.do?method=submitNomData";
  private static final String OUTPUT_DATA = "p_out_data";
  private static final String OUTPUT_CUSTOM_DATA = "p_out_custom_data";
  private static final String OUTPUT_BEHAVIOR_DATA = "p_out_behavior_data";
  private static final String OUTPUT_TEAM_DATA = "p_out_team_data";

  static final int MEGABYTES_TO_BYTES_MULTIPLIER = 1024 * 1024;
  static final List<String> docList = Arrays.asList( "pdf", "xls", "xlsx", "doc", "docx", "ppt", "pptx" );
  static final List<String> imageList = Arrays.asList( "jpg", "png" );
  static final List<String> videoList = Arrays.asList( "mp4", "webm" );

  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return display( mapping, form, request, response );
  }

  /**
   * Method called for viewing the page.  getInfo method will be called for JSON information.
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationMoreInfoForm submitForm = (NominationMoreInfoForm)form;

    // Get claim ID from client state, use it to build the more info json request url
    Long claimId = null;
    Long userId = null;
    Long promotionId = null;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

    if ( clientStateMap.get( "claimId" ) != null )
    {
      claimId = (Long)clientStateMap.get( "claimId" );
      Claim claim = getClaimService().getClaimById( claimId );
      promotionId = claim.getPromotion().getId();

    }
    else
    {
      throw new Exception( "Client state did not contain claim ID" );
    }
    userId = (Long)clientStateMap.get( "userId" );
    String moreInfoUrl = buildMoreInfoUrl( claimId, userId, request );
    request.setAttribute( "moreInfoUrl", moreInfoUrl );

    String translateCommentUrl = buildTranslateCommentUrl( claimId, userId, request );
    request.setAttribute( "translateCommentUrl", translateCommentUrl );

    String uploadDocUrl = buildDocumentUrl( claimId, request );
    request.setAttribute( "uploadDocUrl", uploadDocUrl );

    String submitNominationUrl = buildSubmitNominationUrl( claimId, promotionId, request );
    request.setAttribute( "submitNominationUrl", submitNominationUrl );

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private String buildMoreInfoUrl( Long claimId, Long userId, HttpServletRequest request )
  {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put( "claimId", claimId );
    parameters.put( "userId", userId );
    String url = ClientStateUtils.generateEncodedLink( request.getContextPath(), MORE_INFO_URL, parameters );
    return url;
  }

  private String buildTranslateCommentUrl( Long claimId, Long userId, HttpServletRequest request )
  {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put( "claimId", claimId );
    parameters.put( "userId", userId );
    String url = ClientStateUtils.generateEncodedLink( request.getContextPath(), TRANSLATE_COMMENT_URL, parameters );

    return url;
  }

  private String buildDocumentUrl( Long claimId, HttpServletRequest request )
  {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put( "claimId", claimId );
    String url = ClientStateUtils.generateEncodedLink( request.getContextPath(), UPLOAD_DOC_URL, parameters );
    return url;
  }

  private String buildSubmitNominationUrl( Long claimId, Long promotionId, HttpServletRequest request )
  {
    Map<String, Object> parameters = new HashMap<>();
    parameters.put( "claimId", claimId );
    String url = ClientStateUtils.generateEncodedLink( request.getContextPath(), SUBMIT_NOMS_DATA, parameters );
    return url;
  }

  /**
   * Retrieve the information to display on the page and send it back in JSON format
   */
  public ActionForward getPageInfo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationMoreInfoForm submitForm = (NominationMoreInfoForm)form;

    try
    {
      NominationMoreInfoViewBean moreInfoView = new NominationMoreInfoViewBean();

      Long claimId = null;
      Long submitterId = null;
      String locale = null;

      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      if ( clientStateMap.get( "claimId" ) != null )
      {
        claimId = (Long)clientStateMap.get( "claimId" );
      }
      else
      {
        throw new Exception( "Client state did not contain claim ID" );
      }

      submitterId = UserManager.getUserId();
      locale = UserManager.getUserLocale();

      // Retrieve page data from stored procedure
      Map<String, Object> outPut = getNominationService().getNominationMoreInfoPageData( claimId, submitterId, locale );
      // Convert from value bean to view object
      moreInfoView = buildPageDataView( claimId, outPut, request );

      writeAsJsonToResponse( moreInfoView, response );
    }
    catch( Exception e )
    {
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

  private NominationMoreInfoViewBean buildPageDataView( Long claimId, Map<String, Object> pageValueBeanOutPut, HttpServletRequest request )
  {
    NominationMoreInfoValueBean nominationMoreInfoValueBean = (NominationMoreInfoValueBean)pageValueBeanOutPut.get( OUTPUT_DATA );
    
    Long promotionId = getPromotionService().getPromotionIdByClaimId( claimId );
    
    NominationMoreInfoViewBean pageView = new NominationMoreInfoViewBean();
    pageView.setNominationName( nominationMoreInfoValueBean.getPromotionName() );
    pageView.setApproversMessage( nominationMoreInfoValueBean.getApproverComments() );
    pageView.setAttachmentName( nominationMoreInfoValueBean.getSubmissionWhyText() );
    
 // Client customization for WIP #39189 starts
    NominationPromotion nominationPromotion = (NominationPromotion)getPromotionService().getPromotionById( promotionId );
    pageView.setMinDocsAllowed( nominationPromotion.getFileMinNumber() );
    pageView.setMaxDocsAllowed( nominationPromotion.getFileMaxNumber() );
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_DOCUMENTS ) );
    NominationClaim nomClaim = (NominationClaim)getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
    nomClaim.getClaimFiles().forEach( claimFile ->
    {
      pageView.getNominationLinks().add( new NominationSubmitDataAttachmentViewBean( claimFile.getId(), claimFile.getFileUrl(), claimFile.getFileUrl(), claimFile.getFileName() ) );
    } );
    pageView.setUpdatedDocCount( nomClaim.getClaimFiles().size() );
    // Client customization for WIP #39189 ends

    List<NominationMoreInfoDetailViewBean> detailPageViewList = new ArrayList<NominationMoreInfoDetailViewBean>();
    NominationMoreInfoDetailViewBean detailPageView = new NominationMoreInfoDetailViewBean();

    List<NominatorInfoViewBean> nominatorInfoPageViewList = new ArrayList<NominatorInfoViewBean>();
    NominatorInfoViewBean nominatorInfoPageView = new NominatorInfoViewBean();
    nominatorInfoPageView.setId( nominationMoreInfoValueBean.getParticipantId() );
    if ( StringUtils.isNotBlank( nominationMoreInfoValueBean.getNominatorLastName() ) && StringUtils.isNotBlank( nominationMoreInfoValueBean.getNominatorFirstName() ) )
    {
      nominatorInfoPageView.setName( nominationMoreInfoValueBean.getNominatorLastName() + ", " + nominationMoreInfoValueBean.getNominatorFirstName() );
    }
    else
    {
      List<NominationMoreInfoPageTeamValueBean> teamValueBeanList = (List<NominationMoreInfoPageTeamValueBean>)pageValueBeanOutPut.get( OUTPUT_TEAM_DATA );

      for ( NominationMoreInfoPageTeamValueBean teamValueBean : teamValueBeanList )
      {

        NominationSubmissionTeamViewBean teamViewBean = new NominationSubmissionTeamViewBean();
        teamViewBean.setUserId( teamValueBean.getUserId() );
        teamViewBean.setFirstName( teamValueBean.getFirstName() );
        teamViewBean.setLastName( teamValueBean.getLastName() );
        nominatorInfoPageView.getTeamMembers().add( teamViewBean );
      }
      nominatorInfoPageView.setName( nominationMoreInfoValueBean.getTeamName() );
    }
    nominatorInfoPageView.setFirstName( nominationMoreInfoValueBean.getNominatorFirstName() );
    nominatorInfoPageView.setLastName( nominationMoreInfoValueBean.getNominatorLastName() );
    nominatorInfoPageView.setTitle( nominationMoreInfoValueBean.getNominatorPosition() );
    nominatorInfoPageView.setCountryCode( nominationMoreInfoValueBean.getNominatorCountryCode() );
    nominatorInfoPageView.setCountryName( nominationMoreInfoValueBean.getNominatorCountryName() );
    nominatorInfoPageView.setOrgName( nominationMoreInfoValueBean.getNominatorOrgName() );
    nominatorInfoPageView.setDepartmentName( nominationMoreInfoValueBean.getDepartmentName() );
    nominatorInfoPageView.setJobName( nominationMoreInfoValueBean.getJobName() );

    nominatorInfoPageViewList.add( nominatorInfoPageView );

    List<NominationSubmissionViewBean> orginalSubmissionPageViewList = new ArrayList<NominationSubmissionViewBean>();
    NominationSubmissionViewBean submissionPageView = new NominationSubmissionViewBean();

    List<NominationMoreInfoPageBehaviorValueBean> behaviorValueBeanList = (List<NominationMoreInfoPageBehaviorValueBean>)pageValueBeanOutPut.get( OUTPUT_BEHAVIOR_DATA );
    for ( NominationMoreInfoPageBehaviorValueBean behaviorValueBean : behaviorValueBeanList )
    {
      NominationSubmissionBadgeViewBean badgePageView = new NominationSubmissionBadgeViewBean();
      badgePageView.setId( behaviorValueBean.getBehaviorId().toString() );
      if ( !behaviorValueBean.getBehaviorName().isEmpty() )
      {
        submissionPageView.setBehaviors( true );
      }
      badgePageView.setBehavior( behaviorValueBean.getBehaviorName() );
      if ( StringUtils.isNotBlank( behaviorValueBean.getBadgeName() ) )
      {
        String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
        List earnedNotEarnedImageList = getGamificationService().getEarnedNotEarnedImageList( behaviorValueBean.getBadgeName() );
        Iterator itr = earnedNotEarnedImageList.iterator();
        while ( itr.hasNext() )
        {
          BadgeLibrary badgeLib = (BadgeLibrary)itr.next();
          badgePageView.setBadgeUrl( siteUrlPrefix + "/" + badgeLib.getEarnedImageSmall() );
        }
      }
      submissionPageView.getBadges().add( badgePageView );
    }

    submissionPageView.setVideoContent( StringUtils.isNotBlank( nominationMoreInfoValueBean.getVideoUrl() ) );

    if ( submissionPageView.isVideoContent() )
    {
      submissionPageView.setPosterImg( nominationMoreInfoValueBean.getVideoImage() );
      submissionPageView.setVideoWebLink( nominationMoreInfoValueBean.getVideoUrl() );
    }
    submissionPageView.setDateSubmitted( DateUtils.toDisplayString( nominationMoreInfoValueBean.getDateSubmitted() ) );
    if ( StringUtils.isNotBlank( nominationMoreInfoValueBean.getOwnCardName() ) )
    {
      submissionPageView.setEcardName( nominationMoreInfoValueBean.getCardName() );
      submissionPageView.setEcardUrl( nominationMoreInfoValueBean.getOwnCardName() );
    }
    else if ( StringUtils.isNotBlank( nominationMoreInfoValueBean.getCardImage() ) )
    {
      submissionPageView.setEcardName( nominationMoreInfoValueBean.getCardName() );
      submissionPageView.setEcardUrl( nominationMoreInfoValueBean.getCardImage() );
    }
    if ( StringUtils.isNotBlank( nominationMoreInfoValueBean.getCertificateName() ) )
    {
      submissionPageView.setCertificateUrl( nominationMoreInfoValueBean.getCertificatePreviewImage() );
    }

    submissionPageView.setAllowTranslate( !UserManager.getUserLocale().equals( nominationMoreInfoValueBean.getSubmitterLangId() ) );

    if ( StringUtils.isNotBlank( nominationMoreInfoValueBean.getCertificateName() ) )
    {
      NominationSubmissionCertificateViewBean certificatePageView = new NominationSubmissionCertificateViewBean();
      certificatePageView.setId( nominationMoreInfoValueBean.getCertificateId().toString() );
      certificatePageView.setName( nominationMoreInfoValueBean.getCertificateName() );
      certificatePageView.setImg( nominationMoreInfoValueBean.getCertificateThumbnailImage() );
      certificatePageView.setImgLg( nominationMoreInfoValueBean.getCertificatePreviewImage() );
      submissionPageView.getCertificates().add( certificatePageView );
    }

    List<NominationMoreInfoPageCustomFieldValueBean> customFieldValueBeanList = (List<NominationMoreInfoPageCustomFieldValueBean>)pageValueBeanOutPut.get( OUTPUT_CUSTOM_DATA );

    for ( NominationMoreInfoPageCustomFieldValueBean customFieldValueBean : customFieldValueBeanList )
    {
      if ( StringUtils.isNotBlank( customFieldValueBean.getName() ) && StringUtils.isNotBlank( customFieldValueBean.getDescription() ) )
      {
        NominationSubmissionCustomFieldViewBean customFieldPageView = new NominationSubmissionCustomFieldViewBean();

        if ( customFieldValueBean.isWhyField() && StringUtils.isBlank( nominationMoreInfoValueBean.getReason() ) )
        {
          submissionPageView.setReason( customFieldValueBean.getDescription() );
        }
        else
        {
          customFieldPageView.setFieldId( customFieldValueBean.getClaimStepElmtId() );
          customFieldPageView.setLabel( customFieldValueBean.getName() );
          customFieldPageView.setValue( customFieldValueBean.getDescription() );
          customFieldPageView.setSequenceNumber( customFieldValueBean.getSequenceNum() );
          submissionPageView.getCustomFields().add( customFieldPageView );
        }

      }
    }

    if ( StringUtils.isNotBlank( nominationMoreInfoValueBean.getReason() ) )
    {
      submissionPageView.setReason( nominationMoreInfoValueBean.getReason() );
    }
    orginalSubmissionPageViewList.add( submissionPageView );

    detailPageView.setOriginalSubmission( orginalSubmissionPageViewList );
    detailPageView.setNominatorInfo( nominatorInfoPageViewList );

    detailPageViewList.add( detailPageView );
    pageView.setNominationsMoreInfo( detailPageViewList );

    return pageView;
  }

  /**
   * Method for machine translation of the more information request text
   */
  public ActionForward translateComment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    NominationMoreInfoForm submitForm = (NominationMoreInfoForm)form;

    try
    {
      TranslateCommentViewBean translatedCommentViewBean = new TranslateCommentViewBean();

      Long claimId = null;

      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      if ( clientStateMap.get( "claimId" ) != null )
      {
        claimId = (Long)clientStateMap.get( "claimId" );
      }
      else
      {
        throw new Exception( "Client state did not contain claim ID" );
      }

      translatedCommentViewBean = getClaimService().getTranslatedComments( claimId, UserManager.getUserId() );

      writeAsJsonToResponse( translatedCommentViewBean, response );
    }
    catch( Exception e )
    {
      writeAppErrorAsJsonResponse( response, e );
    }

    return null;
  }

  public ActionForward submitNomData( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {

    NominationMoreInfoForm nomForm = (NominationMoreInfoForm)form;

    Long claimId = null;

    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

    if ( clientStateMap.get( "claimId" ) != null )
    {
      claimId = (Long)clientStateMap.get( "claimId" );
    }
    else
    {
      throw new Exception( "Client state did not contain claim ID" );
    }

    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
    Claim claim = getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );
    NominationClaim nomClaim = (NominationClaim)claim;

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
          if ( claimRecipient.getCurrentClaimItemApprover().getApprovalStatusType().getCode().equals( ApprovalStatusType.MORE_INFO ) )
          {
            claimRecipient.getCurrentClaimItemApprover().getApprovalStatusType().setCode( ApprovalStatusType.PENDING );
            ApprovableItemApprover claimItemApprover = claimRecipient.getCurrentClaimItemApprover();
            approver = (Participant)claimItemApprover.getApproverUser();
            nominee = claimRecipient.getRecipient();
          }
        }
      }
    }
    if ( !nomForm.getMoreInformation().isEmpty() )
    {
      nomClaim.setMoreInfoComments( nomForm.getMoreInformation() );
    }
   // Client customization for WIP #39189 starts
    // Create new set of claim files
    Set<TcccClaimFile> claimFiles = buildClaimFiles( nomForm );
    if ( claimFiles.size() > 0 )
    {
      for ( TcccClaimFile claimFile : claimFiles )
      {
        nomClaim.addClaimFile( claimFile );
      }
    }
    // Client customization for WIP #39189 ends

    getClaimService().saveClaim( nomClaim );

    Message message = getMessageService().getMessageByCMAssetCode( MessageService.APPROVER_REQUEST_MORE_INFO_RECEIVED );
    AssociationRequestCollection arc = new AssociationRequestCollection();
    arc.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    Promotion promotion = getPromotionService().getPromotionByIdWithAssociations( nomClaim.getPromotion().getId(), arc );
    for ( Iterator notificationIter = promotion.getPromotionNotifications().iterator(); notificationIter.hasNext(); )
    {
      PromotionNotification notification = (PromotionNotification)notificationIter.next();
      if ( message.getId().equals( notification.getNotificationMessageId() ) && notification.getNotificationMessageId() != -1 )
      {
        Mailing mailing = getMailingService().buildApproverRequestForMoreInfoReceived( nomClaim, approver, nominee, message, nomForm.getMoreInformation(), false );

        if ( mailing != null )
        {
          getMailingService().submitMailing( mailing, null );
        }
      }
      else if ( !message.getId().equals( notification.getNotificationMessageId() ) && notification.getNotificationMessageId() != -1 )
      {
        Message customMessage = getMessageService().getMessageById( notification.getNotificationMessageId() );
        if ( message.getMessageTypeCode().getCode().equals( customMessage.getMessageTypeCode().getCode() ) )
        {
          Mailing mailing = getMailingService().buildApproverRequestForMoreInfoReceived( nomClaim, approver, nominee, customMessage, nomForm.getMoreInformation(), false );

          if ( mailing != null )
          {
            getMailingService().submitMailing( mailing, null );
          }
        }
      }
    }

    String returnUrl = RequestUtils.getBaseURI( request ) + "/participantProfilePage.do#tab/AlertsAndMessages";
    response.sendRedirect( returnUrl );

    return null;
  }
  
//Client customization for WIP #39189 starts
 private Set<TcccClaimFile> buildClaimFiles( NominationMoreInfoForm nomForm )
 {
   Set<TcccClaimFile> claimFiles = new LinkedHashSet<TcccClaimFile>();
   if ( nomForm.getNominationLinks() != null )
   {
     for ( NominationSubmitDataAttachmentValueBean claimFile : nomForm.getNominationLinks() )
     {
       TcccClaimFile file = new TcccClaimFile();
       file.setFileName( claimFile.getFileName() );
       file.setFileUrl( StringUtil.isNullOrEmpty( claimFile.getNominationLink() ) ? claimFile.getNominationUrl() : claimFile.getNominationLink() );
       claimFiles.add( file );
       //String nominationFileUrl = StringUtil.isNullOrEmpty( claimFile.getNominationLink() ) ? claimFile.getNominationUrl() : claimFile.getNominationLink();
       // An attached URL needs to have http before we save it, one way or another
       //if ( !nominationFileUrl.startsWith( "http://" ) || !nominationFileUrl.startsWith( "https://" ) )
       //  nominationFileUrl = "http://" + nominationFileUrl;
       
     }
   }
   return claimFiles;
 }
 // Client customization for WIP #39189 ends

  public ActionForward uploadWhyAttachment( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    JsonResponseMessageList<NominationsUploadDocView> jsonResponse = new JsonResponseMessageList<NominationsUploadDocView>();

    NominationMoreInfoForm nomForm = (NominationMoreInfoForm)form;
    String fileUrl = null;
    String fileName = null;

    Long claimId = null;
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

    if ( clientStateMap.get( "claimId" ) != null )
    {
      claimId = (Long)clientStateMap.get( "claimId" );
    }
    else
    {
      throw new Exception( "Client state did not contain claim ID" );
    }

    try
    {
      fileName = nomForm.getNominationDoc().getFileName();
      Attachmentvalidator validator = new Attachmentvalidator( nomForm.getNominationDoc() );
      List<String> validationResult = validator.validateAttachment();

      if ( validationResult.size() > 0 )
      {
        jsonResponse.getMessages().addAll( getAttachmentvalidationMessage( validationResult ) );
        writeAsJsonToResponse( jsonResponse, response );
        return null;
      }

      fileUrl = uploadToWebdav( nomForm.getNominationDoc() );
      getNominationClaimService().addWhyAttachmentUrlReference( claimId, fileUrl, fileName );
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

  private static FileUploadStrategy getWebDavFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.WEBDAV );
  }

  /**
   * Gets the password for the client state. Either the password or the global password.
   */
  private String getCryptoPass()
  {
    String cryptoPass = ClientStatePasswordManager.getPassword();
    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      cryptoPass = ClientStatePasswordManager.getGlobalPassword();
    }
    return cryptoPass;
  }

  private NominationService getNominationService()
  {
    return (NominationService)BeanLocator.getBean( NominationService.BEAN_NAME );
  }

  private GamificationService getGamificationService()
  {
    return (GamificationService)getService( GamificationService.BEAN_NAME );
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

  private static FileUploadStrategy getAppDataFileUploadStrategy()
  {
    return (FileUploadStrategy)BeanLocator.getBean( FileUploadStrategy.APPDATADIR );
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

      if ( docList.contains( fileFormat ) )
      {
        validateDocumentType( formFile, validationResult );
      }
      else if ( imageList.contains( fileFormat ) )
      {
        validateImageType( formFile, validationResult );
      }
      else if ( videoList.contains( fileFormat ) )
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

  private boolean canSubmitClaimToday( NominationSubmitForm form )
  {
    String timeZoneID = UserManager.getTimeZoneID();
    return getNominationPromotionService().canSubmitClaimToday( form.getPromotionId(), timeZoneID, UserManager.getUserId() );
  }

  private SystemVariableService getSystemvariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
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
