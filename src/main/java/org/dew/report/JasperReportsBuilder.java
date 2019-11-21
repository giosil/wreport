package org.dew.report;

import java.io.*;
import java.net.*;

import java.sql.Connection;

import java.util.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.*;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

/**
 * Classe per la costruzione di un report tramite JasperReports.
 */
public 
class JasperReportsBuilder implements IReportBuilder 
{
  protected ReportInfo reportInfo;
  
  /**
   * Costruttore.
   */
  public JasperReportsBuilder() {
  }
  
  /**
   * Costruttore
   *
   * @param reportInfo ReportInfo
   */
  public JasperReportsBuilder(ReportInfo reportInfo) {
    this.reportInfo = reportInfo;
  }
  
  /**
   * Imposta le informazioni del report da stampare.
   *
   * @param reportInfo ReportInfo
   */
  public void setReportInfo(ReportInfo reportInfo) {
    this.reportInfo = reportInfo;
  }
  
  /**
   * Restituisce l'oggetto ReportInfo contenente le informazioni del report.
   *
   * @return ReportInfo
   */
  public ReportInfo getReportInfo() {
    return reportInfo;
  }
  
  /**
   * Genera il report.
   *
   * @param outputStream OutputStream
   * @throws Exception
   */
  public void generate(OutputStream outputStream) throws Exception {
    if (reportInfo == null) {
      throw new Exception("ReportInfo undefined.");
    }
    
    URL urlTemplate = getURLTemplate();
    
    if (urlTemplate == null) {
      throw new Exception("Report " + reportInfo.getTemplate() + " not found.");
    }
    
    Map<String,Object> mapParameters = getParameters();
    mapParameters.put(JRParameter.REPORT_LOCALE, Locale.ITALY);
    
    JasperPrint jasperPrint = null;
    Connection conn = reportInfo.getConnection();
    if (conn == null) {
      JRDataSource jrDataSource = getDataSource();
      jasperPrint = JasperFillManager.fillReport(urlTemplate.openStream(), mapParameters, jrDataSource);
    } else {
      jasperPrint = JasperFillManager.fillReport(urlTemplate.openStream(), mapParameters, conn);
    }
    
    String sType = reportInfo.getType();
    if (sType == null || sType.equalsIgnoreCase("pdf")) {
      JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    } else if (sType.equalsIgnoreCase("xls")) {
      JRXlsExporter exporter = new JRXlsExporter();
      
      exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
      exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
      
      SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
      configuration.setOnePagePerSheet(Boolean.FALSE);
      exporter.setConfiguration(configuration);
      
      exporter.exportReport();
    } else if (sType.equalsIgnoreCase("xml")) {
      JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);
    } else {
      throw new Exception("Type report " + sType + " unsupported.");
    }
  }

  /**
   * Restituisce l'URL del template.
   *
   * @return URL Template
   */
  protected URL getURLTemplate() {
    return Thread.currentThread().getContextClassLoader().getResource("reports/" + reportInfo.getTemplate());
  }
  
  /**
   * Restituisce l'URL del sottoreport.
   *
   * @param sSubreport riferimento al sottoreport
   * @return URL Template
   */
  protected URL getURLSubreport(String sSubreport) {
    return Thread.currentThread().getContextClassLoader().getResource("reports/" + sSubreport);
  }
  
  /**
   * Ottiene i parametri del report dall'oggetto ReportInfo. E' stata seguita la
   * seguente mappatura degli attributi di ReportInfo:
   *
   * BT = ReportInfo.BackgroundText title = ReportInfo.Title
   *
   * RH.* = ReportInfo.ReportHeader PH.* = ReportInfo.PageHeader PF.* =
   * ReportInfo.PageFooter RF.* = ReportInfo.ReportFooter
   *
   * @return Map
   */
  protected Map<String,Object> getParameters() {
    Map<String,Object> mapResult = new HashMap<String,Object>();
    
    String sBT = reportInfo.getBackgroundText();
    mapResult.put("BT", sBT);
    String sTitle = reportInfo.getTitle();
    mapResult.put("title", sTitle);
    
    Map<String,Object> mapRH = reportInfo.getReportHeader();
    if (mapRH != null) {
      Iterator<Map.Entry<String, Object>> oItEntry = mapRH.entrySet().iterator();
      while (oItEntry.hasNext()) {
        Map.Entry<String, Object> entry = oItEntry.next();
        mapResult.put("RH." + entry.getKey(), entry.getValue());
      }
    }
    Map<String,Object> mapPH = reportInfo.getPageHeader();
    if (mapPH != null) {
      Iterator<Map.Entry<String, Object>> oItEntry = mapPH.entrySet().iterator();
      while (oItEntry.hasNext()) {
        Map.Entry<String, Object> entry = oItEntry.next();
        mapResult.put("PH." + entry.getKey(), entry.getValue());
      }
    }
    Map<String, Object> mapPF = reportInfo.getPageFooter();
    if (mapPF != null) {
      Iterator<Map.Entry<String, Object>> oItEntry = mapPF.entrySet().iterator();
      while (oItEntry.hasNext()) {
        Map.Entry<String, Object> entry = oItEntry.next();
        mapResult.put("PF." + entry.getKey(), entry.getValue());
      }
    }
    Map<String, Object> mapRF = reportInfo.getReportFooter();
    if (mapRF != null) {
      Iterator<Map.Entry<String, Object>> oItEntry = mapRF.entrySet().iterator();
      while (oItEntry.hasNext()) {
        Map.Entry<String, Object> entry = oItEntry.next();
        mapResult.put("RF." + entry.getKey(), entry.getValue());
      }
    }
    
    Map<String, Object> mapParameters = reportInfo.getParameters();
    if (mapParameters != null) {
      mapResult.putAll(mapParameters);
    }
    
    int iCountAree = reportInfo.count();
    for (int i = 0; i < iCountAree; i++) {
      String sSubreport = reportInfo.getAreaSubreport(i);
      if (sSubreport != null && sSubreport.length() > 0) {
        String sAreaName = reportInfo.getAreaName(i);
        Object oValues = reportInfo.getAreaValues(i);
        if (oValues instanceof Collection) {
          JasperReportsDataSource jrDataSource = new JasperReportsDataSource();
          jrDataSource.setData((Collection<?>) oValues);
          mapResult.put("DS_" + sAreaName, jrDataSource);
          mapResult.put("URL_" + sAreaName, getURLSubreport(sSubreport));
        }
      }
    }
    
    return mapResult;
  }
  
  /**
   * Costruisce l'oggetto JRDataSource;
   *
   * @return JRDataSource
   */
  protected JRDataSource getDataSource() {
    JasperReportsDataSource jrDataSource = new JasperReportsDataSource();
    
    int iCountAree = reportInfo.count();
    if (iCountAree > 0) {
      Object oValues = reportInfo.getAreaValues(iCountAree - 1);
      if (oValues instanceof Collection) {
        jrDataSource.setData((Collection<?>) oValues);
      }
    }
    
    return jrDataSource;
  }
}
