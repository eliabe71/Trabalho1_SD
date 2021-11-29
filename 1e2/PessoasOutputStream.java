import java.io.*;
import java.net.*;

public class PessoasOutputStream extends OutputStream {
    OutputStream os;
    Pessoa[] p;
    String[] js;
    int pos;

    public PessoasOutputStream(Pessoa[] p, OutputStream os) {
        this.os = os ;
        this.p= p;
        this.js =toJson();
        
        for(int i =0 ; i < js.length ; i++){
            pos =i;
            write(this.js[i].getBytes().length);
        }

        //os.close();
         
    }
    private String[] toJson() {
        if(this.p.length ==0) return null;
        String[] v = new String[this.p.length];
        for( int i =0 ; i <this.p.length ; i ++ ){
            v[i] = new String();
            v[i] = "{ \"Nome\": "+ "\""+ this.p[i].nome + "\""+ ", \"Idade\": "+ this.p[i].idade+ ", \"Cpf\": "+ "\""+ this.p[i].cpf+"\"" +" }\n";
            if(i == this.p.length -1) continue;
            v[i] = v[i]+",";
            
        }
        return v;
    }
    @Override
    public void write(int b) {
        write(js[pos].getBytes());
        
    }
   
    public void write(byte[] b){
        try {
            
            os.write(b,0,b.length);
            os.flush();
            
        } catch (Exception e) {
                
            }
        
    }   
   
    public static void main(String []args){
        Pessoa[] p1 = new Pessoa[1];
        p1[0] = new Pessoa(1,"eliabe", "07683654302");
        System.out.println("Saída de Dados Pelo Padrão (System.out)");
        System.out.println();
       PessoasOutputStream pop = new PessoasOutputStream(p1,  System.out); 
       System.out.println();
        try {
            System.out.println("Saída de Dados Por Arquivo (FileOutputStream) Está no arquivo output.txt");
            FileOutputStream fos = new FileOutputStream("output.txt");
            String esc = p1.length + "\n";
            fos.write(esc.getBytes());
            pop = new PessoasOutputStream(p1,fos); 
            fos.close();   
            System.out.println();
        } catch (Exception e2) {
            System.out.println(e2);
        }











        Socket s= null;
        try {
            int serverPort = 8080;
			s = new Socket("localhost", serverPort);
		    String number;
            try {
                Pessoa[] p = new Pessoa[3];
                number = new String();
                number = number.valueOf(p.length);
                number = number+",";
                System.out.println(number);
                s.getOutputStream().write(number.getBytes());
            
                
                p[0] = new Pessoa(1,"eliabesantos", "0000000000");
                p[1] = new Pessoa(12,"eliabe", "0454544544");
                p[2] = new Pessoa(12,"eliabe", "0454544544");                                               

                PessoasOutputStream p_os = new PessoasOutputStream(p, s.getOutputStream()); 
            }
            catch(Exception e1 ){}
            
            s.close();
            
            
        } catch (Exception e) {
            
        }
    }
   };