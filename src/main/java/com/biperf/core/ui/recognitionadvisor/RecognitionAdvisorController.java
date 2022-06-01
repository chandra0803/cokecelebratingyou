
package com.biperf.core.ui.recognitionadvisor;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.utils.UserManager;

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

@Controller
@RequestMapping( "/ra" )
public class RecognitionAdvisorController extends BaseRecognitionAdvisorController
{
  private static final Log log = LogFactory.getLog( RecognitionAdvisorController.class );
  private static final Set VALID_SORT_COL_NAME_PARAMS = new HashSet<String>( Arrays
      .asList( new String[] { "NO_VALUE", "is_new_hire", "first_name", "last_name", "by_me_date_sent", "by_oth_date_sent" } ) );
  private static final Set VALID_FILTER_PARAMS = new HashSet<String>( Arrays.asList( new String[] { null, "0", "1", "2", "3", "" } ) );
  private static final Set VALID_SORT_BY_PARAMS = new HashSet<String>( Arrays.asList( new String[] { null, "asc", "desc", "" } ) );

  @RequestMapping( value = "/reminders.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<RecognitionAdvisorView> reminders( @RequestBody RARemindersRequest model ) throws Exception
  {
    if ( isRAEnabled() && Objects.nonNull( model.getRowNumStart() ) && Objects.nonNull( model.getRowNumEnd() ) && Objects.nonNull( model.getSortColName() )
        && Objects.nonNull( model.getExcludeUpcoming() ) && Objects.nonNull( model.getFilterValue() ) && Objects.nonNull( model.getActivePage() ) && Objects.nonNull( model.getPendingStatus() ) )
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "Recognition advisor reminders userId" + UserManager.getUserId() );
      }
      RecognitionAdvisorView recognitionAdvisorView = recognitionAdvisorService.showRAReminderPaxData( UserManager.getUserId(),
                                                                                                       Long.parseLong( model.getRowNumStart() ),
                                                                                                       Long.parseLong( model.getRowNumEnd() ),
                                                                                                       getValidParameters( model.getSortColName(), VALID_SORT_COL_NAME_PARAMS ),
                                                                                                       getValidParameters( model.getSortedBy(), VALID_SORT_BY_PARAMS ),
                                                                                                       Long.parseLong( model.getExcludeUpcoming() ),
                                                                                                       getValidParameters( model.getFilterValue(), VALID_FILTER_PARAMS ),
                                                                                                       model.getActivePage(),
                                                                                                       Long.parseLong( model.getPendingStatus() ) );
      recognitionAdvisorView.setContent( buildCMSMessage( CM_KEY_PREFIX + "CONTENT" ) );

      return buildResponse( recognitionAdvisorView, HttpStatus.OK );

    }

    return buildResponse( new RecognitionAdvisorView(), HttpStatus.BAD_REQUEST );

  }

  @RequestMapping( value = "/{userId}/sendRecognition.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public void sendRecognition( @PathVariable String userId, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    if ( isRAEnabled() )
    {
      try
      {
        response.sendRedirect( buildRedirect( Long.parseLong( userId ) ) );
      }
      catch( IOException e )
      {
        log.error( e.getMessage(), e );
      }

    }
    else
    {
      response.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }

  }

  @RequestMapping( value = "/eligiblePrograms.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<RAEligibleProgramsView> eligiblePrograms()
  {
    if ( isRAEnabled() )
    {
      RAEligibleProgramsView raEligibleProgramsView = recognitionAdvisorService.getEligiblePromotions( UserManager.getUserId(), isRAEnabled() );
      return buildResponse( raEligibleProgramsView, HttpStatus.OK );
    }
    else
    {
      return buildResponse( new RAEligibleProgramsView(), HttpStatus.BAD_REQUEST );
    }

  }

  /**
   * 
   * @param request
   * @param response
   * @throws IOException
   * This end point is used to Send a Recognition from side bar alert section
   */
  @RequestMapping( value = "/sendRecognitionFromAlerts.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public void sendRecognizationforAlert( HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    if ( isRAEnabled() )
    {
      try
      {
        Long userId = recognitionAdvisorService.getLongOverDueNewHireForManager( UserManager.getUserId() );
        if ( log.isDebugEnabled() )
        {
          log.debug( "Recognition advisor userId" + userId );
        }

        response.sendRedirect( buildRedirect( userId ) );

      }
      catch( Exception e )
      {
        log.error( e.getMessage(), e );
      }

    }
    else
    {
      response.sendError( HttpServletResponse.SC_BAD_REQUEST );
    }
  }

  @RequestMapping( value = "/mobile/reminders.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<RecognitionAdvisorView> mobileReminders( @RequestBody RARemindersRequest raReminderReq ) throws Exception
  {
    if ( isRAEnabled() && ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() ) )
    {
      return reminders( raReminderReq );
    }

    return new ResponseEntity<RecognitionAdvisorView>( new RecognitionAdvisorView(), null, HttpStatus.BAD_REQUEST );

  }

  @RequestMapping( value = "/mobile/RAEnabled.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public ResponseEntity<RAEnableBean> raEnabled()
  {
    if ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() )
    {
      RAEnableBean raEnableBean = new RAEnableBean();
      raEnableBean.setRaEnabled( isRAEnabled() );
      return new ResponseEntity<RAEnableBean>( raEnableBean, HttpStatus.OK );
    }

    return new ResponseEntity<RAEnableBean>( new RAEnableBean(), null, HttpStatus.BAD_REQUEST );

  }

  @RequestMapping( value = "/mobile/paxCount.action", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
  public ResponseEntity<RAPaxCountValueBean> paxCount()
  {
    if ( isRAEnabled() && ( UserManager.getUser().isManager() || UserManager.getUser().isOwner() ) )
    {
      RARemindersRequest raRemindersRequest = getPayLoads();
      RecognitionAdvisorView recognitionAdvisorView = recognitionAdvisorService.showRAReminderPaxData( UserManager.getUserId(),
                                                                                                       Long.parseLong( raRemindersRequest.getRowNumStart() ),
                                                                                                       Long.parseLong( raRemindersRequest.getRowNumEnd() ),
                                                                                                       raRemindersRequest.getSortColName(),
                                                                                                       raRemindersRequest.getSortedBy(),
                                                                                                       Long.parseLong( raRemindersRequest.getExcludeUpcoming() ),
                                                                                                       raRemindersRequest.getFilterValue(),
                                                                                                       raRemindersRequest.getActivePage(),
                                                                                                       Long.parseLong( raRemindersRequest.getPendingStatus() ) );

      RAPaxCountValueBean raPaxCountValueBean = new RAPaxCountValueBean();

      raPaxCountValueBean.setNewHireTotalcount( recognitionAdvisorView.getNewHireTotalcount() );
      raPaxCountValueBean.setOverDueTotalcount( recognitionAdvisorView.getOverDueTotalcount() );

      return new ResponseEntity<RAPaxCountValueBean>( raPaxCountValueBean, HttpStatus.OK );
    }

    return new ResponseEntity<RAPaxCountValueBean>( new RAPaxCountValueBean(), null, HttpStatus.BAD_REQUEST );

  }

}
