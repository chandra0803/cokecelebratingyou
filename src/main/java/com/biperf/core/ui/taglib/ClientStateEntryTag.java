/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/taglib/ClientStateEntryTag.java,v $
 */

package com.biperf.core.ui.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

/*
 * ClientStateEntryTag <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>Thomas Eaton</td> <td>Oct
 * 7, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ClientStateEntryTag extends TagSupport
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The name of this client state entry.
   */
  private String name;

  /**
   * A JSTL expression whose value is the name of this client state entry.
   */
  private String nameExpression;

  /**
   * The value of this client state entry.
   */
  private String value;

  /**
   * A JSTL expression whose value is the value of this client state entry.
   */
  private String valueExpression;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Sets the name of this client state entry.
   * 
   * @param nameExpression a JSTL expression whose value is the name of this client state entry.
   */
  public void setName( String nameExpression )
  {
    this.nameExpression = nameExpression;
  }

  /**
   * Sets the value of this client state entry.
   * 
   * @param valueExpression a JSTL expression whose value is the value of this client state entry.
   */
  public void setValue( String valueExpression )
  {
    this.valueExpression = valueExpression;
  }

  // ---------------------------------------------------------------------------
  // Tag Methods
  // ---------------------------------------------------------------------------

  /**
   * Processes the start tag of the "client state entry" HTML element.
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

    name = (String)ExpressionEvaluatorManager.evaluate( "name", nameExpression, java.lang.String.class, this, pageContext );
    value = (String)ExpressionEvaluatorManager.evaluate( "value", valueExpression, java.lang.String.class, this, pageContext );
    return SKIP_BODY;
  }

  /**
   * Processes the end tag of the "client state entry" HTML element.
   * 
   * @return EVAL_PAGE
   * @throws JspTagException if this client-state-entry tag is not enclosed by a client-state tag.
   */
  public int doEndTag() throws JspTagException
  {
    // Get the enclosing client-state tag.
    Tag parent = findAncestorWithClass( this, ClientStateTag.class );
    if ( parent == null )
    {
      throw new JspTagException( "The client-state-entry tag is not enclosed by a client-state tag" );
    }
    ClientStateTag clientStateTag = (ClientStateTag)parent;

    // Add this client state entry to the client state.
    clientStateTag.addEntry( name, value );

    return EVAL_PAGE;
  }
}
