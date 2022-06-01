
package com.biperf.core.ui.recognition.purl;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.domain.hierarchy.Node;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.participant.ParticipantInfoView;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.objectpartners.cms.util.CmsResourceBundle;

public class PreSelectedContributorsBean
{
  private List<String> messages = new ArrayList<String>();
  private List<Contributor> preselectedContributors = new ArrayList<Contributor>();
  // Client customizations for WIP #26532 starts
  private List<String> purlAllowedDomains = new ArrayList<String>();
  // Client customizations for WIP #26532 ends
  private static final String PRESELECTED = "preselected";
  private PresetSearchFiltersBean presetSearchFiltersBean;
  private ContributorSearchFilterBean contributorSearchFiltersBean;

  public PreSelectedContributorsBean( List<Node> childNodes )
  {
    presetSearchFiltersBean = new PresetSearchFiltersBean( childNodes,
                                                           CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.SELECT_BY_TEAM" ),
                                                           CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.SELECT_TEAM" ) );
  }

  public PreSelectedContributorsBean( List<Participant> contributors, List<Node> childNodes, List<String> allowedDomains )
  {
    if ( contributors != null && !contributors.isEmpty() )
    {
      preselectedContributors = new ArrayList<Contributor>( contributors.size() );

      for ( Participant participant : contributors )
      {
        preselectedContributors.add( new Contributor( participant ) );
      }
    }
    presetSearchFiltersBean = new PresetSearchFiltersBean( childNodes,
                                                           CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.ADD_TEAM_MEMBERS" ),
                                                           CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.SELECT_TEAM" ) );
    contributorSearchFiltersBean = new ContributorSearchFilterBean( childNodes,
                                                                    CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.ADD_TEAM_MEMBERS" ),
                                                                    CmsResourceBundle.getCmsBundle().getString( "recognitionSubmit.contributors.SELECT_TEAM" ) );
 // Client customizations for WIP #26532 starts
    if ( allowedDomains != null )
    {
      this.purlAllowedDomains = allowedDomains;
    }
    // Client customizations for WIP #26532 end
  }

  public List<Contributor> getPreselectedContributors()
  {
    return preselectedContributors;
  }

  public List<String> getMessages()
  {
    return messages;
  }

  // Client customizations for WIP #26532 starts
  public List<String> getPurlAllowedDomains()
  {
    return purlAllowedDomains;
  }

  public void setPurlAllowedDomains( List<String> purlAllowedDomains )
  {
    this.purlAllowedDomains = purlAllowedDomains;
  }
  // Client customizations for WIP #26532 ends
  
  @JsonProperty( "presetSearchFilters" )
  public PresetSearchFiltersBean getPresetSearchFiltersBean()
  {
    return presetSearchFiltersBean;
  }

  public void setPresetSearchFiltersBean( PresetSearchFiltersBean presetSearchFiltersBean )
  {
    this.presetSearchFiltersBean = presetSearchFiltersBean;
  }

  @JsonProperty( "contributorTeamsSearchFilters" )
  public ContributorSearchFilterBean getContributorSearchFiltersBean()
  {
    return contributorSearchFiltersBean;
  }

  public void setContributorSearchFiltersBean( ContributorSearchFilterBean contributorSearchFiltersBean )
  {
    this.contributorSearchFiltersBean = contributorSearchFiltersBean;
  }

  public static final class Contributor extends ParticipantInfoView
  {
    private String contribType;
    private String sourceType;

    public Contributor( Participant participant )
    {
      super( participant );
      setCountryCode( participant.getPrimaryCountryCode() );
      setCountryName( participant.getPrimaryCountryName() );
      setContribType( PRESELECTED );
      setSourceType( participant.getSourceType() );
    }

    public String getContribType()
    {
      return contribType;
    }

    public void setContribType( String contribType )
    {
      this.contribType = contribType;
    }

    public String getSourceType()
    {
      return sourceType;
    }

    public void setSourceType( String sourceType )
    {
      this.sourceType = sourceType;
    }
  }

}
