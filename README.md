<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bank Management System Overview</title>
</head>
<body>

    <h1>Bank Management System using Password Protection</h1>
    <p>The banking system manages user registrations, logins, and account operations (credit, debit, transfer, balance check). 
    The backend uses Java, JDBC for database connectivity, and a MySQL database. It handles secure password management, 
    account creation, and ensures transactional integrity for money operations.</p>

    <h2>1. User Class</h2>
    <p>The <strong>User</strong> class manages user registration and login processes. It interacts with the database to securely store user information.</p>

    <h3>Key Methods:</h3>
    <ul>
        <li><code>register()</code>: Prompts the user for full name, email, and password. 
            <ul>
                <li>Checks if the user already exists in the database to avoid duplication.</li>
                <li>Hashes the password using PBKDF2WithHmacSHA1 with a randomly generated salt.</li>
                <li>Inserts the user details into the database securely with a salted hash for protection.</li>
                <li><strong>Security Enhancement:</strong> Add email validation to prevent malformed input.</li>
            </ul>
        </li>
        <li><code>login()</code>: Prompts the user for email and password.
            <ul>
                <li>Fetches the stored hashed password from the database using the email as a key.</li>
                <li>Compares the entered password with the stored hash using the same salt.</li>
                <li>Returns the user's email if the login is successful; otherwise, an error message is displayed.</li>
                <li><strong>Tip:</strong> Implement a rate limiter to prevent brute force attacks on login attempts.</li>
            </ul>
        </li>
        <li><code>user_exist(String email)</code>: Checks if a user already exists in the database for the provided email.</li>
        <li><code>hashPassword(String password)</code>: Generates a hash of the given password using PBKDF2WithHmacSHA1 with a random salt.
            <ul>
                <li>Returns the salt and hash concatenated into a single string for secure storage.</li>
                <li><strong>Best Practice:</strong> The number of hashing iterations should be high (e.g., 100,000) for better security.</li>
            </ul>
        </li>
        <li><code>verifyPassword(String password, String storedHash)</code>: Verifies the entered password by hashing it with the same salt used during registration. 
            <ul>
                <li>Returns <code>true</code> if the hash matches; otherwise, <code>false</code>.</li>
                <li><strong>Important:</strong> Ensure constant-time comparison to prevent timing attacks.</li>
            </ul>
        </li>
    </ul>

    <p><strong>Security Notes:</strong></p>
    <ul>
        <li>Passwords are hashed using PBKDF2 with a random salt, providing strong protection against dictionary attacks.</li>
        <li>Plain passwords are never stored in the database, ensuring user credentials remain safe even if the database is compromised.</li>
        <li>Consider two-factor authentication (2FA) for an extra layer of security during login.</li>
    </ul>

    <h2>2. Accounts Class</h2>
    <p>The <strong>Accounts</strong> class handles the creation and management of bank accounts.</p>

    <h3>Key Methods:</h3>
    <ul>
        <li><code>open_account(String email)</code>: Generates a new account number and inserts the account details into the database.
            <ul>
                <li>Returns the newly created account number.</li>
                <li>Initial account balance and a secure PIN are also requested from the user.</li>
                <li><strong>Enhancement:</strong> Consider account verification through email before activation.</li>
            </ul>
        </li>
        <li><code>getAccount_number(String email)</code>: Retrieves the account number associated with the provided email.</li>
        <li><code>generateAccountNumber()</code>: Generates a unique account number based on the last used account number in the database.
            <ul>
                <li>If no accounts exist, it starts with a default number (e.g., 10000100).</li>
                <li><strong>Tip:</strong> Use a more advanced method to generate unique, unpredictable account numbers to avoid conflicts.</li>
            </ul>
        </li>
        <li><code>account_exist(String email)</code>: Checks if an account exists for a user with the given email.</li>
    </ul>

    <h2>3. AccountManager Class</h2>
    <p>The <strong>AccountManager</strong> class handles financial transactions such as credit, debit, money transfers, and balance inquiries.</p>

    <h3>Key Methods:</h3>
    <ul>
        <li><code>credit_money(long account_number)</code>: Verifies the PIN and credits the specified amount to the account.
            <ul>
                <li>Ensures the transaction is performed atomically using transaction management to guarantee data consistency.</li>
                <li><strong>Improvement:</strong> Log transaction details (amount, date, source) for audit purposes.</li>
            </ul>
        </li>
        <li><code>debit_money(long account_number)</code>: Checks if the account has sufficient balance before debiting the requested amount.
            <ul>
                <li>Ensures the transaction is either fully completed or rolled back in case of insufficient funds or other errors.</li>
                <li><strong>Enhancement:</strong> Implement overdraft protection or alert the user if the balance is low.</li>
            </ul>
        </li>
        <li><code>transfer_money(long sender_account_number)</code>: Transfers money from one account to another.
            <ul>
                <li>The user provides the recipient's account number, transfer amount, and security PIN.</li>
                <li>Checks the sender's balance and verifies the security PIN before performing the transfer.</li>
                <li>Updates the balances of both accounts (sender and receiver) in a single transaction to ensure consistency.</li>
                <li><strong>Recommendation:</strong> Allow users to add recipients to a “trusted list” to avoid repeated PIN checks for frequent transfers.</li>
            </ul>
        </li>
        <li><code>getBalance(long account_number)</code>: Retrieves the current balance of the account after verifying the security PIN.
            <ul>
                <li><strong>Security:</strong> Ensure the balance is only shown after confirming the correct PIN to protect sensitive information.</li>
            </ul>
        </li>
    </ul>

    <h3>Transaction Management:</h3>
    <p>The class disables auto-commit before performing operations such as debit, credit, or transfer to ensure transactional integrity. 
    If an error occurs (e.g., insufficient funds or incorrect PIN), the transaction is rolled back to its original state. 
    After successful operations, the transaction is committed.</p>

    <ul>
        <li><strong>Atomicity:</strong> Ensures that all financial transactions are fully completed or not executed at all to prevent data corruption.</li>
        <li><strong>Error Handling:</strong> Transaction rollback in case of failure helps maintain data integrity across complex operations.</li>
        <li><strong>Audit Trails:</strong> Keep logs of all transactions for accountability and future reference.</li>
    </ul>

    <h2>4. BankingApp Class</h2>
    <p>The <strong>BankingApp</strong> is the entry point of the system, implementing the user interface logic.</p>

    <h3>Key Methods:</h3>
    <ul>
        <li><code>main(String[] args)</code>: Handles the main application flow.
            <ul>
                <li>Prompts the user to register or log in.</li>
                <li>If login is successful, checks if the user has an account.</li>
            </ul>
        </li>
    </ul>

    <h3>Application Flow:</h3>
    <ol>
        <li>Registration: Users can register by providing their full name, email, and password.</li>
        <li>Login: Registered users can log in with their email and password.</li>
        <li>Account Creation: Once logged in, users without a bank account can create one by providing an initial balance and security PIN.</li>
        <li>Account Operations: After logging in, users can:
            <ul>
                <li>Debit: Withdraw money from their account after PIN verification.</li>
                <li>Credit: Deposit money into their account.</li>
                <li>Transfer: Transfer money to another account after PIN verification and balance check.</li>
                <li>Check Balance: View their account balance securely after verifying the PIN.</li>
            </ul>
        </li>
    </ol>

</body>
</html>
