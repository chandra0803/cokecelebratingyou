
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.budget.BudgetMasterService;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

public class BudgetDistributionImportStrategy extends ImportStrategy
{
  private static final Log logger = LogFactory.getLog( BudgetDistributionImportStrategy.class );

  private BudgetMasterService budgetService;

  /**
   * Verifies the specified import file.
   * 
   * @param records the import records to import.
   * @param importFile the import file to import.
   */
  public void verifyImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( logger.isDebugEnabled() )
    {
      logger.debug( "In verifyImportFile" );
    }
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    validateBudgetDistributionImportRecord( importFile, records );

  }

  /**
   * Verifies the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the records to import.
   * @param justForPaxRightNow
   * @throws ServiceErrorException
   */
  public void verifyImportFile( ImportFile importFile, List records, boolean justForPaxRightNow ) throws ServiceErrorException
  {
    // First, make sure we are in the correct state
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    verifyImportFile( importFile, records );
  }

  /**
   * Validates the given import record.
   * 
   * @param budgetRecord the import record to validate.
   * @param basePromotion the promotion to validate against.
   * @return the number of import records with errors in the import file.
   */
  protected void validateBudgetDistributionImportRecord( ImportFile importFile, List records )
  {

    Map<String, Object> result = getBudgetService().verifyBudgetDistributionImportFile( importFile.getId(), importFile.getBudgetMasterId(), importFile.getBudgetSegmentId() );

    int errorCount = ( (BigDecimal)result.get( "p_file_records_count" ) ).intValue();
    importFile.setImportRecordErrorCount( errorCount );
    importFile.setDateVerified( DateUtils.getCurrentDate() );
    importFile.setVerifiedBy( UserManager.getUserName() );

    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 || resultCode == 1 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );
    }
    else
    {
      importFile.setHierarchy( null );
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_FAILED ) );
    }
  }

  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {

    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    Map<String, Object> result = getBudgetService().importBudgetDistributionImportFile( importFile.getId(), UserManager.getUserId(), importFile.getBudgetMasterId(), importFile.getBudgetSegmentId() );

    int errorCount = ( (BigDecimal)result.get( "p_file_records_count" ) ).intValue();
    importFile.setDateImported( DateUtils.getCurrentDate() );
    importFile.setImportedBy( UserManager.getUserName() );
    importFile.setImportRecordErrorCount( errorCount );

    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 || resultCode == 1 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    }
    else
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_FAILED ) );
    }
  }

  public BudgetMasterService getBudgetService()
  {
    return budgetService;
  }

  public void setBudgetService( BudgetMasterService budgetService )
  {
    this.budgetService = budgetService;
  }
}
