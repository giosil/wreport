package org.dew.report;

import java.util.*;
import java.io.Serializable;
import java.sql.Connection;

/**
 * Classe per la definizione di un report.
 */
public
class ReportInfo implements Serializable
{
  private static final long serialVersionUID = -5965556519990003409L;
  
  protected String sTitle;
  protected String sTemplate;
  protected String sType;
  protected List<String> listAree = new ArrayList<String>();
  protected Map<String,Map<String,Object>> mapAreaInfo = new HashMap<String,Map<String,Object>>();
  
  protected Map<String,Object> mapHeaderValues;
  protected Map<String,Object> mapPageHeaderValues;
  protected Map<String,Object> mapFooterValues;
  protected Map<String,Object> mapPageFooterValues;
  protected Map<String,Object> mapParameters;
  protected String sBackgroundText;
  
  protected final static String sAREA_VALUES    = "values";
  protected final static String sAREA_LINKEDTO  = "linkedTo";
  protected final static String sAREA_SUBREPORT = "subreport";
  
  protected double dMarginHorizontal = -1;
  protected double dMarginVertical   = -1;
  protected int iOrientation         = -1;
  protected Boolean oPrintDialog     = null;
  protected Boolean oPageDialog      = null;
  protected String sDateFormat       = null;
  
  protected Connection conn;
  
  public static final int iORIENTATION_UNDEFINED  = -1;
  public static final int iORIENTATION_VERTICAL   = 0;
  public static final int iORIENTATION_HORIZONTAL = 1;
  
  /**
   * Costruttore
   */
  public
  ReportInfo()
  {
  }
  
  /**
   * Costruttore
   *
   * @param sTitle String
   */
  public
  ReportInfo(String sTitle)
  {
    this.sTitle = sTitle;
  }
  
  /**
   * Costruttore
   *
   * @param sTitle String
   * @param sTemplate String
   */
  public
  ReportInfo(String sTitle, String sTemplate)
  {
    this.sTitle = sTitle;
    this.sTemplate = sTemplate;
  }
  
  /**
   * Restituisce il titolo del report.
   * @return String
   */
  public
  String getTitle()
  {
    return sTitle;
  }
  
  /**
   * Imposta il titolo del report.
   * @param sTitle String
   */
  public
  void setTitle(String sTitle)
  {
    this.sTitle = sTitle;
  }
  
  /**
   * Restituisce il modello del report.
   * @return String
   */
  public
  String getTemplate()
  {
    return sTemplate;
  }
  
  /**
   * Imposta il modello del report.
   * @param sTemplate String
   */
  public
  void setTemplate(String sTemplate)
  {
    this.sTemplate = sTemplate;
  }
  
  /**
   * Restituisce il tipo del report.
   * @return String
   */
  public
  String getType()
  {
    return sType;
  }
  
  /**
   * Imposta il tipo del report (es pdf, html, rtf, ecc.).
   * @param sType Tipo report
   */
  public
  void setType(String sType)
  {
    this.sType = sType;
  }
  
  /**
   * Ottiene il margine destro/sinistro della pagina.
   *
   * @return double
   */
  public
  double getMarginHorizontal()
  {
    return dMarginHorizontal;
  }
  
  /**
   * Imposta il margine destro/sinistro della pagina.
   *
   * @param dMarginHorizontal Margine orizzontale
   */
  public
  void setMarginHorizontal(double dMarginHorizontal)
  {
    this.dMarginHorizontal = dMarginHorizontal;
  }
  
  /**
   * Ottiene il margine superiore/inferiore della pagina.
   *
   * @return double
   */
  public
  double getMarginVertical()
  {
    return dMarginVertical;
  }
  
  /**
   * Imposta il margine superiore/inferiore della pagina.
   *
   * @param dMarginVertical Margine verticale
   */
  public
  void setMarginVertical(double dMarginVertical)
  {
    this.dMarginVertical = dMarginVertical;
  }
  
  /**
   * Imposta l'orientazione della stampa.
   *
   * @param Orientation int
   */
  public
  void setOrientation(int Orientation)
  {
    this.iOrientation = Orientation;
  }
  
  /**
   * Restituisce l'orientazione della stampa.
   *
   * @return int
   */
  public
  int getOrientation()
  {
    return iOrientation;
  }
  
  /**
   * Imposta il flag per la visualizzazione del dialogo di stampa.
   *
   * @param boPrintDialog boolean
   */
  public
  void setPrintDialog(boolean boPrintDialog)
  {
    this.oPrintDialog = Boolean.valueOf(boPrintDialog);
  }
  
  /**
   * Restituisce il flag per la visualizzazione del dialogo di stampa.
   *
   * @return Boolean
   */
  public
  Boolean getPrintDialog()
  {
    return oPrintDialog;
  }
  
