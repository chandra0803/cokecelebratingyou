
package com.biperf.core.value;

import java.io.Serializable;
import java.util.Date;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.managertoolkit.ParticipantAlert;
import com.biperf.core.domain.promotion.Promotion;

public class AlertsValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Promotion promotion;
  private ActivityType activityType;
  boolean showMultipleItem = false;
  private String goalSelectionStartDate;
  private String goalSelectionEnddate;
  private String cpSelectionStartDate;
  private String cpSelectionEndDate;
  private ParticipantAlert participantAlert;
  private CommLog commLog;
  private String onlineShoppingUrl;
  private String awardIssuedDate;
  private String awardExpiryDate;
  private String purlIssuedDate;
  private String purlExpiryDate;
  private String budgetEndDate;
  private Long purlContributorId;
  private Long purlRecipientId;
  private Long celebrationManagerMessageId;
  private boolean defaultInvitee;
  // file alerts
  private Long fileStoreId;
  private String fileDownloadExpiryDate;
  private String fileDownloadRequestedDate;
  private String fileName;
  private int numberOfPaxWithUnclaimedAwards;
  private Long batchId;

  private String surveySelectionStartDate;
  private String surveySelectionEnddate;

  private Date budgetSegmentStartDate;
  private Date budgetSegmentEndDate;

  private NominationsApprovalValueBean nominationsApprovalValueBean;
  private String promotionType;

  private Long claimId;
  private Long userId;

  private Long activityId;
  private Long teamId;

  private int paxNomineeInCompleteSubmissions;

  private Date postedDate;

  private String instantPollId;

  // ra alerts
  private boolean newHireAlert;
  private boolean overDueAlert;
  private String nhFirstName;
  private String odFirstName;
  private Long nhUserId;
  private Long odUserId;
  
  // Client customizations for WIP #43735 starts
  private Long claimItemId;
  private String promotionName;
  private Date submissionDate;
  private Date dueDate; 
  private Date approvedOn;
  // Client customizations for WIP #43735 ends
  // New SA
  private String saCelebrationId;
  private String programName;
  private Long programId;

  public Long getProgramId()
  {
    return programId;
  }

  public void setProgramId( Long programId )
  {
    this.programId = programId;
  }

  public String getSaCelebrationId()
  {
    return saCelebrationId;
  }

  public void setSaCelebrationId( String saCelebrationId )
  {
    this.saCelebrationId = saCelebrationId;
  }

  public String getProgramName()
  {
    return programName;
  }

  public void setProgramName( String programName )
  {
    this.programName = programName;
  }

  public Long getNhUserId()
  {
    return nhUserId;
  }

  public void setNhUserId( Long nhUserId )
  {
    this.nhUserId = nhUserId;
  }

  public Long getOdUserId()
  {
    return odUserId;
  }

  public void setOdUserId( Long odUserId )
  {
    this.odUserId = odUserId;
  }

  public String getNhFirstName()
  {
    return nhFirstName;
  }

  public void setNhFirstName( String nhFirstName )
  {
    this.nhFirstName = nhFirstName;
  }

  public String getOdFirstName()
  {
    return odFirstName;
  }

  public void setOdFirstName( String odFirstName )
  {
    this.odFirstName = odFirstName;
  }

  public Long getFileStoreId()
  {
    return fileStoreId;
  }

  public void setFileStoreId( Long fileStoreId )
  {
    this.fileStoreId = fileStoreId;
  }

  public String getFileDownloadExpiryDate()
  {
    return fileDownloadExpiryDate;
  }

  public void setFileDownloadExpiryDate( String fileDownloadExpiryDate )
  {
    this.fileDownloadExpiryDate = fileDownloadExpiryDate;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

  public AlertsValueBean()
  {

  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setActivityType( ActivityType activityType )
  {
    this.activityType = activityType;
  }

  public ActivityType getActivityType()
  {
    return activityType;
  }

  public boolean isShowMultipleItem()
  {
    return showMultipleItem;
  }

  public void setShowMultipleItem( boolean showMultipleItem )
  {
    this.showMultipleItem = showMultipleItem;
  }

  public void setGoalSelectionEnddate( String goalSelectionEnddate )
  {
    this.goalSelectionEnddate = goalSelectionEnddate;
  }

  public String getGoalSelectionEnddate()
  {
    return goalSelectionEnddate;
  }

  public void setCpSelectionEndDate( String cpSelectionEndDate )
  {
    this.cpSelectionEndDate = cpSelectionEndDate;
  }

  public String getCpSelectionEndDate()
  {
    return cpSelectionEndDate;
  }

  public void setCommLog( CommLog commLog )
  {
    this.commLog = commLog;
  }

  public CommLog getCommLog()
  {
    return commLog;
  }

  public void setGoalSelectionStartDate( String goalSelectionStartDate )
  {
    this.goalSelectionStartDate = goalSelectionStartDate;
  }

  public String getGoalSelectionStartDate()
  {
    return goalSelectionStartDate;
  }

  public void setCpSelectionStartDate( String cpSelectionStartDate )
  {
    this.cpSelectionStartDate = cpSelectionStartDate;
  }

  public String getCpSelectionStartDate()
  {
    return cpSelectionStartDate;
  }

  public void setParticipantAlert( ParticipantAlert participantAlert )
  {
    this.participantAlert = participantAlert;
  }

  public ParticipantAlert getParticipantAlert()
  {
    return participantAlert;
  }

  public String getOnlineShoppingUrl()
  {
    return onlineShoppingUrl;
  }

  public void setOnlineShoppingUrl( String onlineShoppingUrl )
  {
    this.onlineShoppingUrl = onlineShoppingUrl;
  }

  public void setAwardIssuedDate( String awardIssuedDate )
  {
    this.awardIssuedDate = awardIssuedDate;
  }

  public String getAwardIssuedDate()
  {
    return awardIssuedDate;
  }

  public void setAwardExpiryDate( String awardExpiryDate )
  {
    this.awardExpiryDate = awardExpiryDate;
  }

  public String getAwardExpiryDate()
  {
    return awardExpiryDate;
  }

  public String getPurlIssuedDate()
  {
    return purlIssuedDate;
  }

  public void setPurlIssuedDate( String purlIssuedDate )
  {
    this.purlIssuedDate = purlIssuedDate;
  }

  public String getPurlExpiryDate()
  {
    return purlExpiryDate;
  }

  public void setPurlExpiryDate( String purlExpiryDate )
  {
    this.purlExpiryDate = purlExpiryDate;
  }

  public void setBudgetEndDate( String budgetEndDate )
  {
    this.budgetEndDate = budgetEndDate;
  }

  public String getBudgetEndDate()
  {
    return budgetEndDate;
  }

  public Long getPurlContributorId()
  {
    return purlContributorId;
  }

  public void setPurlContributorId( Long purlContributorId )
  {
    this.purlContributorId = purlContributorId;
  }

  public Long getPurlRecipientId()
  {
    return purlRecipientId;
  }

  public void setPurlRecipientId( Long purlRecipientId )
  {
    this.purlRecipientId = purlRecipientId;
  }

  public String getFileDownloadRequestedDate()
  {
    return fileDownloadRequestedDate;
  }

  public void setFileDownloadRequestedDate( String fileDownloadRequestedDate )
  {
    this.fileDownloadRequestedDate = fileDownloadRequestedDate;
  }

  public int getNumberOfPaxWithUnclaimedAwards()
  {
    return numberOfPaxWithUnclaimedAwards;
  }

  public void setNumberOfPaxWithUnclaimedAwards( int numberOfPaxWithUnclaimedAwards )
  {
    this.numberOfPaxWithUnclaimedAwards = numberOfPaxWithUnclaimedAwards;
  }

  public Long getBatchId()
  {
    return batchId;
  }

  public void setBatchId( Long batchId )
  {
    this.batchId = batchId;
  }

  public String getSurveySelectionStartDate()
  {
    return surveySelectionStartDate;
  }

  public void setSurveySelectionStartDate( String surveySelectionStartDate )
  {
    this.surveySelectionStartDate = surveySelectionStartDate;
  }

  public String getSurveySelectionEnddate()
  {
    return surveySelectionEnddate;
  }

  public void setSurveySelectionEnddate( String surveySelectionEnddate )
  {
    this.surveySelectionEnddate = surveySelectionEnddate;
  }

  public Long getCelebrationManagerMessageId()
  {
    return celebrationManagerMessageId;
  }

  public void setCelebrationManagerMessageId( Long celebrationManagerMessageId )
  {
    this.celebrationManagerMessageId = celebrationManagerMessageId;
  }

  public Date getBudgetSegmentStartDate()
  {
    return budgetSegmentStartDate;
  }

  public void setBudgetSegmentStartDate( Date budgetSegmentStartDate )
  {
    this.budgetSegmentStartDate = budgetSegmentStartDate;
  }

  public Date getBudgetSegmentEndDate()
  {
    return budgetSegmentEndDate;
  }

  public void setBudgetSegmentEndDate( Date budgetSegmentEndDate )
  {
    this.budgetSegmentEndDate = budgetSegmentEndDate;
  }

  public NominationsApprovalValueBean getNominationsApprovalValueBean()
  {
    return nominationsApprovalValueBean;
  }

  public void setNominationsApprovalValueBean( NominationsApprovalValueBean nominationsApprovalValueBean )
  {
    this.nominationsApprovalValueBean = nominationsApprovalValueBean;
  }

  public String getPromotionType()
  {
    return promotionType;
  }

  public void setPromotionType( String promotionType )
  {
    this.promotionType = promotionType;
  }

  public Long getClaimId()
  {
    return claimId;
  }

  public void setClaimId( Long claimId )
  {
    this.claimId = claimId;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public Long getActivityId()
  {
    return activityId;
  }

  public void setActivityId( Long activityId )
  {
    this.activityId = activityId;
  }

  public Long getTeamId()
  {
    return teamId;
  }

  public void setTeamId( Long teamId )
  {
    this.teamId = teamId;
  }

  public int getPaxNomineeInCompleteSubmissions()
  {
    return paxNomineeInCompleteSubmissions;
  }

  public void setPaxNomineeInCompleteSubmissions( int paxNomineeInCompleteSubmissions )
  {
    this.paxNomineeInCompleteSubmissions = paxNomineeInCompleteSubmissions;
  }

  public Date getPostedDate()
  {
    return postedDate;
  }

  public void setPostedDate( Date postedDate )
  {
    this.postedDate = postedDate;
  }

  public String getInstantPollId()
  {
    return instantPollId;
  }

  public void setInstantPollId( String instantPollId )
  {
    this.instantPollId = instantPollId;
  }

  public boolean isNewHireAlert()
  {
    return newHireAlert;
  }

  public void setNewHireAlert( boolean newHireAlert )
  {
    this.newHireAlert = newHireAlert;
  }

  public boolean isOverDueAlert()
  {
    return overDueAlert;
  }

  public void setOverDueAlert( boolean overDueAlert )
  {
    this.overDueAlert = overDueAlert;
  }

  public boolean isDefaultInvitee()
  {
    return defaultInvitee;
  }

  public void setDefaultInvitee( boolean defaultInvitee )
  {
    this.defaultInvitee = defaultInvitee;
  }

  // Client customizations for WIP #43735 starts
  public Long getClaimItemId()
  {
    return claimItemId;
  }

  public void setClaimItemId( Long claimItemId )
  {
    this.claimItemId = claimItemId;
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
 
  public Date getDueDate()
  {
    return dueDate;
  }

  public void setDueDate( Date dueDate )
  {
    this.dueDate = dueDate;
  }

  public Date getApprovedOn()
  {
    return approvedOn;
  }

  public void setApprovedOn( Date approvedOn )
  {
    this.approvedOn = approvedOn;
  }
  // Client customizations for WIP #43735 ends
}
