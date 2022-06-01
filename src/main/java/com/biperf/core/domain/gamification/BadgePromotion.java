
package com.biperf.core.domain.gamification;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;

/**
 * UserRole.
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
 * <td>sharafud</td>
 * <td>Aug 22, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BadgePromotion extends BaseDomain
{

  /** serialVersionUID */
  private static final long serialVersionUID = 3905522717791630136L;

  /** promotion */
  private Promotion eligiblePromotion;

  /** badge */
  private Badge badgePromotion;

  public Promotion getEligiblePromotion()
  {
    return eligiblePromotion;
  }

  public void setEligiblePromotion( Promotion eligiblePromotion )
  {
    this.eligiblePromotion = eligiblePromotion;
  }

  public Badge getBadgePromotion()
  {
    return badgePromotion;
  }

  public void setBadgePromotion( Badge badgePromotion )
  {
    this.badgePromotion = badgePromotion;
  }

  /**
   * Builds a String representation of this class. Overridden from
   * 
   * @see java.lang.Object#toString()
   * @return String
   */
  public String toString()
  {
    return ToStringBuilder.reflectionToString( BadgePromotion.class );
  }

  /**
   * Checks equality of the object parameter to this.
   * 
   * @param o
   * @return boolean
   */
  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof BadgePromotion ) )
    {
      return false;
    }

    final BadgePromotion badgePromotion = (BadgePromotion)o;

    if ( getBadgePromotion() != null ? !getBadgePromotion().equals( badgePromotion.getBadgePromotion() ) : badgePromotion.getBadgePromotion() != null )
    {
      return false;
    }
    if ( getEligiblePromotion() != null ? !getEligiblePromotion().equals( badgePromotion.getEligiblePromotion() ) : badgePromotion.getEligiblePromotion() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Define the hashCode from the id. Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result;
    result = getBadgePromotion() != null ? getBadgePromotion().hashCode() : 0;
    result = 29 * result + ( getEligiblePromotion() != null ? getEligiblePromotion().hashCode() : 0 );

    return result;
  }
}
