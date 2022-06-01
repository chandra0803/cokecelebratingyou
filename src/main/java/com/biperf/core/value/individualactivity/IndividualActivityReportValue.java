
package com.biperf.core.value.individualactivity;

import java.math.BigDecimal;
import java.util.Date;

public class IndividualActivityReportValue
{
  // COMMON FIELDS
  private Date dateSubmitted;
  private String promoName;
  private Long userId;
  private Long points;
  private Long sweepstakesWon;
  private Long badgesEarned;
  private Long received;
  private Long sent;

  // TABULAR DATA
  private String moduleName;
  private String moduleAssetCode;
  private Long plateauEarned;
  private String month;
  private String viewAllAwards;

  // AWARDS RECEIVED
  private String paxName;
  private String orgName;
  private Long plateausEarned;
  private String onTheSpot;

  // NOMINATIONS GIVEN
  private Long receiverCnt;
  private Date dateApproved;
  private String nomineeName;

  // NOMINATIONS RECEIVED
  private Long nominationsCnt;
  private Long nominationsWonCnt;
  private String nominatorName;

  // PRODUCT CLAIMS
  private String claimNumber;
  private String soldTo;
  private String claimStatus;

  // QUIZ
  private Long promoId;
  private Long quizAttempts;
  private Long quizAttemptsFailed;
  private Long quizAttemptsPassed;
  private Long awardsGiven;
  private String quizName;
  private Date quizDate;
  private String quizResult;

  // RECOGNITIONS GIVEN
  private Long recognitionCount;
  private String receiverName;
  private String status;

  // RECOGNITIONS RECEIVED
  private String giverName;
  private Long plateauEarnedCount;
  private Long sweepstakesWonCount;
  private Long pointsCount;

  // THROWDOWN
  private String country;
  private String participantStatus;
  private String jobPosition;
  private String department;
  private Long winsCnt;
  private Long tiesCnt;
  private Long lossCnt;
  private BigDecimal activityCnt;
  private Long rank;
  private Long roundNumber;

  // GOALQUEST and CHALLENGEPOINT
  private String goalLevelName;
  private Long baseQuantity;
  private Long amountToAchieve;
  private Long actualResults;
  private double percentageOfGoal;
  private String achieved;
  private String challengePointLevelName;
  private double percentageOfChallengePoint;

  // ONTHESPOT
  private Date transDate;
  private String Recipient;

  // BADGE
  private String promotionName;
  private Date mediaDate;
  private Long badgePoints;

  // CHARTS
  private String userName;
  private Long userRecognitionsSent;
  private Long userRecognitionsRcvd;
  private BigDecimal orgAvgRecognitionsSent;
  private BigDecimal orgAvgRecognitionsRcvd;
  private BigDecimal companyAvgRecognitionsSent;
  private BigDecimal companyAvgRecognitionsRcvd;
  private Long recognitionsReceived;
  private Long recognitionsSent;
  private Long quizzesPassed;
  private Long pointsReceived;
  private Long pointsGiven;

  private Long claimId;
  private Long submitterId;

  // SSI Contest
  private Long other;
  private String otherValue;
  private Long otherAward;
  private Long contestId;
  private String isAtnContest;

  public Date getDateSubmitted()
  {
    return dateSubmitted;
  }

  public void setDateSubmitted( Date dateSubmitted )
  {
    this.dateSubmitted = dateSubmitted;
  }

  public Date getTransDate()
  {
    return transDate;
  }

  public void setTransDate( Date transDate )
  {
    this.transDate = transDate;
  }

  public String getRecipient()
  {
    return Recipient;
  }

  public void setRecipient( String recipient )
  {
    Recipient = recipient;
  }

  public String getGoalLevelName()
  {
    return goalLevelName;
  }

  public void setGoalLevelName( String goalLevelName )
  {
    this.goalLevelName = goalLevelName;
  }

  public String getViewAllAwards()
  {
    return viewAllAwards;
  }

  public void setViewAllAwards( String viewAllAwards )
  {
    this.viewAllAwards = viewAllAwards;
  }

  public Long getBaseQuantity()
  {
    return baseQuantity;
  }

  public void setBaseQuantity( Long baseQuantity )
  {
    this.baseQuantity = baseQuantity;
  }

  public Long getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( Long amountToAchieve )
  {
    this.amountToAchieve = amountToAchieve;
  }

