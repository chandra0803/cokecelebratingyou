
package com.biperf.core.domain.journal;

import com.biperf.core.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonBackReference;

public class JournalBillCode extends BaseDomain
{
  @JsonBackReference
  private Journal journal;
  private String billCode1;
  private String billCode2;
  private String billCode3;
  private String billCode4;
  private String billCode5;
  private String billCode6;
  private String billCode7;
  private String billCode8;
  private String billCode9;
  private String billCode10;

  @Override
  public boolean equals( Object object )
  {
    if ( this == object )
    {
      return true;
    }
    if ( ! ( object instanceof JournalBillCode ) )
    {
      return false;
    }

    final JournalBillCode journalBillCode = (JournalBillCode)object;

    if ( getJournal() != null && !getJournal().equals( journalBillCode.getJournal() ) )
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return getJournal() == null ? 0 : getJournal().hashCode();
  }

  public Journal getJournal()
  {
    return journal;
  }

  public void setJournal( Journal journal )
  {
    this.journal = journal;
  }

  public String getBillCode1()
  {
    return billCode1;
  }

  public void setBillCode1( String billCode1 )
  {
    this.billCode1 = billCode1;
  }

  public String getBillCode2()
  {
    return billCode2;
  }

  public void setBillCode2( String billCode2 )
  {
    this.billCode2 = billCode2;
  }

  public String getBillCode3()
  {
    return billCode3;
  }

  public void setBillCode3( String billCode3 )
  {
    this.billCode3 = billCode3;
  }

  public String getBillCode4()
  {
    return billCode4;
  }

  public void setBillCode4( String billCode4 )
  {
    this.billCode4 = billCode4;
  }

  public String getBillCode5()
  {
    return billCode5;
  }

  public void setBillCode5( String billCode5 )
  {
    this.billCode5 = billCode5;
  }

  public String getBillCode6()
  {
    return billCode6;
  }

  public void setBillCode6( String billCode6 )
  {
    this.billCode6 = billCode6;
  }

  public String getBillCode7()
  {
    return billCode7;
  }

  public void setBillCode7( String billCode7 )
  {
    this.billCode7 = billCode7;
  }

  public String getBillCode8()
  {
    return billCode8;
  }

  public void setBillCode8( String billCode8 )
  {
    this.billCode8 = billCode8;
  }

  public String getBillCode9()
  {
    return billCode9;
  }

  public void setBillCode9( String billCode9 )
  {
    this.billCode9 = billCode9;
  }

  public String getBillCode10()
  {
    return billCode10;
  }

  public void setBillCode10( String billCode10 )
  {
    this.billCode10 = billCode10;
  }

}
