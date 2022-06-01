/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/taglib/AddressDisplayTag.java,v $
 */

package com.biperf.core.ui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import com.biperf.core.domain.enums.StateType;
import com.biperf.core.utils.AddressUtil;
import com.biperf.util.StringUtils;

/**
 * AddressDisplayTag this can be used to format address from pipe delimted address string.
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
 * <td>Ashok Attada</td>
 * <td>Nov 17, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * </p>
 * 
 *
 */

public class AddressDisplayTag extends BodyTagSupport
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String address;

  /**
   * A JSTL expression whose value is the name of this client state entry.
   */
  private String addressExpression;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------
  /**
   * Sets the name of this client state entry.
   * 
   * @param addressExpression a JSTL expression whose value is the name of this client state entry.
   */
  public void setAddress( String addressExpression )
  {
    this.addressExpression = addressExpression;
  }

  // ---------------------------------------------------------------------------
  // Tag Methods
  // ---------------------------------------------------------------------------

  /**
   * Processes the userId tag of the "user name" HTML element.
   * 
   * @return SKIP_BODY
   * @throws JspException if an exception occurs while evaluating a JSTL expression.
   */
  public int doStartTag() throws JspException
  {
    // Expression Language (EL) expressions must be evaluated in doStartTag(),
    // and not in attribute setter methods, because servlet containers can
    // reuse tags, and if a tag attribute is a string literal, the setter
    // method might not be called every time the tag is encountered.

    address = (String)ExpressionEvaluatorManager.evaluate( "address", addressExpression, java.lang.String.class, this, pageContext );
    return SKIP_BODY;
  }

  /**
   * Prints username element.
   * 
   * @return SKIP_BODY
   * @throws JspTagException if an input/output exception occurs while generating the HTML
   *           representation of the client state.
   */
  public int doEndTag() throws JspTagException
  {
    String ADRESS_DELIMITER = "|";
    try
    {

      if ( address != null && !"".equals( address ) )
      {

        StringBuffer buffer = new StringBuffer();

        // Addr1

        String addr1 = address.substring( 0, address.indexOf( ADRESS_DELIMITER ) );
        String addr2 = address.substring( address.indexOf( ADRESS_DELIMITER ) + 1, address.length() );
        String addr3 = addr2.substring( addr2.indexOf( ADRESS_DELIMITER ) + 1, addr2.length() );
        String addr4 = addr3.substring( addr3.indexOf( ADRESS_DELIMITER ) + 1, addr3.length() );
        String addr5 = addr4.substring( addr4.indexOf( ADRESS_DELIMITER ) + 1, addr4.length() );
        String addr6 = addr5.substring( addr5.indexOf( ADRESS_DELIMITER ) + 1, addr5.length() );
        String addr7 = addr6.substring( addr6.indexOf( ADRESS_DELIMITER ) + 1, addr6.length() );
        String stateCode = addr5.substring( 0, addr5.indexOf( ADRESS_DELIMITER ) );
        String countryCode = addr7.substring( 0, addr7.indexOf( ADRESS_DELIMITER ) );
        if ( !StringUtils.isEmpty( countryCode ) )
        {
          // this is to get state informatiom
          if ( isNorthAmerican( countryCode ) && !StringUtils.isEmpty( stateCode ) )
          {
            stateCode = StateType.lookup( stateCode ).getName();
          }

        }

        buffer.append( addr1 ).append( " " );
        buffer.append( addr2.substring( 0, addr2.indexOf( ADRESS_DELIMITER ) ) ).append( " " );
        buffer.append( addr3.substring( 0, addr3.indexOf( ADRESS_DELIMITER ) ) ).append( " " );
        buffer.append( addr4.substring( 0, addr4.indexOf( ADRESS_DELIMITER ) ) ).append( " " );
        buffer.append( stateCode ).append( " " );
        buffer.append( addr6.substring( 0, addr6.indexOf( ADRESS_DELIMITER ) ) ).append( " " );
        buffer.append( addr7.substring( 0, addr7.indexOf( ADRESS_DELIMITER ) ) );

        JspWriter out = pageContext.getOut();
        out.print( buffer.toString() );
      }
    }
    catch( IOException e )
    {
      throw (JspTagException)new JspTagException().initCause( e );
    }

    return SKIP_BODY;
  }

  /**
   * Checks the country code to determine if it is North American or not
   * 
   * @return boolean - true if Country is NorthAmerican - false if Country is not NorthAmerican
   */

  private boolean isNorthAmerican( String countryCode )
  {
    return AddressUtil.isNorthAmerican( countryCode );
  } // end isNorthAmerican

}
