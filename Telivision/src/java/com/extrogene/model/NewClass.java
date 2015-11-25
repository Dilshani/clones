/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.extrogene.model;

import static com.extrogene.controller.MovieControler.searchHistorySessions;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author sandun
 */
public class NewClass {
    
    public static void main(String args[]){
        System.out.println("ela");
        NewClass obj=new NewClass();
        obj.searchMovies("sds");
    }
    public String searchMovies(String id){
        DBControler obj = new DBControler();
        String searchResultMenu = "";
        String apiUrl = "http://www.omdbapi.com/?i=tt0047478&plot=short&r=json";
        try{
                URLConnection connection = new URL(apiUrl).openConnection();
                JSONObject jsonObject=null;
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 2048 * 16);
                StringBuffer builder = new StringBuffer();
                String line;
                String blah="";
                while ((line = reader.readLine()) != null) {
                    blah = line.replace("[", "").replace("]", "");
                    jsonObject = new JSONObject(blah);   
                  //builder.append(line).append("\n");
                }
                //String blah = builder.toString();

                //Parsing string into JSONArray
                //JSONArray data = new JSONArray(new String(blah));
                for(int i=0; i < 1; i++)
                {
                     JSONObject object = jsonObject;
                     String title = object.getString("Title"); 
                     int year = Integer.parseInt(object.getString("Year")); 
                     String imdb= object.getString("imdbRating"); 
                     String genres= object.getString("Genre"); 
                     String director= object.getString("Director"); 
                     String writer= object.getString("Writer"); 
                     String producer= "p"; 
                     String actor= object.getString("Actors"); 
                     obj.setBestMovie(20,title,year ,imdb ,genres ,director ,writer ,producer ,actor);

                }
            } 
        catch(Exception e){
                System.out.println("film name is not correct..!");
        }
        return searchResultMenu;
    }
}
