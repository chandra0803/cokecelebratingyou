/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/approvals/ApprovalsClaimDetailsController.java,v $
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.enums.ApprovalStatusType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ArrayUtil;

/**
 * ApprovalsClaimDetailsController.
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
 * <td>wadzinsk</td>
 * <td>Sep 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ApprovalsClaimDetailsController extends BaseController
{
  /**
   * Overridden from
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @SuppressWarnings( "unchecked" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    AssociationRequestCollection promotionAssociationRequestCollection = new AssociationRequestCollection();
    promotionAssociationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.APPROVAL_OPTION ) );

    // Reset promotion with hydrated version.
    Claim claim = (Claim)request.getAttribute( "claimDetails" );
    claim.setPromotion( getPromotionService().getPromotionByIdWithAssociations( claim.getPromotion().getId(), promotionAssociationRequestCollection ) );

    getClaimElementDomainObjects( claim );

    request.setAttribute( "allApprovalStatusList", ApprovalStatusType.getList() );
    request.getSession().setAttribute( "claimId", Long.parseLong( request.getParameter( "claimId" ) ) );
    request.setAttribute( "approvalStatusWhenCurrentlyDeniedList", ApprovalStatusType.getListWhenCurrentlyDenied() );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  @SuppressWarnings( "unchecked" )
  private Claim getClaimElementDomainObjects( Claim claim )
  {
    for ( Iterator<ClaimElement> iter = claim.getClaimElements().iterator(); iter.hasNext(); )
    {
      ClaimElement claimElement = iter.next();
      if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isMultiSelectField() || claimElement.getClaimFormStepElement().getClaimFormElementType().isSelectField() )
      {
        List<DynaPickListType> pickListItems = new ArrayList<DynaPickListType>();
        // convert the comma delimited list of selected pickListItems to a list of strings
        Iterator<String> pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
        while ( pickListCodes.hasNext() )
        {
          String code = pickListCodes.next();
          pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ) );
        }
        claimElement.setPickListItems( pickListItems );
      }
    }
    return claim;
  }

}
