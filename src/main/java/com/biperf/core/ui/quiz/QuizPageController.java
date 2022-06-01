/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/quiz/QuizPageController.java,v $
 */

package com.biperf.core.ui.quiz;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.UserManager;

/**
 * QuizPageController.
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
 * <td>Oct 09, 2012</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizPageController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    if ( request.getSession().getAttribute( "eligibleQuizList" ) != null )
    {
      request.setAttribute( "quizSubmissionList", request.getSession().getAttribute( "eligibleQuizList" ) );
      request.getSession().removeAttribute( "eligibleQuizList" );
    }
    else
    {
      request.setAttribute( "quizSubmissionList", getPromotionService().getPendingQuizSubmissionList( UserManager.getUserId() ) );
    }

    AuthenticatedUser user = UserManager.getUser();
    boolean displayManageQuizzes = false;
    if ( user.isParticipant() )
    {
      Participant participant = getParticipantService().getParticipantById( user.getUserId() );
      displayManageQuizzes = getPromotionService().isParticipantInDIYPromotionAudience( participant );
    }
    request.setAttribute( "displayManageQuizzes", displayManageQuizzes );
  }

  /**
   * Get ParticipantService
   * @return
   */
  public static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Gets a PromotionService
   * 
   * @return PromotionService
   * @throws Exception
   */
  private PromotionService getPromotionService() throws Exception
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }
}
