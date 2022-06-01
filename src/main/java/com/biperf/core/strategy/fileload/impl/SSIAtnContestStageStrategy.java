
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class SSIAtnContestStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String SSI_CONTEST_ATN_FILE_STAGE = "pkg_ssi_atn_load.p_stage_ssi_atn";

  @Override
  public Map stage( String fileName )
  {

    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), SSI_CONTEST_ATN_FILE_STAGE );
    return stagePrc.executeProcedure( fileName );
  }

}
