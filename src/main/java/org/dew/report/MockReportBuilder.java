package org.dew.report;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Mock IReportBuilder.
 */
public 
class MockReportBuilder implements IReportBuilder 
{
  protected ReportInfo reportInfo;
  
  @Override
  public 
  void setReportInfo(ReportInfo reportInfo) 
  {
    this.reportInfo = reportInfo;
  }
  
  @Override
  public 
  ReportInfo getReportInfo() 
  {
    return reportInfo;
  }
  
  @Override
  public 
  void generate(OutputStream outputStream) 
    throws Exception 
  {
    if (reportInfo == null) {
      throw new Exception("ReportInfo undefined.");
    }
    
    if(outputStream == null) return;
    
    Object values = reportInfo.getAreaValues(0);
    
    PrintStream ps = new PrintStream(outputStream);
    
    ps.println(values);
  }
}
