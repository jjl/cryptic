set -e
apt-get update
apt-get -y install curl build-essential git-core
curl -sL https://deb.nodesource.com/setup_6.x | bash -
apt-get install -y nodejs
.circleci/install_boot.sh
export BOOT_AS_ROOT=yes
export BOOT_JVM_OPTIONS=-Xmx3200m
./boot testing
