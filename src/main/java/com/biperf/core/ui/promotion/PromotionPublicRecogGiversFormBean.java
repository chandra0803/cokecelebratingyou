
package com.biperf.core.ui.promotion;

import java.io.Serializable;

public class PromotionPublicRecogGiversFormBean implements Serializable
{
  private String id;
  private String budget;
  private String name;

  public String getBudget()
  {
    return budget;
  }

  public void setBudget( String budget )
  {
    this.budget = budget;
  }

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
}
