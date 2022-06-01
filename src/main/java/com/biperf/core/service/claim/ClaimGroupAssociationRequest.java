/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/claim/ClaimGroupAssociationRequest.java,v $
 */

package com.biperf.core.service.claim;

import java.util.Iterator;
import java.util.Set;

import com.biperf.core.domain.claim.ApprovableItem;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.promotion.NominationPromotion;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * ClaimAssociationRequest.
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
 * <td>robinsra</td>
 * <td>Jul 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimGroupAssociationRequest extends BaseAssociationRequest
{

  private int hydrateLevel = 0;

  private ClaimAssociationRequest claimAssociationRequest;

  public static final int ALL = 1;
  public static final int ALL_WITH_CLAIMS_ALL = 2;
  public static final int NOMINEE = 3;
  public static final int NODE = 4;
  public static final int CLAIMS = 5;

  public ClaimGroupAssociationRequest( int hydrateLevel )
  {
    this.hydrateLevel = hydrateLevel;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.AssociationRequest#execute(Object)
   * @param domainObject
   */
  public void execute( Object domainObject )
  {
    if ( claimAssociationRequest != null )
    {
      Set claimGroupClaims = ( (ClaimGroup)domainObject ).getClaims();
      for ( Iterator iter = claimGroupClaims.iterator(); iter.hasNext(); )
      {
        Claim claimGroupClaim = (Claim)iter.next();
        claimAssociationRequest.execute( claimGroupClaim );
      }
    }
    else
    {
      ClaimGroup claimGroup = (ClaimGroup)domainObject;

      switch ( hydrateLevel )
      {
        case ALL:
        {
          hydrateAll( claimGroup );
          break;
        }
        case ALL_WITH_CLAIMS_ALL:
        {
          hydrateAll( claimGroup );
          hydrateClaimsAll( claimGroup );
          break;
        }
        case NOMINEE:
        {
          hydrateParticipant( claimGroup );
          hydrateParticipantInfo( claimGroup );
          break;
        }
        case NODE:
        {
          hydrateNode( claimGroup );
          break;
        }
        case CLAIMS:
        {
          hydrateClaims( claimGroup );
          break;
        }
        default:
        {
          break;
        }
      } // switch
    }
  }

  private void hydrateParticipant( ClaimGroup claimGroup )
  {
    initialize( claimGroup.getParticipant() );
  }

  private void hydrateParticipantInfo( ClaimGroup claimGroup )
  {
    initialize( claimGroup.getParticipant().getUserAddresses() );
    initialize( claimGroup.getParticipant().getParticipantEmployers() );
  }

  private void hydrateNode( ClaimGroup claimGroup )
  {
    initialize( claimGroup.getNode() );

  }

  private void hydrateAll( ClaimGroup claimGroup )
  {
    hydrateParticipant( claimGroup );
    hydrateNode( claimGroup );
    hydrateApprovableItems( claimGroup );
  }

  private void hydrateApprovableItems( ClaimGroup claimGroup )
  {
    for ( Iterator iter = claimGroup.getApprovableItems().iterator(); iter.hasNext(); )
    {
      ApprovableItem approvableItem = (ApprovableItem)iter.next();
      initialize( approvableItem.getApprovableItemApprovers() );
    }
  }

  private void hydrateClaims( ClaimGroup claimGroup )
  {
    for ( Iterator iter = claimGroup.getClaims().iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      initialize( claim );
      initialize( claim.getPromotion() );
      if ( claim.getPromotion().isNominationPromotion() )
      {
        NominationPromotion nominationPromotion = (NominationPromotion)claim.getPromotion();
        initialize( nominationPromotion.getNominationLevels() );
      }
    }
  }

  private void hydrateClaimsAll( ClaimGroup claimGroup )
  {
    ClaimAssociationRequest localClaimAssociationRequest = new ClaimAssociationRequest( ClaimAssociationRequest.ALL );
    for ( Iterator iter = claimGroup.getClaims().iterator(); iter.hasNext(); )
    {
      Claim claim = (Claim)iter.next();
      localClaimAssociationRequest.execute( claim );
    }
  }

}
