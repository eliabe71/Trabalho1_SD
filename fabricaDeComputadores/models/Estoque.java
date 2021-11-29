package models;
import java.util.ArrayList; 
public interface Estoque{
    public void remove(ArrayList<Integer> ids);
    public void add(Computador c);
    public ArrayList<String> getEstoque();
    //public boolean find(Computador c); 
}