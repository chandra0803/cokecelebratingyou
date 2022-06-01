
package com.biperf.core.service.fileprocessing.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.fileprocessing.GlobalFileTemplateFactory;
import com.biperf.core.value.fileprocessing.FileInfo;

public class GlobalFileTemplateFactoryImpl implements GlobalFileTemplateFactory
{
  private Map<String, FileInfo> entries;

  public void setEntries( Map<String, FileInfo> entries )
  {
    this.entries = entries;
  }

  public FileInfo getFileDetailInfo( String type )
  {
    FileInfo fileInfo = entries.get( type );
    if ( null == fileInfo )
    {
      throw new BeaconRuntimeException( "File template not setup for '" + type + "'" );
    }
    return fileInfo;
  }
}
