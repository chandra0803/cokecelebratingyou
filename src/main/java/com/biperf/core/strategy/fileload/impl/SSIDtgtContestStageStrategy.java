
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class SSIDtgtContestStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{

  private static final String SSI_CONTEST_DTGT_FILE_STAGE = "pkg_ssi_dtgt_load.p_stage_ssi_dtgt";

  @Override
  public Map stage( String fileName )
  {

    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), SSI_CONTEST_DTGT_FILE_STAGE );
    return stagePrc.executeProcedure( fileName );
  }

}
