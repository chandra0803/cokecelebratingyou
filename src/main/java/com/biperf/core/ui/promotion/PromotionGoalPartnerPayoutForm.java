/*
 * (c) 2008 BI, Inc. All rights reserved. $Source:
 * /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionGoalPartnerPayoutForm.java,v $
 */

package com.biperf.core.ui.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.PartnerEarnings;
import com.biperf.core.domain.enums.PartnerPayoutStructure;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPartnerPayout;
import com.biperf.core.service.SAO;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;

/**
 * PromotionGoalPartnerPayoutForm.
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
 * <td>reddy</td>
 * <td>Feb 25, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 */
public class PromotionGoalPartnerPayoutForm extends BaseForm
{

  /**
   * 
   */
  public PromotionGoalPartnerPayoutForm()
  {
    super();

  }

  private String partnerPayoutStructure;
  private String partnerEarnings;
  private Long promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionStatus;
  private String alternateReturnUrl;
  private Long version;

  private String partnerAwardAmount;
  private String promotionTypeCode;

  private String awardType;
  private String awardTypeName;
  private List goalLevelValueList;

  private String method = "";

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

    int goalCount = RequestUtils.getOptionalParamInt( request, "goalLevelValueListSize" );
    this.goalLevelValueList = getEmptyValueList( goalCount );

  }

  /**
   * Validate goalLevel fields
   * 
   * @param goalLevelFormBean
   * @return boolean
   */
  private boolean validate( PromotionPartnerPayoutBean goalLevelFormBean, ActionErrors actionErrors )
  {
    boolean valid = true;
    if ( StringUtils.isEmpty( goalLevelFormBean.getPartnerAwardAmount() ) )
    {
      valid = false;
    }
    return valid;
  }

  /**
   * Checks if any of the goalLevelFormBean fields are not blank.
   * 
   * @param goalLevelFormBean
   * @return boolean
   */
  private boolean isNotBlank( PromotionPartnerPayoutBean goalLevelFormBean )
  {
    if ( StringUtils.isNotBlank( goalLevelFormBean.getPartnerAwardAmount() ) )
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
    int blankCountValid = 0;
    for ( Iterator goalLevelIter = getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
    {
      PromotionPartnerPayoutBean goalLevelFormBean = (PromotionPartnerPayoutBean)goalLevelIter.next();
      if ( !isNotBlank( goalLevelFormBean ) )
      {
        blankCountValid += 1;
      }
    }
    if ( blankCountValid > 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.NO_PARTNER_AWARD_AMOUNT" ) );
    }
    // Bug Fix 20174,validate the partner award field for numeric validations.
    int numericCountValid = 0;
    for ( Iterator goalLevelIter = getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
    {
      PromotionPartnerPayoutBean goalLevelFormBean = (PromotionPartnerPayoutBean)goalLevelIter.next();
      if ( !validatePositiveNumeric( goalLevelFormBean.getPartnerAwardAmount() ) )
      {
        numericCountValid += 1;
        break;
      }
    }
    // bugFix 20174,To handle the Whole Number validations of award amount field.
    int wholeNumberValid = 0;
    int decimalFieldValid = 0;
    if ( blankCountValid == 0 && numericCountValid == 0 )
    {
      for ( Iterator goalLevelIter = getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
      {
        PromotionPartnerPayoutBean goalLevelFormBean = (PromotionPartnerPayoutBean)goalLevelIter.next();
        if ( !com.biperf.util.StringUtils.isEmpty( goalLevelFormBean.getPartnerAwardAmount() ) )
        {
          if ( goalLevelFormBean.getPartnerAwardAmount().indexOf( "." ) >= 0 ) // BugFix 20174.
          {
            decimalFieldValid += 1;
          }
          BigDecimal bd = new BigDecimal( goalLevelFormBean.getPartnerAwardAmount() );
          if ( bd.floatValue() % bd.intValue() > 0 )
          {
            wholeNumberValid += 1;
            break;
          }
        }
      }
    }
    if ( blankCountValid == 0 && ( wholeNumberValid > 0 || decimalFieldValid > 0 ) )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.AWARD_AMOUNT_NOT_WHOLENUMBER" ) );
    }
    if ( blankCountValid == 0 && numericCountValid > 0 )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.goalquest.errors.AWARD_NON_PERQS" ) );
    }

    return actionErrors;
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
    if ( promotion.getPartnerPayoutStructure() != null )
    {
      this.partnerPayoutStructure = promotion.getPartnerPayoutStructure().getCode();
    }
    if ( promotion.getPartnerEarnings() != null )
    {
      this.partnerEarnings = promotion.getPartnerEarnings().getCode();
    }
    buildGoalLevels( promotion );
  }

  /**
   * Create the goalLevelValueList based on the promotion goal levels.
   * 
   * @param promotion
   */
  public void buildGoalLevels( GoalQuestPromotion promotion )
  {
    Set goalLevels = promotion.getGoalLevels();
    Set partnerGoalLevels = promotion.getPartnerGoalLevels();
    if ( goalLevels != null && !goalLevels.isEmpty() && ( partnerGoalLevels == null || partnerGoalLevels.size() == 0 ) )
    {
      if ( goalLevelValueList == null || goalLevelValueList.isEmpty() )
      {
        goalLevelValueList = new ArrayList();
      }
      else
      {
        goalLevelValueList.clear();
      }
      for ( Iterator goalLevelIter = goalLevels.iterator(); goalLevelIter.hasNext(); )
      {
        GoalLevel currentGoalLevel = (GoalLevel)goalLevelIter.next();
        goalLevelValueList.add( buildPartnerGoalPayoutBean( currentGoalLevel, false ) );
      }
      resortGoalLevels();
    }
    else if ( goalLevels != null && !goalLevels.isEmpty() && partnerGoalLevels != null && partnerGoalLevels.size() > 0 )
    {
      if ( goalLevelValueList == null || goalLevelValueList.isEmpty() )
      {
        goalLevelValueList = new ArrayList();
      }
      else
      {
        goalLevelValueList.clear();
      }
      for ( Iterator goalLevelIter = partnerGoalLevels.iterator(); goalLevelIter.hasNext(); )
      {
        PromotionPartnerPayout currentGoalLevel = (PromotionPartnerPayout)goalLevelIter.next();
        goalLevelValueList.add( buildPartnerGoalPayoutBean( currentGoalLevel, true ) );
      }
      resortGoalLevels();
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
   * 
   * @param goalLevel
   * @return PromotionGoalPayoutLevelFormBean
   */
  private PromotionPartnerPayoutBean buildPartnerGoalPayoutBean( AbstractGoalLevel goalLevel, boolean isPartnerGoallevel )
  {
    PromotionPartnerPayoutBean promotionPartnerPayoutBean = new PromotionPartnerPayoutBean();
    if ( isPartnerGoallevel )
    {
      PromotionPartnerPayout partnerGoallevel = (PromotionPartnerPayout)goalLevel;
      promotionPartnerPayoutBean.setGoalLevelId( partnerGoallevel.getId() );
      promotionPartnerPayoutBean.setSequenceNumber( partnerGoallevel.getSequenceNumber() );
      promotionPartnerPayoutBean.setName( partnerGoallevel.getGoalLevelName() );
      promotionPartnerPayoutBean.setDescription( partnerGoallevel.getGoalLevelDescription() );
      promotionPartnerPayoutBean.setNameKey( partnerGoallevel.getGoalLevelNameKey() );
      promotionPartnerPayoutBean.setDescriptionKey( partnerGoallevel.getGoalLevelDescriptionKey() );
      promotionPartnerPayoutBean.setGoalLevelcmAssetCode( partnerGoallevel.getGoalLevelcmAssetCode() );
      if ( partnerGoallevel.getPartnerAwardAmount() != null )
      {
        promotionPartnerPayoutBean.setPartnerAwardAmount( partnerGoallevel.getPartnerAwardAmount().toString() );
      }
    }
    else
    {
      GoalLevel partnerGoallevel = (GoalLevel)goalLevel;
      promotionPartnerPayoutBean.setSequenceNumber( partnerGoallevel.getSequenceNumber() );
      promotionPartnerPayoutBean.setName( partnerGoallevel.getGoalLevelName() );
      promotionPartnerPayoutBean.setDescription( partnerGoallevel.getGoalLevelDescription() );
      promotionPartnerPayoutBean.setNameKey( goalLevel.getGoalLevelNameKey() );
      promotionPartnerPayoutBean.setDescriptionKey( goalLevel.getGoalLevelDescriptionKey() );
      promotionPartnerPayoutBean.setGoalLevelcmAssetCode( goalLevel.getGoalLevelcmAssetCode() );
      promotionPartnerPayoutBean.setPartnerAwardAmount( null );
    }
    return promotionPartnerPayoutBean;
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
    promotion.setPartnerPayoutStructure( PartnerPayoutStructure.lookup( getPartnerPayoutStructure() ) );
    promotion.setPartnerEarnings( PartnerEarnings.lookup( getPartnerEarnings() ) );
    int currentSequence = 1;
    if ( getGoalLevelValueList() != null )
    {
      for ( Iterator goalLevelIter = getGoalLevelValueList().iterator(); goalLevelIter.hasNext(); )
      {
        PromotionPartnerPayoutBean promotionPartnerPayoutBean = (PromotionPartnerPayoutBean)goalLevelIter.next();
        if ( StringUtils.isNotBlank( promotionPartnerPayoutBean.getNameKey() ) && StringUtils.isNotBlank( promotionPartnerPayoutBean.getDescriptionKey() ) )
        {
          PromotionPartnerPayout promotionPartnerPayout = new PromotionPartnerPayout();
          promotionPartnerPayout.setSequenceNumber( currentSequence++ );
          promotionPartnerPayout.setGoalLevelNameKey( promotionPartnerPayoutBean.getNameKey() );
          promotionPartnerPayout.setGoalLevelDescriptionKey( promotionPartnerPayoutBean.getDescriptionKey() );
          promotionPartnerPayout.setGoalLevelcmAssetCode( promotionPartnerPayoutBean.getGoalLevelcmAssetCode() );
          promotionPartnerPayout.setId( promotionPartnerPayoutBean.getGoalLevelId() );
          promotionPartnerPayout.setPartnerAwardAmount( new BigDecimal( promotionPartnerPayoutBean.getPartnerAwardAmount() ) );
          promotion.addPartnerGoalLevel( promotionPartnerPayout );
        }
      }
    }
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

  public void resetPromoPayoutGoalLevelList( GoalQuestPromotion promotion )
  {
    goalLevelValueList = getEmptyValueList( 3 );
  }

  /**
   * resets the value list with empty PromotionGoalPayoutLevelFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  private List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      PromotionPartnerPayoutBean formBean = new PromotionPartnerPayoutBean();
      formBean.setSequenceNumber( i + 1 );
      valueList.add( formBean );
    }

    return valueList;
  }

  public void addEmptyGoalLevel()
  {
    PromotionPartnerPayoutBean formBean = new PromotionPartnerPayoutBean();
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

  public List getGoalLevelValueList()
  {
    if ( goalLevelValueList == null )
    {
      goalLevelValueList = new ArrayList();
    }
    return goalLevelValueList;
  }

  public void setGoalLevelValueList( List goalLevelValueList )
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
  public PromotionPartnerPayoutBean getPromoPayoutValueList( int index )
  {
    try
    {
      return (PromotionPartnerPayoutBean)goalLevelValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public String getPartnerAwardAmount()
  {
    return partnerAwardAmount;
  }

  public void setPartnerAwardAmount( String partnerAwardAmount )
  {
    this.partnerAwardAmount = partnerAwardAmount;
  }

  public String getPartnerEarnings()
  {
    return partnerEarnings;
  }

  public void setPartnerEarnings( String partnerEarnings )
  {
    this.partnerEarnings = partnerEarnings;
  }

  public String getPartnerPayoutStructure()
  {
    return partnerPayoutStructure;
  }

  public void setPartnerPayoutStructure( String partnerPayoutStructure )
  {
    this.partnerPayoutStructure = partnerPayoutStructure;
  }

  public String getAwardTypeName()
  {
    return awardTypeName;
  }

  public void setAwardTypeName( String awardTypeName )
  {
    this.awardTypeName = awardTypeName;
  }

}
