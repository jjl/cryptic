set -e
curl -fsSLo boot "https://github.com/boot-clj/boot-bin/releases/download/${BOOT_SH_VERSION:-2.5.2}/boot.sh"
chmod 755 boot
