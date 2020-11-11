import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Database {
    protected String[] database;

    void loadDatabase(){
        try{
            int ctr = 0;
            String filepath = "src\\database.csv";
            File f = new File(filepath); //File object to open the database.csv file
            Scanner s = new Scanner (f);//Scanner object that reads data from the file
            s.useDelimiter("[,\n]");
            while(s.hasNext()){
                ctr++;						//Counts the length of database.csv array
                s.next();
            }
            String[] db = new String[ctr];
            Scanner s1 = new Scanner(f);
            s1.useDelimiter("[,\n]");
            for(int i = 0; i<ctr; i++){
                db[i] = s1.next();		//Reads data from database.csv and stores in a local array
//                System.out.print(db[i]+",");
//                System.out.println();
            }
            database = db;					//Copies data from local array to global array
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void updateDatabase(){
        try{
            FileWriter fw=new FileWriter("src\\database.csv");     //Updates database.csv
            for (int i=0;i<database.length;i=i+8) {
                if (i == database.length-2){
                    fw.write(database[i]+","+database[i+1]+",");
                }
                else {
                fw.write(database[i] + "," + database[i + 1] + "," + database[i + 2] +","+ database[i+3]+","+database[i+4]+","+database[i+5]+","+database[i+6]+","+database[i+7]+"\n");
                }
            }
            fw.close();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error in updating database.csv");
        }

    }


}
