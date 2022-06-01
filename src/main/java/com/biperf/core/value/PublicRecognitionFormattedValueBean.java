
package com.biperf.core.value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.PublicRecognitionBadges;
import com.biperf.core.domain.promotion.PublicRecognitionCard;
import com.biperf.core.domain.promotion.PublicRecognitionComment;
import com.biperf.core.domain.promotion.PublicRecognitionLike;
import com.biperf.core.domain.publicrecognition.PublicRecognitionParticipantView;
import com.biperf.core.domain.publicrecognition.PublicRecognitionShareLinkBean;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HtmlUtils;

public class PublicRecognitionFormattedValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  public static final String PROMO_TYPE_NOMINATION = "nomination";
  public static final String PROMO_TYPE_PURL = "purl";
  public static final String PROMO_TYPE_RECOGNITION = "recognition";
  // Client customization for WIP #42701 starts
  public static final String PROMO_TYPE_RECOGNITION_CASH = "cash";
  // Client customization for WIP #42701 ends
  private Long claimId;
  private Date claimSubmissionDate;
  private Date claimApprovedDate;
  private Long submitterId;
  private String submitterFirstName;
  private String submitterLastName;
  private String submitterAvatarSmall;
  private Long promotionId;
  private String promotionName;
  private boolean includePurl;
  private Long purlRecipientId;
  private String submitterComments;
  private String submitterCommentsLanguageType;
  private boolean hidePublicRecognition;
  private Long teamId;
  private String teamName;
  private Long recipientId;
  private String recipientFirstName;
  private String recipientLastName;
  private String recipientAvatarSmall;
  private boolean isCumulative;

  // transient variables
  private String promotionType;
  private String purlUrl;
  private AbstractRecognitionClaim abstractRecognitionClaim;
  private Boolean eCardUsed;

  private String isMine;
  private String isCommenterMine;
  private String isLiked;
  private boolean isPublicClaim;
  private List<BudgetValueBean> budgets = new ArrayList<BudgetValueBean>();

  private double countryRatio;

  private PublicRecognitionCard card;
  private List<PublicRecognitionShareLinkBean> socialLinks;
  private List<PublicRecognitionComment> userComments;
  private List<PublicRecognitionLike> userLikes;
  private List<PublicRecognitionBadges> userBadges;

  // for budget json view
  private boolean allowAddPoints;
  private Long awardAmountMin;
  private Long awardAmountMax;
  private boolean awardAmountFixed;
  private Long fixedAwardAmount;

  private long receiverCount;
  private long likesCount;
  private long publicCommentsCount;
  private List<PublicRecognitionParticipantView> recipients;

  private String eCardImageUrl;
  private String videoUrl;
  private String videoThumbUrl;

  private Long timePeriodId;
  private Long activityId;

  // To display it in PR wall for New SA enabled
  private Long recognitionSequence;
  private String celebImgDesCmxAssetCode;
  private String celebMsgCmxAssetCode;
  private String primaryColor;
  private String secondaryColor;
  private String celebMessage;
  private String celebImgDesc;
  private String programHeader;
  private String programHeaderCmxCode;
  private Long uniqueId;
  private String celebrationId;
  private String celebImageUrl;
  private String programNameCmxCode;
  
  public String getProgramNameCmxCode()
  {
    return programNameCmxCode;
  }

  public void setProgramNameCmxCode( String programNameCmxCode )
  {
    this.programNameCmxCode = programNameCmxCode;
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

  public String getProgramHeaderCmxCode()
  {
    return programHeaderCmxCode;
  }

  public void setProgramHeaderCmxCode( String programHeaderCmxCode )
  {
    this.programHeaderCmxCode = programHeaderCmxCode;
  }

  public Long getRecognitionSequence()
  {
    return recognitionSequence;
  }

  public void setRecognitionSequence( Long recognitionSequence )
  {
    this.recognitionSequence = recognitionSequence;
  }

  public String getCelebImgDesCmxAssetCode()
  {
    return celebImgDesCmxAssetCode;
  }

  public void setCelebImgDesCmxAssetCode( String celebImgDesCmxAssetCode )
  {
    this.celebImgDesCmxAssetCode = celebImgDesCmxAssetCode;
  }

  public String getCelebMsgCmxAssetCode()
  {
    return celebMsgCmxAssetCode;
  }

  public void setCelebMsgCmxAssetCode( String celebMsgCmxAssetCode )
  {
    this.celebMsgCmxAssetCode = celebMsgCmxAssetCode;
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

  public Date getClaimApprovedDate()
  {
    return claimApprovedDate;
  }

  public void setClaimApprovedDate( Date claimApprovedDate )
  {
    this.claimApprovedDate = claimApprovedDate;
  }

  public String getRelativeClaimApprovedDate()
  {
    return claimApprovedDate != null ? DateUtils.toRelativeTimeLapsed( claimApprovedDate ) : null;
  }

  public boolean isAwardAmountFixed()
  {
    return awardAmountFixed;
  }

  public void setAwardAmountFixed( boolean awardAmountFixed )
  {
    this.awardAmountFixed = awardAmountFixed;
  }

  public Long getFixedAwardAmount()
  {
    return fixedAwardAmount;
  }

  public void setFixedAwardAmount( Long fixedAwardAmount )
  {
    this.fixedAwardAmount = fixedAwardAmount;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Date getClaimSubmissionDate()
  {
    return claimSubmissionDate;
  }

  public void setClaimSubmissionDate( Date claimSubmissionDate )
  {
    this.claimSubmissionDate = claimSubmissionDate;
  }

  public String getRelativeClaimSubmissionDate()
  {
    return DateUtils.toRelativeTimeLapsed( claimSubmissionDate );
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public String getSubmitterFirstName()
  {
    return submitterFirstName;
  }

  public void setSubmitterFirstName( String submitterFirstName )
  {
    this.submitterFirstName = submitterFirstName;
  }

  public String getSubmitterLastName()
  {
    return submitterLastName;
  }

  public void setSubmitterLastName( String submitterLastName )
  {
    this.submitterLastName = submitterLastName;
  }

  public String getSubmitterAvatarSmallFullPath()
  {
    return submitterAvatarSmall;
  }

  public void setSubmitterAvatarSmall( String submitterAvatarSmall )
  {
    this.submitterAvatarSmall = submitterAvatarSmall;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public String getSubmitterCommentsLanguageType()
  {
    return submitterCommentsLanguageType;
  }

  public void setSubmitterCommentsLanguageType( String submitterCommentsLanguageType )
  {
    this.submitterCommentsLanguageType = submitterCommentsLanguageType;
  }

  public boolean isHidePublicRecognition()
  {
    return hidePublicRecognition;
  }

  public void setHidePublicRecognition( boolean hidePublicRecognition )
  {
    this.hidePublicRecognition = hidePublicRecognition;
  }

  public Boolean iseCardUsed()
  {
    return eCardUsed;
  }

  public void seteCardUsed( Boolean eCardUsed )
  {
    this.eCardUsed = eCardUsed;
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

  public Long getRecipientId()
  {
    return recipientId;
  }

  public void setRecipientId( Long recipientId )
  {
    this.recipientId = recipientId;
  }

  public String getRecipientFirstName()
  {
    return recipientFirstName;
  }

  public void setRecipientFirstName( String recipientFirstName )
  {
    this.recipientFirstName = recipientFirstName;
  }

  public String getRecipientLastName()
  {
    return recipientLastName;
  }

  public void setRecipientLastName( String recipientLastName )
  {
    this.recipientLastName = recipientLastName;
  }

  public String getRecipientAvatarSmallFullPath()
  {
    return recipientAvatarSmall; // passing the default
                                 // avatar as null required
                                 // by FE
  }

  public void setRecipientAvatarSmall( String recipientAvatarSmall )
  {
    this.recipientAvatarSmall = recipientAvatarSmall;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public boolean isIncludePurl()
  {
    return includePurl;
  }

  public void setIncludePurl( boolean includePurl )
  {
    this.includePurl = includePurl;
  }

  public void setPurlUrl( String purlUrl )
  {
    this.purlUrl = purlUrl;
  }

  public String getPurlUrl()
  {
    return purlUrl;
  }

  public void setAbstractRecognitionClaim( AbstractRecognitionClaim abstractRecognitionClaim )
  {
    this.abstractRecognitionClaim = abstractRecognitionClaim;
  }

  public AbstractRecognitionClaim getAbstractRecognitionClaim()
  {
    return abstractRecognitionClaim;
  }

  public boolean isRecognitionClaim()
  {
    return PromotionType.RECOGNITION.equals( promotionType );
  }

  public boolean isNominationClaim()
  {
    return PromotionType.NOMINATION.equals( promotionType );
  }

  public void setPublicClaim( boolean isPublicClaim )
  {
    this.isPublicClaim = isPublicClaim;
  }

  public void setCard( PublicRecognitionCard card )
  {
    this.card = card;
  }

  public PublicRecognitionCard getCard()
  {
    return card;
  }

  public int getNumLikers()
  {
    return userLikes != null ? userLikes.size() : 0;
  }

  public String getIsLiked()
  {
    return isLiked;
  }

  public void setIsLiked( String isLiked )
  {
    this.isLiked = isLiked;
  }

  public void setIsPublicClaim( boolean isPublicClaim )
  {
    this.isPublicClaim = isPublicClaim;
  }

  public boolean getIsPublicClaim()
  {
    return isPublicClaim;
  }

  public double getCountryRatio()
  {
    return countryRatio;
  }

  public void setCountryRatio( double countryRatio )
  {
    this.countryRatio = countryRatio;
  }

  public List<PublicRecognitionShareLinkBean> getSocialLinks()
  {
    return socialLinks;
  }

  public void setSocialLinks( List<PublicRecognitionShareLinkBean> socialLinks )
  {
    this.socialLinks = socialLinks;
  }

  public List<PublicRecognitionComment> getUserComments()
  {
    return userComments;
  }

  public void setUserComments( List<PublicRecognitionComment> userComments )
  {
    this.userComments = userComments;
  }

  public List<PublicRecognitionLike> getUserLikes()
  {
    return userLikes;
  }

  public void setUserLikes( List<PublicRecognitionLike> userLikes )
  {
    this.userLikes = userLikes;
  }

  public String getIsMine()
  {
    return isMine;
  }

  public void setIsMine( String isMine )
  {
    this.isMine = isMine;
  }

  public String getIsCommenterMine()
  {
    return isCommenterMine;
  }

  public void setIsCommenterMine( String isCommenterMine )
  {
    this.isCommenterMine = isCommenterMine;
  }

  public String getComments()
  {
    String commentStr = HtmlUtils.removeFormatting( submitterComments );
    if ( null != commentStr && commentStr.length() > 100 )
    {
      commentStr = commentStr.substring( 0, 100 ) + "...";
    }
    return commentStr;
  }

  public List<BudgetValueBean> getBudgets()
  {
    return budgets;
  }

  public void setBudgets( List<BudgetValueBean> budgets )
  {
    this.budgets = budgets;
  }

  public boolean isAllowAddPoints()
  {
    return allowAddPoints;
  }

  public void setAllowAddPoints( boolean allowAddPoints )
  {
    this.allowAddPoints = allowAddPoints;
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

  public long getReceiverCount()
  {
    return receiverCount;
  }

  public void setReceiverCount( long receiverCount )
  {
    this.receiverCount = receiverCount;
  }

  public long getLikesCount()
  {
    return likesCount;
  }

  public void setLikesCount( long likesCount )
  {
    this.likesCount = likesCount;
  }

  public long getPublicCommentsCount()
  {
    return publicCommentsCount;
  }

  public void setPublicCommentsCount( long publicCommentsCount )
  {
    this.publicCommentsCount = publicCommentsCount;
  }

  public List<PublicRecognitionParticipantView> getRecipients()
  {
    return recipients;
  }

  public void setRecipients( List<PublicRecognitionParticipantView> recipients )
  {
    this.recipients = recipients;
  }

  public boolean isCumulative()
  {
    return isCumulative;
  }

  public void setCumulative( boolean isCumulative )
  {
    this.isCumulative = isCumulative;
  }

  public String geteCardImageUrl()
  {
    return eCardImageUrl;
  }

  public void seteCardImageUrl( String eCardImageUrl )
  {
    this.eCardImageUrl = eCardImageUrl;
  }

  public String getVideoUrl()
  {
    return videoUrl;
  }

  public void setVideoUrl( String videoUrl )
  {
    this.videoUrl = videoUrl;
  }

  public List<PublicRecognitionBadges> getUserBadges()
  {
    return userBadges;
  }

  public void setUserBadges( List<PublicRecognitionBadges> userBadges )
  {
    this.userBadges = userBadges;
  }

  public Long getTimePeriodId()
  {
    return timePeriodId;
  }

  public void setTimePeriodId( Long timePeriodId )
  {
    this.timePeriodId = timePeriodId;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public String getVideoThumbUrl()
  {
    return videoThumbUrl;
  }

  public void setVideoThumbUrl( String videoThumbUrl )
  {
    this.videoThumbUrl = videoThumbUrl;
  }

  public String getRequestId( String videoUrl )
  {

    return videoUrl.substring( videoUrl.lastIndexOf( ":" ) + 1 );
  }

  public String getActualCardUrl( String path )
  {
    return path.substring( 0, path.lastIndexOf( ActionConstants.REQUEST_ID ) );
  }

}
