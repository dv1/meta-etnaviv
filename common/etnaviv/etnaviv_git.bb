SUMMARY = "Etnaviv open-source 3D GPU drivers for Vivante GPUs"
SECTION = "graphics"
LICENSE = "MIT & GPLv2 & GPLv3"
HOMEPAGE = "https://github.com/laanwj/etna_viv"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ba2fcc0480b365fcee43b28a0fa420e9"

DEPENDS = "virtual/egl virtual/libgles2"

SRC_URI = "git://github.com/laanwj/etna_viv.git;branch=master"

SRCREV = "29b464caed876a7ae2bef568f14898a8180e2535"

S = "${WORKDIR}/git"

PR = "r1"
PV = "git${SRCPV}"


ETNAVIV_EXTRA_CFLAGS = "-D_POSIX_C_SOURCE=200809 -D_GNU_SOURCE -pthread"
ETNAVIV_EXTRA_LDFLAGS = "-ldl -lpthread -pthread"
ETNAVIV_GL_LIBS ?= "-lEGL -lGLESv2"
ETNAVIV_GL_LDFLAGS ?= "-Xlinker --allow-shlib-undefined"

# TODO: this should be set in BSP specific .bbappend files, and not here
# remove or comment out this line once at least one BSP specific .bbappend exists
ETNAVIV_GCABI ?= "dove"


PARALLEL_MAKE = ""


ETNAVIV_EXECUTABLES = " \
	egl/cube \
	egl/cube_companion \
	egl/cube_companion_idx \
	egl/displacement \
	egl/eglinfo \
	egl/ps_sandbox \
	egl/simple_texture_cubemap \
	fb/alpha_blend \
	fb/cube_companion \
	fb/cubemap_sphere \
	fb/displacement \
	fb/etna_gears \
	fb/mip_cube \
	fb/particle_system \
	fb/rotate_cube \
	fb/stencil_test \
	gallium/test \
"

ETNAVIV_STATIC_LIBS = " \
	gallium/libminigallium.a \
"

ETNAVIV_DIR = "/opt/etnaviv"

EXTRA_OEMAKE = " \
	'CC=${CC}' \
	'CXX=${CXX}' \
	'LD=${LD}' \
	'AR=${AR}' \
"

do_configure() {
}

do_compile() {
	export PLATFORM_CFLAGS="${ETNAVIV_EXTRA_CFLAGS}"
	export PLATFORM_CXXFLAGS="${ETNAVIV_EXTRA_CFLAGS}"
	export PLATFORM_LDFLAGS="${ETNAVIV_EXTRA_LDFLAGS}"
	export PLATFORM_GL_LIBS="${ETNAVIV_GL_LIBS} ${ETNAVIV_GL_LDFLAGS}"
	export GCABI="${ETNAVIV_GCABI}"
	cd "${S}/native"
	oe_runmake
}

do_clean() {
	if [ -d "${S}/native" ]
	then
		cd "${S}/native"
		oe_runmake clean
	fi
}

do_install() {
	DESTDIR="${D}${ETNAVIV_DIR}"
	install -d "${DESTDIR}"
	install -d "${DESTDIR}/egl"
	install -d "${DESTDIR}/fb"
	install -d "${DESTDIR}/gallium"

	for executable in ${ETNAVIV_EXECUTABLES}
	do
		install -m 0755 "${S}/native/${executable}" "${DESTDIR}/${executable}"
	done
	for static_lib in ${ETNAVIV_STATIC_LIBS}
	do
		install -m 0644 "${S}/native/${static_lib}" "${DESTDIR}/${static_lib}"
	done
}

FILES_${PN} = " \
	${ETNAVIV_DIR}/egl/* \
	${ETNAVIV_DIR}/fb/* \
	${ETNAVIV_DIR}/gallium/test \
"

FILES_${PN}-staticdev = " \
	${ETNAVIV_DIR}/gallium/*.a \
"

FILES_${PN}-dbg = " \
	${prefix}/src \
	${ETNAVIV_DIR}/egl/.debug \
	${ETNAVIV_DIR}/fb/.debug \
	${ETNAVIV_DIR}/gallium/.debug \
"
