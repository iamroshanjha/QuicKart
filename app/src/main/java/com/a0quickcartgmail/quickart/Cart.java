package com.a0quickcartgmail.quickart;



public class Cart {
    public String pid;
    public String pname;
    public int pprice;
    public int pquantity;

    public Cart() {

    }

    public Cart(String pid, String pname, int pprice, int pquantity) {
        this.pid = pid;
        this.pname = pname;
        this.pprice = pprice;
        this.pquantity=pquantity;
    }

    public String getPid(){return pid;}
    public String getPname(){return pname;}
    public int getPprice(){return pprice;}
    public int getPquantity(){return pquantity;}

    public int getTotal(){

        int total;
        total=pprice*pquantity;
        return total;}

}

