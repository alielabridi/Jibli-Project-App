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
public class Journey {
   private int journeyID;
   private String date;
   private String dest;
   private String depar;
   private String bringerId;

   public String getDate() {
      return date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public Journey( String date, String dest, String depar, String bringerId) {
      this.date = date;
      this.dest = dest;
      this.depar = depar;
      this.bringerId = bringerId;
   }

  

   public int getJourneyID() {
      return journeyID;
   }

   public void setJourneyID(int journeyID) {
      this.journeyID = journeyID;
   }

   public String getBringerId() {
      return bringerId;
   }

   public void setBringerId(String bringerId) {
      this.bringerId = bringerId;
   }



   public String getDest() {
      return dest;
   }

   public void setDest(String dest) {
      this.dest = dest;
   }

   public String getDepar() {
      return depar;
   }

   public void setDepar(String depar) {
      this.depar = depar;
   }

   @Override
   public String toString() {
      return "Journey{" + "date=" + date + ", dest=" + dest + ", depar=" + depar + '}';
   }
   
   
   
   
   
}
