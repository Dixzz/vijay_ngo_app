package org.flobiz.lib

import com.squareup.javapoet.*
import org.flobiz.annotations_names.Annot
import org.flobiz.annotations_names.AnnotMeth
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

class MyClass : AbstractProcessor() {

    private fun isFieldAccessible(element: Element): Boolean {
        val modifiers: Set<Modifier> = element.modifiers
        if (modifiers.isEmpty()) return false
        return !modifiers.contains(Modifier.PRIVATE) && !modifiers.contains(Modifier.STATIC)
    }

    private fun isMethodAccessible(element: Element): Boolean {
        val modifiers: Set<Modifier> = element.modifiers
        if (modifiers.isEmpty()) return false

        return !modifiers.contains(Modifier.PRIVATE) && !modifiers.contains(Modifier.STATIC)
    }

    override fun process(s: Set<TypeElement?>, roundEnvironment: RoundEnvironment): Boolean {
        val set = roundEnvironment.getElementsAnnotatedWith(Annot::class.java)

        set.forEach {
            processingEnv.messager.printMessage(
                javax.tools.Diagnostic.Kind.MANDATORY_WARNING,
                "Found annotated element: ${it.simpleName}, kind: ${it.kind}, type: ${it.asType()}, modifiers: ${it.modifiers}, isFieldAccessible: ${
                    isFieldAccessible(
                        it
                    )
                }, enclosingElement: ${it.enclosingElement}, enclosingElementKind: ${it.enclosingElement?.kind}, enclosingElementType: ${it.enclosingElement?.asType()}, enclosingElementModifiers: ${it.enclosingElement?.modifiers} "
            )
            val methodBuilder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(ClassName.get(it.enclosingElement.asType()), "instance")
            //.returns(ClassName.get(it.asType()))

            if (isFieldAccessible(it)) {
                methodBuilder.addStatement("instance.${it.simpleName} = \"ohBoy\"")

                val spec = methodBuilder.build()
                val typeSpec = TypeSpec.classBuilder("MyClassNew")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(spec)
                    .build()
                val javaFile = JavaFile.builder("org.flobiz.lib", typeSpec).build()
                javaFile.writeTo(processingEnv.filer)
            }
            //val z = it.getAnnotation(Annot::class.java)
        }
        val set2 = roundEnvironment.getElementsAnnotatedWith(AnnotMeth::class.java)
        set2.forEach {
            processingEnv.messager.printMessage(
                Diagnostic.Kind.MANDATORY_WARNING,
                "Found annotated element2: ${it.simpleName}, kind: ${it.kind}, type: ${it.asType()}, modifiers: ${it.modifiers}, isMethodAccessible: ${
                    isMethodAccessible(
                        it
                    )
                }, enclosingElement: ${it.enclosingElement}, enclosingElementKind: ${it.enclosingElement?.kind}, enclosingElementType: ${it.enclosingElement?.asType()}, enclosingElementModifiers: ${it.enclosingElement?.modifiers}"
            )
            val value = it.getAnnotation(AnnotMeth::class.java).permissions.toList()
            val reqCode = it.getAnnotation(AnnotMeth::class.java).requestCode

            if (isMethodAccessible(it)) {
                val ctxCompat = ClassName.get("androidx.core.content", "ContextCompat")
                val pm = ClassName.get("android.content", "pm", "PackageManager")

                val fieldBuilder = FieldSpec.builder(
                    ClassName.get("java.util", "ArrayList"),
                    "arrayList",
                    Modifier.PRIVATE, Modifier.STATIC
                ).build()

                val annotBuilder = AnnotationSpec.builder(ClassName.get("java.lang", "SuppressWarnings")).apply {
                    addMember("value", "\"unchecked\"")
                }.build()

                val methodBuilder = MethodSpec.methodBuilder("runMethod")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .addParameter(ClassName.get(it.enclosingElement.asType()), "instance")
                    .addStatement("${fieldBuilder.name} = new ArrayList<String>()")
                    .addCode(value.joinToString(separator = "\n") { v ->
                        "if ($ctxCompat.checkSelfPermission(instance, \"$v\") != $pm.PERMISSION_GRANTED) {\n" +
                                "${fieldBuilder.name}.add(\"$v\");" +
                                "}"
                    })
                    .addCode("if (${fieldBuilder.name}.size() > 0) {\n" +
                            "instance.requestPermissions((String[])${fieldBuilder.name}.toArray(new String[0]), $reqCode);\n" +
                            "return;\n" +
                            "}")
                    .addStatement("instance.${it.simpleName}()")


                val typeSpec = TypeSpec.classBuilder("MyClassNew2")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(methodBuilder.build())
                    .addField(fieldBuilder)
                    .addAnnotation(annotBuilder)
                    .build()

                val javaFile = JavaFile.builder("org.flobiz.lib", typeSpec).build()
                javaFile.writeTo(processingEnv.filer)
            } else {
                processingEnv.messager.printMessage(
                    Diagnostic.Kind.ERROR,
                    "Method ${it.simpleName} is not accessible"
                )
            }
        }
        return true
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(Annot::class.java.canonicalName, AnnotMeth::class.java.canonicalName)
    }
}