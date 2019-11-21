package org.dew.test;

import java.io.File;
import java.io.FileOutputStream;
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
    FileOutputStream fileOutputStream = null;
    try {
      String folderPath = System.getProperty("user.home") + File.separator + "Desktop";
      File folder = new File(folderPath);
      if(!folder.exists()) folder.mkdirs();
      
      String sFileOutput = folderPath + File.separator + "test.pdf";
      System.out.println("new FileOutputStream(" + sFileOutput + ")...");
      fileOutputStream = new FileOutputStream(sFileOutput);
      
      List<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
      addRecord(listData, "idComune", "060038", "codFiscale", "D810", "descrizione", "FROSINONE");
      addRecord(listData, "idComune", "059011", "codFiscale", "E472", "descrizione", "LATINA");
      addRecord(listData, "idComune", "057059", "codFiscale", "H282", "descrizione", "RIETI");
      addRecord(listData, "idComune", "058091", "codFiscale", "H501", "descrizione", "ROMA");
      addRecord(listData, "idComune", "056059", "codFiscale", "M082", "descrizione", "VITERBO");
      
      System.out.println("ReportFactory.getDefaultReportInfo...");
      ReportInfo reportInfo = ReportFactory.getDefaultReportInfo("Comuni", "comuni.jasper");
      reportInfo.addArea("Detail", listData);
      
      System.out.println("ReportFactory.getDefaultReportBuilder...");
      IReportBuilder reportBuilder = ReportFactory.getDefaultReportBuilder("jasper");
      reportBuilder.setReportInfo(reportInfo);
      
      System.out.println("reportBuilder.generate...");
      reportBuilder.generate(fileOutputStream);
    }
    finally {
      if(fileOutputStream != null) try { fileOutputStream.close(); } catch(Exception ex) {}
    }
  }
  
  public static void addRecord(List<Map<String,Object>> listData, String k0, Object v0, String k1, Object v1, String k2, Object v2) {
    Map<String,Object> mapData = new HashMap<String, Object>();
    if(k0 != null && k0.length() > 0) mapData.put(k0, v0);
    if(k1 != null && k1.length() > 0) mapData.put(k1, v1);
    if(k2 != null && k2.length() > 0) mapData.put(k2, v2);
    listData.add(mapData);
  }
}
