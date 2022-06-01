
package com.biperf.core.ui.diyquiz;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * DIYQuizMaterialView.
 * 
 * @author kandhi
 * @since Jul 10, 2013
 * @version 1.0
 */
public class DIYQuizMaterialView
{
  private Long id;
  private boolean isNew;
  private boolean isSaved;
  private int pageNumber;
  private String type;
  private String text;
  private boolean isEditing;
  private String status;
  private String cmContentResource;
  private boolean ignore;

  private List<DIYQuizMaterialFileView> files = new ArrayList<DIYQuizMaterialFileView>();

  public Long getId()
  {
    return id;
  }

  public void setId( Long id )
  {
    this.id = id;
  }

  public boolean getIsNew()
  {
    return isNew;
  }

  public void setIsNew( boolean isNew )
  {
    this.isNew = isNew;
  }

  public boolean getIsSaved()
  {
    return isSaved;
  }

  public void setIsSaved( boolean isSaved )
  {
    this.isSaved = isSaved;
  }

  public int getPageNumber()
  {
    return pageNumber;
  }

  public void setPageNumber( int pageNumber )
  {
    this.pageNumber = pageNumber;
  }

  public String getType()
  {
    return type;
  }

  public void setType( String type )
  {
    this.type = type;
  }

  public String getText()
  {
    return text;
  }

  public void setText( String text )
  {
    this.text = text;
  }

  public boolean getIsEditing()
  {
    return isEditing;
  }

  public void setIsEditing( boolean isEditing )
  {
    this.isEditing = isEditing;
  }

  public String getStatus()
  {
    return status;
  }

  public void setStatus( String status )
  {
    this.status = status;
  }

  public String getCmContentResource()
  {
    return cmContentResource;
  }

  public boolean isIgnore()
  {
    return ignore;
  }

  public void setIgnore( boolean ignore )
  {
    this.ignore = ignore;
  }

  public List<DIYQuizMaterialFileView> getFiles()
  {
    // With out ignore check the JSON in the prepare update is having empty file views
    if ( !isIgnore() )
    {
      // Temp fix for index out of bounds set to size 10 after discussing with Pramod
      if ( files.isEmpty() )
      {
        for ( int i = 0; i < 10; i++ )
        {
          files.add( new DIYQuizMaterialFileView() );
        }
      }
    }
    return files;
  }

  public void setFiles( List<DIYQuizMaterialFileView> files )
  {
    this.files = files;
  }

  public DIYQuizMaterialFileView getFiles( int index )
  {
    while ( index >= files.size() )
    {
      files.add( new DIYQuizMaterialFileView() );
    }
    return (DIYQuizMaterialFileView)files.get( index );
  }

  public void setFiles( DIYQuizMaterialFileView file )
  {
    files.add( file );
  }

  public void setCmContentResource( String cmContentResource )
  {
    this.cmContentResource = cmContentResource;
  }

}
