
package com.biperf.core.value.approvals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.Factory;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import com.biperf.core.utils.StringUtil;

/**
 * Value bean for nominations approval tabular data result entry 
 * 
 * @author corneliu
 * @since Apr 22, 2016
 */
public class NominationsApprovalResultBean
{
  private String id;
  private String status;
  private String deinalReason;
  private String winnerMessage;
  private String moreinfoMessage;
  private String teamAward;
  private String award;
  private String notificationDate;
  private String timePeriod;
  private List<NominationsApprovalTeamAwardBean> team;
  private String level;// Client customization for WIP #56492

  public String getId()
  {
    return id;
  }

  public void setId( String id )
  {
    this.id = id;
  }

  public List<String> getIdList()
  {
    if ( StringUtils.isNotEmpty( id ) )
    {
      return Arrays.asList( id.split( "," ) );
    }
    return null;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getDeinalReason()
  {
    return ( (String)StringUtil.skipHTML( deinalReason ) ).replace( "\"", "\"\"" );
  }

  public void setDeinalReason( String deinalReason )
  {
    this.deinalReason = deinalReason;
  }

  public String getWinnerMessage()
  {
    return ( (String)StringUtil.skipHTML( winnerMessage ) ).replace( "\"", "\"\"" );
  }

  public void setWinnerMessage( String winnerMessage )
  {
    this.winnerMessage = winnerMessage;
  }

  public String getMoreinfoMessage()
  {
    return ( (String)StringUtil.skipHTML( moreinfoMessage ) ).replace( "\"", "\"\"" );
  }

  public void setMoreinfoMessage( String moreinfoMessage )
  {
    this.moreinfoMessage = moreinfoMessage;
  }

  public String getTeamAward()
  {
    return teamAward;
  }

  public void setTeamAward( String teamAward )
  {
    this.teamAward = teamAward;
  }

  public BigDecimal getConvertedTeamAward()
  {
    BigDecimal b1 = new BigDecimal( this.teamAward );
    return b1;
  }

  public String getAward()
  {
    return award;
  }

  public void setAward( String award )
  {
    this.award = award;
  }

  public BigDecimal getConvertedAward()
  {
    return StringUtils.isEmpty( this.award ) ? null : new BigDecimal( this.award.replaceAll( ",", "" ) );
  }

  public String getNotificationDate()
  {
    return notificationDate;
  }

  public void setNotificationDate( String notificationDate )
  {
    this.notificationDate = notificationDate;
  }

  public String getTimePeriod()
  {
    return timePeriod;
  }

  public void setTimePeriod( String timePeriod )
  {
    this.timePeriod = timePeriod;
  }

  public List<NominationsApprovalTeamAwardBean> getTeam()
  {
    if ( team == null )
    {
      Factory factory = new Factory()
      {
        public Object create()
        {
          return new NominationsApprovalTeamAwardBean();
        }
      };
      team = ListUtils.lazyList( new ArrayList<>(), factory );
    }
    return team;
  }

  public void setTeam( List<NominationsApprovalTeamAwardBean> team )
  {
    this.team = team;
  }
//Client customization for WIP #56492 starts
  public String getLevel()
  {
    return level;
  }

  public void setLevel( String level )
  {
    this.level = level;
  }
//Client customization for WIP #56492 ends 
  

}
