/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/fileload/impl/HierarchyUsingProcImportStrategy.java,v $
 */

package com.biperf.core.service.fileload.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.fileload.ImportStrategy;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;

/*
 * HierarchyUsingProcImportStrategy <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Avinash Gadapa</td> <td>Oct
 * 10, 2007</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 */

public class HierarchyUsingProcImportStrategy extends ImportStrategy
{

  // hierarchy
  private HierarchyService hierarchyService;

  /**
   * Imports the specified import file.
   * 
   * @param importFile the import file to import.
   * @param records the recordst to import.
   * @throws ServiceErrorException
   */
  public void importImportFile( ImportFile importFile, List records ) throws ServiceErrorException
  {
    if ( !importFile.getStatus().isImportInProcess() )
    {
      throw new ServiceErrorException( ServiceErrorMessageKeys.SYSTEM_ERRORS_INVALID_CALL );
    }

    Map result = getHierarchyService().importImportFile( importFile.getId(), importFile.getHierarchy().getId(), UserManager.getUserId() );

    int errorCount = ( (BigDecimal)result.get( "p_total_error_rec" ) ).intValue();
    importFile.setDateImported( DateUtils.getCurrentDate() );
    importFile.setImportedBy( UserManager.getUserName() );
    importFile.setImportRecordErrorCount( errorCount );

    int resultCode = ( (BigDecimal)result.get( "p_out_returncode" ) ).intValue();
    if ( resultCode == 0 )
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORTED ) );
    }
    else
    {
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.IMPORT_FAILED ) );
    }
  }

  /**
   * Verifies the specified import file.
   * 
   * @param records the records to import.
   * @param importFile the import file to import.
   */
  public void verifyImportFile( ImportFile importFile, List records )
  {
    validateImportFilePrc( importFile, records );
  }

  /**
   * Verifies the specified import file.
   * 
   * @param records the records to import.
   * @param importFile the import file to import.
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

    validateImportFilePrc( importFile, records );
  }

  public int validateImportFilePrc( ImportFile importFile, List records )
  {
    Map result = getHierarchyService().verifyImportFile( importFile.getId(), importFile.getHierarchy().getId(), UserManager.getUserId() );

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
      importFile.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.VERIFY_FAILED ) );
    }

    return errorCount;
  }

  public HierarchyService getHierarchyService()
  {
    return hierarchyService;
  }

  public void setHierarchyService( HierarchyService hierarchyService )
  {
    this.hierarchyService = hierarchyService;
  }
}
