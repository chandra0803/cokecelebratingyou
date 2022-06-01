
package com.biperf.core.value.nomination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;

import com.biperf.core.domain.enums.NominationAwardGroupType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a promotion (in JSON form) in the nomination submission wizard
 */
public class NominationSubmitDataPromotionValueBean implements Serializable
{

  private static final long serialVersionUID = 1L;
  private Long id;
  private String name;
  private String rulesText;
  private String individualOrTeam;
  private String nominatingType;
  private boolean awardsActive;
  private boolean behaviorsActive;
  private boolean eCardsActive;
  private boolean customFieldsActive;
  private Integer maxParticipants;
  private String awardType = "none";
  private String awardMin;
  private String awardMax;
  private String awardFixed;
  private Long awardQuantity;
  private String currentStep = "stepNominee";
  private Long claimId;
  private Boolean defaultWhyActive = true;
  private Boolean customBeforeDefault = false;
  private Boolean attachmentActive = false;
  private String whyTabLabel;
  private int totalPromotionCount;
  private String currencyLabel;

  private List<NominationSubmitDataBehaviorValueBean> behaviors;
  private NominationSubmitDataDrawSettingsValueBean drawToolSettings;
  // Client customization for WIP #39189 starts
  private List<NominationSubmitDataAttachmentValueBean> nominationLinks;
  // Client customization for WIP #39189 ends
  private List<NominationSubmitDataECardValueBean> eCards;

  // JSON Ignore fields
  private String cmAssetCode;
  private String webRulesCmKey;
  private String promoNameAssetCode;

  private String _comment;
  // ecard
  private boolean cardCanEdit;
  private String cardData;
  private String ownCardName;
  private String cardType;
  private Long cardId;
  private String cardUrl;
  private String drawingData;
  private String drawingDataUrl;
  private String nomineeType;
  private String teamName;
  private boolean saveTeamAsGroup;
  private String groupName;
  private boolean isEditMode;
  private String comments;
  private boolean privateNomination;
  private String videoUrl;
  private String videoImageUrl;

  private boolean recommendedAward;

//  private String nominationUrl;
//  private String nominationLink;
  private String fileName;
  private boolean addAttachment = false;
  private boolean moreThanOneBehavioursAllowed = true;
  private int maxBehaviorsAllowed;
  private String recipientName;
  
//Client customization for WIP #39189 starts
 private int minDocsAllowed;
 private int minLinksAllowed;
 private int maxDocsAllowed;
 private int maxLinksAllowed;
 private int updatedDocCount;
 // Client customization for WIP #39189 ends
 
  // Client customizations for WIP #59418
  private String teamNameCopyBlock;

  /**
   * Default constructor to initialize empty lists
   */
  public NominationSubmitDataPromotionValueBean()
  {

  }

