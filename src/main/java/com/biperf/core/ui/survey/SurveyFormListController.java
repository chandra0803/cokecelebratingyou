
package com.biperf.core.ui.survey;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.Survey;
import com.biperf.core.service.survey.SurveyService;
import com.biperf.core.ui.BaseController;

public class SurveyFormListController extends BaseController
{
  /**
   * Tiles controller for the SurveyFormList page Overridden from
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
    // use a set so we don't have to worry about duplicate promotions getting added
    Set<Survey> completeSet = new LinkedHashSet<Survey>();
    Set<Survey> underConstructionSet = new LinkedHashSet<Survey>();
    Set<Survey> assignedSet = new LinkedHashSet<Survey>();

    List<Survey> surveyList = getSurveyService().getAll();

    Iterator it = surveyList.iterator();
    while ( it.hasNext() )
    {
      Survey survey = (Survey)it.next();

      if ( survey.isUnderConstruction() )
      {
        underConstructionSet.add( survey );
      }
      else if ( survey.isComplete() )
      {
        completeSet.add( survey );
      }
      else if ( survey.isAssigned() )
      {
        assignedSet.add( survey );
      }
    }
    request.setAttribute( "underConstructionSet", underConstructionSet );
    request.setAttribute( "completeSet", completeSet );
    request.setAttribute( "assignedSet", assignedSet );
  }

  /**
   * Get the SurveyService from the beanLocator.
   * 
   * @return SurveyService
   */
  private SurveyService getSurveyService()
  {
    return (SurveyService)getService( SurveyService.BEAN_NAME );
  }

}
