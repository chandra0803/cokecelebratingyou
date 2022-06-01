/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/characteristic/NodeTypeCharacteristicTypeController.java,v $
 */

package com.biperf.core.ui.characteristic;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.hierarchy.NodeType;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.NodeTypeService;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;

/**
 * NodeTypeCharacteristicTypeController.
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
 * <td>Jun 2, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NodeTypeCharacteristicTypeController extends CharacteristicController
{

  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    super.onExecute( context, request, response, servletContext );
    String domainId = null;
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
      try
      {
        domainId = (String)clientStateMap.get( "domainId" );
      }
      catch( ClassCastException cce )
      {
        Long id = (Long)clientStateMap.get( "domainId" );
        domainId = id.toString();
      }
    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }
    NodeType nodeType = getNodeTypeService().getNodeTypeById( new Long( domainId ) );
    request.setAttribute( "nodeType", nodeType );

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.characteristic.CharacteristicController#makeGetAllCharacteristicServiceCall(java.lang.Long)
   * @param domainId
   * @return List
   */
  public List makeGetAllCharacteristicServiceCall( Long domainId )
  {
    List charList = null;
    if ( domainId == null )
    {
      charList = getNodeTypeCharacteristicService().getAllCharacteristics();
    }
    else
    {
      charList = getNodeTypeCharacteristicService().getAllNodeTypeCharacteristicTypesByNodeTypeId( domainId );
    }

    // Remove inactive characteristics
    Iterator iter = charList.iterator();
    while ( iter.hasNext() )
    {
      Characteristic characteristic = (Characteristic)iter.next();
      if ( !characteristic.isActive() )
      {
        iter.remove();
      }
    }

    return charList;
  }

  /**
   * Overridden from
   * 
   * @return CharacteristicService
   */
  protected NodeTypeCharacteristicService getNodeTypeCharacteristicService()
  {
    NodeTypeCharacteristicService characteristicService = (NodeTypeCharacteristicService)getService( NodeTypeCharacteristicService.BEAN_NAME );
    return characteristicService;
  }

  protected NodeTypeService getNodeTypeService()
  {
    NodeTypeService nodeTypeService = (NodeTypeService)getService( NodeTypeService.BEAN_NAME );
    return nodeTypeService;
  }

}
