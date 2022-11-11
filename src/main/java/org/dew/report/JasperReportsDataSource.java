package org.dew.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRField;

/**
 * Sorgente dati per il motore di stampa JasperReports.
 */
public class JasperReportsDataSource extends JREmptyDataSource {
  
  protected Collection<?> colData = null;
  protected Iterator<?>  iterator = null;
  protected Object oCurrentRecord = null;
  
  /**
   * Costruttore.
   */
  public JasperReportsDataSource() {
    super();
  }
  
  /**
   * Costruttore.
   * 
   * @param count Limite sul numero di dati
   */
  public JasperReportsDataSource(int count) {
    super(count);
  }
  
  /**
   * Costruttore con i dati.
   *
   * @param colData Collection
   */
  public JasperReportsDataSource(Collection<?> colData) {
    super();
    this.colData = colData;
    if (colData != null) {
      iterator = colData.iterator();
    } else {
      iterator = null;
    }
    oCurrentRecord = null;
  }
  
  /**
   * Imposta i dati del report.
   *
   * @param colData Collection
   */
  public void setData(Collection<?> colData) {
    this.colData = colData;
    if (colData != null) {
      iterator = colData.iterator();
    } else {
      iterator = null;
    }
    oCurrentRecord = null;
  }
  
  /**
   * Ottiene i dati del report.
   *
   * @return Collection
   */
  public Collection<?> getData() {
    return colData;
  }
  
  /**
   * Implementazione di JREmptyDataSource.moveFirst().
   */
  @Override
  public void moveFirst() {
    if (colData != null) {
      iterator = colData.iterator();
    } else {
      iterator = null;
    }
    oCurrentRecord = null;
  }
  
  /**
   * Implementazione di JREmptyDataSource.next().
   *
   * @return boolean
   */
  @Override
  public boolean next() {
    if (iterator == null) return false;
    boolean boHasNext = iterator.hasNext();
    if (boHasNext) {
      oCurrentRecord = iterator.next();
    } else {
      oCurrentRecord = null;
    }
    return boHasNext;
  }
  
  /**
   * Implementazione di JREmptyDataSource.getFieldValue(JRField jrField).
   *
   * @param jrField Campo
   * @return Object Value
   */
  @Override
  public Object getFieldValue(JRField jrField) {
    if (iterator == null) return null;
    if (oCurrentRecord instanceof Map) {
      Object oValue = ((Map<?,?>) oCurrentRecord).get(jrField.getName());
      Class<?> oFieldClass = jrField.getValueClass();
      if (oValue == null) {
        if (oFieldClass != null && oFieldClass.equals(String.class)) {
          return "";
        }
        else if (oFieldClass != null && oFieldClass.equals(net.sf.jasperreports.engine.JREmptyDataSource.class)) {
          return new JasperReportsDataSource();
        }
      } 
      else if (oFieldClass != null && oFieldClass.equals(net.sf.jasperreports.engine.JREmptyDataSource.class)) {
        if (oValue instanceof Collection) {
          JasperReportsDataSource jrDataSource = new JasperReportsDataSource();
          jrDataSource.setData((Collection<?>) oValue);
          return jrDataSource;
        }
        else {
          List<Object> listData = new ArrayList<Object>(1);
          listData.add(oValue);
          JasperReportsDataSource jrDataSource = new JasperReportsDataSource();
          jrDataSource.setData(listData);
          return jrDataSource;
        }
      }
      return oValue;
    }
    return oCurrentRecord;
  }
}
