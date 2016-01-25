


Testing the results!

```bash
#/bin/bash!

echo  "LOOP "

NAME="TEST"   << put here the name you have given to the variable

NN="${NAME}_SIZE"

echo "NN=$NN"

for((i=0;i<$NN;i+=1))
    do
      VAL="${NAME}_${i}"
      echo $VAL
      echo " SELECTED FILE ====>>" `eval echo \$\{$VAL\}`
    done
```