/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/taglib/AclTag.java,v $
 */

package com.biperf.core.ui.taglib;

import java.util.StringTokenizer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.util.ExpressionEvaluationUtils;

import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.utils.ServiceLocator;

/**
 * MenuTag
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
/**
 * AclTag
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
 * <td>attada</td>
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
/**
 * AclTag.
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
 * <td>attada</td>
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AclTag extends TagSupport
{

  // ~ Static fields/initializers =============================================

  protected static final Log log = LogFactory.getLog( AclTag.class );

  // ~ Instance fields ========================================================
  private String code;
  private Object domainObject;
  private String hasPermission = "";

  // ~ Methods ================================================================
  /**
   * @return code
   */
  public String getCode()
  {
    return code;
  }

  /**
   * @param code
   */
  public void setCode( String code )
  {
    this.code = code;
  }

  /**
   * @param domainObject
   */
  public void setDomainObject( Object domainObject )
  {
    this.domainObject = domainObject;
  }

  /**
   * @return domainObject
   */
  public Object getDomainObject()
  {
    return domainObject;
  }

  /**
   * @param hasPermission
   */
  public void setHasPermission( String hasPermission )
  {
    this.hasPermission = hasPermission;
  }

  /**
   * @return hasPermission
   */
  public String getHasPermission()
  {
    return hasPermission;
  }

  /**
   * Overridden from Calls AuthorizationService's hasPermission to quick determination of user
   * having the access for specified object.
   * 
   * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
   * @return status code
   * @throws JspException
   */
  public int doStartTag() throws JspException
  {
    boolean isAclGranted = false;
    try
    {

      if ( null == code || "".equals( code ) )
      {
        return Tag.SKIP_BODY;
      }

      if ( null == hasPermission || "".equals( hasPermission ) )
      {
        hasPermission = "0";
      }

      final String evaledPermissionsString = ExpressionEvaluationUtils.evaluateString( "hasPermission", hasPermission, pageContext );

      int totalMaskValue = 0;

      try
      {
        totalMaskValue = getCombinedMaskValue( evaledPermissionsString );
      }
      catch( NumberFormatException nfe )
      {
        throw new JspException( nfe );
      }

      Object resolvedDomainObject = null;

      if ( domainObject instanceof String )
      {
        resolvedDomainObject = ExpressionEvaluationUtils.evaluate( "domainObject", (String)domainObject, Object.class, pageContext );
      }
      else
      {
        resolvedDomainObject = domainObject;
      }

      if ( resolvedDomainObject == null )
      {
        if ( log.isDebugEnabled() )
        {
          log.debug( "domainObject resolved to null, so including tag body" );
        }

        // Of course they have access to a null object!
        return Tag.EVAL_BODY_INCLUDE;
      }

      // See if principal has any of the required permissions
      // need to check combined permission mask
      isAclGranted = getAuthorizationService().hasPermission( code, totalMaskValue, domainObject );
      if ( isAclGranted )
      {
        return Tag.EVAL_BODY_INCLUDE;
      }

    }
    catch( Exception e )
    {
      log.error( e.getMessage(), e );
      throw new JspException( "doStartTag: Exception", e );
    }

    if ( isAclGranted )
    {
      return Tag.EVAL_BODY_INCLUDE;
    }

    return Tag.SKIP_BODY;
  }

  /**
   * @param integersString
   * @return integer consisiting combined permission mask value
   * @throws NumberFormatException
   */

  private int getCombinedMaskValue( String integersString ) throws NumberFormatException
  {
    final StringTokenizer tokenizer;
    tokenizer = new StringTokenizer( integersString, ",", false );
    int totalMaskValue = 0;
    while ( tokenizer.hasMoreTokens() )
    {
      String integer = tokenizer.nextToken();
      totalMaskValue += new Integer( integer ).intValue();
    }

    return totalMaskValue;
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
