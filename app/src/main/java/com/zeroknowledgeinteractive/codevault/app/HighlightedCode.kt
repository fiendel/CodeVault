package com.zeroknowledgeinteractive.codevault.app

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

// This file keeps a small regex-based highlighter for every language offered in the
// language dropdown. It is not a full parser, but it gives each language a useful
// keyword/type/comment/string pass so the UI can feel much closer to a real editor.
private data class LanguageHighlightRules(
    val keywordRegex: Regex,
    val typeRegex: Regex,
    val numberRegex: Regex,
    val stringRegex: Regex,
    val charRegex: Regex,
    val lineCommentRegex: Regex,
    val blockCommentRegex: Regex,
    val annotationRegex: Regex,
    val operatorRegex: Regex
)

private val noMatchRegex = Regex("$^")
private val genericNumberRegex = Regex("\\b(0[xX][0-9A-Fa-f_]+|0[bB][01_]+|\\d[\\d_]*(\\.\\d[\\d_]*)?([eE][+-]?\\d+)?)\\b")
private val cLikeNumberRegex = Regex("\\b(0[xX][0-9A-Fa-f_]+|0[bB][01_]+|\\d[\\d_]*(\\.\\d[\\d_]*)?([eE][+-]?\\d+)?)\\b[uUlLfFdDmM]*")
private val cLikeStringRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\"")
private val cLikeCharRegex = Regex("'(?:\\\\.|[^'\\\\])'")
private val cLikeLineCommentRegex = Regex("//.*")
private val cLikeBlockCommentRegex = Regex("/\\*[\\s\\S]*?\\*/")
private val cLikeOperatorRegex = Regex("(\\?:|\\.\\.|::|->|=>|==|!=|<=|>=|&&|\\|\\||<<|>>|\\+\\+|--|[+\\-*/%&|^~!=<>])")

private fun createRules(
    keywords: String,
    types: String = "",
    numberRegex: Regex = genericNumberRegex,
    stringRegex: Regex = cLikeStringRegex,
    charRegex: Regex = noMatchRegex,
    lineCommentRegex: Regex = noMatchRegex,
    blockCommentRegex: Regex = noMatchRegex,
    annotationRegex: Regex = noMatchRegex,
    operatorRegex: Regex = cLikeOperatorRegex
): LanguageHighlightRules {
    return LanguageHighlightRules(
        keywordRegex = if (keywords.isBlank()) noMatchRegex else Regex("\\b($keywords)\\b"),
        typeRegex = if (types.isBlank()) noMatchRegex else Regex("\\b($types)\\b"),
        numberRegex = numberRegex,
        stringRegex = stringRegex,
        charRegex = charRegex,
        lineCommentRegex = lineCommentRegex,
        blockCommentRegex = blockCommentRegex,
        annotationRegex = annotationRegex,
        operatorRegex = operatorRegex
    )
}

private val kotlinRules = createRules(
    keywords = "as|as\\?|break|class|continue|do|else|false|for|fun|if|in|!in|interface|is|!is|null|object|package|return|super|this|throw|true|try|typealias|typeof|val|var|when|while|by|catch|constructor|delegate|dynamic|field|file|finally|get|import|init|param|property|receiver|set|setparam|where|actual|abstract|annotation|companion|const|crossinline|data|enum|expect|external|final|infix|inline|inner|internal|lateinit|noinline|open|operator|out|override|private|protected|public|reified|sealed|suspend|tailrec|value|vararg",
    types = "Any|Nothing|Unit|String|Char|Boolean|Byte|Short|Int|Long|Float|Double|UByte|UShort|UInt|ULong|Array|List|MutableList|Set|MutableSet|Map|MutableMap|Pair|Triple|Result|Sequence",
    numberRegex = Regex("\\b(0[xX][0-9A-Fa-f_]+|0[bB][01_]+|\\d[\\d_]*(\\.\\d[\\d_]*)?([eE][+-]?\\d+)?)\\b[fFdDlL]?"),
    stringRegex = Regex("\"\"\"[\\s\\S]*?\"\"\"|\"(?:\\\\.|[^\"\\\\])*\""),
    charRegex = cLikeCharRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("@[A-Za-z_][A-Za-z0-9_]*")
)

