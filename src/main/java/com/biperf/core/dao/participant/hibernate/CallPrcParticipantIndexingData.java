
package com.biperf.core.dao.participant.hibernate;

import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import com.biperf.core.value.NameableBean;
import com.biperf.core.value.participant.EmailAddress;
import com.biperf.core.value.participant.PaxIndexData;
import com.biperf.core.value.participant.PersonAddresses;
import com.biperf.core.value.participant.PersonAttributes;
import com.biperf.core.value.participant.PhoneNumbers;
import com.biw.digs.rest.response.PersonPronouns;

import oracle.jdbc.OracleTypes;

public class CallPrcParticipantIndexingData extends StoredProcedure
{

  private static final String STORED_PROC_NAME = "prc_get_user_info";
  public static final String P_OUT_DATA = "p_out_data";
  public static final String P_OUT_PATH = "p_out_path";
  private static final String CM_KEY_PREFIX = "roster.compliance.pronouns.info.";

  public static final String P_OUT_RETURN_CODE = "p_out_retun_code";

  public CallPrcParticipantIndexingData( DataSource ds )
  {
    super( ds, STORED_PROC_NAME );
    declareParameter( new SqlParameter( "p_in_user_ids", Types.VARCHAR ) );
    declareParameter( new SqlOutParameter( P_OUT_RETURN_CODE, Types.NUMERIC ) );
    declareParameter( new SqlOutParameter( P_OUT_DATA, OracleTypes.CURSOR, new PrcParticipantIndexingDataExtractor() ) );
    declareParameter( new SqlOutParameter( P_OUT_PATH, OracleTypes.CURSOR, new PrcParticipantNodePathDataExtractor() ) );
    compile();
  }

  public Map<String, Object> executeProcedure( List<Long> userIds )
  {
    HashMap<String, Object> inParams = new HashMap<String, Object>();
    inParams.put( "p_in_user_ids", StringUtils.join( userIds, "," ) );
    return execute( inParams );

  }

  private class PrcParticipantIndexingDataExtractor implements ResultSetExtractor<Object>
  {
    @Override
    public List<PaxIndexData> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<PaxIndexData> participantIndexingDataList = new ArrayList<PaxIndexData>();

