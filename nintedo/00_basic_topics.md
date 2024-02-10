
### nintendo nes (nis) programming environment.

#### Herramientas necesarias 
* editor
   * vscode
* ensamblador
* emulador

----

#### vscode
* install vscode
* addons
   *  ca65 Macro Assembler Language Support (6502/65816)

----

#### assembler installation
* convert plain text assembly code into binary machine code.
   * <.asm> into <.nes>
* modern assemblers support
  * macro
  * functions
  * scopes
  * includes
* assembling compiler
  * ca65
     * https://github.com/cc65/cc65
     * https://cc65.github.io/

* enter the site
   * https://cc65.github.io/
      * click the link "Getting started"
      * read about installation in: "assmbler installer.md" 
* unzip "cc65-snapshot-win32.zip" into local [c:\c65\] or ["\home\youName\cc65"\]

----

* amulator installation
   * amulator installation.md

------

* first program

#### 01.sample.s

``` 
lda $01
sta $02
```

* save file extensions: <.s>
   *  <.s> is 6502 assembly

---

* code examples:
  * https://github.com/NesHacker/DevEnvironmentDemo.git

---

* vs code config
   * vs code ==> "ctrl"+"shift"+p
      * configure default build taks
         * ca65: build without config
         * created a file: ["tasks.json"]

```
        {
	"version": "2.0.0",
	"tasks": [
		{
			"type": "ca65",
			"problemMatcher": [
				"$ca65",
				"$ld65",
				"$ld65-unresolved",
				"$ld65-config"
			],
			"group": {
				"kind": "build",
				"isDefault": true
			},
			"label": "ca65: Build without config"
		}
	]
}
```
        
----

* execute
  * press <ctrl>+<shift>+<p>
     * run build task

----

* youtube - view
   * https://www.youtube.com/watch?v=RtY5FV5TrIU







  
  


