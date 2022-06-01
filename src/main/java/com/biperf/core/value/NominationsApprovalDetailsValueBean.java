/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.value;

import java.math.BigDecimal;
import java.util.Date;

import com.biperf.core.ui.constants.ActionConstants;

/**
 * 
 * @author poddutur
 * @since May 17, 2016
 */
public class NominationsApprovalDetailsValueBean
{
  private String claimId;
  private String Status;
  private Long teamId;
  private Long paxId;
  private String nomineeName;
  private String orgName;
  private String jobPositionName;
  private String countryName;
  private boolean wonFlag;
  private int numberOfTimesWon;
  private boolean exceedFlag;
  private Date submittedDate;
  private Long nominatorPaxId;
  private String nominatorName;
  private int levelIndex;
  private String avatarUrl;
  private int teamCount;
  private boolean isTeam;
  private BigDecimal awardAmount;
  private String awardAmountType;
  private String ecardImage;
  private String ecardUrl;
  private String cardVideoUrl;
  private String cardImageUrl;
  private String submitterCommentsLangId;
  private String submitterComments;
  private String moreInfoComments;
  private String whyAttachmentName;
  private String whyAttachmentUrl;
  private Long certificateId;
  private boolean potentialAwardExceeded;
  private String recentTimePeriodWon;
  private Date mostRecentTimeDate;
  private String departmentName;
  private int nominatorCount;
  private String denialReason;
  private String winnerMessage;
  private String moreInfoMessage;
  private Date notificationDate;
  private String levelName;
  private Long claimGroupId;
  private boolean optOutAwards;

  public String getClaimId()
  {
    return claimId;
  }

  public void setClaimId( String claimId )
  {
    this.claimId = claimId;
  }

  public String getStatus()
  {
    return Status;
  }

