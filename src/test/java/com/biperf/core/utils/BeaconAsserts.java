/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.utils;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Assert;

import com.biperf.core.exception.BeaconRuntimeException;

/**
 * Holds Asserts used by various test base classes.
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
 * <td>wadzinsk</td>
 * <td>Mar 21, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BeaconAsserts
{
  /**
   * Assert succeeds if the expectedObject is found as a property propertyName of one of the members
   * of actualCollection. <br/>For example, if you want to assert that a particular needle (needleA)
   * exists in a collection of Haystack objects (hayStacks), and haystack.getNeedle() exists, then
   * you would call assertContains(needleA, hayStacks, "needle"). This works for asserting deeper
   * objects as well ("needle.color").
   */
  public static void assertContains( Object expectedObject, Collection actualCollection, String propertyName )
  {
    boolean matchFound = false;

    for ( Iterator iter = actualCollection.iterator(); iter.hasNext(); )
    {
      Object object = iter.next();
      try
      {
        Object actualProperty = PropertyUtils.getProperty( object, propertyName );
        if ( actualProperty == null )
        {
          Assert.fail( "No property found in actualCollection. Property Name: " + propertyName );
        }
        if ( actualProperty.equals( expectedObject ) )
        {
          matchFound = true;
          break;
        }
      }
      catch( Exception e )
      {
        throw new BeaconRuntimeException( "Exception accessing property:" + propertyName, e );
      }

    }

    if ( !matchFound )
    {
      Assert.fail( "expectedObject does not match any member of actualCollection using property (" + propertyName + ")" );
    }
  }
}
