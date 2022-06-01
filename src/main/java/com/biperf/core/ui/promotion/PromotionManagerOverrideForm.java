/*
 * (c) 2005 BI, Inc. All rights reserved. $Source:
 * /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionPayoutForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.CPOverrideStructure;
import com.biperf.core.domain.enums.ManagerOverrideStructure;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.SAO;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * Promotion Manager Override ActionForm transfer object <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>meadows</td>
 * <td>December 15, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionManagerOverrideForm extends BaseForm
{

  private String overrideStructure = ManagerOverrideStructure.NONE;
  private Long promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionStatus;
  private String alternateReturnUrl;
  private Long version;
  private String prevOverrideStructure;
  //
  private List overrideStructureList;
  private String promotionTypeCode;

  private String awardType;
  private List<PromotionManagerOverrideLevelFormBean> managerOverrideValueList;
  private String oldSequence;
  private String newElementSequenceNum;
  private String method = "";
  private boolean beforeGoalSelection;
  private boolean inGoalSelection;
  private boolean afterGoalSelection;
  private boolean percentBaselineTeamProduction;
  private Integer totalTeamProductionQty;
  private Integer totalTeamProductionPct;
  private Integer totalTeamProduction;
  private Integer managerAward;

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // PromotionPayoutFormBeans. If this is not done, the form wont initialize
    // properly.

    int managerOverrideCount = RequestUtils.getOptionalParamInt( request, "managerOverrideValueListSize" );
    String overrideStructureValue = "";
    overrideStructureValue = RequestUtils.getOptionalParamString( request, "oldOverrideStructure" );
    managerOverrideValueList = getEmptyValueList( managerOverrideCount );
    if ( managerOverrideCount > 0 )
    {
      if ( StringUtils.isEmpty( overrideStructureValue ) )
      {
        overrideStructureValue = RequestUtils.getOptionalParamString( request, "overrideStructure" );
      }

    }
  }

  /**
   * Validate managerOverrideLevelFormBean fields
   * 
   * @param goalLevelFormBean
   * @return
   */
  private boolean validate( PromotionManagerOverrideLevelFormBean managerOverrideLevelFormBean, ActionErrors actionErrors )
  {
    boolean valid = true;
    if ( promotionTypeCode.equals( PromotionType.GOALQUEST ) && ( isManagerActualTeamAchievement() || isManagerTeamAchievementPercentage() ) )
    {
      if ( StringUtils.isBlank( managerOverrideLevelFormBean.getName() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.manageroverride", "NAME" ) ) );
        valid = false;
      }
      if ( StringUtils.isBlank( managerOverrideLevelFormBean.getDescription() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.manageroverride", "DESCRIPTION" ) ) );
        valid = false;
      }
      if ( !validatePositiveInteger( managerOverrideLevelFormBean.getTeamAchievementPercent() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INTEGER, ContentReaderManager.getText( "promotion.manageroverride", "TEAM_ACHIEVEMENT_PERCENT" ) ) );
        valid = false;
      }
    }
    if ( isManagerActualTeamAchievement() || isManagerTeamAchievementPercentage() || isAmountPerAchiever() || isPercentTeamEarnings() )
    {
      String cmText = ContentReaderManager.getText( "promotion.manageroverride", "AWARD" );
      if ( isPercentTeamEarnings() )
      {
        cmText = ContentReaderManager.getText( "promotion.manageroverride", "PERCENT" ) + "  " + ContentReaderManager.getText( "promotion.manageroverride", "ACHIEVEMENT_AMOUNT" );
      }
      else if ( isAmountPerAchiever() )
      {
        cmText = ContentReaderManager.getText( "promotion.manageroverride", "AWARD_PER_ACHIEVER" );
      }

      if ( managerOverrideLevelFormBean.getSequenceNumber() == 1 && !validatePositiveNumeric( managerOverrideLevelFormBean.getAward() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INTEGER, cmText ) );
        valid = false;
      }
      else if ( managerOverrideLevelFormBean.getSequenceNumber() == 2 && !managerOverrideLevelFormBean.getAward().equals( "" ) && managerOverrideLevelFormBean.getAward() != null
          && !validatePositiveNumeric( managerOverrideLevelFormBean.getAward() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INTEGER, cmText ) );
        valid = false;
      }
      else if ( !validatePositiveNumericMoreThanZero( managerOverrideLevelFormBean.getAward() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_NON_ZERO_POSITIVE_INTEGER, cmText ) );
        valid = false;
      }

    }

    return valid;
  }

  private boolean validateStackRank( PromotionManagerOverrideLevelFormBean managerOverrideLevelFormBean, ActionErrors actionErrors )
  {
    boolean valid = true;
    if ( !managerOverrideLevelFormBean.getMoStartRank().isEmpty() || !managerOverrideLevelFormBean.getMoEndRank().isEmpty() || !managerOverrideLevelFormBean.getMoAwards().isEmpty() )
    {
      if ( managerOverrideLevelFormBean.getMoStartRank() == null || managerOverrideLevelFormBean.getMoStartRank().isEmpty() )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.manageroverride", "START_RANK" ) ) );
        valid = false;
      }
      if ( managerOverrideLevelFormBean.getMoEndRank() == null || managerOverrideLevelFormBean.getMoEndRank().isEmpty() )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.manageroverride", "END_RANK" ) ) );
        valid = false;
      }
      if ( managerOverrideLevelFormBean.getMoAwards() == null || managerOverrideLevelFormBean.getMoAwards().isEmpty() )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.manageroverride", "STACK_AWARD" ) ) );
        valid = false;
      }
    }

    return valid;
  }

  /**
   * Checks if any of the managerOverrideLevelFormBean fields are not blank.
   * 
   * @param managerOverrideLevelFormBean
   * @return
   */
  private boolean isNotBlank( PromotionManagerOverrideLevelFormBean managerOverrideLevelFormBean )
  {
    if ( ! ( isAmountPerAchiever() || isPercentTeamEarnings() ) && StringUtils.isNotBlank( managerOverrideLevelFormBean.getName() ) )
    {
      return true;
    }
    if ( ! ( isAmountPerAchiever() || isPercentTeamEarnings() ) && StringUtils.isNotBlank( managerOverrideLevelFormBean.getDescription() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( managerOverrideLevelFormBean.getAward() ) )
    {
      return true;
    }
    if ( ! ( isAmountPerAchiever() || isPercentTeamEarnings() ) && StringUtils.isNotBlank( managerOverrideLevelFormBean.getTeamAchievementPercent() ) )
    {
      return true;
    }
    return false;
  }

  /**
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( isPercentTeamEarnings() || isAmountPerAchiever() || isManagerTeamAchievementPercentage() || isManagerActualTeamAchievement() )
    {
      int countValid = 0;
      boolean isMgrSecLevelOptional = true;
      for ( Iterator overrideIter = getManagerOverrideValueAsList().iterator(); overrideIter.hasNext() && isMgrSecLevelOptional; )
      {
        PromotionManagerOverrideLevelFormBean overrideLevelFormBean = (PromotionManagerOverrideLevelFormBean)overrideIter.next();
        if ( isAmountPerAchiever() || isPercentTeamEarnings() || isNotBlank( overrideLevelFormBean ) )
        {
          isMgrSecLevelOptional = false;
          if ( validate( overrideLevelFormBean, actionErrors ) )
          {
            countValid += 1;
          }
        }
      }
      if ( isManagerTeamAchievementPercentage() )
      {
        actionErrors = validateStackRankPayoutRanks( actionMapping, request, actionErrors );
        actionErrors = validateStackRankPayouts( actionMapping, request, actionErrors );
      }

      if ( !isAmountPerAchiever() && countValid == 0 )
      {
        if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) && !isManagerActualTeamAchievement() )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.manageroverride.errors.NO_GOAL_LEVELS" ) );
        }
      }
    }
    if ( promotionTypeCode.equals( PromotionType.CHALLENGE_POINT ) && isManagerActualTeamAchievement() && !isManagerTeamAchievementPercentage() )
    {
      if ( percentBaselineTeamProduction )
      {
        if ( totalTeamProductionPct == null || totalTeamProductionPct.intValue() <= 0 )
        {
          actionErrors.add( "totalteamPercent", new ActionMessage( "promotion.manageroverride.errors.NO_TOTAL_TEAM_PRODUCTION" ) );
        }
      }
      else
      {
        if ( totalTeamProductionQty == null || totalTeamProductionQty.intValue() <= 0 )
        {
          actionErrors.add( "totalteamPercent", new ActionMessage( "promotion.manageroverride.errors.NO_TOTAL_TEAM_PRODUCTION" ) );
        }
      }
      if ( managerAward == null || managerAward.intValue() <= 0 )
      {
        actionErrors.add( "managerAward", new ActionMessage( "promotion.manageroverride.errors.NO_MANAGER_AWARD" ) );
      }
    }

    return actionErrors;
  }

  /**
   * validates String is not null, blank or zero value
   * 
   * @param string String that is going to be validated
   * @return boolean
   */
  private boolean validateNotBlankOrZero( String string )
  {
    if ( StringUtils.isBlank( string ) )
    {
      return false;
    }
    BigDecimal numericValue = getNumericValue( string );
    if ( numericValue.doubleValue() == 0 )
    {
      return false;
    }
    return true;
  }

  /**
   * Return BigDecimal representing string. If string is not a valid number then return null.
   * 
   * @param string
   * @return
   */
  private BigDecimal getNumericValue( String string )
  {
    BigDecimal numericValue = null;
    try
    {
      if ( StringUtils.isNotEmpty( string ) )
      {
        numericValue = NumberUtils.createBigDecimal( string );
      }
    }
    catch( NumberFormatException nfe )
    {
    }
    return numericValue;
  }

  /**
   * validates String is positive numeric, not null, not blank
   * 
   * @param string String that is going to be validated
   * @return boolean
   */
  private boolean validatePositiveNumeric( String string )
  {
    BigDecimal numericValue = getNumericValue( string );
    return numericValue != null && numericValue.doubleValue() >= 0;
  }

  /**
   * validates String is positive numeric, not null, not blank and > 0
   * 
   * @param string String that is going to be valdidated
   * @return boolean
   */
  private boolean validatePositiveNumericMoreThanZero( String string )
  {
    BigDecimal numericValue = getNumericValue( string );
    return numericValue != null && numericValue.doubleValue() > 0;
  }

  /**
   * Return Integer representing string. If string is not a valid number then return null.
   * 
   * @param string
   * @return
   */
  private Integer getIntegerValue( String string )
  {
    Integer integerValue = null;
    try
    {
      if ( StringUtils.isNotEmpty( string ) )
      {
        integerValue = NumberUtils.createInteger( string );
      }
    }
    catch( NumberFormatException nfe )
    {
    }
    return integerValue;
  }

  /**
   * Return Long representing string. If string is not a valid number then return null.
   * 
   * @param string
   * @return
   */
  private Long getLongValue( String string )
  {
    Long longValue = null;
    try
    {
      if ( StringUtils.isNotEmpty( string ) )
      {
        longValue = NumberUtils.createLong( string );
      }
    }
    catch( NumberFormatException nfe )
    {
    }
    return longValue;
  }

  /**
   * validates String is positive integer, not null, not blank
   * 
   * @param string String that is going to be validated
   * @return boolean
   */
  private boolean validatePositiveInteger( String string )
  {
    Integer numericValue = getIntegerValue( string );
    return numericValue != null && numericValue.intValue() >= 0;
  }

  /**
   * validates String is positive integer, not null, not blank and > 0
   * 
   * @param string String that is going to be valdidated
   * @return boolean
   */
  private boolean validatePositiveIntegerMoreThanZero( String string )
  {
    Integer numericValue = getIntegerValue( string );
    return numericValue != null && numericValue.intValue() > 0;
  }

  /**
   * Loads the promotion and any goal levels
   * 
   * @param promo
   */
  public void loadPromotion( Promotion promo )
  {
    Promotion promotion = promo;

    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.version = promotion.getVersion();
    if ( promotion.getPromotionType().getCode().equals( PromotionType.GOALQUEST ) )
    {
      this.awardType = PromotionAwardsType.lookup( PromotionAwardsType.POINTS ).getName();
    }
    else
    {
      if ( promotion.getAwardType() != null )
      {
        this.awardType = promotion.getAwardType().getName();
      }
    }

    if ( promo.isGoalQuestPromotion() )
    {
      GoalQuestPromotion gqPromotion = (GoalQuestPromotion)promo;
      if ( gqPromotion.getOverrideStructure() != null )
      {
        this.overrideStructure = gqPromotion.getOverrideStructure().getCode();
        this.prevOverrideStructure = this.overrideStructure;
      }
      buildManagerOverrideLevels( gqPromotion );
      this.beforeGoalSelection = true;
      this.inGoalSelection = false;
      this.afterGoalSelection = false;
      Date now = new Date();
      if ( gqPromotion.getGoalCollectionStartDate() != null )
      {
        Date startDate = DateUtils.toStartDate( gqPromotion.getGoalCollectionStartDate() );
        if ( now.after( startDate ) )
        {
          beforeGoalSelection = false;
          if ( gqPromotion.getGoalCollectionEndDate() == null )
          {
            this.inGoalSelection = true;
          }
          else
          {
            Date endDate = DateUtils.toEndDate( gqPromotion.getGoalCollectionEndDate() );
            if ( now.before( endDate ) )
            {
              this.inGoalSelection = true;
            }
            else
            {
              this.afterGoalSelection = true;
            }
          }
        }
      }
    }
    else if ( promo.isChallengePointPromotion() )
    {
      ChallengePointPromotion cpPromotion = (ChallengePointPromotion)promo;
      if ( cpPromotion.getOverrideStructure() != null )
      {
        this.overrideStructure = cpPromotion.getOverrideStructure().getCode();
        this.prevOverrideStructure = this.overrideStructure;
        if ( cpPromotion.getTotalTeamProductionMeasure() != null && cpPromotion.getTotalTeamProductionMeasure().equals( ChallengePointPromotion.SECONDARY_TOTAL_PRODUCTION_PERCENTAGE ) )
        {
          this.percentBaselineTeamProduction = true;
          this.totalTeamProductionPct = cpPromotion.getTotalTeamProduction();
        }
        else if ( cpPromotion.getTotalTeamProductionMeasure() != null && cpPromotion.getTotalTeamProductionMeasure().equals( ChallengePointPromotion.SECONDARY_TOTAL_PRODUCTION_QUANTITY ) )
        {
          this.percentBaselineTeamProduction = false;
          this.totalTeamProductionQty = cpPromotion.getTotalTeamProduction();
        }
        this.managerAward = cpPromotion.getManagerAward();

      }
      buildManagerOverrideLevels( cpPromotion );
      this.beforeGoalSelection = true;
      this.inGoalSelection = false;
      this.afterGoalSelection = false;
      Date now = new Date();
      if ( cpPromotion.getGoalCollectionStartDate() != null )
      {
        Date startDate = DateUtils.toStartDate( cpPromotion.getGoalCollectionStartDate() );
        if ( now.after( startDate ) )
        {
          beforeGoalSelection = false;
          if ( cpPromotion.getGoalCollectionEndDate() == null )
          {
            this.inGoalSelection = true;
          }
          else
          {
            Date endDate = DateUtils.toEndDate( cpPromotion.getGoalCollectionEndDate() );
            if ( now.before( endDate ) )
            {
              this.inGoalSelection = true;
            }
            else
            {
              this.afterGoalSelection = true;
            }
          }
        }
      }

    }
  }

  /**
   * Create the managerOverrideValueList based on the promotion goal levels.
   * 
   * @param promotion
   */
  public void buildManagerOverrideLevels( GoalQuestPromotion promotion )
  {
    Set overrideLevels = null;
    if ( isAmountPerAchiever() || isPercentTeamEarnings() )
    {
      if ( managerOverrideValueList == null || managerOverrideValueList.isEmpty() )
      {
        managerOverrideValueList = new ArrayList<PromotionManagerOverrideLevelFormBean>();
      }
      else
      {
        managerOverrideValueList.clear();
      }
      for ( int sequenceNumber = 1; sequenceNumber <= 2; sequenceNumber++ )
      {
        managerOverrideValueList.add( buildManagerOverrideBean( sequenceNumber, promotion ) );
      }
      resortOverrideLevels();
    }
    if ( isManagerTeamAchievementPercentage() )
    {
      overrideLevels = promotion.getManagerOverrideGoalLevels();
    }
    if ( isManagerTeamAchievementPercentage() )
    {
      if ( overrideLevels != null && !overrideLevels.isEmpty() )
      {
        if ( managerOverrideValueList == null || managerOverrideValueList.isEmpty() )
        {
          managerOverrideValueList = new ArrayList<PromotionManagerOverrideLevelFormBean>();
        }
        else
        {
          managerOverrideValueList.clear();
        }
        for ( Iterator overrideLevelIter = overrideLevels.iterator(); overrideLevelIter.hasNext(); )
        {
          AbstractGoalLevel currentOverrideLevelLevel = (AbstractGoalLevel)overrideLevelIter.next();

          PromotionManagerOverrideLevelFormBean formBean = new PromotionManagerOverrideLevelFormBean();
          formBean = buildOverrideLevelBean( currentOverrideLevelLevel, promotion );
          managerOverrideValueList.add( formBean );
          if ( isManagerTeamAchievementPercentage() )
          {
            formBean.setManagerStackRankPayoutValueList( managerOverrideValueList );
          }
        }
        while ( managerOverrideValueList.size() < 2 )
        {
          PromotionManagerOverrideLevelFormBean formBean = new PromotionManagerOverrideLevelFormBean();
          formBean.setSequenceNumber( managerOverrideValueList.size() + 1 );
          managerOverrideValueList.add( formBean );
        }
        resortOverrideLevels();
      }
      else
      {
        while ( managerOverrideValueList.size() < 2 )
        {
          PromotionManagerOverrideLevelFormBean formBean = new PromotionManagerOverrideLevelFormBean();
          formBean.setSequenceNumber( managerOverrideValueList.size() + 1 );
          managerOverrideValueList.add( formBean );
        }
      }
    }
  }

  /**
   * Create the managerOverrideValueList based on the promotion goal levels.
   * 
   * @param promotion
   */
  public void buildManagerOverrideLevels( ChallengePointPromotion promotion )
  {
    Set overrideLevels = null;
    if ( isAmountPerAchiever() || isPercentTeamEarnings() )
    {
      if ( managerOverrideValueList == null || managerOverrideValueList.isEmpty() )
      {
        managerOverrideValueList = new ArrayList<PromotionManagerOverrideLevelFormBean>();
      }
      else
      {
        managerOverrideValueList.clear();
      }
      for ( int sequenceNumber = 1; sequenceNumber <= 2; sequenceNumber++ )
      {
        managerOverrideValueList.add( buildManagerOverrideBean( sequenceNumber, promotion ) );
      }
      resortOverrideLevels();
    }
    if ( isManagerTeamAchievementPercentage() )
    {
      overrideLevels = promotion.getManagerOverrideGoalLevels();
    }
    if ( isManagerTeamAchievementPercentage() )
    {
      if ( overrideLevels != null && !overrideLevels.isEmpty() )
      {
        if ( managerOverrideValueList == null || managerOverrideValueList.isEmpty() )
        {
          managerOverrideValueList = new ArrayList<PromotionManagerOverrideLevelFormBean>();
        }
        else
        {
          managerOverrideValueList.clear();
        }
        for ( Iterator<AbstractGoalLevel> overrideLevelIter = overrideLevels.iterator(); overrideLevelIter.hasNext(); )
        {
          AbstractGoalLevel currentOverrideLevelLevel = overrideLevelIter.next();
          managerOverrideValueList.add( buildOverrideLevelBean( currentOverrideLevelLevel, promotion ) );
        }
        while ( managerOverrideValueList.size() < 2 )
        {
          PromotionManagerOverrideLevelFormBean formBean = new PromotionManagerOverrideLevelFormBean();
          formBean.setSequenceNumber( managerOverrideValueList.size() + 1 );
          managerOverrideValueList.add( formBean );
        }
        resortOverrideLevels();
      }
      else
      {
        while ( managerOverrideValueList.size() < 2 )
        {
          PromotionManagerOverrideLevelFormBean formBean = new PromotionManagerOverrideLevelFormBean();
          formBean.setSequenceNumber( managerOverrideValueList.size() + 1 );
          managerOverrideValueList.add( formBean );
        }
      }
    }
  }

  /**
   * Sort the managerOverrideValueList - natural sort order should be sequence number.
   */
  public void resortOverrideLevels()
  {
    if ( this.managerOverrideValueList != null )
    {
      Collections.sort( this.managerOverrideValueList );
    }

  }

  /**
   * Creates a new PromotionManagerOverrideLevelFormBean from the given override level
   * 
   * @param overrideLevel
   * @param promotion
   * @return
   */
  private PromotionManagerOverrideLevelFormBean buildOverrideLevelBean( GoalLevel overrideLevel, ChallengePointPromotion promotion )
  {
    PromotionManagerOverrideLevelFormBean overrideLevelFormBean = new PromotionManagerOverrideLevelFormBean();
    overrideLevelFormBean.setOverrideLevelId( overrideLevel.getId() );
    overrideLevelFormBean.setSequenceNumber( overrideLevel.getSequenceNumber() );
    overrideLevelFormBean.setName( overrideLevel.getGoalLevelName() );
    overrideLevelFormBean.setNameKey( overrideLevel.getGoalLevelNameKey() );
    overrideLevelFormBean.setDescription( overrideLevel.getGoalLevelDescription() );
    overrideLevelFormBean.setDescriptionKey( overrideLevel.getGoalLevelDescriptionKey() );
    overrideLevelFormBean.setGoalLevelcmAssetCode( overrideLevel.getGoalLevelcmAssetCode() );
    if ( overrideLevel.getManagerAward() != null )
    {
      overrideLevelFormBean.setAward( overrideLevel.getManagerAward().toString() );
    }

    return overrideLevelFormBean;
  }

  /**
   * Creates a new PromotionManagerOverrideLevelFormBean from the given override level
   * 
   * @param overrideLevel
   * @param promotion
   * @return
   */
  private PromotionManagerOverrideLevelFormBean buildOverrideLevelBean( AbstractGoalLevel overrideLevel, GoalQuestPromotion promotion )
  {
    PromotionManagerOverrideLevelFormBean overrideLevelFormBean = new PromotionManagerOverrideLevelFormBean();
    overrideLevelFormBean.setOverrideLevelId( overrideLevel.getId() );
    overrideLevelFormBean.setSequenceNumber( overrideLevel.getSequenceNumber() );
    overrideLevelFormBean.setName( overrideLevel.getGoalLevelName() );
    overrideLevelFormBean.setDescription( overrideLevel.getGoalLevelDescription() );
    overrideLevelFormBean.setNameKey( overrideLevel.getGoalLevelNameKey() );
    overrideLevelFormBean.setDescriptionKey( overrideLevel.getGoalLevelDescriptionKey() );
    overrideLevelFormBean.setGoalLevelcmAssetCode( overrideLevel.getGoalLevelcmAssetCode() );
    if ( overrideLevel.getManagerAward() != null )
    {
      overrideLevelFormBean.setAward( overrideLevel.getManagerAward().toString() );
    }
    if ( isManagerTeamAchievementPercentage() || isManagerActualTeamAchievement() )
    {
      ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)overrideLevel;
      if ( managerOverrideGoalLevel.getTeamAchievementPercent() != null )
      {
        overrideLevelFormBean.setTeamAchievementPercent( managerOverrideGoalLevel.getTeamAchievementPercent().toString() );
      }
      if ( managerOverrideGoalLevel.getMoStartRank() != null )
      {
        overrideLevelFormBean.setMoStartRank( managerOverrideGoalLevel.getMoStartRank().toString() );
      }
      if ( managerOverrideGoalLevel.getMoEndRank() != null )
      {
        overrideLevelFormBean.setMoEndRank( managerOverrideGoalLevel.getMoEndRank().toString() );
      }
      if ( managerOverrideGoalLevel.getMoAwards() != null )
      {
        overrideLevelFormBean.setMoAwards( managerOverrideGoalLevel.getMoAwards().toString() );
      }
    }
    return overrideLevelFormBean;
  }

  private PromotionManagerOverrideLevelFormBean buildManagerOverrideBean( Integer sequenceNumber, GoalQuestPromotion promotion )
  {
    PromotionManagerOverrideLevelFormBean overrideLevelFormBean = new PromotionManagerOverrideLevelFormBean();
    overrideLevelFormBean.setSequenceNumber( sequenceNumber );
    if ( getOverrideStructure() != null && promotion.getOverrideStructure() != null && getOverrideStructure().equals( promotion.getOverrideStructure().getCode() ) )
    {
      if ( sequenceNumber == 1 )
      {
        overrideLevelFormBean.setAward( promotion.getLevelOneMgrAward().toString() );

      }
      else if ( sequenceNumber == 2 && promotion.getLevelTwoMgrAward() != null && promotion.getLevelTwoMgrAward().toString() != null )
      {
        overrideLevelFormBean.setAward( promotion.getLevelTwoMgrAward().toString() );

      }
      else
      {
        overrideLevelFormBean.setAward( "" );
      }
    }
    else
    {
      overrideLevelFormBean.setAward( "" );
    }
    return overrideLevelFormBean;
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @return Promotion
   */
  public Promotion toDomainObject()
  {
    // Create a new Promotion since one was not passed in
    if ( getPromotionTypeCode().equals( PromotionType.GOALQUEST ) )
    {
      return toDomainObject( new GoalQuestPromotion() );
    }
    if ( getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      return toDomainObject( new ChallengePointPromotion() );
    }
    return null;
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( Promotion promotion )
  {
    promotion.setId( this.getPromotionId() );
    promotion.setName( this.getPromotionName() );
    promotion.setVersion( this.getVersion() );
    promotion.setPromotionType( PromotionType.lookup( getPromotionTypeCode() ) );
    if ( getPromotionTypeCode().equals( PromotionType.GOALQUEST ) || getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      ( (GoalQuestPromotion)promotion ).setOverrideStructure( ManagerOverrideStructure.lookup( getOverrideStructure() ) );
      if ( isAmountPerAchiever() || isPercentTeamEarnings() || isNoOverride() )
      {
        ( (GoalQuestPromotion)promotion ).setManagerOverrideGoalLevels( null );
      }
      if ( !isNoOverride() )
      {
        if ( getManagerOverrideValueAsList() != null )
        {
          int currentSequence = 1;
          for ( Iterator overrideLevelIter = getManagerOverrideValueAsList().iterator(); overrideLevelIter.hasNext(); )
          {
            PromotionManagerOverrideLevelFormBean overrideLevelFormBean = (PromotionManagerOverrideLevelFormBean)overrideLevelIter.next();
            AbstractGoalLevel overrideLevel = null;
            if ( isAmountPerAchiever() || isPercentTeamEarnings() )
            {
              if ( currentSequence == 1 )
              {
                ( (GoalQuestPromotion)promotion ).setLevelOneMgrAward( new BigDecimal( overrideLevelFormBean.getAward() ) );
              }
              else if ( currentSequence == 2 && !overrideLevelFormBean.getAward().equals( "" ) && overrideLevelFormBean.getAward() != null )
              {
                ( (GoalQuestPromotion)promotion ).setLevelTwoMgrAward( new BigDecimal( overrideLevelFormBean.getAward() ) );
              }
              currentSequence++;
            }
            else if ( isManagerTeamAchievementPercentage() )
            {
              ManagerOverrideGoalLevel managerOverrideGoalLevel = new ManagerOverrideGoalLevel();
              managerOverrideGoalLevel.setTeamAchievementPercent( getNumericValue( overrideLevelFormBean.getTeamAchievementPercent() ) );
              managerOverrideGoalLevel.setSequenceNumber( currentSequence++ );
              if ( overrideLevelFormBean.getMoStartRank() != null )
              {
                managerOverrideGoalLevel.setMoStartRank( getLongValue( overrideLevelFormBean.getMoStartRank() ) );
              }
              if ( overrideLevelFormBean.getMoEndRank() != null )
              {
                managerOverrideGoalLevel.setMoEndRank( getLongValue( overrideLevelFormBean.getMoEndRank() ) );
              }
              if ( overrideLevelFormBean.getMoAwards() != null )
              {
                managerOverrideGoalLevel.setMoAwards( getLongValue( overrideLevelFormBean.getMoAwards() ) );
              }
              if ( overrideLevelFormBean.getOverrideLevelId() == null || overrideLevelFormBean.getOverrideLevelId().longValue() == 0 )
              {
                managerOverrideGoalLevel.setId( null );
              }
              else
              {
                managerOverrideGoalLevel.setId( overrideLevelFormBean.getOverrideLevelId() );
              }
              overrideLevel = managerOverrideGoalLevel;

              overrideLevel.setId( overrideLevelFormBean.getOverrideLevelId() );
              if ( overrideLevelFormBean.getAward() != null && !overrideLevelFormBean.getAward().isEmpty() )
              {
                overrideLevel.setManagerAward( new BigDecimal( overrideLevelFormBean.getAward() ) );
              }
              ( (GoalQuestPromotion)promotion ).addManagerOverrideGoalLevel( (ManagerOverrideGoalLevel)overrideLevel );
            }
          }
        }
      }
    }
    /*
     * else if ( getPromotionTypeCode().equals( PromotionType.CHALLENGE_POINT ) ) {
     * ChallengePointPromotion cpPromotion = (ChallengePointPromotion)promotion; if (
     * this.getOverrideStructure().equals( ManagerOverrideStructure.STACK_RANKING_LEVEL ) ) { if (
     * this.percentBaselineTeamProduction ) { cpPromotion.setTotalTeamProductionMeasure(
     * ChallengePointPromotion.SECONDARY_TOTAL_PRODUCTION_PERCENTAGE );
     * cpPromotion.setTotalTeamProduction( this.totalTeamProductionPct ); } else {
     * cpPromotion.setTotalTeamProductionMeasure(
     * ChallengePointPromotion.SECONDARY_TOTAL_PRODUCTION_QUANTITY );
     * cpPromotion.setTotalTeamProduction( this.totalTeamProductionQty ); } }
     * cpPromotion.setManagerAward( this.managerAward ); cpPromotion.setOverrideStructure(
     * ManagerOverrideStructure.lookup( getOverrideStructure() ) ); cpPromotion.setOverridePercent(
     * getIntegerValue( getOverridePercent() ) ); if ( getManagerOverrideValueList() != null ) { int
     * currentSequence = 1; for ( Iterator overrideLevelIter =
     * getManagerOverrideValueList().iterator(); overrideLevelIter.hasNext(); ) {
     * PromotionManagerOverrideLevelFormBean overrideLevelFormBean =
     * (PromotionManagerOverrideLevelFormBean)overrideLevelIter.next(); if ( ( (
     * StringUtils.isNotBlank( overrideLevelFormBean.getName() ) ) && ( StringUtils.isNotBlank(
     * overrideLevelFormBean.getDescription() ) ) ) || isManagerTeamAchievementPercentage() ) {
     * AbstractGoalLevel overrideLevel = new GoalLevel(); if ( isAmountPerAchiever() ) { //TODO
     * //overrideLevel.setGoalLevelName( overrideLevelFormBean.getName() );
     * overrideLevel.setManagerAward( new BigDecimal( overrideLevelFormBean.getAward() ) );
     * overrideLevel.setId( overrideLevelFormBean.getOverrideLevelId() ); cpPromotion.addGoalLevel(
     * overrideLevel ); } if( isManagerTeamAchievementPercentage() ) { ManagerOverrideGoalLevel
     * managerOverrideGoalLevel = new ManagerOverrideGoalLevel();
     * managerOverrideGoalLevel.setSequenceNumber( currentSequence++ ); if(
     * overrideLevelFormBean.getMoStartRank() != null ) { managerOverrideGoalLevel.setMoStartRank(
     * getLongValue( overrideLevelFormBean.getMoStartRank() ) ); } if(
     * overrideLevelFormBean.getMoEndRank() != null ) { managerOverrideGoalLevel.setMoEndRank(
     * getLongValue( overrideLevelFormBean.getMoEndRank() ) ); } if(
     * overrideLevelFormBean.getMoAwards() != null ) { managerOverrideGoalLevel.setMoAwards(
     * getLongValue( overrideLevelFormBean.getMoAwards() ) ); } if (
     * overrideLevelFormBean.getOverrideLevelId() == null ||
     * overrideLevelFormBean.getOverrideLevelId().longValue() == 0 ) {
     * managerOverrideGoalLevel.setId( null ); } else { managerOverrideGoalLevel.setId(
     * overrideLevelFormBean.getOverrideLevelId() ); } overrideLevel = managerOverrideGoalLevel; }
     * overrideLevel.setId( overrideLevelFormBean.getOverrideLevelId() ); (
     * (ChallengePointPromotion)promotion ).addManagerOverrideGoalLevel(
     * (ManagerOverrideGoalLevel)overrideLevel ); } } } }
     */
    return promotion;
  }

  public Long getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( Long promotionId )
  {
    this.promotionId = promotionId;
  }

  public String getPromotionName()
  {
    return promotionName;
  }

  public void setPromotionName( String promotionName )
  {
    this.promotionName = promotionName;
  }

  public String getPromotionStatus()
  {
    return promotionStatus;
  }

  public void setPromotionStatus( String promotionStatus )
  {
    this.promotionStatus = promotionStatus;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  /**
   * @return List of PromotionPayoutFormBean objects
   */

  public void resetManagerOverrideValueList( Promotion promotion )
  {
    if ( getOverrideStructure() != null )
    {
      if ( promotion.isGoalQuestPromotion() )
      {
        buildManagerOverrideLevels( (GoalQuestPromotion)promotion );
      }
      else if ( promotion.isChallengePointPromotion() )
      {
        buildManagerOverrideLevels( (ChallengePointPromotion)promotion );
      }
    }
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionECardFormBean from the value list
   */

  public PromotionManagerOverrideLevelFormBean getManagerOverrideValueList( int index )
  {
    try
    {
      return (PromotionManagerOverrideLevelFormBean)managerOverrideValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  /**
   * resets the value list with empty PromotionManagerOverrideLevelFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      PromotionManagerOverrideLevelFormBean formBean = new PromotionManagerOverrideLevelFormBean();
      formBean.setSequenceNumber( i + 1 );
      valueList.add( formBean );
    }

    return valueList;
  }

  public void addEmptyOverrideLevel()
  {
    PromotionManagerOverrideLevelFormBean formBean = new PromotionManagerOverrideLevelFormBean();
    formBean.setSequenceNumber( getManagerOverrideValueListSize() + 1 );
    getManagerOverrideValueAsList().add( formBean );
  }

  public ActionErrors validateStackRankPayouts( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    boolean validPayout = true;
    // even if there is one error in validating payouts there is no point in checking
    if ( this.getManagerOverrideValueListSize() > 0 )
    {
      for ( Iterator overrideIter = getManagerOverrideValueAsList().iterator(); overrideIter.hasNext(); )
      {
        PromotionManagerOverrideLevelFormBean overrideLevelFormBean = (PromotionManagerOverrideLevelFormBean)overrideIter.next();
        validPayout = validatePositiveNumeric( overrideLevelFormBean.getMoAwards() );
        if ( !validPayout )
        {
          // Invalid payout type non numeric - ERROR
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_STACKRANK_PAYOUT_ERROR" ) );
          break;
        }
      }
    }

    return errors;
  }

  public ActionErrors validateStackRankPayoutRanks( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    // even if there is one error in validating from ot rank there is no point in checking up
    // overlap
    boolean checkRankOverlap = false;
    // adding this to make sure we have atlease one payout and atleast one per payout group
    int totalPayoutCount = 0;
    if ( this.getManagerOverrideValueListSize() > 0 )
    {
      /*
       * for ( Iterator overrideLevelIter = this.getManagerOverrideValueList().iterator();
       * overrideLevelIter.hasNext(); ) { PromotionManagerOverrideLevelFormBean
       * promotionStackRankPayoutGroupFormBean =
       * (PromotionManagerOverrideLevelFormBean)overrideLevelIter.next();
       */
      // adding this to make sure we have atlease one payout and atleast one per payout group
      // int groupPayoutCount = 0;
      String cmText = ContentReaderManager.getText( "promotion.manageroverride", "AWARD" );
      for ( int i = 0; i < this.getManagerOverrideValueListSize(); i++ )
      {
        totalPayoutCount++;
        // groupPayoutCount++;
        PromotionManagerOverrideLevelFormBean promoStackRankPayoutValueBean = this.getManagerOverrideValueList( i );
        boolean validFromRank = validatePositiveNumericMoreThanZero( promoStackRankPayoutValueBean.getMoStartRank() );
        boolean validToRank = validatePositiveNumericMoreThanZero( promoStackRankPayoutValueBean.getMoEndRank() );
        boolean validAward = validatePositiveNumericMoreThanZero( promoStackRankPayoutValueBean.getMoAwards() );
        if ( validFromRank && validToRank && validAward )
        {
          if ( Integer.parseInt( promoStackRankPayoutValueBean.getMoStartRank() ) > Integer.parseInt( promoStackRankPayoutValueBean.getMoEndRank() ) )
          {
            // from reank cant be more than torank - ERROR
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
            checkRankOverlap = false;
            break;
          }
          else
          {
            checkRankOverlap = true;
          }
        }
        else if ( !validAward )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_NON_ZERO_POSITIVE_INTEGER, cmText ) );
          checkRankOverlap = false;
        }
        else
        {
          checkRankOverlap = false;
          // one of them is or both are non numeric - ERROR
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.RANK_NUMERIC_ERROR" ) );
          break;
        }
        /*
         * if ( groupPayoutCount <= 0 ) { // group must have atlease one payout - error errors.add(
         * ActionErrors.GLOBAL_MESSAGE, new ActionMessage(
         * "promotion.payout.errors.MIN_ONE_PAYOUT_PER_GROUP_ERROR" ) ); }
         */
      }

      if ( totalPayoutCount <= 0 )
      {
        // we must have atlease one payout - error
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_STACKRANK_PAYOUT_ERROR" ) );
      }

      /*
       * if ( !checkRankOverlap ) { break; }
       */
      // }

      if ( checkRankOverlap )
      {
        validateStackrankOverlap( errors );
      }
    }
    else
    {
      // need atleast on payout
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_STACKRANK_PAYOUT_ERROR" ) );
    }

    return errors;
  }

  private ActionErrors validateStackrankOverlap( ActionErrors errors )
  {
    boolean isOverlap = false;

    if ( this.getManagerOverrideValueListSize() > 0 )
    {
      List<PromotionManagerOverrideLevelFormBean> ranges = this.getManagerOverrideValueAsList();
      for ( int i = 0; i < ranges.size(); i++ )
      {
        PromotionManagerOverrideLevelFormBean outerBean = ranges.get( i );
        if ( Integer.parseInt( outerBean.getMoStartRank() ) <= Integer.parseInt( outerBean.getMoEndRank() ) )
        {
          for ( int j = i + 1; j < ranges.size(); j++ )
          {
            PromotionManagerOverrideLevelFormBean innerBean = ranges.get( j );
            if ( Integer.parseInt( innerBean.getMoStartRank() ) <= Integer.parseInt( innerBean.getMoEndRank() ) )
            {
              if ( overlaps( innerBean, outerBean ) )
              {
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.RANK_OVERLAP_ERROR" ) );
                isOverlap = true;
                break;
              }
              else
              {
                isOverlap = false;
              }
            }
            else
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
              isOverlap = true;
              break;
            }
            if ( isOverlap )
            {
              break;
            }
          }
        }
        else
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
          isOverlap = true;
        }
      }
    }
    return errors;
  }

  private boolean overlaps( PromotionManagerOverrideLevelFormBean inner, PromotionManagerOverrideLevelFormBean outer )
  {
    return Integer.parseInt( inner.getMoStartRank() ) <= Integer.parseInt( outer.getMoEndRank() ) && Integer.parseInt( outer.getMoStartRank() ) <= Integer.parseInt( inner.getMoEndRank() );
  }

  private boolean isPercentTeamEarnings()
  {
    return overrideStructure != null && overrideStructure.equals( ManagerOverrideStructure.OVERRIDE_PERCENT );
  }

  private boolean isAmountPerAchiever()
  {
    return overrideStructure != null && overrideStructure.equals( ManagerOverrideStructure.AWARD_PER_ACHIEVER );
  }

  private boolean isManagerTeamAchievementPercentage()
  {
    return overrideStructure != null && overrideStructure.equals( ManagerOverrideStructure.STACK_RANKING_LEVEL );
  }

  private boolean isManagerActualTeamAchievement()
  {
    return overrideStructure != null && ( overrideStructure.equals( ManagerOverrideStructure.STACK_RANKING_LEVEL ) || overrideStructure.equals( CPOverrideStructure.MANAGER_ACTUAL_TEAM_ACHIEVEMENT ) );
  }

  private boolean isNoOverride()
  {
    return overrideStructure == null || overrideStructure.equals( ManagerOverrideStructure.NONE );
  }

  public String getPromotionTypeName()
  {
    return promotionTypeName;
  }

  public void setPromotionTypeName( String promotionTypeName )
  {
    this.promotionTypeName = promotionTypeName;
  }

  public String getPromotionTypeCode()
  {
    return promotionTypeCode;
  }

  public String getOverrideStructure()
  {
    return overrideStructure;
  }

  public void setPromotionTypeCode( String promotionTypeCode )
  {
    this.promotionTypeCode = promotionTypeCode;
  }

  public String getAwardType()
  {
    return awardType;
  }

  public void setAwardType( String awardType )
  {
    this.awardType = awardType;
  }

  /**
   * Bean location through BeanLocator look-up.
   * 
   * @param beanName
   * @return SAO
   */
  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  /**
   * @return value of alternateReturnUrl property
   */
  public String getAlternateReturnUrl()
  {
    return alternateReturnUrl;
  }

  /**
   * @param alternateReturnUrl value for alternateReturnUrl property
   */
  public void setAlternateReturnUrl( String alternateReturnUrl )
  {
    // check for valid "redirection" URL's.
    if ( alternateReturnUrl != null && ( alternateReturnUrl.contains( "/stackRankListDisplay.do" ) || alternateReturnUrl.contains( "/expiredStackRankListDisplay.do" ) ) )
    {
      this.alternateReturnUrl = alternateReturnUrl;
    }
    else
    {
      this.alternateReturnUrl = "";
    }
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getPrevOverrideStructure()
  {
    return prevOverrideStructure;
  }

  public void setPrevOverrideStructure( String prevOverrideStructure )
  {
    this.prevOverrideStructure = prevOverrideStructure;
  }

  public void setOverrideStructure( String overrideStructure )
  {
    this.overrideStructure = overrideStructure;
  }

  public List getOverrideStructureList()
  {
    return overrideStructureList;
  }

  public void setOverrideStructureList( List overrideStructureList )
  {
    this.overrideStructureList = overrideStructureList;
  }

  public List<PromotionManagerOverrideLevelFormBean> getManagerOverrideValueAsList()
  {
    if ( managerOverrideValueList == null )
    {
      managerOverrideValueList = new ArrayList<PromotionManagerOverrideLevelFormBean>();
    }
    return managerOverrideValueList;
  }

  public void setManagerOverrideValueAsList( List<PromotionManagerOverrideLevelFormBean> managerOverrideValueList )
  {
    this.managerOverrideValueList = managerOverrideValueList;
  }

  public int getManagerOverrideValueListSize()
  {
    if ( this.managerOverrideValueList != null )
    {
      return this.managerOverrideValueList.size();
    }
    return 0;
  }

  public String getnewElementSequenceNum()
  {
    return newElementSequenceNum;
  }

  public void setnewElementSequenceNum( String newElementSequenceNum )
  {
    this.newElementSequenceNum = newElementSequenceNum;
  }

  public String getOldSequence()
  {
    return oldSequence;
  }

  public void setOldSequence( String oldSequence )
  {
    this.oldSequence = oldSequence;
  }

  public boolean isAfterGoalSelection()
  {
    return afterGoalSelection;
  }

  public void setAfterGoalSelection( boolean afterGoalSelection )
  {
    this.afterGoalSelection = afterGoalSelection;
  }

  public boolean isBeforeGoalSelection()
  {
    return beforeGoalSelection;
  }

  public void setBeforeGoalSelection( boolean beforeGoalSelection )
  {
    this.beforeGoalSelection = beforeGoalSelection;
  }

  public boolean isInGoalSelection()
  {
    return inGoalSelection;
  }

  public void setInGoalSelection( boolean inGoalSelection )
  {
    this.inGoalSelection = inGoalSelection;
  }

  public Integer getOverrideAward()
  {
    return managerAward;
  }

  public void setOverrideAward( Integer managerAward )
  {
    this.managerAward = managerAward;
  }

  public boolean isPercentBaselineTeamProduction()
  {
    return percentBaselineTeamProduction;
  }

  public void setPercentBaselineTeamProduction( boolean percentBaselineTeamProduction )
  {
    this.percentBaselineTeamProduction = percentBaselineTeamProduction;
  }

  public Integer getTotalTeamProduction()
  {
    return totalTeamProduction;
  }

  public void setTotalTeamProduction( Integer totalTeamProduction )
  {
    this.totalTeamProduction = totalTeamProduction;
  }

  public Integer getTotalTeamProductionPct()
  {
    return totalTeamProductionPct;
  }

  public void setTotalTeamProductionPct( Integer totalTeamProductionPct )
  {
    this.totalTeamProductionPct = totalTeamProductionPct;
  }

  public Integer getTotalTeamProductionQty()
  {
    return totalTeamProductionQty;
  }

  public void setTotalTeamProductionQty( Integer totalTeamProductionQty )
  {
    this.totalTeamProductionQty = totalTeamProductionQty;
  }

  public int getManagerStackRankPayoutValueListCount()
  {
    if ( managerOverrideValueList == null || managerOverrideValueList.size() == 0 )
    {
      return 0;
    }

    // Get the size of the child collection
    return getManagerOverrideValueListSize();
  }

}
