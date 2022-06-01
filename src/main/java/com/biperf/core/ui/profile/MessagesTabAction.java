
package com.biperf.core.ui.profile;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.MetaDataBean;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.commlog.CommLogService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.HtmlUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.CommLogValueBean;

/**
 * 
 * @author dudam
 * @since Dec 19, 2012
 * @version 1.0
 * 
 * In G5 profile page. Alerts & Messages Tab executes this
 */
public class MessagesTabAction extends BaseDispatchAction
{
  private static final int DEFAULT_SORTED_ON = 1;
  private static final int DEFAULT_DATA_SORTED_ON = 2;
  private static final String DEFAULT_SORTED_BY = "desc";
  private static final int DEFAULT_PAGE_NUMBER = 1;
  private static final int DEFAULT_RESULTS_PER_PAGE = 25;

  public ActionForward fetchMessages( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // Build view object
    AlertsAndMessagesTabView view = new AlertsAndMessagesTabView();

    int sortedOn = StringUtils.isBlank( request.getParameter( "sortedOn" ) ) ? DEFAULT_SORTED_ON : Integer.valueOf( request.getParameter( "sortedOn" ) );
    String sortedBy = StringUtils.isBlank( request.getParameter( "sortedBy" ) ) ? DEFAULT_SORTED_BY : request.getParameter( "sortedBy" );
    int pageNumber = StringUtils.isBlank( request.getParameter( "pageNumber" ) ) ? DEFAULT_PAGE_NUMBER : Integer.valueOf( request.getParameter( "pageNumber" ) );
    int resultsPerPage = StringUtils.isBlank( request.getParameter( "resultsPerPage" ) ) ? DEFAULT_RESULTS_PER_PAGE : Integer.valueOf( request.getParameter( "resultsPerPage" ) );

    AuthenticatedUser authenticatedUser = UserManager.getUser();
    authenticatedUser.setRouteId( request.getHeader( "proxy-jroute" ) );

    // Get List of messages from comm log
    List<CommLogValueBean> userCommLogs = getCommLogService().getCommLogsForUser( UserManager.getUserId(), pageNumber, resultsPerPage, getDefaultStartDate(), getDefaultEndDate(), sortedOn, sortedBy );
    if ( userCommLogs != null && userCommLogs.size() > 0 )
    {
      for ( CommLogValueBean commLog : userCommLogs )
      {
        // BELOW PIECE OF CODE IS COMMENTED SINCE IT IS BREAKING G6
        // if(commLog.getCreatedBy().equals(UserManager.getUserId()))
        // {
        // continue;
        // }
        view.getMessageView().add( buildMessage( commLog, request ) );
      }
    }

    // Get sorting/pagination meta data
    Long maxRows = getCommLogService().getCommLogCountForUser( UserManager.getUserId(), getDefaultStartDate(), getDefaultEndDate() );
    MetaDataBean meta = new MetaDataBean( sortedOn, sortedBy, maxRows );
    view.setMeta( meta );
    if ( maxRows > resultsPerPage )
    {
      view = addPaginationInformation( view, maxRows, pageNumber, resultsPerPage );
    }

    populateMessageIds( view.getMessageView() );

    super.writeAsJsonToResponse( view, response );
    return null;
  }

  private void populateMessageIds( List<MessageView> messageViewList )
  {
    long alertId = 0;
    for ( MessageView av : messageViewList )
    {
      av.setMessageIdNum( alertId++ );
    }
  }

  // QC bug #2606 fix
  private AlertsAndMessagesTabView addPaginationInformation( AlertsAndMessagesTabView view, Long maxRows, int pageNumber, int resultsPerPage )
  {
    int startIndex = 0;
    int endIndex = 0;

    if ( pageNumber == 1 )
    {
      startIndex = 1;
      endIndex = resultsPerPage;
    }
    else
    {
      startIndex = ( pageNumber - 1 ) * resultsPerPage + 1;
      endIndex = startIndex + 24;
    }
    if ( endIndex > maxRows )
    {
      endIndex = maxRows.intValue();
    }
    view.setMessageCounter( startIndex + " - " + endIndex + " of " + maxRows + " items" );
    return view;
  }

  private MessageView buildMessage( CommLogValueBean commLogValueBean, HttpServletRequest request )
  {
    MessageView messageView = new MessageView();
    messageView.setMessageDate( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.getDateFromTimeStamp( commLogValueBean.getDateCreated() ), UserManager.getTimeZoneID() ) ) );
    messageView.setMessageLinkUrl( buildMessageDetailUrl( commLogValueBean, request ) );
    messageView.setMessageText( "" );
    messageView.setMessageTitle( commLogValueBean.getSubject() );
    messageView.setSortDate( DateUtils.getSortDateString( commLogValueBean.getDateCreated() ) );
    messageView.setMessageTextPlain( HtmlUtils.removeFormatting( commLogValueBean.getPlainMessage() ) );
    messageView.setHtmlMessageTextBodyCopy( commLogValueBean.getPlainMessage() );
    return messageView;
  }

  private String buildMessageDetailUrl( CommLogValueBean commLogValueBean, HttpServletRequest request )
  {
    Map<String, Long> clientStateParameterMap = new HashMap<String, Long>();
    clientStateParameterMap.put( "commLogId", commLogValueBean.getCommLogId() );
    return ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.MESSAGE_DETAIL_URL, clientStateParameterMap );
  }

  private CommLogService getCommLogService()
  {
    return (CommLogService)getService( CommLogService.BEAN_NAME );
  }

  private Date getDefaultStartDate()
  {
    Calendar calendar = GregorianCalendar.getInstance();
    calendar.add( GregorianCalendar.YEAR, -1 );
    Date todayMinusMonth = DateUtils.toStartDate( calendar.getTime() );
    return todayMinusMonth;
  }

  private Date getDefaultEndDate()
  {
    return DateUtils.toEndDate( DateUtils.getCurrentDate() );
  }

}
