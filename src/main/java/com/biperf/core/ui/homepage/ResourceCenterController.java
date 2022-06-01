
package com.biperf.core.ui.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.diycommunications.DIYCommunications;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.diycommunications.ParticipantDIYCommunicationsService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.UserManager;
import com.objectpartners.cms.domain.Content;
import com.objectpartners.cms.domain.Translations;
import com.objectpartners.cms.service.ContentReader;
import com.objectpartners.cms.util.ContentReaderManager;

/**
 * ResourceCenterController.
 * 
 *
 */
public class ResourceCenterController extends BaseController
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {
    AuthenticatedUser user = UserManager.getUser();

    if ( user.isParticipant() )
    {
      Participant participant = getParticipantService().getParticipantById( user.getUserId() );
      boolean displayManageQuizzes = getPromotionService().isParticipantInDIYPromotionAudience( participant );
      request.setAttribute( "displayManageQuizzes", displayManageQuizzes );
    }
    List<Content> resourceCenter = new ArrayList<Content>();
    List<Content> activeDIYResourceCenterList = getParticipantDIYCommunicationsService().getDiyCommunications( DIYCommunications.COMMUNICATION_TYPE_RESOURCE_CENTER,
                                                                                                               DIYCommunications.DIY_RESOURCE_SECTION_CODE );

    List<Content> homeResourceCenter = getResourceCenter();

    if ( activeDIYResourceCenterList != null && activeDIYResourceCenterList.size() > 0 )
    {
      resourceCenter.addAll( activeDIYResourceCenterList );
    }

    if ( homeResourceCenter != null && homeResourceCenter.size() > 0 )
    {
      resourceCenter.addAll( homeResourceCenter );
    }

    if ( null != resourceCenter && resourceCenter.size() != 0 )
    {
      // no eligible resource center items
      request.setAttribute( "displayResourceCenter", Boolean.TRUE );
      request.setAttribute( "resourceCenter", resourceCenter );
    }

  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  private List<Content> getResourceCenter()
  {
    ContentReader contentReader = ContentReaderManager.getContentReader();
    List<Content> resourceCenter = (List<Content>)contentReader.getContent( "home.resourceCenter" );
    String siteURL = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    String downloadURL = siteURL + ActionConstants.EXCEL_DOWNLOAD_ACTION;
    for ( Content content : resourceCenter )
    {
      Map contentDataMap = content.getContentDataMap();
      String type = (String)contentDataMap.get( "TYPE" );
      if ( type.equals( ActionConstants.XLS ) || type.equals( ActionConstants.XLSX ) )
      {
        Map clientStateParameterMap = new HashMap();
        Map m = content.getContentDataMapList();
        Translations nameObject = (Translations)m.get( "URL" );
        clientStateParameterMap.put( ActionConstants.FILEPATH, siteURL + "/" + nameObject.getValue() );
        String downLoadButton = ClientStateUtils.generateEncodedLink( downloadURL, "", clientStateParameterMap );
        contentDataMap.remove( "URL" );
        contentDataMap.put( "URL", downLoadButton );
      }
    }

    return resourceCenter;
  }

  public static ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  public static PromotionService getPromotionService()
  {
    return (PromotionService)getService( PromotionService.BEAN_NAME );
  }

  public static CMAssetService getCMAssetService()
  {
    return (CMAssetService)getService( CMAssetService.BEAN_NAME );
  }

  public static ParticipantDIYCommunicationsService getParticipantDIYCommunicationsService()
  {
    return (ParticipantDIYCommunicationsService)getService( ParticipantDIYCommunicationsService.BEAN_NAME );
  }

  public static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
