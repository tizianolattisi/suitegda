/*
 * Copyright (C) 2012 AXIA Studio (http://www.axiastudio.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.axiastudio.suite.plugins.ooops;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XUnoUrlResolver;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XNameAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.io.IOException;
import com.sun.star.lang.*;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.text.XBookmarksSupplier;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiziano
 */
public class OoopsHelper {
    
    private final String connectionString;
    private final Boolean hidden;
    private XBridge bridge=null;
    private XComponent activeComponent;

    public OoopsHelper(String connectionString) {
        this(connectionString, true);
    }

    public OoopsHelper(String connectionString, Boolean hidden) {
        this.connectionString = connectionString;
        this.hidden = hidden;
    }
    
    // TO DELETE!
    /*
    public void composeDocument(Template template, Object obj){
        this.loadDocumentComponent(template.getTemplateUrl());
        Map<String, Object> values = template.getRuleSet().evalJson(obj);
        for( String key: values.keySet() ){
            XTextRange anchor = this.getAnchor(key);
            Object value = values.get(key);
            anchor.setString(value.toString());
        }
        // XXX
        this.storeDocumentComponent("file:///tmp/OOo_doc.odt");
        //this.closeDocumentComponent(document);
        //this.close();
    }*/
    
    public void composeDocument(Map<String, Object> values){
        this.composeDocument(values, this.activeComponent);
    }
    
    public void composeDocument(Map<String, Object> values, XComponent component){
        for( String key: values.keySet() ){
            XTextRange anchor = this.getAnchor(key, component);
            if( anchor != null ){
                Object value = values.get(key);
                if( value != null ){
                    anchor.setString(value.toString());
                }
            }
        }
    }
    
    public void writeText(String text){
        writeText(text, this.activeComponent);
    }
    
    public void writeText(String text, XComponent component){
        XTextDocument document = (XTextDocument) UnoRuntime.queryInterface(XTextDocument.class, component);
        document.getText().setString(text);
    }
    
    private XTextRange getAnchor(String anchorName){
        return this.getAnchor(anchorName, this.activeComponent);
    }

