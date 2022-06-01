/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/service/participant/impl/CharacteristicServiceImplTest.java,v $
 */

package com.biperf.core.service.participant.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

import com.biperf.core.dao.hierarchy.NodeTypeCharacteristicDAO;
import com.biperf.core.dao.participant.UserCharacteristicDAO;
import com.biperf.core.dao.product.ProductCharacteristicDAO;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeTypeCharacteristicService;
import com.biperf.core.service.hierarchy.impl.NodeTypeCharacteristicServiceImpl;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.product.ProductCharacteristicService;
import com.biperf.core.service.product.impl.ProductCharacteristicServiceImpl;

/**
 * CharacteristicServiceImplTest.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Jason</td>
 * <td>Apr 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CharacteristicServiceImplTest extends MockObjectTestCase
{

  /**
   * Constructor to take the name of the test.
   * 
   * @param test
   */
  public CharacteristicServiceImplTest( String test )
  {
    super( test );
  }

  /** CharacteristicServiceImpl */
  private UserCharacteristicService userCharacteristicService = new UserCharacteristicServiceImpl();
  private NodeTypeCharacteristicService nodeTypeCharacteristicService = new NodeTypeCharacteristicServiceImpl();
  private ProductCharacteristicService productCharacteristicService = new ProductCharacteristicServiceImpl();

  /** mockCharDAO */
  private Mock userMockCharDAO = null;
  private Mock nodeTypeMockCharDAO = null;
  private Mock productMockCharDAO = null;
  private Mock mockCmassetService = null;

  private CharacteristicDataType mockCharIntType = new CharacteristicDataType()
  {
    public String getCode()
    {
      return "INT";
    }
  };

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    userMockCharDAO = new Mock( UserCharacteristicDAO.class );
    nodeTypeMockCharDAO = new Mock( NodeTypeCharacteristicDAO.class );
    productMockCharDAO = new Mock( ProductCharacteristicDAO.class );
    mockCmassetService = new Mock( CMAssetService.class );

    userCharacteristicService.setCharacteristicDAO( (CharacteristicDAO)userMockCharDAO.proxy() );
    nodeTypeCharacteristicService.setCharacteristicDAO( (CharacteristicDAO)nodeTypeMockCharDAO.proxy() );
    productCharacteristicService.setCharacteristicDAO( (CharacteristicDAO)productMockCharDAO.proxy() );
    userCharacteristicService.setCmAssetService( (CMAssetService)mockCmassetService.proxy() );
    nodeTypeCharacteristicService.setCmAssetService( (CMAssetService)mockCmassetService.proxy() );
    productCharacteristicService.setCmAssetService( (CMAssetService)mockCmassetService.proxy() );
  }

  /**
   * Tests getting a set of NodeTypeCharacteristicTypes.
   */
  public void testGetAllNodeTypeCharacteristicTypes()
  {
    List expectedChars = new ArrayList();

    NodeTypeCharacteristicType newChar = new NodeTypeCharacteristicType();
    loadCharacteristicValues( newChar );
    newChar.setDomainId( new Long( 1 ) );

    expectedChars.add( newChar );

    NodeTypeCharacteristicType newChar2 = new NodeTypeCharacteristicType();
    loadCharacteristicValues( newChar2 );
    newChar2.setDomainId( new Long( 1 ) );
    expectedChars.add( newChar2 );

    // CharacteristicDAO expected to call getAll once with nothing which will return the set
    // expected
    nodeTypeMockCharDAO.expects( once() ).method( "getAllCharacteristics" ).will( returnValue( expectedChars ) );

    nodeTypeCharacteristicService.getAllCharacteristics();

    nodeTypeMockCharDAO.verify();
  }

  /**
   * Tests getting a set of UserCharacteristicTypes.
   */
  public void testGetAllUserCharacteristicTypes()
  {
    List expectedChars = new ArrayList();

    UserCharacteristicType newChar = new UserCharacteristicType();
    loadCharacteristicValues( newChar );
    expectedChars.add( newChar );

    UserCharacteristicType newChar2 = new UserCharacteristicType();
    loadCharacteristicValues( newChar2 );
    expectedChars.add( newChar2 );

    // CharacteristicDAO expected to call getAll once with nothing which will return the set
    // expected
    userMockCharDAO.expects( once() ).method( "getAllCharacteristics" ).will( returnValue( expectedChars ) );

    userCharacteristicService.getAllCharacteristics();

    userMockCharDAO.verify();
  }

  /**
   * Tests getting a set of UserCharacteristicTypes.
   */
  public void testGetAllProductCharacteristicTypes()
  {
    List expectedChars = new ArrayList();

    ProductCharacteristicType newChar = new ProductCharacteristicType();
    loadCharacteristicValues( newChar );
    expectedChars.add( newChar );

    ProductCharacteristicType newChar2 = new ProductCharacteristicType();
    loadCharacteristicValues( newChar2 );
    expectedChars.add( newChar2 );

    // CharacteristicDAO expected to call getAll once with nothing which will return the set
    // expected
    productMockCharDAO.expects( once() ).method( "getAllCharacteristics" ).will( returnValue( expectedChars ) );

    productCharacteristicService.getAllCharacteristics();

    productMockCharDAO.verify();
  }

  /**
   * Tests saving or updating NodeTypeCharacteristicType
   * 
   * @throws ServiceErrorException
   */
  public void testSaveNodeTypeCharacteristicType() throws ServiceErrorException
  {

    NodeTypeCharacteristicType newChar = new NodeTypeCharacteristicType();
    loadCharacteristicValues( newChar );
    newChar.setDomainId( new Long( 1 ) );

    // Setup the dao with the test characteristic
    nodeTypeMockCharDAO.expects( once() ).method( "saveCharacteristic" ).with( same( newChar ) );
    mockCmassetService.expects( once() ).method( "createOrUpdateAsset" );
    mockCmassetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( "test.asset.name" ) );

    // Test the CharacteristicService.saveCharacteristic
    nodeTypeCharacteristicService.saveCharacteristic( newChar );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    nodeTypeMockCharDAO.verify();
  }

  /**
   * Tests saving or updating UserCharacteristicType
   * 
   * @throws ServiceErrorException
   */
  public void testSaveUserCharacteristicType() throws ServiceErrorException
  {

    UserCharacteristicType newChar = new UserCharacteristicType();
    loadCharacteristicValues( newChar );

    // Setup the dao with the test characteristic
    userMockCharDAO.expects( once() ).method( "saveCharacteristic" ).with( same( newChar ) );
    mockCmassetService.expects( once() ).method( "createOrUpdateAsset" );
    mockCmassetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( "test.asset.name" ) );

    // Test the CharacteristicService.saveCharacteristic
    userCharacteristicService.saveCharacteristic( newChar );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    userMockCharDAO.verify();
  }

  /**
   * Tests saving or updating ProductCharacteristicType
   * 
   * @throws ServiceErrorException
   */
  public void testSaveProductCharacteristicType() throws ServiceErrorException
  {

    ProductCharacteristicType newChar = new ProductCharacteristicType();
    loadCharacteristicValues( newChar );
    Product product = new Product();
    product.setId( new Long( 1 ) );

    // Setup the dao with the test characteristic
    productMockCharDAO.expects( once() ).method( "saveCharacteristic" ).with( same( newChar ) );
    mockCmassetService.expects( once() ).method( "createOrUpdateAsset" );
    mockCmassetService.expects( once() ).method( "getUniqueAssetCode" ).will( returnValue( "test.asset.name" ) );

    // Test the CharacteristicService.saveCharacteristic
    productCharacteristicService.saveCharacteristic( newChar );

    // Verify the mockDAO returns the same thing the service is attempting to save.
    productMockCharDAO.verify();
  }

  /**
   * Loads the common charactertistic info into the object
   * 
   * @param characteristic
   */
  private void loadCharacteristicValues( Characteristic characteristic )
  {
    String uniqueName = String.valueOf( System.currentTimeMillis() % 29930291 );

    characteristic.setDescription( "Desc of New Char" + uniqueName );
    characteristic.setCharacteristicName( "New Char" + uniqueName );
    characteristic.setCharacteristicDataType( mockCharIntType );
    characteristic.setMinValue( new BigDecimal( "1.5" ) );
    characteristic.setMaxValue( new BigDecimal( "10.4" ) );
    characteristic.setMaxSize( new Long( 5 ) );
    characteristic.setPlName( "new picklist" );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );
    characteristic.setIsRequired( Boolean.valueOf( true ) );
    characteristic.setActive( true );
  }

}