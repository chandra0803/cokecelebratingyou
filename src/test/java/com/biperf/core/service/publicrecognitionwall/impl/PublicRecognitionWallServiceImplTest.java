
package com.biperf.core.service.publicrecognitionwall.impl;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;

import java.util.ArrayList;

import org.easymock.classextension.EasyMock;

import com.biperf.core.builders.BuilderUtil;
import com.biperf.core.dao.publicrecognitionwall.PublicRecognitionWallDAO;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.BaseServiceTest;
import com.biperf.core.service.publicrecognitionwall.dto.PublicRecognitionWall;
import com.biperf.core.service.system.SystemVariableService;

public class PublicRecognitionWallServiceImplTest extends BaseServiceTest
{
  
  private SystemVariableService mockSystemVariableService;
  private PublicRecognitionWallDAO mockPublicRecognitionWallDAO;

  /**
   * PublicRecognitionWallServiceImplTest Constructor to take the name of the
   * test.
   * 
   * @param test
   */

  public PublicRecognitionWallServiceImplTest( String test )
  {
    super( test );
  }

  /** PublicRecognitionService */
  private PublicRecognitionWallServiceImpl publicRecognitionWallImpl;

  /**
   * Setup the test. Overridden from
   * 
   * @see junit.framework.TestCase#setUp()
   * @throws Exception
   */
  protected void setUp() throws Exception
  {
    super.setUp();
    
    mockSystemVariableService = createMock( SystemVariableService.class );
    mockPublicRecognitionWallDAO = createMock( PublicRecognitionWallDAO.class );
    
    publicRecognitionWallImpl = new PublicRecognitionWallServiceImpl()
    {
      protected PublicRecognitionWall getCachedWall( String key )
      {
        return new PublicRecognitionWall();
      }
      
      protected void putCachedWall( String key, PublicRecognitionWall wall )
      {
        // Let's just say the 'put' call worked. Static state and unit tests, man.
      }
      
      protected PublicRecognitionWallDAO getPublicRecognitionWallDAO()
      {
        return mockPublicRecognitionWallDAO;
      }
    };
    publicRecognitionWallImpl.setSystemVariableService( mockSystemVariableService );
  }

  public void testGetPublicRecognitionWallByLocationId() throws ServiceErrorException
  {
    final PropertySetItem wallEnabled = new PropertySetItem();
    wallEnabled.setBooleanVal( Boolean.TRUE );
    expect( mockSystemVariableService.getPropertyByName( SystemVariableService.PUBLIC_RECOG_WALL_FEED_ENABLED ) ).andReturn( wallEnabled );
    replay( mockSystemVariableService );

    PublicRecognitionWall publicRecognitionWall = publicRecognitionWallImpl.getPublicRecognitionWall();
    assertNotNull( publicRecognitionWall );
    EasyMock.verify( mockSystemVariableService );
  }

  public void testRefresh() throws ServiceErrorException
  {
    final PropertySetItem pageCount = new PropertySetItem();
    pageCount.setIntVal( 100 );
    expect( mockSystemVariableService.getPropertyByName( SystemVariableService.PUBLIC_RECOG_WALL_SP_PAGECOUNT ) ).andReturn( pageCount );
    expect( mockSystemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ) ).andReturn( BuilderUtil.buildPropertySetItem() );
    replay( mockSystemVariableService );
    
    expect( mockPublicRecognitionWallDAO.getPublicRecognitionWallByPageCount( EasyMock.eq( 100 ) ) ).andReturn( new ArrayList<>() );
    replay( mockPublicRecognitionWallDAO );
    
    PublicRecognitionWall publicRecognitionWall = publicRecognitionWallImpl.refresh();
    assertNotNull( publicRecognitionWall );
  }

}
