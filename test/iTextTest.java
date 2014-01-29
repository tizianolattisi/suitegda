import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.junit.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * User: tiziano
 * Date: 29/01/14
 * Time: 11:23
 */
public class iTextTest {

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void test() throws IOException, DocumentException {

        writePDF();
        markPDF();

    }

    private void writePDF() throws FileNotFoundException, DocumentException, UnsupportedEncodingException {
        Document document = new Document(new Rectangle(PageSize.A4));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("test.pdf"));
        document.open();

        document.add(new Paragraph("Documento originario pdf"));

        document.close();
    }

    private void markPDF() throws IOException, DocumentException {
        PdfReader pdfReader = new PdfReader("test.pdf");
        PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream("test-marked.pdf"));
        Image image = getBarcode();
        PdfContentByte content = pdfStamper.getUnderContent(1); // first page
        image.setAbsolutePosition(40f, 800f);
        content.addImage(image);
        pdfStamper.close();
    }

    private Image getBarcode() throws UnsupportedEncodingException, BadElementException {
        BarcodeDatamatrix bc = new BarcodeDatamatrix();
        bc.setOptions(BarcodeDatamatrix.DM_ASCII);
        bc.generate("Bene, ecco tutto.");
        return bc.createImage();
    }

}
