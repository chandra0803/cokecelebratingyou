/**
 * 
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

/**
 * PromotionApprovalParticipant.
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
public abstract class PromotionApprovalParticipant extends BaseDomain
{

  private Promotion promotion;
  private Participant participant;

  /**
   * Overridden from
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   * @param object
   * @return true if equal
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    // TODO: null should be passed in
    if ( object == null )
    {
      return false;
    }

    PromotionApprovalParticipant promotionApprovalParticipant = (PromotionApprovalParticipant)object;

    if ( promotion != null && !this.promotion.equals( promotionApprovalParticipant.getPromotion() ) )
    {
      return false;
    }

    if ( participant != null && !this.participant.equals( promotionApprovalParticipant.getParticipant() ) )
    {
      return false;
    }

    return true;
  }

  /**
   * Overridden from
   * 
   * @see java.lang.Object#hashCode()
   * @return int the hashCode
   */
  public int hashCode()
  {
    int hashCode = 0;

    hashCode += getPromotion() != null ? getPromotion().hashCode() * 13 : 0;
    hashCode += getParticipant() != null ? getParticipant().hashCode() * 17 : 0;

    return hashCode;
  }

  /**
   * @return promotion
   */
  public Promotion getPromotion()
  {
    return promotion;
  }

  /**
   * @param promotion
   */
  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  /**
   * @return participant
   */
  public Participant getParticipant()
  {
    return participant;
  }

  /**
   * @param participant
   */
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

}
