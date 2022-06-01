
package com.biperf.core.domain.ssi;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.domain.enums.SSIBillPayoutCodeType;
import com.biperf.core.domain.enums.SSIContestActivitySubmissionType;
import com.biperf.core.domain.enums.SSIContestStatus;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIIndividualBaselineType;
import com.biperf.core.domain.enums.SSIPayoutType;
import com.biperf.core.domain.gamification.BadgeRule;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * 
 * @author dudam
 * @since Nov 4, 2014
 * @version 1.0
 */
public class SSIContest extends BaseDomain
{
  private static final long serialVersionUID = 1L;
  private SSIPromotion promotion;
  private Long contestOwnerId;
  private SSIContestType contestType;
  private SSIContestStatus status;
  private Date startDate;
  private Date endDate;
  private Date displayStartDate;
  private String cmAssetCode;
  private Boolean includePersonalMessage;

  private SSIBillPayoutCodeType billPayoutCodeType;
  private SSIActivityMeasureType activityMeasureType;
  private String activityMeasureCurrencyCode;
  private SSIPayoutType payoutType;
  private Boolean sameObjectiveDescription;
  private Date claimSubmissionLastDate;
  private Boolean claimApprovalNeeded;
  private boolean includeStackRank;
  private boolean includeBonus;
  private String stackRankOrder;
  private String payoutOtherCurrencyCode;
  private String activityDescription;
  private BadgeRule badgeRule;
  private Double contestGoal;
  private Long approvedByLevel1;
  private Date dateApprovedLevel1;
  private Long approvedByLevel2;
  private Date dateApprovedLevel2;
  private String denialReason;
  private int levelApproved;
  private Long stepItUpBonusCap;
  private Long stepItUpBonusPayout;
  private Double stepItUpBonusIncrement;
  private Date lastProgressUpdateDate;
  private Boolean uploadInProgress;
  private Boolean updateInProgress;
  private Boolean includeStackRankQualifier;
  private Double stackRankQualifierAmount;
  private SSIIndividualBaselineType individualBaselineType;
  private SSIContestActivitySubmissionType activitysubmissionType;
  private String dataCollectionType;
  private Date dateLaunched;
  private boolean launchNotificationSent;
  private Set<SSIContestDocument> contestDocuments = new LinkedHashSet<SSIContestDocument>();
  private Set<SSIContestApprover> contestLevel1Approvers = new LinkedHashSet<SSIContestApprover>();
  private Set<SSIContestApprover> contestLevel2Approvers = new LinkedHashSet<SSIContestApprover>();
  private Set<SSIContestApprover> claimApprovers = new LinkedHashSet<SSIContestApprover>();
  private Set<SSIContestManager> contestManagers = new LinkedHashSet<SSIContestManager>();
  private Set<SSIContestSuperViewer> contestSuperViewers = new LinkedHashSet<SSIContestSuperViewer>();
  private Set<SSIContestParticipant> contestParticipants = new LinkedHashSet<SSIContestParticipant>();
  private Set<SSIContestActivity> contestActivities = new LinkedHashSet<SSIContestActivity>();
  private Set<SSIContestLevel> contestLevels = new LinkedHashSet<SSIContestLevel>();
  private Set<SSIContestStackRankPayout> stackRankPayouts = new LinkedHashSet<SSIContestStackRankPayout>();
  private Set<SSIContestClaimField> claimFields = new LinkedHashSet<SSIContestClaimField>();

  // Applies only to Award them now. For other contest types this will be null
  private Short awardIssuanceNumber;
  private Set<SSIContestAwardThemNow> contestAtns = new LinkedHashSet<SSIContestAwardThemNow>();

  private Date payoutIssuedDate;

  private String saveAndSendProgressUpdate;

  public static final String CONTEST_SECTION_CODE = "ssi_contest_data";
  public static final String CONTEST_CMASSET_TYPE_NAME = "Contest Name";
  public static final String CONTEST_CMASSET_CONTEST = "SSI Contest Data";
  public static final String CONTEST_CMASSET_NAME = "CONTEST_NAME";
  public static final String CONTEST_CMASSET_DOCUMENT_URL = "DOCUMENT_URL";
  public static final String CONTEST_CMASSET_DOCUMENT_DISPLAY_NAME = "DOCUMENT_DISPLAY_NAME";
  public static final String CONTEST_CMASSET_DOCUMENT_ORIGINAL_NAME = "DOCUMENT_ORIGINAL_NAME";
  public static final String CONTEST_CMASSET_MESSAGE = "PERSONAL_MESSAGE";
  public static final String CONTEST_CMASSET_DESCRIPTION = "CONTEST_DESCRIPTION";
  public static final String CONTEST_CMASSET_PREFIX = "ssi_contest_data.contest_details.";

