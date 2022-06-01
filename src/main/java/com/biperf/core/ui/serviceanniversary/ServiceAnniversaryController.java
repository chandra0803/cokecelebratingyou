
package com.biperf.core.ui.serviceanniversary;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.ui.SpringBaseController;

@Controller
@RequestMapping( "/sa" )
public class ServiceAnniversaryController extends SpringBaseController
{
  private static final Log LOG = LogFactory.getLog( ServiceAnniversaryController.class );

  @Autowired
  private ServiceAnniversaryService serviceAnniversaryService;

  @RequestMapping( value = "/contributelink.action", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<ContributionLinkDetails> getContributionPageURL( @RequestBody ContributionLinkDetails contributionLinkDtlsRequest )
  {
    try
    {
      String url = serviceAnniversaryService.getContributePageUrl( contributionLinkDtlsRequest.getCelebrationId(), null );
      contributionLinkDtlsRequest.setUrl( url );


      contributionLinkDtlsRequest.setRecepientName( getRecepientName( contributionLinkDtlsRequest.getCelebrationId() ) );

      return new ResponseEntity<ContributionLinkDetails>( contributionLinkDtlsRequest, HttpStatus.OK );
    }
    catch( Exception e )
    {
      LOG.error( "Bad Request from SA Service call while getting contributelink. Status code : " + HttpStatus.BAD_REQUEST );
      contributionLinkDtlsRequest.setRecepientName( getRecepientName( contributionLinkDtlsRequest.getCelebrationId() ) );
      return new ResponseEntity<ContributionLinkDetails>( contributionLinkDtlsRequest, HttpStatus.BAD_REQUEST );
    }
  }

  @RequestMapping( value = "/giftcodelink.action", method = { RequestMethod.POST }, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
  public @ResponseBody ResponseEntity<ContributionLinkDetails> getGiftCodePageURL( @RequestBody ContributionLinkDetails contributionLinkDtlsRequest )
  {
    try
    {
      String url = serviceAnniversaryService.getGiftCodePageUrl( contributionLinkDtlsRequest.getCelebrationId() );
      contributionLinkDtlsRequest.setUrl( url );

      return new ResponseEntity<ContributionLinkDetails>( contributionLinkDtlsRequest, HttpStatus.OK );
    }
    catch( Exception e )
    {
      LOG.error( "Bad Request from SA Service call while getting giftcodelink. Status code : " + HttpStatus.BAD_REQUEST );
      return new ResponseEntity<ContributionLinkDetails>( contributionLinkDtlsRequest, HttpStatus.BAD_REQUEST );
    }
  }

  private String getRecepientName( String celebrationId )
  {
    String recipientName = null;
    try
    {
      recipientName = serviceAnniversaryService.getRecipientName( celebrationId );
    }
    catch( Exception exception )
    {
      LOG.error( "Error in getting recepient name : ", exception );
    }
    return recipientName;
  }
}
