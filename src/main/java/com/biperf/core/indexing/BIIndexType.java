
package com.biperf.core.indexing;

import com.biperf.core.value.indexing.BIElasticSearchQueryBuilder;
import com.biperf.core.value.indexing.ESIndexMappingBuilder;
import com.biperf.core.value.indexing.PaxIndexMappingEnum;
import com.biperf.core.value.indexing.Searchable;
import com.biperf.core.value.participant.PaxIndexData;

public enum BIIndexType
{
  PARTICIPANT( "participant", new PaxIndexMappingEnum.PaxIndexMappingBuilder(), PaxIndexData.class, new PaxIndexMappingEnum.PaxIndexSearchQueryBuilder() );

  private String type;
  private ESIndexMappingBuilder indexBuilder;
  private Class<? extends Searchable> classType;
  private BIElasticSearchQueryBuilder queryBuilder;
  private String settings;

  private BIIndexType( String type, ESIndexMappingBuilder thatBuilder, Class<? extends Searchable> thatClassType, BIElasticSearchQueryBuilder thatQueryBuilder )
  {
    this.type = type;
    this.indexBuilder = thatBuilder;
    this.classType = thatClassType;
    this.queryBuilder = thatQueryBuilder;
  }

  public String getType()
  {
    return type;
  }

  public ESIndexMappingBuilder getIndexBuilder()
  {
    return indexBuilder;
  }

  public Class<? extends Searchable> getClassType()
  {
    return classType;
  }

  public BIElasticSearchQueryBuilder getQueryBuilder()
  {
    return queryBuilder;
  }

  public String getSettings()
  {
    return settings;
  }

}
