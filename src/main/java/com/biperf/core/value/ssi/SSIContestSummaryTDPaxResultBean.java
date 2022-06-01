
package com.biperf.core.value.ssi;

import java.util.List;

import com.biperf.core.domain.enums.SSIActivityMeasureType;
import com.biperf.core.utils.SSIContestUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * SSIContestAdminDetailTDResultActivityView.
 * 
 * @author chowdhur
 * @since Jan 13, 2015
 */
@JsonInclude( value = Include.NON_NULL )
public class SSIContestSummaryTDPaxResultBean
{
  private String contestType;
  private String activityMeasureType;
  private String payoutType;
  private Long userId;
  private Long orgUnitId;
  private String orgUnit;
  private String clientState;
  private String lastName;
  private String firstName;
  private Double currentActivity; // objectives, step it up
  private Long contestId;

  // objective
  private Double objective;
  private Long percentToObjective;
  private Long objectivePayout; // payoutType = points
  private Long bonusAmount;
  private Long totalPotentialPayout;
  private Long payoutQuantity; // payoutType = other
  private Long payoutValue;// payoutType = other
  private Long payoutAmount; // Objective, DTGT, Step it up

  // do this get that
  private Long activity1Id;
  private Long payoutValue1;
  private String activityDescription1;
  private Double activityAmount1;
  private Long payoutQuantity1;

  private Long activity2Id;
  private Long payoutValue2;
  private String activityDescription2;
  private Double activityAmount2;
  private Long payoutQuantity2;

  private Long activity3Id;
  private Long payoutValue3;
  private String activityDescription3;
  private Double activityAmount3;
  private Long payoutQuantity3;

  private Long activity4Id;
  private Long payoutValue4;
  private String activityDescription4;
  private Double activityAmount4;
  private Long payoutQuantity4;

  private Long activity5Id;
  private Long payoutValue5;
  private String activityDescription5;
  private Double activityAmount5;
  private Long payoutQuantity5;

  private Long activity6Id;
  private Long payoutValue6;
  private String activityDescription6;
  private Double activityAmount6;
  private Long payoutQuantity6;

  private Long activity7Id;
  private Long payoutValue7;
  private String activityDescription7;
  private Double activityAmount7;
  private Long payoutQuantity7;

  private Long activity8Id;
  private Long payoutValue8;
  private String activityDescription8;
  private Double activityAmount8;
  private Long payoutQuantity8;

  private Long activity9Id;
  private Long payoutValue9;
  private String activityDescription9;
  private Double activityAmount9;
  private Long payoutQuantity9;

  private Long activity10Id;
  private Long payoutValue10;
  private String activityDescription10;
  private Double activityAmount10;
  private Long payoutQuantity10;

  private Long totalPayoutValue;

  private List<SSIContestSummaryTDResultActivityBean> activityDescription;

  // Step it up
  private Long levelCompleted;
  private Long levelPayout;
  private Double baseline;
  private String payoutDescription;

  private String contestUrl;

  public String getPayoutDescription()
  {
    return payoutDescription;
  }

  public void setPayoutDescription( String payoutDescription )
  {
    this.payoutDescription = payoutDescription;
  }

  public SSIContestSummaryTDPaxResultBean()
  {

  }

  public String getContestType()
  {
    return contestType;
  }

  public void setContestType( String contestType )
  {
    this.contestType = contestType;
  }

  @JsonIgnore
  public String getActivityMeasureType()
  {
    return activityMeasureType;
  }

  public void setActivityMeasureType( String activityMeasureType )
  {
    this.activityMeasureType = activityMeasureType;
  }

  @JsonIgnore
  public String getPayoutType()
  {
    return payoutType;
  }

  public void setPayoutType( String payoutType )
  {
    this.payoutType = payoutType;
  }

  public Long getUserId()
  {
    return userId;
  }

  public void setUserId( Long userId )
  {
    this.userId = userId;
  }

  public String getClientState()
  {
    return clientState;
  }

  public void setClientState( String clientState )
  {
    this.clientState = clientState;
  }

  public String getLastName()
  {
    return lastName;
  }

