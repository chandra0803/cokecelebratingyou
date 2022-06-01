
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class ParticipantStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String PARTICIPANT_FILE_LOAD = "PKG_BULK_PAX_STAGE.PRC_STAGE_PAX_FILE";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), PARTICIPANT_FILE_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
