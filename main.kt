
import kotlin.math.*

// Stack functions: I'd rather like to use push pop and peek in a stack
fun <T> ArrayDeque<T>.push(item: T) {
    this.addLast(item)
}

fun <T> ArrayDeque<T>.pop(): T? {
    if (this.isEmpty()) {
        return null
    }
    return this.removeLast()
}

fun <T> ArrayDeque<T>.peek(): T? {
    if (this.isEmpty()) {
        return null
    }
    return this.last()
}

// Tokenizer
sealed class Token

data class NumberToken(val value: Double) : Token()

data class OperatorToken(val operator: String) : Token()

data class InvalidToken(val value: String) : Token()

fun tokenize(input: String): List<Token> {
    val tokens = mutableListOf<Token>()
    val tokenPatterns = """([-]*\d+(\.\d+)?)|([+\-*/]|sqr|exp|sin|cos|pow)""".toRegex()
    val matches = tokenPatterns.findAll(input)
    for (match in matches) {
        val (number, _, operator) = match.destructured
        when {
            number.isNotEmpty() -> tokens.add(NumberToken(number.toDouble()))
            operator.isNotEmpty() -> tokens.add(OperatorToken(operator))
            else -> tokens.add(InvalidToken(match.value))
        }
    }
    return tokens
}

// Operator Definitions
sealed class Operator {
    abstract fun apply(stack: ArrayDeque<Double>)
}

private fun binaryOp(stack: ArrayDeque<Double>, op: (Double, Double) -> Double) {
    if (stack.size >= 2) {
        val right = stack.pop() ?: 0.0
        val left = stack.pop() ?: 0.0
        stack.push(op(left, right))
    } else println("Stack error.")
}

private fun unaryOp(stack: ArrayDeque<Double>, op: (Double) -> Double) {
    if (stack.size >= 1) {
        val value = stack.pop() ?: 0.0
        stack.push(op(value))
    } else println("Stack error.")
}

object Add : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        binaryOp(stack) { left, right -> left + right }
    }
}

object Sub : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        binaryOp(stack) { left, right -> left - right }
    }
}

object Mul : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        binaryOp(stack) { left, right -> left * right }
    }
}

object Div : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        if (stack.size >= 2) {
            val right = stack.pop() ?: 0.0
            val left = stack.pop() ?: 0.0
            if (right != 0.0) stack.push(left / right) else println("Division by zero error.")
        } else println("Stack error.")
    }
}

object Sqr : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        unaryOp(stack) { it * it }
    }
}

object Exp : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        unaryOp(stack) { kotlin.math.exp(it) }
    }
}

object Sin : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        unaryOp(stack) { kotlin.math.sin(it) }
    }
}

object Cos : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        unaryOp(stack) { kotlin.math.cos(it) }
    }
}

object Pow : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        binaryOp(stack) { left, right -> left.pow(right) }
    }
}

object Nop : Operator() {
    override fun apply(stack: ArrayDeque<Double>) {
        // nothing
    }
}

fun operatorFromString(opCode: String): Operator {

    when (opCode) {
        "+" -> return Add
        "-" -> return Sub
        "*" -> return Mul
        "/" -> return Div
        "sqr" -> return Sqr
        "exp" -> return Exp
        "sin" -> return Sin
        "cos" -> return Cos
        "pow" -> return Pow
    }

    return Nop
}

class RPNCalculator {
    private val stack = ArrayDeque<Double>()

    fun evaluateInput(input: String): String {
        val tokens = tokenize(input)
        tokens.forEach { token ->
            when (token) {
                is NumberToken -> stack.push(token.value)
                is OperatorToken -> operatorFromString(token.operator).apply(stack)
                is InvalidToken -> {}
            }
        }
        return stack.peek().toString()
    }
}

fun main(args: Array<String>) {
    if (args.isEmpty().not()) {
        println("Arguments not supported yet.")
    }
    val calculator = RPNCalculator()

    while (true) {
        print("Input>")
        val input = readlnOrNull()
        if (input == "exit") {
            println("Exit.")
            break
        }
        if (input != null) println(calculator.evaluateInput(input))
    }
}
