/**
 * 
 */

package com.biperf.core.value;

/**
 * @author poddutur
 *
 */
public class PromotionsValueBean
{
  private Long id;
  private String name;
  private String approvalEndDate;

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

  public String getApprovalEndDate()
  {
    return approvalEndDate;
  }

  public void setApprovalEndDate( String approvalEndDate )
  {
    this.approvalEndDate = approvalEndDate;
  }
}
