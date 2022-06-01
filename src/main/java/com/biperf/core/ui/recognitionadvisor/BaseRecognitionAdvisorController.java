
package com.biperf.core.ui.recognitionadvisor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.service.recognitionadvisor.RecognitionAdvisorService;
import com.biperf.core.service.security.EncryptionService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.utils.ClientStateUtils;

/**
 * Used for the RecognitonAdvisor Algorithm screen(s).
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
 * <td>Ramesh J</td>
 * <td>Dec 11, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public abstract class BaseRecognitionAdvisorController extends SpringBaseController
{
  protected @Autowired SystemVariableService systemVariableService;
  protected @Autowired RecognitionAdvisorService recognitionAdvisorService;
  protected @Autowired EncryptionService encryptionService;
  protected static final String CM_KEY_PREFIX = "ra.content.model.info.";

  private static final Log log = LogFactory.getLog( BaseRecognitionAdvisorController.class );

  protected <T extends RecognitionAdvisorView> ResponseEntity<T> buildResponse( RecognitionAdvisorView view, HttpStatus httpStatus )
  {
    return buildResponse( view, null, httpStatus );
  }

  protected <T extends RAEligibleProgramsView> ResponseEntity<T> buildResponse( RAEligibleProgramsView view, HttpStatus httpStatus )
  {
    return buildResponse( view, null, httpStatus );
  }

  @SuppressWarnings( "unchecked" )
  protected <T extends RAEligibleProgramsView> ResponseEntity<T> buildResponse( RAEligibleProgramsView view, List<String> messages, HttpStatus httpStatus )
  {

    return (ResponseEntity<T>)new ResponseEntity<RAEligibleProgramsView>( view, getResponseHeaders(), httpStatus );

  }

  @SuppressWarnings( "unchecked" )
  protected <T extends RecognitionAdvisorView> ResponseEntity<T> buildResponse( RecognitionAdvisorView view, List<String> messages, HttpStatus httpStatus )
  {
    view.setResponseCode( httpStatus.value() );
    if ( httpStatus.value() / 100 == 2 )
    {
      // For a success status, there will be only one message which is a success message
      if ( messages != null )
      {
        view.setResponseMessage( messages.get( 0 ) );
      }
    }

    return (ResponseEntity<T>)new ResponseEntity<RecognitionAdvisorView>( view, getResponseHeaders(), httpStatus );

  }

  protected String buildRedirect( Long userId )
  {

    Map<String, Object> parms = new HashMap<String, Object>();
    parms.put( "reporteeId", userId.toString() );
    parms.put( "isRARecognitionFlow", "yes" );
    return ClientStateUtils
        .generateEncodedLink( "",
                              systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/recognitionWizard/sendRecognitionDisplay.do",
                              parms );
  }

  private HttpHeaders getResponseHeaders()
  {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.add( "Cache-Control", "no-cache, no-store, max-age=0, must-revalidate, post-check=0, pre-check=0, private" );

    return responseHeaders;
  }

  protected boolean isRAEnabled()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal();
  }

  protected RARemindersRequest getPayLoads()
  {
    RARemindersRequest raRemindersRequest = new RARemindersRequest();

    raRemindersRequest.setRowNumStart( "1" );
    raRemindersRequest.setRowNumEnd( "7" );
    raRemindersRequest.setActivePage( "0" );
    raRemindersRequest.setSortColName( "NO_VALUE" );
    raRemindersRequest.setSortedBy( "" );
    raRemindersRequest.setExcludeUpcoming( "0" );
    raRemindersRequest.setFilterValue( "" );
    raRemindersRequest.setPendingStatus( "1" );

    return raRemindersRequest;
  }

  protected String getValidParameters( String actualParams, Set validParams )
  {
    if ( validParams.contains( actualParams ) )
    {
      return actualParams;
    }
    else
    {
      throw new BeaconRuntimeException( "Invalid Parameters" );
    }

  }

}
