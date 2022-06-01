
package com.biperf.core.service.claim;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.NominationClaimBehaviors;
import com.biperf.core.domain.claim.RecognitionClaimSource;
import com.biperf.core.value.client.TcccClaimFileValueBean;
import com.biperf.core.value.nomination.NominationSubmitDataPromotionValueBean;
import com.biperf.core.value.nomination.NominationsParticipantDataValueBean;
import com.biperf.core.value.promotion.CustomFormStepElementsView;

public class RecognitionClaimSubmission
{
  private RecognitionClaimSource source = RecognitionClaimSource.UNKNOWN;

  private Long submitterId;
  private Long nodeId;
  private Long promotionId;
  private Long claimId;
  private Long claimFormStepId;
  private Long proxyUserId;

  // nomination only
  private String teamName;
  private boolean draft;
  private String individualOrTeam;

  private String comments;
  private String behavior;
  private Long cardId;
  private Long certificateId;
  private String ownCardName;

  private boolean copySender;
  private boolean copyManager;
  private String copyOthers;
  private boolean privateRecognition;
  private String recipientSendDate;
  private boolean isManagerAward;
  private int anniversaryYears;
  private int anniversaryDays;

  private final List<RecognitionClaimRecipient> recognitionClaimRecipients = new ArrayList<>();
  private List<ClaimElement> claimElements = new ArrayList<>();

  // PURL
  private final List<PurlContributor> purlContributors = new ArrayList<>();

  private NominationSubmitDataPromotionValueBean nomSubmitDataPromotionValueBean;
  private List<NominationsParticipantDataValueBean.ParticipantValueBean> participants = new ArrayList<NominationsParticipantDataValueBean.ParticipantValueBean>();

  private List<NominationClaimBehaviors> nominationBehaviors = new ArrayList<NominationClaimBehaviors>();

  private CustomFormStepElementsView customElements = new CustomFormStepElementsView();

  private String videoUrl;
  private String videoImageUrl;
  private String drawingDataUrl;
  private String cardType;

  // Client customization for WIP #39189 starts
  private final List<TcccClaimFileValueBean> claimUploads = new ArrayList<>();
  // Client customization for WIP #39189 ends
  
  public RecognitionClaimSubmission( RecognitionClaimSource source, Long submitterId, Long nodeId, Long promotionId )
  {
    if ( source != null )
    {
      this.source = source;
    }

    this.submitterId = submitterId;
    this.nodeId = nodeId;
    this.promotionId = promotionId;
  }

  public RecognitionClaimRecipient addRecognitionClaimRecipient( Long userId, Long nodeId, Long awardQuantity, Long awardLevelId, String countryCode )
  {
    RecognitionClaimRecipient recipient = new RecognitionClaimRecipient( userId, nodeId, awardQuantity, awardLevelId, countryCode );
    recognitionClaimRecipients.add( recipient );
    return recipient;
  }
  public RecognitionClaimRecipient addRecognitionClaimRecipient( Long userId, Long nodeId, Long awardQuantity, Long awardLevelId, String countryCode, boolean optOut, String currency, String divisionKey )
  {
    RecognitionClaimRecipient recipient = new RecognitionClaimRecipient( userId, nodeId, awardQuantity, awardLevelId, countryCode, optOut, currency, divisionKey );
    recognitionClaimRecipients.add( recipient );
    return recipient;
  }
  
  public void addPurlContributor( String userId, String firstName, String lastName, String avatarUrl, String email, String contribType, boolean defaultInvitee )
  {
    purlContributors.add( new PurlContributor( userId, firstName, lastName, avatarUrl, email, contribType, defaultInvitee ) );
  }
  
//Client customization for WIP #39189 starts
  
 public void addClaimUploadDocument( String url, String description )
 {
   claimUploads.add( new TcccClaimFileValueBean( url, description ) );
 }
 // Client customization for WIP #39189 ends

  public RecognitionClaimSource getSource()
  {
    return source;
  }

