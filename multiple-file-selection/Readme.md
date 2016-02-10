
# Jenkins Plugin: multiple file selection

This Jenkins plugin allow a multiple selection of files as input parameter. 

The selection can be done among all the files included in the input RegExp and located in one of the folders listed in the input "paths" list.  


The selected file paths are then loaded into the job environment as string variable.

## Example 

 Given a NAME of the parameter and a list of string like: {"alice.txt", "bob.war", "34.md"},  they are exported as:
 * NAME_0 ="alice.txt"
 * NAME_1 ="bob.war"
 * NAME_2 ="34.md"
 * NAME_SIZE = 3
 
If the list is empty, NAME_SIZE = 0 

It is also possible to use the full path in the exported string.  


## Testing the results!

Add an  "Execute shell" step to the job with: 

```bash
#/bin/bash!

echo  "LOOP "

NAME="TEST"   << put here the name you have given to the variable

NN="${NAME}_SIZE"

for((i=0;i<$NN;i+=1))
    do
      VAL="${NAME}_${i}"
      echo " SELECTED FILE ====>>" `eval echo \$\{$VAL\}`
    done
```