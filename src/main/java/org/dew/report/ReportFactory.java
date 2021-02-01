package org.dew.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

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
  
  public static 
  void mergePDF(List<ByteArrayOutputStream> listOfOutputStream, OutputStream outputStream)
    throws DocumentException, IOException 
  {
    Document document = null;
    try {
      document = new Document(PageSize.A4);
      
      PdfWriter writer = PdfWriter.getInstance(document, outputStream);
      document.open();
      
      PdfContentByte pdfContentByte = writer.getDirectContent();
      for (ByteArrayOutputStream singleOut : listOfOutputStream) {
        
        PdfReader reader = new PdfReader(singleOut.toByteArray());
        
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
          document.newPage();
          // import the page from source pdf
          PdfImportedPage page = writer.getImportedPage(reader, i);
          // add the page to the destination pdf
          pdfContentByte.addTemplate(page, 0, 0);
        }
      }
    }
    finally {
      document.close();
      outputStream.flush();
    }
  }
}
