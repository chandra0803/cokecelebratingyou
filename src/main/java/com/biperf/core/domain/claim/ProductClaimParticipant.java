/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/ProductClaimParticipant.java,v $
 */

package com.biperf.core.domain.claim;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.PromotionTeamPosition;

/**
 * ProductClaimParticipant to manage the relationship between claims and participants and their
 * roles in the claim.
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
 * <td>crosenquest</td>
 * <td>Jun 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductClaimParticipant extends BaseDomain
{

  private Claim claim;
  private Participant participant;
  private Node node;
  private PromotionTeamPosition promotionTeamPosition;
  private Long awardQuantity;
  private String optOut;
  // Client customization for WIP 58122
  private boolean levelPaxClaimed;
  private boolean optTraining;
  
  
  public String getOptOut()
  {
    return optOut;
  }

  public void setOptOut( String optOut )
  {
    this.optOut = optOut;
  }
  
  /**
   * Constructs a <code>ProductClaimParticipant</code> object.
   */
  public ProductClaimParticipant()
  {
    // empty default constructor
  }

  /**
   * Constructor to build this.
   * 
   * @param claim
   * @param participant
   * @param promotionTeamPosition
   */
  public ProductClaimParticipant( Claim claim, Participant participant, PromotionTeamPosition promotionTeamPosition )
  {
    this.claim = claim;
    this.participant = participant;
    this.promotionTeamPosition = promotionTeamPosition;
  }

  /**
   * Constructs a <code>ProductClaimParticipant</code> object.
   * 
   * @param participant
   * @param promotionTeamPosition
   */
  public ProductClaimParticipant( Participant participant, PromotionTeamPosition promotionTeamPosition )
  {
    this.participant = participant;
    this.promotionTeamPosition = promotionTeamPosition;
  }

  public Claim getClaim()
  {
    return claim;
  }

  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  public Participant getParticipant()
  {
    return participant;
  }

  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#equals(java.lang.Object)
   * @param object
   * @return boolean
   */
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }

    if ( ! ( object instanceof ProductClaimParticipant ) )
    {
      return false;
    }

    ProductClaimParticipant productClaimParticipant = (ProductClaimParticipant)object;

    if ( this.getClaim() != null ? !this.getClaim().equals( productClaimParticipant.getClaim() ) : productClaimParticipant.getClaim() != null )
    {
      return false;
    }

    if ( this.getParticipant() != null ? !this.getParticipant().equals( productClaimParticipant.getParticipant() ) : productClaimParticipant.getParticipant() != null )
    {
      return false;
    }

    return true;
  } // end equals

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.BaseDomain#hashCode()
   * @return int
   */
  public int hashCode()
  {
    int result = 0;

    result += this.getClaim() != null ? this.getClaim().hashCode() : 0;
    result += this.getParticipant() != null ? this.getParticipant().hashCode() : 13;

    return result;
  }

  /**
   * @return value of promotionTeamPosition property
   */
  public PromotionTeamPosition getPromotionTeamPosition()
  {
    return promotionTeamPosition;
  }

  /**
   * @param promotionTeamPosition value for promotionTeamPosition property
   */
  public void setPromotionTeamPosition( PromotionTeamPosition promotionTeamPosition )
  {
    this.promotionTeamPosition = promotionTeamPosition;
  }

  /**
   * @return value of node property
   */
  public Node getNode()
  {
    return node;
  }

  /**
   * @param node value for node property
   */
  public void setNode( Node node )
  {
    this.node = node;
  }

  public Long getAwardQuantity()
  {
    return awardQuantity;
  }

  public void setAwardQuantity( Long awardQuantity )
  {
    this.awardQuantity = awardQuantity;
  }

//Client customization for WIP #58122
public boolean isLevelPaxClaimed() {
	return levelPaxClaimed;
}

public void setLevelPaxClaimed(boolean levelPaxClaimed) {
	this.levelPaxClaimed = levelPaxClaimed;
}

public boolean isOptTraining() {
	return optTraining;
}

public void setOptTraining(boolean optTraining) {
	this.optTraining = optTraining;
}
}
