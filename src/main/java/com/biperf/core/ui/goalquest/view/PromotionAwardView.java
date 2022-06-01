
package com.biperf.core.ui.goalquest.view;

import java.util.ArrayList;
import java.util.List;

public class PromotionAwardView
{
  private String id;
  private String name;
  private List<GoalAwardView> levels = new ArrayList<GoalAwardView>();

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

  public List<GoalAwardView> getLevels()
  {
    return levels;
  }

  public void setLevels( List<GoalAwardView> levels )
  {
    this.levels = levels;
  }
}
