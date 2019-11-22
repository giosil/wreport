package org.dew.report;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public
class ReportFactory
{
  public static
  ReportInfo getReportInfo(String sTitle, String sTemplate)
  {
    ReportInfo reportInfo = new ReportInfo(sTitle, sTemplate);
    
    URL urlTemplate = Thread.currentThread().getContextClassLoader().getResource("reports/" + reportInfo.getTemplate());
    if(urlTemplate != null) {
      
      String sImagesPath = urlTemplate.toString();
      int iLastSep = sImagesPath.lastIndexOf('/');
      if(iLastSep > 0) sImagesPath = sImagesPath.substring(0, iLastSep + 1);
      
      Map<String,Object> mapParameters = new HashMap<String,Object>();
      mapParameters.put("IMAGES_PATH", sImagesPath);
      reportInfo.setParameters(mapParameters);
      
    }
    
    return reportInfo;
  }
  
  public static
  IReportBuilder getReportBuilder(String sType)
  {
    IReportBuilder reportBuilder = new JasperReportsBuilder();
    
    if(sType != null && sType.equalsIgnoreCase("jasper")) {
      reportBuilder = new JasperReportsBuilder();
    }
    else
    if(sType != null && sType.equalsIgnoreCase("test")) {
      reportBuilder = new TestReportBuilder();
    }
    else {
      // Default Report Builder
      reportBuilder = new JasperReportsBuilder();
    }
    
    return reportBuilder;
  }
}
