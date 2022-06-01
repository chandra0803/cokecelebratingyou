
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class SSISrContestStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String SSI_CONTEST_SR_FILE_STAGE = "pkg_ssi_stack_rank_load.p_stage_ssi_stack_rank";

  @Override
  public Map stage( String fileName )
  {

    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), SSI_CONTEST_SR_FILE_STAGE );
    return stagePrc.executeProcedure( fileName );
  }

}
