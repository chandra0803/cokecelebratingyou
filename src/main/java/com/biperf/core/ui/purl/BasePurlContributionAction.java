/*
 * (c) 2013 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/purl/BasePurlContributionAction.java,v $
 */

package com.biperf.core.ui.purl;

import java.util.HashMap;
import java.util.Map;

import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlContributor;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.PageConstants;

public class BasePurlContributionAction extends BaseDispatchAction
{
  protected String buildContributorBackUrl( PurlContributor contributor )
  {
    Long promotionId = contributor.getPurlRecipient().getPromotion().getId();
    Map<String, Object> parameterMap = new HashMap<String, Object>();
    parameterMap.put( "promotionId", promotionId );
    String contributorBackURL = "";

    if ( isUserRecipientManager( contributor ) )
    {
      contributorBackURL = ClientStateUtils.generateEncodedLink( getSiteUrl(), PageConstants.ALERT_DETAIL_PURL_MAINTENANCE_LIST, parameterMap );
    }
    else
    {
      contributorBackURL = ClientStateUtils.generateEncodedLink( getSiteUrl(), PageConstants.ALERT_DETAIL_PURL_CONTRIBUTION_LIST, parameterMap );
    }
    return contributorBackURL;
  }

  protected boolean isUserRecipientManager( PurlContributor contributor )
  {
    Participant purlRecipientManager = getPurlService().getNodeOwnerForPurlRecipient( contributor.getPurlRecipient() );
    if ( contributor.getUser() != null && purlRecipientManager != null && contributor.getUser().getId().equals( purlRecipientManager.getId() ) )
    {
      return true;
    }
    return false;
  }

  protected String getSiteUrl()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  protected PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
