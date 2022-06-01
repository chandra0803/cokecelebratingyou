/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessParameter;
import com.biperf.core.process.BaseProcess;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.utils.BeanLocator;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Nov 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessUtil
{
  /**
   * protected since util class and no one should instantiate, just call static methods.
   */
  protected ProcessUtil()
  {
    super();
  }

  public static Map filterSystemParameters( Map parameterMap )
  {
    LinkedHashMap filteredMap = new LinkedHashMap( parameterMap );
    filteredMap.remove( ProcessService.PROCESS_BEAN_NAME );
    filteredMap.remove( ProcessService.RUN_BY_USER_ID_PARAM_NAME );

    return filteredMap;
  }

  public static Map getProcessParameterDefinitions( String processBeanName )
  {
    Map processParameters;

    if ( !StringUtils.isBlank( processBeanName ) )
    {
      BaseProcess baseProcess = (BaseProcess)BeanLocator.getBean( processBeanName );
      processParameters = baseProcess.getProcessParameters();
    }
    else
    {
      processParameters = new LinkedHashMap();
    }

    return processParameters;
  }

  /**
   * Get a Map of Parameter Values as a List of formatted Parameter Values (showing formatted value
   * for picklist items and for db source items) keyed by the parameter name.
   * 
   * @param parameterValueMap Map<String paramName, String[] paramValue>
   * @return a Map<String, List>
   */
  public static Map getFormattedParameterValueMap( Process process, Map parameterValueMap )
  {
    LinkedHashMap formattedParameterValueMap = new LinkedHashMap();

    for ( Iterator iter = parameterValueMap.entrySet().iterator(); iter.hasNext(); )
    {

      Map.Entry entry = (Map.Entry)iter.next();
      String processParameterName = (String)entry.getKey();
      String[] unformattedProcessParameterValues = (String[])entry.getValue();
      String[] formattedProcessParameterValues = new String[unformattedProcessParameterValues.length];

      formattedParameterValueMap.put( processParameterName, formattedProcessParameterValues );

      for ( int i = 0; i < unformattedProcessParameterValues.length; i++ )
      {
        String processParameterValue = unformattedProcessParameterValues[i];

        ProcessParameter processParameter = (ProcessParameter)process.getProcessParameters().get( processParameterName );
        if ( processParameter != null )
        {

          formattedProcessParameterValues[i] = processParameter.getFormattedChoice( processParameterValue );
        }
        else
        {
          // Might happen if db has bad data or parameter names changed after deployment. As a
          // backup just display the
          // raw value
          formattedProcessParameterValues[i] = processParameterValue;
        }
      }
    }

    return formattedParameterValueMap;
  }
}
