
package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.SSIContestType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.ssi.SSIContestService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

public class SSIAtnContestUsingPrcImportStrategy extends ImportStrategy
{
  private SSIContestService ssiContestService;
  private static final Log logger = LogFactory.getLog( SSIAtnContestUsingPrcImportStrategy.class );

  @Override
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {

    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    Map<String, Object> result = ssiContestService.importImportFile( importFile.getId(), importFile.getContestId(), SSIContestType.AWARD_THEM_NOW );

    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
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
  public void verifyImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    validateImportFilePrc( importFile, records );
  }

  @SuppressWarnings( "rawtypes" )
  @Override
  public void verifyImportFile( ImportFile importFile, List records, boolean validateNodeRelationship ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isVerifyInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }
    validateImportFilePrc( importFile, records );
  }

  @SuppressWarnings( "rawtypes" )
  private int validateImportFilePrc( ImportFile importFile, List records )
  {
    Map<String, Object> result = ssiContestService.verifyImportFile( importFile.getId(), importFile.getContestId(), SSIContestType.AWARD_THEM_NOW );
    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
    importFile.setDateVerified( DateUtils.getCurrentDate() );
    importFile.setVerifiedBy( UserManager.getUserName() );
    importFile.setImportRecordErrorCount( errorCount );
    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 || resultCode == 1 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFIED ) );
    }
    {
      importFile.setHierarchy( null );
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_FAILED ) );
    }

    return errorCount;
  }

  public void setssiContestService( SSIContestService ssiContestService )
  {
    this.ssiContestService = ssiContestService;
  }
}
