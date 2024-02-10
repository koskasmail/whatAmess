
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

* first program

#### 01.sample.s

``` 
lda $01
sta $02
```

* save file extensions: <.s>
   *  <.s> is 6502 assembly

---





