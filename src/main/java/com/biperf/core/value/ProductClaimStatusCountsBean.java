/**
 * 
 */

package com.biperf.core.value;

/**
 * @author poddutur
 *
 */
public class ProductClaimStatusCountsBean
{
  int claimsSubmitted;
  int claimsPending;
  int productsSubmitted;
  int productsApproved;
  int productsDenied;
  int productsPending;

  public int getClaimsSubmitted()
  {
    return claimsSubmitted;
  }

  public void setClaimsSubmitted( int claimsSubmitted )
  {
    this.claimsSubmitted = claimsSubmitted;
  }

  public int getClaimsPending()
  {
    return claimsPending;
  }

  public void setClaimsPending( int claimsPending )
  {
    this.claimsPending = claimsPending;
  }

  public int getProductsSubmitted()
  {
    return productsSubmitted;
  }

  public void setProductsSubmitted( int productsSubmitted )
  {
    this.productsSubmitted = productsSubmitted;
  }

  public int getProductsApproved()
  {
    return productsApproved;
  }

  public void setProductsApproved( int productsApproved )
  {
    this.productsApproved = productsApproved;
  }

  public int getProductsDenied()
  {
    return productsDenied;
  }

  public void setProductsDenied( int productsDenied )
  {
    this.productsDenied = productsDenied;
  }

  public int getProductsPending()
  {
    return productsPending;
  }

  public void setProductsPending( int productsPending )
  {
    this.productsPending = productsPending;
  }

}
