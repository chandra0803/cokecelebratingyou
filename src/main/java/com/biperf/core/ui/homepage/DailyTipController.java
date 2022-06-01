/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/homepage/DailyTipController.java,v $
 */

package com.biperf.core.ui.homepage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

public class DailyTipController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    request.setAttribute( "dailyTipsRefreshRate", new Integer( getRefreshInterval() ) );

    MainContentService mainContentService = (MainContentService)getService( MainContentService.BEAN_NAME );
    request.setAttribute( "dailyTips", mainContentService.getDailyTips() );
  }

  private int getRefreshInterval()
  {
    PropertySetItem prop = ( (SystemVariableService)getService( SystemVariableService.BEAN_NAME ) ).getPropertyByName( SystemVariableService.TIP_DAY_ROTATE_SECONDS );
    // apply default 10 seconds if there is no value
    return null != prop ? prop.getIntVal() : 10;
  }
}