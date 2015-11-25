/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.extrogene.model;

public class User {
    private String ussdOperation="0";
    private String menuContent="0";
    private String firstMenuSelection="0";
    private String secondMenuSelection="0";
    private String thirdMenuSelection="0";
    long updatedTime=0;
    
    public String getUssdOperation() {
        return ussdOperation;
    }

    public void setUssdOperation(String ussdOperation) {
        this.ussdOperation = ussdOperation;
    }

    public String getMenuContent() {
        return menuContent;
    }

    public void setMenuContent(String menuContent) {
        this.menuContent = menuContent;
    }

    public String getFirstMenuSelection() {
        return firstMenuSelection;
    }

    public void setFirstMenuSelection(String firstMenuSelection) {
        this.firstMenuSelection = firstMenuSelection;
    }

    public String getSecondMenuSelection() {
        return secondMenuSelection;
    }

    public void setSecondMenuSelection(String secondMenuSelection) {
        this.secondMenuSelection = secondMenuSelection;
    }

    public String getThirdMenuSelection() {
        return thirdMenuSelection;
    }

    public void setThirdMenuSelection(String thirdMenuSelection) {
        this.thirdMenuSelection = thirdMenuSelection;
    }

    public long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(long updatedTime) {
        this.updatedTime = updatedTime;
    }
        
}
