
package com.biperf.core.ui.nomination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the participant part of the JSON for nomination submission
 */
public class NominationsParticipantDataViewBean implements Serializable
{

  private static final long serialVersionUID = 1L;
  private List<ParticipantViewBean> participants;

  @SuppressWarnings( "unchecked" )
  public void addParticiapnt( ParticipantViewBean pax )
  {

    if ( participants == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new ParticipantViewBean();
        }
      };
      participants = ListUtils.lazyList( new ArrayList<>(), factory );
    }

    participants.add( pax );
  }

  @SuppressWarnings( "unchecked" )
  @JsonProperty( "participants" )
  public List<ParticipantViewBean> getParticipants()
  {
    if ( participants == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new ParticipantViewBean();
        }
      };
      participants = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return participants;

  }

  public void setParticipants( List<ParticipantViewBean> participants )
  {
    this.participants = participants;
  }

  public static class ParticipantViewBean
  {

    private Long id;
    private String firstName;
    private String lastName;
    private String countryName;
    private String countryCode;
    private Double countryRatio;
    private String jobName;
    private String departmentName;

    private String awardQuantity;
    private List<NodeViewBean> nodes;

    @JsonProperty( "id" )
    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    @JsonProperty( "firstName" )
    public String getFirstName()
    {
      return firstName;
    }

    public void setFirstName( String firstName )
    {
      this.firstName = firstName;
    }

    @JsonProperty( "lastName" )
    public String getLastName()
    {
      return lastName;
    }

    public void setLastName( String lastName )
    {
      this.lastName = lastName;
    }

    @JsonProperty( "countryCode" )
    public String getCountryCode()
    {
      return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
      this.countryCode = countryCode;
    }

    @JsonProperty( "countryName" )
    public String getCountryName()
    {
      return countryName;
    }

    public void setCountryName( String countryName )
    {
      this.countryName = countryName;
    }

    @JsonProperty( "countryRatio" )
    public Double getCountryRatio()
    {
      return countryRatio;
    }

    public void setCountryRatio( Double countryRatio )
    {
      this.countryRatio = countryRatio;
    }

    @JsonProperty( "jobName" )
    public String getJobName()
    {
      return jobName;
    }

    public void setJobName( String jobName )
    {
      this.jobName = jobName;
    }

    @JsonProperty( "departmentName" )
    public String getDepartmentName()
    {
      return departmentName;
    }

    public void setDepartmentName( String departmentName )
    {
      this.departmentName = departmentName;
    }

    @SuppressWarnings( "unchecked" )
    @JsonProperty( "nodes" )
    public List<NodeViewBean> getNodes()
    {
      if ( nodes == null )
      {
        Factory factory = new Factory()
        {
          public Object create()
          {
            return new NodeViewBean();
          }
        };
        nodes = ListUtils.lazyList( new ArrayList<>(), factory );
      }
      return nodes;
    }

    public void setNodes( List<NodeViewBean> nodes )
    {
      this.nodes = nodes;
    }

    @JsonProperty( "awardQuantity" )
    public String getAwardQuantity()
    {
      return awardQuantity;
    }

    public void setAwardQuantity( String awardQuantity )
    {
      this.awardQuantity = awardQuantity;
    }

  }

  public static class NodeViewBean
  {

    private Integer id;
    private String name;

    public NodeViewBean()
    {
    }

    public NodeViewBean( Integer id, String name )
    {
      this.id = id;
      this.name = name;
    }

    @JsonProperty( "id" )
    public Integer getId()
    {
      return id;
    }

    public void setId( Integer id )
    {
      this.id = id;
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
  }

}
