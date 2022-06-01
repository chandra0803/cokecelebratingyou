/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/merchorder/impl/MerchOrderServiceImpl.java,v $
 */

package com.biperf.core.service.merchorder.impl;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.junit.Before;
import org.junit.Test;

import com.biperf.core.dao.merchandise.MerchOrderDAO;
import com.biperf.core.domain.user.PlateauRedemptionTracking;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.ProjectionAttribute;
import com.biperf.core.service.ProjectionCollection;

/**
 * 
 * MerchOrderServiceImplTest.
 * 
 * @author Rameshj
 * @since Aug 25, 2017
 * @version 1.0
 */

public class MerchOrderServiceImplTest extends MockObjectTestCase
{
  private MerchOrderServiceImpl testMerchOrderServiceImpl = new MerchOrderServiceImpl();
  private Mock testMerchOrderDAO;

  public MerchOrderServiceImplTest( String test )
  {
    super( test );
  }

  @Before
  protected void setUp() throws Exception
  {
    super.setUp();
    testMerchOrderDAO = new Mock( MerchOrderDAO.class );
    testMerchOrderServiceImpl.setMerchOrderDAO( (MerchOrderDAO)testMerchOrderDAO.proxy() );
  }

  @Test
  public void testSavePlateauRedemptionTracking() throws ServiceErrorException, SQLException
  {

    PlateauRedemptionTracking plateauRedemptionTracking = new PlateauRedemptionTracking();
    plateauRedemptionTracking.setUserId( new Long( 12345 ) );
    plateauRedemptionTracking.setMerchOrderId( 123L );
    plateauRedemptionTracking.setCreatedBy( new Long( 12345 ) );
    plateauRedemptionTracking.setDateCreated( new Timestamp( System.currentTimeMillis() ) );
    plateauRedemptionTracking.setVersion( new Long( 0 ) );
    testMerchOrderDAO.expects( once() ).method( "savePlateauRedemptionTracking" ).will( returnValue( plateauRedemptionTracking ) );
    PlateauRedemptionTracking testPlateauRedemptionTracking = testMerchOrderServiceImpl.savePlateauRedemptionTracking( plateauRedemptionTracking );
    assertNotNull( testPlateauRedemptionTracking );

  }

  @Test
  public void testGetMerchOrderByIdWithProjections() throws ServiceErrorException, SQLException
  {

    ProjectionCollection projections = new ProjectionCollection();
    projections.add( new ProjectionAttribute( "giftCode" ) );
    assertNotNull( testMerchOrderServiceImpl.getMerchOrderByIdWithProjections( new Long( 0 ), projections ) );

  }

}
