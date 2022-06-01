
package com.biperf.core.service.home;

public class MainMenu
{
  private RewardsMenu data;

  public MainMenu()
  {
    super();
  }

  public MainMenu( RewardsMenu data )
  {
    super();
    this.data = data;
  }

  public RewardsMenu getData()
  {
    return data;
  }

  public void setData( RewardsMenu data )
  {
    this.data = data;
  }
  
}
