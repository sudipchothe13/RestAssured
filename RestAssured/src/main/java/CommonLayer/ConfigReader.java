package CommonLayer;

import java.io.FileInputStream;
import java.util.Properties;

public final class ConfigReader {

	private static Properties prop = new Properties();

	private ConfigReader() {
	}

	static {
		try {
			FileInputStream fis = new FileInputStream(
					System.getProperty("user.dir") + "/src/main/java/ConfigLayer/Config.properties");
			prop.load(fis);
		} catch (Exception e) {
			throw new RuntimeException("Failed to  load Config.properties", e);
		}
	}

	public static String get(String key) {
		return prop.getProperty(key);
	}

}
