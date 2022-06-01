
package com.biperf.core.dao.reports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.type.StandardBasicTypes;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.domain.report.ReportParameterTypeEnum;

public class BaseReportsDAO extends BaseDAO
{

  protected static final String PARAM_EXPORT_LEVEL = "exportLevel";

  protected static final List<String> MULTI_SELECT_PARAMETERS = Arrays.asList( new String[] { ReportParameterTypeEnum.PROMOTION_IDS.getName(),
                                                                                              ReportParameterTypeEnum.COUNTRY_IDS.getName(),
                                                                                              ReportParameterTypeEnum.DEPARTMENTS.getName(),
                                                                                              ReportParameterTypeEnum.CHILDNODE_IDS.getName() } );

  protected void setSortOrderBy( Map<String, Object> reportParameters )
  {
    String sortedBy = "ASC";
    if ( reportParameters.get( "sortedBy" ) != null )
    {
      String unfilteredValue = (String)reportParameters.get( "sortedBy" );
      // this is needed to prevent SQL injection coming in from the front-end
      if ( !unfilteredValue.equalsIgnoreCase( "desc" ) )
      {
        sortedBy = "DESC";
      }
    }
    reportParameters.put( "sortedBy", sortedBy );
  }

  protected void setLocaleDatePattern( Map<String, Object> reportParameters )
  {
    Query queryForPattern = getSession().getNamedQuery( "com.biperf.core.domain.report.getLocaleDatePattern" );
    queryForPattern.setParameter( "languageCode", reportParameters.get( "languageCode" ) );
    String localeDatePattern = queryForPattern.uniqueResult().toString();
    reportParameters.put( "localeDatePattern", localeDatePattern );
  }

  protected void setFilterReportParameters( Map<String, Object> reportParameters )
  {
    String commaSeparatedPromoIds = (String)reportParameters.get( "promotionId" );
    String commaSeparatedCountryIds = (String)reportParameters.get( "countryId" );
    String commaSeparatedDepartments = (String)reportParameters.get( "department" );
    String commaSeparatedChildNodeIds = (String)reportParameters.get( "childNodeId" );

    List<Long> promotionIdList = generateQueryIdListFromCommaSeparatedString( commaSeparatedPromoIds );
    reportParameters.put( ReportParameterTypeEnum.FILTER_PROMOTIONS.getName(), StringUtils.isBlank( commaSeparatedPromoIds ) ? "N" : "Y" );
    reportParameters.put( ReportParameterTypeEnum.PROMOTION_IDS.getName(), promotionIdList );
    List<Long> countryIdList = generateQueryIdListFromCommaSeparatedString( commaSeparatedCountryIds );
    reportParameters.put( ReportParameterTypeEnum.FILTER_COUNTRIES.getName(), StringUtils.isBlank( commaSeparatedCountryIds ) ? "N" : "Y" );
    reportParameters.put( ReportParameterTypeEnum.COUNTRY_IDS.getName(), countryIdList );
    List<String> departmentList = generateQueryStringListFromCommaSeparatedString( commaSeparatedDepartments );
    reportParameters.put( ReportParameterTypeEnum.FILTER_DEPARTMENTS.getName(), StringUtils.isBlank( commaSeparatedDepartments ) ? "N" : "Y" );
    reportParameters.put( ReportParameterTypeEnum.DEPARTMENTS.getName(), departmentList );
    List<Long> childNodeIdList = generateQueryIdListFromCommaSeparatedString( commaSeparatedChildNodeIds );
    reportParameters.put( ReportParameterTypeEnum.FILTER_CHILDNODES.getName(), StringUtils.isBlank( commaSeparatedChildNodeIds ) ? "N" : "Y" );
    reportParameters.put( ReportParameterTypeEnum.CHILDNODE_IDS.getName(), childNodeIdList );
  }

  protected String getSortOrder( Map<String, Object> reportParameters )
  {
    String sortColName = "ASC";
    if ( reportParameters.get( "sortedBy" ) != null )
    {
      sortColName = (String)reportParameters.get( "sortedBy" );
    }
    return sortColName;
  }

