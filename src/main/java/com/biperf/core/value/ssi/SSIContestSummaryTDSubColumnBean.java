
package com.biperf.core.value.ssi;

/**
 * 
 * SSIContestAdminDetailTDSubColumnView.
 * 
 * @author chowdhur
 * @since Jan 13, 2015
 */
public class SSIContestSummaryTDSubColumnBean
{
  private Long id;
  private String name;
  private String type;
  private String tableDisplayName;
  private boolean sortable;
  private String footerDisplayText;

  public SSIContestSummaryTDSubColumnBean()
  {

  }

  /**
   * @param id
   * @param name
   * @param tableDisplayName
   * @param sortable
   * @param footerDisplayText
   */
  public SSIContestSummaryTDSubColumnBean( Long id, String name, String type, String tableDisplayName, boolean sortable, String footerDisplayText )
  {
    super();
    this.id = id;
    this.name = name;
    this.type = type;
    this.tableDisplayName = tableDisplayName;
    this.sortable = sortable;
    this.footerDisplayText = footerDisplayText;
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

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getTableDisplayName()
  {
    return tableDisplayName;
  }

  public void setTableDisplayName( String tableDisplayName )
  {
    this.tableDisplayName = tableDisplayName;
  }

  public boolean isSortable()
  {
    return sortable;
  }

  public void setSortable( boolean sortable )
  {
    this.sortable = sortable;
  }

  public String getFooterDisplayText()
  {
    return footerDisplayText;
  }

  public void setFooterDisplayText( String footerDisplayText )
  {
    this.footerDisplayText = footerDisplayText;
  }
}
