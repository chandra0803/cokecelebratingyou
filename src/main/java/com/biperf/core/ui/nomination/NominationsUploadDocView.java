
package com.biperf.core.ui.nomination;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NominationsUploadDocView
{

  private String type;
  private String name;
  private boolean isSuccess;
  private String text;
  private String nominationLink;
  private String fileName;

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

  @JsonProperty( "isSuccess" )
  public boolean isSuccess()
  {
    return isSuccess;
  }

  public void setSuccess( boolean isSuccess )
  {
    this.isSuccess = isSuccess;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public String getNominationLink()
  {
    return nominationLink;
  }

  public void setNominationLink( String nominationLink )
  {
    this.nominationLink = nominationLink;
  }

  public String getFileName()
  {
    return fileName;
  }

  public void setFileName( String fileName )
  {
    this.fileName = fileName;
  }

}
