
package com.biperf.core.ui.ots;

import com.biperf.core.ui.BaseActionForm;

public class AddOTSProgramForm extends BaseActionForm
{
  private static final long serialVersionUID = 1L;

  private String method;
  private String programNumber;
  private String clientName;
  private String description;

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getClientName()
  {
    return clientName;
  }

  public void setClientName( String clientName )
  {
    this.clientName = clientName;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

  public String getProgramNumber()
  {
    return programNumber;
  }

  public void setProgramNumber( String programNumber )
  {
    this.programNumber = programNumber;
  }

}
