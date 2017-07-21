git checkout -b gh-pages
.circleci/install_boot.sh
export BOOT_AS_ROOT=yes
export BOOT_JVM_OPTIONS=-Xmx3200m
./boot testing
./boot codox target
mv -f target/doc/ ./
find . -mindepth 1 -maxdepth 1 -type f | xargs rm -f
mv -f doc/* .
rmdir doc
