import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class HTTPSRequest {
	public String construct(String country, String type, String startdate, String APIKey, String datatype) {
		String responseBody = "";
		String Request = "GET /historical/country/" + country.replaceAll(" ", "%20") + "/indicator/" + type.replaceAll(" ", "%20") + "/" + startdate + "?c=" + APIKey
				+ "&f=" + datatype + "\r\n" + "Host: api.tradingeconomics.com\r\n" + "accept: */*\r\n"
				+ "user-agent: Tester\r\n" + "accept-language: en-US,en;q=0.5\r\n" + "connection: close\r\n\r\n";
		try {
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket("api.tradingeconomics.com", 443);
			socket.startHandshake();
			BufferedInputStream BIS = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream BOS = new BufferedOutputStream(socket.getOutputStream());
			BOS.write(Request.getBytes());
			BOS.flush();
			String response = new String(BIS.readAllBytes()).replaceFirst("\\[", "");
			response = response.substring(0, response.length() - 1);
			responseBody = response;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return responseBody;
	}
}
