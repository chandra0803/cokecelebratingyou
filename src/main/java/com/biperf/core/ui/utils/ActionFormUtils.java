/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/utils/ActionFormUtils.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;

/**
 * ActionFormUtils.
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
 * <td>sharma</td>
 * <td>May 24, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ActionFormUtils
{
  public static ActionForm getActionForm( HttpServletRequest request, ServletContext sc, String actionFormName )
  {
    ActionForm actionForm;
    actionForm = (ActionForm)request.getAttribute( actionFormName );
    if ( actionForm == null )
    {
      actionForm = (ActionForm)request.getSession().getAttribute( actionFormName );
      if ( actionForm == null )
      {
        actionForm = (ActionForm)sc.getAttribute( actionFormName );
      }
    }
    return actionForm;
  }
}
