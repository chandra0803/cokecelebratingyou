/**
 * 
 */

package com.biperf.core.ui.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.ProcessStatusType;
import com.biperf.core.domain.user.Role;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.security.RoleService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.NewServiceAnniversaryUtil;

/**
 * ProcessDetailController.
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
 * <td>asondgeroth</td>
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessDetailController extends BaseController
{
  /**
   * Overridden from
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param tileContext
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "statusList", ProcessStatusType.getList() );

    Set allRolesList = getRoleService().getAll();
    request.setAttribute( "roleList", allRolesList );

    List restrictedRoleList = new ArrayList();
    for ( Iterator iter = allRolesList.iterator(); iter.hasNext(); )
    {
      Role role = (Role)iter.next();
      if ( role.getCode().equals( "BI_ADMIN" ) || role.getCode().equals( "PROCESS_TEAM" ) || role.getCode().equals( "PROJ_MGR" ) )
      {
        restrictedRoleList.add( role );
      }
    }
    request.setAttribute( "restrictedRoleList", restrictedRoleList );

    List jobs = getProcessService().getProcessJobBeanNames();
    Collections.sort( jobs );
    jobs.remove( "productClaimImportProcess" );

    // For New Service Anniversary Module From Nackle
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      request.setAttribute( "processBeanNameList", NewServiceAnniversaryUtil.removePurlAndCelebrationProcessNames( jobs ) );
    }
    else
    {
      request.setAttribute( "processBeanNameList", jobs );
    }

  }

  private ProcessService getProcessService()
  {
    return (ProcessService)getService( ProcessService.BEAN_NAME );
  }

  /**
   * Gets a RoleService
   * 
   * @return BudgetService
   * @throws RoleService
   */
  private RoleService getRoleService() throws Exception
  {
    return (RoleService)getService( RoleService.BEAN_NAME );
  } // end
}
