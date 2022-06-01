/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.promotion;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.hierarchy.Node;

/*
 * StackRankNode <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Mar 6, 2006</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class StackRankNode extends BaseDomain
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The stack ranking that contains this stack rank list.
   */
  private StackRank stackRank;

  /**
   * All participants on this stack rank list are associated with this node.
   */
  private Node node;

  /**
   * A list of participants associated with the node referenced by field "node" sorted by stack
   * rank, as a <code>Set</code> of {@link StackRankParticipant} objects.
   */
  private Set stackRankParticipants = new LinkedHashSet();

  // ---------------------------------------------------------------------------
  // Equals and Hash Code Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackRankNode ) )
    {
      equals = false;
    }
    else
    {
      StackRankNode that = (StackRankNode)object;

      if ( stackRank != null && !stackRank.equals( that.getStackRank() ) || node != null && !node.equals( that.getNode() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  public int hashCode()
  {
    int hashCode = 0;

    if ( stackRank != null && node != null )
    {
      hashCode = stackRank.hashCode() + node.hashCode();
    }

    return hashCode;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public void addStackRankParticipant( StackRankParticipant stackRankParticipant )
  {
    stackRankParticipant.setStackRankNode( this );
    stackRankParticipants.add( stackRankParticipant );
  }

  public Node getNode()
  {
    return node;
  }

  public StackRank getStackRank()
  {
    return stackRank;
  }

  public Set getStackRankParticipants()
  {
    return stackRankParticipants;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public void setStackRank( StackRank stackRank )
  {
    this.stackRank = stackRank;
  }

  public void setStackRankParticipants( Set stackRankParticipants )
  {
    this.stackRankParticipants = stackRankParticipants;
  }
}
