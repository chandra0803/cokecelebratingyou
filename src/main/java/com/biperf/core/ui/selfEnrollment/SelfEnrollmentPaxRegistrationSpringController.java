package com.biperf.core.ui.selfEnrollment;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.ui.SpringBaseController;
import com.biperf.core.ui.WebErrorMessageList;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.PageConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
@RequestMapping( "/selfenrollment" )
public class SelfEnrollmentPaxRegistrationSpringController extends SpringBaseController
{
  private static final String SELF_ENROLLMENT_NODE_ID = "selfEnrollmentNodeId";
  
  private @Autowired NodeService nodeService;
  
  @RequestMapping( value = "/validateRegistrationCode.action", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<?> validateRegistrationCode( @RequestBody SelfEnrollmentRequest selfEnrollmentRequest, HttpServletRequest request )
  {
    Node node = nodeService.getNodeByEnrollmentCode( selfEnrollmentRequest.getRegistrationCode() );
    if ( node != null && !node.isDeleted() )
    {
      request.getSession().setAttribute( SELF_ENROLLMENT_NODE_ID, node.getId() );
      return buildSelfEnrollmentDisplayRedirect( request );
    }
    else
    {
      WebErrorMessageList messages = new WebErrorMessageList();
      WebErrorMessage message = new WebErrorMessage();
      message = WebErrorMessage.addErrorMessage( message );
      message.setText( CmsResourceBundle.getCmsBundle().getString( "login.errors.REGI_CODE_INVALID" ) );
      messages.getMessages().add( message );
      return new ResponseEntity<>( messages, HttpStatus.OK );
    }
  }
  
  private ResponseEntity<PageRedirectMessage> buildSelfEnrollmentDisplayRedirect( HttpServletRequest request )
  {
    return new ResponseEntity<>( buildPageRedirect( RequestUtils.getBaseURI( request ) + PageConstants.SELF_ENROLLMENT_PAX_REG_DISPLAY ), HttpStatus.OK );
  }

}
