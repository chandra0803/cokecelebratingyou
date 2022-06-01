
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class NominationApproverStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String NOM_APPROVER_FILE_LOAD = "prc_stage_nom_approver_load";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), NOM_APPROVER_FILE_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
