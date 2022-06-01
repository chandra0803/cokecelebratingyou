/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/taglib/AuthorizeTag.java,v $
 */

package com.biperf.core.ui.taglib;

import java.util.Collections;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.springframework.util.StringUtils;

import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.ServiceLocator;

/**
 * AuthorizeTag.
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
 * <td>Oct 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuthorizeTag extends TagSupport
{
  private static final Log log = LogFactory.getLog( AuthorizeTag.class );

  // ~ Instance fields ========================================================

  private String ifAllGrantedExpression = "";
  private String ifAnyGrantedExpression = "";
  private String ifNotGrantedExpression = "";

  // ~ Methods ================================================================

  public void setIfAllGranted( String ifAllGrantedExpression )
  {
    this.ifAllGrantedExpression = ifAllGrantedExpression;
  }

  public void setIfAnyGranted( String ifAnyGrantedExpression )
  {
    this.ifAnyGrantedExpression = ifAnyGrantedExpression;
  }

  public void setIfNotGranted( String ifNotGrantedExpression )
  {
    this.ifNotGrantedExpression = ifNotGrantedExpression;
  }

  /**
   * Overridden from Calls AuthorizationService's isUserInRole to quick determination of user having
   * the specified role.
   * 
   * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
   * @return status code
   * @throws JspException
   */
  public int doStartTag() throws JspException
  {
    // Expression Language (EL) expressions must be evaluated in doStartTag(), and not in attribute
    // setter methods, because servlet containers can reuse tags, and if a tag attribute is a
    // string literal, the setter method might not be called every time the tag is encountered.

    String ifAllGranted = (String)ExpressionEvaluatorManager.evaluate( "ifAllGranted", ifAllGrantedExpression, java.lang.String.class, this, pageContext );
    String ifAnyGranted = (String)ExpressionEvaluatorManager.evaluate( "ifAnyGranted", ifAnyGrantedExpression, java.lang.String.class, this, pageContext );
    String ifNotGranted = (String)ExpressionEvaluatorManager.evaluate( "ifNotGranted", ifNotGrantedExpression, java.lang.String.class, this, pageContext );

    boolean isRoleGranted = false;
    try
    {

      if ( ( null == ifAllGranted || "".equals( ifAllGranted ) ) && ( null == ifAnyGranted || "".equals( ifAnyGranted ) ) && ( null == ifNotGranted || "".equals( ifNotGranted ) ) )
      {
        return Tag.SKIP_BODY;
      }

      Set setAllGranted = parseAuthoritiesString( ifAllGranted );
      Set setAnyGranted = parseAuthoritiesString( ifAnyGranted );
      Set setNoneGranted = parseAuthoritiesString( ifNotGranted );

      isRoleGranted = getAuthorizationService().isUserInRole( setAllGranted, setAnyGranted, setNoneGranted );
    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
      throw new JspException( "doStartTag: Exception", e );
    }
    if ( isRoleGranted )
    {
      return Tag.EVAL_BODY_INCLUDE;
    }

    return Tag.SKIP_BODY;
  }

  /**
   * This mrthod is to parse the given CSV string into a set.
   * 
   * @param authorizationsString
   * @return the set which has roles in it.
   */
  private Set parseAuthoritiesString( String authorizationsString )
  {

    if ( null == authorizationsString || "".equals( authorizationsString ) )
    {
      return Collections.EMPTY_SET;
    }

    return StringUtils.commaDelimitedListToSet( authorizationsString );

  }

  /**
   * Returns the AuthorizationService
   * 
   * @return the AuthorizationService
   */
  private AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)ServiceLocator.getService( AuthorizationService.BEAN_NAME );
  }
}
