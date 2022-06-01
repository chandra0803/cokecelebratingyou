
package com.biperf.core.value.nomination;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;

/**
 * Represents the participant part of the JSON for nomination submission
 */
public class NominationsParticipantDataValueBean implements Serializable
{

  private static final long serialVersionUID = 1L;
  private List<ParticipantValueBean> participants;

  @SuppressWarnings( "unchecked" )
  public void addParticiapnt( ParticipantValueBean pax )
  {

    if ( participants == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new ParticipantValueBean();
        }
      };
      participants = ListUtils.lazyList( new ArrayList<>(), factory );
    }

    participants.add( pax );
  }

  @SuppressWarnings( "unchecked" )
  public List<ParticipantValueBean> getParticipants()
  {
    if ( participants == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new ParticipantValueBean();
        }
      };
      participants = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return participants;

  }

  public void setParticipants( List<ParticipantValueBean> participants )
  {
    this.participants = participants;
  }

  public static class ParticipantValueBean
  {

    private Long id;
    private String firstName;
    private String lastName;
    private String countryName;
    private String countryCode;
    private Double countryRatio;
    private String jobName;
    private String departmentName;
    private String orgName;

    private String awardQuantity;
    private List<NodeValueBean> nodes;

    public Long getId()
    {
      return id;
    }

    public void setId( Long id )
    {
      this.id = id;
    }

    public String getFirstName()
    {
      return firstName;
    }

    public void setFirstName( String firstName )
    {
      this.firstName = firstName;
    }

    public String getLastName()
    {
      return lastName;
    }

    public void setLastName( String lastName )
    {
      this.lastName = lastName;
    }

    public String getCountryCode()
    {
      return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
      this.countryCode = countryCode;
    }

    public String getCountryName()
    {
      return countryName;
    }

    public void setCountryName( String countryName )
    {
      this.countryName = countryName;
    }

    public Double getCountryRatio()
    {
      return countryRatio;
    }

    public void setCountryRatio( Double countryRatio )
    {
      this.countryRatio = countryRatio;
    }

    public String getJobName()
    {
      return jobName;
    }

    public void setJobName( String jobName )
    {
      this.jobName = jobName;
    }

    public String getDepartmentName()
    {
      return departmentName;
    }

    public void setDepartmentName( String departmentName )
    {
      this.departmentName = departmentName;
    }

    @SuppressWarnings( "unchecked" )
    public List<NodeValueBean> getNodes()
    {
      if ( nodes == null )
      {
        Factory factory = new Factory()
        {
          public Object create()
          {
            return new NodeValueBean();
          }
        };
        nodes = ListUtils.lazyList( new ArrayList<>(), factory );
      }
      return nodes;
    }

    public void setNodes( List<NodeValueBean> nodes )
    {
      this.nodes = nodes;
    }

    public String getAwardQuantity()
    {
      return awardQuantity;
    }

    public void setAwardQuantity( String awardQuantity )
    {
      this.awardQuantity = awardQuantity;
    }

    public BigDecimal getConvertedAwardQuantity()
    {
      if ( awardQuantity != null && !awardQuantity.isEmpty() )
      {
        return new BigDecimal( awardQuantity );
      }
      else
      {
        return null;
      }
    }

    public String getOrgName()
    {
      return orgName;
    }

    public void setOrgName( String orgName )
    {
      this.orgName = orgName;
    }

  }

  public static class NodeValueBean
  {

    private Integer id;
    private String name;

    public NodeValueBean()
    {
    }

    public NodeValueBean( Integer id, String name )
    {
      this.id = id;
      this.name = name;
    }

    public Integer getId()
    {
      return id;
    }

    public void setId( Integer id )
    {
      this.id = id;
    }

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
