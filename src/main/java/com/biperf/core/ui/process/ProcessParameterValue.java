/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/javaui/com/biperf/core/ui/process/ProcessParameterValue.java,v $
 */

package com.biperf.core.ui.process;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.biperf.core.domain.process.ProcessParameter;
import com.biperf.core.service.util.ProcessUtil;

/**
 * ProcessParameterValue.
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
 * <td>leep</td>
 * <td>Apr 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ProcessParameterValue implements Serializable
{
  private String name;

  private String value;

  private String dataType;

  private String formatType;

  private String sourceType;

  private String[] values;

  private boolean secret;

  private String processBeanName;

  public String getDataType()
  {
    return dataType;
  }

  public void setDataType( String dataType )
  {
    this.dataType = dataType;
  }

  public String getFormatType()
  {
    return formatType;
  }

  public void setFormatType( String formatType )
  {
    this.formatType = formatType;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public String getValue()
  {
    return value;
  }

  public void setValue( String value )
  {
    this.value = value;
  }

  public String[] getValues()
  {
    return values;
  }

  public void setValues( String[] values )
  {
    this.values = values;
  }

  /**
   * @return value of sourceType property
   */
  public String getSourceType()
  {
    return sourceType;
  }

  /**
   * @param sourceType value for sourceType property
   */
  public void setSourceType( String sourceType )
  {
    this.sourceType = sourceType;
  }

  /**
   * @return value of processBeanName property
   */
  public String getProcessBeanName()
  {
    return processBeanName;
  }

  /**
   * @param processBeanName value for processBeanName property
   */
  public void setProcessBeanName( String processBeanName )
  {
    this.processBeanName = processBeanName;
  }

  public List getSourceValueChoices()
  {
    Map processParameterDefinitions = ProcessUtil.getProcessParameterDefinitions( processBeanName );
    ProcessParameter processParameter = (ProcessParameter)processParameterDefinitions.get( name );

    return processParameter.getSourceValueChoices();
  }

  public boolean isSecret()
  {
    return secret;
  }

  public void setSecret( boolean secret )
  {
    this.secret = secret;
  }

}
