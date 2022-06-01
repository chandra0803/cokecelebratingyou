
package com.biperf.core.ui.claim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.claim.AbstractRecognitionClaim;
import com.biperf.core.domain.claim.ClaimElement;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.promotion.PublicRecognitionService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ArrayUtil;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PublicRecognitionFormattedValueBean;

public class RecognitionDetailAction extends BaseDispatchAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    // Get parameters.
    final Long CLAIM_ID = RequestUtils.getRequiredParamLong( request, "recognitionId" );
    String referralPage = RequestUtils.getOptionalParamString( request, "referralPage" );

    boolean includeTeamClaims = false;
    if ( StringUtil.isNullOrEmpty( referralPage ) || !"activityHistory".equals( referralPage ) )
    {
      includeTeamClaims = true;
    }

    RecognitionDetailBean bean = getRecognitionDetailBean( request, CLAIM_ID, includeTeamClaims );
    if ( bean.getRecognition().getRecipients().size() > 1 )
    {
      bean.getRecognition().setAllowAddPoints( false );
    }
    super.writeAsJsonToResponse( bean, response );
    return null;
  }

  protected RecognitionDetailBean getRecognitionDetailBean( HttpServletRequest request, Long claimId, boolean includeTeamClaims )
  {
    RecognitionDetailBean bean = null;
    AbstractRecognitionClaim abstractRecognitionClaim = getClaim( claimId, RequestUtils.getBaseURI( request ), includeTeamClaims );

    // could also use UserService#getPreferredLanguageFor to determine the user's language; the rest
    // of
    // translation uses it
    String userLang = UserManager.getUserLanguage();

    Long userId;
    String participantId = request.getParameter( "participantId" );
    if ( participantId != null )
    {
      userId = Long.parseLong( participantId );
    }
    else
    {
      userId = UserManager.getUserId();
    }

    List<PublicRecognitionFormattedValueBean> publicRecognitionClaims = getPublicRecognitionService().getPublicRecognitionClaimsByClaimId( claimId, userId );
    if ( !publicRecognitionClaims.isEmpty() )
    {
      for ( PublicRecognitionFormattedValueBean valueBean : publicRecognitionClaims )
      {
        if ( Boolean.valueOf( valueBean.getIsMine() ) )
        {
          valueBean.setAbstractRecognitionClaim( abstractRecognitionClaim );
          bean = new RecognitionDetailBean( userLang, request.getContextPath(), valueBean, includeTeamClaims, false );
          break;
        }
      }
    }
    else
    {
      bean = new RecognitionDetailBean( userLang, request.getContextPath(), abstractRecognitionClaim, true, false );
    }

    if ( bean == null )
    {
      if ( !publicRecognitionClaims.isEmpty() )
      {
        PublicRecognitionFormattedValueBean publicRecognitionClaim = publicRecognitionClaims.iterator().next();
        publicRecognitionClaim.setAbstractRecognitionClaim( abstractRecognitionClaim );
        bean = new RecognitionDetailBean( userLang, request.getContextPath(), publicRecognitionClaim, includeTeamClaims, false );
      }
      else
      {
        bean = new RecognitionDetailBean( userLang, request.getContextPath(), abstractRecognitionClaim, includeTeamClaims, false );
      }
    }

    return bean;
  }

  private AbstractRecognitionClaim getClaimElementDomainObjects( AbstractRecognitionClaim claim )
  {
    List claimElementList = new ArrayList();
    for ( Iterator iter = claim.getClaimElements().iterator(); iter.hasNext(); )
    {
      ClaimElement claimElement = (ClaimElement)iter.next();
      if ( claimElement.getClaimFormStepElement().getClaimFormElementType().isMultiSelectField() || claimElement.getClaimFormStepElement().getClaimFormElementType().isSelectField() )
      {
        List pickListItems = new ArrayList();
        // convert the comma delimited list of selected pickListItems to a list of strings
        Iterator pickListCodes = ArrayUtil.convertDelimitedStringToList( claimElement.getValue(), "," ).iterator();
        while ( pickListCodes.hasNext() )
        {
          String code = (String)pickListCodes.next();
          pickListItems.add( DynaPickListType.lookup( claimElement.getClaimFormStepElement().getSelectionPickListName(), code ) );
        }
        claimElement.setPickListItems( pickListItems );
      }
    }
    return claim;
  }

  private AbstractRecognitionClaim getClaim( Long claimId, String baseContextPath, boolean includeTeamClaims )
  {
    AbstractRecognitionClaim claim = getClaimService().getRecognitionDetail( claimId, includeTeamClaims );
    getClaimElementDomainObjects( claim );

    if ( claim instanceof RecognitionClaim )
    {
      String purlUrl = getPurlService().createPurlRecipientUrlFromClaimId( claimId );
      if ( purlUrl != null )
      {
        PurlRecipient purlRecipient = getPurlService().getPurlRecipientByClaimId( claimId );
        if ( purlRecipient != null )
        {
          Map<String, String> paramMap = new HashMap<String, String>();
          paramMap.put( "purlRecipientId", purlRecipient.getId().toString() );
          ( (RecognitionClaim)claim ).setPurlUrl( ClientStateUtils.generateEncodedLink( baseContextPath, "/purl/purlRecipient.do?method=display", paramMap ) );
        }
      }
    }

    return claim;
  }

  protected static PublicRecognitionService getPublicRecognitionService()
  {
    return (PublicRecognitionService)getService( PublicRecognitionService.BEAN_NAME );
  }

  protected static ClaimService getClaimService()
  {
    return (ClaimService)getService( ClaimService.BEAN_NAME );
  }

  protected static PurlService getPurlService()
  {
    return (PurlService)getService( PurlService.BEAN_NAME );
  }

}
