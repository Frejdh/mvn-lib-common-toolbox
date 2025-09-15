package com.frejdh.util.common.toolbox

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * @see CommonUtils.isNumeric
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNumeric(): Boolean {
    contract {
        returns(true) implies (this@isNumeric != null)
    }

    if (this == null) {
        return false
    }

    return CommonUtils.isNumeric(this.toString())
}

/**
 * @see CommonUtils.softMerge
 */
fun <T : Any> T.softMerge(other: T): T {
    return CommonUtils.softMerge(this, other)
}

/**
 * @see CommonUtils.softMergeInverse
 */
fun <T : Any> T.softMergeInverse(other: T): T {
    return CommonUtils.softMergeInverse(this, other)
}

/**
 * @see CommonUtils.softMerge
 */
fun <T : Any> T.overrideMerge(other: T): T {
    return CommonUtils.overrideMerge(this, other)
}

/**
 * @see CommonUtils.stringToList
 */
fun CharSequence?.toList(separator: String = ","): List<CharSequence> {
    return CommonUtils.stringToList(this?.toString() ?: "", separator)
}
