package com.biperf.core.service.home;

import java.util.ArrayList;
import java.util.List;

public class RewardsMenu
{
  private String menuName;
  private List<MenuItems> menu = new ArrayList<>();

  public RewardsMenu( String menuName, List<MenuItems> menu )
  {
    super();
    this.menuName = menuName;
    this.menu = menu;
  }

  public RewardsMenu()
  {
    super();
  }

  public String getMenuName()
  {
    return menuName;
  }

  public void setMenuName( String menuName )
  {
    this.menuName = menuName;
  }

  public List<MenuItems> getMenu()
  {
    return menu;
  }

  public void setMenu( List<MenuItems> menu )
  {
    this.menu = menu;
  }

}
