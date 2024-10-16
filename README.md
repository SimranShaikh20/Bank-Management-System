# Bank Management System using Password Protection

The banking system manages user registrations, logins, and account operations (credit, debit, transfer, balance check). The backend uses Java, JDBC for database connectivity, and a MySQL database. It handles secure password management, account creation, and ensures transactional integrity for money operations.

## 1. User Class
The **User** class manages user registration and login processes. It interacts with the database to securely store user information.

### Key Methods:
- **register()**: Prompts the user for full name, email, and password.
    - Checks if the user already exists in the database.
    - Hashes the password using PBKDF2WithHmacSHA1 with a random salt.
    - Inserts the user details into the database securely with a salted hash.
    - _Security Enhancement_: Add email validation to prevent malformed input.
  
- **login()**: Prompts the user for email and password.
    - Fetches the stored hashed password from the database.
    - Verifies the entered password by comparing the hashed values.
    - Returns the user's email if the login is successful; otherwise, an error message is displayed.
    - _Tip_: Implement a rate limiter to prevent brute force attacks on login attempts.

- **user_exist(String email)**: Checks if a user already exists in the database for the provided email.

- **hashPassword(String password)**: Generates a hash of the given password using PBKDF2WithHmacSHA1 with a random salt.
    - Returns the salt and hash concatenated into a single string for secure storage.
    - _Best Practice_: Use a high number of iterations (e.g., 100,000) for better security.

- **verifyPassword(String password, String storedHash)**: Verifies the entered password by hashing it with the same salt used during registration.
    - Returns `true` if the hash matches; otherwise, `false`.
    - _Important_: Ensure constant-time comparison to prevent timing attacks.

### Security Notes:
- Passwords are hashed using PBKDF2 with a random salt, providing strong protection against dictionary attacks.
- Plain passwords are never stored in the database.
- Consider two-factor authentication (2FA) for an extra layer of security during login.

## 2. Accounts Class
The **Accounts** class handles the creation and management of bank accounts.

### Key Methods:
- **open_account(String email)**: Generates a new account number and inserts the account details into the database.
    - Returns the newly created account number.
    - Initial account balance and a secure PIN are also requested from the user.
    - _Enhancement_: Consider account verification through email before activation.
  
- **getAccount_number(String email)**: Retrieves the account number associated with the provided email.

- **generateAccountNumber()**: Generates a unique account number based on the last used account number in the database.
    - If no accounts exist, it starts with a default number (e.g., 10000100).
    - _Tip_: Use a more advanced method to generate unique, unpredictable account numbers to avoid conflicts.

- **account_exist(String email)**: Checks if an account exists for a user with the given email.

## 3. AccountManager Class
The **AccountManager** class handles financial transactions such as credit, debit, money transfers, and balance inquiries.

### Key Methods:
- **credit_money(long account_number)**: Verifies the PIN and credits the specified amount to the account.
    - Ensures the transaction is performed atomically using transaction management to guarantee data consistency.
    - _Improvement_: Log transaction details (amount, date, source) for audit purposes.

- **debit_money(long account_number)**: Checks if the account has sufficient balance before debiting the requested amount.
    - Ensures the transaction is either fully completed or rolled back in case of insufficient funds or other errors.
    - _Enhancement_: Implement overdraft protection or alert the user if the balance is low.

- **transfer_money(long sender_account_number)**: Transfers money from one account to another.
    - The user provides the recipient's account number, transfer amount, and security PIN.
    - Checks the sender's balance and verifies the security PIN before performing the transfer.
    - Updates the balances of both accounts (sender and receiver) in a single transaction to ensure consistency.
    - _Recommendation_: Allow users to add recipients to a “trusted list” to avoid repeated PIN checks for frequent transfers.

- **getBalance(long account_number)**: Retrieves the current balance of the account after verifying the security PIN.
    - _Security_: Ensure the balance is only shown after confirming the correct PIN to protect sensitive information.

### Transaction Management:
- Disables auto-commit before performing operations such as debit, credit, or transfer to ensure transactional integrity.
- If an error occurs (e.g., insufficient funds or incorrect PIN), the transaction is rolled back to its original state.
- After successful operations, the transaction is committed.

### Transaction Management Highlights:
- **Atomicity**: Ensures that all financial transactions are fully completed or not executed at all to prevent data corruption.
- **Error Handling**: Transaction rollback in case of failure helps maintain data integrity across complex operations.
- **Audit Trails**: Keep logs of all transactions for accountability and future reference.

## 4. BankingApp Class
The **BankingApp** is the entry point of the system, implementing the user interface logic.

### Key Methods:
- **main(String[] args)**: Handles the main application flow.
    - Prompts the user to register or log in.
    - If login is successful, checks if the user has an account.

### Application Flow:
1. **Registration**: Users can register by providing their full name, email, and password.
2. **Login**: Registered users can log in with their email and password.
3. **Account Creation**: Once logged in, users without a bank account can create one by providing an initial balance and security PIN.
4. **Account Operations**: After logging in, users can:
    - Debit: Withdraw money from their account after PIN verification.
    - Credit: Deposit money into their account.
    - Transfer: Transfer money to another account after PIN verification and balance check.
    - Check Balance: View their account balance securely after verifying the PIN.
