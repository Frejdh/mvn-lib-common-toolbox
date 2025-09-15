package com.frejdh.util.common

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * Returns the opposite of [isNullOrBlank].
 * @see isNullOrBlank
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNotNullOrBlank(): Boolean {
    contract {
        returns(true) implies (this@isNotNullOrBlank != null)
    }

    return !this.isNullOrBlank()
}

/**
 * Return whether this boolean is considered `true` (with null-check).
 */
@OptIn(ExperimentalContracts::class)
fun Boolean?.isTrue(): Boolean {
    contract {
        returns(true) implies (this@isTrue != null)
    }

    return this == true
}

/**
 * Return whether this boolean is considered `false` (with null-check).
 */
@OptIn(ExperimentalContracts::class)
fun Boolean?.isFalse(): Boolean {
    contract {
        returns(true) implies (this@isFalse != null)
    }

    return this == true
}

/**
 * Return whether this boolean is considered `false` or `null`.
 */
fun Boolean?.isFalsy(): Boolean {
    return this == null || !this
}

