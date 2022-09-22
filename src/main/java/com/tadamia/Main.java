package com.tadamia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static java.time.temporal.ChronoUnit.MILLIS;

public class Main {
    public static final Logger lgg = LogManager.getLogger();

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException {
        InputStream in = Main.class.getClassLoader().getResourceAsStream("config.properties");
        Properties props = new Properties();
        props.load(in);

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/http-client-and-urlconnection/"+props.getProperty("http_client_url")+"?s=899"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        // URL for microsoft cognitive server.
        URL url = new URL("http://localhost:8080/http-client-and-urlconnection/"+props.getProperty("http_url_connection")+"?p=12");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        System.out.println("Response -> " + FullResponseBuilder.getFullResponse(con));

//        HttpRequest request1 = HttpRequest.newBuilder()
//                .GET()
//                .uri(new URI("http://localhost:8080/http-client-and-urlconnection/"+props.getProperty("http_client_url")))
//                .build();
//
        HttpClient client = HttpClient.newBuilder()
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("admin", "asdASD123".toCharArray());
                    }
                })
                .build();
//
        System.out.println(client.authenticator().toString());
//
//        System.out.println(response1);
    }
}

/*
დავალება 19.09.2022 (ჩაბარების ბოლო ვადა 22.09.2022 23:59)
+++1. წინა ორი დავალების (14.09.2022 და 16.09.2022) ვებ სერვისებს დაუწერეთ
+++კლიენტები როგორც ჯავას ახალი http client-ით, ისე UrlConnection კლასებით.
+++2. პროექტს გაუკეთეთ კონფიგურაცია (properties ფაილი),
+++სადაც გაწერილი გექნებათ ტაიმაუტები და ვებ სერვისების URL-ები.
3. ასევე პროექტს უნდა ჰქონდეს ლოგირება და ჩანდეს მოთხოვნის გაგზავნისა და პასუხის მიღების დროები.
4.  ახალი http client-ის ნაწილში რამდენიმე request გაუშვით პარალელურადაც
ასინქრონული გაგზავნის მეთოდით და დაამუშავეთ დაბრუნებული პასუხებიც.
5.  ერთ რომელიმე სერვლეტს სერვერის მხარეს დაუმატეთ http ავტორიზაცია და
შემდეგ კლიენტის მხარეს დაწერეთ ამ სერვლეტის გამოძახება ორივე ხერხით:
როგორც PasswordAuthentication, ისე Authorization ჰედერით.
6. კლიენტის მხარეს ისე გამოიძახეთ ვებ მეთოდები,
რომ პასუხად ხან http 200 წარმატებული კოდი დაბრუნდეს ხანაც წარუმატებელი კოდები.
* */