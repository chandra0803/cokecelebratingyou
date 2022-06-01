/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/claimform/ClaimFormViewController.java,v $
 *
 */

package com.biperf.core.ui.claimform;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.claim.ClaimForm;
import com.biperf.core.service.claim.ClaimFormDefinitionService;
import com.biperf.core.ui.BaseController;

/**
 * ClaimFormViewController <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Jun 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class ClaimFormViewController extends BaseController
{
  private static final Log logger = LogFactory.getLog( ClaimFormViewController.class );

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
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    final String METHOD_NAME = "execute";

    logger.info( ">>> " + METHOD_NAME );

    ClaimFormForm claimFormForm = (ClaimFormForm)request.getAttribute( "claimFormForm" );
    ClaimForm claimForm = getClaimFormService().getClaimFormById( new Long( claimFormForm.getClaimFormId() ) );
    List claimFormSteps = getClaimFormService().getClaimFormSteps( claimForm.getId() );
    request.setAttribute( "claimForm", claimForm );
    request.setAttribute( "claimFormSteps", claimFormSteps );
    request.setAttribute( "claimFormStepsSize", new Integer( claimFormSteps.size() ) );

    logger.info( "<<< " + METHOD_NAME );
  }

  private ClaimFormDefinitionService getClaimFormService()
  {
    return (ClaimFormDefinitionService)getService( ClaimFormDefinitionService.BEAN_NAME );
  }

}