private val javaRules = createRules(
    keywords = "abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|int|interface|long|native|new|null|package|private|protected|public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|true|try|void|volatile|while|record|sealed|permits|var",
    types = "String|Integer|Long|Double|Float|Boolean|Character|Byte|Short|Object|List|ArrayList|Map|HashMap|Set|HashSet|Optional",
    numberRegex = cLikeNumberRegex,
    stringRegex = cLikeStringRegex,
    charRegex = cLikeCharRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("@[A-Za-z_][A-Za-z0-9_]*")
)

private val javascriptRules = createRules(
    keywords = "async|await|break|case|catch|class|const|continue|debugger|default|delete|do|else|export|extends|false|finally|for|function|if|import|in|instanceof|let|new|null|of|return|super|switch|this|throw|true|try|typeof|var|void|while|with|yield",
    types = "Array|Boolean|Date|Error|Function|Map|Number|Object|Promise|RegExp|Set|String|Symbol",
    numberRegex = cLikeNumberRegex,
    stringRegex = Regex("`(?:\\\\.|[^`\\\\])*`|\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'"),
    charRegex = noMatchRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex
)

private val typeScriptRules = createRules(
    keywords = "abstract|any|as|asserts|async|await|bigint|boolean|break|case|catch|class|const|continue|debugger|declare|default|delete|do|else|enum|export|extends|false|finally|for|from|function|get|if|implements|import|in|infer|instanceof|interface|is|keyof|let|module|namespace|never|new|null|number|object|of|package|private|protected|public|readonly|required|return|set|static|string|super|switch|symbol|this|throw|true|try|type|typeof|undefined|unique|unknown|var|void|while|with|yield",
    types = "Array|Boolean|Date|Function|Map|Number|Object|Promise|ReadonlyArray|Record|Set|String",
    numberRegex = cLikeNumberRegex,
    stringRegex = Regex("`(?:\\\\.|[^`\\\\])*`|\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'"),
    charRegex = noMatchRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("@[A-Za-z_][A-Za-z0-9_]*")
)

private val pythonRules = createRules(
    keywords = "and|as|assert|async|await|break|case|class|continue|def|del|elif|else|except|False|finally|for|from|global|if|import|in|is|lambda|match|None|nonlocal|not|or|pass|raise|return|True|try|while|with|yield",
    types = "int|float|str|bool|list|tuple|set|dict|bytes|object",
    stringRegex = Regex("\"\"\"[\\s\\S]*?\"\"\"|'''[\\s\\S]*?'''|\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'"),
    lineCommentRegex = Regex("#.*"),
    operatorRegex = Regex("(==|!=|<=|>=|//=|\\*\\*|:=|[-+*/%=&|<>])")
)

private val cRules = createRules(
    keywords = "auto|break|case|const|continue|default|do|else|enum|extern|for|goto|if|register|return|sizeof|static|struct|switch|typedef|union|volatile|while",
    types = "char|double|float|int|long|short|signed|unsigned|void|size_t|FILE",
    numberRegex = cLikeNumberRegex,
    stringRegex = cLikeStringRegex,
    charRegex = cLikeCharRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("#[A-Za-z_][A-Za-z0-9_]*")
)

