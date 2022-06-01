
package com.biperf.core.ui.ssi.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.enums.SSIIndividualBaselineType;
import com.biperf.core.domain.ssi.SSIContest;
import com.biperf.core.domain.ssi.SSIContestParticipant;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.SSIContestUtil;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.ssi.SSIContestPaxProgressDetailValueBean;
import com.biperf.core.value.ssi.SSIContestProgressValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPaxValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankPayoutValueBean;
import com.biperf.core.value.ssi.SSIContestStackRankTeamValueBean;
import com.biperf.core.value.ssi.SSIContestUniqueCheckValueBean;
import com.biperf.core.value.ssi.SSIContestValueBean;
import com.biperf.core.value.ssi.SSIPaxContestLevelValueBean;
import com.biperf.core.value.ssi.SSIPaxDTGTActivityProgressValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

@JsonInclude( value = Include.NON_NULL )
public class SSIPaxContestDataView implements Comparable<SSIPaxContestDataView>
{
  // contest properties
  private String id;
  private String clientState;
  private String contestType;
  private String updatedOnDate;
  private boolean progressLoaded;
  private String startDate;
  private String endDate;
  private Integer daysToEnd;
  private Integer daysToStart;
  private String name;
  private String description;
  private String status;
  private String attachmentTitle;
  private String attachmentUrl;
  private String attachmentType;
  private String activityDescription;
  private String payoutType;
  private Boolean includeBonus;
  private Boolean includeStackRanking;
  private Boolean goalAchieved;
  private Boolean includeSubmitClaim;
  private SSIStackRankParticipantViewBean stackRank; // for pax
  private List<SSIStackRankParticipantViewBean> stackRankParticipants; // for creator/manager also
                                                                       // for pax when stack rank
                                                                       // contest is closed

  private List<SSIStackRankParticipantViewBean> leaders; // for creator/manager/pax for tile
  private Long contestId;// FE need it for bug #59993

  // general properties
  private String creatorDetailPageUrl;
  private String managerDetailPageUrl;
  private String participantDetailPageUrl;
  private String role;
  private Boolean creator;
  private Boolean manager;
  private String creatorName;
  private Integer unaccountedParticipants;
  private Integer achievedParticipantsCount;
  private Integer bonusParticipantsCount;
  private Integer participantsCount;

  // below three properties used to display award section
  private String totalPayout;
  private String payoutDescription;
  private String shopUrl;

  private String bonusForEvery;
  private SSIPaxContestBadgeView badge;

  // objectives properties
  private Boolean objectiveAchieved;
  private Boolean bonusEligible;

  // dtgt properties
  private List<SSIDtgtActivityView> activities;

  // step it up properties
  private String baseline;
  private String baselineType;
  private List<SSIContestLevelView> contestLevels;

  // objectives,dtgt, step it up properties, stack rank
  private String goal;
  private String progress;
  private String remaining;
  private String bonusActivity;
  private Long bonusPayout;
  private String bonusEarned;
  private Long percentProgress;
  private String payout;
  private String payoutProgress;
  private Long payoutPercentProgress;
  private String payoutCap;
  private String payoutBonusCap;
  private String payoutRemaining;
  private Boolean includeMinimumQualifier;
  private String minQualifier;

  // stack rank properties
  private List<SSIContestStackRankPayoutView> payouts;
  private String behindLeader;

  // submit claim
  private String claimUrl;

  // future properties
  // private Integer claimsCount;
  // private String levelAchieved;
  // private Boolean submitClaimActive;
  // private Boolean includeSubmitClaim;

  private Boolean hasApprovePayout = Boolean.FALSE;
  private Boolean multipleActivities = Boolean.FALSE;
  private Boolean payoutIssued = Boolean.FALSE;

  private Boolean viewOrHideAllSr;

  private String activityMeasuredIn;
  private String currencyAbbr;

  // Participant Drilldown properties
  private Boolean isParticipantDrillDown;
  private String participantDrillName;

  // error message properties
  private List<WebErrorMessage> messages;

  // bug fix #62660
  private Boolean hasMultipleObjectives;

  // Approve Claims button on creator details page
  private Integer claimDaysToEnd;

  // Report Drilldown properties
  private Boolean isReportDrillDown;

  private Long objectiveBonusCap;

  public SSIPaxContestDataView()
  {

  }

  public SSIPaxContestDataView( WebErrorMessage error )
  {
    messages = new ArrayList<WebErrorMessage>();
    messages.add( error );
  }

  // pax
  public SSIPaxContestDataView( SSIContest contest,
                                SSIContestPaxProgressDetailValueBean paxProgressSummary,
                                String shoppingUrl,
                                SSIContestValueBean valueBean,
                                List<SSIContestStackRankPaxValueBean> stackRanks,
                                Long currentUserId,
                                SSIContestParticipant contestParticipant,
                                String creatorName,
                                String siteUrl,
                                boolean isCreatorOrManager,
                                String paxName )
  {
    // contest
    populateContestData( contest, valueBean, creatorName, siteUrl, SSIContest.CONTEST_ROLE_PAX, isCreatorOrManager, currentUserId, paxName );

    if ( SSIContestType.OBJECTIVES.equals( contest.getContestType().getCode() ) )
    {
      populatePaxObjectiveContestData( contest, paxProgressSummary, contestParticipant, valueBean );
    }
    else if ( SSIContestType.DO_THIS_GET_THAT.equals( contest.getContestType().getCode() ) )
    {
      populatePaxDoThisGetThatContestData( contest, paxProgressSummary, valueBean );
    }
    else if ( SSIContestType.STEP_IT_UP.equals( contest.getContestType().getCode() ) )
    {
      populatePaxStepItUpContestData( contest, paxProgressSummary, valueBean, siteUrl, isCreatorOrManager );
    }
    else if ( SSIContestType.STACK_RANK.equals( contest.getContestType().getCode() ) )
    {
      populatePaxStackRankContestData( contest, paxProgressSummary, valueBean, currentUserId, stackRanks, siteUrl );
    }

    this.shopUrl = shoppingUrl;
    this.role = SSIContest.CONTEST_ROLE_PAX;
    this.creator = false;
    this.manager = false;
  }

  // creator/manager full view
  public SSIPaxContestDataView( SSIContest contest,
                                List<SSIContestProgressValueBean> contestProgressData,
                                String userRole,
                                SSIContestValueBean valueBean,
                                List<SSIContestStackRankPaxValueBean> stackRanks,
                                SSIContestUniqueCheckValueBean uniqueCheckValueBean,
                                String creatorName,
                                boolean isCreatorOrManager )
  {
    // contest
    populateContestData( contest, valueBean, creatorName, null, userRole, isCreatorOrManager, null, "" );

    populateCreatorManagerContestData( contest, contestProgressData, userRole, stackRanks, valueBean, uniqueCheckValueBean, isCreatorOrManager );
  }

  // creator/manager partial view
  public SSIPaxContestDataView( SSIContest contest, SSIContestProgressValueBean contestProgress, String userRole, SSIContestValueBean valueBean, boolean isCreatorOrManager )
  {
    // contest
    populateContestData( contest, valueBean, this.creatorName, null, userRole, isCreatorOrManager, null, "" );

    // contest progress data for creator and manager
    populateCreatorManagerContestData( contest, contestProgress, userRole, valueBean, isCreatorOrManager );
  }

  private SSIPaxContestLevelValueBean populateLevelEarned( List<SSIPaxContestLevelValueBean> levels )
  {
    SSIPaxContestLevelValueBean highestLevelEarned = null;
    for ( SSIPaxContestLevelValueBean level : levels )
    {
      if ( level.getCompleted() )
      {
        highestLevelEarned = level;
      }
    }
    return highestLevelEarned;
  }

  private SSIContestStackRankPayoutValueBean populatePayoutEarned( List<SSIContestStackRankPayoutValueBean> payouts, int rank )
  {
    SSIContestStackRankPayoutValueBean payoutEarned = null;
    if ( rank > 0 )
    {
      for ( SSIContestStackRankPayoutValueBean payout : payouts )
      {
        if ( payout.getRank().intValue() == rank )
        {
          payoutEarned = payout;
        }
      }
    }
    return payoutEarned;
  }

  private Long populatePayoutPointsEarned( List<SSIPaxContestLevelValueBean> levels )
  {
    SSIPaxContestLevelValueBean highestLevelEarned = null;
    for ( SSIPaxContestLevelValueBean level : levels )
    {
      if ( level.getCompleted() )
      {
        highestLevelEarned = level;
      }
    }
    return highestLevelEarned == null ? null : highestLevelEarned.getPayout();
  }

  private String populatePayoutAwardDescEarned( List<SSIPaxContestLevelValueBean> levels )
  {
    SSIPaxContestLevelValueBean highestLevelEarned = null;
    for ( SSIPaxContestLevelValueBean level : levels )
    {
      if ( level.getCompleted() )
      {
        highestLevelEarned = level;
      }
    }
    return highestLevelEarned == null ? null : highestLevelEarned.getPayoutDescription();
  }

