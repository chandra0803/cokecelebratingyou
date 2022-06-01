
package com.biperf.core.domain.publicrecognition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.biperf.core.value.BudgetValueBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_NULL )
public class PublicRecognitionRecognitionView
{

  @JsonIgnore
  private Long id;
  private String publicRecognitionPageDetailUrl;
  private Set<PublicRecognitionParticipantView> recipients = new HashSet<PublicRecognitionParticipantView>();
  private PublicRecognitionParticipantView recognizer = new PublicRecognitionParticipantView();
  private boolean isMine;
  private boolean isPublicClaim;
  private String comment;
  private boolean allowTranslate;
  private String purlUrl;
  private String promotionType;
  private String promotionName;
  private int numLikers;
  private boolean isLiked;
  private boolean isHidden;
  private String time;
  private List<BudgetValueBean> budgets = new ArrayList<BudgetValueBean>();
  private boolean allowAddPoints;
  private Double countryRatio;
  private Long awardAmountFixed;
  private Long awardAmountMin;
  private Long awardAmountMax;
  private List<PublicRecognitionCommentViewBean> comments = new ArrayList<PublicRecognitionCommentViewBean>();
  private List<PublicRecognitionShareLinkBean> shareLinks = new ArrayList<PublicRecognitionShareLinkBean>();
  private String avatarUrl = null;

  @JsonIgnore
  private Long teamId;
  private String teamName;

  private String eCardImg;
  private String videoUrl;
  private String videoThumbUrl;
  private String badgeImg;
  private String badgeName;
  private long additionalBadges;

  // To display it in PR wall for New SA enabled
  private Long recognitionSequence;
  private String primaryColor;
  private String secondaryColor;
  private String celebMessage;
  private String celebImgDesc;
  private String programHeader;
  private Long uniqueId;
  private String celebrationId;
  private String celebImageUrl;
  private Long claimId;
  
  // Client customizations for WIP #62128 starts
  private boolean cheersPromotion;

  public boolean isCheersPromotion()
  {
    return cheersPromotion;
  }

  public void setCheersPromotion( boolean cheersPromotion )
  {
    this.cheersPromotion = cheersPromotion;
  }

  // Client customizations for WIP #62128 end

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getCelebImageUrl()
  {
    return celebImageUrl;
  }

  public void setCelebImageUrl( String celebImageUrl )
  {
    this.celebImageUrl = celebImageUrl;
  }

  public String getCelebrationId()
  {
    return celebrationId;
  }

  public void setCelebrationId( String celebrationId )
  {
    this.celebrationId = celebrationId;
  }

  @JsonProperty( "id" )
  public Long getUniqueId()
  {
    return uniqueId;
  }

  public void setUniqueId( Long uniqueId )
  {
    this.uniqueId = uniqueId;
  }

  public String getCelebMessage()
  {
    return celebMessage;
  }

  public void setCelebMessage( String celebMessage )
  {
    this.celebMessage = celebMessage;
  }

  public String getCelebImgDesc()
  {
    return celebImgDesc;
  }

  public void setCelebImgDesc( String celebImgDesc )
  {
    this.celebImgDesc = celebImgDesc;
  }

  public String getProgramHeader()
  {
    return programHeader;
  }

  public void setProgramHeader( String programHeader )
  {
    this.programHeader = programHeader;
  }

  public Long getRecognitionSequence()
  {
    return recognitionSequence;
  }

  public void setRecognitionSequence( Long recognitionSequence )
  {
    this.recognitionSequence = recognitionSequence;
  }

  public String getPrimaryColor()
  {
    return primaryColor;
  }

  public void setPrimaryColor( String primaryColor )
  {
    this.primaryColor = primaryColor;
  }

  public String getSecondaryColor()
  {
    return secondaryColor;
  }

