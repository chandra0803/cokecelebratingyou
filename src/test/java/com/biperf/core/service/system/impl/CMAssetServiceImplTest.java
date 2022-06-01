/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/system/impl/CMAssetServiceImplTest.java,v $
 *
 */

package com.biperf.core.service.system.impl;

import org.junit.Ignore;
import org.junit.Test;

import com.biperf.core.dao.oracle.impl.OracleSequenceDAOImpl;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.impl.CMAssetServiceImpl;
import com.biperf.integration.BaseIntegrationTest;

/**
 * CMAssetServiceImplTest. This service test extends the BaseIntegrationTest. Yes the BaseIntegrationTest. The
 * reason is that we want to test modifying cm data and not just mock out these tests. 
 */
public class CMAssetServiceImplTest extends BaseIntegrationTest
{
  // That's right - we are extending the BaseIntegrationTest. The reason why is commented above.

  /**
   * Test the adding of a PickListItem.
   */
  @Test
  @Ignore
  public void testAddPickListItem() throws ServiceErrorException
  {
    // CMAssetServiceImpl cmAssetService = new CMAssetServiceImpl();
    // OracleSequenceDAOImpl oracleSequenceDAO = new OracleSequenceDAOImpl();
    // cmAssetService.setOracleSequenceDAO( oracleSequenceDAO );
    //
    // List originalList = AddressType.getList();
    // cmAssetService.addPickListItem( "picklist.addresstype.items", "myNewItem" );
    // List newList = AddressType.getList();
    // assertEquals( "Expected that the modified PickList will have one more item, and it did not.",
    // originalList.size() + 1,
    // newList.size() );
    // boolean listContainsNewItem = false;
    // for ( int i = 0; i < newList.size(); i++ )
    // {
    // AddressType addressType = (AddressType)newList.get( i );
    // if ( addressType.getName().equals( "myNewItem" ) )
    // {
    // listContainsNewItem = true;
    // }
    // }
    // assertTrue( "Expected to find new list item, but did not.", listContainsNewItem );
  }

  /**
   * Test the creation of a unique asset name.
   * 
   * @throws ServiceErrorException
   */
  @Test
  @Ignore
  public void testGetUniqueAssetName() throws ServiceErrorException
  {
    CMAssetServiceImpl cmAssetService = new CMAssetServiceImpl();
    OracleSequenceDAOImpl oracleSequenceDAO = new OracleSequenceDAOImpl();
    cmAssetService.setOracleSequenceDAO( oracleSequenceDAO );

    String assetName = cmAssetService.getUniqueAssetCode( "claimform" );
    String assetName2 = cmAssetService.getUniqueAssetCode( "claimform" );
    assert !assetName.equals( assetName2 );
  }

