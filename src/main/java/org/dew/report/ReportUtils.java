package org.dew.report;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.itextpdf.tool.xml.XMLWorkerHelper;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

public 
class ReportUtils 
{
  public static 
  void mergePDF(List<ByteArrayOutputStream> listOfOutputStream, OutputStream outputStream)
    throws DocumentException, IOException 
  {
    Document document = null;
    try {
      document = new Document(PageSize.A4);
      
      PdfWriter writer = PdfWriter.getInstance(document, outputStream);
      document.open();
      
      PdfContentByte pdfContentByte = writer.getDirectContent();
      for (ByteArrayOutputStream singleOut : listOfOutputStream) {
        if(singleOut == null) continue;
        
        byte[] pdf = singleOut.toByteArray();
        if(pdf == null || pdf.length < 10) continue;
        
        PdfReader reader = new PdfReader(pdf);
        
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
          document.newPage();
          // import the page from source pdf
          PdfImportedPage page = writer.getImportedPage(reader, i);
          // add the page to the destination pdf
          pdfContentByte.addTemplate(page, 0, 0);
        }
      }
    }
    finally {
      document.close();
      outputStream.flush();
    }
  }
  
  public static 
  byte[] textToPdf(String text)
    throws Exception
  {
    if(text == null) text = "";
    
    Paragraph paragraph = new Paragraph();
    paragraph.add(new Phrase(text));
    
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    
    Document pdfDocument = new Document();
    try {
      pdfDocument.setMargins(4, 4, 4, 4);
      
      PdfWriter.getInstance(pdfDocument, result);
      pdfDocument.open();
      pdfDocument.add(paragraph);
    }
    finally {
      if(pdfDocument != null) try { pdfDocument.close(); } catch(Exception ex) {}
    }
    
    return result.toByteArray();
  }
  
  // dependencies: com.itextpdf.itextpdf, com.itextpdf.xmlworker 
  public static
  byte[] htmlToPdf(String html)
    throws Exception
  {
    if(html == null) html = "";
    if(html.indexOf('<') < 0 || html.indexOf('>') < 0 || html.indexOf('/') < 0) {
      html = "<html><body>" + html + "</body></html>";
    }
    
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    
    com.itextpdf.text.Document pdfDocument = new com.itextpdf.text.Document();
    pdfDocument.setMargins(4, 4, 4, 4);
    
    com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfWriter.getInstance(pdfDocument, result);
    pdfDocument.open();
    
    XMLWorkerHelper.getInstance().parseXHtml(writer, pdfDocument, new ByteArrayInputStream(html.getBytes()));
    pdfDocument.close();
    
    return result.toByteArray();
  }
  
  // dependencies: com.itextpdf.itextpdf
  public static
  byte[] imageToPdf(byte[] content)
    throws Exception
  {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    
    com.itextpdf.text.Document      document  = null;
    com.itextpdf.text.pdf.PdfWriter pdfWriter = null;
    try {
      document = new com.itextpdf.text.Document();
      document.setMargins(25, 25, 25, 25);
      
      pdfWriter = com.itextpdf.text.pdf.PdfWriter.getInstance(document, result);
      pdfWriter.open();
      
      document.open();
      
      com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(content);
      image.scaleToFit(PageSize.A4.getWidth() * 9 / 10, PageSize.A4.getHeight() * 9 / 10);
      
      document.add(image);
    }
    finally {
      if(document  != null) try{ document.close();  } catch(Exception ex) {}
      if(pdfWriter != null) try{ pdfWriter.close(); } catch(Exception ex) {}
    }
    
    return result.toByteArray();
  }
  
  // dependencies: org.apache.pdfbox.pdfbox
  public static
  byte[] pdfToImage(byte[] content, String contentType)
    throws Exception
  {
    ByteArrayOutputStream result = new ByteArrayOutputStream();
    
    BufferedImage bufferedImage = null;
    PDDocument pdDocument = null;
    try {
      pdDocument = PDDocument.load(new ByteArrayInputStream(content));
      
      PDPageTree pdPageTree = pdDocument.getDocumentCatalog().getPages();
      
      int pages = pdPageTree != null ? pdPageTree.getCount() : 0;
      
      PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
      
      if(pages == 1) {
        
        bufferedImage = pdfRenderer.renderImageWithDPI(0, 300);
      
      }
      else if(pages > 1) {
        
        for(int i = 0; i < pages; i++) {
          
          BufferedImage bufferedImagePage = pdfRenderer.renderImageWithDPI(i, 300);
          
          if(bufferedImage == null) {
            bufferedImage = new BufferedImage(bufferedImagePage.getWidth(), bufferedImagePage.getHeight() * pages, BufferedImage.TYPE_INT_RGB);
          }
          
          Graphics graphics = bufferedImage.getGraphics();
          graphics.drawImage(bufferedImagePage, 0, i * bufferedImagePage.getHeight(), null);
        }
        
      }
      
      if(bufferedImage != null) {
        
        if(contentType != null && contentType.endsWith("png")) {
          ImageIO.write(bufferedImage, "png", result);
        }
        else if(contentType != null && contentType.endsWith("bmp")) {
          ImageIO.write(bufferedImage, "bmp", result);
        }
        else {
          ImageIO.write(bufferedImage, "jpg", result);
        }
        
      }
    }
    finally {
      if(pdDocument != null) try{ pdDocument.close(); } catch(Exception ex) {}
    }
    
    return result.toByteArray();
  }
  
  public static
  String detectContentType(byte[] content)
  {
    // <! or <H = text/html
    // <A or <M = application/hl7-v2+xml
    // <C       = application/hl7-v3+xml
    // <? or <* = text/xml
    // #        = text/plain
    // %PDF     = application/pdf
    // {\       = application/rtf
    // { or [   = application/json
    // MSH      = application/hl7-v2
    // PK       = application/zip
    // BM       = image/bmp
    // GIF      = image/gif
    // ID3      = audio/mpeg3
    // II       = image/tiff
    // FF=255   = image/jpeg
    // 89=137   = image/png
    // D0=208   = application/msword
    // 0        = application/pkcs7-mime
    // "        = text/csv
    // \0       = video/mpeg
    
    if(content == null || content.length <= 55) {
      return "text/plain";
    }
    for(int i = 0; i < content.length; i++) {
      byte b = content[i];
      if(b ==    0) return "video/mpeg";
      if(b ==   -1) return "image/jpeg"; // FF = 255 = -1
      if(b == -119) return "image/png";  // 89 = 137 = -119
      if(b ==  -48) return "application/msword"; // D0 = 208 = -48
      if(b ==  -84) return "application/pdf"; // PDF1.4
      if(b > 33) {
        if(b == 60) {
          int next = i + 1;
          if(next < content.length) {
            b = content[next];
            if(b == '!' || b == 'h' || b == 'H') {
              return "text/html";
            }
            else if(b == 'A' || b == 'M') {
              return "application/hl7-v2+xml";
            }
            else if(b == 'C') {
              return "application/hl7-v3+xml";
            }
            else {
              int indexOf = byteArrayIndexOf(content, "ClinicalDocument".getBytes());
              if(indexOf >= 0) return "application/hl7-v3+xml";
              
              indexOf = byteArrayIndexOf(content, "XDW.WorkflowDocument".getBytes());
              if(indexOf >= 0) return "application/xdw";
              
              return "text/xml";
            }
          }
          else {
            return "text/plain";
          }
        }
        if(b == 123) {
          int next = i + 1;
          if(next < content.length) {
            b = content[next];
            if(b == '\\') {
              return "application/rtf";
            }
            else {
              return "application/json";
            }
          }
        }
        if(b ==  34) return "text/csv";           // "
        if(b ==  35) return "text/plain";         // #
        if(b ==  37) return "application/pdf";    // %
        if(b ==  66) return "image/bmp";          // B
        if(b ==  71) return "image/gif";          // G
        if(b ==  73) {                            // I
          int next = i + 1;
          if(next < content.length) {
            b = content[next];
            if(b == 'D') {
              return "audio/mpeg3";
            }
            else {
              return "image/tiff";
            }
          }
          else {
            return "image/tiff";
          }
        }
        if(b ==  77) return "application/hl7-v2"; // M
        if(b ==  80) return "application/zip";    // P
        if(b ==  91) return "application/json";   // [
        if(b ==  48) return "application/pkcs7-mime"; // 0
        break;
      }
    }
    return "text/plain";
  }
  
  public static 
  int byteArrayIndexOf(byte[] source, byte[] target) 
  {
    return byteArrayIndexOf(source, 0, source.length, target, 0, target.length, 0);
  }
  
  public static 
  int byteArrayIndexOf(byte[] source, byte[] target, int fromIndex) 
  {
    return byteArrayIndexOf(source, 0, source.length, target, 0, target.length, fromIndex);
  }
  
  // Preso dalla classe String e riadattato (char[] -> byte[])
  public static 
  int byteArrayIndexOf(byte[] source, int sourceOffset, int sourceCount, byte[] target, int targetOffset, int targetCount, int fromIndex)
  {
    if (fromIndex >= sourceCount) {
      return (targetCount == 0 ? sourceCount : -1);
    }
    if (fromIndex < 0) {
      fromIndex = 0;
    }
    if (targetCount == 0) {
      return fromIndex;
    }
    
    byte first = target[targetOffset];
    int max = sourceOffset + (sourceCount - targetCount);
    
    for (int i = sourceOffset + fromIndex; i <= max; i++) {
      /* Look for first character. */
      if (source[i] != first) {
        while (++i <= max && source[i] != first);
      }
      
      /* Found first character, now look at the rest of v2 */
      if (i <= max) {
        int j = i + 1;
        int end = j + targetCount - 1;
        for (int k = targetOffset + 1; j < end && source[j]
            == target[k]; j++, k++);
        
        if (j == end) {
          /* Found whole string. */
          return i - sourceOffset;
        }
      }
    }
    return -1;
  }
}
