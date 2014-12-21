package com.potaliadmin;
/**
 * Created by Shakti Singh on 12/16/14.
 */
public class Test {

  public static void main(String[] args) {
    int i =12345;

    System.out.println("Number is : "+i);

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
    Test test = new Test();
    System.out.println(test.fact(100));


  }


  public Float fact(int num) {
    if (num == 1) {
      return 1F;
    } else {
      return fact(num-1)*num;
    }

  }
}
