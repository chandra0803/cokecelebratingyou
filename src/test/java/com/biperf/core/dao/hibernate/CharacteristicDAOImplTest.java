/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/hibernate/CharacteristicDAOImplTest.java,v $
 */

package com.biperf.core.dao.hibernate;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.biperf.core.dao.hierarchy.NodeTypeCharacteristicDAO;
import com.biperf.core.dao.participant.UserCharacteristicDAO;
import com.biperf.core.dao.product.ProductCharacteristicDAO;
import com.biperf.core.dao.product.ProductDAO;
import com.biperf.core.dao.product.hibernate.ProductDAOImplTest;
import com.biperf.core.dao.system.CharacteristicDAO;
import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.characteristic.NodeTypeCharacteristicType;
import com.biperf.core.domain.characteristic.UserCharacteristicType;
import com.biperf.core.domain.enums.CharacteristicDataType;
import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCharacteristic;
import com.biperf.core.domain.product.ProductCharacteristicType;
import com.biperf.core.utils.ApplicationContextFactory;

/**
 * CharacteristicDAOImplTest.
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
public class CharacteristicDAOImplTest extends BaseDAOTest
{

  /**
   * Tests saving or updating the NodeTypeCharacteristicType. This needs to fetch the characteristic
   * by Id so it is also testing CharacteristicDAO.getNodeTypeCharacteristicTypeId(Long id).
   */
  public void testSaveAndGetNodeTypeCharacteristicTypeById()
  {

    NodeTypeCharacteristicDAO characteristicDAO = getNodeTypeCharacteristicDAO();

    NodeTypeCharacteristicType expectedChar = new NodeTypeCharacteristicType();
    loadCharacteristicValues( expectedChar );
    Long domainId = new Long( 1 );
    expectedChar.setDomainId( domainId );

    characteristicDAO.saveCharacteristic( expectedChar );
    flushAndClearSession();

    NodeTypeCharacteristicType actualChar = (NodeTypeCharacteristicType)characteristicDAO.getCharacteristicById( expectedChar.getId() );

    assertEquals( "NodeTypeCharacteristicType not equals", expectedChar, actualChar );

    // Update the Characteristic
    expectedChar.setDescription( "testUpdatedDescription" );
    expectedChar.setIsRequired( new Boolean( false ) );

    characteristicDAO.saveCharacteristic( expectedChar );
    flushAndClearSession();

    // Updated Characteristic from the database
    actualChar = (NodeTypeCharacteristicType)characteristicDAO.getCharacteristicById( expectedChar.getId() );

    assertEquals( "Updated NodeTypeCharacteristicType not equals", expectedChar, actualChar );

  }

  /**
   * Tests saving or updating the UserCharacteristicType. This needs to fetch the characteristic by
   * Id so it is also testing CharacteristicDAO.getUserCharacteristicTypeId(Long id).
   */
  public void testSaveAndGetUserCharacteristicTypeById()
  {
    UserCharacteristicDAO characteristicDAO = getUserCharacteristicDAO();

    UserCharacteristicType expectedChar = new UserCharacteristicType();
    loadCharacteristicValues( expectedChar );

    characteristicDAO.saveCharacteristic( expectedChar );

    UserCharacteristicType actualChar = (UserCharacteristicType)characteristicDAO.getCharacteristicById( expectedChar.getId() );

    assertEquals( "NodeTypeCharacteristicType not equals", expectedChar, actualChar );

    // Update the Characteristic
    expectedChar.setCharacteristicName( "testUpdatedName" );
    expectedChar.setIsRequired( Boolean.FALSE );

    characteristicDAO.saveCharacteristic( expectedChar );

    // Updated Characteristic from the database
    actualChar = (UserCharacteristicType)characteristicDAO.getCharacteristicById( expectedChar.getId() );

    assertEquals( "Updated NodeTypeCharacteristicType not equals", expectedChar, actualChar );

  }

  @Test
  public void testGetAvailableUserCharacteristicsForPaxIdentifiers()
  {
    UserCharacteristicDAO characteristicDAO = getUserCharacteristicDAO();
    UserCharacteristicType expectedChar = new UserCharacteristicType();
    loadCharacteristicValues( expectedChar );
    characteristicDAO.saveCharacteristic( expectedChar );

    List<Characteristic> chars = characteristicDAO.getAvailbleParticipantIdentifierCharacteristics();

    assertNotNull( "characterist list is null", chars );
    assertTrue( "characteristic list is empty", !chars.isEmpty() );
    assertTrue( "The newly add UserCharacteristing was not in the returned list", chars.stream().anyMatch( c -> c.getId().equals( expectedChar.getId() ) ) );
  }

  /**
   * Tests saving or updating the ProductCharacteristicType. This needs to fetch the characteristic
   * by Id so it is also testing CharacteristicDAO.getProductCharacteristicTypeeId(Long id).
   */
  public void testSaveAndGetProductCharacteristicTypeById()
  {
    ProductCharacteristicDAO characteristicDAO = getProductCharacteristicDAO();

    ProductCharacteristicType expectedChar = new ProductCharacteristicType();
    loadCharacteristicValues( expectedChar );
    characteristicDAO.saveCharacteristic( expectedChar );

    flushAndClearSession();

    Product product = ProductDAOImplTest.buildStaticProductDomainObject( "55", ProductDAOImplTest.getProductCategoryDomainObject( "1000" ) );
    product.addProductCharacteristicByType( expectedChar );
    getProductDAO().save( product );

    flushAndClearSession();

    ProductCharacteristicType actualChar = (ProductCharacteristicType)characteristicDAO.getCharacteristicById( expectedChar.getId() );

    assertEquals( "ProductCharacteristicType not equals", expectedChar, actualChar );

    // Update the Characteristic
    expectedChar.setIsRequired( new Boolean( false ) );
    expectedChar.setIsUnique( new Boolean( true ) );

    expectedChar = (ProductCharacteristicType)characteristicDAO.saveCharacteristic( expectedChar );

    flushAndClearSession();

    actualChar = (ProductCharacteristicType)characteristicDAO.getCharacteristicById( expectedChar.getId() );

    assertDomainObjectEquals( expectedChar, actualChar );

    // Update the Characteristic again, but with product in session
    flushAndClearSession();

    expectedChar.setIsRequired( new Boolean( false ) );
    expectedChar.setIsUnique( new Boolean( true ) );

    expectedChar = (ProductCharacteristicType)characteristicDAO.saveCharacteristic( expectedChar );

    flushAndClearSession();

    actualChar = (ProductCharacteristicType)characteristicDAO.getCharacteristicById( expectedChar.getId() );

    assertDomainObjectEquals( expectedChar, actualChar );

  }

  /**
   * Confirms that we can merge a detached product which has an unpopulated set into a product that
   * has a poulated set without the persisted set being deleted.
   */
  public void testSaveProductWithEmptyCharacteristics()
  {

    ProductCharacteristic expectedChar = new ProductCharacteristic();

    ProductCharacteristicType pcType = new ProductCharacteristicType();
    loadCharacteristicValues( pcType );

    expectedChar.setProductCharacteristicType( pcType );

    Product product = ProductDAOImplTest.buildStaticProductDomainObject( "56", ProductDAOImplTest.getProductCategoryDomainObject( "efg" ) );
    product.setProductCharacteristics( new LinkedHashSet() );
    product.addProductCharacteristic( expectedChar );
    getProductDAO().save( product );

    // WBR - commented out so that we are testing a MERGE (as per above) and not an UPDATE
    // flushAndClearSession();

    Product productCopy = new Product();
    productCopy.setId( product.getId() );
    productCopy.setName( product.getName() );
    productCopy.setProductCategory( product.getProductCategory() );
    productCopy.setVersion( product.getVersion() );
    productCopy.setDescription( product.getDescription() + "foo" );

    Product savedProductCopy = getProductDAO().save( productCopy );

    flushAndClearSession();

    assertEquals( savedProductCopy.getId(), productCopy.getId() );

    Set actualChars = getProductDAO().getProductById( product.getId() ).getProductCharacteristics();

    assertTrue( actualChars.contains( expectedChar ) );

  }

  /**
   * Tests saving and getting all the NodeTypeCharacteristicTypes saved.
   */
  public void testGetAllNodeTypeCharacteristicTypes()
  {

    CharacteristicDAO characteristicDAO = getNodeTypeCharacteristicDAO();

    int count = 0;

    count = characteristicDAO.getAllCharacteristics().size();

    NodeTypeCharacteristicType expectedChar1 = new NodeTypeCharacteristicType();
    loadCharacteristicValues( expectedChar1 );
    expectedChar1.setDomainId( new Long( 1 ) );
    count++;
    characteristicDAO.saveCharacteristic( expectedChar1 );

    NodeTypeCharacteristicType expectedChar2 = new NodeTypeCharacteristicType();
    loadCharacteristicValues( expectedChar2 );
    expectedChar2.setDomainId( new Long( 2 ) );
    count++;
    characteristicDAO.saveCharacteristic( expectedChar2 );

    // Add one of these, won't go toward the count.
    UserCharacteristicType userChar = new UserCharacteristicType();
    loadCharacteristicValues( userChar );
    characteristicDAO.saveCharacteristic( userChar );

    List actualChars = characteristicDAO.getAllCharacteristics();

    assertEquals( "List of NodeTypeCharacteristicType aren't the same size.", count, actualChars.size() );

  }

  /**
   * Tests saving and getting all the NodeTypeCharacteristicTypes saved.
   */
  public void testGetAllProductCharacteristicTypesByProduct()
  {
    ProductCharacteristicDAO characteristicDAO = getProductCharacteristicDAO();

    int count = 0;

    ProductCharacteristicType expectedChar1 = new ProductCharacteristicType();
    loadCharacteristicValues( expectedChar1 );
    expectedChar1.setIsUnique( Boolean.TRUE );
    characteristicDAO.saveCharacteristic( expectedChar1 );
    flushAndClearSession();

    Product product = ProductDAOImplTest.buildStaticProductDomainObject( "55", ProductDAOImplTest.getProductCategoryDomainObject( "abc" ) );
    product.setProductCharacteristics( new LinkedHashSet() );
    product.addProductCharacteristicByType( expectedChar1 );
    count++;

    getProductDAO().save( product );
    Long productId = product.getId();
    flushAndClearSession();

    // Add NodeTypeCharacteristicType for same domain id, shouldn't go toward the count.
    NodeTypeCharacteristicType expectedChar2 = new NodeTypeCharacteristicType();
    loadCharacteristicValues( expectedChar2 );
    expectedChar2.setDomainId( productId );
    getNodeTypeCharacteristicDAO().saveCharacteristic( expectedChar2 );

    Set actualChars = getProductDAO().getProductById( productId ).getProductCharacteristics();

    assertEquals( "List of ProductCharacteristicType aren't the same size.", count, actualChars.size() );

  }

  /**
   * Tests getActiveProductsWithCharacteristicCount.
   */
  public void testGetActiveProductsWithCharacteristicCount()
  {
    ProductCharacteristicDAO characteristicDAO = getProductCharacteristicDAO();

    int count = 0;

    ProductCharacteristicType expectedChar1 = new ProductCharacteristicType();
    loadCharacteristicValues( expectedChar1 );
    expectedChar1.setIsUnique( Boolean.TRUE );
    characteristicDAO.saveCharacteristic( expectedChar1 );
    flushAndClearSession();

    Product product = ProductDAOImplTest.buildStaticProductDomainObject( "55", ProductDAOImplTest.getProductCategoryDomainObject( "abc" ) );
    product.setProductCharacteristics( new LinkedHashSet() );
    product.addProductCharacteristicByType( expectedChar1 );
    count++;

    getProductDAO().save( product );
    flushAndClearSession();

    int productCount = characteristicDAO.getActiveProductsWithCharacteristicCount( expectedChar1.getId() );

    assertTrue( "There should be at least one product with this characeristic.", productCount > 0 );
  }

  /**
   * Tests saving and getting all the UserCharacteristicTypes saved.
   */
  public void testGetAllUserCharacteristicTypes()
  {

    CharacteristicDAO characteristicDAO = getUserCharacteristicDAO();

    int count = 0;

    count = characteristicDAO.getAllCharacteristics().size();

    // Add one of these, won't go toward the count.
    NodeTypeCharacteristicType expectedChar1 = new NodeTypeCharacteristicType();
    loadCharacteristicValues( expectedChar1 );
    expectedChar1.setDomainId( new Long( 1 ) );
    characteristicDAO.saveCharacteristic( expectedChar1 );

    UserCharacteristicType userChar1 = new UserCharacteristicType();
    loadCharacteristicValues( userChar1 );
    count++;
    characteristicDAO.saveCharacteristic( userChar1 );

    UserCharacteristicType userChar2 = new UserCharacteristicType();
    loadCharacteristicValues( userChar2 );
    count++;
    characteristicDAO.saveCharacteristic( userChar2 );

    List actualChars = characteristicDAO.getAllCharacteristics();

    assertEquals( "List of UserCharacteristicType aren't the same size.", count, actualChars.size() );

  }

  /**
  * Tests saving and getting all the ProductCharacteristicTypes saved.
  */
  public void testGetAllProductCharacteristicTypes()
  {

    CharacteristicDAO characteristicDAO = getProductCharacteristicDAO();

    int count = characteristicDAO.getAllCharacteristics().size();

    ProductCharacteristicType expectedChar1 = new ProductCharacteristicType();
    loadCharacteristicValues( expectedChar1 );
    expectedChar1.setIsUnique( Boolean.TRUE );
    // expectedChar1.setDomainId( new Long(1) );

    characteristicDAO.saveCharacteristic( expectedChar1 );
    count++;

    // Add one of these, won't go toward the count.
    UserCharacteristicType userChar1 = new UserCharacteristicType();
    loadCharacteristicValues( userChar1 );
    characteristicDAO.saveCharacteristic( userChar1 );

    flushAndClearSession();

    List actualChars = characteristicDAO.getAllCharacteristics();

    assertEquals( "List of ProductCharacteristicType aren't the same size.", count, actualChars.size() );

  }

  /**
   * Tests saving and getting all the NodeTypeCharacteristicTypes by nodeTypeId saved.
   */
  public void testGetAllNodeTypeCharacteristicTypesByNodeTypeId()
  {

    NodeTypeCharacteristicDAO characteristicDAO = getNodeTypeCharacteristicDAO();

    Long nodeTypeId = new Long( 1 );

    int count = 0;

    count = characteristicDAO.getAllNodeTypeCharacteristicTypesByNodeTypeId( nodeTypeId ).size();

    NodeTypeCharacteristicType expectedChar1 = new NodeTypeCharacteristicType();
    loadCharacteristicValues( expectedChar1 );
    expectedChar1.setDomainId( nodeTypeId );
    count++;
    characteristicDAO.saveCharacteristic( expectedChar1 );

    NodeTypeCharacteristicType expectedChar2 = new NodeTypeCharacteristicType();
    loadCharacteristicValues( expectedChar2 );
    expectedChar2.setDomainId( new Long( 2 ) );
    characteristicDAO.saveCharacteristic( expectedChar2 );

    // Add one of these, won't go toward the count.
    UserCharacteristicType userChar = new UserCharacteristicType();
    loadCharacteristicValues( userChar );
    characteristicDAO.saveCharacteristic( userChar );

    List actualChars = characteristicDAO.getAllNodeTypeCharacteristicTypesByNodeTypeId( nodeTypeId );

    assertEquals( "Set of NodeTypeCharacteristicType aren't the same size.", count, actualChars.size() );

  }

  /**
   * Loads the common charactertistic info into the object
   * 
   * @param characteristic
   */
  public static void loadCharacteristicValues( Characteristic characteristic )
  {
    String uniqueName = String.valueOf( Math.random() % 29930291 );

    characteristic.setDescription( "Desc of New Char" + uniqueName );
    characteristic.setCharacteristicName( "New Char" + uniqueName );
    characteristic.setCharacteristicDataType( CharacteristicDataType.lookup( CharacteristicDataType.BOOLEAN ) );
    characteristic.setMinValue( new BigDecimal( "1.5" ) );
    characteristic.setMaxValue( new BigDecimal( "10.4" ) );
    characteristic.setMaxSize( new Long( 5 ) );
    characteristic.setPlName( "new picklist" );
    characteristic.setNameCmKey( "asset_key" );
    characteristic.setCmAssetCode( "test.asset.name" + uniqueName );
    characteristic.setIsRequired( Boolean.valueOf( true ) );
    characteristic.setActive( true );
    characteristic.setVisibility( CharacteristicVisibility.lookup( CharacteristicVisibility.VISIBLE ) );
  }

  /**
   * Get the ProductCharacteristicDAO.
   * 
   * @return CharacteristicDAO
   */
  private ProductCharacteristicDAO getProductCharacteristicDAO()
  {
    return (ProductCharacteristicDAO)ApplicationContextFactory.getApplicationContext().getBean( "productCharacteristicDAO" );
  }

  /**
   * Get the NodeTypeCharacteristicDAO.
   * 
   * @return NodeTypeCharacteristicDAO
   */
  private NodeTypeCharacteristicDAO getNodeTypeCharacteristicDAO()
  {
    return (NodeTypeCharacteristicDAO)ApplicationContextFactory.getApplicationContext().getBean( "nodeTypeCharacteristicDAO" );
  }

  /**
   * Get the UserCharacteristicDAO.
   * 
   * @return UserCharacteristicDAO
   */
  private UserCharacteristicDAO getUserCharacteristicDAO()
  {
    return (UserCharacteristicDAO)ApplicationContextFactory.getApplicationContext().getBean( "userCharacteristicDAO" );
  }

  /**
   * Uses the ApplicationContextFactory to look up the ProductDAO implementation.
   * 
   * @return ProductDAO
   */
  private ProductDAO getProductDAO()
  {
    return (ProductDAO)ApplicationContextFactory.getApplicationContext().getBean( "productDAO" );
  }
}
