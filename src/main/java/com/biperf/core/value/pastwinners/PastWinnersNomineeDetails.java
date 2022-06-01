
package com.biperf.core.value.pastwinners;

import java.math.BigDecimal;
import java.sql.Date;

import org.apache.commons.lang3.StringUtils;

public class PastWinnersNomineeDetails
{
  private Long claimId;
  private Long claimGroupId;
  private String promotionName;
  private Date dateApproved;
  private String currencyLabel;
  private String eCardName;
  private String cardVideoUrl;
  private String cardVideoImageUrl;
  private boolean teamNomination;
  private String timePeriodName;
  private String teamName;
  private String nomineeFirstName;
  private String nomineeLastName;
  private BigDecimal awardAmount;
  private Long nomineeHierarchyId;
  private String nomineeOrgName;
  private String nomineePosition;
  private String nomineeCountryCode;
  private String nomineeCountryName;
  private String nomineeAvatarUrl;
  private String submitterComments;
  private Date dateSubmitted;
  private Long certificateId;
  private String ownCardName;
  private String submitterLangId;
  private String nomineeDprtName;
  private String nominatorDprtName;
  private Long levelNumber;
  private String levelName;
  private String otherDescription;
  private Long nomineeUserId;
  private Long teamId;

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getDateApproved()
  {
    return dateApproved;
  }

  public void setDateApproved( Date dateApproved )
  {
    this.dateApproved = dateApproved;
  }

  public String getCurrencyLabel()
  {
    return currencyLabel;
  }

  public void setCurrencyLabel( String currencyLabel )
  {
    this.currencyLabel = currencyLabel;
  }

  public String geteCardName()
  {
    return eCardName;
  }

  public void seteCardName( String eCardName )
  {
    this.eCardName = eCardName;
  }

  public String getCardVideoUrl()
  {
    return cardVideoUrl;
  }

  public void setCardVideoUrl( String cardVideoUrl )
  {
    this.cardVideoUrl = cardVideoUrl;
  }

  public String getCardVideoImageUrl()
  {
    return cardVideoImageUrl;
  }

  public void setCardVideoImageUrl( String cardVideoImageUrl )
  {
    this.cardVideoImageUrl = cardVideoImageUrl;
  }

  public boolean isTeamNomination()
  {
    return teamNomination;
  }

  public void setTeamNomination( boolean teamNomination )
  {
    this.teamNomination = teamNomination;
  }

  public String getTimePeriodName()
  {
    return timePeriodName;
  }

  public void setTimePeriodName( String timePeriodName )
  {
    this.timePeriodName = timePeriodName;
  }

  public String getTeamName()
  {
    return teamName;
  }

  public void setTeamName( String teamName )
  {
    this.teamName = teamName;
  }

  public String getNomineeFirstName()
  {
    return nomineeFirstName;
  }

  public void setNomineeFirstName( String nomineeFirstName )
  {
    this.nomineeFirstName = nomineeFirstName;
  }

  public String getNomineeLastName()
  {
    return nomineeLastName;
  }

  public void setNomineeLastName( String nomineeLastName )
  {
    this.nomineeLastName = nomineeLastName;
  }

  public BigDecimal getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( BigDecimal awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public Long getNomineeHierarchyId()
  {
    return nomineeHierarchyId;
  }

  public void setNomineeHierarchyId( Long nomineeHierarchyId )
  {
    this.nomineeHierarchyId = nomineeHierarchyId;
  }

  public String getNomineeOrgName()
  {
    return nomineeOrgName;
  }

  public void setNomineeOrgName( String nomineeOrgName )
  {
    this.nomineeOrgName = nomineeOrgName;
  }

  public String getNomineePosition()
  {
    return nomineePosition;
  }

  public void setNomineePosition( String nomineePosition )
  {
    this.nomineePosition = nomineePosition;
  }

  public String getNomineeCountryCode()
  {
    return nomineeCountryCode;
  }

  public void setNomineeCountryCode( String nomineeCountryCode )
  {
    this.nomineeCountryCode = nomineeCountryCode;
  }

  public String getNomineeCountryName()
  {
    return nomineeCountryName;
  }

  public void setNomineeCountryName( String nomineeCountryName )
  {
    this.nomineeCountryName = nomineeCountryName;
  }

  public String getNomineeAvatarUrl()
  {
    return nomineeAvatarUrl;
  }

  public void setNomineeAvatarUrl( String nomineeAvatarUrl )
  {
    this.nomineeAvatarUrl = nomineeAvatarUrl;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public Date getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( Date dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public String getNomineeName()
  {
    String firstName = "";
    String lastName = "";

    if ( getNomineeFirstName() != null )
    {
      firstName = getNomineeFirstName();
    }
    if ( getNomineeLastName() != null )
    {
      lastName = getNomineeLastName();
    }
    if ( StringUtils.isNotEmpty( firstName ) && StringUtils.isNotEmpty( lastName ) )
    {
      return firstName + " " + lastName;
    }
    return null;
  }

  public String getOwnCardName()
  {
    return ownCardName;
  }

  public void setOwnCardName( String ownCardName )
  {
    this.ownCardName = ownCardName;
  }

  public String getSubmitterLangId()
  {
    return submitterLangId;
  }

  public void setSubmitterLangId( String submitterLangId )
  {
    this.submitterLangId = submitterLangId;
  }

  public String getNominatorDprtName()
  {
    return nominatorDprtName;
  }

  public void setNominatorDprtName( String nominatorDprtName )
  {
    this.nominatorDprtName = nominatorDprtName;
  }

  public String getNomineeDprtName()
  {
    return nomineeDprtName;
  }

  public void setNomineeDprtName( String nomineeDprtName )
  {
    this.nomineeDprtName = nomineeDprtName;
  }

  public Long getLevelNumber()
  {
    return levelNumber;
  }

  public void setLevelNumber( Long levelNumber )
  {
    this.levelNumber = levelNumber;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public String getOtherDescription()
  {
    return otherDescription;
  }

  public void setOtherDescription( String otherDescription )
  {
    this.otherDescription = otherDescription;
  }

  public Long getClaimGroupId()
  {
    return claimGroupId;
  }

  public void setClaimGroupId( Long claimGroupId )
  {
    this.claimGroupId = claimGroupId;
  }

  public Long getNomineeUserId()
  {
    return nomineeUserId;
  }

  public void setNomineeUserId( Long nomineeUserId )
  {
    this.nomineeUserId = nomineeUserId;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }
  
  public String getRequestId( String videoUrl )
  {

    return videoUrl.substring( videoUrl.lastIndexOf( ":" ) + 1 );
  }
}
