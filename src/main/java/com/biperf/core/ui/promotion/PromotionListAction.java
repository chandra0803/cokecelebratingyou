/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionListAction.java,v $ */

package com.biperf.core.ui.promotion;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

/**
 * Action class for Promotion operations.
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
 * <td>sedey</td>
 * <td>June 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionListAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( PromotionListAction.class );

  /**
   * Delete under construction promotions
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteUnderConstructionPromo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionListForm promoListForm = (PromotionListForm)form;

    if ( promoListForm.getDeleteUnderConstructionPromos() != null && promoListForm.getDeleteUnderConstructionPromos().length > 0 )
    {
      deletePromotions( promoListForm.getDeleteUnderConstructionPromos() );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Delete complete promotions
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteCompletePromo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    PromotionListForm promoListForm = (PromotionListForm)form;

    if ( promoListForm.getDeleteCompletePromos() != null && promoListForm.getDeleteCompletePromos().length > 0 )
    {
      deletePromotions( promoListForm.getDeleteCompletePromos() );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Delete expired promotions
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteExpiredPromo( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    PromotionListForm promoListForm = (PromotionListForm)form;

    if ( promoListForm.getDeleteExpiredPromos() != null && promoListForm.getDeleteExpiredPromos().length > 0 )
    {
      deletePromotions( promoListForm.getDeleteExpiredPromos() );
    }

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * delete a list of promotions
   * 
   * @param promotionIds
   */
  private void deletePromotions( String[] promotionIds )
  {
    // Convert String[] of promotionIds to Long[]
    List promotionIdList = ArrayUtil.convertStringArrayToListOfLongObjects( promotionIds );

    try
    {
      getPromotionService().deletePromotions( promotionIdList );
    }
    catch( ServiceErrorException e )
    {
      // Exception thrown if the promotion to be deleted is live
      logger.error( e.getMessage(), e );
    }
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

}
