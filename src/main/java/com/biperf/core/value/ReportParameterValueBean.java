
package com.biperf.core.value;

import java.io.Serializable;

public class ReportParameterValueBean implements Serializable
{
  private String label;
  private String value;
  private boolean displayOnDashboard;

  public ReportParameterValueBean()
  {
    super();
  }

  public ReportParameterValueBean( String label, String value, boolean displayOnDashboard )
  {
    super();
    this.label = label;
    this.value = value;
    this.displayOnDashboard = displayOnDashboard;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public boolean isDisplayOnDashboard()
  {
    return displayOnDashboard;
  }

  public void setDisplayOnDashboard( boolean displayOnDashboard )
  {
    this.displayOnDashboard = displayOnDashboard;
  }

}
