/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/throwdown/PromotionSelectorAction.java,v $
 */

package com.biperf.core.ui.throwdown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.promotion.ThrowdownPromotion;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.value.PromotionMenuBean;
import com.biperf.core.value.ThrowdownPromotionSelectorView;

public class PromotionSelectorAction extends BaseThrowdownAction
{
  public ActionForward list( ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    List<ThrowdownPromotionSelectorView> selectorViews = new ArrayList<ThrowdownPromotionSelectorView>();

    for ( PromotionMenuBean promoMenuBean : getEligibleThrowdownPromotions( request ) )
    {
      Map<String, Object> parameterMap = new HashMap<String, Object>();
      parameterMap.put( "promotionId", promoMenuBean.getPromotion().getId() );
      ThrowdownPromotionSelectorView promoViewBean = new ThrowdownPromotionSelectorView( (ThrowdownPromotion)promoMenuBean.getPromotion() );
      String viewMatchesUrl = ClientStateUtils.generateEncodedLink( "", request.getServletContext().getContextPath() + "/throwdown/viewMatchesDetail.do?method=detail", parameterMap );
      promoViewBean.setMatchesUrl( viewMatchesUrl.concat( "#Matches" ) );
      promoViewBean.setRulesUrl( generateRulesUrl( promoMenuBean.getPromotion().getId(), request ) );
      selectorViews.add( promoViewBean );
    }

    super.writeAsJsonToResponse( selectorViews, response );
    return null;
  }
}
