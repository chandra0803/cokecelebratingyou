
package com.biperf.core.strategy.fileload.impl;

import java.util.Map;

import com.biperf.core.dao.fileload.hibernate.CallPrcImportAdcFileStageLoad;
import com.biperf.core.strategy.fileload.FileStageStrategy;

public class BudgetDistributionStageStrategy extends BaseFileloadStrategy implements FileStageStrategy
{
  private static final String BUDGET_DISTRIBUTION_FILE_LOAD = "prc_stage_inactive_budgets_rd";

  @SuppressWarnings( "rawtypes" )
  @Override
  public Map stage( String fileName )
  {
    CallPrcImportAdcFileStageLoad stagePrc = new CallPrcImportAdcFileStageLoad( getDataSource(), BUDGET_DISTRIBUTION_FILE_LOAD );
    return stagePrc.executeProcedure( fileName );
  }
}
