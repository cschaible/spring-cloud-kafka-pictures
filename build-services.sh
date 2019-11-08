#!/bin/bash
shopt -s expand_aliases
source "$(dirname ${BASH_SOURCE[0]})/build-utils.sh"

progress "BUILD COMMON"
build_publish "services/common"

progress "BUILD CONFIG"
build "services/config"

progress "BUILD EUREKA"
build "services/eureka"

progress "BUILD GATEWAY"
build services/gateway

progress "BUILD SEARCH SERVICE"
build "services/search-service"

progress "BUILD RESIZE SERVICE"
build "services/resize-service"

progress "BUILD STORAGE SERVICE"
build "services/storage-service"

progress "BUILD USER SERVICE"
build "services/user-service"

progress "BUILD COMMENT SERVICE"
build "services/comment-service"

progress "BUILD LIKE SERVICE"
build "services/like-service"
