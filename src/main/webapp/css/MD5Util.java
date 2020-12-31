// Available source: https://en.gravatar.com/site/implement/

import java.util.*;
import java.io.*;
import java.security.*;
public class MD5Util {
  public static String hex(byte[] array) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
      sb.append(Integer.toHexString((array[i]
          & 0xFF) | 0x100).substring(1,3));
      }
      return sb.toString();
  }
  public static String md5Hex (String message) {
      try {
      MessageDigest md =
          MessageDigest.getInstance("MD5");
      return hex (md.digest(message.getBytes("CP1252")));
      } catch (NoSuchAlgorithmException e) {
      } catch (UnsupportedEncodingException e) {
      }
      return null;
  }
  public static String getImgURL (String email){
      String hash = MD5Util.md5Hex(email);
      String url =  "https://www.gravatar.com/avatar/" + hash+"?d=identicon&s=200";
      return url;
  }

  public static void main(String[] args){
    String email = "derenlei@umail.ucsb.edu";
    String url = MD5Util.getImgURL(email);
    System.out.println(url);
  }
}