  private List<SSIContestStackRankPayoutView> transformStackRankPayoutsValueBeanToView( SSIContest contest,
                                                                                        String payoutPrefix,
                                                                                        List<SSIContestStackRankPayoutValueBean> payoutValueBeans,
                                                                                        String contestRole )
  {
    List<SSIContestStackRankPayoutView> payoutViews = new ArrayList<SSIContestStackRankPayoutView>();
    if ( payoutValueBeans != null && payoutValueBeans.size() > 0 )
    {
      for ( SSIContestStackRankPayoutValueBean payoutValueBean : payoutValueBeans )
      {
        SSIContestStackRankPayoutView payoutView = new SSIContestStackRankPayoutView();
        if ( payoutValueBean.getBadge() != null && !StringUtil.isNullOrEmpty( payoutValueBean.getBadge().getName() ) && !StringUtil.isNullOrEmpty( payoutValueBean.getBadge().getImg() ) )
        {
          payoutView.setBadge( new SSIPaxContestBadgeView( payoutValueBean.getBadge().getId(), payoutValueBean.getBadge().getName(), payoutValueBean.getBadge().getImg() ) );
        }
        if ( contest.getPayoutType() != null && contest.getPayoutType().isOther() )
        {
          payoutView.setPayout( payoutValueBean.getPayoutDescription()
              + ( SSIContest.CONTEST_ROLE_PAX.equals( contestRole ) || SSIContest.CONTEST_ROLE_MGR.equals( contestRole ) ? "" : " (" + payoutPrefix + payoutValueBean.getPayout() + ")" ) );
        }
        else
        {
          payoutView.setPayout( SSIContestUtil.getFormattedValue( payoutValueBean.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
        }
        payoutView.setRank( payoutValueBean.getRank() );
        payoutViews.add( payoutView );
      }
    }
    return payoutViews;
  }

  private SSIStackRankParticipantViewBean transformStackRankValueBeanToView( SSIContest contest,
                                                                             String activityPrefix,
                                                                             int precision,
                                                                             SSIContestStackRankPaxValueBean stackRankValueBean,
                                                                             String contestRole )
  {
    SSIStackRankParticipantViewBean stackRankView = new SSIStackRankParticipantViewBean();
    stackRankView.setAvatarUrl( stackRankValueBean.getAvatarUrl() );
    stackRankView.setCurrentUser( stackRankValueBean.getCurrentUser() );
    stackRankView.setFirstName( stackRankValueBean.getFirstName() );
    stackRankView.setLastName( stackRankValueBean.getLastName() );
    stackRankView.setParticipantId( stackRankValueBean.getParticipantId() );
    stackRankView.setParticipantsCount( stackRankValueBean.getParticipantsCount() );
    stackRankView.setRank( stackRankValueBean.getRank() );
    stackRankView.setScore( stackRankValueBean.getScore() != null ? activityPrefix + SSIContestUtil.getFormattedValue( stackRankValueBean.getScore(), precision ) : null );
    stackRankView.setTeamMember( stackRankValueBean.getTeamMember() );
    stackRankView.setThumbnailUrl( stackRankValueBean.getThumbnailUrl() );
    if ( SSIContest.CONTEST_ROLE_PAX.equals( contestRole )
        || SSIContest.CONTEST_ROLE_MGR.equals( contestRole ) && ! ( stackRankValueBean.getTeamMember() != null && stackRankValueBean.getTeamMember().booleanValue() ) )
    {
      stackRankView.setContestUrl( null );
    }
    else
    {
      stackRankView.setContestUrl( stackRankValueBean.getContestUrl() );
    }

    return stackRankView;
  }

  private List<SSIStackRankParticipantViewBean> transformStackRankTeamValueBeanToView( SSIContest contest,
                                                                                       String activityPrefix,
                                                                                       int precision,
                                                                                       List<SSIContestStackRankTeamValueBean> stackRankTeamValueBeans,
                                                                                       String contestRole )
  {
    List<SSIStackRankParticipantViewBean> stackRankViews = null;
    if ( stackRankTeamValueBeans != null )
    {
      stackRankViews = new ArrayList<SSIStackRankParticipantViewBean>();
      for ( SSIContestStackRankTeamValueBean stackRankTeamValueBean : stackRankTeamValueBeans )
      {
        stackRankViews.add( transformStackRankTeamValueBeanToView( contest, activityPrefix, precision, stackRankTeamValueBean, contestRole ) );
      }
    }
    return stackRankViews;
  }

  private SSIStackRankParticipantViewBean transformStackRankTeamValueBeanToView( SSIContest contest,
                                                                                 String activityPrefix,
                                                                                 int precision,
                                                                                 SSIContestStackRankTeamValueBean stackRankTeamValueBean,
                                                                                 String contestRole )
  {
    SSIStackRankParticipantViewBean stackRankView = new SSIStackRankParticipantViewBean();
    stackRankView.setParticipantId( stackRankTeamValueBean.getParticipantId() );
    stackRankView.setFirstName( stackRankTeamValueBean.getFirstName() );
    stackRankView.setLastName( stackRankTeamValueBean.getLastName() );
    stackRankView.setAvatarUrl( stackRankTeamValueBean.getAvatarUrl() );
    stackRankView.setRank( stackRankTeamValueBean.getRank() );
    stackRankView.setActivityMeasureType( stackRankTeamValueBean.getActivityMeasureType() );
    // setting property with null, jackson will exclude it to write to response
    stackRankView.setScore( stackRankTeamValueBean.getScore() != null ? activityPrefix + SSIContestUtil.getFormattedValue( stackRankTeamValueBean.getScore(), precision ) : null );
    stackRankView.setActivityId( stackRankTeamValueBean.getActivityId() != null && stackRankTeamValueBean.getActivityId().longValue() != 0 ? stackRankTeamValueBean.getActivityId() : null );
    if ( stackRankTeamValueBean.getTeamMember() == null || !stackRankTeamValueBean.getTeamMember().booleanValue() )
    {
      stackRankView.setTeamMember( Boolean.FALSE );
    }
    else
    {
      stackRankView.setTeamMember( Boolean.TRUE );
    }
    if ( SSIContest.CONTEST_ROLE_PAX.equals( contestRole )
        || SSIContest.CONTEST_ROLE_MGR.equals( contestRole ) && ! ( stackRankTeamValueBean.getTeamMember() != null && stackRankTeamValueBean.getTeamMember().booleanValue() ) )
    {
      stackRankView.setContestUrl( null );
    }
    else
    {
      stackRankView.setContestUrl( SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), stackRankTeamValueBean.getParticipantId() ) );
    }
    return stackRankView;
  }

