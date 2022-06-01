/**
 * 
 */

package com.biperf.core.ui.productclaim;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.ui.productclaim.ClaimsJsonApprovalBean.ProductClaimApprovalBean.ProductClaimsParameters.ProductClaimsPromotion;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author poddutur
 *
 */
@JsonInclude( Include.NON_NULL )
public class ClaimsJsonApprovalBean
{
  private ProductClaimApprovalBean productClaimApprovalBean = new ProductClaimApprovalBean();

  @JsonProperty( "claimsJson" )
  public ProductClaimApprovalBean getProductClaimApprovalBean()
  {
    return productClaimApprovalBean;
  }

  public void setProductClaimApprovalBean( ProductClaimApprovalBean productClaimApprovalBean )
  {
    this.productClaimApprovalBean = productClaimApprovalBean;
  }

  public static class ProductClaimApprovalBean
  {
    private ProductClaimsParameters productClaimsParameters = new ProductClaimsParameters();
    private ProductClaimsPromotion productClaimsPromotion = new ProductClaimsPromotion();

    @JsonProperty( "parameters" )
    public ProductClaimsParameters getProductClaimsParameters()
    {
      return productClaimsParameters;
    }

    public void setProductClaimsParameters( ProductClaimsParameters productClaimsParameters )
    {
      this.productClaimsParameters = productClaimsParameters;
    }

    @JsonProperty( "promotion" )
    public ProductClaimsPromotion getProductClaimsPromotion()
    {
      return productClaimsPromotion;
    }

    public void setProductClaimsPromotion( ProductClaimsPromotion productClaimsPromotion )
    {
      this.productClaimsPromotion = productClaimsPromotion;
    }

    public static class ProductClaimsParameters
    {
      @JsonProperty( "promotionId" )
      private Long promotionId;
      @JsonProperty( "claimStatus" )
      private String claimStatus;
      @JsonProperty( "startDate" )
      private String startDate;
      @JsonProperty( "endDate" )
      private String endDate;

      public Long getPromotionId()
      {
        return promotionId;
      }

      public void setPromotionId( Long promotionId )
      {
        this.promotionId = promotionId;
      }

      public String getClaimStatus()
      {
        return claimStatus;
      }

      public void setClaimStatus( String claimStatus )
      {
        this.claimStatus = claimStatus;
      }

      public String getStartDate()
      {
        return startDate;
      }

      public void setStartDate( String startDate )
      {
        this.startDate = startDate;
      }

      public String getEndDate()
      {
        return endDate;
      }

      public void setEndDate( String endDate )
      {
        this.endDate = endDate;
      }

      public static class ProductClaimsPromotion
      {
        @JsonProperty( "id" )
        private Long id;
        @JsonProperty( "name" )
        private String name;
        @JsonProperty( "timestamp" )
        private String timestamp;
        private List<ProductClaimApprovalStats> statsList = new ArrayList<ProductClaimApprovalStats>();
        private Claims claims = new Claims();

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

        public String getTimestamp()
        {
          return timestamp;
        }

        public void setTimestamp( String timestamp )
        {
          this.timestamp = timestamp;
        }

        @JsonProperty( "stats" )
        public List<ProductClaimApprovalStats> getStatsList()
        {
          return statsList;
        }

        public void setStatsList( List<ProductClaimApprovalStats> statsList )
        {
          this.statsList = statsList;
        }

        @JsonProperty( "claims" )
        public Claims getClaims()
        {
          return claims;
        }

        public void setClaims( Claims claims )
        {
          this.claims = claims;
        }

        public static class Claims
        {
          private MetaContent metaContent = new MetaContent();
          private List<Results> resultList = new ArrayList<Results>();

          @JsonProperty( "meta" )
          public MetaContent getMetaContent()
          {
            return metaContent;
          }

          public void setMetaContent( MetaContent metaContent )
          {
            this.metaContent = metaContent;
          }

          @JsonProperty( "results" )
          public List<Results> getResultList()
          {
            return resultList;
          }

          public void setResultList( List<Results> resultList )
          {
            this.resultList = resultList;
          }

          public static class Results
          {
            @JsonProperty( "id" )
            private Long id;
            @JsonProperty( "number" )
            private String number;
            @JsonProperty( "date" )
            private String date;
            @JsonProperty( "submitter" )
            private String submitter;
            private List<Product> productsList = new ArrayList<Product>();

            public Long getId()
            {
              return id;
            }

            public void setId( Long id )
            {
              this.id = id;
            }

            public String getNumber()
            {
              return number;
            }

            public void setNumber( String number )
            {
              this.number = number;
            }

            public String getDate()
            {
              return date;
            }

            public void setDate( String date )
            {
              this.date = date;
            }

            public String getSubmitter()
            {
              return submitter;
            }

            public void setSubmitter( String submitter )
            {
              this.submitter = submitter;
            }

            @JsonProperty( "products" )
            public List<Product> getProductsList()
            {
              return productsList;
            }

            public void setProductsList( List<Product> productsList )
            {
              this.productsList = productsList;
            }

            public static class Product
            {
              @JsonProperty( "id" )
              private Long claimItemId;
              @JsonProperty( "name" )
              private String productName;
              @JsonProperty( "status" )
              private String status;
              @JsonProperty( "statusReason" )
              private String statusReason;
              @JsonProperty( "approver" )
              private String approver;

