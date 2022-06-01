/*
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/participant/AudienceForm.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.ui.participant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.ui.BaseForm;

/**
 * AudienceForm.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>lee</td>
 * <td>Jun 9, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AudienceForm extends BaseForm
{
  /** Log */
  private static final Log logger = LogFactory.getLog( AudienceForm.class );

  private Long id;
  private String name;
  private String audienceType;
  private String code;
  private List audienceParticipants = null;
  private Set audienceCriterias = null;
  private String method;
  private Long version;
  private String createdBy;
  private long dateCreated;

  public void load( Audience audience )
  {

    this.setId( audience.getId() );
    this.setName( audience.getName() );

    if ( audience.getAudienceType() != null )
    {
      this.setAudienceType( audience.getAudienceType().getCode() );
    }

    this.setVersion( audience.getVersion() );
    this.setCreatedBy( audience.getAuditCreateInfo().getCreatedBy().toString() );
    this.setDateCreated( audience.getAuditCreateInfo().getDateCreated().getTime() );
  }

  public Audience toDomainObject()
  {

    PaxAudience paxAudience = null;
    CriteriaAudience criteriaAudience = null;

    if ( this.getAudienceType().equals( "pax" ) )
    {

      paxAudience = new PaxAudience();
      paxAudience.setAudienceParticipants( this.audienceParticipants );
      paxAudience.setId( this.getId() );
      paxAudience.setName( this.getName() );
      paxAudience.setVersion( this.getVersion() );
      paxAudience.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
      paxAudience.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );

      return paxAudience;

    }

    criteriaAudience = new CriteriaAudience();
    criteriaAudience.setAudienceCriterias( this.audienceCriterias );
    criteriaAudience.setId( this.getId() );
    criteriaAudience.setName( this.getName() );
    criteriaAudience.setVersion( this.getVersion() );
    criteriaAudience.getAuditCreateInfo().setCreatedBy( Long.valueOf( this.createdBy ) );
    criteriaAudience.getAuditCreateInfo().setDateCreated( new Timestamp( this.dateCreated ) );

    return criteriaAudience;

  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
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

  public String getCode()
  {
    return code;
  }

  public void setCode( String code )
  {
    this.code = code;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }

  public String getCreatedBy()
  {
    return createdBy;
  }

  public void setCreatedBy( String createdBy )
  {
    this.createdBy = createdBy;
  }

  public long getDateCreated()
  {
    return dateCreated;
  }

  public void setDateCreated( long dateCreated )
  {
    this.dateCreated = dateCreated;
  }

  public String getAudienceType()
  {
    return audienceType;
  }

  public void setAudienceType( String audienceType )
  {
    this.audienceType = audienceType;
  }

  public AudienceParticipant getAudienceParticipant( int index )
  {
    try
    {
      return (AudienceParticipant)this.getAudienceParticipants().get( index );
    }
    catch( Exception exception )
    {
      return null;
    }
  }

  public CriteriaAudience getAudienceCriteriaCharacteristic( int index )
  {
    ArrayList criteriaAudienceList = new ArrayList( this.getAudienceCriterias() );

    try
    {
      return (CriteriaAudience)criteriaAudienceList.get( index );
    }
    catch( Exception exception )
    {
      return null;
    }
  }

  public void reset( ActionMapping anActionMapping, HttpServletRequest request )
  {
    logger.info( ">>> reset" );

    int participantCount = 0;
    int criteriaCount = 0;

    if ( ! ( request.getParameter( "participantCount" ) == null ) )
    {
      participantCount = Integer.parseInt( request.getParameter( "participantCount" ) );
    }

    if ( participantCount > 0 )
    {
      audienceParticipants = new ArrayList();
      for ( int i = 0; i < participantCount; i++ )
      {
        audienceParticipants.add( new AudienceParticipant() );
      }
    }

    if ( ! ( request.getParameter( "criteriaCount" ) == null ) )
    {
      criteriaCount = Integer.parseInt( request.getParameter( "criteriaCount" ) );
    }

    if ( criteriaCount > 0 )
    {
      audienceCriterias = new HashSet();
      for ( int i = 0; i < criteriaCount; i++ )
      {
        audienceCriterias.add( new AudienceCriteriaCharacteristic() );
      }
    }

    logger.info( "<<< reset" );
  }

  public Set getAudienceCriterias()
  {
    return audienceCriterias;
  }

  public void setAudienceCriterias( Set audienceCriterias )
  {
    this.audienceCriterias = audienceCriterias;
  }

  public List getAudienceParticipants()
  {
    return audienceParticipants;
  }

  public void setAudienceParticipants( List audienceParticipants )
  {
    this.audienceParticipants = audienceParticipants;
  }

}
