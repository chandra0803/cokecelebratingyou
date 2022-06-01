/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.NominationClaim;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.promotion.PromotionService;

/**
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
 * <td>jenniget</td>
 * <td>Apr 24, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ExpireNominationsProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ExpireNominationsProcess.class );

  public static final String PROCESS_NAME = "Expire Nominations Process";
  public static final String BEAN_NAME = "expireNominationsProcess";

  private PromotionService promotionService;
  private ClaimService claimService;

  public void onExecute()
  {
    // TODO: Optimize with single query which gives open claims for nomination promotions
    PromotionQueryConstraint delayedApprovalPromotionQueryConstraint = new PromotionQueryConstraint();
    PromotionType[] types = new PromotionType[1];
    types[0] = PromotionType.lookup( PromotionType.NOMINATION );
    delayedApprovalPromotionQueryConstraint.setPromotionTypesIncluded( types );
    List promotions = promotionService.getPromotionList( delayedApprovalPromotionQueryConstraint );
    List claimsToUpdate = new ArrayList();

    for ( Iterator iter = promotions.iterator(); iter.hasNext(); )
    {
      Promotion promotion = (Promotion)iter.next();

      // No end date? No expiration!
      if ( promotion.getApprovalEndDate() != null )
      {
        ClaimQueryConstraint claimQueryConstraint = new ClaimQueryConstraint();
        claimQueryConstraint.setOpen( Boolean.TRUE );
        claimQueryConstraint.setIncludedPromotionIds( new Long[] { promotion.getId() } );

        AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
        associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.CLAIM_RECIPIENTS ) );
        List claims = claimService.getClaimListWithAssociations( claimQueryConstraint, associationRequestCollection );
        for ( Iterator iterator = claims.iterator(); iterator.hasNext(); )
        {
          NominationClaim claim = (NominationClaim)iterator.next();

          // Confirm that we are past the approval end date
          Date now = new Date();
          Date approvalEndDate = promotion.getApprovalEndDate();
          if ( now.after( approvalEndDate ) )
          {
            // expire recipients
            for ( Iterator recipIter = claim.getClaimRecipients().iterator(); recipIter.hasNext(); )
            {
              ClaimRecipient recipient = (ClaimRecipient)recipIter.next();
              recipient.setApprovalStatusType( ApprovalStatusType.lookup( ApprovalStatusType.EXPIRED ) );
            }
            claimsToUpdate.add( claim );
          }
        }
      }
    }
    try
    {
      // NOTE: could update each one on the fly if updateClaims() is long running.
      claimService.saveClaims( claimsToUpdate, null, null, false );
    }
    catch( ServiceErrorException e )
    {
      addComment( "System Error updating claims. See application log for stack trace" );
      throw new BeaconRuntimeException( "Error updating claims during job with processInvocationId: " + getProcessInvocationId(), e );
    }
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   * @param processInvocationId
   */
  public void onExecute( Long processInvocationId )
  {
    log.debug( "process " + processInvocationId + ":" + toString() );
    onExecute();
  }

  /**
   * @param promotionService value for promotionService property
   */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }
}
