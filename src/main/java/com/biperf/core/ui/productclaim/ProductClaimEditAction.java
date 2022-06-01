/**
 * 
 */

package com.biperf.core.ui.productclaim;

import javax.servlet.http.HttpServletRequest;

/**
 * @author poddutur
 *
 */
public class ProductClaimEditAction extends ProductClaimSubmissionAction
{

  @Override
  protected ProductClaimSubmissionForm getProductClaimState( HttpServletRequest request )
  {
    return ProductClaimStateManager.get( request );
  }

  @Override
  public ProductClaimSubmissionForm getProductClaimForm( HttpServletRequest request )
  {
    return ProductClaimStateManager.get( request );
  }

}
