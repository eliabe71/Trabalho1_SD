//package server;
import models.*;
import database.*;
import java.net.*;
import java.io.*;
import java.util.Date;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.util.ArrayList;
public class Laboratorio implements Estoque{
    Database db;
    Connection cnc;
    Statement stmt;
    public Laboratorio(){
        this.db = new Database();
        this.cnc = db.getConnection();
        try {
            this.stmt = cnc.createStatement();  
            this.stmt.setQueryTimeout(30);
        } catch (Exception e) {
            
        }
        
    }
    @Override
    public void remove(ArrayList<Integer> ids){
        try {
            for( int i =0 ; i < ids.size() ; i++){
                int id = (int)ids.get(i);
                stmt.executeUpdate("delete from Laboratorio where id=" + id);
                System.out.println("Computador do id : "+id+ " Removido Com Sucesso" );
            }
            
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override
    public ArrayList<String> getEstoque(){
        try {
            ArrayList<String> np = new ArrayList<String>();
            ResultSet rs = this.stmt.executeQuery("select * from Laboratorio");
            String p = new String();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String model = rs.getString("model");
                String manu = rs.getString("manufacturer");
                float price = rs.getFloat("price"); 
                String year = rs.getString("year");
                p = "{ \"ID\":" +id+","+" \" Name \": "+"\""+name+"\""+","+"\" Model\": "+"\""+model+"\", "+"\"Manufacturer\" : \""+manu +"\", \"Price\": "+price+ ", \"Year\" :"+"\""+year+"\""+"}";
                np.add(p);
            }   
            return np;
        } catch (Exception e) {
            return null;    
        }
        
    }
    @Override
    public void add(Computador c){
        String nome = c.getName();
        String model = c.getModel();
        String year = c.getYear().toString();
        float price = c.getPrice();
        String manufacturer = c.getManufacturer();
        try {
            stmt.execute("insert into Laboratorio(name,model,year,price,manufacturer) values('"+nome+"',"+"'"+model+"',"+"'"+year+"',"+price+",'"+manufacturer+"')");    
        } catch (Exception e) {
            
        }
        
    }
    public static String[] jsonToData(String in){
        in = in.replace("{", "");
        in = in.replace("}", "");
        in = in.trim();
        String[] output = in.split(",");
        for(int i =0 ; i < output.length ; i++){
            output[i] = output[i].split(":")[1];
            output[i] = output[i].trim();
            if(output[i].contains("\"")){
                output[i] = output[i].replace("\"", "");
                output[i] = output[i].trim();
            }
        }

        return output;
    }
    public static Computador createComputador(String[] at){
        try {
            int id = Integer.parseInt(at[0]);
            String name= at[1];
            String model= at[2];
            String manu = at[3];
            float price = Float.parseFloat(at[4]);
            String year = at[5];
            Computador nc = new Computador(name,model,manu, (new Date(year)),price);
            nc.setID(id);
            return nc;
        } catch (Exception e) {
            return null;
        }
    }
    public static void clear(){
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");            
        } catch (Exception e) {
            
        }

    }
    public static void main(String[] args){
        Laboratorio lab = new Laboratorio();
        byte[] test = new byte[1000];
        Scanner entrada = new Scanner(System.in);

        Socket cliente = null;
        try {
            while(true){
                cliente = new Socket("localhost", 8080);
                System.out.println("Para Adquirir um Computador Digite 1");
                System.out.println("Para ver o Estoque No Laboratorio Digite Digite 3");
                System.out.println("Para Remover um computador do Estoque do Laboratorio Digite Digite 4");
                System.out.println("Sair Digite 2");
                int i = entrada.nextInt();
                if( i == 2 ) break;
                if( i == 3){
                    ArrayList<String> comp = new ArrayList<String>();
                    comp = lab.getEstoque();
                    if( comp.size() ==0 ){
                        System.out.println("Não Existe Computadores No Laboratorio");
                    }
                    clear();
                    for(int ii =0 ; ii < comp.size() ; ii++){
                        System.out.println(createComputador(jsonToData((String)comp.get(ii))));
                    }
                }
                if(i == 4){
                    clear();
                    ArrayList<String> comp = new ArrayList<String>();
                    comp = lab.getEstoque();
                    if( comp.size() ==0 ){
                        System.out.println("Não Existe Computadores No Laboratorio");
                    }
                    clear();
                    ArrayList<Integer> remover = new ArrayList<Integer>();
                    for(int ii =0 ; ii < comp.size() ; ii++){
                        System.out.println(createComputador(jsonToData((String)comp.get(ii))));
                        System.out.println("Deseja Remover Esse Computador Digete 1 para sim 2 para não");
                        int remove = entrada.nextInt();
                        
                        if(remove == 1){
                            remover.add(createComputador(jsonToData((String)comp.get(i))).getID());
                        }
                    }
                    lab.remove(remover);

                }
                if(i==1){
                    String request = "{\"Request \": \"computers\" }";
                    cliente.getOutputStream().write(request.getBytes());
                    cliente.getInputStream().read(test);
                    request = new String(test);
                    request = request.replace("{", "");
                    request = request.replace("}", "");
                    request = request.trim();
                    request = request.split(":")[1];
                    request = request.trim();
                    if(request.contains("\"Error\"")){
                        System.out.println("A Fabrica Nao Possui Computadores Disponiveis");
                        continue;
                    }
                    if(request.equals("200")){
                        InputStream en = cliente.getInputStream();
                        char buffer = (char)en.read(); 
                        while(buffer != ':'){
                            buffer = (char)en.read();

                        }
                        while(buffer != '{' && buffer != '[' ){           
                            buffer = (char)en.read();
                        }                 
                        if(buffer == '['){
                            ArrayList<String> comp = new ArrayList<String>();
                            while(buffer!= ']'){
                                if(buffer == '{' ){
                                    String icomp = new String();
                                    while(buffer !=  '}'){
                                        icomp=icomp+buffer;
                                        buffer = (char)en.read();
                                    }
                                    icomp=icomp+buffer;
                                    comp.add(icomp);
                                   // System.out.println(icomp);    
                                }
                                
                                buffer = (char)en.read();
                            }
                            ArrayList<Computador> comps = new ArrayList<Computador>();
                            for(int ii =0 ; ii < comp.size() ; ii++){
                                String entrada2 = (String)comp.get(ii);
                                //System.out.println(entrada2);
                                String[] infos = jsonToData(entrada2);
                                comps.add(createComputador(infos));
                            }
                            ArrayList<Computador> compstrash = new ArrayList<Computador>();
                            for(int c = 0 ; c < comps.size() ; c++){
                                Computador oc = (Computador)comps.get(c);
                                System.out.println(oc);
                                System.out.println("Deseja Adcionar esse Computador Ao Seu Laboratorio Digite 1 Para Sim e 2 Para Nao");
                                int sim = entrada.nextInt();
                                if(sim == 1){
                                    compstrash.add(oc);
                                    lab.add(oc);
                                    comps.remove(oc);
                                    c--;
                                }
                            }
                            clear();
                            if(compstrash.size() <= 0) {
                                String req = "{\"Response \": \"404\"}";
                                cliente.getOutputStream().write(req.getBytes());
                            } 
                            else{
                                String req = "{\"Response \": [";
                                for(int c = 0 ;c<compstrash.size() ; c++ ){
                                    
                                    Computador oc = (Computador)compstrash.get(c);
                                    if( c == compstrash.size()-1){
                                        req = req+oc.getID()+"]}";
                                    }
                                    if(compstrash.size()> 1 && c >=0  ){
                                        req = req+oc.getID()+",";
                                    }
                                }
                                //System.out.println(req);
                                cliente.getOutputStream().write(req.getBytes()); 
                            }
                        } 
                       else if(buffer == '{') {
                            String com = new String();
                            while(buffer != '}'){
                                com = com+buffer;
                                buffer = (char)en.read();
                            }
                            com = com+buffer; 
                            String[] infos = jsonToData(com);
                            Computador c1 = createComputador(infos);
                            System.out.println(c1);
                            System.out.println("Deseja Adcionar esse Computador Ao Seu Laboratorio Digite 1 Para Sim e 2 Para Nao");
                            int sim = entrada.nextInt();
                            if(sim ==1 ){
                                String req= "{\"Response \": [ "+ c1.getID()+"]}";
                                cliente.getOutputStream().write(req.getBytes());
                            }
                            else {
                                String req = "{\"Response \": \"404\"}";
                                cliente.getOutputStream().write(req.getBytes());
                            }
                            
                            clear();
                            continue; 
                        }
                        
                    }
                }           
                else{
                    //;//clear();
                }
            }
        } catch (Exception e) {
            
        }   
    }

};