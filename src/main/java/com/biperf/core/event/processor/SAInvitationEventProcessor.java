/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.event.processor;

import java.util.Objects;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.biperf.core.domain.serviceanniversary.SAInviteAndContributeInfo;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.utils.DateUtils;
import com.biw.event.streams.consume.EventProcessor;
import com.biw.event.streams.event.Event;
import com.fasterxml.jackson.databind.JsonNode;

/** 
 * 
 * @author sivanand
 * @since Jan 7, 2019
 * @version 1.0
 */

@Component
public class SAInvitationEventProcessor extends BaseEventProcessor implements EventProcessor
{
  private static Log log = LogFactory.getLog( SAInvitationEventProcessor.class );

  private static final String[] ACCEPTED_SCHEMAS = new String[] { "service-awards-invitation" };

  private static final String EVENT_CREATE = "create-invite";

  private @Autowired ServiceAnniversaryService serviceAnniversaryService;

  @Override
  protected boolean handleEvent( Event<JsonNode> event ) throws Exception
  {
    if ( isInviteEvent( event ) )
    {
      saveSAInvitationInfo( event );
      return true;
    }
    return false;
  }

  @Override
  protected String[] acceptedSchemas() throws Exception
  {
    return ACCEPTED_SCHEMAS;
  }

  private boolean isInviteEvent( Event<JsonNode> event )
  {
    if ( event.getEvent().equalsIgnoreCase( EVENT_CREATE ) )
    {
      return true;
    }
    return false;

  }

  private void saveSAInvitationInfo( Event<JsonNode> event ) throws Exception
  {
    String contributorRosterPersonId = null;
    Long contributorDMUserId = null;
    UUID celebrationId = null;

    if ( Objects.nonNull( event ) )
    {
      JsonNode data = event.getData();

      celebrationId = Objects.nonNull( data.get( "celebrationSiteId" ).asText() ) ? UUID.fromString( data.get( "celebrationSiteId" ).asText() ) : null;

      contributorRosterPersonId = data.get( "contributorPersonId" ).asText();
      if ( !contributorRosterPersonId.equalsIgnoreCase( "null" ) )
      {
        contributorDMUserId = userService.getUserIdByRosterUserId( UUID.fromString( contributorRosterPersonId ) );
      }

      if ( Objects.nonNull( contributorDMUserId ) )
      {
        try
        {
          SAInviteAndContributeInfo saInviteAndContributeInfoExist = serviceAnniversaryService.getSAInviteAndContributeInfoByPersonIdAndCelebrationId( contributorDMUserId, celebrationId );
          if ( Objects.nonNull( saInviteAndContributeInfoExist ) )
          {
            return;
          }
          else
          {
            SAInviteAndContributeInfo saInviteAndContributeInfo = new SAInviteAndContributeInfo();

            saInviteAndContributeInfo.setCelebrationId( celebrationId );
            saInviteAndContributeInfo.setContributorPersonId( contributorDMUserId );
            saInviteAndContributeInfo.setContributorFirstName( data.get( "contributorFirstName" ).asText() );
            saInviteAndContributeInfo.setContributorLastName( data.get( "contributorLastName" ).asText() );
            saInviteAndContributeInfo.setContributorEmailAddr( data.get( "contributorEmailAddress" ).asText() );
            saInviteAndContributeInfo.setInviteSendDate( DateUtils.convert( data.get( "invitationDate" ).asText() ) );
            saInviteAndContributeInfo.setContributionState( "invited" );
            saInviteAndContributeInfo.setInternalOrExternal( data.get( "isInternal" ).asBoolean() );
            saInviteAndContributeInfo.setInvited( data.get( "isAutoInvited" ).asBoolean() );

            serviceAnniversaryService.saveSAInviteAndContributeInfo( saInviteAndContributeInfo );

          }
        }
        catch( Exception exc )
        {
          log.error( "Exception while getting SA InviteAndContributeInfo : " + exc.getMessage() );
          throw exc;
        }
      }

    }
  }

}
