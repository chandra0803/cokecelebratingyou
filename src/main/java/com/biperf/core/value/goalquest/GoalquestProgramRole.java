package com.biperf.core.value.goalquest;

/**
 * Roles for a goalquest program, as in participant, manager, owner.
 */
public enum GoalquestProgramRole
{
  
  PARTICIPANT( "Selector" ), 
  MANAGER( "Manager" ), 
  MULTILEVEL_MANAGER( "multiowner" ), 
  OWNER( "Owner" ); 

  /** Status code string in honeycomb system */
  private final String honeycombCode;

  private GoalquestProgramRole( String honeycombCode )
  {
    this.honeycombCode = honeycombCode;
  }

  /** Status code string in honeycomb system */
  public String getHoneycombCode()
  {
    return honeycombCode;
  }

}
