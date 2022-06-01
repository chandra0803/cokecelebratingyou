
package com.biperf.core.value.hc;

import java.util.ArrayList;
import java.util.List;

import com.biw.hc.contest.api.response.EligibleProgramVO;
import com.biw.hc.contest.api.response.GoalSelectionResponse;

public class GoalquestDetailsResponse
{
  /** List of eligible goalquest programs and its details */
  private List<GoalquestProgramResponse> programs = new ArrayList<>();
  
  public static class GoalquestProgramResponse
  {
    /** Contains overview details about the program and participant */
    private EligibleProgramVO program;
    
    /** May be null if no goal is selected. Has goal and progress information. */
    private GoalSelectionResponse goalSelection;
    
    public EligibleProgramVO getProgram()
    {
      return program;
    }

    public void setProgram( EligibleProgramVO program )
    {
      this.program = program;
    }

    public GoalSelectionResponse getGoalSelection()
    {
      return goalSelection;
    }

    public void setGoalSelection( GoalSelectionResponse goalSelection )
    {
      this.goalSelection = goalSelection;
    }
  }

  public List<GoalquestProgramResponse> getPrograms()
  {
    return programs;
  }

  public void setPrograms( List<GoalquestProgramResponse> programs )
  {
    this.programs = programs;
  }

}
