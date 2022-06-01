
package com.biperf.core.value.contributor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_NULL )
public class ContributionsList implements Serializable
{

  private Long id;
  private Contributor contributor;
  private String message;
  private Media media;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public Contributor getContributor()
  {
    return contributor;
  }

  public void setContributor( Contributor contributor )
  {
    this.contributor = contributor;
  }

  public String getMessage()
  {
    return message;
  }

  public void setMessage( String message )
  {
    this.message = message != null ? message : "";
  }

  public Media getMedia()
  {
    return media;
  }

  public void setMedia( Media media )
  {
    this.media = media;
  }

}
