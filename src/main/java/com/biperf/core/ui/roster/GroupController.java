
package com.biperf.core.ui.roster;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Controller;

import com.biperf.core.exception.BadRequestException;
import com.biw.digs.rest.request.GroupCreateRequest;
import com.biw.digs.rest.request.GroupSearchRequest;
import com.biw.digs.rest.response.GroupView;
import com.biw.digs.rest.response.PersonView;
import com.biw.digs.rest.service.GroupService;

@Controller
public class GroupController extends RosterBaseController implements GroupService
{
  @SuppressWarnings( "unchecked" )
  @Override
  public List<GroupView> getAllGroups( UUID companyId )
  {
    return buildGroupView( audienceService.getAudienceList() );
  }

  // TODO : Half implementation will re-work later based on the digs implementation.
  @Override
  public GroupView createGroup( UUID companyId, GroupCreateRequest request )
  {
    return createAudienceGroup( request );
  }

  @Override
  public GroupView getGroup( UUID companyId, UUID id )
  {
    return buildGroupView( audienceService.getAudienceByRosterAudienceId( id ) );
  }

  // TODO : Half implementation will re-work later based on the digs implementation.
  @Override
  public GroupView updateGroup( UUID companyId, UUID id, GroupCreateRequest request )
  {
    Long audienceId = audienceService.getAudienceIdByRosterAudienceId( id );

    if ( Objects.nonNull( audienceId ) )
    {
      return buildGroupView( audienceService.updateAudience( audienceId, request.getName(), request.getType(), request.isGroupPublic() ) );
    }
    else
    {
      return new GroupView();
    }

  }

  @Override
  @SuppressWarnings( "rawtypes" )
  public void deleteGroup( UUID companyId, UUID id )
  {
    Long audienceId = audienceService.getAudienceIdByRosterAudienceId( id );

    if ( Objects.nonNull( audienceId ) )
    {
      Map output = audienceService.deleteAudience( audienceId );

      if ( !GOOD.equals( output.get( OUTPUT_RETURN_CODE ) ) )
      {
        throw new BadRequestException( output.get( OUTPUT_RETURN_CODE ).toString() );
      }
    }
    else
    {
      throw new BadRequestException( "Invalid group id" );
    }

  }

  @Override
  public List<GroupView> searchGroups( UUID companyId, GroupSearchRequest request )
  {
    Long audienceId = audienceService.getAudienceIdByRosterAudienceId( request.getGroupId() );

    if ( Objects.nonNull( audienceId ) )
    {
      return buildGroupView( audienceService.getAudiencesForRosterSearchGroups( audienceId, request.getName(), request.getType() ) );
    }
    else
    {
      return new ArrayList<GroupView>();
    }
  }

  @Override
  public List<PersonView> getAllPersons( UUID companyId, UUID id )
  {
    Long audienceId = audienceService.getAudienceIdByRosterAudienceId( id );

    if ( Objects.nonNull( audienceId ) )
    {
      return buildPersonView( audienceService.getAllPersonsByAudienceId( audienceId ) );
    }
    else
    {
      return new ArrayList<PersonView>();
    }
  }

}
