/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/tiles/BeaconTilesRequestProcessor.java,v $
 */

package com.biperf.core.ui.tiles;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.tiles.TilesRequestProcessor;
import org.apache.struts.util.TokenProcessor;

/**
 * BeaconTilesRequestProcessor.
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
 * <td>Adam</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BeaconTilesRequestProcessor extends TilesRequestProcessor
{
  /**
   * Overridden from
   * 
   * @see org.apache.struts.action.RequestProcessor#processActionPerform(javax.servlet.http.HttpServletRequest,
   *      javax.servlet.http.HttpServletResponse, org.apache.struts.action.Action,
   *      org.apache.struts.action.ActionForm, org.apache.struts.action.ActionMapping)
   * @param request
   * @param response
   * @param action
   * @param form
   * @param mapping
   * @return ActionForward return from the Action
   * @throws IOException
   * @throws ServletException
   */
  protected ActionForward processActionPerform( HttpServletRequest request, HttpServletResponse response, Action action, ActionForm form, ActionMapping mapping ) throws IOException, ServletException
  {
    ActionForward forward = null;
    forward = super.processActionPerform( request, response, action, form, mapping );
    if ( request.getParameter( "doNotSaveToken" ) == null )
    {
      /* Bug # 31949 start */
      if ( request.getSession( false ) != null )
      {
        String savedToken = (String)request.getSession( false ).getAttribute( "org.apache.struts.action.TOKEN" );
        if ( savedToken == null )
        {
          TokenProcessor.getInstance().saveToken( request );
        }
      }
      /* Bug # 31949 end */
    }
    return forward;
  }
}
