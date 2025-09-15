package com.frejdh.util.common.toolbox

import com.frejdh.util.common.isNotNullOrBlank
import java.io.File


/**
 * Replace the filename with characters considered safe for Windows and Unix based operating systems.
 * @see OperatingSystemUtils.replaceIllegalFilenameCharacters
 */
fun CharSequence.replaceIllegalFilenameCharacters(): CharSequence {
    return OperatingSystemUtils.replaceIllegalFilenameCharacters(this.toString())
}

/**
 * Replace the filename of a [File] with characters considered safe for Windows and Unix based operating systems.
 * Parent directory is kept intact.
 * @see OperatingSystemUtils.replaceIllegalFilenameCharacters
 */
fun File.replaceIllegalFilenameCharacters(): File {
    return OperatingSystemUtils.replaceIllegalFilenameCharacters(this)
}
