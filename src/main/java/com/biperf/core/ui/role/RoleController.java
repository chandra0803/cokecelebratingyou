/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.role;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.UserType;
import com.biperf.core.ui.BaseController;

public class RoleController extends BaseController
{
  /**
   * Fetches data for the Create Role page.
   *
   * @param tileContext the context for the tile associated with this controller.
   * @param request the HTTP request we are processing.
   * @param response the HTTP response we are creating.
   * @param servletContext the context for servlets of this web application.
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    request.setAttribute( "userTypes", UserType.getList() );
  }
}
