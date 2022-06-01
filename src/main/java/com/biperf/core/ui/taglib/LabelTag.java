/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/taglib/LabelTag.java,v $
 */

package com.biperf.core.ui.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessages;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.springframework.web.util.ExpressionEvaluationUtils;

/**
 * LabelTag.
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
 * <td>robinsra</td>
 * <td>Oct 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class LabelTag extends BodyTagSupport
{
  private static final Log logger = LogFactory.getLog( LabelTag.class );
  private String property;
  private String styleClass;
  private String required;
  private String colspan;
  private String labelColumnWidth;
  private String requiredColumnWidth;

  public void setLabelColumnWidth( String labelColumnWidth )
  {
    this.labelColumnWidth = labelColumnWidth;
  }

  public void setRequiredColumnWidth( String requiredColumnWidth )
  {
    this.requiredColumnWidth = requiredColumnWidth;
  }

  public void setRequired( String required )
  {
    this.required = required;
  }

  public void setProperty( String property ) throws JspException
  {
    this.property = (String)ExpressionEvaluatorManager.evaluate( "property", property, java.lang.String.class, this, pageContext );
  }

  public void setStyleClass( String styleClass )
  {
    this.styleClass = styleClass;
  }

  public void setColspan( String colspan )
  {
    this.colspan = colspan;
  }

  public int doStartTag() throws JspTagException
  {
    return EVAL_BODY_BUFFERED;
  }

  public int doEndTag() throws JspTagException
  {
    JspWriter out = getPreviousOut();
    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

    // -----------------------------
    // Check if error for property
    // -----------------------------
    boolean isErrors = false;
    if ( property != null && !property.equals( "" ) )
    {
      ActionMessages errors = (ActionMessages)request.getAttribute( Globals.ERROR_KEY );
      if ( errors != null && !errors.isEmpty() )
      {
        if ( errors.size( property ) > 0 )
        {
          isErrors = true;
        }
      }
    }

    boolean isRequired = false;
    try
    {
      if ( required != null && !"".equals( required ) )
      {
        isRequired = ExpressionEvaluationUtils.evaluateBoolean( "required", required, pageContext );
      }
    }
    catch( JspException e )
    {
      isRequired = false;
    }

    // -------------------------
    // Setup the requiredClass
    // -------------------------
    String requiredClass = "";
    if ( isRequired || styleClass != null && !styleClass.equals( "" ) )
    {
      requiredClass = " class=\"";
      if ( isRequired )
      {
        requiredClass = requiredClass + "content-field-label-req";
      }
      if ( styleClass != null && !styleClass.equals( "" ) )
      {
        requiredClass = requiredClass + " " + styleClass;
      }
      if ( isErrors )
      {
        requiredClass = requiredClass + " content-error";
      }
      requiredClass = requiredClass + "\"";
    }

    // -------------------------
    // Setup the Label Class
    // -------------------------
    String labelClass = " class=\"content-field-label";
    if ( styleClass != null && !styleClass.equals( "" ) )
    {
      labelClass = labelClass + " " + styleClass;
    }
    if ( isErrors )
    {
      labelClass = labelClass + " content-error";
    }
    labelClass = labelClass + "\"";

    // -------------------------
    // Setup the Column Span
    // -------------------------
    String colspanAttribute = "";
    if ( colspan != null && colspan.length() > 0 )
    {
      colspanAttribute = new StringBuffer().append( " colspan=\"" ).append( colspan ).append( "\"" ).toString();
    }

    // -------------------------
    // Setup the required column width
    // -------------------------
    String requiredWidth = "";
    if ( requiredColumnWidth != null && requiredColumnWidth.length() > 0 )
    {
      requiredWidth = new StringBuffer().append( " width=\"" ).append( requiredColumnWidth ).append( "\"" ).toString();
    }

    // -------------------------
    // Setup the Column Span
    // -------------------------
    String labelWidth = "";
    if ( labelColumnWidth != null && labelColumnWidth.length() > 0 )
    {
      labelWidth = new StringBuffer().append( " width=\"" ).append( labelColumnWidth ).append( "\"" ).toString();
    }

    // ----------------
    // Write out tags
    // ----------------
    try
    {
      // Required td
      out.println( "<td" + requiredClass + requiredWidth + ">" );
      if ( isRequired )
      {
        out.println( "*" );
      }
      out.println( "</td>" );

      // label td
      out.println( "<td" + labelClass + colspanAttribute + labelWidth + ">" );
      bodyContent.writeOut( out );
      out.println( "</td>" );
    }
    catch( IOException e )
    {
      logger.error( e.getMessage(), e );
      throw (JspTagException)new JspTagException().initCause( e );
    }
    return SKIP_BODY;
  }
}
