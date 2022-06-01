/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Map;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.participant.Participant;

/**
 * SalesFacts.
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
 * <td>wadzinsk</td>
 * <td>Aug 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SalesFacts implements PayoutCalculationFacts
{
  private Claim claim;
  private Participant participant;
  private Participant manager;
  private Map minQualifierStatusByPromoPayoutGroup;

  /**
   * Constructs a <code>SalesFacts</code> object.
   * 
   * @param claim
   * @param participant
   * @param manager
   * @param minQualifierStatusByPromoPayoutGroup
   */
  public SalesFacts( Claim claim, Participant participant, Participant manager, Map minQualifierStatusByPromoPayoutGroup )
  {
    super();

    this.claim = claim;
    this.participant = participant;
    this.manager = manager;
    this.minQualifierStatusByPromoPayoutGroup = minQualifierStatusByPromoPayoutGroup;
  }

  /**
   * Returns the claim.
   * 
   * @return value of claim property
   */
  public Claim getClaim()
  {
    return claim;
  }

  /**
   * Sets the claim.
   * 
   * @param claim value for claim property
   */
  public void setClaim( Claim claim )
  {
    this.claim = claim;
  }

  /**
   * Returns the participant.
   * 
   * @return value of participant property
   */
  public Participant getParticipant()
  {
    return participant;
  }

  /**
   * Sets the participant.
   * 
   * @param participant value for participant property
   */
  public void setParticipant( Participant participant )
  {
    this.participant = participant;
  }

  /**
   * @return value of manager property
   */
  public Participant getManager()
  {
    return manager;
  }

  /**
   * @param manager value for the manager property
   */
  public void setManager( Participant manager )
  {
    this.manager = manager;
  }

  /**
   * @return value of minQualifierStatusByPromoPayoutGroup property
   */
  public Map getMinQualifierStatusByPromoPayoutGroup()
  {
    return minQualifierStatusByPromoPayoutGroup;
  }

  /**
   * @param minQualifierStatusByPromoPayoutGroup value for minQualifierStatusByPromoPayoutGroup
   *          property
   */
  public void setMinQualifierStatusByPromoPayoutGroup( Map minQualifierStatusByPromoPayoutGroup )
  {
    this.minQualifierStatusByPromoPayoutGroup = minQualifierStatusByPromoPayoutGroup;
  }

}
