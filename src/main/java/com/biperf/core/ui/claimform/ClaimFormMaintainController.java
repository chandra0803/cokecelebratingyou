/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claimform/ClaimFormMaintainController.java,v $
 */

package com.biperf.core.ui.claimform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ClaimFormModuleType;
import com.biperf.core.ui.BaseController;

/**
 * ClaimFormMaintainController.
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
 * <td>robinsra</td>
 * <td>Jun 7, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ClaimFormMaintainController extends BaseController
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
    List modules = ClaimFormModuleType.getList();
    List filteredModules = new ArrayList( modules.size() );
    // remove Quiz & Goalquest if it exists, because they don't use claim form
    for ( Iterator iter = modules.iterator(); iter.hasNext(); )
    {
      ClaimFormModuleType module = (ClaimFormModuleType)iter.next();
      if ( !module.isQuiz() && !module.isGoalquest() )
      {
        filteredModules.add( module );
      }
    }
    request.setAttribute( "moduleList", filteredModules );
  }

}
