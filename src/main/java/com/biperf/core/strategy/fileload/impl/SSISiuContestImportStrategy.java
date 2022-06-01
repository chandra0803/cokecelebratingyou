
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileImportLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class SSISiuContestImportStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String SSI_CONTEST_SIU_FILE_IMPORT = "pkg_ssi_step_it_up_load.p_ssi_stepit_stg_verify_import";

  @Override
  public Map stage( String fileName )
  {
    String[] fileNameAndID = fileName.split( "@" );
    CallPrcImportAdcFileImportLoad stagePrc = new CallPrcImportAdcFileImportLoad( getDataSource(), SSI_CONTEST_SIU_FILE_IMPORT );
    return stagePrc.executeProcedure( fileNameAndID[0], new Long( fileNameAndID[1] ) );
  }

}
