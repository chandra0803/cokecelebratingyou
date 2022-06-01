
package com.biperf.core.value;

import java.io.Serializable;

@SuppressWarnings( "serial" )
public class QuartzProcessBean implements Serializable
{
  private Long processId;
  private String processName;

  public Long getProcessId()
  {
    return processId;
  }

  public void setProcessId( Long processId )
  {
    this.processId = processId;
  }

  public String getProcessName()
  {
    return processName;
  }

  public void setProcessName( String processName )
  {
    this.processName = processName;
  }

}
