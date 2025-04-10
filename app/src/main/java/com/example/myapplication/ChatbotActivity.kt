package com.example.myapplication

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ChatbotActivity : Fragment() {
    private lateinit var chatContainer: LinearLayout
    private lateinit var inputContainer: LinearLayout
    private lateinit var sendButton: Button
    private lateinit var messageInput: EditText

    private val questions = listOf(
        Question("Нравится ли вам посещать исторические места, такие как Нижегородский кремль?", "История"),
        Question("Часто ли вы читаете книги нижегородских писателей (Максим Горький, Корней Чуковский)?", "Литература"),
        Question("Интересуетесь ли вы выставками в Художественном музее или галереях?", "Живопись"),
        Question("Хотели бы вы научиться народным танцам (например, хороводам или кадрили)?", "Танцы"),
        Question("Любите ли вы изучать старинные фотографии и документы о прошлом Нижнего Новгорода?", "История"),
        Question("Нравятся ли вам спектакли нижегородских театров (Театр драмы, ТЮЗ)?", "Искусство"),
        Question("Посещали бы вы мастер-классы по росписи хохломы или городецкой росписи?", "Живопись"),
        Question("Интересны ли вам поэтические вечера или литературные фестивали?", "Литература"),
        Question("Хотели бы вы участвовать в танцевальных флешмобах или уличных перформансах?", "Танцы"),
        Question("Любите ли вы современное искусство (уличные инсталляции, арт-пространства)?", "Искусство")
    )

    private val categoryMap = mapOf(
        "Танцы" to listOf(3, 8),
        "Искусство" to listOf(5, 9),
        "История" to listOf(0, 4),
        "Литература" to listOf(1, 7),
        "Живопись" to listOf(2, 6)
    )

    private val recommendations = mapOf(
        "Танцы" to "Ваша стихия — движение! Рекомендуем: фольклорные ансамбли, мастер-классы в центре «Светлояр».",
        "Искусство" to "Вас вдохновляет творчество! Посетите Театр драмы или арт-кластеры в Нижнем.",
        "История" to "Вы ценитель прошлого! Исследуйте Нижегородский кремль и музей «Покровка, 8».",
        "Литература" to "Слово — ваш мир! Загляните в музей Горького или на фестиваль «Болдинская осень».",
        "Живопись" to "Краски — ваша страсть! Откройте для себя Художественный музей и мастер-классы по росписи."
    )

    private var currentQuestionIndex = 0
    private val answers = mutableListOf<Answer>()
    private val scores = mutableMapOf(
        "Танцы" to 0,
        "Искусство" to 0,
        "История" to 0,
        "Литература" to 0,
        "Живопись" to 0
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_chatbot, container, false)

        chatContainer = view.findViewById(R.id.chatContainer)
        inputContainer = view.findViewById(R.id.inputContainer)
        sendButton = view.findViewById(R.id.sendButton)
        messageInput = view.findViewById(R.id.messageInput)


        addMessage("Привет! Я помогу определить ваши культурные предпочтения в Нижнем Новгороде. Ответьте на несколько вопросов.", false)
        askNextQuestion()

        sendButton.setOnClickListener {
            val answer = messageInput.text.toString().trim()
            if (answer.isNotEmpty()) {
                addMessage(answer, true)
                messageInput.text.clear()
                processAnswer(answer)
            }
        }

        return view
    }

    private fun askNextQuestion() {
        if (currentQuestionIndex < questions.size) {
            addMessage(questions[currentQuestionIndex].text, false)
            showQuickReplies()
        } else {
            showResult()
        }
    }

    private fun showQuickReplies() {
        inputContainer.visibility = View.GONE

        val quickReplyContainer = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 16, 0, 16)
            }
        }

        listOf("Да", "Нет", "Не знаю").forEach { reply ->
            val button = Button(requireContext()).apply {
                text = reply
                setBackgroundColor(Color.parseColor("#E3F2FD"))
                setTextColor(Color.parseColor("#1976D2"))
                setPadding(16)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
                ).apply {
                    setMargins(4, 0, 4, 0)
                }
                setOnClickListener {
                    addMessage(reply, true)
                    processAnswer(reply)
                    chatContainer.removeView(quickReplyContainer)
                }
            }
            quickReplyContainer.addView(button)
        }

        chatContainer.addView(quickReplyContainer)
    }

    private fun processAnswer(answer: String) {
        val currentQuestion = questions[currentQuestionIndex]
        answers.add(Answer(currentQuestion.text, currentQuestion.category, answer))

        if (answer.equals("Да", ignoreCase = true)) {
            val category = currentQuestion.category
            scores[category] = scores[category]!! + 1
        }

        currentQuestionIndex++
        askNextQuestion()
    }

    private fun showResult() {
        inputContainer.visibility = View.GONE

        val maxScore = scores.maxByOrNull { it.value }?.value
        val topCategories = scores.filter { it.value == maxScore }.keys

        if (maxScore == 0) {
            addMessage("Пока ваш интерес не ясен, но Нижний Новгород полон сюрпризов! Попробуйте экскурсию по городу или квест «Культурный микс».", false)
        } else if (topCategories.size > 1) {
            addMessage("Вы многогранны! Вот несколько рекомендаций для вас:", false)
            topCategories.forEach {
                addMessage(recommendations[it]!!, false)
            }
        } else {
            addMessage(recommendations[topCategories.first()]!!, false)
        }

        saveResultsToFile()
    }

    private fun addMessage(text: String, isUserMessage: Boolean) {
        val messageLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }

        val messageBubble = TextView(requireContext()).apply {
            this.text = text
            setTextColor(if (isUserMessage) Color.WHITE else Color.BLACK)
            setBackgroundResource(
                if (isUserMessage) R.drawable.user_bubble else R.drawable.bot_bubble
            )
            setPadding(24, 16, 24, 16)
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = if (isUserMessage) Gravity.END else Gravity.START
            }
        }

        messageLayout.addView(messageBubble)
        chatContainer.addView(messageLayout)


        chatContainer.post {
            val scrollView = chatContainer.parent as? ScrollView
            scrollView?.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun saveResultsToFile() {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "quiz_results_$timestamp.txt"
        val file = File(requireContext().filesDir, fileName)

        val content = buildString {
            appendln("Результаты опроса:")
            appendln("Дата: ${SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())}")
            appendln()

            appendln("Ответы:")
            answers.forEachIndexed { index, answer ->
                appendln("${index + 1}. ${answer.question}")
                appendln("Категория: ${answer.category}")
                appendln("Ответ: ${answer.answer}")
                appendln()
            }

            appendln("\nБаллы по категориям:")
            scores.forEach { (category, score) ->
                appendln("$category: $score")
            }

            appendln("\nРекомендация:")
            for (i in 0 until chatContainer.childCount) {
                val child = chatContainer.getChildAt(i)
                if (child is LinearLayout && child.childCount > 0) {
                    val textView = child.getChildAt(0) as? TextView
                    textView?.text?.toString()?.let { message ->
                        if (!message.startsWith("Привет") && !message.startsWith("1.")) {
                            appendln(message)
                        }
                    }
                }
            }
        }

        FileOutputStream(file).use {
            it.write(content.toByteArray())
        }
    }
}

data class Question(val text: String, val category: String)
data class Answer(val question: String, val category: String, val answer: String)