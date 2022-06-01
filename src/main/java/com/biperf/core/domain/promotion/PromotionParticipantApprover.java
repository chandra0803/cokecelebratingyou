/**
 * 
 */

package com.biperf.core.domain.promotion;

/**
 * PromotionParticipantApprover.
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
 * <td>asondgeroth</td>
 * <td>Aug 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionParticipantApprover extends PromotionApprovalParticipant implements Cloneable
{
  public static final String PROMO_PARTICIPANT_TYPE = "APPROVER";

  public PromotionParticipantApprover()
  {
    // empty constructor
  }

  public PromotionParticipantApprover( PromotionParticipantApprover toCopy )
  {

    this.setPromotion( toCopy.getPromotion() );
    this.setParticipant( toCopy.getParticipant() );
  }

  public Object clone() throws CloneNotSupportedException
  {

    PromotionParticipantApprover promotionParticipantApprover = (PromotionParticipantApprover)super.clone();
    promotionParticipantApprover.setPromotion( null );
    promotionParticipantApprover.resetBaseDomain();

    return promotionParticipantApprover;

  }

  /**
   * Check for equality. Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof PromotionParticipantApprover ) )
    {
      return false;
    }

    return super.equals( object );

  }

  public int hashCode()
  {
    return super.hashCode();
  }

}
