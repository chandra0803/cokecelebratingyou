/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/survey/SurveyPromotionListController.java,v $
 */

package com.biperf.core.ui.survey;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the SurveyPromotionList page.
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
 * <td>arasi</td>
 * <td>Sept 3, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SurveyPromotionListController extends BaseController
{
  /**
   * Tiles controller for the SurveyPromotionList page Overridden from
   * 
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    SurveyListForm form = (SurveyListForm)request.getAttribute( "surveyListForm" );
    Survey survey = getSurveyService().getSurveyById( new Long( form.getSurveyFormId() ) );
    request.setAttribute( "survey", survey );
  }

  /**
   * Get the QuizService from the beanLocator.
   * 
   * @return QuizService
   */
  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

}
