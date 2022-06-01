/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/promotion/PromotionRegistrationCodeController.java,v $
 */

package com.biperf.core.ui.promotion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.hierarchy.NodeCharacteristic;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionRegistrationCode;
import com.biperf.core.domain.promotion.RegistrationCode;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.hierarchy.NodeToNodeCharacteristicAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;

/**
 * PromotionRegistrationCodeController.
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
 * <td>viswanat</td>
 * <td>Feb 19, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PromotionRegistrationCodeController extends BaseController
{

  /**
   * Overridden from @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext, 
   * javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    Long promotionId = new Long( ClientStateUtils.getParameterValue( request, ClientStateUtils.getClientStateMap( request ), "promotionId" ) );
    Promotion promotion = (Promotion)getPromotionService().getPromotionById( promotionId );
    if ( promotion.isGoalQuestPromotion() )
    {
      promotion = (GoalQuestPromotion)promotion;
    }
    else if ( promotion.isChallengePointPromotion() )
    {
      promotion = (ChallengePointPromotion)promotion;
    }
    final String programCode = promotion.getEnrollProgramCode();
    getNodeService().generateNodeRegistrationLocCodes();
    List promotionRegistrationCodesList = new ArrayList();
    // Loop through the node List
    AssociationRequestCollection nodeAssociationRequestCollection = new AssociationRequestCollection();
    nodeAssociationRequestCollection.add( new NodeToNodeCharacteristicAssociationRequest() );
    for ( Iterator nodeIter = getNodeService().getAll( nodeAssociationRequestCollection ).iterator(); nodeIter.hasNext(); )
    {
      Node node = (Node)nodeIter.next();
      Set nodeCharacteristicList = node.getNodeCharacteristics();
      // Loop through the node characteristic list
      for ( Iterator nodeCharIter = nodeCharacteristicList.iterator(); nodeCharIter.hasNext(); )
      {
        NodeCharacteristic nodeCharacteristic = (NodeCharacteristic)nodeCharIter.next();
        // if the characteristic is found for reg_code_location then create the object and add to
        // the list
        if ( nodeCharacteristic.getNodeTypeCharacteristicType().getCharacteristicName().equals( NodeTypeCharacteristicType.CHARACTERISTIC_REG_CODE_LOC_NAME ) )
        {

          PromotionRegistrationCode promoRegistrationCode = new PromotionRegistrationCode();
          promoRegistrationCode.setNodeName( node.getName() );
          promoRegistrationCode.setCmAssetCode( node.getNodeType().getCmAssetCode() );
          promoRegistrationCode.setCmKey( node.getNodeType().getNameCmKey() );

          RegistrationCode registrationCode = new RegistrationCode();
          registrationCode.setEnrollProgramCode( programCode );
          registrationCode.setLocationCode( nodeCharacteristic.getCharacteristicValue() );
          promoRegistrationCode.setRegistrationCode( registrationCode.toString() );

          promotionRegistrationCodesList.add( promoRegistrationCode );
        }
      }
    }
    request.setAttribute( "promotionName", promotion.getName() );
    request.setAttribute( "registrationCodesList", promotionRegistrationCodesList );
  }

  private NodeService getNodeService()
  {
    return (NodeService)getService( NodeService.BEAN_NAME );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
