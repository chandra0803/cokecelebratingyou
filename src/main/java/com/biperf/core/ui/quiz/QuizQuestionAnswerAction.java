/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/quiz/QuizQuestionAnswerAction.java,v $
 */

package com.biperf.core.ui.quiz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.quiz.QuizQuestionAnswer;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.quiz.QuizService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ActionUtils;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.biperf.core.utils.ClientStateUtils;

/*
 * QuizQuestionAnswerAction <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Oct
 * 28, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class QuizQuestionAnswerAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( QuizQuestionAnswerAction.class );

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "display";
    logger.debug( ">>> " + METHOD_NAME );

    String forward = ActionConstants.SUCCESS_FORWARD;

    QuizQuestionAnswerForm quizQuestionAnswerForm = (QuizQuestionAnswerForm)form;

    quizQuestionAnswerForm.setMethod( "save" );
    Long quizQuestionAnswerId = quizQuestionAnswerForm.getQuizQuestionAnswerId();

    if ( quizQuestionAnswerId != null && quizQuestionAnswerId.longValue() > 0 )
    {
      quizQuestionAnswerForm.load( getQuizService().getQuizQuestionAnswerById( quizQuestionAnswerId ) );
    }

    logger.info( "<<< " + METHOD_NAME );
    return mapping.findForward( forward );
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    final String METHOD_NAME = "save";

    String forwardAction = ActionConstants.SUCCESS_FORWARD;

    ActionMessages errors = new ActionMessages();
    QuizQuestionAnswerForm quizQuestionAnswerForm = (QuizQuestionAnswerForm)form;

    if ( isCancelled( request ) )
    {
      // EARLY EXIT
      logger.info( ">>> " + METHOD_NAME + " cancelled." );
      forwardAction = ActionConstants.CANCEL_FORWARD;
      Map clientStateParameterMap = new HashMap();
      clientStateParameterMap.put( "quizQuestionId", quizQuestionAnswerForm.getQuizQuestionId() );
      String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
      return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=display" } );
    }

    QuizQuestionAnswer answer = null;
    try
    {
      answer = getQuizService().saveQuizQuestionAnswer( quizQuestionAnswerForm.getQuizQuestionId(),
                                                        quizQuestionAnswerForm.toDomainObject(),
                                                        quizQuestionAnswerForm.getQuizQuestionAnswerText(),
                                                        quizQuestionAnswerForm.getQuizQuestionAnswerExplanation() );
      quizQuestionAnswerForm.setQuizQuestionAnswerId( answer.getId() );
    }
    catch( ServiceErrorException se )
    {
      List serviceErrors = se.getServiceErrors();
      ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forwardAction = ActionConstants.FAIL_FORWARD;
    }

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "quizQuestionId", quizQuestionAnswerForm.getQuizQuestionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, forwardAction, new String[] { queryString, "method=display" } );
  } // end save

  private QuizService getQuizService()
  {
    return (QuizService)getService( QuizService.BEAN_NAME );
  }

  public ActionForward saveAndAddAnother( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    this.save( mapping, form, request, response );
    QuizQuestionAnswerForm quizQuestionAnswerForm = (QuizQuestionAnswerForm)form;
    quizQuestionAnswerForm.clearForNewQuestionAnswer();

    Map clientStateParameterMap = new HashMap();
    clientStateParameterMap.put( "quizQuestionId", quizQuestionAnswerForm.getQuizQuestionId() );
    String queryString = ClientStateUtils.generateEncodedLink( "", "", clientStateParameterMap );
    return ActionUtils.forwardWithParameters( mapping, ActionConstants.SUCCESS_CREATE, new String[] { queryString, "method=display" } );
  }
}
