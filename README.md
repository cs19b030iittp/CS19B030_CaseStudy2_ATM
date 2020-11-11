                                     Case Study 2 - ATM
                                                                      
The attatched folder has .java files , .class files  and a .csv file which contain account details of all the users.
The first screen has login option by account number and PIN.The second screen has options:
1. Withdraw
2. Deposit
3. Transfer
4. Balance Enquiry
5. Get Mini Statement 
6. ChangePIN
7. Logout

 We can withdraw maximun Rs 40,000 in a transaction. When ATM don't have enough cash to complete a transaction, it will display a message that "ATM is out of Cash".

Security
⦁	 Two Step Verification - An OTP is generated and is to be verified by user just before proccessing the transaction.
⦁	PIN is encrypted and stored in csv file.
⦁	User cannot do multiple transactions , user has to login for each transaction and after each transaction it shows two options: show current balance and logout. After          successful logout user will be returned to first screen.

Classes
1. ATM - has main method and menu method which call all the operation methods from Operation class. 
2. Account - has login and logout method, object of Database and has getters and setters methods.
3. Operations - has methods to perform all the operations : withdrawal, deposit, transfer, balance enquiry, transaction details, change PIN and logout.
4. Database- has loadDatabase method which reads the data from csv file and store in an array and a updateDatabase method to rewrite the csv file with updated array. 
5. Time - has a Date object.

Account details to run program:
Account number           Password
12345                     11223
23456                     23657

