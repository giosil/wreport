[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/summary/new_code?id=giosil_wreport)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=giosil_wreport&metric=bugs)](https://sonarcloud.io/summary/new_code?id=giosil_wreport)

---

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

## Revert and clean 

- `git reset --hard` to discarde any changes not committed to tracked files in the working tree
- `git clean -fxd` to delete untracked file recursively (f) and directories (d) also in .gitignore (x)

## Dependencies

### Strictly necessary

- commons-beanutils-1.9.4.jar
- commons-collections-3.2.2.jar
- commons-collections4-4.2.jar
- commons-digester-2.1.jar
- commons-logging-1.1.1.jar
- jcommon-1.0.23.jar
- itext-2.1.7.jar
- jasperreports-6.20.0.jar
- jasperreports-fonts-6.20.0.jar (to render correctly styled text in pdf)

### Optional

- bcmail-jdk14-1.38.jar
- bcmail-jdk14-138.jar
- bcprov-jdk14-1.38.jar
- bcprov-jdk14-138.jar
- bctsp-jdk14-1.38.jar
- ecj-3.21.0.jar
- jackson-annotations-2.13.3.jar
- jackson-core-2.13.3.jar
- jackson-databind-2.13.3.jar
- jackson-dataformat-xml-2.13.3.jar
- jfreechart-1.0.19.jar
- stax2-api-4.2.1.jar
- woodstox-core-6.2.7.jar

### ReportUtils

- fontbox-2.0.15.jar
- pdfbox-2.0.15.jar
- itextpdf-5.5.5.jar
- xmlworker-5.5.5.jar

## Contributors

* [Giorgio Silvestris](https://github.com/giosil)
