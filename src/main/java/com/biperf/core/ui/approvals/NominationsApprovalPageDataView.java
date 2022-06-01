/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author poddutur
 * @since Apr 19, 2016
 */
// This annotation is used to not to serialize the null properties.
@JsonInclude( value = Include.NON_NULL )
public class NominationsApprovalPageDataView
{
  NominationPromotionApproval nominationPromotionApproval = new NominationPromotionApproval();

  @JsonProperty( "promotion" )
  public NominationPromotionApproval getNominationPromotionApproval()
  {
    return nominationPromotionApproval;
  }

  public void setNominationPromotionApproval( NominationPromotionApproval nominationPromotionApproval )
  {
    this.nominationPromotionApproval = nominationPromotionApproval;
  }

  public static class NominationPromotionApproval
  {
    @JsonProperty( "promoId" )
    private Long promoId;
    @JsonProperty( "name" )
    private String name;
    @JsonProperty( "isAdmin" )
    private Boolean admin;
    @JsonProperty( "isDelegate" )
    private Boolean delegate;
    @JsonProperty( "nodeId" )
    private Long nodeId;
    @JsonProperty( "finalLevelApprover" )
    private boolean finalLevelApprover;
    @JsonProperty( "totalPromotionCount" )
    private int totalPromotionCount;
    @JsonProperty( "timePeriodEnabled" )
    private boolean timePeriodEnabled;
    @JsonProperty( "notificationDateEnabled" )
    private boolean notificationDateEnabled;
    @JsonProperty( "payoutAtEachLevel" )
    private boolean payoutAtEachLevel;
    private List<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
    @JsonProperty( "status" )
    private String status;
    @JsonProperty( "currencyLabel" )
    private String currecnyLabel;
    @JsonProperty( "payoutType" )
    private String payoutType;
    @JsonProperty( "rulesText" )
    private String rulesText;
    @JsonProperty( "showConversionLink" )
    private boolean showConversionLink;
    private List<ApprovalLevel> approvalLevels = new ArrayList<ApprovalLevel>();

    public Long getPromoId()
    {
      return promoId;
    }

    public void setPromoId( Long promoId )
    {
      this.promoId = promoId;
    }

    public String getName()
    {
      return name;
    }

    public void setName( String name )
    {
      this.name = name;
    }

    public Boolean getAdmin()
    {
      return admin;
    }

    public void setAdmin( Boolean admin )
    {
      this.admin = admin;
    }
    
    public Boolean getDelegate()
    {
      return delegate;
    }

    public void setDelegate( Boolean delegate )
    {
      this.delegate = delegate;
    }

    public Long getNodeId()
    {
      return nodeId;
    }

    public void setNodeId( Long nodeId )
    {
      this.nodeId = nodeId;
    }

    public boolean isFinalLevelApprover()
    {
      return finalLevelApprover;
    }

    public void setFinalLevelApprover( boolean finalLevelApprover )
    {
      this.finalLevelApprover = finalLevelApprover;
    }

    public int getTotalPromotionCount()
    {
      return totalPromotionCount;
    }

    public void setTotalPromotionCount( int totalPromotionCount )
    {
      this.totalPromotionCount = totalPromotionCount;
    }

    public boolean isTimePeriodEnabled()
    {
      return timePeriodEnabled;
    }

    public void setTimePeriodEnabled( boolean timePeriodEnabled )
    {
      this.timePeriodEnabled = timePeriodEnabled;
    }

    public boolean isNotificationDateEnabled()
    {
      return notificationDateEnabled;
    }

    public void setNotificationDateEnabled( boolean notificationDateEnabled )
    {
      this.notificationDateEnabled = notificationDateEnabled;
    }

    public boolean isPayoutAtEachLevel()
    {
      return payoutAtEachLevel;
    }

    public void setPayoutAtEachLevel( boolean payoutAtEachLevel )
    {
      this.payoutAtEachLevel = payoutAtEachLevel;
    }

    @JsonProperty( "timePeriods" )
    public List<TimePeriod> getTimePeriods()
    {
      return timePeriods;
    }

    public void setTimePeriods( List<TimePeriod> timePeriods )
    {
      this.timePeriods = timePeriods;
    }

    public String getStatus()
    {
      return status;
    }

    public void setStatus( String status )
    {
      this.status = status;
    }

    public String getCurrecnyLabel()
    {
      return currecnyLabel;
    }

    public void setCurrecnyLabel( String currecnyLabel )
    {
      this.currecnyLabel = currecnyLabel;
    }

    public String getPayoutType()
    {
      return payoutType;
    }

    public void setPayoutType( String payoutType )
    {
      this.payoutType = payoutType;
    }

    public String getRulesText()
    {
      return rulesText;
    }

    public void setRulesText( String rulesText )
    {
      this.rulesText = rulesText;
    }

    public boolean isShowConversionLink()
    {
      return showConversionLink;
    }

    public void setShowConversionLink( boolean showConversionLink )
    {
      this.showConversionLink = showConversionLink;
    }

    @JsonProperty( "approvalLevels" )
    public List<ApprovalLevel> getApprovalLevels()
    {
      return approvalLevels;
    }

    public void setApprovalLevels( List<ApprovalLevel> approvalLevels )
    {
      this.approvalLevels = approvalLevels;
    }

    public static class TimePeriod
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "name" )
      private String name;

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
    }

    public static class ApprovalLevel
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "name" )
      private String name;
      @JsonProperty( "displayName" )
      private String displayName;
      @JsonProperty( "selected" )
      private boolean selected;

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

      public String getDisplayName()
      {
        return displayName;
      }

      public void setDisplayName( String displayName )
      {
        this.displayName = displayName;
      }

      public boolean isSelected()
      {
        return selected;
      }

      public void setSelected( boolean selected )
      {
        this.selected = selected;
      }
    }

  }

}
