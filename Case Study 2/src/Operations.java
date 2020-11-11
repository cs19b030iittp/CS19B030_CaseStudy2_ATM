import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;

public class Operations {
    private Account account = ATM.account;
    private Scanner scan = new Scanner(System.in);
    private final double maxAmount =40000.00;
    protected Time time = new Time();

    void balanceEnquiryOption() {
        try {
            System.out.println("Press 1 : Show Balance and Logout\nPress 2 : Logout");
            int n = Integer.parseInt(scan.nextLine());
            switch (n){
                case 1 :
                    System.out.println("Your Current Balance is Rs"+account.getBalance());
                    account.logout();
                    break;
                case 2 :
                    account.logout();
                    break;
                default:
                    System.out.println("Invalid Option ");
                    account.logout();
            }
        }
        catch (Exception e1){
            System.out.println(e1.getMessage());
            System.out.println("Invalid Input");
            try { account.logout(); }catch (Exception e){}
        }
    }


    boolean verifyPIN() {

        try {
            System.out.print("Enter 5-digit PIN : ");
            String inputPIN = scan.nextLine();
            System.out.println();
            int pin = Integer.parseInt(inputPIN);
            if (inputPIN.length() != 5){
                System.out.println("Invalid PIN!");
                account.logout();
                return false;
            }

            if (pin == account.getPIN()) {
                System.out.println("Correct PIN");
                return true;
            }

            else {
                System.out.println("Incorrect PIN!");
                account.logout();
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Invalid Input!");
            account.logout();
            return false;
        }
    }
    boolean verifyOTP(){
        try {
            Scanner scan = new Scanner(System.in);
            int OTP = new SecureRandom().nextInt(9999);
            System.out.println("Your OTP is : " + OTP);
            System.out.print("Enter OTP : ");
//            System.out.print("Enter 4-digit OTP : ");
            int inputOTP = Integer.parseInt(scan.nextLine());
//            if (Integer.toString(inputOTP).length()!=4){
//                throw new Exception();
//            }
            if(inputOTP == OTP) {
                System.out.println("Correct OTP");
                return true;
            }
            else{
                System.out.println("Incorrect OTP");
                account.logout();
                return false;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Invalid OTP");
            account.logout();
            return false;
        }
    }

    void retryOptions(int i){
        try {
            System.out.println("Press 1 : Try Again\nPress 2 : Logout");
            int n = Integer.parseInt(scan.nextLine());
            switch (n){
                case 1 :
                    switch(i){
                        case 1: runWithdraw();
                            break;
                        case 2: runDeposit();
                            break;
                        case 3:
                            runTransfer();
                            break;
                        case 4: balanceEnquiryOption();
                            break;
                        case 5: runChangePIN();
                            break;
                        default:
                            System.out.println("Error in retryOption()");
                    }
                    break;
                case 2 : account.logout();
                    break;
                default:
                    System.out.println("Invalid Option ");
                    account.logout();
            }
        }
        catch (Exception e1){
            System.out.println(e1.getMessage());
            System.out.println("Invalid Input");
            account.logout();
        }
    }
    void runWithdraw(){
        try{
            System.out.print("Enter Amount : ");
            double amount = Double.parseDouble(scan.nextLine());
            System.out.println();
            if (Double.parseDouble(account.db.database[account.db.database.length-1])<amount) {
                System.out.println("ATM is out of Cash!");
                account.logout();
            }
            if (amount < maxAmount){
                if (amount < account.getBalance()){
                    if( verifyOTP()){
                        //dispense money
                        account.setBalance(account.getBalance()-amount);
                        //updating atm cash
                        account.db.database[account.db.database.length-1]=Double.toString(Double.parseDouble(account.db.database[account.db.database.length-1])-amount);
                        //Updating statements
                        account.db.database[account.k+7]=account.db.database[account.k+6];
                        account.db.database[account.k+6]=account.db.database[account.k+5];
                        account.db.database[account.k+5]="You have Withdrawn Rs"+ amount + " on " + time.getDate();
                        System.out.println("Your Transaction Is COMPLETED, Please Collect Your CASH");
                        balanceEnquiryOption();
                    }

                }
                else {
                    System.out.println("Unable to Dispense, Your Account don't have enough Balance");
                    account.logout();
                }
            }
            else {
                System.out.println("Unable to dispense more than "+ maxAmount);
                account.logout();
            }
        }
        catch(Exception e){
            System.out.println("Invalid Input!");
            retryOptions(1);
        }
    }

    void runDeposit(){
        try {
            System.out.print("Enter amount : ");
            int amount = Integer.parseInt(scan.nextLine());
            System.out.println();
            if(verifyOTP()) {
                account.setBalance(account.getBalance() + amount);
                //updating ATM cash
                account.db.database[account.db.database.length-1]=Double.toString(Double.parseDouble(account.db.database[account.db.database.length-1])+amount);
                account.db.database[account.k+7]=account.db.database[account.k+6];
                account.db.database[account.k+6]=account.db.database[account.k+5];
                account.db.database[account.k+5]="Deposited Rs"+ amount + " on " + time.getDate();
                System.out.println("You have deposited Rs " + amount );
                balanceEnquiryOption();
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Invalid Input");
            retryOptions(2);
        }
    }
    void runTransfer(){
        try {
            int r = 0; //receiver's account number index in database array
            boolean receiverFound = false;
            System.out.print("Enter Account Number of Receiver : ");
            String inputAccountNo = scan.nextLine();
            System.out.println();
            System.out.print("Enter IFSC Code : ");
            String inputIfscCode = scan.nextLine();
            System.out.println();

            int receiverAccountNo = Integer.parseInt(inputAccountNo);
            if(inputAccountNo.length() != 5 || receiverAccountNo==account.getAccountNo()){
                System.out.println("Invalid Account Number!");
                retryOptions(3);
            }

            int receiverIfscCode = Integer.parseInt(inputIfscCode);
            if (inputIfscCode.length() !=5 || receiverIfscCode == account.getIfscCode()){
                System.out.println("Invalid IFSC Code!");
                retryOptions(3);
            }

            for (int i=8;i < account.db.database.length; i = i+8){          //find receiver's account number
                if (Integer.parseInt(account.db.database[i])==receiverAccountNo && Integer.parseInt(account.db.database[i+3]) == receiverIfscCode){
                    r=i;
                    receiverFound = true;
                    break;
                }
            }

            if (receiverFound){
                System.out.print("Enter amount to be Transferred : ");
                double amount = Double.parseDouble(scan.nextLine());
                System.out.println();
                if (amount < account.getBalance()){
                    if (verifyOTP()) {
                        account.setBalance(account.getBalance() - amount);    //changing balance variable
                        account.db.database[r + 2] = Double.toString(Double.parseDouble(account.db.database[r + 2]) + amount);  //updating receiver's balance in database
                        //updating statements of sender's account
                        account.db.database[account.k+7]=account.db.database[account.k+6];
                        account.db.database[account.k+6]=account.db.database[account.k+5];
                        account.db.database[account.k+5]="Rs"+ amount + " Transferred to Account Number "+receiverAccountNo +" on "+ time.getDate();
                        //updating statements of receiver's account
                        account.db.database[r+7]=account.db.database[r+6];
                        account.db.database[r+6]=account.db.database[r+5];
                        account.db.database[r+5]="You got Rs"+ amount + " from Account Number "+account.getAccountNo()+ " on " + time.getDate();

                        System.out.println("Your Transaction is Successful");
                        balanceEnquiryOption();
                    }

                }
                else {
                    System.out.println("Your Account don't have enough Balance to complete the Transaction !");
                    account.logout();
                }
            }
            else{
                System.out.println("Receiver's Account Number NOT FOUND !");
                retryOptions(3);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            System.out.println("Invalid Input");
            retryOptions(3);
        }
    }
    void runChangePIN(){
        try{

            if (verifyPIN()){
                System.out.print("Enter new 5-digit PIN : ");
                String inputNewPIN = scan.nextLine();
                System.out.println();

                int newPIN = Integer.parseInt(inputNewPIN);
                if (inputNewPIN.length() != 5){
                    System.out.println("Enter Valid PIN!");
                    retryOptions(5);
                }
                else{
                    account.setPIN(newPIN);
                    System.out.println("PIN is changed Successfully");
                    account.logout();
                }
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            System.out.println("Invalid Input!");
            retryOptions(5);
        }
    }

}
