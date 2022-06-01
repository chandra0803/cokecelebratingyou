/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.exception;

import java.util.List;

import com.biperf.core.service.util.ServiceError;

/**
 * This form of ServiceException should only be used for services methods that don't want a
 * transaction rollback. It should rarely be used since almost always we want a rollback when a
 * service fails. An examples for using this Exception is in a mailing service where email send
 * fails but we still want to save a audit record.
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
public class ServiceErrorExceptionNoRollback extends ServiceErrorException
{

  /**
   * @param serviceErrors a list of ServiceError objects
   */
  public ServiceErrorExceptionNoRollback( List serviceErrors )
  {
    super( serviceErrors );
  }

  public ServiceErrorExceptionNoRollback( ServiceError serviceError )
  {
    super( serviceError );
  }

  public ServiceErrorExceptionNoRollback( ServiceError serviceError, Throwable throwable )
  {
    super( serviceError, throwable );
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
  public ServiceErrorExceptionNoRollback( String serviceErrorMsg, Throwable throwable )
  {
    super( serviceErrorMsg, throwable );
  }

  /**
   * Construct a ServiceErrorException with a chained exception.
   * 
   * @param serviceErrors
   * @param throwable
   */
  public ServiceErrorExceptionNoRollback( List serviceErrors, Throwable throwable )
  {
    super( serviceErrors, throwable );
  }

  /**
   * Overloaded constructor for creating a new NonUniqueDataServiceErrorException with a new list
   * that contains one ServiceError created with the serviceErrorMsg.
   * 
   * @param serviceErrorMsg
   */
  public ServiceErrorExceptionNoRollback( String serviceErrorMsg )
  {
    super( serviceErrorMsg );
  }

}
