
package com.biperf.core.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;

public class BudgetUtils
{
  private static MathContext MEDIA_CONVERSION_CONTEXT = new MathContext( 16, RoundingMode.HALF_UP );

  /**
   * Get the the display value (an <code>int</code>) for a given <code>BigDecimal</code> budget amount.
   * When the budget amount has decimal values, proper rounding will be used (4.4 will round off to
   * 4, and 4.5 will round to 5)
   *
   * @param budgetValue - the amount you want to display
   * @return int - the display value for the given budget amount
   */
  public static int getBudgetDisplayValue( BigDecimal budgetValue )
  {
    return getBudgetDisplayValue( budgetValue.doubleValue() );
  }

  /**
   * Get the the display value (an <code>int</code>) for a given <code>double</code> budget amount.
   * When the budget amount has decimal values, proper rounding will be used (4.4 will round off to
   * 4, and 4.5 will round to 5)
   *
   * @param budgetValue - the amount you want to display
   * @return int - the display value for the given budget amount
   */
  public static int getBudgetDisplayValue( double budgetValue )
  {
    if ( budgetValue < 0 )
    {
      return -getBudgetDisplayValue( -budgetValue );
    }
    return (int)Math.round( budgetValue );
  }

  /**
   * Apply the media conversion ratio resulting from dividing the <code>currentMediaValue</code> by the
   * <code>targetMediaValue</code> to the given <code>budgetValue</code>.  This will return a <code>BigDecimal</code>
   * representing the <code>budgetValue</code> in locale-specific units.
   *
   * @param budgetValue - the value of the budget to convert
   * @param currentMediaValue - the media value of the country the budget is currently represented in
   * @param targetMediaValue - the media value of the country you want to convert to
   * @return BigDecimal - the value of the converted budget
   */
  public static BigDecimal applyMediaConversion( BigDecimal budgetValue, BigDecimal currentMediaValue, BigDecimal targetMediaValue )
  {
    BigDecimal convertedValue = budgetValue;

    if ( currentMediaValue != null && targetMediaValue != null )
    {
      BigDecimal conversionRatio = calculateConversionRatio( currentMediaValue, targetMediaValue );
      convertedValue = applyMediaConversion( budgetValue, conversionRatio );
    }

    return convertedValue;
  }

  /**
   * Apply the given <code>conversionRatio</code> to the given <code>budgetValue</code>.
   * This will return a <code>BigDecimal</code> representing the <code>budgetValue</code> in locale-specific units.
   * If <code>conversionRatio</code> is NULL will return <code>budgetValue</code> with no modifications.
   *
   * @param budgetValue - the value of the budget to convert
   * @param conversionRatio - the ratio for converting the budget from it's current units to the correct locale-specific units
   * @return BigDecimal - the value of the converted budget
   */
  public static BigDecimal applyMediaConversion( BigDecimal budgetValue, BigDecimal conversionRatio )
  {
    return conversionRatio == null ? budgetValue : conversionRatio.multiply( budgetValue, MEDIA_CONVERSION_CONTEXT );
  }

  /**
   * Apply the media conversion ratio resulting from dividing the <code>currentMediaValue</code> by the
   * <code>targetMediaValue</code>.  Order matters.  If either parameter is NULL, will return a ration of 1.00.
   *
   * @param currentMediaValue - the media value of the country the budget is currently represented in
   * @param targetMediaValue - the media value of the country you want to convert to
   * @return BigDecimal - the ratio for converting from the <code>currentMediaValue</code> to the <code>targetMediaValue</code>
   */
  public static BigDecimal calculateConversionRatio( BigDecimal currentMediaValue, BigDecimal targetMediaValue )
  {
    BigDecimal conversionRatio = BigDecimal.ONE;

    if ( currentMediaValue != null && targetMediaValue != null )
    {
      conversionRatio = currentMediaValue.divide( targetMediaValue, MEDIA_CONVERSION_CONTEXT );
    }

    return conversionRatio;
  }

  public static BigDecimal getBudgetConversionRatio( UserService userService, Long receiverId, Long submitterId )
  {
    BigDecimal conversionRatio = BigDecimal.ONE;

    if ( receiverId != null && submitterId != null )
    {
      BigDecimal recipientMediaValue = userService.getBudgetMediaValueForUser( receiverId );
      BigDecimal submitterMediaValue = userService.getBudgetMediaValueForUser( submitterId );
      conversionRatio = BudgetUtils.calculateConversionRatio( recipientMediaValue, submitterMediaValue );
    }
    return conversionRatio;
  }

  public static BigDecimal getBudgetConversionRatio( UserService userService, PromotionService promotionService, Long promotionId, Long receiverId, Long submitterId )
  {
    boolean calculateConversionRatio = false;
    if ( promotionId != null )
    {
      Promotion promotion = promotionService.getPromotionById( promotionId );
      calculateConversionRatio = promotion.getBudgetMaster() != null && !promotion.getBudgetMaster().isCentralBudget();
    }
    return calculateConversionRatio ? BudgetUtils.getBudgetConversionRatio( userService, receiverId, submitterId ) : BigDecimal.ONE;
  }
}
