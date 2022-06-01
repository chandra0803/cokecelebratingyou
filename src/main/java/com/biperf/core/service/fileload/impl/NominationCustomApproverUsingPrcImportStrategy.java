
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.nomination.NominationService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

public class NominationCustomApproverUsingPrcImportStrategy extends ImportStrategy
{

  private NominationService nominationService;

  private static final Log logger = LogFactory.getLog( NominationCustomApproverUsingPrcImportStrategy.class );

  @SuppressWarnings( "rawtypes" )
  @Override
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    Map<String, Object> result = nominationService.importImportFile( importFile.getId(), UserManager.getUserId(), importFile.getPromotion().getId() );

    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
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
      logger.debug( "Import file failed   for import Id : " + importFile.getId() );
    }
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public void verifyImportFile( ImportFile importFile, List records )
  {
    validateImportFilePrc( importFile, records );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public void verifyImportFile( ImportFile importFile, List records, boolean justForPaxRightNow ) throws ServiceErrorException
  {

    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    validateImportFilePrc( importFile, records );
  }

  @SuppressWarnings( "rawtypes" )
  public int validateImportFilePrc( ImportFile importFile, List records )
  {

    Map<String, Object> result = nominationService.verifyImportFile( importFile.getId(), UserManager.getUserId(), importFile.getPromotion().getId() );
    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
    importFile.setDateVerified( DateUtils.getCurrentDate() );
    importFile.setVerifiedBy( UserManager.getUserName() );
    importFile.setImportRecordErrorCount( errorCount );

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

    return errorCount;
  }

  public void setNominationService( NominationService nominationService )
  {
    this.nominationService = nominationService;
  }

}
