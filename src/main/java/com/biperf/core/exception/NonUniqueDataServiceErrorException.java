/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/exception/NonUniqueDataServiceErrorException.java,v $
 *
 */

package com.biperf.core.exception;

import java.util.List;

/**
 * NonUniqueDataServiceErrorException <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Sep 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class NonUniqueDataServiceErrorException extends ServiceErrorExceptionWithRollback
{
  /**
   * Construct a NonUniqueDataServiceErrorException with a list of error messages.
   * 
   * @param serviceErrors a list of ServiceError objects
   */
  public NonUniqueDataServiceErrorException( List serviceErrors )
  {
    super( serviceErrors );
  }

  /**
   * Construct a NonUniqueDataServiceErrorException with a chained exception.
   * 
   * @param serviceErrors
   * @param throwable
   */
  public NonUniqueDataServiceErrorException( List serviceErrors, Throwable throwable )
  {
    super( serviceErrors, throwable );
  }

  /**
   * Overloaded constructor for creating a new NonUniqueDataServiceErrorException with a new list
   * that contains one ServiceError created with the serviceErrorMsg.
   * 
   * @param serviceErrorMsg
   */
  public NonUniqueDataServiceErrorException( String serviceErrorMsg )
  {
    super( serviceErrorMsg );
  }

  /**
   * Overloaded constructor for creating a new NonUniqueDataServiceErrorException with a error
   * message and chained exception.
   * 
   * @param serviceErrorMsg
   * @param throwable
   */
  public NonUniqueDataServiceErrorException( String serviceErrorMsg, Throwable throwable )
  {
    super( serviceErrorMsg, throwable );
  }
}
