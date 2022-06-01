/**
 * 
 */

package com.biperf.core.ui.productclaim;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.utils.ClientStateUtils;

/**
 * @author poddutur
 *
 */
public class ProductClaimSubmitStartAction extends ProductClaimSubmissionAction
{

  @Override
  protected ProductClaimSubmissionForm getProductClaimState( HttpServletRequest request )
  {
    // make sure any previous state is removed
    return ProductClaimStateManager.get( request );

    /*
     * ProductClaimSubmissionForm form = new ProductClaimSubmissionForm(); if (
     * ClientStateUtils.getClientStateMap( request ) != null ) { String promotionId =
     * ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ),
     * "promotionId" ); if ( promotionId != null ) { form.setPromotionId( new Long( promotionId ) );
     * } } return form;
     */
  }

  @Override
  public ProductClaimSubmissionForm getProductClaimForm( HttpServletRequest request )
  {
    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );

    ProductClaimSubmissionForm form = new ProductClaimSubmissionForm();

    if ( promotionId != null )
    {
      form.setPromotionId( promotionId );
    }

    return form;
  }

}
