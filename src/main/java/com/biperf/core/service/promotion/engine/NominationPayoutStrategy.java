/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/NominationPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.activity.NominationActivity;
import com.biperf.core.domain.audit.ClaimBasedPayoutCalculationAudit;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.promotion.AbstractRecognitionPromotion;
import com.biperf.core.domain.promotion.Promotion;

/**
 * RecognitionPayoutStrategy.
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
 * <td>zahler</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NominationPayoutStrategy extends AbstractPayoutStrategy
{
  protected Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    NominationFacts nominationFacts = (NominationFacts)payoutCalculationFacts;

    Set payoutCalculationResults = new LinkedHashSet();

    Iterator activitiesIterator = activities.iterator();
    while ( activitiesIterator.hasNext() )
    {
      NominationActivity activity = (NominationActivity)activitiesIterator.next();

      PayoutCalculationResult payoutCalculationResult = new PayoutCalculationResult();

      // Only submitters get a payout, and only when awards are active.
      if ( !activity.isSubmitter() && ( (AbstractRecognitionPromotion)promotion ).isAwardActive() )
      {
        if ( activity.getAwardQuantity() != null )
        {
          payoutCalculationResult.setCalculatedPayout( activity.getAwardQuantity() );
        }
        else if ( activity.getCashAwardQuantity() != null )
        {
          payoutCalculationResult.setCalculatedCashPayout( activity.getCashAwardQuantity() );
        }
        payoutCalculationResult.addContributingActivity( activity );

        ClaimBasedPayoutCalculationAudit audit = new ClaimBasedPayoutCalculationAudit();
        if ( nominationFacts.getApprovable() instanceof ClaimGroup )
        {
          audit.setClaimGroup( (ClaimGroup)nominationFacts.getApprovable() );
        }
        else
        {
          audit.setClaim( (Claim)nominationFacts.getApprovable() );
        }
        audit.setParticipant( nominationFacts.getParticipant() );
        if ( activity.getAwardQuantity() != null )
        {
          audit.setReason( PayoutCalculationAuditReasonType.NOMINATION_SUCCESS, new String[] { String.valueOf( activity.getAwardQuantity() ) } );
        }
        else
        {
          audit.setReason( PayoutCalculationAuditReasonType.NOMINATION_SUCCESS, new String[] { String.valueOf( activity.getCashAwardQuantity() ) } );
        }
        payoutCalculationResult.setPayoutCalculationAudit( audit );

        payoutCalculationResults.add( payoutCalculationResult );
      }
    }

    return payoutCalculationResults;
  }
}
