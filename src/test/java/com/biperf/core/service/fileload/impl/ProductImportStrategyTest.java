
package com.biperf.core.service.fileload.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.easymock.EasyMock;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.ImportFileTypeType;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.fileload.ProductImportRecord;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.exception.UniqueConstraintViolationException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.product.ProductCategoryService;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.ProductService;

/**
 * ProductImportStrategyTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th></th>
 * </tr>
 * <tr>
 * <td>Travis Jenniges</td>
 * <td>Feb 1, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProductImportStrategyTest extends BaseServiceTest
{
  private ProductService productServiceMock;
  private ProductCategoryService productCategoryServiceMock;
  private ProductCharacteristicService productCharacteristicServiceMock;

  private ProductImportStrategy importStrategyUnderTest;

  public void setUp() throws Exception
  {
    super.setUp();
    importStrategyUnderTest = new ProductImportStrategy();

    productServiceMock = EasyMock.createMock( ProductService.class );
    importStrategyUnderTest.setProductService( productServiceMock );

    productCategoryServiceMock = EasyMock.createMock( ProductCategoryService.class );
    importStrategyUnderTest.setProductCategoryService( productCategoryServiceMock );

    productCharacteristicServiceMock = EasyMock.createMock( ProductCharacteristicService.class );
    importStrategyUnderTest.setProductCharacteristicService( productCharacteristicServiceMock );
  }

  public void testVerifyCleanImportFile()
  {
    ImportFile file = buildCleanProductImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );
    for ( Iterator iter = file.getProductImportRecords().iterator(); iter.hasNext(); )
    {
      ProductImportRecord record = (ProductImportRecord)iter.next();
      EasyMock.expect( productServiceMock.getProductByNameAndCategoryId( record.getProductName(), null ) ).andReturn( null );
      EasyMock.expect( productServiceMock.isUniqueProductName( record.getCategoryId(), record.getProductId(), record.getProductName() ) ).andReturn( true );
    }
    EasyMock.replay( productServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getProductImportRecords() ) );
    EasyMock.verify( productServiceMock );

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  public void testImportCleanImportFile()
  {
    ImportFile file = buildCleanProductImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.IMPORT_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    try
    {
      int i = 0;
      for ( Iterator iter = file.getProductImportRecords().iterator(); iter.hasNext(); i++ )
      {
        ProductImportRecord productImportRecord = (ProductImportRecord)iter.next();

        EasyMock.expect( productCharacteristicServiceMock.getCharacteristicById( productImportRecord.getCharacteristicId1() ) ).andReturn( new ProductCharacteristicType() );

        Product p = new Product();
        p.setName( productImportRecord.getProductName() );
        p.setDescription( productImportRecord.getProductDescription() );

        EasyMock.expect( productServiceMock.save( null, p ) ).andReturn( p );
        if ( i == 0 )
        {
          ProductCategory category = new ProductCategory();
          category.setName( "Name" );
          EasyMock.expect( productCategoryServiceMock.saveProductCategory( category, null ) ).andReturn( category );
        }
      }
      EasyMock.replay( productServiceMock );
      EasyMock.replay( productCategoryServiceMock );
      EasyMock.replay( productCharacteristicServiceMock );
      importStrategyUnderTest.importImportFile( file, new ArrayList( file.getProductImportRecords() ) );
      EasyMock.verify( productServiceMock );
      EasyMock.verify( productCategoryServiceMock );
    }
    catch( ServiceErrorException e )
    {
      fail( "" + e );
    }
    catch( UniqueConstraintViolationException e )
    {
      fail( "" + e );
    }

    assertTrue( file.getImportRecordErrors().isEmpty() );
  }

  public void testVerifyDirtyImportFile()
  {
    ImportFile file = buildBadProductImportFile();
    file.setStatus( (ImportFileStatusType)MockPickListFactory.getMockPickListItem( ImportFileStatusType.class, ImportFileStatusType.VERIFY_IN_PROCESS ) );
    assertTrue( file.getImportRecordErrors().isEmpty() );

    for ( Iterator iter = file.getProductImportRecords().iterator(); iter.hasNext(); )
    {
      ProductImportRecord record = (ProductImportRecord)iter.next();
      EasyMock.expect( productServiceMock.getProductByNameAndCategoryId( record.getProductName(), null ) ).andReturn( null );
      EasyMock.expect( productServiceMock.isUniqueProductName( record.getCategoryId(), record.getProductId(), record.getProductName() ) ).andReturn( true );
    }
    EasyMock.replay( productServiceMock );
    importStrategyUnderTest.verifyImportFile( file, new ArrayList( file.getProductImportRecords() ) );
    EasyMock.verify( productServiceMock );

    assertFalse( file.getImportRecordErrors().isEmpty() );
    assertEquals( 2, file.getImportRecordErrorCount() );
  }

  public static ImportFile buildCleanProductImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.PRODUCT ) );
    Set records = new HashSet( 2 );
    file.setProductImportRecords( records );

    ProductImportRecord record = new ProductImportRecord();
    record.setActionType( ProductImportRecord.ADD_ACTION_TYPE );
    record.setProductDescription( "Desc" );
    record.setProductName( "Test Product" );
    record.setCategoryDescription( "Desc" );
    record.setCategoryName( "Name" );
    record.setCharacteristicId1( new Long( 1 ) );
    record.setCharacteristicName1( "CharName" );
    records.add( record );

    ProductImportRecord record2 = new ProductImportRecord();
    record2.setActionType( ProductImportRecord.ADD_ACTION_TYPE );
    record2.setProductDescription( "Desc" );
    record2.setProductName( "Test Product" );
    record2.setCategoryDescription( "Desc" );
    record2.setCategoryName( "Name" );
    record.setCharacteristicId1( new Long( 1 ) );
    record.setCharacteristicName1( "CharName" );
    // record.setCharacteristicId2( new Long(2) );
    // record.setCharacteristicName2( "CharName2" );
    records.add( record2 );

    return file;
  }

  public static ImportFile buildBadProductImportFile()
  {
    ImportFile file = new ImportFile();
    file.setFileType( (ImportFileTypeType)MockPickListFactory.getMockPickListItem( ImportFileTypeType.class, ImportFileTypeType.PRODUCT ) );
    Set records = new HashSet( 2 );
    file.setProductImportRecords( records );

    ProductImportRecord record = new ProductImportRecord();
    record.setActionType( ProductImportRecord.ADD_ACTION_TYPE );
    record.setProductDescription( "" ); // description is required
    record.setProductName( "Test Product" );
    record.setCategoryDescription( "Desc" );
    record.setCategoryName( "Name" );
    records.add( record );

    ProductImportRecord record2 = new ProductImportRecord();
    record2.setActionType( ProductImportRecord.ADD_ACTION_TYPE );
    record2.setProductDescription( "Desc" );
    record2.setProductName( "Test Product" );
    record2.setCategoryDescription( "" ); // description is required
    record2.setCategoryName( "Name" );
    records.add( record2 );

    return file;
  }

  public static void addErrors( ImportFile file )
  {
    Set errors = new HashSet( 1 );
    errors.add( new ImportRecordError() );
    for ( Iterator iter = file.getProductImportRecords().iterator(); iter.hasNext(); )
    {
      ProductImportRecord record = (ProductImportRecord)iter.next();
      record.setImportRecordErrors( errors );
    }
  }
}