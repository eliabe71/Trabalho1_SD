//package server;
import models.*;
import database.*;
import java.net.*;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
public class Fabrica implements Estoque{
    Database db;
    Connection cnc;
    Statement stmt;
    public Fabrica(){
        this.db = new Database();
        this.cnc = db.getConnection();
        try {
            this.stmt = cnc.createStatement();
            this.stmt.setQueryTimeout(30);    
        } catch (Exception e) {
            
        }
        
    }
    @Override
    public ArrayList<String> getEstoque(){
        try {
            ArrayList<String> np = new ArrayList<String>();
            ResultSet rs = this.stmt.executeQuery("select * from Estoque");
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
    public void remove(ArrayList<Integer> ids){
        try {
            for( int i =0 ; i < ids.size() ; i++){
                int id = (int)ids.get(i);
                stmt.executeUpdate("delete from Estoque where id=" + id);
                System.out.println("Computador do id : "+id+ "Removido Com Sucesso" );
            }
            
        } catch (Exception e) {
            System.out.println(e);
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
            stmt.execute("insert into Estoque(name,model,year,price,manufacturer) values('"+nome+"',"+"'"+model+"',"+"'"+year+"',"+price+",'"+manufacturer+"')");    
            System.out.println("Computador do id : "+c.getID()+ "Adcionado Com Sucesso" );
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }
    public static ArrayList<Integer> ids(String ids){
        ArrayList<Integer> idsI = new ArrayList<Integer>();
        String nid = new String();
        char[] arrayid = ids.toCharArray();
        int i=0;
        while(arrayid[i] != '['){
            i++;
        }
        i++;
        while(arrayid[i] != ']'){
            nid = nid+arrayid[i];
            i++;
        }
        nid =nid.trim();
        
        if(nid.contains(",")){
            String[] n = nid.split(",");
            for(int j = 0 ; j < n.length ; j++  ){
                try {
                    int nadd = Integer.parseInt(n[j].trim());
                    idsI.add(nadd);
                } catch (Exception e) {
                       System.out.println(e);
                       return null;
                }
            }
        }
        try {
            int nadd = Integer.parseInt(nid.trim());
            idsI.add(nadd);
        } catch (Exception e) {
               System.out.println(e);
               return null;
        }        
        return idsI;
    }
    public static void main(String[] args){
        ServerSocket ss= null;
        InputStream is = null;
        byte[] test = new byte[10000];
        float ew = 1020;
        // Adcionando COmputadores Na bAse de dados //
        Notebook n1 = new Notebook("Apire2","SempToshiba",(new Date(2001,15,5)),ew);
        MainFrame n2 = new MainFrame("MAin","LG",(new Date(2001,15,5)),ew+34);
        MicroComputador n3 =new MicroComputador("Micro","LG",(new Date(2001,15,5)),ew+34);
        MiniComputador n4 = new MiniComputador("Mini23","Sansumg",(new Date(2001,15,5)),ew+34);

        Fabrica fab = new Fabrica();
        fab.add(n1);
        fab.add(n2);
        fab.add(n3);
        fab.add(n4);
        //==============================///// 

        try {
            ss = new ServerSocket(8080);
            while(true){
                Socket client = ss.accept();
                client.getInputStream().read(test);

                String request = new String(test);
                request = request.replace("{", "");
                request = request.replace("}", "");
                request = request.trim();
                request = request.split(":")[1];
                request = request.trim();
                request = request.replace("\"", "");
                request = request.trim();
                if(request.equals("computers")){
                    ArrayList ip = fab.getEstoque();
                    //System.out.println("Tamanho do Estoque" + ip.size());
                    if(ip.size() <= 0){
                        String p1 = "{\"Response\" : \"Error\"}";
                        client.getOutputStream().write(p1.getBytes());
                        continue;
                    }
                    String p1 = "{\"Response\" : 200}";
                    client.getOutputStream().write(p1.getBytes());
                    p1 = "{ \"Computers\" : ";
                    //ArrayList ip = fab.getEstoque();
                    if(ip.size() > 1){
                        p1 = p1+"[ ";
                    }
                    if(ip.size() == 1){
                        p1 = p1+"{ ";
                    }
                    client.getOutputStream().write(p1.getBytes());
                    for(int i =0 ; i < ip.size() ; i++){
                        String comp = (String)ip.get(i);
                        if(i == ip.size()-1){
                            client.getOutputStream().write(comp.getBytes());
                            continue;
                        }
                        if(ip.size() > 1 && i>=0 ){
                            comp = comp+",\n";
                            client.getOutputStream().write(comp.getBytes());
                        }
                    }
                    if(ip.size() == 1){
                        p1 = p1+"}}";
                        client.getOutputStream().write(p1.getBytes());
                    }
                    if(ip.size() > 1){
                        p1 = " ]}";
                        client.getOutputStream().write(p1.getBytes());
                    }

                }
                client.getInputStream().read(test);
                request = new String(test);

                if(request.contains("\"404\"")){
                    System.out.println("Cliente Nao Deseja Adquirir um Computador");
                } 
                else{
                    System.out.println(request);
                    fab.remove(ids(request));
                }              
                
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}