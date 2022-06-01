/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/claim/TransactionHistoryAction.java,v $
 *
 */

package com.biperf.core.ui.claim;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.commlog.CommLog;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.enums.TransactionHistoryType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.commlog.CommLogAssociationRequest;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.util.StringUtils;

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
public class TransactionHistoryAction extends BaseDispatchAction
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
    ActionForward forward = null;
    TransactionHistoryForm transactionHistoryForm = (TransactionHistoryForm)form;

    Calendar calendar = null;
    Date startDate = null;
    Date endDate = null;
    Date liveStartDate = null;
    Date liveEndDate = null;
    String promotionType = null;
    String promotionId = null;

    if ( request.getParameter( "liveStartDate" ) != null )
    {
      liveStartDate = DateUtils.toDate( request.getParameter( "liveStartDate" ) );
    }
    else
    {
      calendar = GregorianCalendar.getInstance();
      calendar.add( GregorianCalendar.MONTH, -1 );
      liveStartDate = calendar.getTime();
    }

    if ( request.getParameter( "liveEndDate" ) != null )
    {
      liveEndDate = DateUtils.toDate( request.getParameter( "liveEndDate" ) );
    }
    else
    {
      liveEndDate = DateUtils.getCurrentDate();
    }

    if ( request.getParameter( "startDate" ) == null )
    {
      startDate = liveStartDate;
    }
    else
    {
      startDate = DateUtils.toDate( request.getParameter( "startDate" ) );
    }

    if ( request.getParameter( "endDate" ) == null )
    {
      endDate = liveEndDate;
    }
    else
    {
      endDate = DateUtils.toDate( request.getParameter( "endDate" ) );
    }

    if ( request.getParameter( "livePromotionId" ) != null )
    {
      promotionId = request.getParameter( "livePromotionId" );
    }

    transactionHistoryForm.setStartDate( DateUtils.toDisplayString( startDate ) );
    transactionHistoryForm.setEndDate( DateUtils.toDisplayString( endDate ) );
    transactionHistoryForm.setLiveStartDate( DateUtils.toDisplayString( liveStartDate ) );
    transactionHistoryForm.setLiveEndDate( DateUtils.toDisplayString( liveEndDate ) );

    request.setAttribute( "startDate", liveStartDate );
    request.setAttribute( "endDate", liveEndDate );
    request.setAttribute( "promotionId", promotionId );

    if ( request.getParameter( "livePromotionType" ) == null && request.getParameter( "promotionType" ) != null )
    {
      promotionType = request.getParameter( "promotionType" );
      transactionHistoryForm.setLivePromotionType( promotionType );
    }
    else if ( request.getParameter( "livePromotionType" ) != null )
    {
      promotionType = request.getParameter( "livePromotionType" );
    }
    else
    {
      promotionType = "";
    }

    request.setAttribute( "promotionType", request.getParameter( "promotionType" ) );

    if ( promotionType.equals( "" ) )
    {
      forward = mapping.findForward( "no_promotion_type" );
    }
    else if ( promotionType.equals( PromotionType.PRODUCT_CLAIM ) )
    {
      forward = mapping.findForward( PromotionType.PRODUCT_CLAIM );
    }
    else if ( promotionType.equals( PromotionType.RECOGNITION ) )
    {
      forward = mapping.findForward( PromotionType.RECOGNITION );
    }
    else if ( promotionType.equals( PromotionType.NOMINATION ) )
    {
      forward = mapping.findForward( PromotionType.NOMINATION );
    }
    else if ( promotionType.equals( PromotionType.QUIZ ) )
    {
      forward = mapping.findForward( PromotionType.QUIZ );
    }
    else if ( promotionType.equals( TransactionHistoryType.DISCRETIONARY ) )
    {
      forward = mapping.findForward( TransactionHistoryType.DISCRETIONARY );
    }
    else if ( promotionType.equals( TransactionHistoryType.GOALQUEST ) )
    {
      forward = mapping.findForward( TransactionHistoryType.GOALQUEST );
    }
    else if ( promotionType.equals( TransactionHistoryType.REVERSAL ) )
    {
      forward = mapping.findForward( TransactionHistoryType.REVERSAL );
    }
    else
    {
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
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
    ActionForward forward = null;
    TransactionHistoryForm transactionHistoryForm = (TransactionHistoryForm)form;

    Calendar calendar = null;
    Date startDate = null;
    Date endDate = null;
    Date liveStartDate = null;
    Date liveEndDate = null;
    String promotionType = null;
    String promotionId = null;

    if ( transactionHistoryForm.getStartDate() != null )
    {
      startDate = DateUtils.toDate( transactionHistoryForm.getStartDate() );
    }
    else
    {
      calendar = GregorianCalendar.getInstance();
      calendar.add( GregorianCalendar.MONTH, -1 );
      startDate = calendar.getTime();
    }

    if ( transactionHistoryForm.getEndDate() != null )
    {
      endDate = DateUtils.toDate( transactionHistoryForm.getEndDate() );
    }
    else
    {
      endDate = DateUtils.getCurrentDate();
    }

    liveStartDate = startDate;
    liveEndDate = endDate;

    if ( request.getParameter( "promotionId" ) != null && !request.getParameter( "promotionId" ).equals( "" ) )
    {
      promotionId = request.getParameter( "promotionId" );
    }

    transactionHistoryForm.setStartDate( DateUtils.toDisplayString( startDate ) );
    transactionHistoryForm.setEndDate( DateUtils.toDisplayString( endDate ) );
    transactionHistoryForm.setLiveStartDate( DateUtils.toDisplayString( liveStartDate ) );
    transactionHistoryForm.setLiveEndDate( DateUtils.toDisplayString( liveEndDate ) );

    promotionType = transactionHistoryForm.getPromotionType();
    // empty string is okay
    if ( promotionType == null )
    {
      Map clientStateMap = ClientStateUtils.getClientStateMap( request );

      if ( StringUtils.isEmpty( promotionType ) )
      {
        promotionType = (String)clientStateMap.get( "promotionType" );
      }
    }
    transactionHistoryForm.setLivePromotionType( promotionType );

    transactionHistoryForm.setLivePromotionId( promotionId );

    request.setAttribute( "startDate", startDate );
    request.setAttribute( "endDate", endDate );
    request.setAttribute( "promotionId", promotionId );
    request.setAttribute( "promotionType", promotionType );

    if ( promotionType != null )
    {
      if ( promotionType.equals( "" ) )
      {
        forward = mapping.findForward( "no_promotion_type" );
      }
      else if ( promotionType.equals( PromotionType.PRODUCT_CLAIM ) )
      {
        forward = mapping.findForward( PromotionType.PRODUCT_CLAIM );
      }
      else if ( promotionType.equals( PromotionType.NOMINATION ) )
      {
        request.setAttribute( "promotionTypeCode", "nomination.history" );
        forward = mapping.findForward( PromotionType.NOMINATION );
      }
      else if ( promotionType.equals( PromotionType.RECOGNITION ) )
      {
        request.setAttribute( "promotionTypeCode", "recognition.history" );
        forward = mapping.findForward( PromotionType.RECOGNITION );
      }
      else if ( promotionType.equals( PromotionType.QUIZ ) )
      {
        forward = mapping.findForward( PromotionType.QUIZ );
      }
      else if ( promotionType.equals( TransactionHistoryType.DISCRETIONARY ) )
      {
        forward = mapping.findForward( TransactionHistoryType.DISCRETIONARY );
      }
      else if ( promotionType.equals( TransactionHistoryType.GOALQUEST ) )
      {
        forward = mapping.findForward( TransactionHistoryType.GOALQUEST );
      }
      else if ( promotionType.equals( TransactionHistoryType.REVERSAL ) )
      {
        forward = mapping.findForward( TransactionHistoryType.REVERSAL );
      }
    }
    else
    {
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showClaimDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showRecognitionDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showNominationDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward resendEmail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Map clientStateMap = ClientStateUtils.getClientStateMap( request );
    if ( clientStateMap != null )
    {

      Long commLogId = (Long)clientStateMap.get( "commLogId" );
      AssociationRequestCollection requestCollection = new AssociationRequestCollection();
      requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.ALL ) );
      requestCollection.add( new CommLogAssociationRequest( CommLogAssociationRequest.MAILING_WITH_RECIPIENTS_AND_LOCALES ) );

      CommLog commLog = getCommLogService().getCommLogById( commLogId, requestCollection );
      User user = commLog.getUser();
      String newEmail = RequestUtils.getOptionalParamString( request, "newEmail" );
      try
      {
        Mailing mailing = commLog.getMailing();
        mailing = getMailingService().reSubmitMailing( mailing, user, newEmail, true );
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while resubmitting mail from commlog " + commLog.getId() );
      }
    }
    // Need to redirect back with same client state
    try
    {
      String forwardPath = mapping.findForward( "resend_success" ).getPath() + "&clientState=" + URLEncoder.encode( RequestUtils.getRequiredParamString( request, "clientState" ), "UTF-8" )
          + "&cryptoPass=" + RequestUtils.getRequiredParamString( request, "cryptoPass" );
      return new ActionForward( forwardPath, true );
    }
    catch( UnsupportedEncodingException uee )
    {
    }
    return null;
  }

  /**
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showQuizDetail( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Submit before going to product page
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showPayouts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Submit before going to product page
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showActivities( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Submit before going to product page
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward showAuditMessages( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {

    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  /**
   * Get the CommLogService from the beanLocator.
   * 
   * @return CommLogService
   */
  private CommLogService getCommLogService()
  {
    return (CommLogService)getService( CommLogService.BEAN_NAME );
  }

  /**
   * Get the MailingService from the beanLocator.
   * 
   * @return MailingService
   */
  private MailingService getMailingService()
  {
    return (MailingService)getService( MailingService.BEAN_NAME );
  }

}