  public RecognitionClaimSource getSourceIfNotExitsDefaultToUnknown()
  {
    return getSource() != null ? getSource() : RecognitionClaimSource.UNKNOWN;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public Long getNodeId()
  {
    return nodeId;
  }

  public void setNodeId( Long nodeId )
  {
    this.nodeId = nodeId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getClaimFormStepId()
  {
    return claimFormStepId;
  }

  public void setClaimFormStepId( Long claimFormStepId )
  {
    this.claimFormStepId = claimFormStepId;
  }

  public Long getProxyUserId()
  {
    return proxyUserId;
  }

  public void setProxyUserId( Long proxyUserId )
  {
    this.proxyUserId = proxyUserId;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public boolean isDraft()
  {
    return draft;
  }

  public void setDraft( boolean draft )
  {
    this.draft = draft;
  }

  public int getAnniversaryYears()
  {
    return anniversaryYears;
  }

  public void setAnniversaryYears( int anniversaryYears )
  {
    this.anniversaryYears = anniversaryYears;
  }

  public int getAnniversaryDays()
  {
    return anniversaryDays;
  }

  public void setAnniversaryDays( int anniversaryDays )
  {
    this.anniversaryDays = anniversaryDays;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  public Long getCardId()
  {
    return cardId;
  }

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public String getOwnCardName()
  {
    return ownCardName;
  }

  public void setOwnCardName( String ownCardName )
  {
    this.ownCardName = ownCardName;
  }

  public boolean isCopySender()
  {
    return copySender;
  }

  public void setCopySender( boolean copySender )
  {
    this.copySender = copySender;
  }

  public boolean isCopyManager()
  {
    return copyManager;
  }

  public void setCopyManager( boolean copyManager )
  {
    this.copyManager = copyManager;
  }

  public String getCopyOthers()
  {
    return copyOthers;
  }

  public void setCopyOthers( String copyOthers )
  {
    this.copyOthers = copyOthers;
  }

  public boolean isPrivateRecognition()
  {
    return privateRecognition;
  }

  public void setPrivateRecognition( boolean privateRecognition )
  {
    this.privateRecognition = privateRecognition;
  }

  public String getRecipientSendDate()
  {
    return recipientSendDate;
  }

  public void setRecipientSendDate( String recipientSendDate )
  {
    this.recipientSendDate = recipientSendDate;
  }

  public List<RecognitionClaimRecipient> getRecognitionClaimRecipients()
  {
    return recognitionClaimRecipients;
  }

  public List<PurlContributor> getPurlContributors()
  {
    return purlContributors;
  }

  public List<ClaimElement> getClaimElements()
  {
    return claimElements;
  }

  public void setClaimElements( List<ClaimElement> claimElements )
  {
    this.claimElements = claimElements;
  }

  public boolean isManagerAward()
  {
    return isManagerAward;
  }

  public void setManagerAward( boolean isManagerAward )
  {
    this.isManagerAward = isManagerAward;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public NominationSubmitDataPromotionValueBean getNomSubmitDataPromotionValueBean()
  {
    return nomSubmitDataPromotionValueBean;
  }

  public void setNomSubmitDataPromotionValueBean( NominationSubmitDataPromotionValueBean nomSubmitDataPromotionValueBean )
  {
    this.nomSubmitDataPromotionValueBean = nomSubmitDataPromotionValueBean;
  }

  public List<NominationsParticipantDataValueBean.ParticipantValueBean> getParticipants()
  {
    return participants;
  }

  public void setParticipants( List<NominationsParticipantDataValueBean.ParticipantValueBean> participants )
  {
    this.participants = participants;
  }

  public List<NominationClaimBehaviors> getNominationBehaviors()
  {
    return nominationBehaviors;
  }

  public void setNominationBehaviors( List<NominationClaimBehaviors> nominationBehaviors )
  {
    this.nominationBehaviors = nominationBehaviors;
  }

  public CustomFormStepElementsView getCustomElements()
  {
    return customElements;
  }

  public void setCustomElements( CustomFormStepElementsView customElements )
  {
    this.customElements = customElements;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String getVideoImageUrl()
  {
    return videoImageUrl;
  }

  public void setVideoImageUrl( String videoImageUrl )
  {
    this.videoImageUrl = videoImageUrl;
  }

  public String getDrawingDataUrl()
  {
    return drawingDataUrl;
  }

  public void setDrawingDataUrl( String drawingDataUrl )
  {
    this.drawingDataUrl = drawingDataUrl;
  }

  public String getCardType()
  {
    return cardType;
  }

  public void setCardType( String cardType )
  {
    this.cardType = cardType;
  }

  public String getIndividualOrTeam()
  {
    return individualOrTeam;
  }

  public void setIndividualOrTeam( String individualOrTeam )
  {
    this.individualOrTeam = individualOrTeam;
  }
  // Client customization for WIP #39189 starts
  public List<TcccClaimFileValueBean> getClaimUploads()
  {
    return claimUploads;
  }
  // Client customization for WIP #39189 ends

  
}
