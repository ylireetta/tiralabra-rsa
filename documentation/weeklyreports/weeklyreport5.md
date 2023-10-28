# Weekly report 5
I haven't had much time to work on this project this week, which is why the peer review process and the improvement possibilities it provided was greatly appreciated. The report and the screenshots provided by the person who reviewed my project were excellent, and led me to discover a bug in my code! It was a bit of a hassle to fix it, but I think I managed in the end. I also realized that I had neglected to test a couple of methods in the `FileHelper` class, because JaCoCo highlights the lines green if a call to another method results in the line being executed. I wrote a couple of new tests for `FileHelper`, but I still need to make sure that the code in other classes is thoroughly tested, as well.

All in all, the peer review process was very helpful. It made me look at the material I have created through a different lens, and I think e.g., the updated instructions on how to run the program are clearer now after the peer review. It also forced me to take a closer look at the different commands used with Maven and what they actually do.

When I wrote the initial project specification in the beginning of the course, I thought I might implement a binary search algorithm to use when retrieving user key files. This week I finally wrote the code, but I don't think it makes sense to actually use it, since the amount of files I am working with is so small. On the other hand, maybe I should consider the worst case scenario and write code that would (probably) work smoothly even if the amount of files was significantly larger.

I also ran into some very frustrating issues due to the file names I have at hand. I have a few files I use on my local machine when running the program, and the file names as of now are
```
0: apina1
1: apina2
2: apina7
3: halou2
4: helouu
5: hoopo
6: rapu
7: rapurapurallaa
8: reetta2
9: reetta3
10: reetta5
11: reetta
12: reettax
13: testi8
```

Note the order of the file names - after calling `Arrays.sort`, "reetta5" comes before "reetta", and this completely throws off my sorting algorithm on the second iteration. When "reetta5" is compared to "reetta", the index `high` is moved to point to the left of "reetta", which should not happen. Before the first iteration of the binary search, the helper indexes are `low` = 0, `high` = length of the list - 1 = 13, `middle` = 0.

- First iteration
    - middle = low + ((high - low) / 2) = 0 + ((13 - 0) / 2) = *6*
    - File name at index middle: rapu_public_key.txt
    - rapu_public_key.compareTo(reetta) < 0, so:
        - low = middle + 1 = *7*
- Second iteration **wrong**
    - middle = low + ((high - low) / 2) = 7 + ((13 - 7) / 2) = *10*
    - File name at index middle: reetta5_public_key.txt
    - reetta5_public_key.compareTo(reetta) > 0, so:
        - high = middle - 1 = *9*
- Third iteration
    - middle = low + ((high - low) / 2) = 7 + ((9 - 7) / 2) = *8*
    - File name at index middle: reetta2_public_key.txt
    - reetta2_public_key.compareTo(reetta) > 0, so:
        - high = middle - 1 = *7*
- End iterating, since low == high

I'll have to look into this during the next week. Luckily my test data is named so imaginatively that I ran into trouble right at the beginning!