package org.dew.report;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public
class ReportFactory
{
  public static
  ReportInfo getDefaultReportInfo(String sTitle, String sTemplate)
  {
    ReportInfo oReportInfo = new ReportInfo(sTitle, sTemplate);
    URL urlTemplate = Thread.currentThread().getContextClassLoader().getResource("reports/" + oReportInfo.getTemplate());
    if(urlTemplate != null) {
      
      String sImagesPath = urlTemplate.toString();
      int iLastSep = sImagesPath.lastIndexOf('/');
      if(iLastSep > 0) sImagesPath = sImagesPath.substring(0, iLastSep + 1);
      
      Map<String,Object> mapParameters = new HashMap<String,Object>();
      mapParameters.put("IMAGES_PATH", sImagesPath);
      oReportInfo.setParameters(mapParameters);
      
    }
    return oReportInfo;
  }
  
  public static
  IReportBuilder getDefaultReportBuilder(String sType)
  {
    IReportBuilder oReportBuilder = new JasperReportsBuilder();
    return oReportBuilder;
  }
}
