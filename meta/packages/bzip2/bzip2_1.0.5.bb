DESCRIPTION = "Very high-quality data compression program."
SECTION = "console/utils"
PR = "r1"

LICENSE = "bzip2"
SRC_URI = "http://www.bzip.org/1.0.5/bzip2-1.0.5.tar.gz \
           file://configure.ac \
	   file://Makefile.am"

CFLAGS_append = " -fPIC -fpic -Winline -fno-strength-reduce -D_FILE_OFFSET_BITS=64"

inherit autotools

do_configure_prepend () {
	cp ${WORKDIR}/configure.ac ${S}/
	cp ${WORKDIR}/Makefile.am ${S}/
	cp ${STAGING_DATADIR_NATIVE}/automake*/install-sh ${S}/
}

do_install_append () {
	if [ "${BUILD_ARCH}" != "${HOST_ARCH}" ]; then
		mv ${D}${bindir}/bunzip2 ${D}${bindir}/bunzip2.${PN}
		mv ${D}${bindir}/bzcat ${D}${bindir}/bzcat.${PN}
	fi
}

pkg_postinst_${PN} () {
	update-alternatives --install ${bindir}/bunzip2 bunzip2 bunzip2.${PN} 100
	update-alternatives --install ${bindir}/bzcat bzcat bzcat.${PN} 100
}


pkg_prerm_${PN} () {
	update-alternatives --remove bunzip2 bunzip2.${PN}
	update-alternatives --remove bzcat bzcat.${PN}
}

PROVIDES_append_virtclass-native = " bzip2-full-native"
BBCLASSEXTEND = "native"
