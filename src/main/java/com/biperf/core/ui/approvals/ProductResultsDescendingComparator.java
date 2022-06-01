/**
 * 
 */

package com.biperf.core.ui.approvals;

import java.util.Comparator;

import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.Results;

/**
 * @author poddutur
 *
 */
public class ProductResultsDescendingComparator implements Comparator
{
  @Override
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof Results ) || ! ( o2 instanceof Results ) )
    {
      throw new ClassCastException( "Object is not a Results object!" );
    }
    Results result1 = (Results)o1;
    Results result2 = (Results)o2;

    if ( result1 != null && result2 != null )
    {
      if ( result1.getProductsList().get( 0 ).getApprover() != null && result2.getProductsList().get( 0 ).getApprover() != null )
      {
        return result2.getProductsList().get( 0 ).getApprover().toUpperCase().compareTo( result1.getProductsList().get( 0 ).getApprover().toUpperCase() );
      }
    }
    return 0;
  }

}
