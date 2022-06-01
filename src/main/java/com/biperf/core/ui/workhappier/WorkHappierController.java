/*
 * (c) 2015 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/javaui/com/biperf/core/ui/workhappier/WorkHappierController.java,v $
 */

package com.biperf.core.ui.workhappier;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.workhappier.WorkHappier;
import com.biperf.core.service.workhappier.WorkHappierService;
import com.biperf.core.ui.BaseController;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author poddutur
 * @since Nov 25, 2015
 */
public class WorkHappierController extends BaseController
{
  private static final Log localLogger = LogFactory.getLog( WorkHappierController.class );

  protected static ObjectMapper mapper = new ObjectMapper();

  @Override
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    WorkHappinessSliderViewBean workHappinessSliderViewBean = new WorkHappinessSliderViewBean();

    List<WorkHappinessDataViewBean> resultZonesData = new ArrayList<WorkHappinessDataViewBean>();

    List<WorkHappier> workHappierData = getWorkHappierService().getWorkHappier();

    for ( WorkHappier workHappier : workHappierData )
    {
      WorkHappinessDataViewBean workHappinessDataViewBean = new WorkHappinessDataViewBean();
      workHappinessDataViewBean.setHeadline( workHappier.getHeadlineFromCM() );
      workHappinessDataViewBean.setFeeling( workHappier.getFeelingWithPrefixFromCM() );
      workHappinessDataViewBean.setMinValue( workHappier.getMinValue() );
      workHappinessDataViewBean.setThoughts( workHappier.getThoughtsFromCM().split( ";" ) );
      resultZonesData.add( workHappinessDataViewBean );
    }

    workHappinessSliderViewBean.setResultZonesData( resultZonesData );

    request.setAttribute( "happinessSliderOpts", toJson( workHappinessSliderViewBean ) );

  }

  protected String toJson( Object bean )
  {
    ObjectMapper mapper = getObjectMapper();
    Writer writer = new StringWriter();

    try
    {
      mapper.writeValue( writer, bean );
    }
    catch( Throwable t )
    {
      localLogger.error( "\n\n\nERROR!!!\n\n\n" + t.getMessage() );
    }

    return writer.toString();
  }

  protected ObjectMapper getObjectMapper()
  {
    return mapper;
  }

  private WorkHappierService getWorkHappierService()
  {
    return (WorkHappierService)getService( WorkHappierService.BEAN_NAME );
  }
}
