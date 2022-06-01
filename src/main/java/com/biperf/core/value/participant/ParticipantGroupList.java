
package com.biperf.core.value.participant;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;

public class ParticipantGroupList
{

  private List<Group> groups = new ArrayList<ParticipantGroupList.Group>();

  @JsonProperty( "groups" )
  public List<Group> getGroups()
  {
    return groups;
  }

  public void setGroups( List<Group> groups )
  {
    this.groups = groups;
  }

  public void addGroup( Group group )
  {
    groups.add( group );
  }

  public int size()
  {
    return groups.size();
  }

  public static class Group
  {
    private Long id;
    private String name;
    private Long paxCount;
    private Set<Long> groupUserIds = new LinkedHashSet<Long>();

    public Group( Long id, String name, Long paxCount, Set<Long> groupUserIds )
    {
      this.id = id;
      this.name = name;
      this.paxCount = paxCount;
      this.groupUserIds = groupUserIds;
    }

    public Group()
    {
    }

    public Group( Long id, String name )
    {
      this.id = id;
      this.name = name;
    }

    @JsonProperty( "id" )
    public Long getId()
    {
      return id;
    }

    public void setId( Long ig )
    {
      this.id = ig;
    }

    @JsonProperty( "name" )
    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    @JsonProperty( "paxCount" )
    public Long getPaxCount()
    {
      return paxCount;
    }

    public void setPaxCount( Long paxCount )
    {
      this.paxCount = paxCount;
    }

    @JsonProperty( "groupUserIds" )
    public Set<Long> getGroupUserIds()
    {
      return groupUserIds;
    }

    public void setGroupUserIds( Set<Long> groupUserIds )
    {
      this.groupUserIds = groupUserIds;
    }

    public void addGroupUserId( Long userId )
    {
      if ( this.groupUserIds == null )
      {
        this.groupUserIds = Sets.newHashSet();
      }

      this.groupUserIds.add( userId );
    }

  }

}
