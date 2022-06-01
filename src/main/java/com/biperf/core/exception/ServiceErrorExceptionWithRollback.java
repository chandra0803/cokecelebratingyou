/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/exception/ServiceErrorExceptionWithRollback.java,v $
 */

package com.biperf.core.exception;

import java.util.List;

import com.biperf.core.service.util.ServiceError;

/**
 * This is the same as a ServiceErrorException, except when thrown, this exception indicates to
 * Spring that the current transaction should be rolled back.
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
 * <td>tennant</td>
 * <td>May 09, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ServiceErrorExceptionWithRollback extends ServiceErrorException
{

  /**
   * @param serviceErrors a list of ServiceError objects
   */
  public ServiceErrorExceptionWithRollback( List serviceErrors )
  {
    super( serviceErrors );
  }

  /**
   * @return the list of ServiceErrors
   */
  public List getServiceErrors()
  {
    return super.getServiceErrors();
  }

  /**
   * Overloaded constructor for creating a new ServiceErrorException with a error message and
   * chained exception.
   * 
   * @param serviceErrorMsg
   * @param throwable
   */
  public ServiceErrorExceptionWithRollback( String serviceErrorMsg, Throwable throwable )
  {
    super( serviceErrorMsg, throwable );
  }

  /**
   * Construct a ServiceErrorException with a chained exception.
   * 
   * @param serviceErrors
   * @param throwable
   */
  public ServiceErrorExceptionWithRollback( List serviceErrors, Throwable throwable )
  {
    super( serviceErrors, throwable );
  }

  /**
   * Overloaded constructor for creating a new ServiceErrorExceptionWithRollback with a new list
   * that contains one ServiceError created with the serviceErrorMsg.
   * 
   * @param serviceErrorMsg
   */
  public ServiceErrorExceptionWithRollback( String serviceErrorMsg )
  {
    super( serviceErrorMsg );
  }

  /**
   * Overloaded constructor for creating a new ServiceErrorExceptionWithRollback with a
   * ServiceError.
   * 
   * @param serviceError
   */
  public ServiceErrorExceptionWithRollback( ServiceError serviceError )
  {
    super( serviceError );
  }

}
