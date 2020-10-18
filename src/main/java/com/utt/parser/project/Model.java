package com.utt.parser.project;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.nio.DataSource;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Model {
	private static String emailhost;
	private static int emailport;
	private static String emailfrom;
	private static String password;

	static {
		// Reading data from the property file
		Properties prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream("application.properties");
			prop.load(ip);
			emailhost = prop.getProperty("emailhost");
			emailport = Integer.parseInt(prop.getProperty("emailport"));
			emailfrom = prop.getProperty("emailfrom");
			password = prop.getProperty("password");
			ip.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void createExcelSheet(JSONArray jsonArray) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("ExcelData");
			writeHeaderLine(sheet, workbook, jsonArray);
			writeDataLines(sheet, workbook, jsonArray);
			FileOutputStream outputStream;

			outputStream = new FileOutputStream("C:\\Users\\corestratlabs\\Downloads\\JsonExcelDocNew.xlsx");
			workbook.write(outputStream);
			workbook.close();
			outputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void readDataFromTextFile() {
		// Creating a JSONParser object
		try {
			FileReader file = new FileReader("C:\\Users\\corestratlabs\\Downloads\\JsonDocument.pdf");
			JSONParser jsonParser = new JSONParser();
			// Parsing the contents of the JSON file
			JSONArray jsonArray = (JSONArray) jsonParser.parse(file);
			createExcelSheet(jsonArray);
			file.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void readDataFromPdfFile() throws IOException, ParseException {
		System.out.println("here");
		// Creating a JSONParser object
		try (PDDocument document = PDDocument.load(new File("C:\\Users\\corestratlabs\\Downloads\\JsonDocument.pdf"))) {

            document.getClass();

            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                PDFTextStripper tStripper = new PDFTextStripper();

                String pdfFileInText = tStripper.getText(document);
                //System.out.println("Text:" + st);
    		
				// split by whitespace
                String lines[] = pdfFileInText.split("\\r?\\n");
                String data="";
                for (String line : lines) {
                    data+=line;
                }
                JSONParser jsonParser = new JSONParser();
    			// Parsing the contents of the JSON file
    			JSONArray jsonArray = (JSONArray) jsonParser.parse(data);
    			createExcelSheet(jsonArray);
            }
		}

	}

	public void writeHeaderLine(XSSFSheet sheet, XSSFWorkbook workbook, JSONArray jsonArray) {
		String[] headersData = jsonArray.get(0).toString().split(",");
		Row headerRow = sheet.createRow(0);
		CellStyle style = workbook.createCellStyle();
		// Setting Background color
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// Setting the Font
		Font font = workbook.createFont();
		font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		font.setBold(true);
		style.setFont(font);
		// Style for border
		CellStyle borderStyle = workbook.createCellStyle();
		// Setting Background color
		borderStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		borderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		// Setting the Font
		font = workbook.createFont();
		font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
		font.setBold(true);
		borderStyle.setFont(font);
		for (int i = 0; i < headersData.length; i++) {
			if (i == 0) {
				Cell headerCell = headerRow.createCell(i);
				headerCell.setCellValue(
						headersData[i].split(":")[0].substring(2, headersData[i].split(":")[0].length() - 1));
				headerCell.setCellStyle(style);
			} else {
				Cell headerCell = headerRow.createCell(i);
				headerCell.setCellValue(
						headersData[i].split(":")[0].substring(1, headersData[i].split(":")[0].length() - 1));
				headerCell.setCellStyle(style);
			}
		}
	}

	public void writeDataLines(XSSFSheet sheet, XSSFWorkbook workbook, JSONArray jsonArray) {
		String[] data = jsonArray.toJSONString().split("},");
		int k = 1;
		for (int i = 0; i < data.length; i++) {
			Row dataRow = sheet.createRow(k);
			String[] values = data[i].split(",");
			for (int j = 0; j < values.length; j++) {
				if (j == values.length - 1) {
					Cell dataCell = dataRow.createCell(j);
					dataCell.setCellValue(values[j].split(":")[1].substring(1, values[j].split(":")[1].length() - 3));
				} else {
					Cell dataCell = dataRow.createCell(j);
					dataCell.setCellValue(values[j].split(":")[1].substring(1, values[j].split(":")[1].length() - 1));
				}
			}
			k++;
		}
	}

	public void sendEmail(String emailTo) {

		String filePath = "C:\\Users\\corestratlabs\\Downloads\\JsonExcelDoc.xlsx";
		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.put("mail.smtp.host", emailhost);
		properties.put("mail.smtp.port", emailport);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
		// https://www.google.com/settings/security/lesssecureapps
		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(emailfrom, password);
			}
		});
		// Used to debug SMTP issues
		session.setDebug(true);
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(emailfrom));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

			// Set Subject: header field
			message.setSubject("Data sheet");
			Multipart multipart = new MimeMultipart();
			MimeBodyPart attachmentPart = new MimeBodyPart();
			MimeBodyPart textPart = new MimeBodyPart();
			File f = new File(filePath);
			attachmentPart.attachFile(f);
			textPart.setText("PFA the Parsed Excel Data sheet");
			multipart.addBodyPart(textPart);
			multipart.addBodyPart(attachmentPart);
			message.setContent(multipart);
			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (Exception mex) {
			mex.printStackTrace();
		}

	}
	public void excelToJson() throws EncryptedDocumentException, IOException
	{
		FileInputStream inp = new FileInputStream( "C:\\Users\\corestratlabs\\Downloads\\JsonExcelDoc.xlsx" );
		Workbook workbook = WorkbookFactory.create( inp );

		// Get the first Sheet.
		Sheet sheet = workbook.getSheetAt( 0 );

		    // Start constructing JSON.
		    JSONObject json = new JSONObject();

		    // Iterate through the rows.
		    JSONArray rows = new JSONArray();
		    for ( Iterator<Row> rowsIT = sheet.rowIterator(); rowsIT.hasNext(); )
		    {
		        Row row = rowsIT.next();
		        JSONObject jRow = new JSONObject();

		        // Iterate through the cells.
		        JSONArray cells = new JSONArray();
		        for ( Iterator<Cell> cellsIT = row.cellIterator(); cellsIT.hasNext(); )
		        {
		            Cell cell = cellsIT.next();
		            cells.add( cell.getStringCellValue());
		        }
		        jRow.put( "cell", cells );
		        rows.add( jRow );
		    }

		    // Create the JSON.
		    json.put( "rows", rows );

		// Get the JSON text.
		System.out.println(json.toString());
	}
}
