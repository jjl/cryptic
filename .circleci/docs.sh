apt-get update
apt-get -y install curl build-essential git-core
curl -sL https://deb.nodesource.com/setup_6.x | bash -
apt-get install -y nodejs
git checkout -b gh-pages
.circleci/install_boot.sh
export BOOT_AS_ROOT=yes
./boot testing
./boot codox target
mv -f target/doc/ ./
find . -mindepth 1 -maxdepth 1 -type f | xargs rm -f
mv -f doc/* .
rmdir doc
git config user.name "CircleCI"
git config user.email "docs@circleci"
git add -A .
git commit -m "Rebuild website documentation"
openssl aes-256-cbc -d -in docs_rsa.env -out $HOME/.ssh/docs_rsa -k $DOCSKEY
git push -f origin gh-pages
