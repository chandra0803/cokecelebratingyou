/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/fileload/hibernate/ImportFileDAOImplTest.java,v $
 */

package com.biperf.core.dao.fileload.hibernate;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.dao.fileload.ImportFileDAO;
import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ProductClaimImportFieldRecord;
import com.biperf.core.domain.fileload.ProductClaimImportProductRecord;
import com.biperf.core.domain.fileload.ProductClaimImportRecord;
import com.biperf.core.domain.fileload.ProductImportRecord;
import com.biperf.core.utils.ApplicationContextFactory;

/*
 * ImportFileDAOImplTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Sep
 * 8, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 *
 *
 */

public class ImportFileDAOImplTest extends BaseDAOTest
{
  // ---------------------------------------------------------------------------
  // Build Methods
  // ---------------------------------------------------------------------------

  public static ImportFile buildProductClaimImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( ImportFileTypeType.lookup( ImportFileTypeType.PRODUCT_CLAIM ) );
    file.setStatus( ImportFileStatusType.lookup( ImportFileStatusType.STAGED ) );
    file.setStagedBy( "Z" );
    file.setDateStaged( new Date() );
    Set records = new LinkedHashSet();
    file.setProductClaimImportRecords( records );
    file.setFileName( buildUniqueString() );
    file.setImportRecordCount( 1 );

    file.setImportRecordErrorCount( 0 );

    ProductClaimImportRecord record1 = new ProductClaimImportRecord();
    record1.setActionType( ProductImportRecord.ADD_ACTION_TYPE );

    // claim elements
    LinkedHashSet productClaimImportFieldRecords = new LinkedHashSet();
    record1.setProductClaimImportFieldRecords( productClaimImportFieldRecords );
    record1.setSubmitterUserName( "user" );
    record1.setTrackToNodeName( "track-to-node" );

    ProductClaimImportFieldRecord claimImportFieldRecord1 = new ProductClaimImportFieldRecord();
    claimImportFieldRecord1.setClaimFormStepElementId( new Long( 1 ) );
    claimImportFieldRecord1.setClaimFormStepElementName( "element name 1" );
    claimImportFieldRecord1.setClaimFormStepElementValue( "element value 1" );
    claimImportFieldRecord1.setProductClaimImportRecord( record1 );

    productClaimImportFieldRecords.add( claimImportFieldRecord1 );

    // claim products
    LinkedHashSet productClaimImportProductRecords = new LinkedHashSet();
    record1.setProductClaimImportProductRecords( productClaimImportProductRecords );

    ProductClaimImportProductRecord claimImportProductRecord = new ProductClaimImportProductRecord();
    claimImportProductRecord.setProductId( new Long( 1 ) );
    claimImportProductRecord.setProductName( "product name 1" );
    claimImportProductRecord.setSoldQuantity( new Integer( 2 ) );
    claimImportProductRecord.setProductCharacteristicId1( new Long( 1 ) );
    claimImportProductRecord.setProductCharacteristicName1( "prod char name 1" );
    claimImportProductRecord.setProductCharacteristicValue1( "prod char value1" );
    claimImportProductRecord.setProductClaimImportRecord( record1 );
    productClaimImportProductRecords.add( claimImportProductRecord );

    records.add( record1 );

    return file;
  }

  // ---------------------------------------------------------------------------
  // Test Methods
  // ---------------------------------------------------------------------------

  /**
   * Tests inserting and selecting a participant import file.
   */
  public void testSaveAndGetProductClaimImportFile()
  {
    ImportFileDAO importFileDAO = getImportFileDAO();

    // Build the import file.
    ImportFile unsavedImportFile = buildProductClaimImportFile();

    // Save the import file.
    ImportFile savedImportFile = importFileDAO.saveImportFile( unsavedImportFile );
    flushAndClearSession();

    // Retrieve the import file.
    ImportFile retrievedImportFile1 = importFileDAO.getImportFile( savedImportFile.getId() );
    assertNotNull( retrievedImportFile1.getId() );
    assertEquals( savedImportFile, retrievedImportFile1 );

    assertEquals( savedImportFile.getImportRecords().size(), retrievedImportFile1.getImportRecords().size() );
    assertEquals( retrievedImportFile1.getImportRecords().size(), retrievedImportFile1.getImportRecordCount() );

    assertEquals( savedImportFile.getImportRecordErrors().size(), retrievedImportFile1.getImportRecordErrors().size() );
    assertEquals( retrievedImportFile1.getImportRecordErrors().size(), retrievedImportFile1.getImportRecordErrorCount() );

    // Retrieve the import file with associations.
    ImportFile retrievedImportFile2 = importFileDAO.getImportFile( savedImportFile.getId(), null );
    assertNotNull( retrievedImportFile2.getId() );
    assertEquals( savedImportFile, retrievedImportFile2 );

    assertEquals( savedImportFile.getImportRecords().size(), retrievedImportFile2.getImportRecords().size() );
    assertEquals( retrievedImportFile2.getImportRecords().size(), retrievedImportFile2.getImportRecordCount() );

    assertEquals( savedImportFile.getImportRecordErrors().size(), retrievedImportFile2.getImportRecordErrors().size() );
    assertEquals( retrievedImportFile2.getImportRecordErrors().size(), retrievedImportFile2.getImportRecordErrorCount() );
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Returns a reference to the import file DAO.
   * 
   * @return a reference to the import file DAO.
   */
  private static ImportFileDAO getImportFileDAO()
  {
    return (ImportFileDAO)ApplicationContextFactory.getApplicationContext().getBean( ImportFileDAO.BEAN_NAME );
  }

}