  @JsonProperty( "id" )
  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "rulesText" )
  public String getRulesText()
  {
    return rulesText;
  }

  public void setRulesText( String rulesText )
  {
    this.rulesText = rulesText;
  }

  @JsonProperty( "individualOrTeam" )
  public String getIndividualOrTeam()
  {
    return individualOrTeam;
  }

  public void setIndividualOrTeam( String individualOrTeam )
  {
    this.individualOrTeam = individualOrTeam;
  }

  @JsonProperty( "awardsActive" )
  public boolean isAwardsActive()
  {
    return awardsActive;
  }

  public void setAwardsActive( boolean awardsActive )
  {
    this.awardsActive = awardsActive;
  }

  @JsonProperty( "behaviorsActive" )
  public boolean isBehaviorsActive()
  {
    return behaviorsActive;
  }

  public void setBehaviorsActive( boolean behaviorsActive )
  {
    this.behaviorsActive = behaviorsActive;
  }

  @JsonProperty( "eCardsActive" )
  public boolean iseCardsActive()
  {
    return eCardsActive;
  }

  public void seteCardsActive( boolean eCardsActive )
  {
    this.eCardsActive = eCardsActive;
  }

  @JsonProperty( "customFieldsActive" )
  public boolean isCustomFieldsActive()
  {
    return customFieldsActive;
  }

  public void setCustomFieldsActive( boolean customFieldsActive )
  {
    this.customFieldsActive = customFieldsActive;
  }

  @JsonProperty( "maxParticipants" )
  public Integer getMaxParticipants()
  {
    return maxParticipants;
  }

  public void setMaxParticipants( Integer maxParticipants )
  {
    this.maxParticipants = maxParticipants;
  }

  @JsonProperty( "awardType" )
  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  @JsonProperty( "awardMin" )
  public String getAwardMin()
  {
    return awardMin;
  }

  public void setAwardMin( String awardMin )
  {
    this.awardMin = awardMin;
  }

  @JsonProperty( "awardMax" )
  public String getAwardMax()
  {
    return awardMax;
  }

  public void setAwardMax( String awardMax )
  {
    this.awardMax = awardMax;
  }

  @JsonProperty( "awardFixed" )
  public String getAwardFixed()
  {
    return awardFixed;
  }

  public void setAwardFixed( String awardFixed )
  {
    this.awardFixed = awardFixed;
  }

  @SuppressWarnings( "unchecked" )
  @JsonProperty( "behaviors" )
  public List<NominationSubmitDataBehaviorValueBean> getBehaviors()
  {

    if ( behaviors == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationSubmitDataBehaviorValueBean();
        }
      };
      behaviors = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return behaviors;
  }

  public void setBehaviors( List<NominationSubmitDataBehaviorValueBean> behaviors )
  {
    this.behaviors = behaviors;
  }

  public void addBehavior( NominationSubmitDataBehaviorValueBean behavior )
  {
    this.behaviors.add( behavior );
  }

  @JsonProperty( "drawToolSettings" )
  public NominationSubmitDataDrawSettingsValueBean getDrawToolSettings()
  {
    if ( drawToolSettings == null )
    {
      return new NominationSubmitDataDrawSettingsValueBean();
    }

    return drawToolSettings;
  }

  public void setDrawToolSettings( NominationSubmitDataDrawSettingsValueBean drawToolSettings )
  {
    this.drawToolSettings = drawToolSettings;
  }
  
