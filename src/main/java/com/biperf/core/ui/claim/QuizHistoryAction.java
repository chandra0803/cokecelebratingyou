/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/QuizHistoryAction.java,v $
 *
 */

package com.biperf.core.ui.claim;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.StringUtil;

/**
 * ClaimSubmissionAction <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 30, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class QuizHistoryAction extends BaseDispatchAction
{

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    QuizHistoryForm quizHistoryForm = (QuizHistoryForm)form;

    ActionForward forward = mapping.findForward( "display" );

    Date startDate = null;
    Date endDate = null;
    Long submitterId = null;
    String reqSubmitterId = "";

    String promotionId = quizHistoryForm.getChosenPromotionId();
    request.setAttribute( "promotionId", promotionId );

    if ( quizHistoryForm.getChosenStartDate() != null )
    {
      startDate = DateUtils.toDate( quizHistoryForm.getChosenStartDate() );
    }
    else
    {
      if ( request.getParameter( "startDate" ) != null )
      {
        startDate = DateUtils.toDate( request.getParameter( "startDate" ) );
      }
      else
      {
        startDate = getDefaultStartDate();
      }
      quizHistoryForm.setChosenStartDate( DateUtils.toDisplayString( startDate ) );
    }
    request.setAttribute( "startDate", startDate );

    reqSubmitterId = quizHistoryForm.getSubmitterId();
    if ( reqSubmitterId != null && !reqSubmitterId.equals( "" ) )
    {
      submitterId = new Long( Long.parseLong( reqSubmitterId ) );
    }
    request.setAttribute( "submitterId", submitterId );

    if ( quizHistoryForm.getChosenEndDate() != null )
    {
      endDate = DateUtils.toDate( quizHistoryForm.getChosenEndDate() );
    }
    else
    {
      if ( request.getParameter( "endDate" ) != null )
      {
        endDate = DateUtils.toDate( request.getParameter( "endDate" ) );
      }
      else
      {
        endDate = DateUtils.getCurrentDate();
      }
      quizHistoryForm.setChosenEndDate( DateUtils.toDisplayString( endDate ) );
    }
    request.setAttribute( "endDate", endDate );

    quizHistoryForm.setStartDate( DateUtils.toDisplayString( startDate ) );
    quizHistoryForm.setEndDate( DateUtils.toDisplayString( endDate ) );

    return forward;
  }

  /**
   * Returns the default start date.
   *
   * @return the default start date.
   */
  private Date getDefaultStartDate()
  {
    SystemVariableService systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    Date launchDate = systemVariableService.getPropertyByName( SystemVariableService.CLIENT_LAUNCH_DATE ).getDateVal();
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.add( GregorianCalendar.MONTH, -1 );
    Date todayMinusMonth = DateUtils.toStartDate( calendar.getTime() );

    return todayMinusMonth.after( launchDate ) ? todayMinusMonth : launchDate;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showActivity( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    QuizHistoryForm quizHistoryForm = (QuizHistoryForm)form;

    ActionForward forward = mapping.findForward( "display" );

    Date startDate = null;
    Date endDate = null;
    String promotionId = request.getParameter( "promotionId" );

    // if(request.getParameter("promotionId") == null ||
    // request.getParameter("promotionId").equals("")){
    // quizHistoryForm.setChosenPromotionId("");
    // }

    request.setAttribute( "promotionId", promotionId );
    quizHistoryForm.setChosenPromotionId( promotionId );

    if ( request.getParameter( "startDate" ) != null )
    {
      startDate = DateUtils.toDate( request.getParameter( "startDate" ) );
    }
    else
    {
      Calendar calendar = GregorianCalendar.getInstance();
      calendar.add( GregorianCalendar.MONTH, -1 );
      startDate = calendar.getTime();
      quizHistoryForm.setChosenStartDate( DateUtils.toDisplayString( startDate ) );
    }

    request.setAttribute( "startDate", startDate );

    if ( request.getParameter( "endDate" ) != null )
    {
      endDate = DateUtils.toDate( request.getParameter( "endDate" ) );
    }
    else
    {
      endDate = DateUtils.getCurrentDate();
      quizHistoryForm.setChosenEndDate( DateUtils.toDisplayString( endDate ) );
    }

    request.setAttribute( "endDate", endDate );

    quizHistoryForm.setStartDate( DateUtils.toDisplayString( startDate ) );
    quizHistoryForm.setEndDate( DateUtils.toDisplayString( endDate ) );

    return forward;
  }

  public ActionForward showQuizDetailG5( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    ActionForward forward = mapping.findForward( ActionConstants.SUCCESS_FORWARD + "G5" );

    try
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }

      // Deserialize the client state.
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      String isFullPage = (String)clientStateMap.get( "isFullPage" );
      if ( !StringUtil.isEmpty( isFullPage ) )
      {
        if ( !Boolean.valueOf( isFullPage ) )
        {
          forward = mapping.findForward( ActionConstants.SHEET_VIEW );
        }
      }

    }
    catch( InvalidClientStateException e )
    {
      throw new IllegalArgumentException( "request parameter clientState was missing" );
    }

    return forward;
  }
}
