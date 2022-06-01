
package com.biperf.core.ui.participant;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

/**
 * QCardController.
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
 * <td>Dec. 28, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 *
 */
public class QCardController extends BaseController
{
  private static final Log logger = LogFactory.getLog( QCardController.class );

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    String subNavSelected = (String)tileContext.getAttribute( "subNavSelected" );
    request.setAttribute( "subNavSelected", subNavSelected );
    // below lines are required for 'Qcard banner tile on Home page'.
    // Start
    // request.setAttribute("jumpLink", "qcard");
    request.setAttribute( "isConvertCertUsed", new Boolean( getSystemVariableService().getPropertyByName( SystemVariableService.AWARDBANQ_CONVERTCERT_IS_USED ).getBooleanVal() ) );
    // End
    logger.debug( "subNavSelected=" + subNavSelected );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }
}
