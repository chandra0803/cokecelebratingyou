/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/fileload/ImportFileAssociationRequest.java,v $
 */

package com.biperf.core.ui.fileload;

import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecord;
import com.biperf.core.service.BaseAssociationRequest;
import com.objectpartners.cms.util.ContentReaderManager;

/*
 * ImportFileAssociationRequest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 21, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportFileAssociationRequest extends BaseAssociationRequest
{
  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Hydrates the given {@link ImportFile} object when accessing its {@link ImportRecord} objects.
   * 
   * @param domainObject the {@link com.biperf.core.domain.fileload.ImportFile} object to hydrate.
   */
  public void execute( Object domainObject )
  {
    ImportFile importFile = (ImportFile)domainObject;

    if ( importFile.getIsBudgetImportFile() )
    {
      hydrateBudgetImportFile( importFile );
    }
    else if ( importFile.getIsDepositImportFile() )
    {
      hydrateDepositImportFile( importFile );
    }
    else if ( importFile.getIsHierarchyImportFile() )
    {
      hydrateHierarchyImportFile( importFile );
    }
    else if ( importFile.getIsBudgetDistributionImportFile() )
    {
      hydrateBudgetDistributionImportFile( importFile );
    }
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Hydrates the given budget import file.
   * 
   * @param importFile the import file to hydrate.
   */
  private void hydrateBudgetImportFile( ImportFile importFile )
  {
    initialize( importFile.getPromotion() );
    initialize( importFile.getBudgetImportRecords() );
  }

  /**
   * Hydrates the given deposit import file.
   * 
   * @param importFile the import file to hydrate.
   */
  private void hydrateDepositImportFile( ImportFile importFile )
  {
    initialize( importFile.getPromotion() );
    initialize( importFile.getDepositImportRecords() );
  }

  /**
   * Hydrates the given leaderboard import file.
   * 
   * @param importFile the import file to hydrate.
   */
  private void hydrateLeaderBoardImportFile( ImportFile importFile )
  {
    initialize( importFile.getLeaderBoardImportRecords() );
  }

  /**
   * Hydrates the given hierarchy import file.
   * 
   * @param importFile the import file to hydrate.
   */
  private void hydrateHierarchyImportFile( ImportFile importFile )
  {
    initialize( importFile.getHierarchy() );
    if ( importFile.getHierarchy() != null )
    {
      String cmAssetCode = importFile.getHierarchy().getCmAssetCode();
      String cmItemKey = importFile.getHierarchy().getNameCmKey();
      String name = ContentReaderManager.getText( cmAssetCode, cmItemKey );
      importFile.getHierarchy().setName( name );
    }

    /*
     * initialize( importFile.getHierarchyImportRecords() ); // Hydrate each hierarchy import
     * record's import record errors. Iterator iter =
     * importFile.getHierarchyImportRecords().iterator(); while (iter.hasNext()) {
     * HierarchyImportRecord importRecord = (HierarchyImportRecord) iter.next(); initialize(
     * importRecord.getImportRecordErrors() ); }
     */
  }

  /**
   * Hydrates the given participant import file.
   * 
   * @param importFile the import file to hydrate.
   */
  /*
   * private void hydrateParticipantImportFile( ImportFile importFile ) { // Hydrate the import
   * file's participant import records. initialize( importFile.getParticipantImportRecords() ); //
   * Hydrate each participant import record's import record errors. Iterator iter =
   * importFile.getParticipantImportRecords().iterator(); while (iter.hasNext()) {
   * ParticipantImportRecord importRecord = (ParticipantImportRecord) iter.next(); initialize(
   * importRecord.getImportRecordErrors() ); } }
   */

  /**
   * Hydrates the given budget distribution import file.
   * 
   * @param importFile the import file to hydrate.
   */
  private void hydrateBudgetDistributionImportFile( ImportFile importFile )
  {
    initialize( importFile.getBudgetDistributionImportRecords() );
  }
}
