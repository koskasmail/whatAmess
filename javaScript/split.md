# split

#### split text

```
function test() {

    let text = 'Some text \',"First string"\' more text \',"Second string"\' even more \',"Third string"\'';
    let regex = /\"\",(.*?)\"\"/g; // Matches content between `\",` and `\"`
    let extractedStrings = [...text.matchAll(regex)].map(match => match[1]); // Extract and store matches in array

    console.log(extractedStrings);
}

test();
```