      while ( rs.next() )
      {
        PaxIndexData participantIndexingData = new PaxIndexData();
        participantIndexingData.setUserId( rs.getLong( "USER_ID" ) );
        participantIndexingData.setRosterUserId( UUID.fromString( rs.getString( "ROSTER_USER_ID" ) ) );
        participantIndexingData.setFirstname( rs.getString( "FIRST_NAME" ) );
        participantIndexingData.setLastname( rs.getString( "LAST_NAME" ) );
        participantIndexingData.setPositionTypeCode( rs.getString( "POSITION_TYPE" ) );
        participantIndexingData.setDepartmentTypeCode( rs.getString( "DEPARTMENT_TYPE" ) );
        participantIndexingData.setActive( rs.getBoolean( "is_active" ) );
        participantIndexingData.setUserName( rs.getString( "USER_NAME" ) );

        if ( Objects.nonNull( rs.getString( "LANGUAGEPREFERENCE" ) ) )
        {
          participantIndexingData.setLanguagePreference( rs.getString( "LANGUAGEPREFERENCE" ).replaceAll( "_", "-" ) );
        }
        else
        {
          participantIndexingData.setLanguagePreference( "en-US" );
        }

        if ( Objects.nonNull( rs.getString( "STATE" ) ) )
        {
          String[] stateCountryArray = rs.getString( "STATE" ).split( "_" );

          if ( !StringUtils.isEmpty( stateCountryArray[1] ) )
          {
            participantIndexingData.setState( stateCountryArray[1].toUpperCase() );
          }
        }

        participantIndexingData.setRoleType( rs.getString( "ROLE_TYPE" ) );
        participantIndexingData.setPersonCountry( rs.getString( "PERSON_COUNTRY" ) );

        String phoneNbrs = rs.getString( "PERSON_PHONES" );
        PhoneNumbers phone = null;
        List<PhoneNumbers> phoneList = new ArrayList<PhoneNumbers>();

        if ( Objects.nonNull( phoneNbrs ) )
        {
          String[] phoneArray = phoneNbrs.split( "~~~" );

          for ( int i = 0; i < phoneArray.length; i++ )
          {
            phone = new PhoneNumbers();
            List<String> phones = Arrays.asList( phoneArray[i].split( "\\|" ) );

            for ( int j = 0; j < phones.size(); j++ )
            {
              if ( !StringUtils.isEmpty( phones.get( 0 ) ) )
              {
                phone.setPhoneType( phones.get( 0 ) );
              }

              if ( !StringUtils.isEmpty( phones.get( 1 ) ) )
              {
                phone.setPhoneNbr( phones.get( 1 ).replaceAll( "-", "" ) );
              }

              if ( !StringUtils.isEmpty( phones.get( 2 ) ) )
              {
                phone.setCountryPhonecode( phones.get( 2 ) );
              }

              if ( Objects.nonNull( phones.get( 3 ) ) )
              {
                if ( phones.get( 3 ).equals( "1" ) )
                {
                  phone.setPrimary( Boolean.TRUE );

                }
                else
                {
                  phone.setPrimary( Boolean.FALSE );
                }
              }

            }
            phoneList.add( phone );
          }
        }

        participantIndexingData.setPhoneNumbers( phoneList );

        // email address list
        String emailAddress = rs.getString( "EMAIL_ADDRESS" );
        EmailAddress email = null;
        List<EmailAddress> emailsList = new ArrayList<EmailAddress>();

        if ( Objects.nonNull( emailAddress ) )
        {
          String[] emailArray = emailAddress.split( "~~~" );

          for ( int i = 0; i < emailArray.length; i++ )
          {
            email = new EmailAddress();
            List<String> emails = Arrays.asList( emailArray[i].split( "\\|" ) );

            for ( int j = 0; j < emails.size(); j++ )
            {
              if ( !StringUtils.isEmpty( emails.get( 0 ) ) )
              {
                email.setEmailType( emails.get( 0 ).trim().toLowerCase() );
              }

              if ( !StringUtils.isEmpty( emails.get( 1 ) ) )
              {
                email.setEmailAddress( emails.get( 1 ).trim().toLowerCase() );
              }

              if ( Objects.nonNull( emails.get( 2 ) ) )
              {
                if ( emails.get( 2 ).equals( "1" ) )
                {
                  email.setPrimary( Boolean.TRUE );
                }
                else
                {
                  email.setPrimary( Boolean.FALSE );
                }
              }

            }
            emailsList.add( email );
          }
        }
        participantIndexingData.setEmailAddress( emailsList );

        // Person Address List
        String personAddress = rs.getString( "PERSON_ADDRESS" );
        PersonAddresses address = null;
        List<PersonAddresses> addressList = new ArrayList<PersonAddresses>();

        if ( Objects.nonNull( personAddress ) )
        {
          String[] addressArray = personAddress.split( "~~~" );

          for ( int i = 0; i < addressArray.length; i++ )
          {
            address = new PersonAddresses();
            List<String> addresses = Arrays.asList( addressArray[i].split( "\\|" ) );

            for ( int j = 0; j < addresses.size(); j++ )
            {
              if ( !StringUtils.isEmpty( addresses.get( 0 ) ) )
              {
                address.setAddressType( addresses.get( 0 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 1 ) ) )
              {
                address.setCountryName( addresses.get( 1 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 2 ) ) )
              {
                address.setAddress1( addresses.get( 2 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 3 ) ) )
              {
                address.setAddress2( addresses.get( 3 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 4 ) ) )
              {
                address.setAddress3( addresses.get( 4 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 5 ) ) )
              {
                address.setAddress4( addresses.get( 5 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 6 ) ) )
              {
                address.setAddress5( addresses.get( 6 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 7 ) ) )
              {
                address.setAddress6( addresses.get( 7 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 8 ) ) )
              {
                address.setCity( addresses.get( 8 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 9 ) ) )
              {
                address.setState( addresses.get( 9 ) );
              }

              if ( !StringUtils.isEmpty( addresses.get( 10 ) ) )
              {
                address.setPostalCode( addresses.get( 10 ) );
              }

              if ( Objects.nonNull( addresses.get( 11 ) ) )
              {
                if ( addresses.get( 11 ).equals( "1" ) )
                {
                  address.setPrimary( Boolean.TRUE );
                }
                else
                {
                  address.setPrimary( Boolean.FALSE );
                }
              }

            }
            addressList.add( address );
          }
        }

        participantIndexingData.setPersonAddresses( addressList );

        // characteristic fetching from procedure

        String personAttributes = rs.getString( "PERSON_CHARS" );
        PersonAttributes attributes = null;
        List<PersonAttributes> personAttributeList = new ArrayList<PersonAttributes>();
        if ( Objects.nonNull( personAttributes ) )
        {
          String[] personAttributesArray = personAttributes.split( "~~~" );

          for ( int i = 0; i < personAttributesArray.length; i++ )
          {
            attributes = new PersonAttributes();
            List<String> attributesList = Arrays.asList( personAttributesArray[i].split( "\\|" ) );

            for ( int j = 0; j < attributesList.size(); j++ )
            {
              if ( !StringUtils.isEmpty( attributesList.get( 0 ) ) )
              {
                attributes.setCharacteristicName( attributesList.get( 0 ) );
              }

              if ( !StringUtils.isEmpty( attributesList.get( 1 ) ) )
              {
                attributes.setCharacteristicValue( attributesList.get( 1 ) );
              }
            }
            personAttributeList.add( attributes );
          }
        }

        participantIndexingData.setPersonAttributes( personAttributeList );

        // new es-properties needs to be add

        long primaryNodeId = rs.getLong( "NODE_ID" );
        participantIndexingData.setPrimaryNodeId( primaryNodeId );

        participantIndexingData.setCountryId( rs.getLong( "COUNTRY_ID" ) );
        participantIndexingData.setAvatar( rs.getString( "AVATAR_URL" ) );

        String audienceIdsStr = rs.getString( "AUDIENCE_IDS" );

        if ( !StringUtils.isEmpty( audienceIdsStr ) )
        {
          List<Long> audienceIds = Stream.of( audienceIdsStr.split( "," ) ).map( Long::parseLong ).collect( toList() );
          participantIndexingData.setAudienceIds( audienceIds );
        }

        String nonPrimaryNodeIds_Str = rs.getString( "non_primary_node_ids" );
        List<Long> allNodeIds = new ArrayList<Long>();

        if ( !StringUtils.isEmpty( nonPrimaryNodeIds_Str ) )
        {
          allNodeIds = Stream.of( nonPrimaryNodeIds_Str.split( "," ) ).map( Long::parseLong ).collect( toList() );
        }

        allNodeIds.add( primaryNodeId );
        participantIndexingData.setAllNodeIds( allNodeIds );

        participantIndexingData.setOptOutAwards( rs.getBoolean( "is_opt_out_of_awards" ) );

        participantIndexingData.setTitle( rs.getString( "TITLE" ) );
        participantIndexingData.setSuffix( rs.getString( "SUFFIX" ) );

        if ( Objects.nonNull( rs.getString( "HIRE_DATE" ) ) )
        {
          participantIndexingData.setHireDate( rs.getString( "HIRE_DATE" ) );
        }

        if ( Objects.nonNull( rs.getString( "TERMINATION_DATE" ) ) )
        {
          participantIndexingData.setTerminationDate( rs.getString( "TERMINATION_DATE" ) );

        }
        participantIndexingData.setGender( rs.getString( "GENDER" ) );
        try
        {
          PersonPronouns personPronouns = new PersonPronouns();

          if ( Objects.nonNull( participantIndexingData.getGender() ) )
          {
            personPronouns.setObjective( rs.getString( "OBJECTIVE" ) );
            personPronouns.setSubjective( rs.getString( "SUBJECTIVE" ) );
            participantIndexingData.setPronouns( personPronouns );
          }
        }
        catch( Exception e )
        {
        }
        participantIndexingData.setMiddleName( rs.getString( "MIDDLE_NAME" ) );

        participantIndexingDataList.add( participantIndexingData );

      }

      return participantIndexingDataList;
    }
  }

  private class PrcParticipantNodePathDataExtractor implements ResultSetExtractor<Object>
  {
    @Override
    public Map<Long, List<String>> extractData( ResultSet rs ) throws SQLException, DataAccessException
    {
      List<NameableBean> pathList = new ArrayList<NameableBean>();

      while ( rs.next() )
      {
        pathList.add( new NameableBean( rs.getLong( "USER_ID" ), rs.getString( "PATH" ) ) );
      }
      return pathList.parallelStream().collect( Collectors.groupingBy( p -> p.getId(), mapping( p -> p.getName(), Collectors.toList() ) ) );
    }
  }

}
