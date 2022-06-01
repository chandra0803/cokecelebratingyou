/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/user/UserCharacteristicEditController.java,v $
 */

package com.biperf.core.ui.user;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCharacteristic;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.CharacteristicUtils;

/**
 * UserCharacterisitcEditController.
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
 * <td>sedey</td>
 * <td>Apr 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class UserCharacteristicEditController extends BaseController
{
  /** Log */
  private static final Log LOG = LogFactory.getLog( UserCharacteristicEditController.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param componentContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext componentContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {

    final String METHOD_NAME = "execute";

    LOG.info( ">>> " + METHOD_NAME );

    UserCharacteristicForm userCharacteristicForm = (UserCharacteristicForm)request.getAttribute( "userCharacteristicForm" );

    Long userId = userCharacteristicForm.getUserId();

    User user = getUserService().getUserById( userId );

    // This is the set of UserCharacteristic objects the user has values for.
    Set userCharacteristics = getUserService().getUserCharacteristics( userId );

    // This is the set of all UserCharacteristic objects
    List availableCharacteristics = getUserCharacteristicService().getAllCharacteristics();

    List characteristicList = CharacteristicUtils.getUserCharacteristicValueList( userCharacteristics, availableCharacteristics );

    if ( userCharacteristicForm.getUserCharacteristicValueListCount() > 0 )
    {
      CharacteristicUtils.loadExistingValues( characteristicList, userCharacteristicForm.getUserCharacteristicValueList() );
    }
    userCharacteristicForm.setUserCharacteristicValueList( characteristicList );

    // If there is a Dynamic (Dyna) Pick List for any of the Characteristics, that characteristic
    // will have a value for plName. Each of these dyna pick lists needs to be set
    // in the request. This needs to be done for the userCharacteristics Set, and
    // the availableCharacteristics Set.
    Iterator currentIt = userCharacteristics.iterator();
    while ( currentIt.hasNext() )
    {
      UserCharacteristic userCharacteristic = (UserCharacteristic)currentIt.next();
      if ( userCharacteristic.getUserCharacteristicType().getPlName() != null && !userCharacteristic.getUserCharacteristicType().getPlName().equals( "" ) )
      {
        request.setAttribute( userCharacteristic.getUserCharacteristicType().getPlName(), DynaPickListType.getList( userCharacteristic.getUserCharacteristicType().getPlName() ) );
      }
      else
      {
        if ( userCharacteristic.getUserCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.SINGLE_SELECT )
            || userCharacteristic.getUserCharacteristicType().getCharacteristicDataType().getCode().equals( CharacteristicDataType.MULTI_SELECT ) )
        {
          request.setAttribute( userCharacteristic.getUserCharacteristicType().getPlName(), userCharacteristicForm.DELETE_VALUE );
        }
      }
    }

    // This is a little different than the above Iteration because this iteration is
    // dealing with Characteristic Objects rather than UserCharacteristic Objects
    Iterator availableIt = availableCharacteristics.iterator();
    while ( availableIt.hasNext() )
    {
      Characteristic characteristic = (Characteristic)availableIt.next();
      if ( characteristic.getPlName() != null && !characteristic.getPlName().equals( "" ) )
      {
        request.setAttribute( characteristic.getPlName(), DynaPickListType.getList( characteristic.getPlName() ) );
      }
    }

    // Place the user object in the request so user information is availble on the page.
    request.setAttribute( "user", user );
    // Place the userCharacteristicForm in the request so it is available on the page.
    request.setAttribute( "userCharacteristicForm", userCharacteristicForm );

    LOG.info( "<<< " + METHOD_NAME );
  }

  private UserService getUserService() throws Exception
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private UserCharacteristicService getUserCharacteristicService()
  {
    return (UserCharacteristicService)getService( UserCharacteristicService.BEAN_NAME );
  }

}
