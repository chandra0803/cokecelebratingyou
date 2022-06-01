
package com.biperf.core.value.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a color choice for the draw tool for drawing your own ecard during a nomination submission
 */
public class NominationSubmitDataColorSettingValueBean
{
  private String hex;
  private String title;

  /**
   * Default constructor leaves fields uninitialized
   */
  public NominationSubmitDataColorSettingValueBean()
  {
  }

  public NominationSubmitDataColorSettingValueBean( String hex, String title )
  {
    this.hex = hex;
    this.title = title;
  }

  @JsonProperty( "hex" )
  public String getHex()
  {
    return hex;
  }

  public void setHex( String hex )
  {
    this.hex = hex;
  }

  @JsonProperty( "title" )
  public String getTitle()
  {
    return title;
  }

  public void setTitle( String title )
  {
    this.title = title;
  }

}
