
package com.biperf.core.value.goalquest;

import java.util.Arrays;

public enum HoneycombGoalQuestViewStatus
{

  GOAL_SELECTION( "goal_selection", "open" ), 
  GOAL_SELECTED( "goal_selected", "open" ), 
  NO_GOAL_SELECTED( "no_goal_selected", "started" ), 
  NO_PROGRESS( "no_progress", "started" ), 
  PROGRESS_LOADED_NO_ISSUANCE( "progress_loaded_no_issuance", "started" ), 
  FINAL_ISSUANCE( "final_issuance", "ended" );

  /** Status code string in honeycomb system */
  private final String honeycombCode;

  /** Comparable status code string here in the G world */
  private final String gCode;

  private HoneycombGoalQuestViewStatus( String honeycombCode, String gCode )
  {
    this.honeycombCode = honeycombCode;
    this.gCode = gCode;
  }
  
  public static HoneycombGoalQuestViewStatus findByHCCode( String honeycombCode )
  {
    return Arrays.stream( values() )
        .filter( status -> status.getHoneycombCode().equalsIgnoreCase( honeycombCode ) )
        .findFirst()
        .orElse( null );
  }

  /** Status code string in honeycomb system */
  public String getHoneycombCode()
  {
    return honeycombCode;
  }

  /** Comparable status code string here in the G world */
  public String getgCode()
  {
    return gCode;
  }
  
}
