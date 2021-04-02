## Additional PDOM type information export and import

The leaked symbols in the `patch.elf` or `.symdefs` format only contain function and
global variable names. However, there are also a couple `.pdom` Eclipse project files
in WICED Studio 6.2 and 6.4. These contain additional type information. Eclipse uses them
internally for auto completion, function search, etc., and we can utilize them in reverse
to add type information. Since `.pdom` files only contain partial, cached information, it
can be helpful to combine multiple of them.

In a first step, we export `.pdom` type information into an SQLite database. The export
takes a while, but it can even be aborted and continued later on. Export works as follows:

```
java -jar pdom/export/export.jar -P BCM20739-B0.1462220149391.pdom
```

The PDOM import searches for function names in an IDA database, looks them up in the PDOM to search
for type information, and then applies that type information into the IDA database. Thus, the IDA
database needs to contain correct function names in advance. In principle, these can be created with
the [import_export](import_export) scripts from Polypyus. However, the somewhat more advanced scripts
that support PDOM import can also handle `patch.elf` sections. Run the importer as follows:

* Open the firmware binary in IDA.
* Set Thumb mode to `T=0x1` (Alt-g).
* Set the compiler options (Options -> Compiler...) to GNU C++.
* Run the script file [pdom/import/main.py](pdom/import/main.py) (File -> Script file).
* Select an `patch.elf` file (Select file).
* Import it (Import ELF). After a few seconds you will have sections and function names.
* Select a reference database, which should be the PDOM that belongs to your firmware binary.
* Select multiple additional databases, and the importer will pick the best combined matches.
* Import it (Import PDOM). This will take a while.
* You can also import a hardware register file `20739mapb0.h` to name hardware registers (Import map.h).

This script was tested on IDA Pro 7.4 and 7.5.