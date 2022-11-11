# WReport - Wrap Report generation

A small library to wrap report generation engines.

## Example

```java
List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
// Populate data...

ReportInfo ri = ReportFactory.getReportInfo("Test report", "test.jasper");
ri.addArea("Detail", data);

IReportBuilder rb = ReportFactory.getReportBuilder("jasper");
rb.setReportInfo(ri);

rb.generate(new FileOutputStream("test.pdf"));
```

## Servlet Example
```java
public 
class WebReport extends HttpServlet
{
  public 
  void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException 
  {
    try {
      List<Map<String,Object>> listData = DAOTestData.getAll();
      
      ReportInfo reportInfo = ReportFactory.getReportInfo("Comuni", "comuni.jasper");
      
      reportInfo.addArea("Detail", listData);
      
      response.setContentType("application/pdf");
      
      OutputStream out = response.getOutputStream();
      
      IReportBuilder reportBuilder = ReportFactory.getDefaultReportBuilder();
      
      reportBuilder.setReportInfo(reportInfo);
      
      reportBuilder.generate(out);
    } 
    catch (Exception ex) {
      throw new ServletException(ex);
    }
  }
}
```

## Build

- `git clone https://github.com/giosil/wreport.git`
- `mvn clean install`
- `mvn dependency:copy-dependencies`

## Contributors

* [Giorgio Silvestris](https://github.com/giosil)
