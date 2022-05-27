import java.util.HashMap;

public class ValueFromJSON {
	// I get :
	// {"Country":"Mexico","Category":"GDP per capita PPP","DateTime":"2000-12-31T00:00:00","Value":17756.5700,"Frequency":"Yearly","HistoricalDataSymbol":"MEXNYGDPPCAPPPCD","LastUpdate":"2021-07-07T09:13:00"}
	public static String extract(String JSON, int year) {
		HashMap<String, String> point = new JSON().QHM(JSON);
		if(point.containsKey("Value") &&
				(point.get("DateTime").startsWith(year+"-01-") || point.get("DateTime").startsWith(year+"-12-"))) {
			return point.get("Value");
		}
		return "0";
	}
}
