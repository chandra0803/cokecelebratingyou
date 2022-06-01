
package com.biperf.core.ui.process;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.process.ProcessService;
import com.biperf.core.ui.BaseController;

public class ProcessExecutionListController extends BaseController
{
  @SuppressWarnings( "rawtypes" )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List processList = getProcessService().getCurrentlyExecutingProcessess();
    request.setAttribute( "processList", processList );
    request.setAttribute( "allowInterrupt", new Boolean( processList.size() > 0 ) );
  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

}
