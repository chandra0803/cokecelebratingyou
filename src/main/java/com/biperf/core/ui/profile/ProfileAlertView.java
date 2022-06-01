
package com.biperf.core.ui.profile;

/**
 * 
 * @author dudam
 * @since Dec 20, 2012
 * @version 1.0
 */
public class ProfileAlertView
{

  private String type;
  private String name;
  private Alerts data;

  public ProfileAlertView()
  {

  }

  public ProfileAlertView( String name, String type, Alerts data )
  {
    this.name = name;
    this.type = type;
    this.data = data;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public Alerts getData()
  {
    return data;
  }

  public void setData( Alerts data )
  {
    this.data = data;
  }

}
