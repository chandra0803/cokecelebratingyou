
package com.biperf.core.ui.throwdown;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.constants.ActionConstants;

public class ViewRulesAction extends BaseThrowdownAction
{

  public ActionForward detail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException, ServletException
  {
    Long promotionId = buildPromotionId( request );
    Promotion promotion = getPromotionervice().getPromotionById( promotionId );
    request.setAttribute( "promotion", promotion );
    return mapping.findForward( ActionConstants.DETAILS_FORWARD );
  }

  private PromotionService getPromotionervice()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
