/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.domain.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.utils.ArrayUtil;

/**
 * BaseDomainUtils.
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
 * <td>Sep 5, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BaseDomainUtils
{

  /**
   * Protected since static only class
   */
  protected BaseDomainUtils()
  {
    super();
  }

  public static Map getDomainObjectsKeyedById( Collection domainObjects )
  {
    Map domainObjectsKeyedById = new HashMap();
    if ( domainObjects != null )
    {
      for ( Iterator iter = domainObjects.iterator(); iter.hasNext(); )
      {
        BaseDomain domainObject = (BaseDomain)iter.next();
        domainObjectsKeyedById.put( domainObject.getId(), domainObject );
      }
    }

    return domainObjectsKeyedById;
  }

  /**
   * Returns true if the id of any member of domainObjects equals the specified domainObjectId.
   */
  public static boolean containsId( Collection domainObjects, Long domainObjectId )
  {
    return getObjectById( domainObjects, domainObjectId ) != null;
  }

  /**
   * Retieve the domain object from domainObjects with the given id domainObjectId, or null if not
   * found.
   */
  public static BaseDomain getObjectById( Collection domainObjects, Long domainObjectId )
  {
    BaseDomain matchingDomainObject = null;

    if ( domainObjects != null )
    {
      for ( Iterator iter = domainObjects.iterator(); iter.hasNext(); )
      {
        BaseDomain domainObject = (BaseDomain)iter.next();
        if ( domainObject.getId().equals( domainObjectId ) )
        {
          matchingDomainObject = domainObject;
          break;
        }
      }
    }

    return matchingDomainObject;
  }

  /**
   * Return a list of Long ids extracted from each member of domainObjects
   */
  public static List toIds( Collection domainObjects )
  {
    List ids = new ArrayList();
    for ( Iterator iter = domainObjects.iterator(); iter.hasNext(); )
    {
      BaseDomain domainObject = (BaseDomain)iter.next();
      ids.add( domainObject.getId() );
    }
    return ids;
  }

  /**
   * Return a Long array of ids extracted from each member of domainObjects
   */
  public static Long[] toIdArray( Collection domainObjects )
  {
    List ids = toIds( domainObjects );

    return ArrayUtil.convertListToLongArray( ids );
  }
}
