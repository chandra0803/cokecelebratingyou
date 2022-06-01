
package com.biperf.core.ui.mtc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.biperf.core.service.mtc.MTCVideoService;
import com.biperf.core.ui.SpringBaseController;

@Controller
@RequestMapping( "/mtc" )
public class MTCVideoCallBackController extends SpringBaseController
{

  private static final Log LOGGER = LogFactory.getLog( MTCVideoCallBackController.class );

  public @Autowired MTCVideoService mtcVideoService;

  /**
   * This end point is used to save the transcoded video details into the database
   * @param model
   * @param httpRequest
   * @throws Exception
   */
  @RequestMapping( value = "/callbackpost.action", method = RequestMethod.POST )
  public void save( @RequestBody CallBackEntity model, HttpServletRequest httpRequest ) throws Exception
  {

    List<CallBackDetailEntity> entities = model.getOutputs();

    CallBackDetailEntity mp4Entity = entities.stream().filter( x -> "mp4".equals( x.getDescriptor() ) ).findAny().orElse( null );
    CallBackDetailEntity webmEntity = entities.stream().filter( x -> "webm".equals( x.getDescriptor() ) ).findAny().orElse( null );
    CallBackDetailEntity thumbnailEntity = entities.stream().filter( x -> "thumbnail".equals( x.getDescriptor() ) ).findAny().orElse( null );
    Map<String, Object> details = new HashMap<String, Object>();
    details.put( "mp4", mp4Entity.getUrl() );
    details.put( "webm", webmEntity.getUrl() );
    details.put( "thumbnail", thumbnailEntity.getUrl() );
    details.put( "originalFormat", model.getInputKey().substring( model.getInputKey().lastIndexOf( "." ) + 1 ) );

    DateFormat inputFormat = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSSX" );
    Date date = inputFormat.parse( mp4Entity.getExpirationDate() );
    details.put( "expiryDate", date );
    mtcVideoService.save( model.getRequestId(), details );

  }

}
