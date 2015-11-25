/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.extrogene;

import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.StatusCodes;
import hms.kite.samples.api.ussd.MoUssdListener;
import hms.kite.samples.api.ussd.UssdRequestSender;
import hms.kite.samples.api.ussd.messages.MoUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdReq;
import hms.kite.samples.api.ussd.messages.MtUssdResp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

import com.extrogene.controller.MenuHandler;
import com.extrogene.controller.MovieControler;
import com.extrogene.controller.SubscriptionController;
import com.extrogene.controller.TVSeriesControler;
import com.extrogene.model.AppConstant;
import com.extrogene.model.DBControler;
import com.extrogene.model.User;

/**
 *
 * @author sandun
 */
public class USSDReceiver implements MoUssdListener{

    public static final ConcurrentHashMap<String , User > sessions = new ConcurrentHashMap<String, User>();
    private static final String USSD_OP_MO_INIT = "mo-init";
    private static final String USSD_OP_MO_CONT = "mo-cont";
    private static final String USSD_OP_MT_FIN = "mt-fin";
    private static final String USSD_OP_MT_CONT = "mt-cont";
    private MtUssdReq request;
    private UssdRequestSender ussdMtSender;
    private MenuHandler menuHandller;
    private SubscriptionController subscriptionController;
    private MovieControler movieControler;
    private TVSeriesControler tvControler;
    
    DBControler objDB = new DBControler();
    
    @Override
    public void init() {
        try {
            if(AppConstant.ISPRODUCT) {
                ussdMtSender = new UssdRequestSender(new URL("http://api.dialog.lk:8080/ussd/send"));
            }else {
                ussdMtSender = new UssdRequestSender(new URL("http://127.0.0.1:7000/ussd/send"));
            }
        } catch (MalformedURLException e) {
            
        }
        menuHandller = new MenuHandler();
        movieControler = new MovieControler();
        tvControler = new TVSeriesControler();
        subscriptionController = new SubscriptionController();
    }

