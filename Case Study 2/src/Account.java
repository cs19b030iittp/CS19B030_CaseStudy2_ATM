
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class Account {
    protected Database db = new Database();
    protected  int k;
    private int accountNo;
    private int PIN;
    private double balance;
    private int IfscCode;
    private boolean loginStatus;
    private static final byte[] keyValue = {'T','h','i','s','I','s','M','y','K','e','y','V','a','l','u','e'};
    private Scanner scan = new Scanner(System.in);

    public int getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(int accountNo) {
        this.accountNo = accountNo;
    }

    public int getPIN() {
        return PIN;
    }

    public void setPIN(int PIN) {
        this.PIN = PIN;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getIfscCode() { return IfscCode; }

    public void setIfscCode(int ifscCode) { IfscCode = ifscCode; }

    public boolean getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }


    void  login() {
        try{
            db.loadDatabase(); //get the data in string array

            System.out.print("Enter your 5-digit Account Number : ");
            String inputAccountNo = scan.nextLine();
            System.out.println();

            accountNo = Integer.parseInt(inputAccountNo);
            if(inputAccountNo.length() != 5 ){
                System.out.println("Invalid Account Number!");
                login();
            }

            System.out.print("Enter your 5-digit PIN : ");
            String inputPIN = scan.nextLine();
            System.out.println();

            PIN = Integer.parseInt(inputPIN);
            if (inputPIN.length() != 5){
                System.out.println("Invalid PIN!");
                login();
            }

            for(int i =8; i<db.database.length-2; i=i+8){				//i=8 is to ignore first line,length-2 for ignoring ATM Cash in database.csv and Iterates every 8th element as Account number is stored at every 8th index
                if(Integer.parseInt(db.database[i])==accountNo && Integer.parseInt(decrypt(db.database[i+1]))==PIN){
                    k = i;                                //Stores index of Account number
                    setLoginStatus(true);
                    setAccountNo(Integer.parseInt(db.database[k]));
                    setPIN(Integer.parseInt(decrypt(db.database[k + 1])));
                    setBalance(Double.parseDouble(db.database[k + 2]));
                    setIfscCode(Integer.parseInt(db.database[k + 3]));
                    System.out.println("Login Successful :)");
                    System.out.println();
                    System.out.println("                   WELCOME "+db.database[k+4]);
                    System.out.println();
                    break;
                }
            }
            if (!loginStatus){
                System.out.println("Incorrect Account Number or PIN !");
                login();
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Invalid Input ,Please Try Again!");
            login();
        }

    }

    void logout() {
        try {
            db.database[k + 1] = encrypt(Integer.toString(PIN));  //update database array
            db.database[k + 2] = Double.toString(balance);
            db.updateDatabase();   //update database.csv file
            setLoginStatus(false);
            System.out.println("You are logged out Successfully");
            System.out.println();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Error in logging out");
        }
    }


    String encrypt(String message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //encrypting and encoding PIN
        Key key= new SecretKeySpec(keyValue,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] bytes =cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }
    String decrypt(String encryptedMessage) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //decoding and decrypting PIN
        Key key= new SecretKeySpec(keyValue,"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,key);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)));
    }

}


