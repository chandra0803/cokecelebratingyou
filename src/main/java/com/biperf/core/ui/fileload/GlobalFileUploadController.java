
package com.biperf.core.ui.fileload;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.GlobalFileType;
import com.biperf.core.ui.BaseController;

public class GlobalFileUploadController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    request.setAttribute( "fileTypeList", GlobalFileType.getList() );
  }

}
