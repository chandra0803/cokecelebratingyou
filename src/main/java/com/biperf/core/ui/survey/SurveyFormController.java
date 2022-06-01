
package com.biperf.core.ui.survey;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.ui.BaseController;

public class SurveyFormController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "moduleList", PromotionType.getSurveyList() );
  }

}
