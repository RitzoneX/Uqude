package co.ritzonex.uqude.tool;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.ResponseHandlerInterface;

public class MyTool {
	/**
	 * 从url获取字符串数据
	 * 
	 * @param urlStr
	 * @return
	 */
	public static String getUrlData(String urlStr) {
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlStr);
			urlConnection = (HttpURLConnection) url.openConnection();
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			return readStream(in);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			urlConnection.disconnect();
		}
		return null;
	}

	private static String readStream(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		in.close();
		return buffer.toString();
	}

	public static void get(Context context, String url,
			ResponseHandlerInterface responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(myCookieStore);
		client.get(url, responseHandler);
	}
}
