[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-black.svg)](https://sonarcloud.io/summary/new_code?id=giosil_wreport)
<br/>
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=giosil_wreport&metric=bugs)](https://sonarcloud.io/summary/new_code?id=giosil_wreport)

---

# WReport - Wrap Report generation

A small library to wrap report generation engines.

Using `com.lowagie.itext 2.1.7` you may encounter the following warning: `Unpatched iText found, cannot use glyph rendering`.

You can use the modified version of library by downloading it from:

https://jasperreports.sourceforge.net/maven2/com/lowagie/itext/

From 6.21 `com.itextpdf.itextpdf 5.5.6` is required.

## Example

```java
List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
// Populate data...

ReportInfo ri = ReportFactory.getReportInfo("Test report", "test.jasper");
ri.addArea("Detail", data);

IReportBuilder rb = ReportFactory.getReportBuilder("jasper");
rb.setReportInfo(ri);

rb.generate(new FileOutputStream("test.pdf"));
```

## Servlet Example
```java
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public 
class ReportServlet extends HttpServlet 
{
  private static final long serialVersionUID = 1L;

  public 
  void doGet(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException 
  {
    doPost(request, response);
  }
  
  public 
  void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws ServletException, IOException 
  {
    // Get report template
    String pathInfo    = request.getPathInfo();
    if(pathInfo == null || pathInfo.length() == 0) {
      sendMessage(response, "Report non specificato.");
      return;
    }
    String type = "pdf";
    if(pathInfo.startsWith("/")) {
      pathInfo = pathInfo.substring(1);
    }
    int lastDot = pathInfo.lastIndexOf('.');
    if(lastDot > 0) {
      type = pathInfo.substring(lastDot + 1).toLowerCase();
      pathInfo = pathInfo.substring(0, lastDot);
    }
    
    // Check report template
    String template = pathInfo + ".jasper";
    if(!ReportFactory.exists(template)) {
      sendMessage(response, "Report " + template + " non disponibile.");
      return;
    }
    
    // Get report data from body
    List<Map<String, Object>> listData = readData(request);
    
    // Get report data from parameters
    if(listData.size() == 0) {
      Map<String, Object> mapData = null;
      Enumeration<String> enumeration = request.getParameterNames();
      while(enumeration.hasMoreElements()) {
        String paramName = (String) enumeration.nextElement();
        if(mapData == null) mapData = new HashMap<String, Object>();
        mapData.put(paramName, request.getParameter(paramName));
      }
      if(mapData != null) listData.add(mapData);
    }
    
    // Generate report
    try {
      ReportInfo reportInfo = ReportFactory.getReportInfo("Report", template);
      reportInfo.setType(type);
      reportInfo.addArea("Detail", listData);
      
      IReportBuilder reportBuilder = ReportFactory.getDefaultReportBuilder();
      reportBuilder.setReportInfo(reportInfo);
      
      response.setContentType(ReportFactory.getContentType(type));
      reportBuilder.generate(response.getOutputStream());
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }
  
  public
  List<Map<String, Object>> readData(HttpServletRequest request)
  {
    String result = "";
    InputStream is = null;
    try {
      is = request.getInputStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      byte[] buff = new byte[1024];
      int n;
      while((n = is.read(buff)) > 0) {
        baos.write(buff, 0, n);
      }
      result = baos.toString();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    finally {
      if(is != null) try{ is.close(); } catch(Exception ex) {}
    }
    try {
      result = result.trim();
      if(result.length() < 2) {
        return new ArrayList<Map<String,Object>>();
      }
      ObjectMapper objectMapper = new ObjectMapper();
      if(result.startsWith("{") && result.endsWith("}")) {
        List<Map<String, Object>> listResult = new ArrayList<Map<String,Object>>();
        Map<String, Object> mapData = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {});
        if(mapData != null) {
          listResult.add(mapData);
        }
        return listResult;
      }
      if(!result.startsWith("[") || !result.endsWith("]")) {
        return new ArrayList<Map<String,Object>>();
      }
      return objectMapper.readValue(result, new TypeReference<List<Map<String, Object>>>() {});
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
    return new ArrayList<Map<String,Object>>();
  }
  
  protected 
  void sendMessage(HttpServletResponse response, String message) 
    throws ServletException, IOException 
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head>");
    out.println("<title>Report - Messaggio</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>" + message + "</h1>");
    out.println("</body>");
    out.println("</html>");
  }
}
```

## Client Example

```typescript
namespace APP {

  export function launchReport(report: string, data?: any) {
    if (!report) {
      showWarning('Report non specificato');
      return;
    }
    window['BSIT'].showLoader();
    if (!data) {
      data = [];
    }
    else if (!Array.isArray(data)) {
      if(typeof data !== 'object') {
        data = [{"value" : data}];
      }
      else {
        data = [data];
      }
    }
    fetch("/report/" + report + ".pdf", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(data)
    })
    .then(response => {
      window['BSIT'].hideLoader();
      if (!response.ok) {
        throw new Error('Errore nella risposta del server');
      }
      return response.blob();
    })
    .then(blob => {
      if (!blob) {
        throw new Error('Blob non disponibile');
      }
      const u = URL.createObjectURL(blob);
      window.open(u, "_blank");
      setTimeout(() => URL.revokeObjectURL(u), 60000);
    })
    .catch(error => {
      window['BSIT'].hideLoader();
      console.error('Errore in launchReport(' + report + ',...)', error);
      showError('Report ' + report + ' non disponibile.');
    });
  }

}
```

## Build

- `git clone https://github.com/giosil/wreport.git`
- `mvn clean install`
- `mvn dependency:resolve -U`
- `mvn dependency:copy-dependencies`
- `mvn dependency:purge-local-repository`

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
- itext-2.1.7.jar (or patched version: e.g. itext-2.1.7.js5.jar)
- jasperreports 7.x

-6.20.0.jar
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

### jasperreports 7.x

```xml
<dependency>
	<groupId>com.itextpdf</groupId>
	<artifactId>itextpdf</artifactId>
	<version>5.5.6</version>
</dependency>
<dependency>
	<groupId>net.sf.jasperreports</groupId>
	<artifactId>jasperreports</artifactId>
	<version>7.0.3</version>
</dependency>
<dependency>
	<groupId>net.sf.jasperreports</groupId>
	<artifactId>jasperreports-excel-poi</artifactId>
	<version>7.0.3</version>
</dependency>
<dependency>
	<groupId>net.sf.jasperreports</groupId>
	<artifactId>jasperreports-pdf</artifactId>
	<version>7.0.3</version>
</dependency>
<dependency>
	<groupId>net.sf.jasperreports</groupId>
	<artifactId>jasperreports-fonts</artifactId>
	<version>7.0.3</version>
</dependency>
```

## Contributors

* [Giorgio Silvestris](https://github.com/giosil)
