/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/domain/claim/ProductClaim.java,v $
 */

package com.biperf.core.domain.claim;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.BooleanUtils;

import com.biperf.core.domain.enums.ApprovalStatusType;

/**
 * Claim.
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
public class ProductClaim extends Claim
{
  private Set claimProducts = new LinkedHashSet();
  private Set<ProductClaimParticipant> claimParticipants = new LinkedHashSet<ProductClaimParticipant>();
  private boolean managerAward = false;

  /**
   * non-persisted value used during claim payout processing that represents whether or not the
   * submitter has yet met the minimum qualifier(if one is used). if null when accessed by team
   * member, then error, since should be set be submitter.
   */
  private Boolean minimumQualifierMet;

  /**
   * Returns true if this claim can be deleted; otherwise it returns false.
   * <p>
   * A claim is deletable if it is open and the status of all of its claim/ products is pending or
   * hold.
   * </p>
   * 
   * @return true if this claim can be deleted; otherwise it returns false.
   */
  public boolean isDeletable()
  {

    boolean isDeletable = true;

    if ( !isOpen() )
    {
      isDeletable = false;
    }
    else
    {
      for ( Iterator iter = claimProducts.iterator(); iter.hasNext(); )
      {
        ClaimProduct product = (ClaimProduct)iter.next();
        if ( product.getApprovalStatusType().getCode().equals( ApprovalStatusType.APPROVED ) || product.getApprovalStatusType().getCode().equals( ApprovalStatusType.DENIED ) )
        {
          isDeletable = false;
          break;
        }
      }
    }

    return isDeletable;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * @param claimFormStep
   */
  public ProductClaim( ClaimFormStep claimFormStep )
  {
    super( claimFormStep );
  }

  /**
   * 
   */
  public ProductClaim()
  {
    super();
  }

  public void addClaimProduct( ClaimProduct claimProduct )
  {
    if ( null == claimProduct.getSerialId() || "".equals( claimProduct.getSerialId() ) )
    {
      claimProduct.setSerialId( String.valueOf( System.currentTimeMillis() % ( new Random().nextInt() + this.claimProducts.size() ) ) );
    }

    claimProduct.setClaim( this );
    this.claimProducts.add( claimProduct );
  }

  public Set getClaimProducts()
  {
    return this.claimProducts;
  }

  public void setClaimProducts( Set claimProducts )
  {
    this.claimProducts = claimProducts;
  }

  /**
   * Adds a team member to the set of team members who are entitled to receive percs as a result
   * this claim. A team member is a participant.
   * 
   * @param productClaimParticipant a team member who is entitled to recieve percs as a result of
   *          this claim.
   */
  public void addClaimParticipant( ProductClaimParticipant productClaimParticipant )
  {
    productClaimParticipant.setClaim( this );
    this.claimParticipants.add( productClaimParticipant );
  }

  /**
   * Returns the participants who receive percs as a result of this claim.
   * 
   * @return the participants who receive percs as a result of this claim as a <code>Set</code> of
   *         {@link ProductClaimParticipant} objects.
   */
  public Set<ProductClaimParticipant> getClaimParticipants()
  {
    return this.claimParticipants;
  }

  /**
   * Sets the participants who receive percs as a result of this claim.
   * 
   * @param claimParticipants the participants who receive percs as a result of this claim as a
   *          <code>Set</code> of {@link ProductClaimParticipant} objects.
   */
  public void setClaimParticipants( Set<ProductClaimParticipant> claimParticipants )
  {
    this.claimParticipants = claimParticipants;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.claim.Claim#isApprovableClaimType()
   */
  public boolean isApprovableClaimType()
  {
    return true;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.domain.claim.Claim#getApprovableItems()
   */
  public Set getApprovableItems()
  {
    return claimProducts;
  }

  /**
   * @return value of minimumQualifierMet property
   */
  public Boolean getMinimumQualifierMet()
  {
    return minimumQualifierMet;
  }

  /**
   * @param minimumQualifierMet value for minimumQualifierMet property
   */
  public void setMinimumQualifierMet( Boolean minimumQualifierMet )
  {
    this.minimumQualifierMet = minimumQualifierMet;
  }

  public boolean isMinimumQualifierMet()
  {
    return BooleanUtils.isTrue( minimumQualifierMet );
  }

  public boolean isManagerAward()
  {
    return managerAward;
  }

  public void setManagerAward( boolean managerAward )
  {
    this.managerAward = managerAward;
  }

}
