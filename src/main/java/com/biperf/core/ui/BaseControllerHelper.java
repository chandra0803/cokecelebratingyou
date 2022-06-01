
package com.biperf.core.ui;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

@Component
public class BaseControllerHelper
{

  public static final String ELIGIBLE_PROMOTIONS = "eligiblePromotions";

  @Autowired
  private MainContentService mainContentService;

  @SuppressWarnings( "unchecked" )
  public List<PromotionMenuBean> getEligiblePromotions( HttpServletRequest request )
  {

    if ( UserManager.getUser().isParticipant() )
    {
      List<PromotionMenuBean> eligiblePromotions = (List<PromotionMenuBean>)request.getSession().getAttribute( ELIGIBLE_PROMOTIONS );
      if ( null != eligiblePromotions )
      {
        return eligiblePromotions;
      }
      else
      {
        eligiblePromotions = mainContentService.buildEligiblePromoList( UserManager.getUser() );
        request.getSession().setAttribute( ELIGIBLE_PROMOTIONS, eligiblePromotions );
        return eligiblePromotions;
      }
    }
    return new ArrayList<PromotionMenuBean>();
  }

}
