  set -x
  if [[ ! -f ./boot ]]; then
    curl -fsSLo boot "https://github.com/boot-clj/boot-bin/releases/download/${BOOT_SH_VERSION:-2.5.2}/boot.sh" || return $?
    chmod 755 boot || return $?
  fi
  set +x
