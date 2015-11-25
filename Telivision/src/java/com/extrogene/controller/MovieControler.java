package com.extrogene.controller;

import com.extrogene.model.DBControler;
import com.extrogene.model.InputHandler;
import com.extrogene.model.User;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author sandun
 */
public class MovieControler {
    InputHandler inputHandle = new InputHandler();
    public static final ConcurrentHashMap<String , JSONArray > searchHistorySessions = new ConcurrentHashMap<String, JSONArray>();
    String searchTitle = "";
    DBControler objDB = new DBControler();

    public String searchMovies(String movietitle,MoUssdReq moUssdReq){
        String searchResultMenu = "";
        searchTitle = inputHandle.setSearchText(movietitle);
        Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, "searching");
        String apiUrl = "http://api.trakt.tv/search/movies.json/db13df10cd1d0e70712bec679e6c93f5?query="+searchTitle;
        JSONArray data;
        try{
                URLConnection connection = new URL(apiUrl).openConnection();
                Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, "connecting to api");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 2048 * 16);
                StringBuffer builder = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                  builder.append(line).append("\n");
                }
                String blah = builder.toString();

                //Parsing string into JSONArray
                data = new JSONArray(new String(blah));
                searchHistorySessions.put(moUssdReq.getSourceAddress(), data);
                for(int i=0; i < 5; i++)
                {
                     JSONObject object = data.getJSONObject(i);
                     String title = object.getString("title"); 
                     String years = "N/A";
                     try{
                         years =""+object.getInt("year");
                     }catch(Exception e){
                     }
                     searchResultMenu = searchResultMenu +(i+1)+". "+ title+" - "+years+"\n";
                }
            } 
        catch(Exception e){
                System.out.println("Movie name is not correct..!");
                Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, e);
        }
        return searchResultMenu;
    }
    public String searchMovieDetails(MoUssdReq moUssdReq,String input,String hash){
     
        JSONArray data = searchHistorySessions.get(moUssdReq.getSourceAddress());
        String imdbID="";
        String result = "";
        JSONObject object;
        int inpt = 0;
        try {
            inpt = Integer.parseInt(input);
            //for(int i=0; i < inpt; i++)
            //{                
                object = data.getJSONObject(inpt);                                
                imdbID = object.getString("imdb_id"); 
            //}
            result = getsearchMovieDetails(imdbID,hash);

        }
        catch (JSONException ex) {
            Logger.getLogger(MovieControler.class.getName()).log(Level.SEVERE, null, ex);
            try{
            object = data.getJSONObject(inpt);                                
            result = object.getString("title")+"\nYear : "+
                    object.getInt("year")+"\nCountry : "+
                    object.getString("country")+"\nRuntime : "+
                    object.getInt("runtime");
                    //object.getString("genres");
                    //object.getString("genres");
            }catch(Exception e){
                result = "No Movie data found";
            }
        }        
        return result;
    }
    public String getsearchMovieDetails(String imdbID,String hash){
        
        String searchResult = "";
        String apiUrl = "http://www.omdbapi.com/?i="+imdbID+"&plot=full&r=json";//http://api.trakt.tv/movie/summary.json/db13df10cd1d0e70712bec679e6c93f5/"+imdbID;
        try{
                URLConnection connection = new URL(apiUrl).openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()), 2048 * 16);
            StringBuffer builder = new StringBuffer();
                String line;

                while ((line = reader.readLine()) != null) {
                  builder.append(line).append("\n");
                }
                String blah = builder.toString();

                //Parsing string into JSONArray
                JSONArray data = new JSONArray("["+blah+"]");
                for(int i=0; i < 1; i++)
                {
                     JSONObject object = data.getJSONObject(i);                     
                     searchResult = object.getString("Title")+
                             "\nYear : "+object.getString("Year")+
                             "\nIMDB : "+object.getString("imdbRating")+"("+object.getString("imdbVotes")+")"+
                             "\nReleased : "+object.getString("Released")+
                             "\nGenre : "+object.getString("Genre")+                             
                             "\nDir : "+object.getString("Director")+
                             "\nWri : "+object.getString("Writer")+
                             "\nActors : "+object.getString("Actors");
                     
                     objDB.setMovieLogs(object.getString("Title"), Integer.parseInt(object.getString("Year")), object.getString("imdbRating"), object.getString("Genre"), object.getString("Director"), object.getString("Writer"),object.getString("Actors"), hash);
                }
            } 
        catch(Exception e){
                System.out.println("film name is not correct..!");
                searchResult = "0";
        }
        return searchResult;
    }
    
}
