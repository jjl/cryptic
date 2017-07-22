curl -sL https://deb.nodesource.com/setup_6.x | bash -
apt-get install -y nodejs build-essential
.circleci/install_boot.sh
BOOT_AS_ROOT=yes BOOT_JVM_OPTIONS=-Xmx3200m ./boot testing
