/**
 * 
 */

package com.biperf.core.ui.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.enums.ProcessParameterInputFormatType;
import com.biperf.core.domain.enums.ReportName;
import com.biperf.core.process.AdminTestBudgetReminderNotificationEmailProcess;
import com.biperf.core.process.AdminTestCelebMgrNonResponseEmailProcess;
import com.biperf.core.process.AdminTestCelebMgrNotificationEmailProcess;
import com.biperf.core.process.AdminTestCelebRecogReceivedEmailProcess;
import com.biperf.core.process.AdminTestPurlContributorsInvitationEmailProcess;
import com.biperf.core.process.AdminTestPurlMgrNotificationEmailProcess;
import com.biperf.core.process.AdminTestPurlRecipientEmailProcess;
import com.biperf.core.process.AdminTestRecogPurlContributorsNonResponseEmailProcess;
import com.biperf.core.process.AdminTestRecogPurlManagersNonResponseEmailProcess;
import com.biperf.core.process.AdminTestRecogReceivedEmailProcess;
import com.biperf.core.process.EStatementProcess;
import com.biperf.core.process.EcardMigrationProcess;
import com.biperf.core.process.client.CokeLikesAndCommentsProcess;
import com.biperf.core.ui.BaseForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.StringUtil;

/**
 * ProcessLaunchForm.
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
 * <td>Nov 16, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessLaunchForm extends BaseForm
{
  // Non domain data
  private String method;

  private String processId;
  private String processName;
  private List processParameterValueList = new ArrayList();
  private int countOfParameters;
  private String processBeanName = "";

  // This field is used for validation purpose
  private String startDate;
  // This string constant is used for validation purpose
  public static final String Start_Date = "Start Date";
  // Client customizations for wip #23129 starts
  private Long sweepPromoId;
  private String sweepMonthYear;

  public Long getSweepPromoId()
  {
    return sweepPromoId;
  }

  public void setSweepPromoId( Long sweepPromoId )
  {
    this.sweepPromoId = sweepPromoId;
  }

  public String getSweepMonthYear()
  {
    return sweepMonthYear;
  }

  public void setSweepMonthYear( String sweepMonthYear )
  {
    this.sweepMonthYear = sweepMonthYear;
  }
  // Client customizations for wip #23129 ends
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
        processParameterMap.put( processParameterValue.getName(), valueArray );
      }
      // Report Extract Process allows optional process parameters. Do not add to the
      // processParameterMap if the parameter is not used
      else if ( processParameterValue.getValue() != null && !processParameterValue.getValue().equals( "" ) )
      {
        valueArray = new String[1];
        valueArray[0] = processParameterValue.getValue();
        processParameterMap.put( processParameterValue.getName(), valueArray );
      }
    }

    return processParameterMap;
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

  public List getProcessParameterValueList()
  {
    return processParameterValueList;
  }

  public void setProcessParameterValueList( List processParameterValueList )
  {
    this.processParameterValueList = processParameterValueList;
  }

  public int getCountOfParameters()
  {
    return countOfParameters;
  }

  public void setCountOfParameters( int countOfParameters )
  {
    this.countOfParameters = countOfParameters;
  }

  public String getProcessBeanName()
  {
    return processBeanName;
  }

  public void setProcessBeanName( String processBeanName )
  {
    this.processBeanName = processBeanName;
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
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
   * Validate the properties that have been set from this HTTP request, and return an
   * <code>ActionErrors</code> object that encapsulates any validation errors that have been
   * found. If no errors are found, return <code>null</code> or an <code>ActionErrors</code>
   * object with no recorded error messages. Validation is being done inside the form because there
   * are dynamic fields that may or may not need validating.
   * 
   * @param actionMapping the ActionMapping used to select this instance
   * @param request the HTTP request that is being processed
   * @return org.apache.struts.action.ActionErrors - collection of errors found on validation of
   *         data in form
   */
  public ActionErrors validate( ActionMapping actionMapping, HttpServletRequest request )
  {
    ActionErrors actionErrors = super.validate( actionMapping, request );

    if ( actionErrors == null )
    {
      actionErrors = new ActionErrors();
    }

    if ( this.getProcessBeanName().equals( EStatementProcess.BEAN_NAME ) )
    {
      validateParameterValueListForEstatement( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestRecogPurlContributorsNonResponseEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestRecogPurlContributorsNonResponse( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestRecogPurlManagersNonResponseEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestRecogPurlManagersNonResponse( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestPurlRecipientEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestPurlRecipient( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestRecogReceivedEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestRecogReceivedEmail( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestCelebRecogReceivedEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestCelebRecogReceivedEmail( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestCelebMgrNonResponseEmailProcess.BEAN_NAME ) || this.getProcessBeanName().equals( AdminTestCelebMgrNotificationEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestCelebMgrNonResponseEmail( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestPurlContributorsInvitationEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestPurlContributorsInvitationEmail( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestPurlMgrNotificationEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestPurlMgrNotificationEmail( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessBeanName().equals( AdminTestBudgetReminderNotificationEmailProcess.BEAN_NAME ) )
    {
      validateParameterValueListForAdminTestBudgetReminderNotificationEmail( this.getProcessParameterValueList(), actionErrors );
    }
    else if ( this.getProcessParameterValueList().size() > 0 )
    {
      // Skip validation for ecard migration
      if ( !this.getProcessBeanName().equals( EcardMigrationProcess.BEAN_NAME ) && !this.getProcessBeanName().equals( CokeLikesAndCommentsProcess.BEAN_NAME ))
      {
        validateParameterValueList( this.getProcessParameterValueList(), actionErrors );
      }
    }

    return actionErrors;
  }

  /**
   * Validate the values entered for UserCharacteristics. Made this public static so it can be used
   * by other forms to validate characteristics.
   * 
   * @param characteristicValueList is a list of UserCharacteristicFormBeans
   * @param actionErrors
   */
  private void validateParameterValueList( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;

      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {

        processParameterValue = (ProcessParameterValue)processParameterIter.next();

        if ( processParameterValue.getFormatType().equals( "check_boxes" ) )
        {

          if ( processParameterValue.getValues() == null || processParameterValue.getValues().length == 0 )
          {

            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
          }
        }
        else
        {

          if ( processParameterValue.getValue() == null || processParameterValue.getValue().equals( "" ) )
          {

            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
          }
        }
      }
    }
  }

  /**
   * Validate the values entered for UserCharacteristics. Made this public static so it can be used
   * by other forms to validate characteristics.
   * 
   * @param characteristicValueList is a list of UserCharacteristicFormBeans
   * @param actionErrors
   */
  private void validateParameterValueListForReportExtract( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;

      String reportName = null;
      String parentNodeId = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {

        processParameterValue = (ProcessParameterValue)processParameterIter.next();

        if ( processParameterValue.getFormatType().equals( "check_boxes" ) )
        {

          if ( processParameterValue.getValues() == null || processParameterValue.getValues().length == 0 )
          {

            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
          }
        }
        else
        {
          // These are required parameters for all Reports in Report Extract Process
          if ( processParameterValue.getName().equals( "reportName" ) )
          {
            reportName = processParameterValue.getValue();
          }
          else if ( processParameterValue.getName().equals( "parentNodeId" ) )
          {
            parentNodeId = processParameterValue.getValue();
          }

          if ( processParameterValue.getName().equals( "reportName" ) || processParameterValue.getName().equals( "startDate" ) || processParameterValue.getName().equals( "endDate" ) )
          {
            if ( processParameterValue.getValue() == null || processParameterValue.getValue().equals( "" ) )
            {

              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
            }
          }
        }
      }
      if ( reportName != null && !reportName.equals( ReportName.CERTIFICATE ) )
      {
        if ( parentNodeId == null || parentNodeId.equals( "" ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", "parentNodeId" ) );
        }
      }
    }
  }

  /**
   * Validate the values entered for UserCharacteristics. Made this public static so it can be used
   * by other forms to validate characteristics.
   * 
   * @param characteristicValueList is a list of UserCharacteristicFormBeans
   * @param actionErrors
   */
  private void validateParameterValueListForEstatement( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;

      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {

        processParameterValue = (ProcessParameterValue)processParameterIter.next();

        if ( processParameterValue.getFormatType().equals( "check_boxes" ) )
        {

          if ( processParameterValue.getValues() == null || processParameterValue.getValues().length == 0 )
          {

            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
          }
        }
        else
        {
          // These are required parameters for all Reports in Report Extract Process
          if ( processParameterValue.getName().equals( "userId" ) || processParameterValue.getName().equals( "password" ) || processParameterValue.getName().equals( "campaignNumber" ) )
          {
            if ( processParameterValue.getValue() == null || processParameterValue.getValue().equals( "" ) )
            {

              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
            }
          }
          if ( processParameterValue.getName().equals( "startDate" ) )
          {
            if ( processParameterValue.getValue() != null && !processParameterValue.getValue().equals( "" ) )
            {
              startDate = processParameterValue.getValue();
            }
          }

          // Start checking the dates if the process parameter is enddate
          if ( processParameterValue.getName().equals( "endDate" ) )
          {
            if ( startDate == null && processParameterValue.getValue() != null && !processParameterValue.getValue().equals( "" ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", Start_Date ) );
            }
            if ( startDate != null && processParameterValue.getValue().equals( "" ) )
            {
              actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
            }
            // check for StartDate value and EndDate value
            if ( startDate != null && processParameterValue.getValue() != null && !processParameterValue.getValue().equals( "" ) )
            {
              // Verify that end date is after start date
              if ( DateUtils.toDate( startDate ) != null && DateUtils.toDate( processParameterValue.getValue() ) != null
                  && DateUtils.toDate( startDate ).after( DateUtils.toDate( processParameterValue.getValue() ) ) )
              {
                actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "admin.budget.errors.START_DATE_AFTER_END_DATE" ) );
              }
            }
          }
        }
      }
    }
  }

  /**
   * Escaping validation for custom form elements
   * @param characteristicValueList
   * @param actionErrors
   */
  private void validateParameterValueListForAdminTestRecogPurlContributorsNonResponse( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( processParameterValue.getName().equals( "purlRecipientUserName" ) || processParameterValue.getName().equals( "purlContributorUserName" )
            || processParameterValue.getName().equals( "purlInvitedContributorUserName" ) || processParameterValue.getName().equals( "promotionId" )
            || processParameterValue.getName().equals( "awardDate" ) || processParameterValue.getName().equals( "recipientLocale" ) )
        {
          if ( StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
          }
        }
      }
    }
  }

  /**
   * Escaping validation for custom form elements
   * @param characteristicValueList
   * @param actionErrors
   */
  private void validateParameterValueListForAdminTestRecogPurlManagersNonResponse( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( processParameterValue.getName().equals( "purlRecipientUserName" ) || processParameterValue.getName().equals( "purlManagerUserName" )
            || processParameterValue.getName().equals( "promotionId" ) || processParameterValue.getName().equals( "awardDate" ) || processParameterValue.getName().equals( "recipientLocale" ) )
        {
          if ( StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
          }
        }
      }
    }
  }

  /**
   * Escaping validation for custom form elements
   * @param characteristicValueList
   * @param actionErrors
   */
  private void validateParameterValueListForAdminTestPurlRecipient( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( processParameterValue.getName().equals( "purlRecipientUserName" ) || processParameterValue.getName().equals( "promotionId" ) || processParameterValue.getName().equals( "awardDate" )
            || processParameterValue.getName().equals( "award" ) || processParameterValue.getName().equals( "recipientLocale" ) )
        {
          if ( StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
          {
            actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
          }
        }
      }
    }
  }

  private void validateParameterValueListForAdminTestRecogReceivedEmail( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( !processParameterValue.getName().equals( "awardAmount" ) && !processParameterValue.getName().equals( "badgePromotionId" ) && StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
        }
      }
    }
  }

  private void validateParameterValueListForAdminTestCelebRecogReceivedEmail( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( !processParameterValue.getName().equals( "awardAmount" ) && !processParameterValue.getName().equals( "badgePromotionId" ) && !processParameterValue.getName().equals( "managerUserName" )
            && !processParameterValue.getName().equals( "celebManagerMessage" ) && !processParameterValue.getName().equals( "managerAboveUserName" )
            && !processParameterValue.getName().equals( "celebManagerAboveMessage" ) && !processParameterValue.getName().equals( "anniversaryNumberOfYears" )
            && !processParameterValue.getName().equals( "anniversaryNumberOfDays" ) && StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
        }
      }
    }
  }

  private void validateParameterValueListForAdminTestCelebMgrNonResponseEmail( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( !processParameterValue.getName().equals( "anniversaryNumberOfYears" ) && !processParameterValue.getName().equals( "anniversaryNumberOfDays" )
            && !processParameterValue.getName().equals( "customFormField1" ) && !processParameterValue.getName().equals( "customFormField2" )
            && !processParameterValue.getName().equals( "customFormField3" ) && StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
        }
      }
    }
  }

  private void validateParameterValueListForAdminTestPurlContributorsInvitationEmail( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( !processParameterValue.getName().equals( "customFormField1" ) && !processParameterValue.getName().equals( "customFormField2" )
            && !processParameterValue.getName().equals( "customFormField3" ) && StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
        }
      }
    }
  }

  private void validateParameterValueListForAdminTestPurlMgrNotificationEmail( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( !processParameterValue.getName().equals( "customFormField1" ) && !processParameterValue.getName().equals( "customFormField2" )
            && !processParameterValue.getName().equals( "customFormField3" ) && StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
        }
      }
    }
  }

  private void validateParameterValueListForAdminTestBudgetReminderNotificationEmail( List characteristicValueList, ActionErrors actionErrors )
  {
    if ( this.getProcessParameterValueList() != null && this.getProcessParameterValueList().size() > 0 )
    {
      ProcessParameterValue processParameterValue = null;
      for ( Iterator processParameterIter = this.getProcessParameterValueList().iterator(); processParameterIter.hasNext(); )
      {
        processParameterValue = (ProcessParameterValue)processParameterIter.next();
        if ( !processParameterValue.getName().equals( "budgetEndDate" ) && StringUtil.isNullOrEmpty( processParameterValue.getValue() ) )
        {
          actionErrors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( "user.characteristic.errors.REQUIRED", processParameterValue.getName() ) );
        }
      }
    }
  }
}
