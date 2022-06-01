/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/PromotionSweepstakeUpdateAssociation.java,v $
 *
 */

package com.biperf.core.service.promotion;

import java.util.Iterator;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionSweepstake;
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
 * <td>Nov 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author jenniget
 *
 */
public class PromotionSweepstakeUpdateAssociation extends UpdateAssociationRequest
{
  /**
   * Constructor
   * 
   * @param promotion
   */
  public PromotionSweepstakeUpdateAssociation( Promotion promotion )
  {
    super( promotion );
  }

  /**
   * execute the association request and update the specified promotion object into the associated
   * promotion object
   * 
   * @param attachedDomain
   */
  public void execute( BaseDomain attachedDomain )
  {
    Promotion detachedPromo = (Promotion)getDetachedDomain();

    Promotion attachedPromo = (Promotion)attachedDomain;

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

    if ( detachedSweep == null )
    { // detached promotion has no unprocessed
     // find the attached that is unprocessed and flip its flag
      for ( Iterator iter = attachedPromo.getPromotionSweepstakes().iterator(); iter.hasNext(); )
      {
        PromotionSweepstake temp = (PromotionSweepstake)iter.next();
        if ( !temp.isProcessed() )
        {
          temp.setProcessed( true );
          break;
        }
      }
    }
    else
    { // detached promo has an unprocessed sweepstake
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

      if ( attachedSweep == null )
      { // attached has no unprocessed
       // add the sweepstake from detached promotion
        attachedPromo.addPromotionSweepstake( detachedSweep );
      }
      else
      {
        // copy over dates and winners from detached
        attachedSweep.setWinners( detachedSweep.getWinners() );
        attachedSweep.setEndDate( detachedSweep.getEndDate() );
        attachedSweep.setStartDate( detachedSweep.getStartDate() );
      }
    }
  }
}
