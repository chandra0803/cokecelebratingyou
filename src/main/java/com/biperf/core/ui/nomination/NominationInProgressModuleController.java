/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.nomination;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.nomination.NominationsInProgressConstants;
import com.biperf.core.service.claim.NominationClaimService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.NominationInProgressModuleBean;

/**
 * 
 * @author poddutur
 * @since Apr 1, 2016
 */
public class NominationInProgressModuleController extends BaseController
{
  private static final Log log = LogFactory.getLog( NominationInProgressModuleController.class );

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    int inProgressCount = getNominationClaimService().getInProgressNominationClaimsCount( UserManager.getUserId() );

    if ( inProgressCount == 1 )
    {
      NominationInProgressModuleBean nominationInProgressModuleBean = getNominationClaimService().getInProgressNominationClaimAndPromotionId( UserManager.getUserId() );

      String url = getNominationInProgressClaimModifyUrl( request, nominationInProgressModuleBean );

      request.setAttribute( "modifyInProgressEditViewUrl", url );
    }

    request.setAttribute( "inProgressCount", inProgressCount );
  }

  protected String getNominationInProgressClaimModifyUrl( HttpServletRequest request, NominationInProgressModuleBean nominationInProgressModuleBean )
  {
    Map paramMap = new HashMap();
    paramMap.put( "promotionId", nominationInProgressModuleBean.getPromotionId() );
    paramMap.put( "claimId", nominationInProgressModuleBean.getClaimId() );

    String url = ClientStateUtils.generateEncodedLink( getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal(),
                                                       "/" + NominationsInProgressConstants.MODIFY_INPROGRESS_NOM_CLAIM_URL,
                                                       paramMap );
    return url;
  }

  private NominationClaimService getNominationClaimService()
  {
    return (NominationClaimService)getService( NominationClaimService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
