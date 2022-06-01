
package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.nomination.NominationAdminApprovalsBean;

public class NominationApprovalsListController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List<NominationAdminApprovalsBean> promotionList = new ArrayList<NominationAdminApprovalsBean>();
    promotionList = getPromotionService().getNominationApprovalClaimPromotions();

    request.setAttribute( "liveSet", promotionList );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
