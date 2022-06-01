
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class ChallengePointLevelStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String CP_LEVEL_DATA_LOAD = "PRC_CP_DATA_LOAD";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), CP_LEVEL_DATA_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
