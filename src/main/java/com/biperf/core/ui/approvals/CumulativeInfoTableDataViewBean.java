/*
 * (c) 2016 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.ui.approvals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author poddutur
 * @since Sep 12, 2016
 */
public class CumulativeInfoTableDataViewBean
{
  private List<CumulativeInfoView> cumulativeInfoList = new ArrayList<CumulativeInfoView>();

  @JsonProperty( "cumulativeInfo" )
  public List<CumulativeInfoView> getCumulativeInfoList()
  {
    return cumulativeInfoList;
  }

  public void setCumulativeInfoList( List<CumulativeInfoView> cumulativeInfoList )
  {
    this.cumulativeInfoList = cumulativeInfoList;
  }

  public static class CumulativeInfoView
  {
    @JsonProperty( "claimId" )
    private Long claimId;
    @JsonProperty( "dateSubmitted" )
    private Date dateSubmitted;
    @JsonProperty( "nominatorId" )
    private Long nominatorId;
    @JsonProperty( "nominator" )
    private String nominator;
    @JsonProperty( "moreInfo" )
    private String moreInfo;
    @JsonProperty( "reason" )
    private String reason;
    @JsonProperty( "attachmentUrl" )
    private String whyAttachmentUrl;
    @JsonProperty( "attachmentName" )
    private String whyAttachmentName;
    @JsonProperty( "eCertUrl" )
    private String eCertUrl;
    private List<CustomFieldView> customFields = new ArrayList<CustomFieldView>();

    public Long getClaimId()
    {
      return claimId;
    }

    public void setClaimId( Long claimId )
    {
      this.claimId = claimId;
    }

    public Date getDateSubmitted()
    {
      return dateSubmitted;
    }

    public void setDateSubmitted( Date dateSubmitted )
    {
      this.dateSubmitted = dateSubmitted;
    }

    public Long getNominatorId()
    {
      return nominatorId;
    }

    public void setNominatorId( Long nominatorId )
    {
      this.nominatorId = nominatorId;
    }

    public String getNominator()
    {
      return nominator;
    }

    public void setNominator( String nominator )
    {
      this.nominator = nominator;
    }

    public String getMoreInfo()
    {
      return moreInfo;
    }

    public void setMoreInfo( String moreInfo )
    {
      this.moreInfo = moreInfo;
    }

    public String getReason()
    {
      return reason;
    }

    public void setReason( String reason )
    {
      this.reason = reason;
    }

    public String getWhyAttachmentUrl()
    {
      return whyAttachmentUrl;
    }

    public void setWhyAttachmentUrl( String whyAttachmentUrl )
    {
      this.whyAttachmentUrl = whyAttachmentUrl;
    }

    public String getWhyAttachmentName()
    {
      return whyAttachmentName;
    }

    public void setWhyAttachmentName( String whyAttachmentName )
    {
      this.whyAttachmentName = whyAttachmentName;
    }

    public String geteCertUrl()
    {
      return eCertUrl;
    }

    public void seteCertUrl( String eCertUrl )
    {
      this.eCertUrl = eCertUrl;
    }

    @JsonProperty( "customFields" )
    public List<CustomFieldView> getCustomFields()
    {
      return customFields;
    }

    public void setCustomFields( List<CustomFieldView> customFields )
    {
      this.customFields = customFields;
    }

    public static class CustomFieldView
    {
      @JsonProperty( "id" )
      private Long id;
      @JsonProperty( "name" )
      private String name;
      @JsonProperty( "description" )
      private String description;

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

      public String getDescription()
      {
        return description;
      }

      public void setDescription( String description )
      {
        this.description = description;
      }
    }
  }
}
