
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.strategy.fileload.FileStageStrategy;
import com.biperf.core.strategy.fileload.FileStageStrategyFactory;

public class FileStageStrategyFactoryImpl implements FileStageStrategyFactory
{
  private Map<String, FileStageStrategy> entries;

  public void setEntries( Map<String, FileStageStrategy> entries )
  {
    this.entries = entries;
  }

  public FileStageStrategy getStrategy( String type )
  {
    FileStageStrategy strategy = entries.get( type );
    if ( null == strategy )
    {
      throw new BeaconRuntimeException( "FileStageStrategy not setup for '" + type + "'" );
    }
    return strategy;
  }
}
