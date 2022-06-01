/*
 * (c) 2019 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.event.processor;

import java.sql.Timestamp;
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
 * @since Jan 8, 2019
 * @version 1.0
 */

@Component
public class SAContributionEventProcessor extends BaseEventProcessor implements EventProcessor
{
  private static Log log = LogFactory.getLog( SAContributionEventProcessor.class );

  private static final String[] ACCEPTED_SCHEMAS = new String[] { "service-awards-contribution" };

  private static final String EVENT_CREATE = "create-contribution";

  private @Autowired ServiceAnniversaryService serviceAnniversaryService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean handleEvent( Event<JsonNode> event ) throws Exception
  {
    if ( isContributeEvent( event ) )
    {
      saveSAContributionInfo( event );
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String[] acceptedSchemas() throws Exception
  {
    return ACCEPTED_SCHEMAS;
  }

  private boolean isContributeEvent( Event<JsonNode> event )
  {
    if ( event.getEvent().equalsIgnoreCase( EVENT_CREATE ) )
    {
      return true;
    }
    return false;
  }

  private void saveSAContributionInfo( Event<JsonNode> event ) throws Exception
  {
    String strPersonId = null;
    Long numPersonId = null;
    UUID celebrationId = null;
    SAInviteAndContributeInfo saInviteAndContributeInfo = null;

    if ( Objects.nonNull( event ) )
    {
      JsonNode data = event.getData();

      celebrationId = Objects.nonNull( data.get( "celebrationSiteId" ).asText() ) ? UUID.fromString( data.get( "celebrationSiteId" ).asText() ) : null;

      strPersonId = data.get( "contributorPersonId" ).asText();
      if ( !strPersonId.equalsIgnoreCase( "null" ) )
      {
        numPersonId = userService.getUserIdByRosterUserId( UUID.fromString( strPersonId ) );
      }

      if ( Objects.nonNull( numPersonId ) )
      {
        try
        {
          saInviteAndContributeInfo = serviceAnniversaryService.getSAInviteAndContributeInfoByPersonIdAndCelebrationId( numPersonId, celebrationId );
        }
        catch( Exception exc )
        {
          log.error( "Exception while getting existing SA InviteAndContributeInfo : " + exc.getMessage() );
          throw exc;
        }
      }

      if ( Objects.nonNull( saInviteAndContributeInfo ) )
      {
        saInviteAndContributeInfo.setContributionState( "contributed" );
        saInviteAndContributeInfo.setContributedDate( DateUtils.convert( data.get( "contributionDate" ).asText() ) );
        serviceAnniversaryService.saveSAInviteAndContributeInfo( saInviteAndContributeInfo );
      }
      else
      {
        SAInviteAndContributeInfo saInviteAndContributeInfoNotExist = new SAInviteAndContributeInfo();

        if ( Objects.nonNull( numPersonId ) )
        {
          saInviteAndContributeInfoNotExist.setContributorPersonId( numPersonId );
        }

        saInviteAndContributeInfoNotExist.setCelebrationId( celebrationId );
        saInviteAndContributeInfoNotExist.setContributorFirstName( data.get( "contributorFirstName" ).asText() );
        saInviteAndContributeInfoNotExist.setContributorLastName( data.get( "contributorLastName" ).asText() );
        saInviteAndContributeInfoNotExist.setContributorEmailAddr( data.get( "contributorEmailAddress" ).asText() );
        saInviteAndContributeInfoNotExist.setContributionState( "contributed" );
        saInviteAndContributeInfoNotExist.setContributedDate( DateUtils.convert( data.get( "contributionDate" ).asText() ) );
        saInviteAndContributeInfoNotExist.setInternalOrExternal( data.get( "isInternal" ).asBoolean() );
        saInviteAndContributeInfoNotExist.setInvited( data.get( "isAutoInvited" ).asBoolean() );
        saInviteAndContributeInfoNotExist.getAuditUpdateInfo().setDateModified( new Timestamp( System.currentTimeMillis() ) );

        serviceAnniversaryService.saveSAInviteAndContributeInfo( saInviteAndContributeInfoNotExist );

      }

    }

  }

}
