package necoapi.uicomponents.pdf;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.IOException;

public class PDFBackground extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        Image background = null;
        try {
            background = Image.getInstance(ClassLoader.getSystemResource("neco-logo.jpg"));
            // This scales the image to the page,
            // use the image's width & height if you don't want to scale.
            float width = document.getPageSize().getWidth();
            float height = document.getPageSize().getHeight();
            writer.getDirectContentUnder()
                    .addImage(background, 383, 0, 0, 220, 0, 0);
        } catch (BadElementException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

}
