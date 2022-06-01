
package com.biperf.core.ui.celebration;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CelebrationImageFillerTileView
{
  private List<ImageView> images = new ArrayList<ImageView>();

  @JsonProperty( "placeholderImgs" )
  public List<ImageView> getImages()
  {
    return images;
  }

  public void setImages( List<ImageView> images )
  {
    this.images = images;
  }

  public static class ImageView
  {
    private String text;
    private String url;

    public ImageView()
    {
    }

    public ImageView( String text, String url )
    {
      this.text = text;
      this.url = url;
    }

    public String getText()
    {
      return text;
    }

    public void setText( String text )
    {
      this.text = text;
    }

    public String getUrl()
    {
      return url;
    }

    public void setUrl( String url )
    {
      this.url = url;
    }

  }
}
