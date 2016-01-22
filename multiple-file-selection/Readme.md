


Testing the results!

```bash
#/bin/bash!

for((i=0;i<${TEST_size};i+=1))
    do
      VAL="TEST_${i}"
      echo $VAL
      echo " FILE ====>>" `eval echo \$\{$VAL\}`
    done
```