  // below constants are used for front end
  public static final String CONTEST_TYPE_AWARD_THEM_NOW = "awardThemNow";
  public static final String CONTEST_TYPE_DO_THIS_GET_THAT = "doThisGetThat";
  public static final String CONTEST_TYPE_OBJECTIVES = "objectives";
  public static final String CONTEST_TYPE_STACK_RANK = "stackRank";
  public static final String CONTEST_TYPE_STEP_IT_UP = "stepItUp";

  // contest pax roles
  public static final String CONTEST_ROLE_PAX = "pax";
  public static final String CONTEST_ROLE_CREATOR = "creator";
  public static final String CONTEST_ROLE_MGR = "manager";
  public static final String CONTEST_ROLE_SUPERVIEWER = "superViewer";
  public static final String CONTEST_ROLE_APPROVER = "approver";
  public static final String CONTEST_ROLE_CLAIM_APPROVER = "claim_approver";

  private List<SSIContestBillCode> contestBillCodes;

  public List<SSIContestBillCode> getContestBillCodes()
  {
    return contestBillCodes;
  }

  public void setContestBillCodes( List<SSIContestBillCode> contestBillCodes )
  {
    this.contestBillCodes = contestBillCodes;
  }

  public Short getAwardIssuanceNumber()
  {
    return awardIssuanceNumber;
  }

  public void setAwardIssuanceNumber( Short awardIssuanceNumber )
  {
    this.awardIssuanceNumber = awardIssuanceNumber;
  }

  public SSIPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( SSIPromotion promotion )
  {
    this.promotion = promotion;
  }

  public Long getContestOwnerId()
  {
    return contestOwnerId;
  }

  public void setContestOwnerId( Long contestOwnerId )
  {
    this.contestOwnerId = contestOwnerId;
  }

  public SSIContestType getContestType()
  {
    return contestType;
  }

  public void setContestType( SSIContestType contestType )
  {
    this.contestType = contestType;
  }

  public SSIContestStatus getStatus()
  {
    return status;
  }

