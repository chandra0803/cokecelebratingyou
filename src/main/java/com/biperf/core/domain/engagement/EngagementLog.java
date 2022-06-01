
package com.biperf.core.domain.engagement;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;

public class EngagementLog extends BaseDomain
{

  private static final long serialVersionUID = 1L;

  private Promotion promotion;
  private Node senderNodeId;
  private Participant receiverUserId;
  private Participant senderUserId;
  private Node receiverNodeId;
  private String behavior;

  public Promotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( Promotion promotion )
  {
    this.promotion = promotion;
  }

  public Node getSenderNodeId()
  {
    return senderNodeId;
  }

  public void setSenderNodeId( Node senderNodeId )
  {
    this.senderNodeId = senderNodeId;
  }

  public Participant getReceiverUserId()
  {
    return receiverUserId;
  }

  public void setReceiverUserId( Participant receiverUserId )
  {
    this.receiverUserId = receiverUserId;
  }

  public Participant getSenderUserId()
  {
    return senderUserId;
  }

  public void setSenderUserId( Participant senderUserId )
  {
    this.senderUserId = senderUserId;
  }

  public Node getReceiverNodeId()
  {
    return receiverNodeId;
  }

  public void setReceiverNodeId( Node receiverNodeId )
  {
    this.receiverNodeId = receiverNodeId;
  }

  public String getBehavior()
  {
    return behavior;
  }

  public void setBehavior( String behavior )
  {
    this.behavior = behavior;
  }

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    else
    {
      if ( object instanceof EngagementLog )
      {
        EngagementLog castObject = (EngagementLog)object;
        if ( promotion.equals( castObject.promotion ) && senderNodeId.equals( castObject.getSenderNodeId() ) && receiverUserId.equals( castObject.getReceiverUserId() )
            && behavior.equals( castObject.getBehavior() ) )
        {
          return true;
        }
      }
    }
    // TODO DMR: always false
    return false;
  }

  @Override
  public int hashCode()
  {
    return 0;
  }

}
