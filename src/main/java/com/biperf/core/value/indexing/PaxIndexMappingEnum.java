
package com.biperf.core.value.indexing;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.value.participant.PaxIndexData;
import com.biperf.core.value.participant.PaxIndexSearchCriteria;
import com.biperf.core.value.participant.PaxIndexSortOrder;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.searchbox.core.search.sort.Sort;

/**
 * field  mapping of pax index data
 * @see PaxIndexData.java
 */
public enum PaxIndexMappingEnum
{
  USER_ID( "userId" ), FIRST_NAME( "firstname" ), LAST_NAME( "lastname" ), POSITION_TYPE_CODE( "positionTypeCode" ), DEPARTMENT_TYPE_CODE( "departmentTypeCode" ), PRIMARY_NODE_ID(
      "primaryNodeId" ), COUNTRY_ID( "countryId" ), AVATAR( "avatar" ), AUDIENCE_IDS( "audienceIds" ), PATHS( "paths" ), NAME( "name" ), ALL_NODE_IDS( "allNodeIds" ), IS_OPT_OUT_AWARDS(
          "isOptOutAwards" ), ROSTER_USER_ID( "rosterUserId" ), USER_NAME( "userName" ), PERSON_COUNTRY( "personCountry" ), LANGUAGE_PREFERENCE(
              "languagePreference" ), ROLE_TYPE( "roleType" ), EMAIL_ADDRESS(
                  "emailAddress.emailAddress" ), PHONE_NUMBERS( "phoneNumbers.phoneNbr" ), PERSON_ADDRESSES( "personAddresses.countryName" ), PERSON_STATE( "personAddresses.state" );

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private String fieldName;
  private String type;
  private String indexRequired;

  private PaxIndexMappingEnum( String field )
  {
    this.fieldName = field;

  }

  public String getFieldName()
  {
    return fieldName;
  }

  public String getType()
  {
    return type;
  }

  public String getIndexRequired()
  {
    return indexRequired;
  }

  public static class PaxIndexMappingBuilder implements ESIndexMappingBuilder
  {

    public static String JSON_SCHEMA = null;

    @Override
    public String getJsonSchema() throws IOException
    {
      if ( Objects.isNull( JSON_SCHEMA ) )
      {
        InputStream schemaAsStream = null;
        try
        {
          schemaAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream( "/es-model/pax.schema.json" );
          StringWriter writer = new StringWriter();
          IOUtils.copy( schemaAsStream, writer );
          JSON_SCHEMA = writer.toString();
        }
        finally
        {
          try
          {
            if ( Objects.nonNull( schemaAsStream ) )
            {
              schemaAsStream.close();
            }
          }
          catch( IOException e )
          {
            // closing quietly
          }
        }
      }
      return JSON_SCHEMA;
    }

    @Override
    public String getJsonData( Searchable indexData ) throws IOException
    {
      PaxIndexData pax = (PaxIndexData)indexData;
      return objectMapper.writeValueAsString( pax );
    }

    @Override
    public XContentBuilder buildJsonSchema() throws IOException
    {
      return buildXContentBuilder( getJsonSchema() );
    }

    @Override
    public XContentBuilder buildJsonData( Searchable indexData )
    {
      try
      {
        return buildXContentBuilder( getJsonData( indexData ) );
      }
      catch( IOException e )
      {
        throw new BeaconRuntimeException( e );
      }
    }

    @Override
    public Object buildId( Searchable searchable ) throws IOException
    {
      return ( (PaxIndexData)searchable ).getUserId();
    }

    public XContentBuilder buildXContentBuilder( String jsonString ) throws IOException
    {
      //CHECKSTYLE:OFF
      try (XContentParser parser = XContentFactory.xContent( XContentType.JSON ).createParser( NamedXContentRegistry.EMPTY, jsonString ))
      {
        return jsonBuilder().copyCurrentStructure( parser );
      }
      //CHECKSTYLE:ON
    }

  }

