/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ManagerOverridePayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.audit.SimplePayoutCalculationAudit;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;

/*
 * ManagerOverridePayoutStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug
 * 25, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ManagerOverridePayoutStrategy extends AbstractPayoutStrategy
{
  public static final Log log = LogFactory.getLog( ManagerOverridePayoutStrategy.class );

  /**
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @return Set
   */
  protected Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    if ( activities.isEmpty() )
    {
      throw new BeaconRuntimeException( "ManagerOverridePayoutStrategy input activity set must contain at least one activity, was empty" );
    }

    Set payoutCalculationResults = new LinkedHashSet();

    ManagerOverrideFacts managerOverrideFacts = (ManagerOverrideFacts)payoutCalculationFacts;

    PayoutCalculationResult payoutCalculationResult = processPayout( activities, managerOverrideFacts );
    payoutCalculationResults.add( payoutCalculationResult );

    return payoutCalculationResults;
  }

  protected PayoutCalculationResult processPayout( Set activities, ManagerOverrideFacts facts )
  {

    long totalPayout = 0;
    Iterator iter = activities.iterator();
    while ( iter.hasNext() )
    {
      ManagerOverrideActivity overrideActivity = (ManagerOverrideActivity)iter.next();
      totalPayout += overrideActivity.getSubmitterPayout().longValue();
    }
    ProductClaimPromotion promotion = (ProductClaimPromotion) ( (ManagerOverrideActivity)activities.iterator().next() ).getPromotion();
    Integer payoutFactor = promotion.getPayoutManagerPercent();
    Double rawPayout = new Double( totalPayout * ( payoutFactor.doubleValue() / 100 ) );
    int roundPoint = Integer.parseInt( rawPayout.toString().substring( rawPayout.toString().indexOf( '.' ) + 1, rawPayout.toString().indexOf( '.' ) + 2 ) );
    if ( roundPoint > 0 && roundPoint < 5 )
    {
      // Add .5 to the raw number for the rounding to make sure all things round up.
      rawPayout = new Double( rawPayout.doubleValue() + .5 );
    }
    long payout = Math.round( rawPayout.doubleValue() );

    PayoutCalculationResult result = new PayoutCalculationResult();
    result.setCalculatedPayout( new Long( payout ) );
    result.setContributingActivities( activities );
    SimplePayoutCalculationAudit audit = new SimplePayoutCalculationAudit();
    // Calculation produced non-zero payout.
    audit.setProcessStartDate( facts.getStartDate() );
    audit.setProcessEndDate( facts.getEndDate() );
    audit.setReason( PayoutCalculationAuditReasonType.MANAGER_OVERRIDE,
                     new String[] { String.valueOf( totalPayout ), String.valueOf( payoutFactor ), facts.getStartDate().toString(), facts.getEndDate().toString() } );
    audit.setParticipant( facts.getManager() );
    log.info( "Manager Id:" + facts.getManager().getId() + "  calculated payout: " + payout );
    result.setPayoutCalculationAudit( audit );

    return result;
  }
}