  public void setStatus( SSIContestStatus status )
  {
    this.status = status;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate( Date startDate )
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate( Date endDate )
  {
    this.endDate = endDate;
  }

  public Date getLastProgressUpdateDate()
  {
    return lastProgressUpdateDate;
  }

  public void setLastProgressUpdateDate( Date lastProgressUpdateDate )
  {
    this.lastProgressUpdateDate = lastProgressUpdateDate;
  }

  public Boolean getIncludeStackRankQualifier()
  {
    return includeStackRankQualifier;
  }

  public void setIncludeStackRankQualifier( Boolean includeStackRankQualifier )
  {
    this.includeStackRankQualifier = includeStackRankQualifier;
  }

  public Double getStackRankQualifierAmount()
  {
    return stackRankQualifierAmount;
  }

  public void setStackRankQualifierAmount( Double stackRankQualifierAmount )
  {
    this.stackRankQualifierAmount = stackRankQualifierAmount;
  }

  public Date getDisplayStartDate()
  {
    return displayStartDate;
  }

  public void setDisplayStartDate( Date displayStartDate )
  {
    this.displayStartDate = displayStartDate;
  }

  public String getCmAssetCode()
  {
    return cmAssetCode;
  }

  public void setCmAssetCode( String cmAssetCode )
  {
    this.cmAssetCode = cmAssetCode;
  }

  public Boolean getIncludePersonalMessage()
  {
    return includePersonalMessage;
  }

  public void setIncludePersonalMessage( Boolean includePersonalMessage )
  {
    this.includePersonalMessage = includePersonalMessage;
  }

  public SSIBillPayoutCodeType getBillPayoutCodeType()
  {
    return billPayoutCodeType;
  }

  public void setBillPayoutCodeType( SSIBillPayoutCodeType billPayoutCodeType )
  {
    this.billPayoutCodeType = billPayoutCodeType;
  }

  public SSIActivityMeasureType getActivityMeasureType()
  {
    return activityMeasureType;
  }

  public void setActivityMeasureType( SSIActivityMeasureType activityMeasureType )
  {
    this.activityMeasureType = activityMeasureType;
  }

  public String getActivityMeasureCurrencyCode()
  {
    return activityMeasureCurrencyCode;
  }

  public void setActivityMeasureCurrencyCode( String activityMeasureCurrencyCode )
  {
    this.activityMeasureCurrencyCode = activityMeasureCurrencyCode;
  }

  public SSIPayoutType getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( SSIPayoutType payoutType )
  {
    this.payoutType = payoutType;
  }

  public Boolean getSameObjectiveDescription()
  {
    return sameObjectiveDescription;
  }

  public void setSameObjectiveDescription( Boolean sameObjectiveDescription )
  {
    this.sameObjectiveDescription = sameObjectiveDescription;
  }

  public boolean isIncludeStackRank()
  {
    return includeStackRank;
  }

  public void setIncludeStackRank( boolean includeStackRank )
  {
    this.includeStackRank = includeStackRank;
  }

  public boolean isIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public String getStackRankOrder()
  {
    return stackRankOrder;
  }

  public void setStackRankOrder( String stackRankOrder )
  {
    this.stackRankOrder = stackRankOrder;
  }

  public String getPayoutOtherCurrencyCode()
  {
    return payoutOtherCurrencyCode;
  }

  public void setPayoutOtherCurrencyCode( String payoutOtherCurrencyCode )
  {
    this.payoutOtherCurrencyCode = payoutOtherCurrencyCode;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public BadgeRule getBadgeRule()
  {
    return badgeRule;
  }

  public void setBadgeRule( BadgeRule badgeRule )
  {
    this.badgeRule = badgeRule;
  }

  public SSIContestActivitySubmissionType getActivitysubmissionType()
  {
    return activitysubmissionType;
  }

  public void setActivitysubmissionType( SSIContestActivitySubmissionType activitysubmissionType )
  {
    this.activitysubmissionType = activitysubmissionType;
  }

  public Set<SSIContestDocument> getContestDocuments()
  {
    return contestDocuments;
  }

  public void setContestDocuments( Set<SSIContestDocument> contestDocuments )
  {
    this.contestDocuments = contestDocuments;
  }

  public Set<SSIContestApprover> getContestLevel1Approvers()
  {
    return contestLevel1Approvers;
  }

  public void setContestLevel1Approvers( Set<SSIContestApprover> contestLevel1Approvers )
  {
    this.contestLevel1Approvers = contestLevel1Approvers;
  }

  public Set<SSIContestApprover> getContestLevel2Approvers()
  {
    return contestLevel2Approvers;
  }

  public void setContestLevel2Approvers( Set<SSIContestApprover> contestLevel2Approvers )
  {
    this.contestLevel2Approvers = contestLevel2Approvers;
  }

  public Set<SSIContestApprover> getClaimApprovers()
  {
    return claimApprovers;
  }

  public void setClaimApprovers( Set<SSIContestApprover> claimApprovers )
  {
    this.claimApprovers = claimApprovers;
  }

  public Set<SSIContestManager> getContestManagers()
  {
    return contestManagers;
  }

  public void setContestManagers( Set<SSIContestManager> contestManagers )
  {
    this.contestManagers = contestManagers;
  }

  public Set<SSIContestSuperViewer> getContestSuperViewers()
  {
    return contestSuperViewers;
  }

  public void setContestSuperViewers( Set<SSIContestSuperViewer> contestSuperViewers )
  {
    this.contestSuperViewers = contestSuperViewers;
  }

  public Set<SSIContestParticipant> getContestParticipants()
  {
    return contestParticipants;
  }

  public void setContestParticipants( Set<SSIContestParticipant> contestParticipants )
  {
    this.contestParticipants = contestParticipants;
  }

  public boolean isDeleteable()
  {
    return status != null ? status.isDraft() || status.isPending() : true;
  }

  public Double getContestGoal()
  {
    return contestGoal;
  }

  public void setContestGoal( Double contestGoal )
  {
    this.contestGoal = contestGoal;
  }

  public Set<SSIContestActivity> getContestActivities()
  {
    return contestActivities;
  }

  public void setContestActivities( Set<SSIContestActivity> contestActivities )
  {
    this.contestActivities = contestActivities;
  }

  public Set<SSIContestLevel> getContestLevels()
  {
    return contestLevels;
  }

  public void setContestLevels( Set<SSIContestLevel> contestLevels )
  {
    this.contestLevels = contestLevels;
  }

  public Set<SSIContestAwardThemNow> getContestAtns()
  {
    return contestAtns;
  }

  public void setContestAtns( Set<SSIContestAwardThemNow> contestAtns )
  {
    this.contestAtns = contestAtns;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( contestType == null ? 0 : contestType.hashCode() );
    if ( contestType != null && SSIContestType.AWARD_THEM_NOW.equals( contestType.getCode() ) )
    {
      result = prime * result + ( awardIssuanceNumber == null ? 0 : awardIssuanceNumber.hashCode() );
    }
    result = prime * result + ( cmAssetCode == null ? 0 : cmAssetCode.hashCode() );
    result = prime * result + ( promotion == null ? 0 : promotion.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj )
  {
    if ( this == obj )
    {
      return true;
    }
    if ( obj == null )
    {
      return false;
    }
    if ( getClass() != obj.getClass() )
    {
      return false;
    }

    SSIContest other = (SSIContest)obj;
    if ( contestType == null )
    {
      if ( other.contestType != null )
      {
        return false;
      }
    }
    else if ( contestType.equals( other.contestType ) )
    {
      if ( SSIContestType.AWARD_THEM_NOW.equals( contestType.getCode() ) )
      {
        if ( awardIssuanceNumber == null || other.awardIssuanceNumber == null )
        {
          return false;
        }
        else if ( !awardIssuanceNumber.equals( other.awardIssuanceNumber ) )
        {
          return false;
        }
      }
    }
    else if ( !contestType.equals( other.contestType ) )
    {
      return false;
    }
    if ( cmAssetCode == null )
    {
      if ( other.cmAssetCode != null )
      {
        return false;
      }
    }
    else if ( !cmAssetCode.equals( other.cmAssetCode ) )
    {
      return false;
    }
    if ( promotion == null )
    {
      if ( other.promotion != null )
      {
        return false;
      }
    }
    else if ( !promotion.equals( other.promotion ) )
    {
      return false;
    }
    return true;
  }

  public void addContestLevel1Approver( SSIContestApprover ssiContestApprover )
  {
    ssiContestApprover.setContest( this );
    this.contestLevel1Approvers.add( ssiContestApprover );
  }

  public void addContestLevel2Approver( SSIContestApprover ssiContestApprover )
  {
    ssiContestApprover.setContest( this );
    this.contestLevel2Approvers.add( ssiContestApprover );
  }

  public void addClaimApprover( SSIContestApprover ssiClaimApprover )
  {
    ssiClaimApprover.setContest( this );
    this.claimApprovers.add( ssiClaimApprover );
  }

  public void addContestParticipant( SSIContestParticipant ssiContestParticipant )
  {
    ssiContestParticipant.setContest( this );
    this.contestParticipants.add( ssiContestParticipant );
  }

  public void addContestManager( SSIContestManager ssiContestManager )
  {
    ssiContestManager.setContest( this );
    this.contestManagers.add( ssiContestManager );
  }

  public void addContestSuperViewer( SSIContestSuperViewer ssiContestSuperViewer )
  {
    ssiContestSuperViewer.setContest( this );
    this.contestSuperViewers.add( ssiContestSuperViewer );
  }

  public void addContestActivity( SSIContestActivity contestActivity )
  {
    contestActivity.setContest( this );
    this.contestActivities.add( contestActivity );
  }

  public void addContestLevel( SSIContestLevel contestLevel )
  {
    contestLevel.setContest( this );
    this.contestLevels.add( contestLevel );
  }

  public void addStackRankPayout( SSIContestStackRankPayout stackRankPayout )
  {
    stackRankPayout.setContest( this );
    this.stackRankPayouts.add( stackRankPayout );
  }

  public void addClaimField( SSIContestClaimField claimField )
  {
    claimField.setContest( this );
    this.claimFields.add( claimField );
  }

  public Long getApprovedByLevel1()
  {
    return approvedByLevel1;
  }

  public void setApprovedByLevel1( Long approvedByLevel1 )
  {
    this.approvedByLevel1 = approvedByLevel1;
  }

  public Date getDateApprovedLevel1()
  {
    return dateApprovedLevel1;
  }

  public void setDateApprovedLevel1( Date dateApprovedLevel1 )
  {
    this.dateApprovedLevel1 = dateApprovedLevel1;
  }

  public Long getApprovedByLevel2()
  {
    return approvedByLevel2;
  }

  public void setApprovedByLevel2( Long approvedByLevel2 )
  {
    this.approvedByLevel2 = approvedByLevel2;
  }

  public Date getDateApprovedLevel2()
  {
    return dateApprovedLevel2;
  }

  public void setDateApprovedLevel2( Date dateApprovedLevel2 )
  {
    this.dateApprovedLevel2 = dateApprovedLevel2;
  }

  public String getDenialReason()
  {
    return denialReason;
  }

  public void setDenialReason( String denialReason )
  {
    this.denialReason = denialReason;
  }

  public int getLevelApproved()
  {
    return levelApproved;
  }

  public void setLevelApproved( int levelApproved )
  {
    this.levelApproved = levelApproved;
  }

  public Long getStepItUpBonusCap()
  {
    return stepItUpBonusCap;
  }

  public void setStepItUpBonusCap( Long stepItUpBonusCap )
  {
    this.stepItUpBonusCap = stepItUpBonusCap;
  }

  public Long getStepItUpBonusPayout()
  {
    return stepItUpBonusPayout;
  }

  public void setStepItUpBonusPayout( Long stepItUpBonusPayout )
  {
    this.stepItUpBonusPayout = stepItUpBonusPayout;
  }

  public Double getStepItUpBonusIncrement()
  {
    return stepItUpBonusIncrement;
  }

  public void setStepItUpBonusIncrement( Double stepItUpBonusIncrement )
  {
    this.stepItUpBonusIncrement = stepItUpBonusIncrement;
  }

  public SSIIndividualBaselineType getIndividualBaselineType()
  {
    return individualBaselineType;
  }

  public void setIndividualBaselineType( SSIIndividualBaselineType individualBaselineType )
  {
    this.individualBaselineType = individualBaselineType;
  }

  public Set<SSIContestStackRankPayout> getStackRankPayouts()
  {
    return stackRankPayouts;
  }

  public void setStackRankPayouts( Set<SSIContestStackRankPayout> stackRankPayouts )
  {
    this.stackRankPayouts = stackRankPayouts;
  }

  public Set<SSIContestClaimField> getClaimFields()
  {
    return claimFields;
  }

  public void setClaimFields( Set<SSIContestClaimField> claimFields )
  {
    this.claimFields = claimFields;
  }

  public Long getCreatorId()
  {
    return this.getContestOwnerId();
  }

  public Boolean isUploadInProgress()
  {
    return uploadInProgress;
  }

  public void setUploadInProgress( Boolean uploadInProgress )
  {
    this.uploadInProgress = uploadInProgress;
  }

  public Date getPayoutIssuedDate()
  {
    return payoutIssuedDate;
  }

  public void setPayoutIssuedDate( Date payoutIssuedDate )
  {
    this.payoutIssuedDate = payoutIssuedDate;
  }

  public String getSaveAndSendProgressUpdate()
  {
    return saveAndSendProgressUpdate;
  }

  public void setSaveAndSendProgressUpdate( String saveAndSendProgressUpdate )
  {
    this.saveAndSendProgressUpdate = saveAndSendProgressUpdate;
  }

  public Date getClaimSubmissionLastDate()
  {
    return claimSubmissionLastDate;
  }

  public void setClaimSubmissionLastDate( Date claimSubmissionLastDate )
  {
    this.claimSubmissionLastDate = claimSubmissionLastDate;
  }

  public Boolean getClaimApprovalNeeded()
  {
    return claimApprovalNeeded;
  }

  public void setClaimApprovalNeeded( Boolean claimApprovalNeeded )
  {
    this.claimApprovalNeeded = claimApprovalNeeded;
  }

  public String getDataCollectionType()
  {
    return dataCollectionType;
  }

  public void setDataCollectionType( String dataCollectionType )
  {
    this.dataCollectionType = dataCollectionType;
  }

  public Date getDateLaunched()
  {
    return dateLaunched;
  }

  public void setDateLaunched( Date dateLaunched )
  {
    this.dateLaunched = dateLaunched;
  }

  public boolean isLaunchNotificationSent()
  {
    return launchNotificationSent;
  }

  public void setLaunchNotificationSent( boolean launchNotificationSent )
  {
    this.launchNotificationSent = launchNotificationSent;
  }

  public String getContestTypeName()
  {
    String contestName = "";
    if ( this.getContestType().getCode().equals( SSIContestType.AWARD_THEM_NOW ) )
    {
      contestName = SSIContest.CONTEST_TYPE_AWARD_THEM_NOW;
    }
    else if ( this.getContestType().getCode().equals( SSIContestType.DO_THIS_GET_THAT ) )
    {
      contestName = SSIContest.CONTEST_TYPE_DO_THIS_GET_THAT;
    }
    else if ( this.getContestType().getCode().equals( SSIContestType.OBJECTIVES ) )
    {
      contestName = SSIContest.CONTEST_TYPE_OBJECTIVES;
    }
    else if ( this.getContestType().getCode().equals( SSIContestType.STACK_RANK ) )
    {
      contestName = SSIContest.CONTEST_TYPE_STACK_RANK;
    }
    else if ( this.getContestType().getCode().equals( SSIContestType.STEP_IT_UP ) )
    {
      contestName = SSIContest.CONTEST_TYPE_STEP_IT_UP;
    }
    return contestName;
  }

  public static SSIContestType getContestTypeFromName( String contestName )
  {
    SSIContestType returnContestType = null;
    if ( CONTEST_TYPE_AWARD_THEM_NOW.equals( contestName ) )
    {
      returnContestType = SSIContestType.lookup( SSIContestType.AWARD_THEM_NOW );
    }
    else if ( CONTEST_TYPE_DO_THIS_GET_THAT.equals( contestName ) )
    {
      returnContestType = SSIContestType.lookup( SSIContestType.DO_THIS_GET_THAT );
    }
    else if ( CONTEST_TYPE_OBJECTIVES.equals( contestName ) )
    {
      returnContestType = SSIContestType.lookup( SSIContestType.OBJECTIVES );
    }
    else if ( CONTEST_TYPE_STACK_RANK.equals( contestName ) )
    {
      returnContestType = SSIContestType.lookup( SSIContestType.STACK_RANK );
    }
    else if ( CONTEST_TYPE_STEP_IT_UP.equals( contestName ) )
    {
      returnContestType = SSIContestType.lookup( SSIContestType.STEP_IT_UP );
    }
    return returnContestType;
  }

  public String getDisplayBillCode()
  {
    if ( SSIBillPayoutCodeType.CREATOR.equals( this.billPayoutCodeType.getCode() ) )
    {
      return CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.CREATOR_ORG_UNIT" );
    }
    else if ( SSIBillPayoutCodeType.PARTICIPANT.equals( this.billPayoutCodeType.getCode() ) )
    {
      return CmsResourceBundle.getCmsBundle().getString( "ssi_contest.approvals.summary.PARTICIPANT" );
    }
    else
    {
      return StringUtils.EMPTY;
    }
  }

  public String getContestNameFromCM()
  {
    String contestName = null;
    if ( this.cmAssetCode != null )
    {
      contestName = CmsResourceBundle.getCmsBundle().getString( this.cmAssetCode, CONTEST_CMASSET_NAME );
    }
    return StringEscapeUtils.unescapeHtml4( contestName );
  }

  public String getPersonalMessageFromCM()
  {
    String personalMessage = null;
    if ( this.cmAssetCode != null )
    {
      personalMessage = CmsResourceBundle.getCmsBundle().getString( this.cmAssetCode, CONTEST_CMASSET_MESSAGE );
    }
    return StringEscapeUtils.unescapeHtml4( personalMessage );
  }

  public String getContestDescriptionFromCM()
  {
    String contestDescription = null;
    if ( this.cmAssetCode != null )
    {
      contestDescription = CmsResourceBundle.getCmsBundle().getString( this.cmAssetCode, CONTEST_CMASSET_DESCRIPTION );
    }
    return StringEscapeUtils.unescapeHtml4( contestDescription );
  }

  public Boolean getUpdateInProgress()
  {
    return updateInProgress;
  }

  public void setUpdateInProgress( Boolean updateInProgress )
  {
    this.updateInProgress = updateInProgress;
  }
}
