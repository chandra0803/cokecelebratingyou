
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class SSISiuContestStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{

  private static final String SSI_CONTEST_SIU_FILE_STAGE = "pkg_ssi_step_it_up_load.p_stage_ssi_step_it_up";

  @Override
  public Map stage( String fileName )
  {

    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), SSI_CONTEST_SIU_FILE_STAGE );
    return stagePrc.executeProcedure( fileName );
  }

}
