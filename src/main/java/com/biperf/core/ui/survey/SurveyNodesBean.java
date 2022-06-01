
package com.biperf.core.ui.survey;

import java.io.Serializable;

public class SurveyNodesBean implements Serializable
{
  private Long id;
  private String name;
  private boolean isChosen;

  public SurveyNodesBean()
  {
  }

  public SurveyNodesBean( Long id, String name, boolean isChosen )
  {
    this.id = id;
    this.name = name;
    this.isChosen = isChosen;
  }

  public Long getId()
  {
    return id;
  }

  public String getName()
  {
    return name;
  }

  protected void setId( Long id )
  {
    this.id = id;
  }

  protected void setName( String name )
  {
    this.name = name;
  }

  public boolean isIsChosen()
  {
    return isChosen;
  }

  public void setIsChosen( boolean isChosen )
  {
    this.isChosen = isChosen;
  }
}
