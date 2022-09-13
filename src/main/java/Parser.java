import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String url = "https://www.pogoda.spb.ru/";

        Document page = null;
        try {
            page = Jsoup.parse(new URL(url), 3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }

    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");



    private static String getDateFromString(String stringDate) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if(matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can not extract date from string");
    }

    private static int printFourValues(Elements values, int index) {
        int count = 4;
        if(index == 0) {
            Element valueLn = values.get(3);
            boolean isMornning = valueLn.text().contains("Утро");
            if (isMornning) {
                count = 3;
            }
        } else {
            for(int i = 0; i < count; i++) {
                Element valueLine = values.get(index + i);
                for(Element td: valueLine.select("td")) {
                    System.out.print(td.text() + "    ");
                }
                System.out.println();
            }
        }

        return count;

    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWeather = page.select("table[class=wt]").first();
        System.out.println(tableWeather);
        Elements names = tableWeather.select("tr[class=wth]");
        Elements values = tableWeather.select("tr[valign=top]");
        int index = 0;
        for (Element name:names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println("    Явления    Температура    Давление    Влажность    Ветер");
            int count = printFourValues(values, index);
            index = index + count;
        }



    }
}
