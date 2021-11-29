package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class Database{
    Connection cnc;
    File dbFile;
    public Database(){
        dbFile = new File("fabrica.db");
        if(dbFile.exists()){
            try {
                cnc = DriverManager.getConnection("jdbc:sqlite:fabrica.db");
            } catch (Exception dbE) {
                System.out.println(dbE);
            }
        }
        else{
            try {
                dbFile.createNewFile();
                cnc = DriverManager.getConnection("jdbc:sqlite:fabrica.db");
                Statement stmt = cnc.createStatement();
                stmt.execute("CREATE TABLE Estoque(ID INTEGER PRIMARY KEY AUTOINCREMENT,Name Text not NULL,model Text ,manufacturer Text ,year Text ,price Real)");
                stmt.execute("CREATE TABLE Laboratorio(ID INTEGER PRIMARY KEY AUTOINCREMENT,Name Text not NULL,model Text ,manufacturer Text ,year Text ,price Real)");

            } catch (Exception dbE) {
                System.out.println(dbE);
            }

        }

    }
    public Connection getConnection(){
        return cnc;
    }
}