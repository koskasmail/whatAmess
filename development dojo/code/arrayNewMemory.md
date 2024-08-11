<a name="topage"></a>

# array New Memory


### New Memory 

```
require([
  'dojo/store/Memory',
  'dojo/domReady!'
], function(Memory) {
  // Step 1: Create a Memory store
  var Arrlist = new Memory({
    data: []
  });

  // Step 2: Function to add records
  function addRecord(label, value) {
    Arrlist.put({ id: Arrlist.data.length + 1, label: label, value: value });
  }

  // Step 3: Add 3 records
  addRecord("121", 121);
  addRecord("121", 121);
  addRecord("121", 121);

  // Step 4: Log the store data to the console
  console.log(Arrlist.data);
});

var arrList = new Memory ({
  data: []
});

console.log (arrList);
```


-----

<p align="right">(<a href="#topage">back to top</a>)</p>
<br/>
<br/>
