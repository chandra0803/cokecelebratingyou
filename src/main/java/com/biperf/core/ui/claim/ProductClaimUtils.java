/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claim/ProductClaimUtils.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.claim.ProductClaimParticipant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionTeamPosition;

/**
 * ProductClaimUtils. Contains common code used by claim user interface classes.
 * </p>
 * <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Thomas Eaton</td>
 * <td>Aug 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ProductClaimUtils
{
  /**
   * Returns a list of claim participants constructed for display: If the promotion collects claim
   * participants by group, this method returns the claim's claim participants; if the promotion
   * collects claim participants by role, this method returns a claim participant for each claim
   * participant role type--whether or not a participant is assigned to the role.
   * 
   * @param productClaim
   * @return a <code>List</code> of {@link com.biperf.core.domain.claim.ProductClaimParticipant}
   *         objects.
   */
  public static List getClaimParticipantList( ProductClaim productClaim )
  {
    List claimParticipantList = null;

    ProductClaimPromotion pcPromotion = (ProductClaimPromotion)productClaim.getPromotion();

    if ( pcPromotion.isTeamCollectedAsGroup() )
    {
      claimParticipantList = new ArrayList( productClaim.getClaimParticipants() );
    }
    else
    {
      claimParticipantList = new ArrayList();

      // Create a claim participant for each claim participant position.
      for ( Iterator iter = pcPromotion.getPromotionTeamPositions().iterator(); iter.hasNext(); )
      {
        PromotionTeamPosition promotionTeamPosition = (PromotionTeamPosition)iter.next();

        ProductClaimParticipant claimParticipant = new ProductClaimParticipant();
        claimParticipant.setPromotionTeamPosition( promotionTeamPosition );
        claimParticipantList.add( claimParticipant );
      }

      // Set the claim participant's participant.
      Iterator iter2 = productClaim.getClaimParticipants().iterator();
      while ( iter2.hasNext() )
      {
        ProductClaimParticipant claimParticipant = (ProductClaimParticipant)iter2.next();
        PromotionTeamPosition promotionTeamPosition = claimParticipant.getPromotionTeamPosition();

        Iterator iter3 = claimParticipantList.iterator();
        while ( iter3.hasNext() )
        {
          ProductClaimParticipant listClaimParticipant = (ProductClaimParticipant)iter3.next();
          PromotionTeamPosition listClaimPromotionTeamPosition = listClaimParticipant.getPromotionTeamPosition();
          if ( listClaimPromotionTeamPosition.equals( promotionTeamPosition ) )
          {
            listClaimParticipant.setParticipant( claimParticipant.getParticipant() );
            break;
          }
        }
      }
    }

    return claimParticipantList;
  }

}