//Client customization for WIP #39189 starts
 @SuppressWarnings( "unchecked" )
 @JsonProperty( "nominationLinks" )
 public List<NominationSubmitDataAttachmentValueBean> getNominationLinks()
 {
   if ( nominationLinks == null )
   {
     Factory factory = new Factory()
     {
       public Object create()
       {
         return new NominationSubmitDataAttachmentValueBean();
       }
     };
     nominationLinks = ListUtils.lazyList( new ArrayList<>(), factory );
   }
   return nominationLinks;
 }

 public void setNominationLinks( List<NominationSubmitDataAttachmentValueBean> nominationLinks )
 {
   this.nominationLinks = nominationLinks;
 }

 @SuppressWarnings( "unchecked" )
 public void addNominationLinks( NominationSubmitDataAttachmentValueBean nominationLink )
 {
   if ( nominationLinks == null )
   {
     Factory factory = new Factory()
     {
       public Object create()
       {
         return new NominationSubmitDataAttachmentValueBean();
       }
     };
     nominationLinks = ListUtils.lazyList( new ArrayList<>(), factory );
   }
   this.nominationLinks.add( nominationLink );
 }
 // Client customization for WIP #39189 ends


  @SuppressWarnings( "unchecked" )
  @JsonProperty( "eCards" )
  public List<NominationSubmitDataECardValueBean> geteCards()
  {

    if ( eCards == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationSubmitDataECardValueBean();
        }
      };
      eCards = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return eCards;
  }

  public void seteCards( List<NominationSubmitDataECardValueBean> eCards )
  {
    this.eCards = eCards;
  }

  @SuppressWarnings( "unchecked" )
  public void addeCard( NominationSubmitDataECardValueBean eCard )
  {

    if ( eCards == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationSubmitDataECardValueBean();
        }
      };
      eCards = ListUtils.lazyList( new ArrayList<>(), factory );
    }

    this.eCards.add( eCard );
  }

  @JsonIgnore
  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  @JsonIgnore
  public String getWebRulesCmKey()
  {
    return webRulesCmKey;
  }

  public void setWebRulesCmKey( String webRulesCmKey )
  {
    this.webRulesCmKey = webRulesCmKey;
  }

  @JsonIgnore
  public String getPromoNameAssetCode()
  {
    return promoNameAssetCode;
  }

  public void setPromoNameAssetCode( String promoNameAssetCode )
  {
    this.promoNameAssetCode = promoNameAssetCode;
  }

  @JsonProperty( "currentStep" )
  public String getCurrentStep()
  {
    return currentStep;
  }

  public void setCurrentStep( String currentStep )
  {
    this.currentStep = currentStep;
  }

  @JsonProperty( "claimId" )
  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  @JsonProperty( "defaultWhyActive" )
  public Boolean getDefaultWhyActive()
  {
    return defaultWhyActive;
  }

  public void setDefaultWhyActive( Boolean defaultWhyActive )
  {
    this.defaultWhyActive = defaultWhyActive;
  }

  @JsonProperty( "customBeforeDefault" )
  public Boolean getCustomBeforeDefault()
  {
    return customBeforeDefault;
  }

  public void setCustomBeforeDefault( Boolean customBeforeDefault )
  {
    this.customBeforeDefault = customBeforeDefault;
  }

  @JsonProperty( "attachmentActive" )
  public Boolean getAttachmentActive()
  {
    return attachmentActive;
  }

  public void setAttachmentActive( Boolean attachmentActive )
  {
    this.attachmentActive = attachmentActive;
  }

  @JsonProperty( "whyTabLabel" )
  public String getWhyTabLabel()
  {
    return whyTabLabel;
  }

  public void setWhyTabLabel( String whyTabLabel )
  {
    this.whyTabLabel = whyTabLabel;
  }

  @JsonProperty( "totalPromotionCount" )
  public int getTotalPromotionCount()
  {
    return totalPromotionCount;
  }

  public void setTotalPromotionCount( int totalPromotionCount )
  {
    this.totalPromotionCount = totalPromotionCount;
  }

  public String get_comment()
  {
    return _comment;
  }

  public void set_comment( String _comment )
  {
    this._comment = _comment;
  }

  public String getCardType()
  {
    return cardType;
  }

  public void setCardType( String cardType )
  {
    this.cardType = cardType;
  }

  public Long getCardId()
  {
    return cardId;
  }

  public void setCardId( Long cardId )
  {
    this.cardId = cardId;
  }

  public String getCardUrl()
  {
    return cardUrl;
  }

  public void setCardUrl( String cardUrl )
  {
    this.cardUrl = cardUrl;
  }

  public String getDrawingData()
  {
    return drawingData;
  }

  public void setDrawingData( String drawingData )
  {
    this.drawingData = drawingData;
  }

  public String getDrawingDataUrl()
  {
    return drawingDataUrl;
  }

  public void setDrawingDataUrl( String drawingDataUrl )
  {
    this.drawingDataUrl = drawingDataUrl;
  }

  /*
   * public Long geteCardId() { return eCardId; } public void seteCardId( Long eCardId ) {
   * this.eCardId = eCardId; }
   */

  public String getNomineeType()
  {
    return nomineeType;
  }

  public void setNomineeType( String nomineeType )
  {
    this.nomineeType = nomineeType;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public boolean isSaveTeamAsGroup()
  {
    return saveTeamAsGroup;
  }

  public void setSaveTeamAsGroup( boolean saveTeamAsGroup )
  {
    this.saveTeamAsGroup = saveTeamAsGroup;
  }

  public String getGroupName()
  {
    return groupName;
  }

  public void setGroupName( String groupName )
  {
    this.groupName = groupName;
  }

  @JsonProperty( "isEditMode" )
  public boolean isEditMode()
  {
    return isEditMode;
  }

  public void setIsEditMode( boolean isEditMode )
  {
    this.isEditMode = isEditMode;
  }

  public enum AwardTypesCodes
  {

    POINTS_FIXED( "points", "pointsRange" ), POINTS_RANGE( "points", "pointsRange" );

    private String code;
    private String name;

    private AwardTypesCodes( String code, String name )
    {

    }

    public String getCode()
    {
      return code;
    }

    public String getName()
    {
      return name;
    }

  }

  public String getCurrencyLabel()
  {
    return currencyLabel;
  }

  public void setCurrencyLabel( String currencyLabel )
  {
    this.currencyLabel = currencyLabel;
  }

  public String getNominatingType()
  {
    return nominatingType;
  }

  public boolean isTeamSelectedByNominator()
  {
    return NominationAwardGroupType.TEAM.equalsIgnoreCase( getIndividualOrTeam() );
  }

  public boolean isIndividualSelectedByNominator()
  {
    return NominationAwardGroupType.INDIVIDUAL.equalsIgnoreCase( getIndividualOrTeam() );
  }

  public void setNominatingType( String nominatingType )
  {
    this.nominatingType = nominatingType;
  }

  /*
   * public CustomFormStepElements getCustomElements() { return customElements; } public void
   * setCustomElements( CustomFormStepElements customElements ) { this.customElements =
   * customElements; }
   */

  public boolean isCardCanEdit()
  {
    return cardCanEdit;
  }

  public void setCardCanEdit( boolean cardCanEdit )
  {
    this.cardCanEdit = cardCanEdit;
  }

  public String getCardData()
  {
    return cardData;
  }

  public void setCardData( String cardData )
  {
    this.cardData = cardData;
  }

  public String getOwnCardName()
  {
    return ownCardName;
  }

  public void setOwnCardName( String ownCardName )
  {
    this.ownCardName = ownCardName;
  }

  public String getComments()
  {
    return comments;
  }

  public void setComments( String comments )
  {
    this.comments = comments;
  }

  public boolean isPrivateNomination()
  {
    return privateNomination;
  }

  public void setPrivateNomination( boolean privateNomination )
  {
    this.privateNomination = privateNomination;
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

  public boolean isRecommendedAward()
  {
    return recommendedAward;
  }

  public void setRecommendedAward( boolean recommendedAward )
  {
    this.recommendedAward = recommendedAward;
  }


  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }


  public boolean isAddAttachment()
  {
    return addAttachment;
  }

  public void setAddAttachment( boolean addAttachment )
  {
    this.addAttachment = addAttachment;
  }

  public boolean isMoreThanOneBehavioursAllowed()
  {
    return moreThanOneBehavioursAllowed;
  }

  public void setMoreThanOneBehavioursAllowed( boolean moreThanOneBehavioursAllowed )
  {
    this.moreThanOneBehavioursAllowed = moreThanOneBehavioursAllowed;
  }

  public int getMaxBehaviorsAllowed()
  {
    return maxBehaviorsAllowed;
  }

  public void setMaxBehaviorsAllowed( int maxBehaviorsAllowed )
  {
    this.maxBehaviorsAllowed = maxBehaviorsAllowed;
  }

  public String getRecipientName()
  {
    return recipientName;
  }

  public void setRecipientName( String recipientName )
  {
    this.recipientName = recipientName;
  }

  @JsonProperty( "awardQuantity" )
  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }
  
