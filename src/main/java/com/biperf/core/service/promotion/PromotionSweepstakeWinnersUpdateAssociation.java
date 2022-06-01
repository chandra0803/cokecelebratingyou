/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/PromotionSweepstakeWinnersUpdateAssociation.java,v $
 *
 */

package com.biperf.core.service.promotion;

import java.util.HashMap;
import java.util.Iterator;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
import com.biperf.core.domain.promotion.PromotionSweepstakeWinner;
import com.biperf.core.service.UpdateAssociationRequest;

/**
 * PromotionSweepstakeUpdateAssociation <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>jenniget</td>
 * <td>Oct 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author jenniget
 *
 *          Exp $
 */
public class PromotionSweepstakeWinnersUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionSweepstakeWinnersUpdateAssociation( Promotion promotion )
  {
    super( promotion );
  }

  /**
   * execute the association request and update the specified promotion object into the associated
   * promotion object
   * 
   * @param attachedDomain
   */
  @SuppressWarnings( "rawtypes" )
  public void execute( BaseDomain attachedDomain )
  {
    Promotion attachedPromo = (Promotion)attachedDomain;
    Promotion detachedPromo = (Promotion)getDetachedDomain();

    // find the detached sweepstake that is unprocessed
    PromotionSweepstake detachedSweep = null;
    for ( Iterator iter = detachedPromo.getPromotionSweepstakes().iterator(); iter.hasNext(); )
    {
      PromotionSweepstake temp = (PromotionSweepstake)iter.next();
      if ( !temp.isProcessed() )
      {
        detachedSweep = temp;
        break;
      }
    }

    // find the attached sweepstake that is unprocessed
    PromotionSweepstake attachedSweep = null;
    for ( Iterator iter = attachedPromo.getPromotionSweepstakes().iterator(); iter.hasNext(); )
    {
      PromotionSweepstake temp = (PromotionSweepstake)iter.next();
      if ( !temp.isProcessed() )
      {
        attachedSweep = temp;
        break;
      }
    }

    HashMap detachedWinnersToUpdate = new HashMap();

    Iterator detachedWinnersIterator = null;
    if ( detachedSweep != null && detachedSweep.getWinners() != null )
    {
      detachedWinnersIterator = detachedSweep.getWinners().iterator();
    }
    if ( detachedWinnersIterator != null )
    {
      while ( detachedWinnersIterator.hasNext() )
      {
        PromotionSweepstakeWinner sweepstakeWinner = (PromotionSweepstakeWinner)detachedWinnersIterator.next();
        detachedWinnersToUpdate.put( sweepstakeWinner.getId(), sweepstakeWinner );
      }
    }

    // Update all of the PromotionSweepstakeWinners
    Iterator attachedWinnersIterator = null;
    if ( attachedSweep != null && attachedSweep.getWinners() != null )
    {
      attachedWinnersIterator = attachedSweep.getWinners().iterator();
    }
    if ( attachedWinnersIterator != null )
    {
      while ( attachedWinnersIterator.hasNext() )
      {
        PromotionSweepstakeWinner attachedWinner = (PromotionSweepstakeWinner)attachedWinnersIterator.next();

        if ( detachedSweep.getWinners().contains( attachedWinner ) ) // do this just for safety
        {
          PromotionSweepstakeWinner detachedWinner = (PromotionSweepstakeWinner)detachedWinnersToUpdate.get( attachedWinner.getId() );

          attachedWinner.setRemoved( detachedWinner.isRemoved() );
        }
      }
    }

    // now add in any new PromotionSweepstakeWinners (from a replace operation)
    if ( detachedSweep != null )
    {
      Iterator winnersToAddIterator = detachedSweep.getWinners().iterator();
      while ( winnersToAddIterator.hasNext() )
      {
        PromotionSweepstakeWinner detatchedWinner = (PromotionSweepstakeWinner)winnersToAddIterator.next();
        attachedSweep.addWinner( detatchedWinner );
      }
    }
  }
}
