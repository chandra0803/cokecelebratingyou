
package com.biperf.core.domain.promotion;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import com.biperf.core.domain.BaseDomain;
import com.biperf.core.domain.enums.MatchTeamOutcomeType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.objectpartners.cms.util.CmsResourceBundle;

@SuppressWarnings( "serial" )
public class Division extends BaseDomain implements Cloneable
{
  // SAVE DIVISION NAME IN CM
  public static final String DIVISION_NAME_SECTION_CODE = "division_name";
  public static final String DIVISION_NAME_CM_ASSET_TYPE_NAME = "_DIVISION_NAME";
  public static final String DIVISION_NAME_KEY_DESC = "Division Name";
  public static final String DIVISION_NAME_KEY_PREFIX = "DIVISION_NAME_";
  public static final String DIVISION_CM_ASSET_PREFIX = "division_name.";

  private transient String divisionName;
  private ThrowdownPromotion promotion;
  private String divisionNameAssetCode = "";
  private BigDecimal minimumQualifier;
  private Set<Team> teams;

  @JsonManagedReference
  private Set<DivisionPayout> payouts = new HashSet<DivisionPayout>();

  private Set<Round> rounds = new HashSet<Round>();
  private Set<DivisionCompetitorsAudience> competitorsAudience = new HashSet<DivisionCompetitorsAudience>();

  public BigDecimal getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public BigDecimal getMinimumQualifierWithPrecisionAndRounding()
  {
    int precision = promotion.getAchievementPrecision().getPrecision();
    int roundingMode = promotion.getRoundingMethod().getBigDecimalRoundingMode();
    BigDecimal roundedMinimumQualifierValue = minimumQualifier.setScale( precision, roundingMode );
    return roundedMinimumQualifierValue;
  }

  public void setMinimumQualifier( BigDecimal minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public String getDivisionNameFromCM()
  {
    String divisionName = null;
    if ( this.divisionNameAssetCode != null )
    {
      divisionName = CmsResourceBundle.getCmsBundle().getString( this.divisionNameAssetCode, Division.DIVISION_NAME_KEY_PREFIX );
    }
    return StringEscapeUtils.unescapeHtml4( divisionName );
  }

  public String getDivisionNameAssetCode()
  {
    return divisionNameAssetCode;
  }

  public String getDivisionNameForCM()
  {
    return divisionName;
  }

  public String getDivisionName()
  {
    return getDivisionNameFromCM();
  }

  public void setDivisionName( String divisionName )
  {
    this.divisionName = divisionName;
  }

  public void setDivisionNameAssetCode( String divisionNameAssetCode )
  {
    this.divisionNameAssetCode = divisionNameAssetCode;
  }

  public Set<DivisionPayout> getPayouts()
  {
    return payouts;
  }

  public DivisionPayout getPayoutForOutcome( MatchTeamOutcomeType outcome )
  {
    if ( payouts != null && !payouts.isEmpty() )
    {
      for ( DivisionPayout payout : payouts )
      {
        if ( payout.getOutcome().equals( outcome ) )
        {
          return payout;
        }
      }
    }
    return null;
  }

  public void setPayouts( Set<DivisionPayout> payouts )
  {
    this.payouts = payouts;
  }

  @JsonIgnore
  public ThrowdownPromotion getPromotion()
  {
    return promotion;
  }

  @JsonIgnore
  public void setPromotion( ThrowdownPromotion promotion )
  {
    this.promotion = promotion;
  }

  @JsonIgnore
  public Set<Round> getRounds()
  {
    return rounds;
  }

  @JsonIgnore
  public void setRounds( Set<Round> rounds )
  {
    this.rounds = rounds;
  }

  @JsonIgnore
  public Set<DivisionCompetitorsAudience> getCompetitorsAudience()
  {
    return competitorsAudience;
  }

  @JsonIgnore
  public void setCompetitorsAudience( Set<DivisionCompetitorsAudience> competitorsAudience )
  {
    this.competitorsAudience = competitorsAudience;
  }

  public void addDivisionPayout( DivisionPayout divisionPayout )
  {
    divisionPayout.setDivision( this );
    this.payouts.add( divisionPayout );
  }

  public void addRound( Round round )
  {
    round.setDivision( this );
    this.rounds.add( round );
  }

  @JsonIgnore
  public void addDivisionCompetitorsAudience( DivisionCompetitorsAudience competitorsAudience )
  {
    competitorsAudience.setDivision( this );
    this.competitorsAudience.add( competitorsAudience );
  }

  public boolean equals( Object o )
  {
    if ( this == o )
    {
      return true;
    }
    if ( ! ( o instanceof Division ) )
    {
      return false;
    }

    final Division div = (Division)o;

    if ( getPromotion() != null ? !getPromotion().equals( div.getPromotion() ) : div.getPromotion() != null )
    {
      return false;
    }
    if ( getDivisionNameAssetCode() != null ? !getDivisionNameAssetCode().equals( div.getDivisionNameAssetCode() ) : div.getDivisionNameAssetCode() != null )
    {
      return false;
    }

    if ( getId() != null && div.getId() != null )
    {
      return getId().equals( div.getId() );
    }

    if ( getDivisionNameForCM() != null && div.getDivisionNameForCM() != null )
    {
      return getDivisionNameForCM().equals( div.getDivisionNameForCM() );
    }
    else
    {
      return false;
    }
  }

  public int hashCode()
  {
    int result;
    result = getPromotion() != null ? getPromotion().hashCode() : 0;
    result = 31 * result + ( getDivisionNameAssetCode() != null ? getDivisionNameAssetCode().hashCode() : 0 );
    result = result + ( getDivisionName() != null ? getDivisionName().hashCode() : 0 );

    return result;
  }

  /**
   * Clones this, removes the auditInfo and Id and clones the divisions if applicable.
   * Overridden from
   * 
   * @see java.lang.Object#clone()
   * @return Object
   * @throws CloneNotSupportedException
   */
  public Object clone() throws CloneNotSupportedException
  {
    Division clonedDivision = (Division)super.clone();
    clonedDivision.resetBaseDomain();

    // copy the payouts
    clonedDivision.setPayouts( new HashSet<DivisionPayout>() );
    for ( DivisionPayout payout : this.getPayouts() )
    {
      clonedDivision.addDivisionPayout( (DivisionPayout)payout.clone() );
    }

    clonedDivision.setRounds( new HashSet<Round>() );

    clonedDivision.setTeams( new HashSet<Team>() );

    // copy the DivisionCompetitorAudience
    clonedDivision.setCompetitorsAudience( new HashSet<DivisionCompetitorsAudience>() );
    for ( DivisionCompetitorsAudience competitor : this.getCompetitorsAudience() )
    {
      clonedDivision.addDivisionCompetitorsAudience( (DivisionCompetitorsAudience)competitor.clone() );
    }
    return clonedDivision;
  }

  @JsonIgnore
  public Set<Team> getTeams()
  {
    return teams;
  }

  @JsonIgnore
  public void setTeams( Set<Team> teams )
  {
    this.teams = teams;
  }

  public void addTeam( Team team )
  {
    team.setDivision( this );
    this.teams.add( team );
  }
}
