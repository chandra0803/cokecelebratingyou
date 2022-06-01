
package com.biperf.core.service.activity;

import com.biperf.core.domain.activity.MerchOrderActivity;
import com.biperf.core.domain.promotion.StackRankNode;
import com.biperf.core.service.BaseAssociationRequest;

public class MerchOrderActivityAssociationRequest extends BaseAssociationRequest
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  public static final int ALL_HYDRATION_LEVEL = 1;

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private int hydrationLevel;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Constructs a <code>MerchOrderActivityAssociationRequest</code> object.
   *
   * @param hydrationLevel the hydration level.
   */
  public MerchOrderActivityAssociationRequest( int hydrationLevel )
  {
    this.hydrationLevel = hydrationLevel;
  }

  /**
   * Hydrate the given {@link StackRankNode} object.
   *
   * @param domainObject the {@link StackRankNode} object to hydrate.
   */
  public void execute( Object domainObject )
  {
    MerchOrderActivity merchOrderActivity = (MerchOrderActivity)domainObject;

    switch ( hydrationLevel )
    {
      case ALL_HYDRATION_LEVEL:
        hydrateMerchOrderActivity( merchOrderActivity );
        break;
      default:
        break;
    }
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private void hydrateMerchOrderActivity( MerchOrderActivity merchOrderActivity )
  {
    if ( merchOrderActivity == null )
    {
      return;
    }
    initialize( merchOrderActivity.getMerchOrder() );
  }
}
