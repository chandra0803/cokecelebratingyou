
package com.biperf.core.ui.fileload;

import com.biperf.core.ui.BaseForm;

public class ImportFileStageForm extends BaseForm
{

  private String securityToken;
  private String filename;
  private String fileType;

  public String getSecurityToken()
  {
    return securityToken;
  }

  public void setSecurityToken( String securityToken )
  {
    this.securityToken = securityToken;
  }

  public String getFilename()
  {
    return filename;
  }

  public void setFilename( String filename )
  {
    this.filename = filename;
  }

  public String getFileType()
  {
    return fileType;
  }

  public void setFileType( String fileType )
  {
    this.fileType = fileType;
  }

}
