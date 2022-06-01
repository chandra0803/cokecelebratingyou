/**
 * 
 */

package com.biperf.core.ui.instantpoll;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.instantpoll.InstantPollService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.value.instantpoll.InstantPollsListbean;

/**
 * @author poddutur
 *
 */
public class InstantPollListController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List<InstantPollsListbean> instantPollsList = new ArrayList<InstantPollsListbean>();

    instantPollsList = getInstantPollService().getAllInstantPollsList();

    request.setAttribute( "instantPollsList", instantPollsList );
  }

  private InstantPollService getInstantPollService()
  {
    return (InstantPollService)getService( InstantPollService.BEAN_NAME );
  }

}
