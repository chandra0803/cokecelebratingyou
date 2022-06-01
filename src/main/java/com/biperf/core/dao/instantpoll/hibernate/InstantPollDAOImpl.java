/**
 * 
 */

package com.biperf.core.dao.instantpoll.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.instantpoll.InstantPollDAO;
import com.biperf.core.domain.promotion.InstantPoll;
import com.biperf.core.domain.promotion.SurveyQuestion;
import com.biperf.core.domain.promotion.SurveyQuestionResponse;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.instantpoll.InstantPollAudienceFormBean;
import com.biperf.core.value.instantpoll.InstantPollsListbean;

/**
 * @author poddutur
 *
 */
public class InstantPollDAOImpl extends BaseDAO implements InstantPollDAO
{

  @Override
  public InstantPoll saveInstantPoll( InstantPoll instantPoll )
  {
    getSession().saveOrUpdate( instantPoll );
    return instantPoll;
  }

  @Override
  public SurveyQuestionResponse saveSurveyQuestionResponse( SurveyQuestionResponse surveyQuestionResponse )
  {
    getSession().saveOrUpdate( surveyQuestionResponse );
    return surveyQuestionResponse;
  }

  @SuppressWarnings( "unchecked" )
  public List<Long> getEligibleInstantPollIds( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.InstantPoll.getEligibleInstantPollIds" );
    query.setParameter( "userId", userId );
    return query.list();
  }

  public List<InstantPoll> getInstantPollsForTileDisplay( Long userId, AssociationRequestCollection associationRequestCollection )
  {
    List<Long> instantPollIds = getEligibleInstantPollIds( userId );
    List<InstantPoll> instantPolls = new ArrayList<InstantPoll>();
    for ( Long id : instantPollIds )
    {
      InstantPoll instantPoll = getInstantPollByIdWithAssociations( id, associationRequestCollection );
      instantPolls.add( instantPoll );
    }
    return instantPolls;
  }

  public InstantPoll getInstantPollByIdWithAssociations( Long id, AssociationRequestCollection associationRequestCollection )
  {
    InstantPoll instantPoll = (InstantPoll)getSession().get( InstantPoll.class, id );
    if ( associationRequestCollection != null )
    {
      associationRequestCollection.process( instantPoll );
    }
    return instantPoll;
  }

  @Override
  public InstantPoll getInstantPollByName( String name )
  {
    return (InstantPoll)getSession().getNamedQuery( "com.biperf.core.domain.promotion.InstantPollByName" ).setString( "name", name.toUpperCase() ).uniqueResult();
  }

  @Override
  public InstantPoll getInstantPollById( Long id )
  {
    return (InstantPoll)getSession().get( InstantPoll.class, id );
  }

