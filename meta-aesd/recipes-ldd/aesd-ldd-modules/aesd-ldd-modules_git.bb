LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-sk614.git;protocol=ssh;branch=main \
           file://lddmodules \
"

SRCREV = "630229e790eb1638aa7f9a8e3a0488b0430c3556"

PV = "1.0+git${SRCPV}"

S = "${WORKDIR}/git"

inherit module update-rc.d

INITSCRIPT_NAME = "lddmodules"
INITSCRIPT_PARAMS = "start 98 S . stop 02 0 1 6 ."

FILES:${PN} += "${sysconfdir}/init.d/lddmodules"
FILES:${PN} += "${sysconfdir}/init.d/S98lddmodules"

do_compile() {
    unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS

    oe_runmake -C ${S}/misc-modules \
        KERNELDIR=${STAGING_KERNEL_DIR} \
        CC="${KERNEL_CC}" \
        LD="${KERNEL_LD}" \
        AR="${KERNEL_AR}" \
        O=${STAGING_KERNEL_BUILDDIR}

    oe_runmake -C ${S}/scull \
        KERNELDIR=${STAGING_KERNEL_DIR} \
        CC="${KERNEL_CC}" \
        LD="${KERNEL_LD}" \
        AR="${KERNEL_AR}" \
        O=${STAGING_KERNEL_BUILDDIR}
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

    install -m 0644 ${S}/misc-modules/hello.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/hello.ko

    install -m 0644 ${S}/misc-modules/faulty.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/faulty.ko

    install -m 0644 ${S}/scull/scull.ko \
        ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/scull.ko

    install -d ${D}${sysconfdir}/init.d

    install -m 0755 ${WORKDIR}/lddmodules \
        ${D}${sysconfdir}/init.d/lddmodules

    ln -sf lddmodules \
        ${D}${sysconfdir}/init.d/S98lddmodules
}

RDEPENDS:${PN} += "kernel-module-hello kernel-module-faulty kernel-module-scull"
