/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.jibli.jibli;

import java.util.Date;

/**
 *
 * @author Mohamed
 */
public class Announce {
   
   private int announceId;
   private String product;
   private double price;
   private double profit;
   private String location;
   private String   postdatetime;
   private String comment;
   private String buyerId;


   public Announce(String product, double price, double profit, String location, String comment, String buyerId) {
      this.product = product;
      this.price = price;
      this.profit = profit;
      this.location = location;
      this.comment = comment;
      this.buyerId = buyerId;
   }

   public Announce(int announceId, String product, double price, double profit, String location, String postdatetime, String comment, String buyerId) {
      this.announceId = announceId;
      this.product = product;
      this.price = price;
      this.profit = profit;
      this.location = location;
      this.postdatetime = postdatetime;
      this.comment = comment;
      this.buyerId = buyerId;
   }

   public int getAnnounceId() {
      return announceId;
   }

   public void setAnnounceId(int announceId) {
      this.announceId = announceId;
   }

 

   public String getProduct() {
      return product;
   }

   public void setProduct(String product) {
      this.product = product;
   }

   public double getPrice() {
      return price;
   }

   public void setPrice(double price) {
      this.price = price;
   }

   public double getProfit() {
      return profit;
   }

   public void setProfit(double profit) {
      this.profit = profit;
   }

   public String getLocation() {
      return location;
   }

   public void setLocation(String location) {
      this.location = location;
   }

   public String getPostdatetime() {
      return postdatetime;
   }

   public void setPostdatetime(String postdatetime) {
      this.postdatetime = postdatetime;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }

   public String getBuyerId() {
      return buyerId;
   }

   public void setBuyerId(String buyerId) {
      this.buyerId = buyerId;
   }

   @Override
   public String toString() {
      return "Announce{" + "product=" + product + ", price=" + price + ", profit=" + profit + ", location=" + location + ", postdatetime=" + postdatetime + ", comment=" + comment + '}';
   }
   
   
   
}
