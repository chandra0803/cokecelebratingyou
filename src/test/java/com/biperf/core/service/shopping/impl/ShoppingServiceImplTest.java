/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/service/shopping/impl/ShoppingServiceImplTest.java,v $
 */

package com.biperf.core.service.shopping.impl;

import org.easymock.EasyMock;

import com.biperf.core.domain.Address;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.MockPickListFactory;
import com.biperf.core.domain.enums.PickListItem;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.participant.impl.ParticipantServiceImplTest;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.MockContentReader;
import com.biperf.core.value.ShoppingValueBean;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ShoppingServiceImplTest.
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
 * <td>zahler</td>
 * <td>Sep 19, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ShoppingServiceImplTest extends BaseServiceTest
{
  private ShoppingServiceImpl shoppingServiceImplUnderTest;

  private ParticipantService participantServiceMock;
  private SystemVariableService systemVariableServiceMock;
  private UserService userServiceMock;

  /**
   * Setup this test case. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  @Override
  protected void setUp() throws Exception
  {
    super.setUp();
    shoppingServiceImplUnderTest = new ShoppingServiceImpl();

    participantServiceMock = EasyMock.createMock( ParticipantService.class );
    shoppingServiceImplUnderTest.setParticipantService( participantServiceMock );

    systemVariableServiceMock = EasyMock.createMock( SystemVariableService.class );
    shoppingServiceImplUnderTest.setSystemVariableService( systemVariableServiceMock );

    userServiceMock = EasyMock.createMock( UserService.class );
    shoppingServiceImplUnderTest.setUserService( userServiceMock );

    // Prepares the pickListFactory
    PickListItem.setPickListFactory( new MockPickListFactory() );
    // check if the ContentReader is already set - true if we are in container.
    if ( ContentReaderManager.getContentReader() == null )
    {
      ContentReaderManager.setContentReader( new MockContentReader() );
    }
  }

  public void testSetupInternalShopping() throws ServiceErrorException
  {
    Participant pax = ParticipantServiceImplTest.buildStaticParticipant();
    pax.setId( new Long( 1 ) );

    Country country = new Country();
    country.setProgramId( "12345" );
    country.setProgramPassword( "password" );
    country.setDisplayTravelAward( Boolean.FALSE );
    // country.setTimeZoneId( TimeZoneId.lookup( TimeZoneId.AUSTRALIA_MELBOURNE ) );
    Address address = new Address();
    address.setCountry( country );

    UserAddress userAddress = new UserAddress();
    userAddress.setIsPrimary( Boolean.TRUE );
    userAddress.setAddress( address );

    pax.addUserAddress( userAddress );

    EasyMock.expect( participantServiceMock.getParticipantById( pax.getId() ) ).andReturn( pax );
    EasyMock.replay( participantServiceMock );

    String envName = Environment.getEnvironment();
    String url = "http://www.awardslinq.com";
    String proxy = "proxy.biperf.com";
    Integer proxyPort = new Integer( 8080 );

    StringBuffer systemVariableName1 = new StringBuffer();
    systemVariableName1.append( SystemVariableService.SHOPPING_INTERNAL_REMOTE_URL_PREFIX );
    systemVariableName1.append( "." );
    systemVariableName1.append( envName );

    PropertySetItem systemVariableLookupResult1 = new PropertySetItem();
    systemVariableLookupResult1.setStringVal( url );
    systemVariableLookupResult1.setKey( "01" );
    systemVariableLookupResult1.setEntityName( systemVariableName1.toString() );

    EasyMock.expect( systemVariableServiceMock.getPropertyByName( systemVariableName1.toString() ) ).andReturn( systemVariableLookupResult1 );

    StringBuffer systemVariableName4 = new StringBuffer();
    // systemVariableName4.append( SystemVariableService.SHOPPING_INTERNAL_POST_LOGIN_URL_PREFIX );
    systemVariableName4.append( "." );
    systemVariableName4.append( envName );

    PropertySetItem systemVariableLookupResult4 = new PropertySetItem();
    systemVariableLookupResult4.setStringVal( url );
    systemVariableLookupResult4.setKey( "01" );
    systemVariableLookupResult4.setEntityName( systemVariableName4.toString() );

    EasyMock.expect( systemVariableServiceMock.getPropertyByName( systemVariableName4.toString() ) ).andReturn( systemVariableLookupResult4 );

    EasyMock.replay( systemVariableServiceMock );

    ShoppingValueBean shoppingValueBean = new ShoppingValueBean();
    shoppingValueBean.setAccount( pax.getAwardBanqNumber() );
    shoppingValueBean.setFirstName( pax.getFirstName() );
    shoppingValueBean.setLastName( pax.getLastName() );
    shoppingValueBean.setProgramId( "12345" );
    shoppingValueBean.setProgramPassword( "password" );
    shoppingValueBean.setProxy( proxy );
    shoppingValueBean.setProxyPort( proxyPort.intValue() );
    shoppingValueBean.setRemoteURL( url );
    shoppingValueBean.setPostLoginURL( url );
    System.setProperty( "bi.http.proxyPort", proxyPort.toString() );
    System.setProperty( "bi.http.proxyHost", proxy );

    ShoppingValueBean actualShoppingValueBean = shoppingServiceImplUnderTest.setupInternalShopping( pax.getId() );

    assertEquals( "Actual returned Proxy wasn't equal to what was expected", actualShoppingValueBean.getProxy(), shoppingValueBean.getProxy() );

    assertEquals( "Actual returned ProxyPort wasn't equal to what was expected", actualShoppingValueBean.getProxyPort(), shoppingValueBean.getProxyPort() );

    assertEquals( "Actual returned RemoteURL wasn't equal to what was expected", actualShoppingValueBean.getRemoteURL(), shoppingValueBean.getRemoteURL() );
  }

  public void testGetShoppingUrlForInactiveUser()
  {
    Long userId = 1234L;
    UserAddress userAddress = new UserAddress();

    String multiSupplierUrl = "multi";
    String inactiveUserUrl = "inactiveUserUrl";
    String externalUrl = "external";
    shoppingServiceImplUnderTest.setMultipleSupplierUrl( multiSupplierUrl );
    shoppingServiceImplUnderTest.setInactivatedUserUrl( inactiveUserUrl );
    shoppingServiceImplUnderTest.setExternalSupplierUrl( externalUrl );

    EasyMock.expect( userServiceMock.getPrimaryUserAddress( userId ) ).andReturn( userAddress );
    String shoppingUrl = shoppingServiceImplUnderTest.getShoppingUrlForInactiveUser( userId );

    assertEquals( inactiveUserUrl, shoppingUrl );
  }

}
