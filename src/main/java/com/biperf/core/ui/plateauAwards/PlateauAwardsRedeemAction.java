/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/plateauAwards/PlateauAwardsRedeemAction.java,v $
 */

package com.biperf.core.ui.plateauAwards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.plateauawards.PlateauAwardsRedeemBean;
import com.biperf.core.domain.plateauawards.PlateauAwardsRedeemView;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.MerchAwardReminderBean;
import com.biperf.core.value.PromotionMenuBean;

/**
 * 
 * @author poddutur
 * @since Oct 9, 2015
 */
public class PlateauAwardsRedeemAction extends BaseDispatchAction
{
  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward(response put to stream for the ajax call)
   */
  public ActionForward fetchPlateauAwardsRedeemList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    PlateauAwardsRedeemView plateauAwardsRedeemView = new PlateauAwardsRedeemView();
    List<PlateauAwardsRedeemBean> promotions = new ArrayList<PlateauAwardsRedeemBean>();

    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );

    Country country = getUserService().getPrimaryUserAddressCountry( UserManager.getUserId() );

    List promoAwardReminderList = getMainContentService().getMerchAwardRemindersForAcivityList( UserManager.getUserId(), eligiblePromotions, country );

    Collections.sort( promoAwardReminderList, EXPIRY_DATE_COMPARATOR );

    if ( promoAwardReminderList != null && promoAwardReminderList.size() > 0 )
    {
      Long endDate = null;
      Iterator promoAwardReminderListIter = promoAwardReminderList.iterator();
      while ( promoAwardReminderListIter.hasNext() )
      {
        MerchAwardReminderBean merchAwardReminder = (MerchAwardReminderBean)promoAwardReminderListIter.next();
        endDate = merchAwardReminder.getExpirationDate() != null ? merchAwardReminder.getExpirationDate().getTime() : null;

        PlateauAwardsRedeemBean plateauAwardsRedeemBean = new PlateauAwardsRedeemBean();
        plateauAwardsRedeemBean.setPromotionName( merchAwardReminder.getPromotionName() );
        plateauAwardsRedeemBean.setEndDate( endDate );
        plateauAwardsRedeemBean.setCatalogUrl( merchAwardReminder.getOnlineShoppingUrl() );
        promotions.add( plateauAwardsRedeemBean );
      }
    }

    plateauAwardsRedeemView.setPromotions( promotions );

    super.writeAsJsonToResponse( plateauAwardsRedeemView, response );
    return null;
  }

  private static Comparator<MerchAwardReminderBean> EXPIRY_DATE_COMPARATOR = new Comparator<MerchAwardReminderBean>()
  {
    public int compare( MerchAwardReminderBean a1, MerchAwardReminderBean a2 )
    {
      if ( a1.getExpirationDate() == null || a2.getExpirationDate() == null )
      {
        return 0;
      }

      return a1.getExpirationDate().compareTo( a2.getExpirationDate() );
    }
  };

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  public MainContentService getMainContentService()
  {
    return (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
  }

}
