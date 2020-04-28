package io.lasalle.models

import java.time.LocalDate

//São eles: código, dataEntrega, disciplina, DescriçãoTrabalho e status (entregue ou não).
data class Task(val code: Int,
                val subject: String,
                val deliverTime: LocalDate,
                val description: String,
                val status: TaskStatus
                ) {

    override fun toString(): String {
        return """
            
            Código: $code
            Disciplina: $subject
            Data de entrega: $deliverTime
            Descrição: $description
            Estado: $status
            
        """.trimIndent()
    }
}

enum class TaskStatus {
    COMPLETED,
    PENDING
}
