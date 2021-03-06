package games.league;

import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Scanner;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.GrayColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class WPCVisualizerPDF {

	public static void main(String[] args) {
		Document document = new Document(PageSize.A4);

		try {
			PdfWriter.getInstance(document, new FileOutputStream("WPC.pdf"));
			document.open();

			float max = Float.MIN_VALUE;
			float min = Float.MAX_VALUE;
			float w[] = new float[25];
			
			PdfPTable table = new PdfPTable(5);
			Scanner sc = new Scanner(System.in).useLocale(Locale.ENGLISH);
			double sum = 0;
			
			for (int i = 0; i < 25; i++) {
				w[i] = (float)sc.nextDouble();
				max = Math.max(w[i], max);
				min = Math.min(w[i], min);
			}
			
			for (int i = 0; i < 25; i++) {
				sum += w[i];
				System.out.println(w);
				PdfPCell cell = new PdfPCell(new Paragraph(" "));
				cell.setPadding(40.0f);
				cell.setBorder(Rectangle.BOX);
				cell.setBorderColor(new GrayColor(0.7f));
				
//				cell.setBackgroundColor(new GrayColor((max - w[i]) / (max - min)));
				cell.setBackgroundColor(new GrayColor(0.5f - w[i] / 2.0f));
				
//				if (w >= 0) {
//					cell.setBackgroundColor(new GrayColor(w));					
//					cell.setBackgroundColor(new Color(0, Math.min(1, w), 0));					
//				} else {
//					cell.setBackgroundColor(new Color(Math.min(1, Math.abs(w)), 0, 0));
//				}

				table.addCell(cell);
			}
			
			System.err.println(sum / 25);
			document.add(table);
		} catch (Exception de) {
			de.printStackTrace();
		}
		document.close();
	}
}