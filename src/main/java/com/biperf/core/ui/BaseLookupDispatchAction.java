/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/BaseLookupDispatchAction.java,v $
 */

package com.biperf.core.ui;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.actions.LookupDispatchAction;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * BaseLookupDispatchAction to be extended for all LookupDispatchActions.
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
 * <td>waldal</td>
 * <td>Jan 14, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class BaseLookupDispatchAction extends LookupDispatchAction
{

  private BeanFactory beanFactory;

  /*
   * (non-Javadoc)
   * @see org.apache.struts.actions.Action#setServlet()
   */
  public void setServlet( ActionServlet actionServlet )
  {
    super.setServlet( actionServlet );
    if ( actionServlet != null )
    {
      ServletContext servletContext = actionServlet.getServletContext();
      beanFactory = WebApplicationContextUtils.getRequiredWebApplicationContext( servletContext );

    }
  }

  protected Object getBean( String beanName )
  {
    return beanFactory.getBean( beanName );
  }

  protected Map getKeyMethodMap()
  {
    return Collections.EMPTY_MAP;
  }
}
