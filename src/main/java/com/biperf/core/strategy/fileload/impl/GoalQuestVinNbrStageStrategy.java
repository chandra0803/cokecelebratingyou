
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class GoalQuestVinNbrStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String GQ_VIN_NUMBER_LOAD = "PRC_GQ_VIN_NBR_IMPORT";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), GQ_VIN_NUMBER_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
