
package com.biperf.core.ui.homepage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.WhatsNewBean;

public class AnniversaryAwardsAjaxAction extends BaseDispatchAction
{

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    List eligiblePromoList = null;
    Country country = null;
    if ( UserManager.getUser().isParticipant() )
    {
      eligiblePromoList = getEligiblePromotions( request );
      UserAddress address = getUserService().getPrimaryUserAddress( UserManager.getUserId() );
      if ( address != null && address.getAddress() != null )
      {
        country = address.getAddress().getCountry();
      }
    }
    if ( eligiblePromoList != null && eligiblePromoList.size() > 0 && country != null )
    {
      MainContentService mainContentService = (MainContentService)getService( MainContentService.BEAN_NAME );
      List whatsNewList = mainContentService.getWhatsNewList( eligiblePromoList, country );
      request.setAttribute( "whatsNewList", whatsNewList );
      request.setAttribute( "maxAnniversaryAwardItems", whatsNewList.size() );
      if ( whatsNewList != null && whatsNewList.size() > 0 )
      {
        request.setAttribute( "promotionName", ( (WhatsNewBean)whatsNewList.get( 0 ) ).getHomePageItem().getPromotion().getName() );
      }
    }

    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }
}
