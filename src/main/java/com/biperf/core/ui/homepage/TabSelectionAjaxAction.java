
package com.biperf.core.ui.homepage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.ui.BaseDispatchAction;

/**
 * TabSelectionAjaxAction.
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
 * <td>Dec. 14, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class TabSelectionAjaxAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( TabSelectionAjaxAction.class );

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    String tabid = (String)request.getParameter( "tabid" );
    logger.debug( "tabid=" + tabid );
    if ( tabid != null )
    {
      request.getSession().setAttribute( "selectedTabId", tabid );
    }
    return null;
  }

}
