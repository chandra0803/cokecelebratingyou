
package com.biperf.core.ui.process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.service.jms.GJavaMessageService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.jms.JmsProcessInterruptMessage;

public class ProcessExecutionListAction extends BaseDispatchAction
{
  public ActionForward unspecified( ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return display( actionMapping, actionForm, request, response );
  }

  public ActionForward display( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.SUCCESS_FORWARD );
  }

  public ActionForward interruptProcess( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    ProcessExecutionListForm form = (ProcessExecutionListForm)actionForm;

    String[] processIds = form.getInterruptProcessIds();
    if ( null != processIds && processIds.length > 0 )
    {
      for ( String processId : processIds )
      {
        JmsProcessInterruptMessage jmsMessage = new JmsProcessInterruptMessage();
        jmsMessage.setProcessId( new Long( processId ) );
        getGJavaMessageService().sendToJmsTopic( jmsMessage );
      }
    }

    return display( mapping, actionForm, request, response );
  }

  public ActionForward interruptAllProcesses( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    JmsProcessInterruptMessage jmsMessage = new JmsProcessInterruptMessage();
    jmsMessage.setInterruptAllProcesses( Boolean.TRUE );
    getGJavaMessageService().sendToJmsTopic( jmsMessage );

    return display( mapping, actionForm, request, response );
  }

  private GJavaMessageService getGJavaMessageService()
  {
    return (GJavaMessageService)getService( GJavaMessageService.BEAN_NAME );
  }
}
