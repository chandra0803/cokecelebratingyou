
package com.biperf.core.ui.diycommunication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.service.SAO;
import com.biperf.core.service.hierarchy.HierarchyService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.participant.ListBuilderService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.diycommunication.ParticipantList;

public class DIYAudienceExportAction extends BaseDispatchAction
{
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )
  {
    Long sessionAudienceId;
    List<ParticipantList> sortedPaxList = new ArrayList<ParticipantList>();
    List<Long> audienceIds = (ArrayList<Long>)request.getSession().getAttribute( "audienceResultsList" );
    Set<Audience> audienceSet = new HashSet<Audience>();

    Map clientStateMap = ClientStateUtils.getClientStateMap( request );
    sessionAudienceId = (Long)ClientStateUtils.getParameterValueAsObject( request, clientStateMap, "audienceId" );

    for ( Iterator<Long> iter = audienceIds.iterator(); iter.hasNext(); )
    {
      Long audienceId = iter.next();
      if ( audienceId.equals( sessionAudienceId ) )
      {
        Audience audience = getAudienceService().getAudienceById( audienceId, null );
        audienceSet.add( audience );

        List<ParticipantList> newFvbPaxSecondList = getFormattedAudiences( audienceSet );
        Collections.sort( newFvbPaxSecondList, new ParticipantNameComparator() );
        sortedPaxList.addAll( newFvbPaxSecondList );

        DIYAudienceExportBean audienceBean = new DIYAudienceExportBean();
        audienceBean.setExportList( sortedPaxList );
        audienceBean.extractAsCsv( response );
      }

    }

    return null;
  }

  private List<ParticipantList> getFormattedAudiences( Set<Audience> audienceSet )
  {
    Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
    List<ParticipantList> formattedPaxList = new ArrayList<ParticipantList>();
    List<FormattedValueBean> fvbPaxSecondList = getListBuilderService().searchParticipants( audienceSet, primaryHierarchy.getId(), true, null, true );
    for ( Iterator<FormattedValueBean> fvbIter = fvbPaxSecondList.iterator(); fvbIter.hasNext(); )
    {
      FormattedValueBean fmv = fvbIter.next();
      fmv.setFnameLname( getParticipantService().getLNameFNameByPaxId( fmv.getId() ) );
      formattedPaxList.add( new ParticipantList( fmv ) );
    }
    return formattedPaxList;
  }

  protected static SAO getService( String beanName )
  {
    return (SAO)BeanLocator.getBean( beanName );
  }

  public AudienceService getAudienceService()
  {
    return (AudienceService)getService( AudienceService.BEAN_NAME );
  }

  public HierarchyService getHierarchyService()
  {
    return (HierarchyService)getService( HierarchyService.BEAN_NAME );
  }

  public ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  public ListBuilderService getListBuilderService()
  {
    return (ListBuilderService)getService( ListBuilderService.BEAN_NAME );
  }

}

class ParticipantNameComparator implements Comparator
{
  public int compare( Object o1, Object o2 )
  {
    if ( ! ( o1 instanceof ParticipantList ) || ! ( o2 instanceof ParticipantList ) )
    {
      throw new ClassCastException( "Object is not a PromotionView object!" );
    }

    String fname1 = ( (ParticipantList)o1 ).getName();
    String fname2 = ( (ParticipantList)o2 ).getName();
    return fname1.compareTo( fname2 );
  }

}
