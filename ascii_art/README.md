# Ascii Art

Implementation of brightnesses in "BrightnessImgCharMatcher" class:

--- private method - "charsArrayToBrightnessArray" ---
The function Takes array of chars and return array of the brightnesses of each char in the input array.
This function uses the private function "numOfTrueCells".
Actually, we take a char and by using the function "getImg" from the class "CharRenderer" we get a 2d
array that contain true or false boolean object in each cell.
When we get this array we count the number of the true cells and divide it by the number
of (image-pixels)^2. This is according to the instructions we have got.

--- private method - "newCharBrightness" ---
Takes the array that created from the "charsArrayToBrightnessArray" and "stretch" it exactly
according to the formula in the pdf.

--- private method - "convertImageToAscii" ---
The function takes an image and iterates on it sub-images.
For any sub-image we find it brightness with the "calculateImageBrightness" (private methods that
works according the instructions, for every pixel in the sub-image we calculate the compatible grey pixel
and follows the formula), and then we find the char that has the closest brightness to sub-image brightness
with the private function "findingIndexOfIdealChar".
"findingIndexOfIdealChar" gets a brightness and passing on the charSet. for any char in the charSet
we check if it's brightness is the closest so far and if it is, we save it in a field.
finally we return the field/index.
I chose to implement it this way because this operation done in O(1) due to constant number of chars.
According to that, sorting the arrays not necessarily be needed here.
In addition its written in the forum that there is no must to sort the arrays.
What's left is to insert the char to the Asci-image array.
And at the end we return the Asci-image array.


Usage of Collections in the class "Shell":

I created an HashSet called charSet which contains all the chars that the user decided to add.
The charSet is initialized to 0-9 according to instructions.
I chose to work with this collection because I wanted to prevent rehearsals of a single char.
In addition all the relevant operations we were needed to do in this class are performed O(1) -
e.g. search, add, remove...

Usage of Collections in the class "BrightnessImgCharMatcher":
I created an HashMap called cache that represented by (key - Image object, value - Double), so the value
is the brightness image. With this implementation we can add every image that we pass on to cache,
and by doing that we can remember calculations that we already did and use them instead of calculate them
again. We can check if the brightness of the image was already calculated by using the function containsKey
in complexity time of O(1), what makes our usage very efficient.


Algorithms:

findDuplicate:
This algorithm is totally based on the "cycle finding algorithm" of floyd.
With a little changes from dealing with linked lists(in the original algorithm) I wrote an algorithm
That solving our problem.
If we look on the time complexity - There are two while loops that can take in the worst case O(n),
that because we stop running the loops when we find duplicate and it's happen when we found a cycle -
meaning O(n). The other operations takes O(1).
Space complexity - we saving the calculation in a several constant fields, and we gets O(1).

uniqueMorseRepresentations:
I created an HashMap called morseCharMap that represented by (key - Character, value - String), so the string
is the morse code of this key/char. When we convert a word to morse code we can search in O(1) the value of
each char in the current word in the morseCharMap.
In addition for any word in the input array that we convert to morse code I check if it is already exist
in the field morseWordsSet (a set of Strings/words) which hold all the unique words.
In order to check if the current word is unique I just need to use the function morseWordsSet.contains
that preformed in O(1). If it's in the set we don't count it as a unique word otherwise we count it and
add it to the set.
Time complexity - initialize morseCharMap takes O(1) due to constant number in the alphabet.
In addition we pass on every word in the array (takes O(n)) and make an operations in O(1) thanks to the
collections we chose.(operations like search, add).

*************** I attached an HTML file of a funny morse code image, hope you'll enjoy!!! *************
