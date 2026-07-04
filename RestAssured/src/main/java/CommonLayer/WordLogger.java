package CommonLayer;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordLogger {

	private static ThreadLocal<XWPFDocument> document = new ThreadLocal<>();
	private static ThreadLocal<FileOutputStream> outputStream = new ThreadLocal<>();
	private static String scenarioFolderPath;

	public static void startScenario(String scenarioName, String browser) {
		try {
			if (scenarioFolderPath == null) {
				String baseDir = System.getProperty("user.dir") + "/Reports/WordLogs";
				String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
				String folder = baseDir + "/" + scenarioName.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp;
				File dir = new File(folder);
				if (!dir.exists())
					dir.mkdirs();
				scenarioFolderPath = folder;
			}

			String browserName = browser.substring(0, 1).toUpperCase() + browser.substring(1).toLowerCase();
			String fileName = browserName + "_WordLog.docx";
			File file = new File(scenarioFolderPath, fileName);

			XWPFDocument doc = new XWPFDocument();
			FileOutputStream fos = new FileOutputStream(file);

			document.set(doc);
			outputStream.set(fos);

		} catch (Exception e) {
			throw new RuntimeException("WordLogger init failed: " + e.getMessage());
		}
	}

	public static void writeBase64Screenshot(String base64, String message) {
		try {
			byte[] bytes = java.util.Base64.getDecoder().decode(base64);
			XWPFDocument doc = document.get();
			if (doc != null) {
				XWPFParagraph para = doc.createParagraph();
				XWPFRun run = para.createRun();
				run.setText(message);
				run.addBreak();
				run.addPicture(new java.io.ByteArrayInputStream(bytes), XWPFDocument.PICTURE_TYPE_PNG, "screenshot.png",
						Units.toEMU(400), Units.toEMU(250));
			}
		} catch (Exception e) {
			Log.error("Failed WordLogger screenshot: " + e.getMessage());
		}
	}

	public static void endScenario() {
		try {
			if (document.get() != null)
				document.get().write(outputStream.get());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (outputStream.get() != null)
					outputStream.get().close();
			} catch (Exception ignored) {
			}
			try {
				if (document.get() != null)
					document.get().close();
			} catch (Exception ignored) {
			}
			document.remove();
			outputStream.remove();
			scenarioFolderPath = null;
		}
	}

	public static String getScenarioFolder() {
		return scenarioFolderPath;
	}
}
