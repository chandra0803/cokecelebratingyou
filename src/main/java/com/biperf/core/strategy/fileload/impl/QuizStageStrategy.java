
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class QuizStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String QUIZ_FILE_LOAD = "PRC_STAGE_QUIZ_LOAD";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), QUIZ_FILE_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
