/**
 * 
 */

package com.biperf.core.ui.approvals;

import java.util.Comparator;

import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion.Claims.Results.Product;

/**
 * @author poddutur
 *
 */
public class ProductApproverAscendingComparator implements Comparator
{

  @Override
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof Product ) || ! ( o2 instanceof Product ) )
    {
      throw new ClassCastException( "Object is not a Product object!" );
    }
    Product product1 = (Product)o1;
    Product product2 = (Product)o2;

    if ( product1 != null && product2 != null )
    {
      if ( product1.getApprover() != null && product2.getApprover() != null )
      {
        return product1.getApprover().toUpperCase().compareTo( product2.getApprover().toUpperCase() );
      }
    }
    return 0;
  }

}
