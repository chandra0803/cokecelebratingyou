/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/quiz/QuizFormListController.java,v $
 */

package com.biperf.core.ui.quiz;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.quiz.Quiz;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the QuizFormList page.
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
 * <td>jenniget</td>
 * <td>Oct 26, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizFormListController extends BaseController
{
  /**
   * Tiles controller for the QuizFormList page Overridden from
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
    Set completeSet = new LinkedHashSet();
    Set underConstructionSet = new LinkedHashSet();
    Set assignedSet = new LinkedHashSet();

    List quizList = new ArrayList();
    quizList = getQuizService().getAll();

    Iterator it = quizList.iterator();

    while ( it.hasNext() )
    {
      Quiz quiz = (Quiz)it.next();

      if ( quiz.isUnderConstruction() )
      {
        underConstructionSet.add( quiz );
      }
      else if ( quiz.isComplete() )
      {
        completeSet.add( quiz );
      }
      else if ( quiz.isAssigned() )
      {
        assignedSet.add( quiz );
      }
    }
    request.setAttribute( "underConstructionSet", underConstructionSet );
    request.setAttribute( "completeSet", completeSet );
    request.setAttribute( "assignedSet", assignedSet );
  }

  /**
   * Get the QuizService from the beanLocator.
   * 
   * @return QuizService
   */
  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }
}
