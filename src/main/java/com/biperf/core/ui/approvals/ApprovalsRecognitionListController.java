/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsRecognitionListController.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;
import org.springframework.util.StringUtils;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.service.client.CokeClientService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.TcccClientUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionsValueBean;

/**
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
public class ApprovalsRecognitionListController extends BaseController
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
    // Build approvals list
    ApprovalsRecognitionListForm recognitionApprovalListForm = (ApprovalsRecognitionListForm)request.getSession().getAttribute( "approvalsRecognitionListForm" );

    if ( ApprovalsRecognitionListAction.SAVE_METHOD.equals( recognitionApprovalListForm.getMethod() ) )
    {
      recognitionApprovalListForm.setRequestedPage( recognitionApprovalListForm.getListPageInfo().getCurrentPage() );
    }

    List<AbstractRecognitionClaim> approvables = ApprovalsRecognitionListAction.buildPaginatedApprovablesList( recognitionApprovalListForm, true );
    recognitionApprovalListForm.setRequestedPage( 1L );
    request.setAttribute( ApprovalsRecognitionListAction.APPROVABLES, approvables );
    // Client customizations for WIP #42701 starts
    Boolean cashEnabledPromo = ( approvables != null && approvables.size() > 0 ? approvables.get( 0 ).getPromotion().getAdihCashOption() : Boolean.FALSE );
    if ( cashEnabledPromo )
    {
      request.setAttribute( "cashEnabledPromo", cashEnabledPromo );
      populateUsdAwardValue( approvables );
    }
    // Client customizations for WIP #42701 ends
    if ( ApprovalsRecognitionListAction.DISPLAY_METHOD.equals( recognitionApprovalListForm.getMethod() )
        || ( request.getSession().getAttribute( "saveOccurred" ) != null && ( (String)request.getSession().getAttribute( "saveOccurred" ) ).equals( "false" ) ) )
    {
      recognitionApprovalListForm.getRecognitionApprovalFormByClaimRecipientIdString().clear();
      recognitionApprovalListForm.load( approvables );
    }
    // End build approvals list

    List<PromotionsValueBean> sortedApproverPromotions = getPromotionService().getAllSortedApproverPromotions( UserManager.getUserId(), PromotionType.RECOGNITION );
    request.setAttribute( "promotionList", sortedApproverPromotions );
    request.setAttribute( "approvalStatusWhenCurrentlyDeniedList", ApprovalStatusType.getListWhenCurrentlyDenied() );
  }
  // Client customizations for WIP #42701 starts
  private void populateUsdAwardValue( List<AbstractRecognitionClaim> claims ) throws Exception
  {
    CokeClientService cokeClientService = (CokeClientService)getService( CokeClientService.BEAN_NAME );
    for ( AbstractRecognitionClaim claim : claims )
    {
      for ( ClaimRecipient claimRecipient : claim.getClaimRecipients() )
      {    	  
        if ( "USD".equals( claimRecipient.getCashCurrencyCode() ) )
        {
        	if ( null != claimRecipient.getAwardQuantity() ){
        		claimRecipient.setDisplayUSDAwardQuantity( claimRecipient.getAwardQuantity() + " " + claimRecipient.getCashCurrencyCode() );
        	}else if (StringUtils.isEmpty(claimRecipient.getAwardQuantity()) && null != claimRecipient.getCashAwardQuantity() ){
        		claimRecipient.setAwardQuantity(0L);        		
        		claimRecipient.setDisplayUSDAwardQuantity( claimRecipient.getAwardQuantity() + " " + claimRecipient.getCashCurrencyCode() );
        	}else if (StringUtils.isEmpty(claimRecipient.getAwardQuantity()) ){
        		claimRecipient.setAwardQuantity(0L);        		
        		claimRecipient.setDisplayUSDAwardQuantity( claimRecipient.getAwardQuantity() + " " + claimRecipient.getCashCurrencyCode() );
        	}
        }
	    else {
	    	if(null != claimRecipient.getAwardQuantity() && null != claimRecipient.getCashCurrencyCode()){
				claimRecipient.setDisplayUSDAwardQuantity(TcccClientUtils.convertToUSDString(cokeClientService,claimRecipient.getAwardQuantity(), claimRecipient.getCashCurrencyCode()));
			}else{
				if (StringUtils.isEmpty(claimRecipient.getAwardQuantity()))		claimRecipient.setAwardQuantity(0L);
				if (StringUtils.isEmpty(claimRecipient.getCashCurrencyCode())) {					
					claimRecipient.setCashCurrencyCode(getUserService().getUserCurrencyCharValue( claimRecipient.getRecipient().getId()));
				}
				claimRecipient.setDisplayUSDAwardQuantity( claimRecipient.getAwardQuantity() + " " + claimRecipient.getCashCurrencyCode() );
			}
		}
      }
    }
  }
  // Client customizations for WIP #42701 ends
  private PromotionService getPromotionService() throws Exception
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

}
