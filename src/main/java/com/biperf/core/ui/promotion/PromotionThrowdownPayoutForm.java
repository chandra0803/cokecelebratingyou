
package com.biperf.core.ui.promotion;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.RankingsPayoutType;
import com.biperf.core.domain.enums.ThrowdownAchievementPrecision;
import com.biperf.core.domain.enums.ThrowdownRoundingMethod;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.domain.promotion.Division;
import com.biperf.core.domain.promotion.DivisionPayout;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionBillCode;
import com.biperf.core.domain.promotion.Round;
import com.biperf.core.domain.promotion.StackStandingPayout;
import com.biperf.core.domain.promotion.StackStandingPayoutGroup;
import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.service.SAO;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.service.throwdown.ThrowdownService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.DateFormatterUtil;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PromotionThrowdownPayoutForm extends BaseForm
{

  private static final long serialVersionUID = 1L;

  private Long promotionId;
  private String promotionName;
  private String promotionTypeName;
  private String promotionStatus;
  private String promotionTypeCode;
  private String promotionStartDate;
  private String promotionEndDate;
  private String daysPriorToGenerateMatches;

  private String awardType;
  private String awardTypeName;

  private Long version;
  private String method = "";

  private String baseUnit;
  private String baseUnitPosition = BaseUnitPosition.UNIT_AFTER;
  private String achievementPrecision = ThrowdownAchievementPrecision.ZERO;
  private String roundingMethod = ThrowdownRoundingMethod.STANDARD;

  private boolean billCodesActive;
  private String billCode1;
  private String customValue1;
  private String billCode2;
  private String customValue2;
  private String billCode3;
  private String customValue3;
  private String billCode4;
  private String customValue4;
  private String billCode5;
  private String customValue5;
  private String billCode6;
  private String customValue6;
  private String billCode7;
  private String customValue7;
  private String billCode8;
  private String customValue8;
  private String billCode9;
  private String customValue9;
  private String billCode10;
  private String customValue10;

  private String numberOfRounds;
  private String numberOfDayPerRound;
  private String startDateForFirstRound = DateUtils.displayDateFormatMask;

  private List<PromotionStackStandingPayoutGroupFormBean> promoStackStandingPayoutGroupValueList;
  private String stackStandingGroupEditId;

  private List<DivisionFormBean> divisionValueList;
  private String divisionEditId;

  private List<RoundFormBean> roundsList;

  public static final String CUSTOM_VALUE = "customValue";

  /**
   * Reset all properties to their default values.
   * 
   * @param mapping The mapping used to select this instance
   * @param request The servlet request we are processing
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    resetDivisionPayout( mapping, request );
    resetStackStandingPayout( mapping, request );
    resetRounds( mapping, request );
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

    if ( this.billCodesActive )
    {
      boolean customBillCodeMissing = false;
      int missingBillCodesCount = 0;

      if ( this.billCode1 != null && !this.billCode1.equals( "" ) )
      {
        if ( this.billCode1.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue1.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }
      if ( this.billCode2 != null && !this.billCode2.equals( "" ) )
      {
        if ( this.billCode2.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue2.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode3 != null && !this.billCode3.equals( "" ) )
      {
        if ( this.billCode3.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue3.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode4 != null && !this.billCode4.equals( "" ) )
      {
        if ( this.billCode4.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue4.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode5 != null && !this.billCode5.equals( "" ) )
      {
        if ( this.billCode5.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue5.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode6 != null && !this.billCode6.equals( "" ) )
      {
        if ( this.billCode6.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue6.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode7 != null && !this.billCode7.equals( "" ) )
      {
        if ( this.billCode7.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue7.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode8 != null && !this.billCode8.equals( "" ) )
      {
        if ( this.billCode8.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue8.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode9 != null && !this.billCode9.equals( "" ) )
      {
        if ( this.billCode9.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue9.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( this.billCode10 != null && !this.billCode10.equals( "" ) )
      {
        if ( this.billCode10.equalsIgnoreCase( CUSTOM_VALUE ) && this.customValue10.equals( "" ) )
        {
          customBillCodeMissing = true;
        }
      }
      else
      {
        missingBillCodesCount++;
      }

      if ( missingBillCodesCount == 10 )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.bill.code.NO_BILL_CODES" ) );
      }
      else if ( customBillCodeMissing )
      {
        actionErrors.add( "promotionBillCodes",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.bill.code.CUSTOM_BILL_CODE" ) ) );
      }
    }

    try
    {
      if ( this.numberOfRounds == null || this.numberOfRounds.equals( "" ) )
      {
        actionErrors.add( "numberOfRounds",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.payout.throwdown.NUMBER_OF_ROUNDS" ) ) );
      }
      else
      {
        if ( Integer.parseInt( numberOfRounds ) <= 0 || Integer.parseInt( numberOfRounds ) > 99 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "system.errors.RANGE", CmsResourceBundle.getCmsBundle().getString( "promotion.payout.throwdown.NUMBER_OF_ROUNDS" ), 1, 99 ) );
        }
      }
    }
    catch( NumberFormatException e )
    {
      actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "promotion.payout.throwdown.NUMBER_OF_ROUNDS" ) ) );
    }

    try
    {
      if ( this.numberOfDayPerRound == null || this.numberOfDayPerRound.equals( "" ) )
      {
        actionErrors.add( "numberOfDayPerRound",
                          new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "promotion.payout.throwdown.NUMBER_OF_DAYS_PER_ROUND" ) ) );
      }
      else
      {
        if ( Integer.parseInt( numberOfDayPerRound ) <= 0 || Integer.parseInt( numberOfDayPerRound ) > 999 )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE,
                            new ActionMessage( "system.errors.RANGE", CmsResourceBundle.getCmsBundle().getString( "promotion.payout.throwdown.NUMBER_OF_DAYS_PER_ROUND" ), 1, 999 ) );
        }
        else if ( Integer.parseInt( numberOfDayPerRound ) < Integer.parseInt( daysPriorToGenerateMatches ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.ROUND_LENGTH_RANGE", daysPriorToGenerateMatches ) );
        }
      }
    }
    catch( NumberFormatException e )
    {
      actionErrors.add( ActionMessages.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID, CmsResourceBundle.getCmsBundle().getString( "promotion.payout.throwdown.NUMBER_OF_DAYS_PER_ROUND" ) ) );
    }

    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    String startKey = "promotion.payout.throwdown.START_DATE_FOR_FIRST_ROUND";

    String promoStartDateForFirstRound = request.getParameter( "startDateForFirstRound" );
    boolean checkPromoStatus = true;

    if ( this.promotionStatus.equals( "live" ) || this.promotionStatus.equals( "complete" ) || this.promotionStatus.equals( "expired" ) )
    {
      checkPromoStatus = false;
    }

    // Make sure its not empty
    if ( promoStartDateForFirstRound == null || promoStartDateForFirstRound.length() == 0 )
    {
      actionErrors.add( "startDateForFirstRound", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( startKey ) ) );
    }
    else
    {
      // Now validate the date
      try
      {
        sdf.parse( promoStartDateForFirstRound );
      }
      catch( ParseException e )
      {
        actionErrors.add( "startDateForFirstRound", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( startKey ) ) );
      }

      if ( DateUtils.toDate( promoStartDateForFirstRound ).before( DateUtils.toDate( promotionStartDate ) ) )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.START_DATE_RANGE", promotionStartDate ) );
      }
      else if ( DateUtils.toDate( promoStartDateForFirstRound ).before( DateUtils.getCurrentDateTrimmed() ) && checkPromoStatus )
      {
        actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.ROUND_DATE_RANGE", DateUtils.getCurrentDateTrimmed() ) );
      }
      String lastRoundEndDate = getLastRoundEndDate();
      if ( lastRoundEndDate != null )
      {
        if ( DateUtils.toDate( lastRoundEndDate ).after( DateUtils.toDate( promotionEndDate ) ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.END_DATE_RANGE", promotionEndDate ) );
        }
      }
    }

    actionErrors = validateDivision( actionMapping, request, actionErrors );
    actionErrors = validateDivisionPayouts( actionMapping, request, actionErrors );
    if ( isDivisionAlreadySelected() )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.DIVISION_UNIQUE_ERROR" ) );
    }
    if ( isOutcomeAlreadySelectedForDivision() )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.OUTCOME_UNIQUE_ERROR" ) );
    }
    actionErrors = validateStackStandingPayoutStandings( actionMapping, request, actionErrors );
    actionErrors = validateStackStandingPayouts( actionMapping, request, actionErrors );
    if ( isStackStandingNodeTypeAlreadySelected() )
    {
      actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NODE_TYPE_UNIQUE_ERROR" ) );
    }

    return actionErrors;
  }

  private boolean validatePositiveNumericMoreThanZero( String string )
  {
    boolean valid = validatePositiveNumeric( string );
    if ( valid )
    {
      return Integer.parseInt( string ) > 0;
    }
    return false;
  }

  private boolean validatePositiveNumeric( String string )
  {
    return string != null && !string.equals( "" ) && string.matches( "\\d*" );
  }

  /**
   * Loads the promotion and any goal levels
   * 
   * @param promo
   */
  public void loadPromotion( Promotion promo )
  {
    ThrowdownPromotion promotion = (ThrowdownPromotion)promo;

    this.promotionId = promotion.getId();
    this.promotionName = promotion.getName();
    this.promotionTypeName = promotion.getPromotionType().getName();
    this.promotionTypeCode = promotion.getPromotionType().getCode();
    this.promotionStatus = promotion.getPromotionStatus().getCode();
    this.promotionStartDate = DateUtils.toDisplayString( promotion.getSubmissionStartDate() );
    this.promotionEndDate = DateUtils.toDisplayString( promotion.getSubmissionEndDate() );
    this.daysPriorToGenerateMatches = Integer.toString( promotion.getDaysPriorToRoundStartSchedule() );
    this.version = promotion.getVersion();
    if ( promotion.getAchievementPrecision() != null )
    {
      this.achievementPrecision = promotion.getAchievementPrecision().getCode();
    }
    if ( promotion.getRoundingMethod() != null )
    {
      this.roundingMethod = promotion.getRoundingMethod().getCode();
    }
    if ( promotion.getAwardType() != null )
    {
      this.awardTypeName = promotion.getAwardType().getName();
      this.awardType = promotion.getAwardType().getCode();
    }
    this.baseUnit = promotion.getBaseUnitText();
    if ( promotion.getBaseUnitPosition() != null )
    {
      this.baseUnitPosition = promotion.getBaseUnitPosition().getCode();
    }
    this.billCodesActive = promotion.isBillCodesActive();

    List<PromotionBillCode> promoBillCodes = null;

    if ( this.billCodesActive )
    {
      promoBillCodes = new ArrayList<PromotionBillCode>();

      for ( PromotionBillCode promoBillCode : promotion.getPromotionBillCodes() )
      {
        promoBillCodes.add( promoBillCode );
      }
      loadPromotionBillCodes( promoBillCodes );
    }

    this.numberOfRounds = new Integer( promotion.getNumberOfRounds() ).toString();
    this.numberOfDayPerRound = new Integer( promotion.getLengthOfRound() ).toString();
    if ( promotion.getHeadToHeadStartDate() != null )
    {
      this.startDateForFirstRound = DateUtils.toDisplayString( promotion.getHeadToHeadStartDate() );
    }

    buildDivisions( promotion );
    buildStackStandingGroups( promotion );
    buildRounds( promotion );
  }

  private void loadPromotionBillCodes( List<PromotionBillCode> promotionBillCodes )
  {
    if ( promotionBillCodes != null && promotionBillCodes.size() > 0 )
    {
      Iterator promotionBillCodesList = promotionBillCodes.iterator();
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode1 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode1 != null )
        {
          this.billCode1 = promotionBillCode1.getBillCode();
          this.customValue1 = promotionBillCode1.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode2 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode2 != null )
        {
          this.billCode2 = promotionBillCode2.getBillCode();
          this.customValue2 = promotionBillCode2.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode3 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode3 != null )
        {
          this.billCode3 = promotionBillCode3.getBillCode();
          this.customValue3 = promotionBillCode3.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode4 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode4 != null )
        {
          this.billCode4 = promotionBillCode4.getBillCode();
          this.customValue4 = promotionBillCode4.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode5 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode5 != null )
        {
          this.billCode5 = promotionBillCode5.getBillCode();
          this.customValue5 = promotionBillCode5.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode6 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode6 != null )
        {
          this.billCode6 = promotionBillCode6.getBillCode();
          this.customValue6 = promotionBillCode6.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode7 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode7 != null )
        {
          this.billCode7 = promotionBillCode7.getBillCode();
          this.customValue7 = promotionBillCode7.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode8 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode8 != null )
        {
          this.billCode8 = promotionBillCode8.getBillCode();
          this.customValue8 = promotionBillCode8.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode9 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode9 != null )
        {
          this.billCode9 = promotionBillCode9.getBillCode();
          this.customValue9 = promotionBillCode9.getCustomValue();
        }
      }
      if ( promotionBillCodesList.hasNext() )
      {
        PromotionBillCode promotionBillCode10 = (PromotionBillCode)promotionBillCodesList.next();
        if ( promotionBillCode10 != null )
        {
          this.billCode10 = promotionBillCode10.getBillCode();
          this.customValue10 = promotionBillCode10.getCustomValue();
        }
      }
    }
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
    return toDomainObject( new ThrowdownPromotion() );
  }

  /**
   * Creates a detatched Promotion Domain Object that will later be synchronized with a looked up
   * promotion object in the service
   * 
   * @param promotion
   * @return Promotion
   */
  public Promotion toDomainObject( ThrowdownPromotion promotion )
  {
    promotion.setId( this.getPromotionId() );
    promotion.setName( this.getPromotionName() );
    promotion.setVersion( this.getVersion() );
    promotion.setPromotionType( PromotionType.lookup( getPromotionTypeCode() ) );

    promotion.setBaseUnitPosition( BaseUnitPosition.lookup( getBaseUnitPosition() ) );

    promotion.setBillCodesActive( this.billCodesActive );
    if ( this.billCodesActive )
    {
      promotion.setPromotionBillCodes( getPromoBillCodeList( promotion ) );
    }

    promotion.setNumberOfRounds( new Integer( this.numberOfRounds ) );
    promotion.setLengthOfRound( new Integer( this.numberOfDayPerRound ) );
    promotion.setAchievementPrecision( ThrowdownAchievementPrecision.lookup( getAchievementPrecision() ) );
    promotion.setRoundingMethod( ThrowdownRoundingMethod.lookup( getRoundingMethod() ) );

    if ( this.startDateForFirstRound != null && this.startDateForFirstRound.length() > 0 )
    {
      promotion.setHeadToHeadStartDate( DateUtils.toDate( this.startDateForFirstRound ) );
    }

    promotion = (ThrowdownPromotion)toDivisionDomainObject( promotion );
    promotion = (ThrowdownPromotion)toStackStandingDomainObject( promotion );

    return promotion;
  }

  private List<PromotionBillCode> getPromoBillCodeList( Promotion promotion )
  {
    List<PromotionBillCode> promoBillCodes = new ArrayList<PromotionBillCode>();

    if ( this.billCode1 != null && !this.billCode1.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 0 ), this.billCode1, this.customValue1 ) );
    }
    if ( this.billCode2 != null && !this.billCode2.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 1 ), this.billCode2, this.customValue2 ) );
    }
    if ( this.billCode3 != null && !this.billCode3.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 2 ), this.billCode3, this.customValue3 ) );
    }
    if ( this.billCode4 != null && !this.billCode4.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 3 ), this.billCode4, this.customValue4 ) );
    }
    if ( this.billCode5 != null && !this.billCode5.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 4 ), this.billCode5, this.customValue5 ) );
    }
    if ( this.billCode6 != null && !this.billCode6.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 5 ), this.billCode6, this.customValue6 ) );
    }
    if ( this.billCode7 != null && !this.billCode7.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 6 ), this.billCode7, this.customValue7 ) );
    }
    if ( this.billCode8 != null && !this.billCode8.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 7 ), this.billCode8, this.customValue8 ) );
    }
    if ( this.billCode9 != null && !this.billCode9.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 8 ), this.billCode9, this.customValue9 ) );
    }
    if ( this.billCode10 != null && !this.billCode10.equals( "" ) )
    {
      promoBillCodes.add( new PromotionBillCode( promotion, new Long( 9 ), this.billCode10, this.customValue10 ) );
    }
    return promoBillCodes;
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

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
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

  public boolean isBillCodesActive()
  {
    return billCodesActive;
  }

  public void setBillCodesActive( boolean billCodesActive )
  {
    this.billCodesActive = billCodesActive;
  }

  public String getNumberOfRounds()
  {
    return numberOfRounds;
  }

  public void setNumberOfRounds( String numberOfRounds )
  {
    this.numberOfRounds = numberOfRounds;
  }

  public String getNumberOfDayPerRound()
  {
    return numberOfDayPerRound;
  }

  public void setNumberOfDayPerRound( String numberOfDayPerRound )
  {
    this.numberOfDayPerRound = numberOfDayPerRound;
  }

  public String getStartDateForFirstRound()
  {
    return startDateForFirstRound;
  }

  public void setStartDateForFirstRound( String startDateForFirstRound )
  {
    this.startDateForFirstRound = startDateForFirstRound;
  }

  public List<PromotionStackStandingPayoutGroupFormBean> getPromoStackStandingPayoutGroupValueList()
  {
    return promoStackStandingPayoutGroupValueList;
  }

  public void setPromoStackStandingPayoutGroupValueList( List<PromotionStackStandingPayoutGroupFormBean> promoStackStandingPayoutGroupValueList )
  {
    this.promoStackStandingPayoutGroupValueList = promoStackStandingPayoutGroupValueList;
  }

  public String getStackStandingGroupEditId()
  {
    return stackStandingGroupEditId;
  }

  public void setStackStandingGroupEditId( String stackStandingGroupEditId )
  {
    this.stackStandingGroupEditId = stackStandingGroupEditId;
  }

  public List<DivisionFormBean> getDivisionValueList()
  {
    return divisionValueList;
  }

  public void setDivisionValueList( List<DivisionFormBean> divisionValueList )
  {
    this.divisionValueList = divisionValueList;
  }

  public String getDivisionEditId()
  {
    return divisionEditId;
  }

  public void setDivisionEditId( String divisionEditId )
  {
    this.divisionEditId = divisionEditId;
  }

  public List<RoundFormBean> getRoundsList()
  {
    return roundsList;
  }

  public void setRoundsList( List<RoundFormBean> roundsList )
  {
    this.roundsList = roundsList;
  }

  public String getPromotionStartDate()
  {
    return promotionStartDate;
  }

  public void setPromotionStartDate( String promotionStartDate )
  {
    this.promotionStartDate = promotionStartDate;
  }

  public String getPromotionEndDate()
  {
    return promotionEndDate;
  }

  public void setPromotionEndDate( String promotionEndDate )
  {
    this.promotionEndDate = promotionEndDate;
  }

  public String getDaysPriorToGenerateMatches()
  {
    return daysPriorToGenerateMatches;
  }

  public void setDaysPriorToGenerateMatches( String daysPriorToGenerateMatches )
  {
    this.daysPriorToGenerateMatches = daysPriorToGenerateMatches;
  }

  private void resetStackStandingPayout( ActionMapping mapping, HttpServletRequest request )
  {
    int groupCountStackStanding = RequestUtils.getOptionalParamInt( request, "promoStackStandingPayoutGroupValueListCount" );
    promoStackStandingPayoutGroupValueList = getStackStandingEmptyValueList( groupCountStackStanding );
    if ( groupCountStackStanding > 0 )
    {
      // Look for groupId's
      for ( int i = 0; i < groupCountStackStanding; i++ )
      {
        String key = "promoStackStandingPayoutGroupValueList[" + i + "].guid";
        String guid = RequestUtils.getOptionalParamString( request, key );
        if ( guid != null && guid.length() > 0 )
        {
          PromotionStackStandingPayoutGroupFormBean groupStackStanding = promoStackStandingPayoutGroupValueList.get( i );
          groupStackStanding.setGuid( guid );
          String payoutSizeId = "groupStackStandingPayoutSize" + guid;
          int payoutSize = RequestUtils.getOptionalParamInt( request, payoutSizeId );
          groupStackStanding.setPromoStackStandingPayoutValueList( getStackStandingEmptyPayoutValueList( payoutSize ) );
        }
      }
    }
    else
    {
      promoStackStandingPayoutGroupValueList = getStackStandingEmptyValueList( 1 );
    }
  }

  private void resetDivisionPayout( ActionMapping mapping, HttpServletRequest request )
  {
    int countDivision = RequestUtils.getOptionalParamInt( request, "divisionValueListCount" );
    divisionValueList = getDivisionEmptyValueList( countDivision );
    if ( countDivision > 0 )
    {
      for ( int i = 0; i < countDivision; i++ )
      {
        String key = "divisionValueList[" + i + "].guid";
        String guid = RequestUtils.getOptionalParamString( request, key );
        if ( guid != null && guid.length() > 0 )
        {
          DivisionFormBean div = divisionValueList.get( i );
          div.setGuid( guid );
          String payoutSizeId = "divisionPayoutSize" + guid;
          int payoutSize = RequestUtils.getOptionalParamInt( request, payoutSizeId );
          div.setDivisionPayoutValueList( getDivisionEmptyPayoutValueList( payoutSize ) );
        }
      }
    }
    else
    {
      divisionValueList = getDivisionEmptyValueList( 1 );
    }
  }

  private void resetRounds( ActionMapping mapping, HttpServletRequest request )
  {
    int countRounds = RequestUtils.getOptionalParamInt( request, "roundListCount" );
    roundsList = geRoundEmptyValueList( countRounds );
  }

  public ActionErrors validateStackStandingPayoutStandings( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    // even if there is one error in validating from ot Standing there is no point in checking up
    // overlap
    boolean checkStandingOverlap = true;
    boolean fromStandingLessToStandingError = false;
    boolean allExists = false;

    if ( this.getPromoStackStandingPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator<PromotionStackStandingPayoutGroupFormBean> promoStackStandingPayoutGroupValueListIter = this.getPromoStackStandingPayoutGroupValueList()
          .iterator(); promoStackStandingPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackStandingPayoutGroupFormBean promotionStackStandingPayoutGroupFormBean = (PromotionStackStandingPayoutGroupFormBean)promoStackStandingPayoutGroupValueListIter.next();
        if ( ! ( null == promotionStackStandingPayoutGroupFormBean.getNodeTypeId() ) && promotionStackStandingPayoutGroupFormBean.getNodeTypeId().equals( "" ) )
        {
          allExists = true;
        }
        if ( promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType() != null
            && promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType().equals( RankingsPayoutType.RANKINGS_AND_PAYOUT ) )
        {
          for ( int i = 0; i < promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueListCount(); i++ )
          {
            PromotionStackStandingPayoutFormBean promoStackStandingPayoutValueBean = promotionStackStandingPayoutGroupFormBean.getPromoPayoutValue( i );

            boolean validFromStanding = validatePositiveNumericMoreThanZero( promoStackStandingPayoutValueBean.getFromStanding() );
            boolean validToStanding = validatePositiveNumericMoreThanZero( promoStackStandingPayoutValueBean.getToStanding() );

            if ( validFromStanding && validToStanding && !fromStandingLessToStandingError )
            {
              if ( Integer.parseInt( promoStackStandingPayoutValueBean.getFromStanding() ) > Integer.parseInt( promoStackStandingPayoutValueBean.getToStanding() ) )
              {
                // from Standing cant be more than toStanding - ERROR
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
                fromStandingLessToStandingError = true;
                checkStandingOverlap = false;
                break;
              }
            }
            else
            {
              checkStandingOverlap = false;
              // one of them is or both are non numeric - ERROR
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.RANK_NUMERIC_ERROR" ) );
              break;
            }
          }

          if ( !checkStandingOverlap )
          {
            break;
          }
        }
      }

      if ( checkStandingOverlap )
      {
        validateStackStandingOverlap( errors );
      }
    }
    if ( !allExists )
    {
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.errors.ALL_RANKING_REQUIRED" ) );
    }

    return errors;
  }

  public ActionErrors validateDivision( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    if ( this.getDivisionValueListCount() > 0 )
    {
      for ( Iterator<DivisionFormBean> divValueListIter = this.getDivisionValueList().iterator(); divValueListIter.hasNext(); )
      {
        DivisionFormBean divFormBean = (DivisionFormBean)divValueListIter.next();

        boolean validMinimumQualifier = validateMinimumQualifier( divFormBean.getMinimumQualifier() );

        if ( !validMinimumQualifier && divFormBean.getDivisionPayoutValueListCount() > 0 )
        {
          errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.DIVISION_MINIMUM_QUALIFIER_NUMERIC_ERROR" ) );
          break;
        }
      }
    }

    return errors;
  }

  public boolean validateMinimumQualifier( String minimumQualifier )
  {
    BigDecimal numericValue = getNumericValue( minimumQualifier );
    return numericValue != null;
  }

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

  public ActionErrors validateStackStandingPayouts( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    boolean validPayout = true;
    // even if there is one error in validating payouts there is no point in checking
    if ( this.getPromoStackStandingPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator<PromotionStackStandingPayoutGroupFormBean> promoStackStandingPayoutGroupValueListIter = this.getPromoStackStandingPayoutGroupValueList()
          .iterator(); promoStackStandingPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackStandingPayoutGroupFormBean promotionStackStandingPayoutGroupFormBean = (PromotionStackStandingPayoutGroupFormBean)promoStackStandingPayoutGroupValueListIter.next();
        if ( promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType() != null
            && promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType().equals( RankingsPayoutType.RANKINGS_AND_PAYOUT ) )
        {
          for ( int i = 0; i < promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueListCount(); i++ )
          {
            PromotionStackStandingPayoutFormBean promoStackStandingPayoutValueBean = promotionStackStandingPayoutGroupFormBean.getPromoPayoutValue( i );
            validPayout = validatePositiveNumeric( promoStackStandingPayoutValueBean.getPayoutAmount() );
            if ( !validPayout )
            {
              // Invalid payout type non numeric - ERROR
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.NO_STACKRANK_PAYOUT_ERROR" ) );
              break;
            }
          }
          if ( !validPayout )
          {
            break;
          }
        }
      }
    }

    return errors;
  }

  public ActionErrors validateDivisionPayouts( ActionMapping actionMapping, HttpServletRequest request, ActionErrors errors )
  {
    boolean validPayout = true;

    List<Division> divisions = getThrowdownService().getDivisionsByPromotionId( getPromotionId() );
    List<String> outcomes = new ArrayList<String>();
    if ( this.getDivisionValueListCount() > 0 )
    {
      for ( Iterator<DivisionFormBean> divisionValueListIter = this.getDivisionValueList().iterator(); divisionValueListIter.hasNext(); )
      {
        DivisionFormBean divisionFormBean = (DivisionFormBean)divisionValueListIter.next();
        for ( int i = 0; i < divisionFormBean.getDivisionPayoutValueListCount(); i++ )
        {
          DivisionPayoutFormBean divisionPayoutValueBean = divisionFormBean.getDivisionPayoutValue( i );
          String outcome = divisionPayoutValueBean.getOutcomeCode();
          if ( outcome.equals( MatchTeamOutcomeType.WIN ) )
          {
            outcomes.add( outcome );
          }
          validPayout = validatePositiveNumeric( divisionPayoutValueBean.getPayoutAmount() );
          if ( !validPayout )
          {
            // Invalid payout type non numeric - ERROR
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.DIVISION_PAYOUT_NUMERIC_ERROR" ) );
            break;
          }
        }
        if ( !validPayout )
        {
          break;
        }
      }
    }

    if ( !divisions.isEmpty() && outcomes.size() < divisions.size() )
    {
      // when divisions exists, each division should have pay out at lest for WIN outcome.
      errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.ALL_DIVISIONS_NEED_WIN_PAYOUT_ERROR" ) );
    }

    return errors;
  }

  private boolean isStackStandingNodeTypeAlreadySelected()
  {

    if ( this.getPromoStackStandingPayoutGroupValueListCount() > 0 )
    {
      Set<String> nodeTypeIds = new HashSet<String>();
      int totalGroups = 0; // which has pay out row added in UI
      for ( Iterator<PromotionStackStandingPayoutGroupFormBean> divPayoutValueListIter = this.getPromoStackStandingPayoutGroupValueList().iterator(); divPayoutValueListIter.hasNext(); )
      {
        PromotionStackStandingPayoutGroupFormBean promoStackStandingPayoutGroupFormBean = (PromotionStackStandingPayoutGroupFormBean)divPayoutValueListIter.next();
        if ( promoStackStandingPayoutGroupFormBean.getRankingsPayoutType() != null && ( promoStackStandingPayoutGroupFormBean.getRankingsPayoutType().equals( RankingsPayoutType.RANKINGS_ONLY )
            || promoStackStandingPayoutGroupFormBean.getRankingsPayoutType().equals( RankingsPayoutType.RANKINGS_AND_PAYOUT )
                && !promoStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueList().isEmpty() ) )
        {
          totalGroups = totalGroups + 1;
          nodeTypeIds.add( promoStackStandingPayoutGroupFormBean.getNodeTypeId() );
        }
      }
      if ( nodeTypeIds.size() < totalGroups )
      {
        return true;
      }
    }
    return false;
  }

  private boolean isDivisionAlreadySelected()
  {
    if ( this.getDivisionValueListCount() > 0 )
    {
      Set<Long> divIds = new HashSet<Long>();
      int totalDivs = 0; // which has pay out row added in UI
      for ( Iterator<DivisionFormBean> divValueListIter = this.getDivisionValueList().iterator(); divValueListIter.hasNext(); )
      {
        DivisionFormBean divFormBean = (DivisionFormBean)divValueListIter.next();
        if ( !divFormBean.getDivisionPayoutValueList().isEmpty() )
        {
          totalDivs = totalDivs + 1;
          divIds.add( divFormBean.getDivisionId() );
        }
        divIds.add( divFormBean.getDivisionId() );
      }
      if ( divIds.size() < totalDivs )
      {
        return true;
      }
    }
    return false;
  }

  private ActionErrors validateStackStandingOverlap( ActionErrors errors )
  {
    boolean isOverlap = false;

    if ( this.getPromoStackStandingPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator<PromotionStackStandingPayoutGroupFormBean> promoStackStandingPayoutGroupValueListIter = this.getPromoStackStandingPayoutGroupValueList()
          .iterator(); promoStackStandingPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackStandingPayoutGroupFormBean promotionStackStandingPayoutGroupFormBean = (PromotionStackStandingPayoutGroupFormBean)promoStackStandingPayoutGroupValueListIter.next();
        if ( promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType() == null || promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType().equals( RankingsPayoutType.RANKINGS_ONLY ) )
        {
          continue;
        }
        int currFromStanding = 0;
        int currToStanding = 0;

        if ( promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueList() != null && promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueList().size() > 0 )
        {
          PropertyComparator.sort( promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueList(), new MutableSortDefinition( "intFromStanding", true, true ) );
          PromotionStackStandingPayoutFormBean promoStackStandingPayoutValueBean = promotionStackStandingPayoutGroupFormBean.getPromoPayoutValue( 0 );

          currFromStanding = Integer.parseInt( promoStackStandingPayoutValueBean.getFromStanding() );
          currToStanding = Integer.parseInt( promoStackStandingPayoutValueBean.getToStanding() );

          if ( currFromStanding <= currToStanding )
          {
            for ( int i = 1; i < promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueListCount(); i++ )
            {
              PromotionStackStandingPayoutFormBean promoStackStandingPayoutValueBeanNext = promotionStackStandingPayoutGroupFormBean.getPromoPayoutValue( i );
              int nextFromStanding = Integer.parseInt( promoStackStandingPayoutValueBeanNext.getFromStanding() );
              int nextToStanding = Integer.parseInt( promoStackStandingPayoutValueBeanNext.getToStanding() );
              if ( nextFromStanding <= nextToStanding )
              {
                if ( nextFromStanding > currToStanding && nextToStanding > currFromStanding && nextToStanding > currToStanding )
                {
                  currFromStanding = nextFromStanding;
                  currToStanding = nextToStanding;
                }
                else
                {
                  // ERROR
                  errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.RANK_OVERLAP_ERROR" ) );
                  isOverlap = true;
                  break;
                }
              }
              else
              {
                // ERROR
                errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
                isOverlap = true;
                break;
              }
              // even if you find one error break out of this validation
              if ( isOverlap )
              {
                break;
              }
            } // end of payout for
          } // end of if
          else
          {
            // Error
            errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "promotion.payout.errors.FROM_RANK_LESS_TO_RANK" ) );
            isOverlap = true;
            break;
          }
        }

        if ( isOverlap )
        {
          break;
        }
      } // end of bean for
    }

    return errors;
  }

  private boolean isOutcomeAlreadySelectedForDivision()
  {
    if ( this.getDivisionValueListCount() > 0 )
    {
      for ( Iterator<DivisionFormBean> divValueListIter = this.getDivisionValueList().iterator(); divValueListIter.hasNext(); )
      {
        Set<String> outcomes = new HashSet<String>();
        DivisionFormBean divFormBean = divValueListIter.next();
        for ( Iterator<DivisionPayoutFormBean> divPayoutValueListIter = divFormBean.getDivisionPayoutValueList().iterator(); divPayoutValueListIter.hasNext(); )
        {
          DivisionPayoutFormBean divPayoutFormBean = divPayoutValueListIter.next();
          outcomes.add( divPayoutFormBean.getOutcomeCode() );
        }
        if ( outcomes.size() < divFormBean.getDivisionPayoutValueListCount() )
        {
          return true;
        }
      }
    }
    return false;
  }

  private Promotion toStackStandingDomainObject( ThrowdownPromotion promotion )
  {
    if ( this.getPromoStackStandingPayoutGroupValueListCount() > 0 )
    {
      for ( Iterator<PromotionStackStandingPayoutGroupFormBean> promoStackStandingPayoutGroupValueListIter = this.getPromoStackStandingPayoutGroupValueList()
          .iterator(); promoStackStandingPayoutGroupValueListIter.hasNext(); )
      {
        PromotionStackStandingPayoutGroupFormBean promotionStackStandingPayoutGroupFormBean = promoStackStandingPayoutGroupValueListIter.next();
        StackStandingPayoutGroup stackStandingPayoutGroup = new StackStandingPayoutGroup();
        stackStandingPayoutGroup.setId( promotionStackStandingPayoutGroupFormBean.getPromoPayoutGroupId() );

        NodeType nodeType = null;

        // we are aware of the use of service against our normal standards
        // here that is the best way to get nodetype instead of having tons of hidden variables
        // here we need most of the information of node type

        if ( !StringUtils.isEmpty( promotionStackStandingPayoutGroupFormBean.getNodeTypeId() ) )
        {
          if ( Integer.parseInt( promotionStackStandingPayoutGroupFormBean.getNodeTypeId() ) > 0 )
          {
            nodeType = getNodeTypeService().getNodeTypeById( Long.valueOf( promotionStackStandingPayoutGroupFormBean.getNodeTypeId() ) );
            stackStandingPayoutGroup.setNodeType( nodeType );
          }
        }
        else
        {
          nodeType = new NodeType();
          nodeType.setName( promotionStackStandingPayoutGroupFormBean.getNodeTypeName() );
        }

        if ( promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType() != null
            && promotionStackStandingPayoutGroupFormBean.getRankingsPayoutType().equals( RankingsPayoutType.RANKINGS_AND_PAYOUT ) )
        {
          for ( int i = 0; i < promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueListCount(); i++ )
          {
            PromotionStackStandingPayoutFormBean promoStackStandingPayoutValueBean = promotionStackStandingPayoutGroupFormBean.getPromoPayoutValue( i );
            StackStandingPayout stackStandingPayout = new StackStandingPayout();

            // if the id is null or 0, set it to null
            if ( promoStackStandingPayoutValueBean.getPromoPayoutId() == null || promoStackStandingPayoutValueBean.getPromoPayoutId().longValue() == 0 )
            {
              stackStandingPayout.setId( null );
            }
            else
            {
              stackStandingPayout.setId( promoStackStandingPayoutValueBean.getPromoPayoutId() );
            }

            if ( !StringUtils.isEmpty( promoStackStandingPayoutValueBean.getFromStanding() ) )
            {
              stackStandingPayout.setStartStanding( Integer.parseInt( promoStackStandingPayoutValueBean.getFromStanding() ) );
            }
            if ( !StringUtils.isEmpty( promoStackStandingPayoutValueBean.getToStanding() ) )
            {
              stackStandingPayout.setEndStanding( Integer.parseInt( promoStackStandingPayoutValueBean.getToStanding() ) );
            }
            if ( !StringUtils.isEmpty( promoStackStandingPayoutValueBean.getPayoutAmount() ) )
            {
              stackStandingPayout.setPayout( Integer.parseInt( promoStackStandingPayoutValueBean.getPayoutAmount() ) );
            }

            stackStandingPayoutGroup.addStackStandingPayout( stackStandingPayout );
          }
        }

        promotion.addStackStandingPayoutGroup( stackStandingPayoutGroup );
      }
    }
    return promotion;
  }

  private Promotion toDivisionDomainObject( ThrowdownPromotion promotion )
  {
    if ( this.getDivisionValueListCount() > 0 )
    {
      for ( Iterator<DivisionFormBean> divValueListIter = this.getDivisionValueList().iterator(); divValueListIter.hasNext(); )
      {
        DivisionFormBean divisionFormBean = divValueListIter.next();
        Division div = new Division();
        div.setId( divisionFormBean.getDivisionId() );
        div.setDivisionNameAssetCode( divisionFormBean.getDivisionName() );

        if ( !StringUtils.isEmpty( divisionFormBean.getMinimumQualifier() ) )
        {
          div.setMinimumQualifier( new BigDecimal( divisionFormBean.getMinimumQualifier() ) );
        }

        for ( int i = 0; i < divisionFormBean.getDivisionPayoutValueListCount(); i++ )
        {
          DivisionPayoutFormBean divisionPayoutValueBean = divisionFormBean.getDivisionPayoutValue( i );
          DivisionPayout divPayout = new DivisionPayout();
          divPayout.setId( divisionPayoutValueBean.getDivisionPayoutId() );

          if ( !StringUtils.isEmpty( divisionPayoutValueBean.getPayoutAmount() ) )
          {
            divPayout.setPoints( Integer.parseInt( divisionPayoutValueBean.getPayoutAmount() ) );
          }
          divPayout.setOutcome( MatchTeamOutcomeType.lookup( divisionPayoutValueBean.getOutcomeCode() ) );
          div.addDivisionPayout( divPayout );
        }
        promotion.addDivision( div );
      }
    }
    return promotion;
  }

  private List<PromotionStackStandingPayoutGroupFormBean> getStackStandingEmptyValueList( int valueListCount )
  {
    List<PromotionStackStandingPayoutGroupFormBean> valueList = new ArrayList<PromotionStackStandingPayoutGroupFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new PromotionStackStandingPayoutGroupFormBean() );
    }

    return valueList;
  }

  private List<DivisionFormBean> getDivisionEmptyValueList( int valueListCount )
  {
    List<DivisionFormBean> valueList = new ArrayList<DivisionFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new DivisionFormBean() );
    }

    return valueList;
  }

  private List<RoundFormBean> geRoundEmptyValueList( int valueListCount )
  {
    List<RoundFormBean> valueList = new ArrayList<RoundFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new RoundFormBean() );
    }

    return valueList;
  }

  public void addPayoutLevelForThisNode()
  {
    PromotionStackStandingPayoutFormBean promoPayoutBean = new PromotionStackStandingPayoutFormBean();
    PromotionStackStandingPayoutGroupFormBean promotionStackStandingPayoutGroupFormBean = null; // new

    if ( stackStandingGroupEditId != null && stackStandingGroupEditId.length() > 0 )
    {
      for ( Iterator<PromotionStackStandingPayoutGroupFormBean> iter = promoStackStandingPayoutGroupValueList.iterator(); iter.hasNext(); )
      {
        promotionStackStandingPayoutGroupFormBean = (PromotionStackStandingPayoutGroupFormBean)iter.next();
        // if a groupEditId has been set, then that is where the payout belongs
        if ( promotionStackStandingPayoutGroupFormBean.getGuid().equals( stackStandingGroupEditId )
            && !promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueList().contains( promoPayoutBean ) )
        {
          promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueList().add( promoPayoutBean );
        }
      }
    }
    else
    {
      promotionStackStandingPayoutGroupFormBean = new PromotionStackStandingPayoutGroupFormBean();
      promotionStackStandingPayoutGroupFormBean.getPromoStackStandingPayoutValueList().add( promoPayoutBean );
      this.getPromoStackStandingPayoutGroupValueList().add( promotionStackStandingPayoutGroupFormBean );
    }
  }

  public void addPayoutLevelForThisDivision()
  {
    DivisionPayoutFormBean divisionPayoutBean = new DivisionPayoutFormBean();
    DivisionFormBean divisionFormBean = null; // new

    if ( divisionEditId != null && divisionEditId.length() > 0 )
    {
      for ( Iterator<DivisionFormBean> iter = divisionValueList.iterator(); iter.hasNext(); )
      {
        divisionFormBean = iter.next();
        // if a groupEditId has been set, then that is where the payout belongs
        if ( divisionFormBean.getGuid().equals( divisionEditId ) && !divisionFormBean.getDivisionPayoutValueList().contains( divisionPayoutBean ) )
        {
          divisionFormBean.getDivisionPayoutValueList().add( divisionPayoutBean );
        }
      }
    }
    else
    {
      divisionFormBean = new DivisionFormBean();
      divisionFormBean.getDivisionPayoutValueList().add( divisionPayoutBean );
      this.getDivisionValueList().add( divisionFormBean );
    }
  }

  public void addPromoStackStandingPayoutGroup()
  {
    if ( promoStackStandingPayoutGroupValueList != null )
    {
      PromotionStackStandingPayoutGroupFormBean group = new PromotionStackStandingPayoutGroupFormBean();
      promoStackStandingPayoutGroupValueList.add( group );
    }
    else
    {
      promoStackStandingPayoutGroupValueList = getStackStandingEmptyValueList( 1 );
      PromotionStackStandingPayoutGroupFormBean group = promoStackStandingPayoutGroupValueList.get( 0 );
      group.setPromoStackStandingPayoutValueList( getStackStandingEmptyPayoutValueList( 1 ) );
    }
  }

  public void addDivision()
  {
    if ( divisionValueList != null )
    {
      DivisionFormBean div = new DivisionFormBean();
      divisionValueList.add( div );
    }
    else
    {
      divisionValueList = getDivisionEmptyValueList( 1 );
      DivisionFormBean div = divisionValueList.get( 0 );
      div.setDivisionPayoutValueList( getDivisionEmptyPayoutValueList( 1 ) );
    }
  }

  private List<PromotionStackStandingPayoutFormBean> getStackStandingEmptyPayoutValueList( int valueListCount )
  {
    List<PromotionStackStandingPayoutFormBean> valueList = new ArrayList<PromotionStackStandingPayoutFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new PromotionStackStandingPayoutFormBean() );
    }

    return valueList;
  }

  private List<DivisionPayoutFormBean> getDivisionEmptyPayoutValueList( int valueListCount )
  {
    List<DivisionPayoutFormBean> valueList = new ArrayList<DivisionPayoutFormBean>();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new DivisionPayoutFormBean() );
    }

    return valueList;
  }

  public int getPromoStackStandingPayoutValueListCount()
  {
    if ( promoStackStandingPayoutGroupValueList == null || promoStackStandingPayoutGroupValueList.size() == 0 )
    {
      return 0;
    }

    // Get the size of the child collection
    return getPromoStackStandingPayoutGroupValueListElement( 0 ).getPromoStackStandingPayoutValueListCount();
  }

  public int getDivisionPayoutValueListCount()
  {
    if ( divisionValueList == null || divisionValueList.size() == 0 )
    {
      return 0;
    }

    // Get the size of the child collection
    return getDivisionValueListElement( 0 ).getDivisionPayoutValueListCount();
  }

  public PromotionStackStandingPayoutGroupFormBean getPromoStackStandingPayoutGroupValueListElement( int index )
  {
    try
    {
      return (PromotionStackStandingPayoutGroupFormBean)promoStackStandingPayoutGroupValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public DivisionFormBean getDivisionValueListElement( int index )
  {
    try
    {
      return (DivisionFormBean)divisionValueList.get( index );
    }
    catch( Exception anException )
    {
      return null;
    }
  }

  public int getPromoStackStandingPayoutGroupValueListCount()
  {
    if ( promoStackStandingPayoutGroupValueList == null )
    {
      return 0;
    }

    return promoStackStandingPayoutGroupValueList.size();
  }

  public int getDivisionValueListCount()
  {
    if ( divisionValueList == null )
    {
      return 0;
    }

    return divisionValueList.size();
  }

  public int getRoundListCount()
  {
    if ( roundsList == null )
    {
      return 0;
    }

    return roundsList.size();
  }

  public void buildStackStandingGroups( ThrowdownPromotion promotion )
  {
    Set<StackStandingPayoutGroup> promotionGroups = null;

    promotionGroups = promotion.getStackStandingPayoutGroups();

    if ( promotionGroups != null && promotionGroups.size() != 0 )
    {
      if ( promoStackStandingPayoutGroupValueList == null )
      {
        promoStackStandingPayoutGroupValueList = new ArrayList<PromotionStackStandingPayoutGroupFormBean>();
      }
      else
      {
        promoStackStandingPayoutGroupValueList.clear();
      }

      Iterator<StackStandingPayoutGroup> promotionGroupsIter = promotionGroups.iterator();
      while ( promotionGroupsIter.hasNext() )
      {
        StackStandingPayoutGroup promotionStackStandingPayoutGroup = promotionGroupsIter.next();
        PromotionStackStandingPayoutGroupFormBean promotionStackStandingPayoutGroupFormBean = new PromotionStackStandingPayoutGroupFormBean();
        promotionStackStandingPayoutGroupFormBean.setPromoPayoutGroupId( promotionStackStandingPayoutGroup.getId() );
        if ( !promotionStackStandingPayoutGroup.isHierarchyPayoutGroup() )
        {
          promotionStackStandingPayoutGroupFormBean.setNodeTypeName( promotionStackStandingPayoutGroup.getNodeType().getNodeTypeName() );
          promotionStackStandingPayoutGroupFormBean.setNodeTypeId( String.valueOf( promotionStackStandingPayoutGroup.getNodeType().getId() ) );
        }
        else
        {
          promotionStackStandingPayoutGroupFormBean.setNodeTypeName( CmsResourceBundle.getCmsBundle().getString( "system.general.ALL" ) );
        }

        if ( promotionStackStandingPayoutGroup.getStackStandingPayouts().isEmpty() )
        {
          promotionStackStandingPayoutGroupFormBean.setRankingsPayoutType( RankingsPayoutType.RANKINGS_ONLY );
        }
        else
        {
          promotionStackStandingPayoutGroupFormBean.setRankingsPayoutType( RankingsPayoutType.RANKINGS_AND_PAYOUT );
        }

        if ( promotionStackStandingPayoutGroup.getPromotionStackStandingPayout() != null && promotionStackStandingPayoutGroup.getPromotionStackStandingPayout().size() > 0 )
        {
          ArrayList<PromotionStackStandingPayoutFormBean> promotionPayoutsFormBeans = new ArrayList<PromotionStackStandingPayoutFormBean>();

          List<StackStandingPayout> stackStandingPayoutList = new ArrayList<StackStandingPayout>();
          Set<StackStandingPayout> stackStandingPayoutSet = promotionStackStandingPayoutGroup.getPromotionStackStandingPayout();
          stackStandingPayoutList.addAll( stackStandingPayoutSet );
          Collections.sort( stackStandingPayoutList, StackStandingPayout.PayoutIdComparator );

          for ( StackStandingPayout promoPayout : stackStandingPayoutList )
          {
            PromotionStackStandingPayoutFormBean promoPayoutFormBean = buildStackStandingPayoutBean( promoPayout );
            promotionPayoutsFormBeans.add( promoPayoutFormBean );
          }

          promotionStackStandingPayoutGroupFormBean.setPromoStackStandingPayoutValueList( promotionPayoutsFormBeans );
        }
        else
        {
          ArrayList<PromotionStackStandingPayoutFormBean> promotionPayoutsFormBeans = new ArrayList<PromotionStackStandingPayoutFormBean>();
          PromotionStackStandingPayoutFormBean promoFormBean = new PromotionStackStandingPayoutFormBean();
          promotionPayoutsFormBeans.add( promoFormBean );
          promotionStackStandingPayoutGroupFormBean.setPromoStackStandingPayoutValueList( promotionPayoutsFormBeans );
        }
        this.promoStackStandingPayoutGroupValueList.add( promotionStackStandingPayoutGroupFormBean );
      } // end for(...promotionGroups.iterator()...)
    }
  }

  private PromotionStackStandingPayoutFormBean buildStackStandingPayoutBean( StackStandingPayout promoPayout )
  {
    PromotionStackStandingPayoutFormBean promoFormBean = new PromotionStackStandingPayoutFormBean();
    promoFormBean.setPromoPayoutId( promoPayout.getId() );
    promoFormBean.setVersion( promoPayout.getVersion() );
    promoFormBean.setFromStanding( String.valueOf( promoPayout.getStartStanding() ) );
    promoFormBean.setToStanding( String.valueOf( promoPayout.getEndStanding() ) );
    promoFormBean.setPayoutAmount( String.valueOf( promoPayout.getPayout() ) );
    return promoFormBean;
  }

  public void buildDivisions( ThrowdownPromotion promotion )
  {
    Set<Division> divisions = null;

    divisions = promotion.getDivisions();

    if ( divisions != null && divisions.size() != 0 )
    {
      if ( divisionValueList == null )
      {
        divisionValueList = new ArrayList<DivisionFormBean>();
      }
      else
      {
        divisionValueList.clear();
      }

      Iterator<Division> divsIter = divisions.iterator();
      while ( divsIter.hasNext() )
      {
        Division div = divsIter.next();
        DivisionFormBean divFormBean = new DivisionFormBean();
        divFormBean.setDivisionId( div.getId() );
        divFormBean.setDivisionName( div.getDivisionName() );
        divFormBean.setMinimumQualifier( div.getMinimumQualifier().toString() );

        if ( div.getPayouts() != null && div.getPayouts().size() > 0 )
        {
          List<DivisionPayoutFormBean> divPayoutFormBeans = new ArrayList<DivisionPayoutFormBean>();
          Iterator<DivisionPayout> divPayoutsIter = div.getPayouts().iterator();

          while ( divPayoutsIter.hasNext() )
          {
            DivisionPayout divPayout = divPayoutsIter.next();
            DivisionPayoutFormBean divPayoutFormBean = buildDivisionPayoutBean( divPayout, div );
            divPayoutFormBean.setDivisionPayoutId( divPayout.getId() );
            divPayoutFormBean.setVersion( divPayout.getVersion() );
            divPayoutFormBeans.add( divPayoutFormBean );
          }

          divFormBean.setDivisionPayoutValueList( divPayoutFormBeans );
          this.divisionValueList.add( divFormBean );
        }
      }

      if ( divisionValueList.isEmpty() )
      {
        divisionValueList = getDivisionEmptyValueList( 1 );
      }

    }
  }

  private DivisionPayoutFormBean buildDivisionPayoutBean( DivisionPayout divPayout, Division div )
  {
    DivisionPayoutFormBean divPayoutFormBean = new DivisionPayoutFormBean();
    divPayoutFormBean.setDivisionPayoutId( divPayout.getId() );
    divPayoutFormBean.setVersion( divPayout.getVersion() );
    divPayoutFormBean.setDivisionId( div.getId().toString() );
    divPayoutFormBean.setOutcomeCode( divPayout.getOutcome().getCode() );
    divPayoutFormBean.setPayoutAmount( Integer.toString( divPayout.getPoints() ) );
    return divPayoutFormBean;
  }

  private void buildRounds( ThrowdownPromotion promotion )
  {
    if ( promotion.getNumberOfRounds() > 0 && promotion.getLengthOfRound() > 0 && promotion.getHeadToHeadStartDate() != null )
    {
      createRounds( promotion );
    }
  }

  public void recalculateRounds()
  {
    try
    {
      getRoundsList().clear();
      int noOfRounds = Integer.parseInt( getNumberOfRounds() );
      int noOfDaysPerRound = Integer.parseInt( getNumberOfDayPerRound() );
      Date startDate = DateUtils.toDate( getStartDateForFirstRound() );
      if ( noOfRounds > 0 && noOfDaysPerRound > 0 && startDate != null )
      {
        createRounds( noOfRounds, noOfDaysPerRound, startDate, false, false );
      }
    }
    catch( NumberFormatException e )
    {
      // dont do anything. we wont recalculate rounds and user will be intimated when he submits
      // forms.
    }
  }

  private String getLastRoundEndDate()
  {
    String lastRoundEndDate = null;
    try
    {
      int noOfRounds = Integer.parseInt( getNumberOfRounds() );
      int noOfDaysPerRound = Integer.parseInt( getNumberOfDayPerRound() );
      Date startDate = DateUtils.toDate( getStartDateForFirstRound() );
      if ( noOfRounds > 0 && noOfDaysPerRound > 0 && startDate != null )
      {
        SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
        for ( int i = 0; i < noOfRounds; i++ )
        {
          Date endDate = org.apache.commons.lang3.time.DateUtils.addDays( startDate, noOfDaysPerRound - 1 );
          lastRoundEndDate = sdf.format( endDate );
          startDate = org.apache.commons.lang3.time.DateUtils.addDays( endDate, 1 );
        }
      }
    }
    catch( NumberFormatException e )
    {
      // dont do anything. we wont recalculate rounds and user will be intimated when he submits
      // forms.
    }
    return lastRoundEndDate;
  }

  private void createRounds( ThrowdownPromotion promotion )
  {
    if ( promotion.getNumberOfRounds() > 0 && promotion.getLengthOfRound() > 0 && promotion.getHeadToHeadStartDate() != null )
    {
      createRounds( promotion.getNumberOfRounds(), promotion.getLengthOfRound(), promotion.getHeadToHeadStartDate(), false, false );
    }
    setPayoutStatus( promotion );
  }

  private void setPayoutStatus( ThrowdownPromotion promotion )
  {
    Set<Division> divisions = promotion.getDivisions();
    for ( RoundFormBean roundFormBean : getRoundsList() )
    {
      boolean scheduled = false;
      boolean payedOut = false;
      // check each division
      for ( Division division : divisions )
      {
        Set<Round> rounds = division.getRounds();
        for ( Round round : rounds )
        {
          if ( round.getRoundNumber() == Integer.parseInt( roundFormBean.getRoundNumber() ) )
          {
            scheduled = !round.getMatches().isEmpty();
            payedOut = round.isPayoutsIssued();
          }
        }
      }
      roundFormBean.setScheduled( scheduled );
      roundFormBean.setPayoutComplete( payedOut );
    }
  }

  private void createRounds( int numberOfRounds, int numberOfDaysPerRound, Date startDate, boolean scheduled, boolean payoutComplete )
  {
    SimpleDateFormat sdf = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
    String strStartDate = null;
    String strEndDate = null;
    for ( int i = 0; i < numberOfRounds; i++ )
    {
      Date endDate = org.apache.commons.lang3.time.DateUtils.addDays( startDate, numberOfDaysPerRound - 1 );
      strStartDate = sdf.format( startDate );
      strEndDate = sdf.format( endDate );

      RoundFormBean round = new RoundFormBean();
      round.setRoundNumber( Integer.toString( i + 1 ) );
      round.setStartDate( strStartDate );
      round.setEndDate( strEndDate );
      round.setScheduled( scheduled );
      round.setPayoutComplete( payoutComplete );
      startDate = org.apache.commons.lang3.time.DateUtils.addDays( endDate, 1 );
      getRoundsList().add( round );
    }
  }

  public String getBillCode1()
  {
    return billCode1;
  }

  public void setBillCode1( String billCode1 )
  {
    this.billCode1 = billCode1;
  }

  public String getCustomValue1()
  {
    return customValue1;
  }

  public void setCustomValue1( String customValue1 )
  {
    this.customValue1 = customValue1;
  }

  public String getBillCode2()
  {
    return billCode2;
  }

  public void setBillCode2( String billCode2 )
  {
    this.billCode2 = billCode2;
  }

  public String getCustomValue2()
  {
    return customValue2;
  }

  public void setCustomValue2( String customValue2 )
  {
    this.customValue2 = customValue2;
  }

  public String getBillCode3()
  {
    return billCode3;
  }

  public void setBillCode3( String billCode3 )
  {
    this.billCode3 = billCode3;
  }

  public String getCustomValue3()
  {
    return customValue3;
  }

  public void setCustomValue3( String customValue3 )
  {
    this.customValue3 = customValue3;
  }

  public String getBillCode4()
  {
    return billCode4;
  }

  public void setBillCode4( String billCode4 )
  {
    this.billCode4 = billCode4;
  }

  public String getCustomValue4()
  {
    return customValue4;
  }

  public void setCustomValue4( String customValue4 )
  {
    this.customValue4 = customValue4;
  }

  public String getBillCode5()
  {
    return billCode5;
  }

  public void setBillCode5( String billCode5 )
  {
    this.billCode5 = billCode5;
  }

  public String getCustomValue5()
  {
    return customValue5;
  }

  public void setCustomValue5( String customValue5 )
  {
    this.customValue5 = customValue5;
  }

  public String getBillCode6()
  {
    return billCode6;
  }

  public void setBillCode6( String billCode6 )
  {
    this.billCode6 = billCode6;
  }

  public String getCustomValue6()
  {
    return customValue6;
  }

  public void setCustomValue6( String customValue6 )
  {
    this.customValue6 = customValue6;
  }

  public String getBillCode7()
  {
    return billCode7;
  }

  public void setBillCode7( String billCode7 )
  {
    this.billCode7 = billCode7;
  }

  public String getCustomValue7()
  {
    return customValue7;
  }

  public void setCustomValue7( String customValue7 )
  {
    this.customValue7 = customValue7;
  }

  public String getBillCode8()
  {
    return billCode8;
  }

  public void setBillCode8( String billCode8 )
  {
    this.billCode8 = billCode8;
  }

  public String getCustomValue8()
  {
    return customValue8;
  }

  public void setCustomValue8( String customValue8 )
  {
    this.customValue8 = customValue8;
  }

  public String getBillCode9()
  {
    return billCode9;
  }

  public void setBillCode9( String billCode9 )
  {
    this.billCode9 = billCode9;
  }

  public String getCustomValue9()
  {
    return customValue9;
  }

  public void setCustomValue9( String customValue9 )
  {
    this.customValue9 = customValue9;
  }

  public String getBillCode10()
  {
    return billCode10;
  }

  public void setBillCode10( String billCode10 )
  {
    this.billCode10 = billCode10;
  }

  public String getCustomValue10()
  {
    return customValue10;
  }

  public void setCustomValue10( String customValue10 )
  {
    this.customValue10 = customValue10;
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

  private NodeTypeService getNodeTypeService()
  {
    return (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
  }

  private ThrowdownService getThrowdownService()
  {
    return (ThrowdownService)getService( ThrowdownService.BEAN_NAME );
  }

}
