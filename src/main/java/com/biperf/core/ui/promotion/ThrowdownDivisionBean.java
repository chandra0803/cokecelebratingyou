
package com.biperf.core.ui.promotion;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ThrowdownDivisionBean
{
  private Long id;
  private String divisionName;
  private String divisionNameAssetCode;
  private BigDecimal minimumQualifier = new BigDecimal( 0 );
  private int sequenceNumber;
  private List<PromotionAudienceFormBean> divisionAudiences = new ArrayList<PromotionAudienceFormBean>();
  private Long version;

  public ThrowdownDivisionBean()
  {
  }

  public ThrowdownDivisionBean( int count )
  {
    divisionAudiences = new ArrayList<PromotionAudienceFormBean>( count );
    for ( int i = 0; i < count; i++ )
    {
      PromotionAudienceFormBean formBean = new PromotionAudienceFormBean();
      divisionAudiences.add( formBean );
    }
  }

  public String getDivisionNameAssetCode()
  {
    return divisionNameAssetCode;
  }

  public void setDivisionNameAssetCode( String divisionNameAssetCode )
  {
    this.divisionNameAssetCode = divisionNameAssetCode;
  }

  public BigDecimal getMinimumQualifier()
  {
    return minimumQualifier;
  }

  public void setMinimumQualifier( BigDecimal minimumQualifier )
  {
    this.minimumQualifier = minimumQualifier;
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getDivisionName()
  {
    return divisionName;
  }

  public void setDivisionName( String divisionName )
  {
    this.divisionName = divisionName;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public List<PromotionAudienceFormBean> getDivisionAudiences()
  {
    return divisionAudiences;
  }

  public void setDivisionAudiences( List<PromotionAudienceFormBean> divisionAudiences )
  {
    this.divisionAudiences = divisionAudiences;
  }

  public PromotionAudienceFormBean getDivisionAudience( int index )
  {
    return divisionAudiences.get( index );
  }

  public int getDivisionAudienceListCount()
  {
    if ( null == divisionAudiences || divisionAudiences.isEmpty() )
    {
      return 0;
    }
    else
    {
      return divisionAudiences.size();
    }
  }

  public Long getVersion()
  {
    return version;
  }

  public void setVersion( Long version )
  {
    this.version = version;
  }
}
