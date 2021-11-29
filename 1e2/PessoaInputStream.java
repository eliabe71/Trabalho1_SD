import java.net.*;
import java.io.*;

class PessoasInputStream extends InputStream{
    public Pessoa[] people;
    public String jsonToPeoople(String person, int i ){
        
        String ne = person.trim();
        ne = ne.replace("{", "");
        ne = ne.replace("}", "");
        String[] ns = ne.split(",");
        String name = ns[0];
        name = name.split(":")[1];
        name = name.replace("\"" , "");
        name = name.trim();
        String age = ns[1];
        age = age.split(":")[1];
        age = age.trim();
        int ageI;
        try {
            ageI = Integer.parseInt(age);
            
            String cpf = ns[2];
            cpf = cpf.split(":")[1];
            cpf = cpf.replace("\"", "");
            cpf = cpf.trim();
            people[i] = new Pessoa(ageI,name,cpf);
        } catch (Exception ageE) {
            System.out.println(ageE);
        }
        
        
        
        return person;
    }   

    public PessoasInputStream(FileInputStream is){
        String nmr= new String();
        try {
            char buffer = (char)is.read();
            while( '9' >= buffer && '0' <= buffer){
                nmr = nmr+buffer;
                buffer = (char)is.read();
            }
            
            nmr = nmr.trim();
            int nmrI;
            String person = new String();
            try {
                nmrI = Integer.parseInt(nmr);
                this.people = new Pessoa[nmrI];

                for(int i=0 ; i < nmrI ; i++){
                    // char buffer = (char)is.read();
                    while( buffer != '{'){
                        buffer = (char)is.read();
                    }
                    
                    while( buffer != '}'){
                        person =person+buffer;
                        buffer = (char)is.read();
                    }
                    person =person+buffer;
                    jsonToPeoople(person, i);
                    person = new String();
                }
                
            } catch (Exception e) {
                System.out.println(e);
            }
            return;        
        } catch (Exception e) {
            //TODO: handle exception
        }
            
    }
    public PessoasInputStream(InputStream is){ 
        
        byte[] n = new byte[100];
        byte[] person = new byte[1000000];
        try {
            is.read(n);
            String nmr = new String(n);
             nmr = new String(n);
            char[] nn = nmr.toCharArray();
            nmr = new String();
            for( int i =0 ; nn[i] !=',' && nn[i] <= '9' && nn[i] >='0';i++ ){
                nmr = nmr +nn[i];
            }
            
            nmr = nmr.trim();
            try {
                int nm = Integer.parseInt(nmr);
                
                this.people = new Pessoa[nm];
                is.read(person);
                String r = new String(person);

                r = r.trim();
                char [] p = r.toCharArray();
                
                String personS = new String();
                int j = 0;
                for(int i =0 ; i < nm && j < p.length; i++){                    
                    while(p[j] != '{' ){
                        j++;
                    }
                    while(p[j] != '}'){
                        personS = personS+p[j];
                        j++;
                    }
                    personS = personS+p[j];
                    jsonToPeoople(personS, i);
                    personS = new String();
                }    
               
            }
            catch(Exception en){
                System.out.println(en);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    @Override 
    public int read(){
        return 0;
    }
    public Pessoa[] getPerson(){
        return people;
    }
    public static void main(String []args) {

        //Entrada Padrão System.in
        System.out.println("Entrada Padrao (System.in)");
        System.out.println("Primeiro Digite o Numero de Pessoas");
        System.out.println("Logo Apos digite Nome Idade Cpf nessa Ordem em Fomato JSon Ex {\"Nome\" : \"Eliabe\" , \"idade\": 13, \"Cpf\" : \"07683654302\"}");
        System.out.println("Se for mais de uma pessoa JSon Ex {\"Nome\" : \"Eliabe\" , \"idade\": 13, \"Cpf\" : \"07683654302\"},{\"Nome\" : \"Eliabe\" , \"idade\": 13, \"Cpf\" : \"07683654302\"} Na mesma linha");
        PessoasInputStream piN = new PessoasInputStream(System.in);
        System.out.println();
        Pessoa[] pEp = piN.getPerson();
        System.out.println("Entrada Padrao (System.in)");
        for(int i =0 ; i < pEp.length ; i++){
            System.out.println("Pessoa :" + (i+1));
            System.out.println("Nome : " + pEp[i].nome +" "+ "Idade :" + pEp[i].idade +" "+ " Cpf :"+pEp[i].cpf);
        }
        System.out.println("==================================================================================");  
        System.out.println();
        // //=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

        
        InputStream in= null; 
        byte[] n = new byte[100];
        byte[] person = new byte[10000];
        
        // Entrada Pelo Arquivo 
        try {

            FileInputStream file =  new FileInputStream("output.txt");  
            PessoasInputStream pi = new PessoasInputStream(file);
            Pessoa[] p = pi.getPerson();
            System.out.println("Entrada Pelo FileInputStream");
            System.out.println();
            for(int i =0 ; i < p.length ; i++){
                System.out.println("Pessoa :" + (i+1));
                System.out.println("Nome : " + p[i].nome +" "+ "Idade :" + p[i].idade +" "+ " Cpf :"+p[i].cpf);
            }     
            System.out.println("==================================================================================");  
            System.out.println();
       } catch (Exception Fe) {
           System.out.println(Fe);
       } 
        //-------------------------------------------------------------
       
       
        // Entrada Pelo Socket 
        ServerSocket s = null;
        InputStream in2 = null;
        byte[] out = new byte[10000];
        PessoasInputStream pi ;
        try {
             s = new ServerSocket(8080);
             while(true){
                Socket cliente= s.accept();
                
                
                pi = new PessoasInputStream(cliente.getInputStream());
                
                Pessoa[] p = pi.getPerson();
                System.out.println("Entrada Pelo Socket");
                System.out.println();
                for(int i =0 ; i < p.length ; i++){
                    System.out.println("Pessoa :" + (i+1));
                    System.out.println("Nome : " + p[i].nome +" "+ "Idade :" + p[i].idade +" "+ " Cpf :"+p[i].cpf);
                }    
                System.out.println("==================================================================================");  
                System.out.println();
             }
             
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}