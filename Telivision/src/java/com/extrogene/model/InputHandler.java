package com.extrogene.model;

/**
 *
 * @author sandun
 */
public class InputHandler {
    public String setSearchText(String input){
        String result="";
        String[] parts = input.split(" ");
        for(int i=0;i<parts.length;i++){
            result = result+parts[i]+"+";
        }
        return result;
    }
}