//Client customization for WIP #39189 starts
 public int getMinDocsAllowed()
 {
   return minDocsAllowed;
 }

 public void setMinDocsAllowed( int minDocsAllowed )
 {
   this.minDocsAllowed = minDocsAllowed;
 }

 public int getMaxDocsAllowed()
 {
   return maxDocsAllowed;
 }

 public void setMaxDocsAllowed( int maxDocsAllowed )
 {
   this.maxDocsAllowed = maxDocsAllowed;
 }

 public int getMaxLinksAllowed()
 {
   return maxLinksAllowed;
 }

 public void setMaxLinksAllowed( int maxLinksAllowed )
 {
   this.maxLinksAllowed = maxLinksAllowed;
 }

 public int getMinLinksAllowed()
 {
   return minLinksAllowed;
 }

 public void setMinLinksAllowed( int minLinksAllowed )
 {
   this.minLinksAllowed = minLinksAllowed;
 }

 public int getUpdatedDocCount()
 {
   return updatedDocCount;
 }

 public void setUpdatedDocCount( int updatedDocCount )
 {
   this.updatedDocCount = updatedDocCount;
 }
 // Client customization for WIP #39189 ends

  // Client customizations for WIP #59418
  public String getTeamNameCopyBlock()
  {
    return teamNameCopyBlock;
  }

  public void setTeamNameCopyBlock( String teamNameCopyBlock )
  {
    this.teamNameCopyBlock = teamNameCopyBlock;
  }
  // Client customizations for WIP #59418 end
}