  protected void populateQueryParameters( Query query, Map<String, Object> reportParameters )
  {
    Query queryForPattern = getSession().getNamedQuery( "com.biperf.core.domain.report.getLocaleDatePattern" );
    queryForPattern.setParameter( "languageCode", reportParameters.get( "languageCode" ) );
    String localeDatePattern = queryForPattern.uniqueResult().toString();
    reportParameters.put( "localeDatePattern", localeDatePattern );

    List<String> queryParameters = Arrays.asList( query.getNamedParameters() );
    for ( String paramName : queryParameters )
    {
      if ( !MULTI_SELECT_PARAMETERS.contains( paramName ) )
      {
        if ( StandardBasicTypes.DOUBLE == ReportParameterTypeEnum.getByName( paramName ).getType() )
        {
          query.setParameter( paramName, Double.valueOf( (String)reportParameters.get( paramName ) ), StandardBasicTypes.DOUBLE );
        }
        else if ( StandardBasicTypes.LONG == ReportParameterTypeEnum.getByName( paramName ).getType() && reportParameters.get( paramName ) != null )
        {
          query.setParameter( paramName, Long.valueOf( String.valueOf( reportParameters.get( paramName ) ) ), StandardBasicTypes.LONG );
        }
        else
        {
          query.setParameter( paramName, reportParameters.get( paramName ), ReportParameterTypeEnum.getByName( paramName ).getType() );
        }
      }
    }

    if ( queryParameters.contains( ReportParameterTypeEnum.FILTER_PROMOTIONS.getName() ) )
    {
      String commaSeparatedPromoIds = (String)reportParameters.get( "promotionId" );
      List<Long> promotionIdList = generateQueryIdListFromCommaSeparatedString( commaSeparatedPromoIds );
      query.setParameter( ReportParameterTypeEnum.FILTER_PROMOTIONS.getName(), StringUtils.isBlank( commaSeparatedPromoIds ) ? "N" : "Y", ReportParameterTypeEnum.FILTER_PROMOTIONS.getType() );
      query.setParameterList( ReportParameterTypeEnum.PROMOTION_IDS.getName(), promotionIdList, ReportParameterTypeEnum.PROMOTION_IDS.getType() );
    }

    if ( queryParameters.contains( ReportParameterTypeEnum.FILTER_COUNTRIES.getName() ) )
    {
      String commaSeparatedCountryIds = (String)reportParameters.get( "countryId" );
      List<Long> countryIdList = generateQueryIdListFromCommaSeparatedString( commaSeparatedCountryIds );
      query.setParameter( ReportParameterTypeEnum.FILTER_COUNTRIES.getName(), StringUtils.isBlank( commaSeparatedCountryIds ) ? "N" : "Y", ReportParameterTypeEnum.FILTER_COUNTRIES.getType() );
      query.setParameterList( ReportParameterTypeEnum.COUNTRY_IDS.getName(), countryIdList, ReportParameterTypeEnum.COUNTRY_IDS.getType() );
    }

    if ( queryParameters.contains( ReportParameterTypeEnum.FILTER_DEPARTMENTS.getName() ) )
    {
      String commaSeparatedDepartments = (String)reportParameters.get( "department" );
      List<String> departmentList = generateQueryStringListFromCommaSeparatedString( commaSeparatedDepartments );
      query.setParameter( ReportParameterTypeEnum.FILTER_DEPARTMENTS.getName(), StringUtils.isBlank( commaSeparatedDepartments ) ? "N" : "Y", ReportParameterTypeEnum.FILTER_DEPARTMENTS.getType() );
      query.setParameterList( ReportParameterTypeEnum.DEPARTMENTS.getName(), departmentList, ReportParameterTypeEnum.DEPARTMENTS.getType() );
    }

    if ( queryParameters.contains( ReportParameterTypeEnum.FILTER_CHILDNODES.getName() ) )
    {
      String commaSeparatedChildNodeIds = (String)reportParameters.get( "childNodeId" );
      List<Long> childNodeIdList = generateQueryIdListFromCommaSeparatedString( commaSeparatedChildNodeIds );
      query.setParameter( ReportParameterTypeEnum.FILTER_CHILDNODES.getName(), StringUtils.isBlank( commaSeparatedChildNodeIds ) ? "N" : "Y", ReportParameterTypeEnum.FILTER_CHILDNODES.getType() );
      query.setParameterList( ReportParameterTypeEnum.CHILDNODE_IDS.getName(), childNodeIdList, ReportParameterTypeEnum.CHILDNODE_IDS.getType() );
    }

    if ( queryParameters.contains( ReportParameterTypeEnum.DEPARTMENTS.getName() ) )
    {
      String commaSeparatedDepartments = (String)reportParameters.get( "department" );
      List<String> departmentList = generateQueryStringListFromCommaSeparatedString( commaSeparatedDepartments );
      query.setParameterList( ReportParameterTypeEnum.DEPARTMENTS.getName(), departmentList, ReportParameterTypeEnum.DEPARTMENTS.getType() );
    }

    if ( queryParameters.contains( ReportParameterTypeEnum.PROMOTION_IDS.getName() ) )
    {
      String commaSeparatedPromoIds = (String)reportParameters.get( "promotionId" );
      List<Long> promotionIdList = generateQueryIdListFromCommaSeparatedString( commaSeparatedPromoIds );
      query.setParameterList( ReportParameterTypeEnum.PROMOTION_IDS.getName(), promotionIdList, ReportParameterTypeEnum.PROMOTION_IDS.getType() );
    }
  }

  private List<Long> generateQueryIdListFromCommaSeparatedString( String commaSeparatedString )
  {
    List<Long> idList = new ArrayList<Long>();
    if ( !StringUtils.isBlank( commaSeparatedString ) )
    {
      for ( String itemId : commaSeparatedString.split( "," ) )
      {
        if ( !itemId.contains( "null" ) )
        {
          idList.add( Long.valueOf( itemId ) );
        }
      }
    }
    else
    {
      idList.add( null );
    }

    return idList;
  }

  private List<String> generateQueryStringListFromCommaSeparatedString( String commaSeparatedString )
  {
    List<String> queryList = new ArrayList<String>();
    if ( !StringUtils.isBlank( commaSeparatedString ) )
    {
      queryList = Arrays.asList( commaSeparatedString.split( "," ) );
    }
    else
    {
      queryList.add( null );
    }

    return queryList;
  }
}
