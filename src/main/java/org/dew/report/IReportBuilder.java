package org.dew.report;

import java.io.OutputStream;

/**
 * Interfaccia base per un generatore di report.
 */
public interface IReportBuilder {
  
  /**
   * Imposta le informazioni del report da stampare.
   *
   * @param oTheReportInfo Descrizione del report
   */
  public void setReportInfo(ReportInfo reportInfo);
  
  /**
   * Restituisce l'oggetto ReportInfo contenente le informazioni del report.
   *
   * @return ReportInfo
   */
  public ReportInfo getReportInfo();
  
  /**
   * Genera il report.
   *
   * @param outputStream OutputStream
   * @throws Exception
   */
  public void generate(OutputStream outputStream) throws Exception;
}
