/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/homepage/QuickSearchController.java,v $
 */

package com.biperf.core.ui.homepage;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.QuickSearchClaimSearchByField;
import com.biperf.core.domain.enums.QuickSearchParticipantSearchByField;
import com.biperf.core.domain.enums.QuickSearchSearchForField;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.UserManager;

/**
 * QuickSearchController.
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
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuickSearchController extends BaseController
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
    if ( UserManager.getUser().isUser() )
    {
      request.setAttribute( "quickSearchEnabled", "true" );
      request.setAttribute( "quickSearchSearchForFields", QuickSearchSearchForField.getList() );
      request.setAttribute( "quickSearchSearchForDefault", QuickSearchSearchForField.PARTICIPANT );
      request.setAttribute( "quickSearchSearchByFields", buildSearchByFields() );
      request.setAttribute( "quickSearchSearchByDefault", "pax_lastname" );
      request.setAttribute( "quickSearchClientName", getSystemVariableService().getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
      request.setAttribute( "quickSearchEnvironment", Environment.getEnvironment() );

      if ( Environment.getEnvironment().equalsIgnoreCase( Environment.ENV_PROD ) )
      {
        request.setAttribute( "prodEnvLabel", Environment.ENV_PROD );
      }
    }

  }

  private List buildSearchByFields()
  {
    List searchByFields = new ArrayList();

    searchByFields.addAll( QuickSearchParticipantSearchByField.getList() );
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.INSTALL_PRODUCTCLAIMS ).getBooleanVal() )
    {
      searchByFields.addAll( QuickSearchClaimSearchByField.getList() );
    }

    return searchByFields;
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }
}