  public void setLastName( String lastName )
  {
    this.lastName = lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

  public void setFirstName( String firstName )
  {
    this.firstName = firstName;
  }

  @JsonProperty( "participantName" )
  public String getFullName()
  {
    return this.lastName + ", " + this.firstName;
  }

  public void setObjective( Double objective )
  {
    this.objective = objective;
  }

  @JsonIgnore
  public Double getObjective()
  {
    return objective;
  }

  @JsonProperty( "objective" )
  public String getDisplayObjective()
  {
    if ( SSIActivityMeasureType.CURRENCY_CODE.equals( this.activityMeasureType ) )
    {
      return SSIContestUtil.getFormattedValue( this.objective, SSIContestUtil.ACTIVITY_CURRENCY_DECIMAL_PRECISION );
    }
    else
    {
      return SSIContestUtil.getFormattedValue( this.objective, SSIContestUtil.ACTIVITY_UNITS_DECIMAL_PRECISION );
    }
  }

  public void setCurrentActivity( Double currentActivity )
  {
    this.currentActivity = currentActivity;
  }

  @JsonIgnore
  public Double getCurrentActivity()
  {
    return currentActivity;
  }

  @JsonProperty( "currentActivity" )
  public String getDisplayCurrentActivity()
  {
    if ( SSIActivityMeasureType.CURRENCY_CODE.equals( this.activityMeasureType ) )
    {
      return SSIContestUtil.getFormattedValue( this.currentActivity, SSIContestUtil.ACTIVITY_CURRENCY_DECIMAL_PRECISION );
    }
    else
    {
      return SSIContestUtil.getFormattedValue( this.currentActivity, SSIContestUtil.ACTIVITY_UNITS_DECIMAL_PRECISION );
    }
  }

  public String getPercentToObjective()
  {
    return percentToObjective != null ? percentToObjective + "%" : null;
  }

  public Long getObjectivePayout()
  {
    return objectivePayout;
  }

  public void setObjectivePayout( Long objectivePayout )
  {
    this.objectivePayout = objectivePayout;
  }

  public Long getBonusAmount()
  {
    return bonusAmount;
  }

  public void setBonusAmount( Long bonusAmount )
  {
    this.bonusAmount = bonusAmount;
  }

  public Long getTotalPotentialPayout()
  {
    return totalPotentialPayout;
  }

  public void setTotalPotentialPayout( Long totalPotentialPayout )
  {
    this.totalPotentialPayout = totalPotentialPayout;
  }

  public Long getPayoutQuantity()
  {
    return payoutQuantity;
  }

  public void setPayoutQuantity( Long payoutQuantity )
  {
    this.payoutQuantity = payoutQuantity;
  }

  public Long getPayoutValue()
  {
    return payoutValue;
  }

  public void setPayoutValue( Long payoutValue )
  {
    this.payoutValue = payoutValue;
  }

  public Long getPayoutAmount()
  {
    return payoutAmount;
  }

  public void setPayoutAmount( Long payoutAmount )
  {
    this.payoutAmount = payoutAmount;
  }

  public void setPercentToObjective( Long percentToObjective )
  {
    this.percentToObjective = percentToObjective;
  }

  public Long getOrgUnitId()
  {
    return orgUnitId;
  }

  public void setOrgUnitId( Long orgUnitId )
  {
    this.orgUnitId = orgUnitId;
  }

  public String getOrgUnit()
  {
    return orgUnit;
  }

  public void setOrgUnit( String orgUnit )
  {
    this.orgUnit = orgUnit;
  }

  public Long getActivity1Id()
  {
    return activity1Id;
  }

  public void setActivity1Id( Long activity1Id )
  {
    this.activity1Id = activity1Id;
  }

  public Long getPayoutValue1()
  {
    return payoutValue1;
  }

  public void setPayoutValue1( Long payoutValue1 )
  {
    this.payoutValue1 = payoutValue1;
  }

  public Double getActivityAmount1()
  {
    return activityAmount1;
  }

  public void setActivityAmount1( Double activityAmount1 )
  {
    this.activityAmount1 = activityAmount1;
  }

  public Long getPayoutQuantity1()
  {
    return payoutQuantity1;
  }

  public void setPayoutQuantity1( Long payoutQuantity1 )
  {
    this.payoutQuantity1 = payoutQuantity1;
  }

  public Long getActivity2Id()
  {
    return activity2Id;
  }

  public void setActivity2Id( Long activity2Id )
  {
    this.activity2Id = activity2Id;
  }

  public Long getPayoutValue2()
  {
    return payoutValue2;
  }

  public void setPayoutValue2( Long payoutValue2 )
  {
    this.payoutValue2 = payoutValue2;
  }

  public Double getActivityAmount2()
  {
    return activityAmount2;
  }

  public void setActivityAmount2( Double activityAmount2 )
  {
    this.activityAmount2 = activityAmount2;
  }

  public Long getPayoutQuantity2()
  {
    return payoutQuantity2;
  }

  public void setPayoutQuantity2( Long payoutQuantity2 )
  {
    this.payoutQuantity2 = payoutQuantity2;
  }

  public Long getActivity3Id()
  {
    return activity3Id;
  }

  public void setActivity3Id( Long activity3Id )
  {
    this.activity3Id = activity3Id;
  }

  public Long getPayoutValue3()
  {
    return payoutValue3;
  }

  public void setPayoutValue3( Long payoutValue3 )
  {
    this.payoutValue3 = payoutValue3;
  }

  public Double getActivityAmount3()
  {
    return activityAmount3;
  }

  public void setActivityAmount3( Double activityAmount3 )
  {
    this.activityAmount3 = activityAmount3;
  }

  public Long getPayoutQuantity3()
  {
    return payoutQuantity3;
  }

  public void setPayoutQuantity3( Long payoutQuantity3 )
  {
    this.payoutQuantity3 = payoutQuantity3;
  }

  public Long getActivity4Id()
  {
    return activity4Id;
  }

  public void setActivity4Id( Long activity4Id )
  {
    this.activity4Id = activity4Id;
  }

  public Long getPayoutValue4()
  {
    return payoutValue4;
  }

  public void setPayoutValue4( Long payoutValue4 )
  {
    this.payoutValue4 = payoutValue4;
  }

  public Double getActivityAmount4()
  {
    return activityAmount4;
  }

  public void setActivityAmount4( Double activityAmount4 )
  {
    this.activityAmount4 = activityAmount4;
  }

  public Long getPayoutQuantity4()
  {
    return payoutQuantity4;
  }

  public void setPayoutQuantity4( Long payoutQuantity4 )
  {
    this.payoutQuantity4 = payoutQuantity4;
  }

  public Long getActivity5Id()
  {
    return activity5Id;
  }

  public void setActivity5Id( Long activity5Id )
  {
    this.activity5Id = activity5Id;
  }

  public Long getPayoutValue5()
  {
    return payoutValue5;
  }

  public void setPayoutValue5( Long payoutValue5 )
  {
    this.payoutValue5 = payoutValue5;
  }

  public Double getActivityAmount5()
  {
    return activityAmount5;
  }

  public void setActivityAmount5( Double activityAmount5 )
  {
    this.activityAmount5 = activityAmount5;
  }

  public Long getPayoutQuantity5()
  {
    return payoutQuantity5;
  }

  public void setPayoutQuantity5( Long payoutQuantity5 )
  {
    this.payoutQuantity5 = payoutQuantity5;
  }

  public Long getActivity6Id()
  {
    return activity6Id;
  }

  public void setActivity6Id( Long activity6Id )
  {
    this.activity6Id = activity6Id;
  }

  public Long getPayoutValue6()
  {
    return payoutValue6;
  }

  public void setPayoutValue6( Long payoutValue6 )
  {
    this.payoutValue6 = payoutValue6;
  }

  public Double getActivityAmount6()
  {
    return activityAmount6;
  }

  public void setActivityAmount6( Double activityAmount6 )
  {
    this.activityAmount6 = activityAmount6;
  }

  public Long getPayoutQuantity6()
  {
    return payoutQuantity6;
  }

  public void setPayoutQuantity6( Long payoutQuantity6 )
  {
    this.payoutQuantity6 = payoutQuantity6;
  }

  public Long getActivity7Id()
  {
    return activity7Id;
  }

  public void setActivity7Id( Long activity7Id )
  {
    this.activity7Id = activity7Id;
  }

  public Long getPayoutValue7()
  {
    return payoutValue7;
  }

  public void setPayoutValue7( Long payoutValue7 )
  {
    this.payoutValue7 = payoutValue7;
  }

  public Double getActivityAmount7()
  {
    return activityAmount7;
  }

  public void setActivityAmount7( Double activityAmount7 )
  {
    this.activityAmount7 = activityAmount7;
  }

  public Long getPayoutQuantity7()
  {
    return payoutQuantity7;
  }

  public void setPayoutQuantity7( Long payoutQuantity7 )
  {
    this.payoutQuantity7 = payoutQuantity7;
  }

  public Long getActivity8Id()
  {
    return activity8Id;
  }

  public void setActivity8Id( Long activity8Id )
  {
    this.activity8Id = activity8Id;
  }

  public Long getPayoutValue8()
  {
    return payoutValue8;
  }

  public void setPayoutValue8( Long payoutValue8 )
  {
    this.payoutValue8 = payoutValue8;
  }

  public Double getActivityAmount8()
  {
    return activityAmount8;
  }

  public void setActivityAmount8( Double activityAmount8 )
  {
    this.activityAmount8 = activityAmount8;
  }

  public Long getPayoutQuantity8()
  {
    return payoutQuantity8;
  }

  public void setPayoutQuantity8( Long payoutQuantity8 )
  {
    this.payoutQuantity8 = payoutQuantity8;
  }

  public Long getActivity9Id()
  {
    return activity9Id;
  }

  public void setActivity9Id( Long activity9Id )
  {
    this.activity9Id = activity9Id;
  }

  public Long getPayoutValue9()
  {
    return payoutValue9;
  }

  public void setPayoutValue9( Long payoutValue9 )
  {
    this.payoutValue9 = payoutValue9;
  }

  public Double getActivityAmount9()
  {
    return activityAmount9;
  }

  public void setActivityAmount9( Double activityAmount9 )
  {
    this.activityAmount9 = activityAmount9;
  }

  public Long getPayoutQuantity9()
  {
    return payoutQuantity9;
  }

  public void setPayoutQuantity9( Long payoutQuantity9 )
  {
    this.payoutQuantity9 = payoutQuantity9;
  }

  public Long getActivity10Id()
  {
    return activity10Id;
  }

  public void setActivity10Id( Long activity10Id )
  {
    this.activity10Id = activity10Id;
  }

  public Long getPayoutValue10()
  {
    return payoutValue10;
  }

  public void setPayoutValue10( Long payoutValue10 )
  {
    this.payoutValue10 = payoutValue10;
  }

  public Double getActivityAmount10()
  {
    return activityAmount10;
  }

  public void setActivityAmount10( Double activityAmount10 )
  {
    this.activityAmount10 = activityAmount10;
  }

  public Long getPayoutQuantity10()
  {
    return payoutQuantity10;
  }

  public void setPayoutQuantity10( Long payoutQuantity10 )
  {
    this.payoutQuantity10 = payoutQuantity10;
  }

  public String getActivityDescription1()
  {
    return activityDescription1;
  }

  public void setActivityDescription1( String activityDescription1 )
  {
    this.activityDescription1 = activityDescription1;
  }

  public String getActivityDescription2()
  {
    return activityDescription2;
  }

  public void setActivityDescription2( String activityDescription2 )
  {
    this.activityDescription2 = activityDescription2;
  }

  public String getActivityDescription3()
  {
    return activityDescription3;
  }

  public void setActivityDescription3( String activityDescription3 )
  {
    this.activityDescription3 = activityDescription3;
  }

  public String getActivityDescription4()
  {
    return activityDescription4;
  }

  public void setActivityDescription4( String activityDescription4 )
  {
    this.activityDescription4 = activityDescription4;
  }

  public String getActivityDescription5()
  {
    return activityDescription5;
  }

  public void setActivityDescription5( String activityDescription5 )
  {
    this.activityDescription5 = activityDescription5;
  }

  public String getActivityDescription6()
  {
    return activityDescription6;
  }

  public void setActivityDescription6( String activityDescription6 )
  {
    this.activityDescription6 = activityDescription6;
  }

  public String getActivityDescription7()
  {
    return activityDescription7;
  }

  public void setActivityDescription7( String activityDescription7 )
  {
    this.activityDescription7 = activityDescription7;
  }

  public String getActivityDescription8()
  {
    return activityDescription8;
  }

  public void setActivityDescription8( String activityDescription8 )
  {
    this.activityDescription8 = activityDescription8;
  }

  public String getActivityDescription9()
  {
    return activityDescription9;
  }

  public void setActivityDescription9( String activityDescription9 )
  {
    this.activityDescription9 = activityDescription9;
  }

  public String getActivityDescription10()
  {
    return activityDescription10;
  }

  public void setActivityDescription10( String activityDescription10 )
  {
    this.activityDescription10 = activityDescription10;
  }

  public Long getTotalPayoutValue()
  {
    return totalPayoutValue;
  }

  public void setTotalPayoutValue( Long totalPayoutValue )
  {
    this.totalPayoutValue = totalPayoutValue;
  }

  public List<SSIContestSummaryTDResultActivityBean> getActivityDescription()
  {
    return activityDescription;
  }

  public void setActivityDescription( List<SSIContestSummaryTDResultActivityBean> activityDescription )
  {
    this.activityDescription = activityDescription;
  }

  public Long getLevelCompleted()
  {
    return levelCompleted;
  }

  public void setLevelCompleted( Long levelCompleted )
  {
    this.levelCompleted = levelCompleted;
  }

  public Long getLevelPayout()
  {
    return levelPayout;
  }

  public void setLevelPayout( Long levelPayout )
  {
    this.levelPayout = levelPayout;
  }

  public Double getBaseline()
  {
    return baseline;
  }

  public void setBaseline( Double baseline )
  {
    this.baseline = baseline;
  }

  public String getContestUrl()
  {
    return SSIContestUtil.populateParticipantDetailPageUrl( this.contestId, this.userId );
  }

  public void setContestUrl( String contestUrl )
  {
    this.contestUrl = contestUrl;
  }

  public Long getContestId()
  {
    return contestId;
  }

  public void setContestId( Long contestId )
  {
    this.contestId = contestId;
  }

}