  public void setSecondaryColor( String secondaryColor )
  {
    this.secondaryColor = secondaryColor;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getPublicRecognitionPageDetailUrl()
  {
    return publicRecognitionPageDetailUrl;
  }

  public void setPublicRecognitionPageDetailUrl( String publicRecognitionPageDetailUrl )
  {
    this.publicRecognitionPageDetailUrl = publicRecognitionPageDetailUrl;
  }

  public Set<PublicRecognitionParticipantView> getRecipients()
  {
    return recipients;
  }

  public void setRecipients( Set<PublicRecognitionParticipantView> recipients )
  {
    this.recipients = recipients;
  }

  public PublicRecognitionParticipantView getRecognizer()
  {
    return recognizer;
  }

  public void setRecognizer( PublicRecognitionParticipantView recognizer )
  {
    this.recognizer = recognizer;
  }

  @JsonProperty( "isMine" )
  public boolean isMine()
  {
    return isMine;
  }

  public void setMine( boolean isMine )
  {
    this.isMine = isMine;
  }

  @JsonProperty( "isPublicClaim" )
  public boolean isPublicClaim()
  {
    return isPublicClaim;
  }

  public void setPublicClaim( boolean isPublicClaim )
  {
    this.isPublicClaim = isPublicClaim;
  }

  public void setPurlUrl( String purlUrl )
  {
    this.purlUrl = purlUrl;
  }

  public String getPurlUrl()
  {
    return purlUrl;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getComment()
  {
    return comment;
  }

  public void setComment( String comment )
  {
    this.comment = comment;
  }

  public boolean getAllowTranslate()
  {
    return allowTranslate;
  }

  public void setAllowTranslate( boolean allowTranslate )
  {
    this.allowTranslate = allowTranslate;
  }

  public int getNumLikers()
  {
    return numLikers;
  }

  public void setNumLikers( int numLikers )
  {
    this.numLikers = numLikers;
  }

  @JsonProperty( "isLiked" )
  public boolean isLiked()
  {
    return isLiked;
  }

  public void setLiked( boolean isLiked )
  {
    this.isLiked = isLiked;
  }

  public String getTime()
  {
    return time;
  }

  public void setTime( String time )
  {
    this.time = time;
  }

  public List<BudgetValueBean> getBudgets()
  {
    return budgets;
  }

  public void setBudgets( List<BudgetValueBean> budgets )
  {
    this.budgets = budgets;
  }

  @JsonProperty( "allowAddPoints" )
  public boolean isAllowAddPoints()
  {
    return allowAddPoints;
  }

  public void setAllowAddPoints( boolean allowAddPoints )
  {
    this.allowAddPoints = allowAddPoints;
  }

  public Double getCountryRatio()
  {
    return countryRatio;
  }

  public void setCountryRatio( Double countryRatio )
  {
    this.countryRatio = countryRatio;
  }

  public List<PublicRecognitionCommentViewBean> getComments()
  {
    return comments;
  }

  public void setComments( List<PublicRecognitionCommentViewBean> comments )
  {
    this.comments = comments;
  }

  public List<PublicRecognitionShareLinkBean> getShareLinks()
  {
    return shareLinks;
  }

  public void setShareLinks( List<PublicRecognitionShareLinkBean> shareLinks )
  {
    this.shareLinks = shareLinks;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public Long getAwardAmountFixed()
  {
    return awardAmountFixed;
  }

  public void setAwardAmountFixed( Long awardAmountFixed )
  {
    this.awardAmountFixed = awardAmountFixed;
  }

  public Long getAwardAmountMin()
  {
    return awardAmountMin;
  }

  public void setAwardAmountMin( Long awardAmountMin )
  {
    this.awardAmountMin = awardAmountMin;
  }

  public Long getAwardAmountMax()
  {
    return awardAmountMax;
  }

  public void setAwardAmountMax( Long awardAmountMax )
  {
    this.awardAmountMax = awardAmountMax;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  @JsonProperty( "isHidden" )
  public boolean isHidden()
  {
    return isHidden;
  }

  public void setHidden( boolean isHidden )
  {
    this.isHidden = isHidden;
  }

  /**
   * merge a recognition from the same team into this recognition view
   * 
   * @param recognitions
   * @return List<PublicRecognitionRecognitionView>
   */
  public void mergeTeamClaim( PublicRecognitionRecognitionView recognition )
  {
    this.allowAddPoints = false;
    if ( recognition.isLiked() )
    {
      this.isLiked = true;
    }
    if ( recognition.isMine() )
    {
      this.isMine = true;
      this.shareLinks = recognition.getShareLinks();
    }

    this.recipients.addAll( recognition.getRecipients() );
  }

  public String geteCardImg()
  {
    return eCardImg;
  }

  public void seteCardImg( String eCardImg )
  {
    this.eCardImg = eCardImg;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public String getBadgeImg()
  {
    return badgeImg;
  }

  public void setBadgeImg( String badgeImg )
  {
    this.badgeImg = badgeImg;
  }

  public String getBadgeName()
  {
    return badgeName;
  }

  public void setBadgeName( String badgeName )
  {
    this.badgeName = badgeName;
  }

  public long getAdditionalBadges()
  {
    return additionalBadges;
  }

  public void setAdditionalBadges( long additionalBadges )
  {
    this.additionalBadges = additionalBadges;
  }

  public String getVideoThumbUrl()
  {
    return videoThumbUrl;
  }

  public void setVideoThumbUrl( String videoThumbUrl )
  {
    this.videoThumbUrl = videoThumbUrl;
  }

}
