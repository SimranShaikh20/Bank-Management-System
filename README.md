# BankManagementSystem
Bank Management System using Password Protection

Overview -
The banking system manages user registrations, logins, and account operations (credit, debit, transfer, balance check). The backend uses Java, JDBC for database connectivity, and a MySQL database. It handles secure password management, account creation, and ensures transactional integrity for money operations.


1. User Class
The User class manages user registration and login processes. It interacts with the database to securely store user information.

Key Methods:
register(): -> In which  user for full name, email, and password.
Checks if the user already exists in the database.
Hashes the password using PBKDF2WithHmacSHA1 with a random salt.
Inserts the user details into the database.

login(): ->In which user for email and password.
Fetches the stored hashed password from the database.
Verifies the entered password by comparing the hashed values.
Returns the user's email if the login is successful, otherwise returns null.


user_exist(String email): -> Checks if a user already exists in the database for a given email.
hashPassword(String password):

Generates a hash of the given password using PBKDF2WithHmacSHA1 with a random salt.
Returns the salt and hash concatenated as a single string.
verifyPassword(String password, String storedHash):

Verifies the entered password by hashing it with the same salt used during registration.
Returns true if the hash matches, otherwise false.
Security Notes:
Passwords are hashed using PBKDF2 with a random salt for protection against dictionary attacks.
Plain passwords are never stored in the database.


2. Accounts Class
->The Accounts class handles the creation and management of bank accounts.

* Key Methods:
open_account(String email): -> Generates a new account number and inserts the account details into the database.
Returns the newly created account number.
getAccount_number(String email):

Retrieves the account number associated with the provided email.
generateAccountNumber():-> Generates a unique account number based on the last used account number in the database.

If no accounts exist, it starts with a default number (e.g., 10000100).
account_exist(String email):->Checks if an account exists for a user with the given email.


3. AccountManager Class
The AccountManager class handles financial transactions such as credit, debit, money transfers, and balance inquiries.

* Key Methods:
credit_money(long account_number):->Verifies the pin and credits the specified amount to the account.
Uses transaction management to ensure data consistency.


debit_money(long account_number):-> Checks if the account has sufficient balance before debiting.
Ensures the transaction is either fully completed or rolled back in case of an error.

transfer_money(long sender_account_number):->Transfers money from one account to another.The user for the recipient's account number, transfer amount, and security pin.
Verifies the pin and checks the balance before performing the transaction.
Updates the balances of both accounts (sender and receiver) in a single transaction.

getBalance(long account_number):-> Retrieves the current balance of the account after verifying the security pin.

* Transaction Management:
The class disables auto-commit before operations such as debit, credit, or transfer to ensure transactional integrity.
If an error occurs (e.g., insufficient funds or incorrect pin), the transaction is rolled back to its original state.After successful operations, the transaction is committed.


4. BankingApp Class
The BankingApp is the entry point of the system, implementing the user interface logic.

* Key Methods:
main(String[] args):
Handles the main application flow:-> user to register or login.
If login is successful, checks if the user has an account.

* Provides options for account operations: debit, credit, transfer, and balance inquiry.Ensures smooth navigation through the application’s different functionalities.

* Application Flow:
1] Registration: Users can register by providing their full name, email, and password.
2] Login: Registered users can log in with their email and password.
3] Account Creation: Once logged in, users without a bank account can create one by providing an initial balance 

    and security pin.
4] Account Operations: After logging in, users can:
5] Debit: Withdraw money from their account.
6] Credit: Deposit money into their account.
7] Transfer: Transfer money to another account.
8] Check Balance: View their account balance.
