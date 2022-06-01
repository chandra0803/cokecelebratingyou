/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/domain/audit/SalesPayoutCalculationAudit.java,v $
 */

package com.biperf.core.domain.audit;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;

/*
 * SalesPayoutCalculationAudit <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Aug
 * 18, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class SalesPayoutCalculationAudit extends ClaimBasedPayoutCalculationAudit
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The participant who receives the payout if the promotion payout calculation succeeds.
   */
  private Participant participant;

  /**
   * Defines the payout.
   */
  private PromotionPayoutGroup promotionPayoutGroup;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns the participant who receives the payout if the promotion payout calculation succeeds.
   * 
   * @return the participant who receives the payout if the promotion payout calculation succeeds.
   */
  public Participant getParticipant()
  {
    return participant;
  }

  /**
   * Returns promotion payout group used to calculate the payout.
   * 
   * @return promotion payout group used to calculate the payout.
   */
  public PromotionPayoutGroup getPromotionPayoutGroup()
  {
    return promotionPayoutGroup;
  }

  /**
   * Sets the participant who receives the payout if the promotion payout calculation succeeds.
   * 
   * @param participant the participant who receives the payout if the promotion payout calculation
   *          succeeds.
   */
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  /**
   * Sets promotion payout group used to calculate the payout.
   * 
   * @param promotionPayoutGroup promotion payout group used to calculate the payout.
   */
  public void setPromotionPayoutGroup( PromotionPayoutGroup promotionPayoutGroup )
  {
    this.promotionPayoutGroup = promotionPayoutGroup;
  }

  // ---------------------------------------------------------------------------
  // Equals, Hashcode, and To String Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns true if this object equals the given object; returns false otherwise.
   * 
   * @param object the object to be compared to this object.
   * @return true if this object equals the given object; false otherwise.
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof SalesPayoutCalculationAudit ) )
    {
      return false;
    }

    final SalesPayoutCalculationAudit that = (SalesPayoutCalculationAudit)object;

    if ( this.getParticipant() != null ? !this.getParticipant().equals( that.getParticipant() ) : that.getParticipant() != null )
    {
      return false;
    }

    if ( this.getClaim() != null ? !this.getClaim().equals( that.getClaim() ) : that.getClaim() != null )
    {
      return false;
    }

    if ( this.getPromotionPayoutGroup() != null ? !this.getPromotionPayoutGroup().equals( that.getPromotionPayoutGroup() ) : that.getPromotionPayoutGroup() != null )
    {
      return false;
    }

    return true;
  }

  /**
   * Returns the hashcode for this object.
   * 
   * @return the hashcode for this object.
   */
  public int hashCode()
  {
    int result;

    result = this.getParticipant() != null ? this.getParticipant().hashCode() * 13 : 0;
    result += this.getClaim() != null ? this.getClaim().hashCode() : 0;
    result += this.getPromotionPayoutGroup() != null ? this.getPromotionPayoutGroup().hashCode() : 0;

    return result;
  }
}
