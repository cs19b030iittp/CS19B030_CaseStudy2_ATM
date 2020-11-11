import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    protected static Account account = new Account();
    protected Operations o = new Operations();
    private Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        ATM obj = new ATM();
        while (true){
            obj.run();
        }
    }
    void run(){
        account.login();
        if (account.getLoginStatus()){
            menu();
        }
        else{
            System.out.println("Login Unsuccessful!");
            run();
        }
    }

    void menu(){
        System.out.println("Press 1: Withdraw\nPress 2: Deposit\nPress 3: Transfer\nPress 4: Balance Enquiry\nPress 5: Get Mini Statement \nPress 6: ChangePIN\nPress 7: Logout");

        try {
            int n = Integer.parseInt(scan.nextLine());

            switch (n) {
                case 1:
                    o.runWithdraw();
                    break;
                case 2:
                    o.runDeposit();
                    break;
                case 3:
                    o.runTransfer();
                    break;
                case 4:
                    System.out.println("Your Current Balance is Rs"+account.getBalance());
                    break;
                case 5 :
                    System.out.println(account.db.database[account.k+5]);
                    System.out.println(account.db.database[account.k+6]);
                    System.out.println(account.db.database[account.k+7]);
                    System.out.println();
                    break;
                case 6:
                    o.runChangePIN();
                    break;
                case 7:
                    account.logout();
                    break;
                default:
                    System.out.println("Enter Valid Input!");
                    menu();
            }
        }
        catch (Exception e) {
            System.out.println("Invalid Input!");
            menu();
        }
    }



}
