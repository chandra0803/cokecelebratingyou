/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/taglib/UserNameTag.java,v $
 */

package com.biperf.core.ui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.ServiceLocator;

/*
 * ClientStateTag <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th> <th>Date</th>
 * <th>Version</th> <th>Comments</th> </tr> <tr> <td>Ashok Attada</td> <td>Nov 17, 2005</td>
 * <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class UserNameTag extends BodyTagSupport
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  private String userId;

  /**
   * A JSTL expression whose value is the name of this client state entry.
   */
  private String userIdExpression;

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------
  /**
   * Sets the name of this client state entry.
   * 
   * @param userIdExpression a JSTL expression whose value is the name of this client state entry.
   */
  public void setUserId( String userIdExpression )
  {
    this.userIdExpression = userIdExpression;
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

    userId = (String)ExpressionEvaluatorManager.evaluate( "userId", userIdExpression, java.lang.String.class, this, pageContext );
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
    try
    {

      User user = null;

      if ( userId != null && !"".equals( userId ) )
      {
        user = getUserService().getUserById( new Long( userId ) );
      }

      if ( user != null )
      {
        StringBuffer buffer = new StringBuffer();
        buffer.append( "<span class=" + "subheadline" + ">" );

        if ( user.getTitleType() != null )
        {
          if ( user.getTitleType().getName() != null && !"".equals( user.getTitleType().getName() ) )
          {
            buffer.append( user.getTitleType().getName() ).append( " " );
          }
        }
        if ( user.getFirstName() != null && !"".equals( user.getFirstName() ) )
        {
          buffer.append( user.getFirstName() ).append( " " );
        }

        if ( user.getMiddleName() != null && !"".equals( user.getMiddleName() ) )
        {
          buffer.append( user.getMiddleName() ).append( " " );
        }

        if ( user.getLastName() != null && !"".equals( user.getLastName() ) )
        {
          buffer.append( user.getLastName() ).append( " " );
        }

        if ( user.getSuffixType() != null )
        {
          if ( user.getSuffixType().getName() != null && !"".equals( user.getSuffixType().getName() ) )
          {
            buffer.append( user.getSuffixType().getName() );
          }
        }
        buffer.append( " </span>" );

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
   * Get the userService from the BeanFactory.
   * 
   * @return UserService
   */
  private UserService getUserService()
  {
    return (UserService)ServiceLocator.getService( UserService.BEAN_NAME );
  }
}
