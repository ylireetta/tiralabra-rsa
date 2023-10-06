# Project specification
Degree programme: bachelor's in computer science (CS).

This project depicts the implementation of the [RSA algorithm](https://en.wikipedia.org/wiki/RSA_(cryptosystem) "RSA in Wikipedia"). The problem I will solve is the basic use case of encrypting and decrypting messages using user-specific public and private keys. The reason I chose the RSA encryption algorithm is personal interest - I thought the topic was interesting when I first came across the more detailed explanation of the algorithm during Introduction to Networking, and this is a perfect opportunity to dive a bit deeper.

There are several different algorithms that need to be implemented in order for the RSA algorithm to work as expected. Here are a few I have thought of so far:
- Miller-Rabin primality test to determine whether a randomly generated integer is *likely* to be prime
- Euclidean algorithm to find the greatest common divisor when implementing the Miller-Rabin test
- Extended Euclidean algorithm when generating the private exponent part of the private key

I might also implement some sort of binary search to make it more efficient to search for text files that start with the user's username (i.e., it makes no sense to start the search from 'a' when the username received starts with 'x').

For the sake of consistency, both the code and the documentation is written in English. The programming language chosen for this project is Java. In addition, I know C#, Python, and JavaScript to such a degree that I should be able to peer review projects written in those languages.

## Program use cases
Users provide the program input. The user inputs a message they want to either encrypt or decrypt, as well as their username. The usernames are needed for storage purposes: when the program generates key pairs (i.e., public and private keys), they are stored in text files which are distinguished by including the username in the file name. *Please note that the security aspect of storing the data is not the focus point in this project, and the text files are used just for simplicity*.

There are two use cases:
1. Message encryption
    - Program input 1 = recipient's username
    - Program input 2 = the message to encrypt
    - Encryption:
        - The program searches for a text file containing the public key of the recipient using program input 1 (e.g., recipientname_public_key.txt)
        - If a public key is found, it is used to encrypt the message (program input 2)
        - If a public key is *not* found, the key pair for the recipient is generated on the fly, written in text files, and the public key is used to encrypt the message (program input 2)
    - The output is the RSA encrypted message

2. Message decryption
    - Program input 1 = current user's username (i.e., the recipient)
    - Program input 2 = the message to decrypt
    - Decryption:
        - The program searches for a text file containing the private key of the recipient using program input 1 (e.g., recipientname_private_key.txt)
        - If a private key is found, it used to decrypt the message (program input 2)
        - If a private key is *not* found, an error message is shown to the user and the program returns to the main menu
    - In case of successful encryption, the output is the decrypted message

Usernames need to be unique for the text file system to work, so the program contains validation whenever key files should be generated. When a user specifically asks to generate new key files, the program checks whether the username is already taken. If not, key files are created. If yes, the user is informed and the program returns to the main menu.
When a user asks to encrypt a message, the program checks if a key pair exists for the given username. If yes, the existing information is used to encrypt any new messages. If not, keys are generated on the fly and they are then used to encrypt the message.

