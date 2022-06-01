/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/utils/ActionUtils.java,v $
 */

package com.biperf.core.ui.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * ActionUtils.
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
 * <td>zahler</td>
 * <td>Apr 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class ActionUtils
{

  /**
   * Updates ActionForward object with URL parameters.
   * 
   * @param actionMapping action mapping object
   * @param forwardName mapping name
   * @param urlParams array of "key=value" strings which should be added to actionForward path as
   *          HTTP GET parameters
   * @return modified ActionForward object with updated GET parameters
   */
  public static ActionForward forwardWithParameters( ActionMapping actionMapping, String forwardName, String[] urlParams )
  {
    /*
     * Find ActionForward object, defined in struts-config.xml
     */
    ActionForward actionForward = actionMapping.findForward( forwardName );
    if ( actionForward == null )
    {
      return null;
    }

    /*
     * Do not use URL modification on forward, ActionForm fields should be used instead.
     */
    if ( !actionForward.getRedirect() )
    {
      return actionForward;
    }

    /*
     * Build URL parameters necessary on redirect because HTTPRequest object will be destroyed,
     * ActionForm fields can be reset as well if form scope is "request".
     */
    String actionPath = actionForward.getPath();
    actionPath = addParameters( actionPath, urlParams );
    /*
     * Create new ActionForward object. Stuts does not allow to modify ActionForward objects,
     * statically defined in struts-config.xml
     */
    ActionForward actionRedirect = new ActionForward( actionForward.getName(), actionPath, true /* REDIRECT */
    );

    actionRedirect.setModule( actionForward.getModule() );
    return actionRedirect;
  }

  /**
   * Retrieves the URI for the specified action forward and adds query parametes
   * 
   * @param request
   * @param forward
   * @param parameters array of "key=value" strings which should be added to actionForward path as
   *          HTTP GET parameters
   * @return String
   */
  public static String getForwardUriWithParameters( HttpServletRequest request, ActionForward forward, String[] parameters )
  {
    String forwardURI;

    String forwardPath = forward.getPath();

    // paths not starting with / should be passed through without any processing
    // (ie. they're absolute)
    if ( forwardPath.startsWith( "/" ) )
    {
      forwardURI = org.apache.struts.util.RequestUtils.forwardURL( request, forward, null ); // get
      // module
      // relative
      // uri
    }
    else
    {
      forwardURI = forwardPath;
    }

    if ( forward.getRedirect() )
    {
      // only prepend context path for relative uri
      if ( forwardURI.startsWith( "/" ) )
      {
        forwardURI = request.getContextPath() + forwardURI;
      }
    }

    forwardURI = ActionUtils.urlBuilder( forwardURI, parameters );

    return forwardURI;
  }

  public static String urlBuilder( String currentURL, String[] parameters )
  {
    String url = addParameters( currentURL, parameters );
    return url;
  }

  private static String addParameters( String url, String[] parameters )
  {
    if ( url != null )
    {
      for ( int i = 0; i < parameters.length; i++ )
      {
        if ( i == 0 )
        { // make sure we have a question mark before the parameters
          if ( !parameters[i].startsWith( "?" ) )
          {
            url += "?";
          }
          url += parameters[i];
        }
        else
        {
          url += "&";
          if ( parameters[i].startsWith( "?" ) )
          { // make sure we don't have extra question marks
            url += parameters[i].substring( 1 );
          }
          else
          {
            url += parameters[i];
          }
        }
      }
    }
    return url;
  }

}
