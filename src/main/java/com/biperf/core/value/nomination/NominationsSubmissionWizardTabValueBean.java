
package com.biperf.core.value.nomination;

/**
 * Represents a tab (step) of the 'submit a nomination' wizard
 */
public class NominationsSubmissionWizardTabValueBean
{
  private int id;
  private String name;
  private boolean active;
  private String state;
  private String contentSel;
  private String wtvNumber;
  private String wtvName;

  public int getId()
  {
    return id;
  }

  public void setId( int id )
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

  public boolean isActive()
  {
    return active;
  }

  public void setActive( boolean active )
  {
    this.active = active;
  }

  public String getState()
  {
    return state;
  }

  public void setState( String state )
  {
    this.state = state;
  }

  public String getContentSel()
  {
    return contentSel;
  }

  public void setContentSel( String contentSel )
  {
    this.contentSel = contentSel;
  }

  public String getWtvNumber()
  {
    return wtvNumber;
  }

  public void setWtvNumber( String wtvNumber )
  {
    this.wtvNumber = wtvNumber;
  }

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
