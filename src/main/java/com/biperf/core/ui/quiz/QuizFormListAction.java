/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/quiz/QuizFormListAction.java,v $ */

package com.biperf.core.ui.quiz;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ArrayUtil;

/**
 * Action class for Quiz Form List operations.
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
 * <td>Oct 27, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class QuizFormListAction extends BaseDispatchAction
{
  /** Log */
  private static final Log logger = LogFactory.getLog( QuizFormListAction.class );

  /**
   * Delete under construction quizes
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteUnderConstructionQuizes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    QuizFormListForm quizListForm = (QuizFormListForm)form;

    if ( quizListForm.getDeleteUnderConstructionIds() != null && quizListForm.getDeleteUnderConstructionIds().length > 0 )
    {
      deleteQuizes( quizListForm.getDeleteUnderConstructionIds() );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * Delete complete quizes
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward deleteCompleteQuizes( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    String forwardTo = ActionConstants.SUCCESS_FORWARD;

    QuizFormListForm quizListForm = (QuizFormListForm)form;

    if ( quizListForm.getDeleteCompletedIds() != null && quizListForm.getDeleteCompletedIds().length > 0 )
    {
      deleteQuizes( quizListForm.getDeleteCompletedIds() );
    }

    // get the actionForward to display the udpate pages.
    return mapping.findForward( forwardTo );
  }

  /**
   * delete a list of quizes
   * 
   * @param quizFormIds
   */
  private void deleteQuizes( String[] quizFormIds )
  {
    // Convert String[] of quizFormIds to Long[]
    List quizFormIdList = ArrayUtil.convertStringArrayToListOfLongObjects( quizFormIds );
    try
    {
      getQuizService().deleteQuizes( quizFormIdList );
    }
    catch( ServiceErrorException e )
    {
      // Exception thrown if the quiz to be deleted is assigned
      logger.error( e.getMessage(), e );
    }
  }

  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }
}
