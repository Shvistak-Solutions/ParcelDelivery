package com.example.ParcelDelivery.ui.password_reset;

public class ForgotPasswordGenerator {
    int numberOfCharacters;
    private String smallLettersBox = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,y,z,";
    private String bigLettersBox = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,Y,Z,";
    private String specialCharsBox = "!,@,#,$,%,^,&,*,";
    private String numbersBox = "1,2,3,4,5,6,7,8,9,0,";
    private String box = "";
    private int boxLenght;

    public ForgotPasswordGenerator(int numberOfCharacters) {

        this.numberOfCharacters = numberOfCharacters;
        makeBox();
    }

    public ForgotPasswordGenerator() {

        this.numberOfCharacters = 6;
        makeBox();
    }

    public void makeBox() {

        this.box += this.smallLettersBox;                                    // suma zbioru znakow
        this.box += this.bigLettersBox;
        this.box += this.numbersBox;
        //this.box += this.specialCharsBox;
        this.box = this.box.substring(0, (this.box.lastIndexOf(",")));    // usuniecie przecinka
    }

    public String[] generateBox() {

        String[] boxArray = this.box.split(",");

        this.boxLenght = boxArray.length;
        return boxArray;
    }

    public String generateLetter() {
        int count = (int) ((this.boxLenght) * Math.random());

        return generateBox()[count];
    }

    public String generatePassword() {

        String password = "";

        for (int i = 0; i < this.numberOfCharacters; i++) {

            password += generateLetter();
        }

        return password;
    }
}
