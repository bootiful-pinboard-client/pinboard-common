# A Simple Pinboard Client


![CI](https://github.com/joshlong/pinboard-client/workflows/CI/badge.svg)

## Non-Reactive Spring MVC Applications

The `pinboard.resttemplate.RestTemplatePinboardClient` is a client based on the Spring `RestTemplate`. This uses traditional blocking I/O, based on `InputStream` and `OutputStream`. This might be your only choice in more traditional, Spring MVC, or Servlet-based environments.  

``` 
String myPinboardAccessToken = "...";
new pinboard.resttemplate.RestTemplatePinboardClient(myPinboardAccessToken);
```

## Reactive Spring Webflux Applications 

The `pinboard.webclient.WebClientPinboardClient` is a client based on the reactive Spring `WebClient`. This uses asynchronous, non-blocking I/O, based on Netty. This is a more natural fit in a reactive application. If you can, choose this one first.

``` 
String myPinboardAccessToken = "...";
new pinboard.webclient.WebClientPinboardClient(myPinboardAccessToken);
```