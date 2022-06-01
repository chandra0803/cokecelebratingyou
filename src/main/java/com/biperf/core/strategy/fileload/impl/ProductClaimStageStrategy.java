
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class ProductClaimStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String PRODUCT_CLAIM_FILE_LOAD = "PRC_STAGE_PRD_CLAIM";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), PRODUCT_CLAIM_FILE_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
