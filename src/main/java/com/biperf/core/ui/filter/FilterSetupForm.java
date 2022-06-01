
package com.biperf.core.ui.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.service.util.ServiceErrorMessageKeys;
import com.biperf.core.ui.BaseActionForm;
import com.biperf.core.ui.utils.RequestUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class FilterSetupForm extends BaseActionForm
{
  public static final String FORM_NAME = "filterSetupForm";

  private String filterCode;

  private boolean priority;

  private String method;

  private String oldSequence;

  private String newElementSequenceNum;

  private String priorityAction;

  private List<FilterSetupBean> priorityOne = new ArrayList<FilterSetupBean>();

  private Map<Integer, CrossPromotionalModuleApp> crossPromotionId = new HashMap<Integer, CrossPromotionalModuleApp>();

  public static final int DEFAULT_ROW_COUNT = 2;

  private boolean searchEnabled;

  public static String getFormName()
  {
    return FORM_NAME;
  }

  public boolean isPriority()
  {
    return priority;
  }

  public void setPriority( boolean priority )
  {
    this.priority = priority;
  }

  public List<FilterSetupBean> getPriorityOne()
  {

    if ( priorityOne == null )
    {
      priorityOne = new ArrayList<FilterSetupBean>();
    }
    return priorityOne;
  }

  public int getPriorityOneListSize()
  {
    if ( this.priorityOne != null )
    {
      return this.priorityOne.size();
    }

    return 0;
  }

  public void setPriorityOne( List<FilterSetupBean> priorityOne )
  {
    this.priorityOne = priorityOne;
  }

  public String getMethod()
  {
    return method;
  }

  public void setMethod( String method )
  {
    this.method = method;
  }

  public String getFilterCode()
  {
    return filterCode;
  }

  public void setFilterCode( String filterCode )
  {
    this.filterCode = filterCode;
  }

  public String getOldSequence()
  {
    return oldSequence;
  }

  public void setOldSequence( String oldSequence )
  {
    this.oldSequence = oldSequence;
  }

  public String getNewElementSequenceNum()
  {
    return newElementSequenceNum;
  }

  public void setNewElementSequenceNum( String newElementSequenceNum )
  {
    this.newElementSequenceNum = newElementSequenceNum;
  }

  public void resortPriority()
  {
    if ( this.getPriorityAction().equals( "priorityOneReorder" ) )
    {
      if ( this.priorityOne != null )
      {
        Collections.sort( this.priorityOne );
      }
    }
  }

  public void loadFilterSetup( List<FilterAppSetup> listappSetup )
  {
    List<FilterSetupBean> listPriorityOneSetupBean = new ArrayList<FilterSetupBean>();
    int sequenceone = 0;
    if ( listappSetup != null && listappSetup.size() != 0 )
    {
      this.filterCode = listappSetup.get( 0 ).getFilterSetupType().getCode();
      for ( FilterAppSetup appSetup : listappSetup )
      {
        FilterSetupBean filterSetupBean = new FilterSetupBean();
        filterSetupBean.setFilterAppSetupId( appSetup.getId().toString() );
        filterSetupBean.setRank( String.valueOf( appSetup.getOrderNumber() ) );
        filterSetupBean.setTileId( appSetup.getModuleApp().getId().toString() );
        if ( appSetup.getPriority() == 1 )
        {
          filterSetupBean.setSequenceNumber( sequenceone + 1 );
          listPriorityOneSetupBean.add( filterSetupBean );
          sequenceone = sequenceone + 1;
        }
      }
      this.priorityOne = listPriorityOneSetupBean;
      this.searchEnabled = listappSetup.get( 0 ).isSearchEnabled(); // search enabled from db
    }
    if ( getPriorityOne() != null && getPriorityOne().isEmpty() )
    {
      priorityOne = this.buildEmptyFilterSetupBean( DEFAULT_ROW_COUNT, true );
    }
  }

  public void reset( ActionMapping mapping, HttpServletRequest request )
  {
    // reset needs to be used to populate an empty list of
    // FilterSetupBeans. If this is not done, the form wont initialize
    // properly.

    int priorityOneCount = RequestUtils.getOptionalParamInt( request, "priorityOneListSize" );
    priorityOne = buildEmptyFilterSetupBean( priorityOneCount, true );
  }

  public ActionErrors validate( ActionMapping mapping, HttpServletRequest request )
  {
    ActionErrors errors = super.validate( mapping, request );
    if ( getMethod() != null && !getMethod().equals( "" ) && ( getMethod().equals( "populateFilter" ) || getMethod().equals( "save" ) || getMethod().equals( "addAnother" ) ) )
    {
      if ( getFilterCode() != null && getFilterCode().equals( "" ) )
      {
        errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "filtersetup.setup.FILTER_NAME" ) ) );
      }
    }
    if ( getMethod() != null && !getMethod().equals( "" ) && getMethod().equals( "save" ) )
    {
      if ( getPriorityOne() != null && getPriorityOne().size() != 0 )
      {
        for ( int index1 = 0; index1 < getPriorityOne().size(); index1++ )
        {
          FilterSetupBean bean = getPriorityOne().get( index1 );
          if ( bean.getTileId() == null || bean.getTileId().equals( "" ) || Integer.parseInt( bean.getTileId() ) == 0 )
          {
            errors.add( ActionErrors.GLOBAL_MESSAGE,
                        new ActionMessage( ServiceErrorMessageKeys.SYSTEM_ERRORS_REQUIRED, CmsResourceBundle.getCmsBundle().getString( "filtersetup.setup.SELECT_TILE_IN_PRIORITY_ONE" ) ) );
            break;
          }
        }
      }

      int errorCount = 0;
      boolean hasError = false;
      if ( getPriorityOne() != null && getPriorityOne().size() != 0 )
      {
        for ( FilterSetupBean priorityOneOuterbean : getPriorityOne() ) // inside priority one
                                                                        // validation
        {
          if ( hasError )
          {
            break;
          }
          else
          {
            errorCount = 0;
          }
          for ( FilterSetupBean priorityOneInnerbean : getPriorityOne() )
          {
            if ( priorityOneOuterbean.getTileId() != null && !priorityOneOuterbean.getTileId().equals( "" ) && priorityOneOuterbean.getTileId().equals( priorityOneInnerbean.getTileId() ) )
            {
              errorCount = errorCount + 1;
            }
            if ( errorCount > 1 )
            {
              errors.add( ActionErrors.GLOBAL_MESSAGE, new ActionMessage( ServiceErrorMessageKeys.DUPLICATE_TILE_PRIORITY1 ) );
              hasError = true;
              break;
            }
          }
        }
      }
    }
    return errors;
  }

  private List<FilterSetupBean> buildEmptyFilterSetupBean( int priorityeCount, boolean priority )
  {
    List<FilterSetupBean> filterSetupBeansList = new ArrayList<FilterSetupBean>();
    int rank = priorityOne.size();
    for ( int index1 = 0; index1 < priorityeCount; index1++ )
    {
      FilterSetupBean filterSetupBean = new FilterSetupBean();
      filterSetupBean.setSequenceNumber( index1 + 1 );
      if ( priority )
      {
        filterSetupBean.setRank( String.valueOf( index1 ) );
      }
      else
      {
        filterSetupBean.setRank( String.valueOf( rank ) );
        rank++;
      }

      filterSetupBeansList.add( filterSetupBean );
    }
    return filterSetupBeansList;
  }

  public void addEmptyFilterSetupBean()
  {
    FilterSetupBean filterSetupBean = new FilterSetupBean();
    if ( this.getPriorityAction().equals( "priorityOneAdd" ) )
    {
      int prioritySequence = getPriorityOneListSize() + 1;
      filterSetupBean.setSequenceNumber( prioritySequence );
      filterSetupBean.setRank( String.valueOf( getPriorityOneListSize() ) );
      this.priorityOne.add( filterSetupBean );
    }
  }

  public String getPriorityAction()
  {
    return priorityAction;
  }

  public void setPriorityAction( String priorityAction )
  {
    this.priorityAction = priorityAction;
  }

  public CrossPromotionalModuleApp getCrossPromotionId( int index )
  {
    CrossPromotionalModuleApp bean = crossPromotionId.get( index );
    if ( bean == null )
    {
      bean = new CrossPromotionalModuleApp();
      crossPromotionId.put( index, bean );
    }
    return bean;
  }

  public List<CrossPromotionalModuleApp> getCrossPromotionIdList()
  {
    return new ArrayList<CrossPromotionalModuleApp>( crossPromotionId.values() );
  }

  public boolean isSearchEnabled()
  {
    return searchEnabled;
  }

  public void setSearchEnabled( boolean searchEnabled )
  {
    this.searchEnabled = searchEnabled;
  }
}
