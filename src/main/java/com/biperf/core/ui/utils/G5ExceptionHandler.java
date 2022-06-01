
package com.biperf.core.ui.utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

public class G5ExceptionHandler extends ExceptionHandler
{
  protected final Log logger = LogFactory.getLog( this.getClass() );

  @Override
  public ActionForward execute( Exception ex, ExceptionConfig ae, ActionMapping mapping, ActionForm formInstance, HttpServletRequest request, HttpServletResponse response ) throws ServletException
  {

    // log the error message
    logger.error( "Global Exception: ", ex );

    return super.execute( ex, ae, mapping, formInstance, request, response );
  }

}
