package models;

import java.util.Date;
public class MiniComputador extends Computador{
    public MiniComputador(String name,String manufacturer,Date year,float price){
        super(name,"MiniComputador",manufacturer,year,price);
    }
}