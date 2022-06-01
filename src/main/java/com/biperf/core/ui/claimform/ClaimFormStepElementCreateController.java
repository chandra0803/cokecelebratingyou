/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/ClaimFormStepElementCreateController.java,v $
 *
 */

package com.biperf.core.ui.claimform;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ClaimFormElementType;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.ui.BaseController;

/**
 * ClaimFormStepElementCreateController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 23, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ClaimFormStepElementCreateController extends BaseController
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
    ClaimFormStepElementForm form = (ClaimFormStepElementForm)request.getAttribute( ClaimFormStepElementForm.FORM_NAME );
    ClaimFormElementType elementType = ClaimFormElementType.lookup( form.getClaimFormStepElementTypeCode() );
    form.setClaimFormStepElementTypeDesc( elementType.getName() );
    request.setAttribute( "elementItems", ClaimFormStepElementItem.getItemListByType( elementType ) );

    // Need the claim form module for text box's "is why field" section
    ClaimForm claimForm = getClaimFormDefinitionService().getClaimFormById( form.getClaimFormId() );
    request.setAttribute( "formModuleType", claimForm.getClaimFormModuleType().getCode() );

    List pickListAssetList = getCMAssetService().getAllPickListItemAssets();

    // set the list of allPickLists in the request
    request.setAttribute( "pickListItemAssets", pickListAssetList );

  }

  /**
   * Get the CharacteristicService from the beanLocator.
   * 
   * @return CharacteristicService
   */
  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  /**
   * Get the ClaimFormDefinitionService from the beanLocator.
   * 
   * @return ClaimFormDefinitionService
   */
  private ClaimFormDefinitionService getClaimFormDefinitionService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

}
