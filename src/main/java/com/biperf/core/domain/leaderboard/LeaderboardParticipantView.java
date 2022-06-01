/*
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.domain.leaderboard;

import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.DynaPickListType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.utils.BeanLocator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * ParticipantSearchView.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>putta</th>
 * <th>12/03/2012</th>
 * <th>1.0</th>
 *
 */
@JsonInclude( value = Include.NON_NULL )
public class LeaderboardParticipantView extends ParticipantInfoView implements Comparable
{
  private static final long serialVersionUID = 1L;
  private Integer score;
  private Integer newScore;
  private String organization;
  private String departmentName;
  private String jobName;

  public LeaderboardParticipantView( Participant pax )
  {

    setId( pax.getId() );
    setFirstName( pax.getFirstName() );
    setLastName( pax.getLastName() );
    setCountryDtls( pax );
    setAvatarUrl( pax.getAvatarSmall() );
    setOrganization( pax.getPaxOrgName() );
    setDepartmentName( pax.getDepartmentType() );
    setJobName( pax.getPositionType() );
    setCountryRatio( 1D );
  }

  public void setCountryDtls( Participant pax )
  {
    Country country = getUserService().getPrimaryUserAddressCountry( pax.getId() );
    this.setCountryCode( country.getCountryCode() );
    this.setCountryName( country.getCountryName() );

  }


  public LeaderboardParticipantView()
  {

  }

  @JsonIgnore
  public Integer getScore()
  {
    return score;
  }

  public void setScore( Integer score )
  {
    this.score = score;
  }

  public Integer getNewScore()
  {
    return newScore;
  }

  public void setNewScore( Integer newScore )
  {
    this.newScore = newScore;
  }

  public int compareTo( Object object ) throws ClassCastException
  {
    if ( ! ( object instanceof LeaderboardParticipantView ) )
    {
      throw new ClassCastException( "A LeaderboardParticipantView was expected." );
    }
    LeaderboardParticipantView leaderBoardView = (LeaderboardParticipantView)object;
    return this.getNewScore().compareTo( leaderBoardView.getNewScore() );

  }

  @JsonIgnore
  public String getFullName()
  {
    return getLastName() + getFirstName();
  }

  public void setOrganization( String organization )
  {
    this.organization = organization;
  }

  public String getOrganization()
  {
    return this.organization;
  }

  public String getDepartmentName()
  {
    return departmentName;
  }

  public void setDepartmentName( String departmentType )
  {
    if ( departmentType != null )
    {
      DynaPickListType departmentItem = DynaPickListType.lookup( DepartmentType.PICKLIST_ASSET + DepartmentType.ASSET_ITEM_SUFFIX, departmentType );
      if ( departmentItem != null )
      {
        this.departmentName = departmentItem.getName();
      }
    }
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName( String positionType )
  {
    if ( positionType != null )
    {
      DynaPickListType jobPositionItem = DynaPickListType.lookup( PositionType.PICKLIST_ASSET + PositionType.ASSET_ITEM_SUFFIX, positionType );
      if ( jobPositionItem != null )
      {
        this.jobName = jobPositionItem.getName();
      }
    }
  }


  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

}
