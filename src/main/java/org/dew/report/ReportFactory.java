package org.dew.report;

import java.net.URL;

import java.util.HashMap;
import java.util.Map;

public
class ReportFactory
{
  public static String REPORTS_FOLDER  = "reports";
  public static String PAR_IMAGES_PATH = "IMAGES_PATH";
  
  public static
  ReportInfo getReportInfo(String title, String template)
  {
    ReportInfo reportInfo = new ReportInfo(title, template);
    
    URL urlTemplate = Thread.currentThread().getContextClassLoader().getResource(REPORTS_FOLDER + "/" + reportInfo.getTemplate());
    if(urlTemplate == null) {
      return reportInfo;
    }
    
    String sImagesPath = urlTemplate.toString();
    int iLastSep = sImagesPath.lastIndexOf('/');
    if(iLastSep > 0) sImagesPath = sImagesPath.substring(0, iLastSep + 1);
    
    Map<String,Object> mapParameters = new HashMap<String,Object>();
    mapParameters.put(PAR_IMAGES_PATH, sImagesPath);
    reportInfo.setParameters(mapParameters);
    
    return reportInfo;
  }
  
  public static
  IReportBuilder getDefaultReportBuilder()
  {
    return getReportBuilder(null);
  }
  
  public static
  IReportBuilder getReportBuilder(String sType)
  {
    IReportBuilder reportBuilder = null;
    
    if(sType != null && sType.equalsIgnoreCase("jasper")) {
      reportBuilder = new JasperReportsBuilder();
    }
    else if(sType != null && sType.equalsIgnoreCase("mock")) {
      reportBuilder = new MockReportBuilder();
    }
    else if(sType != null && sType.equalsIgnoreCase("test")) {
      reportBuilder = new MockReportBuilder();
    }
    else {
      // Default Report Builder
      reportBuilder = new JasperReportsBuilder();
    }
    
    return reportBuilder;
  }
}
