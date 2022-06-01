/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/promotion/GoalQuestPromotion.java,v $
 */

package com.biperf.core.domain.promotion;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.ManagerWebRulesAudienceType;
import com.biperf.core.domain.enums.MerchGiftCodeType;
import com.biperf.core.domain.enums.PartnerEarnings;
import com.biperf.core.domain.enums.PartnerPayoutStructure;
import com.biperf.core.domain.enums.PartnerWebRulesAudienceType;
import com.biperf.core.domain.enums.PayoutStructure;
import com.biperf.core.domain.enums.ProgressLoadType;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.goalquest.PromotionGoalQuestSurvey;
import com.biperf.core.domain.participant.ParticipantPartner;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * GoalQuestPromotion.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>meadows</td>
 * <td>Dec 5, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalQuestPromotion extends Promotion
{
  private static final long serialVersionUID = 1L;

  private Date goalCollectionStartDate;
  private Date goalCollectionEndDate;
  private AchievementRuleType achievementRule;
  private PayoutStructure payoutStructure;
  private RoundingMethod roundingMethod;
  private AchievementPrecision achievementPrecision;
  private ManagerOverrideStructure overrideStructure;
  private BigDecimal levelOneMgrAward;
  private BigDecimal levelTwoMgrAward;
  private Set<AbstractGoalLevel> goalLevels = new LinkedHashSet<AbstractGoalLevel>();
  private Set<ManagerOverrideGoalLevel> managerOverrideGoalLevels = new LinkedHashSet<ManagerOverrideGoalLevel>();
  private Set<PromotionPartnerPayout> partnerGoalLevels = new LinkedHashSet<PromotionPartnerPayout>();
  private Date finalProcessDate;
  private String programId;
  private String goalPlanningWorksheet;
  private ProgressLoadType progressLoadType;
  private boolean issueAwardsRun;
  private boolean autoCompletePartners = false;
  private Date issueAwardsRunDate;
  private String objective;
  private String objectiveAssetCode;
  // BugFix 17935
  private String baseUnit;
  private BaseUnitPosition baseUnitPosition;
  // Partner Payout
  private PartnerPayoutStructure partnerPayoutStructure;
  private PartnerEarnings partnerEarnings;
  private Set<ParticipantPartner> promotionParticipantPartners = new LinkedHashSet<ParticipantPartner>();
  private Set<PromotionGoalQuestSurvey> promotionGoalQuestSurveys = new LinkedHashSet<PromotionGoalQuestSurvey>();

  private MerchGiftCodeType merchGiftCodeType = null;
  // convert to PerQs
  private boolean apqConversion;

  private String managerCmAssetCode;
  private String managerWebRulesCmKey;
  private String partnerCmAssetCode;
  private String partnerWebRulesCmKey;
  private ManagerWebRulesAudienceType managerWebRulesAudienceType;
  private PartnerWebRulesAudienceType partnerWebRulesAudienceType;

  private Set promotionManagerWebRulesAudience = new LinkedHashSet();
  private Set promotionPartnerWebRulesAudience = new LinkedHashSet();

  private String preSelectedPartnerChars;

  private Integer partnerCount;

  // UnderArmour
  private boolean allowUnderArmour = false;

  /**
  * @param notificationTypeCode
  * @return boolean true if notification required else false
  */
  public boolean isNotificationRequired( String notificationTypeCode )
  {
    if ( this.getPromotionNotifications() != null )
    {
      for ( Iterator<PromotionNotification> notificationsIter = this.getPromotionNotifications().iterator(); notificationsIter.hasNext(); )
      {
        PromotionNotificationType promotionNotificationType = (PromotionNotificationType)notificationsIter.next();
        // if notification is set up for a particular Email Notification Type then return true
        if ( promotionNotificationType.getPromotionEmailNotificationType().getCode().equals( notificationTypeCode ) )
        {
          if ( promotionNotificationType.getNotificationMessageId() != -1 )
          {
            return true;
          }
        }
      }
    }
    return false;
  }

  /* Round the given value according to rounding rules in the promotion */
  public BigDecimal roundValue( BigDecimal value )
  {
    if ( value != null && getAchievementPrecision() != null && getRoundingMethod() != null )
    {
      return value.setScale( getAchievementPrecision().getPrecision(), getRoundingMethod().getBigDecimalRoundingMode() );
    }
    return value;
  }

  /**
   * Given a goalLevelName, return a GoalLevel or ManagerOverrideGoalLevel object
   * 
   * @param goalLevelName
   * @return AbstractGoalLevel
   */
  public AbstractGoalLevel getGoalLevelByName( String goalLevelName )
  {
    if ( goalLevels != null && !goalLevels.isEmpty() )
    {
      Iterator<AbstractGoalLevel> iter = goalLevels.iterator();
      while ( iter.hasNext() )
      {
        GoalLevel goalLevel = (GoalLevel)iter.next();
        if ( StringUtils.isNotBlank( goalLevel.getGoalLevelName() ) && goalLevel.getGoalLevelName().equalsIgnoreCase( goalLevelName ) )
        {
          return goalLevel;
        }
      }
    }
    if ( managerOverrideGoalLevels != null && !managerOverrideGoalLevels.isEmpty() )
    {
      Iterator<ManagerOverrideGoalLevel> iter2 = managerOverrideGoalLevels.iterator();
      while ( iter2.hasNext() )
      {
        ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)iter2.next();
        if ( StringUtils.isNotBlank( managerOverrideGoalLevel.getGoalLevelName() ) && managerOverrideGoalLevel.getGoalLevelName().equalsIgnoreCase( goalLevelName ) )
        {
          return managerOverrideGoalLevel;
        }
      }
    }
    if ( partnerGoalLevels != null && !partnerGoalLevels.isEmpty() )
    {
      Iterator<PromotionPartnerPayout> iter3 = partnerGoalLevels.iterator();
      while ( iter3.hasNext() )
      {
        PromotionPartnerPayout promotionPartnerPayout = (PromotionPartnerPayout)iter3.next();
        if ( StringUtils.isNotBlank( promotionPartnerPayout.getGoalLevelName() ) && promotionPartnerPayout.getGoalLevelName().equalsIgnoreCase( goalLevelName ) )
        {
          return promotionPartnerPayout;
        }
      }
    }
    return null;
  }

  /**
   * Returns false because goalguest type promotions cannot have child
   * promotions.
   *
   * @return false.
   */
  public boolean hasParent()
  {
    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.promotion.Promotion#isClaimFormUsed()
   */
  public boolean isClaimFormUsed()
  {
    return true;
  }

  /**
   * Get if in valid date range for final processing
   * 
   * @return true if past final process date; return false if in any other status
   */
  public boolean isAfterFinalProcessingValid()
  {
    if ( finalProcessDate != null )
    {
      return !finalProcessDate.after( DateUtils.getCurrentDate() );
    }
    return false;
  }

  /**
   * Does a deep copy of the promotion and its children if specified. This is a customized
   * implementation of
   * 
   * @see java.lang.Object#clone()
   * @param cloneWithChildren
   * @param newPromotionName
   * @param newChildPromotionNameHolders
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object deepCopy( boolean cloneWithChildren, String newPromotionName, List newChildPromotionNameHolders ) throws CloneNotSupportedException
  {
    GoalQuestPromotion clonedPromotion = (GoalQuestPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );
    clonedPromotion.setId( null );

    // copy the promotionParticipant partners
    clonedPromotion.setPromotionParticipantPartners( new LinkedHashSet<ParticipantPartner>() );

    // copy the goalLevels
    clonedPromotion.setGoalLevels( new LinkedHashSet<AbstractGoalLevel>() );
    for ( Iterator<AbstractGoalLevel> iter = this.getGoalLevels().iterator(); iter.hasNext(); )
    {
      GoalLevel goalLevelToCopy = (GoalLevel)iter.next();
      clonedPromotion.addGoalLevel( (GoalLevel)goalLevelToCopy.clone() );
    }

    // copy the manager override goalLevels
    clonedPromotion.setManagerOverrideGoalLevels( new LinkedHashSet<ManagerOverrideGoalLevel>() );
    for ( Iterator<ManagerOverrideGoalLevel> iter = this.getManagerOverrideGoalLevels().iterator(); iter.hasNext(); )
    {
      ManagerOverrideGoalLevel goalLevelToCopy = (ManagerOverrideGoalLevel)iter.next();
      clonedPromotion.addManagerOverrideGoalLevel( (ManagerOverrideGoalLevel)goalLevelToCopy.clone() );
    }
    // copy the partner goalLevels
    clonedPromotion.setPartnerGoalLevels( new LinkedHashSet<PromotionPartnerPayout>() );
    for ( Iterator<PromotionPartnerPayout> iter = this.getPartnerGoalLevels().iterator(); iter.hasNext(); )
    {
      PromotionPartnerPayout goalLevelToCopy = (PromotionPartnerPayout)iter.next();
      clonedPromotion.addPartnerGoalLevel( (PromotionPartnerPayout)goalLevelToCopy.clone() );
    }

    // copy the Promotion Goal Quest surveys.
    clonedPromotion.setPromotionGoalQuestSurveys( new LinkedHashSet<PromotionGoalQuestSurvey>() );
    for ( Iterator<PromotionGoalQuestSurvey> iter = this.getPromotionGoalQuestSurveys().iterator(); iter.hasNext(); )
    {
      PromotionGoalQuestSurvey goalSurveyToCopy = (PromotionGoalQuestSurvey)iter.next();
      clonedPromotion.addPromotionGoalQuestSurvey( (PromotionGoalQuestSurvey)goalSurveyToCopy.clone() );
    }

    // Clear enroll program code and audience since this must be unique
    clonedPromotion.setEnrollProgramCode( null );
    clonedPromotion.setSecondaryAudienceType( null );
    // when copying promotion set issue awards flag to false
    clonedPromotion.setIssueAwardsRun( false );

    // copy the promotionManagerWebRulesAudiences
    clonedPromotion.setPromotionManagerWebRulesAudience( new LinkedHashSet() );
    for ( Iterator promotionManagerWebRulesAudienceIter = this.promotionManagerWebRulesAudience.iterator(); promotionManagerWebRulesAudienceIter.hasNext(); )
    {
      PromotionManagerWebRulesAudience managerWebRulesAudience = (PromotionManagerWebRulesAudience)promotionManagerWebRulesAudienceIter.next();
      clonedPromotion.addPromotionManagerWebRulesAudience( (PromotionManagerWebRulesAudience)managerWebRulesAudience.clone() );
    }

    // copy the promotionPartnerWebRulesAudiences
    clonedPromotion.setPromotionPartnerWebRulesAudience( new LinkedHashSet() );
    for ( Iterator promotionPartnerWebRulesAudienceIter = this.promotionPartnerWebRulesAudience.iterator(); promotionPartnerWebRulesAudienceIter.hasNext(); )
    {
      PromotionPartnerWebRulesAudience promotionPartnerWebRulesAudience = (PromotionPartnerWebRulesAudience)promotionPartnerWebRulesAudienceIter.next();
      clonedPromotion.addPromotionPartnerWebRulesAudience( (PromotionPartnerWebRulesAudience)promotionPartnerWebRulesAudience.clone() );
    }

    clonedPromotion.setPreSelectedPartnerChars( this.preSelectedPartnerChars );

    return clonedPromotion;

  }

  // ===================================
  // GETTERS & SETTERS
  // ===================================

  public MerchGiftCodeType getMerchGiftCodeType()
  {
    return merchGiftCodeType;
  }

  public void setMerchGiftCodeType( MerchGiftCodeType merchGiftCodeType )
  {
    this.merchGiftCodeType = merchGiftCodeType;
  }

  public Date getGoalCollectionStartDate()
  {
    return goalCollectionStartDate;
  }

  public void setGoalCollectionStartDate( Date goalCollectionStartDate )
  {
    this.goalCollectionStartDate = goalCollectionStartDate;
  }

  public Date getGoalCollectionEndDate()
  {
    return goalCollectionEndDate;
  }

  public void setGoalCollectionEndDate( Date goalCollectionEndDate )
  {
    this.goalCollectionEndDate = goalCollectionEndDate;
  }

  public void setAchievementRule( AchievementRuleType achievementRule )
  {
    this.achievementRule = achievementRule;
  }

  public AchievementRuleType getAchievementRule()
  {
    return achievementRule;
  }

  public PayoutStructure getPayoutStructure()
  {
    return payoutStructure;
  }

  public void setPayoutStructure( PayoutStructure payoutStructure )
  {
    this.payoutStructure = payoutStructure;
  }

  public RoundingMethod getRoundingMethod()
  {
    return roundingMethod;
  }

  public void setRoundingMethod( RoundingMethod roundingMethod )
  {
    this.roundingMethod = roundingMethod;
  }

  public AchievementPrecision getAchievementPrecision()
  {
    return achievementPrecision;
  }

  public void setAchievementPrecision( AchievementPrecision achievementPrecision )
  {
    this.achievementPrecision = achievementPrecision;
  }

  public ManagerOverrideStructure getOverrideStructure()
  {
    return overrideStructure;
  }

  public void setOverrideStructure( ManagerOverrideStructure overrideStructure )
  {
    this.overrideStructure = overrideStructure;
  }

  public Set<AbstractGoalLevel> getGoalLevels()
  {
    return goalLevels;
  }

  public void setGoalLevels( Set<AbstractGoalLevel> goalLevels )
  {
    this.goalLevels = goalLevels;
  }

  public void addGoalLevel( AbstractGoalLevel goalLevel )
  {
    goalLevel.setPromotion( this );
    this.goalLevels.add( goalLevel );
  }

  public Set<ManagerOverrideGoalLevel> getManagerOverrideGoalLevels()
  {
    return managerOverrideGoalLevels;
  }

  public void setManagerOverrideGoalLevels( Set<ManagerOverrideGoalLevel> managerOverrideGoalLevels )
  {
    this.managerOverrideGoalLevels = managerOverrideGoalLevels;
  }

  public void addManagerOverrideGoalLevel( ManagerOverrideGoalLevel managerOverrideGoalLevel )
  {
    managerOverrideGoalLevel.setPromotion( this );
    this.managerOverrideGoalLevels.add( managerOverrideGoalLevel );
  }

  public Set<PromotionPartnerPayout> getPartnerGoalLevels()
  {
    return partnerGoalLevels;
  }

  public void setPartnerGoalLevels( Set<PromotionPartnerPayout> partnerGoalLevels )
  {
    this.partnerGoalLevels = partnerGoalLevels;
  }

  public void addPartnerGoalLevel( PromotionPartnerPayout promotionPartnerPayout )
  {
    promotionPartnerPayout.setPromotion( this );
    this.partnerGoalLevels.add( promotionPartnerPayout );
  }

  public Date getFinalProcessDate()
  {
    return finalProcessDate;
  }

  public void setFinalProcessDate( Date finalProcessDate )
  {
    this.finalProcessDate = finalProcessDate;
  }

  public String getProgramId()
  {
    return programId;
  }

  public void setProgramId( String programId )
  {
    this.programId = programId;
  }

  public String getGoalPlanningWorksheet()
  {
    return goalPlanningWorksheet;
  }

  public void setGoalPlanningWorksheet( String goalPlanningWorksheet )
  {
    this.goalPlanningWorksheet = goalPlanningWorksheet;
  }

  public ProgressLoadType getProgressLoadType()
  {
    return progressLoadType;
  }

  public void setProgressLoadType( ProgressLoadType progressLoadType )
  {
    this.progressLoadType = progressLoadType;
  }

  public boolean isIssueAwardsRun()
  {
    return issueAwardsRun;
  }

  public void setIssueAwardsRun( boolean issueAwardsRun )
  {
    this.issueAwardsRun = issueAwardsRun;
  }

  public Date getIssueAwardsRunDate()
  {
    return issueAwardsRunDate;
  }

  public void setIssueAwardsRunDate( Date issueAwardsRunDate )
  {
    this.issueAwardsRunDate = issueAwardsRunDate;
  }

  public String getBaseUnit()
  {
    return baseUnit;
  }

  public String getBaseUnitText()
  {
    String baseUnit = null;
    if ( this.baseUnit != null )
    {
      baseUnit = CmsResourceBundle.getCmsBundle().getString( this.baseUnit, Promotion.GQ_CP_PROMO_BASE_UNIT_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( baseUnit );
  }

  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  public BaseUnitPosition getBaseUnitPosition()
  {
    return baseUnitPosition;
  }

  public void setBaseUnitPosition( BaseUnitPosition baseUnitPosition )
  {
    this.baseUnitPosition = baseUnitPosition;
  }

  public boolean isAutoCompletePartners()
  {
    return autoCompletePartners;
  }

  public void setAutoCompletePartners( boolean autoCompletePartners )
  {
    this.autoCompletePartners = autoCompletePartners;
  }

  public PartnerPayoutStructure getPartnerPayoutStructure()
  {
    return partnerPayoutStructure;
  }

  public void setPartnerPayoutStructure( PartnerPayoutStructure partnerPayoutStructure )
  {
    this.partnerPayoutStructure = partnerPayoutStructure;
  }

  public PartnerEarnings getPartnerEarnings()
  {
    return partnerEarnings;
  }

  public void setPartnerEarnings( PartnerEarnings partnerEarnings )
  {
    this.partnerEarnings = partnerEarnings;
  }

  public Set<ParticipantPartner> getPromotionParticipantPartners()
  {
    return promotionParticipantPartners;
  }

  public void setPromotionParticipantPartners( Set<ParticipantPartner> promotionParticipantPartners )
  {
    this.promotionParticipantPartners = promotionParticipantPartners;
  }

  public void addPromotionParticipantPartners( ParticipantPartner participantPartner )
  {
    participantPartner.setPromotion( this );
    this.promotionParticipantPartners.add( participantPartner );
  }

  public boolean isApqConversion()
  {
    return apqConversion;
  }

  public void setApqConversion( boolean apqConversion )
  {
    this.apqConversion = apqConversion;
  }

  public String getObjectiveFromCM()
  {
    String objective = null;
    if ( this.objectiveAssetCode != null )
    {
      objective = CmsResourceBundle.getCmsBundle().getString( this.objectiveAssetCode, Promotion.GQ_CP_PROMO_OBJECTIVE_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( objective );
  }

  public String getManagerWebRulesFromCM()
  {
    String rulesText = "";

    if ( this.managerCmAssetCode != null && this.managerWebRulesCmKey != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.managerCmAssetCode, this.managerWebRulesCmKey );
    }

    return rulesText;
  }

  public String getPartnerWebRulesFromCM()
  {
    String rulesText = "";

    if ( this.partnerCmAssetCode != null && this.partnerWebRulesCmKey != null )
    {
      rulesText = CmsResourceBundle.getCmsBundle().getString( this.partnerCmAssetCode, this.partnerWebRulesCmKey );
    }

    return rulesText;
  }

  public String getObjective()
  {
    return getObjectiveFromCM();
  }

  public void setObjective( String objective )
  {
    this.objective = objective;
  }

  public String getObjectiveAssetCode()
  {
    return objectiveAssetCode;
  }

  public void setObjectiveAssetCode( String objectiveAssetCode )
  {
    this.objectiveAssetCode = objectiveAssetCode;
  }

  public String getManagerCmAssetCode()
  {
    return managerCmAssetCode;
  }

  public void setManagerCmAssetCode( String managerCmAssetCode )
  {
    this.managerCmAssetCode = managerCmAssetCode;
  }

  public String getManagerWebRulesCmKey()
  {
    return managerWebRulesCmKey;
  }

  public void setManagerWebRulesCmKey( String managerWebRulesCmKey )
  {
    this.managerWebRulesCmKey = managerWebRulesCmKey;
  }

  public String getPartnerCmAssetCode()
  {
    return partnerCmAssetCode;
  }

  public void setPartnerCmAssetCode( String partnerCmAssetCode )
  {
    this.partnerCmAssetCode = partnerCmAssetCode;
  }

  public String getPartnerWebRulesCmKey()
  {
    return partnerWebRulesCmKey;
  }

  public void setPartnerWebRulesCmKey( String partnerWebRulesCmKey )
  {
    this.partnerWebRulesCmKey = partnerWebRulesCmKey;
  }

  public ManagerWebRulesAudienceType getManagerWebRulesAudienceType()
  {
    return managerWebRulesAudienceType;
  }

  public void setManagerWebRulesAudienceType( ManagerWebRulesAudienceType managerWebRulesAudienceType )
  {
    this.managerWebRulesAudienceType = managerWebRulesAudienceType;
  }

  public PartnerWebRulesAudienceType getPartnerWebRulesAudienceType()
  {
    return partnerWebRulesAudienceType;
  }

  public void setPartnerWebRulesAudienceType( PartnerWebRulesAudienceType partnerWebRulesAudienceType )
  {
    this.partnerWebRulesAudienceType = partnerWebRulesAudienceType;
  }

  public Set getPromotionManagerWebRulesAudience()
  {
    return promotionManagerWebRulesAudience;
  }

  public void setPromotionManagerWebRulesAudience( Set promotionManagerWebRulesAudience )
  {
    this.promotionManagerWebRulesAudience = promotionManagerWebRulesAudience;
  }

  public Set getPromotionPartnerWebRulesAudience()
  {
    return promotionPartnerWebRulesAudience;
  }

  public void setPromotionPartnerWebRulesAudience( Set promotionPartnerWebRulesAudience )
  {
    this.promotionPartnerWebRulesAudience = promotionPartnerWebRulesAudience;
  }

  public void addPromotionManagerWebRulesAudience( PromotionManagerWebRulesAudience promotionManagerWebRulesAudience )
  {
    promotionManagerWebRulesAudience.setPromotion( this );
    this.promotionManagerWebRulesAudience.add( promotionManagerWebRulesAudience );
  }

  public void addPromotionPartnerWebRulesAudience( PromotionPartnerWebRulesAudience promotionPartnerWebRulesAudience )
  {
    promotionPartnerWebRulesAudience.setPromotion( this );
    this.promotionPartnerWebRulesAudience.add( promotionPartnerWebRulesAudience );
  }

  public String getPreSelectedPartnerChars()
  {
    return preSelectedPartnerChars;
  }

  public void setPreSelectedPartnerChars( String preSelectedPartnerChars )
  {
    this.preSelectedPartnerChars = preSelectedPartnerChars;
  }

  public Integer getPartnerCount()
  {
    return partnerCount;
  }

  public void setPartnerCount( Integer partnerCount )
  {
    this.partnerCount = partnerCount;
  }

  public BigDecimal getLevelOneMgrAward()
  {
    return levelOneMgrAward;
  }

  public void setLevelOneMgrAward( BigDecimal levelOneMgrAward )
  {
    this.levelOneMgrAward = levelOneMgrAward;
  }

  public BigDecimal getLevelTwoMgrAward()
  {
    return levelTwoMgrAward;
  }

  public void setLevelTwoMgrAward( BigDecimal levelTwoMgrAward )
  {
    this.levelTwoMgrAward = levelTwoMgrAward;
  }

  public Set<PromotionGoalQuestSurvey> getPromotionGoalQuestSurveys()
  {
    return promotionGoalQuestSurveys;
  }

  public void setPromotionGoalQuestSurveys( Set<PromotionGoalQuestSurvey> promotionGoalQuestSurveys )
  {
    this.promotionGoalQuestSurveys = promotionGoalQuestSurveys;
  }

  public void addPromotionGoalQuestSurvey( PromotionGoalQuestSurvey promotionGoalQuestSurvey )
  {
    promotionGoalQuestSurvey.setPromotion( this );
    this.promotionGoalQuestSurveys.add( promotionGoalQuestSurvey );
  }

  public boolean isAllowUnderArmour()
  {
    return allowUnderArmour;
  }

  public void setAllowUnderArmour( boolean allowUnderArmour )
  {
    this.allowUnderArmour = allowUnderArmour;
  }

}
