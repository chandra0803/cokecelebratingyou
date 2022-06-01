
package com.biperf.core.dao.recognitionadvisor.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.BaseResultTransformer;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.dao.recognitionadvisor.RecognitionAdvisorDao;
import com.biperf.core.domain.enums.ActivityType;
import com.biperf.core.domain.user.User;
import com.biperf.core.ui.recognitionadvisor.EligibleProgramBean;
import com.biperf.core.value.AlertsValueBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorUnusedBudgetBean;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;

/**
 * Used for the RecognitonAdvisor Algorithm screen(s).
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
 * <td>Ramesh J</td>
 * <td>Dec 11, 2017</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class RecognitionAdvisorDaoImpl extends BaseDAO implements RecognitionAdvisorDao
{
  private DataSource dataSource;
  private UserDAO userDAO;

  public UserDAO getUserDAO()
  {
    return userDAO;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  private static final BigDecimal BAD_OUTPUT = new BigDecimal( "99" );
  private static final Object OUTPUT_RETURN_CODE = "p_out_return_code";
  public static final String P_OUT_DATA = "p_out_user_data";
  private static final Log log = LogFactory.getLog( RecognitionAdvisorDaoImpl.class );

  public void setDataSource( DataSource dataSource )
  {
    this.dataSource = dataSource;
  }

  @Override
  public List<RecognitionAdvisorValueBean> showRAReminderPaxData( Long userId,
                                                                  Long rowStart,
                                                                  Long rowEnd,
                                                                  String sortColumnName,
                                                                  String sortBy,
                                                                  Long excludeUpcoming,
                                                                  String filterValue,
                                                                  Long pendingStatus )
  {
    List<RecognitionAdvisorValueBean> raValueBeanList = null;
    @SuppressWarnings( "rawtypes" )
    Map results = new HashMap();
    CallPrcRecognitionAdvisor recognitionAdvisorpaxdata = new CallPrcRecognitionAdvisor( dataSource );
    results = recognitionAdvisorpaxdata.executeProcedure( userId, rowStart, rowEnd, sortColumnName, sortBy, excludeUpcoming, filterValue, pendingStatus );
    raValueBeanList = validateOutput( raValueBeanList, results );

    return raValueBeanList;

  }

  @Override
  public List<EligibleProgramBean> getEligiblePromotions( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.promotion.getRAEligiblePromotions" );

    query.setLong( "p_in_user_id", userId );
    query.setResultTransformer( new EligibleProgramResultTransformer() );
    return query.list();
  }

  private class EligibleProgramResultTransformer extends BaseResultTransformer
  {
    @Override
    public Object transformTuple( Object[] tuple, String[] aliases )
    {
      EligibleProgramBean bean = new EligibleProgramBean();
      bean.setProgramId( extractLong( tuple[0] ) );
      bean.setProgramName( extractString( tuple[1] ) );

      return bean;
    }
  }

  @Override
  public List<RecognitionAdvisorValueBean> getRATeamMemberReminderData( Long paxId )
  {
    List<RecognitionAdvisorValueBean> raValueBeanList = null;
    @SuppressWarnings( "rawtypes" )
    Map results = new HashMap();
    CallPrcRecognitionAdvisorTeamMemberEmail recognitionAdvisorPaxdata = new CallPrcRecognitionAdvisorTeamMemberEmail( dataSource );
    results = recognitionAdvisorPaxdata.executeProcedure( paxId );
    raValueBeanList = validateOutput( raValueBeanList, results );

    return raValueBeanList;

  }

  @SuppressWarnings( "unchecked" )
  private List<RecognitionAdvisorValueBean> validateOutput( List<RecognitionAdvisorValueBean> raValueBeanList, @SuppressWarnings( "rawtypes" ) Map results )
  {
    if ( BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
    }
    else
    {
      raValueBeanList = (List<RecognitionAdvisorValueBean>)results.get( "p_out_user_data" );
    }
    return raValueBeanList;
  }

  @Override
  public List<RecognitionAdvisorValueBean> getRANewHireForEmail( Long paxId )
  {
    List<RecognitionAdvisorValueBean> raValueBeanList = null;
    @SuppressWarnings( "rawtypes" )
    Map results = new HashMap();
    CallPrcRecognitionAdvisorNewHireForEmail recognitionAdvisorNewHireForEmail = new CallPrcRecognitionAdvisorNewHireForEmail( dataSource );
    results = recognitionAdvisorNewHireForEmail.executeProcedure( paxId );
    raValueBeanList = validateOutput( raValueBeanList, results );
    return raValueBeanList;
  }

  @Override
  public Long getLongOverDueNewHireForManager( Long participantId )
  {
    @SuppressWarnings( "rawtypes" )
    Map results = new HashMap();
    CallPrcRecognitionAdvisorNewHireForEmail recognitionAdvisorNewHireForEmail = new CallPrcRecognitionAdvisorNewHireForEmail( dataSource );
    results = recognitionAdvisorNewHireForEmail.executeProcedure( participantId );
    return raValidateOutput( results );
  }

  @SuppressWarnings( "unchecked" )
  private Long raValidateOutput( @SuppressWarnings( "rawtypes" ) Map results )
  {
    List<RecognitionAdvisorValueBean> raValueBeanList = null;
    Long userId = null;
    if ( BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
    }
    else
    {
      raValueBeanList = (List<RecognitionAdvisorValueBean>)results.get( "p_out_user_data" );

      if ( raValueBeanList.size() > 0 )
      {
        userId = raValueBeanList.get( 0 ).getId();
      }
    }
    return userId;
  }

  public List<AlertsValueBean> checkNewEmployeeAndTeamMemberExist( Long participantId )
  {
    Query query = getSession().createSQLQuery( "select fnc_ra_actions(:participantId) from dual" );
    query.setParameter( "participantId", participantId );

    String nhAndTmEmpCheckFlag = (String)query.list().get( 0 );
    String[] a = nhAndTmEmpCheckFlag.split( "\\|" );
    List<AlertsValueBean> raAlertList = new ArrayList<AlertsValueBean>();

    if ( a[0] != null )
    {
      AlertsValueBean newEmpAlterBean = new AlertsValueBean();
      newEmpAlterBean.setActivityType( ActivityType.RA_ALERT_NEW_HIRE );
      newEmpAlterBean.setNewHireAlert( a[0].toUpperCase().equals( "Y" ) ? true : false );
      if ( !a[0].toUpperCase().equals( "N" ) && !a[0].toUpperCase().equals( "Y" ) )
      {
        User user = getUserDAO().getUserById( new Long( a[0] ) );
        newEmpAlterBean.setNhFirstName( user.getFirstName() );
        newEmpAlterBean.setNewHireAlert( true );
        newEmpAlterBean.setNhUserId( user.getId() );
      }
      raAlertList.add( newEmpAlterBean );
    }
    if ( a[1] != null && !a[1].toUpperCase().equals( "N" ) )
    {
      AlertsValueBean ODEmpAlterBean = new AlertsValueBean();
      ODEmpAlterBean.setActivityType( ActivityType.RA_ALERT_OVER_DUE );
      ODEmpAlterBean.setOverDueAlert( a[1].toUpperCase().equals( "Y" ) ? true : false );
      if ( !a[1].toUpperCase().equals( "N" ) && !a[1].toUpperCase().equals( "Y" ) )
      {
        User user = getUserDAO().getUserById( new Long( a[1] ) );
        ODEmpAlterBean.setOdFirstName( user.getFirstName() );
        ODEmpAlterBean.setOverDueAlert( true );
        ODEmpAlterBean.setOdUserId( user.getId() );
      }
      raAlertList.add( ODEmpAlterBean );
    }

    return raAlertList;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public List<RecognitionAdvisorUnusedBudgetBean> getRAUnusedBudgetReminderData( Long participantId )
  {
    List<RecognitionAdvisorUnusedBudgetBean> raValueBeanList = null;
    @SuppressWarnings( "rawtypes" )
    Map results = new HashMap();
    CallPrcRecognitionAdvisorUnusedBudgetEmail recognitionAdvisorPaxdata = new CallPrcRecognitionAdvisorUnusedBudgetEmail( dataSource );
    results = recognitionAdvisorPaxdata.executeProcedure( participantId );
    if ( BAD_OUTPUT.equals( results.get( OUTPUT_RETURN_CODE ) ) )
    {
      log.error( "Stored procedure returned error. Procedure returned: " + results.get( OUTPUT_RETURN_CODE ) );
    }
    else
    {
      raValueBeanList = (List<RecognitionAdvisorUnusedBudgetBean>)results.get( "p_out_user_data" );
    }

    return raValueBeanList;
  }

  @Override
  public boolean isHavingMembers( Long userId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.isHavingMembers" );
    query.setParameter( "userId", userId );
    return null == query.uniqueResult() ? false : true;
  }

}