  /**
   * Imposta il flag per la visualizzazione del dialogo di impostazione pagina.
   *
   * @param boPageDialog boolean
   */
  public
  void setPageDialog(boolean boPageDialog)
  {
    this.oPageDialog = Boolean.valueOf(boPageDialog);
  }
  
  /**
   * Restituisce il flag per la visualizzazione del dialogo di impostazione pagina.
   *
   * @return Boolean
   */
  public
  Boolean getPageDialog()
  {
    return oPageDialog;
  }
  
  /**
   * Imposta il formato delle date.
   *
   * @param sDateFormat String
   */
  public
  void setDateFormat(String sDateFormat)
  {
    this.sDateFormat = sDateFormat;
  }
  
  /**
   * Restituisce il formato delle date.
   *
   * @return Formato date.
   */
  public
  String getDateFormat()
  {
    return sDateFormat;
  }
  
  /**
   * Restituisce il testo di sfondo.
   *
   * @return Testo di sfondo.
   */
  public
  String getBackgroundText()
  {
    return sBackgroundText;
  }
  
  /**
   * Imposta il testo di sfondo.
   * @param sBackgroundText String
   */
  public
  void setBackgroundText(String sBackgroundText)
  {
    this.sBackgroundText = sBackgroundText;
  }
  
  /**
   * Imposta la connessione alla base dati.
   *
   * @param conn Connection
   */
  public
  void setConnection(Connection conn)
  {
    this.conn = conn;
  }
  
  /**
   * Ottiene la connessione alla base dati.
   *
   * @return Connection
   */
  public
  Connection getConnection()
  {
    return conn;
  }
  
  /**
   * Cancella tutte le aree.
   */
  public
  void clear()
  {
    listAree.clear();
    mapAreaInfo.clear();
  }
  
  /**
   * Restituisce il numero delle aree inserite.
   *
   * @return numero di aree inserite.
   */
  public
  int count()
  {
    return listAree.size();
  }
  
  /**
   * Imposta l'intestazione del report.
   *
   * @param mapValues Valori
   */
  public
  void setReportHeader(Map<String,Object> mapValues)
  {
    this.mapHeaderValues = mapValues;
  }

  /**
   * Restituisce i dati dell'intestazione del report.
   *
   * @return Mappa dei valori dell'intestazione di report
   */
  public
  Map<String,Object> getReportHeader()
  {
    return mapHeaderValues;
  }
  
  /**
   * Imposta l'intestazione di pagina.
   *
   * @param mapValues Valori
   */
  public
  void setPageHeader(Map<String,Object> mapValues)
  {
    this.mapPageHeaderValues = mapValues;
  }
  
  /**
   * Restituisce i dati dell'intestazione di pagina.
   *
   * @return Map Valori
   */
  public
  Map<String,Object> getPageHeader()
  {
    return mapPageHeaderValues;
  }
  
  /**
   * Imposta il fine report.
   *
   * @param mapValues Valori
   */
  public
  void setReportFooter(Map<String,Object> mapValues)
  {
    this.mapFooterValues = mapValues;
  }
  
  /**
   * Restituisce i dati del fine report.
   *
   * @return Map
   */
  public
  Map<String,Object> getReportFooter()
  {
    return mapFooterValues;
  }
  
  /**
   * Imposta il fine pagina.
   *
   * @param mapValues Valori
   */
  public
  void setPageFooter(Map<String,Object> mapValues)
  {
    this.mapPageFooterValues = mapValues;
  }
  
  /**
   * Restituisce i dati del fine pagina.
   *
   * @return Map
   */
  public
  Map<String,Object> getPageFooter()
  {
    return mapPageFooterValues;
  }
  
  /**
   * Imposta gli eventuali parametri del report.
   *
   * @param mapParameters Parametri
   */
  public
  void setParameters(Map<String,Object> mapParameters)
  {
    if(this.mapParameters != null && !this.mapParameters.isEmpty()) {
      this.mapParameters.putAll(mapParameters);
    }
    else {
      this.mapParameters = mapParameters;
    }
  }
  
  /**
   * Restituisce gli eventuali parametri del report.
   *
   * @return Map
   */
  public
  Map<String,Object> getParameters()
  {
    return mapParameters;
  }
  
  /**
   * Aggiunge un'area dati indipendente dalle altre.
   *
   * @param sAreaName String
   * @param oValues Object
   */
  public
  void addArea(String sAreaName, Object oValues)
  {
    listAree.add(sAreaName);
    
    Map<String,Object> mapInfo = new HashMap<String,Object>();
    mapInfo.put(sAREA_VALUES,   oValues);
    mapInfo.put(sAREA_LINKEDTO, null);
    
    mapAreaInfo.put(sAreaName, mapInfo);
  }
  
