LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit update-rc.d

INITSCRIPT_NAME = "aesdsocket"
INITSCRIPT_PARAMS = "start 99 2 3 4 5 . stop 20 0 1 6 ."

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignments-3-and-later-sk614.git;protocol=ssh;branch=main"

PV = "1.0+git${SRCPV}"
SRCREV = "cffb8872fa18fa7e6ca0b40c3a65f48f5dc0c342"

S = "${WORKDIR}/git/server"

FILES:${PN} += "${bindir}/aesdsocket"
FILES:${PN} += "${sysconfdir}/init.d/aesdsocket"

CFLAGS:append = " -pthread"
LDFLAGS:append = " -pthread"

do_configure () {
    :
}

do_compile () {
    oe_runmake clean
    oe_runmake \
        CC="${CC}" \
        CFLAGS="${CFLAGS}" \
        LDFLAGS="${LDFLAGS}"
}

do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${S}/aesdsocket ${D}${bindir}/aesdsocket

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${S}/aesdsocket-start-stop \
        ${D}${sysconfdir}/init.d/aesdsocket
}
