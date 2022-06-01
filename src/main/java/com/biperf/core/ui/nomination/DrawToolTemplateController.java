
package com.biperf.core.ui.nomination;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.recognition.SupportedEcardVideoTypes;
import com.biperf.core.utils.ImageUtils;

public class DrawToolTemplateController extends BaseController
{

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    // Attributes for the error modal
    request.setAttribute( "maxImageSize", String.valueOf( ImageUtils.getImageSizeLimit() ) );
    request.setAttribute( "maxVideoSize", String.valueOf( ImageUtils.getVideoSizeLimit() ) );
    request.setAttribute( "supportedImageTypes", ImageUtils.getValidImageTypes().toUpperCase() );
    request.setAttribute( "supportedVideoTypes", SupportedEcardVideoTypes.getSupportedVideoTypes().toUpperCase() );
  }

}
