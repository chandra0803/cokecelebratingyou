/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/homepage/BudgetTrackerAction.java,v $
 */

package com.biperf.core.ui.homepage;

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

import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.BudgetMeter;
import com.biperf.core.value.BudgetMeterDetailBean;
import com.biperf.core.value.BudgetMeterDetailPromoBean;
import com.biperf.core.value.PromotionMenuBean;

/**
 * BudgetTrackerAction.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>gadapa</td>
 * <td>Jul 31, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BudgetTrackerAction extends BaseDispatchAction
{
  /**
   * Prepare the display for Internal Shopping
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward clearCache( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    // Clear the Cache
    getMainContentService().clearBudgetTrackerCache( UserManager.getUser() );

    return mapping.findForward( "success" );
  }

  /**
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return
   * @throws IOException
   * 
   *  This method is used to fetch the budgets
   */
  public ActionForward fetchBudgetsForTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    List<PromotionMenuBean> eligiblePromotions = getEligiblePromotions( request );
    List<PromotionMenuBean> validPromotions = new ArrayList<PromotionMenuBean>();
    BudgetMeter budgetMeter = new BudgetMeter();
    if ( eligiblePromotions != null && !eligiblePromotions.isEmpty() )
    {
      for ( PromotionMenuBean promotionMenuBean : eligiblePromotions )
      {
        if ( promotionMenuBean.isCanSubmit() )
        {
          validPromotions.add( promotionMenuBean );
        }
      }
      budgetMeter = getMainContentService().getBudgetMeter( UserManager.getUserId(), UserManager.getTimeZoneID(), validPromotions );
      for ( BudgetMeterDetailBean budgetMeterDetailBean : budgetMeter.getBudgetMeterDetails() )
      {
        for ( BudgetMeterDetailPromoBean budgetMeterDetailPromoBean : budgetMeterDetailBean.getPromoList() )
        {
          budgetMeterDetailPromoBean.setUrl( buildUrl( budgetMeterDetailPromoBean.getPromoId(), request ) );
          Promotion promotion= getPromotionService().getPromotionById( budgetMeterDetailPromoBean.getPromoId());
          if(promotion!=null)
          {
              if(promotion.isUtilizeParentBudgets())
                  budgetMeterDetailPromoBean.setBudgetOwner(budgetMeterDetailBean.getNodeName());
          }
        }
      }
    }
    super.writeAsJsonToResponse( budgetMeter, response );
    return null;
  }

  /**
   * Get the MainContentService From the bean factory through a locator.
   * 
   * @return MainContentService
   */
  private MainContentService getMainContentService()
  {
    return (MainContentService)getService( MainContentService.BEAN_NAME );
  }

  private String buildUrl( Long promotionId, HttpServletRequest request )
  {
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    return ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), "/recognitionWizard/prepopulatePromotionRecognition.do", parameterMap );
  }
  
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
