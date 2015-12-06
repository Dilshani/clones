package com.extrogene.model;

import com.extrogene.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBControler {
    Util util;
    Connection con;
    public void setMovieLogs(String title,int year,String imdb,String genres,String director,String writer,String actor,String hash){
        util = new Util();
        try {
            con = util.GetConnection();
            String query = "INSERT INTO movie_logs ( title,year,imdb_id,genres,directors,writers,actors,hash ) VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement preSt = con.prepareStatement(query);
            preSt.setInt(3, year);
            preSt.setString(3, imdb);
			//preSt.setString(3, imdb);
            preSt.setString(4, genres);
            preSt.setString(5, director);
            preSt.setString(6, writer);
            preSt.setString(7, actor);
            preSt.setString(8, hash);
			//preSt.setString(8, hash);
			//preSt.setString(8, hash);
			//preSt.setString(8, hash);
			//preSt.setString(8, hash);
            
            preSt.execute();
            
        } catch (SQLException ex) {
            System.out.print(ex);
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void setBestMovie(int id,String title,int year,String imdb,String genres,String director,String writer,String producer,String actor){
        util = new Util();
        try {
            con = util.GetConnection();
            String query = "INSERT INTO best_movies (id, title,year,imdb_id,genres,directors,writers,producers,actors ) VALUES (?,?,?,?,?,?,?,?,?);";
            PreparedStatement preSt = con.prepareStatement(query);
            preSt.setInt(1, id);
            preSt.setString(2, title);
            preSt.setInt(3, year);
            preSt.setString(4, imdb);
            preSt.setString(5, genres);
            preSt.setString(6, director);
            preSt.setString(7, writer);
            preSt.setString(8, producer);
            preSt.setString(9, actor);
			//preSt.setString(9, actor);
            
            preSt.execute();
            
        } catch (SQLException ex) {
            System.out.print(ex);
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public String getMovieList(){
        util = new Util();
        String res = "";
        try{
            con = util.GetConnection();
            String query = "SELECT * FROM best_movies";
            PreparedStatement preSt = con.prepareStatement(query);
            ResultSet rs = preSt.executeQuery();
            int  i=0;
            while(rs.next()){
                i++;
                res = res +i+"."+rs.getString("title")+" - "+rs.getString("year")+"\n";
            }
            rs.close();
            
        }catch (SQLException ex) {
            
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        return res;
    }
    public String getSelectedMovieList(int input){
        util = new Util();
        String res = "No data found";
        try{
            con = util.GetConnection();
            String query = "SELECT * FROM best_movies WHERE id = ?";
            PreparedStatement preSt = con.prepareStatement(query);
            preSt.setInt(1, input);
            ResultSet rs = preSt.executeQuery();
            while(rs.next()){
                res = rs.getString("title")+
                             "\nYear : "+rs.getString("year")+
                             "\nIMDB : "+rs.getString("imdb_id")+"("+rs.getString("votes")+")"+
                             "\nGenre : "+rs.getString("genres")+                             
                             "\nDir : "+rs.getString("directors")+
                             "\nWri : "+rs.getString("writers")+
                             "\nActors : "+rs.getString("actors");
            }
            rs.close();
            
        }catch (SQLException ex) {
            String error = "error";
        } finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } 
        
        return res;
    }
    public void setTvshowLogs(String title,String year,String imdb,String genres,String director,String writer,String actor,String hash){
        util = new Util();
//added new line
        try {
           
            String query = "INSERT INTO tvshow_logs ( title,year,imdb_id,genres,directors,writers,actors,hash ) VALUES (?,?,?,?,?,?,?,?);";
            PreparedStatement preSt = con.prepareStatement(query);
            preSt.setString(1, title);
            preSt.setString(2, year);
            preSt.setString(3, imdb);
            preSt.setString(4, genres);
            preSt.setString(5, director);
            preSt.setString(6, writer);
            preSt.setString(7, actor);
            preSt.setString(8, hash);
            
            preSt.execute();
            
        } catch (SQLException ex) {
            System.out.print(ex);
        }finally{
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(DBControler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
