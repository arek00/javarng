#!/bin/sh
FILE=javarng-1.0.2
rm -rf $FILE
mkdir $FILE
cp -r com $FILE
find $FILE -name '.svn' -print | xargs rm -rf
find $FILE -name '*~' -print  | xargs rm -f
find $FILE -name '\#*' -print | xargs rm -f

tar -czvf ${FILE}.tar.gz $FILE
rm -rf ${FILE}
