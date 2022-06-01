
package com.biperf.core.ui.mobilerecogapp.view;

import java.util.ArrayList;
import java.util.List;

import com.biperf.core.value.contributor.ContributionsList;
import com.biperf.core.value.contributor.Contributor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude( value = Include.NON_NULL )
public class ContributorView
{

  Contributor contributor;
  List<ContributionsList> contributions = new ArrayList<ContributionsList>();

  public Contributor getContributor()
  {
    return contributor;
  }

  public void setContributor( Contributor contributor )
  {
    this.contributor = contributor;
  }

  public List<ContributionsList> getContributions()
  {
    return contributions;
  }

  public void setContributions( List<ContributionsList> contributions )
  {
    this.contributions = contributions;
  }

}
