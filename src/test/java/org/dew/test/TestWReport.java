package org.dew.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dew.report.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestWReport extends TestCase {
  
  public TestWReport(String testName) {
    super(testName);
  }
  
  public static Test suite() {
    return new TestSuite(TestWReport.class);
  }
  
  public void testApp() throws Exception {
    
    generateTestPdf();
    
  }
  
  public 
  void generateTestImg() 
    throws Exception 
  {
    String html = "<html><body><h1>Test</h1></body></html>";
    
    byte[] pdf = ReportUtils.htmlToPdf(html);
    
    byte[] img = ReportUtils.pdfToImage(pdf, "image/png");
    
    saveContent(img, getDesktopPath("img.png"));
  }
  
  public 
  void generateTestPdf() 
    throws Exception 
  {
    FileOutputStream fileOutputStream = null;
    try {
      File folder = new File(getDesktop());
      if(!folder.exists()) folder.mkdirs();
      
      String sFileOutput = folder + File.separator + "test.pdf";
      System.out.println("new FileOutputStream(" + sFileOutput + ")...");
      fileOutputStream = new FileOutputStream(sFileOutput);
      
      List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
      addRecord(listData, "idComune", "060038", "codFiscale", "D810", "descrizione", "FROSINONE");
      addRecord(listData, "idComune", "059011", "codFiscale", "E472", "descrizione", "LATINA");
      addRecord(listData, "idComune", "057059", "codFiscale", "H282", "descrizione", "RIETI");
      addRecord(listData, "idComune", "058091", "codFiscale", "H501", "descrizione", "ROMA");
      addRecord(listData, "idComune", "056059", "codFiscale", "M082", "descrizione", "VITERBO");
      
      System.out.println("ReportFactory.getReportInfo...");
      ReportInfo reportInfo = ReportFactory.getReportInfo("Comuni", "comuni.jasper");
      reportInfo.addArea("Detail", listData);
      
      System.out.println("ReportFactory.getReportBuilder...");
      IReportBuilder reportBuilder = ReportFactory.getReportBuilder("jasper");
      reportBuilder.setReportInfo(reportInfo);
      
      System.out.println("reportBuilder.generate...");
      reportBuilder.generate(fileOutputStream);
    }
    finally {
      if(fileOutputStream != null) try { fileOutputStream.close(); } catch(Exception ex) {}
    }
  }
  
  public static 
  void addRecord(List<Map<String,Object>> listData, String k0, Object v0, String k1, Object v1, String k2, Object v2) 
  {
    if(listData == null) return;
    
    Map<String,Object> mapData = new HashMap<String, Object>();
    
    if(k0 != null && k0.length() > 0) mapData.put(k0, v0);
    if(k1 != null && k1.length() > 0) mapData.put(k1, v1);
    if(k2 != null && k2.length() > 0) mapData.put(k2, v2);
    
    if(mapData.isEmpty()) return;
    
    listData.add(mapData);
  }
  
  public static
  String getDesktop()
  {
    String sUserHome = System.getProperty("user.home");
    return sUserHome + File.separator + "Desktop";
  }
  
  public static
  String getDesktopPath(String sFileName)
  {
    String sUserHome = System.getProperty("user.home");
    return sUserHome + File.separator + "Desktop" + File.separator + sFileName;
  }
  
  public static
  byte[] readFile(String fileName)
    throws Exception
  {
    if(fileName == null || fileName.length() == 0) {
      return new byte[0];
    }
    
    int iFileSep = fileName.indexOf('/');
    if(iFileSep < 0) iFileSep = fileName.indexOf('\\');
    InputStream is = null;
    if(iFileSep < 0) {
      URL url = Thread.currentThread().getContextClassLoader().getResource(fileName);
      is = url.openStream();
    }
    else {
      is = new FileInputStream(fileName);
    }
    try {
      int n;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buff = new byte[1024];
      while((n = is.read(buff)) > 0) baos.write(buff, 0, n);
      return baos.toByteArray();
    }
    finally {
      if(is != null) try{ is.close(); } catch(Exception ex) {}
    }
  }
  
  public static
  void saveContent(byte[] content, String sFilePath)
    throws Exception
  {
    if(content == null) return;
    if(content == null || content.length == 0) return;
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(sFilePath);
      fos.write(content);
    }
    finally {
      if(fos != null) try{ fos.close(); } catch(Exception ex) {}
    }
  }
}
