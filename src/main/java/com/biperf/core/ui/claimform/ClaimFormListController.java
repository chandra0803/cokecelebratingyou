/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/ClaimFormListController.java,v $
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.dao.promotion.hibernate.PromotionQueryConstraint;
import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.domain.enums.ClaimFormStatusType;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;

/**
 * ClaimFormListController.
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
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormListController extends BaseController
{

  private static final Log logger = LogFactory.getLog( ClaimFormListController.class );

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
    final String METHOD_NAME = "execute";

    logger.info( ">>> " + METHOD_NAME );

    Map formLibraryMap = getClaimFormService().getFormLibraryMap();

    request.setAttribute( "formTemplateList", formLibraryMap.get( ClaimFormStatusType.TEMPLATE ) );
    request.setAttribute( "underConstructionFormList", formLibraryMap.get( ClaimFormStatusType.UNDER_CONSTRUCTION ) );
    request.setAttribute( "completedFormList", formLibraryMap.get( ClaimFormStatusType.COMPLETED ) );

    // get promotion count for the assigned claim forms
    ArrayList assignedClaimForms = (ArrayList)formLibraryMap.get( ClaimFormStatusType.ASSIGNED );
    List claimFormWithPromo = new ArrayList( assignedClaimForms.size() );
    for ( Iterator iter = assignedClaimForms.iterator(); iter.hasNext(); )
    {
      ClaimForm claimForm = (ClaimForm)iter.next();
      PromotionQueryConstraint queryConstraint = new PromotionQueryConstraint();
      queryConstraint.setPromotionClaimFormIncluded( claimForm );
      int promoCount = getPromotionService().getPromotionListCount( queryConstraint );
      claimForm.setPromotionCount( promoCount );
      claimFormWithPromo.add( claimForm );
    }
    request.setAttribute( "assignedFormList", claimFormWithPromo );

    logger.info( "<<< " + METHOD_NAME );
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
