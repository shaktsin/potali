package com.potaliadmin.util.rest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;

import org.glassfish.jersey.client.*;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakti Singh on 12/5/14.
 */
public class HippoHttpUtils {

  private static Logger logger = LoggerFactory.getLogger(HippoHttpUtils.class);

  public static boolean sendVerificationToken(int token, String firstName, String email) {
    //Client client = Client.create();
    boolean isMailSent = false;
    HttpAuthenticationFeature feature
        = HttpAuthenticationFeature.basicBuilder()
        .credentials("api", "key-9b6d30bbedea07334ebb9ad972cb58f1").build();

    ClientConfig config = new ClientConfig();
    config.register(feature);
    JerseyClient client = JerseyClientBuilder.createClient(config);

    JerseyWebTarget target = client.target("https://api.mailgun.net/v2/sandbox49f46b595832462381bb61ada09f6e38.mailgun.org/messages");


    Form form = new Form();
    form.param("from","shakti@ofcampus.com");
    form.param("to",email);
    form.param("subject","Welcome to ofcampus");
    String text = "Congratulations "+firstName+ "! you have just joined Ofcampus!" + " Your verification token is "+token;
    form.param("text",text);


    Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED).post(Entity.form(form));
    if (response.getStatus() == HttpStatus.OK.value()) {
      isMailSent = true;
    } else {
      logger.error("Couldn't send verification mail : status code "+response.getStatus());
    }
    return isMailSent;
  }


  public static boolean sendReGenVerificationMail(int token, String firstName, String email) {
    boolean isMailSent = false;
    HttpAuthenticationFeature feature
        = HttpAuthenticationFeature.basicBuilder()
        .credentials("api", "key-9b6d30bbedea07334ebb9ad972cb58f1").build();

    ClientConfig config = new ClientConfig();
    config.register(feature);
    JerseyClient client = JerseyClientBuilder.createClient(config);

    JerseyWebTarget target = client.target("https://api.mailgun.net/v2/sandbox49f46b595832462381bb61ada09f6e38.mailgun.org/messages");


    Form form = new Form();
    form.param("from","shakti@ofcampus.com");
    form.param("to",email);
    form.param("subject","Verification Token - OfCampus");
    String text = "Welcome Again "+firstName+ "! Your verification token is "+token;
    form.param("text",text);


    Response response = target.request(MediaType.APPLICATION_FORM_URLENCODED).post(Entity.form(form));
    if (response.getStatus() == HttpStatus.OK.value()) {
      isMailSent = true;
    } else {
      logger.error("Couldn't send verification mail : status code "+response.getStatus());
    }
    return isMailSent;
  }

  public static void main(String[] args) {
    sendVerificationToken(1,"shakti", "shakti@ofcampus.com");
  }
}