    @Override
    public void onReceivedUssd(MoUssdReq moUssdReq) {        
        User user = null;        
        if(moUssdReq.getUssdOperation().equals(USSD_OP_MO_INIT)){            
            user = new User();
            if(subscriptionController.checkSubscription(moUssdReq.getSourceAddress())){
                user.setUssdOperation(USSD_OP_MT_CONT);
                user.setUpdatedTime(System.currentTimeMillis());
                user.setMenuContent(menuHandller.createMainMenu());
            }
            else{
                subscriptionController.subscribeUser(moUssdReq.getSourceAddress());
                if(subscriptionController.checkSubscription(moUssdReq.getSourceAddress())){
                    user.setUssdOperation(USSD_OP_MT_CONT);
                    user.setUpdatedTime(System.currentTimeMillis());
                    user.setMenuContent(menuHandller.createMainMenu());
                }else{
                    user.setUssdOperation(USSD_OP_MT_FIN);
                    user.setUpdatedTime(System.currentTimeMillis());
                    user.setMenuContent("MoviePlus\nWait confirmation msg from dialog and try again.");
                }
            }            
            sessions.put(moUssdReq.getSourceAddress() , user);            
        }
        
        else{
            user = sessions.get(moUssdReq.getSourceAddress());
            user.setUpdatedTime(System.currentTimeMillis());
            String userInput="0";
            try{
                userInput = moUssdReq.getMessage().toString();
                
                switch(user.getFirstMenuSelection()){
                    case "0":
                        switch(userInput){
                            case "1":
                                user.setMenuContent("Movies\n1. Top Movies\n2. Search Movies\n0. Main Menu");
                                user.setFirstMenuSelection(userInput);
                                break;                                
                            case "2":
                                user.setMenuContent("Enter the name of the TV show to search\n\n0. Main Menu");
                                user.setFirstMenuSelection(userInput);
                                break;                                
                            default :
                                user.setMenuContent("MoviePlus\nSorry Invalid Input");
                                user.setUssdOperation(USSD_OP_MT_FIN);
                                break;
                        }
                        break;
                    case "1":
                        switch(user.getSecondMenuSelection()){
                            case "0":
                                if(userInput.equalsIgnoreCase("0")){
                                    user.setFirstMenuSelection("0");
                                    user.setSecondMenuSelection("0");
                                    user.setThirdMenuSelection("0");
                                    user.setMenuContent(menuHandller.createMainMenu());
                                }else if(userInput.equalsIgnoreCase("1")){
                                    String resBestMovies = objDB.getMovieList();
                                    user.setMenuContent("Top Movies \n"+resBestMovies+"\n0.Main Menu");
                                    user.setSecondMenuSelection(userInput);
                                }else if(userInput.equalsIgnoreCase("2")){
                                    user.setMenuContent("Enter the name of the Movie to search\n\n0.Main Menu");
                                    user.setSecondMenuSelection(userInput);
                                }else{
                                    user.setMenuContent("MoviePlus\nSorry Invalid Input.");
                                    user.setUssdOperation(USSD_OP_MT_FIN);
                                }                                
                                break;                                    
                            case "1":
                                if(userInput.equalsIgnoreCase("0")){
                                    user.setFirstMenuSelection("0");
                                    user.setSecondMenuSelection("0");
                                    user.setThirdMenuSelection("0");
                                    user.setMenuContent(menuHandller.createMainMenu());
                                }else{
                                    int bestMovie = 1;
                                    try{
                                        bestMovie = Integer.parseInt(userInput);
                                    }catch(Exception e){
                                        
                                    }
                                    String resSelectedMovie = objDB.getSelectedMovieList(bestMovie);
                                    user.setMenuContent("Top movies\n"+resSelectedMovie+"\n0.Main Menu");
                                    
                                }
                                break;
                            case "2":
                                switch(user.getThirdMenuSelection()){
                                    case "0":
                                        if(userInput.equalsIgnoreCase("0")){
                                            user.setFirstMenuSelection("0");
                                            user.setSecondMenuSelection("0");
                                            user.setThirdMenuSelection("0");
                                            user.setMenuContent(menuHandller.createMainMenu());
                                        }else{
                                            String moviesearchMenu = movieControler.searchMovies(userInput,moUssdReq);
                                            user.setMenuContent("Result\n"+moviesearchMenu+"\n0.Main Menu"); 
                                            user.setThirdMenuSelection("1");
                                        }
                                        break;
                                        
                                    case "1":
                                        if(userInput.equalsIgnoreCase("0")){
                                            user.setFirstMenuSelection("0");
                                            user.setSecondMenuSelection("0");
                                            user.setThirdMenuSelection("0");
                                            user.setMenuContent(menuHandller.createMainMenu());
                                        }else{
                                            user.setUssdOperation(USSD_OP_MT_FIN);
                                            String movieDetails = movieControler.searchMovieDetails(moUssdReq, userInput , moUssdReq.getSourceAddress());
                                            user.setMenuContent(movieDetails+"\n0.Main Menu");
                                        }
                                        break;   
                                }
                                break;                                   
                        }
                        break;    
                    case "2":                        
                        switch(user.getSecondMenuSelection()){
                            case "0":
                                if(userInput.equalsIgnoreCase("0")){
                                    user.setFirstMenuSelection("0");
                                    user.setSecondMenuSelection("0");
                                    user.setThirdMenuSelection("0");
                                    user.setMenuContent(menuHandller.createMainMenu());
                                }else{
                                    String tvsearchMenu = tvControler.searchTVSeries(userInput,moUssdReq);
                                    user.setMenuContent("Result\n"+tvsearchMenu+"\n0.Main Menu"); 
                                    user.setSecondMenuSelection("1");
                                }
                                break;
                                        
                            case "1":
                                if(userInput.equalsIgnoreCase("0")){
                                    user.setFirstMenuSelection("0");
                                    user.setSecondMenuSelection("0");
                                    user.setThirdMenuSelection("0");
                                    user.setMenuContent(menuHandller.createMainMenu());
                                }else{
                                    user.setUssdOperation(USSD_OP_MT_FIN);
                                    String tvDetails = tvControler.searchTVDetails(moUssdReq , userInput ,moUssdReq.getSourceAddress() );
                                    user.setMenuContent(tvDetails+"\n0.Main Menu");
                                }
                                break;       
                        }
                        break;  
                }                
            }catch(NumberFormatException ex){ 
                user.setMenuContent("MoviePlus\nSorry Invalid Input");
                user.setUssdOperation(USSD_OP_MT_FIN);
            }
            sessions.put(moUssdReq.getSourceAddress(), user);
        }
        
        MtUssdReq mtUssdReq = createRequest(moUssdReq, user.getMenuContent(), user.getUssdOperation() );
        
        try {
            sendRequest(mtUssdReq);
        } catch (SdpException ex) {
            //Logger.getLogger(UssdReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }   
    
    private MtUssdResp sendRequest(final MtUssdReq request) throws SdpException {
        MtUssdResp response = null;
        try {
            response = ussdMtSender.sendUssdRequest(request);
        } catch (SdpException e) {
            throw e;
        }

        String statusCode = response.getStatusCode();
        String statusDetails = response.getStatusDetail();
        if (StatusCodes.SuccessK.equals(statusCode)) {
        } else {
        }
        return response;
    }

    private MtUssdReq createRequest(final MoUssdReq moUssdReq, final String menuContent, final String ussdOperation) {
        final MtUssdReq request = new MtUssdReq();
        request.setApplicationId(moUssdReq.getApplicationId());
        request.setEncoding(moUssdReq.getEncoding());
        request.setMessage(menuContent.replace("%", " percent"));
        request.setPassword(AppConstant.CUSUSSD_APP_PASSWORD);
        request.setSessionId(moUssdReq.getSessionId());
        request.setUssdOperation(ussdOperation);
        request.setVersion(moUssdReq.getVersion());
        request.setDestinationAddress(moUssdReq.getSourceAddress());
        return request;
    }    
}