  public Long getActualResults()
  {
    return actualResults;
  }

  public void setActualResults( Long actualResults )
  {
    this.actualResults = actualResults;
  }

  public double getPercentageOfGoal()
  {
    return percentageOfGoal;
  }

  public void setPercentageOfGoal( double percentageOfGoal )
  {
    this.percentageOfGoal = percentageOfGoal;
  }

  public String getAchieved()
  {
    return achieved;
  }

  public void setAchieved( String achieved )
  {
    this.achieved = achieved;
  }

  public String getPromoName()
  {
    return promoName;
  }

  public void setPromoName( String promoName )
  {
    this.promoName = promoName;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getPoints()
  {
    return points;
  }

  public void setPoints( Long points )
  {
    this.points = points;
  }

  public Long getSweepstakesWon()
  {
    return sweepstakesWon;
  }

  public void setSweepstakesWon( Long sweepstakesWon )
  {
    this.sweepstakesWon = sweepstakesWon;
  }

  public Long getBadgesEarned()
  {
    return badgesEarned;
  }

  public void setBadgesEarned( Long badgesEarned )
  {
    this.badgesEarned = badgesEarned;
  }

  public String getModuleName()
  {
    return moduleName;
  }

  public void setModuleName( String moduleName )
  {
    this.moduleName = moduleName;
  }

  public String getModuleAssetCode()
  {
    return moduleAssetCode;
  }

  public void setModuleAssetCode( String moduleAssetCode )
  {
    this.moduleAssetCode = moduleAssetCode;
  }

  public Long getPlateauEarned()
  {
    return plateauEarned;
  }

  public void setPlateauEarned( Long plateauEarned )
  {
    this.plateauEarned = plateauEarned;
  }

  public String getMonth()
  {
    return month;
  }

  public void setMonth( String month )
  {
    this.month = month;
  }

  public String getPaxName()
  {
    return paxName;
  }

  public void setPaxName( String paxName )
  {
    this.paxName = paxName;
  }

  public String getOrgName()
  {
    return orgName;
  }

  public void setOrgName( String orgName )
  {
    this.orgName = orgName;
  }

  public Long getPlateausEarned()
  {
    return plateausEarned;
  }

  public void setPlateausEarned( Long plateausEarned )
  {
    this.plateausEarned = plateausEarned;
  }

  public String getOnTheSpot()
  {
    return onTheSpot;
  }

  public void setOnTheSpot( String onTheSpot )
  {
    this.onTheSpot = onTheSpot;
  }

  public Long getReceiverCnt()
  {
    return receiverCnt;
  }

  public void setReceiverCnt( Long receiverCnt )
  {
    this.receiverCnt = receiverCnt;
  }

  public Date getDateApproved()
  {
    return dateApproved;
  }

  public void setDateApproved( Date dateApproved )
  {
    this.dateApproved = dateApproved;
  }

  public String getNomineeName()
  {
    return nomineeName;
  }

  public void setNomineeName( String nomineeName )
  {
    this.nomineeName = nomineeName;
  }

  public Long getNominationsCnt()
  {
    return nominationsCnt;
  }

  public void setNominationsCnt( Long nominationsCnt )
  {
    this.nominationsCnt = nominationsCnt;
  }

  public Long getNominationsWonCnt()
  {
    return nominationsWonCnt;
  }

  public void setNominationsWonCnt( Long nominationsWonCnt )
  {
    this.nominationsWonCnt = nominationsWonCnt;
  }

  public String getNominatorName()
  {
    return nominatorName;
  }

  public void setNominatorName( String nominatorName )
  {
    this.nominatorName = nominatorName;
  }

  public String getClaimNumber()
  {
    return claimNumber;
  }

  public void setClaimNumber( String claimNumber )
  {
    this.claimNumber = claimNumber;
  }

  public String getSoldTo()
  {
    return soldTo;
  }

  public void setSoldTo( String soldTo )
  {
    this.soldTo = soldTo;
  }

  public String getClaimStatus()
  {
    return claimStatus;
  }

  public void setClaimStatus( String claimStatus )
  {
    this.claimStatus = claimStatus;
  }

  public Long getPromoId()
  {
    return promoId;
  }

  public void setPromoId( Long promoId )
  {
    this.promoId = promoId;
  }

  public Long getQuizAttempts()
  {
    return quizAttempts;
  }

  public void setQuizAttempts( Long quizAttempts )
  {
    this.quizAttempts = quizAttempts;
  }

  public Long getQuizAttemptsFailed()
  {
    return quizAttemptsFailed;
  }

  public void setQuizAttemptsFailed( Long quizAttemptsFailed )
  {
    this.quizAttemptsFailed = quizAttemptsFailed;
  }

  public Long getQuizAttemptsPassed()
  {
    return quizAttemptsPassed;
  }

  public void setQuizAttemptsPassed( Long quizAttemptsPassed )
  {
    this.quizAttemptsPassed = quizAttemptsPassed;
  }

  public Long getAwardsGiven()
  {
    return awardsGiven;
  }

  public void setAwardsGiven( Long awardsGiven )
  {
    this.awardsGiven = awardsGiven;
  }

  public String getQuizName()
  {
    return quizName;
  }

  public void setQuizName( String quizName )
  {
    this.quizName = quizName;
  }

  public Date getQuizDate()
  {
    return quizDate;
  }

  public void setQuizDate( Date quizDate )
  {
    this.quizDate = quizDate;
  }

  public String getQuizResult()
  {
    return quizResult;
  }

  public void setQuizResult( String quizResult )
  {
    this.quizResult = quizResult;
  }

  public Long getRecognitionCount()
  {
    return recognitionCount;
  }

  public void setRecognitionCount( Long recognitionCount )
  {
    this.recognitionCount = recognitionCount;
  }

  public String getReceiverName()
  {
    return receiverName;
  }

  public void setReceiverName( String receiverName )
  {
    this.receiverName = receiverName;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getGiverName()
  {
    return giverName;
  }

  public void setGiverName( String giverName )
  {
    this.giverName = giverName;
  }

  public Long getPlateauEarnedCount()
  {
    return plateauEarnedCount;
  }

  public void setPlateauEarnedCount( Long plateauEarnedCount )
  {
    this.plateauEarnedCount = plateauEarnedCount;
  }

  public Long getSweepstakesWonCount()
  {
    return sweepstakesWonCount;
  }

  public void setSweepstakesWonCount( Long sweepstakesWonCount )
  {
    this.sweepstakesWonCount = sweepstakesWonCount;
  }

  public Long getPointsCount()
  {
    return pointsCount;
  }

  public void setPointsCount( Long pointsCount )
  {
    this.pointsCount = pointsCount;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public Long getUserRecognitionsSent()
  {
    return userRecognitionsSent;
  }

  public void setUserRecognitionsSent( Long userRecognitionsSent )
  {
    this.userRecognitionsSent = userRecognitionsSent;
  }

  public Long getUserRecognitionsRcvd()
  {
    return userRecognitionsRcvd;
  }

  public void setUserRecognitionsRcvd( Long userRecognitionsRcvd )
  {
    this.userRecognitionsRcvd = userRecognitionsRcvd;
  }

  public BigDecimal getOrgAvgRecognitionsSent()
  {
    return orgAvgRecognitionsSent;
  }

  public void setOrgAvgRecognitionsSent( BigDecimal orgAvgRecognitionsSent )
  {
    this.orgAvgRecognitionsSent = orgAvgRecognitionsSent;
  }

  public BigDecimal getOrgAvgRecognitionsRcvd()
  {
    return orgAvgRecognitionsRcvd;
  }

  public void setOrgAvgRecognitionsRcvd( BigDecimal orgAvgRecognitionsRcvd )
  {
    this.orgAvgRecognitionsRcvd = orgAvgRecognitionsRcvd;
  }

  public BigDecimal getCompanyAvgRecognitionsSent()
  {
    return companyAvgRecognitionsSent;
  }

  public void setCompanyAvgRecognitionsSent( BigDecimal companyAvgRecognitionsSent )
  {
    this.companyAvgRecognitionsSent = companyAvgRecognitionsSent;
  }

  public BigDecimal getCompanyAvgRecognitionsRcvd()
  {
    return companyAvgRecognitionsRcvd;
  }

  public void setCompanyAvgRecognitionsRcvd( BigDecimal companyAvgRecognitionsRcvd )
  {
    this.companyAvgRecognitionsRcvd = companyAvgRecognitionsRcvd;
  }

  public Long getRecognitionsReceived()
  {
    return recognitionsReceived;
  }

  public void setRecognitionsReceived( Long recognitionsReceived )
  {
    this.recognitionsReceived = recognitionsReceived;
  }

  public Long getRecognitionsSent()
  {
    return recognitionsSent;
  }

  public void setRecognitionsSent( Long recognitionsSent )
  {
    this.recognitionsSent = recognitionsSent;
  }

  public Long getQuizzesPassed()
  {
    return quizzesPassed;
  }

  public void setQuizzesPassed( Long quizzesPassed )
  {
    this.quizzesPassed = quizzesPassed;
  }

  public Long getPointsReceived()
  {
    return pointsReceived;
  }

  public void setPointsReceived( Long pointsReceived )
  {
    this.pointsReceived = pointsReceived;
  }

  public Long getPointsGiven()
  {
    return pointsGiven;
  }

  public void setPointsGiven( Long pointsGiven )
  {
    this.pointsGiven = pointsGiven;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getSubmitterId()
  {
    return submitterId;
  }

  public void setSubmitterId( Long submitterId )
  {
    this.submitterId = submitterId;
  }

  public Long getReceived()
  {
    return received;
  }

  public void setReceived( Long received )
  {
    this.received = received;
  }

  public Long getSent()
  {
    return sent;
  }

  public void setSent( Long sent )
  {
    this.sent = sent;
  }

  public String getCountry()
  {
    return country;
  }

  public void setCountry( String country )
  {
    this.country = country;
  }

  public String getParticipantStatus()
  {
    return participantStatus;
  }

  public void setParticipantStatus( String participantStatus )
  {
    this.participantStatus = participantStatus;
  }

  public String getJobPosition()
  {
    return jobPosition;
  }

  public void setJobPosition( String jobPosition )
  {
    this.jobPosition = jobPosition;
  }

  public String getDepartment()
  {
    return department;
  }

  public void setDepartment( String department )
  {
    this.department = department;
  }

  public Long getWinsCnt()
  {
    return winsCnt;
  }

  public void setWinsCnt( Long winsCnt )
  {
    this.winsCnt = winsCnt;
  }

  public Long getTiesCnt()
  {
    return tiesCnt;
  }

  public void setTiesCnt( Long tiesCnt )
  {
    this.tiesCnt = tiesCnt;
  }

  public Long getLossCnt()
  {
    return lossCnt;
  }

  public void setLossCnt( Long lossCnt )
  {
    this.lossCnt = lossCnt;
  }

  public BigDecimal getActivityCnt()
  {
    return activityCnt;
  }

  public void setActivityCnt( BigDecimal activityCnt )
  {
    this.activityCnt = activityCnt;
  }

  public Long getRank()
  {
    return rank;
  }

  public void setRank( Long rank )
  {
    this.rank = rank;
  }

  public Long getRoundNumber()
  {
    return roundNumber;
  }

  public void setRoundNumber( Long roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public String getChallengePointLevelName()
  {
    return challengePointLevelName;
  }

  public void setChallengePointLevelName( String challengePointLevelName )
  {
    this.challengePointLevelName = challengePointLevelName;
  }

  public double getPercentageOfChallengePoint()
  {
    return percentageOfChallengePoint;
  }

  public void setPercentageOfChallengePoint( double percentageOfChallengePoint )
  {
    this.percentageOfChallengePoint = percentageOfChallengePoint;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public Date getMediaDate()
  {
    return mediaDate;
  }

  public void setMediaDate( Date mediaDate )
  {
    this.mediaDate = mediaDate;
  }

  public Long getBadgePoints()
  {
    return badgePoints;
  }

  public void setBadgePoints( Long badgePoints )
  {
    this.badgePoints = badgePoints;
  }

  public String getOtherValue()
  {
    return otherValue;
  }

  public void setOtherValue( String otherValue )
  {
    this.otherValue = otherValue;
  }

  public Long getOther()
  {
    return other;
  }

  public void setOther( Long other )
  {
    this.other = other;
  }

  public Long getOtherAward()
  {
    return otherAward;
  }

  public void setOtherAward( Long otherAward )
  {
    this.otherAward = otherAward;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public String getIsAtnContest()
  {
    return isAtnContest;
  }

  public void setIsAtnContest( String isAtnContest )
  {
    this.isAtnContest = isAtnContest;
  }

}
