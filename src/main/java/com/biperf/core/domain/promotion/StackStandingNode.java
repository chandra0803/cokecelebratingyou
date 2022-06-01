
package com.biperf.core.domain.promotion;

import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.hierarchy.Node;
import com.objectpartners.cms.util.ContentReaderManager;

@SuppressWarnings( "serial" )
public class StackStandingNode extends BaseDomain
{

  /**
  * The stack ranking that contains this stack rank list.
  */
  private StackStanding stackStanding;

  /**
   * All participants on this stack rank list are associated with this node.
   */
  private Node node;

  /**
   * A list of participants associated with the node referenced by field "node" sorted by stack
   * rank, as a <code>Set</code> of {@link StackStandingParticipant} objects.
   */
  private Set<StackStandingParticipant> stackStandingParticipants = new LinkedHashSet<StackStandingParticipant>();

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  public void addStackStandingParticipant( StackStandingParticipant stackStandingParticipant )
  {
    stackStandingParticipant.setStackStandingNode( this );
    stackStandingParticipants.add( stackStandingParticipant );
  }

  public Node getNode()
  {
    return node;
  }

  public StackStanding getStackStanding()
  {
    return stackStanding;
  }

  public Set<StackStandingParticipant> getStackStandingParticipants()
  {
    return stackStandingParticipants;
  }

  public void setNode( Node node )
  {
    this.node = node;
  }

  public void setStackStanding( StackStanding stackStanding )
  {
    this.stackStanding = stackStanding;
  }

  public void setStackStandingParticipants( Set<StackStandingParticipant> stackStandingParticipants )
  {
    this.stackStandingParticipants = stackStandingParticipants;
  }

  // ---------------------------------------------------------------------------
  // Equals and Hash Code Methods
  // ---------------------------------------------------------------------------

  public boolean equals( Object object )
  {
    boolean equals = true;

    if ( ! ( object instanceof StackStandingNode ) )
    {
      equals = false;
    }
    else
    {
      StackStandingNode that = (StackStandingNode)object;

      if ( stackStanding != null && !stackStanding.equals( that.getStackStanding() ) || node != null && !node.equals( that.getNode() ) )
      {
        equals = false;
      }
    }

    return equals;
  }

  public int hashCode()
  {
    int hashCode = 0;

    if ( stackStanding != null && node != null )
    {
      hashCode = stackStanding.hashCode() + node.hashCode();
    }

    return hashCode;
  }

  public boolean isHierarchyRanking()
  {
    return node == null;
  }

  public String getNodeTypeName()
  {
    return node != null ? this.node.getNodeType().getNodeTypeName() : ContentReaderManager.getText( "system.general", "ALL" );
  }

  public String getNodeName()
  {
    return node != null ? node.getName() : ContentReaderManager.getText( "system.general", "ALL" );
  }

}
