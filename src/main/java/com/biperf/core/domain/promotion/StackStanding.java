
package com.biperf.core.domain.promotion;

import java.util.HashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;

@SuppressWarnings( "serial" )
public class StackStanding extends BaseDomain
{

  /**
   * Uniquely identifies this stack ranking. Acts as a surrogate business key.
   */
  private String guid;

  /**
   * Identifies the current pay out state of this stack ranking.
   */
  private boolean payoutsIssued = false;

  /**
   * Identifies the current active state of this stack ranking.
   */
  private boolean active = false;

  /**
   * The promotion whose sales activity is used to create this stack ranking.
   */
  private ThrowdownPromotion promotion;

  /**
   * The round whose sales activity is used to create this stack ranking.
   */
  private Integer roundNumber;

  /**   * Stack rank lists by node, as a <code>Set</code> of {@link StackStandingNode} objects.
   */
  private Set<StackStandingNode> stackStandingNodes = new HashSet<StackStandingNode>();

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public void addStackStandingNode( StackStandingNode stackStandingNode )
  {
    stackStandingNode.setStackStanding( this );
    stackStandingNodes.add( stackStandingNode );
  }

  public String getGuid()
  {
    return guid;
  }

  public Integer getRoundNumber()
  {
    return roundNumber;
  }

  public Set<StackStandingNode> getStackStandingNodes()
  {
    return stackStandingNodes;
  }

  public void setGuid( String guid )
  {
    this.guid = guid;
  }

  public void setRoundNumber( Integer roundNumber )
  {
    this.roundNumber = roundNumber;
  }

  public void setStackStandingNodes( Set<StackStandingNode> stackStandingNodes )
  {
    this.stackStandingNodes = stackStandingNodes;
  }

  public boolean isPayoutsIssued()
  {
    return payoutsIssued;
  }

  public void setPayoutsIssued( boolean payoutsIssued )
  {
    this.payoutsIssued = payoutsIssued;
  }

  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  // ---------------------------------------------------------------------------
  // Equals and Hash Code Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackStanding ) )
    {
      equals = false;
    }
    else
    {
      StackStanding that = (StackStanding)object;

      if ( guid != null && !guid.equals( that.getGuid() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  public int hashCode()
  {
    return guid.hashCode();
  }

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public boolean isEligibleForOverallBadges()
  {
    return roundNumber == promotion.getNumberOfRounds();
  }

}