  /**
   * Aggiunge un'area dati specificando l'area a cui � legata.
   * Nei report Master-Detail � importante specificare le relazioni.
   *
   * @param sAreaName String
   * @param oValues Object
   * @param sLinkedArea Nome dell'area collegata
   */
  public
  void addArea(String sAreaName, Object oValues, String sLinkedArea)
  {
    listAree.add(sAreaName);
    
    Map<String,Object> mapInfo = new HashMap<String,Object>();
    mapInfo.put(sAREA_VALUES,   oValues);
    mapInfo.put(sAREA_LINKEDTO, sLinkedArea);
    
    mapAreaInfo.put(sAreaName, mapInfo);
  }
  
  /**
   * Rimuove un'area dati.
   * @param sAreaName String
   */
  public
  void removeArea(String sAreaName)
  {
    listAree.remove(sAreaName);
    mapAreaInfo.remove(sAreaName);
  }
  
  /**
   * Restituisce il nome dell'area.
   *
   * @param iIndex int
   * @return String
   */
  public
  String getAreaName(int iIndex)
  {
    if(listAree.size() <= iIndex) return null;
    return (String) listAree.get(iIndex);
  }
  
  /**
   * Restituisce i dati contenuti nell'area individuata da iIndex.
   *
   * @param iIndex int
   * @return Object values
   */
  public
  Object getAreaValues(int iIndex)
  {
    if(listAree.size() <= iIndex) return null;
    String sAreaName = (String) listAree.get(iIndex);
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return null;
    return mapInfo.get(sAREA_VALUES);
  }
  
  /**
   * Restituisce i dati contenuti nell'area individuata dal nome.
   *
   * @param sAreaName String
   * @return Object values
   */
  public
  Object getAreaValues(String sAreaName)
  {
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return null;
    return mapInfo.get(sAREA_VALUES);
  }
  
  /**
   * Restituisce l'area collegata.
   *
   * @param iIndex int
   * @return String
   */
  public
  String getLinkedArea(int iIndex)
  {
    if(listAree.size() <= iIndex) return null;
    String sAreaName = (String) listAree.get(iIndex);
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return null;
    return (String) mapInfo.get(sAREA_LINKEDTO);
  }
  
  /**
   * Restituisce l'area collegata.
   *
   * @param sAreaName String
   * @return String
   */
  public
  String getLinkedArea(String sAreaName)
  {
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return null;
    return (String) mapInfo.get(sAREA_LINKEDTO);
  }
  
  /**
   * Verifica che l'area sia colleagata.
   *
   * @param iIndex int
   * @return boolean
   */
  public
  boolean isLinked(int iIndex)
  {
    if(listAree.size() <= iIndex) return false;
    String sAreaName = (String) listAree.get(iIndex);
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return false;
    String sLinkedArea = (String) mapInfo.get(sAREA_LINKEDTO);
    return sLinkedArea != null;
  }
  
  /**
   * Verifica che l'area sia colleagata.
   *
   * @param sAreaName String
   * @return boolean
   */
  public
  boolean isLinked(String sAreaName)
  {
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return false;
    String sLinkedArea = (String) mapInfo.get(sAREA_LINKEDTO);
    return sLinkedArea != null;
  }
  
  /**
   * Aggiunge un sottoreport.
   *
   * @param sAreaName Nome dell'area
   * @param sSubreport Riferimento al sottoreport
   */
  public
  void addSubreport(String sAreaName, String sSubreport)
  {
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo != null) mapInfo.put(sAREA_SUBREPORT, sSubreport);
  }
  
  /**
   * Restituisce il riferimento al sottoreport legato all'area.
   *
   * @param iIndex int
   * @return Riferimento al sottoreport
   */
  public
  String getAreaSubreport(int iIndex)
  {
    if(listAree.size() <= iIndex) return null;
    String sAreaName = (String) listAree.get(iIndex);
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return null;
    return (String) mapInfo.get(sAREA_SUBREPORT);
  }
  
  /**
   * Restituisce il riferimento al sottoreport legato all'area.
   *
   * @param sAreaName String
   * @return Riferimento al sottoreport 
   */
  public
  String getAreaSubreport(String sAreaName)
  {
    Map<String,Object> mapInfo = mapAreaInfo.get(sAreaName);
    if(mapInfo == null) return null;
    return (String) mapInfo.get(sAREA_SUBREPORT);
  }
  
  @Override
  public boolean equals(Object object) {
    if(object instanceof ReportInfo) {
      String sObjTitle    = ((ReportInfo) object).getTitle();
      String sObjTemplate = ((ReportInfo) object).getTemplate();
      String sObjType     = ((ReportInfo) object).getType();
      return (sTitle + ":" + sTemplate + ":" + sType).equals(sObjTitle + ":" + sObjTemplate + ":" + sObjType);
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return (sTitle + ":" + sTemplate + ":" + sType).hashCode();
  }
  
  @Override
  public
  String toString()
  {
    return sTitle + ":" + sTemplate + ":" + sType;
  }
}
