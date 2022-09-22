package com.tadamia;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Main {
    public static final Logger lgg = LogManager.getLogger();

    public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException, ExecutionException {
        InputStream in = Main.class.getClassLoader().getResourceAsStream("config.properties");
        Properties props = new Properties();
        props.load(in);

        //1. წინა ორი დავალების (14.09.2022 და 16.09.2022) ვებ სერვისებს დაუწერეთ
        //კლიენტები როგორც ჯავას ახალი http client-ით, ისე UrlConnection კლასებით.
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/http-client-and-urlconnection/" + props.getProperty("http_client_url") + "?s=899"))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());

        // URL for microsoft cognitive server.
        URL url = new URL("http://localhost:8080/http-client-and-urlconnection/" + props.getProperty("http_url_connection") + "?p=12");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        System.out.println("Response -> " + FullResponseBuilder.getFullResponse(con));
        System.out.println();
        System.out.println("Start sending multiple http requests");

        //4.  ახალი http client-ის ნაწილში რამდენიმე request გაუშვით პარალელურადაც
        //ასინქრონული გაგზავნის მეთოდით და დაამუშავეთ დაბრუნებული პასუხებიც.
        HttpClient clientAsync = HttpClient.newHttpClient();
        List<URI> uris = Arrays.asList(new URI("http://localhost:8080/http-client-and-urlconnection/" + props.getProperty("http_client_url") + "?s=899"),
                new URI("http://localhost:8080/http-client-and-urlconnection/" + props.getProperty("http_url_connection") + "?p=12"));

        List<CompletableFuture<HttpResponse<String>>> futures = uris.stream().map(uri -> clientAsync.sendAsync(HttpRequest.newBuilder(uri).build(), HttpResponse.BodyHandlers.ofString())).collect(Collectors.toList());
        System.out.println("http requests sent successfully");
        System.out.println("start waiting for http responses");

        System.out.println("http requests sent successfully");
        System.out.println("start waiting for http responses");
        CompletableFuture.allOf(futures.toArray(CompletableFuture<?>[]::new)).join();

        for (var future : futures) {
            HttpResponse<String> response_ = future.get();
            System.out.println("http_Get_Response_URL: {} " + response_.uri());
            System.out.println("http_Get_Response_Body: {} " + response_.body());
            System.out.println("http_Get_Response_Status_Code: {} " + response_.statusCode());
        }
        System.out.println("http responses got successfully");
    }
}

/*
დავალება 19.09.2022 (ჩაბარების ბოლო ვადა 22.09.2022 23:59)
+++1. წინა ორი დავალების (14.09.2022 და 16.09.2022) ვებ სერვისებს დაუწერეთ
+++კლიენტები როგორც ჯავას ახალი http client-ით, ისე UrlConnection კლასებით.
+++2. პროექტს გაუკეთეთ კონფიგურაცია (properties ფაილი),
+++სადაც გაწერილი გექნებათ ტაიმაუტები და ვებ სერვისების URL-ები.
3. ასევე პროექტს უნდა ჰქონდეს ლოგირება და ჩანდეს მოთხოვნის გაგზავნისა და პასუხის მიღების დროები.
+++4.  ახალი http client-ის ნაწილში რამდენიმე request გაუშვით პარალელურადაც
+++ასინქრონული გაგზავნის მეთოდით და დაამუშავეთ დაბრუნებული პასუხებიც.
5.  ერთ რომელიმე სერვლეტს სერვერის მხარეს დაუმატეთ http ავტორიზაცია და
შემდეგ კლიენტის მხარეს დაწერეთ ამ სერვლეტის გამოძახება ორივე ხერხით:
როგორც PasswordAuthentication, ისე Authorization ჰედერით.
6. კლიენტის მხარეს ისე გამოიძახეთ ვებ მეთოდები,
რომ პასუხად ხან http 200 წარმატებული კოდი დაბრუნდეს ხანაც წარუმატებელი კოდები.
* */