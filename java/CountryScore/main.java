
public class main {
/**
 * This program is supposed to take GDP-PPP, unemp. rate, and inflation rate from
 *  mexico, sweden, thailand. and score the countries historically based on a chosen date.
 *  Then output a JSON file with historical scores from Jan.1 2000. 
 *  This file is only a launcher. A web API can be placed above the processor in order to input data. Use JSON format.
 */
	public static void main(String[] args) {
		String APIKey = "5d2avqc3dq4sdmc:x8xt41fp19ifiiq"; // My API Key xD
		String[] Countries = {"sweden","mexico","thailand"};
		String[] Indicators = {"GDP per capita PPP","Unemployment Rate","Inflation Rate"};
		Processor P = new Processor(APIKey, Countries, Indicators, 2000, 2021);
		System.out.println(P.calculate());
	}

}