private val cppRules = createRules(
    keywords = "alignas|alignof|asm|auto|break|case|catch|class|const|constexpr|constinit|consteval|continue|co_await|co_return|co_yield|default|delete|do|else|enum|explicit|export|extern|false|final|for|friend|goto|if|inline|mutable|namespace|new|noexcept|nullptr|operator|override|private|protected|public|register|reinterpret_cast|requires|return|sizeof|static|struct|switch|template|this|throw|true|try|typedef|typename|union|using|virtual|volatile|while",
    types = "bool|char|char8_t|char16_t|char32_t|double|float|int|long|short|signed|unsigned|void|wchar_t|string|vector|map|set|unordered_map|unique_ptr|shared_ptr",
    numberRegex = cLikeNumberRegex,
    stringRegex = cLikeStringRegex,
    charRegex = cLikeCharRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("#[A-Za-z_][A-Za-z0-9_]*")
)

private val cSharpRules = createRules(
    keywords = "abstract|as|base|break|case|catch|checked|class|const|continue|default|delegate|do|else|enum|event|explicit|extern|false|finally|fixed|for|foreach|goto|if|implicit|in|interface|internal|is|lock|namespace|new|null|operator|out|override|params|private|protected|public|readonly|ref|required|return|sealed|sizeof|stackalloc|static|struct|switch|this|throw|true|try|typeof|unchecked|unsafe|using|virtual|void|volatile|while|async|await|get|set|init|partial|record|var|yield|where|select|from|group|into|join|let|orderby|on|equals|by|ascending|descending",
    types = "bool|byte|char|decimal|double|dynamic|float|int|long|nint|nuint|object|sbyte|short|string|uint|ulong|ushort|DateTime|Guid|Task|List|Dictionary|HashSet|IEnumerable|IList|IDictionary|Action|Func|Span|Memory",
    numberRegex = cLikeNumberRegex,
    stringRegex = Regex("@\"(?:\"\"|[^\"])*\"|\\$@\"(?:\"\"|[^\"])*\"|@\\$\"(?:\"\"|[^\"])*\"|\\$\"(?:\\\\.|[^\"\\\\])*\"|\"(?:\\\\.|[^\"\\\\])*\""),
    charRegex = cLikeCharRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("\\[[A-Za-z_][A-Za-z0-9_]*(?:\\([^\\]]*\\))?\\]")
)

private val goRules = createRules(
    keywords = "break|case|chan|const|continue|default|defer|else|fallthrough|for|func|go|goto|if|import|interface|map|package|range|return|select|struct|switch|type|var",
    types = "bool|byte|complex64|complex128|error|float32|float64|int|int8|int16|int32|int64|rune|string|uint|uint8|uint16|uint32|uint64|uintptr",
    numberRegex = cLikeNumberRegex,
    stringRegex = Regex("`[\\s\\S]*?`|\"(?:\\\\.|[^\"\\\\])*\""),
    charRegex = cLikeCharRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex
)

private val rustRules = createRules(
    keywords = "as|async|await|break|const|continue|crate|dyn|else|enum|extern|false|fn|for|if|impl|in|let|loop|match|mod|move|mut|pub|ref|return|self|Self|static|struct|super|trait|true|type|unsafe|use|where|while",
    types = "bool|char|f32|f64|i8|i16|i32|i64|i128|isize|str|String|u8|u16|u32|u64|u128|usize|Vec|Option|Result",
    numberRegex = cLikeNumberRegex,
    stringRegex = Regex("r#*\"[\\s\\S]*?\"#*|\"(?:\\\\.|[^\"\\\\])*\""),
    charRegex = cLikeCharRegex,
    lineCommentRegex = Regex("//.*"),
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("#!?\\[[^\\]]+\\]")
)

private val swiftRules = createRules(
    keywords = "associatedtype|break|case|catch|class|continue|default|defer|deinit|do|else|enum|extension|fallthrough|false|for|func|guard|if|import|in|init|inout|internal|is|let|nil|operator|private|protocol|public|repeat|rethrows|return|self|Self|static|struct|subscript|super|switch|throw|throws|true|try|typealias|var|where|while",
    types = "Any|AnyObject|Array|Bool|Character|Dictionary|Double|Float|Int|Optional|Set|String|UInt",
    numberRegex = cLikeNumberRegex,
    stringRegex = Regex("#?\"(?:\\\\.|[^\"\\\\])*\"#?"),
    charRegex = noMatchRegex,
    lineCommentRegex = cLikeLineCommentRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("@[A-Za-z_][A-Za-z0-9_]*")
)