  private List<SSIContestLevelView> populateContestLevels( List<SSIPaxContestLevelValueBean> levelValueBeans, SSIContestValueBean valueBean, SSIContest contest, boolean isCreatorOrManager )
  {
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );
    List<SSIContestLevelView> levelViews = new ArrayList<SSIContestLevelView>();
    for ( SSIPaxContestLevelValueBean valueBeanLevel : levelValueBeans )
    {
      SSIContestLevelView viewLevel = new SSIContestLevelView();
      viewLevel.setAvatarUrl( valueBeanLevel.getAvatarUrl() );
      viewLevel.setBadgeName( valueBeanLevel.getBadgeName() );
      viewLevel.setBadgeRuleId( valueBeanLevel.getBadgeRuleId() );
      viewLevel.setBadgeUrl( valueBeanLevel.getBadgeUrl() );
      viewLevel.setBaseline( valueBeanLevel.getBaseline() != null ? activityPrefix + SSIContestUtil.getFormattedValue( valueBeanLevel.getBaseline(), precision ) : null );
      viewLevel.setCompleted( valueBeanLevel.getCompleted() );
      viewLevel.setCurrentLevel( valueBeanLevel.getCurrentLevel() );
      viewLevel.setGoal( valueBeanLevel.getGoal() != null ? activityPrefix + SSIContestUtil.getFormattedValue( valueBeanLevel.getGoal(), precision ) : null );
      viewLevel.setGoalAmount( valueBeanLevel.getGoalAmount() != null ? activityPrefix + SSIContestUtil.getFormattedValue( valueBeanLevel.getGoalAmount(), precision ) : null );
      if ( contest.getIndividualBaselineType().isCurrencyOverBaseline() )
      {
        if ( valueBeanLevel.getGoal() != null && valueBeanLevel.getBaseline() != null )
        {
          Double actualGoal = valueBeanLevel.getGoal() - new Double( valueBeanLevel.getBaseline() );
          viewLevel.setGoalPercent( activityPrefix + SSIContestUtil.getFormattedValue( actualGoal, precision ) );
        }
        else
        {
          viewLevel.setGoalPercent( valueBeanLevel.getGoalPercent() );
        }
      }
      else if ( contest.getIndividualBaselineType().isPercentageOverBaseline() )
      {
        if ( isCreatorOrManager )
        {
          if ( valueBeanLevel.getGoalAmount() != null && valueBeanLevel.getBaseline() != null )
          {
            String goalPercentage = SSIContestUtil.getFormattedValue( (long)Math.floor( ( valueBeanLevel.getGoalAmount() * 100 ) / valueBeanLevel.getBaseline() ),
                                                                      SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
            viewLevel.setGoalPercent( goalPercentage + "%" );
          }
          else
          {
            viewLevel.setGoalPercent( valueBeanLevel.getGoalPercent() );
          }
        }
        else
        {
          if ( valueBeanLevel.getGoalPercent() != null )
          {
            viewLevel.setGoalPercent( SSIContestUtil.getFormattedValue( Double.parseDouble( valueBeanLevel.getGoalPercent() ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + "%" );
          }
        }
      }
      else
      {
        viewLevel.setGoalPercent( valueBeanLevel.getGoalPercent() );
      }
      viewLevel.setId( valueBeanLevel.getId() );
      viewLevel.setIndex( valueBeanLevel.getIndex() );
      viewLevel.setLevelMax( valueBeanLevel.getLevelMax() );
      viewLevel.setLevelMin( valueBeanLevel.getLevelMin() );
      viewLevel.setName( valueBeanLevel.getName() );
      viewLevel.setParticipantsCount( valueBeanLevel.getParticipantsCount() );
      if ( contest.getPayoutType().isPoints() )
      {
        viewLevel.setPayout( SSIContestUtil.getFormattedValue( valueBeanLevel.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      }
      else
      {
        viewLevel.setPayout( valueBeanLevel.getPayoutDescription() );
      }
      viewLevel.setProgress( valueBeanLevel.getProgress() != null ? activityPrefix + SSIContestUtil.getFormattedValue( valueBeanLevel.getProgress(), precision ) : null );
      viewLevel.setProgressFormatted( valueBeanLevel.getProgressFormatted() );
      viewLevel.setRemaining( valueBeanLevel.getRemaining() != null ? activityPrefix + SSIContestUtil.getFormattedValue( valueBeanLevel.getRemaining(), precision ) : null );
      viewLevel.setRemainingProgress( valueBeanLevel.getRemainingProgress() );
      viewLevel.setStackRank( valueBeanLevel.getStackRank() );
      viewLevel.setBonusEarned( valueBeanLevel.getBonusEarned() );
      levelViews.add( viewLevel );
    }
    return levelViews;
  }

  private void populateContestData( SSIContest contest, SSIContestValueBean valueBean, String creatorName, String siteUrl, String userRole, boolean isCreatorOrManager, Long paxId, String paxName )
  {
    populateDetailPageUrls( userRole );

    this.contestId = contest.getId();
    this.clientState = SSIContestUtil.getClientState( contest.getId() );
    this.contestType = contest.getContestTypeName();
    if ( contest.getLastProgressUpdateDate() != null )
    {
      hasApprovePayout = DateUtils.getDaysToGoFromToday( contest.getEndDate() ) == 0 && !contest.getStatus().isFinalizeResults();
    }
    if ( contest.getStatus().isFinalizeResults() )
    {
      payoutIssued = true;
    }
    this.updatedOnDate = SSIContestUtil.getContestProgressUpdateDate( contest );
    this.progressLoaded = contest.getLastProgressUpdateDate() != null;
    this.startDate = DateUtils.toDisplayString( contest.getStartDate() );
    this.endDate = DateUtils.toDisplayString( contest.getEndDate() );
    this.daysToEnd = DateUtils.getDaysToGoFromToday( contest.getEndDate() );
    if ( contest.getStatus().isPending() )
    {
      this.daysToStart = DateUtils.getDaysToGoFromToday( contest.getStartDate() ) - 1;
    }
    else
    {
      this.daysToStart = null;
    }
    String contestName = valueBean.getContestName();
    this.name = contestName.length() > 50 ? contestName.substring( 0, 50 ) : contestName;
    this.description = valueBean.getDescription();
    this.status = contest.getStatus().getCode();
    this.attachmentTitle = !StringUtil.isNullOrEmpty( valueBean.getAttachmentTitle() ) ? valueBean.getAttachmentTitle() : null;
    this.attachmentUrl = !StringUtil.isNullOrEmpty( valueBean.getAttachmentUrl() ) ? valueBean.getAttachmentUrl() : null;
    this.attachmentType = !StringUtil.isNullOrEmpty( valueBean.getAttachmentType() ) ? valueBean.getAttachmentType() : null;
    this.activityDescription = contest.getActivityDescription();
    this.payoutType = contest.getPayoutType() != null ? contest.getPayoutType().getCode() : null;
    this.includeBonus = contest.isIncludeBonus();
    this.badge = new SSIPaxContestBadgeView( contest.getBadgeRule() );
    // to show the stack rank link creator has to upload the activity
    this.includeStackRanking = contest.getContestType().isStackRank() ? true : contest.isIncludeStackRank() && contest.getLastProgressUpdateDate() != null;
    this.includeMinimumQualifier = contest.getIncludeStackRankQualifier();
    this.activityMeasuredIn = contest.getActivityMeasureType() != null ? contest.getActivityMeasureType().getCode() : null;
    this.currencyAbbr = valueBean.getActivityMeasureCurrency() != null ? valueBean.getActivityMeasureCurrency().getCurrencyCode() : null;
    this.creatorName = creatorName;
    this.includeSubmitClaim = SSIContestUtil.CLAIM_SUBMISSION.equals( contest.getDataCollectionType() );
    if ( includeSubmitClaim )
    {
      this.claimDaysToEnd = DateUtils.getDaysToGoFromToday( contest.getClaimSubmissionLastDate(), true );
      if ( SSIContest.CONTEST_ROLE_PAX.equals( userRole ) && this.claimDaysToEnd.intValue() > 0 && !contest.getStatus().isFinalizeResults() )
      {
        Map params = new HashMap();
        params.put( "contestId", contest.getId() );
        this.claimUrl = ClientStateUtils.generateEncodedLink( siteUrl, PageConstants.SSI_PARTICIPANT_SUBMIT_CLAIM, params );
      }
    }
    if ( isCreatorOrManager && paxId != null )
    {
      this.isParticipantDrillDown = true;
      this.participantDrillName = paxName;
      this.id = SSIContestUtil.getClientState( contest.getId(), paxId );
    }
    else
    {
      this.id = SSIContestUtil.getClientState( contest.getId() );
    }
  }

  private void populateDetailPageUrls( String userRole )
  {
    if ( SSIContest.CONTEST_ROLE_CREATOR.equals( userRole ) )
    {
      this.creatorDetailPageUrl = "ssi/creatorContestList.do?method=display";
    }
    else if ( SSIContest.CONTEST_ROLE_MGR.equals( userRole ) )
    {
      this.managerDetailPageUrl = "ssi/managerContestList.do?method=display";
    }
    else
    {
      this.participantDetailPageUrl = "ssi/participantContestList.do?method=display";
    }
  }

  private void populatePaxObjectiveContestData( SSIContest contest, SSIContestPaxProgressDetailValueBean paxProgress, SSIContestParticipant contestParticipant, SSIContestValueBean valueBean )
  {
    int decimalPrecision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );
    String progressLoadDate = null;

    // progress
    this.startDate = progressLoadDate == null ? DateUtils.toDisplayString( contest.getStartDate() ) : progressLoadDate;
    this.goal = paxProgress.getObjectiveAmount() != null ? activityPrefix + SSIContestUtil.getFormattedValue( paxProgress.getObjectiveAmount(), decimalPrecision ) : "";
    this.progress = paxProgress.getActivityAmount() != null ? activityPrefix + SSIContestUtil.getFormattedValue( paxProgress.getActivityAmount(), decimalPrecision ) : "";
    this.remaining = paxProgress.getToGoAmount() != null ? activityPrefix + SSIContestUtil.getFormattedValue( paxProgress.getToGoAmount(), decimalPrecision ) : "";
    this.percentProgress = paxProgress.getPercentageAcheived() != null ? paxProgress.getPercentageAcheived().longValue() : null;
    this.payout = SSIContestUtil.getFormattedValue( paxProgress.getObjectivePayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
    this.payoutDescription = paxProgress.getObjectivePayoutDescription();
    this.objectiveAchieved = new Double( 0 ).equals( paxProgress.getToGoAmount() );

    // bonus
    if ( contest.isIncludeBonus() )
    {
      this.bonusForEvery = paxProgress.getObjectiveBonusIncrement() != null
          ? activityPrefix + SSIContestUtil.getFormattedValue( paxProgress.getObjectiveBonusIncrement(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION )
          : "";
      this.bonusPayout = paxProgress.getObjectiveBonusPayout();
      this.bonusEarned = SSIContestUtil.getFormattedValue( paxProgress.getBonusEarned(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
      if ( paxProgress.getActivityAmount() != null && paxProgress.getObjectiveAmount() != null )
      {
        this.bonusActivity = activityPrefix + SSIContestUtil.getFormattedValue( paxProgress.getActivityAmount() - paxProgress.getObjectiveAmount(), decimalPrecision );
      }
      if ( this.objectiveAchieved && !contest.getStatus().isFinalizeResults() )
      {
        this.bonusEligible = Boolean.TRUE;
      }
    }

    // stack rank
    if ( contest.isIncludeStackRank() && paxProgress.getStackRank() != null )
    {
      this.setStackRank( new SSIStackRankParticipantViewBean( paxProgress.getStackRank(), paxProgress.getTotalPax(), paxProgress.getAvatarUrl() ) );
    }
    else
    {
      this.setStackRank( null );
    }

    this.setActivityDescription( contest.getSameObjectiveDescription() != null && contest.getSameObjectiveDescription()
        ? contest.getActivityDescription()
        : contestParticipant.getActivityDescription() );

    // final results
    if ( contest.getStatus().isFinalizeResults() || contest.getStatus().isClosed() )
    {
      if ( this.objectiveAchieved )
      {
        Long award = paxProgress.getObjectivePayout() + ( paxProgress.getBonusEarned() != null ? paxProgress.getBonusEarned() : 0 );
        if ( contest.getPayoutType().isPoints() )
        {
          this.totalPayout = SSIContestUtil.getFormattedValue( award, SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        }
        else if ( contest.getPayoutType().isOther() )
        {
          this.totalPayout = paxProgress.getObjectivePayoutDescription();
        }
      }
      else
      {
        this.badge = null;
      }
    }

    if ( paxProgress.getObjectiveBonusCap() != null )
    {
      this.objectiveBonusCap = paxProgress.getObjectiveBonusCap();
    }

  }

  private void populatePaxDoThisGetThatContestData( SSIContest contest, SSIContestPaxProgressDetailValueBean paxProgress, SSIContestValueBean valueBean )
  {
    long award = 0;
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );
    List<SSIDtgtActivityView> activitiesView = new ArrayList<SSIDtgtActivityView>();
    int activityAwardCount = 0;
    for ( SSIPaxDTGTActivityProgressValueBean dtgtValueBean : paxProgress.getActivities() )
    {
      SSIDtgtActivityView activityView = new SSIDtgtActivityView();
      activityView.setContestId( dtgtValueBean.getContestId() );
      activityView.setActivityId( dtgtValueBean.getActivityId() );
      activityView.setActivityDescription( dtgtValueBean.getActivityDescription() );
      activityView.setName( dtgtValueBean.getActivityDescription() );
      activityView.setGoal( activityPrefix + SSIContestUtil.getFormattedValue( dtgtValueBean.getGoal(), precision ) );
      String progress = SSIContestUtil.getFormattedValue( dtgtValueBean.getProgress(), precision );
      if ( progress != null )
      {
        activityView.setSubmitted( activityPrefix + SSIContestUtil.getFormattedValue( dtgtValueBean.getProgress(), precision ) );
      }
      activityView.setPercentProgress( SSIContestUtil.getFormattedValue( dtgtValueBean.getPercentProgress(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      activityView.setMinQualifier( activityPrefix + SSIContestUtil.getFormattedValue( dtgtValueBean.getMinQualifier(), precision ) );
      activityView.setForEvery( activityPrefix + SSIContestUtil.getFormattedValue( dtgtValueBean.getForEvery(), precision ) );
      activityView.setPayoutDescription( dtgtValueBean.getPayoutDescription() );
      if ( contest.getPayoutType().isPoints() )
      {
        activityView.setWillEarn( SSIContestUtil.getFormattedValue( dtgtValueBean.getWillEarn(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      }
      else if ( contest.getPayoutType().isOther() && dtgtValueBean.getPayoutQuantity() != null && dtgtValueBean.getPayoutQuantity() > 0 )
      {
        activityAwardCount++;
      }
      activityView.setPayoutCap( SSIContestUtil.getFormattedValue( dtgtValueBean.getPayoutCap(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      if ( contest.getPayoutType().isPoints() )
      {
        activityView.setPayout( SSIContestUtil.getFormattedValue( dtgtValueBean.getPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      }
      else if ( contest.getPayoutType().isOther() )
      {
        activityView.setPayout( SSIContestUtil.getFormattedValue( dtgtValueBean.getPayoutQuantity(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
      }
      activityView.setStackRank( dtgtValueBean.getStackRank() );
      activityView.setPayoutType( contest.getPayoutType().getCode() );
      activitiesView.add( activityView );
      award += dtgtValueBean.getPayout();
    }
    this.activities = activitiesView;

    // Final results to show on black color bar
    if ( ( contest.getStatus().isFinalizeResults() || contest.getStatus().isClosed() ) && contest.getPayoutType().isPoints() && award != 0 )
    {
      this.totalPayout = SSIContestUtil.getFormattedValue( award, SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
    }
    else if ( ( contest.getStatus().isFinalizeResults() || contest.getStatus().isClosed() ) && contest.getPayoutType().isOther() )
    {
      if ( activityAwardCount >= 1 )
      {
        // more then or equal to one activity display general message for award
        this.totalPayout = String.valueOf( Boolean.TRUE );// ANY not empty value, to show the
        this.multipleActivities = Boolean.TRUE;
      }
    }
  }

  private void populatePaxStepItUpContestData( SSIContest contest, SSIContestPaxProgressDetailValueBean paxProgress, SSIContestValueBean valueBean, String siteUrl, boolean isCreatorOrManager )
  {
    this.baselineType = contest.getIndividualBaselineType() != null ? contest.getIndividualBaselineType().getCode() : null;
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    SSIPaxContestLevelValueBean highestLevel = paxProgress.getLevels().get( paxProgress.getLevels().size() - 1 );
    String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );
    String payoutPrefix = SSIContestUtil.getPayoutPrefix( valueBean );

    // last level will be current level even though it is completed bug #61526
    if ( highestLevel.getCompleted() != null && highestLevel.getCompleted() )
    {
      paxProgress.getLevels().get( paxProgress.getLevels().size() - 1 ).setCurrentLevel( Boolean.TRUE );
    }

    if ( this.includeBonus )
    {
      this.bonusForEvery = activityPrefix + SSIContestUtil.getFormattedValue( contest.getStepItUpBonusIncrement(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
      this.bonusPayout = contest.getStepItUpBonusPayout();
      if ( highestLevel.getCompleted() != null && highestLevel.getCompleted() )
      {
        if ( highestLevel.getBonusEarned() > contest.getStepItUpBonusCap() )
        {
          this.bonusEarned = payoutPrefix + SSIContestUtil.getFormattedValue( contest.getStepItUpBonusCap(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        }
        else
        {
          this.bonusEarned = payoutPrefix + SSIContestUtil.getFormattedValue( highestLevel.getBonusEarned(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        }
        this.bonusActivity = activityPrefix + SSIContestUtil.getFormattedValue( highestLevel.getProgress() - highestLevel.getGoalAmount(), precision );
      }
    }
    if ( paxProgress.getLevels().size() > 0 )
    {
      SSIPaxContestLevelValueBean levelValueBean = paxProgress.getLevels().get( 0 );
      if ( contest.getIndividualBaselineType().isCurrencyOverBaseline() || contest.getIndividualBaselineType().isPercentageOverBaseline() )
      {
        this.baseline = activityPrefix + SSIContestUtil.getFormattedValue( levelValueBean.getBaseline(), precision );
      }
      this.stackRank = new SSIStackRankParticipantViewBean( levelValueBean.getStackRank(), levelValueBean.getParticipantsCount(), levelValueBean.getAvatarUrl() );
    }
    boolean currentLevelFound = false;
    for ( SSIPaxContestLevelValueBean vb : paxProgress.getLevels() )
    {
      if ( currentLevelFound )
      {
        vb.setCurrentLevel( false );
      }
      if ( vb.getCurrentLevel() != null && vb.getCurrentLevel() )
      {
        currentLevelFound = true;
      }
    }
    this.contestLevels = populateContestLevels( paxProgress.getLevels(), valueBean, contest, isCreatorOrManager );

    // PROGRESS NOT LOADED, bug #59968
    if ( !this.getContestLevels().get( 0 ).getCurrentLevel() && !this.getContestLevels().get( 0 ).getCompleted() )
    {
      this.getContestLevels().get( 0 ).setCurrentLevel( Boolean.TRUE );
    }

    // FINALIZED RESULTS TO SHOW
    if ( contest.getStatus().isFinalizeResults() || contest.getStatus().isClosed() )
    {
      if ( contest.getPayoutType().isPoints() )
      {
        Long payoutEarned = populatePayoutPointsEarned( paxProgress.getLevels() );
        if ( payoutEarned != null )
        {
          this.totalPayout = SSIContestUtil.getFormattedValue( payoutEarned + ( highestLevel.getBonusEarned() != null ? highestLevel.getBonusEarned() : 0L ), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        }
      }
      else if ( contest.getPayoutType().isOther() )
      {
        this.totalPayout = populatePayoutAwardDescEarned( paxProgress.getLevels() );
      }

      // populated badge earned #61607
      SSIPaxContestLevelValueBean levelAchieved = populateLevelEarned( paxProgress.getLevels() );
      if ( levelAchieved != null && !StringUtil.isNullOrEmpty( levelAchieved.getBadgeUrl() ) )
      {
        this.badge = new SSIPaxContestBadgeView( levelAchieved.getBadgeRuleId(), levelAchieved.getBadgeName(), siteUrl + levelAchieved.getBadgeUrl() );
      }

      // making every current level to false bug #61878
      for ( int i = 0; i <= this.contestLevels.size() - 1; i++ )
      {
        this.contestLevels.get( i ).setCurrentLevel( Boolean.FALSE );
      }

      // setting highest level achieved to current level bug #61878
      for ( int i = this.contestLevels.size() - 1; i >= 0; i-- )
      {
        if ( this.contestLevels.get( i ).getCompleted() )
        {
          this.contestLevels.get( i ).setCurrentLevel( Boolean.TRUE );
          break;
        }
      }
    }
  }

  private void populatePaxStackRankContestData( SSIContest contest,
                                                SSIContestPaxProgressDetailValueBean paxProgressSummary,
                                                SSIContestValueBean valueBean,
                                                Long currentUserId,
                                                List<SSIContestStackRankPaxValueBean> stackRanks,
                                                String siteUrl )
  {
    int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
    String payoutPrefix = SSIContestUtil.getPayoutPrefix( valueBean );
    String activityPrefix = SSIContestUtil.getActivityPrefix( valueBean );

    setIncludeStackRanking( Boolean.TRUE );// overriding FE needs it
    this.progress = paxProgressSummary.getActivityAmount() != null ? activityPrefix + SSIContestUtil.getFormattedValue( paxProgressSummary.getActivityAmount(), precision ) : null;
    this.minQualifier = contest.getStackRankQualifierAmount() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contest.getStackRankQualifierAmount(), precision ) : null;
    this.behindLeader = activityPrefix + SSIContestUtil.getFormattedValue( paxProgressSummary.getBehindLeader(), precision );
    this.stackRank = new SSIStackRankParticipantViewBean( paxProgressSummary.getStackRank(), paxProgressSummary.getTotalPax(), paxProgressSummary.getAvatarUrl() );
    this.payouts = transformStackRankPayoutsValueBeanToView( contest, payoutPrefix, paxProgressSummary.getPayouts(), SSIContest.CONTEST_ROLE_PAX );
    if ( paxProgressSummary.getStackRankParticipants() != null )
    {
      if ( this.stackRankParticipants == null )
      {
        this.stackRankParticipants = new ArrayList<SSIStackRankParticipantViewBean>();
      }
      for ( SSIContestStackRankTeamValueBean stackRankTeamValueBean : paxProgressSummary.getStackRankParticipants() )
      {
        SSIStackRankParticipantViewBean stackRankPaxViewBean = transformStackRankTeamValueBeanToView( contest, activityPrefix, precision, stackRankTeamValueBean, SSIContest.CONTEST_ROLE_PAX );
        SSIContestStackRankPayoutView stackRankPayout = new SSIContestStackRankPayoutView();
        stackRankPayout.setRank( Long.valueOf( stackRankTeamValueBean.getRank() ) );
        stackRankPayout.setBadge( getBadgeByRankFromPayouts( stackRankPayout.getRank(), this.payouts ) );
        String payout = stackRankTeamValueBean.getPayout() != null && stackRankTeamValueBean.getPayout().longValue() != 0 ? stackRankTeamValueBean.getPayout().toString() : null;
        if ( contest.getPayoutType().isPoints() )
        {
          stackRankPayout.setPayout( payout );
        }
        else
        {
          stackRankPayout.setPayout( stackRankTeamValueBean.getPayoutDescription() );
        }
        stackRankPaxViewBean.setPayout( stackRankPayout );

        this.stackRankParticipants.add( stackRankPaxViewBean );
      }
    }
    if ( stackRanks != null )
    {
      if ( this.leaders == null )
      {
        this.leaders = new ArrayList<SSIStackRankParticipantViewBean>();
      }
      for ( SSIContestStackRankPaxValueBean stackRankValueBean : stackRanks )
      {
        if ( currentUserId.equals( stackRankValueBean.getParticipantId() ) )
        {
          stackRankValueBean.setCurrentUser( Boolean.TRUE );
        }
        stackRankValueBean.setTeamMember( null );
        this.leaders.add( transformStackRankValueBeanToView( contest, activityPrefix, precision, stackRankValueBean, SSIContest.CONTEST_ROLE_PAX ) );
      }
    }
    // FINALIZED RESULTS TO SHOW
    if ( contest.getStatus().isFinalizeResults() || contest.getStatus().isClosed() )
    {
      if ( paxProgressSummary.getPayoutAmount() != null && !paxProgressSummary.getPayoutAmount().equals( 0L ) )
      {
        if ( contest.getPayoutType().isPoints() )
        {
          this.totalPayout = SSIContestUtil.getFormattedValue( paxProgressSummary.getPayoutAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        }
        else if ( contest.getPayoutType().isOther() )
        {
          this.totalPayout = paxProgressSummary.getPayoutDescription();
        }
      }
      int currentUserStackRank = paxProgressSummary.getStackRank() != null ? paxProgressSummary.getStackRank().intValue() : 0;
      SSIContestStackRankPayoutValueBean payoutEarned = populatePayoutEarned( paxProgressSummary.getPayouts(), currentUserStackRank );
      if ( payoutEarned != null && payoutEarned.getBadge() != null && !StringUtil.isNullOrEmpty( payoutEarned.getBadge().getImg() ) )
      {
        // populate badge earned
        this.badge = new SSIPaxContestBadgeView( payoutEarned.getBadge().getId(), payoutEarned.getBadge().getName(), siteUrl + payoutEarned.getBadge().getImg() );
      }
    }
  }

  // creator and manager full view
  private void populateCreatorManagerContestData( SSIContest contest,
                                                  List<SSIContestProgressValueBean> contestProgressData,
                                                  String contestRole,
                                                  List<SSIContestStackRankPaxValueBean> stackRanks,
                                                  SSIContestValueBean contestValueBean,
                                                  SSIContestUniqueCheckValueBean uniqueCheckValueBean,
                                                  boolean isCreatorOrManager )
  {
    if ( contestProgressData != null && contestProgressData.size() > 0 )
    {
      int decimalPrecision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
      String activityPrefix = SSIContestUtil.getActivityPrefix( contestValueBean );
      String payoutPrefix = SSIContestUtil.getPayoutPrefix( contestValueBean );
      if ( SSIContestType.OBJECTIVES.equals( contest.getContestType().getCode() ) && contestProgressData.size() > 0 )
      {
        SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
        this.goal = activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), decimalPrecision );
        this.progress = activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTeamActivity(), decimalPrecision );
        if ( contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity() <= new Double( 0 ) )
        {
          this.goalAchieved = Boolean.TRUE;
        }
        this.remaining = activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal() - contestProgressValueBean.getTeamActivity(), decimalPrecision );
        this.percentProgress = contestProgressValueBean.getPercentageAcheived() != null ? contestProgressValueBean.getPercentageAcheived().longValue() : 0;
        this.participantsCount = contestProgressValueBean.getTotalParticipant();
        this.achievedParticipantsCount = contestProgressValueBean.getParticipantAchieved();
        this.unaccountedParticipants = participantsCount > 0 ? participantsCount - achievedParticipantsCount : participantsCount;
        this.payoutCap = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getMaximumPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        this.payoutProgress = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        Long payoutLong = null;
        if ( contest.isIncludeBonus() )
        {
          this.payoutBonusCap = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getMaximumPayoutWithBonus(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
          payoutLong = contestProgressValueBean.getMaximumPayoutWithBonus();
        }
        else
        {
          payoutLong = contestProgressValueBean.getMaximumPayout();
        }
        if ( contestProgressValueBean.getPotentialPayout() != null && payoutLong != null && payoutLong.longValue() > 0 )
        {
          this.payoutPercentProgress = (long) ( (float)contestProgressValueBean.getPotentialPayout() / payoutLong * 100 );
          this.payoutRemaining = payoutPrefix + SSIContestUtil.getFormattedValue( payoutLong - contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        }
        if ( contestProgressValueBean.getStackRankParticipants() != null )
        {
          if ( this.stackRankParticipants == null )
          {
            this.stackRankParticipants = new ArrayList<SSIStackRankParticipantViewBean>();
          }
          for ( SSIContestStackRankTeamValueBean stackRankTeamValueBean : contestProgressValueBean.getStackRankParticipants() )
          {
            this.stackRankParticipants.add( transformStackRankTeamValueBeanToView( contest, activityPrefix, decimalPrecision, stackRankTeamValueBean, contestRole ) );
          }
        }
        // Activity Description
        if ( contest.getSameObjectiveDescription() != null && contest.getSameObjectiveDescription() )
        {
          this.setActivityDescription( contest.getActivityDescription() );
        }
        else if ( uniqueCheckValueBean != null )
        {
          this.setActivityDescription( uniqueCheckValueBean.isActivityDescSame()
              ? uniqueCheckValueBean.getActivityDesc()
              : CmsResourceBundle.getCmsBundle().getString( "ssi_contest.payout_objectives.DIFFERENT_OBJECTIVE" ) );
        }
        // bug fix 62660
        this.hasMultipleObjectives = !contest.getSameObjectiveDescription();

        // display for creator when contest is ended
        if ( contest.getStatus().isFinalizeResults() )
        {
          this.totalPayout = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        }
      }
      else if ( SSIContestType.DO_THIS_GET_THAT.equals( contest.getContestType().getCode() ) )
      {
        List<SSIDtgtActivityView> dtgtActivities = new ArrayList<SSIDtgtActivityView>();
        Long totalPayout = 0L;
        for ( SSIContestProgressValueBean contestProgressValueBean : contestProgressData )
        {
          SSIDtgtActivityView dtgtActivity = new SSIDtgtActivityView();
          dtgtActivity.setActivityId( contestProgressValueBean.getActivityId() );
          dtgtActivity.setActivityDescription( contestProgressValueBean.getActivityDescription() );
          dtgtActivity.setGoal( activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), decimalPrecision ) );
          dtgtActivity.setProgress( activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTeamActivity(), decimalPrecision ) );
          if ( contestProgressValueBean.getTogo() != null && ( contestProgressValueBean.getTogo() <= new Double( 0 ) || contestProgressValueBean.getTogo() <= new Double( 0.0 ) ) )
          {
            dtgtActivity.setGoalAchieved( Boolean.TRUE );
          }
          dtgtActivity.setRemaining( activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTogo(), decimalPrecision ) );
          dtgtActivity.setPercentProgress( SSIContestUtil.getFormattedValue( (long)Math.floor( contestProgressValueBean.getTeamActivity() / contestProgressValueBean.getGoal() * 100 ),
                                                                             SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
          dtgtActivity.setPayoutCap( SSIContestUtil.getPayoutPrefix( contestValueBean )
              + SSIContestUtil.getFormattedValue( contestProgressValueBean.getPayoutCapAmount() * contestProgressValueBean.getTotalParticipant(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
          dtgtActivity.setPayout( SSIContestUtil.getFormattedValue( contestProgressValueBean.getMaximumPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
          dtgtActivity.setPayoutProgress( SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
          dtgtActivity.setMinQualifier( activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getMinQualifier(), decimalPrecision ) );
          dtgtActivity.setForEvery( activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getForEvery(), decimalPrecision ) );

          if ( contest.getPayoutType().isPoints() )
          {
            dtgtActivity.setWillEarn( SSIContestUtil.getFormattedValue( contestProgressValueBean.getWillEarn(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
          }
          else if ( contest.getPayoutType().isOther() )
          {
            dtgtActivity.setPayout( SSIContestUtil.getFormattedValue( contestProgressValueBean.getPayoutQuantity(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
            dtgtActivity.setPayoutDescription( contestProgressValueBean.getPayoutDescription() );
          }
          dtgtActivity.setPayoutPercentProgress( SSIContestUtil.getFormattedValue(
                                                                                   (long)Math.floor( contestProgressValueBean.getPotentialPayout()
                                                                                       / ( contestProgressValueBean.getPayoutCapAmount() * contestProgressValueBean.getTotalParticipant() ) * 100 ),
                                                                                   SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) );
          dtgtActivity.setStackRankParticipants( transformStackRankTeamValueBeanToView( contest, activityPrefix, decimalPrecision, contestProgressValueBean.getStackRankParticipants(), contestRole ) );
          dtgtActivities.add( dtgtActivity );
          totalPayout += contestProgressValueBean.getPotentialPayout();
        }
        this.setActivities( dtgtActivities );
        // it will display only if contest ended on a results bar
        this.totalPayout = payoutPrefix + SSIContestUtil.getFormattedValue( totalPayout, SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
      }
      else if ( SSIContestType.STEP_IT_UP.equals( contest.getContestType().getCode() ) )
      {
        SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
        List<SSIPaxContestLevelValueBean> levels = new ArrayList<SSIPaxContestLevelValueBean>();
        for ( SSIPaxContestLevelValueBean valueBean : contestProgressValueBean.getContestSiuLevels() )
        {
          if ( valueBean.getIndex() > 0 )
          {
            SSIPaxContestLevelValueBean level = new SSIPaxContestLevelValueBean();
            level.setIndex( valueBean.getIndex() );
            level.setName( String.valueOf( valueBean.getIndex() ) );
            level.setParticipantsCount( valueBean.getParticipantsCount() );
            if ( SSIIndividualBaselineType.CURRENCY_OVER_BASELINE_CODE.equals( contestProgressValueBean.getBaselineType() ) )
            {
              level.setGoalPercent( SSIContestUtil.getActivityPrefix( contestValueBean ) + SSIContestUtil.getFormattedValue( valueBean.getGoalAmount(), decimalPrecision ) );
            }
            else if ( SSIIndividualBaselineType.PERCENTAGE_OVER_BASELINE_CODE.equals( contestProgressValueBean.getBaselineType() ) )
            {
              level.setGoalPercent( SSIContestUtil.getFormattedValue( valueBean.getGoalAmount(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION ) + "%" );
              level.setGoalAmount( valueBean.getGoalAmount() );
            }
            else
            {
              level.setGoalPercent( SSIContestUtil.getActivityPrefix( contestValueBean ) + SSIContestUtil.getFormattedValue( valueBean.getGoalAmount(), decimalPrecision ) );
            }
            level.setPayout( valueBean.getPayout() );
            level.setPayoutDescription( valueBean.getPayoutDescription() );
            levels.add( level );
          }
          else if ( valueBean.getIndex().equals( -1L ) )
          {
            this.bonusParticipantsCount = valueBean.getParticipantsCount();
          }
          else if ( valueBean.getIndex().equals( 0L ) )
          {
            this.unaccountedParticipants = valueBean.getParticipantsCount();
          }
        }
        this.contestLevels = populateContestLevels( levels, contestValueBean, contest, isCreatorOrManager );
        this.bonusForEvery = activityPrefix + SSIContestUtil.getFormattedValue( contest.getStepItUpBonusIncrement(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        this.bonusPayout = contest.getStepItUpBonusPayout();
        this.goal = contestProgressValueBean.getContestGoal() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getContestGoal(), decimalPrecision ) : "";
        this.progress = contestProgressValueBean.getActivity() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getActivity(), decimalPrecision ) : "";
        if ( contestProgressValueBean.getTogo() != null && ( contestProgressValueBean.getTogo() <= new Double( 0 ) || contestProgressValueBean.getTogo() <= new Double( 0.0 ) ) )
        {
          this.goalAchieved = Boolean.TRUE;
        }
        this.remaining = contestProgressValueBean.getTogo() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTogo(), decimalPrecision ) : "";
        this.baselineType = contestProgressValueBean.getBaselineType();
        this.payoutProgress = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTotalPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        this.payoutCap = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        this.payoutBonusCap = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTotalPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        this.payoutRemaining = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getRemainingPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        this.payoutPercentProgress = contestProgressValueBean.getPercPayout();
        if ( contestProgressValueBean.getStackRankParticipants() != null )
        {
          if ( this.stackRankParticipants == null )
          {
            this.stackRankParticipants = new ArrayList<SSIStackRankParticipantViewBean>();
          }
          for ( SSIContestStackRankTeamValueBean stackRankTeamValueBean : contestProgressValueBean.getStackRankParticipants() )
          {
            this.stackRankParticipants.add( transformStackRankTeamValueBeanToView( contest, activityPrefix, decimalPrecision, stackRankTeamValueBean, contestRole ) );
          }
        }
        // it will display only if contest ended on a results bar
        this.totalPayout = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTotalPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
      }
      else if ( SSIContestType.STACK_RANK.equals( contest.getContestType().getCode() ) )
      {
        SSIContestProgressValueBean contestProgressValueBean = contestProgressData.get( 0 );
        this.goal = activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getGoal(), decimalPrecision );
        this.progress = contestProgressValueBean.getProgress() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getProgress(), decimalPrecision ) : "";
        if ( contestProgressValueBean.getTogo() != null && ( contestProgressValueBean.getTogo() <= new Double( 0 ) || contestProgressValueBean.getTogo() <= new Double( 0.0 ) ) )
        {
          this.goalAchieved = Boolean.TRUE;
        }
        this.remaining = activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getTogo(), decimalPrecision );
        this.payoutCap = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getPayoutCap(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        if ( contestProgressValueBean.getMinQualifier() != null )
        {
          this.minQualifier = activityPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getMinQualifier(), decimalPrecision );
        }
        this.payouts = transformStackRankPayoutsValueBeanToView( contest, payoutPrefix, contestProgressValueBean.getPayouts(), contestRole );
        if ( stackRanks != null )
        {
          this.leaders = new ArrayList<SSIStackRankParticipantViewBean>();
          for ( SSIContestStackRankPaxValueBean stackRankValueBean : stackRanks )
          {
            this.leaders.add( transformStackRankValueBeanToView( contest, activityPrefix, decimalPrecision, stackRankValueBean, contestRole ) );
          }
        }
        if ( contestProgressValueBean.getStackRankParticipants() != null )
        {
          if ( this.stackRankParticipants == null )
          {
            this.stackRankParticipants = new ArrayList<SSIStackRankParticipantViewBean>();
          }
          for ( SSIContestStackRankTeamValueBean stackRankTeamValueBean : contestProgressValueBean.getStackRankParticipants() )
          {
            SSIStackRankParticipantViewBean stackRankPaxViewBean = transformStackRankTeamValueBeanToView( contest, activityPrefix, decimalPrecision, stackRankTeamValueBean, contestRole );
            SSIContestStackRankPayoutView stackRankPayout = new SSIContestStackRankPayoutView();
            stackRankPayout.setRank( Long.valueOf( stackRankTeamValueBean.getRank() ) );
            stackRankPayout.setBadge( getBadgeByRankFromPayouts( stackRankPayout.getRank(), this.payouts ) );
            String payout = stackRankTeamValueBean.getPayout() != null && stackRankTeamValueBean.getPayout().longValue() != 0 ? stackRankTeamValueBean.getPayout().toString() : null;
            if ( contest.getPayoutType().isPoints() )
            {
              stackRankPayout.setPayout( payout );
            }
            else
            {
              stackRankPayout.setPayout( stackRankTeamValueBean.getPayoutDescription() + ( SSIContest.CONTEST_ROLE_MGR.equals( contestRole ) ? "" : " (" + payoutPrefix + payout + ")" ) );
            }
            stackRankPaxViewBean.setPayout( stackRankPayout );
            this.stackRankParticipants.add( stackRankPaxViewBean );
          }
        }
        // it will display only if contest ended on a results bar
        this.totalPayout = payoutPrefix + SSIContestUtil.getFormattedValue( contestProgressValueBean.getPotentialPayout(), SSIContestUtil.PAYOUT_DECIMAL_PRECISION );
        if ( contestProgressValueBean.getParticipantAchieved() != null && contestProgressValueBean.getTotalParticipant() != null )
        {
          this.viewOrHideAllSr = contestProgressValueBean.getParticipantAchieved().intValue() < contestProgressValueBean.getTotalParticipant().intValue() ? true : false;
        }
      }
    }
    this.role = contestRole;
    this.creator = SSIContest.CONTEST_ROLE_CREATOR.equals( contestRole );
    this.manager = SSIContest.CONTEST_ROLE_MGR.equals( contestRole );
  }

  private SSIPaxContestBadgeView getBadgeByRankFromPayouts( Long rank, List<SSIContestStackRankPayoutView> payouts )
  {
    SSIPaxContestBadgeView badge = new SSIPaxContestBadgeView();
    for ( SSIContestStackRankPayoutView stackRankPayoutView : payouts )
    {
      if ( stackRankPayoutView.getRank().equals( rank ) )
      {
        badge = stackRankPayoutView.getBadge();
        break;
      }
    }
    return badge;
  }

  // creator and manager partial view
  private void populateCreatorManagerContestData( SSIContest contest, SSIContestProgressValueBean contestProgress, String userRole, SSIContestValueBean contestValueBean, boolean isCreatorOrManager )
  {
    if ( contestProgress != null )
    {
      int precision = SSIContestUtil.getPrecision( contest.getActivityMeasureType().getCode() );
      String activityPrefix = SSIContestUtil.getActivityPrefix( contestValueBean );
      if ( SSIContestType.OBJECTIVES.equals( contest.getContestType().getCode() ) )
      {
        this.progress = activityPrefix + SSIContestUtil.getFormattedValue( contestProgress.getActivity(), precision );
        this.participantsCount = contestProgress.getTotalParticipant();
        this.achievedParticipantsCount = contestProgress.getParticipantAchieved();
      }
      else if ( SSIContestType.STEP_IT_UP.equals( contest.getContestType().getCode() ) )
      {
        this.goal = contestProgress.getContestGoal() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contestProgress.getContestGoal(), precision ) : "";
        this.progress = contestProgress.getActivity() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contestProgress.getActivity(), precision ) : "";
      }
      else if ( SSIContestType.STACK_RANK.equals( contest.getContestType().getCode() ) )
      {
        if ( this.leaders == null )
        {
          this.leaders = new ArrayList<SSIStackRankParticipantViewBean>();
        }
        this.progress = activityPrefix + ( contestProgress.getActivity() != null ? SSIContestUtil.getFormattedValue( contestProgress.getActivity(), precision ) : "0" );
        SSIStackRankParticipantViewBean topPax = new SSIStackRankParticipantViewBean();
        topPax.setFirstName( contestProgress.getFirstName() );
        topPax.setLastName( contestProgress.getLastName() );
        topPax.setAvatarUrl( contestProgress.getAvatarUrl() );
        topPax.setParticipantId( contestProgress.getParticipantId() );
        topPax.setScore( contestProgress.getScore() != null ? activityPrefix + SSIContestUtil.getFormattedValue( contestProgress.getScore(), precision ) : null );
        topPax.setRank( contestProgress.getRank() );
        topPax.setContestUrl( SSIContestUtil.populateParticipantDetailPageUrl( contest.getId(), contestProgress.getParticipantId() ) );
        this.leaders.add( topPax );
      }
    }
    this.role = userRole;
    if ( SSIContest.CONTEST_ROLE_CREATOR.equals( userRole ) )
    {
      this.creator = Boolean.TRUE;
    }
    if ( SSIContest.CONTEST_ROLE_MGR.equals( userRole ) )
    {
      this.manager = Boolean.TRUE;
    }
  }

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getUpdatedOnDate()
  {
    return updatedOnDate;
  }

  public void setUpdatedOnDate( String updatedOnDate )
  {
    this.updatedOnDate = updatedOnDate;
  }

  @JsonProperty( "isProgressLoaded" )
  public boolean isProgressLoaded()
  {
    return progressLoaded;
  }

  public void setProgressLoaded( boolean progressLoaded )
  {
    this.progressLoaded = progressLoaded;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public Integer getDaysToEnd()
  {
    return daysToEnd;
  }

  public void setDaysToEnd( Integer daysToEnd )
  {
    this.daysToEnd = daysToEnd;
  }

  public Integer getDaysToStart()
  {
    return daysToStart;
  }

  public void setDaysToStart( Integer daysToStart )
  {
    this.daysToStart = daysToStart;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getAttachmentTitle()
  {
    return attachmentTitle;
  }

  public void setAttachmentTitle( String attachmentTitle )
  {
    this.attachmentTitle = attachmentTitle;
  }

  public String getAttachmentUrl()
  {
    return attachmentUrl;
  }

  public void setAttachmentUrl( String attachmentUrl )
  {
    this.attachmentUrl = attachmentUrl;
  }

  public String getAttachmentType()
  {
    return attachmentType;
  }

  public void setAttachmentType( String attachmentType )
  {
    this.attachmentType = attachmentType;
  }

  public String getCreatorDetailPageUrl()
  {
    return creatorDetailPageUrl;
  }

  public void setCreatorDetailPageUrl( String creatorDetailPageUrl )
  {
    this.creatorDetailPageUrl = creatorDetailPageUrl;
  }

  public String getManagerDetailPageUrl()
  {
    return managerDetailPageUrl;
  }

  public void setManagerDetailPageUrl( String managerDetailPageUrl )
  {
    this.managerDetailPageUrl = managerDetailPageUrl;
  }

  public String getParticipantDetailPageUrl()
  {
    return participantDetailPageUrl;
  }

  public void setParticipantDetailPageUrl( String participantDetailPageUrl )
  {
    this.participantDetailPageUrl = participantDetailPageUrl;
  }

  public String getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( String activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public Boolean getIncludeBonus()
  {
    return includeBonus;
  }

  public void setIncludeBonus( Boolean includeBonus )
  {
    this.includeBonus = includeBonus;
  }

  public Boolean getIncludeStackRanking()
  {
    return includeStackRanking;
  }

  public void setIncludeStackRanking( Boolean includeStackRanking )
  {
    this.includeStackRanking = includeStackRanking;
  }

  public String getBonusForEvery()
  {
    return bonusForEvery;
  }

  public void setBonusForEvery( String bonusForEvery )
  {
    this.bonusForEvery = bonusForEvery;
  }

  public SSIPaxContestBadgeView getBadge()
  {
    return badge;
  }

  public void setBadge( SSIPaxContestBadgeView badge )
  {
    this.badge = badge;
  }

  public List<SSIDtgtActivityView> getActivities()
  {
    return activities;
  }

  public void setActivities( List<SSIDtgtActivityView> activities )
  {
    this.activities = activities;
  }

  public String getBaseline()
  {
    return baseline;
  }

  public void setBaseline( String baseline )
  {
    this.baseline = baseline;
  }

  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public List<SSIContestLevelView> getContestLevels()
  {
    return contestLevels;
  }

  public void setContestLevels( List<SSIContestLevelView> contestLevels )
  {
    this.contestLevels = contestLevels;
  }

  public String getGoal()
  {
    return goal;
  }

  public void setGoal( String goal )
  {
    this.goal = goal;
  }

  public String getProgress()
  {
    if ( progress == null || progress.equals( "" ) )
    {
      return "0";
    }
    return progress;
  }

  public void setProgress( String progress )
  {
    this.progress = progress;
  }

  public String getRemaining()
  {
    return remaining;
  }

  public void setRemaining( String remaining )
  {
    this.remaining = remaining;
  }

  public SSIStackRankParticipantViewBean getStackRank()
  {
    return stackRank;
  }

  public void setStackRank( SSIStackRankParticipantViewBean stackRank )
  {
    this.stackRank = stackRank;
  }

  public Long getPercentProgress()
  {
    return percentProgress;
  }

  public void setPercentProgress( Long percentProgress )
  {
    this.percentProgress = percentProgress;
  }

  public String getShopUrl()
  {
    return shopUrl;
  }

  public void setShopUrl( String shopUrl )
  {
    this.shopUrl = shopUrl;
  }

  public Long getBonusPayout()
  {
    return bonusPayout;
  }

  public void setBonusPayout( Long bonusPayout )
  {
    this.bonusPayout = bonusPayout;
  }

  public Boolean getObjectiveAchieved()
  {
    return objectiveAchieved;
  }

  public void setObjectiveAchieved( Boolean objectiveAchieved )
  {
    this.objectiveAchieved = objectiveAchieved;
  }

  public Boolean getBonusEligible()
  {
    return bonusEligible;
  }

  public void setBonusEligible( Boolean bonusEligible )
  {
    this.bonusEligible = bonusEligible;
  }

  public Integer getParticipantsCount()
  {
    return participantsCount;
  }

  public void setParticipantsCount( Integer participantsCount )
  {
    this.participantsCount = participantsCount;
  }

  public Integer getAchievedParticipantsCount()
  {
    return achievedParticipantsCount;
  }

  public void setAchievedParticipantsCount( Integer achievedParticipantsCount )
  {
    this.achievedParticipantsCount = achievedParticipantsCount;
  }

  public Long getPayoutPercentProgress()
  {
    return payoutPercentProgress;
  }

  public void setPayoutPercentProgress( Long payoutPercentProgress )
  {
    this.payoutPercentProgress = payoutPercentProgress;
  }

  public String getPayoutRemaining()
  {
    return payoutRemaining;
  }

  public void setPayoutRemaining( String payoutRemaining )
  {
    this.payoutRemaining = payoutRemaining;
  }

  public String getRole()
  {
    return role;
  }

  public void setRole( String role )
  {
    this.role = role;
  }

  @JsonProperty( "isCreator" )
  public Boolean getCreator()
  {
    return creator;
  }

  public void setCreator( Boolean creator )
  {
    this.creator = creator;
  }

  @JsonProperty( "isManager" )
  public Boolean getManager()
  {
    return manager;
  }

  public void setManager( Boolean manager )
  {
    this.manager = manager;
  }

  public String getCreatorName()
  {
    return creatorName;
  }

  public void setCreatorName( String creatorName )
  {
    this.creatorName = creatorName;
  }

  public Integer getBonusParticipantsCount()
  {
    return bonusParticipantsCount;
  }

  public void setBonusParticipantsCount( Integer bonusParticipantsCount )
  {
    this.bonusParticipantsCount = bonusParticipantsCount;
  }

  public List<SSIStackRankParticipantViewBean> getStackRankParticipants()
  {
    return stackRankParticipants;
  }

  public void setStackRankParticipants( List<SSIStackRankParticipantViewBean> stackRankParticipants )
  {
    this.stackRankParticipants = stackRankParticipants;
  }

  public Integer getUnaccountedParticipants()
  {
    return unaccountedParticipants;
  }

  public void setUnaccountedParticipants( Integer unaccountedParticipants )
  {
    this.unaccountedParticipants = unaccountedParticipants;
  }

  public String getMinQualifier()
  {
    return minQualifier;
  }

  public void setMinQualifier( String minQualifier )
  {
    this.minQualifier = minQualifier;
  }

  public Boolean getIncludeMinimumQualifier()
  {
    return includeMinimumQualifier;
  }

  public void setIncludeMinimumQualifier( Boolean includeMinimumQualifier )
  {
    this.includeMinimumQualifier = includeMinimumQualifier;
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public void setMessages( List<WebErrorMessage> messages )
  {
    this.messages = messages;
  }

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

  public Boolean getHasApprovePayout()
  {
    return hasApprovePayout;
  }

  public void setHasApprovePayout( Boolean hasApprovePayout )
  {
    this.hasApprovePayout = hasApprovePayout;
  }

  public List<SSIContestStackRankPayoutView> getPayouts()
  {
    return payouts;
  }

  public void setPayouts( List<SSIContestStackRankPayoutView> payouts )
  {
    this.payouts = payouts;
  }

  public String getBehindLeader()
  {
    return behindLeader;
  }

  public void setBehindLeader( String behindLeader )
  {
    this.behindLeader = behindLeader;
  }

  public String getPayout()
  {
    return payout;
  }

  public void setPayout( String payout )
  {
    this.payout = payout;
  }

  public String getPayoutProgress()
  {
    return payoutProgress;
  }

  public void setPayoutProgress( String payoutProgress )
  {
    this.payoutProgress = payoutProgress;
  }

  public String getBaselineType()
  {
    return baselineType;
  }

  public void setBaselineType( String baselineType )
  {
    this.baselineType = baselineType;
  }

  public List<SSIStackRankParticipantViewBean> getLeaders()
  {
    return leaders;
  }

  public void setLeaders( List<SSIStackRankParticipantViewBean> leaders )
  {
    this.leaders = leaders;
  }

  public String getPayoutCap()
  {
    return payoutCap;
  }

  public void setPayoutCap( String payoutCap )
  {
    this.payoutCap = payoutCap;
  }

  public String getPayoutBonusCap()
  {
    return payoutBonusCap;
  }

  public void setPayoutBonusCap( String payoutBonusCap )
  {
    this.payoutBonusCap = payoutBonusCap;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

  public String getBonusActivity()
  {
    return bonusActivity;
  }

  public void setBonusActivity( String bonusActivity )
  {
    this.bonusActivity = bonusActivity;
  }

  public String getBonusEarned()
  {
    return bonusEarned;
  }

  public void setBonusEarned( String bonusEarned )
  {
    this.bonusEarned = bonusEarned;
  }

  public String getTotalPayout()
  {
    return totalPayout;
  }

  public void setTotalPayout( String totalPayout )
  {
    this.totalPayout = totalPayout;
  }

  public String getActivityMeasuredIn()
  {
    return activityMeasuredIn;
  }

  public void setActivityMeasuredIn( String activityMeasuredIn )
  {
    this.activityMeasuredIn = activityMeasuredIn;
  }

  public String getCurrencyAbbr()
  {
    return currencyAbbr;
  }

  public void setCurrencyAbbr( String currencyAbbr )
  {
    this.currencyAbbr = currencyAbbr;
  }

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  @JsonProperty( "multipleActivities" )
  public Boolean getMultipleActivities()
  {
    return multipleActivities;
  }

  public void setMultipleActivities( Boolean multipleActivities )
  {
    this.multipleActivities = multipleActivities;
  }

  public Boolean getPayoutIssued()
  {
    return payoutIssued;
  }

  public void setPayoutIssued( Boolean payoutIssued )
  {
    this.payoutIssued = payoutIssued;
  }

  public Boolean getGoalAchieved()
  {
    return goalAchieved;
  }

  public void setGoalAchieved( Boolean goalAchieved )
  {
    this.goalAchieved = goalAchieved;
  }

  public Boolean getViewOrHideAllSr()
  {
    return viewOrHideAllSr;
  }

  public void setViewOrHideAllSr( Boolean viewOrHideAllSr )
  {
    this.viewOrHideAllSr = viewOrHideAllSr;
  }

  public String getClaimUrl()
  {
    return claimUrl;
  }

  public void setClaimUrl( String claimUrl )
  {
    this.claimUrl = claimUrl;
  }

  @Override
  public int compareTo( SSIPaxContestDataView paxContestDataView )
  {
    return this.name.compareToIgnoreCase( ( (SSIPaxContestDataView)paxContestDataView ).getName() );
  }

  public Boolean getIncludeSubmitClaim()
  {
    return includeSubmitClaim;
  }

  public void setIncludeSubmitClaim( Boolean includeSubmitClaim )
  {
    this.includeSubmitClaim = includeSubmitClaim;
  }

  public Boolean getIsParticipantDrillDown()
  {
    return isParticipantDrillDown;
  }

  public void setIsParticipantDrillDown( Boolean isParticipantDrillDown )
  {
    this.isParticipantDrillDown = isParticipantDrillDown;
  }

  public String getParticipantDrillName()
  {
    return participantDrillName;
  }

  public void setParticipantDrillName( String participantDrillName )
  {
    this.participantDrillName = participantDrillName;
  }

  public Boolean getHasMultipleObjectives()
  {
    return hasMultipleObjectives;
  }

  public void setHasMultipleObjectives( Boolean hasMultipleObjectives )
  {
    this.hasMultipleObjectives = hasMultipleObjectives;
  }

  public Integer getClaimDaysToEnd()
  {
    return claimDaysToEnd;
  }

  public void setClaimDaysToEnd( Integer claimDaysToEnd )
  {
    this.claimDaysToEnd = claimDaysToEnd;
  }

  public Boolean getIsReportDrillDown()
  {
    return isReportDrillDown;
  }

  public void setIsReportDrillDown( Boolean isReportDrillDown )
  {
    this.isReportDrillDown = isReportDrillDown;
  }

  public Long getObjectiveBonusCap()
  {
    return objectiveBonusCap;
  }

  public void setObjectiveBonusCap( Long objectiveBonusCap )
  {
    this.objectiveBonusCap = objectiveBonusCap;
  }

}
