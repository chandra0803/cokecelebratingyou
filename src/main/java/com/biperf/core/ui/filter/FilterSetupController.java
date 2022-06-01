
package com.biperf.core.ui.filter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.tiles.ComponentContext;

import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.FilterAppSetupComparator;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseController;

public class FilterSetupController extends BaseController
{
  public void onExecute( ComponentContext tileContext, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext ) throws Exception
  {
    request.setAttribute( "filterNameList", FilterSetupType.getAdjustableFilterSetupList() );
    List<ModuleApp> listpriorityOne = getFilterAppSetupService().getAllAvailableModuleApps();
    request.setAttribute( "tileNamePriorityOneList", getAvailableTiles( listpriorityOne ) );

    // TODO: Poorly named. This is a map, not a list.
    request.setAttribute( "listofAppSetup", getFilter( request ) );

    List<CrossPromotionalModuleApp> crossPromotionalModuleApps = getFilterAppSetupService().getCrossPromotionalModuleApps();

    for ( CrossPromotionalModuleApp crossPromotionalModuleApp : crossPromotionalModuleApps )
    {
      if ( crossPromotionalModuleApp.getOrder() != -1 )
      {
        crossPromotionalModuleApp.setSelected( true );
      }
    }
    request.setAttribute( "crossPromotionList", crossPromotionalModuleApps );
  }

  // TODO: This method has side effects outside of getting a value
  private HashMap<String, List<FilterAppSetup>> getFilter( HttpServletRequest request )
  {
    LinkedHashMap<String, List<FilterAppSetup>> map = new LinkedHashMap<String, List<FilterAppSetup>>();
    List list = FilterSetupType.getGenericList();
    for ( int i = 0; i < list.size(); i++ )
    {
      FilterSetupType setupType = (FilterSetupType)list.get( i );
      List<FilterAppSetup> appSetup = getFilterAppSetupService().getFilterAppSetupByFilterTypeCode( setupType.getCode() );

      if ( appSetup != null )
      {
        if ( appSetup.size() == 0 )
        {
          // This would probably be better as switch(setupType.getCode()
          if ( setupType.getCode().equals( FilterSetupType.HOME ) )
          {
            request.setAttribute( "homeTileFalse", true );
          }
          else if ( setupType.getCode().equals( FilterSetupType.INFORMATION ) )
          {
            request.setAttribute( "informationTileFalse", true );
          }
          else if ( setupType.getCode().equals( FilterSetupType.PROGRAMS ) )
          {
            request.setAttribute( "programsTileFalse", true );
          }
          else if ( setupType.getCode().equals( FilterSetupType.MANAGER ) )
          {
            request.setAttribute( "managerTileFalse", true );
          }
          else if ( setupType.getCode().equals( FilterSetupType.REPORTS ) )
          {
            request.setAttribute( "reportsFalse", true );
          }
        }
        Comparator<FilterAppSetup> filterAppSetupComparator = new FilterAppSetupComparator();
        Collections.sort( appSetup, filterAppSetupComparator );
        if ( !setupType.getCode().equals( FilterSetupType.REPORTS ) )
        {
          map.put( setupType.getName(), appSetup );
        }
      }
    }

    return map;
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

  public boolean isModuleInstalled( String module )
  {
    return getSystemVariableService().getPropertyByName( module ).getBooleanVal();
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private List getAvailableTiles( List<ModuleApp> avialableTiles )
  {
    if ( avialableTiles != null )
    {
      for ( Iterator<ModuleApp> audienceIterator = avialableTiles.iterator(); audienceIterator.hasNext(); )
      {
        ModuleApp app = audienceIterator.next();
        if ( !isModuleInstalled( SystemVariableService.INSTALL_GOAL_QUEST ) && app.getName().equals( "GoalQuest" ) )
        {
          audienceIterator.remove();
        }
        if ( !isModuleInstalled( SystemVariableService.INSTALL_RECOGNITION ) )
        {
          if ( app.getName().equals( "Browse Awards" ) || app.getName().equals( "Budget Meter" ) || app.getName().equals( "Public Recognition" ) || app.getName().equals( "Recognition" )
              || app.getName().equals( "PURL (Managers only)" ) )
          {
            audienceIterator.remove();
          }
        }
        if ( !isModuleInstalled( SystemVariableService.INSTALL_NOMINATIONS ) && app.getName().equals( "Nomination" ) )
        {
          audienceIterator.remove();
        }
        if ( !isModuleInstalled( SystemVariableService.INSTALL_QUIZZES ) && app.getName().equals( "Quiz" ) )
        {
          audienceIterator.remove();
        }
        if ( !isModuleInstalled( SystemVariableService.INSTALL_PRODUCTCLAIMS ) && app.getName().equals( "Product Claim" ) )
        {
          audienceIterator.remove();
        }
      }
    }
    return avialableTiles;
  }
}
