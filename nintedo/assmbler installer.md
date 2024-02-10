
#### assmbler installer

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
         * simple download
        ```
        git clone https://github.com/cc65/cc65.git
        cd cc65
        make
        ```
    * download zip file.

     -----
  
    * This builds both the cc65 binaries (located in the 'bin' subdirectory) and the cc65 libraries. Now cc65 is fully functional without further steps.
    * If you want to be able to run the cc65 binaries without providing their path you may additionally enter sudo make avail. This creates symbolic links to the binaries in '/usr/local/bin'.

       -----
    * Install one of the pre-built packages for RPM based systems (OpenSUSE) or DEB based systems (Debian) from the openSUSE Build Service download page for cc65. 
      * openSUSE Build Service download page for cc65.
        * https://software.opensuse.org/download.html?project=home%3Astrik&package=cc65
        * For xUbuntu 23.04 run the following:
          * Keep in mind that the owner of the key may distribute updates, packages and repositories that your system will trust (more information).
          ```
          echo 'deb http://download.opensuse.org/repositories/home:/strik/xUbuntu_23.04/ /' | sudo tee /etc/apt/sources.list.d/home:strik.list
          curl -fsSL https://download.opensuse.org/repositories/home:strik/xUbuntu_23.04/Release.key | gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/home_strik.gpg > /dev/null
          sudo apt update
          sudo apt install cc65
          ```
        * For xUbuntu 22.10 run the following:
          *  Keep in mind that the owner of the key may distribute updates, packages and repositories that your system will trust ([more information](https://help.ubuntu.com/community/SecureApt)).
          ```
          echo 'deb http://download.opensuse.org/repositories/home:/strik/xUbuntu_22.10/ /' | sudo tee /etc/apt/sources.list.d/home:strik.list
          curl -fsSL https://download.opensuse.org/repositories/home:strik/xUbuntu_22.10/Release.key | gpg --dearmor | sudo tee /etc/apt/trusted.gpg.d/home_strik.gpg > /dev/null
          sudo apt update
          sudo apt install cc65
          ```