private val phpRules = createRules(
    keywords = "abstract|and|array|as|break|callable|case|catch|class|clone|const|continue|declare|default|do|echo|else|elseif|empty|enddeclare|endfor|endforeach|endif|endswitch|endwhile|enum|eval|exit|extends|false|final|finally|fn|for|foreach|function|global|goto|if|implements|include|include_once|instanceof|insteadof|interface|isset|list|match|namespace|new|null|or|print|private|protected|public|readonly|require|require_once|return|self|static|switch|throw|trait|true|try|unset|use|var|while|xor|yield",
    types = "array|bool|callable|float|int|iterable|mixed|object|self|string|void",
    numberRegex = cLikeNumberRegex,
    stringRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'"),
    charRegex = noMatchRegex,
    lineCommentRegex = Regex("//.*|#.*"),
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("#\\[[^\\]]+\\]|@[A-Za-z_][A-Za-z0-9_]*"),
    operatorRegex = Regex("(=>|\\?->|::|==|!=|<=|>=|&&|\\|\\||[-+*/%.=&|!<>])")
)

private val sqlRules = createRules(
    keywords = "add|all|alter|and|as|asc|between|by|case|check|column|constraint|create|database|default|delete|desc|distinct|drop|else|exists|foreign|from|group|having|in|index|inner|insert|into|is|join|key|left|like|limit|not|null|on|or|order|outer|primary|procedure|right|rownum|select|set|table|then|top|truncate|union|unique|update|values|view|when|where",
    types = "bigint|bit|blob|bool|boolean|char|date|datetime|decimal|double|float|int|integer|json|nchar|numeric|nvarchar|real|text|time|timestamp|tinyint|uuid|varchar",
    stringRegex = Regex("'(?:''|[^'])*'|\"(?:\\\\.|[^\"\\\\])*\""),
    charRegex = noMatchRegex,
    lineCommentRegex = Regex("--.*"),
    blockCommentRegex = cLikeBlockCommentRegex,
    operatorRegex = Regex("(<>|!=|<=|>=|==|[-+*/%=<>,.])")
)

private val jsonRules = createRules(
    keywords = "true|false|null",
    types = "",
    stringRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\""),
    charRegex = noMatchRegex,
    lineCommentRegex = noMatchRegex,
    blockCommentRegex = noMatchRegex,
    annotationRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\"(?=\\s*:)"),
    operatorRegex = Regex("[:{},\\[\\]]")
)

private val xmlRules = createRules(
    keywords = "",
    types = "",
    stringRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'"),
    charRegex = noMatchRegex,
    lineCommentRegex = noMatchRegex,
    blockCommentRegex = Regex("<!--[\\s\\S]*?-->"),
    annotationRegex = Regex("</?[A-Za-z_:][A-Za-z0-9_:\\-.]*|/?>|<[!?][^>]*>"),
    operatorRegex = Regex("[=<>/]")
)

private val htmlRules = createRules(
    keywords = "html|head|body|div|span|section|article|main|header|footer|nav|script|style|link|meta|title|button|input|form|label|table|thead|tbody|tr|td|th|ul|ol|li|img|a|p|h1|h2|h3|h4|h5|h6|canvas|svg",
    types = "",
    stringRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'"),
    charRegex = noMatchRegex,
    lineCommentRegex = noMatchRegex,
    blockCommentRegex = Regex("<!--[\\s\\S]*?-->"),
    annotationRegex = Regex("</?[A-Za-z][A-Za-z0-9:-]*|/?>|<!DOCTYPE[^>]*>"),
    operatorRegex = Regex("[=<>/]")
)

