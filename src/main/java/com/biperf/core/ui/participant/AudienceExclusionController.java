
package com.biperf.core.ui.participant;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;

public class AudienceExclusionController extends BaseController
{

  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    ListBuilderForm listBuilderForm = (ListBuilderForm)request.getAttribute( "listBuilderForm" );
  }
}
