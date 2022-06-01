
package com.biperf.core.service.ssi;

import com.biperf.core.domain.ssi.SSIContestPaxClaim;
import com.biperf.core.service.BaseAssociationRequest;

/**
 * SSIContestPaxClaimAssociationRequest.
 * 
 * @author dudam
 * @since May 26, 2015
 * @version 1.0
 */
public class SSIContestPaxClaimAssociationRequest extends BaseAssociationRequest
{
  protected int hydrateLevel = 0;
  public static final int CLAIM_FIELDS = 1;

  /**
   * Constructor with hydrateLevel as argument
   *
   * @param hydrateLevel
   */
  public SSIContestPaxClaimAssociationRequest( int hydrateLevel )
  {
    super();
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
    SSIContestPaxClaim paxClaim = (SSIContestPaxClaim)domainObject;
    switch ( hydrateLevel )
    {
      case CLAIM_FIELDS:
        initialize( paxClaim.getPaxClaimFields() );
        break;
      default:
        break;
    }
  }

}
