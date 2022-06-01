/*
 File: ContactUsController.java
 (c) 2005 BI, Inc.  All rights reserved.
 
 Change History:

 Author       Date        Version  Comments
 -----------  ----------  -------  -----------------------------
 tennant       04/09/2005  1.0      Created
 
 */

package com.biperf.core.ui.characteristic;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.CharacteristicVisibility;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.ui.BaseController;

/**
 * Implements the controller for the SystemVariableEdit page
 * 
 * @author sedey
 */
public class CharacteristicEditController extends BaseController
{
  /**
   * Tiles controller for the CharacteristicCreate/Edit page
   * 
   * @see org.apache.struts.tiles.Controller#execute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext) Overridden from
   * @see com.biperf.core.ui.BaseController#onExecute(org.apache.struts.tiles.ComponentContext,
   *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
   *      javax.servlet.ServletContext)
   * @param context
   * @param request
   * @param response
   * @param servletContext
   * @throws Exception
   */
  public void onExecute( ComponentContext context, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    List pickListAssetList = getCMAssetService().getAllPickListItemAssets();
    request.setAttribute( "pickListItemAssets", pickListAssetList );
    request.setAttribute( "characterVisibilities", CharacteristicVisibility.getList() );
  }

  /**
   * Get the CharacteristicService from the beanLocator.
   * 
   * @return CharacteristicService
   */
  private CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }
}
