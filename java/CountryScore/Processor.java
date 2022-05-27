import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Processor {
	private String Key = "";
	private String[] Countries;
	private String[] Indicators;
	private int max = 0;
	private int start = 0;

	public Processor(String APIKey, String[] Countries, String[] Indicators, int start_year, int max_year) {
		Key = APIKey;
		this.Countries = Countries;
		this.Indicators = Indicators;
		this.max = max_year;
		this.start = start_year;
	}

	public String calculate() {
		String output = "";
		// 1. Construct a HTTP Request for a specific country and indicator. Get the
		// data historically from 2000.
		LinkedHashMap<String, String> data = new LinkedHashMap<>(); // K: country,indicator,year , V: value
		for (String country : Countries) {
			for (String indicator : Indicators) {
				System.out.println("Retrieving "+indicator+" in "+country+" from TradingEconomics API");
				try {
					String[] raw = new HTTPSRequest().construct(country, indicator, start + "-01-01", Key, "json")
							.split("\\},\\{");
					qq: for (String query : raw) {
						if (!query.startsWith("{"))
							query = "{" + query;
						if (!query.endsWith("}"))
							query = query + "}";
						if (query.contains("Free accounts have access to the following countries"))
							break;
						HashMap<String, String> q = new HashMap<>();
						new JSON();
						q = JSON.QHM(query);
						if (q.containsKey("Value")) {
							String value = q.get("Value");
							String date = q.get("DateTime");
							if (value.isBlank() || value.isEmpty() || (value == null))
								break qq;
							boolean is_yearly = false;
							for (int i = 0; i < (max - start); i++) {
								if (date.startsWith((start + i) + "-01") || date.startsWith((start + i) + "-12")) {
									is_yearly = true;
									break;
								}
							}
							if (is_yearly)
								data.put(country + "," + indicator + "," + date.split("-")[0], value);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// 2. Calculate score
		System.out.println("Retrieval is done, calculating..");
		LinkedHashMap<String, Double> scores = new LinkedHashMap<>(); // K: country,year V: score
		for (String country : Countries) {
			for (int i = 0; i < (max - start); i++) {

				double PPP = 1;
				String PPPS = data.get(country + ",GDP per capita PPP," + (start + i));
				if (!((PPPS == null) || PPPS.isBlank() || PPPS.isEmpty()))
					PPP = Double.parseDouble(PPPS);
				double Unemp = 1;
				String UnempS = data.get(country + ",Unemployment Rate," + (start + i));
				if (!((UnempS == null) || UnempS.isBlank() || UnempS.isEmpty()))
					Unemp = Double.parseDouble(UnempS);
				double Inf = 1;
				String InfS = data.get(country + ",Inflation Rate," + (start + i));
				if (!((InfS == null) || InfS.isBlank() || InfS.isEmpty()))
					Unemp = Double.parseDouble(UnempS);
				scores.put(country + "," + (start + i), ((PPP / 1000.0) * (1.0 / Math.log(0.5 * Unemp)) * 1) / Inf);
			}
		}
		// 3. return the whole calculation.
		output = "Country, Year, Value\n";
		for (Entry<String, Double> entry : scores.entrySet()) {
			output += (entry.getKey() + "," + entry.getValue() + "\n");
		}
		return output;
	}

}
