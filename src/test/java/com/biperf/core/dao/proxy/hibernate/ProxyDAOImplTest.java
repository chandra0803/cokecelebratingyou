/**
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javatest/com/biperf/core/dao/proxy/hibernate/ProxyDAOImplTest.java,v $
 */

package com.biperf.core.dao.proxy.hibernate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.participant.hibernate.UserDAOImplTest;
import com.biperf.core.dao.promotion.PromotionDAO;
import com.biperf.core.dao.promotion.hibernate.PromotionDAOImplTest;
import com.biperf.core.dao.proxy.ProxyDAO;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.proxy.Proxy;
import com.biperf.core.domain.proxy.ProxyModule;
import com.biperf.core.domain.proxy.ProxyModulePromotion;

/**
 * ProxyDAOImplTest.
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
 * <td>sedey</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProxyDAOImplTest extends BaseDAOTest
{
  /**
   * Tests create and selecting the proxy by the Id.
   */
  public void testProxySaveAndGetByIdAndDelete()
  {
    // create a new proxy
    ProxyDAO proxyDAO = getProxyDAO();

    Proxy expectedProxy = ProxyDAOImplTest.buildProxy();
    proxyDAO.save( expectedProxy );

    flushAndClearSession();

    Proxy actualProxy = proxyDAO.getProxyById( expectedProxy.getId() );

    assertEquals( "Actual proxy doesn't match with expected", expectedProxy, actualProxy );

    proxyDAO.delete( actualProxy );

    flushAndClearSession();

    assertNull( "Proxy Not Deleted", proxyDAO.getProxyById( expectedProxy.getId() ) );
  }

  /**
   * Tests create and selecting the proxy by the userId and proxyUserId.
   */
  public void testGetProxiesByUserId()
  {
    List proxyList = new ArrayList();

    // create a new proxy
    ProxyDAO proxyDAO = getProxyDAO();

    Proxy expectedProxy = ProxyDAOImplTest.buildProxy();

    proxyList = proxyDAO.getProxiesByUserId( expectedProxy.getUser().getId() );

    proxyList.add( expectedProxy );

    proxyDAO.save( expectedProxy );

    flushAndClearSession();

    assertEquals( "Actual proxyList Size doesn't match with expected", proxyList.size(), proxyDAO.getProxiesByUserId( expectedProxy.getUser().getId() ).size() );
  }

  /**
   * Tests create and selecting the proxy by the userId and proxyUserId.
   */
  public void testProxySaveAndGetByUserIdAndProxyUserId()
  {
    // create a new proxy
    ProxyDAO proxyDAO = getProxyDAO();

    Proxy expectedProxy = ProxyDAOImplTest.buildProxy();

    proxyDAO.save( expectedProxy );

    flushAndClearSession();

    assertEquals( "Actual proxy doesn't match with expected",
                  expectedProxy,
                  proxyDAO.getProxyByUserAndProxyUserWithAssociations( expectedProxy.getUser().getId(), expectedProxy.getProxyUser().getId(), null ) );
  }

  /**
   * Tests create and selecting the proxy by the userId and proxyUserId.
   */
  public void testGetByUsersByProxyUserId()
  {
    List userList = new ArrayList();

    // create a new proxy
    ProxyDAO proxyDAO = getProxyDAO();

    Proxy expectedProxy = ProxyDAOImplTest.buildProxy();

    userList = proxyDAO.getUsersByProxyUserId( expectedProxy.getProxyUser().getId() );

    userList.add( expectedProxy.getUser() );

    proxyDAO.save( expectedProxy );

    flushAndClearSession();

    List userListAfterSave = proxyDAO.getUsersByProxyUserId( expectedProxy.getProxyUser().getId() );

    assertEquals( "User Lists Not Equal", userList.size(), userListAfterSave.size() );
  }

  /**
   * Tests create and selecting the proxy by the Id.
   */
  public void testProxySaveWithModuleAndPromotions()
  {
    // create a new proxy
    ProxyDAO proxyDAO = getProxyDAO();

    Proxy expectedProxy = ProxyDAOImplTest.buildProxy();

    ProxyModule proxyModule = buildProxyModule( PromotionType.lookup( PromotionType.RECOGNITION ) );
    ProxyModule proxyModule2 = buildProxyModule( PromotionType.lookup( PromotionType.PRODUCT_CLAIM ) );

    ProxyModulePromotion proxyModulePromo = buildProxyModulePromotion( "PromoProxyTest", "prod_claim" );
    proxyModule2.addProxyModulePromotion( proxyModulePromo );

    expectedProxy.addProxyModule( proxyModule );
    expectedProxy.addProxyModule( proxyModule2 );

    proxyDAO.save( expectedProxy );

    flushAndClearSession();

    Proxy actualProxy = proxyDAO.getProxyById( expectedProxy.getId() );

    assertEquals( "Actual proxy doesn't match with expected", expectedProxy, actualProxy );

    assertEquals( "Actual proxyModuleSize doesn't match with expected", expectedProxy.getProxyModules().size(), actualProxy.getProxyModules().size() );

    Iterator proxyModuleIterator = actualProxy.getProxyModules().iterator();
    while ( proxyModuleIterator.hasNext() )
    {
      ProxyModule actualProxyModule = (ProxyModule)proxyModuleIterator.next();
      if ( actualProxyModule.equals( proxyModule ) )
      {
        assertEquals( "Actual ProxyModulePromotion size doesn't match with expected", proxyModule.getProxyModulePromotions().size(), actualProxyModule.getProxyModulePromotions().size() );
      }
      if ( actualProxyModule.equals( proxyModule2 ) )
      {
        assertEquals( "Actual ProxyModulePromotion size doesn't match with expected", proxyModule2.getProxyModulePromotions().size(), actualProxyModule.getProxyModulePromotions().size() );
      }
    }
  }

  /**
   * Creates a proxy domain object
   * 
   * @return Promotion
   */
  public static Proxy buildProxy()
  {
    Proxy proxy = new Proxy();

    UserDAO userDAO = getUserDAO();

    // A proxy needs both a user and proxyUser so create those first.
    Participant user = new Participant();
    user = UserDAOImplTest.buildStaticUser( "userWithProxy", "UserWithProxyFirst", "UserWithProxyLast" );
    userDAO.saveUser( user );
    Participant proxyUser = new Participant();
    proxyUser = UserDAOImplTest.buildStaticUser( "proxyUser", "ProxyUserFirst", "ProxyUserLast" );
    userDAO.saveUser( proxyUser );

    proxy.setProxyUser( proxyUser );
    proxy.setUser( user );

    return proxy;
  }

  public static ProxyModule buildProxyModule( PromotionType promoType )
  {
    ProxyModule proxyModule = new ProxyModule();

    proxyModule.setPromotionType( promoType );
    proxyModule.setAllPromotions( false );

    return proxyModule;
  }

  public static ProxyModulePromotion buildProxyModulePromotion( String promoName, String promoType )
  {
    PromotionDAO promoDAO = getPromotionDAO();

    ProxyModulePromotion proxyModulePromo = new ProxyModulePromotion();

    Promotion promotion = null;

    if ( promoType.equals( "prod_claim" ) )
    {
      promotion = PromotionDAOImplTest.buildProductClaimPromotion( promoName );
    }
    else
    {
      promotion = PromotionDAOImplTest.buildRecognitionPromotion( promoName );
    }

    promoDAO.save( promotion );

    proxyModulePromo.setPromotion( promotion );

    return proxyModulePromo;

  }

  /**
   * Uses the ApplicationContextFactory to look up the ProxyDAO implementation.
   * 
   * @return ProxyDAO
   */
  private static ProxyDAO getProxyDAO()
  {
    return (ProxyDAO)getDAO( ProxyDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the UserDAO implementation.
   * 
   * @return UserDAO
   */
  private static UserDAO getUserDAO()
  {
    return (UserDAO)getDAO( UserDAO.BEAN_NAME );
  }

  /**
   * Uses the ApplicationContextFactory to look up the PromotionDAO implementation.
   * 
   * @return PromotionDAO
   */
  private static PromotionDAO getPromotionDAO()
  {
    return (PromotionDAO)getDAO( PromotionDAO.BEAN_NAME );
  }
}