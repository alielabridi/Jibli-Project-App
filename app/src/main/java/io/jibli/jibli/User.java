/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.jibli.jibli;



/**
 *
 * @author Mohamed
 */
public class User {
   private String passWord;
   private String firstname;
   private String lastname;
   private String email;
   private String phoneNumber;
   private int rating;
   private double transact;



   public User(String passWord, String firstname,String lastname, String email, String phoneNumber) {
      this.passWord = passWord;
      this.lastname = lastname;
      this.firstname = firstname;
      this.email = email;
      this.phoneNumber = phoneNumber;
   }

   public String getPassWord() {
      return passWord;
   }

   public void setPassWord(String passWord) {
      this.passWord = passWord;
   }

   public String getLastname() {
      return lastname;
   }

   public void setLastname(String lastname) {
      this.lastname = lastname;
   }

   public String getFirstname() {
      return firstname;
   }

   public void setFirstname(String firstname) {
      this.firstname = firstname;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public int getRating() {
      return rating;
   }

   public void setRating(int rating) {
      this.rating = rating;
   }

   public double getTransact() {
      return transact;
   }

   public void setTransact(double transact) {
      this.transact = transact;
   }




  
   
}
