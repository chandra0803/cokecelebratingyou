/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/fileload/ImportStrategyTest.java,v $
 */

package com.biperf.core.service.fileload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecord;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.fileload.ParticipantImportRecord;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.utils.MockContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

import junit.framework.TestCase;

/*
 * ImportStrategyTest <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Erik Tennant</td> <td>Sep
 * 9, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ImportStrategyTest extends TestCase
{

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed.
   * 
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();

    // setup of the mock pick list factory
    PickListItem.setPickListFactory( new MockPickListFactory() );
    // check if the ContentReader is already set - true if we are in container.
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }
  }

  public void testCreateAndAddImportRecordErrors()
  {

    ImportRecord record = new ParticipantImportRecord();
    assertTrue( record.getImportRecordErrors().isEmpty() );
    ServiceError error = new ServiceError( "key", "arg1", "arg2", "arg3" );
    ServiceError error2 = new ServiceError( "keya", "arg1a", "arg2a" );
    Collection errors = new ArrayList();
    errors.add( error );
    errors.add( error2 );

    ImportFile file = new ImportFile();
    assertTrue( file.getImportRecordErrors().isEmpty() );

    ImportStrategy.createAndAddImportRecordErrors( file, record, errors );

    Collection importRecorderrors = record.getImportRecordErrors();
    assertEquals( 2, importRecorderrors.size() );

    Iterator iterator = importRecorderrors.iterator();

    ImportRecordError recordError = (ImportRecordError)iterator.next();
    assertEquals( "key", recordError.getItemKey() );
    assertEquals( "arg1", recordError.getParam1() );
    assertEquals( "arg2", recordError.getParam2() );
    assertEquals( "arg3", recordError.getParam3() );

    recordError = (ImportRecordError)iterator.next();
    assertEquals( "keya", recordError.getItemKey() );
    assertEquals( "arg1a", recordError.getParam1() );
    assertEquals( "arg2a", recordError.getParam2() );

    // make sure the errors were also added to the file
    assertEquals( 2, file.getImportRecordErrors().size() );

  }

  public void testCheckBothRequiredOrBothEmpty()
  {

    ArrayList errors = new ArrayList();
    ImportStrategy.checkBothRequiredOrBothEmpty( null, "cmkey1", null, "cmkey2", errors );
    assertTrue( errors.isEmpty() );

    errors = new ArrayList();
    ImportStrategy.checkBothRequiredOrBothEmpty( "Hi", "cmkey1", null, "cmkey2", errors );
    assertEquals( 1, errors.size() );

    errors = new ArrayList();
    ImportStrategy.checkBothRequiredOrBothEmpty( null, "cmkey1", "Hi", "cmkey2", errors );
    assertEquals( 1, errors.size() );
    assertEquals( "???cmkey2???", ( (ServiceError)errors.get( 0 ) ).getArg2() );
    assertEquals( "???cmkey1???", ( (ServiceError)errors.get( 0 ) ).getArg1() );

    errors = new ArrayList();
    ImportStrategy.checkBothRequiredOrBothEmpty( "HELLO", "cmkey1", Boolean.TRUE, "cmkey2", errors );
    assertTrue( errors.isEmpty() );

  }

}