  public static class PaxIndexSearchQueryBuilder implements BIElasticSearchQueryBuilder
  {
    @Override
    public <T extends BIElasticSearchCriteria> String buildJsonQuery( T criteria )
    {
      SearchSourceBuilder searchBuilder = new SearchSourceBuilder();
      PaxIndexSearchCriteria searchCriteria = (PaxIndexSearchCriteria)criteria;

      if ( Objects.nonNull( searchCriteria.getEmailAddress() ) || Objects.nonNull( searchCriteria.getPhoneNumber() ) || Objects.nonNull( searchCriteria.getPersonAddressCountry() )
          || Objects.nonNull( searchCriteria.getState() ) )
      {
        searchBuilder.query( nestedBuildQuery( searchCriteria ) );
      }
      else
      {
        searchBuilder.query( buildQuery( searchCriteria ) );
      }

      for ( PaxIndexSortOrder sort : searchCriteria.getFieldSortOrders() )
      {
        searchBuilder.sort( sort.getIndexedField().getFieldName(), SortOrder.fromString( sort.getSortByType().getSortBy() ) );
      }

      if ( Objects.nonNull( searchCriteria.getPaginationRowStart() ) && Objects.nonNull( searchCriteria.getPaginationRowEnd() ) )
      {
        searchBuilder.from( searchCriteria.getPaginationRowStart() );
        searchBuilder.size( searchCriteria.getPaginationRowEnd() );
      }
      else
      {
        searchBuilder.from( (int)searchCriteria.getFromIndex() );
        searchBuilder.size( criteria.getRecordsMaxSize() );
      }

      if ( isNotEmpty( searchCriteria.getFields() ) )
      {
        List<String> allFieldsAsString = searchCriteria.getAllFieldsAsString();
        searchBuilder.fetchSource( allFieldsAsString.toArray( new String[allFieldsAsString.size()] ), null );
      }

      return searchBuilder.toString();
    }

    @Override
    public <T extends BIElasticSearchCriteria> Collection<Sort> buildSort( T criteria )
    {
      List<Sort> sortList = new ArrayList<Sort>();

      if ( Objects.isNull( criteria ) )
      {
        return sortList;
      }

      PaxIndexSearchCriteria paxCriteria = (PaxIndexSearchCriteria)criteria;

      for ( PaxIndexSortOrder sort : paxCriteria.getFieldSortOrders() )
      {
        sortList.add( new Sort( sort.getIndexedField().getFieldName(), getSorting( sort.getSortByType() ) ) );
      }

      return sortList;
    }

    public static QueryBuilder buildQuery( PaxIndexSearchCriteria criteria )
    {
      BoolQueryBuilder query = QueryBuilders.boolQuery();

      if ( isNotBlank( criteria.getName() ) )
      {
        // slight hack to allow names with hyphens to be searched with or without the hyphen
        String nameCriteria = criteria.getName().replace( "-", " " );
        String[] params = nameCriteria.toLowerCase().split( "\\s+" );
        for ( String name : params )
        {
          query.must( QueryBuilders.prefixQuery( PaxIndexMappingEnum.NAME.getFieldName(), name ) );
        }
      }
      // filters are a "must" but do not effect the score for each result.
      if ( CollectionUtils.isNotEmpty( criteria.getIncludeUserIds() ) )
      {
        int size = criteria.getIncludeUserIds().size();
        query.filter( QueryBuilders.idsQuery( "participant" ).addIds( criteria.getIncludeUserIds().toArray( new String[size] ) ) );
      }

      if ( isNotBlank( criteria.getPositionTypeCode() ) )
      {
        query.filter( QueryBuilders.matchQuery( PaxIndexMappingEnum.POSITION_TYPE_CODE.getFieldName(), criteria.getPositionTypeCode() ) );
      }

      if ( isNotBlank( criteria.getDepartmentTypeCode() ) )
      {
        query.filter( QueryBuilders.matchQuery( PaxIndexMappingEnum.DEPARTMENT_TYPE_CODE.getFieldName(), criteria.getDepartmentTypeCode() ) );
      }

      if ( Objects.nonNull( criteria.getCountryId() ) )
      {
        query.filter( QueryBuilders.matchQuery( PaxIndexMappingEnum.COUNTRY_ID.getFieldName(), criteria.getCountryId() ) );
      }

      if ( Objects.nonNull( criteria.getPrimaryNodeId() ) )
      {
        query.filter( QueryBuilders.matchQuery( PaxIndexMappingEnum.PRIMARY_NODE_ID.getFieldName(), criteria.getPrimaryNodeId() ) );
      }

      if ( CollectionUtils.isNotEmpty( criteria.getAllNodeIds() ) )
      {
        query.filter( QueryBuilders.termsQuery( PaxIndexMappingEnum.ALL_NODE_IDS.getFieldName(), criteria.getAllNodeIds() ) );
      }

      if ( CollectionUtils.isNotEmpty( criteria.getAudienceIds() ) )
      {
        query.filter( QueryBuilders.termsQuery( PaxIndexMappingEnum.AUDIENCE_IDS.getFieldName(), criteria.getAudienceIds() ) );
      }

      if ( Objects.nonNull( criteria.getPath() ) )
      {
        query.filter( QueryBuilders.matchQuery( PaxIndexMappingEnum.PATHS.getFieldName(), criteria.getPath() ) );
      }

      if ( Objects.nonNull( criteria.getExcludeUserIds() ) )
      {
        int size = criteria.getExcludeUserIds().size();
        query.mustNot( QueryBuilders.idsQuery( "participant" ).addIds( criteria.getExcludeUserIds().toArray( new String[size] ) ) );
      }

      // Roster Search Related Attributes

      if ( Objects.nonNull( criteria.getRosterUserId() ) )
      {
        query.must( QueryBuilders.termQuery( PaxIndexMappingEnum.ROSTER_USER_ID.getFieldName(), criteria.getRosterUserId() ) );
        criteria.setRecordsMaxSize( 1 );
        criteria.setPaginationRowStart( null );
        criteria.setPaginationRowEnd( null );
      }

      if ( Objects.nonNull( criteria.getUserName() ) )
      {
        query.must( QueryBuilders.termQuery( PaxIndexMappingEnum.USER_NAME.getFieldName(), criteria.getUserName() ) );
        criteria.setRecordsMaxSize( 1 );
        criteria.setPaginationRowStart( null );
        criteria.setPaginationRowEnd( null );
      }

      if ( Objects.nonNull( criteria.getPersonCountry() ) )
      {
        query.must( QueryBuilders.termQuery( PaxIndexMappingEnum.PERSON_COUNTRY.getFieldName(), criteria.getPersonCountry() ) );
      }

      if ( Objects.nonNull( criteria.getLanguagePreference() ) )
      {
        query.must( QueryBuilders.termQuery( PaxIndexMappingEnum.LANGUAGE_PREFERENCE.getFieldName(), criteria.getLanguagePreference() ) );
      }

      if ( Objects.nonNull( criteria.getRoleType() ) )
      {
        query.must( QueryBuilders.termQuery( PaxIndexMappingEnum.ROLE_TYPE.getFieldName(), criteria.getRoleType() ) );
      }

      return query;
    }

