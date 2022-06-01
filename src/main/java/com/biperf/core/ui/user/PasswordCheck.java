
package com.biperf.core.ui.user;

public class PasswordCheck
{
  private String label;
  private boolean available;

  public PasswordCheck( boolean available, String label )
  {
    this.available = available;
    this.label = label;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public boolean isAvailable()
  {
    return available;
  }

  public void setAvailable( boolean available )
  {
    this.available = available;
  }

}