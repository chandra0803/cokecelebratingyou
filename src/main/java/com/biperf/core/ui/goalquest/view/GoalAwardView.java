
package com.biperf.core.ui.goalquest.view;

import java.util.ArrayList;
import java.util.List;

public class GoalAwardView
{
  private String id = null;
  private String name = null;
  private String desc = null;
  private List<AwardProductView> products = new ArrayList<AwardProductView>();

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getDesc()
  {
    return desc;
  }

  public void setDesc( String desc )
  {
    this.desc = desc;
  }

  public List<AwardProductView> getProducts()
  {
    return products;
  }

  public void setProducts( List<AwardProductView> products )
  {
    this.products = products;
  }

}
