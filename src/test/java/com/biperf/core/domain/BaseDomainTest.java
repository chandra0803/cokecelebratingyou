/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javatest/com/biperf/core/domain/BaseDomainTest.java,v $
 */

package com.biperf.core.domain;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.employer.Employer;
import com.biperf.core.exception.BeaconRuntimeException;

import junit.framework.TestCase;

/**
 * BaseDomainTest <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>tennant</td>
 * <td>Jun 13, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class BaseDomainTest extends TestCase
{

  public void testGetBusinessObjectWithIdForListWithNullList()
  {
    try
    {
      BaseDomain.getBusinessObjectWithId( null, new Long( 1 ) );
      fail( "should have thrown an exception" );
    }
    catch( BeaconRuntimeException e )
    {
      // do nothing, success
    }
  }

  public void testGetBusinessObjectWithIdForListWithNullId()
  {

    List list = new ArrayList();
    // just pick any domain object
    Employer item1 = new Employer();
    item1.setId( new Long( 1 ) );
    try
    {
      BaseDomain.getBusinessObjectWithId( list, null );
      fail( "should have thrown an exception" );
    }
    catch( BeaconRuntimeException e )
    {
      // do nothing, success
    }

  }

  public void testGetBusinessObjectWithIdForList()
  {

    List list = new ArrayList();
    // just pick any domain object
    Employer item1 = new Employer();
    item1.setId( new Long( 1 ) );
    Employer item2 = new Employer();
    item2.setId( new Long( 2 ) );

    list.add( item1 );
    list.add( item2 );

    BaseDomain found = BaseDomain.getBusinessObjectWithId( list, new Long( 2 ) );
    assertSame( found, item2 );

    try
    {
      BaseDomain.getBusinessObjectWithId( list, new Long( 3 ) );
      fail( "should have thrown an exception" );
    }
    catch( BeaconRuntimeException e )
    {
      // nothing, success
    }

  }

}
