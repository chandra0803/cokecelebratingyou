/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsClaimsListController.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionsValueBean;

/**
 * ApprovalsClaimsListController.
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
 * <td>zahler</td>
 * <td>Aug 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsClaimsListController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // List sortedApproverPromotions = new ArrayList( getPromotionService().getAllForApprover(
    // UserManager.getUserId(), PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) ) );
    List<PromotionsValueBean> sortedApproverPromotions = getPromotionService().getAllSortedApproverPromotions( UserManager.getUserId(), PromotionType.PRODUCT_CLAIM );
    request.setAttribute( "promotionList", sortedApproverPromotions );
    request.setAttribute( "allApprovalStatusList", ApprovalStatusType.getList() );
    request.setAttribute( "approvalStatusWhenCurrentlyDeniedList", ApprovalStatusType.getListWhenCurrentlyDenied() );

    ApprovalsClaimsListForm claimProductApprovalListForm = (ApprovalsClaimsListForm)request.getSession().getAttribute( "approvalsClaimsListForm" );
    if ( claimProductApprovalListForm != null && !StringUtils.isBlank( claimProductApprovalListForm.getPromotionId() ) )
    {
      if ( request.getAttribute( ApprovalsClaimsListAction.ATTR_PROMOTION_CLAIMS_VALUE_LIST ) == null )
      {
        List promotionClaimsValueList = ApprovalsClaimsListAction.buildApprovablesList( claimProductApprovalListForm, true );
        claimProductApprovalListForm.setRequestedPage( 1L );
        request.setAttribute( ApprovalsClaimsListAction.ATTR_PROMOTION_CLAIMS_VALUE_LIST, promotionClaimsValueList );
        if ( promotionClaimsValueList != null )
        {
          claimProductApprovalListForm.load( promotionClaimsValueList );
        }

        request.setAttribute( "showExports", promotionClaimsValueList == null ? false : promotionClaimsValueList.size() > 0 ? true : false );
      }
    }

  }

  private PromotionService getPromotionService() throws Exception
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
