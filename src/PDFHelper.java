import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import processing.core.PConstants;
import processing.core.PImage;

public class PDFHelper {

	/***
	 * Load a pdf and convert each page to a PImage.  Return a List of PImages.
	 * @param path path to the pdf including full filename and file extension
	 * @return a List of PImage objects corresponding to the pages of the pdf
	 */
	public static ArrayList<PImage> getPImagesFromPdf(String path) {
		InputStream is = PDFHelper.class.getResourceAsStream(path);
		ArrayList<PImage> images = new ArrayList<PImage>();
		PDDocument pdf = null;
		
		try {
			pdf = PDDocument.load(is);
		} catch (IOException e) {
			System.out.println("Couldn't load pdf");
			System.out.println("DID YOU ADD THE ASSETS FOLDER TO YOUR CLASS PATH IN ECLIPSE?");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		List<PDPage> pages = pdf.getDocumentCatalog().getAllPages();

		for (PDPage page : pages) {
			try {
				BufferedImage image = page.convertToImage();

				PImage img = new PImage(image.getWidth(), image.getHeight(), PConstants.ARGB);
				image.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
				img.updatePixels();

				images.add(img);
				System.out.println("Adding page " + images.size());
			} catch (IOException e) {
				System.out.println("problem converting to image");
				e.printStackTrace();
			}
		}

		try {
			pdf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return images;
	}
	
	public static PImage getPageImage(String pathtoPdf, int pageNum) {
		InputStream is = PDFHelper.class.getResourceAsStream(pathtoPdf);
		ArrayList<PImage> images = new ArrayList<PImage>();
		PDDocument pdf = null;
		
		try {
			pdf = PDDocument.load(is);
		} catch (IOException e) {
			System.out.println("Couldn't load pdf");
			System.out.println("DID YOU ADD THE ASSETS FOLDER TO YOUR CLASS PATH IN ECLIPSE?");
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		List<PDPage> pages = pdf.getDocumentCatalog().getAllPages();

		if (pageNum >= 0 && pageNum < pages.size()) {
			PDPage page = pages.get(pageNum);
			
			try {
				BufferedImage image = page.convertToImage();

				PImage img = new PImage(image.getWidth(), image.getHeight(), PConstants.ARGB);
				image.getRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);
				img.updatePixels();
				return img;
			} catch (IOException e) {
				System.out.println("problem converting to image");
				e.printStackTrace();
			}
		
		} else return null;
		
		try {
			pdf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