    private XTextRange getAnchor(String anchorName, XComponent component){
        XBookmarksSupplier supplier = (XBookmarksSupplier) UnoRuntime.queryInterface(XBookmarksSupplier.class, component);
        XNameAccess bookmarks = supplier.getBookmarks();
        try {
            Object myBookmark = bookmarks.getByName(anchorName);
            XTextContent content = (XTextContent)UnoRuntime.queryInterface(XTextContent.class, myBookmark);
            XTextRange range = content.getAnchor();
            return range;
        } catch (NoSuchElementException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.WARNING, "unable to find "+anchorName+" anchor", ex);
        } catch (WrappedTargetException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.WARNING, "unable to find "+anchorName+" anchor", ex);
        }     
        return null;
    }
    
    private XComponentLoader queryComponentLoader(){
        XComponentLoader loader=null;
        try{
            XComponentContext context = com.sun.star.comp.helper.Bootstrap.createInitialComponentContext(null);
            XMultiComponentFactory factory = context.getServiceManager();
            Object objResolver = factory.createInstanceWithContext("com.sun.star.bridge.UnoUrlResolver", context);
            XUnoUrlResolver resolver = (XUnoUrlResolver) UnoRuntime.queryInterface(XUnoUrlResolver.class, objResolver);
            Object objectInitial = resolver.resolve(this.connectionString);
            factory = (XMultiComponentFactory) UnoRuntime.queryInterface(XMultiComponentFactory.class, objectInitial);
            XPropertySet properties = (XPropertySet) UnoRuntime.queryInterface(XPropertySet.class, factory);
            Object objContext = properties.getPropertyValue("DefaultContext");
            context = (XComponentContext) UnoRuntime.queryInterface(XComponentContext.class, objContext);
            loader = (XComponentLoader) UnoRuntime.queryInterface(XComponentLoader.class, factory.createInstanceWithContext("com.sun.star.frame.Desktop", context));            
        } catch (Exception ex) {
            Logger.getLogger(OoopsPlugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return loader;
    }
    
    public XComponent loadDocumentComponent(){
        return this.loadDocumentComponent("private:factory/swriter");
    }

    public XComponent loadDocumentComponent(InputStream inputStream){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            byte[] byteBuffer = new byte[4096];
            int byteBufferLength;
            while ((byteBufferLength = inputStream.read(byteBuffer)) > 0) {
                bytes.write(byteBuffer, 0, byteBufferLength);
            }
            inputStream.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (java.io.IOException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        InStream inStream = new InStream(bytes.toByteArray());
        return loadDocumentComponent(inStream);
    }
    
    public XComponent loadDocumentComponent(InStream inStream){
        XComponent documentComponent=null;
        XComponentLoader loader = this.queryComponentLoader();
        try {
            PropertyValue[] propertyValue = new PropertyValue[2];
            propertyValue[0] = new PropertyValue();
            propertyValue[0].Name = "InputStream";
            propertyValue[0].Value = inStream;
            if( hidden ){
                propertyValue[1] = new PropertyValue();
                propertyValue[1].Name = "Hidden";
                propertyValue[1].Value = new Boolean(true);
            } else {
                propertyValue[1] = new PropertyValue();
                propertyValue[1].Name = "Hidden";
                propertyValue[1].Value = new Boolean(false);                
            }
            try {
                documentComponent = loader.loadComponentFromURL("private:stream", "_blank", 0, propertyValue);
            } catch (com.sun.star.lang.IllegalArgumentException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (IOException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.activeComponent = documentComponent;
        return this.activeComponent;
    }
    
    public XComponent loadDocumentComponent(String url){
        XComponent documentComponent=null;
        XComponentLoader loader = this.queryComponentLoader();
        try {
            PropertyValue[] propertyvalue = new PropertyValue[0];
            documentComponent = loader.loadComponentFromURL(url, "_blank", 0, propertyvalue);
        } catch (IOException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.activeComponent = documentComponent;
        return this.activeComponent;
    }

    public Boolean storeDocumentComponent(OutputStream outputStream){
        //return this.storeDocumentComponent(outputStream, "writer_pdf_Export");
        return this.storeDocumentComponent(outputStream, "writer8");
    }

    public Boolean storeDocumentComponent(OutputStream outputStream, String filter){
        OutStream outStream = new OutStream();
        PropertyValue[] propertyValue = null;
        if( "writer_pdf_Export".equals(filter) ){
            propertyValue = new PropertyValue[3];
            propertyValue[2] = new PropertyValue();
            propertyValue[2].Name = "SelectPdfVersion";
            propertyValue[2].Value = 1; // PDF/A
        } else {
            propertyValue = new PropertyValue[2];
        }
        propertyValue[0] = new PropertyValue();
        propertyValue[0].Name = "OutputStream";
        propertyValue[0].Value = outStream;
        propertyValue[1] = new PropertyValue();
        propertyValue[1].Name = "FilterName";
        propertyValue[1].Value = filter;
        XStorable xstorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, this.activeComponent);
        try {
            xstorable.storeToURL("private:stream", propertyValue);
        } catch (IOException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            outputStream.write(outStream.toByteArray());
        } catch (java.io.IOException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Boolean storeDocumentComponent(String storeUrl){
        XStorable xStorable = (XStorable) UnoRuntime.queryInterface(XStorable.class, this.activeComponent);
        try {
            xStorable.storeAsURL(storeUrl, new PropertyValue[0]);
            return true;
        } catch (IOException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Boolean closeDocumentComponent(){
        XCloseable xclosable = (XCloseable) UnoRuntime.queryInterface(XCloseable.class, this.activeComponent);
        try {
            xclosable.close(true);
            return true;
        } catch (CloseVetoException ex) {
            Logger.getLogger(OoopsHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /*
    @Override
    protected void finalize() throws Throwable {
        // how to destroy the bridge?
        super.finalize();
    }*/
    
}


/* OpenOffice.org 3.0.1 filters

"Name","Type","UIName"
"Rich Text Format","writer_Rich_Text_Format","Rich Text Format"
"SVG - Scalable Vector Graphics","svg_Scalable_Vector_Graphics","SVG - Scalable Vector Graphics"
"draw_pct_Export","pct_Mac_Pict","PCT - Mac Pict"
"draw_eps_Export","eps_Encapsulated_PostScript","EPS - Encapsulated PostScript"
"Lotus","calc_Lotus","Lotus 1-2-3"
"draw_gif_Export","gif_Graphics_Interchange","GIF - Graphics Interchange Format"
"PCX - Zsoft Paintbrush","pcx_Zsoft_Paintbrush","PCX - Zsoft Paintbrush"
"DIF","calc_DIF","Data Interchange Format"
"HTML (StarWriter)","writer_web_HTML","HTML Document (OpenOffice.org Writer)"
"draw_flash_Export","graphic_SWF","Macromedia Flash (SWF)"
"MS WinWord 6.0","writer_MS_WinWord_60","Microsoft Word 6.0"
"StarOffice XML (Chart)","chart_StarOffice_XML_Chart","OpenOffice.org 1.0 Chart"
"impress8","impress8","ODF Presentation"
"draw_tif_Export","tif_Tag_Image_File","TIFF - Tagged Image File Format"
"MS Word 95","writer_MS_Word_95","Microsoft Word 95"
"draw_wmf_Export","wmf_MS_Windows_Metafile","WMF - Windows Metafile"
"MS Word 97","writer_MS_Word_97","Microsoft Word 97/2000/XP"
"writerglobal8","writerglobal8","ODF Master Document"
"HTML","writer_web_HTML","HTML Document"
"impress_StarOffice_XML_Impress_Template","impress_StarOffice_XML_Impress_Template","OpenOffice.org 1.0 Presentation Template"
"XHTML Draw File","XHTML_File","XHTML"
"MS Word 97 Vorlage","writer_MS_Word_97_Vorlage","Microsoft Word 97/2000/XP Template"
"MS Word 95 Vorlage","writer_MS_Word_95_Vorlage","Microsoft Word 95 Template"
"WordPerfect Graphics","draw_WordPerfect_Graphics","WordPerfect Graphics"
"MS Word 2007 XML Template","writer_MS_Word_2007_Template","Microsoft Word 2007 XML Template"
"calc8_template","calc8_template","ODF Spreadsheet Template"
"XHTML Impress File","XHTML_File","XHTML"
"SGF - StarOffice Writer SGF","sgf_StarOffice_Writer_SGF","SGF - StarWriter Graphics Format"
"OpenDocument Drawing Flat XML","draw_ODG_FlatXML","OpenDocument Drawing (Flat XML)"
"EMF - MS Windows Metafile","emf_MS_Windows_Metafile","EMF - Enhanced Metafile"
"UOF presentation","Unified_Office_Format_presentation","Unified Office Format presentation"
"StarOffice XML (Calc)","calc_StarOffice_XML_Calc","OpenOffice.org 1.0 Spreadsheet"
"calc_pdf_Export","pdf_Portable_Document_Format","PDF - Portable Document Format"
"calc_HTML_WebQuery","writer_web_HTML","Web Page Query (OpenOffice.org Calc)"
"writer_web_StarOffice_XML_Writer_Web_Template","writer_web_StarOffice_XML_Writer_Web_Template","OpenOffice.org 1.0 HTML Template"
"writer8_template","writer8_template","ODF Text Document Template"
"draw_pbm_Export","pbm_Portable_Bitmap","PBM - Portable Bitmap"
"calc_StarOffice_XML_Calc_Template","calc_StarOffice_XML_Calc_Template","OpenOffice.org 1.0 Spreadsheet Template"
"writer_web_HTML_help","writer_web_HTML_help","Help content"
"SVM - StarView Metafile","svm_StarView_Metafile","SVM - StarView Metafile"
"OpenDocument Spreadsheet Flat XML","calc_ODS_FlatXML","OpenDocument Spreadsheet (Flat XML)"
"writer_web_pdf_Export","pdf_Portable_Document_Format","PDF - Portable Document Format"
"math_pdf_Export","pdf_Portable_Document_Format","PDF - Portable Document Format"
"chart8","chart8","ODF Chart"
"LotusWordPro","writer_LotusWordPro_Document","Lotus WordPro Document"
"writer_MIZI_Hwp_97","writer_MIZI_Hwp_97","Hangul WP 97"
"MS_Works","writer_MS_Works_Document","Microsoft Works Document"
"StarOffice XML (Draw)","draw_StarOffice_XML_Draw","OpenOffice.org 1.0 Drawing"
"XPM","xpm_XPM","XPM - X PixMap"
"draw_xpm_Export","xpm_XPM","XPM - X PixMap"
"draw_PCD_Photo_CD_Base16","pcd_Photo_CD_Base16","PCD - Kodak Photo CD (192x128)"
"math8","math8","ODF Formula"
"MS Excel 5.0 (StarWriter)","calc_MS_Excel_5095","Microsoft Excel 5.0 (OpenOffice.org Writer)"
"UOF spreadsheet","Unified_Office_Format_spreadsheet","Unified Office Format spreadsheet"
"PCT - Mac Pict","pct_Mac_Pict","PCT - Mac Pict"
"MediaWiki_Web","MediaWiki","MediaWiki"
"writerweb8_writer_template","writerweb8_writer_template","HTML Document Template"
"writer8","writer8","ODF Text Document"
"writerweb8_writer","writer8","OpenOffice.org Text (OpenOffice.org Writer/Web)"
"HTML (StarCalc)","writer_web_HTML","HTML Document (OpenOffice.org Calc)"
"draw_ppm_Export","ppm_Portable_Pixelmap","PPM - Portable Pixelmap"
"SYLK","calc_SYLK","SYLK"
"Calc MS Excel 2007 XML","MS Excel 2007 XML","Microsoft Excel 2007 XML"
"MET - OS/2 Metafile","met_OS2_Metafile","MET - OS/2 Metafile"
"OpenDocument Presentation Flat XML","impress_ODP_FlatXML","OpenDocument Presentation (Flat XML)"
"impress_html_Export","graphic_HTML","HTML Document (OpenOffice.org Impress)"
"impress_StarOffice_XML_Draw","draw_StarOffice_XML_Draw","OpenOffice.org 1.0 Drawing (OpenOffice.org Impress)"
"WordPerfect","writer_WordPerfect_Document","WordPerfect Document"
"Lotus 1-2-3 1.0 (WIN) (StarWriter)","writer_Lotus_1_2_3_10_WIN_StarWriter","Lotus 1-2-3 1.0 WIN (OpenOffice.org Writer)"
"draw_emf_Export","emf_MS_Windows_Metafile","EMF - Enhanced Metafile"
"DocBook File","writer_DocBook_File","DocBook"
"draw_StarOffice_XML_Draw_Template","draw_StarOffice_XML_Draw_Template","OpenOffice.org 1.0 Drawing Template"
"MS Excel 95 Vorlage/Template","calc_MS_Excel_95_VorlageTemplate","Microsoft Excel 95 Template"
"writer_web_StarOffice_XML_Writer","writer_StarOffice_XML_Writer","OpenOffice.org 1.0 Text Document (OpenOffice.org Writer/Web)"
"Rich Text Format (StarCalc)","writer_Rich_Text_Format","Rich Text Format (OpenOffice.org Calc)"
"DXF - AutoCAD Interchange","dxf_AutoCAD_Interchange","DXF - AutoCAD Interchange Format"
"Calc MS Excel 2007 Binary","MS Excel 2007 Binary","Microsoft Excel 2007 Binary"
"impress8_template","impress8_template","ODF Presentation Template"
"MediaWiki","MediaWiki","MediaWiki"
"MS Excel 4.0 Vorlage/Template","calc_MS_Excel_40_VorlageTemplate","Microsoft Excel 4.0 Template"
"Impress MS PowerPoint 2007 XML Template","MS PowerPoint 2007 XML Template","Microsoft PowerPoint 2007 XML Template"
"RAS - Sun Rasterfile","ras_Sun_Rasterfile","RAS - Sun Raster Image"
"Text (encoded) (StarWriter/Web)","writer_Text","Text Encoded (OpenOffice.org Writer/Web)"
"MS Excel 4.0","calc_MS_Excel_40","Microsoft Excel 4.0"
"Quattro Pro 6.0","calc_QPro","Quattro Pro 6.0"
"MS Excel 5.0/95","calc_MS_Excel_5095","Microsoft Excel 5.0"
"impress_xpm_Export","xpm_XPM","XPM - X PixMap"
"impress_wmf_Export","wmf_MS_Windows_Metafile","WMF - Windows Metafile"
"impress_tif_Export","tif_Tag_Image_File","TIFF - Tagged Image File Format"
"impress_svm_Export","svm_StarView_Metafile","SVM - StarView Metafile"
"impress_svg_Export","svg_Scalable_Vector_Graphics","SVG - Scalable Vector Graphics"
"impress_ras_Export","ras_Sun_Rasterfile","RAS - Sun Raster Image"
"impress_ppm_Export","ppm_Portable_Pixelmap","PPM - Portable Pixelmap"
"impress_png_Export","png_Portable_Network_Graphic","PNG - Portable Network Graphic"
"impress_pgm_Export","pgm_Portable_Graymap","PGM - Portable Graymap"
"impress_pdf_Export","pdf_Portable_Document_Format","PDF - Portable Document Format"
"impress_pct_Export","pct_Mac_Pict","PCT - Mac Pict"
"impress_pbm_Export","pbm_Portable_Bitmap","PBM - Portable Bitmap"
"impress_met_Export","met_OS2_Metafile","MET - OS/2 Metafile"
"impress_jpg_Export","jpg_JPEG","JPEG - Joint Photographic Experts Group"
"impress_gif_Export","gif_Graphics_Interchange","GIF - Graphics Interchange Format"
"impress_eps_Export","eps_Encapsulated_PostScript","EPS - Encapsulated PostScript"
"impress_emf_Export","emf_MS_Windows_Metafile","EMF - Enhanced Metafile"
"impress_bmp_Export","bmp_MS_Windows","BMP - Windows Bitmap"
"Text (encoded) (StarWriter/GlobalDocument)","writer_Text","Text Encoded (OpenOffice.org Master Document)"
"PNG - Portable Network Graphic","png_Portable_Network_Graphic","PNG - Portable Network Graphic"
"PSD - Adobe Photoshop","psd_Adobe_Photoshop","PSD - Adobe Photoshop"
"draw_png_Export","png_Portable_Network_Graphic","PNG - Portable Network Graphic"
"impress8_draw","draw8","ODF Drawing (Impress)"
"draw_PCD_Photo_CD_Base4","pcd_Photo_CD_Base4","PCD - Kodak Photo CD (384x256)"
"Text","writer_Text","Text"
"draw_PCD_Photo_CD_Base","pcd_Photo_CD_Base","PCD - Kodak Photo CD (768x512)"
"XHTML Writer File","XHTML_File","XHTML"
"placeware_Export","pwp_PlaceWare","PWP - PlaceWare"
"draw_met_Export","met_OS2_Metafile","MET - OS/2 Metafile"
"MS WinWord 5","writer_MS_WinWord_5","Microsoft WinWord 5"
"Text (encoded)","writer_Text_encoded","Text Encoded"
"writer_globaldocument_StarOffice_XML_Writer","writer_StarOffice_XML_Writer","OpenOffice.org 1.0 Text Document"
"MS Excel 5.0/95 Vorlage/Template","calc_MS_Excel_5095_VorlageTemplate","Microsoft Excel 5.0 Template"
"draw_pgm_Export","pgm_Portable_Graymap","PGM - Portable Graymap"
"T602Document","writer_T602_Document","T602 Document"
"StarOffice XML (Math)","math_StarOffice_XML_Math","OpenOffice.org 1.0 Formula"
"MS Excel 95 (StarWriter)","calc_MS_Excel_95","Microsoft Excel 95 (OpenOffice.org Writer)"
"writerglobal8_HTML","writer_web_HTML","HTML (Writer/Global)"
"MathML XML (Math)","math_MathML_XML_Math","MathML 1.01"
"Calc MS Excel 2007 XML Template","MS Excel 2007 XML Template","Microsoft Excel 2007 XML Template"
"StarOffice XML (Impress)","impress_StarOffice_XML_Impress","OpenOffice.org 1.0 Presentation"
"MS Excel 2003 XML","calc_MS_Excel_2003_XML","Microsoft Excel 2003 XML"
"SGV - StarDraw 2.0","sgv_StarDraw_20","SGV - StarDraw 2.0"
"OpenDocument Text Flat XML","writer_ODT_FlatXML","OpenDocument Text (Flat XML)"
"XHTML Calc File","XHTML_File","XHTML"
"writerglobal8_writer","writer8","ODF Text Document"
"PBM - Portable Bitmap","pbm_Portable_Bitmap","PBM - Portable Bitmap"
"draw_svm_Export","svm_StarView_Metafile","SVM - StarView Metafile"
"MathType 3.x","math_MathType_3x","MathType3.x"
"CGM - Computer Graphics Metafile","impress_CGM_Computer_Graphics_Metafile","CGM - Computer Graphics Metafile"
"writer_pdf_Export","pdf_Portable_Document_Format","PDF - Portable Document Format"
"draw8_template","draw8_template","ODF Drawing Template"
"Text - txt - csv (StarCalc)","calc_Text_txt_csv_StarCalc","Text CSV"
"GIF - Graphics Interchange","gif_Graphics_Interchange","GIF - Graphics Interchange Format"
"MS PowerPoint 97","impress_MS_PowerPoint_97","Microsoft PowerPoint 97/2000/XP"
"XBM - X-Consortium","xbm_X_Consortium","XBM - X Bitmap"
"TIF - Tag Image File","tif_Tag_Image_File","TIFF - Tagged Image File Format"
"BMP - MS Windows","bmp_MS_Windows","BMP - Windows Bitmap"
"TGA - Truevision TARGA","tga_Truevision_TARGA","TGA - Truevision Targa"
"MS Excel 97 Vorlage/Template","calc_MS_Excel_97_VorlageTemplate","Microsoft Excel 97/2000/XP Template"
"draw_ras_Export","ras_Sun_Rasterfile","RAS - Sun Raster Image"
"dBase","calc_dBase","dBASE"
"Text (StarWriter/Web)","writer_Text","Text (OpenOffice.org Writer/Web)"
"Lotus 1-2-3 1.0 (DOS) (StarWriter)","writer_Lotus_1_2_3_10_DOS_StarWriter","Lotus 1-2-3 1.0 DOS (OpenOffice.org Writer)"
"UOF text","Unified_Office_Format_text","Unified Office Format text"
"PPM - Portable Pixelmap","ppm_Portable_Pixelmap","PPM - Portable Pixelmap"
"Impress MS PowerPoint 2007 XML","MS PowerPoint 2007 XML","Microsoft PowerPoint 2007 XML"
"calc8","calc8","ODF Spreadsheet"
"draw_svg_Export","svg_Scalable_Vector_Graphics","SVG - Scalable Vector Graphics"
"writer_globaldocument_StarOffice_XML_Writer_GlobalDocument","writer_globaldocument_StarOffice_XML_Writer_GlobalDocument","OpenOffice.org 1.0 Master Document"
"writer_globaldocument_pdf_Export","pdf_Portable_Document_Format","PDF - Portable Document Format"
"draw_jpg_Export","jpg_JPEG","JPEG - Joint Photographic Experts Group"
"MS Word 2007 XML","writer_MS_Word_2007","Microsoft Word 2007 XML"
"impress_flash_Export","graphic_SWF","Macromedia Flash (SWF)"
"PGM - Portable Graymap","pgm_Portable_Graymap","PGM - Portable Graymap"
"StarOffice XML (Writer)","writer_StarOffice_XML_Writer","OpenOffice.org 1.0 Text Document"
"MS Word 2003 XML","writer_MS_Word_2003_XML","Microsoft Word 2003 XML"
"writer_StarOffice_XML_Writer_Template","writer_StarOffice_XML_Writer_Template","OpenOffice.org 1.0 Text Document Template"
"MS Excel 4.0 (StarWriter)","calc_MS_Excel_40","Microsoft Excel 4.0 (OpenOffice.org Writer)"
"MS PowerPoint 97 Vorlage","impress_MS_PowerPoint_97_Vorlage","Microsoft PowerPoint 97/2000/XP Template"
"draw_pdf_Export","pdf_Portable_Document_Format","PDF - Portable Document Format"
"draw_html_Export","graphic_HTML","HTML Document (OpenOffice.org Draw)"
"draw_bmp_Export","bmp_MS_Windows","BMP - Windows Bitmap"
"MS Excel 95","calc_MS_Excel_95","Microsoft Excel 95"
"draw8","draw8","ODF Drawing"
"WMF - MS Windows Metafile","wmf_MS_Windows_Metafile","WMF - Windows Metafile"
"MS Excel 97","calc_MS_Excel_97","Microsoft Excel 97/2000/XP"
"JPG - JPEG","jpg_JPEG","JPEG - Joint Photographic Experts Group"
"EPS - Encapsulated PostScript","eps_Encapsulated_PostScript","EPS - Encapsulated PostScript"

*/