    @Override
    public <T extends Searchable> String buildDeleteQuery( T t )
    {
      PaxIndexData indexdata = (PaxIndexData)t;
      StringBuilder sb = new StringBuilder();
      sb.append( "{" ).append( "\"query\":" ).append( buildDeleteQueryBuilder( indexdata ).toString() ).append( "}" );
      return sb.toString();
    }

    private BoolQueryBuilder buildDeleteQueryBuilder( PaxIndexData indexdata )
    {
      BoolQueryBuilder query = QueryBuilders.boolQuery();
      if ( Objects.nonNull( indexdata.getUserId() ) )
      {
        query.must( QueryBuilders.termQuery( PaxIndexMappingEnum.USER_ID.getFieldName(), indexdata.getUserId() ) );
      }

      return query;
    }

    public static NestedQueryBuilder nestedBuildQuery( PaxIndexSearchCriteria criteria )
    {
      BoolQueryBuilder query = QueryBuilders.boolQuery();
      NestedQueryBuilder nestedQueryBuilder = null;

      if ( Objects.nonNull( criteria.getEmailAddress() ) )
      {
        query.should( QueryBuilders.matchQuery( PaxIndexMappingEnum.EMAIL_ADDRESS.getFieldName(), criteria.getEmailAddress() ) );
        nestedQueryBuilder = QueryBuilders.nestedQuery( "emailAddress", query, ScoreMode.Max );

      }
      else if ( Objects.nonNull( criteria.getPhoneNumber() ) )
      {
        query.should( QueryBuilders.matchQuery( PaxIndexMappingEnum.PHONE_NUMBERS.getFieldName(), criteria.getPhoneNumber() ) );
        nestedQueryBuilder = QueryBuilders.nestedQuery( "phoneNumbers", query, ScoreMode.Max );
      }
      else if ( Objects.nonNull( criteria.getPersonAddressCountry() ) )
      {
        query.should( QueryBuilders.matchQuery( PaxIndexMappingEnum.PERSON_ADDRESSES.getFieldName(), criteria.getPersonAddressCountry() ) );
        nestedQueryBuilder = QueryBuilders.nestedQuery( "personAddresses", query, ScoreMode.Max );
      }
      else if ( Objects.nonNull( criteria.getState() ) )
      {
        query.should( QueryBuilders.matchQuery( PaxIndexMappingEnum.PERSON_STATE.getFieldName(), criteria.getState() ) );
        nestedQueryBuilder = QueryBuilders.nestedQuery( "personAddresses", query, ScoreMode.Max );
      }

      return nestedQueryBuilder;

    }

  }
}
