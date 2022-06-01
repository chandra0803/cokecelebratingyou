
package com.biperf.core.ui.recognition.purl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
 

import com.biperf.core.domain.enums.DepartmentType;
import com.biperf.core.domain.enums.PositionType;
import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.UserAddress;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.recognition.BaseRecognitionAction;
import com.biperf.core.ui.recognition.purl.PreSelectedContributorsBean.Contributor;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PickListValueBean;

public class PreSelectedContributorsAction extends BaseRecognitionAction
{
  @Override
  public ActionForward execute( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws Exception
  {
    Long recipientId = RequestUtils.getRequiredParamLong( request, REQUEST_PARAM_RECIPIENT_ID );
    Long nodeId = RequestUtils.getRequiredParamLong( request, REQUEST_PARAM_NODE_ID );

    List<Participant> preSelectedContributors = getParticipantService().getAllPreSelectedContributors( recipientId );

    List<Node> childNodes = getNodeService().getNodeAndNodesBelow( nodeId );

    // Client customizations for WIP #26532 starts
    List<String> allowedDomains = null;
    boolean isAllowPurlOutsideInvites = getParticipantService().isAllowePurlOutsideInvites( recipientId );
    if ( !isAllowPurlOutsideInvites )
    {
      // set the system default allowed domains if the user doesn't want to invite to outsiders
      String allowedDomainsString = getSystemVariableService().getPropertyByName( SystemVariableService.COKE_ACCEPTABLE_DOMAINS ).getStringVal();
      allowedDomains = Arrays.asList( StringUtils.split( allowedDomainsString, "," ) );
    }
    // convert to the bean
    PreSelectedContributorsBean bean = new PreSelectedContributorsBean( preSelectedContributors, childNodes, allowedDomains );
    // Client customizations for WIP #26532 ends
    // We need to set the country, department and the job position type by looping the
    // PreSelectedContributorsBean. This is because the constructor is looking for the hibernate
    // isInitialized to populate these fields and Since this is a native query the fields are not
    // getting populated. The look up for department and job postion is implemented here to improve
    // the query performance.
    if ( bean.getPreselectedContributors() != null )
    {
      for ( Contributor contributor : bean.getPreselectedContributors() )
      {
        if ( preSelectedContributors != null )
        {
          for ( Participant participant : preSelectedContributors )
          {
            PickListValueBean pickListDeptValueBean = getUserService().getPickListValueFromCMView( DepartmentType.PICKLIST_ASSET + ".items",
                                                                                                   participant.getLanguageType() == null
                                                                                                       ? UserManager.getDefaultLocale().toString()
                                                                                                       : participant.getLanguageType().getCode(),
                                                                                                   participant.getDepartmentType() );
            PickListValueBean pickListPositionValueBean = getUserService().getPickListValueFromCMView( PositionType.PICKLIST_ASSET + ".items",
                                                                                                       participant.getLanguageType() == null
                                                                                                           ? UserManager.getDefaultLocale().toString()
                                                                                                           : participant.getLanguageType().getCode(),
                                                                                                       participant.getPositionType() );
            if ( participant.getId().equals( contributor.getId() ) )
            {
              if ( participant.getUserAddresses() != null )
              {
                for ( Iterator iterator = participant.getUserAddresses().iterator(); iterator.hasNext(); )
                {
                  UserAddress address = (UserAddress)iterator.next();
                  contributor.setCountryCode( address.getAddress().getCountry().getCountryCode() );
                  contributor.setCountryName( address.getAddress().getCountry().getI18nCountryName() );
                }
              }
              if ( !StringUtils.isEmpty( participant.getDepartmentType() ) )
              {
                contributor.setDepartmentName( pickListDeptValueBean.getName() );
              }
              if ( !StringUtils.isEmpty( participant.getPositionType() ) )
              {
                contributor.setJobName( pickListPositionValueBean.getName() );
              }
            }
          }
        }
      }
    }
    // write the json to the response
    super.writeAsJsonToResponse( bean, response );

    return null;
  }
}
