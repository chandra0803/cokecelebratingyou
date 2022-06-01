
package com.biperf.core.value.ssi;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 
 * SSIContestFileUploadValue.
 * 
 * @author chowdhur
 * @since Nov 13, 2014
 */
public class SSIContestFileUploadValue implements Serializable
{
  public static final String TYPE_PDF = "pdf";
  public static final String TYPE_DOC = "doc";
  public static final String TYPE_DOCX = "docx";

  private Long id;
  private byte[] data;
  private String type;
  private String name;
  private String adcFileName;
  private String full;
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

  public String getAdcFileName()
  {
    return adcFileName;
  }

  public void setAdcFileName( String adcFileName )
  {
    this.adcFileName = adcFileName;
  }

}
