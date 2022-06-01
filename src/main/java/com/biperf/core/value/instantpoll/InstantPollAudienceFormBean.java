/**
 * 
 */

package com.biperf.core.value.instantpoll;

/**
 * @author poddutur
 *
 */
public class InstantPollAudienceFormBean
{
  private Long id;
  private String name;
  private Long audienceId;
  private String audienceType;
  private String teamPositionCode;
  private Long version;
  private int size;
  private boolean required;
  private boolean removed;
  private boolean selected;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
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

  public Long getAudienceId()
  {
    return audienceId;
  }

  public void setAudienceId( Long audienceId )
  {
    this.audienceId = audienceId;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public String getTeamPositionCode()
  {
    return teamPositionCode;
  }

  public void setTeamPositionCode( String teamPositionCode )
  {
    this.teamPositionCode = teamPositionCode;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public int getSize()
  {
    return size;
  }

  public void setSize( int size )
  {
    this.size = size;
  }

  public boolean isRequired()
  {
    return required;
  }

  public void setRequired( boolean required )
  {
    this.required = required;
  }

  public boolean isRemoved()
  {
    return removed;
  }

  public void setRemoved( boolean removed )
  {
    this.removed = removed;
  }

  public boolean isSelected()
  {
    return selected;
  }

  public void setSelected( boolean selected )
  {
    this.selected = selected;
  }

}
