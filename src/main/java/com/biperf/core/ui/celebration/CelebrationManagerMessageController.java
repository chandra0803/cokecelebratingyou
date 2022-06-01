/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/help/ContactUsController.java,v $
 */

package com.biperf.core.ui.celebration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.celebration.CelebrationService;
import com.biperf.core.ui.BaseController;

public class CelebrationManagerMessageController extends BaseController
{
  /**
   * Tiles controller for the ContactUs page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    CelebrationManagerMessageForm managerMessageForm = (CelebrationManagerMessageForm)request.getAttribute( "managerMessageForm" );
    if ( managerMessageForm == null )
    {
      managerMessageForm = new CelebrationManagerMessageForm();
      request.setAttribute( "managerMessageForm", managerMessageForm );
    }

  }

  private CelebrationService getCelebrationService()
  {
    return (CelebrationService)getService( CelebrationService.BEAN_NAME );
  }

}
