#/bin/bash

set -e
cd /home/core/

# get pypy
PYPY_VERSION=2.4.0
DIRNAME=pypy-$PYPY_VERSION-linux64
ARCHIVE=${DIRNAME}.tar.bz2

if [ ! -e $ARCHIVE ]
then
    wget https://bitbucket.org/pypy/pypy/downloads/$ARCHIVE
fi

# checksum archive
echo "c362247226f1cde2b957ab5e885f41475381553b pypy-2.4.0-linux64.tar.bz2" | sha1sum -c

# install it in /usr/local, overwriting files if they exist
tar -xf $ARCHIVE

# symlink to a current filename:
if [ "$(readlink pypy)" != "$DIRNAME" ]
then
    if [ -h pypy ]
    then
        rm pypy
    fi
    ln -s $DIRNAME pypy
fi


# create a symlink to systems ncurses lib
if [ ! -d pypy/lib ]
then
  mkdir pypy/lib
fi

if [ ! -h pypy/lib/libtinfo.so.5 ]
then
  ln -s /lib64/libncurses.so.5.9 pypy/lib/libtinfo.so.5
fi


# we need a local bin dir in PATH
if [ ! -d bin ]
then
  mkdir bin
fi

# have a python wrapper in bin:
cat > bin/python <<EOF
#!/bin/bash
LD_LIBRARY_PATH=/home/core/pypy/lib:$LD_LIBRARY_PATH /home/core/pypy/bin/pypy "\$@"
EOF

chmod +x bin/python
bin/python --version


