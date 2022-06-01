/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/dao/promotion/hibernate/PromotionNotificationDAOImplTest.java,v $
 */

package com.biperf.core.dao.homepage.hibernate;

import java.util.List;

import com.biperf.core.dao.hibernate.BaseDAOTest;
import com.biperf.core.dao.homepage.ModuleAppDAO;
import com.biperf.core.domain.enums.ModuleAppAudienceCodeType;
import com.biperf.core.domain.enums.ModuleAppAudienceType;
import com.biperf.core.domain.enums.TileMappingType;
import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.ModuleApp;

/**
 * ModuleAppDAOImplTest.
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
 * <td>dudyala</td>
 * <td>Aug 2, 2016</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ModuleAppDAOImplTest extends BaseDAOTest
{
  private ModuleAppDAO moduleAppDAO;

  /**
   * Sets up the fixture, for example, open a network connection. This method is called before a
   * test is executed. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  public void setUp() throws Exception
  {
    super.setUp();
    moduleAppDAO = getModuleAppDAO();
  }

  /**
   * Uses the ApplicationContextFactory to look up the ModuleAppDAO implementation.
   * 
   * @return ModuleAppDAO
   */
  private static ModuleAppDAO getModuleAppDAO()
  {
    return (ModuleAppDAO)getDAO( "moduleAppDAO" );
  }

  /**
   * Test getting a list of module app objects for xpromos.
   */
  public void testSaveAndGetAllXPromoComponents()
  {

    CrossPromotionalModuleApp newsCrossPromotionalModuleApp = new CrossPromotionalModuleApp();
    newsCrossPromotionalModuleApp.setOrder( 1 );
    newsCrossPromotionalModuleApp.setName( "news" );
    newsCrossPromotionalModuleApp.setDescription( "Desc - News " );
    newsCrossPromotionalModuleApp.setAppAudienceType( ModuleAppAudienceCodeType.lookup( "newsModule" ) );
    newsCrossPromotionalModuleApp.setAudienceType( ModuleAppAudienceType.lookup( "allactivepaxaudience" ) );
    newsCrossPromotionalModuleApp.setAppName( "news" );
    newsCrossPromotionalModuleApp.setTileMappingType( TileMappingType.lookup( "newsModule" ) );
    newsCrossPromotionalModuleApp.setAdminAudienceSetup( true );
    newsCrossPromotionalModuleApp.setMobileEnabled( false );
    newsCrossPromotionalModuleApp.setAvailableSizes( "4X4,4X2,2X2,2X1,1X1" );

    CrossPromotionalModuleApp bannersCrossPromotionalModuleApp = new CrossPromotionalModuleApp();
    bannersCrossPromotionalModuleApp.setOrder( 2 );
    bannersCrossPromotionalModuleApp.setName( "Banner Ads" );
    bannersCrossPromotionalModuleApp.setDescription( "Desc - Banner Ads" );
    bannersCrossPromotionalModuleApp.setAppAudienceType( ModuleAppAudienceCodeType.lookup( "banner" ) );
    bannersCrossPromotionalModuleApp.setAudienceType( ModuleAppAudienceType.lookup( "allactivepaxaudience" ) );
    bannersCrossPromotionalModuleApp.setAppName( "bannerModule" );
    bannersCrossPromotionalModuleApp.setTileMappingType( TileMappingType.lookup( "bannerModuleModule" ) );
    bannersCrossPromotionalModuleApp.setAdminAudienceSetup( true );
    bannersCrossPromotionalModuleApp.setMobileEnabled( false );
    bannersCrossPromotionalModuleApp.setAvailableSizes( "4X4,4X2,2X2,2X1,1X1" );

    ModuleApp quizModuleApp = new ModuleApp();
    quizModuleApp.setName( "Quiz" );
    quizModuleApp.setDescription( "Desc - Quiz  " );
    quizModuleApp.setAppAudienceType( ModuleAppAudienceCodeType.lookup( "quizModule" ) );
    quizModuleApp.setAudienceType( ModuleAppAudienceType.lookup( "allactivepaxaudience" ) );
    quizModuleApp.setAppName( "quiz" );
    quizModuleApp.setTileMappingType( TileMappingType.lookup( "quizModule" ) );
    quizModuleApp.setAdminAudienceSetup( false );
    quizModuleApp.setMobileEnabled( false );
    quizModuleApp.setAvailableSizes( "2X2,2X1,1X1" );

    moduleAppDAO.save( newsCrossPromotionalModuleApp );
    moduleAppDAO.save( bannersCrossPromotionalModuleApp );
    moduleAppDAO.save( quizModuleApp );

    // Clear the session
    flushAndClearSession();

    List<CrossPromotionalModuleApp> crossPromotionalComponentList = moduleAppDAO.getCrossPromotionalModuleApps();

    assertEquals( "expected CrossPromotionalList wasn't equal to actual", 4, crossPromotionalComponentList.size() );

  }

  public void testUpdateSalesMakerFilterPageSetup()
  {
    moduleAppDAO.updateSalesMakerFilterPageSetup( true );

    List<ModuleApp> modules = moduleAppDAO.getAllAvailableModuleApps();
    assertEquals( "expected modules equal to actual", 3, modules.size() );

    moduleAppDAO.updateSalesMakerFilterPageSetup( false );

    modules = moduleAppDAO.getAllAvailableModuleApps();
    assertEquals( "expected Modules wasn't equal to actual", 3, modules.size() );

  }

}
