/**
 * 
 */

package com.biperf.core.ui.recognition;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.characteristic.CharacteristicController;
import com.biperf.core.ui.homepage.HomePageController;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.ClientStatePasswordManager;
import com.biperf.core.utils.ClientStateSerializer;
import com.biperf.core.utils.ImageUtils;
import com.biperf.core.utils.InvalidClientStateException;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PromotionMenuBean;

/**
 * @author poddutur
 *
 */
public class RecognitionSubmitPageController extends HomePageController
{
  private static final Log logger = LogFactory.getLog( CharacteristicController.class );

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext )
  {

    super.onExecute( tileContext, request, response, servletContext );
    request.setAttribute( "allowPasswordFieldAutoComplete", getSystemVariableService().getPropertyByName( SystemVariableService.ALLOW_PASSWORD_FIELD_AUTO_COMPLETE ).getBooleanVal() );
    
 // Client Customization for WIP #39189 starts
    // Converting from MB to KB
    request.setAttribute( "claimFileSize", ( getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal() ) * 100000 );
    request.setAttribute( "claimFileSizeInMB", getSystemVariableService().getPropertyByName( SystemVariableService.SYSTEM_IMG_SIZE_LIMIT ).getIntVal() );
    //request.setAttribute( "claimFileTypes", getSystemVariableService().getPropertyByName( SystemVariableService.COKE_UPLOAD_FILE_TYPES ).getStringVal() );
    // Client Customization for WIP #39189 ends

    // Attributes for the error modal
    request.setAttribute( "maxImageSize", String.valueOf( ImageUtils.getImageSizeLimit() ) );
    request.setAttribute( "maxVideoSize", String.valueOf( ImageUtils.getVideoSizeLimit() ) );
    request.setAttribute( "supportedImageTypes", ImageUtils.getValidImageTypes().toUpperCase() );
    request.setAttribute( "supportedVideoTypes", SupportedEcardVideoTypes.getSupportedVideoTypes().toUpperCase() );
    request.setAttribute( "isDelegate", UserManager.getUser().isDelegate() );
    String isRARecognitionFlow = null;
    // RA Recognition Variable Flow
    if ( getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal() )
    {
      if ( ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() ) && !UserManager.isUserDelegateOrLaunchedAs() )
      {

        List<PromotionMenuBean> raEligiblePromotions = getEligiblePromotions( request ).stream()
            .filter( ( p ) -> p.isCanSubmit() && p.getPromotion().isRecognitionPromotion() && p.getPromotion().isLive() && ! ( (RecognitionPromotion)p.getPromotion() ).isIncludePurl()
                && ! ( (RecognitionPromotion)p.getPromotion() ).isIncludeCelebrations() && !p.getPromotion().getAwardType().isMerchandiseAwardType() && !p.getPromotion().isFileLoadEntry() )
            .collect( Collectors.toList() );

        if ( Objects.nonNull( raEligiblePromotions ) && raEligiblePromotions.size() > 0 )
        {
          request.setAttribute( "isRAPromoFlag", true );
        }
        else
        {
          request.setAttribute( "isRAPromoFlag", false );
        }
        request.setAttribute( "raEligiblePromotionList", raEligiblePromotions );
      }
      else
      {
        request.setAttribute( "isRAPromoFlag", false );
      }

      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );

      if ( StringUtils.isNotBlank( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }

        try
        {
          Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
          if ( !Objects.isNull( clientStateMap.get( "isRARecognitionFlow" ) ) )
          {
            isRARecognitionFlow = (String)clientStateMap.get( "isRARecognitionFlow" );
            if ( "yes".equals( isRARecognitionFlow ) )
            {
              request.setAttribute( "isRARecognitionFlow", "yes" );
            }
          }
          if ( !Objects.isNull( clientStateMap.get( "reporteeId" ) ) )
          {
            request.setAttribute( "reporteeId", clientStateMap.get( "reporteeId" ) );
          }

        }
        catch( InvalidClientStateException e )
        {
          logger.error( "Invalid client state exception" );
        }

        if ( logger.isDebugEnabled() )
        {
          logger.debug( " isRARecognitionFlow value is " + isRARecognitionFlow );
        }
      }
      else if ( request.getParameter( "isRARecognitionFlow" ) != null )
      {
        isRARecognitionFlow = (String)request.getParameter( "isRARecognitionFlow" );
        if ( "yes".equals( isRARecognitionFlow ) )
        {
          request.setAttribute( "isRARecognitionFlow", "yes" );
        }
        if ( !Objects.isNull( request.getParameter( "reporteeId" ) ) )
        {
          request.setAttribute( "reporteeId", request.getParameter( "reporteeId" ) );
        }
      }

    }

  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

}
