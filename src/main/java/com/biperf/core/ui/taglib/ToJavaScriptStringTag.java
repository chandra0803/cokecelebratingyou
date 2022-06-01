
package com.biperf.core.ui.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ToJavaScriptStringTag.
 * 
 *
 */
public class ToJavaScriptStringTag extends BodyTagSupport
{
  private static final Log logger = LogFactory.getLog( ToJavaScriptStringTag.class );

  public int doEndTag() throws JspTagException
  {
    String body = bodyContent.getString();
    body = body.replaceAll( "\\'", "\\\\'" );
    body = body.replaceAll( "\\%2F", "\\\\'" );
    body = body.replaceAll( "&#039;", "\\\\'" );
    body = body.replaceAll( "\\n", "<br>" );
    body = body.replaceAll( "\\r", "<br>" );

    try
    {
      pageContext.getOut().print( body );
    }
    catch( IOException e )
    {
      logger.error( e.getMessage(), e );
      throw (JspTagException)new JspTagException().initCause( e );
    }

    return SKIP_BODY;
  }
}
