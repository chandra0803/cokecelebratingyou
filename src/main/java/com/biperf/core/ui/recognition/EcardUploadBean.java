
package com.biperf.core.ui.recognition;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.WebErrorMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EcardUploadBean
{
  private final EcardUploadProperties properties;
  private final List<WebErrorMessage> messages = new ArrayList<WebErrorMessage>();

  public EcardUploadBean( WebErrorMessage message, boolean success, String ownCardName, String imageUrl, String videoUrl )
  {
    messages.add( message );
    properties = new EcardUploadProperties( success, ownCardName, imageUrl, videoUrl );
  }

  public EcardUploadBean( WebErrorMessage message )
  {
    messages.add( message );

    // assume it's a failure....
    properties = new EcardUploadProperties( false, null, null, null );
  }

  public List<WebErrorMessage> getMessages()
  {
    return messages;
  }

  public EcardUploadProperties getProperties()
  {
    return properties;
  }

  public boolean isSuccess()
  {
    if ( properties != null )
    {
      return properties.isSuccess();
    }
    return false;
  }

  public String getOwnCardName()
  {
    if ( properties != null )
    {
      return properties.getOwnCardName();
    }
    return null;
  }

  public String getImageUrl()
  {
    if ( properties != null )
    {
      return properties.getImageUrl();
    }
    return null;
  }

  public String getVideoUrl()
  {
    if ( properties != null )
    {
      return properties.getVideoUrl();
    }
    return null;
  }

  public static class EcardUploadProperties
  {
    private final boolean success;
    private final String ownCardName;
    private final String imageUrl;
    private final String videoUrl;

    private EcardUploadProperties( boolean success, String ownCardName, String imageUrl, String videoUrl )
    {
      this.success = success;
      this.ownCardName = ownCardName;
      this.imageUrl = imageUrl;
      this.videoUrl = videoUrl;
    }

    @JsonProperty( "isSuccess" )
    public boolean isSuccess()
    {
      return success;
    }

    public String getOwnCardName()
    {
      return ownCardName;
    }

    public String getImageUrl()
    {
      return imageUrl;
    }

    public String getVideoUrl()
    {
      return videoUrl;
    }
  }
}
