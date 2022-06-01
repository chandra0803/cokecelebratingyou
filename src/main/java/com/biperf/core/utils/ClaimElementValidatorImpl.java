/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/ClaimElementValidatorImpl.java,v $
 */

package com.biperf.core.utils;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.ClaimFormStepElement;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.domain.enums.PromotionClaimFormStepElementValidationType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionClaimFormStepElementValidation;

/*
 * ClaimElementValidatorImpl <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 26, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ClaimElementValidatorImpl implements ClaimElementValidator
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private static final Log logger = LogFactory.getLog( ClaimElementValidatorImpl.class );
  private PromotionDAO promotionDAO;

  // ---------------------------------------------------------------------------
  // Validation Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if the value of the given claim element is valid, returns false otherwise.
   * 
   * @param claimElement the claim element whose value will be validated.
   * @param promotion the promotion associated with the given claim element
   * @return true if the given claim element's value is valid, false otherwise.
   */
  public boolean isValid( ClaimElement claimElement, Promotion promotion )
  {
    boolean isValid = true;

    ClaimElementAdapter claimElementAdapter = new ClaimElementAdapter( claimElement, promotion );
    if ( claimElementAdapter.isValidateable() )
    {
      if ( claimElementAdapter.isDate() )
      {
        isValid = isValidDate( claimElementAdapter );
      }
      else if ( claimElementAdapter.isNumber() )
      {
        isValid = isValidNumber( claimElementAdapter );
      }
      else if ( claimElementAdapter.isText() )
      {
        if ( claimElement.getValue() != null && !"".equals( claimElement.getValue() ) )
        {
          isValid = isValidText( claimElementAdapter );
        }
      }
    }

    return isValid;
  }

  /**
   * Returns true if the value of the given claim element represents a date that is equal to or
   * later than the start date and earlier than or equal to the end date; returns false otherwise.
   * The start and end dates are specified by the claim element's promotion claim form step element
   * validation object.
   * 
   * @param claimElementAdapter the claim element whose value will be validated, adapted for use by
   *          the ClaimElementValidatorImpl class.
   * @return true if the value of the given claim element represents a valid date; false otherwise.
   */
  private boolean isValidDate( ClaimElementAdapter claimElementAdapter )
  {
    boolean isValidDate = true;

    try
    {
      Date value = claimElementAdapter.getValueAsDate();

      // Is the date later than or equal to the starting date?
      Date startDate = claimElementAdapter.getStartDate();
      if ( startDate != null && value != null )
      {
        isValidDate = value.equals( startDate ) || value.after( startDate );
      }

      // Is the date earlier than or equal to the ending date?
      Date endDate = claimElementAdapter.getEndDate();
      if ( isValidDate && endDate != null && value != null )
      {
        isValidDate = value.before( endDate ) || value.equals( endDate );
      }
    }
    catch( ParseException e )
    {
      if ( logger.isErrorEnabled() )
      {
        logger.error( "Claim element value is not a date.", e );
        // Bug fix # 21271
        isValidDate = false;
      }
    }

    return isValidDate;
  }

  /**
   * Returns true if the value of the given claim element represents a real number that is equal to
   * or greater than the minimum value and less than or equal to the maximum value; returns false
   * otherwise. The minimum and maximum values are specified by the claim element's promotion claim
   * form step element validation object.
   * 
   * @param claimElementAdapter the claim element whose value will be validated, adapted for use by
   *          the ClaimElementValidatorImpl class.
   * @return true if the value of the given claim element represents a valid number; false
   *         otherwise.
   */
  private boolean isValidNumber( ClaimElementAdapter claimElementAdapter )
  {
    boolean isValidNumber = true;

    BigDecimal value = new BigDecimal( 0 );
    if ( claimElementAdapter.getValue() != null && !claimElementAdapter.getValue().equals( "" ) )
    {
      value = claimElementAdapter.getValueAsNumber();
    }

    // Is the number greater than or equal to the minimum number?
    BigDecimal minValue = claimElementAdapter.getMinValue();
    if ( minValue != null )
    {
      isValidNumber = value.compareTo( minValue ) >= 0;
    }

    // Is the number less than or equal to the maximum number?
    BigDecimal maxValue = claimElementAdapter.getMaxValue();
    if ( isValidNumber && maxValue != null )
    {
      isValidNumber = value.compareTo( maxValue ) <= 0;
    }

    return isValidNumber;
  }

  /**
   * Returns true if the value of the given claim element represents valid text; returns false
   * otherwise.
   * 
   * @param claimElementAdapter the claim element whose value will be validated, adapted for use by
   *          the ClaimElementValidatorImpl class.
   * @return true if the value of the given claim element represents valid text; false otherwise.
   */
  private boolean isValidText( ClaimElementAdapter claimElementAdapter )
  {
    boolean isValidText = true;

    String text = "";

    if ( claimElementAdapter.getValue() != null )
    {
      text = claimElementAdapter.getValue();
    }

    // Is the text shorter than or equal to the maximum length?
    Integer maxLength = claimElementAdapter.getMaxLength();
    if ( maxLength != null )
    {
      isValidText = text.length() <= maxLength.intValue();
    }

    // Does the text start with the specified text?
    String startsWith = claimElementAdapter.getStartsWith();
    if ( isValidText && startsWith != null && startsWith.length() > 0 )
    {
      isValidText = text.toLowerCase().startsWith( startsWith.toLowerCase() );
    }

    // Does the text not start with the specified text?
    String notStartWith = claimElementAdapter.getNotStartWith();
    if ( isValidText && notStartWith != null && notStartWith.length() > 0 )
    {
      isValidText = !text.toLowerCase().startsWith( notStartWith.toLowerCase() );
    }

    // Does the text end with the specified text?
    String endsWith = claimElementAdapter.getEndsWith();
    if ( isValidText && endsWith != null && endsWith.length() > 0 )
    {
      isValidText = text.toLowerCase().endsWith( endsWith.toLowerCase() );
    }

    // Does the text not end with the specified text?
    String notEndWith = claimElementAdapter.getNotEndWith();
    if ( isValidText && notEndWith != null && notEndWith.length() > 0 )
    {
      isValidText = !text.toLowerCase().endsWith( notEndWith.toLowerCase() );
    }

    // Does the text contain the specified text?
    String contains = claimElementAdapter.getContains();
    if ( isValidText && contains != null && contains.length() > 0 )
    {
      isValidText = text.toLowerCase().indexOf( contains.toLowerCase() ) != -1;
    }

    // Does the text not contain the specified text?
    String notContain = claimElementAdapter.getNotContain();
    if ( isValidText && notContain != null && notContain.length() > 0 )
    {
      isValidText = text.toLowerCase().indexOf( notContain.toLowerCase() ) == -1;
    }

    return isValidText;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Sets a reference to the promotion DAO.
   * 
   * @param promotionDAO a reference to the promotion DAO.
   */
  public void setPromotionDAO( PromotionDAO promotionDAO )
  {
    this.promotionDAO = promotionDAO;
  }

  // ---------------------------------------------------------------------------
  // Inner Classes
  // ---------------------------------------------------------------------------

  /**
   * Adapts a {@link ClaimElement} object to the implementation of the
   * <code>ClaimElementValidatorImpl</code> class.
   */
  private class ClaimElementAdapter
  {
    /**
     * the adapted claim element
     */
    private ClaimElement claimElement;
    private Promotion promotion;

    /**
     * Used to cache the claim element's {@link PromotionClaimFormStepElementValidation} object.
     */
    private PromotionClaimFormStepElementValidation promotionClaimFormStepElementValidation;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Constructs a <code>ClaimElementAdapter</code> object.
     */
    ClaimElementAdapter( ClaimElement claimElement, Promotion promotion )
    {
      this.claimElement = claimElement;
      this.promotion = promotion;
    }

    // -------------------------------------------------------------------------
    // Date Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the latest valid date.
     * 
     * @return the latest valid date.
     */
    public Date getEndDate()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getEndDate();
    }

    /**
     * Returns the earliest valid date.
     * 
     * @return the earliest valid date.
     */
    public Date getStartDate()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getStartDate();
    }

    /**
     * Returns the value of this claim element as a date.
     * 
     * @return the value of this claim element as a date.
     * @throws ParseException
     */
    public Date getValueAsDate() throws ParseException
    {
      DateFormat dateFormat = new SimpleDateFormat( DateFormatterUtil.getDatePattern( UserManager.getLocale() ) );
      if ( !StringUtil.isEmpty( claimElement.getValue() ) )
      {
        return dateFormat.parse( claimElement.getValue() );
      }
      return null;
    }

    // -------------------------------------------------------------------------
    // Number Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the maximum valid value, or null if the maximum valid value does not exist.
     * 
     * @return the maximum valid value, or null if the maximum valid value does not exist.
     */
    public BigDecimal getMaxValue()
    {
      BigDecimal maxValue = null;

      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();

      Integer maxValueAsInteger = pcfsev.getMaxValue();
      if ( maxValueAsInteger != null )
      {
        maxValue = new BigDecimal( maxValueAsInteger.doubleValue() ).setScale( 0 );
      }

      return maxValue;
    }

    /**
     * Returns the minimum valid value, or null if the minimum valid value does not exist.
     * 
     * @return the minimum valid value, or null if the minimum valid value does not exist.
     */
    public BigDecimal getMinValue()
    {
      BigDecimal minValue = null;

      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();

      Integer minValueAsInteger = pcfsev.getMinValue();
      if ( minValueAsInteger != null )
      {
        minValue = new BigDecimal( minValueAsInteger.doubleValue() ).setScale( 0 );
      }

      return minValue;
    }

    /**
     * Returns the value of this claim element as a number.
     * 
     * @return the value of this claim element as a number.
     */
    public BigDecimal getValueAsNumber()
    {
      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
      int scale = claimFormStepElement.getNumberOfDecimals().intValue();

      return new BigDecimal( claimElement.getValue() ).setScale( scale );
    }

    // -------------------------------------------------------------------------
    // Text Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the "contains" prefix, or null if the "contains" prefix does not exist.
     * 
     * @return the "contains" prefix, or null if the "contains" prefix does not exist.
     */
    public String getContains()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getContains();
    }

    /**
     * Returns the "ends with" prefix, or null if the "ends with" prefix does not exist.
     * 
     * @return the "ends with" prefix, or null if the "ends with" prefix does not exist.
     */
    public String getEndsWith()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getEndsWith();
    }

    /**
     * Returns the maximum valid length, or null if the maximum valid length does not exist.
     * 
     * @return the maximum valid length, or null if the maximum valid length does not exist.
     */
    public Integer getMaxLength()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getMaxLength();
    }

    /**
     * Returns the "not contain" prefix, or null if the "not contains" prefix does not exist.
     * 
     * @return the "not contain" prefix, or null if the "not contains" prefix does not exist.
     */
    public String getNotContain()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getNotContain();
    }

    /**
     * Returns the "not ends with" prefix, or null if the "not ends with" prefix does not exist.
     * 
     * @return the "not ends with" prefix, or null if the "not ends with" prefix does not exist.
     */
    public String getNotEndWith()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getNotEndWith();
    }

    /**
     * Returns the "not start with" prefix, or null if the "not start with" prefix does not exist.
     * 
     * @return the "not start with" prefix, or null if the "not start with" prefix does not exist.
     */
    public String getNotStartWith()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getNotStartWith();
    }

    /**
     * Returns the "starts with" prefix, or null if the "starts with" prefix does not exist.
     * 
     * @return the "starts with" prefix, or null if the "starts with" prefix does not exist.
     */
    public String getStartsWith()
    {
      PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
      return pcfsev.getStartsWith();
    }

    /**
     * Returns the value of this claim element.
     * 
     * @return the value of this claim element.
     */
    public String getValue()
    {
      return claimElement.getValue();
    }

    // -------------------------------------------------------------------------
    // Test Methods
    // -------------------------------------------------------------------------

    /**
     * Returns true if the type of this claim element's value is "date;" returns false otherwise.
     * 
     * @return true if the type of this claim element's value is "date;" false otherwise.
     */
    public boolean isDate()
    {
      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
      ClaimFormElementType claimFormElementType = claimFormStepElement.getClaimFormElementType();

      return claimFormElementType.getCode().equalsIgnoreCase( ClaimFormElementType.DATE_FIELD );
    }

    /**
     * Returns true if the type of this claim element's value is "number;" returns false otherwise.
     * 
     * @return true if the type of this claim element's value is "number;" false otherwise.
     */
    public boolean isNumber()
    {
      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
      ClaimFormElementType claimFormElementType = claimFormStepElement.getClaimFormElementType();

      return claimFormElementType.getCode().equalsIgnoreCase( ClaimFormElementType.NUMBER_FIELD );
    }

    /**
     * Returns true if the type of this claim element's value is "text;" returns false otherwise.
     * 
     * @return true if the type of this claim element's value is "text;" false otherwise.
     */
    public boolean isText()
    {
      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
      ClaimFormElementType claimFormElementType = claimFormStepElement.getClaimFormElementType();

      return claimFormElementType.getCode().equalsIgnoreCase( ClaimFormElementType.TEXT_FIELD );
    }

    /**
     * Returns true if the value of this claim element should be validated; returns false otherwise.
     * 
     * @return true if the value of this claim element should be validated; false otherwise.
     */
    public boolean isValidateable()
    {
      boolean isValidateable = false;

      ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();
      ClaimFormElementType claimFormElementType = claimFormStepElement.getClaimFormElementType();

      if ( claimFormElementType.getCode().equalsIgnoreCase( ClaimFormElementType.DATE_FIELD ) || claimFormElementType.getCode().equalsIgnoreCase( ClaimFormElementType.NUMBER_FIELD )
          || claimFormElementType.getCode().equalsIgnoreCase( ClaimFormElementType.TEXT_FIELD ) )
      {
        PromotionClaimFormStepElementValidation pcfsev = getPromotionClaimFormStepElementValidation();
        if ( pcfsev != null )
        {
          PromotionClaimFormStepElementValidationType validationType = pcfsev.getValidationType();
          isValidateable = validationType.getCode().equalsIgnoreCase( PromotionClaimFormStepElementValidationType.VALIDATE );
        }
      }
      return isValidateable;
    }

    // -------------------------------------------------------------------------
    // Private Methods
    // -------------------------------------------------------------------------

    /**
     * Returns the claim element's promotion claim form step element validation.
     * 
     * @return the claim element's promotion claim form step element validation.
     */
    private PromotionClaimFormStepElementValidation getPromotionClaimFormStepElementValidation()
    {
      if ( promotionClaimFormStepElementValidation == null )
      {
        // Lazy load the promotion claim form step element validation.
        ClaimFormStepElement claimFormStepElement = claimElement.getClaimFormStepElement();

        promotionClaimFormStepElementValidation = promotionDAO.getPromotionClaimFormStepElementValidation( promotion, claimFormStepElement );
      }

      return promotionClaimFormStepElementValidation;
    }
  }
}
