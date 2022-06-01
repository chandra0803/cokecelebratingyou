/*
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/participant/hibernate/ListBuilderDAOImpl.java,v $
 * (c) 2005 BI, Inc.  All rights reserved.
 */

package com.biperf.core.dao.participant.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.participant.ListBuilderDAO;
import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceCriteria;
import com.biperf.core.domain.participant.AudienceCriteriaCharacteristic;
import com.biperf.core.domain.participant.CriteriaAudience;
import com.biperf.core.domain.participant.PaxAudience;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.SqlQueryBuilder;
import com.biperf.core.value.FormattedValueBean;

/**
 * ListBuilderDAOImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>sharma</td>
 * <td>Jun 22, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class ListBuilderDAOImpl extends BaseDAO implements ListBuilderDAO
{
  /**
   * Logger for this class
   */

  public static final String SORT_LASTNAME = "last_name";
  public static final String SORT_EMAIL = "email_addr";
  public static final String SORT_LOGINID = "user_name";
  public static final String SORT_USERID = "user_id";
  public static final String SORT_ = "";
  public static final String SORT_COUNTRY = "country_name";
  private static final Log log = LogFactory.getLog( ListBuilderDAOImpl.class );
  String sortedOn;
  String sortedBy;

  private JdbcTemplate jdbcTemplate;

  private static final String POSITION_TYPE_SUBQUERY = " ( SELECT pe.position_type\r\n" + "                       FROM participant_employer pe\r\n"
      + "                      WHERE pe.user_id = a.USER_ID\r\n" + "                        AND ( pe.termination_date is null\r\n" + "                              AND ROWNUM = 1\r\n"
      + "                            ) \r\n" + "            ) as position_type ";

  private static final String DEPARTMENT_TYPE_SUBQUERY = " ( SELECT pe.department_type\r\n" + "                       FROM participant_employer pe\r\n"
      + "                      WHERE pe.user_id = a.USER_ID\r\n" + "                        AND ( pe.termination_date is null\r\n" + "                              AND ROWNUM = 1\r\n"
      + "                            ) \r\n" + "            ) as department_type ";

  private static final String POSITION_TYPE_SUBQUERY_INACTIVE_PAX = " ( SELECT pe.position_type\r\n" + "                       FROM VW_CURR_PAX_EMPLOYER pe\r\n"
      + "                      WHERE pe.user_id = a.USER_ID\r\n" + "                        AND (  ROWNUM = 1\r\n" + "                            ) \r\n" + "            ) as position_type ";

  private static final String DEPARTMENT_TYPE_SUBQUERY_INACTIVE_PAX = " ( SELECT pe.department_type\r\n" + "                       FROM VW_CURR_PAX_EMPLOYER pe\r\n"
      + "                      WHERE pe.user_id = a.USER_ID\r\n" + "  ) as department_type ";

  private static final String queryStartAll = "SELECT \n" + "  a.user_id, a.first_name, a.middle_name, a.last_name, \n" + "  " + POSITION_TYPE_SUBQUERY + ", " + DEPARTMENT_TYPE_SUBQUERY + " \n"
      + "  FROM \n" + "  application_user a, participant b \n";
  private static final String queryStartAllInactivePax = "SELECT \n" + "  a.user_id, a.first_name, a.middle_name, a.last_name, \n" + "  " + POSITION_TYPE_SUBQUERY_INACTIVE_PAX + ", "
      + DEPARTMENT_TYPE_SUBQUERY_INACTIVE_PAX + " \n" + "  FROM \n" + "  application_user a, participant b \n";

  private static final String queryStartUserIdOnly = "SELECT \n" + "  a.user_id FROM \n" + "  application_user a, participant b \n";

  private static final String queryWhereInitial = "  WHERE \n" + "  a.user_id = b.user_id\n" + "                                              AND a.is_active = 1\n"
      + "                                              AND b.status = 'active'\n";

  private static final String queryWhereInitialInactive = "  WHERE \n" + "  a.user_id = b.user_id\n";

  private static final String queryWhereStandardClientAdmin = "  WHERE \n" + "  a.user_id = b.user_id\n";

  private static final String userIdClauseStart = "AND a.user_id in\n(\n";
  private static final String userIdClauseEnd = "\n)";
  private static final String orderByClause = "\nORDER BY a.last_name, a.first_name, a.middle_name\n";

  private static final String queryRosterStartAll = "SELECT \n"
      + "FNC_CMS_ASSET_CODE_VALUE(c.CM_ASSET_CODE) countryName,a.user_name, a.user_id, a.first_name, a.middle_name, a.last_name,a.suffix,a.title,a.language_id, a.gender \n" + "  FROM \n"
      + "  application_user a, participant b, user_address ua,country c \n";

  private static final String queryRosteremailSearch = "SELECT \n"
      + "FNC_CMS_ASSET_CODE_VALUE(c.CM_ASSET_CODE) countryName,uea.email_addr, a.user_name, a.user_id, a.first_name, a.middle_name, a.last_name,a.suffix,a.title,a.language_id, a.gender \n"
      + "  FROM \n" + "  application_user a, participant b, user_address ua,user_email_address uea,country c \n";

  private static final String queryRosterWhereInitial = "  WHERE \n" + "  a.user_id = b.user_id\n" + " AND a.is_active = 1\n" + "AND b.status = 'active'\n" + " AND b.user_id = ua.user_id\n"
      + " AND ua.country_id = c.COUNTRY_ID\n" + " AND ua.IS_PRIMARY =1 \n";

  private static final String queryRosterEmailWhereInitial = "  WHERE \n" + "  a.user_id = b.user_id\n" + " AND a.is_active = 1\n" + "AND b.status = 'active'\n" + " AND b.user_id = ua.user_id\n"
      + " AND ua.user_id = uea.user_id\n " + " AND ua.user_id = uea.user_id\n" + " AND ua.country_id = c.COUNTRY_ID\n" + " AND ua.IS_PRIMARY =1 \n" + " AND uea.IS_PRIMARY =1 \n";

  /**
   * @param audiences
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @return List
   */
  public List searchParticipantsInactivePaxIncluded( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated )
  {
    if ( audiences.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    List bindVariables = new ArrayList();
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();

    if ( produceUserIdOnly )
    {
      queryBuilder.append( queryStartUserIdOnly );
    }
    else
    {
      queryBuilder.append( queryStartAll );
    }

    queryBuilder.append( queryWhereInitialInactive );

    applyAudiencesAndFilterAudiences( audiences, hierarchyId, bindVariables, queryBuilder, filterAudiences, preCalculated );

    if ( !produceUserIdOnly )
    {
      queryBuilder.append( orderByClause );
    }

    String queryString = queryBuilder.toString();
    log.info( "ListBuilder Query:\n" + queryString );
    log.info( "ListBuilder Query Bind Variables:" + bindVariables );

    // Spring 2.5.3 onwards, RowMapperResultReader is not supported.
    // The method query takes in rowmapper directly, so we are removing RowMapperResultReader
    List results = jdbcTemplate.query( queryString, bindVariables.toArray(), new BasicRowMapper( produceUserIdOnly ) );

    return results;

  }

  /**
   * @param audiences
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @return List
   */
  public List searchParticipants( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated )
  {
    if ( audiences.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    List bindVariables = new ArrayList();
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();

    if ( produceUserIdOnly )
    {
      queryBuilder.append( queryStartUserIdOnly );
    }
    else
    {
      queryBuilder.append( queryStartAll );
    }

    queryBuilder.append( queryWhereInitialInactive );  //custom for wip # 51222

    applyAudiencesAndFilterAudiences( audiences, hierarchyId, bindVariables, queryBuilder, filterAudiences, preCalculated );

    if ( !produceUserIdOnly )
    {
      queryBuilder.append( orderByClause );
    }

    String queryString = queryBuilder.toString();
    log.info( "ListBuilder Query:\n" + queryString );
    log.info( "ListBuilder Query Bind Variables:" + bindVariables );

    // Spring 2.5.3 onwards, RowMapperResultReader is not supported.
    // The method query takes in rowmapper directly, so we are removing RowMapperResultReader
    List results = jdbcTemplate.query( queryString, bindVariables.toArray(), new BasicRowMapper( produceUserIdOnly ) );

    return results;

  }

  public List searchParticipants( Set audiences, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated, boolean isStandardClientAdmin )
  {
    if ( audiences.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    List bindVariables = new ArrayList();
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();

    if ( produceUserIdOnly )
    {
      queryBuilder.append( queryStartUserIdOnly );
    }
    else
    {
      if ( isStandardClientAdmin )
      {
        queryBuilder.append( queryStartAllInactivePax );
      }
      else
      {
        queryBuilder.append( queryStartAll );
      }
    }
    if ( isStandardClientAdmin )
    {
      queryBuilder.append( queryWhereStandardClientAdmin );
    }
    else
    {
      queryBuilder.append( queryWhereInitial );
    }

    applyAudiencesAndFilterAudiences( audiences, hierarchyId, bindVariables, queryBuilder, filterAudiences, preCalculated );

    if ( !produceUserIdOnly )
    {
      queryBuilder.append( orderByClause );
    }

    String queryString = queryBuilder.toString();
    log.info( "ListBuilder Query:\n" + queryString );
    log.info( "ListBuilder Query Bind Variables:" + bindVariables );

    // Spring 2.5.3 onwards, RowMapperResultReader is not supported.
    // The method query takes in rowmapper directly, so we are removing RowMapperResultReader
    List results = jdbcTemplate.query( queryString, bindVariables.toArray(), new BasicRowMapper( produceUserIdOnly ) );

    return results;

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.ListBuilderDAO#searchParticipants(com.biperf.core.domain.participant.AudienceCriteria,
   *      Long)
   * @param audienceCriteria
   * @return List
   */
  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean preCalculated )
  {
    CriteriaAudience criteriaAudience = new CriteriaAudience();
    criteriaAudience.addAudienceCriteria( audienceCriteria );
    return searchParticipants( criteriaAudience, hierarchyId, false, null, preCalculated, false );
  }

  private void applyAudiencesAndFilterAudiences( Set audiences, Long hierarchyId, List bindVariables, SqlQueryBuilder queryBuilder, Set filterAudiences, boolean preCalculated )
  {
    if ( isSqlRequiredForAudiences( audiences, preCalculated ) || isSqlRequiredForAudiences( filterAudiences, preCalculated ) )
    {
      queryBuilder.append( userIdClauseStart );
      if ( isSqlRequiredForAudiences( audiences, preCalculated ) )
      {
        applyAudiences( audiences, hierarchyId, bindVariables, queryBuilder, preCalculated );
      }

      if ( isSqlRequiredForAudiences( audiences, preCalculated ) && isSqlRequiredForAudiences( filterAudiences, preCalculated ) )
      {
        // Both (sql producing) audience and filter exist so insert intersect to filter out
        // filterAudience
        queryBuilder.append( "--Apply Filter Audience\n" );
        queryBuilder.append( "INTERSECT\n" );
      }

      if ( isSqlRequiredForAudiences( filterAudiences, preCalculated ) )
      {
        applyAudiences( filterAudiences, hierarchyId, bindVariables, queryBuilder, preCalculated );
      }

      queryBuilder.append( userIdClauseEnd );
    }
  }

  private void applyAudiences( Set audiences, Long hierarchyId, List bindVariables, SqlQueryBuilder queryBuilder, boolean preCalculated )
  {
    queryBuilder.append( " ( --For each audience in audiences\n" );
    boolean appendUnion = false;
    for ( Iterator iter = audiences.iterator(); iter.hasNext(); )
    {
      Audience audience = (Audience)iter.next();
      if ( isSqlRequiredForAudience( audience, preCalculated ) )
      {
        if ( appendUnion )
        {
          queryBuilder.append( "UNION\n" );
        }
        appendUnion = true;
      }
      applyAudience( hierarchyId, bindVariables, queryBuilder, audience, preCalculated );
    }
    queryBuilder.append( " ) --For each audience in audiences\n" );
  }

  private void applyAudience( Long hierarchyId, List bindVariables, SqlQueryBuilder queryBuilder, Audience audience, boolean preCalculated )
  {
    if ( isSqlRequiredForAudience( audience, preCalculated ) )
    {
      // queryBuilder.append( " --Audience Name: " + audience.getName() + "\n" );
      if ( audience instanceof PaxAudience || preCalculated )
      {
        SqlQueryBuilder filterPaxQueryBuilder = new SqlQueryBuilder();
        filterPaxQueryBuilder.appendSimpleQuery( "PARTICIPANT_AUDIENCE.USER_ID", "PARTICIPANT_AUDIENCE", "PARTICIPANT_AUDIENCE.AUDIENCE_ID = ?" );
        bindVariables.add( audience.getId() );

        queryBuilder.append( filterPaxQueryBuilder.toString() );
      }
      else if ( audience instanceof CriteriaAudience )
      {
        CriteriaAudience filterCriteriaAudience = (CriteriaAudience)audience;
        applyCriteriaAudienceConstraints( filterCriteriaAudience, hierarchyId, bindVariables, queryBuilder );
      }
      else
      {
        throw new BeaconRuntimeException( "unknown filter Audience class type: " + audience.getClass().getName() );
      }
    }
  }

  private void applyCriteriaAudienceConstraints( CriteriaAudience criteriaAudience, Long hierarchyId, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    if ( criteriaAudience.hasConstraints() )
    {
      queryBuilder.append( "   ( --Criteria Audience Constraints\n" ); // Wrap entire group so that
      // whole output could
      // intersect with an
      // audience filter.
      boolean appendUnion = false;
      for ( Iterator iter = criteriaAudience.getAudienceCriterias().iterator(); iter.hasNext(); )
      {
        AudienceCriteria audienceCriteria = (AudienceCriteria)iter.next();

        if ( audienceCriteria.hasConstraints() )
        {
          if ( !appendUnion )
          {
            appendUnion = true;
          }
          else
          {
            queryBuilder.append( "UNION\n" );
          }

          // Apply Basic Intersects for various user_id constraining tables
          queryBuilder.append( buildAudienceCriteriaConstraints( hierarchyId, bindVariables, audienceCriteria ) );
        }
      }
      queryBuilder.append( "  ) --Criteria Audience Constraints\n" );
    }
  }

  private String buildAudienceCriteriaConstraints( Long hierarchyId, List bindVariables, AudienceCriteria audienceCriteria )
  {
    SqlQueryBuilder constraintQueryBuilder = new SqlQueryBuilder();

    constraintQueryBuilder.append( "     ( --audience criteria constraints\n" );
    applyApplicationUserCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
    applyParticipantStateCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );

    applyParticipantEmailCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
    applyParticipantEmployerTableCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
    applyParticipantCountryCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
    applyUserCharacteristicTableCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
    applyParticipantLanguageCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );

    applyUserNodeTableCriteria( audienceCriteria, bindVariables, constraintQueryBuilder, hierarchyId );
    if ( audienceCriteria.hasExcludeConstraints() )
    {
      constraintQueryBuilder.append( "MINUS " );
      constraintQueryBuilder.append( "   ( -- Excludes constraints starts\n" );
      constraintQueryBuilder.applyIntersect = false;

      applyExcludeParticipantEmployerTableCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
      applyExcludeParticipantCountryCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
      applyExcludeUserCharacteristicTableCriteria( audienceCriteria, bindVariables, constraintQueryBuilder );
      applyExcludeUserNodeTableCriteria( audienceCriteria, bindVariables, constraintQueryBuilder, hierarchyId );

      // end of excludes
      constraintQueryBuilder.append( "      ) --End of excludes\n" );
    }

    constraintQueryBuilder.append( "      ) --audience criteria constraints\n" );

    return constraintQueryBuilder.toString();
  }

  /**
   * @param audience
   * @param preCalculated
   * @return true if an additional sql section will need to be produced for the audience
   */
  private boolean isSqlRequiredForAudience( Audience audience, boolean preCalculated )
  {
    boolean isRequired = false;

    if ( audience == null )
    {
      isRequired = false;
    }
    else if ( audience instanceof PaxAudience || preCalculated )
    {
      // Assumes a paxAudience is required to have at least one member.
      isRequired = true;
    }
    else if ( audience instanceof CriteriaAudience )
    {
      isRequired = ( (CriteriaAudience)audience ).hasConstraints();
      /*
       * if ( ( (CriteriaAudience)audience ).hasConstraints() || ( (CriteriaAudience)audience
       * ).hasExcludeConstraints() ) { isRequired = true; }
       */
    }
    else
    {
      throw new BeaconRuntimeException( "unknown filterAudience class type: " + audience.getClass().getName() );
    }

    return isRequired;
  }

  /**
   * @param audiences
   * @return boolean
   */
  private boolean isSqlRequiredForAudiences( Set audiences, boolean preCalculated )
  {
    if ( audiences == null )
    {
      return false;
    }

    boolean isRequired = false;

    for ( Iterator iter = audiences.iterator(); iter.hasNext(); )
    {
      Audience audience = (Audience)iter.next();
      if ( isSqlRequiredForAudience( audience, preCalculated ) )
      {
        isRequired = true;
        break;
      }
    }
    return isRequired;
  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param queryBuilder
   */
  private void applyApplicationUserCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {

    // // TABLE: APPLICATION_USER
    List clauses = new ArrayList();
    String firstName = audienceCriteria.getFirstName();
    if ( firstName != null )
    {
      clauses.add( "LOWER(APPLICATION_USER.FIRST_NAME) LIKE ?" );
      bindVariables.add( firstName.toLowerCase() + "%" );
      // TODO: escape % and _ since we are using like, else users could do substrings, etc...
    }
    String lastName = audienceCriteria.getLastName();
    if ( lastName != null )
    {
      clauses.add( "LOWER(APPLICATION_USER.LAST_NAME) LIKE ?" );
      bindVariables.add( lastName.toLowerCase() + "%" );
      // TODO: escape % and _ since we are using like, else users could do substrings, etc...
    }
    String loginId = audienceCriteria.getLoginId();
    if ( loginId != null )
    {
      clauses.add( "LOWER(APPLICATION_USER.USER_NAME) LIKE ?" );
      bindVariables.add( loginId.toLowerCase() + "%" );
      // TODO: escape % and _ since we are using like, else users could do substrings, etc...
    }
    Long userId = audienceCriteria.getId();
    if ( userId != null )
    {
      clauses.add( "APPLICATION_USER.USER_ID =  ?" );
      bindVariables.add( userId );
      // TODO: escape % and _ since we are using like, else users could do substrings, etc...
    }
    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryInIntersectQuery( "APPLICATION_USER.USER_ID", "APPLICATION_USER", clauses );
    }
  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param queryBuilder
   */
  private void applyParticipantEmployerTableCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    // // TABLE: PARTICIPANT_EMPLOYER
    List clauses = new ArrayList();
    Long employerId = audienceCriteria.getEmployerId();
    if ( employerId != null )
    {
      clauses.add( "PARTICIPANT_EMPLOYER.EMPLOYER_ID = ?" );
      bindVariables.add( employerId );
    }
    String departmentType = audienceCriteria.getDepartmentType();
    if ( departmentType != null )
    {
      clauses.add( "PARTICIPANT_EMPLOYER.DEPARTMENT_TYPE = ?" );
      bindVariables.add( departmentType );
    }
    String positionType = audienceCriteria.getPositionType();
    if ( positionType != null )
    {
      clauses.add( "PARTICIPANT_EMPLOYER.POSITION_TYPE = ?" );
      bindVariables.add( positionType );
    }
    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryInIntersectQuery( "PARTICIPANT_EMPLOYER.USER_ID", "PARTICIPANT_EMPLOYER", clauses );
      /* bug fix for 17653 */
      queryBuilder.append( " AND ( PARTICIPANT_EMPLOYER.TERMINATION_DATE IS NULL OR PARTICIPANT_EMPLOYER.TERMINATION_DATE >= SYSDATE )" );
    }
  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param queryBuilder
   */
  private void applyExcludeParticipantEmployerTableCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    // // TABLE: PARTICIPANT_EMPLOYER
    List clauses = new ArrayList();

    String excludeDepartmentType = audienceCriteria.getExcludeDepartmentType();
    String departmentType = audienceCriteria.getDepartmentType();
    if ( excludeDepartmentType != null )
    {
      clauses.add( "PARTICIPANT_EMPLOYER.DEPARTMENT_TYPE = ?" );
      bindVariables.add( excludeDepartmentType );
    }
    String excludePositionType = audienceCriteria.getExcludePositionType();
    String positionType = audienceCriteria.getPositionType();
    if ( excludePositionType != null )
    {
      clauses.add( "PARTICIPANT_EMPLOYER.POSITION_TYPE = ?" );
      bindVariables.add( excludePositionType );
    }
    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryWithOR( "PARTICIPANT_EMPLOYER.USER_ID", "PARTICIPANT_EMPLOYER", clauses, true );
      /* bug fix for 17653 */
      queryBuilder.append( " AND ( PARTICIPANT_EMPLOYER.TERMINATION_DATE IS NULL OR PARTICIPANT_EMPLOYER.TERMINATION_DATE >= SYSDATE )" );
    }
  }

  private void applyParticipantLanguageCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    List clauses = new ArrayList();
    if ( audienceCriteria.getLanguageType() != null && audienceCriteria.getLanguageType().getCode() != null )
    {
      String languageId = audienceCriteria.getLanguageType().getCode().toString();
      if ( languageId != null )
      {
        clauses.add( "APPLICATION_USER.LANGUAGE_ID = ?" );
        bindVariables.add( languageId );
      }
    }
    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryInIntersectQuery( "APPLICATION_USER.USER_ID", "APPLICATION_USER", clauses );
    }

  }

  private void applyParticipantCountryCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    List clauses = new ArrayList();
    String countryId = null;
    if ( audienceCriteria.getCountryId() != null )
    {
      countryId = audienceCriteria.getCountryId().toString();
    }
    if ( countryId != null && new Long( countryId ).longValue() > 0 )
    {
      clauses.add( "USER_ADDRESS.COUNTRY_ID = ?" );
      bindVariables.add( new Long( countryId ) );
    }
    else
    {
      clauses.add( "USER_ADDRESS.COUNTRY_ID = USER_ADDRESS.COUNTRY_ID" );
    }
    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryInIntersectQuery( "USER_ADDRESS.USER_ID", "USER_ADDRESS", clauses );
    }

  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void applyParticipantStateCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    List clauses = new ArrayList();
    String stateName = null;
    if ( audienceCriteria.getStateName() != null )
    {
      stateName = audienceCriteria.getStateName();
    }
    if ( stateName != null )
    {
      clauses.add( "LOWER(USER_ADDRESS.STATE)  LIKE ?" );
      bindVariables.add( "%_" + stateName );
    }
    else
    {
      clauses.add( "USER_ADDRESS.COUNTRY_ID = USER_ADDRESS.COUNTRY_ID" );
    }
    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryInIntersectQuery( "USER_ADDRESS.USER_ID", "USER_ADDRESS", clauses );
    }

  }

  private void applyParticipantEmailCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    List clauses = new ArrayList();
    String emailAddr = audienceCriteria.getEmailAddr();
    if ( emailAddr != null )
    {
      clauses.add( "LOWER(USER_EMAIL_ADDRESS.EMAIL_ADDR)  LIKE ?" );
      bindVariables.add( "%" + emailAddr + "%" );
      // TODO: escape % and _ since we are using like, else users could do substrings, etc...
    }

    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryInIntersectQuery( "USER_EMAIL_ADDRESS.USER_ID", "USER_EMAIL_ADDRESS", clauses );
    }
  }

  private void applyExcludeParticipantCountryCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    List clauses = new ArrayList();
    String countryId = null;
    String excludeCountryId = null;
    if ( audienceCriteria.getCountryId() != null )
    {
      countryId = audienceCriteria.getCountryId().toString();
    }
    if ( audienceCriteria.getExcludeCountryId() != null )
    {
      excludeCountryId = audienceCriteria.getExcludeCountryId().toString();
    }

    if ( excludeCountryId != null && new Long( excludeCountryId ).longValue() > 0 )
    {
      clauses.add( "USER_ADDRESS.COUNTRY_ID = ?" );
      bindVariables.add( new Long( excludeCountryId ) );
    }
    /*
     * else { clauses.add( "USER_ADDRESS.COUNTRY_ID = USER_ADDRESS.COUNTRY_ID" ); }
     */
    if ( !clauses.isEmpty() )
    {
      queryBuilder.appendSimpleQueryInIntersectQuery( "USER_ADDRESS.USER_ID", "USER_ADDRESS", clauses );
    }

  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param queryBuilder
   */
  private void applyUserCharacteristicTableCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    // // TABLE: USER_CHARACTERISTIC
    Set userCharacteristicCriterias = audienceCriteria.getUserCharacteristicCriterias();
    if ( userCharacteristicCriterias != null )
    {
      for ( Iterator iter = userCharacteristicCriterias.iterator(); iter.hasNext(); )
      {
        AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

        if ( audienceCriteriaCharacteristic.getSearchType() == null || audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "include" ) )
        {

          List clauses = new ArrayList();
          clauses.add( "uc.characteristic_id = ?" );
          bindVariables.add( audienceCriteriaCharacteristic.getCharacteristic().getId() );

          // "starts with" - "LIKE
          // ?" (with % appended to value) ?
          if ( audienceCriteriaCharacteristic.getCharactersticDataType().equals( "int" ) )
          {
            clauses.add( "uc.characteristic_value= ?" ); // TODO: Should this be
            bindVariables.add( Integer.parseInt( audienceCriteriaCharacteristic.getCharacteristicValue() ) );
          }
          else
          {
            clauses.add( "UPPER(uc.characteristic_value) = UPPER(?)" ); // TODO: Should this be
            bindVariables.add( audienceCriteriaCharacteristic.getCharacteristicValue() );
          }

          queryBuilder.appendSimpleQueryInIntersectQuery( "uc.USER_ID", "USER_CHARACTERISTIC uc", clauses );
        }
      }
    }
  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param queryBuilder
   */
  private void applyExcludeUserCharacteristicTableCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder )
  {
    // // TABLE: USER_CHARACTERISTIC
    Set userCharacteristicCriterias = audienceCriteria.getUserCharacteristicCriterias();
    if ( userCharacteristicCriterias != null )
    {
      for ( Iterator iter = userCharacteristicCriterias.iterator(); iter.hasNext(); )
      {
        AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

        if ( audienceCriteriaCharacteristic.getSearchType() != null && audienceCriteriaCharacteristic.getSearchType().equals( "exclude" ) )
        {

          List clauses = new ArrayList();
          clauses.add( "uc.characteristic_id = ?" );
          bindVariables.add( audienceCriteriaCharacteristic.getCharacteristic().getId() );
          clauses.add( "UPPER(uc.characteristic_value) LIKE UPPER(?)" ); // TODO: Should this be
                                                                         // "starts with" - "LIKE
          // ?" (with % appended to value) ?
          bindVariables.add( "%" + audienceCriteriaCharacteristic.getCharacteristicValue() + "%" );

          queryBuilder.appendSimpleQueryInIntersectQuery( "uc.USER_ID", "USER_CHARACTERISTIC uc", clauses );
        }
      }
    }
  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param queryBuilder
   * @param hierarchyId
   */
  private void applyUserNodeTableCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder, Long hierarchyId )
  {
    if ( audienceCriteria.hasNodeConstraints() )
    {
      List clauses = new ArrayList();

      HierarchyRoleType nodeRole = audienceCriteria.getNodeRole();
      if ( nodeRole != null )
      {
        clauses.add( "USER_NODE.ROLE = ?" );
        bindVariables.add( nodeRole.getCode() );
      }
      if ( audienceCriteria.getNodeName() != null || audienceCriteria.getNodeTypeId() != null || audienceCriteria.getNodeId() != null )
      {
        // Apply node query
        clauses.add( "USER_NODE.NODE_ID in \n(\n" + buildNodeQuery( audienceCriteria, bindVariables, hierarchyId ) + "\n)" );
        // NOTE: bind vars added in buildNodeQuery()
      }

      queryBuilder.appendSimpleQueryInIntersectQuery( "USER_NODE.USER_ID", "USER_NODE", clauses );

    }

  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param queryBuilder
   * @param hierarchyId
   */
  private void applyExcludeUserNodeTableCriteria( AudienceCriteria audienceCriteria, List bindVariables, SqlQueryBuilder queryBuilder, Long hierarchyId )
  {
    if ( audienceCriteria.hasExcludeNodeConstraints() )
    {
      List clauses = new ArrayList();

      HierarchyRoleType excludeNodeRole = audienceCriteria.getExcludeNodeRole();
      if ( excludeNodeRole != null )
      {
        clauses.add( "USER_NODE.ROLE = ?" );
        bindVariables.add( excludeNodeRole.getCode() );
      }
      if ( audienceCriteria.getExcludeNodeName() != null || audienceCriteria.getExcludeNodeTypeId() != null || audienceCriteria.getExcludeNodeId() != null )
      {
        // Apply node query
        clauses.add( "USER_NODE.NODE_ID in \n(\n" + buildExcludeNodeQuery( audienceCriteria, bindVariables, hierarchyId ) + "\n)" );
        // NOTE: bind vars added in buildNodeQuery()
      }

      queryBuilder.appendSimpleQueryInIntersectQuery( "USER_NODE.USER_ID", "USER_NODE", clauses );

    }

  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param hierarchyId
   * @return String
   */
  private String buildNodeQuery( AudienceCriteria audienceCriteria, List bindVariables, Long hierarchyId )
  {
    SqlQueryBuilder nodeQueryBuilder = new SqlQueryBuilder();

    String nodeName = audienceCriteria.getNodeName();
    Long nodeTypeId = audienceCriteria.getNodeTypeId();
    Long nodeId = audienceCriteria.getNodeId();

    if ( !audienceCriteria.isChildNodesIncluded() )
    {
      // Selected Node only - if nodeId set, ignore nodeName. Also, since only selected nodes,
      // nodeTypeId not used
      // if nodeName/nodeID set

      if ( nodeId != null )
      {
        // Simple case, just append the Id.
        nodeQueryBuilder.append( "?" );
        bindVariables.add( nodeId );
      }
      else if ( nodeName != null )
      {
        appendStartsWithNodeNameQuery( bindVariables, nodeQueryBuilder, nodeName, hierarchyId );
      }
      else if ( nodeTypeId != null )
      {
        // Append all nodes of the given node type
        nodeQueryBuilder.appendSimpleQueryInIntersectQuery( "NODE.NODE_ID", "NODE", "NODE.NODE_TYPE_ID = ?" );
        bindVariables.add( nodeTypeId );

      }
      else
      {
        throw new BeaconRuntimeException( "Shouldn't be possible to have no nodeName and no nodeId and no nodeTypeId and be in this method...." );
      }
    }
    else
    {
      // Selected Node and children
      List clauses = new ArrayList();
      if ( nodeTypeId != null )
      {
        // Only include nodes of selected node type
        clauses.add( "NODE.NODE_TYPE_ID = ?" );
        bindVariables.add( nodeTypeId );
      }
      nodeQueryBuilder.appendSimpleQueryInIntersectQuery( "NODE.NODE_ID", "NODE", clauses );

      // Add on "connect by" to include children nodes given root node(s)
      nodeQueryBuilder.append( "\nSTART WITH NODE.NODE_ID in (\n" );

      // append list of root nodes (or query to give root nodes). If nodeId set, ignore nodeName.
      if ( nodeId != null )
      {
        // Simple case, just append the Id.
        nodeQueryBuilder.append( "?" );
        bindVariables.add( nodeId );
      }
      else if ( nodeTypeId != null )
      {
        // Append all nodes of the given node type
        nodeQueryBuilder.appendSimpleQuery( "NODE.NODE_ID", "NODE", "NODE.NODE_TYPE_ID = ?" );
        bindVariables.add( nodeTypeId );
      }
      else if ( nodeName != null )
      {
        appendStartsWithNodeNameQuery( bindVariables, nodeQueryBuilder, nodeName, hierarchyId );
      }

      nodeQueryBuilder.append( "\n)\nconnect by\nprior NODE.NODE_ID = NODE.PARENT_NODE_ID" );
    }

    // NODE_CHARACTERISTIC
    Set nodeTypeCharacteristicCriterias = audienceCriteria.getNodeTypeCharacteristicCriterias();
    if ( nodeTypeCharacteristicCriterias != null )
    {
      for ( Iterator iter = nodeTypeCharacteristicCriterias.iterator(); iter.hasNext(); )
      {
        AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

        List clauses = new ArrayList();
        clauses.add( "nc.characteristic_id = ?" );
        bindVariables.add( audienceCriteriaCharacteristic.getCharacteristic().getId() );
        clauses.add( "nc.characteristic_value = ?" ); // TODO: Should this be "LIKE '?%'" ?
        bindVariables.add( audienceCriteriaCharacteristic.getCharacteristicValue() );

        nodeQueryBuilder.appendSimpleQueryInIntersectQuery( "nc.NODE_ID", "NODE_CHARACTERISTIC nc", clauses );
      }
    }

    return nodeQueryBuilder.toString();
  }

  /**
   * @param audienceCriteria
   * @param bindVariables
   * @param hierarchyId
   * @return String
   */
  private String buildExcludeNodeQuery( AudienceCriteria audienceCriteria, List bindVariables, Long hierarchyId )
  {
    SqlQueryBuilder nodeQueryBuilder = new SqlQueryBuilder();

    String excludeNodeName = audienceCriteria.getExcludeNodeName();
    Long excludeNodeTypeId = audienceCriteria.getExcludeNodeTypeId();
    Long excludeNodeId = audienceCriteria.getExcludeNodeId();

    if ( !audienceCriteria.isExcludeChildNodesIncluded() )
    {
      // Selected Node only - if nodeId set, ignore nodeName. Also, since only selected nodes,
      // nodeTypeId not used
      // if nodeName/nodeID set

      if ( excludeNodeId != null )
      {
        // Simple case, just append the Id.
        nodeQueryBuilder.append( "?" );
        bindVariables.add( excludeNodeId );
      }
      else if ( excludeNodeName != null )
      {
        appendStartsWithNodeNameQuery( bindVariables, nodeQueryBuilder, excludeNodeName, hierarchyId );
      }
      else if ( excludeNodeTypeId != null )
      {
        // Append all nodes of the given node type
        nodeQueryBuilder.appendSimpleQueryInIntersectQuery( "NODE.NODE_ID", "NODE", "NODE.NODE_TYPE_ID = ?" );
        bindVariables.add( excludeNodeTypeId );

      }
      else
      {
        throw new BeaconRuntimeException( "Shouldn't be possible to have no nodeName and no nodeId and no nodeTypeId and be in this method...." );
      }
    }
    else
    {
      // Selected Node and children
      List clauses = new ArrayList();
      if ( excludeNodeTypeId != null )
      {
        // Only include nodes of selected node type
        clauses.add( "NODE.NODE_TYPE_ID = ?" );
        bindVariables.add( excludeNodeTypeId );
      }
      nodeQueryBuilder.appendSimpleQueryInIntersectQuery( "NODE.NODE_ID", "NODE", clauses );

      // Add on "connect by" to include children nodes given root node(s)
      nodeQueryBuilder.append( "\nSTART WITH NODE.NODE_ID in (\n" );

      // append list of root nodes (or query to give root nodes). If nodeId set, ignore nodeName.
      if ( excludeNodeId != null )
      {
        // Simple case, just append the Id.
        nodeQueryBuilder.append( "?" );
        bindVariables.add( excludeNodeId );
      }
      else if ( excludeNodeName != null )
      {
        appendStartsWithNodeNameQuery( bindVariables, nodeQueryBuilder, excludeNodeName, hierarchyId );
      }

      nodeQueryBuilder.append( "\n)\nconnect by\nprior NODE.NODE_ID = NODE.PARENT_NODE_ID" );
    }

    // NODE_CHARACTERISTIC
    Set excludeNodeTypeCharacteristicCriterias = audienceCriteria.getExcludeNodeTypeCharacteristicCriterias();
    if ( excludeNodeTypeCharacteristicCriterias != null )
    {
      for ( Iterator iter = excludeNodeTypeCharacteristicCriterias.iterator(); iter.hasNext(); )
      {
        AudienceCriteriaCharacteristic audienceCriteriaCharacteristic = (AudienceCriteriaCharacteristic)iter.next();

        List clauses = new ArrayList();
        clauses.add( "nc.characteristic_id = ?" );
        bindVariables.add( audienceCriteriaCharacteristic.getCharacteristic().getId() );
        clauses.add( "nc.characteristic_value = ?" ); // TODO: Should this be "LIKE '?%'" ?
        bindVariables.add( audienceCriteriaCharacteristic.getCharacteristicValue() );

        nodeQueryBuilder.appendSimpleQueryInIntersectQuery( "nc.NODE_ID", "NODE_CHARACTERISTIC nc", clauses );
      }
    }

    return nodeQueryBuilder.toString();
  }

  /**
   * Append all node names that start with the given nodeName fragment (case-insensitive search)
   * 
   * @param bindVariables
   * @param nodeQueryBuilder
   * @param nodeName
   */
  private void appendStartsWithNodeNameQuery( List bindVariables, SqlQueryBuilder nodeQueryBuilder, String nodeName, Long hierarchyId )
  {
    List clauses = new ArrayList();
    if ( hierarchyId != null && hierarchyId.longValue() != 0 )
    {
      clauses.add( "NODE.HIERARCHY_ID = ?" );
      bindVariables.add( hierarchyId );
    }

    clauses.add( "LOWER(NODE.NAME) = ?" );
    bindVariables.add( nodeName.toLowerCase() );

    nodeQueryBuilder.appendSimpleQuery( "NODE.NODE_ID", "NODE", clauses );
    // TODO: escape % and _ since we are using like, else users could do substrings, etc...
  }

  /**
   *
   */
  private class BasicRowMapper implements RowMapper<FormattedValueBean>
  {
    private boolean produceUserIdOnly;

    public BasicRowMapper( boolean produceUserIdOnly )
    {
      this.produceUserIdOnly = produceUserIdOnly;
    }

    /**
     * Overridden from
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     * @param rs
     * @param rowNum
     * @return Map valueMapByColumn
     * @throws SQLException
     */
    public FormattedValueBean mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      FormattedValueBean formattedValueBean = new FormattedValueBean();
      formattedValueBean.setId( new Long( rs.getBigDecimal( "USER_ID" ).longValue() ) );

      if ( !produceUserIdOnly )
      {
        StringBuffer sb = new StringBuffer();

        sb.append( rs.getString( "LAST_NAME" ) );
        sb.append( ", " );
        sb.append( rs.getString( "FIRST_NAME" ) );
        String middleName = rs.getString( "MIDDLE_NAME" );
        if ( middleName != null )
        {
          sb.append( " " );
          sb.append( rs.getString( "MIDDLE_NAME" ) );
        }

        // job position
        String positionType = rs.getString( "POSITION_TYPE" );
        if ( positionType != null )
        {
          sb.append( " - " );
          PositionType jobPositionItem = PositionType.lookup( positionType );
          sb.append( jobPositionItem != null ? jobPositionItem.getName() : "" );
        }

        // department
        String departmentType = rs.getString( "DEPARTMENT_TYPE" );
        if ( departmentType != null )
        {
          sb.append( " - " );
          DepartmentType departmentItem = DepartmentType.lookup( departmentType );
          sb.append( departmentItem != null ? departmentItem.getName() : "" );
        }

        formattedValueBean.setValue( sb.toString() );
      }

      return formattedValueBean;
    }

  }

  private class BasicRosterRowMapper implements RowMapper<FormattedValueBean>
  {
    private boolean produceUserIdOnly;

    public BasicRosterRowMapper( boolean produceUserIdOnly )
    {
      this.produceUserIdOnly = produceUserIdOnly;
    }

    /**
     * Overridden from
     * 
     * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
     * @param rs
     * @param rowNum
     * @return Map valueMapByColumn
     * @throws SQLException
     */
    public FormattedValueBean mapRow( ResultSet rs, int rowNum ) throws SQLException
    {
      FormattedValueBean formattedValueBean = new FormattedValueBean();
      formattedValueBean.setId( new Long( rs.getBigDecimal( "USER_ID" ).longValue() ) );
      formattedValueBean.setGivenName( rs.getString( "FIRST_NAME" ) );
      formattedValueBean.setSurname( rs.getString( "LAST_NAME" ) );
      formattedValueBean.setMiddleName( rs.getString( "MIDDLE_NAME" ) );
      formattedValueBean.setCountry( rs.getString( "COUNTRYnAME" ) );
      formattedValueBean.setGender( rs.getString( "GENDER" ) );
      formattedValueBean.setExternalId( rs.getBigDecimal( "USER_ID" ).toString() );
      formattedValueBean.setUserName( rs.getString( "USER_NAME" ) );
      formattedValueBean.setLocale( rs.getString( "LANGUAGE_ID" ) );
      formattedValueBean.setSuffix( rs.getString( "SUFFIX" ) );
      formattedValueBean.setTitle( rs.getString( "TITLE" ) );

      return formattedValueBean;
    }

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.ListBuilderDAO#searchParticipants(com.biperf.core.domain.participant.CriteriaAudience,
   *      java.lang.Long, boolean, java.util.Set)
   * @param criteriaAudience
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @return List
   */
  public List searchParticipants( CriteriaAudience criteriaAudience, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated, boolean isStandardClientAdmin )
  {
    Set audiences = new LinkedHashSet();
    audiences.add( criteriaAudience );
    if ( isStandardClientAdmin )
    {
      return searchParticipants( audiences, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated, isStandardClientAdmin );
    }
    return searchParticipants( audiences, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.participant.ListBuilderDAO#searchParticipants(com.biperf.core.domain.participant.AudienceCriteria,
   *      java.lang.Long, boolean, java.util.Set)
   * @param audienceCriteria
   * @param hierarchyId
   * @param produceUserIdOnly
   * @param filterAudiences
   * @return List
   */
  public List searchParticipants( AudienceCriteria audienceCriteria, Long hierarchyId, boolean produceUserIdOnly, Set filterAudiences, boolean preCalculated, boolean isStandardClientAdmin )
  {
    Set audienceCriterias = new LinkedHashSet();
    audienceCriterias.add( audienceCriteria ); // not using addAudienceCriteria to avoid setting
    // the audienceCriteria's Criteria audience.
    CriteriaAudience criteriaAudience = new CriteriaAudience();
    criteriaAudience.setAudienceCriterias( audienceCriterias );

    return searchParticipants( criteriaAudience, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated, isStandardClientAdmin );
  }

  @Override
  public List getParticipantsListByAudienceId( Long audienceId )
  {
    String sql = buildParticipantsListByAudienceIdQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "audienceId", audienceId.longValue() );
    return query.list();
  }

  private String buildParticipantsListByAudienceIdQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT au.first_name ||' '||au.last_name FROM participant_audience pa, application_user au WHERE pa.user_id = au.user_id and pa.audience_id = :audienceId order by au.last_name " );

    return sql.toString();
  }

  public List getParticipantsUserIdListByAudienceId( Long audienceId )
  {
    String sql = buildParticipantsUserIdListByAudienceIdQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "audienceId", audienceId.longValue() );
    return query.list();
  }

  private String buildParticipantsUserIdListByAudienceIdQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT pa.user_id FROM participant_audience pa, application_user au WHERE pa.user_id = au.user_id and au.is_active = 1 and pa.audience_id = :audienceId " );

    return sql.toString();
  }

  /**
   * Setter: DataSource is provided by Dependency Injection.
   * 
   * @param dataSource
   */
  public void setDataSource( DataSource dataSource )
  {
    // TODO what if you want to use a JdbcTemplate by preference,
    // for a native extractor?
    this.jdbcTemplate = new JdbcTemplate( dataSource );
  }

  public List rosterSearch( AudienceCriteria audienceCriteria,
                            Long hierarchyId,
                            boolean produceUserIdOnly,
                            Set filterAudiences,
                            boolean preCalculated,
                            boolean isStandardClientAdmin,
                            boolean eamilSearch,
                            String sortedOn,
                            String sortedBy,
                            int pageNbr,
                            int pageSize )

  {
    Set audienceCriterias = new LinkedHashSet();
    audienceCriterias.add( audienceCriteria ); // not using addAudienceCriteria to avoid setting
    // the audienceCriteria's Criteria audience.
    CriteriaAudience criteriaAudience = new CriteriaAudience();
    criteriaAudience.setAudienceCriterias( audienceCriterias );
    Set audiences = new LinkedHashSet();
    audiences.add( criteriaAudience );
    return rosterSearchParticipants( audiences, hierarchyId, produceUserIdOnly, filterAudiences, preCalculated, eamilSearch, sortedOn, sortedBy, pageNbr, pageSize );
  }

  public List rosterSearchParticipants( Set audiences,
                                        Long hierarchyId,
                                        boolean produceUserIdOnly,
                                        Set filterAudiences,
                                        boolean preCalculated,
                                        boolean eamilSearch,
                                        String sortedOn,
                                        String sortedBy,
                                        int pageNbr,
                                        int pageSize )
  {
    if ( audiences.isEmpty() )
    {
      return Collections.EMPTY_LIST;
    }

    List bindVariables = new ArrayList();
    SqlQueryBuilder queryBuilder = new SqlQueryBuilder();

    if ( produceUserIdOnly )
    {
      queryBuilder.append( queryStartUserIdOnly );
    }
    else
    {
      if ( eamilSearch )
      {
        queryBuilder.append( queryRosteremailSearch );
        queryBuilder.append( queryRosterEmailWhereInitial );
      }
      else
      {
        queryBuilder.append( queryRosterStartAll );
        queryBuilder.append( queryRosterWhereInitial );
      }

    }

    applyAudiencesAndFilterAudiences( audiences, hierarchyId, bindVariables, queryBuilder, filterAudiences, preCalculated );

    if ( !produceUserIdOnly )
    {

      queryBuilder.append( sortedOnField( sortedOn, sortedBy ) );

      queryBuilder.append( "OFFSET " + pageSize * ( pageNbr - 1 ) + " ROWS FETCH NEXT  " + pageSize + "  ROWS ONLY " );
    }

    String queryString = queryBuilder.toString();
    log.info( "ListBuilder Query:\n" + queryString );
    log.info( "ListBuilder Query Bind Variables:" + bindVariables );

    // Spring 2.5.3 onwards, RowMapperResultReader is not supported.
    // The method query takes in rowmapper directly, so we are removing RowMapperResultReader

    List results = jdbcTemplate.query( queryString, bindVariables.toArray(), new BasicRosterRowMapper( produceUserIdOnly ) );

    return results;

  }

  private static String sortedOnField( String sortedOn, String sortedBy )
  {
    if ( Objects.isNull( sortedBy ) )
    {
      sortedBy = "ASC";
    }
    String sortField = "ORDER BY a.last_name";
    if ( "firstName".equalsIgnoreCase( sortedOn ) )
    {
      sortField = "ORDER BY a.first_name";
    }
    else if ( "userName".equalsIgnoreCase( sortedOn ) || "loginId".equalsIgnoreCase( sortedOn ) )
    {
      sortField = "ORDER BY a.user_name";
    }
    else if ( "orgunit".equalsIgnoreCase( sortedOn ) || "orgUnitName".equalsIgnoreCase( sortedOn ) )
    {
      sortField = "ORDER BY  orgUnitPersons.orgUnit.sortOrgUnitName";
    }
    else if ( "department".equalsIgnoreCase( sortedOn ) )
    {
      sortField = "ORDER BY  pe.department_type";
    }
    else if ( "jobtitle".equalsIgnoreCase( sortedOn ) )
    {
      sortField = "ORDER BY pe.position_type";
    }
    else if ( "country".equalsIgnoreCase( sortedOn ) )
    {
      sortField = "ORDER BY  fnc_cms_asset_code_val_extr(country.cmAssetCode, country.nameCmKey, 'en_US')" + " " + sortedBy;
    }
    return sortField + " " + sortedBy;
  }

}
