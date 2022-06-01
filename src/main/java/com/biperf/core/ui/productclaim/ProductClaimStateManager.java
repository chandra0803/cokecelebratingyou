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
public class ProductClaimStateManager
{
  private static final String STATE_SESSION_KEY = ProductClaimSubmissionForm.class.getName() + ".SESSION_KEY";
  private static final String STATE_REQUEST_KEY = "productClaimState";

  private ProductClaimStateManager()
  {
  }

  public static ProductClaimSubmissionForm get( HttpServletRequest request )
  {
    ProductClaimSubmissionForm form = (ProductClaimSubmissionForm)request.getSession().getAttribute( STATE_SESSION_KEY );
    if ( form == null )
    {
      form = new ProductClaimSubmissionForm();
      if ( ClientStateUtils.getClientStateMap( request ) != null )
      {
        String promotionId = ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" );
        if ( promotionId != null )
        {
          form.setPromotionId( new Long( promotionId ) );
        }
      }
    }
    return form;
  }

  public static void remove( HttpServletRequest request )
  {
    request.getSession().removeAttribute( STATE_SESSION_KEY );
  }

  public static void store( ProductClaimSubmissionForm form, HttpServletRequest request )
  {
    if ( form != null )
    {
      request.getSession().setAttribute( STATE_SESSION_KEY, form );
    }
  }

  public static void addToRequest( ProductClaimSubmissionForm form, HttpServletRequest request )
  {
    request.setAttribute( STATE_REQUEST_KEY, form );
  }

}
