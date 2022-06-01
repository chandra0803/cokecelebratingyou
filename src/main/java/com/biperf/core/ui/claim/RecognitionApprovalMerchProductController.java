/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/RecognitionApprovalMerchProductController.java,v $
 */

package com.biperf.core.ui.claim;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.awardbanq.AwardBanqMerchResponseValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelProductValueObject;
import com.biperf.core.service.awardbanq.impl.MerchLevelValueObject;
import com.biperf.core.service.merchlevel.MerchLevelService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;

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
public class RecognitionApprovalMerchProductController extends BaseController
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
    String programId = (String)tileContext.getAttribute( "programId" );
    String productSetId = (String)tileContext.getAttribute( "productSetId" );

    // check for alternate instances using client state utils
    if ( null == programId && null == productSetId )
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
      programId = (String)clientStateMap.get( "programId" );
      productSetId = (String)clientStateMap.get( "productSetId" );
    }

    AwardBanqMerchResponseValueObject levels = getMerchLevelService().getMerchlinqLevelDataWebService( programId, true );
    Iterator levelIter = levels.getMerchLevel().iterator();

    // iterator through the levels to determine which one maps to the correct productSetId
    while ( levelIter.hasNext() )
    {
      MerchLevelValueObject level = (MerchLevelValueObject)levelIter.next();
      Iterator productIter = level.getMerchLevelProduct().iterator();
      while ( productIter.hasNext() )
      {
        MerchLevelProductValueObject product = (MerchLevelProductValueObject)productIter.next();
        String omProductSetId = product.getProductSetId();

        if ( omProductSetId != null && omProductSetId.equals( productSetId ) )
        {
          request.setAttribute( "merchLevelProduct", product );
          request.setAttribute( "programId", programId );
          request.setAttribute( "productSetId", productSetId );
          return;
        }
      }
    }
  }

  private MerchLevelService getMerchLevelService() throws Exception
  {
    return (MerchLevelService)getService( MerchLevelService.BEAN_NAME );
  }
}
