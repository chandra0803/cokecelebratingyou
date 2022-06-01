
package com.biperf.core.ui.groups;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.service.groups.ParticipantGroupException;
import com.biperf.core.service.groups.ParticipantGroupService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.ResponseEntity;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.participant.ParticipantGroupList;
import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
@RequestMapping( "/groups" )
public class ParticipantGroupController extends SpringBaseController
{
  private static final Log LOGGER = LogFactory.getLog( ParticipantGroupController.class );

  public @Autowired ParticipantGroupService participantGroupService;

  /**
   * This end point is used to save the group info into the database
   * @param model
   * @param httpRequest
   * @throws Exception
   */
  @RequestMapping( value = "/save.action", method = RequestMethod.POST )
  public @ResponseBody ResponseEntity<List<WebErrorMessage>, Object> saveParticipantGroup( @ModelAttribute ParticipantGroupModel model, HttpServletRequest httpRequest ) throws Exception
  {
    ParticipantGroupView participantGroupView = participantGroupService.saveParticipantGroup( model.getGroupId(), model.getGroupName(), model.getGroupMembers() );
    return new ResponseEntity<List<WebErrorMessage>, Object>( buildSuccessMessage( "" ), participantGroupView );
  }

  /**
   * Used to get the group details based on the group id
   * @param model
   * @param httpRequest
   * @return
   * @throws Exception
   */
  @RequestMapping( value = "/groupInfoByGroupId.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantGroupView getGroupDetailsByGroupId( @ModelAttribute ParticipantGroupModel model, HttpServletRequest httpRequest ) throws Exception
  {
    return participantGroupService.getGroupDetailsByGroupId( model.getGroupId() );
  }

  /**
   * Used to get the group details based on the user id
   * @param model
   * @param httpRequest
   * @return
   * @throws Exception
   */
  @RequestMapping( value = "/groupInfoByUserId.action", method = RequestMethod.POST )
  public @ResponseBody ParticipantGroupList getGroupDetailsByUserId( @ModelAttribute ParticipantGroupModel model, HttpServletRequest httpRequest ) throws Exception
  {
    Long userId = model.getUserId() != null ? model.getUserId() : UserManager.getUserId();
    return participantGroupService.getGroupDetailsByUserId( userId );
  }

  /**
   * Used to delete the group
   * @param model
   * @param httpRequest
   * @throws Exception
   */
  @RequestMapping( value = "/delete.action", method = RequestMethod.POST )
  public @ResponseBody ResponseEntity<List<WebErrorMessage>, Object> deleteGroup( @ModelAttribute ParticipantGroupModel model, HttpServletRequest httpRequest ) throws Exception
  {
    participantGroupService.delete( model.getGroupId() );
    return new ResponseEntity<List<WebErrorMessage>, Object>( buildSuccessMessage( "" ), null );
  }

  @SuppressWarnings( "unchecked" )
  @ExceptionHandler( Exception.class )
  @ResponseStatus( value = INTERNAL_SERVER_ERROR )
  public @ResponseBody ResponseEntity<List<WebErrorMessage>, Object> handleInternalException( HttpServletRequest request, Exception ex )
  {
    LOGGER.error( "Requested URL=" + request.getRequestURL(), ex );

    if ( ex instanceof ParticipantGroupException )
    {
      ParticipantGroupException participantGroupException = (ParticipantGroupException)ex;
      List<ServiceError> serviceErrors = (List<ServiceError>)participantGroupException.getServiceErrors();
      if ( CollectionUtils.isNotEmpty( serviceErrors ) )
      {
        return new ResponseEntity<List<WebErrorMessage>, Object>( buildCustomExceptionMessage( CmsResourceBundle.getCmsBundle().getString( ( (ServiceError)serviceErrors.get( 0 ) ).getKey() ) ),
                                                                  null );
      }
    }

    return new ResponseEntity<List<WebErrorMessage>, Object>( buildAppExceptionMessage(), null );
  }
}
