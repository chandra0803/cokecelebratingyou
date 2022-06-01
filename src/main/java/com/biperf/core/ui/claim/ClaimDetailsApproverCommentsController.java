/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/ClaimDetailsApproverCommentsController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimProduct;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.ApprovalType;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.claim.ClaimAssociationRequest;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * ClaimProductApprovalDetailsController.
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
public class ClaimDetailsApproverCommentsController extends BaseController
{
  private static final Log LOG = LogFactory.getLog( ClaimDetailsApproverCommentsController.class );

  /**
   * Overridden from
   * 
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      Long claimId = null;
      try
      {
        claimId = (Long)clientStateMap.get( "claimId" );
      }
      catch( ClassCastException cce )
      {
        String claimIdString = (String)clientStateMap.get( "claimId" );
        if ( claimIdString != null && claimIdString.length() > 0 )
        {
          claimId = new Long( claimIdString );
        }
      }
      if ( claimId != null )
      {
        Claim claim = null;
        if ( request.getAttribute( "claimDetails" ) == null )
        {
          // claim = getClaimService().getClaimById( claimId );
          // bug fix, 11036
          AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
          associationRequestCollection.add( new ClaimAssociationRequest( ClaimAssociationRequest.ALL ) );
          claim = getClaimService().getClaimByIdWithAssociations( claimId, associationRequestCollection );

          request.setAttribute( "claimDetails", claim );
        }

        // bug fix, 11036
        ProductClaim productClaim = (ProductClaim)claim;

        if ( productClaim != null )
        {
          List claimProducts = new ArrayList( productClaim.getClaimProducts() );

          PropertyComparator.sort( claimProducts, new MutableSortDefinition( "claimProduct.dateApproved", true, true ) );

          ClaimProduct mostRecentClaimProduct = (ClaimProduct)claimProducts.get( 0 );

          Boolean systemApproved = new Boolean( false );

          if ( productClaim.getPromotion() != null && productClaim.getPromotion().getApprovalType() != null && productClaim.getPromotion().getApprovalType().getCode() != null
              && ( productClaim.getPromotion().getApprovalType().getCode().equals( ApprovalType.AUTOMATIC_DELAYED )
                  || productClaim.getPromotion().getApprovalType().getCode().equals( ApprovalType.AUTOMATIC_IMMEDIATE ) ) )
          {
            systemApproved = new Boolean( true );
          }

          request.setAttribute( "systemApproved", systemApproved );
          request.setAttribute( "mostRecentClaimProduct", mostRecentClaimProduct );
        }
      }

      else
      {
        LOG.error( "claimId not found in client state" );
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
  }

  private ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

}
