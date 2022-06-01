
package com.biperf.core.strategy.fileload;

public interface FileStageStrategyFactory
{
  public String BEAN_NAME = "fileStageStrategyFactory";

  public FileStageStrategy getStrategy( String type );
}
