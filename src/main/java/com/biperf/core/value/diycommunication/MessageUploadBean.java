
package com.biperf.core.value.diycommunication;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageUploadBean
{
  private String type;
  private String name;
  @JsonProperty( "isSuccess" )
  private boolean isSuccess;
  private String text;
  private String docURL;

  private List<ImagesUploadBean> images = new ArrayList<ImagesUploadBean>();

  public MessageUploadBean()
  {
  }

  public MessageUploadBean( MessageUploadBean uploadBean )
  {
    this.setType( uploadBean.getType() );
    this.setName( uploadBean.getName() );
    this.setSuccess( uploadBean.isSuccess() );
    this.setText( uploadBean.getText() );
    this.setDocURL( uploadBean.getDocURL() );
    this.setImages( uploadBean.getImages() );
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

  public String getDocURL()
  {
    return docURL;
  }

  public void setDocURL( String docURL )
  {
    this.docURL = docURL;
  }

  @JsonProperty( "images" )
  public List<ImagesUploadBean> getImages()
  {
    return images;
  }

  public void setImages( List<ImagesUploadBean> images )
  {
    this.images = images;
  }

  public static class ImagesUploadBean
  {
    @JsonProperty( "imageUrl" )
    private String imageUrl;
    @JsonProperty( "size" )
    private String imageSize;

    public ImagesUploadBean()
    {

    }

    public ImagesUploadBean( ImagesUploadBean imagesUploadBean )
    {
      this.setImageSize( imagesUploadBean.getImageSize() );
      this.setImageUrl( imagesUploadBean.getImageUrl() );
    }

    public String getImageUrl()
    {
      return imageUrl;
    }

    public void setImageUrl( String imageUrl )
    {
      this.imageUrl = imageUrl;
    }

    public String getImageSize()
    {
      return imageSize;
    }

    public void setImageSize( String imageSize )
    {
      this.imageSize = imageSize;
    }

  }

}
