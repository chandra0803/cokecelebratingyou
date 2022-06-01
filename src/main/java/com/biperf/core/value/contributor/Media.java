
package com.biperf.core.value.contributor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@SuppressWarnings( "serial" )
@JsonInclude( value = Include.NON_NULL )
public class Media implements Serializable
{

  String type;
  String url;
  String thumbnailUrl;

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getThumbnailUrl()
  {
    return thumbnailUrl;
  }

  public void setThumbnailUrl( String thumbnailUrl )
  {
    this.thumbnailUrl = thumbnailUrl;
  }

}
