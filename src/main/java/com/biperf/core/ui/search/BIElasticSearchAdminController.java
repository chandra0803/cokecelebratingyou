
package com.biperf.core.ui.search;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.biperf.core.domain.WebErrorMessage;
import com.biperf.core.indexing.BIElasticSearchAdminService;
import com.biperf.core.indexing.BIIndex;
import com.biperf.core.ui.ResponseEntity;
import com.biperf.core.utils.WebResponseConstants;
import com.objectpartners.cms.util.CmsResourceBundle;

@Controller
@RequestMapping( "/index" )
public class BIElasticSearchAdminController
{

  private static final Log LOGGER = LogFactory.getLog( BIElasticSearchAdminController.class );

  @Autowired
  BIElasticSearchAdminService esAdminService;
  @Autowired
  BIIndex index;

  @RequestMapping( value = "/create.action", method = POST )
  public @ResponseBody String create() throws Exception
  {
    esAdminService.createIndex();
    return getSuccessMessage();
  }

  @RequestMapping( value = "/delete.action", method = POST )
  public @ResponseBody String deleteIndex() throws Exception
  {
    esAdminService.deleteIndex();
    return getSuccessMessage();
  }

  @RequestMapping( value = "/close.action", method = POST )
  public @ResponseBody String close() throws Exception
  {
    esAdminService.close();
    return getSuccessMessage();
  }

  @RequestMapping( value = "/open.action", method = POST )
  public @ResponseBody String open() throws Exception
  {
    esAdminService.open();
    return getSuccessMessage();
  }

  @RequestMapping( value = "/reset.action", method = POST )
  public @ResponseBody String reset() throws Exception
  {
    esAdminService.sendClientResetMessage();
    return getSuccessMessage();
  }

  @RequestMapping( value = "/list.action", method = GET )
  public @ResponseBody String getIndexName()
  {
    return index.getName();
  }

  private String getSuccessMessage()
  {
    return CmsResourceBundle.getCmsBundle().getString( "admin.indexing.SUCCESS" );
  }

  @ExceptionHandler( Exception.class )
  @ResponseStatus( value = INTERNAL_SERVER_ERROR )
  public @ResponseBody ResponseEntity<List<WebErrorMessage>, Object> handleInternalException( HttpServletRequest request, Exception ex )
  {
    LOGGER.error( "Requested URL=" + request.getRequestURL(), ex );
    WebErrorMessage errorMessage = new WebErrorMessage();
    errorMessage.setSuccess( false );
    errorMessage.setType( WebResponseConstants.RESPONSE_TYPE_SERVER_ERROR );
    errorMessage.setText( CmsResourceBundle.getCmsBundle().getString( "system.errors.USER_FRIENDLY_SYSTEM_ERROR_MESSAGE" ) );
    return new ResponseEntity<List<WebErrorMessage>, Object>( Arrays.asList( errorMessage ), null );
  }

}
