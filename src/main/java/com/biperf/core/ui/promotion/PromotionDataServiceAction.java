
package com.biperf.core.ui.promotion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.json.simple.JSONObject;

import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.value.promotion.CustomFormStepElementsView;

//context/promotion/dataService.do
public class PromotionDataServiceAction extends PromotionBaseDispatchAction
{
  @SuppressWarnings( "unchecked" )
  public ActionForward getCustomElements( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    try
    {
      String promotionId = request.getParameter( "promotionId" );
      String claimId = request.getParameter( "claimId" );
      CustomFormStepElementsView stepElements = null;

      if ( !StringUtil.isEmpty( claimId ) )
      {
        stepElements = getClaimService().getClaimStepElementsWithValue( Long.valueOf( claimId ) );
      }
      else if ( !StringUtil.isEmpty( promotionId ) )
      {
        stepElements = getPromotionService().getStepElements( Long.valueOf( promotionId ) );
      }

      JSONObject jsonObject = new JSONObject();
      jsonObject.put( "customElements", stepElements );
      writeAsJsonToResponse( jsonObject, response, ContentType.JSON );
    }
    catch( Exception e )
    {
      e.printStackTrace();
      writeAppErrorAsJsonResponse( response, e );
    }
    return null;
  }

  protected static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

}
