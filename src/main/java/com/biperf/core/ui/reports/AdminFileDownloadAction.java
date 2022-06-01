
package com.biperf.core.ui.reports;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.profile.AlertView;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AlertsValueBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class AdminFileDownloadAction extends BaseDispatchAction
{
  public ActionForward fetchAlrts( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    List<AlertsValueBean> alertBeans = getPromotionService().getPendingFileDownloadsForAlerts( UserManager.getUserId() );
    List<AlertView> alerts = new ArrayList<AlertView>();
    for ( AlertsValueBean bean : alertBeans )
    {
      alerts.add( buildAlert( bean, request ) );
    }
    request.setAttribute( "alerts", alerts );
    return mapping.findForward( "success" );
  }

  private AlertView buildAlert( AlertsValueBean alert, HttpServletRequest request )
  {
    AlertView alertView = new AlertView();

    alertView.setLink( true );
    alertView.setAlertLinkUrl( buildAlertUrl( alert, request ) );
    alertView.setAlertLinkText( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.DOWNLOAD" ) );
    alertView.setAlertText( "" );
    alertView.setAlertSubject( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.YOU_HAVE" ) + " " + alert.getFileName() + " "
        + CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.FILE_EXTRACT" ) + " " + alert.getFileDownloadExpiryDate() );
    alertView.setDatePostedDisplay( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getFileDownloadRequestedDate() ), UserManager.getTimeZoneID() ) ) );
    alertView.setDatePostedSort( DateUtils.toDisplayString( DateUtils.applyTimeZone( DateUtils.toDate( alert.getFileDownloadRequestedDate() ), UserManager.getTimeZoneID() ) ) );
    alertView.setOpenNewWindow( false );

    return alertView;
  }

  private String buildAlertUrl( AlertsValueBean alert, HttpServletRequest request )
  {
    Map<String, Long> clientStateParameterMap = new HashMap<String, Long>();
    clientStateParameterMap.put( "fileStoreId", alert.getFileStoreId() );
    return ClientStateUtils.generateEncodedLink( RequestUtils.getBaseURI( request ), PageConstants.ALERT_DETAIL_FILE_DOWNLOAD, clientStateParameterMap );
  }

  private PromotionService getPromotionService()
  {
    return (PromotionService)BeanLocator.getBean( PromotionService.BEAN_NAME );
  }
}
