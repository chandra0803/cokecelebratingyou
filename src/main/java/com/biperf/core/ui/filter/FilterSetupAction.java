
package com.biperf.core.ui.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.FilterSetupType;
import com.biperf.core.domain.homepage.CrossPromotionalModuleApp;
import com.biperf.core.domain.homepage.FilterAppSetup;
import com.biperf.core.domain.homepage.ModuleApp;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.home.FilterAppSetupService;
import com.biperf.core.service.util.ServiceError;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.utils.ServiceErrorStrutsUtils;
import com.objectpartners.cms.util.CmsResourceBundle;

public class FilterSetupAction extends BaseDispatchAction
{
  /**
   * Dispatcher.  Default to home page display.  Too much work to append 'method=display'
   * to all the paths that lead to the home page.  
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response )
  {
    return initialDisplay( mapping, actionForm, request, response );
  }

  public ActionForward initialDisplay( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    return mapping.findForward( ActionConstants.DISPLAY_FORWARD );
  }

  public ActionForward populateFilter( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.DISPLAY_FORWARD );
    FilterSetupForm setupForm = (FilterSetupForm)form;
    List<FilterAppSetup> listappSetup = getFilterAppSetupService().getFilterAppSetupByFilterTypeCode( setupForm.getFilterCode() );
    if ( listappSetup != null && listappSetup.size() == 0 )
    {
      setupForm.setFilterCode( setupForm.getFilterCode() );
      setupForm.setPriorityOne( new ArrayList<FilterSetupBean>() );
      setupForm.setSearchEnabled( true ); // default search enabled is true
    }
    setupForm.loadFilterSetup( listappSetup );

    return forward;
  }

  public ActionForward addAnother( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.DISPLAY_FORWARD );
    ActionMessages errors = new ActionMessages();
    FilterSetupForm setupForm = (FilterSetupForm)form;
    setupForm.addEmptyFilterSetupBean();

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }
    return forward;
  }

  public ActionForward display( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.DISPLAY_FORWARD );
    // form.reset( mapping, request );
    return forward;
  }

  public ActionForward doCancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.CANCEL_FORWARD );
    return forward;
  }

  public FilterAppSetup savePriority( List<FilterSetupBean> priority, String filterCode, int priorityValue, boolean searchEnabled )
  {
    FilterAppSetup filterAppSetup = null;

    for ( FilterSetupBean bean : priority )
    {
      if ( bean.getFilterAppSetupId() != null && !bean.getFilterAppSetupId().equals( "" ) && Integer.parseInt( bean.getFilterAppSetupId() ) != 0 ) // update
      {
        filterAppSetup = getFilterAppSetupService().getFilterAppSetupById( new Long( bean.getFilterAppSetupId() ) );
        filterAppSetup.setPriority( priorityValue );
        filterAppSetup.setMobileEnabled( new Boolean( false ) );
        filterAppSetup.setFilterSetupType( FilterSetupType.lookup( filterCode ) );
        ModuleApp moduleApp = getFilterAppSetupService().getModuleAppById( new Long( bean.getTileId() ) ); // updating
                                                                                                           // new
                                                                                                           // moduleApp
        filterAppSetup.setModuleApp( moduleApp );
        filterAppSetup.setOrderNumber( Integer.parseInt( bean.getRank() ) );
        filterAppSetup.setSearchEnabled( searchEnabled );
        filterAppSetup = getFilterAppSetupService().save( filterAppSetup );
      }
      else // insert
      {
        filterAppSetup = new FilterAppSetup();
        filterAppSetup.setPriority( priorityValue );
        filterAppSetup.setMobileEnabled( new Boolean( false ) );
        filterAppSetup.setFilterSetupType( FilterSetupType.lookup( filterCode ) );
        ModuleApp moduleApp = getFilterAppSetupService().getModuleAppById( new Long( bean.getTileId() ) );
        filterAppSetup.setModuleApp( moduleApp );
        filterAppSetup.setOrderNumber( Integer.parseInt( bean.getRank() ) );
        filterAppSetup.setSearchEnabled( searchEnabled );
        filterAppSetup = getFilterAppSetupService().save( filterAppSetup );
      }
    }

    return filterAppSetup;
  }

  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionMessages errors = new ActionMessages();
    ActionForward forward = mapping.findForward( ActionConstants.DISPLAY_FORWARD );
    FilterSetupForm setupForm = (FilterSetupForm)form;
    List<FilterSetupBean> priority = new ArrayList<FilterSetupBean>();
    String filterCode = null;
    int priorityValue = 0;
    FilterAppSetup filterAppSetup = null;
    String successMessage = "";

    if ( isCancelled( request ) )
    {
      // setupForm.setFilterCode( "" );
      // initialDisplay( mapping, setupForm, request, response );
      forward = mapping.findForward( ActionConstants.CANCEL_FORWARD );
      return forward;
    }

    if ( setupForm.getPriorityOne() != null && setupForm.getPriorityOne().size() != 0 )
    {
      priority = setupForm.getPriorityOne();
      filterCode = setupForm.getFilterCode();
      priorityValue = 1;
      filterAppSetup = savePriority( priority, filterCode, priorityValue, setupForm.isSearchEnabled() );
    }

    if ( setupForm.getCrossPromotionIdList() != null && setupForm.getCrossPromotionIdList().size() > 0 )
    {
      List<CrossPromotionalModuleApp> crossPromotionalModuleAppList = setupForm.getCrossPromotionIdList();

      for ( int i = 1; i <= crossPromotionalModuleAppList.size(); i++ )
      {
        CrossPromotionalModuleApp crossPromotionalModuleApp = crossPromotionalModuleAppList.get( i - 1 );
        CrossPromotionalModuleApp dbCrossPromotionalModuleApp = (CrossPromotionalModuleApp)getFilterAppSetupService().getModuleAppById( crossPromotionalModuleApp.getId() );
        if ( dbCrossPromotionalModuleApp != null )
        {
          // put the order if we have selected
          if ( crossPromotionalModuleApp.isSelected() )
          {
            dbCrossPromotionalModuleApp.setOrder( i );
          }
          else
          {
            dbCrossPromotionalModuleApp.setOrder( -1 );
          }

          try
          {
            getFilterAppSetupService().save( dbCrossPromotionalModuleApp );
          }
          catch( ServiceErrorException e )
          {
            List<ServiceError> serviceErrors = e.getServiceErrors();
            ServiceErrorStrutsUtils.convertServiceErrorsToActionErrors( serviceErrors, errors );
            saveErrors( request, errors );
          }
        }
      }
    }

    if ( filterAppSetup != null )
    {
      successMessage = CmsResourceBundle.getCmsBundle().getString( "filtersetup.setup.SAVE_SUCCESS" );

    }
    else
    {
      // QC bug #2717 fix
      successMessage = CmsResourceBundle.getCmsBundle().getString( "filtersetup.setup.SAVE_INACTIVE" );

    }
    populateFilter( mapping, form, request, response );
    request.setAttribute( "successMessage", successMessage );
    return forward;

  }

  public ActionForward removeTile( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.DISPLAY_FORWARD );
    FilterSetupForm setupForm = (FilterSetupForm)form;
    FilterAppSetup filterAppSetup = null;

    if ( setupForm.getPriorityAction().equals( "priorityOneRemove" ) )
    {
      if ( setupForm.getPriorityOne() != null && setupForm.getPriorityOne().size() != 0 )
      {
        int i = 0;
        for ( Iterator<FilterSetupBean> beanIter = setupForm.getPriorityOne().iterator(); beanIter.hasNext(); )
        {
          FilterSetupBean bean = beanIter.next();
          String removeTile = bean.getRemoveTile();
          if ( removeTile != null && removeTile.equalsIgnoreCase( "Y" ) )
          {
            beanIter.remove();
            // check if newly added rows and not saved in DB
            if ( bean.getFilterAppSetupId() != null && !bean.getFilterAppSetupId().equals( "" ) && Integer.parseInt( bean.getFilterAppSetupId() ) != 0 )
            {
              filterAppSetup = getFilterAppSetupService().getFilterAppSetupById( new Long( bean.getFilterAppSetupId() ) );
              getFilterAppSetupService().delete( filterAppSetup );
            }
          }
          else
          {
            bean.setRank( String.valueOf( i++ ) );
          }
        }
        for ( FilterSetupBean bean : setupForm.getPriorityOne() )
        {
          if ( bean.getFilterAppSetupId() != null && !bean.getFilterAppSetupId().equals( "" ) && Integer.parseInt( bean.getFilterAppSetupId() ) != 0 )
          {
            filterAppSetup = getFilterAppSetupService().getFilterAppSetupById( new Long( bean.getFilterAppSetupId() ) );
            getFilterAppSetupService().save( filterAppSetup );
          }
        }
      }
    }
    return forward;
  }

  public ActionForward reorder( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    ActionForward forward = mapping.findForward( ActionConstants.DISPLAY_FORWARD );
    ActionMessages errors = new ActionMessages();

    FilterSetupForm setupForm = (FilterSetupForm)form;
    int oldSequence = 0;
    int newSequence = 0;
    int oldRank = 0;
    int newRank = 0;
    try
    {
      oldSequence = Integer.parseInt( setupForm.getOldSequence() );
      newSequence = Integer.parseInt( setupForm.getNewElementSequenceNum() );
    }
    catch( NumberFormatException nfe )
    {
      //
    }
    if ( oldSequence != 0 && newSequence != 0 )
    {
      if ( setupForm.getPriorityAction().equals( "priorityOneReorder" ) && setupForm.getPriorityOneListSize() != 0 )
      {
        for ( Iterator<FilterSetupBean> iter = setupForm.getPriorityOne().iterator(); iter.hasNext(); )
        {
          FilterSetupBean filterSetupBean = iter.next();
          if ( filterSetupBean.getSequenceNumber() == oldSequence )
          {
            filterSetupBean.setSequenceNumber( newSequence );
            String rank = filterSetupBean.getRank();
            newRank = getNewRank( oldSequence, newSequence, rank );
            filterSetupBean.setRank( new Integer( newRank ).toString() );
          }
          else if ( filterSetupBean.getSequenceNumber() == newSequence )
          {
            filterSetupBean.setSequenceNumber( oldSequence );
            String rank = filterSetupBean.getRank();
            oldRank = getOldRank( oldSequence, newSequence, rank );
            filterSetupBean.setRank( new Integer( oldRank ).toString() );
          }
        }
      }
      setupForm.resortPriority();
    }
    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      forward = mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return forward;
  }

  private int getNewRank( int oldSequence, int newSequence, String rank )
  {
    int newRank = 0;
    if ( oldSequence > newSequence ) // rank will decrease for the instance moving up
    {
      newRank = Integer.parseInt( rank ) - 1;
    }
    else
    {
      newRank = Integer.parseInt( rank ) + 1;
    }
    return newRank;
  }

  private int getOldRank( int oldSequence, int newSequence, String rank )
  {
    int oldRank = 0;
    if ( oldSequence > newSequence )// rank will increase for the instance moving down
    {
      oldRank = Integer.parseInt( rank ) + 1;
    }
    else
    {
      oldRank = Integer.parseInt( rank ) - 1;
    }
    return oldRank;
  }

  private FilterAppSetupService getFilterAppSetupService()
  {
    return (FilterAppSetupService)getService( FilterAppSetupService.BEAN_NAME );
  }

}
