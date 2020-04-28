package io.lasalle

import arrow.core.Try
import arrow.core.extensions.fx
import arrow.core.getOrElse
import arrow.core.maybe
import io.lasalle.models.Task
import io.lasalle.models.TaskStatus
import java.time.LocalDate
import kotlin.system.exitProcess

/*
* Faça um programa para gerenciar uma Agenda de trabalhos em Casa.
* Em tempos de atividades virtuais relacionada a Covid-19 o numero de atividades em casa multiplicou.
* Para a estrutura de nossa agenda de trabalhos, cada trabalho possui 5 campos de informação.
* São eles: código, dataEntrega, disciplina, DescriçãoTrabalho e status (entregue ou não).
* Este programa deverá ser desenvolvido usando Listas Lineares.
* O programa deverá seguir a especificação fornecida abaixo.
* Poderão ser utilizadas linguagens de programação diferentes de C.
*
* O programa deve possuir um menu com as seguintes opções:
* - Opção "I" - Insere um trabalho na agenda
* - Opção "R" - Remove trabalho da agenda
* - Opção "C" - Consulta os trabalhos da agenda
* - Opção "E" - Exibe os trabalhos da agenda por data
* - Opção "F" - Termina a execuçăo do programa
*
* Detalhamento de cada opcão do programa:
*
* 1) Opção I (insere): Esta opção permite que o usuário indique se deseja inserir um novo trabalho na agenda (código + data + disciplina + texto que descreve + status) Em relação aos trabalhos da agenda:
* - Cada trabalho compreende um código (gerado automaticamente pela aplicação), uma data (dia, mês e ano), uma disciplina, um texto que descreve e o status;
* 2) Opção R (remove): Esta opşão permite que o usuário remova da agenda. Esta opção permite que o usuário apague da agenda todos os trabalhos de uma determinada data, ou seja, a idéia é depois de terminado o dia, o usuário remove todos os compromissos daquele dia de uma vez só. O usuário apenas informa uma data (dia, mês e ano) e o sistema apaga todos os compromissos referentes aquele dia. Ou, remover da agenda todos os trabalhos com status entregue.
* 3) Opção C (consulta): Esta opção permite que o usuário consulte os seus trabalhos agendados. O usuário indica uma data (dia, mês e ano) e o sistema irá exibir todos os compromissos agendados nesta data, listando todas as informações de cada um.
* 4) Opção E (exibe): Esta opão permite que seja exibido na tela todos os trabalhos agendados.
* 5) Opção F (fim): Termina a execução do programa.
* */

fun main() {
    val tasks = List.of<Task>()
    var code = 0

    while (true) {
        println("""
            - Opção "I" - Insere um trabalho na agenda
            - Opção "R" - Remove trabalho da agenda
            - Opção "C" - Consulta os trabalhos da agenda
            - Opção "E" - Exibe os trabalhos da agenda por data
            - Opção "F" - Termina a execuçăo do programa
        """.trimIndent())

        val option = readLine()

        when {
            option.equals("I", ignoreCase = true) -> {
                //Esta opção permite que o usuário indique se deseja inserir um novo trabalho na agenda (código + data + disciplina + texto que descreve + status)
                //Em relação aos trabalhos da agenda:
                //- Cada trabalho compreende um código (gerado automaticamente pela aplicação), uma data (dia, mês e ano), uma disciplina, um texto que descreve e o status;

                println()

                createTask(code++)
                    .fold({ println("Erro ao criar trabalho: $it") }, { tasks.add(it); println("\n Trabalho criado na agenda: \n$it") })
            }

            option.equals("R", ignoreCase = true) -> {
                //O usuário apenas informa uma data (dia, mês e ano) e o sistema apaga todos os compromissos referentes aquele dia.
                //Ou, remover da agenda todos os trabalhos com status entregue.

                val completed = tasks.filter { it?.status == TaskStatus.COMPLETED }

                tasks.removeAll(completed)

                println("Removido da agenda os items entregues: $completed")

                print("Deseja remover um item? (S/N) ")

                val remove = readLine()?.contains("S", ignoreCase = true) ?: false

                if (remove) {
                    print("Informe uma data para remover no formato 'yyyy-MM-dd': ")

                    readDate().fold({ println("Data inválida: $it") }, { date -> tasks.removeAll { it?.deliverTime == date } })
                }

            }

            option.equals("C", ignoreCase = true) -> {
                //Opção C (consulta): Esta opção permite que o usuário consulte os seus trabalhos agendados.
                //O usuário indica uma data (dia, mês e ano)
                //e o sistema irá exibir todos os compromissos agendados nesta data,
                //listando todas as informações de cada um.

                print("Informe uma data no formato 'yyyy-MM-dd': ")

                readDate().map { date -> tasks.filter { it?.deliverTime == date } }
                    .fold({ println("Data inválida: $it") }, { println("Encontrados trabalhos: $it") })
            }
            option.equals("E", ignoreCase = true) -> {
                tasks.groupBy { it?.deliverTime }
                    .forEach { (k, v) -> println("Data: $k \n"); v.forEach { println(it) } }
            }
            option.equals("F", ignoreCase = true) -> exitProcess(0)
        }

        println()
        println()
    }



}

private fun readDate(): Try<LocalDate> {
    return Try {
        val date = readLine()
        LocalDate.parse(date)
    }
}


private fun createTask(code: Int): Try<Task> {
    //Cada trabalho compreende
    //um código (gerado automaticamente pela aplicação),
    //uma data (dia, mês e ano),
    //uma disciplina,
    //um texto que descreve e o status;

    print("Digite a data de entrega no formato 'yyyy-MM-dd': ")

    return Try.fx {
        val deliverDate = readDate().bind()

        print("Digite a disciplina: ")

        val subject = readLine() ?: ""

        print("Descrição: ")

        val description = readLine() ?: ""

        print("Trabalho foi entregue? (S/N) ")

        val status = (readLine()?.contains("S", ignoreCase = true) ?: false)
            .maybe { TaskStatus.COMPLETED }
            .getOrElse { TaskStatus.PENDING }

        Task(code, subject = subject, deliverTime = deliverDate, description = description, status = status)
    }
}
