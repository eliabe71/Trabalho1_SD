package models;
import java.util.Date;
public class Computador{
    private int id;
    private String name;
    private String model;
    private String manufacturer;
    private Date year;
    private float price;
    public Computador(String name,String model, String manufacturer, Date year ,float price){
        this.name = name;
        this.model = model;
        this.manufacturer = manufacturer;
        this.year = year;
        this.price = price;
    }
    protected void setName (String name) {
        this.name= name;
    }
    protected void setModel(String model ){
        this.model =model;
    }
    protected void setManufacturer(String manufacturer){
        this.manufacturer = manufacturer;
    }
    protected void setYear(Date year){
        this.year = year;
    }
    protected void setPrice(float price){
        this.price = price;
    }
    public String getName() {
            return this.name;
    }
    public String getModel(){
        return this.model;
    }
    public String getManufacturer(){
        return this.manufacturer;
    }
    public Date getYear(){
        return this.year;
    }
    public float getPrice(){
        return this.price;
    }
    public int getID(){
        return id;
    }
    public void setID(int id){
        this.id = id;
    }

    @Override
    public String toString(){
        return "Nome : " + this.name +" Fabricante : "+this.manufacturer+" Modelo :"+model+" Ano :" +year+" Preco :"+price;
    }
}