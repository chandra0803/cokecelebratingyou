
package com.biperf.core.ui.process;

import com.biperf.core.ui.BaseForm;

public class ProcessListForm extends BaseForm
{

  public static final String FORM_NAME = "processListForm";

  private String method;
  private String processType = "general"; // default to general

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getProcessType()
  {
    return processType;
  }

  public void setProcessType( String processType )
  {
    this.processType = processType;
  }

}