  @Override
  public List<InstantPollsListbean> getAllInstantPollsList()
  {
    String languageCode = UserManager.getLocale().toString();
    String sql = buildAllInstantPollsListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "languageCode", languageCode );
    query.setResultTransformer( new InstantPollsValuebeanMapper() );
    return query.list();
  }

  private String buildAllInstantPollsListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT ip.survey_id, " );
    sql.append( "   (SELECT cms_value FROM vw_cms_asset_value WHERE locale = DECODE(locale,:languageCode,:languageCode,(select string_val from os_propertyset where entity_name = 'default.language')) AND asset_code = sq.cm_asset_name ) question, " );
    sql.append( "   ip.submission_start_date, " );
    sql.append( "   ip.submission_end_date, " );
    sql.append( "   ip.audience_type, " );
    sql.append( "   ip.status, " );
    sql.append( "   ip.notify_pax, " );
    sql.append( "   ip.is_email_already_sent " );
    sql.append( " FROM Instant_Poll ip, " );
    sql.append( "   Survey su, " );
    sql.append( "   Survey_Question sq " );
    sql.append( " WHERE ip.survey_id = su.survey_id " );
    sql.append( " AND su.survey_id   = sq.survey_id " );
    sql.append( " AND ip.status = 'active' " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class InstantPollsValuebeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      InstantPollsListbean instantPollsValue = new InstantPollsListbean();

      instantPollsValue.setInstantPollId( extractLong( tuple[0] ) );
      instantPollsValue.setQuestion( extractString( tuple[1] ) );
      instantPollsValue.setSubmissionStartDate( extractDate( tuple[2] ) );
      instantPollsValue.setSubmissionEndDate( extractDate( tuple[3] ) );
      instantPollsValue.setAudienceType( extractString( tuple[4] ) );
      instantPollsValue.setStatus( extractString( tuple[5] ) );
      instantPollsValue.setNotifyPax( extractBoolean( tuple[6] ) );
      instantPollsValue.setIsEmailAlreadySent( extractBoolean( tuple[7] ) );

      return instantPollsValue;
    }
  }

  @Override
  public List<InstantPollAudienceFormBean> getAudienceByInstantPollId( Long instantPollId )
  {
    String sql = buildAudienceListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "instantPollId", instantPollId );
    query.setResultTransformer( new InstantPollAudienceValueBeanMapper() );
    return query.list();
  }

  private String buildAudienceListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT a.name, a.audience_id, Count(pa.user_id) " );
    sql.append( "   FROM audience a, " );
    sql.append( " instant_poll_audience ipa, participant_audience pa " );
    sql.append( " WHERE a.audience_id    = ipa.audience_id AND a.audience_id = pa.audience_id " );
    sql.append( " AND ipa.survey_id = :instantPollId " );
    sql.append( " GROUP BY  a.audience_id, a.name " );

    return sql.toString();
  }

  @SuppressWarnings( "serial" )
  private class InstantPollAudienceValueBeanMapper extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      InstantPollAudienceFormBean reportValue = new InstantPollAudienceFormBean();

      reportValue.setName( extractString( tuple[0] ) );
      reportValue.setAudienceId( extractLong( tuple[1] ) );
      reportValue.setSize( extractInt( tuple[2] ) );

      return reportValue;
    }
  }

  @Override
  public SurveyQuestion getSurveyQuestionById( Long instantPollId )
  {
    return (SurveyQuestion)getSession().get( SurveyQuestion.class, instantPollId );
  }

  @Override
  public void deleteInstantPoll( InstantPoll instantPollToDelete )
  {
    instantPollToDelete.setStatus( "InActive" );
    getSession().update( instantPollToDelete );
  }

  @Override
  public List<BigDecimal> getUsersListOfSpecifyAudienceByInstantPollId( Long instantPollId )
  {
    String sql = buildUsersListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "instantPollId", instantPollId );
    return query.list();
  }

  private String buildUsersListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT DISTINCT pa.user_id " );
    sql.append( " FROM instant_poll_audience ipa, " );
    sql.append( " participant_audience pa " );
    sql.append( " WHERE pa.audience_id = ipa.audience_id " );
    sql.append( " AND ipa.survey_id   = :instantPollId " );

    return sql.toString();
  }

  @Override
  public List getSurveyQuestionResponseByInstantPollId( Long instantPollId )
  {
    String languageCode = UserManager.getLocale().toString();
    String sql = buildSurveyQuestionResponseListQuery();
    Query query = getSession().createSQLQuery( sql );
    query.setParameter( "instantPollId", instantPollId );
    query.setParameter( "languageCode", languageCode );
    return query.list();
  }

  private String buildSurveyQuestionResponseListQuery()
  {
    StringBuffer sql = new StringBuffer();

    sql.append( " SELECT " );
    sql.append( "   (SELECT cms_value " );
    sql.append( "   FROM vw_cms_asset_value " );
    sql.append( "   WHERE locale = DECODE(locale,:languageCode,:languageCode, " );
    sql.append( "     (SELECT string_val FROM os_propertyset WHERE entity_name = 'default.language' " );
    sql.append( "     )) " );
    sql.append( "   AND asset_code = sqr.cm_asset_name " );
    sql.append( "   ) question_response " );
    sql.append( " FROM instant_poll ip, " );
    sql.append( "   survey_question sq, " );
    sql.append( "   survey_question_response sqr " );
    sql.append( " WHERE ip.survey_id        = sq.survey_id " );
    sql.append( " AND sq.survey_question_id = sqr.survey_question_id " );
    sql.append( " AND ip.survey_id          = :instantPollId " );

    return sql.toString();
  }

}
