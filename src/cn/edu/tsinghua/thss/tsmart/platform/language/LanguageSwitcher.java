package cn.edu.tsinghua.thss.tsmart.platform.language;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

public class LanguageSwitcher {

	@SuppressWarnings("unused")
    public static void changeLocale(String locale) {
		Location configArea = Platform.getInstallLocation();
		String product = Platform.getProduct().getName();
		if (configArea == null) {
			return;
		}

		URL location = null;
		try {
			location = new URL(configArea.getURL().toExternalForm() + product
					+ ".ini");
		} catch (MalformedURLException e) {
			// This should never happen
		}
		// System.out.println("LanguageSwitchHandler.loadConfigurationInfo(): "
		// + location);

		try {
			String fileName = location.getFile();
			File file = new File(fileName);
			fileName += ".bak";
			file.renameTo(new File(fileName));
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			BufferedWriter out = new BufferedWriter(new FileWriter(location
					.getFile()));
			List<String> paraList = new ArrayList<String>();
			try {
				boolean isNl = false;
				boolean isNlWiritten = false;
				String line = in.readLine();
				while(line != null)
				{
					paraList.add(line);
					line = in.readLine();
				}
				for (int i = 0; i < paraList.size(); i++) {
					if (paraList.get(i).startsWith("-nl")) {
						paraList.set(i+1, locale);
						isNlWiritten = true;
						break;
					}
				}
				
				if (!isNlWiritten) {
					out.write("-nl");
					out.newLine();
					out.write(locale);
					out.newLine();
				}
				for (int i = 0; i < paraList.size(); i++) {
					out.write(paraList.get(i));
					out.newLine();
				}
				
//				while (line != null) {
//					paraList.add(line);
//					if (!isNl) {
//						out.write(line);
//					} else {
//						out.write(locale);
//						isNl = false;
//						isNlWiritten = true;
//					}
//					out.newLine();
//					if (line.equals("-nl")) {
//						isNl = true;
//					}
//					line = in.readLine();
//				}
//				if (!isNlWiritten) {
//					out.write("-nl");
//					out.newLine();
//					out.write(locale);
//					out.newLine();
//				}
				out.flush();
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
