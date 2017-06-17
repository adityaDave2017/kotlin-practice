fun main(args: Array<String>) {

    stop@ while (true) {

        val choice: Int = askChoice()
        if (choice == 5) {
            println("\nGood Bye")
            break@stop
        } else if (choice !in 1..5) {
            println("\nInvalid choice. Try Again!")
            continue@stop
        }

        print("\nEnter first number : ")
        val num1: Double = readLine()?.toDouble() ?: 0.0

        print("Enter second number : ")
        val num2: Double = readLine()?.toDouble() ?: 0.0

        when(choice) {
            1 -> println("\nAddition is : ${num1 + num2}\n")
            2 -> println("\nSubtraction is : ${num1 - num2}\n")
            3 -> println("\nMultiplication is : ${num1 * num2}\n")
            4 -> println("\nDivision is : ${num1 / num2}\n")
            else -> {
                println("\nInvalid choice. Try Again!")
                continue@stop
            }
        }

    }

}


fun askChoice(): Int {
    println("\n1. Add")
    println("2. Subtract")
    println("3. Multiply")
    println("4. Divide")
    println("5. Exit")
    print("\nEnter your choice: ")
    return readLine()?.toInt() ?: askChoice()
}