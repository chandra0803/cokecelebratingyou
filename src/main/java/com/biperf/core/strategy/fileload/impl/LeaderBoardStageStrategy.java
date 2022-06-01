
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class LeaderBoardStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String LEADERBOARD_FILE_LOAD = "PRC_STAGE_LEADERBOARD_LOAD";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), LEADERBOARD_FILE_LOAD );
    return stagePrc.executeProcedure( fileName );
  }

}
