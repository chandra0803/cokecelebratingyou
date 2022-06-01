
package com.biperf.core.ui.admin.indexing;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.indexing.BIElasticSearchAdminService;
import com.biperf.core.indexing.EsIndexStatus;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;

public class ElastiSearchAdminAction extends BaseDispatchAction
{
  private static final Log log = LogFactory.getLog( ElastiSearchAdminAction.class );

  protected ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    return display( mapping, form, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm aForm, HttpServletRequest request, HttpServletResponse response )
  {
    EsIndexStatus status = new EsIndexStatus();
    try
    {
      status = getIndexingService().getIndexStatus();
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
    }
    request.setAttribute( "status", status );
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  private BIElasticSearchAdminService getIndexingService()
  {
    return (BIElasticSearchAdminService)getService( BIElasticSearchAdminService.BEAN_NAME );
  }
}
