/**
 * 
 */

package com.biperf.core.domain.promotion;

/**
 * PromotionParticipantSubmitter.
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
public class PromotionParticipantSubmitter extends PromotionApprovalParticipant implements Cloneable
{
  public static final String PROMO_PARTICIPANT_TYPE = "SUBMITTER";

  public PromotionParticipantSubmitter()
  {
    // empty constructor
  }

  public PromotionParticipantSubmitter( PromotionParticipantSubmitter toCopy )
  {

    this.setPromotion( toCopy.getPromotion() );
    this.setParticipant( toCopy.getParticipant() );

  }

  /**
   * Clone this when the promotion is cloned. Will return a clone without the promotion and the id,
   * version and audit info reset. Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {

    PromotionParticipantSubmitter clonedPromotionParticipantSubmitter = (PromotionParticipantSubmitter)super.clone();

    clonedPromotionParticipantSubmitter.setPromotion( null );
    clonedPromotionParticipantSubmitter.resetBaseDomain();

    return clonedPromotionParticipantSubmitter;

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

    if ( ! ( object instanceof PromotionParticipantSubmitter ) )
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
