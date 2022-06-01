
package com.biperf.core.value.claim;

import java.io.Serializable;
import java.util.Date;

import com.biperf.core.domain.enums.PromotionBehaviorType;

public class RecognitionClaimValueObject implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String promotionName;
  private Long earnings;
  private String recognitionClaimNodeName;
  private String submitterLastName;
  private String submitterFirstName;
  private String positionTypePickListName;
  private String departmentTypePickListName;
  private Long submitterId;
  private boolean nominationPromotion;
  private boolean recognitionPromotion;
  private String certificate;
  private Long certificateId;
  private boolean scoreByGiver;
  private boolean displayScores;
  private boolean open;
  private String promotionTypeCode;
  private Date submissionDate;
  private boolean recognitionClaim;
  private boolean copyManager;
  private boolean copySender;
  private Long promotionId;
  private String promotionType;
  private PromotionBehaviorType behavior;
  private Long awardQuantity;
  private String behaviorName;
  private String submitterComments;
  private Long paxSubmitterId;
  private String paxFirstName;
  private String paxLastName;
  private Long claimId;
  private Long paxId;

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  private Long clamGroupAwardQuantity;

  public Long getClamGroupAwardQuantity()
  {
    return clamGroupAwardQuantity;
  }

  public void setClamGroupAwardQuantity( Long clamGroupAwardQuantity )
  {
    this.clamGroupAwardQuantity = clamGroupAwardQuantity;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getEarnings()
  {
    return earnings;
  }

  public void setEarnings( Long earnings )
  {
    this.earnings = earnings;
  }

  public String getRecognitionClaimNodeName()
  {
    return recognitionClaimNodeName;
  }

  public void setRecognitionClaimNodeName( String recognitionClaimNodeName )
  {
    this.recognitionClaimNodeName = recognitionClaimNodeName;
  }

  public String getSubmitterLastName()
  {
    return submitterLastName;
  }

  public void setSubmitterLastName( String submitterLastName )
  {
    this.submitterLastName = submitterLastName;
  }

  public String getSubmitterFirstName()
  {
    return submitterFirstName;
  }

  public void setSubmitterFirstName( String submitterFirstName )
  {
    this.submitterFirstName = submitterFirstName;
  }

  public String getPositionTypePickListName()
  {
    return positionTypePickListName;
  }

  public void setPositionTypePickListName( String positionTypePickListName )
  {
    this.positionTypePickListName = positionTypePickListName;
  }

  public String getDepartmentTypePickListName()
  {
    return departmentTypePickListName;
  }

  public void setDepartmentTypePickListName( String departmentTypePickListName )
  {
    this.departmentTypePickListName = departmentTypePickListName;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public String getCertificate()
  {
    return certificate;
  }

  public void setCertificate( String certificate )
  {
    this.certificate = certificate;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public boolean isScoreByGiver()
  {
    return scoreByGiver;
  }

  public void setScoreByGiver( boolean scoreByGiver )
  {
    this.scoreByGiver = scoreByGiver;
  }

  public boolean isDisplayScores()
  {
    return displayScores;
  }

  public void setDisplayScores( boolean displayScores )
  {
    this.displayScores = displayScores;
  }

  public boolean isOpen()
  {
    return open;
  }

  public void setOpen( boolean open )
  {
    this.open = open;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public boolean isNominationPromotion()
  {
    return nominationPromotion;
  }

  public void setNominationPromotion( boolean nominationPromotion )
  {
    this.nominationPromotion = nominationPromotion;
  }

  public boolean isRecognitionPromotion()
  {
    return recognitionPromotion;
  }

  public void setRecognitionPromotion( boolean recognitionPromotion )
  {
    this.recognitionPromotion = recognitionPromotion;
  }

  public boolean isRecognitionClaim()
  {
    return recognitionClaim;
  }

  public void setRecognitionClaim( boolean recognitionClaim )
  {
    this.recognitionClaim = recognitionClaim;
  }

  public boolean isCopyManager()
  {
    return copyManager;
  }

  public void setCopyManager( boolean copyManager )
  {
    this.copyManager = copyManager;
  }

  public boolean isCopySender()
  {
    return copySender;
  }

  public void setCopySender( boolean copySender )
  {
    this.copySender = copySender;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public PromotionBehaviorType getBehavior()
  {
    return behavior;
  }

  public void setBehavior( PromotionBehaviorType behavior )
  {
    this.behavior = behavior;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

  public String getBehaviorName()
  {
    return behaviorName;
  }

  public void setBehaviorName( String behaviorName )
  {
    this.behaviorName = behaviorName;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public Long getPaxSubmitterId()
  {
    return paxSubmitterId;
  }

  public void setPaxSubmitterId( Long paxSubmitterId )
  {
    this.paxSubmitterId = paxSubmitterId;
  }

  public String getPaxFirstName()
  {
    return paxFirstName;
  }

  public void setPaxFirstName( String paxFirstName )
  {
    this.paxFirstName = paxFirstName;
  }

  public String getPaxLastName()
  {
    return paxLastName;
  }

  public void setPaxLastName( String paxLastName )
  {
    this.paxLastName = paxLastName;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getSubmissionDate()
  {
    return submissionDate;
  }

  public void setSubmissionDate( Date submissionDate )
  {
    this.submissionDate = submissionDate;
  }

}