  public void setStatus( String status )
  {
    Status = status;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public Long getPaxId()
  {
    return paxId;
  }

  public void setPaxId( Long paxId )
  {
    this.paxId = paxId;
  }

  public String getNomineeName()
  {
    return nomineeName;
  }

  public void setNomineeName( String nomineeName )
  {
    this.nomineeName = nomineeName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public String getJobPositionName()
  {
    return jobPositionName;
  }

  public void setJobPositionName( String jobPositionName )
  {
    this.jobPositionName = jobPositionName;
  }

  public String getCountryName()
  {
    return countryName;
  }

  public void setCountryName( String countryName )
  {
    this.countryName = countryName;
  }

  public boolean isWonFlag()
  {
    return wonFlag;
  }

  public void setWonFlag( boolean wonFlag )
  {
    this.wonFlag = wonFlag;
  }

  public int getNumberOfTimesWon()
  {
    return numberOfTimesWon;
  }

  public void setNumberOfTimesWon( int numberOfTimesWon )
  {
    this.numberOfTimesWon = numberOfTimesWon;
  }

  public boolean isExceedFlag()
  {
    return exceedFlag;
  }

  public void setExceedFlag( boolean exceedFlag )
  {
    this.exceedFlag = exceedFlag;
  }

  public Date getSubmittedDate()
  {
    return submittedDate;
  }

  public void setSubmittedDate( Date submittedDate )
  {
    this.submittedDate = submittedDate;
  }

  public Long getNominatorPaxId()
  {
    return nominatorPaxId;
  }

  public void setNominatorPaxId( Long nominatorPaxId )
  {
    this.nominatorPaxId = nominatorPaxId;
  }

  public String getNominatorName()
  {
    return nominatorName;
  }

  public void setNominatorName( String nominatorName )
  {
    this.nominatorName = nominatorName;
  }

  public int getLevelIndex()
  {
    return levelIndex;
  }

  public void setLevelIndex( int levelIndex )
  {
    this.levelIndex = levelIndex;
  }

  public String getAvatarUrl()
  {
    return avatarUrl;
  }

  public void setAvatarUrl( String avatarUrl )
  {
    this.avatarUrl = avatarUrl;
  }

  public int getTeamCount()
  {
    return teamCount;
  }

  public void setTeamCount( int teamCount )
  {
    this.teamCount = teamCount;
  }

  public boolean isTeam()
  {
    return isTeam;
  }

  public void setTeam( boolean isTeam )
  {
    this.isTeam = isTeam;
  }

  public BigDecimal getAwardAmount()
  {
    return awardAmount;
  }

  public void setAwardAmount( BigDecimal awardAmount )
  {
    this.awardAmount = awardAmount;
  }

  public String getAwardAmountType()
  {
    return awardAmountType;
  }

  public void setAwardAmountType( String awardAmountType )
  {
    this.awardAmountType = awardAmountType;
  }

  public String getEcardImage()
  {
    return ecardImage;
  }

  public void setEcardImage( String ecardImage )
  {
    this.ecardImage = ecardImage;
  }

  public String getEcardUrl()
  {
    return ecardUrl;
  }

  public void setEcardUrl( String ecardUrl )
  {
    this.ecardUrl = ecardUrl;
  }

  public String getCardVideoUrl()
  {
    return cardVideoUrl;
  }

  public void setCardVideoUrl( String cardVideoUrl )
  {
    this.cardVideoUrl = cardVideoUrl;
  }

  public String getCardImageUrl()
  {
    return cardImageUrl;
  }

  public void setCardImageUrl( String cardImageUrl )
  {
    this.cardImageUrl = cardImageUrl;
  }

  public String getSubmitterCommentsLangId()
  {
    return submitterCommentsLangId;
  }

  public void setSubmitterCommentsLangId( String submitterCommentsLangId )
  {
    this.submitterCommentsLangId = submitterCommentsLangId;
  }

  public String getSubmitterComments()
  {
    return submitterComments;
  }

  public void setSubmitterComments( String submitterComments )
  {
    this.submitterComments = submitterComments;
  }

  public String getMoreInfoComments()
  {
    return moreInfoComments;
  }

  public void setMoreInfoComments( String moreInfoComments )
  {
    this.moreInfoComments = moreInfoComments;
  }

  public String getWhyAttachmentName()
  {
    return whyAttachmentName;
  }

  public void setWhyAttachmentName( String whyAttachmentName )
  {
    this.whyAttachmentName = whyAttachmentName;
  }

  public String getWhyAttachmentUrl()
  {
    return whyAttachmentUrl;
  }

  public void setWhyAttachmentUrl( String whyAttachmentUrl )
  {
    this.whyAttachmentUrl = whyAttachmentUrl;
  }

  public Long getCertificateId()
  {
    return certificateId;
  }

  public void setCertificateId( Long certificateId )
  {
    this.certificateId = certificateId;
  }

  public boolean isPotentialAwardExceeded()
  {
    return potentialAwardExceeded;
  }

  public void setPotentialAwardExceeded( boolean potentialAwardExceeded )
  {
    this.potentialAwardExceeded = potentialAwardExceeded;
  }

  public String getRecentTimePeriodWon()
  {
    return recentTimePeriodWon;
  }

  public void setRecentTimePeriodWon( String recentTimePeriodWon )
  {
    this.recentTimePeriodWon = recentTimePeriodWon;
  }

  public Date getMostRecentTimeDate()
  {
    return mostRecentTimeDate;
  }

  public void setMostRecentTimeDate( Date mostRecentTimeDate )
  {
    this.mostRecentTimeDate = mostRecentTimeDate;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentName )
  {
    this.departmentName = departmentName;
  }

  public int getNominatorCount()
  {
    return nominatorCount;
  }

  public void setNominatorCount( int nominatorCount )
  {
    this.nominatorCount = nominatorCount;
  }

  public String getDenialReason()
  {
    return denialReason;
  }

  public void setDenialReason( String denialReason )
  {
    this.denialReason = denialReason;
  }

  public String getWinnerMessage()
  {
    return winnerMessage;
  }

  public void setWinnerMessage( String winnerMessage )
  {
    this.winnerMessage = winnerMessage;
  }

  public String getMoreInfoMessage()
  {
    return moreInfoMessage;
  }

  public void setMoreInfoMessage( String moreInfoMessage )
  {
    this.moreInfoMessage = moreInfoMessage;
  }

  public Date getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( Date notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public String getLevelName()
  {
    return levelName;
  }

  public void setLevelName( String levelName )
  {
    this.levelName = levelName;
  }

  public Long getClaimGroupId()
  {
    return claimGroupId;
  }

  public void setClaimGroupId( Long claimGroupId )
  {
    this.claimGroupId = claimGroupId;
  }

  public boolean isOptOutAwards()
  {
    return optOutAwards;
  }

  public void setOptOutAwards( boolean optOutAwards )
  {
    this.optOutAwards = optOutAwards;
  }

  public String getActualCardUrl( String path )
  {
    return path.substring( 0, path.lastIndexOf( ActionConstants.REQUEST_ID ) );
  }

  public String getRequestId( String videoUrl )
  {

    return videoUrl.substring( videoUrl.lastIndexOf( ":" ) + 1 );
  }

}
