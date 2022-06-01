
package com.biperf.core.strategy.fileload;

import java.util.Map;

public interface FileStageStrategy
{
  @SuppressWarnings( "rawtypes" )
  public Map stage( String fileName );
}
