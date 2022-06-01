/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/commlog/CommLogMaintainController.java,v $
 */

package com.biperf.core.ui.commlog;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.CommLogCategoryType;
import com.biperf.core.domain.enums.CommLogReasonType;
import com.biperf.core.domain.enums.CommLogSourceType;
import com.biperf.core.domain.enums.CommLogStatusType;
import com.biperf.core.domain.enums.CommLogUrgencyType;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.util.StringUtils;

/**
 * CommLogMaintainController.
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
 * <td>Ashok</td>
 * <td>Nov 18, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CommLogMaintainController extends BaseController
{

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

    CommLogForm commLogForm = (CommLogForm)request.getAttribute( CommLogForm.FORM_NAME );

    if ( "create".equals( commLogForm.getMethod() ) )
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
        String userId = (String)clientStateMap.get( "userId" );
        if ( userId != null )
        {
          commLogForm.setUserId( userId );
          if ( StringUtils.isEmpty( commLogForm.getAssignedToUserId() ) )
          { // do this check for change user link
            commLogForm.setAssignedToUserId( UserManager.getUserId().toString() );
            commLogForm.setAssignedToName( UserManager.getNameLFMWithComma() );
          }
          else
          { // must be returning from user search
            User user = getUserService().getUserById( new Long( commLogForm.getAssignedToUserId() ) );
            commLogForm.setAssignedToName( user.getNameLFMWithComma() );
          }

          commLogForm.setAssignedByUserId( UserManager.getUserId().toString() );
        }
      }
      catch( InvalidClientStateException e )
      {
        throw new IllegalArgumentException( "request parameter clientState was missing" );
      }
    }

    List commLogSourceTypeList = CommLogSourceType.getList();
    List commLogCategoryTypeList = CommLogCategoryType.getList();
    List commLogReasonTypeList = CommLogReasonType.getList();
    List commLogStatusTypeList = CommLogStatusType.getList();
    List commLogUrgencyTypeList = CommLogUrgencyType.getList();

    request.setAttribute( "commLogSourceTypesList", commLogSourceTypeList );
    request.setAttribute( "commLogCategoryTypesList", commLogCategoryTypeList );
    request.setAttribute( "commLogReasonTypesList", commLogReasonTypeList );
    request.setAttribute( "commLogStatusTypesList", commLogStatusTypeList );
    request.setAttribute( "commLogUrgencyTypesList", commLogUrgencyTypeList );

    // for displaying name
    if ( commLogForm.getUserId() != null && !"".equals( commLogForm.getUserId() ) )
    {
      request.setAttribute( "displayNameUserId", commLogForm.getUserId() );
    }

  }

  /**
   * Returns a reference to the User service.
   * 
   * @return a reference to the User service.
   */
  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }
}
