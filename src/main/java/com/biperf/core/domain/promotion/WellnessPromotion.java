
package com.biperf.core.domain.promotion;

import java.util.List;

/**
 * WellnessPromotion.
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
 * <td>Bala</td>
 * <td>July 14, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class WellnessPromotion extends Promotion
{

  private boolean includeBanner;

  private boolean awardActive;
  private boolean awardAmountTypeFixed;

  private Long awardAmountFixed;
  private Long awardAmountMin;
  private Long awardAmountMax;

  /**
   * Constructor
   */
  public WellnessPromotion()
  {
    super();
  }

  public boolean isIncludeBanner()
  {
    return includeBanner;
  }

  public void setIncludeBanner( boolean includeBanner )
  {
    this.includeBanner = includeBanner;
  }

  public boolean isAwardActive()
  {
    return awardActive;
  }

  public void setAwardActive( boolean awardActive )
  {
    this.awardActive = awardActive;
  }

  public Long getAwardAmountFixed()
  {
    return awardAmountFixed;
  }

  public void setAwardAmountFixed( Long awardAmountFixed )
  {
    this.awardAmountFixed = awardAmountFixed;
  }

  public Long getAwardAmountMin()
  {
    return awardAmountMin;
  }

  public void setAwardAmountMin( Long awardAmountMin )
  {
    this.awardAmountMin = awardAmountMin;
  }

  public Long getAwardAmountMax()
  {
    return awardAmountMax;
  }

  public void setAwardAmountMax( Long awardAmountMax )
  {
    this.awardAmountMax = awardAmountMax;
  }

  public boolean isAwardAmountTypeFixed()
  {
    /*
     * if(this.calculator != null ) return calculator.isAwardTypeFixed();
     */
    return awardAmountTypeFixed;
  }

  public void setAwardAmountTypeFixed( boolean awardAmountTypeFixed )
  {
    this.awardAmountTypeFixed = awardAmountTypeFixed;
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

    WellnessPromotion clonedPromotion = (WellnessPromotion)super.deepCopy( cloneWithChildren, newPromotionName, newChildPromotionNameHolders );

    // award properties
    clonedPromotion.setAwardActive( this.isAwardActive() );
    clonedPromotion.setAwardAmountTypeFixed( this.isAwardAmountTypeFixed() );
    clonedPromotion.setAwardAmountFixed( this.getAwardAmountFixed() );
    clonedPromotion.setAwardAmountMax( this.getAwardAmountMax() );
    clonedPromotion.setAwardAmountMin( this.getAwardAmountMin() );

    return clonedPromotion;

  }

  /**
   * Returns false because wellness type promotions cannot have child
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
    return false;
  }

}
