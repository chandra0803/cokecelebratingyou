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

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.PayoutStructure;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.SAO;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * PromotionGoalPayoutForm <p/> <b>Change History:</b><br>
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
public class PromotionGoalPayoutForm extends BaseForm
{

  private static final long serialVersionUID = 1L;

  private String achievementRuleTypeCode;
  private String payoutStructure = PayoutStructure.BOTH;
  private String achievementPrecision = AchievementPrecision.ZERO;
  private String roundingMethod = RoundingMethod.STANDARD;
  private Long promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionStatus;
  private String alternateReturnUrl;
  private Long version;
  private String promotionTypeCode;
  private String awardType;
  private String awardTypeName;
  private List<PromotionGoalPayoutLevelFormBean> goalLevelValueList;
  private String oldSequence;
  private String newElementSequenceNum;
  private String method = "";
  private boolean beforeGoalSelection;
  private boolean inGoalSelection;
  private boolean afterGoalSelection;
  private String goalPlanningWorksheet;
  private String baseUnit;
  private String baseUnitPosition = BaseUnitPosition.UNIT_AFTER;

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    int goalCount = RequestUtils.getOptionalParamInt( request, "goalLevelValueListSize" );
    goalLevelValueList = getEmptyValueList( goalCount );
  }

  /**
   * Validate goalLevel fields
   * 
   * @param goalLevelFormBean
   * @return
   */
  protected boolean validate( PromotionGoalPayoutLevelFormBean goalLevelFormBean, ActionErrors actionErrors )
  {
    boolean valid = true;
    if ( StringUtils.isBlank( goalLevelFormBean.getName() ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.payout.goalquest", "NAME" ) ) );
      valid = false;
    }
    if ( StringUtils.isBlank( goalLevelFormBean.getDescription() ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, ContentReaderManager.getText( "promotion.payout.goalquest", "DESCRIPTION" ) ) );
      valid = false;
    }
    if ( payoutStructure == null || payoutStructure.equals( "" ) || payoutStructure.equalsIgnoreCase( PayoutStructure.FIXED ) )
    {
      if ( !validatePositiveNumeric( goalLevelFormBean.getAchievementAmount() ) && goalLevelFormBean.getAchievementAmount() == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.ACHIEVEMENT_AMOUNT" ) );
        valid = false;
      }

      if ( validatePositiveDoubleEquals( goalLevelFormBean.getAchievementAmount() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.ACHIEVEMENT_AMOUNT_LESSER" ) );
        valid = false;
      }

    }

    if ( awardType != null && awardType.equalsIgnoreCase( "points" ) )
    {
      if ( !validatePositiveInteger( goalLevelFormBean.getAward() ) && goalLevelFormBean.getAward() == null )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.AWARD_PERQS" ) );
        valid = false;
      }

      if ( validatePositiveLongEquals( goalLevelFormBean.getAward() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.AWARD_LESSER" ) );
        valid = false;
      }

      if ( payoutStructure != null && !payoutStructure.equals( "" ) && !payoutStructure.equalsIgnoreCase( PayoutStructure.FIXED ) )
      {
        if ( !validatePositiveNumeric( goalLevelFormBean.getMinimumQualifier() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.MINIMUM_QUALIFIER" ) );
          valid = false;
        }
        if ( !validatePositiveNumeric( goalLevelFormBean.getIncrementalQuantity() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.INCREMENTAL_QUANTITY" ) );
          valid = false;
        }
        if ( StringUtils.isNotBlank( goalLevelFormBean.getMaximumPoints() ) && !validatePositiveInteger( goalLevelFormBean.getMaximumPoints() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.MAXIMUM_POINTS" ) );
          valid = false;
        }
      }
      if ( payoutStructure != null && !payoutStructure.equals( "" ) && payoutStructure.equalsIgnoreCase( PayoutStructure.BOTH ) )
      {
        if ( !validatePositiveInteger( goalLevelFormBean.getBonusAward() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.BONUS_AWARD" ) );
          valid = false;
        }
      }

    }

    if ( awardType != null && !awardType.equalsIgnoreCase( "points" ) )
    {
      if ( !validatePositiveNumeric( goalLevelFormBean.getAward() ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.AWARD_NON_PERQS" ) );
        valid = false;
      }
    }

    return valid;
  }

  /**
   * Checks if any of the goalLevelFormBean fields are not blank.
   * 
   * @param goalLevelFormBean
   * @return
   */
  protected boolean isNotBlank( PromotionGoalPayoutLevelFormBean goalLevelFormBean )
  {
    if ( StringUtils.isNotBlank( goalLevelFormBean.getName() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getDescription() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getAchievementAmount() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getAward() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getMinimumQualifier() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getMinimumQualifier() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getIncrementalQuantity() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getBonusAward() ) )
    {
      return true;
    }
    if ( StringUtils.isNotBlank( goalLevelFormBean.getMaximumPoints() ) )
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

    Promotion promotion = getPromotionService().getPromotionById( new Long( getPromotionId() ) );

    if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) )
    {
      if ( ( (ChallengePointPromotion)promotion ).getChallengePointAwardType() != null )
      {
        this.payoutStructure = PayoutStructure.FIXED;
      }
    }

    int countValid = 0;
    for ( Iterator<PromotionGoalPayoutLevelFormBean> goalLevelIter = getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
    {
      PromotionGoalPayoutLevelFormBean goalLevelFormBean = goalLevelIter.next();
      if ( isNotBlank( goalLevelFormBean ) )
      {
        if ( validate( goalLevelFormBean, actionErrors ) )
        {
          countValid += 1;
        }
      }
    }
    if ( countValid == 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.NO_GOAL_LEVELS" ) );

    }

    return actionErrors;
  }

  /**
   * Return BigDecimal representing string.  If string is not a valid
   * number then return null.
   * 
   * @param string
   * @return
   */
  protected BigDecimal getNumericValue( String string )
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

  private Double getDoubleValue( String string )
  {
    Double doubleValue = null;
    try
    {
      if ( StringUtils.isNotEmpty( string ) )
      {
        doubleValue = NumberUtils.createDouble( string );
      }
    }
    catch( NumberFormatException nfe )
    {
    }
    return doubleValue;
  }

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
   * Return Integer representing string.  If string is not a valid
   * number then return null.
   * 
   * @param string
   * @return
   */
  protected Integer getIntegerValue( String string )
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
   * validates String is positive integer, not null, not blank
   * 
   * @param string String that is going to be validated
   * @return boolean
   */
  protected boolean validatePositiveInteger( String string )
  {
    Integer numericValue = getIntegerValue( string );
    return numericValue != null && numericValue.intValue() >= 0;
  }

  private boolean validatePositiveDoubleEquals( String string )
  {
    Double doubleValue = getDoubleValue( string );
    boolean isNotEqual = false;
    if ( doubleValue == null && string != null )
    {
      isNotEqual = true;
    }
    return isNotEqual;
  }

  private boolean validatePositiveLongEquals( String string )
  {
    Long longValue = getLongValue( string );
    boolean isNotEqual = false;
    if ( longValue == null && string != null )
    {
      isNotEqual = true;
    }
    return isNotEqual;
  }

  /**
   * Loads the promotion and any goal levels
   * 
   * @param promo
   */
  public void loadPromotion( Promotion promo )
  {
    GoalQuestPromotion promotion = (GoalQuestPromotion)promo;

    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.version = promotion.getVersion();
    if ( promotion.getAwardType() != null )
    {
      this.awardTypeName = promotion.getAwardType().getName();
      this.awardType = promotion.getAwardType().getCode();
    }
    if ( promotion.getPromotionType().getCode().equals( PromotionType.CHALLENGE_POINT ) && ( (ChallengePointPromotion)promotion ).getChallengePointAwardType() != null )
    {
      this.payoutStructure = PayoutStructure.FIXED;
    }
    else
    {
      if ( promotion.getPayoutStructure() != null )
      {
        this.payoutStructure = promotion.getPayoutStructure().getCode();
      }
    }
    if ( promotion.getAchievementRule() != null )
    {
      this.achievementRuleTypeCode = promotion.getAchievementRule().getCode();
    }
    if ( promotion.getAchievementPrecision() != null )
    {
      this.achievementPrecision = promotion.getAchievementPrecision().getCode();
    }
    if ( promotion.getRoundingMethod() != null )
    {
      this.roundingMethod = promotion.getRoundingMethod().getCode();
    }
    this.goalPlanningWorksheet = promotion.getGoalPlanningWorksheet();
    buildGoalLevels( promotion );
    this.beforeGoalSelection = true;
    this.inGoalSelection = false;
    this.afterGoalSelection = false;
    Date now = new Date();
    if ( promotion.getGoalCollectionStartDate() != null )
    {
      Date startDate = DateUtils.toStartDate( promotion.getGoalCollectionStartDate() );
      if ( now.after( startDate ) )
      {
        beforeGoalSelection = false;
        if ( promotion.getGoalCollectionEndDate() == null )
        {
          this.inGoalSelection = true;
        }
        else
        {
          Date endDate = DateUtils.toEndDate( promotion.getGoalCollectionEndDate() );
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
    // BugFix 17935
    this.baseUnit = promotion.getBaseUnitText();
    if ( promotion.getBaseUnitPosition() != null )
    {
      this.baseUnitPosition = promotion.getBaseUnitPosition().getCode();
    }
  }

  /**
   * Create the goalLevelValueList based on the promotion goal levels.
   * @param promotion
   */
  public void buildGoalLevels( GoalQuestPromotion promotion )
  {
    Set<AbstractGoalLevel> goalLevels = promotion.getGoalLevels();
    if ( goalLevels != null && !goalLevels.isEmpty() )
    {
      if ( goalLevelValueList == null || goalLevelValueList.isEmpty() )
      {
        goalLevelValueList = new ArrayList<PromotionGoalPayoutLevelFormBean>();
      }
      else
      {
        goalLevelValueList.clear();
      }
      for ( Iterator<AbstractGoalLevel> goalLevelIter = goalLevels.iterator(); goalLevelIter.hasNext(); )
      {
        GoalLevel currentGoalLevel = (GoalLevel)goalLevelIter.next();
        goalLevelValueList.add( buildGoalPayoutLevelBean( currentGoalLevel ) );
      }

      while ( getGoalLevelValueListSize() < 3 )
      {
        addEmptyGoalLevel();
      }
      resortGoalLevels();
    }
    else
    {
      while ( getGoalLevelValueListSize() < 3 )
      {
        addEmptyGoalLevel();
      }
    }
  }

  /**
   * Sort the goalLevelValueList - natural sort order should be sequence number.
   */
  public void resortGoalLevels()
  {
    if ( this.goalLevelValueList != null )
    {
      Collections.sort( this.goalLevelValueList );
    }

  }

  /**
   * Creates a new PromotionGoalPayoutLevelFormBean from the given GoalLevel
   * @param goalLevel
   * @return
   */
  private PromotionGoalPayoutLevelFormBean buildGoalPayoutLevelBean( GoalLevel goalLevel )
  {
    PromotionGoalPayoutLevelFormBean goalPayoutLevelFormBean = new PromotionGoalPayoutLevelFormBean();
    goalPayoutLevelFormBean.setGoalLevelId( goalLevel.getId() );
    goalPayoutLevelFormBean.setSequenceNumber( goalLevel.getSequenceNumber() );
    goalPayoutLevelFormBean.setName( goalLevel.getGoalLevelName() );
    goalPayoutLevelFormBean.setDescription( goalLevel.getGoalLevelDescription() );
    goalPayoutLevelFormBean.setNameKey( goalLevel.getGoalLevelNameKey() );
    goalPayoutLevelFormBean.setDescriptionKey( goalLevel.getGoalLevelDescriptionKey() );
    goalPayoutLevelFormBean.setGoalLevelcmAssetCode( goalLevel.getGoalLevelcmAssetCode() );
    if ( goalLevel.getAchievementAmount() != null )
    {
      goalPayoutLevelFormBean.setAchievementAmount( goalLevel.getAchievementAmount().toString() );
    }
    if ( goalLevel.getAward() != null )
    {
      goalPayoutLevelFormBean.setAward( goalLevel.getAward().toString() );
    }
    if ( goalLevel.getMinimumQualifier() != null )
    {
      goalPayoutLevelFormBean.setMinimumQualifier( goalLevel.getMinimumQualifier().toString() );
    }
    if ( goalLevel.getIncrementalQuantity() != null )
    {
      goalPayoutLevelFormBean.setIncrementalQuantity( goalLevel.getIncrementalQuantity().toString() );
    }
    if ( goalLevel.getBonusAward() != null )
    {
      goalPayoutLevelFormBean.setBonusAward( goalLevel.getBonusAward().toString() );
    }
    if ( goalLevel.getMaximumPoints() != null )
    {
      goalPayoutLevelFormBean.setMaximumPoints( goalLevel.getMaximumPoints().toString() );
    }
    if ( goalLevel.getManagerAward() != null )
    {
      goalPayoutLevelFormBean.setManagerAward( goalLevel.getManagerAward().toString() );
    }
    if ( this.awardType != null )
    {
      goalPayoutLevelFormBean.setAwardType( this.awardType );
    }

    return goalPayoutLevelFormBean;
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
    return toDomainObject( new GoalQuestPromotion() );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( GoalQuestPromotion promotion )
  {
    promotion.setId( this.getPromotionId() );
    promotion.setName( this.getPromotionName() );
    promotion.setVersion( this.getVersion() );
    promotion.setPromotionType( PromotionType.lookup( getPromotionTypeCode() ) );
    promotion.setAchievementRule( AchievementRuleType.lookup( getAchievementRuleTypeCode() ) );
    promotion.setPayoutStructure( PayoutStructure.lookup( getPayoutStructure() ) );
    promotion.setRoundingMethod( RoundingMethod.lookup( getRoundingMethod() ) );
    promotion.setAchievementPrecision( AchievementPrecision.lookup( getAchievementPrecision() ) );
    promotion.setGoalPlanningWorksheet( getGoalPlanningWorksheet() );
    int currentSequence = 1;
    if ( getGoalLevelValueList() != null )
    {
      for ( Iterator<PromotionGoalPayoutLevelFormBean> goalLevelIter = getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
      {
        PromotionGoalPayoutLevelFormBean goalLevelFormBean = goalLevelIter.next();
        if ( StringUtils.isNotBlank( goalLevelFormBean.getName() ) && StringUtils.isNotBlank( goalLevelFormBean.getDescription() ) )
        {
          GoalLevel goalLevel = new GoalLevel();
          goalLevel.setId( goalLevelFormBean.getGoalLevelId() );
          goalLevel.setSequenceNumber( currentSequence++ );
          goalLevel.setGoalLevelNameKey( goalLevelFormBean.getNameKey() );
          goalLevel.setGoalLevelDescriptionKey( goalLevelFormBean.getDescriptionKey() );
          goalLevel.setGoalLevelcmAssetCode( goalLevelFormBean.getGoalLevelcmAssetCode() );
          goalLevel.setAward( getNumericValue( goalLevelFormBean.getAward() ) );
          int precision = AchievementPrecision.lookup( getAchievementPrecision() ).getPrecision();
          int roundingMode = RoundingMethod.lookup( getRoundingMethod() ).getBigDecimalRoundingMode();

          if ( !goalLevelFormBean.getAchievementAmount().equals( "" ) )
          {
            BigDecimal roundedAchievemnetAmount = getNumericValue( goalLevelFormBean.getAchievementAmount() ).setScale( precision, roundingMode );
            goalLevel.setAchievementAmount( roundedAchievemnetAmount );
          }
          else
          {
            goalLevel.setAchievementAmount( getNumericValue( goalLevelFormBean.getAchievementAmount() ) );
          }

          // modified to implement precision as it does for achievement amount and
          // incrementalQuantity.
          if ( !goalLevelFormBean.getMinimumQualifier().equals( "" ) )
          {
            BigDecimal roundedMinimumQualifier = getNumericValue( goalLevelFormBean.getMinimumQualifier() ).setScale( precision, roundingMode );
            goalLevel.setMinimumQualifier( roundedMinimumQualifier );
          }
          else
          {
            goalLevel.setMinimumQualifier( getNumericValue( goalLevelFormBean.getMinimumQualifier() ) );
          }

          // new
          if ( !goalLevelFormBean.getIncrementalQuantity().equals( "" ) )
          {
            BigDecimal roundedIncrementalQuantity = getNumericValue( goalLevelFormBean.getIncrementalQuantity() ).setScale( precision, roundingMode );
            goalLevel.setIncrementalQuantity( roundedIncrementalQuantity );
          }
          else
          {
            goalLevel.setIncrementalQuantity( getNumericValue( goalLevelFormBean.getIncrementalQuantity() ) );
          }
          goalLevel.setMaximumPoints( getIntegerValue( goalLevelFormBean.getMaximumPoints() ) );
          goalLevel.setBonusAward( getIntegerValue( goalLevelFormBean.getBonusAward() ) );
          goalLevel.setManagerAward( getNumericValue( goalLevelFormBean.getManagerAward() ) );
          promotion.addGoalLevel( goalLevel );

        }
      }
    }
    // BugFix 17935
    // promotion.setBaseUnit( getBaseUnit() );
    promotion.setBaseUnitPosition( BaseUnitPosition.lookup( getBaseUnitPosition() ) );

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

  public void resetPromoPayoutGoalLevelList()
  {
    goalLevelValueList = getEmptyValueList( 3 );
  }

  /**
   * resets the value list with empty PromotionGoalPayoutLevelFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  protected List<PromotionGoalPayoutLevelFormBean> getEmptyValueList( int valueListCount )
  {
    List<PromotionGoalPayoutLevelFormBean> valueList = new ArrayList<PromotionGoalPayoutLevelFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      PromotionGoalPayoutLevelFormBean formBean = new PromotionGoalPayoutLevelFormBean();
      formBean.setSequenceNumber( i + 1 );
      valueList.add( formBean );
    }

    return valueList;
  }

  public void addEmptyGoalLevel()
  {
    PromotionGoalPayoutLevelFormBean formBean = new PromotionGoalPayoutLevelFormBean();
    formBean.setSequenceNumber( getGoalLevelValueListSize() + 1 );
    getGoalLevelValueList().add( formBean );
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

  public String getPayoutStructure()
  {
    return payoutStructure;
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
    this.alternateReturnUrl = alternateReturnUrl;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getAchievementRuleTypeCode()
  {
    return achievementRuleTypeCode;
  }

  public void setAchievementRuleTypeCode( String achievementRuleTypeCode )
  {
    this.achievementRuleTypeCode = achievementRuleTypeCode;
  }

  public void setPayoutStructure( String payoutStructure )
  {
    this.payoutStructure = payoutStructure;
  }

  public String getAchievementPrecision()
  {
    return achievementPrecision;
  }

  public void setAchievementPrecision( String achievementPrecision )
  {
    this.achievementPrecision = achievementPrecision;
  }

  public String getRoundingMethod()
  {
    return roundingMethod;
  }

  public void setRoundingMethod( String roundingMethod )
  {
    this.roundingMethod = roundingMethod;
  }

  public List<PromotionGoalPayoutLevelFormBean> getGoalLevelValueList()
  {
    if ( goalLevelValueList == null )
    {
      goalLevelValueList = new ArrayList<PromotionGoalPayoutLevelFormBean>();
    }
    return goalLevelValueList;
  }

  public void setGoalLevelValueList( List<PromotionGoalPayoutLevelFormBean> goalLevelValueList )
  {
    this.goalLevelValueList = goalLevelValueList;
  }

  public int getGoalLevelValueListSize()
  {
    if ( this.goalLevelValueList != null )
    {
      return this.goalLevelValueList.size();
    }
    return 0;
  }

  /**
   * Accessor for the value list
   * 
   * @param index
   * @return Single instance of PromotionGoalPayoutLevelFormBean from the value list
   */
  public PromotionGoalPayoutLevelFormBean getPromoPayoutValueList( int index )
  {
    try
    {
      return goalLevelValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
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

  public String getGoalPlanningWorksheet()
  {
    return goalPlanningWorksheet;
  }

  public void setGoalPlanningWorksheet( String goalPlanningWorksheet )
  {
    this.goalPlanningWorksheet = goalPlanningWorksheet;
  }

  /**
   * @return baseUnit
   */
  public String getBaseUnit()
  {
    return baseUnit;
  }

  /**
   * @param baseUnit
   */
  public void setBaseUnit( String baseUnit )
  {
    this.baseUnit = baseUnit;
  }

  /**
   * @return  baseUnitPosition
   */
  public String getBaseUnitPosition()
  {
    return baseUnitPosition;
  }

  /**
   * @param baseUnitPosition
   */
  public void setBaseUnitPosition( String baseUnitPosition )
  {
    this.baseUnitPosition = baseUnitPosition;
  }

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

  private static PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }
}