  /**
   * Test the full cycle round trip of creation, updating and reading of asset data.
   */
  @Test
  @Ignore
  public void testFullRoundTripCreationAndReading()
  // throws ServiceErrorException
  {
    // CMAssetServiceImpl cmAssetService = new CMAssetServiceImpl();

    // String assetName = cmAssetService.getUniqueAssetCode( "test.asset.name" );
    // CMSection cmSection = ( CMSection ) cmAssetService.getCMSections().get( 0 );
    // CMType cmType = CMUtil.createCMType( assetName , "Test Asset", cmSection.getSectionID(),
    // false );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY", "Test Key",
    // true));
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY2", "Test Key 2",
    // true ) );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY3", "Test Key 3",
    // true ) );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY4", "Test Key 4",
    // true ) );
    // cmAssetService.saveCMType( cmType );
    //
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY", "Value 1b" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY2", "Value 2a" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY3", "Value 3a" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY4", "Value 4a" );
    // cmAssetService.approveCMAsset( assetName );
    //
    // String datatext1 = CMReaderManager.getText( assetName, "TEST_KEY" );
    // assertEquals("Expected value from CMReader of 'Value 1b' and it was not.", datatext1, "Value
    // 1b");
    //
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY", "Value 1c" );
    // cmAssetService.approveCMAsset( assetName );
    //
    // //clear CM cache by hand in case udp message was lost...
    // DBReaderHost.getInstance().contentChanged( CMReaderManager.getCMReader().getAppCode() ,
    // CMReaderManager.getCMReader().getLocale(), assetName, null );
    //
    // String datatext2 = CMReaderManager.getText( assetName, "TEST_KEY" );
    // assertEquals("Expected value from CMReader of 'Value 1c' and it was not.", datatext2, "Value
    // 1c");
  }

  /**
   * Test deleting an Asset.
   */
  @Test
  @Ignore
  public void testDeleteAsset()
  // throws ServiceErrorException
  {
    // CMAssetServiceImpl cmAssetService = new CMAssetServiceImpl();

    // String assetName = cmAssetService.getUniqueAssetName("test.asset.name");
    // CMSection cmSection = ( CMSection ) cmAssetService.getCMSections().get( 0 );
    // CMType cmType = CMUtil.createCMType( assetName , "Test Asset", cmSection.getSectionID(),
    // false );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY", "Test Key",
    // true));
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY2", "Test Key 2",
    // true ) );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY3", "Test Key 3",
    // true ) );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY4", "Test Key 4",
    // true ) );
    // cmAssetService.saveCMType( cmType );
    //
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY", "Value 1b" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY2", "Value 2a" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY3", "Value 3a" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY4", "Value 4a" );
    // cmAssetService.approveCMAsset( assetName );

    // cmAssetService.deleteCMAsset( assetName );

    // assertNull(cmAssetService.getCMTypeByAssetKey( assetName ));
  }

  /**
   * Test copying a CMAsset.
   */
  @Test
  @Ignore
  public void testCopyCMAsset()
  // throws ServiceErrorException
  {
    // CMAssetServiceImpl cmAssetService = new CMAssetServiceImpl();

    // String assetName = cmAssetService.getUniqueAssetName("test.asset.name");
    // CMSection cmSection = ( CMSection ) cmAssetService.getCMSections().get( 0 );
    // CMType cmType = CMUtil.createCMType( assetName , "Test Asset", cmSection.getSectionID(),
    // false );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY", "Test Key",
    // true));
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY2", "Test Key 2",
    // true ) );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY3", "Test Key 3",
    // true ) );
    // cmType.getMetaDataList().add( CMUtil.createSimpleTextMetaData( "TEST_KEY4", "Test Key 4",
    // true ) );
    // cmAssetService.saveCMType( cmType );
    //
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY", "Value 1b" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY2", "Value 2a" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY3", "Value 3a" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY4", "Value 4a" );
    // cmAssetService.approveCMAsset( assetName );
    //
    // cmAssetService.createNewCMHeader( cmType, "de" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY", "de Value 1b", "de" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY2", "de Value 2a", "de" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY3", "de Value 3a", "de" );
    // cmAssetService.saveCMAssetData( assetName, "TEST_KEY4", "de Value 4a", "de" );
    // cmAssetService.approveCMAsset( assetName, "de" );
    //
    // String copyAssetName = cmAssetService.getUniqueAssetName("test.asset.name");
    // cmAssetService.copyCMAsset( assetName, copyAssetName );
    //
    // String data = CMReaderManager.getText( assetName, "TEST_KEY" );
    // String dataCopy = CMReaderManager.getText( copyAssetName, "TEST_KEY" );
    // assertEquals("Expected value from CMReader to equal copyied asset and it was not.", data,
    // dataCopy);
    //
    // CMReaderIF reader = DBReaderHost.getInstance( "BCN", Locale.GERMAN, CMContentType.LIVE);
    // String dataDe = CMReaderManager.getText( assetName, "TEST_KEY" );
    // String dataCopyDe = CMReaderManager.getText( copyAssetName, "TEST_KEY" );
    // assertEquals("Expected German value from CMReader to equal copyied asset and it was not.",
    // dataDe, dataCopyDe);
  }

}