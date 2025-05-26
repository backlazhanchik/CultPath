package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatbotActivity extends Fragment {

    private LinearLayout chatContainer;
    private LinearLayout inputContainer;
    private Button sendButton;
    private EditText messageInput;

    private final List<Question> questions = new ArrayList<Question>() {{
        add(new Question("Нравится ли вам посещать исторические места, такие как Нижегородский кремль?", "История"));
        add(new Question("Часто ли вы читаете книги нижегородских писателей (Максим Горький, Корней Чуковский)?", "Литература"));
        add(new Question("Интересуетесь ли вы выставками в Художественном музее или галереях?", "Живопись"));
        add(new Question("Хотели бы вы научиться народным танцам (например, хороводам или кадрили)?", "Танцы"));
        add(new Question("Любите ли вы изучать старинные фотографии и документы о прошлом Нижнего Новгорода?", "История"));
        add(new Question("Нравятся ли вам спектакли нижегородских театров (Театр драмы, ТЮЗ)?", "Искусство"));
        add(new Question("Посещали бы вы мастер-классы по росписи хохломы или городецкой росписи?", "Живопись"));
        add(new Question("Интересны ли вам поэтические вечера или литературные фестивали?", "Литература"));
        add(new Question("Хотели бы вы участвовать в танцевальных флешмобах или уличных перформансах?", "Танцы"));
        add(new Question("Любите ли вы современное искусство (уличные инсталляции, арт-пространства)?", "Искусство"));
    }};

    private final Map<String, List<Integer>> categoryMap = new HashMap<String, List<Integer>>() {{
        put("Танцы", List.of(3, 8));
        put("Искусство", List.of(5, 9));
        put("История", List.of(0, 4));
        put("Литература", List.of(1, 7));
        put("Живопись", List.of(2, 6));
    }};

    private final Map<String, String> recommendations = new HashMap<String, String>() {{
        put("Танцы", "Ваша стихия — движение! Рекомендуем: фольклорные ансамбли, мастер-классы в центре «Светлояр».");
        put("Искусство", "Вас вдохновляет творчество! Посетите Театр драмы или арт-кластеры в Нижнем.");
        put("История", "Вы ценитель прошлого! Исследуйте Нижегородский кремль и музей «Покровка, 8».");
        put("Литература", "Слово — ваш мир! Загляните в музей Горького или на фестиваль «Болдинская осень».");
        put("Живопись", "Краски — ваша страсть! Откройте для себя Художественный музей и мастер-классы по росписи.");
    }};

    private int currentQuestionIndex = 0;
    private final List<Answer> answers = new ArrayList<>();
    private final Map<String, Integer> scores = new HashMap<String, Integer>() {{
        put("Танцы", 0);
        put("Искусство", 0);
        put("История", 0);
        put("Литература", 0);
        put("Живопись", 0);
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_chatbot, container, false);

        chatContainer = view.findViewById(R.id.chatContainer);
        inputContainer = view.findViewById(R.id.inputContainer);
        sendButton = view.findViewById(R.id.sendButton);
        messageInput = view.findViewById(R.id.messageInput);

        addMessage("Привет! Я помогу определить ваши культурные предпочтения в Нижнем Новгороде. Ответьте на несколько вопросов.", false);
        askNextQuestion();

        sendButton.setOnClickListener(v -> {
            String answer = messageInput.getText().toString().trim();
            if (!answer.isEmpty()) {
                addMessage(answer, true);
                messageInput.getText().clear();
                processAnswer(answer);
            }
        });

        return view;
    }

    private void askNextQuestion() {
        if (currentQuestionIndex < questions.size()) {
            addMessage(questions.get(currentQuestionIndex).getText(), false);
            showQuickReplies();
        } else {
            showResult();
        }
    }

    private void showQuickReplies() {
        inputContainer.setVisibility(View.GONE);

        LinearLayout quickReplyContainer = new LinearLayout(requireContext());
        quickReplyContainer.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 16, 0, 16);
        quickReplyContainer.setLayoutParams(layoutParams);

        String[] replies = {"Да", "Нет", "Не знаю"};
        for (String reply : replies) {
            Button button = new Button(requireContext());
            button.setText(reply);
            button.setBackgroundColor(Color.parseColor("#E3F2FD"));
            button.setTextColor(Color.parseColor("#1976D2"));
            button.setPadding(16, 16, 16, 16);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    1f
            );
            buttonParams.setMargins(4, 0, 4, 0);
            button.setLayoutParams(buttonParams);
            button.setOnClickListener(v -> {
                addMessage(reply, true);
                processAnswer(reply);
                chatContainer.removeView(quickReplyContainer);
            });
            quickReplyContainer.addView(button);
        }

        chatContainer.addView(quickReplyContainer);
    }

    private void processAnswer(String answer) {
        Question currentQuestion = questions.get(currentQuestionIndex);
        answers.add(new Answer(currentQuestion.getText(), currentQuestion.getCategory(), answer));

        if (answer.equalsIgnoreCase("Да")) {
            String category = currentQuestion.getCategory();
            scores.put(category, scores.get(category) + 1);
        }

        currentQuestionIndex++;
        askNextQuestion();
    }

    private void showResult() {
        inputContainer.setVisibility(View.GONE);

        int maxScore = -1;
        for (int score : scores.values()) {
            if (score > maxScore) {
                maxScore = score;
            }
        }

        List<String> topCategories = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() == maxScore) {
                topCategories.add(entry.getKey());
            }
        }

        if (maxScore == 0) {
            addMessage("Пока ваш интерес не ясен, но Нижний Новгород полон сюрпризов! Попробуйте экскурсию по городу или квест «Культурный микс».", false);
        } else if (topCategories.size() > 1) {
            addMessage("Вы многогранны! Вот несколько рекомендаций для вас:", false);
            for (String category : topCategories) {
                addMessage(recommendations.get(category), false);
            }
        } else {
            addMessage(recommendations.get(topCategories.get(0)), false);
        }

        saveResultsToFile();
    }

    private void addMessage(String text, boolean isUserMessage) {
        LinearLayout messageLayout = new LinearLayout(requireContext());
        messageLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0, 8, 0, 8);
        messageLayout.setLayoutParams(layoutParams);

        TextView messageBubble = new TextView(requireContext());
        messageBubble.setText(text);
        messageBubble.setTextColor(isUserMessage ? Color.WHITE : Color.BLACK);
        messageBubble.setBackgroundResource(
                isUserMessage ? R.drawable.user_bubble : R.drawable.bot_bubble
        );
        messageBubble.setPadding(24, 16, 24, 16);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.gravity = isUserMessage ? Gravity.END : Gravity.START;
        messageBubble.setLayoutParams(bubbleParams);

        messageLayout.addView(messageBubble);
        chatContainer.addView(messageLayout);

        chatContainer.post(() -> {
            ViewParent parent = chatContainer.getParent();
            if (parent instanceof ScrollView) {
                ((ScrollView) parent).fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    private void saveResultsToFile() {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "quiz_results_" + timestamp + ".txt";
        File file = new File(requireContext().getFilesDir(), fileName);

        StringBuilder content = new StringBuilder();
        content.append("Результаты опроса:\n");
        content.append("Дата: ").append(new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date())).append("\n\n");

        content.append("Ответы:\n");
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            content.append(i + 1).append(". ").append(answer.getQuestion()).append("\n");
            content.append("Категория: ").append(answer.getCategory()).append("\n");
            content.append("Ответ: ").append(answer.getAnswer()).append("\n\n");
        }

        content.append("\nБаллы по категориям:\n");
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            content.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        content.append("\nРекомендация:\n");
        for (int i = 0; i < chatContainer.getChildCount(); i++) {
            View child = chatContainer.getChildAt(i);
            if (child instanceof LinearLayout && ((LinearLayout) child).getChildCount() > 0) {
                View potentialTextView = ((LinearLayout) child).getChildAt(0);
                if (potentialTextView instanceof TextView) {
                    String message = ((TextView) potentialTextView).getText().toString();
                    if (!message.startsWith("Привет") && !message.startsWith("1.")) {
                        content.append(message).append("\n");
                    }
                }
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.toString().getBytes());
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Ошибка при сохранении результатов", Toast.LENGTH_SHORT).show();
        }
    }

    private static class Question {
        private final String text;
        private final String category;

        public Question(String text, String category) {
            this.text = text;
            this.category = category;
        }

        public String getText() {
            return text;
        }

        public String getCategory() {
            return category;
        }
    }

    private static class Answer {
        private final String question;
        private final String category;
        private final String answer;

        public Answer(String question, String category, String answer) {
            this.question = question;
            this.category = category;
            this.answer = answer;
        }

        public String getQuestion() {
            return question;
        }

        public String getCategory() {
            return category;
        }

        public String getAnswer() {
            return answer;
        }
    }
}