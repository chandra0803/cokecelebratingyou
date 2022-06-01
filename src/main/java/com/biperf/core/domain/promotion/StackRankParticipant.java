/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.promotion;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.participant.Participant;

/*
 * StackRankParticipant <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar
 * 6, 2006</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankParticipant extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The stack rank list that contains this entry.
   */
  private StackRankNode stackRankNode;

  /**
   * The participant being ranked.
   */
  private Participant participant;

  /**
   * The value on which the participant is ranked.
   */
  private int stackRankFactor;

  /**
   * The participant's stack rank.
   */
  private int rank;

  /**
   * If true, then the participant shares his or her rank with one or more participants; if false,
   * then the participant is the only one who holds this rank.
   */
  private boolean tied;

  /**
   * The amount that the participant receives for holding his or her rank.
   */
  private Long payout;

  // ---------------------------------------------------------------------------
  // Equals and Hash Code Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackRankParticipant ) )
    {
      equals = false;
    }
    else
    {
      StackRankParticipant that = (StackRankParticipant)object;

      if ( stackRankNode != null && !stackRankNode.equals( that.getStackRankNode() ) || participant != null && !participant.equals( that.getParticipant() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  public int hashCode()
  {
    int hashCode = 0;

    if ( stackRankNode != null && participant != null )
    {
      hashCode = stackRankNode.hashCode() + participant.hashCode();
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

  public int getRank()
  {
    return rank;
  }

  public int getStackRankFactor()
  {
    return stackRankFactor;
  }

  public StackRankNode getStackRankNode()
  {
    return stackRankNode;
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

  public void setRank( int rank )
  {
    this.rank = rank;
  }

  public void setStackRankFactor( int stackRankFactor )
  {
    this.stackRankFactor = stackRankFactor;
  }

  public void setStackRankNode( StackRankNode stackRankNode )
  {
    this.stackRankNode = stackRankNode;
  }
}
