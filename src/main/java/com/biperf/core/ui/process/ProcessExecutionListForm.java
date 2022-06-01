
package com.biperf.core.ui.process;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseForm;

@SuppressWarnings( "serial" )
public class ProcessExecutionListForm extends BaseForm
{
  public static final String NAME = "processExecutionListForm";

  private String method;
  private String[] interruptProcessIds = null;

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    interruptProcessIds = request.getParameterValues( "interruptProcessIds" );
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String[] getInterruptProcessIds()
  {
    return interruptProcessIds;
  }

  public void setInterruptProcessIds( String[] interruptProcessIds )
  {
    this.interruptProcessIds = interruptProcessIds;
  }

}
