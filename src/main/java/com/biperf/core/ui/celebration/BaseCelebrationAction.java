
package com.biperf.core.ui.celebration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.value.celebration.CelebrationPageValue;

/**
 * 
 * CelebrationPageAction
 * Main action to load the secondary page with all the tiles.
 * 
 */
public class BaseCelebrationAction extends BaseDispatchAction
{
  protected static final String CELEBRATION_CLAIM_MAP = "celebrationClaimMap";
  protected static final String CELEBRATION_VALUEBEAN_MAP = "celebrationValueMap";
  protected static final String CELEBRATION_VALUE = "celebrationValue";
  protected static final String CELEBRATION_PURL_URL = "celebrationRecognitionPurlUrl";
  protected static final String CELEBRATION_IMAGE_FILLER_URL = "celebrationImageFillerUrl";

  public RecognitionClaim getRecognitionClaim( Long claimId, HttpServletRequest request )
  {
    Map<Long, Object> celebrationClaimMap = null;
    if ( request.getSession().getAttribute( CELEBRATION_CLAIM_MAP ) != null )
    {
      celebrationClaimMap = (HashMap)request.getSession().getAttribute( CELEBRATION_CLAIM_MAP );
    }
    RecognitionClaim recognitionClaim = null;
    if ( celebrationClaimMap != null )
    {
      recognitionClaim = (RecognitionClaim)celebrationClaimMap.get( claimId );
    }
    return recognitionClaim;
  }

  public CelebrationPageValue getCelebrationValue( Long claimId, HttpServletRequest request )
  {
    Map<Long, Object> celebrationValueMap = null;
    if ( request.getSession().getAttribute( CELEBRATION_VALUEBEAN_MAP ) != null )
    {
      celebrationValueMap = (HashMap)request.getSession().getAttribute( CELEBRATION_VALUEBEAN_MAP );
    }
    CelebrationPageValue celebrationValue = null;
    if ( celebrationValueMap != null )
    {
      celebrationValue = (CelebrationPageValue)celebrationValueMap.get( claimId );
    }
    return celebrationValue;
  }

  public Long getClaimId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "claimId" );
  }

  public Long getRecipientId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "recipientId" );
  }

  public Long getSendToUserId( HttpServletRequest request ) throws InvalidClientStateException
  {
    return getClientStateParameterValueAsLong( request, "sendToUserId" );
  }

  public Long getClientStateParameterValueAsLong( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    Object paramValue = getClientStateParameterValue( request, parameter );
    Long parameterValue = null;
    try
    {
      parameterValue = (Long)paramValue;
    }
    catch( ClassCastException cce )
    {
      parameterValue = new Long( (String)paramValue );
    }
    return parameterValue;
  }

  public String getClientStateParameterValueAsString( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    Object paramValue = getClientStateParameterValue( request, parameter );
    String parameterValue = null;
    try
    {
      parameterValue = (String)paramValue;
    }
    catch( ClassCastException cce )
    {
      parameterValue = new String( (String)paramValue );
    }
    return parameterValue;
  }

  public Object getClientStateParameterValue( HttpServletRequest request, String parameter ) throws InvalidClientStateException
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    // Deserialize the client state.
    Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
    return clientStateMap.get( parameter );
  }

  protected String getFinalImagePrefixUrl()
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    if ( !Environment.isCtech() )
    {
      siteUrlPrefix = getSystemVariableService().getPropertyByName( SystemVariableService.SITE_URL_PREFIX + "." + Environment.ENV_QA ).getStringVal();
    }
    return siteUrlPrefix + "-cm/cm3dam" + '/';
  }

  protected String getSiteUrlPrefix()
  {
    return getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
