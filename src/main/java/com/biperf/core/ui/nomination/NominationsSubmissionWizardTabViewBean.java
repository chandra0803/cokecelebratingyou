
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a tab (step) of the 'submit a nomination' wizard
 */
public class NominationsSubmissionWizardTabViewBean
{
  private int id;
  private String name;
  private boolean active;
  private String state;
  private String contentSel;
  private String wtvNumber;
  private String wtvName;

  public NominationsSubmissionWizardTabViewBean()
  {

  }

  public NominationsSubmissionWizardTabViewBean( int id, String name, boolean active, String state, String contentSel, String wtvNumber, String wtvName )
  {
    this.id = id;
    this.name = name;
    this.active = active;
    this.state = state;
    this.contentSel = contentSel;
    this.wtvNumber = wtvNumber;
    this.wtvName = wtvName;

  }

  @JsonProperty( "id" )
  public int getId()
  {
    return id;
  }

  public void setId( int id )
  {
    this.id = id;
  }

  @JsonProperty( "name" )
  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @JsonProperty( "isActive" )
  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  @JsonProperty( "state" )
  public String getState()
  {
    return state;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  @JsonProperty( "contentSel" )
  public String getContentSel()
  {
    return contentSel;
  }

  public void setContentSel( String contentSel )
  {
    this.contentSel = contentSel;
  }

  @JsonProperty( "wtvNumber" )
  public String getWtvNumber()
  {
    return wtvNumber;
  }

  public void setWtvNumber( String wtvNumber )
  {
    this.wtvNumber = wtvNumber;
  }

  @JsonProperty( "wtvName" )
  public String getWtvName()
  {
    return wtvName;
  }

  public void setWtvName( String wtvName )
  {
    this.wtvName = wtvName;
  }

  @Override
  public String toString()
  {
    return "NominationsSubmissionWizardTab [id=" + id + ", name=" + name + ", active=" + active + ", state=" + state + ", contentSel=" + contentSel + ", wtvNumber=" + wtvNumber + ", wtvName="
        + wtvName + "]";
  }

}
