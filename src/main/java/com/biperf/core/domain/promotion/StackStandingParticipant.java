
package com.biperf.core.domain.promotion;

import java.math.BigDecimal;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

@SuppressWarnings( "serial" )
public class StackStandingParticipant extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The stack rank list that contains this entry.
   */
  private StackStandingNode stackStandingNode;

  /**
   * The participant being ranked.
   */
  private Participant participant;

  /**
   * The value on which the participant is ranked.
   */
  private BigDecimal stackStandingFactor;

  /**
   * The participant's stack rank.
   */
  private int standing;

  /**
   * If true, then the participant shares his or her rank with one or more participants; if false,
   * then the participant is the only one who holds this rank.
   */
  private boolean tied;

  /**
   * The amount that the participant receives for holding his or her rank.
   */
  private Long payout;
  /**
   * Identifies the current pay out state of this stack ranking pax.
   */
  private boolean payoutsIssued = false;

  // ---------------------------------------------------------------------------
  // Equals and Hash Code Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackStandingParticipant ) )
    {
      equals = false;
    }
    else
    {
      StackStandingParticipant that = (StackStandingParticipant)object;

      if ( stackStandingNode != null && !stackStandingNode.equals( that.getStackStandingNode() ) || participant != null && !participant.equals( that.getParticipant() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  public int hashCode()
  {
    int hashCode = 0;

    if ( stackStandingNode != null && participant != null )
    {
      hashCode = stackStandingNode.hashCode() + participant.hashCode();
    }

    return hashCode;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public boolean isTied()
  {
    return tied;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public Long getPayout()
  {
    return payout;
  }

  public int getStanding()
  {
    return standing;
  }

  public BigDecimal getStackStandingFactor()
  {
    return stackStandingFactor;
  }

  public StackStandingNode getStackStandingNode()
  {
    return stackStandingNode;
  }

  public void setTied( boolean tied )
  {
    this.tied = tied;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  public void setPayout( Long payout )
  {
    this.payout = payout;
  }

  public void setStanding( int standing )
  {
    this.standing = standing;
  }

  public void setStackStandingFactor( BigDecimal stackStandingFactor )
  {
    this.stackStandingFactor = stackStandingFactor;
  }

  public void setStackStandingNode( StackStandingNode stackStandingNode )
  {
    this.stackStandingNode = stackStandingNode;
  }

  public boolean isPayoutsIssued()
  {
    return payoutsIssued;
  }

  public void setPayoutsIssued( boolean payoutsIssued )
  {
    this.payoutsIssued = payoutsIssued;
  }

}