              public Long getClaimItemId()
              {
                return claimItemId;
              }

              public void setClaimItemId( Long claimItemId )
              {
                this.claimItemId = claimItemId;
              }

              public String getProductName()
              {
                return productName;
              }

              public void setProductName( String productName )
              {
                this.productName = productName;
              }

              public String getStatus()
              {
                return status;
              }

              public void setStatus( String status )
              {
                this.status = status;
              }

              public String getStatusReason()
              {
                return statusReason;
              }

              public void setStatusReason( String statusReason )
              {
                this.statusReason = statusReason;
              }

              public String getApprover()
              {
                return approver;
              }

              public void setApprover( String approver )
              {
                this.approver = approver;
              }
            }
          }

          public static class MetaContent
          {
            @JsonProperty( "sortedOn" )
            private String sortedOn;
            @JsonProperty( "sortedBy" )
            private String sortedBy;
            @JsonProperty( "maxRows" )
            private int maxRows;
            @JsonProperty( "rowsPerPage" )
            private int rowsPerPage;
            @JsonProperty( "pageNumber" )
            private int pageNumber;
            @JsonProperty( "exportUrl" )
            private String exportUrl;
            private List<Columns> columnList = new ArrayList<Columns>();
            private List<ProductStatus> statusesList = new ArrayList<ProductStatus>();

            public String getSortedOn()
            {
              return sortedOn;
            }

            public void setSortedOn( String sortedOn )
            {
              this.sortedOn = sortedOn;
            }

            public String getSortedBy()
            {
              return sortedBy;
            }

            public void setSortedBy( String sortedBy )
            {
              this.sortedBy = sortedBy;
            }

            public int getMaxRows()
            {
              return maxRows;
            }

            public void setMaxRows( int maxRows )
            {
              this.maxRows = maxRows;
            }

            public int getRowsPerPage()
            {
              return rowsPerPage;
            }

            public void setRowsPerPage( int rowsPerPage )
            {
              this.rowsPerPage = rowsPerPage;
            }

            public int getPageNumber()
            {
              return pageNumber;
            }

            public void setPageNumber( int pageNumber )
            {
              this.pageNumber = pageNumber;
            }

            public String getExportUrl()
            {
              return exportUrl;
            }

            public void setExportUrl( String exportUrl )
            {
              this.exportUrl = exportUrl;
            }

            @JsonProperty( "columns" )
            public List<Columns> getColumnList()
            {
              return columnList;
            }

            public void setColumnList( List<Columns> columnList )
            {
              this.columnList = columnList;
            }

            @JsonProperty( "statuses" )
            public List<ProductStatus> getStatusesList()
            {
              return statusesList;
            }

            public void setStatusesList( List<ProductStatus> statusesList )
            {
              this.statusesList = statusesList;
            }

            public static class ProductStatus
            {
              @JsonProperty( "value" )
              private String value;
              @JsonProperty( "text" )
              private String text;
              private List<Reason> reasonsList = new ArrayList<Reason>();

              public String getValue()
              {
                return value;
              }

              public void setValue( String value )
              {
                this.value = value;
              }

              public String getText()
              {
                return text;
              }

              public void setText( String text )
              {
                this.text = text;
              }

              @JsonProperty( "reasons" )
              public List<Reason> getReasonsList()
              {
                return reasonsList;
              }

              public void setReasonsList( List<Reason> reasonsList )
              {
                this.reasonsList = reasonsList;
              }

              public static class Reason
              {
                @JsonProperty( "value" )
                private String reasonValue;
                @JsonProperty( "text" )
                private String reasonText;

                public String getReasonValue()
                {
                  return reasonValue;
                }

                public void setReasonValue( String reasonValue )
                {
                  this.reasonValue = reasonValue;
                }

                public String getReasonText()
                {
                  return reasonText;
                }

                public void setReasonText( String reasonText )
                {
                  this.reasonText = reasonText;
                }
              }
            }

            public static class Columns
            {
              @JsonProperty( "name" )
              private String name;
              @JsonProperty( "text" )
              private String text;
              @JsonProperty( "sortable" )
              private boolean sortable;

              public String getName()
              {
                return name;
              }

              public void setName( String name )
              {
                this.name = name;
              }

              public String getText()
              {
                return text;
              }

              public void setText( String text )
              {
                this.text = text;
              }

              public boolean isSortable()
              {
                return sortable;
              }

              public void setSortable( boolean sortable )
              {
                this.sortable = sortable;
              }

            }

          }
        }

        public static class ProductClaimApprovalStats
        {
          @JsonProperty( "type" )
          private String type;
          @JsonProperty( "status" )
          private String status;
          @JsonProperty( "name" )
          private String name;
          @JsonProperty( "count" )
          private int count;

          public String getType()
          {
            return type;
          }

          public void setType( String type )
          {
            this.type = type;
          }

          public String getStatus()
          {
            return status;
          }

          public void setStatus( String status )
          {
            this.status = status;
          }

          public String getName()
          {
            return name;
          }

          public void setName( String name )
          {
            this.name = name;
          }

          public int getCount()
          {
            return count;
          }

          public void setCount( int count )
          {
            this.count = count;
          }

        }

      }

    }
  }
}
