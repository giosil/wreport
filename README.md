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

## Build

- `git clone https://github.com/giosil/wreport.git`
- `mvn clean install`

## Contributors

* [Giorgio Silvestris](https://github.com/giosil)
