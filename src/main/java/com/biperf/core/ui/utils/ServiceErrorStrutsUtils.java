/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/utils/ServiceErrorStrutsUtils.java,v $
 */

package com.biperf.core.ui.utils;

import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.service.util.ServiceError;

/**
 * ServiceErrorStrutsUtils.
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
public class ServiceErrorStrutsUtils
{

  /**
   * Takes in a list of ServiceErrors, and populates the ActionMessages with struts ActionErrors
   * 
   * @param serviceErrors
   * @param errors
   */
  public static void convertServiceErrorsToActionErrors( List serviceErrors, ActionMessages errors )
  {
    // For each ServiceError, convert it to an ActionError, and add it to the ActionMessages object
    for ( Iterator iter = serviceErrors.iterator(); iter.hasNext(); )
    {
      ServiceError error = (ServiceError)iter.next();

      ActionMessage actionMessage = null;

      if ( error.getNumberOfArgs() > 0 )
      {
        actionMessage = new ActionMessage( error.getKey(), error.getArgs() );
      }
      else
      {
        actionMessage = new ActionMessage( error.getKey() );
      }

      errors.add( ActionMessages.GLOBAL_MESSAGE, actionMessage );
    }

  }

}
