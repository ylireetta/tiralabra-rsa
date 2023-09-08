# Project specification
Degree programme: bachelor's in computer science (CS).

This project depicts the implementation of the [RSA algorithm](https://en.wikipedia.org/wiki/RSA_(cryptosystem) "RSA in Wikipedia").

For the sake of consistency, both the code and the documentation is written in English. The programming language chosen for this project is Java. In addition, I know C#, Python, and JavaScript to such a degree that I should be able to peer review projects written in those languages.

## Program use cases
Users provide the program input. The user inputs a message they want to either encrypt or decrypt, as well as their username. The usernames are needed for storage purposes: when the program generates key pairs (i.e., public and private keys), they are stored in text files which are distinguished by including the username in the file name. *Please note that the security aspect of storing the data is not the focus point in this project, and the text files are used just for simplicity*.

There are two use cases:
1. Message encryption
- Program input = username and the message to encrypt
- The program generates a public and a private key for the user
- The key pair is stored in a text file (separate file for each key). For later user, the username is included in the file name
- The program encrypts the message
- The output is the RSA encrypted message

2. Message decryption
- Program input = sender username and the message to decrypt
- The program searches for private key text files that contain the sender username in the file name
- If found, the private key located in the text file is used for message decryption
- The output is the decrypted message

Usernames need to be unique for the text file system to work, so the program contains some sort of validation when a user asks to encrypt a message. If a key pair for the given username already exists, the existing information is used to encrypt any new messages.

## Should contain the following:
- What data structures and algorithms will you be using

- What problem are you solving and why did you choose these specific data structures and algorithms

- What is the program input and how will it be used :white_check_mark:

- Expected time and space complexities of the program (big-O notations)

- Sources

- Due to administrative practicalities you should also mention your degree programme in the Project Specification. For example, bachelor’s in computer science (CS) or bachelor’s in science (bSc) :white_check_mark:

- You should also mention the documentation language you are going to use and have all code, comments and documentation written in this language. Typically Finnish or English. This requirement is due to the code reviews done around the half way point of the course. This hopefully helps keep the internal language of the project consistent. :white_check_mark:

- The specification document is a tool to be used in the beginning of the course that need not be updated during the course unless the course staff explicitly ask for specifications. 

- Note that the assistant has approved the project topic based on the specification document. As such a final submitted project that does not cover the specification document will most likely not be sufficient for passing the course.
