/**
 * 
 */

package com.biperf.core.value;

import com.biperf.core.domain.user.UserNode;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author poddutur
 *
 */
@JsonInclude( Include.NON_NULL )
public final class NodeBean
{
  private Long id;
  private String name;

  public NodeBean()
  {
    super();
  }

  public NodeBean( UserNode userNode )
  {
    this.id = userNode.getNode().getId();
    this.name = userNode.getNode().getName();
  }

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

}
