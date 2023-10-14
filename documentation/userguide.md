# User guide

- How is the program executed. How do different features work
- What kind of input does the program support
- Where can the executable be found and where can required files be found

## Features
There are four functionalities to choose from. The user can create a public and a private key for themselves, read the keys of a specific user, as well as encrypt or decrypt a message using the existing user keys. The command `x` ends the program.

![alt text](program_menu.png "Main menu")

### Generate keys
The user chooses the command `1` and presses enter. The program asks the user for their name and checks if it's valid (i.e., does not contain underscores due to the file naming conventions) and available (i.e., if there already exists a key file with the provided name). If the name is free to use, public and private keys are generated and saved into text files. If the username is already taken, the program infroms the user of this, and the user is returned to the main menu.

![alt text](invalid_username.png "Invalid username")

![alt text](username_taken.png "Username taken")

### Read keys
The user chooses the command `2` and presses enter. The program asks the user for their name and checks if key files for the provided username exist. If files are found, both the public and private keys are read from the files and printed on the command line. If no key files exist for the provided username, the program infroms the user of this, and the user is returned to the main menu.

![alt text](print_keys.png "Print")

![alt text](keyfiles_missing.png "Key files missing")


### Encrypt a message
The user chooses the command `3` and presses enter. The program asks the user for the username of the *message recipient*, as well as the message that should be encrypted. If a public key file for the given username is not found, the key files are generated on the fly and the new public key is used for encryption right away. If key files already exist, the public key file contents are used for encryption. The encryption end result is printed on the command line and copied to the clipboard for further use.

![alt text](encrypting.png "Encryption result")


### Decrypt a message
The user chooses the command `4` and presses enter. The program asks the user for *their* name, as well as the message that should be decrypted. The message should contain only numbers at this point. If there are other characters present, the input is invalid and the user is informed of this. If the message is valid, the private key of the user is retrieved and used to decrypt the message. The result is printed on the command line.

![alt text](decrypting.png "Decryption result")

![alt text](decrypting_wrong.png "Decryption result using the wrong key")