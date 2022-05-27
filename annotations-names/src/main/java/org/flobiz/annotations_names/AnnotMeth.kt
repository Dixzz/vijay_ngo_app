package org.flobiz.annotations_names

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class AnnotMeth(
    val permissions: Array<String>,
    val requestCode: Int
)