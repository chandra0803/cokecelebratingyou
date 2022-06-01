
package com.biperf.core.value;

import java.io.InputStream;
import java.io.Serializable;

public class CelebrationAvatarUploadValue implements Serializable
{
  public static final String STATUS_SUCCESS = "success";
  public static final String STATUS_FAIL = "fail";

  public static final String TYPE_AVATAR = "avatar";

  public static final int THUMB_DIMENSION = 48;
  public static final int DETAIL_DIMENSION = 432;

  private Long id;
  private byte[] data;
  private String type;
  private String name;
  private String full;
  private String thumb;
  private String status;
  private InputStream inputStream;
  private int size;
  private String fileFullPath;

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public byte[] getData()
  {
    return data;
  }

  public void setData( byte[] data )
  {
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

  public String getFull()
  {
    return full;
  }

  public void setFull( String full )
  {
    this.full = full;
  }

  public String getThumb()
  {
    return thumb;
  }

  public void setThumb( String thumb )
  {
    this.thumb = thumb;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public InputStream getInputStream()
  {
    return inputStream;
  }

  public void setInputStream( InputStream inputStream )
  {
    this.inputStream = inputStream;
  }

  public int getSize()
  {
    return size;
  }

  public void setSize( int size )
  {
    this.size = size;
  }

  public String getFileFullPath()
  {
    return fileFullPath;
  }

  public void setFileFullPath( String fileFullPath )
  {
    this.fileFullPath = fileFullPath;
  }

}
