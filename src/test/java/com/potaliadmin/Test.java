package com.potaliadmin;

import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Shakti Singh on 12/16/14.
 */
public class Test {

  public static void main(String[] args) {

    //ContentDisposition contentDisposition = new ContentDisposition("image/png", "3_profile_susi.png",null,null,null ,495);

    /*ContentDisposition contentDisposition =
        FormDataContentDisposition.name("image/png").fileName("3_profile_susi.png").size(495).build();*/

    //System.out.println(contentDisposition.toString());

    Integer a = 0;
    System.out.println("Please enter a number");
    Scanner in = new Scanner(System.in);
    a = in.nextInt();

    boolean palindrome = false ;
    String num = a.toString();
    for (int i=0, j = num.length() - 1; i <= j ; i++, j--) {
      palindrome = (num.charAt(i) == num.charAt(j));
      if (!palindrome) break;
    }

    if (palindrome) {
      System.out.println("Number "+num + " is a palindrome");
    } else {
      System.out.println("Number "+num + " is a not palindrome");
    }




    //int i =12345;
    //char c = '%';
    //boolean isControl = Character.isISOControl(c);
    //System.out.println(isControl);

    //String str = "com.hk.exception.HealthkartPaymentGatewayException: Mandatory fields missing , response is: {\"status\":0,\"msg\":\"0 out of 1 Transactions Fetched Successfully\",\"transaction_details\":{\"1038069-329013\":{\"mihpayid\":\"Not Found\",\"status\":\"Not Found\"}}}";
    //str = str.replaceAll("[{]", "");
    //str = str.replaceAll("[}]","");
    //System.out.println(str);




    //System.out.println("Number is : "+i);

    /*//length of digits and sum of digits
    int length = 0;
    int sum = 0;
    int temp = i;
    int reverse = 0;

    do {
      int digit = temp%10;
      sum +=digit;
      length += 1;
      reverse = reverse*10+digit;
      temp = temp/10;
    } while(temp != 0);

    System.out.println("Length of digits is "+length);
    System.out.println("Sum of digits is "+sum);
    System.out.println("Reverse of number is "+reverse);*/
    //Test test = new Test();
    //System.out.println(test.fact(100));


  }


  public Float fact(int num) {
    if (num == 1) {
      return 1F;
    } else {
      return fact(num-1)*num;
    }

  }
}
