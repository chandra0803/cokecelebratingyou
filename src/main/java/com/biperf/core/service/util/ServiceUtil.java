/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/util/ServiceUtil.java,v $
 */

package com.biperf.core.service.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.exception.ServiceErrorException;

/**
 * ServiceUtil - Utility class for common service tasks.
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
 * <td>Jun 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ServiceUtil
{

  /**
   * Protected since this is a static method-only Util class which doesn't need to be instantiated.
   */
  protected ServiceUtil()
  {
    super();

  }

  /**
   * Compare an unsaved Domain Object's id with an object (of the same type) from the database,
   * throwing a ServiceException if the objects' ids are not equal.
   * 
   * @param unsavedObject
   * @param dbObject
   * @param duplicateMessageKey
   * @throws ServiceErrorException
   */
  public static void checkEqualsId( BaseDomain unsavedObject, BaseDomain dbObject, String duplicateMessageKey ) throws ServiceErrorException
  {
    if ( dbObject != null && !dbObject.equalsId( unsavedObject ) )
    {
      List serviceErrors = new ArrayList();
      ServiceError error = new ServiceError( duplicateMessageKey );
      serviceErrors.add( error );
      throw new ServiceErrorException( serviceErrors );
    }
  }

  /**
   * return empty list if it is null
   * 
   * @param unsavedObject
   * @return List
   */
  public static <T> Iterable<T> emptyIfNull( Iterable<T> iterable )
  {
    return iterable == null ? Collections.emptyList() : iterable;
  }
}