private val cssRules = createRules(
    keywords = "@media|@import|@keyframes|@supports|@font-face|from|to",
    types = "px|em|rem|vh|vw|fr|deg|s|ms|rgb|rgba|hsl|hsla|url",
    stringRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'"),
    charRegex = noMatchRegex,
    lineCommentRegex = noMatchRegex,
    blockCommentRegex = cLikeBlockCommentRegex,
    annotationRegex = Regex("\\.[A-Za-z_-][A-Za-z0-9_-]*|#[A-Za-z_-][A-Za-z0-9_-]*|@[A-Za-z-]+"),
    operatorRegex = Regex("[:;{}(),.#>%]")
)

private val shellRules = createRules(
    keywords = "if|then|else|elif|fi|for|in|do|done|while|until|case|esac|function|select|time",
    types = "echo|printf|export|local|readonly|declare|typeset|alias|unset|source|cd|pwd|test",
    stringRegex = Regex("\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|`(?:\\\\.|[^`\\\\])*`"),
    charRegex = noMatchRegex,
    lineCommentRegex = Regex("#.*"),
    blockCommentRegex = noMatchRegex,
    annotationRegex = Regex("\\$\\{?[A-Za-z_][A-Za-z0-9_]*\\}?"),
    operatorRegex = Regex("(\\|\\||&&|>>|<<|\\|>|[-+*/%=<>|])")
)

// Choose the correct rule set from the language label stored on the snippet.
private fun rulesForLanguage(language: String): LanguageHighlightRules {
    return when (language.trim().lowercase()) {
        "kotlin" -> kotlinRules
        "java" -> javaRules
        "javascript" -> javascriptRules
        "typescript" -> typeScriptRules
        "python" -> pythonRules
        "c" -> cRules
        "c++" -> cppRules
        "c#", "csharp" -> cSharpRules
        "go" -> goRules
        "rust" -> rustRules
        "swift" -> swiftRules
        "php" -> phpRules
        "sql" -> sqlRules
        "json" -> jsonRules
        "xml" -> xmlRules
        "html" -> htmlRules
        "css" -> cssRules
        "shell", "bash", "sh" -> shellRules
        else -> kotlinRules
    }
}

// highlightCode builds an AnnotatedString, which is Compose text that can hold
// different styles on different character ranges inside the same Text composable.
fun highlightCode(code: String, language: String): AnnotatedString {
    val rules = rulesForLanguage(language)

    val keywordColor = Color(0xFF569CD6)
    val typeColor = Color(0xFF4EC9B0)
    val stringColor = Color(0xFFCE9178)
    val commentColor = Color(0xFF6A9955)
    val numberColor = Color(0xFFB5CEA8)
    val annotationColor = Color(0xFFDCDCAA)
    val operatorColor = Color(0xFFC586C0)

    return buildAnnotatedString {
        append(code)

        rules.keywordRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = keywordColor), match.range.first, match.range.last + 1)
        }

        rules.typeRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = typeColor), match.range.first, match.range.last + 1)
        }

        rules.numberRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = numberColor), match.range.first, match.range.last + 1)
        }

        rules.operatorRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = operatorColor), match.range.first, match.range.last + 1)
        }

        rules.annotationRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = annotationColor), match.range.first, match.range.last + 1)
        }

        // Strings and comments are applied later so they visually win over keywords inside them.
        rules.stringRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = stringColor), match.range.first, match.range.last + 1)
        }

        rules.charRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = stringColor), match.range.first, match.range.last + 1)
        }

        rules.blockCommentRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = commentColor), match.range.first, match.range.last + 1)
        }

        rules.lineCommentRegex.findAll(code).forEach { match ->
            addStyle(SpanStyle(color = commentColor), match.range.first, match.range.last + 1)
        }
    }
}
