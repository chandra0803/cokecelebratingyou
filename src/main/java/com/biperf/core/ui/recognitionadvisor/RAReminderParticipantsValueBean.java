
package com.biperf.core.ui.recognitionadvisor;

import java.io.Serializable;
import java.util.List;

import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude( value = Include.NON_EMPTY )
public class RAReminderParticipantsValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  @JsonProperty( "recentlyAddedParticipants" )
  List<RecognitionAdvisorValueBean> recentlyAddedParticipants;
  @JsonProperty( "overDueParticipants" )
  List<RecognitionAdvisorValueBean> overDueParticipants;
  @JsonProperty( "upComingParticipants" )
  List<RecognitionAdvisorValueBean> upComingParticipants;
  @JsonProperty( "sortingAndPaginationParticipants" )
  List<RecognitionAdvisorValueBean> sortingAndPaginationParticipants;

  public List<RecognitionAdvisorValueBean> getRecentlyAddedParticipants()
  {
    return recentlyAddedParticipants;
  }

  public void setRecentlyAddedParticipants( List<RecognitionAdvisorValueBean> recentlyAddedParticipants )
  {
    this.recentlyAddedParticipants = recentlyAddedParticipants;
  }

  public List<RecognitionAdvisorValueBean> getOverDueParticipants()
  {
    return overDueParticipants;
  }

  public void setOverDueParticipants( List<RecognitionAdvisorValueBean> overDueParticipants )
  {
    this.overDueParticipants = overDueParticipants;
  }

  public List<RecognitionAdvisorValueBean> getUpComingParticipants()
  {
    return upComingParticipants;
  }

  public void setUpComingParticipants( List<RecognitionAdvisorValueBean> upComingParticipants )
  {
    this.upComingParticipants = upComingParticipants;
  }

  public List<RecognitionAdvisorValueBean> getSortingAndPaginationParticipants()
  {
    return sortingAndPaginationParticipants;
  }

  public void setSortingAndPaginationParticipants( List<RecognitionAdvisorValueBean> sortingAndPaginationParticipants )
  {
    this.sortingAndPaginationParticipants = sortingAndPaginationParticipants;
  }

}
