# Implementation document

## Should contain the following

- Project structure
- Implemented time and space complexities (big-O complexity analysis of (pseudo)code)
- Comparative performance and complexity analysis if applicable
- Possible flaws and improvements
- The use of large language models. **The project should always include a statement on how such models have been used, even if they have not.**
- References

## Project structure
The program logic is divided into several classes.
- `UserInteface`: handles user interaction and validates input.
- `KeyGenerator`: tool that generates public and private keys using the methods provided by `PrimeHelper`.
- `PrimeHelper`: tool that generates (probable) primes using a myriad of helper methods.
- `FileHelper`: handles file operations when writing user keys to files and reading file contents.
- `EncryptionHelper`: encrypts a message using the public key of the recipient.
- `DecryptionHelper`: decrypts a message using the private key of the recipient.

In addition, some basic structures have been created to hold the information related to user keys.
- `UserKey`: interface to allow looping through a list that contains both public and private keys.
- `PublicKey`: implements the `UserKey` interface. Holds the public exponent and modulus.
- `PrivateKey`: implements the `UserKey` interface. Holds the private exponent and modulus.

## Time and space complexities
TODO.

## Possible flaws and improvements
TODO.

## Use of language models
I have used (or tried to use) ChatGPT at multiple stages of this project. In the beginning, I asked whether my thought process was on the right track (i.e., am I right in thinking that I have to implement algorithms x, y, and z to successfully implement some other algorithm). I have also asked ChatGPT to explain the maths behind the algorithms in simple terms and with examples, because 1) I am not good at mathematics and 2) working with BigIntegers makes it impossible to understand what is going on while running the program.

ChatGPT has proven to be quite useful with the tasks mentioned above, but anything more complex than that, and it fails miserably. I found it difficult to come up with useful test data for `PrimeHelper` and asked ChatGPT if it could provide some large integers I could use to test the helper methods of that class. Alas, _none_ of the values it provided worked, and I learned that language models are indeed _language_ models, not calculators.
![alt text](chatgpt.png "ChatGPT calculation")

## References
TODO.