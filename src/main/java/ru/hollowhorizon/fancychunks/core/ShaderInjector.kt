package ru.hollowhorizon.fancychunks.core

class ShaderInjector {
    val transform = mutableListOf<(String) -> String>()

    fun insertAfterDefines(vararg code: String) {
        transform.add { src ->
            var toInsert = "\n${java.lang.String.join("\n", *code)}"
            val lastDefineIdx = src.lastIndexOf("#")
            val newlineIdx = src.indexOf("\n", lastDefineIdx)

            toInsert = replaceParts(src, toInsert)

            return@add insertAt(newlineIdx, src, toInsert)
        }
    }

    fun appendToFunction(function: String, vararg code: String) {
        transform.add { src ->
            val indentation = getIndentation(0)
            val toInsert = "\n${indentation}${java.lang.String.join("\n${indentation}", *code)}\n"

            return@add insertToFunction(src, toInsert, function, -1)
        }
    }

    fun addToFunction(function: String, vararg code: String) {
        transform.add { src ->
            val indentation = getIndentation(0)
            val toInsert = "\n${indentation}${java.lang.String.join("\n${indentation}", *code)}"

            return@add insertToFunction(src, toInsert, function, -1)
        }
    }

    operator fun get(code: String): String {
        var shadowcode = code
        for (func in transform) shadowcode = func.invoke(shadowcode)
        return shadowcode
    }

    fun copyFrom(injector: ShaderInjector) {
        transform.addAll(injector.transform)
    }

    companion object {
        private fun insertToFunction(src: String, code: String, function: String, offset: Int): String {
            var shadowcode = code
            val functionIdx = src.indexOf(function)
            if (functionIdx == -1) throw IllegalStateException("Failed to append code, function '${function}' was not found!")

            val firstBracketIdx = src.indexOf('{', functionIdx)
            var bracketCount = 0

            shadowcode = replaceParts(src, shadowcode)

            if (offset > 0) return insertAt(firstBracketIdx + offset, src, shadowcode)
            else if (offset < 0) for (i in firstBracketIdx until src.length) {
                val symbol = src[i]
                if (symbol == '{') bracketCount++
                else if (symbol == '}') bracketCount--

                if (bracketCount == 0) return insertAt(i + offset, src, shadowcode)
            }

            throw IllegalStateException("Failed to append code, function '${function}' was not found!")
        }

        private fun getIndentation(bracketCount: Int): String {
            var value = ""

            for (i in 0 until bracketCount) value += "    "

            return value
        }

        private fun insertAt(i: Int, original: String, target: String): String = "${original.substring(0, i)}${target}${original.substring(i)}"

        private fun getUniformAtLayout(code: String, uniform: Int): UniformData {
            val index = code.indexOf("layout(location = ${uniform})")
            if (index == -1) return UniformData("vec4", "iris_FragData0")
            var line = ""

            for (i in index until code.length) line += if (code[i] == '\n') break else code[i]

            line = line.replace(";".toRegex(), "")
            val split = line.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            return UniformData(split[split.size - 2], split[split.size - 1])
        }

        private fun replaceParts(shaderCode: String, toInject: String): String {
            var shadowtoInject = toInject
            if (shadowtoInject.contains("\${uniform_0_prefix}")
                || shadowtoInject.contains("\${uniform_0_postfix}")
                || shadowtoInject.contains("\${uniform_0}")
            ) {
                val (type, name) = getUniformAtLayout(shaderCode, 0)
                if (type != "uvec4" && type != "vec4") return ""
                val isUvec = type == "uvec4"
                shadowtoInject = shadowtoInject.replace(
                    "\\$\\{uniform_0_prefix\\}".toRegex(),
                    if (isUvec) "uvec4(" else ""
                )
                shadowtoInject = shadowtoInject.replace(
                    "\\$\\{uniform_0_postfix\\}".toRegex(),
                    if (isUvec) ")" else ""
                )
                shadowtoInject = shadowtoInject.replace(
                    "\\$\\{uniform_0\\}".toRegex(),
                    name
                )
            }
            return shadowtoInject
        }

    }
}