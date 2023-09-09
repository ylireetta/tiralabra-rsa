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
    - Program input 1 = sender's username 
    - Program input 2 = recipient's username
    - Program input 3 = the message to encrypt
    - If there is no key pair for the recipient yet:
        - The program generates a public and a private key for the user, using program input 1 (for possible future use)
        - The sender's key pair is stored in text files (separate file for each key)
    - Encryption:
        - The program searches for a text file containing the public key of the recipient using program input 2 (e.g., recipientname_public_key.txt)
        - If a public key is found, it is used to encrypt the message (program input 3)
        - If a public key is *not* found, the key pair for the recipient is generated on the fly, written in text files, and the public key is used to encrypt the message (program input 3)
    - The output is the RSA encrypted message

2. Message decryption
    - Program input 1 = current user's username (i.e., the recipient)
    - Program input 2 = the message to decrypt
    - The program searches for a text file containing the private key of the recipient using program input 1 (e.g., recipientname_private_key.txt)
    - If found, the private key located in the text file is used for message decryption
    - The output is the decrypted message

Usernames need to be unique for the text file system to work, so the program contains some sort of validation when a user asks to encrypt a message. If a key pair for the given username already exists, the existing information is used to encrypt any new messages.

