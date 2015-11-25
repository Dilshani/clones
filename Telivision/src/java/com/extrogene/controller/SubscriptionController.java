/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.extrogene.controller;

import com.extrogene.model.AppConstant;
import hms.kite.samples.api.SdpException;
import hms.kite.samples.api.subscription.SubscriptionRequestSender;
import hms.kite.samples.api.subscription.messages.SubscriptionRequest;
import hms.kite.samples.api.subscription.messages.SubscriptionStatusRequest;
import hms.kite.samples.api.subscription.messages.SubscriptionStatusResponse;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sanjeewa
 */
public class SubscriptionController {
        public boolean checkSubscription(String userHash){
        boolean isSubscribed = false;
        
        SubscriptionStatusRequest subscriptionStatusRequest = new SubscriptionStatusRequest();
        subscriptionStatusRequest.setApplicationId(AppConstant.APP_ID);
        subscriptionStatusRequest.setPassword(AppConstant.CUSUSSD_APP_PASSWORD);
        subscriptionStatusRequest.setSubscriberId(userHash);
        
        try {
            SubscriptionRequestSender subscriptionRequestSender;
            if(AppConstant.ISPRODUCT){
                subscriptionRequestSender = new SubscriptionRequestSender(new URL("http://api.dialog.lk:8080/subscription/getStatus"));
            }else{
                subscriptionRequestSender= new SubscriptionRequestSender(new URL("http://127.0.0.1:7000/subscription/getStatus")); 
            }
            
            SubscriptionStatusResponse subscriptionStatus = subscriptionRequestSender.sendSubscriptionStatusRequest(subscriptionStatusRequest);
            
            if("S1000".equals(subscriptionStatus.getStatusCode())){
                if("REGISTERED".equals(subscriptionStatus.getSubscriptionStatus())){
                    isSubscribed = true;
                }else{
                    isSubscribed = false;
                }
            }else{
                isSubscribed = true;
            }
        } catch (SdpException ex) {
            Logger.getLogger(SubscriptionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SubscriptionController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            return isSubscribed;
        }
    }
    
    public void subscribeUser(String userHash){
        SubscriptionRequest subscriptionRequest=new SubscriptionRequest();
        subscriptionRequest.setApplicationId(AppConstant.APP_ID);
        subscriptionRequest.setPassword( AppConstant.CUSUSSD_APP_PASSWORD );
        subscriptionRequest.setSubscriberId( userHash );
        subscriptionRequest.setAction("1");
        
        try {
            SubscriptionRequestSender subscriptionRequestSender;
            if(AppConstant.ISPRODUCT){
                subscriptionRequestSender = new SubscriptionRequestSender(new URL("http://api.dialog.lk:8080/subscription/send"));
            }else{
                subscriptionRequestSender= new SubscriptionRequestSender(new URL("http://127.0.0.1:7000/subscription/send")); 
            }
            subscriptionRequestSender.sendSubscriptionRequest(subscriptionRequest);
            
        } catch (SdpException ex) {
            Logger.getLogger(SubscriptionController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(SubscriptionController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
