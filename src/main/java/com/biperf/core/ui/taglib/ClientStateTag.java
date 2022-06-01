/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/taglib/ClientStateTag.java,v $
 */

package com.biperf.core.ui.taglib;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;

/*
 * ClientStateTag <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct 7, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ClientStateTag extends BodyTagSupport
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * A map of client state entries.
   */
  private Map clientStateMap = new HashMap();

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Adds a client state entry to the map of client state entries.
   * 
   * @param name the name of the client state entry.
   * @param value the value of the client state entry.
   * @throws JspTagException if an exception occurs while adding a client state entry.
   */
  public void addEntry( String name, Serializable value ) throws JspTagException
  {
    try
    {
      clientStateMap.put( name, value );
    }
    catch( Exception e )
    {
      throw (JspTagException)new JspTagException().initCause( e );
    }
  }

  // ---------------------------------------------------------------------------
  // Tag Methods
  // ---------------------------------------------------------------------------

  /**
   * Processes the end tag of the "client state" HTML element.
   * 
   * @return SKIP_BODY
   * @throws JspTagException if an input/output exception occurs while generating the HTML
   *           representation of the client state.
   */
  public int doEndTag() throws JspTagException
  {
    // Serialize the client state.
    String password = ClientStatePasswordManager.getPassword();
    String clientState = ClientStateSerializer.serialize( clientStateMap, password );

    // Output the client state.
    try
    {
      StringBuffer buffer = new StringBuffer();
      buffer.append( "<input name='clientState' type='hidden' value='" ).append( clientState ).append( "' />" );

      JspWriter out = pageContext.getOut();
      out.print( buffer.toString() );
    }
    catch( IOException e )
    {
      throw (JspTagException)new JspTagException().initCause( e );
    }

    return SKIP_BODY;
  }
}
