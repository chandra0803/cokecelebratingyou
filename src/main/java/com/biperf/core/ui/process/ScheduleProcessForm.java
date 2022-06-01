/**
 * 
 */

package com.biperf.core.ui.process;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.quartz.CronExpression;

import com.biperf.core.domain.enums.AmPmType;
import com.biperf.core.domain.enums.DayOfMonthType;
import com.biperf.core.domain.enums.DayOfWeekType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.enums.ProcessParameterInputFormatType;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

/**
 * ScheduleProcessForm.
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
 * <td>asondgeroth</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ScheduleProcessForm extends BaseForm
{
  private static final Log log = LogFactory.getLog( ScheduleProcessForm.class );
  private static final String GMT = "GMT";

  private String processId;
  private String scheduleId;
  private String processName;
  private String frequency;
  private String time;
  private String date = DateUtils.displayDateFormatMask;
  private String startDate = DateUtils.displayDateFormatMask;
  private String endDate = DateUtils.displayDateFormatMask;
  private String dayOfWeek;
  private String dayOfMonth;
  private String amPm;
  private String cronExpression;

  private String method;
  public static final String FORM_NAME = "scheduleProcessForm";
  private List processParameterValueList = new ArrayList();
  private int countOfParameters;

  public Map returnParametersAsMap()
  {
    Map processParameterMap = new HashMap();
    String[] valueArray;

    for ( Iterator processParameterIter = processParameterValueList.iterator(); processParameterIter.hasNext(); )
    {

      ProcessParameterValue processParameterValue = (ProcessParameterValue)processParameterIter.next();

      if ( processParameterValue.getFormatType().equals( ProcessParameterInputFormatType.CHECK_BOXES ) )
      {

        valueArray = processParameterValue.getValues();

      }
      else
      {

        valueArray = new String[1];
        valueArray[0] = processParameterValue.getValue();
      }

      processParameterMap.put( processParameterValue.getName(), valueArray );
    }

    return processParameterMap;
  }

  public ProcessSchedule toDomain()
  {
    ProcessSchedule schedule = new ProcessSchedule();

    try
    {
      schedule.setProcessFrequencyType( ProcessFrequencyType.lookup( this.frequency ) );

      if ( this.frequency.equals( ProcessFrequencyType.ONE_TIME_ONLY ) )
      {
        schedule.setStartDate( DateUtils.toStartDate( this.date ) );
      }
      else
      {
        schedule.setStartDate( DateUtils.toStartDate( this.startDate ) );

        if ( this.endDate != null && !this.endDate.equals( "" ) )
        {
          schedule.setEndDate( DateUtils.toEndDate( this.endDate ) );
        }
      }

      if ( this.frequency.equals( ProcessFrequencyType.MONTHLY ) )
      {
        schedule.setDayOfMonthType( DayOfMonthType.lookup( this.dayOfMonth ) );
      }
      else if ( this.frequency.equals( ProcessFrequencyType.WEEKLY ) )
      {
        schedule.setDayOfWeekType( DayOfWeekType.lookup( this.dayOfWeek ) );
      }

      if ( !StringUtils.isEmpty( cronExpression ) )
      {
        schedule.setCronExpression( cronExpression );
      }
      else // time based job
      {
        // Expects date format of "08:24am", "8:24am", "11:15pm"
        SimpleDateFormat sdf = new SimpleDateFormat( "hh:mmaz" );
        Date timeOfDay = sdf.parse( this.time + amPm + GMT );
        schedule.setTimeOfDayMillis( new Long( timeOfDay.getTime() ) );
      }
    }
    catch( Exception e )
    {
      // TODO: This shouldn't happen - validation should have handled any problems
      log.error( e.getMessage(), e );
    }

    return schedule;
  }

  public String getCronExpression()
  {
    return cronExpression;
  }

  public void setCronExpression( String cronExpression )
  {
    this.cronExpression = cronExpression;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public String getFrequency()
  {
    return frequency;
  }

  public void setFrequency( String frequency )
  {
    this.frequency = frequency;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getProcessId()
  {
    return processId;
  }

  public void setProcessId( String processId )
  {
    this.processId = processId;
  }

  public String getProcessName()
  {
    return processName;
  }

  public void setProcessName( String processName )
  {
    this.processName = processName;
  }

  public String getScheduleId()
  {
    return scheduleId;
  }

  public void setScheduleId( String scheduleId )
  {
    this.scheduleId = scheduleId;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getTime()
  {
    return time;
  }

  public void setTime( String time )
  {
    this.time = time;
  }

  public String getDayOfMonth()
  {
    return dayOfMonth;
  }

  public void setDayOfMonth( String dayOfMonth )
  {
    this.dayOfMonth = dayOfMonth;
  }

  public String getDayOfWeek()
  {
    return dayOfWeek;
  }

  public void setDayOfWeek( String dayOfWeek )
  {
    this.dayOfWeek = dayOfWeek;
  }

  public String getAmPm()
  {
    return amPm;
  }

  public void setAmPm( String amPm )
  {
    this.amPm = amPm;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate( String date )
  {
    this.date = date;
  }

  public List getProcessParameterValueList()
  {
    return processParameterValueList;
  }

  public void setProcessParameterValueList( List processParameterValueList )
  {
    this.processParameterValueList = processParameterValueList;
  }

  /**
   * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   */
  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    this.countOfParameters = RequestUtils.getOptionalParamInt( request, "countOfParameters" );

    this.setProcessParameterValueList( getEmptyValueList( countOfParameters ) );
  }

  /**
   * resets the value list with empty UserCharacteristicFormBeans
   * 
   * @param valueListCount
   * @return List
   */
  public static List getEmptyValueList( int valueListCount )
  {
    List valueList = new ArrayList();

    for ( int i = 0; i < valueListCount; i++ )
    {
      valueList.add( new ProcessParameterValue() );
    }

    return valueList;
  }

  /**
   * Validate the form before submitting Overridden from
   * 
   * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping,
   *      javax.servlet.http.HttpServletRequest)
   * @param mapping
   * @param request
   * @return ActionErrors
   */
  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );

    // ***** Validate promotionTypes *****/
    for ( Iterator iter = processParameterValueList.iterator(); iter.hasNext(); )
    {
      ProcessParameterValue temp = (ProcessParameterValue)iter.next();
      if ( temp.getName().equals( "promotionTypes" ) && ( temp.getValues() == null || temp.getValues().length == 0 ) )
      {
        // At least one promotion type must be selected.
        errors.add( "processParameterValueList", new ActionMessage( "process.schedule.MISSING_PROMOTION_TYPE" ) );
      }
    }
    // ***** Validate frequency *****/
    String frequency = request.getParameter( "frequency" );
    // Make sure its not empty
    if ( frequency == null || frequency.length() == 0 )
    {
      errors.add( "frequency", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "process.schedule.FREQUENCY" ) ) );
    }

    // ***** Validate time if it's not a cron based expression *****/
    if ( !StringUtils.isEmpty( frequency ) && !frequency.equals( "cron" ) )
    {
      String time = request.getParameter( "time" );
      // Make sure its not empty
      if ( time == null || time.length() == 0 && !StringUtils.isEmpty( frequency ) && !frequency.equals( "cron" ) )
      {
        errors.add( "time", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "process.schedule.TIME" ) ) );
      }
      else
      {
        // Now validate the time
        try
        {
          SimpleDateFormat sdf = new SimpleDateFormat( "hh:mma" );
          sdf.parse( time + AmPmType.AM );
          sdf.parse( time + AmPmType.PM );
        }
        catch( ParseException e )
        {
          errors.add( "time", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_TIME, CmsResourceBundle.getCmsBundle().getString( "process.schedule.TIME" ) ) );
        }
      }
    }

    // If one time only, then only check 'date' otherwise check 'startDate' and 'endDate'
    if ( frequency != null && frequency.equals( ProcessFrequencyType.ONE_TIME_ONLY ) )
    {
      // ***** Validate date *****/
      String date = request.getParameter( "date" );

      // Make sure its not empty
      if ( date == null || date.length() == 0 )
      {
        errors.add( "date", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "process.schedule.DATE" ) ) );
      }
      else
      {
        // Now validate the date
        try
        {
          DateUtils.toStartDate( date );
        }
        catch( ParseException e )
        {
          errors.add( "date", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "process.schedule.DATE" ) ) );
        }
      }
    }
    else
    {
      if ( frequency != null )
      {
        if ( frequency.equals( ProcessFrequencyType.MONTHLY ) )
        {
          // ***** Validate dayOfMonth *****/
          String dayOfMonth = request.getParameter( "dayOfMonth" );
          // Make sure its not empty
          if ( dayOfMonth == null || dayOfMonth.length() == 0 )
          {
            errors.add( "dayOfMonth", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "process.schedule.DAY_OF_MONTH" ) ) );
          }
        }
        else if ( frequency.equals( ProcessFrequencyType.WEEKLY ) )
        {
          // ***** Validate dayOfWeek *****/
          String dayOfWeek = request.getParameter( "dayOfWeek" );
          // Make sure its not empty
          if ( dayOfWeek == null || dayOfWeek.length() == 0 )
          {
            errors.add( "dayOfWeek", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "process.schedule.DAY_OF_WEEK" ) ) );
          }
        }
        else if ( frequency.equals( ProcessFrequencyType.CRON_EXPRESSION ) )
        {
          System.out.println( "IS VALID??? " + CronExpression.isValidExpression( this.cronExpression ) + " : " + cronExpression );
          System.out.println( "\n\nVALIDATE THE CRON HERE!!!\n" );
        }
      }

      // ***** Validate startDate and endDate *****/
      String startDate = request.getParameter( "startDate" );
      String endDate = request.getParameter( "endDate" );

      // Make sure its not empty
      if ( startDate == null || startDate.length() == 0 )
      {
        errors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "process.schedule.START_DATE" ) ) );
      }
      else
      {
        Date dateStart = null;
        Date dateEnd = null;

        // Now validate the date
        try
        {
          dateStart = DateUtils.toStartDate( startDate );
        }
        catch( ParseException e )
        {
          errors.add( "startDate", new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "process.schedule.START_DATE" ) ) );
        }

        if ( endDate != null && endDate.length() > 0 )
        {
          try
          {
            dateEnd = DateUtils.toEndDate( endDate );
            if ( dateEnd.before( dateStart ) )
            {
              // The date is before the start date
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_END_BEFORE_START_DATE ) );
            }

          }
          catch( ParseException e )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_DATE, CmsResourceBundle.getCmsBundle().getString( "process.schedule.END_DATE" ) ) );
          }
        }
      }
    }

    return errors;
  }

}
