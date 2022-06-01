/**
 * 
 */

package com.biperf.core.ui.promotion;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.ui.BaseController;

/**
 * PromotionAudienceController.
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
 * <td>ernste</td>
 * <td>Jul 25, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromoAudienceCountryListController extends BaseController
{

  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
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
    PromotionAudienceForm audienceForm = (PromotionAudienceForm)request.getAttribute( "promotionAudienceForm" );

    boolean isRecognition = audienceForm.getPromotionTypeCode().equals( PromotionType.lookup( "recognition" ).getCode() );

    /*
     * //add active countries lists for spotlight if (isRecognition &&
     * audienceForm.isAwardMerchandise()) { audienceForm.loadPromoMerchCountryLists(new
     * Long(audienceForm.getPromotionId())); request.setAttribute( "requiredActiveCountryListCount",
     * String.valueOf( audienceForm.getRequiredActiveCountryList().size()) ); request.setAttribute(
     * "nonRequiredActiveCountryListCount", String.valueOf(
     * audienceForm.getNonRequiredActiveCountryList().size() ) ); }
     */

  }
}
