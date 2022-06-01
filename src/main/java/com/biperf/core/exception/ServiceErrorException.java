/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/exception/ServiceErrorException.java,v $
 */

package com.biperf.core.exception;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.service.util.ServiceError;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * This class is the base Exception class for any exception which is thrown by the service layer
 * which contains an error message the user will see. It includes things such as validation errors.
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
 * <td>Apr 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ServiceErrorException extends Exception
{

  // Contains ServiceError objects
  private List errors = new ArrayList();

  /**
   * @param serviceErrors a list of ServiceError objects
   */
  public ServiceErrorException( List serviceErrors )
  {
    errors = serviceErrors;
  }

  /**
   * Construct a ServiceErrorException with a chained exception.
   * 
   * @param serviceErrors
   * @param throwable
   */
  public ServiceErrorException( List serviceErrors, Throwable throwable )
  {
    super( throwable );
    errors = serviceErrors;
  }

  /**
   * Overloaded constructor for creating a new ServiceErrorException with a new list that contains
   * one ServiceError created with the serviceErrorMsg.
   * 
   * @param serviceErrorMsg
   */
  public ServiceErrorException( String serviceErrorMsg )
  {
    errors.add( new ServiceError( serviceErrorMsg ) );
  }

  /**
   * Overloaded constructor for creating a new ServiceErrorException with a ServiceError.
   * 
   * @param serviceError
   */
  public ServiceErrorException( ServiceError serviceError )
  {
    errors.add( serviceError );
  }

  /**
   * Overloaded constructor for creating a new ServiceErrorException with a error message and
   * chained exception.
   * 
   * @param serviceErrorMsg
   * @param throwable
   */
  public ServiceErrorException( String serviceErrorMsg, Throwable throwable )
  {
    super( throwable );
    errors.add( new ServiceError( serviceErrorMsg ) );
  }

  /**
   * Overloaded constructor for creating a new ServiceErrorException with a ServiceError and chained
   * exception.
   * 
   * @param serviceError
   * @param throwable
   */
  public ServiceErrorException( ServiceError serviceError, Throwable throwable )
  {
    super( throwable );
    errors.add( serviceError );
  }

  /**
   * @return the list of ServiceErrors
   */
  public List getServiceErrors()
  {
    return errors;
  }

  public List<String> getServiceErrorsCMText()
  {
    CmsResourceBundle cmsResourceBundle = CmsResourceBundle.getCmsBundle();

    List serviceErrorsCMText = new ArrayList();
    for ( Iterator iter = errors.iterator(); iter.hasNext(); )
    {
      ServiceError error = (ServiceError)iter.next();

      String entireAssetKey = error.getKey();
      if ( entireAssetKey.lastIndexOf( "." ) > 1 )
      {
        String asset = entireAssetKey.substring( 0, entireAssetKey.lastIndexOf( "." ) );
        String key = entireAssetKey.substring( entireAssetKey.lastIndexOf( "." ) + 1 );

        String cmString = cmsResourceBundle.getString( asset, key );

        if ( cmString != null )
        {
          if ( error.getArgs() != null && error.getArgs().length > 0 )
          {
            cmString = MessageFormat.format( cmString, (Object[])error.getArgs() );
          }
          serviceErrorsCMText.add( cmString );
        }
      }
    }
    return serviceErrorsCMText;
  }
}
