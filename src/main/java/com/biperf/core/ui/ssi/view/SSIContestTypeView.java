
package com.biperf.core.ui.ssi.view;

public class SSIContestTypeView
{
  private String contestType;
  private String contestTypeName;
  private String description;

  public SSIContestTypeView( String contestType, String contestTypeName, String description )
  {
    super();
    this.contestType = contestType;
    this.contestTypeName = contestTypeName;
    this.description = description;
  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  public String getContestTypeName()
  {
    return contestTypeName;
  }

  public void setContestTypeName( String contestTypeName )
  {
    this.contestTypeName = contestTypeName;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription( String description )
  {
    this.description = description;
  }

}
