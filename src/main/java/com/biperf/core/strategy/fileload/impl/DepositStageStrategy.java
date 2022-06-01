
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class DepositStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String DEPOSIT_FILE_LOAD = "PRC_DEPOSIT_LOAD";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), DEPOSIT_FILE_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
