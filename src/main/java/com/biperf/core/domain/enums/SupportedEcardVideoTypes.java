
package com.biperf.core.domain.enums;

import org.apache.commons.io.FilenameUtils;

public enum SupportedEcardVideoTypes
{
  MOV( "mov", "video/mov" ), MP4( "mp4", "video/mp4" ), WEBM( "webm", "video/webm" );

  private final String informalName;
  private final String fileType;

  private SupportedEcardVideoTypes( String informalName, String fileType )
  {
    this.informalName = informalName;
    this.fileType = fileType;
  }

  public String getInformalName()
  {
    return informalName;
  }

  public String getFileType()
  {
    return fileType;
  }

  public static boolean isSupported( String fileExtension )
  {
    for ( SupportedEcardVideoTypes type : SupportedEcardVideoTypes.values() )
    {
      if ( type.getInformalName().equalsIgnoreCase( fileExtension ) )
      {
        return true;
      }
    }
    return false;
  }

  public static boolean isSupportedVideo( String fileName )
  {
    if ( fileName == null || fileName.trim().length() == 0 )
    {
      return false;
    }
    String extension = FilenameUtils.getExtension( fileName );
    return isSupported( extension );
  }

  public static String getVideoFileTypeFor( String fileName )
  {
    if ( fileName == null || fileName.trim().length() == 0 )
    {
      return null;
    }

    String fileExtension = FilenameUtils.getExtension( fileName );

    for ( SupportedEcardVideoTypes type : SupportedEcardVideoTypes.values() )
    {
      if ( type.getInformalName().equalsIgnoreCase( fileExtension ) )
      {
        return type.getFileType();
      }
    }

    return null;
  }

  /** Returns a string with a comma separated list of file types */
  public static String getSupportedVideoTypes()
  {
    StringBuilder sb = new StringBuilder();
    for ( int i = 0; i < values().length; ++i )
    {
      if ( i + 1 < values().length )
      {
        sb.append( ", " );
      }
      sb.append( values()[i].informalName );
    }
    return sb.toString();
  }
}
